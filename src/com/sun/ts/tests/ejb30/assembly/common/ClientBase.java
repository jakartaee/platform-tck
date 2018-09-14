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

package com.sun.ts.tests.ejb30.assembly.common;

import java.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

public class ClientBase extends EETest {

  @EJB(name = "remoteAssemblyBean", beanInterface = AssemblyRemoteIF.class)
  static protected AssemblyCommonIF remoteAssemblyBean;

  // helloBean is deployed in a separate ejb module (see tests/ejb30/common/
  // helloejbjar/). This ejb-ref is resolved by sun-ejb-jar.xml or sun-
  // application-client.xml
  @EJB(name = "helloBean")
  private static HelloRemoteIF helloBean;

  private static int postConstructCallsCount;

  private Properties props;

  public static void main(String[] args) {
    ClientBase theTests = new ClientBase();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  @PostConstruct
  private static void postConstruct() {
    postConstructCallsCount++;
  }

  /*
   * testName: postConstructInvokedInSuperElseWhere
   * 
   * @test_Strategy: PostConstruct method must be invoked even when it's in a
   * superclass not packaged in appclient-client.jar
   */
  public void postConstructInvokedInSuperElseWhere() throws Fault {
    if (postConstructCallsCount > 0) {
      TLogger
          .log("Got expected result.  Client.PostConstruct method is invoked "
              + postConstructCallsCount + " times");
    } else {
      throw new Fault("Expecting the Client.PostConstruct to be invoked "
          + " at least once, but actual " + postConstructCallsCount);
    }
  }

  /*
   * testName: remoteAdd
   * 
   * @test_Strategy:
   */
  public void remoteAdd() throws Fault {
    int a = 1;
    int b = 2;
    int additionalByInterceptor = 100 * 2;
    int expected = a + b + additionalByInterceptor;
    int actual = remoteAssemblyBean.remoteAdd(a, b);
    if (actual == expected) {
      TLogger.log("Got expected result: " + expected);
    } else {
      throw new Fault("Expecting " + expected + ", but actual " + actual
          + ". The interceptor may not have been invoked.");
    }
  }

  /*
   * testName: remoteAddByHelloEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * helloBean
   */
  public void remoteAddByHelloEJB() throws Fault {
    int a = 1;
    int b = 1;
    int expected = a + b;
    int actual = helloBean.add(a, b);
    if (actual == expected) {
      TLogger.log("Got expected result from calling helloBean.");
    } else {
      throw new Fault("Expecting helloBean.add to return " + expected
          + ", but actual was " + actual);
    }
  }

  /*
   * testName: remoteAddByHelloEJBFromAssemblyBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * assemblyBean -> helloBean
   */
  public void remoteAddByHelloEJBFromAssemblyBean() throws Fault {
    String result = remoteAssemblyBean.callHelloBean();
    if (result != null) {
      TLogger.log("Got expected result: " + result);
    } else {
      throw new Fault(
          "Expecting a non-null result from remoteAssemblyBean.callHelloBean(), but got null.");
    }
  }

  public void cleanup() throws Fault {
    // bean already removed in test method. If not, need to remove it here.
  }
}
