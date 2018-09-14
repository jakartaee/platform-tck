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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

public class TCKContainerProvider extends ContainerProvider
    implements InvocationHandler {

  private static WebSocketContainer provider;

  public static void setOriginalContainer(WebSocketContainer provider) {
    TCKContainerProvider.provider = provider;
  }

  @Override
  protected WebSocketContainer getContainer() {
    Object o = Proxy.newProxyInstance(getClass().getClassLoader(),
        new Class<?>[] { TCKWebSocketContainer.class }, this);
    return (WebSocketContainer) o;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    Method m = find(ContainerProvider.class.getMethods(), method);
    if (m != null) {
      Object ret = m.invoke(provider, args);
      return ret;
    }
    return null;
  }

  private static final Method find(Method[] array, Method object) {
    for (Method i : array)
      if (equals(object, i))
        return i;
    return null;
  }

  private static final boolean equals(Method obj, Method other) {
    if (!obj.getName().equals(other.getName()))
      return false;
    if (!obj.getReturnType().equals(other.getReturnType()))
      return false;
    return parameterEquals(obj.getParameterTypes(), other.getParameterTypes());
  }

  private static final boolean parameterEquals(Class<?>[] params1,
      Class<?>[] params2) {
    if (params1.length != params2.length)
      return false;
    for (int i = 0; i < params1.length; i++)
      if (params1[i] != params2[i])
        return false;
    return true;
  }
}
