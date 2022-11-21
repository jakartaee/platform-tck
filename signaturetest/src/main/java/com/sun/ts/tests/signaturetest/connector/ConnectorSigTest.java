/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.signaturetest.connector;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTest;

/**
 * The ConnectorSigTest class provides signature tests for the Java EE TCK. This
 * class extends SigTest which contains the signature test code. This class is
 * responsible for providing implementations of the abstract method defined in
 * SigTest, namely the getPackages method.
 */
public class ConnectorSigTest extends SigTest {

  /*
   * Returns a list of strings where each string represents a package or
   * classname. Each package or classname have it's signature tested by the
   * signature test framework.
   * 
   */
  protected String[] getPackages() {
    return new String[] { "jakarta.resource", "jakarta.resource.cci",
        "jakarta.resource.spi", "jakarta.resource.spi.work",
        "jakarta.resource.spi.endpoint", "jakarta.resource.spi.security" };

  };

  /**
   * ** Boilerplate Code ****
   */

  /*
   * Initial entry point for JavaTest.
   */
  public static void main(String[] args) {
    ConnectorSigTest theTests = new ConnectorSigTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * The following comments are specified in the base class that defines the
   * signature tests. This is done so the test finders will find the right class
   * to run. The implementation of these methods is inherited from the super
   * class which is part of the signature test framework.
   */

  // NOTE: If the API under test is not part of your testing runtime
  // environment, you may use the property sigTestClasspath to specify
  // where the API under test lives. This should almost never be used.
  // Normally the API under test should be specified in the classpath
  // of the VM running the signature tests. Use either the first
  // comment or the one below it depending on which properties your
  // signature tests need. Please do not use both comments.

  /*
   * @class.setup_props: ts_home, The base path of this TCK; sigTestClasspath;
   */

  /*
   * @testName: signatureTest
   * 
   * @assertion: A Connector implementation must provide the required classes
   * and and APIs specified in the JavaEE Connector Architecture Spec.
   * 
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   *
   */

  /*
   * Call the parent class's cleanup method.
   */

} // end class CTSSigTest
