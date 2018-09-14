/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javamail.ee.internetMimeMultipart;

import java.util.*;
import java.io.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.Serializable;

import com.sun.ts.tests.javamail.ee.common.MailTestUtil;

public class internetMimeMultipart_Test extends ServiceEETest {

  private int errors = 0; // number of unit test errors

  private MailTestUtil mailTestUtil;

  private Folder folder;

  private Message[] msgs;

  private Store store;

  private Status status;

  private int msgcount = -1;

  static Session session;

  private String workDir;

  private String ioFile;

  public static void main(String[] args) {
    internetMimeMultipart_Test theTests = new internetMimeMultipart_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; work.dir; iofile;
   * smtp.port; imap.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      String user = TestUtil.getProperty("javamail.username");
      String password = TestUtil.getProperty("javamail.password");
      String mailbox = TestUtil.getProperty("javamail.mailbox");
      workDir = TestUtil.getProperty("work.dir");
      ioFile = TestUtil.getProperty("iofile");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      mailTestUtil = new MailTestUtil();
      store = mailTestUtil.connect2host(protocol, host, imapPort, user,
          password);
      session = mailTestUtil.getSession();

      // Get a Folder object
      Folder root = mailTestUtil.getRootFolder(store);
      folder = root.getFolder(mailbox);

      if (folder == null) {
        throw new Fault("Invalid folder object!");
      }
      folder.open(Folder.READ_ONLY);

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite getMessageContent_Test

  public void test1() throws Fault {

    try {
      TestUtil.logMsg(
          "\nTesting class MimeMultipart: " + "initializeProperties method\n");
      clearAll();

      // BEGIN UNIT TEST:
      TestUtil.logMsg("UNIT TEST 1:  initializeProperties()");
      MyMimeMultipart mp = new MyMimeMultipart();
      if (mp.checkDefault()) {
        TestUtil.logMsg("UNIT TEST 1:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 1:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      TestUtil.logMsg("UNIT TEST 2:  initializeProperties()");
      System.setProperty("mail.mime.multipart.ignoreexistingboundaryparameter",
          "true");
      System.setProperty("mail.mime.multipart.ignoremissingboundaryparameter",
          "false");
      System.setProperty("mail.mime.multipart.ignoremissingendboundary",
          "false");
      System.setProperty("mail.mime.multipart.allowempty", "true");
      mp = new MyMimeMultipart();
      if (mp.checkNonDefault()) {
        TestUtil.logMsg("UNIT TEST 2:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 2:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      checkStatus();

      if (errors > 0) {
        throw new Fault("test1 Failed: No of unit test failed = " + errors);
      }

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 Failed");

    }
  }// end of test1

  /*
   * @testName: properties_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite properties_Test

  public void properties_Test() throws Fault {

    TestUtil.logMsg("\nTesting class MimeMultipart: effect of "
        + "system property settings\n");
    Properties properties = mailTestUtil.getProperties();
    try {

      MimeMessage m;
      MimeMultipart mp;

      // BEGIN UNIT TEST:
      // test simple correct case
      TestUtil.logMsg("UNIT TEST 1:  test for parse(InputStream) with "
          + "no System properties set");
      clearAll();
      m = createMessage("x", "x", true);
      mp = (MimeMultipart) m.getContent();
      if (mp.getCount() == 2) {
        TestUtil.logMsg("UNIT TEST 1:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 1:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.ignoreexistingboundaryparameter
      TestUtil.logMsg("UNIT TEST 2:  test for parse(InputStream) with "
          + "mail.mime.multipart.ignoreexistingboundaryparameter=true");
      clearAll();
      System.setProperty("mail.mime.multipart.ignoreexistingboundaryparameter",
          "true");
      m = createMessage("x", "-", true);
      mp = (MimeMultipart) m.getContent();
      if (mp.getCount() == 2) {
        TestUtil.logMsg("UNIT TEST 2:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 2:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.ignoremissingboundaryparameter default
      TestUtil.logMsg("UNIT TEST 3:  test for parse(InputStream) with "
          + "no boundary parameter");
      clearAll();
      m = createMessage(null, "x", true);
      mp = (MimeMultipart) m.getContent();
      if (mp.getCount() == 2) {
        TestUtil.logMsg("UNIT TEST 3:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 3:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.ignoremissingboundaryparameter=false
      TestUtil.logMsg("UNIT TEST 4:  test for parse(InputStream) with "
          + "no boundary parameter and "
          + "mail.mime.multipart.ignoremissingboundaryparameter=false");
      clearAll();
      System.setProperty("mail.mime.multipart.ignoremissingboundaryparameter",
          "false");
      try {
        m = createMessage(null, "x", true);
        mp = (MimeMultipart) m.getContent();
        mp.getCount(); // throw exception
        TestUtil.logMsg("UNIT TEST 4:  FAILED\n");
        errors++;
      } catch (MessagingException mex) {
        TestUtil.logMsg("UNIT TEST 4:  passed\n");
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.ignoreexistingmissingendboundary default
      TestUtil.logMsg("UNIT TEST 5:  test for parse(InputStream) with "
          + "no end boundary");
      clearAll();
      m = createMessage("x", "x", false);
      mp = (MimeMultipart) m.getContent();
      if (mp.getCount() == 2) {
        TestUtil.logMsg("UNIT TEST 5:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 5:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.ignoreexistingmissingendboundary=false
      TestUtil.logMsg(
          "UNIT TEST 6:  test for parse(InputStream) with " + "no end boundary"
              + "mail.mime.multipart.ignoremissingendboundary=false");
      clearAll();
      try {
        System.setProperty("mail.mime.multipart.ignoremissingendboundary",
            "false");
        m = createMessage("x", "x", false);
        mp = (MimeMultipart) m.getContent();
        mp.getCount(); // throw exception
        TestUtil.logMsg("UNIT TEST 6:  FAILED\n");
        errors++;
      } catch (MessagingException mex) {
        TestUtil.logMsg("UNIT TEST 6:  passed\n");
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.allowempty=true
      TestUtil.logMsg("UNIT TEST 7:  test for parse(InputStream) with "
          + "mail.mime.multipart.allowempty=true");
      clearAll();
      System.setProperty("mail.mime.multipart.allowempty", "true");
      m = createEmptyMessage();
      mp = (MimeMultipart) m.getContent();
      if (mp.getCount() == 0) {
        TestUtil.logMsg("UNIT TEST 7:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 7:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.allowempty default
      TestUtil.logMsg("UNIT TEST 8:  test for parse(InputStream) with "
          + "mail.mime.multipart.allowempty default");
      clearAll();
      try {
        m = createEmptyMessage();
        mp = (MimeMultipart) m.getContent();
        mp.getCount(); // throw exception
        TestUtil.logMsg("UNIT TEST 8:  FAILED\n");
        errors++;
      } catch (MessagingException mex) {
        TestUtil.logMsg("UNIT TEST 8:  passed\n");
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.allowempty=true with output
      TestUtil.logMsg("UNIT TEST 9:  test for writeTo(OutputStream) with "
          + "mail.mime.multipart.allowempty=true");
      clearAll();
      System.setProperty("mail.mime.multipart.allowempty", "true");
      m = new MimeMessage(session);
      mp = new MimeMultipart();
      m.setContent(mp);
      m.writeTo(new NullOutputStream());
      if (mp.getCount() == 0) {
        TestUtil.logMsg("UNIT TEST 9:  passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 9:  FAILED\n");
        errors++;
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // test mail.mime.multipart.allowempty default with output
      TestUtil.logMsg("UNIT TEST 10:  test for writeTo(OutputStream) with "
          + "mail.mime.multipart.allowempty default");
      clearAll();
      try {
        m = new MimeMessage(session);
        mp = new MimeMultipart();
        m.setContent(mp);
        m.writeTo(new NullOutputStream()); // throw exception
        TestUtil.logMsg("UNIT TEST 10:  FAILED\n");
        errors++;
      } catch (IOException ioex) {
        TestUtil.logMsg("UNIT TEST 10:  passed\n");
      }
      // END UNIT TEST:

      checkStatus();

      if (errors > 0) {
        throw new Fault("test1 Failed: No of unit test failed = " + errors);
      }
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 Failed");
    }

  }

  /*
   * @testName: mimeMultipartTest
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite MimeMultipart test

  public void mimeMultipartTest() throws Fault {

    TestUtil.logMsg(
        "\nTesting class MimeMultipart: MimeMultipart(void | DataSource)\n");

    try {
      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1:  MimeMultipart()");
      MimeMultipart mp = new MimeMultipart(); // API TEST

      if ((mp != null) && (mp instanceof MimeMultipart))
        TestUtil.logMsg("UNIT TEST 1: passed");
      else {
        TestUtil.logMsg("UNIT TEST 1: FAILED");
        errors++;
      }
      // END UNIT TEST 1:
      // BEGIN UNIT TEST 2:
      // Create a file DataSource object
      FileDataSource ds = new FileDataSource(workDir + ioFile);

      if (ds == null) {
        throw new Fault("WARNING: null FileDataSource object");
      }
      TestUtil.logMsg("UNIT TEST 2:  MimeMultipart(DataSource)");

      MimeMultipart mmp = new MimeMultipart((DataSource) ds); // API TEST

      if ((mmp != null) && (mmp instanceof MimeMultipart))
        TestUtil.logMsg("UNIT TEST 2: passed");
      else {
        TestUtil.logMsg("UNIT TEST 2: FAILED");
        errors++;
      }
      // END UNIT TEST 2:
      // BEGIN UNIT TEST 3:
      TestUtil.logMsg("UNIT TEST 3:  MimeMultipart(String, BodyParts...)");
      MimeBodyPart mbp1 = new MimeBodyPart();
      String cont1 = "part1";
      mbp1.setText(cont1);
      MimeBodyPart mbp2 = new MimeBodyPart();
      String cont2 = "part2";
      mbp2.setText(cont2);
      MimeMultipart mpp = new MimeMultipart("mixed", mbp1, mbp2);// API
      // TEST

      if (mpp.getCount() == 2 && mpp.getBodyPart(0).getContent().equals(cont1)
          && mpp.getBodyPart(1).getContent().equals(cont2))
        TestUtil.logMsg("UNIT TEST 3: passed");
      else {
        TestUtil.logMsg("UNIT TEST 3: FAILED");
        errors++;
      }
      // END UNIT TEST 3:
      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 Failed");
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ;");
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }

  public void checkStatus() {
    if (errors == 0)
      status = Status.passed("OKAY");
    else
      status = Status.failed("");
  }

  private static void clearAll() {
    System.clearProperty("mail.mime.multipart.ignoreexistingboundaryparameter");
    System.clearProperty("mail.mime.multipart.ignoremissingboundaryparameter");
    System.clearProperty("mail.mime.multipart.ignoremissingendboundary");
    System.clearProperty("mail.mime.multipart.allowempty");
  }

  /**
   * Create a test message. If param is not null, it specifies the boundary
   * parameter. The actual boundary is specified by "actual". If "end" is true,
   * include the end boundary.
   */
  private static MimeMessage createMessage(String param, String actual,
      boolean end) throws MessagingException {
    String content = "Mime-Version: 1.0\n" + "Subject: Example\n"
        + "Content-Type: multipart/mixed; "
        + (param != null ? "boundary=\"" + param + "\"" : "") + "\n" + "\n"
        + "preamble\n" + "--" + actual + "\n" + "\n" + "first part\n" + "\n"
        + "--" + actual + "\n" + "\n" + "second part\n" + "\n"
        + (end ? "--" + actual + "--\n" : "");

    return new MimeMessage(session, new StringBufferInputStream(content));
  }

  /**
   * Create a test message with no parts.
   */
  private static MimeMessage createEmptyMessage() throws MessagingException {
    String content = "Mime-Version: 1.0\n" + "Subject: Example\n"
        + "Content-Type: multipart/mixed; boundary=\"x\"\n\n";

    return new MimeMessage(session, new StringBufferInputStream(content));
  }

}

/**
 * An OutputStream that throws away all data written to it.
 */
class MyMimeMultipart extends MimeMultipart {

  public MyMimeMultipart() {
    super();
  }

  // check that fields have default values
  public boolean checkDefault() {
    return ignoreMissingEndBoundary && ignoreMissingBoundaryParameter
        && !ignoreExistingBoundaryParameter && !allowEmpty;
  }

  // check that all fields have been changed
  public boolean checkNonDefault() {
    return !ignoreMissingEndBoundary && !ignoreMissingBoundaryParameter
        && ignoreExistingBoundaryParameter && allowEmpty;
  }
}

/**
 * An OutputStream that throws away all data written to it.
 */
class NullOutputStream extends OutputStream {

  public void write(int b) throws IOException {
  }

  public void write(byte[] b) throws IOException {
  }

  public void write(byte[] b, int off, int len) throws IOException {
  }
}
