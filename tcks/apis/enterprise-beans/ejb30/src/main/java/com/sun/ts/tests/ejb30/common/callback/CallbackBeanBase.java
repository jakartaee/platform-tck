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

package com.sun.ts.tests.ejb30.common.callback;

abstract public class CallbackBeanBase extends SharedCallbackBeanBase
    implements CallbackIF, java.io.Serializable {
  public static final String isPostConstructCalledTest = "isPostConstructCalledTest";

  public static final String isInjectionDoneTest = "isInjectionDoneTest";

  public static final String isPostConstructOrPreDestroyCalledTest = "isPostConstructOrPreDestroyCalledTest";

  // ================== business methods ====================================
  public void removeFoo() {
  }

  public boolean isPostConstructCalledTest() {
    return isPostConstructCalled();
  }

  public boolean isInjectionDoneTest() {
    return isInjectionDone();
  }

  public boolean runtimeExceptionTest() {
    return true;
  }

}
