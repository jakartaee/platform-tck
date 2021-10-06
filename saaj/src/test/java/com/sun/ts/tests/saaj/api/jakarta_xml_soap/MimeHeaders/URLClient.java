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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MimeHeaders;

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

  private static final String TESTSERVLET = "/MimeHeaders_web/MimeHeadersTestServlet";

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
   * @testName: addHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Add a single header.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader1Test");
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
        throw new Fault("addHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader1Test failed", e);
    }
  }

  /*
   * @testName: addHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Add two headers.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader2Test");
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
        throw new Fault("addHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader2Test failed", e);
    }
  }

  /*
   * @testName: addHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Add two headers that have different
   * values.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader3Test");
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
        throw new Fault("addHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader3Test failed", e);
    }
  }

  /*
   * @testName: addHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Attempt to add an empty header and
   * non-empty value.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader4Test");
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
        throw new Fault("addHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader4Test failed", e);
    }
  }

  /*
   * @testName: addHeader5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Attempt to add a non-empty header and
   * empty value.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader5Test");
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
        throw new Fault("addHeader5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader5Test failed", e);
    }
  }

  /*
   * @testName: addHeader6Test
   *
   * @assertion_ids: SAAJ:JAVADOC:100;
   *
   * @test_Strategy: Call MimeHeaders.addHeader(String,String) method and verify
   * creation of a new MimeHeaders object. Attempt to add a null header and null
   * value.
   *
   * Description: Construct a MimeHeaders object.
   */
  public void addHeader6Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addHeader6Test");
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
        throw new Fault("addHeader6Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addHeader6Test failed", e);
    }
  }

  /*
   * @testName: getHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:98;
   *
   * @test_Strategy: Call MimeHeaders.getHeader(String) method and verify return
   * of a the MimeHeaders object. Get a single header.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getHeader1Test");
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
        throw new Fault("getHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getHeader1Test failed", e);
    }
  }

  /*
   * @testName: getHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:98;
   *
   * @test_Strategy: Call MimeHeaders.getHeader(String) method and verify return
   * of the MimeHeaders object. Get single header from multiple headers.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getHeader2Test");
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
        throw new Fault("getHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getHeader2Test failed", e);
    }
  }

  /*
   * @testName: getHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:98;
   *
   * @test_Strategy: Call MimeHeaders.getHeader(String) method and verify return
   * of the MimeHeaders object. Get header that contains two entries.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getHeader3Test");
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
        throw new Fault("getHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getHeader3Test failed", e);
    }
  }

  /*
   * @testName: getHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:98;
   *
   * @test_Strategy: Call MimeHeaders.getHeader(String) method and verify return
   * of the MimeHeaders object. Attempt to get a header that doesn't exist
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getHeader4Test");
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
        throw new Fault("getHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getHeader4Test failed", e);
    }
  }

  /*
   * @testName: getAllHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:103;
   *
   * @test_Strategy: Call MimeHeaders.getAllHeaders() method and verify return
   * of all MimeHeaders objects. Get single header.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getAllHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllHeaders1Test");
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
        throw new Fault("getAllHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getAllHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:103;
   *
   * @test_Strategy: Call MimeHeaders.getAllHeaders() method and verify return
   * of all MimeHeaders objects. Get multiple headers.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getAllHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllHeaders2Test");
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
        throw new Fault("getAllHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getAllHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:103;
   *
   * @test_Strategy: Call MimeHeaders.getAllHeaders() method and verify return
   * of all MimeHeaders objects. Get single header that contains mulitple
   * entries.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getAllHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllHeaders3Test");
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
        throw new Fault("getAllHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getAllHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:103;
   *
   * @test_Strategy: Call MimeHeaders.getAllHeaders() method and verify return
   * of all MimeHeaders objects. Attempt to get all headers when none exist.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getAllHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllHeaders4Test");
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
        throw new Fault("getAllHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllHeaders4Test failed", e);
    }
  }

  /*
   * @testName: removeAllHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:102;
   *
   * @test_Strategy: Call MimeHeaders.removeAllHeaders() method and verify
   * removal of all MimeHeaders objects. Remove single header.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeAllHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllHeaders1Test");
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
        throw new Fault("removeAllHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllHeaders1Test failed", e);
    }
  }

  /*
   * @testName: removeAllHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:102;
   *
   * @test_Strategy: Call MimeHeaders.removeAllHeaders() method and verify
   * removal of all MimeHeaders objects. Remove multiple headers.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeAllHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllHeaders2Test");
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
        throw new Fault("removeAllHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllHeaders2Test failed", e);
    }
  }

  /*
   * @testName: removeAllHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:102;
   *
   * @test_Strategy: Call MimeHeaders.removeAllHeaders() method and verify
   * removal of all MimeHeaders objects. Remove header that contains multiple
   * entries.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeAllHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllHeaders3Test");
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
        throw new Fault("removeAllHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllHeaders3Test failed", e);
    }
  }

  /*
   * @testName: removeAllHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:102;
   *
   * @test_Strategy: Call MimeHeaders.removeAllHeaders() method and verify
   * removal of all MimeHeaders objects. Remove headers when none exist.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeAllHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllHeaders4Test");
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
        throw new Fault("removeAllHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllHeaders4Test failed", e);
    }
  }

  /*
   * @testName: setHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(String,String) method and verify
   * return of a the MimeHeaders object. Set exist header.
   *
   * Description: Replace a MimeHeaders object.
   */
  public void setHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader1Test");
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
        throw new Fault("setHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader1Test failed", e);
    }
  }

  /*
   * @testName: setHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(String,String) method and verify
   * return of the MimeHeaders object. Set existing header from list of two
   * headers.
   *
   * Description: Replace a MimeHeaders object.
   */
  public void setHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader2Test");
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
        throw new Fault("setHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader2Test failed", e);
    }
  }

  /*
   * @testName: setHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(String,String) method and verify
   * return of the MimeHeaders object. Set existing header that contains
   * multiple values.
   *
   * Description: Replace/Construct a MimeHeaders object.
   */
  public void setHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader3Test");
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
        throw new Fault("setHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader3Test failed", e);
    }
  }

  /*
   * @testName: setHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(String,String) method and verify
   * return of the MimeHeaders object. Set header that doesn't exist
   *
   * Description: Replace/Construct a MimeHeaders object.
   */
  public void setHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader4Test");
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
        throw new Fault("setHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader4Test failed", e);
    }
  }

  /*
   * @testName: setHeader5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(Name,value) method and verify
   * return of the MimeHeaders object. Set an existing header twice.
   *
   * Description: Replace/Construct a MimeHeaders object.
   */
  public void setHeader5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader5Test");
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
        throw new Fault("setHeader5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader5Test failed", e);
    }
  }

  /*
   * @testName: setHeader6Test
   *
   * @assertion_ids: SAAJ:JAVADOC:99;
   *
   * @test_Strategy: Call MimeHeaders.setHeader(Name,emptyvalue|null) method and
   * verify return of the MimeHeaders object. Set an existing header twice.
   *
   * Description: Replace/Construct a MimeHeaders object.
   */
  public void setHeader6Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setHeader6Test");
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
        throw new Fault("setHeader6Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setHeader6Test failed", e);
    }
  }

  /*
   * @testName: removeHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:101;
   *
   * @test_Strategy: Call MimeHeaders.removeHeader(String) method and verify
   * return of a the MimeHeaders object. Remove single header.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeHeader1Test");
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
        throw new Fault("removeHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeHeader1Test failed", e);
    }
  }

  /*
   * @testName: removeHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:101;
   *
   * @test_Strategy: Call MimeHeaders.removeHeader(String) method and verify
   * return of the MimeHeaders object. Remove single header from list of two.
   *
   * Description: Replace a MimeHeaders object.
   */
  public void removeHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeHeader2Test");
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
        throw new Fault("removeHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeHeader2Test failed", e);
    }
  }

  /*
   * @testName: removeHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:101;
   *
   * @test_Strategy: Call MimeHeaders.removeHeader(String) method and verify
   * return of the MimeHeaders object. Remove single header that contains
   * multiple values.
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeHeader3Test");
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
        throw new Fault("removeHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeHeader3Test failed", e);
    }
  }

  /*
   * @testName: removeHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:101;
   *
   * @test_Strategy: Call MimeHeaders.removeHeader(String) method and verify
   * return of the MimeHeaders object. Remove header that doesn't exist
   *
   * Description: Remove a MimeHeaders object.
   */
  public void removeHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeHeader4Test");
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
        throw new Fault("removeHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeHeader4Test failed", e);
    }
  }

  /*
   * @testName: getMatchingHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:104;
   *
   * @test_Strategy: Call MimeHeaders.getMatchingHeaders(String[]) method and
   * verify return of a the MimeHeaders object.
   *
   * Description: Retrieve a MimeHeaders object. Get single header.
   */
  public void getMatchingHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingHeaders1Test");
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
        throw new Fault("getMatchingHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getMatchingHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:104;
   *
   * @test_Strategy: Call MimeHeaders.getMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Get single header from list of
   * two.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getMatchingHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingHeaders2Test");
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
        throw new Fault("getMatchingHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getMatchingHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:104;
   *
   * @test_Strategy: Call MimeHeaders.getMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Get single header that contains
   * multiple values.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getMatchingHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingHeaders3Test");
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
        throw new Fault("getMatchingHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getMatchingHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:104;
   *
   * @test_Strategy: Call MimeHeaders.getMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Attempt to get a header that
   * doesn't exist
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getMatchingHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingHeaders4Test");
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
        throw new Fault("getMatchingHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingHeaders4Test failed", e);
    }
  }

  /*
   * @testName: getMatchingHeaders5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:104;
   *
   * @test_Strategy: Call MimeHeaders.getMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Attempt to get a headers and a
   * non-existent header
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getMatchingHeaders5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingHeaders5Test");
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
        throw new Fault("getMatchingHeaders5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingHeaders5Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call MimeHeaders.getNonMatchingHeaders(String[]) method and
   * verify return of a the MimeHeaders object. Get single header.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getNonMatchingHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingHeaders1Test");
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
        throw new Fault("getNonMatchingHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call MimeHeaders.getNonMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Get single header from list of
   * two.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getNonMatchingHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingHeaders2Test");
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
        throw new Fault("getNonMatchingHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call MimeHeaders.getNonMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Get single header that contains
   * multiple values.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getNonMatchingHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingHeaders3Test");
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
        throw new Fault("getNonMatchingHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call MimeHeaders.getNonMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Attempt to get header that results
   * in no headers being returned.
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getNonMatchingHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingHeaders4Test");
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
        throw new Fault("getNonMatchingHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingHeaders4Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingHeaders5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:105;
   *
   * @test_Strategy: Call MimeHeaders.getNonMatchingHeaders(String[]) method and
   * verify return of the MimeHeaders object. Attempt to get a header and a
   * non-existent header
   *
   * Description: Retrieve a MimeHeaders object.
   */
  public void getNonMatchingHeaders5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingHeaders5Test");
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
        throw new Fault("getNonMatchingHeaders5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingHeaders5Test failed", e);
    }
  }
}
