/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.spec.filter.interceptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.impl.StringDataSource;
import com.sun.ts.tests.jaxrs.common.provider.StringBean;
import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * Test the interceptor is called when any entity provider is called
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 3841348335979312551L;

  public static final String plaincontent = JAXRSClient.class.getName();

  public static final String content = "<content>" + plaincontent
      + "</content>";

  private static String getJaxbToken() {
    JAXBElement<String> element = new JAXBElement<String>(new QName("content"),
        String.class, plaincontent);
    try {
      JAXBContext context = JAXBContext.newInstance(String.class);
      java.io.StringWriter writer = new java.io.StringWriter();
      context.createMarshaller().marshal(element, writer);
      return writer.toString();
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public JAXRSClient() {
    setContextRoot("/jaxrs_spec_filter_interceptor_web/resource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: byteArrayReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void byteArrayReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postbytearray"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered reader interceptor for bytearray provider");
  }

  /*
   * @testName: byteArrayReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void byteArrayReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postbytearray"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: byteArrayWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void byteArrayWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getbytearray"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for bytearray provider");
  }

  /*
   * @testName: byteArrayWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void byteArrayWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getbytearray"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: byteArrayWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void byteArrayWriterClientInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    addProvider(EntityWriterInterceptor.class);
    setRequestContentEntity(content.getBytes());
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for bytearray provider");
  }

  // ------------------------- String -----------------------------------

  /*
   * @testName: stringReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void stringReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered reader interceptor for string provider");
  }

  /*
   * @testName: stringReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void stringReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: stringWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void stringWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getstring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for string provider");
  }

  /*
   * @testName: stringWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void stringWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getstring"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: stringWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void stringWriterClientInterceptorTest() throws Fault {
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setRequestContentEntity(content);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for string provider");
  }

  // ------------------------- InputStream -----------------------------------

  /*
   * @testName: inputStreamReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void inputStreamReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "postinputstream"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered reader interceptor for inputstream provider");
  }

  /*
   * @testName: inputStreamReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void inputStreamReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "postinputstream"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: inputStreamWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void inputStreamWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getinputstream"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for inputstream provider");
  }

  /*
   * @testName: inputStreamWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void inputStreamWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getinputstream"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: inputStreamWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void inputStreamWriterClientInterceptorTest() throws Fault {
    ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes());
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setRequestContentEntity(stream);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for inputstream provider");
  }

  // ------------------------- Reader -----------------------------------
  /*
   * @testName: readerReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void readerReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postreader"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered reader interceptor for reader provider");
  }

  /*
   * @testName: readerReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void readerReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postreader"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: readerWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void readerWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getreader"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for reader provider");
  }

  /*
   * @testName: readerWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void readerWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getreader"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: readerWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void readerWriterClientInterceptorTest() throws Fault {
    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
    InputStreamReader reader = new InputStreamReader(bais);
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setRequestContentEntity(reader);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for reader provider");
  }

  // ------------------------- File -----------------------------------

  /*
   * @testName: fileReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void fileReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postfile"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered reader interceptor for file provider");
  }

  /*
   * @testName: fileReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void fileReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postfile"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: fileWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void fileWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getfile"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for file provider");
  }

  /*
   * @testName: fileWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void fileWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getfile"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: fileWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void fileWriterClientInterceptorTest() throws Fault {
    try {
      File file = File.createTempFile("temp", "tmp");
      FileWriter fw = new FileWriter(file);
      fw.append(content);
      fw.close();
      setRequestContentEntity(file);
    } catch (IOException e) {
      throw new Fault(e);
    }
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for file provider");
  }

  // ------------------------- DataSource -----------------------------------

  /*
   * @testName: dataSourceReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void dataSourceReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postdatasource"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered reader interceptor for dataSource provider");
  }

  /*
   * @testName: dataSourceReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void dataSourceReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postdatasource"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: dataSourceWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void dataSourceWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getdatasource"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for dataSource provider");
  }

  /*
   * @testName: dataSourceWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void dataSourceWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getdatasource"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: dataSourceWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void dataSourceWriterClientInterceptorTest() throws Fault {
    StringDataSource source = new StringDataSource(content,
        MediaType.WILDCARD_TYPE);
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setRequestContentEntity(source);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postdatasource"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for dataSource provider");
  }

  // ------------------------- Source -----------------------------------

  /*
   * @testName: sourceWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void sourceWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getsource"));
    setProperty(Property.REQUEST_HEADERS, buildAccept(MediaType.TEXT_XML_TYPE));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for source provider");
  }

  /*
   * @testName: sourceWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void sourceWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getsource"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    setProperty(Property.REQUEST_HEADERS, buildAccept(MediaType.TEXT_XML_TYPE));
    invoke();
  }

  // ------------------------- JAXBElement -----------------------------------

  /*
   * @testName: jaxbReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void jaxbReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postjaxb"));
    setRequestContentEntity(getJaxbToken());
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.REQUEST_HEADERS,
        buildContentType(MediaType.TEXT_XML_TYPE));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered reader interceptor for jaxb provider");
  }

  /*
   * @testName: jaxbReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void jaxbReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "postjaxb"));
    setRequestContentEntity(getJaxbToken());
    setProperty(Property.REQUEST_HEADERS,
        buildContentType(MediaType.TEXT_XML_TYPE));
    setProperty(Property.SEARCH_STRING, plaincontent);
    invoke();
  }

  /*
   * @testName: jaxbWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void jaxbWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getjaxb"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.REQUEST_HEADERS, buildAccept(MediaType.TEXT_XML_TYPE));
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg("JAXRS called registered writer interceptor for jaxb provider");
  }

  /*
   * @testName: jaxbWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void jaxbWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getjaxb"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    setProperty(Property.REQUEST_HEADERS, buildAccept(MediaType.TEXT_XML_TYPE));
    invoke();
  }

  /*
   * @testName: jaxbWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when mapping representations to Java types and vice versa.
   */
  public void jaxbWriterClientInterceptorTest() throws Fault {
    JAXBElement<String> element = new JAXBElement<String>(new QName("element"),
        String.class, content);
    GenericEntity<JAXBElement<String>> entity = new GenericEntity<JAXBElement<String>>(
        element) {
    };
    addProvider(EntityWriterInterceptor.class);
    addInterceptors(EntityWriterInterceptor.class);
    setRequestContentEntity(entity);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.REQUEST_HEADERS,
        buildContentType(MediaType.TEXT_XML_TYPE));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for JAXBElement provider");
  }

  // ------------------------- StringBean -----------------------------------

  /*
   * @testName: stringBeanReaderContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when stringBeanping representations to Java types and vice
   * versa.
   */
  public void stringBeanReaderContainerInterceptorTest() throws Fault {
    addInterceptors(EntityReaderInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststringbean"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING,
        EntityReaderInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered reader interceptor for StringBean provider");
  }

  /*
   * @testName: stringBeanReaderNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when stringBeanping representations to Java types and vice
   * versa.
   */
  public void stringBeanReaderNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststringbean"));
    setRequestContentEntity(content);
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }

  /*
   * @testName: stringBeanWriterContainerInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when stringBeanping representations to Java types and vice
   * versa.
   */
  public void stringBeanWriterContainerInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getstringbean"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for StringBean provider");
  }

  /*
   * @testName: stringBeanWriterNoInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when stringBeanping representations to Java types and vice
   * versa.
   */
  public void stringBeanWriterNoInterceptorTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "getstringbean"));
    setProperty(Property.SEARCH_STRING, Resource.getName());
    invoke();
  }

  /*
   * @testName: stringBeanWriterClientInterceptorTest
   * 
   * @assertion_ids: JAXRS:SPEC:84;
   * 
   * @test_Strategy: JAX-RS implementations are REQUIRED to call registered
   * interceptors when stringBeanping representations to Java types and vice
   * versa.
   */
  public void stringBeanWriterClientInterceptorTest() throws Fault {
    addInterceptors(EntityWriterInterceptor.class);
    addProvider(EntityWriterInterceptor.class);
    addProvider(StringBeanEntityProvider.class);
    setRequestContentEntity(new StringBean(content));
    setProperty(Property.REQUEST, buildRequest(Request.POST, "poststring"));
    setProperty(Property.SEARCH_STRING,
        EntityWriterInterceptor.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, Resource.DIRECTION);
    invoke();
    logMsg(
        "JAXRS called registered writer interceptor for StringBean provider");
  }

  // ------------------------- String -----------------------------------

  // //////////////////////////////////////////////////////////////////////
  private void addInterceptors(Class<?> clazz) {
    setProperty(Property.REQUEST_HEADERS,
        Resource.HEADERNAME + ":" + clazz.getName());
  }
}
