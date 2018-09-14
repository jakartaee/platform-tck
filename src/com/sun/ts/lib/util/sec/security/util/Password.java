/*
 * Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.util;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.Arrays;

/**
 * A utility class for reading passwords
 *
 */
public class Password {
  /** Reads user password from given input stream. */
  public static char[] readPassword(InputStream in) throws IOException {

    char[] consoleEntered = null;
    byte[] consoleBytes = null;

    try {
      // Use the new java.io.Console class
      Console con = null;
      if (in == System.in && ((con = System.console()) != null)) {
        consoleEntered = con.readPassword();
        // readPassword returns "" if you just print ENTER,
        // to be compatible with old Password class, change to null
        if (consoleEntered != null && consoleEntered.length == 0) {
          return null;
        }
        consoleBytes = convertToBytes(consoleEntered);
        in = new ByteArrayInputStream(consoleBytes);
      }

      // Rest of the lines still necessary for KeyStoreLoginModule
      // and when there is no console.

      char[] lineBuffer;
      char[] buf;
      int i;

      buf = lineBuffer = new char[128];

      int room = buf.length;
      int offset = 0;
      int c;

      boolean done = false;
      while (!done) {
        switch (c = in.read()) {
        case -1:
        case '\n':
          done = true;
          break;

        case '\r':
          int c2 = in.read();
          if ((c2 != '\n') && (c2 != -1)) {
            if (!(in instanceof PushbackInputStream)) {
              in = new PushbackInputStream(in);
            }
            ((PushbackInputStream) in).unread(c2);
          } else {
            done = true;
            break;
          }

        default:
          if (--room < 0) {
            buf = new char[offset + 128];
            room = buf.length - offset - 1;
            System.arraycopy(lineBuffer, 0, buf, 0, offset);
            Arrays.fill(lineBuffer, ' ');
            lineBuffer = buf;
          }
          buf[offset++] = (char) c;
          break;
        }
      }

      if (offset == 0) {
        return null;
      }

      char[] ret = new char[offset];
      System.arraycopy(buf, 0, ret, 0, offset);
      Arrays.fill(buf, ' ');

      return ret;
    } finally {
      if (consoleEntered != null) {
        Arrays.fill(consoleEntered, ' ');
      }
      if (consoleBytes != null) {
        Arrays.fill(consoleBytes, (byte) 0);
      }
    }
  }

  /**
   * Change a password read from Console.readPassword() into its original bytes.
   *
   * @param pass
   *          a char[]
   * @return its byte[] format, similar to new String(pass).getBytes()
   */
  private static byte[] convertToBytes(char[] pass) {
    if (enc == null) {
      synchronized (Password.class) {
        enc = com.sun.ts.lib.util.sec.misc.SharedSecrets.getJavaIOAccess()
            .charset().newEncoder().onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
      }
    }
    byte[] ba = new byte[(int) (enc.maxBytesPerChar() * pass.length)];
    ByteBuffer bb = ByteBuffer.wrap(ba);
    synchronized (enc) {
      enc.reset().encode(CharBuffer.wrap(pass), bb, true);
    }
    if (bb.position() < ba.length) {
      ba[bb.position()] = '\n';
    }
    return ba;
  }

  private static volatile CharsetEncoder enc;
}
