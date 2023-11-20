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

  private static final String TESTSERVLET = "/SOAPFault_web/SOAPFaultTestServlet";

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
   * @testName: SetGetFaultString1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:200; SAAJ:JAVADOC:201; SAAJ:JAVADOC:204
   *
   * @test_Strategy: Call SOAPFault.setFaultString/getFaultString with a string.
   * Must succeed.
   *
   */
  @Test
  public void SetGetFaultString1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultString1Test");
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
      throw new Exception("SetGetFaultString1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultString1Test failed");
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
  @Test
  public void SetGetFaultCode1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultCode1Test");
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
      throw new Exception("SetGetFaultCode1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultCode1Test failed");
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
  @Test
  public void SetGetFaultActor1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetFaultActor1Test");
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
      throw new Exception("setFaultActor1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultActor1Test failed");
  }

  /*
   * @testName: AddGetDetail1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:207; SAAJ:JAVADOC:208;
   *
   * @test_Strategy: Call SOAPFault.addDetail/getDetail
   *
   */
  @Test
  public void AddGetDetail1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "AddGetDetail1Test");
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
      throw new Exception("AddGetDetail1Test failed", e);
    }
    if (!pass)
      throw new Exception("AddGetDetail1Test failed");
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
  @Test
  public void addFaultReasonText1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultReasonText1Test failed", e);
    }

    if (!pass)
      throw new Exception("addFaultReasonText1Test failed");
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
  @Test
  public void addFaultReasonText2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultReasonText2Test failed", e);
    }

    if (!pass)
      throw new Exception("addFaultReasonText2Test failed");
  }

  /*
   * @testName: addFaultReasonText3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() using 2 different
   * locales. See that both are returned on a SOAP1.2 message.
   */
  @Test
  public void addFaultReasonText3Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultReasonText3Test failed", e);
    }

    if (!pass)
      throw new Exception("addFaultReasonText3Test failed");
  }

  /*
   * @testName: addFaultReasonText4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:216; SAAJ:JAVADOC:217;
   *
   * @test_Strategy: Call SOAPFault.addFaultReasonText() using the same locale.
   * See that the first is overwritten by the second on a SOAP1.2 message.
   */
  @Test
  public void addFaultReasonText4Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "addFaultReasonText4Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addFaultReasonText4Test failed", e);
    }

    if (!pass)
      throw new Exception("addFaultReasonText4Test failed");
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
  @Test
  public void getFaultReasonLocales1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonLocales1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonLocales1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonLocales1Test failed");
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
  @Test
  public void getFaultReasonLocales2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonLocales2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonLocales2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonLocales2Test failed");
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
  @Test
  public void getFaultReasonText1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonText1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonText1Test failed");
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
  @Test
  public void getFaultReasonText2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonText2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonText2Test failed");
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
  @Test
  public void getFaultReasonText3Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonText3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonText3Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonText3Test failed");
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
  @Test
  public void getFaultReasonTexts1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonTexts1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonTexts1Test failed");
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
  @Test
  public void getFaultReasonTexts2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonTexts2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonTexts2Test failed");
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
  @Test
  public void getFaultReasonTexts3Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultReasonTexts3Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultReasonTexts3Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultReasonTexts3Test failed");
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
  @Test
  public void setFaultNode1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Exception("setFaultNode1Test failed");
  }

  /*
   * @testName: SetGetFaultNode1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218; SAAJ:JAVADOC:219;
   *
   * @test_Strategy: Call SOAPFault.setFaultNode() and getFaultNode() on a
   * SOAP1.2 message must succeed.
   */
  @Test
  public void SetGetFaultNode1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultNode1Test failed");
  }

  /*
   * @testName: SetGetFaultNode2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:218; SAAJ:JAVADOC:219;
   *
   * @test_Strategy: Call SOAPFault.setFaultNode() twice on a SOAP1.2 message.
   */
  @Test
  public void SetGetFaultNode2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultNode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultNode2Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultNode2Test failed");
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
  @Test
  public void getFaultNode1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultNode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultNode1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultNode1Test failed");
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
  @Test
  public void getFaultNode2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultNode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultNode2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultNode2Test failed");
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
  @Test
  public void setFaultRole1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Exception("setFaultRole1Test failed");
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
  @Test
  public void SetGetFaultRole1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "SetGetFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultRole1Test failed");
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
  @Test
  public void getFaultRole1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultRole1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultRole1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultRole1Test failed");
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
  @Test
  public void getFaultRole2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultRole2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultRole2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultRole2Test failed");
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
  @Test
  public void SetGetFaultStringLocale1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultStringLocale1Test");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultStringLocale1Test failed");
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
  @Test
  public void setFaultStringLocale1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "setFaultStringLocale1Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass")) {
        pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Exception("setFaultStringLocale1Test failed");
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
  @Test
  public void getFaultStringLocale1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0) {
          props.setProperty("SOAPVERSION", "soap11");
          props.setProperty("TESTNAME", "getFaultStringLocale1SOAP11Test");
        } else {
          props.setProperty("SOAPVERSION", "soap12");
          props.setProperty("TESTNAME", "getFaultStringLocale1SOAP12Test");
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
      throw new Exception("getFaultStringLocale1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultStringLocale1Test failed");
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
  @Test
  public void SetGetFaultCodeAsName1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultCodeAsName1Test");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultCodeAsName1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultCodeAsName1Test failed");
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
  @Test
  public void SetGetFaultCodeAsQName1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "SetGetFaultCodeAsQName1Test");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("SetGetFaultCodeAsQName1Test failed", e);
    }

    if (!pass)
      throw new Exception("SetGetFaultCodeAsQName1Test failed");
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
  @Test
  public void appendFaultSubcode1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "appendFaultSubcode1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("appendFaultSubcode1Test failed", e);
    }

    if (!pass)
      throw new Exception("appendFaultSubcode1Test failed");
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
  @Test
  public void appendFaultSubcode2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "appendFaultSubcode2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("appendFaultSubcode2Test failed", e);
    }

    if (!pass)
      throw new Exception("appendFaultSubcode2Test failed");
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
  @Test
  public void getFaultSubcodes1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultSubcodes1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultSubcodes1Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultSubcodes1Test failed");
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
  @Test
  public void getFaultSubcodes2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getFaultSubcodes2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getFaultSubcodes2Test failed", e);
    }

    if (!pass)
      throw new Exception("getFaultSubcodes2Test failed");
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
  @Test
  public void hasDetail1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "hasDetail1Test");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("hasDetail1Test failed", e);
    }

    if (!pass)
      throw new Exception("hasDetail1Test failed");
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
  @Test
  public void hasDetail2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "hasDetail2Test");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("hasDetail2Test failed", e);
    }

    if (!pass)
      throw new Exception("hasDetail2Test failed");
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
  @Test
  public void removeAllFaultSubcodes1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "removeAllFaultSubcodes1Test");
      props.setProperty("SOAPVERSION", "soap11");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeAllFaultSubcodes1Test failed", e);
    }

    if (!pass)
      throw new Exception("removeAllFaultSubcodes1Test failed");
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
  @Test
  public void removeAllFaultSubcodes2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "removeAllFaultSubcodes2Test");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeAllFaultSubcodes2Test failed", e);
    }

    if (!pass)
      throw new Exception("removeAllFaultSubcodes2Test failed");
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
  @Test
  public void SetFaultCodeNameSOAPExceptionTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeNameSOAPExceptionTest");
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
      throw new Exception("SetFaultCodeNameSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Exception("SetFaultCodeNameSOAPExceptionTest failed");
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
  @Test
  public void SetFaultCodeQNameSOAPExceptionTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeQNameSOAPExceptionTest");
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
      throw new Exception("SetFaultCodeQNameSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Exception("SetFaultCodeQNameSOAPExceptionTest failed");
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
  @Test
  public void SetFaultCodeStringSOAPExceptionTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetFaultCodeStringSOAPExceptionTest");
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
      throw new Exception("SetFaultCodeStringSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Exception("SetFaultCodeStringSOAPExceptionTest failed");
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
  @Test
  public void AppendFaultSubcodeSOAPExceptionTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "AppendFaultSubcodeSOAPExceptionTest");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("AppendFaultSubcodeSOAPExceptionTest failed", e);
    }

    if (!pass)
      throw new Exception("AppendFaultSubcodeSOAPExceptionTest failed");
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
  @Test
  public void AddDetailSOAPExceptionTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "AddDetailSOAPExceptionTest");
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
      throw new Exception("AddDetailSOAPExceptionTest failed", e);
    }
    if (!pass)
      throw new Exception("AddDetailSOAPExceptionTest failed");
  }
}
