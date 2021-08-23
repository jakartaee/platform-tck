/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.client;

import java.io.IOException;

import org.apache.commons.httpclient.Header;

import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint.ClientEndpointData;
import com.sun.ts.tests.websocket.common.util.IOUtil;

public class ApacheResponseAdapter extends HttpResponse {

  public ApacheResponseAdapter(String host, int port) {
    super(host, port, port == 443, null, null);
    this.caser = TextCaser.NONE;
  }

  public ApacheResponseAdapter(String host, int port, TextCaser caser) {
    this(host, port);
    this.caser = caser;
  }

  private TextCaser caser = null;

  /**
   * Simulates HTTP status code, 500 on error, 200 on ok
   */
  @Override
  public String getStatusCode() {
    if (ClientEndpointData.getError() != null)
      WebSocketTestCase.logMsg("Unexpected error on client endpoint:",
          IOUtil.printStackTrace(ClientEndpointData.getError()));
    return ClientEndpointData.getError() == null ? "200" : "500";
  }

  @Override
  public String getResponseBodyAsString() throws IOException {
    return caser.getCasedText(ClientEndpointData.getResponseAsString());
  }

  @Override
  public String getResponseBodyAsRawString() throws IOException {
    return getResponseBodyAsString();
  }

  @Override
  public String getReasonPhrase() {
    return null;
  }

  @Override
  public Header[] getResponseHeaders() {
    return new Header[0];
  }

  @Override
  public Header getResponseHeader(String headerName) {
    return null;
  }

  @Override
  public String getResponseEncoding() {
    return null;
  }
}
