/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry;

import com.sun.ts.tests.ejb30.common.annotation.enventry.ClientBase;
import com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryIF;
import javax.ejb.EJB;
import com.sun.javatest.Status;

public class Client extends ClientBase {
  @EJB(beanName = "EnvEntrySetterBean")
  private static EnvEntryIF envEntrySetterBean;

  @EJB(beanName = "EnvEntryFieldBean")
  private static EnvEntryIF envEntryFieldBean;

  @EJB(beanName = "EnvEntryTypeBean")
  private static EnvEntryIF envEntryTypeBean;

  protected EnvEntryIF getEnvEntrySetterBean() {
    return envEntrySetterBean;
  }

  protected EnvEntryIF getEnvEntryFieldBean() {
    return envEntryFieldBean;
  }

  protected EnvEntryIF getEnvEntryTypeBean() {
    return envEntryTypeBean;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: stringTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */

  /*
   * @testName: charTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: intTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: booleanTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: doubleTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: byteTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: shortTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: longTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: floatTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: stringDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: charDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: intDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: booleanDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: doubleDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: byteDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: shortDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: longDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
  /*
   * @testName: floatDeepTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   * 
   */
}
