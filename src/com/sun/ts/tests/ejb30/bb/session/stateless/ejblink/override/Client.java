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

package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override;

import com.sun.ts.tests.ejb30.common.ejblink.ClientBase;
import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF;
import javax.ejb.EJB;

public class Client extends ClientBase {
  // this ejb-ref is overridden in application-client.xml
  @EJB(name = "ejb/EjbLink1Bean", beanName = "EjbLink2Bean", description = "should be overridden by application-client.xml")
  private static EjbLinkIF bean1;

  // this ejb-ref is overridden in application-client.xml
  @EJB(name = "ejb/EjbLink2Bean", beanName = "EjbLink1Bean", description = "should be overridden by application-client.xml")
  private static EjbLinkIF bean2;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: callBean1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: callBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: callBean1Bean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: callBean2Bean1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  /*
   * @testName: callBean2Bean3
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
  /*
   * @testName: callBean2Bean1Local
   * 
   * @test_Strategy: bean1 packaged in jar1, bean2 and bean3 packaged in jar2.
   * Bean2 should be able to locally access bean1. <ejb-local-ref> is declared
   * in two_ejb.xml
   *
   */
  /*
   * @testName: callInjectedBean1
   * 
   * @test_Strategy: inject bean1, using fully qualified bean name
   * (<ejb-jar-name>#<bean-name>)
   *
   */
}
