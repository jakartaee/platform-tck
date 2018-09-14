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
 * @(#)send_Test.java	1.17 03/05/16
 */
package com.sun.ts.tests.javamail.ee.transport;

import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;

import javax.mail.*;
import javax.mail.internet.*;

public class send_Test extends ServiceEETest implements Serializable {

  // get this from ts.jte
  private String transport_protocol = null;

  private transient MailTestUtil mailTestUtil;

  private String user;

  private String password;

  private transient Status status;

  private int errors = 0; // number of unit test errors

  // get this from ts.jte
  private String mailTo = null;

  private transient Session session;

  public String TO = "ksnijjar@eng";

  public static final String SUBJECT = "Transport class test";

  public static final String TEXT = "Testing Transport class send() API";

  public static final String MAILER = "JavaMail";

  static String msgText = "This is a message body.\nHere's the second line.";

  /* Run test in standalone mode */
  public static void main(String[] args) {
    send_Test theTests = new send_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * 
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; transport_protocol; mailuser1;
   * smtp.port; imap.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      user = TestUtil.getProperty("javamail.username");
      password = TestUtil.getProperty("javamail.password");
      String mailbox = TestUtil.getProperty("javamail.mailbox");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      mailTestUtil = new MailTestUtil();
      session = mailTestUtil.createSession(host, smtpPortStr, user, password);

      // mail recipient
      mailTo = TestUtil.getProperty("mailuser1");
      if (mailTo.length() == 0)
        throw new Fault("Invalid mailuser1 - the mail to property");

      transport_protocol = TestUtil.getProperty("transport_protocol");
      ;
      if (transport_protocol.length() == 0)
        throw new Fault("Invalid transport_protocol");

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSend
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite send_Test class
  public void testSend() throws Fault {
    String msgText = "Testing Transport.send(Message msg).\nPASS.";

    try {

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      // msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("testSend()" + new Date());
      msg.setContent(msgText, "text/plain");

      // BEGIN UNIT TEST 1:
      TestUtil.logTrace("UNIT TEST 1: send(Message)");

      // send the mail message
      Transport.send(msg); // API TEST

      TestUtil.logTrace("UNIT TEST 1: passed\n");
      // END UNIT TEST 1:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSend() Failed!", e);
    }
  }

  //
  /*
   * @testName: testSend2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite send_Test class
  public void testSend2() throws Fault {
    String msgText = "Testing Transport.send(Message, Address[]).\nPASS.";
    try {

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      // msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("testSend2()" + new Date());
      msg.setContent(msgText, "text/plain");

      // BEGIN UNIT TEST 2:

      // send the mail message off via the specified addresses
      Transport.send(msg, addrs); // API TEST

      // END UNIT TEST 2:

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSend2() Failed!", e);
    }
  }

  /*
   * @testName: testSend3
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: construct the message - check the type verify that addtype
   * returns rfc822
   */
  // derived from javamail suite getType class
  public void testSend3() throws Fault {
    String msgText = "Testing message/rfc822.";
    try {

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);
      Address addrs[] = new Address[1];
      addrs[0] = addr;
      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("get rfc822 type");
      msg.setContent("Verify that type is rfc822", "text/plain");
      addrs = msg.getFrom();
      String addtype = addrs[0].getType(); // API TEST
      TestUtil.logTrace("addtype is " + addtype);
      if (addtype != null) {
        if (addtype.equals("rfc822"))
          TestUtil.logTrace("UNIT TEST  passed\n");
        else {
          throw new Fault("UNIT TEST  FAILED\n");
        }
      }

      // BEGIN UNIT TEST 1:

      // send the mail message
      Transport.send(msg); // API TEST

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSend3() Failed!", e);
    }
  }

  /*
   * @testName: testconnect1
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite connect_Test class
  public void testconnect1() throws Fault {
    String host = null;
    String user = null;
    String password = null;
    String msgText = "Testing connect().\nPASS.";
    try {

      // Get a Transport object
      Transport transport = session.getTransport(transport_protocol);
      if (transport == null) {
        throw new Fault("WARNING: Failed to create a transport object!");
      }

      // Create a MimeMessage object
      MimeMessage msg = new MimeMessage(session);

      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("testconnect1" + new Date());
      msg.setContent(msgText, "text/plain");

      // BEGIN UNIT TEST 1:

      // Connect
      transport.connect(); // API TEST

      // TestUtil.logMsg("UNIT TEST 1: passed\n");
      // END UNIT TEST 1:

      // send the mail message via specified 'protocol'
      transport.sendMessage(msg, addrs);

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testConnect1() Failed!", e);
    }

  }// end of testconnect1()

  /*
   * @testName: testSendMessage
   * 
   * @assertion_ids: JavaEE:SPEC:238; JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call this API for given message objects with address list.
   * If this invocation is successful then this testcase passes.
   */
  // derived from javamail suite sendMessage_Test class
  public void testSendMessage() throws Fault {
    String msgText = "Testing sendMessage(Message,Address[]).\nPASS.";
    try {

      // Get a Transport object
      Transport transport = session.getTransport(transport_protocol);
      if (transport == null) {
        throw new Fault("WARNING: Failed to create a transport object!");
      }

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);

      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("testSendMessage()" + new Date());
      msg.setContent(msgText, "text/plain");

      // Connect
      transport.connect();

      // send the mail message off via the specified addresses
      transport.sendMessage(msg, addrs); // API TEST
    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testSendMessage() Failed!", e);
    }
  } // end of testSendMessage()

  /*
   * @testName: test4
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>send()</strong> API. It does this by passing
   * various valid input values and then checking the type of the returned
   * object. <p>
   * 
   * Send this message. <p> api2test: public void send(msg) <p> Send this
   * message to the specified addresses. <p> api2test: public void send(Message,
   * Address[]) <p> Send this message using the specified username and password.
   * <p> api2test: public void send(Message, String, String) <p>
   * 
   * how2test: Call these APIs for given message objects with or without address
   * list. If this invocation is successfull then this testcase passes otherwise
   * it fails. <p>
   * 
   * derived from javamail suite send_Test
   */
  public void test4() throws Fault {

    TO = user;
    TestUtil.logMsg("\nTesting Transport class => send(Message, Address[])\n");

    try {

      // Create a message object
      MimeMessage msg = new MimeMessage(session);

      // Get a Transport object
      Transport transport = session.getTransport(transport_protocol);
      if (transport == null) {
        throw new Fault("WARNING: Failed to create a transport object!");
      }

      // Construct an address array
      InternetAddress addr = new InternetAddress(TO);
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("JavaMail send() API Test");
      msg.setContent(msgText, "text/plain");

      // BEGIN UNIT TEST 1:
      TestUtil.logMsg("UNIT TEST 1: send(Message)");

      // send the mail message
      transport.send(msg); // API TEST

      TestUtil.logMsg("UNIT TEST 1: passed\n");
      // END UNIT TEST 1:

      // BEGIN UNIT TEST 2:
      TestUtil.logMsg("UNIT TEST 2: send(Message, Address[])");

      // send the mail message off via the specified addresses
      transport.send(msg, addrs); // API TEST

      TestUtil.logMsg("UNIT TEST 2: passed\n");
      // END UNIT TEST 2:

      // BEGIN UNIT TEST 3:
      TestUtil.logMsg("UNIT TEST 3: send(Message, String, String)");

      // create a Session with no authenticator
      msg = new MimeMessage(session);
      msg.setFrom(addr);
      msg.setRecipients(Message.RecipientType.TO, addrs);
      msg.setSubject("JavaMail send() API Test");
      msg.setContent(msgText, "text/plain");

      // send the mail message off with the username and password
      transport.send(msg, user, password); // API TEST

      TestUtil.logMsg("UNIT TEST 3: passed\n");
      // END UNIT TEST 3:

      checkStatus();

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test4() Failed!", e);
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
      TestUtil.logTrace("Cleanup ;");
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }
}
