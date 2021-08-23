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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPPart;

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

  private static final String TESTSERVLET = "/SOAPPart_web/SOAPPartTestServlet";

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
   * @testName: addMimeHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MIMEHeader object. Add a single header.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader1Test");
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
        throw new Fault("addMimeHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader1Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Add two headers.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader2Test");
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
        throw new Fault("addMimeHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader2Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Add two headers that have
   * different values.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader3Test");
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
        throw new Fault("addMimeHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader3Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Attempt to add an empty header
   * and value.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader4Test");
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
        throw new Fault("addMimeHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader4Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Attempt to add an empty header
   * and non-empty value.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader5Test");
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
        throw new Fault("addMimeHeader5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader5Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader6Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Attempt to add a non-empty
   * header and empty value.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader6Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader6Test");
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
        throw new Fault("addMimeHeader6Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader6Test failed", e);
    }
  }

  /*
   * @testName: addMimeHeader7Test
   *
   * @assertion_ids: SAAJ:JAVADOC:12;
   *
   * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and
   * verify creation of a new MimeHeader object. Attempt to add a null header
   * and null value.
   *
   * Description: Construct a MimeHeader object.
   */
  public void addMimeHeader7Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addMimeHeader7Test");
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
        throw new Fault("addMimeHeader7Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("addMimeHeader7Test failed", e);
    }
  }

  /*
   * @testName: getMimeHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:10;
   *
   * @test_Strategy: Call SOAPPart.getMimeHeader(String,String) method and
   * verify return of a the MimeHeader object. Get a single header.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMimeHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMimeHeader1Test");
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
        throw new Fault("getMimeHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMimeHeader1Test failed", e);
    }
  }

  /*
   * @testName: getMimeHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:10;
   *
   * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify
   * return of the MimeHeader object. Get single header from multiple headers.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMimeHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMimeHeader2Test");
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
        throw new Fault("getMimeHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMimeHeader2Test failed", e);
    }
  }

  /*
   * @testName: getMimeHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:10;
   *
   * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify
   * return of the MimeHeader object. Get header that contains two entries.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMimeHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMimeHeader3Test");
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
        throw new Fault("getMimeHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMimeHeader3Test failed", e);
    }
  }

  /*
   * @testName: getMimeHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:10;
   *
   * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify
   * return of the MimeHeader object. Attempt to get a header that doesn't exist
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMimeHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMimeHeader4Test");
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
        throw new Fault("getMimeHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMimeHeader4Test failed", e);
    }
  }

  /*
   * @testName: getAllMimeHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:13;
   *
   * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return
   * of all MimeHeader objects. Get single header.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getAllMimeHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllMimeHeaders1Test");
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
        throw new Fault("getAllMimeHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllMimeHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getAllMimeHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:13;
   *
   * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return
   * of all MimeHeader objects. Get multiple headers.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getAllMimeHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllMimeHeaders2Test");
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
        throw new Fault("getAllMimeHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllMimeHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getAllMimeHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:13;
   *
   * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return
   * of all MimeHeader objects. Get single header that contains mulitple
   * entries.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getAllMimeHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllMimeHeaders3Test");
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
        throw new Fault("getAllMimeHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllMimeHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getAllMimeHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:13;
   *
   * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return
   * of all MimeHeader objects. Attempt to get all headers when none exist.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getAllMimeHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllMimeHeaders4Test");
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
        throw new Fault("getAllMimeHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getAllMimeHeaders4Test failed", e);
    }
  }

  /*
   * @testName: removeAllMimeHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:9;
   *
   * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
   * removal of all MimeHeader objects. Remove single header.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeAllMimeHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllMimeHeaders1Test");
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
        throw new Fault("removeAllMimeHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllMimeHeaders1Test failed", e);
    }
  }

  /*
   * @testName: removeAllMimeHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:9;
   *
   * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
   * removal of all MimeHeader objects. Remove multiple headers.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeAllMimeHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllMimeHeaders2Test");
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
        throw new Fault("removeAllMimeHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllMimeHeaders2Test failed", e);
    }
  }

  /*
   * @testName: removeAllMimeHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:9;
   *
   * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
   * removal of all MimeHeader objects. Remove header that contains multiple
   * entries.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeAllMimeHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllMimeHeaders3Test");
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
        throw new Fault("removeAllMimeHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllMimeHeaders3Test failed", e);
    }
  }

  /*
   * @testName: removeAllMimeHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:9;
   *
   * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
   * removal of all MimeHeader objects. Remove headers when none exist.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeAllMimeHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllMimeHeaders4Test");
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
        throw new Fault("removeAllMimeHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeAllMimeHeaders4Test failed", e);
    }
  }

  /*
   * @testName: setMimeHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:11;
   *
   * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and
   * verify return of a the MimeHeader object. Set exist header.
   *
   * Description: Replace a MimeHeader object.
   */
  public void setMimeHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMimeHeader1Test");
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
        throw new Fault("setMimeHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setMimeHeader1Test failed", e);
    }
  }

  /*
   * @testName: setMimeHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:11;
   *
   * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and
   * verify return of the MimeHeader object. Set existing header from list of
   * two headers.
   *
   * Description: Replace a MimeHeader object.
   */
  public void setMimeHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMimeHeader2Test");
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
        throw new Fault("setMimeHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setMimeHeader2Test failed", e);
    }
  }

  /*
   * @testName: setMimeHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:11;
   *
   * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and
   * verify return of the MimeHeader object. Set existing header that contains
   * multiple values.
   *
   * Description: Replace/Construct a MimeHeader object.
   */
  public void setMimeHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMimeHeader3Test");
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
        throw new Fault("setMimeHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setMimeHeader3Test failed", e);
    }
  }

  /*
   * @testName: setMimeHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:11;
   *
   * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and
   * verify return of the MimeHeader object. Set header that doesn't exist
   *
   * Description: Replace/Construct a MimeHeader object.
   */
  public void setMimeHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMimeHeader4Test");
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
        throw new Fault("setMimeHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setMimeHeader4Test failed", e);
    }
  }

  /*
   * @testName: setMimeHeader5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:11;
   *
   * @test_Strategy: Call SOAPPart.setMimeHeader(Name,emptyvalue) method and
   * verify return of the MimeHeader object. Set an existing header twice.
   *
   * Description: Replace/Construct a MimeHeader object.
   */
  public void setMimeHeader5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setMimeHeader5Test");
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
        throw new Fault("setMimeHeader5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setMimeHeader5Test failed", e);
    }
  }

  /*
   * @testName: removeMimeHeader1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:8;
   *
   * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
   * return of a the MimeHeader object. Remove single header.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeMimeHeader1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeMimeHeader1Test");
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
        throw new Fault("removeMimeHeader1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeMimeHeader1Test failed", e);
    }
  }

  /*
   * @testName: removeMimeHeader2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:8;
   *
   * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
   * return of the MimeHeader object. Remove single header from list of two.
   *
   * Description: Replace a MimeHeader object.
   */
  public void removeMimeHeader2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeMimeHeader2Test");
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
        throw new Fault("removeMimeHeader2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeMimeHeader2Test failed", e);
    }
  }

  /*
   * @testName: removeMimeHeader3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:8;
   *
   * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
   * return of the MimeHeader object. Remove single header that contains
   * multiple values.
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeMimeHeader3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeMimeHeader3Test");
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
        throw new Fault("removeMimeHeader3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeMimeHeader3Test failed", e);
    }
  }

  /*
   * @testName: removeMimeHeader4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:8;
   *
   * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
   * return of the MimeHeader object. Remove header that doesn't exist
   *
   * Description: Remove a MimeHeader object.
   */
  public void removeMimeHeader4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeMimeHeader4Test");
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
        throw new Fault("removeMimeHeader4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("removeMimeHeader4Test failed", e);
    }
  }

  /*
   * @testName: getMatchingMimeHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:14;
   *
   * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
   * verify return of a the MimeHeader object. Get single header.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMatchingMimeHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingMimeHeaders1Test");
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
        throw new Fault("getMatchingMimeHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingMimeHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getMatchingMimeHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:14;
   *
   * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
   * verify return of the MimeHeader object. Get single header from list of two.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMatchingMimeHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingMimeHeaders2Test");
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
        throw new Fault("getMatchingMimeHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingMimeHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getMatchingMimeHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:14;
   *
   * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
   * verify return of the MimeHeader object. Get single header that contains
   * multiple values.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMatchingMimeHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingMimeHeaders3Test");
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
        throw new Fault("getMatchingMimeHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingMimeHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getMatchingMimeHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:14;
   *
   * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
   * verify return of the MimeHeader object. Attempt to get a header that
   * doesn't exist
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMatchingMimeHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingMimeHeaders4Test");
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
        throw new Fault("getMatchingMimeHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingMimeHeaders4Test failed", e);
    }
  }

  /*
   * @testName: getMatchingMimeHeaders5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:14;
   *
   * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
   * verify return of the MimeHeader object. Attempt to get a headers and a
   * non-existent header
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getMatchingMimeHeaders5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMatchingMimeHeaders5Test");
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
        throw new Fault("getMatchingMimeHeaders5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getMatchingMimeHeaders5Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingMimeHeaders1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:15;
   *
   * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method
   * and verify return of a the MimeHeader object.
   *
   * Description: Retrieve a MimeHeader object. Get single header.
   */
  public void getNonMatchingMimeHeaders1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingMimeHeaders1Test");
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
        throw new Fault("getNonMatchingMimeHeaders1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingMimeHeaders1Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingMimeHeaders2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:15;
   *
   * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Get single header from list of
   * two.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getNonMatchingMimeHeaders2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingMimeHeaders2Test");
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
        throw new Fault("getNonMatchingMimeHeaders2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingMimeHeaders2Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingMimeHeaders3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:15;
   *
   * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Get single header that contains
   * multiple values.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getNonMatchingMimeHeaders3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingMimeHeaders3Test");
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
        throw new Fault("getNonMatchingMimeHeaders3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingMimeHeaders3Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingMimeHeaders4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:15;
   *
   * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Attempt to get header that
   * results in no headers being returned.
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getNonMatchingMimeHeaders4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingMimeHeaders4Test");
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
        throw new Fault("getNonMatchingMimeHeaders4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingMimeHeaders4Test failed", e);
    }
  }

  /*
   * @testName: getNonMatchingMimeHeaders5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:15;
   *
   * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Attempt to get a header and a
   * non-existent header
   *
   * Description: Retrieve a MimeHeader object.
   */
  public void getNonMatchingMimeHeaders5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNonMatchingMimeHeaders5Test");
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
        throw new Fault("getNonMatchingMimeHeaders5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getNonMatchingMimeHeaders5Test failed", e);
    }
  }

  /*
   * @testName: setContentId1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:6;
   *
   * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no
   * errors occur. Attempt to set id to a valid value.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentId1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentId1Test");
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
        throw new Fault("setContentId1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentId1Test failed", e);
    }
  }

  /*
   * @testName: setContentId2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:6;
   *
   * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no
   * errors occur. Attempt to set id to null.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentId2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentId2Test");
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
        throw new Fault("setContentId2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentId2Test failed", e);
    }
  }

  /*
   * @testName: setContentId3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:6;
   *
   * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no
   * errors occur. Set the id twice in a row.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentId3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentId3Test");
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
        throw new Fault("setContentId3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentId3Test failed", e);
    }
  }

  /*
   * @testName: setContentId4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:6;
   *
   * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no
   * errors occur. Attempt to set id to a valid value then to another string.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentId4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentId4Test");
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
        throw new Fault("setContentId4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentId4Test failed", e);
    }
  }

  /*
   * @testName: getContentId1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:4;
   *
   * @test_Strategy: Call SOAPPart.getContentId() method and verify correct
   * content id is returned. Get Id.
   *
   * Description: Get the Mime Header named Content-Id
   */
  public void getContentId1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentId1Test");
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
        throw new Fault("getContentId1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentId1Test failed", e);
    }
  }

  /*
   * @testName: getContentId2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:4;
   *
   * @test_Strategy: Call SOAPPart.getContentId() method and verify correct
   * content id is returned. Get id after it has been set twice in a row.
   *
   * Description: Get the Mime Header named Content-Id
   */
  public void getContentId2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentId2Test");
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
        throw new Fault("getContentId2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentId2Test failed", e);
    }
  }

  /*
   * @testName: getContentId3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:4;
   *
   * @test_Strategy: Call SOAPPart.getContentId(value) method and verify correct
   * content id is returned. Get id when non has been set.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void getContentId3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentId3Test");
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
        throw new Fault("getContentId3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentId3Test failed", e);
    }
  }

  /*
   * @testName: setContentLocation1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:7;
   *
   * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify
   * no errors occur. Set URL location
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentLocation1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentLocation1Test");
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
        throw new Fault("setContentLocation1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentLocation1Test failed", e);
    }
  }

  /*
   * @testName: setContentLocation2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:7;
   *
   * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify
   * no errors occur.
   *
   * Description: Set the Mime Header named Content-Id Set empty string
   * location.
   */
  public void setContentLocation2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentLocation2Test");
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
        throw new Fault("setContentLocation2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentLocation2Test failed", e);
    }
  }

  /*
   * @testName: setContentLocation3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:7;
   *
   * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify
   * no errors occur. Set URI location
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentLocation3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentLocation3Test");
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
        throw new Fault("setContentLocation3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentLocation3Test failed", e);
    }
  }

  /*
   * @testName: setContentLocation4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:7;
   *
   * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify
   * no errors occur. Set location twice.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentLocation4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentLocation4Test");
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
        throw new Fault("setContentLocation4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentLocation4Test failed", e);
    }
  }

  /*
   * @testName: setContentLocation5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:7;
   *
   * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify
   * no errors occur. Set location to null.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void setContentLocation5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentLocation5Test");
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
        throw new Fault("setContentLocation5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContentLocation5Test failed", e);
    }
  }

  /*
   * @testName: getContentLocation1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:5;
   *
   * @test_Strategy: Call SOAPPart.getContentLocation() method and verify
   * correct content location is returned. Get URL location.
   *
   * Description: Get the Mime Header named Content-Id
   */
  public void getContentLocation1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentLocation1Test");
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
        throw new Fault("getContentLocation1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentLocation1Test failed", e);
    }
  }

  /*
   * @testName: getContentLocation2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:5;
   *
   * @test_Strategy: Call SOAPPart.getContentLocation() method and verify
   * correct content location is returned. Get location when none has been set.
   *
   * Description: Get the Mime Header named Content-Id
   */
  public void getContentLocation2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentLocation2Test");
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
        throw new Fault("getContentLocation2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentLocation2Test failed", e);
    }
  }

  /*
   * @testName: getContentLocation3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:5;
   *
   * @test_Strategy: Call SOAPPart.getContentLocation() method and verify
   * correct content location is returned. Get location after it has been set
   * twice.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void getContentLocation3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentLocation3Test");
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
        throw new Fault("getContentLocation3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentLocation3Test failed", e);
    }
  }

  /*
   * @testName: getContentLocation4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:5;
   *
   * @test_Strategy: Call SOAPPart.getContentLocation() method and verify
   * correct content location is returned. Get URI location.
   *
   * Description: Set the Mime Header named Content-Id
   */
  public void getContentLocation4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentLocation4Test");
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
        throw new Fault("getContentLocation4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContentLocation4Test failed", e);
    }
  }

  /*
   * @testName: setContent1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:16; SAAJ:JAVADOC:17;
   *
   * @test_Strategy: Call SOAPPart.setContent(Source) method and verify no
   * exception is thrown.
   *
   * Description: Set the Content
   */
  public void setContent1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContent1Test");
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
        throw new Fault("setContent1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContent1Test failed", e);
    }
  }

  /*
   * @testName: getContent1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:18; SAAJ:JAVADOC:19;
   *
   * @test_Strategy: Call SOAPPart.getContent() method and verify a non null
   * source object is returned.
   *
   * Description: Get the Content
   */
  public void getContent1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContent1Test");
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
        throw new Fault("getContent1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContent1Test failed", e);
    }
  }

  /*
   * @testName: getEnvelope1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:2; SAAJ:JAVADOC:3;
   *
   * @test_Strategy: Call SOAPPart.getEnvelope() method and verify envelope is
   * returned.
   *
   * Description: Get the Envelope
   */
  public void getEnvelope1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getEnvelope1Test");
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
        throw new Fault("getEnvelope1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getEnvelope1Test failed", e);
    }
  }
}
