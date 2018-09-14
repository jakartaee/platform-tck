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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig.configurator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.impl.ExtensionImpl;
import com.sun.ts.tests.websocket.common.impl.ExtensionParameterImpl;

public class ExtensionsServerEndpointConfig implements ServerEndpointConfig {

  static final String[] EXT_NAMES = new String[] { "firstExtName",
      "secondExtName", "thirdExtName" };

  static final ExtensionParameterImpl[] PARAMETER = {
      new ExtensionParameterImpl("firstName", "firstValue"),
      new ExtensionParameterImpl("secondName", "secondValue") };

  @Override
  public Map<String, Object> getUserProperties() {
    return Collections.emptyMap();
  }

  @Override
  public Class<?> getEndpointClass() {
    return WSCExtensionsServer.class;
  }

  @Override
  public String getPath() {
    return "/extensions";
  }

  @Override
  public List<String> getSubprotocols() {
    return Collections.emptyList();
  }

  @Override
  public List<Extension> getExtensions() {
    Extension firstExt = new ExtensionImpl(EXT_NAMES[0], PARAMETER);
    Extension secondExt = new ExtensionImpl(EXT_NAMES[1], PARAMETER);
    Extension thirdExt = new ExtensionImpl(EXT_NAMES[2]);
    return Arrays.asList(firstExt, secondExt, thirdExt);
  }

  @Override
  public Configurator getConfigurator() {
    return new ExtensionsConfigurator();
  }

  @Override
  public List<Class<? extends Encoder>> getEncoders() {
    return Collections.emptyList();
  }

  @Override
  public List<Class<? extends Decoder>> getDecoders() {
    return Collections.emptyList();
  }

}
