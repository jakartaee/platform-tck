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
 * $Id: JaspicSigTest.java 51070 2006-08-13 14:01:40Z lschwenk $
 */

/*
 * @(#)SigTestTSUtils.java	1.11 02/05/14
 */

package com.sun.ts.tests.signaturetest.jaspic;

import java.util.LinkedList;
import java.util.List;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTestEE;

/**
 * The JaspicSigTest class provides signature tests for the Java EE TCK. This
 * class extends SigTestEE which contains the signature test code. This class is
 * responsible for providing implementations of the abstract method defined in
 * SigTestEE, namely the getPackages method.
 */
public class JaspicSigTest extends SigTestEE {

  public static final String EJB_VEHICLE = "ejb";

  public static final String SERVLET_VEHICLE = "servlet";

  public static final String JSP_VEHICLE = "jsp";

  public static final String APP_CLIENT_VEHICLE = "appclient";

  public static final String NO_VEHICLE = "standalone";

  /*
   * Defines the packages that are included when running signature tests for any
   * container (the default packages). This includes the appclient, ejb, jsp,
   * and servlet containers.
   */
  private static final String[] DEFAULT_PKGS = { "jakarta.security.auth.message",
      "jakarta.security.auth.message.callback",
      "jakarta.security.auth.message.config",
      "jakarta.security.auth.message.module", };

  /*
   * Defines additional packages that are included when running signature tests
   * for the ejb, jsp and servlet containers.
   */
  private static final String[] EJB_SERVLET_JSP_PKGS = {};

  /*
   * Defines additional packages that are included when running signature tests
   * for the jsp and servlet containers.
   */
  private static final String[] SERVLET_JSP_PKGS = {};

  private static final String[] NO_CONTAINER_PKGS = {
      "jakarta.security.auth.message", "jakarta.security.auth.message.callback",
      "jakarta.security.auth.message.config",
      "jakarta.security.auth.message.module", };

  /**
   * Adds the default packages and the command line flags to the specified list
   * for each package defined in the list of default packages to check during
   * signature tests. Note: The specified list is modified as a result of this
   * method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addDefaultPkgs(List<String> sigArgsList) {
    for (int i = 0; i < DEFAULT_PKGS.length; i++) {
      sigArgsList.add(DEFAULT_PKGS[i]);
    }
  }

  /**
   * Adds the ejb, servlet, and jsp packages and the command line flags to the
   * specified list for each package defined in the list of ejb, servlet, and
   * jsp packages to check during signature tests. Note: The specified list is
   * modified as a result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addEjbServletJspPkgs(List<String> sigArgsList) {
    for (int i = 0; i < EJB_SERVLET_JSP_PKGS.length; i++) {
      sigArgsList.add(EJB_SERVLET_JSP_PKGS[i]);
    }
  }

  /**
   * Adds the servlet, and jsp packages and the command line flags to the
   * specified list for each package defined in the list of servlet, and jsp
   * packages to check during signature tests. Note: The specified list is
   * modified as a result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addServletJspPkgs(List<String> sigArgsList) {
    for (int i = 0; i < SERVLET_JSP_PKGS.length; i++) {
      sigArgsList.add(SERVLET_JSP_PKGS[i]);
    }
  }

  /**
   * Adds the pkgs for tests to be run in NO Container (ie standalone) packages
   * to check during signature tests. Note: The specified list is modified as a
   * result of this method call.
   *
   * @param sigArgsList
   *          The arg list being constructed to pass to the utility that records
   *          and runs signature file tests.
   */
  private void addNoContainerPkgs(List<String> sigArgsList) {
    for (int i = 0; i < NO_CONTAINER_PKGS.length; i++) {
      sigArgsList.add(NO_CONTAINER_PKGS[i]);
    }
  }

  /**
   * ** Abstract Method Implementations **** Returns a list of strings where
   * each string represents a package name. Each package name will have it's
   * signature tested by the signature test framework.
   *
   * @param vehicleName
   *          The name of the Jaspic container where the signature tests should
   *          be conducted.
   *
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @param vehicleName
   *          The name of the Jaspic container where the signature tests should
   *          be conducted.
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */
  protected String[] getPackages(String vehicleName) {
    List<String> packages = new LinkedList<String>();

    if (vehicleName.equals(NO_VEHICLE)) {
      addNoContainerPkgs(packages);
    } else {
      addDefaultPkgs(packages); // add default vehicle packages
      if (vehicleName.equals(EJB_VEHICLE) || vehicleName.equals(SERVLET_VEHICLE)
          || vehicleName.equals(JSP_VEHICLE)) {
        addEjbServletJspPkgs(packages);
      }
      if (vehicleName.equals(SERVLET_VEHICLE)
          || vehicleName.equals(JSP_VEHICLE)) {
        addServletJspPkgs(packages);
      }
    }
    return packages.toArray(new String[packages.size()]);
  }

  /**
   * ** Boilerplate Code ****
   */

  /*
   * Initial entry point for JavaTest.
   */
  public static void main(String[] args) {
    JaspicSigTest theTests = new JaspicSigTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: sigTestClasspath, Location of Jaspic jar files;
   * ts_home, The base path of this TCK;
   */

  /*
   * @testName: signatureTest
   * 
   * @assertion: A Jaspic platform must implement the required classes and and
   * APIs specified in the Jaspic Platform Specification.
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
