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

package com.sun.ts.tests.samples.ejb.ee.simpleHello;

import java.io.*;
import java.util.*;
import javax.ejb.EJBHome;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 * The HelloClient class tests creating a simple hello bean using 
 * Sun's EJB Reference Implementation.
 * @author  Tester
 * @version %I%, $LastChangedDate$
 */
import com.sun.javatest.Status;

public class HelloClient extends EETest {
  final static String helloStr = "Hello World!";

  private static final String testName = "HelloTest";

  private static final String testProps = "sessionbean.properties";

  private static final String testDir = System.getProperty("user.dir");
  // private static final String testBean = "java:comp/env/ejb/Hello";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private Hello hr = null;

  private HelloHome home = null;

  private StringBuffer logData = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    HelloClient theTests = new HelloClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      jc = new TSNamingContext();
      logMsg("Looked up home!!");
      home = (HelloHome) jc.lookup("java:comp/env/ejb/Hello", HelloHome.class);
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /* Run test */

  /*
   * @testName: helloTest1
   * 
   * @assertion: Call the sayhello() "business" method and return the "hello"
   * string.
   * 
   * @test_Strategy: Create a stateful Session EJBean. Deploy it on the J2EE
   * server. From the EJB client. call the sayHello() business method. It should
   * return the "hello world" string.
   *
   */
  @CleanupMethod(name = "helloTestCleanup")
  public void helloTest1() throws Fault {
    try {
      hr = home.create(helloStr, props);
      // invoke method on the EJB
      logMsg(hr.sayHello());
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void helloTestCleanup() throws Fault {
    logMsg(
        "Cleanup method, helloTestCleanup, specified by @CleanupMethod annotation ok;");
  }
}
