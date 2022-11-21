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
 * @(#)JSTLClient.java	1.3 03/02/10
 */

/*
 *
 */

package com.sun.ts.tests.signaturetest.jstl;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTest;

/*
 * This class is a simple example of a signature test that extends the
 * SigTest framework class.  This signature test is run outside of the
 * Java EE containers.  This class also contains the boilerplate
 * code necessary to create a signature test using the test framework.
 * To see a complete TCK example see the javaee directory for the Java EE
 * TCK signature test class.
 */
public class JSTLClient extends SigTest {

  /***** Abstract Method Implementation *****/

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */
  protected String[] getPackages() {
    return new String[] { "jakarta.servlet.jsp.jstl.core",
        "jakarta.servlet.jsp.jstl.fmt", "jakarta.servlet.jsp.jstl.sql",
        "jakarta.servlet.jsp.jstl.tlv" };
  }

  /***** Boilerplate Code *****/

 /*
  *   @class.setup_props: ts_home, The base path of this TCK;
  *                       sigTestClasspath;
  */

 /*
  * @testName:         signatureTest
  * @assertion:        A Provisioning platform must implement the required
  *                    classes and APIs specified in the Provisioning
  *                    Specification.
  * @test_Strategy:    Using reflection, gather the implementation specific
  *                    classes and APIs.  Compare these results with the
  *                    expected (required) classes and APIs.
  *
  */
  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

} // end class JSTLClient
