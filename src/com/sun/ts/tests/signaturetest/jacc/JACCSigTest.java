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
 * $Id$
 */

package com.sun.ts.tests.signaturetest.jacc;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTestEE;

/*
 * This class is a simple example of a signature test that extends the
 * SigTestEE framework class.  This signature test must be run from
 * within the Java EE containers.  This class also contains the boilerplate
 * code necessary to create a signature test using the test framework.
 * To see a complete TCK example see the javaee directory for the Java EE
 * TCK signature test class.
 */
public class JACCSigTest extends SigTestEE {

  private static final String[] packagesNameArray = { "jakarta.security.jacc" };

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @param vehicleName
   *          The name of the Java EE container where the signature tests should
   *          be conducted.
   * @return String[] The names of the packages whose signatures should be
   *         verified within the specified vehicle.
   */
  protected String[] getPackages(String vehicleName) {
    return packagesNameArray;
  }

  /*
   * Initial entry point for JavaTest.
   */
  public static void main(String[] args) {
    JACCSigTest theTests = new JACCSigTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * The following comments are specified in the base class that defines the
   * signature tests. This is done so the test finders will find the right class
   * to run. The implementation of these methods is inherited from the super
   * class which is part of the signature test framework.
   */

  /*
   * @class.setup_props: sigTestClasspath; ts_home, The base path of this TCK;
   */

  /*
   * @testName: signatureTest
   * 
   * @assertion: A Java EE platform must implement the required classes and APIs
   * specified in the JSR 115 Specification.
   *
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   *
   */

} // end class JACCSigTest
