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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFactory;

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

  private static final String SOAPFACTORY_TESTSERVLET = "/SOAPFactory_web/SOAPFactoryTestServlet";

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
   * @testName: newInstanceTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:66; SAAJ:JAVADOC:67;
   *
   * @test_Strategy: Call SOAPFactory.newInstance()
   *
   * Description: Creates a new SOAPFactory object that is an instance of the
   * default implementation.
   *
   */
  public void newInstanceTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest1: create a SOAPFactory object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
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
   * @assertion_ids: SAAJ:JAVADOC:68;
   *
   * @test_Strategy: Call SOAPFactory.newInstance(
   * SOAPConstants.SOAP_1_1_PROTOCOL)
   *
   * Description: Creates a new SOAPFactory object that is a SOAP 1.1
   * implementation.
   *
   */
  public void newInstanceTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest2: create a SOAP1.1 SOAPFactory object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest2");
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
   * @testName: newInstanceTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:68;
   *
   * @test_Strategy: Call SOAPFactory.newInstance(
   * SOAPConstants.SOAP_1_2_PROTOCOL)
   *
   * Description: Creates a new SOAPFactory object that is a SOAP 1.2
   * implementation.
   *
   */
  public void newInstanceTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest3: create a SOAP1.2 SOAPFactory object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest3");
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
   * @assertion_ids: SAAJ:JAVADOC:69;
   *
   * @test_Strategy: Call SOAPFactory.newInstance(BOGUS)
   *
   * Description: Call SOAPFactory.newInstance(String) with a BOGUS value and
   * expect a SOAPException.
   *
   */
  public void newInstanceTest4() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest4: create a BOGUS SOAPFactory object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest4");
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
   * @testName: createElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:52; SAAJ:JAVADOC:53;
   *
   * @test_Strategy: Call SOAPFactory.createElement(Name)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * Name object.
   *
   */
  public void createElementTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest1: create a SOAPElement object constructor1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest1");
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
      throw new Fault("createElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest1 failed");
  }

  /*
   * @testName: createElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:56; SAAJ:JAVADOC:57;
   *
   * @test_Strategy: Call SOAPFactory.createElement(String)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * local name.
   *
   */
  public void createElementTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest2: create a SOAPElement object constructor2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest2");
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
      throw new Fault("createElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest2 failed");
  }

  /*
   * @testName: createElementTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:58; SAAJ:JAVADOC:59;
   *
   * @test_Strategy: Call SOAPFactory.createElement(String, String, String)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * local name, prefix, and uri.
   *
   */
  public void createElementTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest3: create a SOAPElement object constructor3");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest3");
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
      throw new Fault("createElementTest3 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest3 failed");
  }

  /*
   * @testName: createElementTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:54; SAAJ:JAVADOC:55;
   *
   * @test_Strategy: Call SOAPFactory.createElement(QName)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * QName object.
   *
   */
  public void createElementTest4() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest4: create a SOAPElement object constructor4");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest4");
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
      throw new Fault("createElementTest4 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest4 failed");
  }

  /*
   * @testName: createElementTest5
   *
   * @assertion_ids: SAAJ:JAVADOC:50; SAAJ:JAVADOC:51;
   *
   * @test_Strategy: Call SOAPFactory.createElement(org.w3c.dom.Element)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * DOM Element object. This test case creates and passes in a DOM
   * org.w3c.dom.Element.
   *
   */
  public void createElementTest5() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest5: create a SOAPElement object constructor5");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest5");
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
      throw new Fault("createElementTest5 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest5 failed");
  }

  /*
   * @testName: createElementTest6
   *
   * @assertion_ids: SAAJ:JAVADOC:50; SAAJ:JAVADOC:51;
   *
   * @test_Strategy: Call SOAPFactory.createElement(org.w3c.dom.Element)
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * DOM Element object. This test case creates and passes in a SOAPElement
   * which extends org.w3c.dom.Element.
   *
   */
  public void createElementTest6() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createElementTest6: create a SOAPElement object constructor5");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createElementTest6");
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
      throw new Fault("createElementTest6 failed", e);
    }

    if (!pass)
      throw new Fault("createElementTest6 failed");
  }

  /*
   * @testName: createDetailTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:60; SAAJ:JAVADOC:61;
   *
   * @test_Strategy: Call SOAPFactory.createDetail(String)
   *
   * Description: Creates a new Detail object.
   *
   */
  public void createDetailTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createDetailTest1: create a Detail object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createDetailTest1");
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
      throw new Fault("createDetailTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createDetailTest1 failed");
  }

  /*
   * @testName: createNameTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:64; SAAJ:JAVADOC:65;
   *
   * @test_Strategy: Call SOAPFactory.createName(String)
   *
   * Description: Creates a new Name object initialized with the given local
   * name.
   *
   */
  public void createNameTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createNameTest1: create a Name object constructor1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createNameTest1");
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
      throw new Fault("createNameTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createNameTest1 failed");
  }

  /*
   * @testName: createNameTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:62; SAAJ:JAVADOC:63;
   *
   * @test_Strategy: Call SOAPFactory.createName(String, String, String)
   *
   * Description: Creates a new Name object initialized with the given local
   * name, namespace prefix, and namespace uri.
   *
   */
  public void createNameTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createNameTest2: create a Name object constructor2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createNameTest2");
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
      throw new Fault("createNameTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createNameTest2 failed");
  }

  /*
   * @testName: createFaultTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:317; SAAJ:JAVADOC:318;
   *
   * @test_Strategy: Call SOAPFactory.createFault()
   *
   * Description: Creates a new SOAPFault object using default constructor.
   *
   */
  public void createFaultTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createFaultTest1: create a SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createFaultTest1");
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
      throw new Fault("createFaultTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createFaultTest1 failed");
  }

  /*
   * @testName: createFaultTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:315; SAAJ:JAVADOC:316;
   *
   * @test_Strategy: Call SOAPFactory.createFault(String, QName).
   *
   * Description: Creates a new SOAPFault object passing in the reason text as a
   * String and the fault code as a QName.
   * 
   *
   */
  public void createFaultTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createFaultTest2: create a SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createFaultTest2");
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
      throw new Fault("createFaultTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createFaultTest2 failed");
  }

  /*
   * @testName: createFaultSOAPExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:316;
   *
   * @test_Strategy: Call SOAPFactory.createFault(String, QName).
   *
   * Description: Creates a new SOAPFault object passing in the reason text as a
   * String and the fault code as a QName. Pass in an invalid fault code. Should
   * get a SOAPException.
   *
   */
  public void createFaultSOAPExceptionTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("createFaultSOAPExceptionTest1: test for a SOAPException");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createFaultSOAPExceptionTest1");
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
      throw new Fault("createFaultSOAPExceptionTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createFaultSOAPExceptionTest1 failed");
  }
}
