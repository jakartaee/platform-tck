/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.async.singleton.descriptor;

import java.io.Serializable;

import com.sun.ts.tests.ejb30.lite.async.common.descriptor.DescriptorJsfClientBase;
import com.sun.ts.tests.ejb30.lite.async.common.descriptor.TimeoutDescriptorBeanBase;
import jakarta.ejb.EJB;

@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient extends DescriptorJsfClientBase implements Serializable {

  private static final long serialVersionUID = 1L;

  @EJB(beanName = "TimeoutDescriptorBean")
  private TimeoutDescriptorBean timeoutDescriptorBean;

  @Override
  protected TimeoutDescriptorBeanBase getTimeoutDescriptorBean() {
    return timeoutDescriptorBean;
  }

  /*
   * @testName: allViews
   * 
   * @test_Strategy:
   */
  /*
   * @testName: localViews
   * 
   * @test_Strategy:
   */
  /*
   * @testName: allParams
   * 
   * @test_Strategy:
   */
  /*
   * @testName: noParams
   * 
   * @test_Strategy:
   */
  /*
   * @testName: intParams
   * 
   * @test_Strategy:
   */
  /*
   * @testName: intParamsLocalViews
   * 
   * @test_Strategy:
   */
  /*
   * @testName: timeoutDescriptorBean
   * 
   * @test_Strategy:
   */

}
