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

import org.junit.jupiter.api.Test;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

public class URLClient {
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
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
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
  @Test
  public void VerifyEncodingStyleAttributeInfoItem() throws Exception {
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
      throw new Exception("VerifyEncodingStyleAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyEncodingStyleAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyRoleAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Role attribute information item.
   *
   */
  public void VerifyRoleAttributeInfoItem() throws Exception {
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
      throw new Exception("VerifyRoleAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyRoleAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyRelayAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Relay attribute information item.
   *
   */
  @Test
  public void VerifyRelayAttributeInfoItem() throws Exception {
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
      throw new Exception("VerifyRelayAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyRelayAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyMustUnderstandAttributeInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP mustUnderstand attribute information item.
   *
   */
  @Test
  public void VerifyMustUnderstandAttributeInfoItem() throws Exception {
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
      throw new Exception("VerifyMustUnderstandAttributeInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyMustUnderstandAttributeInfoItem failed");
  }

  /*
   * @testName: VerifyEnvelopeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Envelope element information item.
   *
   */
  @Test
  public void VerifyEnvelopeElementInfoItem() throws Exception {
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
      throw new Exception("VerifyEnvelopeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyEnvelopeElementInfoItem failed");
  }

  /*
   * @testName: VerifyHeaderElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Header element information item.
   *
   */
  @Test
  public void VerifyHeaderElementInfoItem() throws Exception {
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
      throw new Exception("VerifyHeaderElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyHeaderElementInfoItem failed");
  }

  /*
   * @testName: VerifyBodyElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Body element information item.
   *
   */
  @Test
  public void VerifyBodyElementInfoItem() throws Exception {
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
      throw new Exception("VerifyBodyElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyBodyElementInfoItem failed");
  }

  /*
   * @testName: VerifyBodyChildElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Body Child element information item.
   *
   */
  @Test
  public void VerifyBodyChildElementInfoItem() throws Exception {
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
      throw new Exception("VerifyBodyChildElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyBodyChildElementInfoItem failed");
  }

  /*
   * @testName: VerifyFaultElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the SOAP Fault element information item.
   *
   */
  @Test
  public void VerifyFaultElementInfoItem() throws Exception {
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
      throw new Exception("VerifyFaultElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyFaultElementInfoItem failed");
  }

  /*
   * @testName: VerifyCodeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Fault Code element information item.
   *
   */
  @Test
  public void VerifyCodeElementInfoItem() throws Exception {
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
      throw new Exception("VerifyCodeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyCodeElementInfoItem failed");
  }

  /*
   * @testName: VerifySubcodeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Fault Subcode element information item.
   *
   */
  @Test
  public void VerifySubcodeElementInfoItem() throws Exception {
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
      throw new Exception("VerifySubcodeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifySubcodeElementInfoItem failed");
  }

  /*
   * @testName: VerifyDetailElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Detail element information item.
   *
   */
  @Test
  public void VerifyDetailElementInfoItem() throws Exception {
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
      throw new Exception("VerifyDetailElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyDetailElementInfoItem failed");
  }

  /*
   * @testName: VerifyUpgradeElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the Upgrade element information item.
   *
   */
  @Test
  public void VerifyUpgradeElementInfoItem() throws Exception {
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
      throw new Exception("VerifyUpgradeElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyUpgradeElementInfoItem failed");
  }

  /*
   * @testName: VerifyNotUnderstoodElementInfoItem
   *
   * @assertion_ids: SAAJ:SPEC:21;
   *
   * @test_Strategy: Verify the NotUnderstood element information item.
   *
   */
  @Test
  public void VerifyNotUnderstoodElementInfoItem() throws Exception {
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
      throw new Exception("VerifyNotUnderstoodElementInfoItem failed", e);
    }

    if (!pass)
      throw new Exception("VerifyNotUnderstoodElementInfoItem failed");
  }
}
