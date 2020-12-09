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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnection;

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

  private static final String SOAPCONNECTION_TESTSERVLET = "/SOAPConnection_web/SOAPConnectionTestServlet";

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
   * @testName: closeTest
   *
   * @assertion_ids: SAAJ:JAVADOC:90; SAAJ:JAVADOC:91;
   *
   * @test_Strategy: Call SOAPConnection.close().
   *
   * Description: Closes this SOAPConntection object.
   *
   */
  public void closeTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("closeTest: close a SOAPConnection object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONNECTION_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "closeTest");
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
      throw new Fault("closeTest failed", e);
    }

    if (!pass)
      throw new Fault("closeTest failed");
  }

  /*
   * @testName: callTest
   *
   * @assertion_ids: SAAJ:JAVADOC:86; SAAJ:JAVADOC:87;
   *
   * @test_Strategy: Call SOAPConnection.call().
   *
   * Description: Sends the given message to the specified endpoint and blocks
   * until it has returned the response.
   *
   */
  public void callTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("callTest: send SOAPMessage and block for reply");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONNECTION_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "callTest");
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
      throw new Fault("callTest failed", e);
    }

    if (!pass)
      throw new Fault("callTest failed");
  }

  /*
   * @testName: getTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call SOAPConnection.get().
   *
   * Description: Gets a SOAP response message from a specific endpoint and
   * blocks until it has received the response. HTTP-GET from a valid endpoint
   * that contains a valid webservice resource should succeed. The endpoint
   * tested contains a valid webservice resource that must return a SOAP
   * response. HTTP-GET must succeed.
   *
   */
  public void getTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getTest1: Get SOAPMessage from valid endpoint");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONNECTION_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getTest1");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getTest1 failed");
  }

  /*
   * @testName: getTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call SOAPConnection.get().
   *
   * Description: Gets a SOAP response message from a specific endpoint and
   * blocks until it has received the response. HTTP-GET from a valid endpoint
   * that does not contain a valid webservice resource should fail with a
   * SOAPExcpetion. The endpoint tested does not contain a valid webservice
   * resource but contains an html resource. HTTP-GET must throw a
   * SOAPException.
   *
   */
  public void getTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getTest2: Get SOAPMessage from valid endpoint");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONNECTION_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getTest2");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getTest2 failed");
  }

  /*
   * @testName: getTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call SOAPConnection.get().
   *
   * Description: Gets a message from a specific endpoint and blocks until it
   * has received a response. HTTP-GET from an invalid endpoint (no such host)
   * must throw a SOAPException. Endpoint does not exist.
   * 
   */
  public void getTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getTest3: Get SOAPMessage from an invalid endpoint");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPCONNECTION_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "getTest3");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getTest3 failed", e);
    }

    if (!pass)
      throw new Fault("getTest3 failed");
  }
}
