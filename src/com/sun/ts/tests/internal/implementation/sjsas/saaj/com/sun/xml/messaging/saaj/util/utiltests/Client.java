/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.util.utiltests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Base64;
import java.util.Properties;

import org.apache.commons.io.input.TeeInputStream;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
// Import implementation specific classes to test
import com.sun.xml.messaging.saaj.util.*;

import jakarta.xml.soap.MimeHeaders;

public class Client extends EETest {
  private Properties props = null;

  private String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/saaj/com/sun/xml/messaging/saaj/util/utiltests";

  private String testDir = null;

  private String pkgName = "com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.util.utiltests";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    String tsHome = p.getProperty("ts_home");
    testDir = tsHome + "/" + srcDir;
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: Base64Test
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void Base64Test() throws Fault {
    TestUtil.logTrace("Base64Test");
    boolean pass = true;
    String string1 = "This is a string to encode in Base64.";
    try {
      TestUtil.logMsg("Call Base64() constructor");
      Base64 base64 = new Base64();
      TestUtil.logMsg("Call Base64.encode(byte[]) method");
      byte[] data1 = string1.getBytes();
      byte[] base64Data = Base64.encode(data1);
      TestUtil.logMsg("Call Base64.decode(byte[]) method");
      byte[] data2 = base64.decode(base64Data);
      TestUtil.logMsg("Call Base64.base64Decode(String) method");
      String string2 = Base64.base64Decode(new String(base64Data));
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Base64Test failed", e);
    }

    if (!pass)
      throw new Fault("Base64Test failed");
  }

