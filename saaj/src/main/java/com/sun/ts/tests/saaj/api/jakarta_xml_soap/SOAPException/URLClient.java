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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPException;

import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

public class URLClient {
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String SOAPEXCEPTION_TESTSERVLET = "/SOAPException_web/SOAPExceptionTestServlet";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;
  
  private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());



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
   * @testName: SOAPExceptionConstructor1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:70;
   *
   * @test_Strategy: Call SOAPException() constructor and verify creation of a
   * new SOAPException object.
   *
   * Description: Create a SOAPException object
   *
   */
  @Test
  public void SOAPExceptionConstructor1Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "SOAPExceptionConstructor1Test: call SOAPException() constructor");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPEXCEPTION_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SOAPExceptionConstructor1Test");
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
      throw new Exception("SOAPExceptionConstructor1Test failed", e);
    }

    if (!pass)
      throw new Exception("SOAPExceptionConstructor1Test failed");
  }

  /*
   * @testName: SOAPExceptionConstructor2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:71;
   *
   * @test_Strategy: Call SOAPException(String) constructor and verify creation
   * of a new SOAPException object.
   *
   * Description: Create a SOAPException object
   *
   */
  @Test
  public void SOAPExceptionConstructor2Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "SOAPExceptionConstructor2Test: call SOAPException(String) constructor");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPEXCEPTION_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SOAPExceptionConstructor2Test");
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
      throw new Exception("SOAPExceptionConstructor2Test failed", e);
    }

    if (!pass)
      throw new Exception("SOAPExceptionConstructor2Test failed");
  }

  /*
   * @testName: SOAPExceptionConstructor3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:72;
   *
   * @test_Strategy: Call SOAPException(String, Throwable) constructor and
   * verify creation of a new SOAPException object.
   *
   * Description: Create a SOAPException object
   *
   */
  @Test
  public void SOAPExceptionConstructor3Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "SOAPExceptionConstructor3Test: call SOAPException(String, Throwable) constructor");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPEXCEPTION_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SOAPExceptionConstructor3Test");
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
      throw new Exception("SOAPExceptionConstructor3Test failed", e);
    }

    if (!pass)
      throw new Exception("SOAPExceptionConstructor3Test failed");
  }

  /*
   * @testName: SOAPExceptionConstructor4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:73;
   *
   * @test_Strategy: Call SOAPException(Throwable) constructor and verify
   * creation of a new SOAPException object.
   *
   * Description: Create a SOAPException object
   *
   */
  @Test
  public void SOAPExceptionConstructor4Test() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,
          "SOAPExceptionConstructor4Test: call SOAPException(String, Throwable) constructor");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPEXCEPTION_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SOAPExceptionConstructor4Test");
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
      throw new Exception("SOAPExceptionConstructor4Test failed", e);
    }

    if (!pass)
      throw new Exception("SOAPExceptionConstructor4Test failed");
  }

  /*
   * @testName: InitGetCauseTest
   *
   * @assertion_ids: SAAJ:JAVADOC:75; SAAJ:JAVADOC:76;
   *
   * @test_Strategy: Call the SOAPException.initCause(Throwable) method to set
   * the cause field to the Throwable object. Then call the
   * SOAPException.getCause() method to return the Throwable object embedded in
   * this SOAPException.
   *
   * Description: Initialize the cause field of this SOAPException with the
   * given Throwable object. Then retrieve the cause field of this SOAPException
   * to get the Throwable object.
   */
  @Test
  public void InitGetCauseTest() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"InitGetCauseTest: call SOAPException.initCause(Throwable)");
      logger.log(Logger.Level.INFO,"InitGetCauseTest: call SOAPException.getCause()");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPEXCEPTION_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "InitGetCauseTest");
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
      throw new Exception("InitGetCauseTest failed", e);
    }

    if (!pass)
      throw new Exception("InitGetCauseTest failed");
  }
}
