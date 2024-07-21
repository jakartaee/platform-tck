/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJB;

public class Client extends EETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private URL url = null;

  private URLConnection urlConn = null;

  private String SERVLET = "/cditestsejbweb_web/ServletTest";

  @EJB(name = "ejb/CDITestsEjbWebClntBean")
  static EjbClientIF ejbclient;

  private static final long serialVersionUID = 1L;

  long timeout;

  String user;

  String password;

  String mode;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    boolean pass = true;
    try {
      // get props
      timeout = Integer.parseInt(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      hostname = p.getProperty("webServerHost");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      if (hostname == null) {
        throw new Exception("'webServerHost' in ts.jte must not be null");
      }
      try {
        portnum = Integer.parseInt(p.getProperty("webServerPort"));
      } catch (Exception e) {
        throw new Exception("'webServerPort' in ts.jte must be a number");
      }
      TestUtil.logMsg("AppClient DEBUG: ejbclient=" + ejbclient);
      if (ejbclient == null) {
        throw new Exception("setup failed: ejbclient injection failure");
      } else {
        ejbclient.init(p);
      }
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    ejbclient.init(p);
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: sendRecvQueueTestUsingCDIFromServlet
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Queue is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Queue destination.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/QueueConnectionFactory") JMSContext context1;
   * 
   * ConnectionFactory --> jms/QueueConnectionFactory Queue --> jms/MY_QUEUE
   * JMSContext --> context1 [Variables injected: --> qcfactory, context1,
   * queue]
   */
  public void sendRecvQueueTestUsingCDIFromServlet() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------");
      TestUtil.logMsg("sendRecvQueueTestUsingCDIFromServlet");
      TestUtil.logMsg("------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "sendRecvQueueTestUsingCDIFromServlet");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Servlet");
      } else {
        TestUtil.logMsg("CDI injection test passed from Servlet");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Servlet");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvQueueTestUsingCDIFromServlet failed");
    }
  }

  /*
   * @testName: sendRecvTopicTestUsingCDIFromServlet
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Topic is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Queue destination.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/TopicConnectionFactory") JMSContext context2;
   * 
   * ConnectionFactory --> jms/TopicConnectionFactory Topic --> jms/MY_TOPIC
   * JMSContext --> context1 [Variables injected: --> tcfactory, context2,
   * topic]
   */
  public void sendRecvTopicTestUsingCDIFromServlet() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------");
      TestUtil.logMsg("sendRecvTopicTestUsingCDIFromServlet");
      TestUtil.logMsg("------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "sendRecvTopicTestUsingCDIFromServlet");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Servlet");
      } else {
        TestUtil.logMsg("CDI injection test passed from Servlet");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Servlet");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvTopicTestUsingCDIFromServlet failed");
    }
  }

  /*
   * @testName: sendRecvUsingCDIDefaultFactoryFromServlet
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to/from Queue/Topic using
   * CDI with the system default connection factory.
   *
   * @Inject JMSContext context4;
   *
   * ConnectionFactory --> jms/ConnectionFactory Topic --> jms/MY_TOPIC Queue
   * --> jms/MY_QUEUE JMSContext --> context4 [Variables injected: --> cfactory,
   * context4, topic, queue]
   */
  public void sendRecvUsingCDIDefaultFactoryFromServlet() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("-----------------------------------------");
      TestUtil.logMsg("sendRecvUsingCDIDefaultFactoryFromServlet");
      TestUtil.logMsg("-----------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "sendRecvUsingCDIDefaultFactoryFromServlet");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Servlet");
      } else {
        TestUtil.logMsg("CDI injection test passed from Servlet");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Servlet");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvUsingCDIDefaultFactoryFromServlet failed");
    }
  }

  /*
   * @testName: verifySessionModeOnCDIJMSContextFromServlet
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Check the Session Mode on JMSContext for correctness.
   * Checks the session mode of the 3 context injections.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/QueueConnectionFactory") JMSContext context1;
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/TopicConnectionFactory") JMSContext context2;
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/ConnectionFactory")
   * 
   * @JMSPasswordCredential(userName="j2ee", password="j2ee")
   * 
   * @JMSSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE) JMSContext context3;
   */
  public void verifySessionModeOnCDIJMSContextFromServlet() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("-------------------------------------------");
      TestUtil.logMsg("verifySessionModeOnCDIJMSContextFromServlet");
      TestUtil.logMsg("-------------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "verifySessionModeOnCDIJMSContextFromServlet");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Servlet");
      } else {
        TestUtil.logMsg("CDI injection test passed from Servlet");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Servlet");
      pass = false;
    }

    if (!pass) {
      throw new Exception("verifySessionModeOnCDIJMSContextFromServlet failed");
    }
  }

  /*
   * @testName: testRestrictionsOnCDIJMSContextFromServlet
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:281; JMS:JAVADOC:1366; JMS:JAVADOC:1367;
   * JMS:JAVADOC:1368; JMS:JAVADOC:1353; JMS:JAVADOC:1354; JMS:JAVADOC:917;
   * JMS:JAVADOC:997; JMS:JAVADOC:1043;
   * 
   * @test_Strategy: Test restrictions on a CDI injected JMSContext. The
   * following API calls MUST throw a JMSRuntimeException or
   * IllegalStateRuntimeException if called on an injected CDI JMSContext.
   * JMSContext.setClientID() JMSContext.setExceptionListener()
   * JMSContext.stop() JMSContext.acknowledge() JMSContext.commit()
   * JMSContext.rollback() JMSContext.recover() JMSContext.setAutoStart()
   * JMSContext.start() JMSContext.close()
   */
  public void testRestrictionsOnCDIJMSContextFromServlet() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------------");
      TestUtil.logMsg("testRestrictionsOnCDIJMSContextFromServlet");
      TestUtil.logMsg("------------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "testRestrictionsOnCDIJMSContextFromServlet");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Servlet");
      } else {
        TestUtil.logMsg("CDI injection test passed from Servlet");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Servlet");
      pass = false;
    }

    if (!pass) {
      throw new Exception("testRestrictionsOnCDIJMSContextFromServlet failed");
    }
  }

  /*
   * @testName: sendRecvQueueTestUsingCDIFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Queue is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Queue destination.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/QueueConnectionFactory") JMSContext context1;
   * 
   * ConnectionFactory --> jms/QueueConnectionFactory Queue --> jms/MY_QUEUE
   * JMSContext --> context1 [Variables injected: --> qcfactory, context1,
   * queue]
   */
  public void sendRecvQueueTestUsingCDIFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("--------------------------------");
      TestUtil.logMsg("sendRecvQueueTestUsingCDIFromEjb");
      TestUtil.logMsg("--------------------------------");
      boolean passEjb = ejbclient.echo("sendRecvQueueTestUsingCDIFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvQueueTestUsingCDIFromEjb failed");
    }
  }

  /*
   * @testName: sendRecvTopicTestUsingCDIFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Topic is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Queue destination.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/TopicConnectionFactory") JMSContext context2;
   * 
   * ConnectionFactory --> jms/TopicConnectionFactory Topic --> jms/MY_TOPIC
   * JMSContext --> context2 [Variables injected: --> tcfactory, context2,
   * topic]
   */
  public void sendRecvTopicTestUsingCDIFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("--------------------------------");
      TestUtil.logMsg("sendRecvTopicTestUsingCDIFromEjb");
      TestUtil.logMsg("--------------------------------");
      boolean passEjb = ejbclient.echo("sendRecvTopicTestUsingCDIFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvTopicTestUsingCDIFromEjb failed");
    }
  }

  /*
   * @testName: sendRecvUsingCDIDefaultFactoryFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to/from Queue/Topic using
   * CDI with the system default connection factory.
   *
   * @Inject JMSContext context4;
   *
   * ConnectionFactory --> jms/ConnectionFactory Topic --> jms/MY_TOPIC Queue
   * --> jms/MY_QUEUE JMSContext --> context4 [Variables injected: --> cfactory,
   * context4, topic, queue]
   */
  public void sendRecvUsingCDIDefaultFactoryFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("-------------------------------------");
      TestUtil.logMsg("sendRecvUsingCDIDefaultFactoryFromEjb");
      TestUtil.logMsg("-------------------------------------");
      boolean passEjb = ejbclient.echo("sendRecvUsingCDIDefaultFactoryFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvUsingCDIDefaultFactoryFromServlet failed");
    }
  }

  /*
   * @testName: verifySessionModeOnCDIJMSContextFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Check the Session Mode on JMSContext for correctness.
   * Checks the session mode of the 3 context injections.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/QueueConnectionFactory") JMSContext context1;
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/TopicConnectionFactory") JMSContext context2;
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/ConnectionFactory")
   * 
   * @JMSPasswordCredential(userName="j2ee", password="j2ee")
   * 
   * @JMSSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE) JMSContext context3;
   */
  public void verifySessionModeOnCDIJMSContextFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("---------------------------------------");
      TestUtil.logMsg("verifySessionModeOnCDIJMSContextFromEjb");
      TestUtil.logMsg("---------------------------------------");
      boolean passEjb = ejbclient
          .echo("verifySessionModeOnCDIJMSContextFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("verifySessionModeOnCDIJMSContextFromEjb failed");
    }
  }

  /*
   * @testName: testRestrictionsOnCDIJMSContextFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:281; JMS:JAVADOC:1366; JMS:JAVADOC:1367;
   * JMS:JAVADOC:1368; JMS:JAVADOC:1353; JMS:JAVADOC:1354; JMS:JAVADOC:917;
   * JMS:JAVADOC:997; JMS:JAVADOC:1043;
   * 
   * @test_Strategy: Test restrictions on a CDI injected JMSContext. The
   * following API calls MUST throw a JMSRuntimeException or
   * IllegalStateRuntimeException if called on an injected CDI JMSContext.
   * JMSContext.setClientID() JMSContext.setExceptionListener()
   * JMSContext.stop() JMSContext.acknowledge() JMSContext.commit()
   * JMSContext.rollback() JMSContext.recover() JMSContext.setAutoStart()
   * JMSContext.start() JMSContext.close()
   */
  public void testRestrictionsOnCDIJMSContextFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("--------------------------------------");
      TestUtil.logMsg("testRestrictionsOnCDIJMSContextFromEjb");
      TestUtil.logMsg("--------------------------------------");
      boolean passEjb = ejbclient
          .echo("testRestrictionsOnCDIJMSContextFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("testRestrictionsOnCDIJMSContextFromEjb failed");
    }
  }

  /*
   * @testName: testActiveJTAUsingCDIAcross2MethodsFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:280;
   * 
   * @test_Strategy: Test active JTA transaction on CDI injected JMSContext
   * across 2 methods.
   */
  public void testActiveJTAUsingCDIAcross2MethodsFromEjb() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------------");
      TestUtil.logMsg("testActiveJTAUsingCDIAcross2MethodsFromEjb");
      TestUtil.logMsg("------------------------------------------");
      boolean passEjb = ejbclient
          .echo("testActiveJTAUsingCDICallMethod1FromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
      passEjb = ejbclient.echo("testActiveJTAUsingCDICallMethod2FromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Exception("testActiveJTAUsingCDIAcross2MethodsFromEjb failed");
    }
  }

  /*
   * @testName: sendRecvQueueTestUsingCDIFromManagedBean
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Queue is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Queue destination.
   * Verifies CDI injection from a CDI Managed Bean.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/QueueConnectionFactory") JMSContext context1;
   * 
   * ConnectionFactory --> jms/QueueConnectionFactory Queue --> jms/MY_QUEUE
   * JMSContext --> context1 [Variables injected: --> qcfactory, context1,
   * queue]
   */
  public void sendRecvQueueTestUsingCDIFromManagedBean() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("----------------------------------------");
      TestUtil.logMsg("sendRecvQueueTestUsingCDIFromManagedBean");
      TestUtil.logMsg("----------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "sendRecvQueueTestUsingCDIFromManagedBean");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from ManagedBean");
      } else {
        TestUtil.logMsg("CDI injection test passed from ManagedBean");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from ManagedBean");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvQueueTestUsingCDIFromManagedBean failed");
    }
  }

  /*
   * @testName: sendRecvTopicTestUsingCDIFromManagedBean
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:277; JMS:SPEC:278; JMS:SPEC:279;
   * 
   * @test_Strategy: Send and receive single message to verify that Topic is
   * working. Uses CDI and resource injection to inject the JMSContext. Uses
   * Resource injection to inject the ConnectionFactory, and Topic destination.
   * Verifies CDI injection from a CDI Managed Bean.
   *
   * @Inject
   * 
   * @JMSConnectionFactory("jms/TopicConnectionFactory") JMSContext context1;
   * 
   * ConnectionFactory --> jms/TopicConnectionFactory Topic --> jms/MY_TOPIC
   * JMSContext --> context2 [Variables injected: --> tcfactory, context2,
   * topic]
   */
  public void sendRecvTopicTestUsingCDIFromManagedBean() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("----------------------------------------");
      TestUtil.logMsg("sendRecvTopicTestUsingCDIFromManagedBean");
      TestUtil.logMsg("----------------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "sendRecvTopicTestUsingCDIFromManagedBean");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from ManagedBean");
      } else {
        TestUtil.logMsg("CDI injection test passed from ManagedBean");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from ManagedBean");
      pass = false;
    }

    if (!pass) {
      throw new Exception("sendRecvTopicTestUsingCDIFromManagedBean failed");
    }
  }
}
