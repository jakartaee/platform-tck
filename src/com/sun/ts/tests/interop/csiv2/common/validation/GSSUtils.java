/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * @(#)GSSUtils.java	1.10 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common.validation;

import java.io.*;
import java.util.*;

import com.sun.ts.lib.util.*;

/**
 * Utility class to parse GSS-related structures.
 *
 * @see RFC2743, Section 3.1 and 3.2.
 * @author Mark Roth
 */
public class GSSUtils {
  /** See RFC2743, Section 3.1, Bullet 3 */
  private static final int OBJECT_IDENTIFIER = 6;

  /** Until this constant is checked in, define it locally: */
  public static final String GSSUPMechOID = "oid:2.23.130.1.1.1";

  /**
   * Parses the given OID and returns the extracted components.
   *
   * @param oid
   *          The byte[] containing the OID to parse
   * @param offset
   *          The offset of the first octet of the OID
   * @return The long[] containing the components.
   * @exception GSSUtilsException
   *              thrown if components could not be extracted.
   */
  public static long[] extractComponentsFromOID(byte[] oid, int offset)
      throws GSSUtilsException {
    long[] result;
    ByteArrayInputStream in = new ByteArrayInputStream(oid, offset,
        oid.length - offset);

    try {
      // Verify OBJECT_IDENTIFIER tag:
      int tag = in.read() & 0xFF;
      if (tag != OBJECT_IDENTIFIER) {
        throw new GSSUtilsException(
            "Did not find OBJECT_IDENTIFIER " + "(0x06) in octet 0 of OID");
      }

      // Read in length, and advance offset:
      int length = (int) readVariableOctet(in);

      // Read OID components:
      result = new long[length + 1];

      // First and second component are encoded as 40 * a + b
      long value = readVariableOctet(in);
      result[0] = value / 40;
      result[1] = value - result[0] * 40;
      int count = 2;

      for (int i = 1; i < length;) {
        value = readVariableOctet(in);
        result[count++] = value;
        do {
          i++;
        } while ((value >>= 7) > 0);
      }

      // If the array was too large, create a more compact one:
      if (result.length > count) {
        long[] newresult = new long[count];
        System.arraycopy(result, 0, newresult, 0, count);
        result = newresult;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      TestUtil.printStackTrace(e);
      throw new GSSUtilsException("ArrayIndexOutOfBounds: " + e);
    }

    return result;
  }

  /**
   * Returns a String version of the given components array, formatted like
   * "oid:2.23.130.1.1.1";
   */
  public static String formatComponents(long[] components) {
    String result = "oid:";

    for (int i = 0; i < components.length; i++) {
      result += components[i];
      if (i < (components.length - 1))
        result += ".";
    }

    return result;
  }

  /**
   * Encodes the given OID String of the format similar to "oid:2.23.130.1.1.1"
   * into an OID, as per RFC2743, section 3.1, bullet 2
   */
  private static byte[] convertOIDStringToOID(String oidString)
      throws GSSUtilsException {
    byte[] result;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offset = 0;

    if (!oidString.startsWith("oid:")) {
      throw new GSSUtilsException(
          "Given OIDString does not start with \"oid:\"");
    }
    oidString = oidString.substring(4);

    try {
      StringTokenizer st = new StringTokenizer(oidString, ".");
      int firstComponent = Integer.parseInt(st.nextToken());
      int secondComponent = Integer.parseInt(st.nextToken());
      out.write((byte) (firstComponent * 40 + secondComponent));

      while (st.hasMoreTokens()) {
        int comp = Integer.parseInt(st.nextToken());
        writeVariableOctet(out, comp);
      }

      byte[] octets = out.toByteArray();

      // Encode the length:
      ByteArrayOutputStream lengthOut = new ByteArrayOutputStream();
      writeVariableOctet(lengthOut, octets.length);
      byte[] lengthOutArray = lengthOut.toByteArray();

      // Prepare result:
      result = new byte[octets.length + lengthOutArray.length + 1];
      result[0] = (byte) OBJECT_IDENTIFIER;
      System.arraycopy(lengthOutArray, 0, result, 1, lengthOutArray.length);
      System.arraycopy(octets, 0, result, 1 + lengthOutArray.length,
          octets.length);
    } catch (NumberFormatException e) {
      TestUtil.printStackTrace(e);
      throw new GSSUtilsException("Given OIDString is invalid: " + e);
    } catch (NoSuchElementException e) {
      TestUtil.printStackTrace(e);
      throw new GSSUtilsException("Given OIDString is invalid: " + e);
    }

    return result;
  }

  /**
   * Encodes an exported name as per RFC2743, section 3.2
   */
  public static byte[] encodeGSSUPExportedName(byte[] name)
      throws GSSUtilsException {
    byte[] result;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    // Write Token identifier:
    out.write(0x04);
    out.write(0x01);

    // Write mech ID:
    byte[] gssupOID = convertOIDStringToOID(GSSUPMechOID);
    out.write(gssupOID.length >> 8);
    out.write(gssupOID.length & 0xFF);
    out.write(gssupOID, 0, gssupOID.length);

    // Write name:
    out.write(name.length >> 24);
    out.write((name.length >> 16) & 0xFF);
    out.write((name.length >> 8) & 0xFF);
    out.write(name.length & 0xFF);
    out.write(name, 0, name.length);

    return out.toByteArray();
  }

  /**
   * Extracts the OID from the given exported name. Uses standards as outlined
   * in RFC2743, section 3.1 and section 3.2.
   */
  public static byte[] extractOIDFromExportedName(byte[] exportedName)
      throws GSSUtilsException {
    byte[] result;

    try {
      int tokenID0 = exportedName[0] & 0xFF;
      int tokenID1 = exportedName[1] & 0xFF;

      if ((tokenID0 != 0x04) || (tokenID1 != 0x01)) {
        throw new GSSUtilsException(
            "Given exported name does not start with 04 01");
      }

      int oidlen = ((exportedName[2] & 0xFF) << 8) + (exportedName[3] & 0xFF);
      result = new byte[oidlen];
      System.arraycopy(exportedName, 4, result, 0, oidlen);
    } catch (ArrayIndexOutOfBoundsException e) {
      TestUtil.printStackTrace(e);
      throw new GSSUtilsException("Given exported name is invalid: " + e);
    }

    return result;
  }

  /**
   * Extracts the name from the given exported name. Uses standards as outlined
   * in RFC2743, section 3.1 and section 3.2.
   */
  public static byte[] extractNameFromExportedName(byte[] exportedName)
      throws GSSUtilsException {
    byte[] result;

    try {
      int tokenID0 = exportedName[0] & 0xFF;
      int tokenID1 = exportedName[1] & 0xFF;

      if ((tokenID0 != 0x04) || (tokenID1 != 0x01)) {
        throw new GSSUtilsException(
            "Given exported name does not start with 04 01");
      }

      int oidlen = ((exportedName[2] & 0xFF) << 8) + (exportedName[3] & 0xFF);
      int namelen = ((exportedName[4 + oidlen] & 0xFF) << 24)
          + ((exportedName[4 + oidlen + 1] & 0xFF) << 16)
          + ((exportedName[4 + oidlen + 2] & 0xFF) << 8)
          + (exportedName[4 + oidlen + 3] & 0xFF);
      result = new byte[namelen];
      System.arraycopy(exportedName, 8 + oidlen, result, 0, namelen);
    } catch (ArrayIndexOutOfBoundsException e) {
      TestUtil.printStackTrace(e);
      throw new GSSUtilsException("Given exported name is invalid: " + e);
    }

    return result;
  }

  /**
   * Reads a variable octet, and returns the value, according to the algorithm
   * specified in RFC2743, section 3.1, bullet 2.
   */
  private static long readVariableOctet(ByteArrayInputStream in) {
    long result = 0;
    boolean highbit;

    do {
      int b = in.read() & 0xFF;
      highbit = (b & 0x80) == 0x80;
      result += b & 0x7F;
      if (highbit)
        result <<= 7;
    } while (highbit);

    return result;
  }

  /**
   * Writes a variable octet, and returns the value, according to the algorithm
   * specified in RFC2743, section 3.1, bullet 2.
   */
  private static void writeVariableOctet(OutputStream out, long value) {
    // Write values in reverse, first:
    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    do {
      out2.write((byte) (value & 0x7F));
      value >>= 7;
    } while (value > 0);
    byte[] result = out2.toByteArray();

    for (int i = result.length - 1; i >= 0; i--) {
      try {
        int b = (byte) (result[i] & 0xFF);
        if (i != 0)
          b |= 0x80;
        out.write(b);
      } catch (IOException e) {
        // ignore - this should not happen for ByteArrayOutputStream
      }
    }
  }

  public static String binHex(byte[] bytes) {
    StringBuffer result = new StringBuffer("");
    char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
        'B', 'C', 'D', 'E', 'F' };
    for (int i = 0; i < bytes.length; i++) {
      int b = (int) bytes[i];
      result.append(digits[(b & 0xF0) >> 4]);
      result.append(digits[b & 0x0F]);
    }
    return result.toString();
  }

  public static byte[] parseBinHex(String data) {
    data = data.toUpperCase();
    byte[] result = new byte[data.length() / 2];
    for (int i = 0; i < data.length(); i += 2) {
      char d1 = data.charAt(i);
      char d2 = data.charAt(i + 1);
      int v1 = d1 - ((d1 >= 'A') ? ('A' - 10) : '0');
      int v2 = d2 - ((d2 >= 'A') ? ('A' - 10) : '0');
      result[i / 2] = (byte) (v1 * 16 + v2);
    }
    return result;
  }

  /**
   * Thrown if an error occurred while performing an operation.
   */
  public static class GSSUtilsException extends Exception {
    public GSSUtilsException() {
    }

    public GSSUtilsException(String message) {
      super(message);
    }
  }

  /**
   * Tests GSSUtils
   */
  public static void main(String[] args) {
    byte[] testdata = parseBinHex("0606678102010101");
    byte[] testname = parseBinHex(
        "0401000806066781020101010000000764656661756C74");

    try {
      long[] components = GSSUtils.extractComponentsFromOID(testdata, 0);
      TestUtil.logMsg(GSSUtils.formatComponents(components));

      byte[] encoded = GSSUtils.convertOIDStringToOID(GSSUPMechOID);
      TestUtil.logMsg(binHex(encoded));

      TestUtil.logMsg(binHex(encodeGSSUPExportedName("default".getBytes())));

      TestUtil.logMsg(binHex(extractOIDFromExportedName(testname)));
      TestUtil.logMsg(new String(extractNameFromExportedName(testname)));
    } catch (GSSUtilsException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Error parsing: " + e);
    }

  }

}
