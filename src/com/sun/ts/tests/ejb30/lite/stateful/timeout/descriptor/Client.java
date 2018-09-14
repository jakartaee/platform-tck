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
package com.sun.ts.tests.ejb30.lite.stateful.timeout.descriptor;

import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.timeout.common.ClientBase;
import com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutBeanBase;
import com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutIF;

@EJBs({
    @EJB(name = ClientBase.minus1TimeoutBeanLocalName, beanName = "Minus1TimeoutBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.zeroTimeoutBeanLocalName, beanName = "ZeroTimeoutBean", beanInterface = StatefulTimeoutIF.class),

    @EJB(name = ClientBase.defaultUnitBeanNoInterfaceName, beanName = "DefaultUnitBean", beanInterface = StatefulTimeoutBeanBase.class),
    @EJB(name = ClientBase.secondUnitBeanNoInterfaceName, beanName = "SecondUnitBean", beanInterface = StatefulTimeoutBeanBase.class),
    @EJB(name = ClientBase.dayUnitBeanLocalName, beanName = "DayUnitBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.hourUnitBeanLocalName, beanName = "HourUnitBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.millisecondUnitBeanLocalName, beanName = "MillisecondUnitBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.microsecondUnitBeanLocalName, beanName = "MicrosecondUnitBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.nanosecondUnitBeanLocalName, beanName = "NanosecondUnitBean", beanInterface = StatefulTimeoutIF.class) })

public class Client extends ClientBase {
  /*
   * @testName: defaultUnitLocal
   * 
   * @test_Strategy: the default time unit is minute for @StatefulTimeout or
   * stateful-timeout. Verify that the target bean instance is removed after the
   * timeout value has passed.
   */
  /*
   * @testName: defaultUnitNoInterface
   * 
   * @test_Strategy: Same as above
   */
  /*
   * @testName: secondUnitLocal
   * 
   * @test_Strategy: The StatefulTimeout in the target bean (SecondUnitBean) is
   * specified with time unit second. Verify that the target bean instance is
   * removed after the timeout value has passed. Verify that the target bean
   * instance is still active within the timeout limit.
   */
  /*
   * @testName: secondUnitNoInterface
   * 
   * @test_Strategy: Same as above
   */

  /*
   * @testName: dayUnitLocal
   * 
   * @test_Strategy: the target bean is still active till timeout.
   */

  /*
   * @testName: minus1Timeout
   * 
   * @test_Strategy: the target bean not timeout until undeployment.
   */

  /*
   * @testName: hourUnitLocal
   * 
   * @test_Strategy: the target bean is still active till timeout.
   */
  /*
   * @testName: millisecondUnitLocal
   * 
   * @test_Strategy:
   */
  /*
   * @testName: microsecondUnitLocal
   * 
   * @test_Strategy:
   */
  /*
   * @testName: nanosecondUnitLocal
   * 
   * @test_Strategy:
   */
  /*
   * @testName: zeroTimeout
   * 
   * @test_Strategy: the target bean is eligible for removal immediately after
   * being idle.
   */
}
