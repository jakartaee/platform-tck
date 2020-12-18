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

package com.sun.ts.tests.saaj.ee.RestrictionsAllowables;

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

  private static final String SERVLET = "/RestrictionsAllowables_web/RestrictionsAllowablesServlet";

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
      throw new Fault("setup failed: ", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed");
    }
    logMsg("setup ok");
  }

  /*
   * @testName: encodingStyleAttrSOAP11Test1
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify encodingStyle attribute can be set on
   * Envelope. This is allowed for SOAP1.1 protocol.
   *
   */
  public void encodingStyleAttrSOAP11Test1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("encodingStyleAttrSOAP11Test1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "encodingStyleAttrSOAP11Test1");
      props.setProperty("SOAPVERSION", "soap11");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("encodingStyleAttrSOAP11Test1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("encodingStyleAttrSOAP11Test1 failed", e);
    }
  }

  /*
   * @testName: encodingStyleAttrSOAP11Test2
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify encodingStyle attribute can be set on
   * Envelope. This is allowed for SOAP1.1 protocol.
   *
   */
  public void encodingStyleAttrSOAP11Test2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("encodingStyleAttrSOAP11Test2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "encodingStyleAttrSOAP11Test2");
      props.setProperty("SOAPVERSION", "soap11");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("encodingStyleAttrSOAP11Test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("encodingStyleAttrSOAP11Test2 failed", e);
    }
  }

  /*
   * @testName: noTrailingBlockBodySOAP11Test
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify trailing blocks are allowed after Body. This
   * is allowed for SOAP1.1 protocol.
   *
   */
  public void noTrailingBlockBodySOAP11Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("noTrailingBlockBodySOAP11Test");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "noTrailingBlockBodySOAP11Test");
      props.setProperty("SOAPVERSION", "soap11");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("noTrailingBlockBodySOAP11Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("noTrailingBlockBodySOAP11Test failed", e);
    }
  }

  /*
   * @testName: enforcedQNameBodyElemSOAP11Test
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify non qualified QNames are not enforced on
   * BodyElements. This is allowed for SOAP1.1 protocol.
   *
   */
  public void enforcedQNameBodyElemSOAP11Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("enforcedQNameBodyElemSOAP11Test");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "enforcedQNameBodyElemSOAP11Test");
      props.setProperty("SOAPVERSION", "soap11");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("enforcedQNameBodyElemSOAP11Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("enforcedQNameBodyElemSOAP11Test failed", e);
    }
  }

  /*
   * @testName: encodingStyleAttrSOAP12Test1
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify encodingStyle attribute cannot be set on
   * Envelope. This is restricted for SOAP1.2 protocol.
   *
   */
  public void encodingStyleAttrSOAP12Test1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("encodingStyleAttrSOAP12Test1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "encodingStyleAttrSOAP12Test1");
      props.setProperty("SOAPVERSION", "soap12");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("encodingStyleAttrSOAP12Test1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("encodingStyleAttrSOAP12Test1 failed", e);
    }
  }

  /*
   * @testName: encodingStyleAttrSOAP12Test2
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify encodingStyle attribute cannot be set on
   * Envelope. This is restricted for SOAP1.2 protocol.
   *
   */
  public void encodingStyleAttrSOAP12Test2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("encodingStyleAttrSOAP12Test2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "encodingStyleAttrSOAP12Test2");
      props.setProperty("SOAPVERSION", "soap12");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("encodingStyleAttrSOAP12Test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("encodingStyleAttrSOAP12Test2 failed", e);
    }
  }

  /*
   * @testName: noTrailingBlockBodySOAP12Test
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify no trailing blocks allowed after Body. This
   * is restricted for SOAP1.2 protocol.
   *
   */
  public void noTrailingBlockBodySOAP12Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("noTrailingBlockBodySOAP12Test");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "noTrailingBlockBodySOAP12Test");
      props.setProperty("SOAPVERSION", "soap12");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("noTrailingBlockBodySOAP12Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("noTrailingBlockBodySOAP12Test failed", e);
    }
  }

  /*
   * @testName: enforcedQNameHdrElemTest1
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify that unqualified QNames are enforced on
   * HeaderElements. This is restricted for for both SOAP1.1 and SOAP1.2
   * protocol.
   *
   */
  public void enforcedQNameHdrElemTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("enforcedQNameHdrElemTest1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "enforcedQNameHdrElemTest1");
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
      if (!pass)
        throw new Fault("enforcedQNameHdrElemTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("enforcedQNameHdrElemTest1 failed", e);
    }
  }

  /*
   * @testName: enforcedQNameHdrElemTest2
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify that unqualified QNames are enforced on
   * ChildElements of Headers. This is restricted for both SOAP1.1 and SOAP1.2
   * protocol.
   */
  public void enforcedQNameHdrElemTest2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("enforcedQNameHdrElemTest2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "enforcedQNameHdrElemTest2");
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

      if (!pass)
        throw new Fault("enforcedQNameHdrElemTest2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("enforcedQNameHdrElemTest2 failed", e);
    }
  }

  /*
   * @testName: enforcedQNameBodyElemSOAP12Test
   *
   * @assertion_ids: SAAJ:SPEC:22;
   *
   * @test_Strategy: Test to verify non qualified QNames are not enforced on
   * BodyElements. This is restricted for SOAP1.2 protocol.
   *
   */
  public void enforcedQNameBodyElemSOAP12Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("enforcedQNameBodyElemSOAP12Test");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
      TestUtil.logMsg(url.toString());
      props.setProperty("TESTNAME", "enforcedQNameBodyElemSOAP12Test");
      props.setProperty("SOAPVERSION", "soap12");
      TestUtil.logMsg("Sending post request to test servlet.....");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;

      if (!pass)
        throw new Fault("enforcedQNameBodyElemSOAP12Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("enforcedQNameBodyElemSOAP12Test failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
