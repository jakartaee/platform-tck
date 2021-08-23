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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPMessage;

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

  private static final String TESTSERVLET = "/SOAPMessage_web/SOAPMessageTestServlet";

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
   * @testName: addAttachmentPartTest
   *
   * @assertion_ids: SAAJ:JAVADOC:34;
   *
   * @test_Strategy: Call SOAPMessage.addAttachmentPart(AttachmentPart) and
   * verify attachment part was added.
   *
   * Description: add an attachment object to the message
   *
   */
  public void addAttachmentPartTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "addAttachmentPartTest: add an attachmentpart to a SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addAttachmentPartTest");
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
      throw new Fault("addAttachmentPartTest failed", e);
    }

    if (!pass)
      throw new Fault("addAttachmentPartTest failed");
  }

  /*
   * @testName: countAttachmentsTest
   *
   * @assertion_ids: SAAJ:JAVADOC:29;
   *
   * @test_Strategy: Call SOAPMessage.countAttachments() and verify correct
   * count of attachments added.
   *
   * Description: count number of attachments in the message
   *
   */
  public void countAttachmentsTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "countAttachmentsTest: count number of attachments in SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "countAttachmentsTest");
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
      throw new Fault("countAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("countAttachmentsTest failed");
  }

  /*
   * @testName: getAttachmentsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:30;
   *
   * @test_Strategy: Call SOAPMessage.getAttachments() and verify correct get of
   * attachments.
   *
   * Description: get number of attachments in the message
   *
   */
  public void getAttachmentsTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getAttachmentsTest1: get number of attachments in SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentsTest1");
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
      throw new Fault("getAttachmentsTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentsTest1 failed");
  }

  /*
   * @testName: getAttachmentsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:31;
   *
   * @test_Strategy: Call SOAPMessage.getAttachments(MimeHeaders) and verify
   * correct get of attachments.
   *
   * Description: get number of attachments in the message
   *
   */
  public void getAttachmentsTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getAttachmentsTest2: get number of attachments in SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentsTest2");
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
      throw new Fault("getAttachmentsTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentsTest2 failed");
  }

  /*
   * @testName: removeAllAttachmentsTest
   *
   * @assertion_ids: SAAJ:JAVADOC:28;
   *
   * @test_Strategy: Call SOAPMessage.removeAllAttachments() and verify correct
   * remove of attachments.
   *
   * Description: remove attachments in the message
   *
   */
  public void removeAllAttachmentsTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "removeAllAttachmentsTest: remove attachments in SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAllAttachmentsTest");
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
      throw new Fault("removeAllAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("removeAllAttachmentsTest failed");
  }

  /*
   * @testName: removeAttachmentsTest
   *
   * @assertion_ids: SAAJ:JAVADOC:28;
   *
   * @test_Strategy: Call SOAPMessage.removeAttachments() and verify correct
   * attachments were removed.
   *
   * Description: remove attachments in the message
   *
   */
  public void removeAttachmentsTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("removeAttachmentsTest: remove attachments in SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAttachmentsTest");
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
      throw new Fault("removeAttachmentsTest failed", e);
    }

    if (!pass)
      throw new Fault("removeAttachmentsTest failed");
  }

  /*
   * @testName: setContentDescriptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:21;
   *
   * @test_Strategy: Call SOAPMessage.setContentDecription(String) and verify
   * correct setting of description.
   *
   * Description: Set the description of this SOAPMessage object's content with
   * the given description.
   *
   */
  public void setContentDescriptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "setContentDescriptionTest: set content description of SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setContentDescriptionTest");
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
      throw new Fault("setContentDescriptionTest failed", e);
    }

    if (!pass)
      throw new Fault("setContentDescriptionTest failed");
  }

  /*
   * @testName: getContentDescriptionTest
   *
   * @assertion_ids: SAAJ:JAVADOC:22;
   *
   * @test_Strategy: Call SOAPMessage.getContentDecription() and verify correct
   * description is returned.
   *
   * Description: Get the description of this SOAPMessage object's content.
   *
   */
  public void getContentDescriptionTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getContentDescriptionTest: get content description of SOAPMessage");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getContentDescriptionTest");
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
      throw new Fault("getContentDescriptionTest failed", e);
    }

    if (!pass)
      throw new Fault("getContentDescriptionTest failed");
  }

  /*
   * @testName: createAttachmentPartTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:35;
   *
   * @test_Strategy: Call SOAPMessage.createAttachmentPart() and verify an empty
   * AttachmentPart object is returned.
   *
   * Description: Create a new empty AttachmentPart object
   *
   */
  public void createAttachmentPartTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil
          .logMsg("createAttachmentPartTest1: create empty attachment part");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createAttachmentPartTest1");
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
      throw new Fault("createAttachmentPartTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createAttachmentPartTest1 failed");
  }

  /*
   * @testName: createAttachmentPartTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:36;
   *
   * @test_Strategy: Call SOAPMessage.createAttachmentPart(DataHandler) and
   * verify a non-empty AttachmentPart object is returned.
   *
   * Description: Create an AttachmentPart object and populate it with a given
   * DataHandler object.
   *
   */
  public void createAttachmentPartTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createAttachmentPartTest2: create attachment part and populate with given DataHandler");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createAttachmentPartTest2");
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
      throw new Fault("createAttachmentPartTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createAttachmentPartTest2 failed");
  }

  /*
   * @testName: createAttachmentPartTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:38;
   *
   * @test_Strategy: Call SOAPMessage.createAttachmentPart(Object,String) and
   * verify non-empty AttachmentPart object is returned.
   *
   * Description: Create an AttachmentPart object and populate it with the
   * specified data of the specified content-type.
   *
   */
  public void createAttachmentPartTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "createAttachmentPartTest3: create attachment part and populate wth given object and content type");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "createAttachmentPartTest3");
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
      throw new Fault("createAttachmentPartTest3 failed", e);
    }

    if (!pass)
      throw new Fault("createAttachmentPartTest3 failed");
  }

  /*
   * @testName: writeToTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:42; SAAJ:JAVADOC:43; SAAJ:JAVADOC:44;
   *
   * @test_Strategy: Call SOAPMessage.writeTo(OutputStream) and verify
   * SOAPMessage was written to stream.
   *
   * Description: Write a SOAPMessage without attachments to output stream
   *
   */
  public void writeToTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "writeToTest1: write a SOAPMessage without attachments to OutputStream");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "writeToTest1");
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
      throw new Fault("writeToTest1 failed", e);
    }

    if (!pass)
      throw new Fault("writeToTest1 failed");
  }

  /*
   * @testName: writeToTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:42; SAAJ:JAVADOC:43; SAAJ:JAVADOC:44;
   *
   * @test_Strategy: Call SOAPMessage.writeTo(OutputStream) and verify
   * SOAPMessage was written to stream.
   *
   * Description: Write a SOAPMessage with attachments to output stream
   *
   */
  public void writeToTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "writeToTest2: write SOAPMessage with attachments to OutputStream");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "writeToTest2");
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
      throw new Fault("writeToTest2 failed", e);
    }

    if (!pass)
      throw new Fault("writeToTest2 failed");
  }

  /*
   * @testName: saveRequiredTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:41;
   *
   * @test_Strategy: Call SOAPMessage.saveRequired() and verify false is
   * returned since saveChanges has not been called.
   *
   * Description: Test SOAPMessage object to see that saveChanges has not been
   * called on it.
   *
   */
  public void saveRequiredTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "saveRequiredTest1: verify SOAPMessage.saveRequired() is false");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "saveRequiredTest1");
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
      throw new Fault("saveRequiredTest1 failed", e);
    }

    if (!pass)
      throw new Fault("saveRequiredTest1 failed");
  }

  /*
   * @testName: saveRequiredTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:41; SAAJ:JAVADOC:40;
   *
   * @test_Strategy: Call SOAPMessage.saveRequired() after calling
   * SOAPMessage.saveChanges() and verify true is returned.
   *
   * Description: Test SOAPMessage object to see that saveChanges has been
   * called on it.
   *
   */
  public void saveRequiredTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "saveRequiredTest2: verify SOAPMessage.saveRequired() is true");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "saveRequiredTest2");
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
      throw new Fault("saveRequiredTest2 failed", e);
    }

    if (!pass)
      throw new Fault("saveRequiredTest2 failed");
  }

  /*
   * @testName: getMimeHeadersTest
   *
   * @assertion_ids: SAAJ:JAVADOC:37;
   *
   * @test_Strategy: Call SOAPMessage.getMimeHeaders() to return the
   * transport-specific MimeHeaders for this SOAPMessage.
   *
   * Description: Test get of transport-specific MimeHeaders for this
   * SOAPMessage.
   *
   */
  public void getMimeHeadersTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "getMimeHeadersTest: verify SOAPMessage.saveRequired() is true");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getMimeHeadersTest");
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
      throw new Fault("getMimeHeadersTest failed", e);
    }

    if (!pass)
      throw new Fault("getMimeHeadersTest failed");
  }

  /*
   * @testName: getSOAPPartTest
   *
   * @assertion_ids: SAAJ:JAVADOC:23;
   *
   * @test_Strategy: Call SOAPMessage.getSOAPPart() method to retrieve the
   * SOAPPart of the message.
   *
   * Description: Get the SOAPPart for this soap message.
   *
   */
  public void getSOAPPartTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getSOAPPartTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSOAPPartTest");
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
      throw new Fault("getSOAPPartTest failed", e);
    }

    if (!pass)
      throw new Fault("getSOAPPartTest failed");
  }

  /*
   * @testName: setPropertyTest
   *
   * @assertion_ids: SAAJ:JAVADOC:45; SAAJ:JAVADOC:46;
   *
   * @test_Strategy: Call SOAPMessage.setProperty(String, String) method and
   * verify that the property was set correctly.
   *
   * Description: Set a property for this soap message.
   *
   */
  public void setPropertyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("setPropertyTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setPropertyTest");
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
      throw new Fault("setPropertyTest failed", e);
    }

    if (!pass)
      throw new Fault("setPropertyTest failed");
  }

  /*
   * @testName: getPropertyTest
   *
   * @assertion_ids: SAAJ:JAVADOC:47; SAAJ:JAVADOC:48;
   *
   * @test_Strategy: Call SOAPMessage.getProperty(String) method and verify that
   * the correct property was retrieved.
   *
   * Description: Set a property for this soap message.
   *
   */
  public void getPropertyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getPropertyTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getPropertyTest");
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
      throw new Fault("getPropertyTest failed", e);
    }

    if (!pass)
      throw new Fault("getPropertyTest failed");
  }

  /*
   * @testName: getSOAPBodyTest
   *
   * @assertion_ids: SAAJ:JAVADOC:24; SAAJ:JAVADOC:25;
   *
   * @test_Strategy: Call SOAPMessage.getSOAPBody() method to retrieve the
   * SOAPBody of the message.
   *
   * Description: Get the SOAPBody for this soap message.
   *
   */
  public void getSOAPBodyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getSOAPBodyTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSOAPBodyTest");
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
      throw new Fault("getSOAPBodyTest failed", e);
    }

    if (!pass)
      throw new Fault("getSOAPBodyTest failed");
  }

  /*
   * @testName: getSOAPHeaderTest
   *
   * @assertion_ids: SAAJ:JAVADOC:26; SAAJ:JAVADOC:27;
   *
   * @test_Strategy: Call SOAPMessage.getSOAPHeader() method to retrieve the
   * SOAPHeader of the message.
   *
   * Description: Get the SOAPHeader for this soap message.
   *
   */
  public void getSOAPHeaderTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getSOAPHeaderTest");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getSOAPHeaderTest");
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
      throw new Fault("getSOAPHeaderTest failed", e);
    }

    if (!pass)
      throw new Fault("getSOAPHeaderTest failed");
  }

  /*
   * @testName: getAttachmentBySwaRefTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
   *
   * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
   * attachment from the message. Retrieve attachments using SwaRef "cid:uri"
   * scheme. Create a SOAP message with a Body element and a text node of SwaRef
   * "cid:uri".
   *
   */
  public void getAttachmentBySwaRefTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getAttachmentBySwaRefTest1");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentBySwaRefTest1");
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
      throw new Fault("getAttachmentBySwaRefTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentBySwaRefTest1 failed");
  }

  /*
   * @testName: getAttachmentBySwaRefTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
   *
   * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
   * attachment from the message. Retrieve attachments using SwaRef "cid:uri"
   * scheme. Create a SOAP message with a Body element and a Child element with
   * a text node of SwaRef "cid:uri".
   *
   */
  public void getAttachmentBySwaRefTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getAttachmentBySwaRefTest2");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentBySwaRefTest2");
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
      throw new Fault("getAttachmentBySwaRefTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentBySwaRefTest2 failed");
  }

  /*
   * @testName: getAttachmentBySwaRefTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
   *
   * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
   * attachment from the message. Retrieve attachments using SwaRef "uri"
   * scheme. Create a SOAP message with a Body element and a text node of SwaRef
   * "uri". This uses URI and Content-Location.
   *
   */
  public void getAttachmentBySwaRefTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getAttachmentBySwaRefTest3");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentBySwaRefTest3");
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
      throw new Fault("getAttachmentBySwaRefTest3 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentBySwaRefTest3 failed");
  }

  /*
   * @testName: getAttachmentByHrefTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
   *
   * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
   * attachment from the message. Retrieve attachments using "href=cid:uri"
   * attribute scheme. This deals with using the Content-ID and URI setting to
   * retrieve the attachment.
   *
   */
  public void getAttachmentByHrefTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getAttachmentByHrefTest1");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentByHrefTest1");
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
      throw new Fault("getAttachmentByHrefTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentByHrefTest1 failed");
  }

  /*
   * @testName: getAttachmentByHrefTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:32; SAAJ:JAVADOC:33;
   *
   * @test_Strategy: Call SOAPMessage.getAttachment() method to retrieve an
   * attachment from the message. Retrieve attachments using "href=uri"
   * attribute scheme. This deals with using the Content-Location and URI
   * setting to retrieve the attachment.
   */
  public void getAttachmentByHrefTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("getAttachmentByHrefTest2");

      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttachmentByHrefTest2");
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
      throw new Fault("getAttachmentByHrefTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getAttachmentByHrefTest2 failed");
  }
}
