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

package com.sun.ts.tests.ejb30.misc.getresource.appclient;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceIF;

public class Client extends EETest {
  protected Properties props;

  private GetResourceIF tester = new GetResourceDelegate();

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  public void cleanup() throws Fault {
  }

  /*
   * @testName: getResourceNullParam
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceNullParam() throws TestFailedException {
    tester.getResourceNullParam();
  }

  /*
   * @testName: getResourceAsStreamNullParam
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceAsStreamNullParam() throws TestFailedException {
    tester.getResourceAsStreamNullParam();
  }

  /*
   * @testName: getResourceNonexisting
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceNonexisting() throws TestFailedException {
    tester.getResourceNonexisting();
  }

  /*
   * @testName: getResourceAsStreamNonexisting
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceAsStreamNonexisting() throws TestFailedException {
    tester.getResourceAsStreamNonexisting();
  }

  /*
   * @testName: getResourceSamePackage
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceSamePackage() throws TestFailedException {
    tester.getResourceSamePackage();
  }

  /*
   * @testName: getResourceAsStreamSamePackage
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceAsStreamSamePackage() throws TestFailedException {
    tester.getResourceAsStreamSamePackage();
  }

  /*
   * @testName: getResourceResolve
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceResolve() throws TestFailedException {
    tester.getResourceResolve();
  }

  /*
   * @testName: getResourceAsStreamResolve
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceAsStreamResolve() throws TestFailedException {
    tester.getResourceAsStreamResolve();
  }

  /*
   * @testName: getResourceResolveEarLib
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceResolveEarLib() throws TestFailedException {
    tester.getResourceResolveEarLib();
  }

  /*
   * @testName: getResourceAsStreamResolveEarLib
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: access resource in application client component
   */
  public void getResourceAsStreamResolveEarLib() throws TestFailedException {
    tester.getResourceAsStreamResolveEarLib();
  }
}
