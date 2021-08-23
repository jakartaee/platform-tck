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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPBody;

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

  private static final String SOAPBODY_TESTSERVLET = "/SOAPBody_web/SOAPBodyTestServlet";

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
   * @testName: addBodyElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:291; SAAJ:JAVADOC:292;
   *
   * @test_Strategy: Call SOAPBody.addBodyElement(Name) method and verify
   * creation of a new SOAPBodyElement.
   *
   * Description: Create a SOAPBodyElement object
   *
   */
  public void addBodyElementTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addBodyElementTest1: add SOAPBodyElement object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addBodyElementTest1");
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
      throw new Fault("addBodyElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("addBodyElementTest1 failed");
  }

  /*
   * @testName: addBodyElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:293; SAAJ:JAVADOC:294;
   *
   * @test_Strategy: Call SOAPBody.addBodyElement(QName) method and verify
   * creation of a new SOAPBodyElement.
   *
   * Description: Create a SOAPBodyElement object
   *
   */
  public void addBodyElementTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addBodyElementTest2: add SOAPBodyElement object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addBodyElementTest2");
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
      throw new Fault("addBodyElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("addBodyElementTest2 failed");
  }

  /*
   * @testName: addFaultTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:279; SAAJ:JAVADOC:280;
   *
   * @test_Strategy: Call SOAPBody.addFault() method and verify the creation of
   * a new SOAPFault object.
   *
   * Description: Creates a new SOAPFault object and adds it to this SOAPBody
   * object.
   *
   */
  public void addFaultTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addFaultTest1: add SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest1");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultTest1 failed", e);
    }

    if (!pass)
      throw new Fault("addFaultTest1 failed");
  }

  /*
   * @testName: addFaultTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:285; SAAJ:JAVADOC:286;
   *
   * @test_Strategy: Call SOAPBody.addFault(Name, String) method and verify the
   * creation of a new SOAPFault object.
   *
   * Description: Creates a new SOAPFault object and adds it to this SOAPBody
   * object.
   *
   */
  public void addFaultTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addFaultTest2: add SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest2");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultTest2 failed", e);
    }

    if (!pass)
      throw new Fault("addFaultTest2 failed");
  }

  /*
   * @testName: addFaultTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:281; SAAJ:JAVADOC:282;
   *
   * @test_Strategy: Call SOAPBody.addFault(Name, String, Locale) method and
   * verify the creation of a new SOAPFault object.
   *
   * Description: Creates a new SOAPFault object and adds it to this SOAPBody
   * object.
   *
   */
  public void addFaultTest3() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addFaultTest3: add SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest3");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultTest3 failed", e);
    }

    if (!pass)
      throw new Fault("addFaultTest3 failed");
  }

  /*
   * @testName: addFaultTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:287; SAAJ:JAVADOC:288;
   *
   * @test_Strategy: Call SOAPBody.addFault(QName, String) method and verify the
   * creation of a new SOAPFault object.
   *
   * Description: Creates a new SOAPFault object and adds it to this SOAPBody
   * object.
   *
   */
  public void addFaultTest4() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addFaultTest4: add SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest4");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultTest4 failed", e);
    }

    if (!pass)
      throw new Fault("addFaultTest4 failed");
  }

  /*
   * @testName: addFaultTest5
   *
   * @assertion_ids: SAAJ:JAVADOC:283; SAAJ:JAVADOC:284;
   *
   * @test_Strategy: Call SOAPBody.addFault(QName, String, Locale) method and
   * verify the creation of a new SOAPFault object.
   *
   * Description: Creates a new SOAPFault object and adds it to this SOAPBody
   * object.
   *
   */
  public void addFaultTest5() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addFaultTest5: add SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest5");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addFaultTest5 failed", e);
    }

    if (!pass)
      throw new Fault("addFaultTest5 failed");
  }

  /*
   * @testName: getFaultTest
   *
   * @assertion_ids: SAAJ:JAVADOC:290;
   *
   * @test_Strategy: Call SOAPBody.getFault() method and verify the return of
   * the SOAPFault object.
   *
   * Description: Return the SOAPFault object that was added to this SOAPBody
   * object.
   *
   */
  public void getFaultTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getFaultTest: get SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getFaultTest");
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
      throw new Fault("getFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("getFaultTest failed");
  }

  /*
   * @testName: hasFaultTest
   *
   * @assertion_ids: SAAJ:JAVADOC:289;
   *
   * @test_Strategy: Call SOAPBody.hasFault() method and verify whether this
   * SOAPBody object has a SOAPFault object.
   *
   * Description: Check whether a SOAPFault object exists in this SOAPBody
   * object.
   *
   */
  public void hasFaultTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("hasFaultTest: has SOAPFault object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "hasFaultTest");
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
      throw new Fault("hasFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("hasFaultTest failed");
  }

  /*
   * @testName: addDocumentTest
   *
   * @assertion_ids: SAAJ:JAVADOC:295; SAAJ:JAVADOC:296;
   *
   * @test_Strategy: Call SOAPBody.addBodyElement(org.w3c.dom.Document) method
   * and verify
   *
   * Description: Create a SOAPBodyElement object
   *
   */
  public void addDocumentTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addDocumentTest: add SOAPBodyElement object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "addDocumentTest");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addDocumentTest failed", e);
    }

    if (!pass)
      throw new Fault("addDocumentTest failed");
  }

  /*
   * @testName: extractContentAsDocumentTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:297;
   *
   * @test_Strategy: Call SOAPBody.extractContentAsDocument() method and verify.
   *
   * Description: Extract content as a DOM Document.
   *
   */
  public void extractContentAsDocumentTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg(
          "extractContentAsDocumentTest1: extract content as DOM Document");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractContentAsDocumentTest1");
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
      throw new Fault("extractContentAsDocumentTest1 failed", e);
    }

    if (!pass)
      throw new Fault("extractContentAsDocumentTest1 failed");
  }

  /*
   * @testName: extractContentAsDocumentTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:298;
   *
   * @test_Strategy: Call SOAPBody.extractContentAsDocument() method and verify.
   *
   * Description: Extract content as a DOM Document. Negative test case for
   * SOAPException to be thrown.
   *
   */
  public void extractContentAsDocumentTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg(
          "extractContentAsDocumentTest2: negative test case for SOAPException");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractContentAsDocumentTest2");
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
      throw new Fault("extractContentAsDocumentTest2 failed", e);
    }

    if (!pass)
      throw new Fault("extractContentAsDocumentTest2 failed");
  }
}
