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
 * @(#)mimemessage_Test.java	1.17 03/05/16
 */
package com.sun.ts.tests.javamail.ee.mimemessage;

import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;

import javax.mail.*;
import javax.mail.internet.*;

public class mimemessage_Test extends ServiceEETest implements Serializable {

  private String mailTo = null;

  private int errors = 0; // number of unit test errors

  private transient MailTestUtil mailTestUtil;

  private transient Session session;

  private Folder folder;

  private Message[] msgs;

  private Store store;

  private transient Status status;

  private String user;

  private String[] addrlist = { "ksnijjar@eng", "ksnijjar@eng.sun.com",
      "French@physicists", "cannot@waste", "us/@mhs-mci.ebay", "it@is",
      "tower@ihug.co.nz", "root@mxrelay.lanminds.com", "javaworld",
      "xx.zzz12@fea.net", "javamail-api-eng@icdev",
      "ksnijjar@java-test.Eng.Sun.COM" };

  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    mimemessage_Test theTests = new mimemessage_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; smtp.port;
   * imap.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      user = TestUtil.getProperty("javamail.username");
      String password = TestUtil.getProperty("javamail.password");
      String mailbox = TestUtil.getProperty("javamail.mailbox");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      MailTestUtil mailTestUtil = new MailTestUtil();

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

      // Get all the messages
      msgs = folder.getMessages();

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetContent1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call setContent with required arguments for multipart then
   * call getContent() to verify.
   */
  // derived from javamail suite setContent_Test class
  public void testSetContent1() throws Fault {

    try {

      // BEGIN UNIT TEST:

      // Create a MimeMessage object
      MimeMessage mob = new MimeMessage(session);

      if (mob == null) {
        throw new Fault("Warning: Failed to create a MimeMessage object!");
      }
      // Create a Multipart object
      Multipart mmp = new MimeMultipart();

      if (mmp == null) {
        throw new Fault("Warning: Failed to create a Multipart object!");
      }
      // BEGIN UNIT TEST 1:
      TestUtil.logTrace("UNIT TEST 1:  setContent(Multipart)");

      mob.setContent(mmp); // API TEST
      Object content = mob.getContent();

      if ((content != null) && (content instanceof Multipart)) {
        TestUtil.logTrace("This is a Multipart");
        Multipart mp1 = (Multipart) content;
        int count = mp1.getCount();
        TestUtil.logTrace("UNIT TEST 1:  passed\n");
      } else {
        TestUtil.logTrace("UNIT TEST 1:  FAILED\n");
        throw new Fault("testSetContent1 failed\n");
      }
      // END UNIT TEST 1:

      // END UNIT TEST:
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSetContent1() Failed!", e);
    }

  } // end of testSetContent1()

  /*
   * @testName: testSetContent2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Call setContent with required arguments for text/plain then
   * call getContentType() to verify.
   */
  // derived from javamail suite sendMessage_Test class
  public void testSetContent2() throws Fault {
    String msgText = "testing 1,2,3 ...";
    try {

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      if (msg == null) {
        throw new Fault("WARNING: Failed to create a message object!");
      }
      // Construct an address array
      InternetAddress addr = new InternetAddress(user);

      if (addr == null) {
        throw new Fault("WARNING: Failed to create a InternetAddress object!");
      }
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("testSetContent2()" + new Date());
      msg.setContent(msgText, "text/plain");
      Object content = msg.getContentType();
      TestUtil.logTrace("content is " + content.toString());
      if ((content != null) && (content instanceof String)) {
        if (((String) content).equals("text/plain"))
          TestUtil.logTrace("UNIT TEST 1:  passed\n");
        else {
          TestUtil.logTrace("UNIT TEST 1:  FAILED\n");
          throw new Fault("testSetContent2() failed\n");
        }
      }
      // END UNIT TEST 1:
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSetContent2() Failed!", e);
    }

  } // end of testSetContent2()

  /*
   * @testName: getSession_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy:
   * 
   * This class tests the <strong>getSession()</strong> API. It does this by
   * invoking the test api and then checking that the returned object is the
   * same object used to create the message.<p>
   * 
   * Get the session of this message. <p> api2test: public String getSession()
   * <p>
   * 
   * how2test: Call this API on given message object, verify that it returns the
   * Session object used to create this message. If this operation is
   * successfull then this testcase passes, otherwise it fails. <p>
   * 
   * Returns the Session object used when the message was created. Returns null
   * if no Session is available. <p>
   */
  // derived from javamail suite getSession_Test class

  public void getSession_Test() throws Fault {

    TestUtil.logMsg("\nTesting class Message: getSession()\n");

    try {

      // BEGIN UNIT TEST 1:
      Message msg = new MimeMessage(session);
      TestUtil.logMsg("UNIT TEST 1:  getSession()");

      Session sess = msg.getSession(); // API TEST

      if (sess == session) {
        TestUtil.logMsg("UNIT TEST 1:  passed\n");
      } else {
        TestUtil.logMsg("got Session: " + sess);
        TestUtil.logMsg("UNIT TEST 1:  Failed\n");
        errors++;
      }
      // END UNIT TEST 1:

      // BEGIN UNIT TEST 2:
      msg = new MimeMessage((Session) null);
      TestUtil.logMsg("UNIT TEST 2:  getSession() null");

      sess = msg.getSession(); // API TEST

      if (sess == null) {
        TestUtil.logMsg("UNIT TEST 2:  passed\n");
      } else {
        TestUtil.logMsg("got Session: " + sess);
        TestUtil.logMsg("UNIT TEST 2:  Failed\n");
        errors++;
      }
      // END UNIT TEST 2:
      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getSession_Test Failed!", e);
    }

  }

  /**
  
   */
  /*
   * @testName: createMimeMessage_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: This test tests the <strong>createMessage()</strong> API.
   * It does by passing various valid input values and then checking the type of
   * the returned object. <p>
   * 
   * Create a subclassed MimeMessage object and in reply(boolean) return
   * instance of this new subclassed object. <p> api2test: protected void
   * createMimeMessage() <p>
   * 
   * how2test: Call API with various arguments, then call getRecipients() api,
   * verify that user specified recipient address types have been added. If so
   * then this testcase passes, otherwise it fails. <p>
   */
  // derived from javamail suite createMimeMessage_Test
  public void createMimeMessage_Test() throws Fault {

    try {
      // Create a custom MimeMessage objectcreateMimeMessage_Test
      // with custom message ID algorithm
      MimeMessage msg = new MyMimeMessage(session);

      // BEGIN UNIT TEST:
      TestUtil.logMsg("UNIT TEST 1: createMimeMessage");

      // create Message ID
      Message replyMsg = msg.reply(false);// API TEST for
      // createMimeMessage called in reply(boolean).
      // See the reply() in the inner class below

      if (replyMsg instanceof MyReplyMimeMessage) {
        TestUtil.logMsg("UNIT TEST 1: passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 1: FAILED\n");
        errors++;
      }

      // END UNIT TEST:
      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createMimeMessage_Test Failed!", e);
    }

  }

  /*
   * @testName: reply_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>reply()</strong> APIs. <p>
   * 
   * Create a reply MimeMessage object and check that it has the appropriate
   * headers. <p> api2test: public Message reply() <p>
   * 
   * how2test: Call API with various arguments, then verify that the reply
   * message has the required recipients and subject. If so then this testcase
   * passes, otherwise it fails. <p>
   * 
   * derived from javamail suite reply_Test
   */
  public void reply_Test() throws Fault {

    try {

      InternetAddress from = new InternetAddress("joe@example.com");
      InternetAddress to = new InternetAddress("bob@example.com");
      String subj = "test";

      MimeMessage msg = new MimeMessage(session);
      msg.setFrom(from);
      msg.setRecipient(Message.RecipientType.TO, to);
      msg.setSubject(subj);
      msg.setText("test");

      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1: reply(false)");

      Message replyMsg = msg.reply(false); // API TEST

      Address[] addrs = replyMsg.getRecipients(Message.RecipientType.TO);
      if (addrs != null && addrs.length == 1 && addrs[0].equals(from)
          && replyMsg.getSubject().equals("Re: " + subj)
          && msg.isSet(Flags.Flag.ANSWERED)) {
        TestUtil.logMsg("UNIT TEST 1: passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 1: FAILED\n");
        errors++;
      }

      // END UNIT TEST 1:

      // BEGIN UNIT TEST 2:
      TestUtil.logMsg("UNIT TEST 2: reply(true)");

      replyMsg = msg.reply(true); // API TEST

      addrs = replyMsg.getRecipients(Message.RecipientType.TO);
      if (addrs != null && addrs.length == 2
          && ((addrs[0].equals(from) && addrs[1].equals(to))
              || (addrs[0].equals(to) && addrs[1].equals(from)))) {
        TestUtil.logMsg("UNIT TEST 2: passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 2: FAILED\n");
        errors++;
      }

      // END UNIT TEST 2:

      // BEGIN UNIT TEST 3:
      TestUtil.logMsg("UNIT TEST 3: reply(false, false)");

      msg = new MimeMessage(session);
      msg.setFrom(from);
      msg.setRecipient(Message.RecipientType.TO, to);
      msg.setSubject(subj);
      msg.setText("test");
      replyMsg = msg.reply(false, false); // API TEST

      addrs = replyMsg.getRecipients(Message.RecipientType.TO);
      if (addrs != null && addrs.length == 1 && addrs[0].equals(from)
          && replyMsg.getSubject().equals("Re: " + subj)
          && !msg.isSet(Flags.Flag.ANSWERED)) {
        TestUtil.logMsg("UNIT TEST 3: passed\n");
      } else {
        TestUtil.logMsg("UNIT TEST 3: FAILED\n");
        errors++;
      }

      // END UNIT TEST 3:
      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("reply_Test Failed!", e);
    }

  }

  /*
   * @testName: setFrom_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>setFrom(String)</strong> API. It does this by
   * passing various valid input values and then checking the type of the
   * returned object. <p>
   * 
   * Set the "From" attribute in this Message. <p> api2test: public void
   * setFrom(String) <p>
   * 
   * how2test: Call this API with various addresses, then call call getFrom()
   * api, if the setFrom values and getFrom values are the same, then this
   * testcase passes, otherwise it fails. <p>
   * 
   * derived from javamail suite setContent_Test
   */
  public void setFrom_Test() throws Fault {

    TestUtil.logMsg("\nTesting class MimeMessage: setFrom(String)\n");

    try {

      // Create Message object
      MimeMessage msg = new MimeMessage(session);

      int i;
      // BEGIN UNIT TEST:
      for (i = 0; i < addrlist.length; i++) {
        // Create the Address object
        InternetAddress addr = new InternetAddress(addrlist[i]);

        // set whom the message is from
        TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  setFrom(String)");

        msg.setFrom(addrlist[i]); // API TEST

        Address[] nowfrom = msg.getFrom();
        String newFrom = nowfrom[0].toString();

        if (newFrom != null) {
          if (addrlist[i].equals(newFrom)) {
            TestUtil.logMsg("setFrom(" + addrlist[i] + ")");
            TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  passed\n");
          } else {
            TestUtil.logMsg("getFrom() :=> " + newFrom);
            TestUtil.logMsg("setFrom(" + addrlist[i] + ")");
            TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  FAILED\n");
            errors++;
          }
        } else {
          TestUtil.logMsg(
              "WARNING: Message " + (i + 1) + " has null 'From' header");
          TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  FAILED\n");
          errors++;
        }
      }
      // END UNIT TEST:

      // BEGIN UNIT TEST:
      // now try with more than one From address
      TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  setFrom(String)");

      String addr1 = "joe@example.com";
      String addr2 = "bob@example.com";
      msg.setFrom(addr1 + "," + addr2); // API TEST

      Address[] afrom = msg.getFrom();
      if (afrom != null && afrom.length == 2
          && ((afrom[0].toString().equals(addr1)
              && afrom[1].toString().equals(addr2))
              || (afrom[0].toString().equals(addr2)
                  && afrom[1].toString().equals(addr1)))) {
        TestUtil.logMsg("setFrom(" + addr1 + "," + addr2 + ")");
        TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  passed\n");
      } else {
        // TestUtil.logMsg("getFrom() :=> " + afrom);
        TestUtil.logMsg("setFrom(" + addr1 + "," + addr2 + ")");
        TestUtil.logMsg("UNIT TEST " + (i + 1) + ":  FAILED\n");
        errors++;
      }
      // END UNIT TEST:
      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("reply_Test Failed!", e);
    }

  }

  public void checkStatus() {
    if (errors == 0)
      status = Status.passed("OKAY");
    else
      status = Status.failed("");
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ;");
      if (store != null) {
        store = null;
      }
      if (session != null) {
        session = null;
      }
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }
}

class MyMimeMessage extends MimeMessage {

  public MyMimeMessage(Session session) {
    super(session);
  }

  public MimeMessage createMimeMessage(Session session)
      throws MessagingException {
    return new MyReplyMimeMessage(session);
  }
}

class MyReplyMimeMessage extends MimeMessage {

  public MyReplyMimeMessage(Session session) {
    super(session);
  }
}
