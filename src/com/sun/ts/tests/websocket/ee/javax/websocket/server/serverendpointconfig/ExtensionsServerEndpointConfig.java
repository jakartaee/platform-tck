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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

public class ExtensionsServerEndpointConfig implements ServerEndpointConfig {

  static final String[] EXT_NAMES = new String[] { "empty", "secondEmpty" };

  @Override
  public Map<String, Object> getUserProperties() {
    return Collections.emptyMap();
  }

  @Override
  public Class<?> getEndpointClass() {
    return WSProgramaticExtensionsServer.class;
  }

  @Override
  public String getPath() {
    return "/programatic/extensions";
  }

  @Override
  public List<String> getSubprotocols() {
    return Collections.emptyList();
  }

  @Override
  public List<Extension> getExtensions() {
    Extension firstExt = new Extension() {
      @Override
      public String getName() {
        return EXT_NAMES[0];
      }

      @Override
      public List<Parameter> getParameters() {
        return Collections.emptyList();
      }
    };

    Extension secondExt = new Extension() {
      @Override
      public String getName() {
        return EXT_NAMES[1];
      }

      @Override
      public List<Parameter> getParameters() {
        return Collections.emptyList();
      }
    };
    return Arrays.asList(firstExt, secondExt);
  }

  @Override
  public Configurator getConfigurator() {
    return new ServerEndpointConfig.Configurator() {
    };
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
