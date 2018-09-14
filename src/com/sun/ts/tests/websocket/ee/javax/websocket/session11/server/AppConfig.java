/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.websocket.Decoder;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.LinkedListHashSetTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.StringListTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.TypeEnum;

public class AppConfig implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(
      Set<Class<? extends Endpoint>> endpointClasses) {
    Set<ServerEndpointConfig> set = new HashSet<ServerEndpointConfig>();
    addServerEndpoint(set, LinkedListHashSetTextDecoder.class,
        TypeEnum.LINKEDLIST_HASHSET_TEXT);
    addServerEndpoint(set, StringListTextDecoder.class, TypeEnum.LIST_TEXT);
    addServerEndpoint(set, StringBeanTextDecoder.class, TypeEnum.STRINGBEAN);
    addServerEndpoint(set, StringBeanTextStreamDecoder.class,
        TypeEnum.STRINGBEANSTREAM);
    addServerEndpoint(set, StringBeanBinaryDecoder.class,
        TypeEnum.STRINGBEANBINARY);
    addServerEndpoint(set, StringBeanBinaryStreamDecoder.class,
        TypeEnum.STRINGBEANBINARYSTREAM);
    addServerEndpoint(set, null, TypeEnum.STRING_WHOLE);
    addServerEndpoint(set, null, TypeEnum.STRING_PARTIAL);
    addServerEndpoint(set, null, TypeEnum.PONG);
    addServerEndpoint(set, null, TypeEnum.BYTEBUFFER_WHOLE);
    addServerEndpoint(set, null, TypeEnum.BYTEARRAY_WHOLE);
    addServerEndpoint(set, null, TypeEnum.BYTEBUFFER_PARTIAL);
    addServerEndpoint(set, null, TypeEnum.BYTEARRAY_PARTIAL);
    addServerEndpoint(set, null, TypeEnum.INPUTSTREAM);
    addServerEndpoint(set, null, TypeEnum.READER);
    return set;
  }

  private Set<ServerEndpointConfig> addServerEndpoint(
      Set<ServerEndpointConfig> set, Class<? extends Decoder> decoder,
      final TypeEnum typeEnum) {
    List<Class<? extends Decoder>> decoders = new LinkedList<Class<? extends Decoder>>();
    decoders.add(decoder);
    ServerEndpointConfig.Configurator configurator = new ServerEndpointConfig.Configurator() {
      @SuppressWarnings("unchecked")
      @Override
      public <T> T getEndpointInstance(Class<T> endpointClass)
          throws InstantiationException {
        return (T) new WSCServerEndpoint(typeEnum);
      }
    };
    ServerEndpointConfig.Builder builder = ServerEndpointConfig.Builder
        .create(WSCServerEndpoint.class, "/" + typeEnum.name().toLowerCase());
    if (decoder != null)
      builder = builder.decoders(decoders);
    ServerEndpointConfig config = builder.configurator(configurator).build();
    set.add(config);
    return set;
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
    return scanned;
  }
}
