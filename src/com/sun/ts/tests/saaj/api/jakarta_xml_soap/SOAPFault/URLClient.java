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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFault;

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

  private static final String TESTSERVLET = "/SOAPFault_web/SOAPFaultTestServlet";

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
   * @testName: SetGetFaultString1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:200; SAAJ:JAVADOC:201; SAAJ:JAVADOC:204
   *
   * @test_Strategy: Call SOAPFault.setFaultString/getFaultString with a string.
   * Must succeed.
   *
   */
  public void SetGetFaultString1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultString1Test");
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
      throw new Fault("SetGetFaultString1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultString1Test failed");
  }

  /*
   * @testName: SetGetFaultCode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:188; SAAJ:JAVADOC:196;
   *
   * @test_Strategy: Call SOAPFault.setFaultCode/getFaultCode with a string.
   * Must succeed.
   *
   */
  public void SetGetFaultCode1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultCode1Test");
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
      throw new Fault("SetGetFaultCode1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultCode1Test failed");
  }

  /*
   * @testName: SetGetFaultActor1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:197; SAAJ:JAVADOC:199;
   *
   * @test_Strategy: Call SOAPFault.setFaultActor/getFaultActor with a URI. Must
   * succeed.
   *
   */
  public void SetGetFaultActor1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultActor1Test");
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
      throw new Fault("setFaultActor1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultActor1Test failed");
  }

  /*
   * @testName: AddGetDetail1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:207; SAAJ:JAVADOC:208;
   *
   * @test_Strategy: Call SOAPFault.addDetail/getDetail
   *
   */
  public void AddGetDetail1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "AddGetDetail1Test");
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
      throw new Fault("AddGetDetail1Test failed", e);
    }
    if (!pass)
      throw new Fault("AddGetDetail1Test failed");
  }

  /*
   * @testName: addFaultReasonText1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() on a SOAP1.1 message.
   *
   * Description: Calling SOAPFault.addFaultReasonText() on a SOAP1.1 message
   * must throw UnsupportedOperationException.
   *
   */
  public void addFaultReasonText1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultReasonText1Test failed", e);
    }

    if (!pass)
      throw new Fault("addFaultReasonText1Test failed");
  }

  /*
   * @testName: addFaultReasonText2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() on a SOAP1.2 message.
   *
   * Description: Calling SOAPFault.addFaultReasonText() on a SOAP1.2 message
   * must succeed.
   *
   */
  public void addFaultReasonText2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultReasonText2Test failed", e);
    }

    if (!pass)
      throw new Fault("addFaultReasonText2Test failed");
  }

  /*
   * @testName: addFaultReasonText3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() using 2 different
   * locales. See that both are returned on a SOAP1.2 message.
   */
  public void addFaultReasonText3Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultReasonText3Test failed", e);
    }

    if (!pass)
      throw new Fault("addFaultReasonText3Test failed");
  }

  /*
   * @testName: addFaultReasonText4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() using the same locale.
   * See that the first is overwritten by the second on a SOAP1.2 message.
   */
  public void addFaultReasonText4Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText4Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultReasonText4Test failed", e);
    }

    if (!pass)
      throw new Fault("addFaultReasonText4Test failed");
  }

  /*
   * @testName: getFaultReasonLocales1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:210; SAAJ:JAVADOC:211;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonLocales() on a SOAP1.1
   * message.
   *
   * Description: Calling SOAPFault.getFaultReasonLocales() on a SOAP1.1 message
   * must throw UnsupportedOperationException.
   *
   */
  public void getFaultReasonLocales1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonLocales1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonLocales1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonLocales1Test failed");
  }

  /*
   * @testName: getFaultReasonLocales2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:210; SAAJ:JAVADOC:211;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonLocales() on a SOAP1.2 message
   * that added 1 FaultReasonText.
   *
   * Description: Calling SOAPFault.getFaultReasonLocales() on a SOAP1.2 message
   * must succeed.
   *
   */
  public void getFaultReasonLocales2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonLocales2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonLocales2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonLocales2Test failed");
  }

  /*
   * @testName: getFaultReasonText1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:214; SAAJ:JAVADOC:215;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonText() on a SOAP1.1 message.
   *
   * Description: Calling SOAPFault.getFaultReasonText() on a SOAP1.1 message
   * must throw UnsupportedOperationException.
   *
   */
  public void getFaultReasonText1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonText1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonText1Test failed");
  }

  /*
   * @testName: getFaultReasonText2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:214; SAAJ:JAVADOC:215;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonText() on a SOAP1.2 message
   * that added 3 FaultReasonTexts. Two using Locale.ENGLISH and on with
   * Locale.UK. Only the text associated with Locale.UK should be returned.
   *
   * Description: Calling SOAPFault.getFaultReasonText() on a SOAP1.2 message
   * must succeed.
   *
   */
  public void getFaultReasonText2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonText2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonText2Test failed");
  }

  /*
   * @testName: getFaultReasonText3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:214; SAAJ:JAVADOC:215;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonText() after calling
   * SOAPFault.addFaultReasonText() twice, the first time to set a reason text
   * and the second to replace the old text with a new text value
   *
   */
  public void getFaultReasonText3Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonText3Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonText3Test failed");
  }

  /*
   * @testName: getFaultReasonTexts1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:212; SAAJ:JAVADOC:213;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonTexts() on a SOAP1.1 message.
   *
   * Description: Calling SOAPFault.getFaultReasonTexts() on a SOAP1.1 message
   * must throw UnsupportedOperationException.
   *
   */
  public void getFaultReasonTexts1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonTexts1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonTexts1Test failed");
  }

  /*
   * @testName: getFaultReasonTexts2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:212; SAAJ:JAVADOC:213;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonTexts() on a SOAP1.2 message
   * that added 1 FaultReasonText.
   *
   * Description: Calling SOAPFault.getFaultReasonTexts() on a SOAP1.2 message
   * must succeed.
   *
   */
  public void getFaultReasonTexts2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonTexts2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonTexts2Test failed");
  }

  /*
   * @testName: getFaultReasonTexts3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:212; SAAJ:JAVADOC:213;
   *
   * @test_Strategy: Call SOAPFault.getFaultReasonTexts() on a SOAP1.2 message
   * that added 2 FaultReasonTexts that each have different Locales.
   *
   * Description: Calling SOAPFault.getFaultReasonTexts() on a SOAP1.2 message
   * must succeed.
   *
   */
  public void getFaultReasonTexts3Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultReasonTexts3Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultReasonTexts3Test failed");
  }

  /*
   * @testName: setFaultNode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:219; SAAJ:JAVADOC:220;
   *
   * @test_Strategy: Call SOAPFault.setFaultNode() on a SOAP1.1 message must
   * fail.
   *
   * Description: Calling SOAPFault.setFaultNode() on a SOAP1.1 message must
   * throw UnsupportedOperationException.
   *
   */
  public void setFaultNode1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Fault("setFaultNode1Test failed");
  }

  /*
   * @testName: SetGetFaultNode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218; SAAJ:JAVADOC:219;
   *
   * @test_Strategy: Call SOAPFault.setFaultNode() and getFaultNode() on a
   * SOAP1.2 message must succeed.
   */
  public void SetGetFaultNode1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultNode1Test failed");
  }

  /*
   * @testName: SetGetFaultNode2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218; SAAJ:JAVADOC:219;
   *
   * @test_Strategy: Call SOAPFault.setFaultNode() twice on a SOAP1.2 message.
   */
  public void SetGetFaultNode2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultNode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultNode2Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultNode2Test failed");
  }

  /*
   * @testName: getFaultNode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218;
   *
   * @test_Strategy: Call SOAPFault.getFaultNode() on a SOAP1.1 message must
   * fail.
   *
   * Description: Calling SOAPFault.getFaultNode() on a SOAP1.1 message must
   * throw UnsupportedOperationException.
   *
   */
  public void getFaultNode1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultNode1Test failed");
  }

  /*
   * @testName: getFaultNode2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218;
   *
   * @test_Strategy: Call SOAPFault.getFaultNode() on a SOAP1.2 message that
   * doesn't have a node set.
   *
   */
  public void getFaultNode2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultNode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultNode2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultNode2Test failed");
  }

  /*
   * @testName: setFaultRole1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:222; SAAJ:JAVADOC:223;
   *
   * @test_Strategy: Call SOAPFault.setFaultRole() on a SOAP1.1 message must
   * fail.
   *
   * Description: Calling SOAPFault.setFaultRole() on a SOAP1.1 message must
   * throw UnsupportedOperationException.
   *
   */
  public void setFaultRole1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Fault("setFaultRole1Test failed");
  }

  /*
   * @testName: SetGetFaultRole1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:221; SAAJ:JAVADOC:222;
   *
   * @test_Strategy: Call SOAPFault.setFaultRole/getFaultRole on a SOAP1.2
   * message must succeed.
   *
   */
  public void SetGetFaultRole1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultRole1Test failed");
  }

  /*
   * @testName: getFaultRole1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:221;
   *
   * @test_Strategy: Call SOAPFault.getFaultRole() on a SOAP1.1 message must
   * fail.
   *
   * Description: Calling SOAPFault.getFaultRole() on a SOAP1.1 message must
   * throw UnsupportedOperationException.
   *
   */
  public void getFaultRole1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultRole1Test failed");
  }

  /*
   * @testName: getFaultRole2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:221;
   *
   * @test_Strategy: Call SOAPFault.getFaultRole() on a SOAP1.2 message that
   * does not have a role set.
   *
   */
  public void getFaultRole2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultRole2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultRole2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultRole2Test failed");
  }

  /*
   * @testName: SetGetFaultStringLocale1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:202; SAAJ:JAVADOC:205
   *
   * @test_Strategy: Call SOAPFault.setFaultString() and getFaultString() with a
   * string and locale
   *
   */
  public void SetGetFaultStringLocale1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultStringLocale1Test");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultStringLocale1Test failed");
  }

  /*
   * @testName: setFaultStringLocale1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:202;
   *
   * @test_Strategy: Call SOAPFault.setFaultString and addFaultReasonText and
   * verify they behave the same
   *
   */
  public void setFaultStringLocale1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultStringLocale1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass")) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Fault("setFaultStringLocale1Test failed");
  }

  /*
   * @testName: getFaultStringLocale1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:205;
   *
   * @test_Strategy: Call SOAPFault.getFaultStringLocale where a fault string
   * was set but not the locale
   *
   */
  public void getFaultStringLocale1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0) {
          props.setProperty("SOAPVERSION", "soap11");
          props.setProperty("TESTNAME", "getFaultStringLocale1SOAP11Test");
        } else {
          props.setProperty("SOAPVERSION", "soap12");
          props.setProperty("TESTNAME", "getFaultStringLocale1SOAP12Test");
        }
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultStringLocale1Test failed");
  }

  /*
   * @testName: SetGetFaultCodeAsName1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:184; SAAJ:JAVADOC:190;
   *
   * @test_Strategy: Call SOAPFault.setFaultCodeAsName() and
   * getFaultCodeAsName() to return Name.
   *
   */
  public void SetGetFaultCodeAsName1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultCodeAsName1Test");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultCodeAsName1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultCodeAsName1Test failed");
  }

  /*
   * @testName: SetGetFaultCodeAsQName1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:184; SAAJ:JAVADOC:190;
   *
   * @test_Strategy: Call SOAPFault.setFaultCodeAsQName() and
   * getFaultCodeAsQName() to return QName.
   *
   */
  public void SetGetFaultCodeAsQName1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultCodeAsQName1Test");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetFaultCodeAsQName1Test failed", e);
    }

    if (!pass)
      throw new Fault("SetGetFaultCodeAsQName1Test failed");
  }

  /*
   * @testName: appendFaultSubcode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:194;
   *
   * @test_Strategy: Call SOAPFault.appendFaultSubcode() on a SOAP1.1 message.
   *
   * Description: Calling SOAPFault.appendFaultSubcode() on a SOAP1.1 message
   * must throw UnsupportedOperationException.
   *
   */
  public void appendFaultSubcode1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "appendFaultSubcode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("appendFaultSubcode1Test failed", e);
    }

    if (!pass)
      throw new Fault("appendFaultSubcode1Test failed");
  }

  /*
   * @testName: appendFaultSubcode2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:194;
   *
   * @test_Strategy: Call SOAPFault.appendFaultSubcode() twice on a SOAP1.2
   * message must succeed.
   *
   */
  public void appendFaultSubcode2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "appendFaultSubcode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("appendFaultSubcode2Test failed", e);
    }

    if (!pass)
      throw new Fault("appendFaultSubcode2Test failed");
  }

  /*
   * @testName: getFaultSubcodes1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:192;
   *
   * @test_Strategy: Call SOAPFault.getFaultSubcodes() on a SOAP1.1 message.
   *
   * Description: Calling SOAPFault.getFaultSubcodes() on a SOAP1.1 message must
   * throw UnsupportedOperationException.
   *
   */
  public void getFaultSubcodes1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultSubcodes1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultSubcodes1Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultSubcodes1Test failed");
  }

  /*
   * @testName: getFaultSubcodes2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:192;
   *
   * @test_Strategy: Call SOAPFault.getFaultSubcodes() on a SOAP1.2 message that
   * contains 2 Subcodes.
   *
   */
  public void getFaultSubcodes2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultSubcodes2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getFaultSubcodes2Test failed", e);
    }

    if (!pass)
      throw new Fault("getFaultSubcodes2Test failed");
  }

  /*
   * @testName: hasDetail1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:206;
   *
   * @test_Strategy: Call SOAPFault.hasDetail when no detail exists
   *
   * Description: Sets this SOAPFault object
   *
   */
  public void hasDetail1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "hasDetail1Test");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("hasDetail1Test failed", e);
    }

    if (!pass)
      throw new Fault("hasDetail1Test failed");
  }

  /*
   * @testName: hasDetail2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:206;
   *
   * @test_Strategy: Call SOAPFault.hasDetail when no detail exists
   *
   * Description: Sets this SOAPFault object
   *
   */
  public void hasDetail2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "hasDetail2Test");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("hasDetail2Test failed", e);
    }

    if (!pass)
      throw new Fault("hasDetail2Test failed");
  }

  /*
   * @testName: removeAllFaultSubcodes1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:193;
   *
   * @test_Strategy: Call SOAPFault.removeAllFaultSubcodes() on a SOAP1.1
   * message.
   *
   * Description: Calling SOAPFault.removeAllFaultSubcodes() on a SOAP1.1
   * message must throw UnsupportedOperationException.
   *
   */
  public void removeAllFaultSubcodes1Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "removeAllFaultSubcodes1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllFaultSubcodes1Test failed", e);
    }

    if (!pass)
      throw new Fault("removeAllFaultSubcodes1Test failed");
  }

  /*
   * @testName: removeAllFaultSubcodes2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:193;
   *
   * @test_Strategy: Call SOAPFault.removeAllFaultSubcodes() on a SOAP1.2
   * message that contains 2 Subcodes. Then call getFaultSubcodes() is ensure an
   * empty interator is returned
   *
   */
  public void removeAllFaultSubcodes2Test() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "removeAllFaultSubcodes2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllFaultSubcodes2Test failed", e);
    }

    if (!pass)
      throw new Fault("removeAllFaultSubcodes2Test failed");
  }

  /*
   * @testName: SetFaultCodeNameSOAPExceptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:185;
   *
   * @test_Strategy: Call SOAPFault.setFaultCode with a non qualified Name
   * object. Must throw SOAPException for SOAP1.2 protocol.
   *
   */
  public void SetFaultCodeNameSOAPExceptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeNameSOAPExceptionTest");
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
      throw new Fault("SetFaultCodeNameSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SetFaultCodeNameSOAPExceptionTest failed");
  }

  /*
   * @testName: SetFaultCodeQNameSOAPExceptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:187;
   *
   * @test_Strategy: Call SOAPFault.setFaultCode with a non qualified QName
   * object. Must throw SOAPException for SOAP1.2 protocol.
   *
   */
  public void SetFaultCodeQNameSOAPExceptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeQNameSOAPExceptionTest");
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
      throw new Fault("SetFaultCodeQNameSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SetFaultCodeQNameSOAPExceptionTest failed");
  }

  /*
   * @testName: SetFaultCodeStringSOAPExceptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:189;
   *
   * @test_Strategy: Call SOAPFault.setFaultCode with a non qualified String
   * object. Must throw SOAPException for SOAP1.2 protocol.
   *
   */
  public void SetFaultCodeStringSOAPExceptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeStringSOAPExceptionTest");
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
      throw new Fault("SetFaultCodeStringSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SetFaultCodeStringSOAPExceptionTest failed");
  }

  /*
   * @testName: AppendFaultSubcodeSOAPExceptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:195;
   *
   * @test_Strategy: Call SOAPFault.appendFaultSubcode with a non qualified
   * QName object. Must throw SOAPException.
   *
   */
  public void AppendFaultSubcodeSOAPExceptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "AppendFaultSubcodeSOAPExceptionTest");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("AppendFaultSubcodeSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("AppendFaultSubcodeSOAPExceptionTest failed");
  }

  /*
   * @testName: AddDetailSOAPExceptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:209;
   *
   * @test_Strategy: Call SOAPFault.addDetail() twice. Must throw a
   * SOAPException.
   *
   */
  public void AddDetailSOAPExceptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "AddDetailSOAPExceptionTest");
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
      throw new Fault("AddDetailSOAPExceptionTest failed", e);
    }
    if (!pass)
      throw new Fault("AddDetailSOAPExceptionTest failed");
  }
}
