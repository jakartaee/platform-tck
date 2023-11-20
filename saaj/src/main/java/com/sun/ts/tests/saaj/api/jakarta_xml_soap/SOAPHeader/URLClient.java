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

import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

@ExtendWith(ArquillianExtension.class)
public class URLClient {
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
  
  private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

  @Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPHeader_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeader");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};


  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup() throws Exception {

    boolean pass = true;

    try {
      hostname = System.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(System.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    if (!pass) {
      logger.log(Logger.Level.ERROR,
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
    logger.log(Logger.Level.INFO,"setup ok");
  }

  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO,"cleanup ok");
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
  @Test
  public void addHeaderElementTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addHeaderElementTest1: add SOAPHeaderElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeaderElementTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addHeaderElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addHeaderElementTest1 failed");
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
  @Test
  public void addHeaderElementTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addHeaderElementTest2: add SOAPHeaderElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeaderElementTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addHeaderElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addHeaderElementTest2 failed");
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
  @Test
  public void examineHeaderElementsTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineHeaderElementsTest1: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("examineHeaderElementsTest1 failed");
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
  @Test
  public void examineHeaderElementsTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineHeaderElementsTest2: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("examineHeaderElementsTest2 failed");
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
  @Test
  public void examineHeaderElementsTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineHeaderElementsTest3: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest3");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("examineHeaderElementsTest3 failed");
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
  @Test
  public void examineHeaderElementsTest4() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineHeaderElementsTest4: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "examineHeaderElementsTest4");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineHeaderElementsTest4 failed", e);
    }

    if (!pass)
      throw new Exception("examineHeaderElementsTest4 failed");
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
  @Test
  public void extractHeaderElementsTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractHeaderElementsTest1: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("extractHeaderElementsTest1 failed");
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
  @Test
  public void extractHeaderElementsTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractHeaderElementsTest2: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("extractHeaderElementsTest2 failed");
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
  @Test
  public void extractHeaderElementsTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractHeaderElementsTest3: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractHeaderElementsTest3");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("extractHeaderElementsTest3 failed");
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
  @Test
  public void addNotUnderstoodHeaderElementTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addNotUnderstoodHeaderElementTest");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
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
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addNotUnderstoodHeaderElementTest failed", e);
    }

    if (!pass)
      throw new Exception("addNotUnderstoodHeaderElementTest failed");
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
  @Test
  public void addUpgradeHeaderElementTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addUpgradeHeaderElementTest1");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest1");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest1");
          props.setProperty("SOAPVERSION", "soap12");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addUpgradeHeaderElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addUpgradeHeaderElementTest1 failed");
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
  @Test
  public void addUpgradeHeaderElementTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addUpgradeHeaderElementTest2");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest2");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest2");
          props.setProperty("SOAPVERSION", "soap12");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addUpgradeHeaderElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addUpgradeHeaderElementTest2 failed");
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
  @Test
  public void addUpgradeHeaderElementTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addUpgradeHeaderElementTest3");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest3");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addUpgradeHeaderElementTest3");
          props.setProperty("SOAPVERSION", "soap12");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addUpgradeHeaderElementTest3 failed", e);
    }

    if (!pass)
      throw new Exception("addUpgradeHeaderElementTest3 failed");
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
  @Test
  public void examineAllHeaderElementsTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineAllHeaderElementsTest1: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineAllHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("examineAllHeaderElementsTest1 failed");
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
  @Test
  public void examineAllHeaderElementsTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineAllHeaderElementsTest2: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineAllHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("examineAllHeaderElementsTest2 failed");
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
  @Test
  public void examineAllHeaderElementsTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"examineAllHeaderElementsTest3: examine SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "examineAllHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineAllHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("examineAllHeaderElementsTest3 failed");
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
  @Test
  public void examineMustUnderstandHeaderElementsTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "examineMustUnderstandHeaderElementsTest1: examine MustUnderstand");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineMustUnderstandHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("examineMustUnderstandHeaderElementsTest1 failed");
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
  @Test
  public void examineMustUnderstandHeaderElementsTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "examineMustUnderstandHeaderElementsTest2: examine MustUnderstand");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineMustUnderstandHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("examineMustUnderstandHeaderElementsTest2 failed");
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
  @Test
  public void examineMustUnderstandHeaderElementsTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "examineMustUnderstandHeaderElementsTest3: examine MustUnderstand");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME",
            "examineMustUnderstandHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("examineMustUnderstandHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("examineMustUnderstandHeaderElementsTest3 failed");
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
  @Test
  public void extractAllHeaderElementsTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractAllHeaderElementsTest1: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractAllHeaderElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("extractAllHeaderElementsTest1 failed");
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
  @Test
  public void extractAllHeaderElementsTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractAllHeaderElementsTest2: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractAllHeaderElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("extractAllHeaderElementsTest2 failed");
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
  @Test
  public void extractAllHeaderElementsTest3() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"extractAllHeaderElementsTest3: extract SOAPHeaderElements");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPHEADER_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "extractAllHeaderElementsTest3");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("extractAllHeaderElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("extractAllHeaderElementsTest3 failed");
  }
}
