/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;

public class BeanBase implements CommonSingletonIF {
  @Resource
  private EJBContext ejbContext;

  @EJB
  private ASingletonBean aSingleton; // no interface

  @EJB
  private BSingletonIF bSingleton; // 1 interface

  @EJB
  private CSingletonIF cSingleton; // c interface 1

  @EJB
  private C2SingletonIF c2Singleton; // c interface 2

  public <T extends CommonSingletonIF> T getSingletonReference(Class<T> type) {
    if (ASingletonBean.class.isAssignableFrom(type)) {
      return (T) aSingleton;
    } else if (BSingletonIF.class.isAssignableFrom(type)) {
      return (T) bSingleton;
    } else if (C2SingletonIF.class.isAssignableFrom(type)) {
      // Since C2SingletonIF extends CSingletonIF, we should first compare C2
      // to avoid fall into CSingleton
      return (T) c2Singleton;
    } else if (CSingletonIF.class.isAssignableFrom(type)) {
      return (T) cSingleton;
    }
    throw new IllegalArgumentException("Unrecognized singleton type: " + type);
  }

  public <T extends CommonSingletonIF> T getSingletonReferenceFromEJBContext(
      Class<T> type) {
    String prefix = BeanBase.class.getName();
    if (ASingletonBean.class.isAssignableFrom(type)) {
      return (T) ejbContext.lookup(prefix + "/aSingleton");
    } else if (BSingletonIF.class.isAssignableFrom(type)) {
      return (T) ejbContext.lookup(prefix + "/bSingleton");
    } else if (C2SingletonIF.class.isAssignableFrom(type)) {
      // Since C2SingletonIF extends CSingletonIF, we should first compare C2
      // to avoid fall into CSingleton
      return (T) ejbContext.lookup(prefix + "/c2Singleton");
    } else if (CSingletonIF.class.isAssignableFrom(type)) {
      return (T) ejbContext.lookup(prefix + "/cSingleton");
    }
    throw new IllegalArgumentException("Unrecognized singleton type: " + type);
  }

  public void error() throws RuntimeException {
    throw new RuntimeException(
        "System exception from tests, but the single bean should not be destroyed.");
  }

  public int identityHashCode() {
    return System.identityHashCode(this);
  }

  @PreDestroy
  private void preDestroy() {
    Helper.getLogger().info("In BeanBase.preDestroy()");
  }
}
