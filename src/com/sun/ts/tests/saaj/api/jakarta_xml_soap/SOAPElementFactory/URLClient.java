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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElementFactory;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class URLClient extends EETest {
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  private static final String SOAPELEMENTFACTORY_TESTSERVLET = "/SOAPElementFactory_web/SOAPElementFactoryTestServlet";

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
   * @testName: newInstanceTest
   *
   * @assertion_ids: SAAJ:JAVADOC:83; SAAJ:JAVADOC:84;
   *
   * @test_Strategy: Call SOAPElementFactory.newInstance().
   *
   * Description: Creates a new SOAPElementFactory object.
   *
   */
  public void newInstanceTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("newInstanceTest: create a SOAPElementFactory object");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPELEMENTFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "newInstanceTest");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("newInstanceTest failed", e);
    }

    if (!pass)
      throw new Fault("newInstanceTest failed");
  }

  /*
   * @testName: createTest1
   *
   * @assertion_ids: SAAJ:JAVADOC:77; SAAJ:JAVADOC:78;
   *
   * @test_Strategy: Call SOAPElementFactory.create(Name).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * Name object.
   *
   */
  public void createTest1() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createTest1: create a SOAPElement object constructor1");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPELEMENTFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "createTest1");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("createTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createTest1 failed");
  }

  /*
   * @testName: createTest2
   *
   * @assertion_ids: SAAJ:JAVADOC:79; SAAJ:JAVADOC:80;
   *
   * @test_Strategy: Call SOAPElementFactory.create(String).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * local name.
   *
   */
  public void createTest2() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createTest2: create a SOAPElement object constructor2");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPELEMENTFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "createTest2");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("createTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createTest2 failed");
  }

  /*
   * @testName: createTest3
   *
   * @assertion_ids: SAAJ:JAVADOC:81; SAAJ:JAVADOC:82;
   *
   * @test_Strategy: Call SOAPElementFactory.create(String, String, String).
   *
   * Description: Creates a new SOAPElement object initialized with the given
   * local name, prefix, and uri.
   *
   */
  public void createTest3() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("createTest3: create a SOAPElement object constructor3");
      TestUtil.logMsg("Creating url to test servlet.....");
      url = tsurl.getURL(PROTOCOL, hostname, portnum,
          SOAPELEMENTFACTORY_TESTSERVLET);
      TestUtil.logMsg(url.toString());
      TestUtil.logMsg("Sending post request to test servlet.....");
      props.setProperty("TESTNAME", "createTest3");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil.logMsg("Getting response from test servlet.....");
      Properties resProps = TestUtil.getResponseProperties(urlConn);
      if (!resProps.getProperty("TESTRESULT").equals("pass"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("createTest3 failed", e);
    }

    if (!pass)
      throw new Fault("createTest3 failed");
  }
}
