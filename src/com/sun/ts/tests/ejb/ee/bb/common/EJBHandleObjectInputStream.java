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

package com.sun.ts.tests.ejb.ee.bb.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.io.ObjectInputStream;

// This class overrides the resolveClass() and resolveProxyClass() methods  of ObjectInputStream
public class EJBHandleObjectInputStream extends ObjectInputStream {

  public EJBHandleObjectInputStream(ByteArrayInputStream bais)
      throws IOException {
    super(bais);
  }

  @Override
  protected Class<?> resolveClass(final ObjectStreamClass desc)
      throws IOException, ClassNotFoundException {
    return Class.forName(desc.getName(), true,
        EJBHandleObjectInputStream.class.getClassLoader());
  }

  @Override
  protected Class<?> resolveProxyClass(String[] interfaces)
      throws IOException, ClassNotFoundException {

    ClassLoader latestLoader = EJBHandleObjectInputStream.class
        .getClassLoader();
    ClassLoader nonPublicLoader = null;
    boolean hasNonPublicInterface = false;

    // define proxy in class loader of non-public interface(s), if any
    Class[] classObjs = new Class[interfaces.length];
    for (int i = 0; i < interfaces.length; i++) {
      Class cl = Class.forName(interfaces[i], false, latestLoader);
      if ((cl.getModifiers() & Modifier.PUBLIC) == 0) {
        if (hasNonPublicInterface) {
          if (nonPublicLoader != cl.getClassLoader()) {
            throw new IllegalAccessError(
                "conflicting non-public interface class loaders");
          }
        } else {
          nonPublicLoader = cl.getClassLoader();
          hasNonPublicInterface = true;
        }
      }
      classObjs[i] = cl;
    }
    try {
      return Proxy.getProxyClass(
          hasNonPublicInterface ? nonPublicLoader : latestLoader, classObjs);
    } catch (IllegalArgumentException e) {
      throw new ClassNotFoundException(null, e);
    }
  }

}
