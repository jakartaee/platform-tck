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
 * @(#)send_Test.java	1.13 03/05/16
 */
package com.sun.ts.tests.samples.javamail.ee.transport;

import java.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;
import javax.mail.*;
import javax.mail.internet.*;

public class send_Test extends ServiceEETest implements Serializable {

  // get this from ts.jte
  private String transport_protocol = null;

  private Session session;

  // get this from ts.jte
  private String mailTo = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    send_Test theTests = new send_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; mailuser1;
   * transport_protocol; smtp.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {

      String protocol = TestUtil.getProperty("javamail.protocol");
      String host = TestUtil.getProperty("javamail.server");
      String user = TestUtil.getProperty("javamail.username");
      String password = TestUtil.getProperty("javamail.password");
      String mailbox = TestUtil.getProperty("javamail.mailbox");
      String port = TestUtil.getProperty("smtp.port");

      // mail recipient
      mailTo = TestUtil.getProperty("mailuser1");
      if (mailTo.length() == 0)
        throw new Fault("Invalid mailuser1 - the mail to property");

      transport_protocol = TestUtil.getProperty("transport_protocol");
      ;
      if (transport_protocol.length() == 0)
        throw new Fault("Invalid transport_protocol");

      MailTestUtil mailTestUtil = new MailTestUtil();

      session = mailTestUtil.createSession(host, port, user, password);

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSend
   * 
   * @assertion: Transport.send(MESSAGE) must be supported
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
      if (msg == null) {
        throw new Fault("WARNING: Failed to create a message object!");
      }
      // Construct an address array
      InternetAddress addr = new InternetAddress(mailTo);
      InternetAddress addrs[] = new InternetAddress[1];
      addrs[0] = addr;

      msg.setFrom(addr);
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

  /*
   * @testName: testconnect1
   * 
   * @assertion: connect() test transport connect method
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite connect_Test class
  public void testconnect1() throws Fault {
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

      // out.println("UNIT TEST 1: passed\n");
      // END UNIT TEST 1:

      // send the mail message via specified 'protocol'
      transport.sendMessage(msg, addrs);

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testConnect1() Failed!", e);
    }

  }// end of testconnect1()

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      TestUtil.logTrace("Cleanup ;");
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }
}
