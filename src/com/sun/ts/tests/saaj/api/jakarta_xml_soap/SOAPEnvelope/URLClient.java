/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPEnvelope;

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

  private static final String SOAPENVELOPE_TESTSERVLET = "/SOAPEnvelope_web/SOAPEnvelopeTestServlet";

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
   * @testName: addBodyTest
   *
   * @assertion_ids: SAAJ:JAVADOC:234; SAAJ:JAVADOC:235;
   *
   * @test_Strategy: Call SOAPEnvelope.addBody().
   *
   * Description: Creates a SOAPBody object and sets it as the SOAPBody object
   * for this SOAPEnvelope object.
   *
   */
  public void addBodyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addBodyTest: create a SOAPBody object for "
          + "this SOAPEnvelope object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addBodyTest");
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
      throw new Fault("addBodyTest failed", e);
    }

    if (!pass)
      throw new Fault("addBodyTest failed");
  }

  /*
   * @testName: getBodyTest
   *
   * @assertion_ids: SAAJ:JAVADOC:230; SAAJ:JAVADOC:231;
   *
   * @test_Strategy: Call SOAPEnvelope.getBody().
   *
   * Description: Returns the SOAPBody object associated with this SOAPEnvelope
   * object.
   *
   */
  public void getBodyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getBodyTest: retrieve a SOAPBody object for "
          + "this SOAPEnvelope object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getBodyTest");
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
      throw new Fault("getBodyTest failed", e);
    }

    if (!pass)
      throw new Fault("getBodyTest failed");
  }

  /*
   * @testName: addHeaderTest
   *
   * @assertion_ids: SAAJ:JAVADOC:232; SAAJ:JAVADOC:233;
   *
   * @test_Strategy: Call SOAPEnvelope.addHeader().
   *
   * Description: Creates a SOAPHeader object and sets it as the SOAPHeader
   * object for this SOAPEnvelope object.
   *
   */
  public void addHeaderTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addHeaderTest: create a SOAPHeader object for "
          + "this SOAPEnvelope object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeaderTest");
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
      throw new Fault("addHeaderTest failed", e);
    }

    if (!pass)
      throw new Fault("addHeaderTest failed");
  }

  /*
   * @testName: getHeaderTest
   *
   * @assertion_ids: SAAJ:JAVADOC:228; SAAJ:JAVADOC:229;
   *
   * @test_Strategy: Call SOAPEnvelope.getHeader().
   *
   * Description: Returns the SOAPHeader object associated with this
   * SOAPEnvelope object.
   */
  public void getHeaderTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getHeaderTest: retrieve a SOAPHeader object for "
          + "this SOAPEnvelope object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getHeaderTest");
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
      throw new Fault("getHeaderTest failed", e);
    }

    if (!pass)
      throw new Fault("getHeaderTest failed");
  }

  /*
   * @testName: createNameTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:226; SAAJ:JAVADOC:227;
   *
   * @test_Strategy: Call SOAPEnvelope.createName(String).
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
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
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
   * @assertion_ids: SAAJ:JAVADOC:224; SAAJ:JAVADOC:225;
   *
   * @test_Strategy: Call SOAPEnvelope.createName(String, String, String).
   *
   * Description: Creates a new Name object initialized with the given local
   * name, prefix, and URI.
   *
   */
  public void createNameTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createNameTest2: create a Name object constructor2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
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
   * @testName: createNameTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:329;
   *
   * @test_Strategy: Call SOAPEnvelope.createName(String, String).
   *
   * Description: Creates a new Name object initialized with the given local
   * name, prefix, and URI.
   *
   */
  public void createNameTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createNameTest3: create a Name object constructor2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createNameTest3");
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
      throw new Fault("createNameTest3 failed", e);
    }

    if (!pass)
      throw new Fault("createNameTest3 failed");
  }
}
