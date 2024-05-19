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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeaderElement;

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

  private static final String SOAPHEADERELEMENT_TESTSERVLET = "/SOAPHeaderElement_web/SOAPHeaderElementTestServlet";

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
   * @testName: setMustUnderstandTrueTest
   *
   * @assertion_ids: SAAJ:JAVADOC:162;
   *
   * @test_Strategy: Call SOAPHeaderElement.setMustUnderstand(true) method and
   * verify mustunderstand attribute is set to true
   *
   * Description: Set the mustunderstand attribute associated with the soap
   * header element to true
   *
   */
  @Test
  public void setMustUnderstandTrueTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("setMustUnderstandTrueTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMustUnderstandTrueTest");
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
      throw new Exception("setMustUnderstandTrueTest failed", e);
    }

    if (!pass)
      throw new Exception("setMustUnderstandTrueTest failed");
  }

  /*
   * @testName: setMustUnderstandFalseTest
   *
   * @assertion_ids: SAAJ:JAVADOC:162;
   *
   * @test_Strategy: Call SOAPHeaderElement.setMustUnderstand(false) method and
   * verify mustunderstand attribute is set to false
   *
   * Description: Set the mustunderstand attribute associated with the soap
   * header element to false
   *
   */
  @Test
  public void setMustUnderstandFalseTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("setMustUnderstandFalseTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMustUnderstandFalseTest");
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
      throw new Exception("setMustUnderstandFalseTest failed", e);
    }

    if (!pass)
      throw new Exception("setMustUnderstandFalseTest failed");
  }

  /*
   * @testName: getMustUnderstandTrueTest
   *
   * @assertion_ids: SAAJ:JAVADOC:163;
   *
   * @test_Strategy: Call SOAPHeaderElement.getMustUnderstand() method and
   * verify mustunderstand attribute is set to true
   *
   * Description: Get the mustunderstand attribute associated with the soap
   * header element when it is true
   *
   */
  @Test
  public void getMustUnderstandTrueTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("getMustUnderstandTrueTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMustUnderstandTrueTest");
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
      throw new Exception("getMustUnderstandTrueTest failed", e);
    }

    if (!pass)
      throw new Exception("getMustUnderstandTrueTest failed");
  }

  /*
   * @testName: getMustUnderstandFalseTest
   *
   * @assertion_ids: SAAJ:JAVADOC:163;
   *
   * @test_Strategy: Call SOAPHeaderElement.getMustUnderstand() method and
   * verify mustunderstand attribute is set to false
   *
   * Description: Get the mustunderstand attribute associated with the soap
   * header element when it is false
   *
   */
  @Test
  public void getMustUnderstandFalseTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("getMustUnderstandFalseTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMustUnderstandFalseTest");
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
      throw new Exception("getMustUnderstandFalseTest failed", e);
    }

    if (!pass)
      throw new Exception("getMustUnderstandFalseTest failed");
  }

  /*
   * @testName: setActorTest
   *
   * @assertion_ids: SAAJ:JAVADOC:157;
   *
   * @test_Strategy: Call SOAPHeaderElement.setActor(actor) method and verify
   * actor associated is set properly.
   *
   * Description: Set the actor associated with the soap header element. For a
   * SOAP1.1 or SOAP1.2 message this should succeed.
   *
   */
  @Test
  public void setActorTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("setActorTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "setActorTest");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "setActorTest");
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
      throw new Exception("setActorTest failed", e);
    }

    if (!pass)
      throw new Exception("setActorTest failed");
  }

  /*
   * @testName: getActorTest
   *
   * @assertion_ids: SAAJ:JAVADOC:160;
   *
   * @test_Strategy: Call SOAPHeaderElement.getActor() method and verify actor
   * associated is set properly.
   *
   * Description: Get the actor associated with the soap header element. For a
   * SOAP1.1 or SOAP1.2 message this should succeed.
   *
   */
  @Test
  public void getActorTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("getActorTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "getActorTest");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "getActorTest");
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
      throw new Exception("getActorTest failed", e);
    }

    if (!pass)
      throw new Exception("getActorTest failed");
  }

  /*
   * @testName: setRoleTest
   *
   * @assertion_ids: SAAJ:JAVADOC:158; SAAJ:JAVADOC:159;
   *
   * @test_Strategy: Call SOAPHeaderElement.setRole(role) method and verify role
   * associated is set properly.
   *
   * Description: Set the role associated with the soap header element. For a
   * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
   * UnsupportedOperation- Exception.
   *
   */
  @Test
  public void setRoleTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("setRoleTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "setRoleSOAP11Test");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "setRoleSOAP12Test");
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
      throw new Exception("setRoleTest failed", e);
    }

    if (!pass)
      throw new Exception("setRoleTest failed");
  }

  /*
   * @testName: getRoleTest
   *
   * @assertion_ids: SAAJ:JAVADOC:161;
   *
   * @test_Strategy: Call SOAPHeaderElement.getRole() method and verify role
   * associated is set properly.
   *
   * Description: Get the role associated with the soap header element. For a
   * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
   * UnsupportedOperation- Exception.
   *
   */
  @Test
  public void getRoleTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("getRoleTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "getRoleSOAP11Test");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "getRoleSOAP12Test");
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
      throw new Exception("getRoleTest failed", e);
    }

    if (!pass)
      throw new Exception("getRoleTest failed");
  }

  /*
   * @testName: setRelayTest
   *
   * @assertion_ids: SAAJ:JAVADOC:164; SAAJ:JAVADOC:165;
   *
   * @test_Strategy: Call SOAPHeaderElement.setRelay(relay) method and verify
   * relay attribute is set properly.
   *
   * Description: Set the relay attribute for this soap header element. For a
   * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
   * UnsupportedOperation- Exception.
   *
   */
  @Test
  public void setRelayTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("setRelayTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "setRelaySOAP11Test");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "setRelaySOAP12Test");
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
      throw new Exception("setRelayTest failed", e);
    }

    if (!pass)
      throw new Exception("setRelayTest failed");
  }

  /*
   * @testName: getRelayTest
   *
   * @assertion_ids: SAAJ:JAVADOC:166;
   *
   * @test_Strategy: Call SOAPHeaderElement.getRelay() method and verify relay
   * attribute is set properly.
   *
   * Description: Get the relay attribute for this soap header element. For a
   * SOAP1.2 message this should succeed and for a SOAP1.1 message it must throw
   * UnsupportedOperation- Exception.
   *
   */
  @Test
  public void getRelayTest() throws Exception {
    boolean pass = true;
    try {
      TestUtil.logMsg("getRelayTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPHEADERELEMENT_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "getRelaySOAP11Test");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "getRelaySOAP12Test");
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
      throw new Exception("getRelayTest failed", e);
    }

    if (!pass)
      throw new Exception("getRelayTest failed");
  }
}
