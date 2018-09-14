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
package com.sun.ts.tests.ejb30.lite.stateful.timeout.common;

import static com.sun.ts.lib.util.TestUtil.sleepSec;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.ejb.NoSuchEJBException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

@EJBs({
    @EJB(name = ClientBase.defaultUnitBeanLocalName, beanName = "DefaultUnitBean", beanInterface = StatefulTimeoutIF.class),
    @EJB(name = ClientBase.secondUnitBeanLocalName, beanName = "SecondUnitBean", beanInterface = StatefulTimeoutIF.class) })
abstract public class ClientBase extends EJBLiteClientBase {

  public static final String defaultUnitBeanLocalName = "defaultUnitBeanLocalName";

  public static final String defaultUnitBeanRemoteName = "defaultUnitBeanRemoteName";

  public static final String defaultUnitBeanNoInterfaceName = "defaultUnitBeanNoInterfaceName";

  public static final String secondUnitBeanLocalName = "secondUnitBeanLocalName";

  public static final String secondUnitBeanRemoteName = "secondUnitBeanRemoteName";

  public static final String secondUnitBeanNoInterfaceName = "secondUnitBeanNoInterfaceName";

  // for uncommon time unit, just test it with local interface view
  public static final String dayUnitBeanLocalName = "dayUnitBeanLocalName";

  public static final String hourUnitBeanLocalName = "hourUnitBeanLocalName";

  public static final String millisecondUnitBeanLocalName = "millisecondUnitBeanLocalName";

  public static final String microsecondUnitBeanLocalName = "microsecondUnitBeanLocalName";

  public static final String nanosecondUnitBeanLocalName = "nanosecondUnitBeanLocalName";

  public static final String minus1TimeoutBeanLocalName = "minus1TimeoutBeanLocalName";

  public static final String zeroTimeoutBeanLocalName = "zeroTimeoutBeanLocalName";

