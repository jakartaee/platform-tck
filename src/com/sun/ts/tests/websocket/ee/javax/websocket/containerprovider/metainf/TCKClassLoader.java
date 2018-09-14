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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class TCKClassLoader extends ClassLoader {

  private ClassLoader orig;

  public TCKClassLoader(ClassLoader orig) {
    this.orig = orig;
  }

  @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    Enumeration<URL> en = orig.getResources(name);
    return filter(en);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return orig.loadClass(name);
  }

  @Override
  public boolean equals(Object obj) {
    return orig.equals(obj);
  }

  @Override
  public void clearAssertionStatus() {
    orig.clearAssertionStatus();
  }

  @Override
  public URL getResource(String name) {
    return orig.getResource(name);
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    return orig.getResourceAsStream(name);
  }

  @Override
  public int hashCode() {
    return orig.hashCode();
  }

  private static Enumeration<URL> filter(Enumeration<URL> orig) {
    List<URL> list = new LinkedList<URL>();
    while (orig.hasMoreElements()) {
      URL url = orig.nextElement();
      String file = url.getFile();
      if (file.contains("lib.jar") && file
          .contains("META-INF/services/javax.websocket.ContainerProvider"))
        list.add(url);
    }
    return Collections.enumeration(list);
  }
}
