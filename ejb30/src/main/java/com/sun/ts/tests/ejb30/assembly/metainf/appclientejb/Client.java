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

package com.sun.ts.tests.ejb30.assembly.metainf.appclientejb;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.assembly.common.ClientBase;
import com.sun.ts.tests.ejb30.assembly.common.ConcurrentLookup;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class Client extends ClientBase {

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: dirUsedInClassPath
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: include a directory in addition to jars in Class-Path
   * attribute of MANIFEST.MF
   */
  public void dirUsedInClassPath() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      loader = getClass().getClassLoader();
    }
    TLogger.log("ContextClassLoader: " + loader);
    CalculatorException e = new CalculatorException();
    TLogger.log(
        "CalculatorException.class is available in classpath.  Instance:" + e);
  }

  /*
   * @testName: dirUsedInClassPathEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: include a directory in addition to jars in Class-Path
   * attribute of MANIFEST.MF
   */
  public void dirUsedInClassPathEJB() {
    String e = remoteAssemblyBean.dirUsedInClassPath();
    TLogger.log(
        "CalculatorException.class is available in EJB classpath.  Instance:"
            + e);
  }

  /*
   * @testName: concurrentLookupHelloBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: Multiple threads doing InitialContext lookup of a ejb-ref
   * helloBean2
   */
  public void concurrentLookupHelloBean() throws TestFailedException {
    for (int i = 0; i < 2; i++) {
      String result = (new ConcurrentLookup()).concurrentLookup("helloBean2",
          null);
      TLogger.log(result);
    }
  }

  /*
   * @testName: concurrentLookupDataSource
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: Multiple threads doing InitialContext lookup of a
   * datasource
   */
  public void concurrentLookupDataSource() throws TestFailedException {
    for (int i = 0; i < 2; i++) {
      String result = (new ConcurrentLookup()).concurrentLookup("dataSource",
          null);
      TLogger.log(result);
    }
  }

  /*
   * @testName: postConstructInvokedInSuperElseWhere
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: PostConstruct method must be invoked even when it's in a
   * superclass not packaged in appclient-client.jar
   */

  /*
   * @testName: remoteAdd
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: remoteAddByHelloEJB
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * helloBean
   */

  /*
   * @testName: remoteAddByHelloEJBFromAssemblyBean
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: hello ejb is packaged as a standalone ejb module and
   * deployed separately. It client view jar is packaged inside current ear and
   * referenced by both appclient and ejb jar thru MANIFEST.MF appclient ->
   * assemblyBean -> helloBean
   */

}
