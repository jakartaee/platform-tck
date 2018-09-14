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

package com.sun.ts.tests.websocket.ee.javax.websocket.containerprovider.metainf;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

public class LibrariedQuestionaire {
  public String getContainerProviderName() {
    WebSocketContainer origContainer = ContainerProvider
        .getWebSocketContainer();
    TCKContainerProvider.setOriginalContainer(origContainer);

    String name = null;

    name = AccessController.doPrivileged(new PrivilegedAction<String>() {
      @Override
      public String run() {
        String name = null;
        ClassLoader origCl = Thread.currentThread().getContextClassLoader();
        try {
          TCKClassLoader myCl = new TCKClassLoader(origCl);
          Thread.currentThread().setContextClassLoader(myCl);

          WebSocketContainer container = ContainerProvider
              .getWebSocketContainer();

          if (TCKWebSocketContainer.class.isInstance(container))
            name = TCKWebSocketContainer.class.getName();
          else
            name = container.getClass().getName();
        } finally {
          Thread.currentThread().setContextClassLoader(origCl);
        }
        return name;
      }
    });
    return name;
  }

}
