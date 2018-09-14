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
package com.sun.ts.tests.jsf.api.javax_faces.validator.common;

import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public abstract class BaseValidatorClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  // ------------------------------------------------------- StateHolder Tests
  /*
   * testName: stateHolderIsSetTransientTest
   * 
   * @test_Strategy: Verify {is,set}Transient() - if a value is set, the same
   * value is returned.
   */
  public void stateHolderIsSetTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderIsSetTransientTest");
    invoke();
  }

  /*
   * testName: stateHolderSaveRestoreStateTest
   * 
   * @test_Strategy: Verify saveState returns a serialized object of the
   * component's current state and that this state can be restored when passing
   * this state back into the restoreState() method.
   */
  public void stateHolderSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveRestoreStateTest");
    invoke();
  }

  /*
   * testName: stateHolderRestoreStateNPETest
   * 
   * @test_Strategy: Verify that restoreState throws a NullpointerException if
   * either FacesContext or state is null.
   */
  public void stateHolderRestoreStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderRestoreStateNPETest");
    invoke();
  }

  /*
   * testName: stateHolderSaveStateNPETest
   * 
   * @test_Strategy: Verify that saveState throws a NullpointerException if
   * either FacesContext null.
   */
  public void stateHolderSaveStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveStateNPETest");
    invoke();
  }

  // ------------------------------------------------ PartialStateHolder Tests

  /*
   * testName: validatorPartialStateTest
   * 
   * @test_Strategy: Verify that the following method calls perform as expected.
   * 
   * RegexValidator.clearInitialState(); RegexValidator.initialStateMarked();
   * RegexValidator.markInitialState();
   * 
   * 
   * @since: 2.0
   */
  public void validatorPartialStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "validatorPartialStateTest");
    invoke();
  }

} // end of URLClient
