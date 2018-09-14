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
package com.sun.ts.tests.ejb30.bb.session.stateful.timeout.annotated;

import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.timeout.common.ClientBase;
import com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutRemoteIF;

/**
 * Verifies @StatefulTimeout with no-interface, local business interface and
 * remote business interface. This test directory is similar to
 * lite/stateful/timeout, which only tests local and no-interface views
 */

@EJBs({
    @EJB(name = ClientBase.defaultUnitBeanRemoteName, beanName = "DefaultUnitBean", beanInterface = StatefulTimeoutRemoteIF.class),
    @EJB(name = ClientBase.secondUnitBeanRemoteName, beanName = "SecondUnitBean", beanInterface = StatefulTimeoutRemoteIF.class),
    @EJB(name = ClientBase.defaultUnitBeanNoInterfaceName, beanName = "DefaultUnitBean", beanInterface = DefaultUnitBean.class),
    @EJB(name = ClientBase.secondUnitBeanNoInterfaceName, beanName = "SecondUnitBean", beanInterface = SecondUnitBean.class) })
public class Client extends ClientBase {
  /*
   * @testName: defaultUnitLocal
   * 
   * @test_Strategy: the default time unit is minute for @StatefulTimeout or
   * stateful-timeout. Verify that the target bean instance is removed after the
   * timeout value has passed.
   */
  /*
   * @testName: defaultUnitRemote
   * 
   * @test_Strategy: Same as above
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
   * @testName: secondUnitRemote
   * 
   * @test_Strategy: Same as above
   */
  /*
   * @testName: secondUnitNoInterface
   * 
   * @test_Strategy: Same as above
   */
}
