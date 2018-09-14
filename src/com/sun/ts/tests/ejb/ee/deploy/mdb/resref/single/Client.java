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
 * $Id$
 */

package com.sun.ts.tests.ejb.ee.deploy.mdb.resref.single;

import java.util.Properties;
import javax.jms.Queue;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private String recipient = null;

  private Queue mdbQ = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     mailuser1; webServerHost; webServerPort; mailFrom;
   *                     mailHost; jms_timeout; user; password;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);
      recipient = TestUtil.getProperty("mailuser1");

      mdbQ = (Queue) context.lookup("java:comp/env/jms/MDBTest");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testSession
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a message-driven Bean declaring a resource reference
   *                 for a javax.mail.Session.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the mail session. - We can use this factory to send
   *                 a mail.
   */
  public void testSession() throws Fault {

    boolean pass = false;
    String testCase = "testSession";
    int testNum = 2;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("recipient", recipient);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Fault("[Client] " + testCase + " failed ");
      }
    } catch (Exception e) {
      logErr("[Client] " + testCase + " failed: " + e);
      throw new Fault("[Client] " + testCase + " failed: ", e);
    }
  }

  /**
   * @testName: testURL
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a message-driven Bean declaring a resource reference
   *                 for a java.net.URL.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the URL. - We can use this URL factory to open a
   *                 connection to a HTML page bundled in the application.
   */
  public void testURL() throws Fault {

    boolean pass = false;
    String testCase = "testURL";
    int testNum = 3;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Fault("[Client] " + testCase + " failed ");
      }
    } catch (Exception e) {
      logErr("[Client] " + testCase + " failed: " + e);
      throw new Fault("[Client] " + testCase + " failed: ", e);
    }
  }

  /**
   * @testName: testQueue
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a message-driven Bean declaring a resource reference
   *                 for a javax.jms.QueueConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Queue Connection Factory.
   */
  public void testQueue() throws Fault {

    boolean pass = false;
    String testCase = "testQueue";
    int testNum = 4;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);

      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Fault("[Client] " + testCase + " failed ");
      }
    } catch (Exception e) {
      logErr("[Client] " + testCase + " failed: " + e);
      throw new Fault("[Client] " + testCase + " failed: ", e);
    }
  }

  /**
   * @testName: testTopic
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a message-driven Bean declaring a resource reference
   *                 for a javax.jms.TopicConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Topic Connection Factory.
   */
  public void testTopic() throws Fault {

    boolean pass = false;
    String testCase = "testTopic";
    int testNum = 5;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Fault("[Client] " + testCase + " failed ");
      }
    } catch (Exception e) {
      logErr("[Client] " + testCase + " failed: " + e);
      throw new Fault("[Client] " + testCase + " failed: ", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a message-driven bean declaring a resource reference
   *                 for all the standard resource manager connection factory
   *                 types.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup all the declared resource factories.
   */
  public void testAll() throws Fault {
    boolean pass = false;
    String testCase = "testAll";
    int testNum = 6;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("recipient", recipient);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Fault("[Client] " + testCase + " failed ");
      }
    } catch (Exception e) {
      logErr("[Client] " + testCase + " failed: " + e);
      throw new Fault("[Client] " + testCase + " failed: ", e);
    }
  }
}