  /*
   * @testName: ByteInputStreamTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ByteInputStreamTest() throws Fault {
    TestUtil.logTrace("ByteInputStreamTest");
    boolean pass = true;
    String string1 = "This is a string to create a ByteInputStream with.";
    try {
      TestUtil.logMsg("Call ByteInputStream(byte[], int) constructor");
      byte[] data1 = string1.getBytes();
      ByteInputStream bis = new ByteInputStream(data1, data1.length);
      try {
        bis.close();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call ByteInputStream(byte[], int, int) constructor");
      bis = new ByteInputStream(data1, 0, data1.length);
      TestUtil.logMsg("Call ByteInputStream.getBytes() method");
      byte[] data2 = bis.getBytes();
      if (data1.length != data2.length) {
        TestUtil.logErr("data1.length != data2.length");
        TestUtil.logErr(
            "data1.length=" + data1.length + ", data2.length=" + data2.length);
        pass = false;
      } else {
        for (int i = 0; i < data1.length; ++i) {
          if (data1[i] != data2[i]) {
            TestUtil.logErr("data1 contents != data2 contents");
            TestUtil.logMsg("data1[" + i + "]=" + data1[i] + ", data2[" + i
                + "]=" + data2[i]);
            pass = false;
            break;
          }
        }
      }
      TestUtil.logMsg("Call ByteInputStream.getCount() method");
      int count = bis.getCount();
      TestUtil.logMsg("Call ByteInputStream.close() method");
      try {
        bis.close();
      } catch (Exception e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ByteInputStreamTest failed", e);
    }

    if (!pass)
      throw new Fault("ByteInputStreamTest failed");
  }

  /*
   * @testName: ByteOutputStreamTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ByteOutputStreamTest() throws Fault {
    TestUtil.logTrace("ByteOutputStreamTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call ByteOutputStream() constructor");
      ByteOutputStream bos = new ByteOutputStream();
      try {
        bos.close();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call ByteOutputStream(int) constructor");
      bos = new ByteOutputStream(1024);
      TestUtil.logMsg("Call ByteOutputStream.getBytes() method");
      byte[] data = bos.getBytes();
      TestUtil.logMsg("Call ByteOutputStream.getCount() method");
      int count = bos.getCount();
      TestUtil.logMsg("Call ByteOutputStream.close() method");
      try {
        bos.close();
      } catch (Exception e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ByteOutputStreamTest failed", e);
    }

    if (!pass)
      throw new Fault("ByteOutputStreamTest failed");
  }

  /*
   * @testName: CharReaderTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void CharReaderTest() throws Fault {
    TestUtil.logTrace("CharReaderTest");
    boolean pass = true;
    String string1 = "This is a string to create a CharReader with.";
    try {
      TestUtil.logMsg("Call CharReader(char[], int) constructor");
      char[] data1 = string1.toCharArray();
      CharReader cr = new CharReader(data1, data1.length);
      cr.close();
      TestUtil.logMsg("Call CharReader(char[], int, int) constructor");
      cr = new CharReader(data1, 0, data1.length);
      TestUtil.logMsg("Call CharReader.getChars() method");
      char[] data2 = cr.getChars();
      TestUtil.logMsg("Call CharReader.getCount() method");
      int count = cr.getCount();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CharReaderTest failed", e);
    }

    if (!pass)
      throw new Fault("CharReaderTest failed");
  }

  /*
   * @testName: CharWriterTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void CharWriterTest() throws Fault {
    TestUtil.logTrace("CharWriterTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call CharWriter() constructor");
      CharWriter cw = new CharWriter();
      cw.close();
      TestUtil.logMsg("Call CharWriter(int) constructor");
      cw = new CharWriter(1024);
      TestUtil.logMsg("Call CharWriter.getChars() method");
      char[] data = cw.getChars();
      TestUtil.logMsg("Call CharWriter.getCount() method");
      int count = cw.getCount();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CharWriterTest failed", e);
    }

    if (!pass)
      throw new Fault("CharWriterTest failed");
  }

  /*
   * @testName: ParseUtilTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ParseUtilTest() throws Fault {
    TestUtil.logTrace("ParseUtilTest");
    boolean pass = true;
    String string1 = "This is a string that will be used to call ParseUtil.decode().";
    try {
      TestUtil.logMsg("Call ParseUtil() constructor");
      ParseUtil pu = new ParseUtil();
      TestUtil.logMsg("Call ParseUtil.decode(String) method");
      String string2 = pu.decode(string1);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ParseUtilTest failed", e);
    }

    if (!pass)
      throw new Fault("ParseUtilTest failed");
  }

  /*
   * @testName: MimeHeadersUtilTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MimeHeadersUtilTest() throws Fault {
    TestUtil.logTrace("MimeHeadersUtilTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call MimeHeadersUtil() constructor");
      MimeHeadersUtil mhu = new MimeHeadersUtil();
      TestUtil.logMsg("Call MimeHeadersUtil.copy(MimeHeaders) method");
      MimeHeaders mhs = new MimeHeaders();
      mhs.addHeader("MimeHeader1", "Value1");
      mhs.addHeader("MimeHeader2", "Value2");
      mhs.addHeader("MimeHeader3", "Value3");
      MimeHeaders mhs2 = mhu.copy(mhs);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MimeHeadersUtilTest failed", e);
    }

    if (!pass)
      throw new Fault("MimeHeadersUtilTest failed");
  }

  /*
   * @testName: TeeInputStreamTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void TeeInputStreamTest() throws Fault {
    TestUtil.logTrace("TeeInputStreamTest");
    boolean pass = true;
    String string1 = "This is a string to use for ByteArrayInputStream.";
    byte[] buf = new byte[1024];
    ByteArrayInputStream bais = new ByteArrayInputStream(string1.getBytes());
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    try {
      TestUtil
          .logMsg("Call TeeInputStream(InputStream, OutputStream) constructor");
      TeeInputStream tis = new TeeInputStream(bais, baos);
      TestUtil.logMsg("Call TeeInputStream.read() method");
      try {
        int count = tis.read();
        tis.reset();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.read(byte[], int, int) method");
      try {
        int count = tis.read(buf, 0, 1024);
        tis.reset();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.read(byte[]) method");
      try {
        int count = tis.read(buf);
        tis.reset();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.skip(long) method");
      try {
        tis.skip(512);
        tis.reset();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.available() method");
      try {
        int available = tis.available();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.reset() method");
      try {
        tis.reset();
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call TeeInputStream.markSupported() method");
      boolean mark = tis.markSupported();
      TestUtil.logMsg("Call TeeInputStream.mark(int) method");
      if (mark)
        tis.mark(512);
      TestUtil.logMsg("Call TeeInputStream.close() method");
      try {
        tis.close();
      } catch (Exception e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TeeInputStreamTest failed", e);
    }

    if (!pass)
      throw new Fault("TeeInputStreamTest failed");
  }

  /*
   * @testName: XMLDeclarationParserTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void XMLDeclarationParserTest() throws Fault {
    TestUtil.logTrace("XMLDeclarationParserTest");
    boolean pass = true;
    String[] docs = { "", "<?xml version='1.0' encoding= 'UTF-8'?>",
        "<?xml version='1.0' encoding= 'UTF-16'?>",
        "<?xml version='1.0' encoding ='US-ASCII' ?>", };
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    for (int i = 0; i < docs.length; i++) {
      try {
        StringReader sr = new StringReader(docs[i]);
        TestUtil
            .logMsg("Call XMLDeclarationParser(PushbackReader) constructor");
        XMLDeclarationParser p = new XMLDeclarationParser(
            new PushbackReader(sr));
        TestUtil.logMsg("Call XMLDeclarationParser.parse() method");
        p.parse();
        TestUtil.logMsg("Call XMLDeclarationParser.getEncoding() method");
        String encoding = p.getEncoding();
        PrintWriter pw = new PrintWriter(baos);
        TestUtil
            .logMsg("Call XMLDeclarationParser.writeTo(PrintWriter) method");
        p.writeTo(pw);
        pw.flush();
        pw.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        throw new Fault("XMLDeclarationParserTest failed", e);
      }
    }

    if (!pass)
      throw new Fault("XMLDeclarationParserTest failed");
  }
}
