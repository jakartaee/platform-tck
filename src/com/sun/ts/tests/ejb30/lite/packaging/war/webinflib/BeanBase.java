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
package com.sun.ts.tests.ejb30.lite.packaging.war.webinflib;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.transaction.UserTransaction;

abstract public class BeanBase {

  @Resource
  private EJBContext ejbContext;

  @Resource
  private UserTransaction ut;

  /**
   * @EJB injects of OneBean, TwoBean, and ThreeBeans into all 3 bean classes to
   *      ensure the 9 ejb-refs are all scoped to the entire .war, not just each
   *      EJB.
   */
  protected OneBean one;

  protected TwoBean two;

  protected ThreeBean three;

  protected boolean injectionStatus;

  protected String injectionRecords;

  abstract public String getRefNamePrefix();

  public String getInjectionRecords() {
    return injectionRecords;
  }

  public boolean getInjectionStatus() {
    return injectionStatus;
  }

  public Object beanClassToClientLookup(String shortName) {
    return ServiceLocator.lookupByShortNameNoTry(shortName);
  }

  public Object lookupWithEJBContext(String shortName) {
    return ejbContext.lookup(shortName);
  }

  public int getClassLoaderId() {
    return System.identityHashCode(getClass().getClassLoader());
  }

  @PostConstruct
  private void postConstruct() {
    if (one != null && two != null && three != null) {
      injectionStatus = true;
      injectionRecords = String
          .format("Successfully injected 3 beans: %s, %s, %s", one, two, three);
    } else {
      injectionStatus = false;
      injectionRecords = String
          .format("Some or all injections failed: %s, %s, %s", one, two, three);
    }
  }

}
