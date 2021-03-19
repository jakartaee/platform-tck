/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: Client.java.se 51058 2006-02-11 20:00:31Z adf $
 */

package com.sun.ts.tests.jaxws.se.api.jakarta_xml_ws.Endpoint;

import java.util.concurrent.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.sharedclients.HttpClient;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.nio.charset.Charset;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String ENDPOINTPUBLISHPROP = "http.server.supports.endpoint.publish";

  private boolean endpointPublishSupport;

  // service and port info
  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  // Endpoint info
  private Endpoint endpoint = null;

  private static final Object IMPLEMENTOR = new com.sun.ts.tests.jaxws.se.api.jakarta_xml_ws.Endpoint.HelloImpl();

  private static final String CONTEXTROOT = "/WSEndpoint";

  private static final String URL_ALIAS = "/jaxws/Hello";

  private String ts_home;

  private String sepChar = System.getProperty("file.separator");

  private String testDir = "src" + sepChar + "com" + sepChar + "sun" + sepChar
      + "ts" + sepChar + "tests" + sepChar + "jaxws" + sepChar + "se" + sepChar
      + "api" + sepChar + "jakarta_xml_ws" + sepChar + "Endpoint";

  private TSURL ctsurl = new TSURL();

  private String url = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // SE URL properties used by the test
  private static final String SE_ENDPOINT_URL = "/WSEndpoint/jaxws/Hello";

  private static final String SE_WSDLLOC_URL = "/WSEndpoint/jaxws/Hello?wsdl";

  private URL wsdlurl = null;

  private int javaseServerPort;

  private String helloReq = "<?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S='http://schemas.xmlsoap.org/soap/envelope/'><S:Body><ns2:hello xmlns:ns2='http://helloservice.org/wsdl'><arg0>you</arg0></ns2:hello><S:Body></S:Envelope>";

  private EndpointReference epr = null;

  private boolean makeHTTPRequest(String request, String url) throws Exception {
    boolean pass = true;
    TestUtil.logMsg("HTTP REQUEST: " + request);
    HttpClient httpClient = new HttpClient();
    httpClient.setUrl(url);
    InputStream response = httpClient
        .makeRequest(getInputStreamForString(request));
    if (response != null) {
      ByteArrayOutputStream baos = getInputStreamAsOutputStream(response);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      TestUtil.logMsg("HTTP RESPONSE: " + baos.toString());
      if (baos.toString().indexOf("Hello, you") < 0) {
        TestUtil.logErr(
            "HTTP RESPONSE does not contain expected string -> Hello, you");
        pass = false;
      }
    } else {
      TestUtil.logErr("HTTP RESPONSE no response returned");
      pass = false;
    }
    try {
      int status = httpClient.getStatusCode();
      TestUtil.logMsg("HTTP STATUS CODE: " + status);
      if (status != 200) {
        TestUtil.logErr("HTTP STATUS CODE is not okay");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil
          .logErr("unable to get HTTP STATUS CODE, exception occurred: " + e);
      pass = false;
    }
    return pass;
  }

  private ByteArrayInputStream getInputStreamForString(String request)
      throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    OutputStreamWriter osw;
    osw = new OutputStreamWriter(bos, Charset.forName("UTF-8"));
    osw.write(request);
    osw.flush();
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    return bis;
  }

  private ByteArrayOutputStream getInputStreamAsOutputStream(InputStream is)
      throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    do {
      length = is.read(buffer);
      if (length > 0) {
        baos.write(buffer, 0, length);
      }
    } while (length > 0);
    return baos;
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    url = ctsurl.getURLString(PROTOCOL, hostname, javaseServerPort,
        SE_ENDPOINT_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, javaseServerPort,
        SE_WSDLLOC_URL);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   * http.server.supports.endpoint.publish; ts.home;
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
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      endpointPublishSupport = Boolean
          .parseBoolean(p.getProperty(ENDPOINTPUBLISHPROP));
      ts_home = p.getProperty("ts.home");
      TestUtil.logMsg("ts_home=" + ts_home);
      javaseServerPort = JAXWS_Util.getFreePort();
      if (javaseServerPort <= 0) {
        TestUtil.logErr("Free port not found.");
        pass = false;
      }
      getTestURLs();
      endpoint = Endpoint.create(SOAPBinding.SOAP11HTTP_BINDING, IMPLEMENTOR);
      if (endpoint == null)
        throw new Fault("setup failed, unable to create endpoint");
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
   * @testName: createTest
   *
   * @assertion_ids: JAXWS:JAVADOC:12; JAXWS:JAVADOC:13; JAXWS:JAVADOC:115;
   *
   * @test_Strategy:
   */
  public void createTest() throws Fault {
    TestUtil.logTrace("createTest");
    boolean pass = true;
    try {
      Endpoint theEndpoint = Endpoint.create(IMPLEMENTOR);
      TestUtil.logMsg("endpoint=" + theEndpoint);
      if (theEndpoint == null) {
        TestUtil.logErr("Endpoint.create returned null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("createTest failed", e);
    }

    if (!pass)
      throw new Fault("createTest failed");
  }

  /*
   * @testName: createTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:13; JAXWS:JAVADOC:14; JAXWS:JAVADOC:115;
   *
   * @test_Strategy:
   */
  public void createTest2() throws Fault {
    TestUtil.logTrace("createTest2");
    boolean pass = true;
    try {
      Endpoint theEndpoint = Endpoint.create(SOAPBinding.SOAP11HTTP_BINDING,
          IMPLEMENTOR);
      TestUtil.logMsg("endpoint=" + theEndpoint);
      if (theEndpoint == null) {
        TestUtil.logErr("Endpoint.create returned null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("createTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createTest2 failed");
  }

  /*
   * @testName: getBindingTest
   *
   * @assertion_ids: JAXWS:JAVADOC:15;
   *
   * @test_Strategy:
   */
  public void getBindingTest() throws Fault {
    TestUtil.logTrace("getBindingTest");
    boolean pass = true;
    try {
      Binding binding = endpoint.getBinding();
      TestUtil.logMsg("binding=" + binding);
      if (binding == null) {
        TestUtil.logErr("Endpoint.getBinding returned null");
        pass = false;
      }
      if (!(binding instanceof SOAPBinding)) {
        TestUtil.logErr("binding is not an instance of SOAPBinding");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getBindingTest failed", e);
    }

    if (!pass)
      throw new Fault("getBindingTest failed");
  }

  /*
   * @testName: getImplementorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:17;
   *
   * @test_Strategy:
   */
  public void getImplementorTest() throws Fault {
    TestUtil.logTrace("getImplementorTest");
    boolean pass = true;
    try {
      Object implementor = endpoint.getImplementor();
      TestUtil.logMsg("implementor=" + implementor);
      if (implementor == null) {
        TestUtil.logErr("Endpoint.getImplementor returned null");
        pass = false;
      }
      if (!(implementor instanceof com.sun.ts.tests.jaxws.se.api.jakarta_xml_ws.Endpoint.HelloImpl)) {
        TestUtil.logErr("binding is not an instance of HelloImpl");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getImplementorTest failed", e);
    }

    if (!pass)
      throw new Fault("getImplementorTest failed");
  }

  public void collectMetadata(File wsdlDirFile, List<File> metadataFile) {
    File[] files = wsdlDirFile.listFiles();
    if (files == null) {
      TestUtil.logMsg("no metadata files to collect");
      return;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        // collectMetadata(file, metadataFile);
        continue;
      }
      if (file.getName().endsWith(".xsd") || file.getName().endsWith(".wsdl")) {
        TestUtil.logMsg("collectMetadata: adding file " + file.getName());
        metadataFile.add(file);
      }
    }
  }

  /*
   * @testName: publishTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:21; JAXWS:JAVADOC:27; JAXWS:SPEC:5005;
   * JAXWS:SPEC:5007; JAXWS:SPEC:5008; JAXWS:SPEC:5017; JAXWS:SPEC:5018;
   * JAXWS:SPEC:5019; JAXWS:SPEC:5020; JAXWS:SPEC:5021;
   *
   * @test_Strategy:
   */
  public void publishTest2() throws Fault {
    TestUtil.logTrace("publishTest2");
    boolean pass = true;
    try {
      endpoint.publish(url);
      if (modeProperty.equals("jakartaEE") || !endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.logMsg("invoke hello operation of published endpoint");
        if (makeHTTPRequest(helloReq, url))
          TestUtil.logMsg("Successful invocation of published endpoint");
        else {
          TestUtil.logErr("Unsuccessful invocation of published endpoint");
          pass = false;
        }
        endpoint.stop();
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("publishTest2 failed");
  }

  /*
   * @testName: publishTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:22; JAXWS:JAVADOC:27; JAXWS:JAVADOC:114;
   * JAXWS:SPEC:5004; JAXWS:SPEC:6002; JAXWS:SPEC:5005; JAXWS:SPEC:5007;
   * JAXWS:SPEC:5008; JAXWS:SPEC:5017; JAXWS:SPEC:5018; JAXWS:SPEC:5019;
   * JAXWS:SPEC:5020; JAXWS:SPEC:5021;
   *
   * @test_Strategy:
   */
  public void publishTest3() throws Fault {
    TestUtil.logTrace("publishTest3");
    boolean pass = true;
    try {
      endpoint = Endpoint.publish(url, IMPLEMENTOR);
      if (modeProperty.equals("jakartaEE") || !endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.logMsg("invoke hello operation of published endpoint");
        if (makeHTTPRequest(helloReq, url))
          TestUtil.logMsg("Successful invocation of published endpoint");
        else {
          TestUtil.logErr("Unsuccessful invocation of published endpoint");
          pass = false;
        }
        endpoint.stop();
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("publishTest3 failed");
  }

  /*
   * @testName: stopTest
   *
   * @assertion_ids: JAXWS:JAVADOC:21; JAXWS:JAVADOC:27; JAXWS:SPEC:5005;
   * JAXWS:SPEC:5007; JAXWS:SPEC:5008; JAXWS:SPEC:5017; JAXWS:SPEC:5018;
   * JAXWS:SPEC:5019; JAXWS:SPEC:5020; JAXWS:SPEC:5021;
   *
   * @test_Strategy:
   */
  public void stopTest() throws Fault {
    TestUtil.logTrace("stopTest");
    boolean pass = true;
    try {
      endpoint.publish(url);
      if (modeProperty.equals("jakartaEE") || !endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.logMsg("invoke hello operation of published endpoint");
        if (makeHTTPRequest(helloReq, url))
          TestUtil.logMsg("Successful invocation of published endpoint");
        else {
          TestUtil.logErr("Unsuccessful invocation of published endpoint");
          pass = false;
        }
        endpoint.stop();
        TestUtil.logMsg(
            "invoke hello operation of published endpoint that is stopped");
        if (makeHTTPRequest(helloReq, url)) {
          TestUtil.logErr(
              "Successful invocation of a stopped endpoint - unexpected ");
          pass = false;
        } else
          TestUtil.logMsg(
              "Unsuccessful invocation of a stopped endpoint - expected ");
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("stopTest failed");
  }

  /*
   * @testName: isPublishedTest
   *
   * @assertion_ids: JAXWS:JAVADOC:20;
   *
   * @test_Strategy:
   */
  public void isPublishedTest() throws Fault {
    TestUtil.logTrace("isPublishedTest");
    boolean pass = true;
    try {
      boolean isPub = endpoint.isPublished();
      if (isPub) {
        TestUtil.logErr("Endpoint is published - unexpected");
        pass = false;
      } else
        TestUtil.logMsg("Endpoint is not published - expected");
      endpoint.publish(url);
      if (modeProperty.equals("jakartaEE") || !endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else if (modeProperty.equals("standalone") && endpointPublishSupport) {
        isPub = endpoint.isPublished();
        if (!isPub) {
          TestUtil.logErr("Endpoint is not published - unexpected");
          pass = false;
        } else
          TestUtil.logMsg("Endpoint is published - expected");
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("isPublishedTest failed");
  }

  /*
   * @testName: GetSetPropertiesTest
   *
   * @assertion_ids: JAXWS:JAVADOC:19; JAXWS:JAVADOC:26;
   *
   * @test_Strategy:
   */
  public void GetSetPropertiesTest() throws Fault {
    TestUtil.logTrace("GetSetPropertiesTest");
    boolean pass = true;
    try {
      Map<String, Object> map = endpoint.getProperties();
      if (map == null) {
        map = new HashMap<String, Object>();
        endpoint.setProperties(map);
      }
      map.put(Endpoint.WSDL_SERVICE, SERVICE_QNAME);
      map.put(Endpoint.WSDL_PORT, PORT_QNAME);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("GetSetPropertiesTest failed", e);
    }

    if (!pass)
      throw new Fault("GetSetPropertiesTest failed");
  }

  /*
   * @testName: GetSetExecutorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:16; JAXWS:JAVADOC:24; JAXWS:SPEC:5011;
   * JAXWS:SPEC:5012;
   *
   * @test_Strategy:
   */
  public void GetSetExecutorTest() throws Fault {
    TestUtil.logTrace("GetSetExecutorTest");
    boolean pass = true;
    try {
      Executor executor = endpoint.getExecutor();
      if (executor != null) {
        TestUtil.logMsg("set same Executor");
        endpoint.setExecutor(executor);
      } else {
        ExecutorService appExecutorService = Executors.newFixedThreadPool(5);
        endpoint.setExecutor(appExecutorService);
        TestUtil.logMsg("set new Executor");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("GetSetExecutorTest failed", e);
    }

    if (!pass)
      throw new Fault("GetSetExecutorTest failed");
  }

  /*
   * @testName: GetSetMetaDataTest
   *
   * @assertion_ids: JAXWS:JAVADOC:18; JAXWS:JAVADOC:25; JAXWS:SPEC:5010;
   * JAXWS:SPEC:5011;
   *
   * @test_Strategy:
   */
  public void GetSetMetaDataTest() throws Fault {
    TestUtil.logTrace("GetSetMetaDataTest");
    boolean pass = true;
    File metaGood = new File(ts_home + sepChar + testDir);
    File metaBad = new File(ts_home + sepChar + testDir + sepChar
        + "contentRoot" + sepChar + "mydocs");
    TestUtil.logMsg("metaGood=" + metaGood);
    TestUtil.logMsg("metaBad=" + metaBad);
    try {
      TestUtil.logMsg("Testing GetSetMetaData using good MetaData");
      List<Source> metadata = endpoint.getMetadata();
      List<File> metadataFile = new ArrayList();
      collectMetadata(metaGood, metadataFile);
      if (metadataFile.size() > 0) {
        metadata = new ArrayList<Source>();
        for (File file : metadataFile) {
          Source source = new StreamSource(new FileInputStream(file));
          source.setSystemId(file.toURL().toExternalForm());
          metadata.add(source);
        }
        endpoint.setMetadata(metadata);
        if (endpointPublishSupport) {
          TestUtil.logMsg("Publishing endpoint to url: " + url);
          endpoint.publish(url);
          TestUtil.logMsg("Invoking published endpoint");
          if (makeHTTPRequest(helloReq, url))
            TestUtil.logMsg("Successful invocation of published endpoint");
          else {
            TestUtil.logMsg("Unsuccessful invocation of published endpoint");
            pass = false;
          }
          TestUtil.logMsg("Stopping endpoint");
          endpoint.stop();
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("GetSetMetaDataTest failed", e);
    }
    try {
      TestUtil.logMsg("Testing GetSetMetaData using bad MetaData");
      List<Source> metadata = endpoint.getMetadata();
      List<File> metadataFile = new ArrayList();
      collectMetadata(metaBad, metadataFile);
      if (metadataFile.size() > 0) {
        metadata = new ArrayList<Source>();
        for (File file : metadataFile) {
          Source source = new StreamSource(new FileInputStream(file));
          source.setSystemId(file.toURL().toExternalForm());
          metadata.add(source);
        }
        endpoint.setMetadata(metadata);
        if (endpointPublishSupport) {
          try {
            TestUtil.logMsg("Publishing endpoint to url: " + url);
            endpoint.publish(url);
            TestUtil.logMsg("Stopping endpoint");
            endpoint.stop();
            pass = false;
            TestUtil.logErr("publishing should have failed with bad metadata");
          } catch (Exception e) {
            TestUtil.logMsg("Got expected exception on bad metadata");
          }
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("GetSetMetaDataTest failed", e);
    }

    if (!pass)
      throw new Fault("GetSetMetaDataTest failed");
  }

  /*
   * @testName: getEndpointReferenceParamsTest
   *
   * @assertion_ids: JAXWS:JAVADOC:137;
   *
   * @test_Strategy:
   */
  public void getEndpointReferenceParamsTest() throws Fault {
    TestUtil.logTrace("getEndpointReferenceParamsTest");
    boolean pass = true;
    if (modeProperty.equals("jakartaEE")) {
      TestUtil.logMsg("Not tested in jakartaEE platform");
      pass = false;
    }
    try {
      endpoint.publish(url);
      if (!endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else {
        epr = endpoint.getEndpointReference();
        TestUtil.logMsg("EndpointReference object=" + epr);
        if (epr == null) {
          TestUtil.logErr("getEndpointReference() returned null");
          pass = false;
        } else
          TestUtil.logMsg(
              "getEndpointReference() returned EndpointReference object: "
                  + epr);
        if (epr instanceof W3CEndpointReference)
          TestUtil.logMsg("epr instanceof W3CEndpointReference");
        else {
          TestUtil.logErr("epr not instanceof W3CEndpointReference");
          pass = false;
        }
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("getEndpointReferenceParamsTest failed");
  }

  /*
   * @testName: getEndpointReferenceClassTest
   *
   * @assertion_ids: JAXWS:JAVADOC:138;
   *
   * @test_Strategy:
   */
  public void getEndpointReferenceClassTest() throws Fault {
    TestUtil.logTrace("getEndpointReferenceClassTest");
    boolean pass = true;
    if (modeProperty.equals("jakartaEE")) {
      TestUtil.logMsg("Not tested in jakartaEE platform");
      pass = false;
    }
    try {
      endpoint.publish(url);
      if (!endpointPublishSupport) {
        TestUtil
            .logErr("expected exception when endpoint publish not supported");
        pass = false;
      } else {
        epr = endpoint.getEndpointReference(W3CEndpointReference.class);
        TestUtil.logMsg("EndpointReference object=" + epr);
        if (epr == null) {
          TestUtil.logErr("getEndpointReference() returned null");
          pass = false;
        } else
          TestUtil.logMsg(
              "getEndpointReference() returned EndpointReference object: "
                  + epr);
        if (epr instanceof W3CEndpointReference)
          TestUtil.logMsg("epr instanceof W3CEndpointReference");
        else {
          TestUtil.logErr("epr not instanceof W3CEndpointReference");
          pass = false;
        }
      }
    } catch (Exception e) {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("getEndpointReferenceClassTest failed");
  }

}
