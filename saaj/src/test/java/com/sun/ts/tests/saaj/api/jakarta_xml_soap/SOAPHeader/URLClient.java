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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeader;

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

  private static final String SOAPHEADER_TESTSERVLET = "/SOAPHeader_web/SOAPHeaderTestServlet";

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
   * @testName: addHeaderElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:167; SAAJ:JAVADOC:168;
   *
   * @test_Strategy: Call SOAPHeader.addHeaderElement(Name) method and verify
   * creation of a new SOAPHeaderElement.
   *
   * Description: Create a SOAPHeaderElement object
   *
   */
  public void addHeaderElementTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addHeaderElementTest1: add SOAPHeaderElement object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeaderElementTest1");
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
      throw new Fault("addHeaderElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("addHeaderElementTest1 failed");
  }

  /*
   * @testName: addHeaderElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:169; SAAJ:JAVADOC:170;
   *
   * @test_Strategy: Call SOAPHeader.addHeaderElement(QName) method and verify
   * creation of a new SOAPHeaderElement.
   *
   * Description: Create a SOAPHeaderElement object
   *
   */
  public void addHeaderElementTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("addHeaderElementTest2: add SOAPHeaderElement object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeaderElementTest2");
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
      throw new Fault("addHeaderElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("addHeaderElementTest2 failed");
  }

  /*
   * @testName: examineHeaderElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:172;
   *
   * @test_Strategy: Call SOAPHeader.ExamineHeaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Single element test.
   *
   * Description: Examine SOAPHeaderElements that have specified actor
   *
   */
  public void examineHeaderElementsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("examineHeaderElementsTest1: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest1");
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
      throw new Fault("examineHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("examineHeaderElementsTest1 failed");
  }

  /*
   * @testName: examineHeaderElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:172;
   *
   * @test_Strategy: Call SOAPHeader.ExamineHeaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Multiple element test.
   *
   * Description: Examine SOAPHeaderElements that have specified actor
   *
   */
  public void examineHeaderElementsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("examineHeaderElementsTest2: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest2");
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
      throw new Fault("examineHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("examineHeaderElementsTest2 failed");
  }

  /*
   * @testName: examineHeaderElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:172;
   *
   * @test_Strategy: Call SOAPHeader.ExamineHeaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Zero elements test.
   *
   * Description: Examine SOAPHeaderElements that have specified actor
   *
   */
  public void examineHeaderElementsTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("examineHeaderElementsTest3: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest3");
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
      throw new Fault("examineHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Fault("examineHeaderElementsTest3 failed");
  }

  /*
   * @testName: examineHeaderElementsTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:172;
   *
   * @test_Strategy: Call SOAPHeader.ExamineHeaderElements(QName) method and
   * verify correct SOAPHeaderElements returned. Zero elements test.
   *
   * Description: Examine SOAPHeaderElements that have specified actor
   *
   */
  public void examineHeaderElementsTest4() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("examineHeaderElementsTest4: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest4");
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
      throw new Fault("examineHeaderElementsTest4 failed", e);
    }

    if (!pass)
      throw new Fault("examineHeaderElementsTest4 failed");
  }

  /*
   * @testName: extractHeaderElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:173;
   *
   * @test_Strategy: Call SOAPHeader.extractHeaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Single element test.
   *
   * Description: Extract SOAPHeaderElements that have specified actor
   *
   */
  public void extractHeaderElementsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("extractHeaderElementsTest1: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest1");
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
      throw new Fault("extractHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("extractHeaderElementsTest1 failed");
  }

  /*
   * @testName: extractHeaderElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:173;
   *
   * @test_Strategy: Call SOAPHeader.extracteaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Multiple element test.
   *
   * Description: Extract SOAPHeaderElements that have specified actor
   *
   */
  public void extractHeaderElementsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("extractHeaderElementsTest2: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest2");
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
      throw new Fault("extractHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("extractHeaderElementsTest2 failed");
  }

  /*
   * @testName: extractHeaderElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:173;
   *
   * @test_Strategy: Call SOAPHeader.extractHeaderElements(String) method and
   * verify correct SOAPHeaderElements returned. Zero elements test.
   *
   * Description: Extract SOAPHeaderElements that have specified actor
   *
   */
  public void extractHeaderElementsTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("extractHeaderElementsTest3: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest3");
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
      throw new Fault("extractHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Fault("extractHeaderElementsTest3 failed");
  }

  /*
   * @testName: addNotUnderstoodHeaderElementTest
   *
   * @assertion_ids: SAAJ:JAVADOC:174; SAAJ:JAVADOC:175;
   *
   * @test_Strategy: Call SOAPHeader.addNotUnderstoodHeaderElement(QName) method
   * and verify correct SOAPHeaderElement contents.
   *
   * Description: Add NotUnderstood SOAPHeaderElement and verify contents.
   *
   */
  public void addNotUnderstoodHeaderElementTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addNotUnderstoodHeaderElementTest");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME",
              "addNotUnderstoodHeaderElementSOAP11Test");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME",
              "addNotUnderstoodHeaderElementSOAP12Test");
          props.setProperty("SOAPVERSION", "soap12");
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
      throw new Fault("addNotUnderstoodHeaderElementTest failed", e);
    }

    if (!pass)
      throw new Fault("addNotUnderstoodHeaderElementTest failed");
  }

  /*
   * @testName: addUpgradeHeaderElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:176; SAAJ:JAVADOC:177;
   *
   * @test_Strategy: Call SOAPHeader.addUpgradeHeaderElement(Iterator) method
   * and verify correct SOAPHeaderElement contents.
   *
   * Description: Add Upgrade SOAPHeaderElement and verify contents.
   *
   */
  public void addUpgradeHeaderElementTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addUpgradeHeaderElementTest1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest1");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest1");
          props.setProperty("SOAPVERSION", "soap12");
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
      throw new Fault("addUpgradeHeaderElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("addUpgradeHeaderElementTest1 failed");
  }

  /*
   * @testName: addUpgradeHeaderElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:178; SAAJ:JAVADOC:179;
   *
   * @test_Strategy: Call SOAPHeader.addUpgradeHeaderElement(String[]) method
   * and verify correct SOAPHeaderElement contents.
   *
   * Description: Add Upgrade SOAPHeaderElement and verify contents.
   *
   */
  public void addUpgradeHeaderElementTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addUpgradeHeaderElementTest2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest2");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest2");
          props.setProperty("SOAPVERSION", "soap12");
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
      throw new Fault("addUpgradeHeaderElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("addUpgradeHeaderElementTest2 failed");
  }

  /*
   * @testName: addUpgradeHeaderElementTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:180; SAAJ:JAVADOC:181;
   *
   * @test_Strategy: Call SOAPHeader.addUpgradeHeaderElement(String) method and
   * verify correct SOAPHeaderElement contents.
   *
   * Description: Add Upgrade SOAPHeaderElement and verify contents.
   *
   */
  public void addUpgradeHeaderElementTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("addUpgradeHeaderElementTest3");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest3");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest3");
          props.setProperty("SOAPVERSION", "soap12");
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
      throw new Fault("addUpgradeHeaderElementTest3 failed", e);
    }

    if (!pass)
      throw new Fault("addUpgradeHeaderElementTest3 failed");
  }

  /*
   * @testName: examineAllHeaderElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:182;
   *
   * @test_Strategy: Call SOAPHeader.examineAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Single element test.
   *
   * Description: Examine all SOAPHeaderElements.
   *
   */
  public void examineAllHeaderElementsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("examineAllHeaderElementsTest1: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineAllHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("examineAllHeaderElementsTest1 failed");
  }

  /*
   * @testName: examineAllHeaderElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:182;
   *
   * @test_Strategy: Call SOAPHeader.examineAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Multiple element test same
   * actor.
   *
   * Description: Examine all SOAPHeaderElement
   *
   */
  public void examineAllHeaderElementsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("examineAllHeaderElementsTest2: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineAllHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("examineAllHeaderElementsTest2 failed");
  }

  /*
   * @testName: examineAllHeaderElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:182;
   *
   * @test_Strategy: Call SOAPHeader.examineAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Multiple element test different
   * actors.
   *
   * Description: Examine all SOAPHeaderElements.
   *
   */
  public void examineAllHeaderElementsTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("examineAllHeaderElementsTest3: examine SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineAllHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Fault("examineAllHeaderElementsTest3 failed");
  }

  /*
   * @testName: examineMustUnderstandHeaderElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:171;
   *
   * @test_Strategy: Call SOAPHeader.examineMustUnderstandHeaderElements(String)
   * method and verify that the specified actor is returned when the
   * MustUnderstand attribute is true.
   *
   * Description: Examine SOAPHeaderElements
   *
   */
  public void examineMustUnderstandHeaderElementsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "examineMustUnderstandHeaderElementsTest1: examine MustUnderstand");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineMustUnderstandHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("examineMustUnderstandHeaderElementsTest1 failed");
  }

  /*
   * @testName: examineMustUnderstandHeaderElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:171;
   *
   * @test_Strategy: Call SOAPHeader.examineMustUnderstandHeaderElements(String)
   * method and verify that the specified actor is not returned when the
   * MustUnderstand attribute is false.
   *
   * Description: Examine SOAPHeaderElements
   *
   */
  public void examineMustUnderstandHeaderElementsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "examineMustUnderstandHeaderElementsTest2: examine MustUnderstand");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineMustUnderstandHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("examineMustUnderstandHeaderElementsTest2 failed");
  }

  /*
   * @testName: examineMustUnderstandHeaderElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:171;
   *
   * @test_Strategy: Call SOAPHeader.examineMustUnderstandHeaderElements(String)
   * method and verify that the specified actor is returned when the
   * MustUnderstand attribute is true with multiple actors.
   *
   * Description: Examine SOAPHeaderElements
   *
   */
  public void examineMustUnderstandHeaderElementsTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "examineMustUnderstandHeaderElementsTest3: examine MustUnderstand");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("examineMustUnderstandHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Fault("examineMustUnderstandHeaderElementsTest3 failed");
  }

  /*
   * @testName: extractAllHeaderElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:183;
   *
   * @test_Strategy: Call SOAPHeader.extractAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Single element test.
   *
   * Description: Examine all SOAPHeaderElements.
   *
   */
  public void extractAllHeaderElementsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("extractAllHeaderElementsTest1: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("extractAllHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("extractAllHeaderElementsTest1 failed");
  }

  /*
   * @testName: extractAllHeaderElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:183;
   *
   * @test_Strategy: Call SOAPHeader.extractAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Multiple element test same
   * actor.
   *
   * Description: Examine all SOAPHeaderElement
   *
   */
  public void extractAllHeaderElementsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("extractAllHeaderElementsTest2: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("extractAllHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("extractAllHeaderElementsTest2 failed");
  }

  /*
   * @testName: extractAllHeaderElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:183;
   *
   * @test_Strategy: Call SOAPHeader.extractAllHeaderElements() method and
   * verify correct SOAPHeaderElements returned. Multiple element test different
   * actors.
   *
   * Description: Examine all SOAPHeaderElements.
   *
   */
  public void extractAllHeaderElementsTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("extractAllHeaderElementsTest3: extract SOAPHeaderElements");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("extractAllHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Fault("extractAllHeaderElementsTest3 failed");
  }
}
