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
 * @(#)multipart_Test.java	1.16 03/05/16
 */
package com.sun.ts.tests.javamail.ee.multipart;

import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import com.sun.javatest.Status;

import java.io.Serializable;

import javax.mail.*;
import javax.mail.internet.*;

public class multipart_Test extends ServiceEETest implements Serializable {
  static String msgText1 = "This is a message body.\nHere's line two.";

  static String msgText2 = "This is the text in the message attachment.";

  // get this from ts.jte
  private String transport_protocol = null;

  // get this from ts.jte
  private String mailTo = null;

  private String user;

  private String password;

  private transient Session session;

  private String host;

  private transient MailTestUtil mailTestUtil;

  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    multipart_Test theTests = new multipart_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
  /*
   * @class.setup_props: javamail.protocol; javamail.server; javamail.username;
   * javamail.password ; javamail.mailbox; javamail.root.path; mailuser1;
   * mailHost; mailFrom; transport_protocol; smtp.port; imap.port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {
      // mail recipient
      mailTo = props.getProperty("mailuser1");
      if (mailTo.length() == 0)
        throw new Fault("Invalid mailuser1 - the mail to property");

      transport_protocol = props.getProperty("transport_protocol");
      if (transport_protocol.length() == 0)
        throw new Fault("Invalid transport_protocol");

      user = TestUtil.getProperty("javamail.username");
      password = TestUtil.getProperty("javamail.password");
      host = TestUtil.getProperty("javamail.server");

      String smtpPortStr = TestUtil.getProperty("smtp.port");
      int smtpPort = Integer.parseInt(smtpPortStr);
      TestUtil.logTrace("SMTP Port = " + smtpPort);

      String imapPortStr = TestUtil.getProperty("imap.port");
      int imapPort = Integer.parseInt(imapPortStr);
      TestUtil.logTrace("IMAP Port = " + imapPort);

      mailTestUtil = new MailTestUtil();
      session = mailTestUtil.createSession(host, smtpPortStr, user, password);

    } catch (Exception e) {
      logErr("Exception : " + e.getMessage());
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testAddBodyPart1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call api with part argument, then verify by calling
   * getCount.
   */
  // derived from javamail suite multipart_Test class
  public void testAddBodyPart1() throws Fault {
    String msgText = "Testing addBodyPart(BodyPart).\nPASS.";

    try {

      // create a message
      MimeMessage msg = new MimeMessage(session);

      InternetAddress[] address = { new InternetAddress(mailTo) };

      msg.setRecipients(Message.RecipientType.TO, address);
      msg.setSubject("TestAddBodyPart1()" + new Date());

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(msgText1);

      // create and fill the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();

      // Use setText(text, charset), to show it off !
      mbp2.setText(msgText2, "us-ascii");

      // create the Multipart and its parts to it
      Multipart mp = new MimeMultipart();

      // BEGIN UNIT TEST:

      mp.addBodyPart(mbp1); // API TEST
      mp.addBodyPart(mbp2); // API TEST
      mp.addBodyPart(mbp1); // API TEST

      if (mp.getCount() == 3)
        TestUtil.logTrace("Multipart1: passed.\n");
      else {
        throw new Fault("Multipart1: count incorrect- failed\n");
      }
      TestUtil.logTrace("Count returned is : " + mp.getCount());
      // END UNIT TEST:

      // add the Multipart to the message
      msg.setContent(mp);

      // send the message
      Transport.send(msg);

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testAddBodyPart1 Failed!", e);
    }

  }

  /*
   * @testName: testAddBodyPart2
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call api with part argument and position, then verify by
   * calling getCount.
   */
  public void testAddBodyPart2() throws Fault {

    try {

      // create a message
      MimeMessage msg = new MimeMessage(session);

      InternetAddress[] address = { new InternetAddress(mailTo) };

      msg.setRecipients(Message.RecipientType.TO, address);
      msg.setSubject("testAddBodyPart2()" + new Date());

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(msgText1);

      // create and fill the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();

      // Use setText(text, charset), to show it off !
      mbp2.setText(msgText2, "us-ascii");

      // create the Multipart and its parts to it
      Multipart mp = new MimeMultipart();

      // BEGIN UNIT TEST:
      mp.addBodyPart(mbp1, 0); // API TEST
      mp.addBodyPart(mbp2, 1); // API TEST
      mp.addBodyPart(mbp1, 2); // API TEST
      mp.addBodyPart(mbp2, 3); // API TEST

      if (mp.getCount() == 4)
        TestUtil.logTrace("Multipart2: passed.\n");
      else {
        throw new Fault("Multipart2: count incorrect- failed\n");
      }
      TestUtil.logTrace("Count returned is : " + mp.getCount());

      // END UNIT TEST:

      // add the Multipart to the message
      msg.setContent(mp);

      // send the message
      Transport.send(msg);

    } catch (Exception e) {
      logErr("Unexpected Exception " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("Call to testAddBodyPart2 Failed!", e);
    }

  }// end of testAddBodyPart2
   //

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ;");
    } catch (Exception e) {
      logErr("An error occurred in cleanup!", e);
    }
  }
}
