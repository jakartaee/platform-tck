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
package com.sun.ts.tests.ejb30.lite.singleton.dependson.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@TransactionManagement(TransactionManagementType.BEAN)
abstract public class BeanBase {
  @EJB
  private HistoryBean historyBean;

  abstract protected String getBeanName();

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    long creationTimeMillis = System.currentTimeMillis();
    int id = System.identityHashCode(this);
    historyBean.addCreationTimeMillisForBeanName(getBeanName(),
        creationTimeMillis, this);
    historyBean.addInstanceIdForBeanName(getBeanName(), id, this);
    Helper.busyWait(1);
  }

  /**
   * From an implementation perspective, a singleton bean class may have more
   * than one instances. So we cannot count instance inside bean class
   * constructor, which may record more than one instances of the bean class and
   * some of them may be subclass instances. But PostConstruct is only invoked
   * for the real singleton bean instance.
   */

  public void ping() {
  }
}
