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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.assembly.classpath.ejb;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  private TestBeanHome home;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logMsg("[Client] setup(): getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Looking up bean Home...");
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testDirectLibrary
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: We package an application with:
   *
   *                 - A utility .jar file containing the class ClassPathUtil.
   *                 This .jar file is not a J2EE module and do not appear in
   *                 the upper level application DD. It includes a "dummy" DD
   *                 though, that must be ignored by the deployment tool.
   *
   *                 - An EJB jar file. This jar file includes a Class-Path
   *                 header referencing the utility .jar file in its manifest
   *                 file, and does not contain any definition of ClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The EJB can create a ClassPathUtil instance at runtime,
   *                 and invoke a method on that instance. This validates that
   *                 the referenced .jar file appears in the logical classpath
   *                 of the EJB.
   *
   */
  public void testDirectLibrary() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Creating bean instance...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.testDirectLibrary();
      if (!pass) {
        throw new Fault("EJB direct classpath test failed");
      }
    } catch (Exception e) {
      throw new Fault("Caught exception: " + e, e);
    }
  }

  /**
   * @testName: testIndirectLibrary
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: We package an application with:
   *
   *                 - A utility .jar file containing the class
   *                 IndirectClassPathUtil. This .jar file is not a J2EE module
   *                 and do not appear in the upper level application DD. It
   *                 includes a "dummy" DD though, that must be ignored by the
   *                 deployment tool.
   *
   *                 - A second utility .jar file containing the class
   *                 ClassPathUtil. This .jar file is not a J2EE module and do
   *                 not appear in the upper level application DD. It includes a
   *                 "dummy" DD though, that must be ignored by the deployment
   *                 tool. This jar file includes in its manifest file a
   *                 Class-Path header referencing the utility .jar file
   *                 containing IndirectClassPathUtil. It does not contain any
   *                 definition of IndirectClassPathUtil.
   *
   *                 - An EJB jar file. This jar file includes in its manifest
   *                 file a Class-Path header referencing the second utility
   *                 .jar file. It does not contain any definition of
   *                 ClassPathUtil nor IndirectClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The EJB can create a ClassPathUtil instance at runtime,
   *                 and invoke a method on that instance. This validates that
   *                 the second utility .jar file appears in the logical
   *                 classpath of the EJB.
   *
   *                 - The EJB can create a IndirectClassPathUtil instance at
   *                 runtime, and invoke a method on that instance. This
   *                 validates that the first utility .jar file appears in the
   *                 logical classpath of the EJB.
   *
   */
  public void testIndirectLibrary() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Creating bean instance...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.testIndirectLibrary();
      if (!pass) {
        throw new Fault("EJB indirect classpath test failed");
      }
    } catch (Exception e) {
      throw new Fault("Caught exception: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
