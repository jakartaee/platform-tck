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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.nio.charset.Charset;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class HttpClient {

  private final static String DEFAULT_CHARSET = "UTF-8";

  /**
   * HTTP GET method.
   */
  public static final String HTTP_METHOD_GET = "GET";

  /**
   * HTTP POST method.
   */
  public static final String HTTP_METHOD_POST = "POST";

  /**
   * The HTTP method.
   */
  private String method;

  /**
   * The HTTP headers.
   */
  private HashMap headers;

  /**
   * The URL.
   */
  private String url;

  /**
   * The Charset.
   */
  private Charset cs;

  private HttpURLConnection conn;

  /**
   * Constructs a new HttpRequest instance with the specified URL.
   *
   * @param url
   *          the URL.
   */
  public HttpClient() throws EETest.Fault {
    super();
    this.method = HTTP_METHOD_POST;
    headers = new HashMap();
    headers.put("SOAPAction", "\"\"");
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Charset getCharset() {
    return cs;
  }

  public void setCharset(Charset cs) {
    this.cs = cs;
  }

  private String getCharsetAsName() {
    if (cs == null)
      return DEFAULT_CHARSET;
    else
      return cs.name().toUpperCase();
  }

  /**
   * Sets the HTTP request method.
   *
   * @param method
   *          the HTTP request method.
   */
  public void setMethod(String method) {
    if (method != null) {
      this.method = method;
    } else {
      this.method = HTTP_METHOD_POST;
    }
  }

  /**
   * Returns the HTTP request method.
   *
   * @return the HTTP request method.
   */
  public String getMethod() {
    return method;
  }

  /**
   * Adds the specified HTTP header.
   *
   * @param name
   *          the header name.
   * @param value
   *          the header value.
   */
  public void addHeader(String name, String value) {
    name = properCase(name);
    String current = (String) headers.get(name);
    if (current != null) {
      value = current + ", " + value;
    }
    headers.put(name, value);
  }

  /**
   * Sets the specified HTTP header.
   *
   * @param name
   *          the header name.
   * @param value
   *          the header value.
   */
  public void setHeader(String name, String value) {
    headers.put(properCase(name), value);
  }

  /**
   * Returns the HTTP status code. Until the request has completed, this value
   * is 200.
   *
   * @return the HTTP status code.
   */
  public int getStatusCode() throws IOException {
    return conn.getResponseCode();
  }

  /**
   * Returns the HTTP status message. Until the request has completed, this
   * value is OK.
   *
   * @return the HTTP status message.
   */
  public String getStatusMessage() throws IOException {
    return conn.getResponseMessage();
  }

  /**
   * Interacts with the server by sending the request and receiving the
   * response.
   *
   * @param is
   *          the request message input stream.
   *
   * @throws java.io.IOException
   */
  public InputStream makeRequest(InputStream messageContent)
      throws IOException {
    String name = getCharsetAsName();
    TestUtil.logMsg("Charset name=" + name);
    addHeader("Content-Type", "text/xml; charset=" + name);
    conn = (HttpURLConnection) new URL(url).openConnection();
    sendRequest(messageContent);
    return receiveResponse();
  }

  /**
   * Sends the request to the server.
   *
   * @param conn
   *          the HTTP URL connection.
   * @param is
   *          the request message input stream.
   *
   * @throws java.io.IOException
   */
  protected void sendRequest(InputStream is) throws IOException {
    conn.setRequestMethod(method);
    Iterator iterator = headers.keySet().iterator();
    while (iterator.hasNext()) {
      String name = (String) iterator.next();
      String value = (String) headers.get(name);
      conn.setRequestProperty(name, value);
    }
    if (is != null) {
      conn.setDoOutput(true);
      OutputStream os = conn.getOutputStream();
      transfer(is, os);
    }
  }

  /**
   * Receives the response from the server. If no content is returned, this
   * method returns null.
   *
   * @param conn
   *          the HTTP URL connection.
   *
   * @return the response message input stream.
   *
   * @throws java.io.IOException
   */
  protected InputStream receiveResponse() throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    byte[] response;
    InputStream is;
    try {
      try {
        is = conn.getInputStream();
      } catch (IOException e) {
        is = conn.getErrorStream();
      }
      transfer(is, os);
      response = os.toByteArray();
    } catch (IOException e) {
      TestUtil.printStackTrace(e);
      response = null;
    }
    return (response != null ? new ByteArrayInputStream(response) : null);
  }

  /**
   * Transfers all data from the specified input stream to the specified output
   * stream.
   *
   * @param is
   *          the input stream.
   * @param os
   *          the output stream.
   *
   * @throws java.io.IOException
   */
  protected void transfer(InputStream is, OutputStream os) throws IOException {
    byte[] buffer = new byte[1024];
    int length;
    do {
      length = is.read(buffer);
      if (length > 0) {
        os.write(buffer, 0, length);
      }
    } while (length > 0);
    os.flush();
  }

  /**
   * Proper cases an HTTP header name (e.g. Accept-Language).
   *
   * @param name
   *          the name.
   * @return the proper cased name.
   */
  protected String properCase(String name) {
    StringBuffer buffer = new StringBuffer(name.length());
    char previous = '-';
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (previous == '-') {
        buffer.append(Character.toUpperCase(c));
      } else {
        buffer.append(Character.toLowerCase(c));
      }
      previous = c;
    }
    return buffer.toString();
  }

  public void logMessageInHarness(InputStream message) {
    // This method is deprecated. Logging is done in SOAPClient. Set
    // message=null.
    message = null;
    if (message != null) {
      try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transfer(message, bos);
        TestUtil.logMsg(bos.toString());
      } catch (Exception e) {
        // ignore
        // test passed, this is just for logging
      }
    }
  }

  public String getResponseHeader(String name) {
    return conn.getHeaderField(name);
  }
}
