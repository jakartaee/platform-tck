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
package com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.ExcludeDefaultInterceptors;

/**
 * A no-interface singleton for storing the order in which lifcecycle
 * interceptor methods are called.
 */
@Startup
@Singleton
@ExcludeDefaultInterceptors
// to avoid infinite loop (InterceptorBaseBase -> HistorySingletonBean)
public class HistorySingletonBean {
  /**
   * This singleton stores PostConstruct records for multiple EJBs, and each may
   * have multiple instances. Use a map to ensure each bean instance has its own
   * slot. The map key is bean instance (this, if called inside a bean class or
   * its superclass; or InvocationContext.getTarget if called from an
   * interceptor class.
   */
  private Map<Object, List<String>> postConstructRecordsMap = new HashMap<Object, List<String>>();

  private List<String> aroundInvokeRecords = new ArrayList<String>();

  public List<String> getPostConstructRecordsFor(Object beanInstance) {
    List<String> rec = postConstructRecordsMap.get(beanInstance);
    return Collections.unmodifiableList(rec);
  }

  public void addPostConstructRecordFor(Object beanInstance, String aEntry) {
    List<String> rec = postConstructRecordsMap.get(beanInstance);
    if (rec == null) {
      rec = new ArrayList<String>();
      postConstructRecordsMap.put(beanInstance, rec);
    }
    rec.add(aEntry);
  }

  public List<String> getAroundInvokeRecords() {
    return Collections.unmodifiableList(aroundInvokeRecords);
  }

  public void addAroundInvokeRecord(String aEntry) {
    aroundInvokeRecords.add(aEntry);
  }

  public void clearAroundInvokeRecords() {
    aroundInvokeRecords.clear();
  }

  public List<String> getAndClearAroundInvokeRecords() {
    List<String> result = new ArrayList<String>(aroundInvokeRecords);
    clearAroundInvokeRecords();
    return result;
  }
}
