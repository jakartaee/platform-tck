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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;

import com.sun.ts.tests.websocket.common.impl.ExtensionImpl;
import com.sun.ts.tests.websocket.common.impl.ExtensionParameterImpl;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;

/*
 * The comparison is to be made here, as the Extension.Parameters are not sorted
 * within Extension. Also, the serialization and deserialization would not help 
 * the search of sublist, here we can use StringUtil.contains 
 */
public class WSCExtensionsServer extends Endpoint
    implements MessageHandler.Whole<String> {

  Session session;

  ServerEndpointConfig config;

  @Override
  public void onMessage(String msg) {
    boolean contains = false;
    List<ExtensionImpl> response = null;
    try {
      if (msg.equals("requested")) {
        contains = StringUtil.contains(ExtensionsConfigurator.getRequested(),
            getRequestedExtension());
        response = contains ? getRequestedExtension()
            : ExtensionsConfigurator.getRequested();
      } else if (msg.equals("resulted")) {
        contains = StringUtil.contains(ExtensionsConfigurator.getResulted(),
            getRequestedExtension());
        response = contains ? getRequestedExtension()
            : ExtensionsConfigurator.getResulted();
      } else if (msg.equals("installed")) {
        List<ExtensionImpl> configured = ExtensionImpl
            .transformToImpl(config.getExtensions());
        contains = StringUtil.contains(ExtensionsConfigurator.getInstalled(),
            configured);
        response = contains ? ExtensionsConfigurator.getInstalled()
            : configured;
      } else if (msg.equals("resultedinorder")) {
        contains = StringUtil.containsInOrder(
            ExtensionsConfigurator.getResulted(), getOrderedExtensions());
        response = contains ? getOrderedExtensions()
            : ExtensionsConfigurator.getResulted();
      }
      session.getBasicRemote().sendText(ExtensionImpl.toString(response));
    } catch (IOException e) {
      e.printStackTrace();
      try {
        session.getBasicRemote().sendText(IOUtil.printStackTrace(e));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.config = (ServerEndpointConfig) config;
    session.addMessageHandler(this);
  }

  @Override
  public void onError(Session session, Throwable thr) {
    thr.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(thr);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<ExtensionImpl> getRequestedExtension() {
    Extension.Parameter firstParam = ExtensionsServerEndpointConfig.PARAMETER[0];
    Extension.Parameter secondParam = ExtensionsServerEndpointConfig.PARAMETER[1];
    ExtensionImpl extension = new ExtensionImpl(
        ExtensionsServerEndpointConfig.EXT_NAMES[0], firstParam, secondParam);
    List<ExtensionImpl> list = new ArrayList<ExtensionImpl>();
    list.add(extension);
    return list;
  }

  public static List<ExtensionImpl> getOrderedExtensions() {
    Extension.Parameter firstParam = ExtensionsServerEndpointConfig.PARAMETER[0];
    Extension.Parameter secondParam = ExtensionsServerEndpointConfig.PARAMETER[1];
    ExtensionImpl extension = new ExtensionImpl(
        ExtensionsServerEndpointConfig.EXT_NAMES[1], firstParam, secondParam);
    List<ExtensionImpl> list = new ArrayList<ExtensionImpl>();
    list.add(extension);
    list.add(getRequestedExtension().iterator().next());
    return list;
  }

  public static List<ExtensionImpl> getNotNegotiableExtension() {
    Extension.Parameter secondParam = new ExtensionParameterImpl("thirdName",
        "thirdValue");
    List<ExtensionImpl> list = getRequestedExtension();
    list.iterator().next().addParameters(secondParam);
    return list;
  }

}
