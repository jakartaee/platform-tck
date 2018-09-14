/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanNoInterface;

import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.ClientBase;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.StatefulConcurrencyBeanBase;

@EJBs({
    @EJB(name = containerConcurrencyBeanNoInterface, beanName = "ContainerConcurrencyBean", beanInterface = StatefulConcurrencyBeanBase.class),
    @EJB(name = defaultConcurrencyBeanNoInterface, beanName = "DefaultConcurrencyBean", beanInterface = StatefulConcurrencyBeanBase.class),
    @EJB(name = notAllowedConcurrencyBeanNoInterface, beanName = "NotAllowedConcurrencyBean", beanInterface = StatefulConcurrencyBeanBase.class)

})
public class Client extends ClientBase {
  /*
   * @testName: notAllowed
   * 
   * @test_Strategy:
   */
  /*
   * @testName: containerConcurrent
   * 
   * @test_Strategy:
   */
  /*
   * @testName: defaultConcurrent
   * 
   * @test_Strategy:
   */
}
