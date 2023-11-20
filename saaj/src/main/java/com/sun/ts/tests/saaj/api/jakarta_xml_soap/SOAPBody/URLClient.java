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

  private static final String SOAPBODY_TESTSERVLET = "/SOAPBody_web/SOAPBodyTestServlet";

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
 		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPBody_web.war");
 		archive.addPackages(false, Filters.exclude(URLClient.class),
 				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPBody");
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
  @Test
  public void addBodyElementTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addBodyElementTest1: add SOAPBodyElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addBodyElementTest1");
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
      throw new Exception("addBodyElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addBodyElementTest1 failed");
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
  @Test
  public void addBodyElementTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addBodyElementTest2: add SOAPBodyElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addBodyElementTest2");
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
      throw new Exception("addBodyElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addBodyElementTest2 failed");
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
  @Test
  public void addFaultTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addFaultTest1: add SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest1");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addFaultTest1 failed");
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
  @Test
  public void addFaultTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addFaultTest2: add SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest2");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addFaultTest2 failed");
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
  @Test
  public void addFaultTest3() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addFaultTest3: add SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest3");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultTest3 failed", e);
    }

    if (!pass)
      throw new Exception("addFaultTest3 failed");
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
  @Test
  public void addFaultTest4() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addFaultTest4: add SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest4");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultTest4 failed", e);
    }

    if (!pass)
      throw new Exception("addFaultTest4 failed");
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
  @Test
  public void addFaultTest5() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addFaultTest5: add SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultTest5");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultTest5 failed", e);
    }

    if (!pass)
      throw new Exception("addFaultTest5 failed");
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
  @Test
  public void getFaultTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getFaultTest: get SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getFaultTest");
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
      throw new Exception("getFaultTest failed", e);
    }

    if (!pass)
      throw new Exception("getFaultTest failed");
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
  @Test
  public void hasFaultTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"hasFaultTest: has SOAPFault object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "hasFaultTest");
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
      throw new Exception("hasFaultTest failed", e);
    }

    if (!pass)
      throw new Exception("hasFaultTest failed");
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
  @Test
  public void addDocumentTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addDocumentTest: add SOAPBodyElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "addDocumentTest");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addDocumentTest failed", e);
    }

    if (!pass)
      throw new Exception("addDocumentTest failed");
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
  @Test
  public void extractContentAsDocumentTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "extractContentAsDocumentTest1: extract content as DOM Document");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractContentAsDocumentTest1");
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
      throw new Exception("extractContentAsDocumentTest1 failed", e);
    }

    if (!pass)
      throw new Exception("extractContentAsDocumentTest1 failed");
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
  @Test
  public void extractContentAsDocumentTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "extractContentAsDocumentTest2: negative test case for SOAPException");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPBODY_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "extractContentAsDocumentTest2");
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
      throw new Exception("extractContentAsDocumentTest2 failed", e);
    }

    if (!pass)
      throw new Exception("extractContentAsDocumentTest2 failed");
  }
}
