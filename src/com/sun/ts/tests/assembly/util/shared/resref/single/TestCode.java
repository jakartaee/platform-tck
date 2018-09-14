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
 * @(#)TestCode.java	1.10 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.resref.single;

import java.io.Serializable;
import java.io.IOException;
import java.util.Properties;
import java.util.Date;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.activation.DataHandler;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import java.net.URL;
import java.net.URLConnection;

import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {

  /** Prefix used for JNDI lookups */
  private static final String prefix = "java:comp/env/";

  /*
   * JNDI lookup names for resource manager connection factories.
   */
  protected static final String dbLookup = prefix + "jdbc/DB1";

  protected static final String mailLookup = prefix + "mail/MailSession";

  protected static final String urlLookup = prefix + "url/URL";

  protected static final String queueLookup = prefix
      + "jms/myQueueConnectionFactory";

  protected static final String topicLookup = prefix
      + "jms/myTopicConnectionFactory";

  /*
   * Hard coded values used to send mail.
   */
  protected static final String mailer = "JavaMailer";

  protected static final String subject = "Test message";

  protected static final String htmlContents = "This is a test message";

  public static boolean testDatasource(TSNamingContext nctx) {
    DataSource ds;
    Connection connection;

    try {
      TestUtil.logTrace("[TestCode] looking up " + dbLookup);
      ds = (DataSource) nctx.lookup(dbLookup);
      TestUtil.logTrace("[TestCode] get a new DB connection...");
      connection = ds.getConnection();
      connection.close();
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e, e);
      return false;
    }

    return true;
  }

  public static boolean testSession(TSNamingContext nctx) {
    String recipient;
    Session session;

    if (null == (recipient = getRecipient())) {
      TestUtil.logErr("RestTest: Aborting testSession() [setup]");
      return false;
    }

    try {
      TestUtil.logTrace("[TestCode] looking up " + mailLookup);
      session = (Session) nctx.lookup(mailLookup);
      TestUtil.logTrace("[TestCode] sendind mail to " + recipient);
      Transport.send(compose(session, recipient));
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);

      return false;
    }

    return true;
  }

  public static boolean testURL(TSNamingContext nctx) {
    Properties props;
    String resRef;
    URL myUrl;
    URLConnection urlConnection;

    try {
      TestUtil.logTrace("[TestCode] looking up " + urlLookup);
      myUrl = (java.net.URL) nctx.lookup(urlLookup);
      TestUtil.logTrace("[TestCode] get a new URL connection...");
      urlConnection = myUrl.openConnection();

      props = TestUtil.getResponseProperties(urlConnection);
      resRef = props.getProperty("resourceref");

      if ((null == resRef) || (!resRef.equals("true"))) {
        TestUtil.logErr("ResRef: Invalid connection!");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

  public static boolean testQueue(TSNamingContext nctx) {
    QueueConnectionFactory queueFact;

    try {
      TestUtil.logTrace("[TestCode] looking up " + queueLookup);
      queueFact = (QueueConnectionFactory) nctx.lookup(queueLookup);
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

  public static boolean testTopic(TSNamingContext nctx) {
    TopicConnectionFactory topicFact;

    try {
      TestUtil.logTrace("[TestCode] looking up " + topicLookup);
      topicFact = (TopicConnectionFactory) nctx.lookup(topicLookup);
    } catch (Exception e) {
      TestUtil.logErr("RestTest: Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return true;
  }

  public static boolean testAll(TSNamingContext nctx) {
    boolean pass = true;

    try {
      pass &= testDatasource(nctx);
      pass &= testSession(nctx);
      pass &= testURL(nctx);
      pass &= testQueue(nctx);
      pass &= testTopic(nctx);
    } catch (Exception e) {
      TestUtil.logErr("ResRef: Caught exception in testAll: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return pass;
  }

  public static boolean testAllButDataSource(TSNamingContext nctx) {
    boolean pass = true;

    try {
      pass &= testSession(nctx);
      pass &= testURL(nctx);
      pass &= testQueue(nctx);
      pass &= testTopic(nctx);
    } catch (Exception e) {
      TestUtil.logErr("ResRef: Caught exception in testAllButDataSource: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

    return pass;
  }

  /*
   * Helper methods.
   */

  /** Get mail recipient address */
  protected static String getRecipient() {
    String recipient;

    try {
      recipient = TestUtil.getProperty("mailuser1");

      if ("" == recipient || null == recipient) {
        TestUtil.logErr("[TestCode] 'mailuser1' property is "
            + ((null == recipient) ? "null" : "empty"));
        return null;
      }

      TestUtil.logTrace("[TestCode] Sending mail to:" + recipient);
    } catch (Exception e) {
      TestUtil.logErr("[TestCode] setupMail() failed: " + e, e);
      return null;
    }

    return recipient;
  }

  /** Send a mail to 'recipient', using 'session' object. */
  protected static Message compose(Session session, String recipient)
      throws Exception {

    Message msg;

    try {
      msg = new MimeMessage(session);
      msg.setFrom();

      msg.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(recipient, false));

      msg.setSubject(subject);
      collect(subject, htmlContents, msg);
      msg.setHeader("X-Mailer", mailer);
      msg.setSentDate(new Date());

    } catch (Exception e) {
      TestUtil.logErr("[TestCode] Caught exception in compose(): " + e);
      TestUtil.printStackTrace(e);
      throw new Exception("compose() failed due to " + e);
    }

    return msg;
  }

  protected static void collect(String subject, String htmlContents,
      Message msg) throws MessagingException, IOException {

    String line;
    StringBuffer sb = new StringBuffer();

    sb.append("<HTML>\n");

    sb.append("<HEAD>\n");
    sb.append("<TITLE>\n");
    sb.append(subject + "\n");
    sb.append("</TITLE>\n");
    sb.append("</HEAD>\n");

    sb.append("<BODY>\n");
    sb.append("<H1>" + subject + "</H1>" + "\n");
    sb.append(htmlContents);
    sb.append("</BODY>\n");

    sb.append("</HTML>\n");

    msg.setDataHandler(
        new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
  }

}
