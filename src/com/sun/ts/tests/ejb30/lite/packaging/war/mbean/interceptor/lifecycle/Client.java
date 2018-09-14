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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF;

public class Client extends
    com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.ClientBase {

  private InterceptorIF bean;

  private InterceptorIF overrideBean;

  private InterceptorIF override34Bean;

  @Override
  protected InterceptorIF getBean() {
    return bean;
  }

  @Override
  protected InterceptorIF getOverride34Bean() {
    return override34Bean;
  }

  @Override
  protected InterceptorIF getOverrideBean() {
    return overrideBean;
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    Helper.getLogger().info("In PreDestroy of " + this);
  }

  @Resource(lookup = "java:module/InterceptorBean", type = InterceptorBean.class)
  protected void setBean(InterceptorIF b) {
    bean = b;
  }

  @Resource(lookup = "java:module/InterceptorOverrideBean", type = InterceptorOverrideBean.class)
  protected void setOverrideBean(InterceptorIF b) {
    overrideBean = b;
  }

  @Resource(lookup = "java:module/InterceptorOverride34Bean", type = InterceptorOverride34Bean.class)
  protected void setOverride34Bean(InterceptorIF b) {
    override34Bean = b;
  }

  /*
   * @testName: allInterceptors
   * 
   * @test_Strategy: all interceptors class-level should be invoked, as well as
   * PostConstruct methods on bean class. For Interceptor8, the PostConstruct
   * methods from super and super-super classes are overridden and therefore are
   * not invoked.
   */
  /*
   * @testName: overrideBeanInterceptorMethod
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a non-PostConstruct method. This test also
   * excludes default and class-level interceptors.
   */
  /*
   * @testName: overrideBeanInterceptorMethod3
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a PostConstruct method.
   */

  /*
   * @testName: identityHashCode
   * 
   * @test_Strategy:
   */
  public void identityHashCode() {
    Set<Integer> instances = new HashSet<Integer>();
    for (int i = 0; i < 50; i++) {
      AManagedBean b = (AManagedBean) ServiceLocator
          .lookupNoTry("java:module/AManagedBean");
      int c = b.identityHashCode();
      assertEquals("Existing instance: " + b + "?", false,
          instances.contains(c));
      instances.add(c);
    }
    appendReason(instances);
  }
}
