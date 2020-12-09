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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SAAJResult;

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

  private static final String SAAJRESULT_TESTSERVLET = "/SAAJResult_web/SAAJResultTestServlet";

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
   * @testName: SAAJResultConstructorTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:92;
   *
   * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
   * SAAJResult object.
   *
   * Description: Create a SAAJResult object.
   *
   */
  public void SAAJResultConstructorTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("SAAJResultConstructorTest1: create SAAJResult object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SAAJResultConstructorTest1");
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
      throw new Fault("SAAJResultConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("SAAJResultConstructorTest1 failed");
  }

  /*
   * @testName: SAAJResultConstructorTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:95;
   *
   * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
   * SAAJResult object.
   *
   * Description: Create a SAAJResult object.
   *
   */
  public void SAAJResultConstructorTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("SAAJResultConstructorTest2: create SAAJResult object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SAAJResultConstructorTest2");
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
      throw new Fault("SAAJResultConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("SAAJResultConstructorTest2 failed");
  }

  /*
   * @testName: SAAJResultConstructorTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:94;
   *
   * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
   * SAAJResult object.
   *
   * Description: Create a SAAJResult object.
   *
   */
  public void SAAJResultConstructorTest3() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("SAAJResultConstructorTest3: create SAAJResult object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SAAJResultConstructorTest3");
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
      throw new Fault("SAAJResultConstructorTest3 failed", e);
    }

    if (!pass)
      throw new Fault("SAAJResultConstructorTest3 failed");
  }

  /*
   * @testName: SAAJResultConstructorTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:93;
   *
   * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
   * SAAJResult object.
   *
   * Description: Create a SAAJResult object.
   *
   */
  public void SAAJResultConstructorTest4() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("SAAJResultConstructorTest4: create SAAJResult object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SAAJResultConstructorTest4");
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
      throw new Fault("SAAJResultConstructorTest4 failed", e);
    }

    if (!pass)
      throw new Fault("SAAJResultConstructorTest4 failed");
  }

  /*
   * @testName: getResultTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:96;
   *
   * @test_Strategy: Call getResultTest1() and verify a result object is
   * returned.
   *
   * Description: Get a Node object.
   *
   */
  public void getResultTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getResultTest1: get result object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getResultTest1");
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
      throw new Fault("getResultTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getResultTest1 failed");
  }

  /*
   * @testName: getResultTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:96;
   *
   * @test_Strategy: Call getResultTest2() and verify a result object is
   * returned.
   *
   * Description: Get a Node object.
   *
   */
  public void getResultTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getResultTest2: get result object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getResultTest2");
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
      throw new Fault("getResultTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getResultTest2 failed");
  }
}
