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
package com.sun.ts.tests.ejb30.lite.packaging.war.enventry;

import java.util.ArrayList;
import java.util.List;

abstract public class ComponentBase {
  private Boolean injectionStatus;

  private List<String> injectionRecords;

  abstract protected void verify(StringBuilder sb) throws RuntimeException;

  public List<String> getInjectionRecords() {
    return injectionRecords;
  }

  public boolean getInjectionStatus() {
    return injectionStatus;
  }

  protected void setInjectionStatus(Boolean is) {
    // if status is already false, which means test should fail, then do nothing
    if (injectionStatus == null || injectionStatus == true) {
      injectionStatus = is;
    }
  }

  protected void addInjectionRecord(String rec) {
    if (injectionRecords == null) {
      injectionRecords = new ArrayList<String>();
    }
    injectionRecords.add(rec);
  }

  /**
   * A template method to check injections into bean/interceptors. Always called
   * by concrete subclass, not by BeanBase or InterceptorBase.
   */
  protected void checkInjections(int nPreviousRecords) {
    StringBuilder sb = new StringBuilder();
    try {
      // always dispatched to verify method on concrete subclass.
      // No easy way to call the concrete verify method (if there is one)
      // in ComponentBase/BeanBase. A more natural option is to let subclass
      // verify call super.verify() and have no verification in superclasses.
      // But then the verification is only triggered by subclass PostConstruct,
      // not from superclass PostConstruct. We want to ensure that superclass
      // PostConstruct is called in the correct order and that all injections
      // in the superclass have been done.
      verify(sb);
      setInjectionStatusAndRecord(true, sb.toString());
    } catch (Throwable e) {
      setInjectionStatusAndRecord(false, sb.toString(), e);
    }
  }

  // only 0 or 1 Throwable, and other Throwable's are ignored
  protected void setInjectionStatusAndRecord(boolean b, String s,
      Throwable... e) {
    setInjectionStatus(b);
    if (e.length == 0) {
      addInjectionRecord(s);
    } else {
      addInjectionRecord(s + " " + e[0]);
    }
  }
}
