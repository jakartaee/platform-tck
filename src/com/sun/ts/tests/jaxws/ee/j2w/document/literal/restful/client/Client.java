/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jaxws.ee.j2w.document.literal.restful.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.dom.Node;

import java.net.URL;
import java.net.HttpURLConnection;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import java.util.Properties;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.sharedclients.HttpClient;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.j2w.document.literal.restful.client.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wdlrestful.endpoint.1";

  private String url = null;

  /*
   * GET http://host:port/WSJ2WDLRESTFUL/jaxws/tokens?token=1
   * http://host:port/WSJ2WDLRESTFUL/jaxws/tokens/token/1
   *
   * PUT http://host:port/WSJ2WDLRESTFUL/jaxws/tokens?token=15&value=1000
   * http://host:port/WSJ2WDLRESTFUL/jaxws/tokens/token/16/value/1001
   *
   * DELETE http://host:port/WSJ2WDLRESTFUL/jaxws/tokens?token=15
   * http://host:port/WSJ2WDLRESTFUL/jaxws/tokens/token/16
   */

  private static String queryString = "?token=1";

  private static String pathInfo = "/token/1";

  private static String putqueryString = "?token=15&value=1000";

  private static String putpathInfo = "/token/16/value/1001";

  private static String putgetqueryString = "?token=15";

  private static String putgetpathInfo = "/token/16";

  private static String deletequeryString = "?token=5";

  private static String deletegetqueryString = "?token=5";

  HttpClient httpClient;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
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
        TestUtil.printStackTrace(e);
        pass = false;
      }
      getTestURLs();
      httpClient = new HttpClient();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
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
   * @testName: testGETwithQUERYSTRING
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testGETwithQUERYSTRING() throws Fault {
    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + queryString);
      httpClient.setMethod("GET");
      process();
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testGETwithQUERYSTRING failed");
  }

  /*
   * @testName: testGETwithPATHINFO
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testGETwithPATHINFO() throws Fault {

    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + pathInfo);
      httpClient.setMethod("GET");
      process();
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testGETwithPATHINFO failed");
  }

  /*
   * @testName: testPUTwithQUERYSTRING
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testPUTwithQUERYSTRING() throws Fault {
    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + putqueryString);
      httpClient.setMethod("PUT");
      process();
      httpClient.setUrl(url.toString() + putgetqueryString);
      httpClient.setMethod("GET");
      process();
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testPUTwithQUERYSTRING failed");
  }

  /*
   * @testName: testPUTwithPATHINFO
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testPUTwithPATHINFO() throws Fault {

    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + putpathInfo);
      httpClient.setMethod("PUT");
      process();
      httpClient.setUrl(url.toString() + putgetpathInfo);
      httpClient.setMethod("GET");
      process();
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testPUTwithPATHINFO failed");
  }

  /*
   * @testName: testDELETEwithQUERYSTRING
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testDELETEwithQUERYSTRING() throws Fault {
    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + deletequeryString);
      httpClient.setMethod("DELETE");
      process();
      httpClient.setUrl(url.toString() + deletegetqueryString);
      httpClient.setMethod("GET");
      process();
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testDELETEwithQUERYSTRING failed");
  }

  /*
   * @testName: testPOST
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void testPOST() throws Fault {

    boolean pass = true;
    try {
      httpClient.setUrl(url.toString() + putpathInfo);
      httpClient.setMethod("POST");
      // TODO
    } catch (Exception e) {
      e.printStackTrace();
      pass = false;
    }
    if (!pass)
      throw new Fault("testPOST failed");
  }

  private Source process() throws Exception {
    return process(null);
  }

  private Source process(InputStream is) throws Exception {
    InputStream responseIs = httpClient.makeRequest(is);
    StreamSource source = new StreamSource(responseIs);
    printSource(source);
    return source;
  }

  private void printSource(Source source) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      StreamResult sr = new StreamResult(bos);
      Transformer trans = TransformerFactory.newInstance().newTransformer();
      Properties oprops = new Properties();
      oprops.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.setOutputProperties(oprops);
      trans.transform(source, sr);
      System.out.println("**** Response ******" + bos.toString());
      System.out.println("");
      bos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
