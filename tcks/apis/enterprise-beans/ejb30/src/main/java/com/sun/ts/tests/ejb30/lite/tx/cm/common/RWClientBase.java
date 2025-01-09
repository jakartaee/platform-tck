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
package com.sun.ts.tests.ejb30.lite.tx.cm.common;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class RWClientBase extends EJBLiteClientBase {
  protected RWTestBeanBase0 testBean; // injected in subclass

  // overridden in stateful to use lookup
  protected RWTestBeanBase0 getTestBean() {
    return testBean;
  }

  /*
   * testName: mandatory
   * 
   * @test_Strategy:
   */
  public void mandatory() {
    appendReason("Persist and flush:", getTestBean().mandatory(true));
    appendReason("Persist without flush:", getTestBean().mandatory(false));
  }

  /*
   * testName: required
   * 
   * @test_Strategy:
   */
  public void required() {
    appendReason("Persist without flush:", getTestBean().required(false));
    appendReason("Persist and flush:", getTestBean().required(true));

    appendReason("Persist without flush:",
        getTestBean().requiredNoExistingTransaction(false));
    appendReason("Persist and flush:",
        getTestBean().requiredNoExistingTransaction(true));
  }

  /*
   * testName: requiredNoExistingTransaction
   * 
   * @test_Strategy:
   */
  public void requiredNoExistingTransaction() {
    appendReason("Persist without flush:",
        getTestBean().requiredNoExistingTransaction(false));
    appendReason("Persist and flush:",
        getTestBean().requiredNoExistingTransaction(true));
  }

  /*
   * testName: supports
   * 
   * @test_Strategy:
   */
  public void supports() {
    appendReason("Persist without flush:", getTestBean().supports(false));
    appendReason("Persist and flush:", getTestBean().supports(true));
  }

  /*
   * testName: requiresNew
   * 
   * @test_Strategy:
   */
  public void requiresNew() {
    appendReason("Persist without flush:", getTestBean().requiresNew(false));
    appendReason("Persist and flush:", getTestBean().requiresNew(true));
  }

  /*
   * testName: postConstructTransaction
   * 
   * @test_Strategy:
   */
  public void postConstructTransaction() {
    getTestBean().postConstructTransaction(getReasonBuffer());
  }
}