  protected StatefulTimeoutIF getMinus1TimeoutBeanLocal() {
    return (StatefulTimeoutIF) lookup(minus1TimeoutBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getZeroTimeoutBeanLocal() {
    return (StatefulTimeoutIF) lookup(zeroTimeoutBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getDefaultUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(defaultUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getDefaultUnitBeanRemote() {
    return (StatefulTimeoutIF) lookup(defaultUnitBeanRemoteName, null, null);
  }

  protected StatefulTimeoutIF getDefaultUnitBeanNoInterface() {
    return (StatefulTimeoutIF) lookup(defaultUnitBeanNoInterfaceName, null,
        null);
  }

  protected StatefulTimeoutIF getSecondUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(secondUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getSecondUnitBeanRemote() {
    return (StatefulTimeoutIF) lookup(secondUnitBeanRemoteName, null, null);
  }

  protected StatefulTimeoutIF getSecondUnitBeanNoInterface() {
    return (StatefulTimeoutIF) lookup(secondUnitBeanNoInterfaceName, null,
        null);
  }

  protected StatefulTimeoutIF getDayUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(dayUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getHourUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(hourUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getMillisecondUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(millisecondUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getMicrosecondUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(microsecondUnitBeanLocalName, null, null);
  }

  protected StatefulTimeoutIF getNanosecondUnitBeanLocal() {
    return (StatefulTimeoutIF) lookup(nanosecondUnitBeanLocalName, null, null);
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger()
        .info("The test will wait for at least "
            + StatefulTimeoutIF.EXTRA_WAIT_SECONDS
            + " seconds before verifying the bean's timeout and removal.");
  }

  private void assertBeanRemoved(StatefulTimeoutIF b) {
    try {
      b.ping();
      throw new RuntimeException("Expecting NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      appendReason("Got expected NoSuchEJBException: " + e);
    }
  }

  private void assertBeanActive(StatefulTimeoutIF b) {
    b.ping();
    appendReason("The target bean is still active, as expected.");
  }

  private void defaultUnit(StatefulTimeoutIF b) {
    b.ping();
    sleepSec((int) (StatefulTimeoutIF.TIMEOUT_MINUTES * 50));
    assertBeanActive(b);
    sleepSec((int) (StatefulTimeoutIF.TIMEOUT_MINUTES * 60
        + StatefulTimeoutIF.EXTRA_WAIT_SECONDS));
    assertBeanRemoved(b);
  }

  private void secondUnit(StatefulTimeoutIF b) {
    b.ping();
    sleepSec((int) (StatefulTimeoutIF.TIMEOUT_SECONDS / 2));
    assertBeanActive(b);
    sleepSec((int) (StatefulTimeoutIF.TIMEOUT_SECONDS
        + StatefulTimeoutIF.EXTRA_WAIT_SECONDS));
    assertBeanRemoved(b);
  }

  /*
   * testName: defaultUnitLocal
   * 
   * @test_Strategy: the default time unit is minute for @StatefulTimeout or
   * stateful-timeout. Verify that the target bean instance is removed after the
   * timeout value has passed. Verify that the target bean instance is still
   * active within the timeout limit.
   */
  public void defaultUnitLocal() {
    defaultUnit(getDefaultUnitBeanLocal());
  }

  /*
   * testName: defaultUnitRemote
   * 
   * @test_Strategy: Same as above. This test invokes remote business method and
   * is not available in ejb lite test suite.
   */
  public void defaultUnitRemote() {
    defaultUnit(getDefaultUnitBeanRemote());
  }

  /*
   * testName: defaultUnitNoInterface
   * 
   * @test_Strategy: Same as above
   */
  public void defaultUnitNoInterface() {
    defaultUnit(getDefaultUnitBeanNoInterface());
  }

  /*
   * testName: secondUnitLocal
   * 
   * @test_Strategy: The StatefulTimeout in the target bean (SecondUnitBean) is
   * specified with time unit second. Verify that the target bean instance is
   * removed after the timeout value has passed. Verify that the target bean
   * instance is still active within the timeout limit.
   */
  public void secondUnitLocal() {
    secondUnit(getSecondUnitBeanLocal());
  }

  /*
   * testName: secondUnitRemote
   * 
   * @test_Strategy: Same as above. This test invokes remote business method and
   * is not available in ejb lite test suite.
   */
  public void secondUnitRemote() {
    secondUnit(getSecondUnitBeanRemote());
  }

  /*
   * testName: secondUnitNoInterface
   * 
   * @test_Strategy: Same as above
   */
  public void secondUnitNoInterface() {
    secondUnit(getSecondUnitBeanNoInterface());
  }

  /*
   * testName: dayUnitLocal
   * 
   * @test_Strategy: the target bean is still active till timeout.
   */
  public void dayUnitLocal() {
    StatefulTimeoutIF b = getDayUnitBeanLocal();
    b.ping();
    sleepSec((int) StatefulTimeoutIF.EXTRA_WAIT_SECONDS);
    assertBeanActive(b);
  }

  /*
   * testName: minus1Timeout
   * 
   * @test_Strategy: the target bean not timeout until undeployment.
   */
  public void minus1Timeout() {
    StatefulTimeoutIF b = getMinus1TimeoutBeanLocal();
    b.ping();
    sleepSec((int) StatefulTimeoutIF.EXTRA_WAIT_SECONDS);
    assertBeanActive(b);
  }

  /*
   * testName: hourUnitLocal
   * 
   * @test_Strategy: the target bean is still active till timeout.
   */
  public void hourUnitLocal() {
    StatefulTimeoutIF b = getHourUnitBeanLocal();
    b.ping();
    sleepSec((int) StatefulTimeoutIF.EXTRA_WAIT_SECONDS);
    assertBeanActive(b);
  }

  /*
   * testName: millisecondUnitLocal
   * 
   * @test_Strategy:
   */
  public void millisecondUnitLocal() {
    secondUnit(getMillisecondUnitBeanLocal());
  }

  /*
   * testName: microsecondUnitLocal
   * 
   * @test_Strategy:
   */
  public void microsecondUnitLocal() {
    secondUnit(getMicrosecondUnitBeanLocal());
  }

  /*
   * testName: nanosecondUnitLocal
   * 
   * @test_Strategy:
   */
  public void nanosecondUnitLocal() {
    secondUnit(getNanosecondUnitBeanLocal());
  }

  /*
   * testName: zeroTimeout
   * 
   * @test_Strategy: the target bean is eligible for removal immediately after
   * being idle.
   */
  public void zeroTimeout() {
    boolean passed = false;
    StatefulTimeoutIF b = getZeroTimeoutBeanLocal();

    try {
      b.ping();
    } catch (NoSuchEJBException e) {
      appendReason("Got the expected ", e, TestUtil.NEW_LINE, b,
          " was removed immediately after lookup.");
      passed = true;
    }

    if (passed) {
      return;
    }

    sleepSec((int) (StatefulTimeoutIF.TIMEOUT_SECONDS
        + StatefulTimeoutIF.EXTRA_WAIT_SECONDS));
    assertBeanRemoved(b);
  }
}
