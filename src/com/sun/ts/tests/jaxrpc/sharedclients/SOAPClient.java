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

package com.sun.ts.tests.jaxrpc.sharedclients;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.w3c.dom.Document;

public abstract class SOAPClient extends Client {

  private final static String DEFAULT_CHARSET = "UTF-8";

  public static final int MODE_JAXRPC = 0;

  public static final int MODE_JAVAEE = 1;

  private HttpClient httpClient;

  private SaajClient saajClient;

  private DescriptionClient descriptionClient;

  private String endpointURL;

  private String wsdlURL;

  protected StubContext stubContext;

  public SOAPClient(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    endpointURL = getEndpointURLString();
    wsdlURL = getWSDLURLString();
    httpClient = new HttpClient();
    httpClient.setUrl(endpointURL);
    saajClient = new SaajClient();
    saajClient.setUrl(endpointURL);
    descriptionClient = new DescriptionClient();
    descriptionClient.setURL(wsdlURL);
    initStubContext(mode);
  }

  private void initStubContext(int mode) {
    stubContext = new StubContext();
    stubContext.setMode(mode);
    stubContext.setEndpointURL(endpointURL);
    stubContext.setWsdllocURL(wsdlURL);
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

  public SOAPMessage makeSaajRequest(String request)
      throws SOAPException, IOException {
    return makeSaajRequest(request, null);
  }

  public SOAPMessage makeSaajRequest(SOAPMessage request)
      throws SOAPException, IOException {
    return makeSaajRequest(request, null);
  }

  public SOAPMessage makeSaajRequest(String request, Charset cs)
      throws SOAPException, IOException {
    saajClient.setCharset(cs);
    LoggingSOAPRequest(request);
    SOAPMessage response = saajClient
        .makeRequest(getInputStreamForString(request, cs));
    LoggingSOAPResponse(getResponseAsString(response));
    return response;
  }

  public SOAPMessage makeSaajRequest(SOAPMessage request, Charset cs)
      throws SOAPException, IOException {
    saajClient.setCharset(cs);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    request.writeTo(baos);
    String requestAsString = baos.toString();
    LoggingSOAPRequest(requestAsString);
    SOAPMessage response = saajClient
        .makeRequest(getInputStreamForString(requestAsString, cs));
    LoggingSOAPResponse(getResponseAsString(response));
    return response;
  }

  public InputStream makeHTTPRequest(String request) throws IOException {
    return makeHTTPRequest(request, null);
  }

  public InputStream makeHTTPRequest(String request, Charset cs)
      throws IOException {
    LoggingSOAPRequest(request);
    httpClient.setCharset(cs);
    InputStream response = httpClient
        .makeRequest(getInputStreamForString(request, cs));
    ByteArrayOutputStream baos = getInputStreamAsOutputStream(response);
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    LoggingSOAPResponse(baos.toString());
    LoggingHTTPStatusCode();
    return bais;
  }

  public void logMessageInHarness(SOAPMessage message) {
    saajClient.logMessageInHarness(message);
  }

  public void logMessageInHarness(InputStream message) {
    httpClient.logMessageInHarness(message);
  }

  public int getStatusCode() throws IOException {
    return httpClient.getStatusCode();
  }

  public Document getDocument() throws EETest.Fault {
    return descriptionClient.getDocument();
  }

  public String getDescriptionURL() {
    return descriptionClient.getURL();
  }

  public void addHeader(String name, String value) {
    httpClient.addHeader(name, value);
  }

  public String getResponseHeader(String name) {
    return httpClient.getResponseHeader(name);
  }

  private ByteArrayInputStream getInputStreamForString(String request,
      Charset cs) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    OutputStreamWriter osw;
    if (cs != null) {
      osw = new OutputStreamWriter(bos, cs);
    } else {
      osw = new OutputStreamWriter(bos, Charset.forName(DEFAULT_CHARSET));
    }
    osw.write(request);
    osw.flush();
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    return bis;
  }

  private String getResponseAsString(SOAPMessage response)
      throws SOAPException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    response.writeTo(baos);
    return baos.toString();
  }

  private void LoggingSOAPRequest(String request) {
    if (request == null)
      return;
    TestUtil.logMsg("-------------------");
    TestUtil.logMsg("Logging SOAPRequest");
    TestUtil.logMsg("-------------------");
    TestUtil.logMsg(request);
  }

  private void LoggingSOAPResponse(String response) {
    if (response == null)
      return;
    TestUtil.logMsg("--------------------");
    TestUtil.logMsg("Logging SOAPResponse");
    TestUtil.logMsg("--------------------");
    TestUtil.logMsg(response);
  }

  private void LoggingHTTPStatusCode() throws IOException {
    TestUtil.logMsg("HTTPStatusCode=" + getStatusCode());
  }
}
