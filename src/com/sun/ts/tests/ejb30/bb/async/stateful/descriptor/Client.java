/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.async.stateful.descriptor;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2IF;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2RemoteIF;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorBean;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorClientBase;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorIF;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorRemoteIF;
import com.sun.ts.tests.ejb30.bb.async.common.descriptor.TimeoutDescriptorBeanBase;

@EJB(name = "timeoutDescriptorBean", beanInterface = TimeoutDescriptorBean.class, beanName = "TimeoutDescriptorBean")
public class Client extends DescriptorClientBase {

  @Override
  protected Descriptor2IF getDescriptor2IF() {
    return (Descriptor2IF) ServiceLocator
        .lookupByShortNameNoTry("descriptor2IF");
  }

  @Override
  protected Descriptor2RemoteIF getDescriptor2RemoteIF() {
    return (Descriptor2RemoteIF) ServiceLocator
        .lookupByShortNameNoTry("descriptor2RemoteIF");
  }

  @Override
  protected DescriptorIF getDescriptorIF() {
    return (DescriptorIF) ServiceLocator.lookupByShortNameNoTry("descriptorIF");
  }

  @Override
  protected DescriptorRemoteIF getDescriptorRemoteIF() {
    return (DescriptorRemoteIF) ServiceLocator
        .lookupByShortNameNoTry("descriptorRemoteIF");
  }

  @Override
  protected DescriptorBean getNoInterface() {
    return (DescriptorBean) ServiceLocator
        .lookupByShortNameNoTry("noInterface");
  }

  @Override
  protected TimeoutDescriptorBeanBase getTimeoutDescriptorBean() {
    return (TimeoutDescriptorBeanBase) ServiceLocator
        .lookupByShortNameNoTry("timeoutDescriptorBean");
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
   * @testName: remoteViews
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
