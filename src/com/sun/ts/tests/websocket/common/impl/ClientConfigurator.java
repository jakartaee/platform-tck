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

package com.sun.ts.tests.websocket.common.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.websocket.HandshakeResponse;
import javax.websocket.ClientEndpointConfig.Configurator;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.StringUtil;

public class ClientConfigurator extends Configurator {

  private Map<String, List<String>> requestMap = new TreeMap<String, List<String>>();

  private Map<String, List<String>> responseMap = new TreeMap<String, List<String>>();

  private boolean hasBeenAfterResponse = false;

  private boolean hasBeenBeforeRequest = false;

  public boolean hasBeenBeforeRequest() {
    return hasBeenBeforeRequest;
  }

  public boolean hasBeenAfterResponse() {
    return hasBeenAfterResponse;
  }

  public void addToRequest(String key, String... values) {
    requestMap.put(key, Arrays.asList(values));
  }

  public void addToResponse(String key, String... values) {
    responseMap.put(key, Arrays.asList(values));
  }

  public void addToRequestAndResponse(String key, String... values) {
    addToRequest(key, values);
    addToResponse(key, values);
  }

  @Override
  public void beforeRequest(Map<String, List<String>> headers) {
    super.beforeRequest(headers);
    for (Entry<String, List<String>> set : requestMap.entrySet())
      headers.put(set.getKey(), set.getValue());
    hasBeenBeforeRequest = true;
  }

  @Override
  public void afterResponse(HandshakeResponse hr) {
    super.afterResponse(hr);
    Map<String, List<String>> headers = hr.getHeaders();
    for (Entry<String, List<String>> set : responseMap.entrySet()) {
      String key = set.getKey();
      assertTrue(headers.containsKey(key), "key", key,
          "was not found in HandshakeResponse headers");
      assertTrue(StringUtil.contains(headers.get(key), set.getValue(), false),
          "value \"", StringUtil.collectionToString(set.getValue()), "\"",
          "was not found for key", key, "only \"",
          StringUtil.collectionToString(headers.get(key)), "\" has been found");
      WebSocketCommonClient.logTrace("found expected pair [", key, ",",
          headers.get(key), "]");
    }
    hasBeenAfterResponse = true;
  }

  public void assertAfterResponseHasBeenCalled() {
    assertTrue(hasBeenAfterResponse(),
        "Configurator#afterResponse has not been called");
    WebSocketCommonClient
        .logTrace("Configurator#afterResponse has been called as expected", "");
  }

  public void assertBeforeRequestHasBeenCalled() {
    assertTrue(hasBeenBeforeRequest(),
        "Configurator#beforeRequest has not been called");
    WebSocketCommonClient
        .logTrace("Configurator#beforeRequest has been called as expected", "");
  }

  protected static void assertTrue(boolean t, String... msg) {
    if (!t)
      throw new RuntimeException(StringUtil.objectsToString((Object[]) msg));
  }
}
