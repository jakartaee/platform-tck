/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.net.URL;
import javax.naming.NamingException;

abstract public class AssemblyBeanBase
    implements AssemblyRemoteIF, AssemblyLocalIF {
  private int postConstructCalls;

  public AssemblyBeanBase() {
  }

  // helloBean is deployed in a separate ejb module. Therefore, it can only
  // be accessed via its remote business intf.
  protected HelloRemoteIF getHelloBean() {
    HelloRemoteIF helloBean;
    try {
      helloBean = (HelloRemoteIF) ServiceLocator.lookupByShortName("helloBean");
    } catch (NamingException ex) {
      throw new IllegalStateException(ex);
    }
    return helloBean;
  }

  protected void incrementPostConstructCalls() {
    this.postConstructCalls++;
  }

  //////////////////////////////////////////////////////////////////////
  // business methods
  //////////////////////////////////////////////////////////////////////
  public int remoteAdd(int a, int b) {
    return a + b;
  }

  public int getPostConstructCalls() {
    return postConstructCalls;
  }

  public String callHelloBean() {
    // just check if helloBean can be accessed
    HelloRemoteIF helloBean = getHelloBean();
    helloBean.add(1, 2);
    return helloBean.toString();
  }

  public void libSubdirNotScanned() throws TestFailedException {
    Util.verifyGetResource(getClass(), RESOURCE_NAME, null);
  }

  public void earLibNotInClasspath() throws TestFailedException {
    Util.verifyGetResource(getClass(), EAR_LIB_JAR_NAME, null);
  }

  public URL getResource(String name) {
    return getClass().getResource(name);
  }

  public String dirUsedInClassPath() {
    throw new UnsupportedOperationException(
        "This is the default implementation. Subclasses that need this method should override it");
  }

  public String getResourceContent(String name) throws TestFailedException {
    throw new UnsupportedOperationException(
        "This is the default implementation. Subclasses that need this method should override it");
  }
}
