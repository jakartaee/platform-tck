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

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.Charset;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class SaajClient {

  private final static String DEFAULT_CHARSET = "UTF-8";

  private SOAPConnectionFactory connectionFactory;

  private MessageFactory messageFactory;

  private MimeHeaders headers;

  private HttpClient httpClient;

  private String url;

  private Charset cs;

  private boolean SoapConnectionFactorySupported = true;

  public SaajClient() throws EETest.Fault {
    super();
    try {
      messageFactory = MessageFactory.newInstance();
      httpClient = new HttpClient();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Unable to create message factory", e);
    }
    try {
      connectionFactory = SOAPConnectionFactory.newInstance();
    } catch (UnsupportedOperationException e) {
      SoapConnectionFactorySupported = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Unable to create connection factory", e);
    }
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private SOAPMessage createSOAPMessage(InputStream is) {
    SOAPMessage message = null;
    headers = new MimeHeaders();
    headers.addHeader("Content-Type",
        "text/xml; charset=\"" + getCharsetAsName() + "\"");
    try {
      message = messageFactory.createMessage(headers, is);
    } catch (Exception e) {
      message = null;
    }
    return message;
  }

  private SOAPMessage sendAsHTTPMessage(InputStream is) throws IOException {
    httpClient.setUrl(url);
    httpClient.setCharset(getCharset());
    InputStream response = httpClient.makeRequest(is);
    return createSOAPMessage(response);
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

  public SOAPMessage makeRequest(InputStream messageContent)
      throws SOAPException, IOException {
    if (SoapConnectionFactorySupported) {
      ByteArrayOutputStream baos = getInputStreamAsOutputStream(messageContent);
      ByteArrayInputStream bais1 = new ByteArrayInputStream(baos.toByteArray());
      ByteArrayInputStream bais2 = new ByteArrayInputStream(baos.toByteArray());
      SOAPMessage message = createSOAPMessage(bais1);
      if (message != null) {
        SOAPMessage msg = null;
        try {
          // This could fail for invalid messages for negative test cases
          TestUtil.logMsg("***** makeRequest via SAAJ first *****");
          msg = connectionFactory.createConnection().call(message, url);
        } catch (Exception e) {
          // Send as HTTP if above fails (only way left to send it)
          TestUtil.logMsg("***** makeRequest via HTTP second *****");
          msg = sendAsHTTPMessage(bais2);
        }
        return msg;
      } else {
        return sendAsHTTPMessage(bais2);
      }
    } else {
      return sendAsHTTPMessage(messageContent);
    }
  }

  public void logMessageInHarness(SOAPMessage message) {
    // This method is deprecated. Logging is done in SOAPClient. Set
    // message=null.
    message = null;
    if (message != null) {
      try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        message.writeTo(bos);
        TestUtil.logMsg(bos.toString());
      } catch (Exception e) {
        // ignore
        // test passed, this is just for logging
      }
    }
  }
}
