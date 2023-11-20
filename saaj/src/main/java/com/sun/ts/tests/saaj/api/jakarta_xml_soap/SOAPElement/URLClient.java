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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElement;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

@ExtendWith(ArquillianExtension.class)
public class URLClient {
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String SOAPELEMENT_TESTSERVLET = "/SOAPElement_web/SOAPElementTestServlet";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL tsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;
  
  private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

  @Deployment(testable = false)
 	public static WebArchive createDeployment() throws IOException {
 		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPElement_web.war");
 		archive.addPackages(false, Filters.exclude(URLClient.class),
 				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElement");
 		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
 		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
 		return archive;
 	};


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
      logger.log(Logger.Level.ERROR,
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
    logger.log(Logger.Level.INFO,"setup ok");
  }

  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO,"cleanup ok");
  }

  /*
   * @testName: addAttributeTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:251; SAAJ:JAVADOC:252;
   *
   * @test_Strategy: Call SOAPElement.addAttribute(Name, String).
   *
   * Description: Adds an attribute with the specified name and value to this
   * SOAPElement object.
   *
   */
  @Test
  public void addAttributeTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addAttributeTest1: add an attribute to "
          + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addAttributeTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addAttributeTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addAttributeTest1 failed");
  }

  /*
   * @testName: addAttributeTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:251; SAAJ:JAVADOC:252;
   *
   * @test_Strategy: Call SOAPElement.addAttribute(QName, String).
   *
   * Description: Adds an attribute with the specified name and value to this
   * SOAPElement object.
   *
   */
  @Test
  public void addAttributeTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"addAttributeTest2: add an attribute to "
          + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addAttributeTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addAttributeTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addAttributeTest2 failed");
  }

  /*
   * @testName: getAttributeValueTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:257;
   *
   * @test_Strategy: Call SOAPElement.getAttributeValue(Name).
   *
   * Description: Returns the value of the attribute with the specified name for
   * this SOAPElement object.
   *
   */
  @Test
  public void getAttributeValueTest1() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"getAttributeValueTest1: get attribute value "
          + "of this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttributeValueTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getAttributeValueTest1 failed", e);
    }

    if (!pass)
      throw new Exception("getAttributeValueTest1 failed");
  }

  /*
   * @testName: getAttributeValueTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:258;
   *
   * @test_Strategy: Call SOAPElement.getAttributeValue(QName).
   *
   * Description: Returns the value of the attribute with the specified name for
   * this SOAPElement object.
   *
   */
  @Test
  public void getAttributeValueTest2() throws Exception {
    boolean pass = true;
    try {
      logger.log(Logger.Level.INFO,"getAttributeValueTest2: get attribute value "
          + "of this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAttributeValueTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getAttributeValueTest2 failed", e);
    }

    if (!pass)
      throw new Exception("getAttributeValueTest2 failed");
  }

  /*
   * @testName: getAllAttributesTest
   *
   * @assertion_ids: SAAJ:JAVADOC:259;
   *
   * @test_Strategy: Call SOAPElement.getAllAttributes().
   *
   * Description: Returns an iterator over all the attribute names in to this
   * SOAPElement object.
   *
   */
  @Test
  public void getAllAttributesTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getAllAttributesTest: return iterator of all "
          + "attribute names in this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getAllAttributesTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getAllAttributesTest failed", e);
    }

    if (!pass)
      throw new Exception("getAllAttributesTest failed");
  }

  /*
   * @testName: removeAttributeTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:269;
   *
   * @test_Strategy: Call SOAPElement.removeAttribute(Name).
   *
   * Description: Remove the attribute with the specified name from this
   * SOAPElement object.
   *
   */
  @Test
  public void removeAttributeTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"removeAttributeTest1: remove attribute "
          + "with the specified name from this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAttributeTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeAttributeTest1 failed", e);
    }

    if (!pass)
      throw new Exception("removeAttributeTest1 failed");
  }

  /*
   * @testName: removeAttributeTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:270;
   *
   * @test_Strategy: Call SOAPElement.removeAttribute(QName).
   *
   * Description: Remove the attribute with the specified name from this
   * SOAPElement object.
   *
   */
  @Test
  public void removeAttributeTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"removeAttributeTest2: remove attribute "
          + "with the specified name from this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeAttributeTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeAttributeTest2 failed", e);
    }

    if (!pass)
      throw new Exception("removeAttributeTest2 failed");
  }

  /*
   * @testName: getElementNameTest
   *
   * @assertion_ids: SAAJ:JAVADOC:265;
   *
   * @test_Strategy: Call SOAPElement.getElementName().
   *
   * Description: Returns the name of this SOAPElement object.
   *
   */
  @Test
  public void getElementNameTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "getElementNameTest: return name of " + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getElementNameTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getElementNameTest failed", e);
    }

    if (!pass)
      throw new Exception("getElementNameTest failed");
  }

  /*
   * @testName: getElementQNameTest
   *
   * @assertion_ids: SAAJ:JAVADOC:266;
   *
   * @test_Strategy: Call SOAPElement.getElementQName().
   *
   * Description: Returns the name of this SOAPElement object.
   *
   */
  @Test
  public void getElementQNameTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "getElementQNameTest: return name of " + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getElementQNameTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getElementQNameTest failed", e);
    }

    if (!pass)
      throw new Exception("getElementQNameTest failed");
  }

  /*
   * @testName: addNamespaceDeclarationTest
   *
   * @assertion_ids: SAAJ:JAVADOC:255; SAAJ:JAVADOC:256;
   *
   * @test_Strategy: Call SOAPElement.addNamespaceDeclaration(String, String).
   *
   * Description: Adds a namespace declaration with the specified prefix and URI
   * to this SOAPElement object.
   *
   */
  @Test
  public void addNamespaceDeclarationTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addNamespaceDeclarationTest: add namespace "
          + "declaration with specified prefix and uri");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addNamespaceDeclarationTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addNamespaceDeclarationTest failed", e);
    }

    if (!pass)
      throw new Exception("addNamespaceDeclarationTest failed");
  }

  /*
   * @testName: removeNamespaceDeclarationTest
   *
   * @assertion_ids: SAAJ:JAVADOC:271;
   *
   * @test_Strategy: Call SOAPElement.removeNamespaceDeclaration(String).
   *
   * Description: Removes the namespace declaration corresponding to the given
   * prefix.
   *
   */
  @Test
  public void removeNamespaceDeclarationTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"removeNamespaceDeclarationTest: remove namespace "
          + "declaration with specified prefix");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "removeNamespaceDeclarationTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeNamespaceDeclarationTest failed", e);
    }

    if (!pass)
      throw new Exception("removeNamespaceDeclarationTest failed");
  }

  /*
   * @testName: getNamespacePrefixesTest
   *
   * @assertion_ids: SAAJ:JAVADOC:261;
   *
   * @test_Strategy: Call SOAPElement.getNamespacePrefixes().
   *
   * Description: Return an iterator of namespace prefixes.
   *
   */
  @Test
  public void getNamespacePrefixesTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "getNamespacePrefixesTest: return all " + "namespace prefixes");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNamespacePrefixesTest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getNamespacePrefixesTest failed", e);
    }

    if (!pass)
      throw new Exception("getNamespacePrefixesTest failed");
  }

  /*
   * @testName: getNamespaceURITest
   *
   * @assertion_ids: SAAJ:JAVADOC:260;
   *
   * @test_Strategy: Call SOAPElement.getNamespaceURI(String).
   *
   * Description: Return the URI of the namespace that has the given prefix.
   *
   */
  @Test
  public void getNamespaceURITest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getNamespaceURITest: return the URI "
          + "of the namespace with a given prefix");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getNamespaceURITest");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getNamespaceURITest failed", e);
    }

    if (!pass)
      throw new Exception("getNamespaceURITest failed");
  }

  /*
   * @testName: addTextNodeTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:249; SAAJ:JAVADOC:250;
   *
   * @test_Strategy: Call SOAPElement.addTextNode(String).
   *
   * Description: Creates a new Text object initialized with text and adds it to
   * this SOAPElement object. Add text object to SOAPBody.
   *
   */
  @Test
  public void addTextNodeTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addTextNodeTest1: add text node");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addTextNodeTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addTextNodeTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addTextNodeTest1 failed");
  }

  /*
   * @testName: addTextNodeTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:249; SAAJ:JAVADOC:250;
   *
   * @test_Strategy: Call SOAPElement.addTextNode(String).
   *
   * Description: Creates a new Text object initialized with text and adds it to
   * this SOAPElement object. Add Text object to SOAPHeader. This is legal for a
   * SOAP1.1 message but illegal for a SOAP1.2 message.
   *
   */
  @Test
  public void addTextNodeTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addTextNodeTest2: add text node");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("TESTNAME", "addTextNodeSOAP11Test2");
          props.setProperty("SOAPVERSION", "soap11");
        } else {
          props.setProperty("TESTNAME", "addTextNodeSOAP12Test2");
          props.setProperty("SOAPVERSION", "soap12");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addTextNodeTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addTextNodeTest2 failed");
  }

  /*
   * @testName: setEncodingStyleTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:275; SAAJ:JAVADOC:276;
   *
   * @test_Strategy: Call SOAPElement.setEncodingStyle(String).
   *
   * Description: Sets the encoding style for this SOAPElement object.
   *
   */
  @Test
  public void setEncodingStyleTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setEncodingStyleTest1: set encoding style");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("SOAPVERSION", "soap11");
          props.setProperty("TESTNAME", "setEncodingStyleSOAP11Test1");
        } else {
          props.setProperty("SOAPVERSION", "soap12");
          props.setProperty("TESTNAME", "setEncodingStyleSOAP12Test1");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setEncodingStyleTest1 failed", e);
    }

    if (!pass)
      throw new Exception("setEncodingStyleTest1 failed");
  }

  /*
   * @testName: getEncodingStyleTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:277;
   *
   * @test_Strategy: Call SOAPElement.getEncodingStyle().
   *
   * Description: Sets the encoding style for this SOAPElement object.
   *
   */
  @Test
  public void getEncodingStyleTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getEncodingStyleTest1: get encoding style");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        if (i == 0) {
          props.setProperty("SOAPVERSION", "soap11");
          props.setProperty("TESTNAME", "getEncodingStyleSOAP11Test1");
        } else {
          props.setProperty("SOAPVERSION", "soap12");
          props.setProperty("TESTNAME", "getEncodingStyleSOAP12Test1");
        }
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getEncodingStyleTest1 failed", e);
    }

    if (!pass)
      throw new Exception("getEncodingStyleTest1 failed");
  }

  /*
   * @testName: addChildElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:236; SAAJ:JAVADOC:237;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(Name).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * Name object and adds this new element to this SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest1");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest1 failed");
  }

  /*
   * @testName: addChildElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:240; SAAJ:JAVADOC:241;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(String).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * String object and adds this new element to this SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest2");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest2 failed");
  }

  /*
   * @testName: addChildElementTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:242; SAAJ:JAVADOC:243;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(String, String).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * specified local name and prefix and adds this new element to this
   * SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest3() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest3");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest3");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest3 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest3 failed");
  }

  /*
   * @testName: addChildElementTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:244; SAAJ:JAVADOC:245;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(String, String, String).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * specified local name, prefix, and URI and adds this new element to this
   * SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest4() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest4");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest4");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest4 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest4 failed");
  }

  /*
   * @testName: addChildElementTest5
   *
   * @assertion_ids: SAAJ:JAVADOC:246; SAAJ:JAVADOC:247;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(SOAPElement).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * SOAPElement object and adds this new element to this SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest5() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest5");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest5");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest5 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest5 failed");
  }

  /*
   * @testName: addChildElementTest6
   *
   * @assertion_ids: SAAJ:JAVADOC:238; SAAJ:JAVADOC:239;
   *
   * @test_Strategy: Call SOAPElement.addChildElement(QName).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * QName object and adds this new element to this SOAPElement object.
   *
   */
  @Test
  public void addChildElementTest6() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"addChildElementTest6");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "addChildElementTest6");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("addChildElementTest6 failed", e);
    }

    if (!pass)
      throw new Exception("addChildElementTest6 failed");
  }

  /*
   * @testName: getChildElementsTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:272;
   *
   * @test_Strategy: Call SOAPElement.getChildElements().
   *
   * Description: Returns an Iterator over all the immediate content of this
   * element.
   *
   */
  @Test
  public void getChildElementsTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getChildElementsTest1");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getChildElementsTest1");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getChildElementsTest1 failed", e);
    }

    if (!pass)
      throw new Exception("getChildElementsTest1 failed");
  }

  /*
   * @testName: getChildElementsTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:273;
   *
   * @test_Strategy: Call SOAPElement.getChildElements(Name).
   *
   * Description: Returns an Iterator over all the child elements with the
   * specified name.
   *
   */
  @Test
  public void getChildElementsTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getChildElementsTest2");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getChildElementsTest2");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getChildElementsTest2 failed", e);
    }

    if (!pass)
      throw new Exception("getChildElementsTest2 failed");
  }

  /*
   * @testName: getChildElementsTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:272;
   *
   * @test_Strategy: Call SOAPElement.getChildElements().
   *
   * Description: Returns an Iterator over all the immediate content of this
   * element.
   *
   */
  @Test
  public void getChildElementsTest3() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getChildElementsTest3");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getChildElementsTest3");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getChildElementsTest3 failed", e);
    }

    if (!pass)
      throw new Exception("getChildElementsTest3 failed");
  }

  /*
   * @testName: getChildElementsTest4
   *
   * @assertion_ids: SAAJ:JAVADOC:274;
   *
   * @test_Strategy: Call SOAPElement.getChildElements(QName).
   *
   * Description: Returns an Iterator over all the child elements with the
   * specified name.
   *
   */
  @Test
  public void getChildElementsTest4() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getChildElementsTest4");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getChildElementsTest4");
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }

    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getChildElementsTest4 failed", e);
    }

    if (!pass)
      throw new Exception("getChildElementsTest4 failed");
  }

  /*
   * @testName: removeContentsTest
   *
   * @assertion_ids: SAAJ:JAVADOC:248;
   *
   * @test_Strategy:
   *
   * Description:
   * 
   */
  @Test
  public void removeContentsTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"removeContentsTest");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "removeContentsTest");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("removeContentsTest failed", e);
    }

    if (!pass)
      throw new Exception("removeContestsTest failed");
  }

  /*
   * @testName: getVisibleNamespacePrefixesTest
   *
   * @assertion_ids: SAAJ:JAVADOC:262;
   *
   * @test_Strategy: Call SOAPElement.getVisibleNamespacePrefixes().
   *
   * Description: Return an iterator of visible namespace prefixes.
   *
   */
  @Test
  public void getVisibleNamespacePrefixesTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getVisibleNamespacePrefixesTest: return all "
          + "namespace prefixes");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "getVisibleNamespacePrefixesTest");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getVisibleNamespacePrefixesTest failed", e);
    }

    if (!pass)
      throw new Exception("getVisibleNamespacePrefixesTest failed");
  }

  /*
   * @testName: setElementQNameTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:267;
   *
   * @test_Strategy: Call SOAPElement.setElementQName(QName).
   *
   * Description: Changes the name of this Element if possible.
   *
   */
  @Test
  public void setElementQNameTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "setElementQNameTest1: return qname of " + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setElementQNameTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setElementQNameTest1 failed", e);
    }

    if (!pass)
      throw new Exception("setElementQNameTest1 failed");
  }

  /*
   * @testName: setElementQNameTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:268;
   *
   * @test_Strategy: Call SOAPElement.setElementQName(QName).
   *
   * Description: Changes the name of this Element if possible. Negative test
   * cases for SOAPException to be thrown.
   *
   */
  @Test
  public void setElementQNameTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "setElementQNameTest2: negative test case" + " for SOAPException");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setElementQNameTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setElementQNameTest1 failed", e);
    }

    if (!pass)
      throw new Exception("setElementQNameTest1 failed");
  }

  /*
   * @testName: createQNameTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:263;
   *
   * @test_Strategy: Call SOAPElement.createQName(String, String).
   *
   * Description: Create a qname.
   *
   */
  @Test
  public void createQNameTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "createQNameTest1: return qname of " + "this SOAPElement object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "createQNameTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("createQNameTest1 failed", e);
    }

    if (!pass)
      throw new Exception("createQNameTest1 failed");
  }

  /*
   * @testName: createQNameTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:264;
   *
   * @test_Strategy: Call SOAPElement.createQName(String, String).
   *
   * Description: Create a qname. Negative test case for a SOAPException to be
   * thrown.
   *
   */
  @Test
  public void createQNameTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "createQNameTest2: negative test case" + " for SOAPException");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPELEMENT_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "createQNameTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("createQNameTest2 failed", e);
    }

    if (!pass)
      throw new Exception("createQNameTest2 failed");
  }
}
