/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handler.HandlerSec;

public class ClassLoaderInfo {
  private boolean hasHandlerClassLoader;

  private boolean hasBeanClassLoader;

  private boolean sameClassLoaders;

  public ClassLoaderInfo() {
  }

  public ClassLoaderInfo(boolean hasHandlerClassLoader,
      boolean hasBeanClassLoader, boolean sameClassLoaders) {
    this.hasHandlerClassLoader = hasHandlerClassLoader;
    this.hasBeanClassLoader = hasBeanClassLoader;
    this.sameClassLoaders = sameClassLoaders;

  }

  public boolean isHasHandlerClassLoader() {
    return hasHandlerClassLoader;
  }

  public void setHasHandlerClassLoader(boolean hasHandlerClassLoader) {
    this.hasHandlerClassLoader = hasHandlerClassLoader;
  }

  public boolean isHasBeanClassLoader() {
    return hasBeanClassLoader;
  }

  public void setHasBeanClassLoader(boolean hasBeanClassLoader) {
    this.hasBeanClassLoader = hasBeanClassLoader;
  }

  public boolean isSameClassLoaders() {
    return sameClassLoaders;
  }

  public void setSameClassLoaders(boolean sameClassLoaders) {
    this.sameClassLoaders = sameClassLoaders;
  }
}
