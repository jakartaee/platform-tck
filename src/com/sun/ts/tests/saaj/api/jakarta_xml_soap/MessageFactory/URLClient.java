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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MessageFactory;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

public class URLClient extends EETest {
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String SOAPMESSAGEFACTORY_TESTSERVLET = "/MessageFactory_web/MessageFactoryTestServlet";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: createMessageTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:114; SAAJ:JAVADOC:115;
   *
   * @test_Strategy: Call MessageFactory.createMessage().
   *
   * Description: Creates a new SOAPMessage object with default SOAPPart,
   * SOAPEnvelope, SOAPBody, and SOAPHeader objects.
   *
   */
  public void createMessageTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createMessageTest1: create SOAPMessage object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createMessageTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("createMessageTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createMessageTest1 failed");
  }

  /*
   * @testName: createMessageTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:116; SAAJ:JAVADOC:117; SAAJ:JAVADOC:118;
   *
   * @test_Strategy: Call MessageFactory.createMessage(MimeHeaders,
   * java.io.InputStream)
   *
   * Description: Internalizes the contents of the given InputStream object into
   * a new SOAPMessage object and returns the SOAPMessage object.
   *
   */
  public void createMessageTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createMessageTest2: create SOAPMessage object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createMessageTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("createMessageTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createMessageTest2 failed");
  }

  /*
   * @testName: newInstanceTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:110; SAAJ:JAVADOC:111;
   *
   * @test_Strategy: Call MessageFactory.newInstance()
   *
   * Description: Creates a new MessageFactory object that is an instance of the
   * default implementation.
   *
   */
  public void newInstanceTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest1: create MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance()");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest1");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest1 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest1 failed");
  }

  /*
   * @testName: newInstanceTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:112;
   *
   * @test_Strategy: Call MessageFactory.newInstance(
   * SOAPConstants.SOAP_1_1_PROTOCOL)
   *
   * Description: Creates a new MessageFactory object that is a SOAP 1.1
   * implementation.
   *
   */
  public void newInstanceTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest2: create SOAP1.1 MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(SOAPConstants."
          + "SOAP11_PROTOCOL)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest2");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest2 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest3 failed");
  }

  /*
   * @testName: newInstanceTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:112;
   *
   * @test_Strategy: Call MessageFactory.newInstance(
   * SOAPConstants.SOAP_1_2_PROTOCOL)
   *
   * Description: Creates a new MessageFactory object that is a SOAP1.2
   * implementation.
   *
   */
  public void newInstanceTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest3: create SOAP1.2 MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(SOAPConstants."
          + "SOAP12_PROTOCOL)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest3");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest3 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest3 failed");
  }

  /*
   * @testName: newInstanceTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:112;
   *
   * @test_Strategy: Call MessageFactory.newInstance()
   * SOAPConstants.DYNAMIC_SOAP_PROTOCOL)
   *
   * Description: Creates a new MessageFactory object that is a dynamic message
   * factory implementation.
   *
   */
  public void newInstanceTest4() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest4: create Dynamic MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(SOAPConstants."
          + "DYNAMIC_PROTOCOL)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest4");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest4 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest4 failed");
  }

  /*
   * @testName: newInstanceTest4b
   *
   * @assertion_ids: SAAJ:JAVADOC:113;
   *
   * @test_Strategy: Call MessageFactory.newInstance()
   * SOAPConstants.DYNAMIC_SOAP_PROTOCOL)
   *
   * Description: Creates a new MessageFactory object that is a dynamic message
   * factory implementation.
   *
   */
  public void newInstanceTest4b() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("newInstanceTest4b: create Dynamic MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(SOAPConstants."
          + "DYNAMIC_PROTOCOL)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest4b");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest4b failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest4b failed");
  }

  /*
   * @testName: newInstanceTest5
   *
   * @assertion_ids: SAAJ:JAVADOC:113;
   *
   * @test_Strategy: Call MessageFactory.newInstance(BOGUS)
   *
   * Description: Call MessageFactory.newInstance(String) with a BOGUS value and
   * expect a SOAPException.
   *
   */
  public void newInstanceTest5() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest5: create BOGUS MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(BOGUS)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest5");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest5 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest5 failed");
  }

  /*
   * @testName: newInstanceTest6
   *
   * @assertion_ids: SAAJ:JAVADOC:112
   *
   * @test_Strategy: Call MessageFactory.newInstance()
   * SOAPConstants.DEFAULT_SOAP_PROTOCOL)
   *
   * Description: Creates a new MessageFactory object that is a dynamic message
   * factory implementation.
   *
   */
  public void newInstanceTest6() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest6: create Dynamic MessageFactory object");
      TestUtil.logMsg("Call MessageFactory.newInstance(SOAPConstants."
          + "DEFAULT_PROTOCOL)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPMESSAGEFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest6");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest6 failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest6 failed");
  }
}
