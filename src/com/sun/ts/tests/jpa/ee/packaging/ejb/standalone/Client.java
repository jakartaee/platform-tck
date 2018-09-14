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

package com.sun.ts.tests.jpa.ee.packaging.ejb.standalone;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import javax.ejb.EJB;
import java.util.Properties;

public class Client extends EETest {

  @EJB(name = "ejb/Stateful3Bean", beanInterface = Stateful3IF.class)
  private static Stateful3IF bean;

  private Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      props = p;
      bean.init(props);
      cleanup();
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * Packaging:
   *
   * ejb-jar - EJB 3.0 Container-Managed Stateful Session Bean
   *
   * EJB-JAR is the root of the persistence unit persistence.xml resides in
   * EJB-JAR META-INF directory
   *
   * persistence unit configuration information: Container-Managed JTA Entity
   * Manager looked up with sessionContext.lookup EntityManager defined in
   * ejb.jar.xml with persistence-context-ref deployment descriptor
   */

  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:897; PERSISTENCE:SPEC:900;
   * PERSISTENCE:SPEC:901; PERSISTENCE:SPEC:907; PERSISTENCE:SPEC:938;
   * JavaEE:SPEC:10063; JavaEE:SPEC:10064; JavaEE:SPEC:10065
   * 
   * @test_Strategy: It is not required that an EJB-JAR containing a persistence
   * unit be packaged in an EAR unless the persistence unit contains persistence
   * classes in addition to those contained in the EJB-JAR.
   *
   * Deploy the standalone archive to the application server with the above
   * content. Create entities, persist them, then find.
   *
   */

  public void test1() throws Fault {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;

    try {
      pass = bean.test1();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);

    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  public void cleanup() throws Fault {
    try {
      bean.removeTestData();
    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in entity cleanup:", re);
    }
    TestUtil.logTrace("cleanup complete");
  }

}
