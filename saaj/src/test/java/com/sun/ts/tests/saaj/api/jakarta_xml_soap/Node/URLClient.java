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

  private static final String NODE_TESTSERVLET = "/Node_web/NodeTestServlet";

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
  public void detachNodeTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("detachNodeTest: detach a node element object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "detachNodeTest");
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
      throw new Fault("detachNodeTest failed", e);
    }

    if (!pass)
      throw new Fault("detachNodeTest failed");
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
  public void recycleNodeTest() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("recycleNodeTest: recycle a node element object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "recycleNodeTest");
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
      throw new Fault("recycleNodeTest failed", e);
    }

    if (!pass)
      throw new Fault("recycleNodeTest failed");
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
  public void getParentElementTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getParentElementTest1: get parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getParentElementTest1");
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
      throw new Fault("getParentElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getParentElementTest1 failed");
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
  public void getParentElementTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getParentElementTest2: get parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getParentElementTest2");
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
      throw new Fault("getParentElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getParentElementTest2 failed");
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
  public void setParentElementTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("setParentElementTest1: set parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest1");
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
      throw new Fault("setParentElementTest1 failed", e);
    }

    if (!pass)
      throw new Fault("setParentElementTest1 failed");
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
  public void setParentElementTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("setParentElementTest2: set parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest2");
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
      throw new Fault("setParentElementTest2 failed", e);
    }

    if (!pass)
      throw new Fault("setParentElementTest2 failed");
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
  public void setParentElementTest3() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("setParentElementTest3: set parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "setParentElementTest3");
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
      throw new Fault("setParentElementTest3 failed", e);
    }

    if (!pass)
      throw new Fault("setParentElementTest3 failed");
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
  public void getValueTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getValueTest1: get parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getValueTest1");
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
      throw new Fault("getValueTest1 failed", e);
    }

    if (!pass)
      throw new Fault("getValueTest1 failed");
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
  public void getValueTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("getValueTest2: get parent node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      for (int i = 0; i < 2; i++) {
        TestUtil.logMsg("Sending post request to test servlet.....");
        props.setProperty("TESTNAME", "getValueTest2");
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
      throw new Fault("getValueTest2 failed", e);
    }

    if (!pass)
      throw new Fault("getValueTest2 failed");
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
  public void setValueTest1() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("setValueTest1: set value of Node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest1");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setValueTest1 failed", e);
    }

    if (!pass)
      throw new Fault("setValueTest1 failed");
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
  public void setValueTest2() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg("setValueTest2: set value of Node element");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest2");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setValueTest2 failed", e);
    }

    if (!pass)
      throw new Fault("setValueTest2 failed");
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
  public void setValueTest3() throws Fault {
    boolean pass = true;
    try {

      TestUtil.logMsg(
          "setValueTest3: set value of non Text Node (IllegalStateException)");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum, NODE_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      for (int i = 0; i < 2; i++) {
        if (i == 0)
          props.setProperty("SOAPVERSION", "soap11");
        else
          props.setProperty("SOAPVERSION", "soap12");
        props.setProperty("TESTNAME", "setValueTest3");
        urlConn = TestUtil.sendPostData(props, url);
        TestUtil.logMsg("Getting response from test servlet.....");
        Properties resProps = TestUtil.getResponseProperties(urlConn);
        if (!resProps.getProperty("TESTRESULT").equals("pass"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("setValueTest2 failed", e);
    }

    if (!pass)
      throw new Fault("setValueTest2 failed");
  }
}
