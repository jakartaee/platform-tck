/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated;

import java.io.Serializable;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.JsfClientBase;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBs;

@EJBs({
    @EJB(name = AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanLocal, beanName = "BeanClassMethodLevelOverrideAccessTimeoutBean", beanInterface = AccessTimeoutIF.class),
    @EJB(name = AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanLocal, beanName = "BeanClassMethodLevelAccessTimeoutBean", beanInterface = AccessTimeoutIF.class),
    @EJB(name = AccessTimeoutIF.beanClassLevelAccessTimeoutBeanLocal, beanName = "BeanClassLevelAccessTimeoutBean", beanInterface = AccessTimeoutIF.class),
    @EJB(name = AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanLocal, beanName = "AnnotatedSuperClassAccessTimeoutBean", beanInterface = AccessTimeoutIF.class) })
@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient extends JsfClientBase implements Serializable {

    private static final long serialVersionUID = 1L;

  /*
   * @testName: beanClassLevel
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanClassLevel2
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanSuperClassLevel
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanSuperClassMethodLevel
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanSuperClassMethodLevelOverride
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanClassMethodLevel
   * 
   * @test_Strategy:
   */
  /*
   * @testName: beanClassMethodLevelOverride
   * 
   * @test_Strategy:
   */

}
