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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.AttachmentPart;

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

  private static final String TESTSERVLET = "/AttachmentPart_web/AttachmentPartTestServlet";

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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:147;
   *
   * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:145;
   *
   * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
   * return of a the MimeHeader object. Get a single header.
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
   * @assertion_ids: SAAJ:JAVADOC:145;
   *
   * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
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
   * @assertion_ids: SAAJ:JAVADOC:145;
   *
   * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
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
   * @assertion_ids: SAAJ:JAVADOC:145;
   *
   * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
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
   * @assertion_ids: SAAJ:JAVADOC:148;
   *
   * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
   * return of all MimeHeader objects.
   *
   * Description: Retrieve a MimeHeader object. Get single.
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
   * @assertion_ids: SAAJ:JAVADOC:148;
   *
   * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
   * return of all MimeHeader objects. Get multiple.
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
   * @assertion_ids: SAAJ:JAVADOC:148;
   *
   * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
   * return of all MimeHeader objects. Get single header that contains mulitple
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
   * @assertion_ids: SAAJ:JAVADOC:148;
   *
   * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
   * return of all MimeHeader objects. Attempt to get all headers when none
   * exist.
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
   * @assertion_ids: SAAJ:JAVADOC:144;
   *
   * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and
   * verify removal of all MimeHeader objects. Remove single header.
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
   * @assertion_ids: SAAJ:JAVADOC:144;
   *
   * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and
   * verify removal of all MimeHeader objects. Remove multiple headers.
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
   * @assertion_ids: SAAJ:JAVADOC:144;
   *
   * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and
   * verify removal of all MimeHeader objects. Remove header that contains
   * multiple entries.
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
   * @assertion_ids: SAAJ:JAVADOC:144;
   *
   * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and
   * verify removal of all MimeHeader objects. Remove headers when none exist.
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
   * @assertion_ids: SAAJ:JAVADOC:146;
   *
   * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:146;
   *
   * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:146;
   *
   * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:146;
   *
   * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:146;
   *
   * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
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
   * @assertion_ids: SAAJ:JAVADOC:143;
   *
   * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
   * verify return of a the MimeHeader object. Remove single header.
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
   * @assertion_ids: SAAJ:JAVADOC:143;
   *
   * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
   * verify return of the MimeHeader object. Remove single header from list of
   * two.
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
   * @assertion_ids: SAAJ:JAVADOC:143;
   *
   * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
   * verify return of the MimeHeader object. Remove single header that contains
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
   * @assertion_ids: SAAJ:JAVADOC:143;
   *
   * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
   * verify return of the MimeHeader object. Remove header that doesn't exist
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
   * @assertion_ids: SAAJ:JAVADOC:149;
   *
   * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
   * and verify return of a the MimeHeader object.
   *
   * Description: Retrieve a MimeHeader object. Get single header.
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
   * @assertion_ids: SAAJ:JAVADOC:149;
   *
   * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Get single header from list of
   * two.
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
   * @assertion_ids: SAAJ:JAVADOC:149;
   *
   * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Get single header that contains
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
   * @assertion_ids: SAAJ:JAVADOC:149;
   *
   * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Attempt to get a header that
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
   * @assertion_ids: SAAJ:JAVADOC:149;
   *
   * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
   * and verify return of the MimeHeader object. Attempt to get a headers and a
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
   * @assertion_ids: SAAJ:JAVADOC:150;
   *
   * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
   * method and verify return of a the MimeHeader object. Get single header.
   *
   * Description: Retrieve a MimeHeader object.
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
   * @assertion_ids: SAAJ:JAVADOC:150;
   *
   * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
   * method and verify return of the MimeHeader object. Get single header from
   * list of two.
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
   * @assertion_ids: SAAJ:JAVADOC:150;
   *
   * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
   * method and verify return of the MimeHeader object. Get single header that
   * contains multiple values.
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
   * @assertion_ids: SAAJ:JAVADOC:150;
   *
   * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
   * method and verify return of the MimeHeader object. Attempt to get header
   * that results in no headers being returned.
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
   * @assertion_ids: SAAJ:JAVADOC:150;
   *
   * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
   * method and verify return of the MimeHeader object. Attempt to get a header
   * and a non-existent header
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
   * @assertion_ids: SAAJ:JAVADOC:140;
   *
   * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify
   * no errors occur. Attempt to set id to a valid value.
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
   * @assertion_ids: SAAJ:JAVADOC:140;
   *
   * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify
   * no errors occur.
   *
   * Description: Set the Mime Header named Content-Id Set the id to null.
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
   * @assertion_ids: SAAJ:JAVADOC:140;
   *
   * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify
   * no errors occur.
   *
   * Description: Set the Mime Header named Content-Id Set the id twice in a
   * row.
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
   * @assertion_ids: SAAJ:JAVADOC:140;
   *
   * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify
   * no errors occur. Attempt to set id to a valid value then to another string.
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
   * @assertion_ids: SAAJ:JAVADOC:139;
   *
   * @test_Strategy: Call AttachmentPart.getContentId() method and verify
   * correct content id is returned. Get Id.
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
   * @assertion_ids: SAAJ:JAVADOC:139;
   *
   * @test_Strategy: Call AttachmentPart.getContentId() method and verify
   * correct content id is returned. Get id after it has been set twice in a
   * row.
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
   * @testName: setContentLocation1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:141;
   *
   * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
   * verify no errors occur. Set URL location
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
   * @assertion_ids: SAAJ:JAVADOC:141;
   *
   * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
   * verify no errors occur. Set empty string location.
   *
   * Description: Set the Mime Header named Content-Id
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
   * @assertion_ids: SAAJ:JAVADOC:141;
   *
   * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
   * verify no errors occur. Set URI location
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
   * @assertion_ids: SAAJ:JAVADOC:141;
   *
   * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
   * verify no errors occur. Set location twice.
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
   * @assertion_ids: SAAJ:JAVADOC:141;
   *
   * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
   * verify no errors occur. Set location to null.
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
   * @assertion_ids: SAAJ:JAVADOC:138;
   *
   * @test_Strategy: Call AttachmentPart.getContentLocation() method and verify
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
   * @assertion_ids: SAAJ:JAVADOC:138;
   *
   * @test_Strategy: Call AttachmentPart.getContentLocation(String) method and
   * verify correct content location is returned. Get location after it has been
   * set twice.
   *
   * Description: Set the Mime Header named Content-Id
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
   * @assertion_ids: SAAJ:JAVADOC:138;
   *
   * @test_Strategy: Call AttachmentPart.getContentLocation(String) method and
   * verify correct content location is returned. Get URI location.
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
   * @testName: setContent1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:129;
   *
   * @test_Strategy: Call AttachmentPart.setContent(Object, String) method
   * passing a null and verify exception is thrown.
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
   * @testName: setContent2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:129;
   *
   * @test_Strategy: Call AttachmentPart.setContent(Object, String) method
   * passing an invalid object type verify exception is thrown.
   *
   * Description: Set the Content
   */
  public void setContent2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContent2Test");
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
        throw new Fault("setContent2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContent2Test failed", e);
    }
  }

  /*
   * @testName: setContent3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:129;
   *
   * @test_Strategy: Call AttachmentPart.setContent(Object, String) method
   * passing invalid object types and verify exception is thrown.
   *
   * Description: Set the Content
   */
  public void setContent3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContent3Test");
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
        throw new Fault("setContent3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setContent3Test failed", e);
    }
  }

  /*
   * @testName: getContent1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
   *
   * @test_Strategy: Call AttachmentPart.getContent() method and verify that a
   * StreamSource object is returned properly for the text/xml mime type.
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
   * @testName: getContent2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
   *
   * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
   * InputStream object or String object is returned properly for the text/html
   * mime type.
   *
   * Description: Get the Content
   */
  public void getContent2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContent2Test");
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
        throw new Fault("getContent2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContent2Test failed", e);
    }
  }

  /*
   * @testName: getContent3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
   *
   * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
   * Image object is returned properly for the image/gif mime type.
   *
   * Description: Get the Content
   */
  public void getContent3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContent3Test");
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
        throw new Fault("getContent3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContent3Test failed", e);
    }
  }

  /*
   * @testName: getContent4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
   *
   * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
   * Image object is returned properly for the image/jpeg mime type.
   *
   * Description: Get the Content
   */
  public void getContent4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContent4Test");
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
        throw new Fault("getContent4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContent4Test failed", e);
    }
  }

  /*
   * @testName: getContent5Test
   *
   * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
   *
   * @test_Strategy: Call AttachmentPart.getContent() method and verify that a
   * String object is returned properly for the text/plain mime type.
   *
   * Description: Get the Content
   */
  public void getContent5Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContent5Test");
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
        throw new Fault("getContent5Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getContent5Test failed", e);
    }
  }

  /*
   * @testName: setDataHandler1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:136;
   *
   * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) with valid
   * object
   *
   * Description: Set the DataHandler object
   */
  public void setDataHandler1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setDataHandler1Test");
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
        throw new Fault("setDataHandler1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setDataHandler1Test failed", e);
    }
  }

  /*
   * @testName: setDataHandler2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:136;
   *
   * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) with a null
   * object.
   *
   * Description: Set the DataHandler object
   */
  public void setDataHandler2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setDataHandler2Test");
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
        throw new Fault("setDataHandler2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setDataHandler2Test failed", e);
    }
  }

  /*
   * @testName: setDataHandler3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:136;
   *
   * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) twice and
   * verify no errors occur.
   *
   * Description: Set the DataHandler object
   */
  public void setDataHandler3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setDataHandler3Test");
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
        throw new Fault("setDataHandler3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setDataHandler3Test failed", e);
    }
  }

  /*
   * @testName: getDataHandler1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
   *
   * @test_Strategy: Call AttachmentPart.getDataHandler() and verify the
   * contents of the data.
   *
   * Description: Get the DataHandler object
   */
  public void getDataHandler1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getDataHandler1Test");
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
        throw new Fault("getDataHandler1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getDataHandler1Test failed", e);
    }
  }

  /*
   * @testName: getDataHandler2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
   *
   * @test_Strategy: Call AttachmentPart.getDataHandler() when no DataHandler
   * has been set, should result in an exception.
   *
   * Description: Get the DataHandler object
   */
  public void getDataHandler2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getDataHandler2Test");
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
        throw new Fault("getDataHandler2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getDataHandler2Test failed", e);
    }
  }

  /*
   * @testName: getDataHandler3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
   *
   * @test_Strategy: Call AttachmentPart.getDataHandler() twice and verify the
   * contents of the data.
   *
   * Description: Get the DataHandler object
   */
  public void getDataHandler3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getDataHandler3Test");
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
        throw new Fault("getDataHandler3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getDataHandler3Test failed", e);
    }
  }

  /*
   * @testName: getSize1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
   *
   * @test_Strategy: Call AttachmentPart.getSize() on empty Attachment.
   *
   * Description: Get the size of the AttachmentPart Object
   */
  public void getSize1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSize1Test");
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
        throw new Fault("getSize1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getSize1Test failed", e);
    }
  }

  /*
   * @testName: getSize2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
   *
   * @test_Strategy: Call AttachmentPart.getSize() after Mime header is added.
   *
   * Description: Get the size of the AttachmentPart Object
   */
  public void getSize2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSize2Test");
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
        throw new Fault("getSize2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getSize2Test failed", e);
    }
  }

  /*
   * @testName: getSize3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
   *
   * @test_Strategy: Call AttachmentPart.getSize() after AttachmentPart.
   * setContent is done. A SOAPException should be thrown.
   *
   * Description: Get the size of the AttachmentPart Object
   */
  public void getSize3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSize3Test");
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
        throw new Fault("getSize3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getSize3Test failed", e);
    }
  }

  /*
   * @testName: getSize4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
   *
   * @test_Strategy: Call AttachmentPart.getSize() after AttachmentPart.
   * setDataHandler is done.
   *
   * Description: Get the size of the AttachmentPart Object
   */
  public void getSize4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSize4Test");
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
        throw new Fault("getSize4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("getSize4Test failed", e);
    }
  }

  /*
   * @testName: clearContent1Test
   *
   * @assertion_ids: SAAJ:JAVADOC:122;
   *
   * @test_Strategy: Call AttachmentPart.clearContent() after setting content.
   *
   * Description: Clear content of AttachmentPart object
   */
  public void clearContent1Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "clearContent1Test");
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
        throw new Fault("clearContent1Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("clearContent1Test failed", e);
    }
  }

  /*
   * @testName: clearContent2Test
   *
   * @assertion_ids: SAAJ:JAVADOC:122;
   *
   * @test_Strategy: Call AttachmentPart.clearContent() after setting content
   * twice.
   *
   * Description: Clear content of AttachmentPart object
   */
  public void clearContent2Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "clearContent2Test");
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
        throw new Fault("clearContent2Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("clearContent2Test failed", e);
    }
  }

  /*
   * @testName: clearContent3Test
   *
   * @assertion_ids: SAAJ:JAVADOC:122;
   *
   * @test_Strategy: Call AttachmentPart.clearContent() after setting content
   * and mime headers. Mime Headers should remain untouched.
   *
   * Description: Clear content of AttachmentPart object
   */
  public void clearContent3Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "clearContent3Test");
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
        throw new Fault("clearContent3Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("clearContent3Test failed", e);
    }
  }

  /*
   * @testName: clearContent4Test
   *
   * @assertion_ids: SAAJ:JAVADOC:122;
   *
   * @test_Strategy: Call AttachmentPart.clearContent() when no content has been
   * set.
   *
   * Description: Clear content of AttachmentPart object
   */
  public void clearContent4Test() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "clearContent4Test");
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
        throw new Fault("clearContent4Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("clearContent4Test failed", e);
    }
  }

  /*
   * @testName: SetGetBase64ContentTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
   * verify correct behavior.
   */
  public void SetGetBase64ContentTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetBase64ContentTest1");
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
        throw new Fault("SetGetBase64ContentTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetBase64ContentTest1 failed", e);
    }
  }

  /*
   * @testName: SetGetBase64ContentTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
   * verify correct behavior.
   */
  public void SetGetBase64ContentTest2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetBase64ContentTest2");
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
        throw new Fault("SetGetBase64ContentTest2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetBase64ContentTest2 failed", e);
    }
  }

  /*
   * @testName: SetGetBase64ContentTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
   * verify correct behavior.
   */
  public void SetGetBase64ContentTest3() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetBase64ContentTest3");
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
        throw new Fault("SetGetBase64ContentTest3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetBase64ContentTest3 failed", e);
    }
  }

  /*
   * @testName: SetBase64ContentIOExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:133;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content and test for
   * IOException.
   */
  public void SetBase64ContentIOExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setBase64ContentIOExceptionTest1");
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
        throw new Fault("SetBase64ContentIOExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetBase64ContentIOExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: SetBase64ContentNullPointerExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:133;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content and test for
   * NullPointerException.
   */
  public void SetBase64ContentNullPointerExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME",
            "setBase64ContentNullPointerExceptionTest1");
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
        throw new Fault("SetBase64ContentNullPointerExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetBase64ContentNullPointerExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: SetBase64ContentSOAPExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:133;
   *
   * @test_Strategy: Call AttachmentPart.setBase64Content and test for
   * SOAPException.
   */
  public void SetBase64ContentSOAPExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setBase64ContentSOAPExceptionTest1");
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
        throw new Fault("SetBase64ContentSOAPExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetBase64ContentSOAPExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: GetBase64ContentSOAPExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:128;
   *
   * @test_Strategy: Call AttachmentPart.getBase64Content and test for
   * SOAPException.
   */
  public void GetBase64ContentSOAPExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getBase64ContentSOAPExceptionTest1");
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
        throw new Fault("GetBase64ContentSOAPExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("GetBase64ContentSOAPExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: SetGetRawContentTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
   * correct behavior.
   */
  public void SetGetRawContentTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetRawContentTest1");
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
        throw new Fault("SetGetRawContentTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetRawContentTest1 failed", e);
    }
  }

  /*
   * @testName: SetGetRawContentTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
   * correct behavior.
   */
  public void SetGetRawContentTest2() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetRawContentTest2");
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
        throw new Fault("SetGetRawContentTest2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetRawContentTest2 failed", e);
    }
  }

  /*
   * @testName: SetGetRawContentTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
   * correct behavior.
   */
  public void SetGetRawContentTest3() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetRawContentTest3");
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
        throw new Fault("SetGetRawContentTest3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetRawContentTest3 failed", e);
    }
  }

  /*
   * @testName: SetGetRawContentTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
   * correct behavior.
   */
  public void SetGetRawContentTest4() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetRawContentTest4");
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
        throw new Fault("SetGetRawContentTest4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetRawContentTest4 failed", e);
    }
  }

  /*
   * @testName: SetGetRawContentBytesTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:319; SAAJ:JAVADOC:321;
   *
   * @test_Strategy: Call AttachmentPart.setRawContentBytes/getRawContentBytes
   * and verify correct behavior.
   */
  public void SetGetRawContentBytesTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "SetGetRawContentBytesTest1");
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
        throw new Fault("SetGetRawContentBytesTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetGetRawContentBytesTest1 failed", e);
    }
  }

  /*
   * @testName: SetRawContentBytesSOAPOrNullPointerExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:322;
   *
   * @test_Strategy: Call AttachmentPart.setRawContentBytes with a null byte[]
   * array and verify correct behavior.
   */
  public void SetRawContentBytesSOAPOrNullPointerExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME",
            "SetRawContentBytesSOAPOrNullPointerExceptionTest1");
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
        throw new Fault(
            "SetRawContentBytesSOAPOrNullPointerExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault(
          "SetRawContentBytesSOAPOrNullPointerExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: GetRawContentBytesSOAPOrNullPointerExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:320;
   *
   * @test_Strategy: Call AttachmentPart.getRawContentBytes on an empty
   * Attachment object and verify correct behavior.
   */
  public void GetRawContentBytesSOAPOrNullPointerExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME",
            "GetRawContentBytesSOAPOrNullPointerExceptionTest1");
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
        throw new Fault(
            "GetRawContentBytesSOAPOrNullPointerExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault(
          "GetRawContentBytesSOAPOrNullPointerExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: SetRawContentIOExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:131;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent and test for IOException.
   */
  public void SetRawContentIOExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setRawContentIOExceptionTest1");
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
        throw new Fault("SetRawContentIOExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetRawContentIOExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: SetRawContentNullPointerExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:131;
   *
   * @test_Strategy: Call AttachmentPart.setRawContent and test for
   * NullPointerException.
   */
  public void SetRawContentNullPointerExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setRawContentNullPointerExceptionTest1");
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
        throw new Fault("SetRawContentNullPointerExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SetRawContentNullPointerExceptionTest1 failed", e);
    }
  }

  /*
   * @testName: GetRawContentSOAPExceptionTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:126;
   *
   * @test_Strategy: Call AttachmentPart.getRawContent and test for
   * SOAPException.
   */
  public void GetRawContentSOAPExceptionTest1() throws Fault {
    try {
      boolean pass = true;

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getRawContentSOAPExceptionTest1");
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
        throw new Fault("GetRawContentSOAPExceptionTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("GetRawContentSOAPExceptionTest1 failed", e);
    }
  }
}
