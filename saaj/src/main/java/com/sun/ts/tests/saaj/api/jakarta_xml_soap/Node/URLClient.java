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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Node;

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

  private static final String NODE_TESTSERVLET = "/Node_web/NodeTestServlet";

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
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "Node_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.Node");
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
   * @testName: detachNodeTest
   *
   * @assertion_ids: SAAJ:JAVADOC:304;
   *
   * @test_Strategy: Call Node.detachNode() method and verify that the given
   * node is indeed removed.
   *
   * Description: Through the Node interface you can detach a node element
   *
   */
  @Test
  public void detachNodeTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"detachNodeTest: detach a node element object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "detachNodeTest");
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
      throw new Exception("detachNodeTest failed", e);
    }

    if (!pass)
      throw new Exception("detachNodeTest failed");
  }

  /*
   * @testName: recycleNodeTest
   *
   * @assertion_ids: SAAJ:JAVADOC:305;
   *
   * @test_Strategy: Call Node.recycleNode() method and verify that the given
   * node is indeed recycled.
   *
   * Description: Through the Node interface you can recycle a node element
   *
   */
  @Test
  public void recycleNodeTest() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"recycleNodeTest: recycle a node element object");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "recycleNodeTest");
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
      throw new Exception("recycleNodeTest failed", e);
    }

    if (!pass)
      throw new Exception("recycleNodeTest failed");
  }

  /*
   * @testName: getParentElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:303;
   *
   * @test_Strategy: Call Node.getParentElement() method on an element with a
   * parent and verify that the correct parent node is indeed returned.
   *
   * Description: Through the Node interface you can get parent node element of
   * a Node that has a parent.
   *
   */
  @Test
  public void getParentElementTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getParentElementTest1: get parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getParentElementTest1");
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
      throw new Exception("getParentElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("getParentElementTest1 failed");
  }

  /*
   * @testName: getParentElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:303;
   *
   * @test_Strategy: Call Node.getParentElement() method on an element without a
   * parent and verify that the parent node returned is null.
   *
   * Description: Through the Node interface you cannot get parent node element
   * of a Node that does not have a parent.
   *
   */
  @Test
  public void getParentElementTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getParentElementTest2: get parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getParentElementTest2");
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
      throw new Exception("getParentElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("getParentElementTest2 failed");
  }

  /*
   * @testName: setParentElementTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:301;
   *
   * @test_Strategy: Call Node.setParentElementTest(SOAPElement) method on an
   * element without a parent and verify that the parent is set correctly.
   *
   * Description: Through the Node interface you can set parent node element
   *
   */
  @Test
  public void setParentElementTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setParentElementTest1: set parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest1");
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
      throw new Exception("setParentElementTest1 failed", e);
    }

    if (!pass)
      throw new Exception("setParentElementTest1 failed");
  }

  /*
   * @testName: setParentElementTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:302;
   *
   * @test_Strategy: Call Node.setParentElementTest(SOAPElement) method on an
   * element without a parent to an improper parent and verify that a
   * SOAPException condition occurs.
   *
   * Description: Through the Node interface you cannot set parent node element
   * to an improper parent.
   *
   */
  @Test
  public void setParentElementTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setParentElementTest2: set parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest2");
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
      throw new Exception("setParentElementTest2 failed", e);
    }

    if (!pass)
      throw new Exception("setParentElementTest2 failed");
  }

  /*
   * @testName: setParentElementTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:302;
   *
   * @test_Strategy: Call Node.setParentElementTest(SOAPElement) method on an
   * element without a parent to a null parent and verify that a SOAPException
   * condition occurs.
   *
   * Description: Through the Node interface you cannot set parent node element
   * to a null parent.
   *
   */
  @Test
  public void setParentElementTest3() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setParentElementTest3: set parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest3");
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
      throw new Exception("setParentElementTest3 failed", e);
    }

    if (!pass)
      throw new Exception("setParentElementTest3 failed");
  }

  /*
   * @testName: getValueTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:299;
   *
   * @test_Strategy: Call Node.getValue() method on an element with no child
   * element and verify that null is returned.
   *
   * Description: Through the Node interface you cannot get the value of the
   * immediate child of a node element if a child does not exist.
   *
   */
  @Test
  public void getValueTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getValueTest1: get parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getValueTest1");
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
      throw new Exception("getValueTest1 failed", e);
    }

    if (!pass)
      throw new Exception("getValueTest1 failed");
  }

  /*
   * @testName: getValueTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:299;
   *
   * @test_Strategy: Call Node.getValue() method on an element with a child
   * element and verify that proper value is returned.
   *
   * Description: Through the Node interface you can get the value of the
   * immediate child of a node element if a child exists.
   *
   */
  @Test
  public void getValueTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"getValueTest2: get parent node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      for (int i = 0; i < 2; i++) {
        logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getValueTest2");
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
      throw new Exception("getValueTest2 failed", e);
    }

    if (!pass)
      throw new Exception("getValueTest2 failed");
  }

  /*
   * @testName: setValueTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:300;
   *
   * @test_Strategy: Call Node.setValue() method to set the value of this node
   * element.
   *
   * Description: Sets the value of this node if this is a Text node or the
   * immediate child of this node otherwise.
   *
   */
  @Test
  public void setValueTest1() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setValueTest1: set value of Node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest1");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setValueTest1 failed", e);
    }

    if (!pass)
      throw new Exception("setValueTest1 failed");
  }

  /*
   * @testName: setValueTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:300;
   *
   * @test_Strategy: Call Node.setValue() method to set the value of this node
   * element.
   *
   * Description: Sets the value of this node if this is a Text node or the
   * immediate child of this node otherwise.
   *
   */
  @Test
  public void setValueTest2() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,"setValueTest2: set value of Node element");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest2");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setValueTest2 failed", e);
    }

    if (!pass)
      throw new Exception("setValueTest2 failed");
  }

  /*
   * @testName: setValueTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:208;
   *
   * @test_Strategy: Call Node.setValue() method to set the value of this node
   * element. Try setting value on a Node which is not a Text Node. Should get
   * an IllegalStateException thrown.
   *
   * Description: If the node is not a Text node and either has more than one
   * child node or has a child node that is not a Text node then an
   * IllegalStateException MUST be thrown.
   *
   */
  @Test
  public void setValueTest3() throws Exception {
    boolean pass = true;
    try {

      logger.log(Logger.Level.INFO,
          "setValueTest3: set value of non Text Node (IllegalStateException)");
      logger.log(Logger.Level.INFO,"Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      logger.log(Logger.Level.INFO,url.toString());
      logger.log(Logger.Level.INFO,"Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest3");
        urlConn = TestUtil.sendPostData(props, url);
        logger.log(Logger.Level.INFO,"Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("setValueTest2 failed", e);
    }

    if (!pass)
      throw new Exception("setValueTest2 failed");
  }
}
