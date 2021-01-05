/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * %W %E
 */

package com.sun.ts.tests.assembly.compat.standalone.jar.compat12_13;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class Client extends EETest {
  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private TestBeanHome home;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      logMsg("[Client] setup(): getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Looking up bean Home...");
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testStandaloneJar
   *
   * @assertion_ids: J2EE:SPEC:261; J2EE:SPEC:283; J2EE:SPEC:284
   *
   * @test_Strategy: Package an ejb-jar file using a J2EE 1.2 DD
   *                 (assembly_standalone_jar_compat12_13_component_ejb.jar).
   *
   *                 Package a .ear file (J2EE 1.3 DD's) containing an
   *                 application client accessing a bean in the stand-alone
   *                 ejb-jar module (JNDI names match in runtime information).
   *
   *                 Deploy the ejb-jar module and the .ear file.
   *
   *                 Run the client and check that we can call a business method
   *                 on the referenced bean at runtime.
   */
  public void testStandaloneJar() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Creating bean instance...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.ping();
      if (!pass) {
        throw new Fault("classpath test failed");
      }
    } catch (Exception e) {
      throw new Fault("classpath test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }
}
