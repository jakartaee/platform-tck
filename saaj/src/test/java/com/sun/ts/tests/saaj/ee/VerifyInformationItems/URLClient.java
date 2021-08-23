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

package com.sun.ts.tests.saaj.ee.VerifyInformationItems;

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

  private static final String VERIFYINFORMATIONITEMS_TESTSERVLET = "/VerifyInformationItems_web/VerifyInformationItemsTestServlet";

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
   * @testName: VerifyEncodingStyleAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP encodingStyle attribute information item.
   *
   */
  public void VerifyEncodingStyleAttributeInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyEncodingStyleAttributeInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyEncodingStyleAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyEncodingStyleAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyRoleAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Role attribute information item.
   *
   */
  public void VerifyRoleAttributeInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyRoleAttributeInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyRoleAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyRoleAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyRelayAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Relay attribute information item.
   *
   */
  public void VerifyRelayAttributeInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyRelayAttributeInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyRelayAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyRelayAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyMustUnderstandAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP mustUnderstand attribute information item.
   *
   */
  public void VerifyMustUnderstandAttributeInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyMustUnderstandAttributeInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyMustUnderstandAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyMustUnderstandAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyEnvelopeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Envelope element information item.
   *
   */
  public void VerifyEnvelopeElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyEnvelopeElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyEnvelopeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyEnvelopeElementInfoItem failed");
  }

  /*
   * @testName: VerifyHeaderElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Header element information item.
   *
   */
  public void VerifyHeaderElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyHeaderElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyHeaderElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyHeaderElementInfoItem failed");
  }

  /*
   * @testName: VerifyBodyElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Body element information item.
   *
   */
  public void VerifyBodyElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyBodyElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyBodyElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyBodyElementInfoItem failed");
  }

  /*
   * @testName: VerifyBodyChildElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Body Child element information item.
   *
   */
  public void VerifyBodyChildElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyBodyChildElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyBodyChildElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyBodyChildElementInfoItem failed");
  }

  /*
   * @testName: VerifyFaultElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Fault element information item.
   *
   */
  public void VerifyFaultElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyFaultElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyFaultElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyFaultElementInfoItem failed");
  }

  /*
   * @testName: VerifyCodeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Fault Code element information item.
   *
   */
  public void VerifyCodeElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyCodeElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyCodeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyCodeElementInfoItem failed");
  }

  /*
   * @testName: VerifySubcodeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Fault Subcode element information item.
   *
   */
  public void VerifySubcodeElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifySubcodeElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifySubcodeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifySubcodeElementInfoItem failed");
  }

  /*
   * @testName: VerifyDetailElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Detail element information item.
   *
   */
  public void VerifyDetailElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyDetailElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyDetailElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyDetailElementInfoItem failed");
  }

  /*
   * @testName: VerifyUpgradeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Upgrade element information item.
   *
   */
  public void VerifyUpgradeElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyUpgradeElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyUpgradeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyUpgradeElementInfoItem failed");
  }

  /*
   * @testName: VerifyNotUnderstoodElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the NotUnderstood element information item.
   *
   */
  public void VerifyNotUnderstoodElementInfoItem() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          VERIFYINFORMATIONITEMS_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "VerifyNotUnderstoodElementInfoItem");
      props.setProperty("SOAPVERSION", "soap12");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("VerifyNotUnderstoodElementInfoItem failed", e);
    }

    if (!pass)
      throw new Fault("VerifyNotUnderstoodElementInfoItem failed");
  }
}
