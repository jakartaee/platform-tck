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

/*
 *
 */

package com.sun.ts.tests.signaturetest.caj;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.signaturetest.SigTest;
import com.sun.ts.tests.signaturetest.SignatureTestDriver;
import com.sun.ts.tests.signaturetest.SignatureTestDriverFactory;

/*
 * This class is a simple example of a signature test that extends the
 * SigTest framework class.  This signature test is run outside of the
 * J2EE containers.  This class also contains the boilerplate
 * code necessary to create a signature test using the test framework.
 * To see a complete TCK example see the j2ee directory for the J2EE
 * TCK signature test class.
 */
public class CAJSigTest extends SigTest {

  // all the classes that make up common annotations
  private final String GENERATED = "jakarta.annotation.Generated";

  private final String MANAGEDBEAN = "jakarta.annotation.ManagedBean";

  private final String POSTCONSTRUCT = "jakarta.annotation.PostConstruct";

  private final String PREDESTROY = "jakarta.annotation.PreDestroy";

  private final String PRIORITY = "jakarta.annotation.Priority";

  private final String RESOURCE = "jakarta.annotation.Resource,jakarta.annotation.Resource$AuthenticationType";

  private final String RESOURCES = "jakarta.annotation.Resources";

  private final String SECURITYDECLAREROLES = "jakarta.annotation.security.DeclareRoles";

  private final String SECURITYDENYALL = "jakarta.annotation.security.DenyAll";

  private final String SECURITYPERMITALL = "jakarta.annotation.security.PermitAll";

  private final String SECURITYROLESALLOWED = "jakarta.annotation.security.RolesAllowed";

  private final String SECURITYRUNAS = "jakarta.annotation.security.RunAs";

  private final String SQLDATASOURCEDEFINITION = "jakarta.annotation.sql.DataSourceDefinition";

  private final String SQLDATASOURCEDEFINITIONS = "jakarta.annotation.sql.DataSourceDefinitions";

  private boolean ca_generated = false;

  private boolean ca_managedbean = false;

  private boolean ca_postconstruct = false;

  private boolean ca_priority = false;

  private boolean ca_predestroy = false;

  private boolean ca_resource = false;

  private boolean ca_resources = false;

  private boolean ca_securitydeclareroles = false;

  private boolean ca_securitydenyall = false;

  private boolean ca_securitypermitall = false;

  private boolean ca_securityrolesallowed = false;

  private boolean ca_securityrunas = false;

  private boolean ca_sqldatasourcedefinition = false;

  private boolean ca_sqldatasourcedefinitions = false;

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
    return new String[] {};
  }

  protected String[] getClasses() {
    // generate a String[] of the classes that should be verified based
    // on what annotations are selected in the ts.jte
    ArrayList classesArray = new ArrayList();
    if (ca_generated) {
      classesArray.add(GENERATED);
    }
    if (ca_managedbean) {
      classesArray.add(MANAGEDBEAN);
    }
    if (ca_postconstruct) {
      classesArray.add(POSTCONSTRUCT);
    }
    if (ca_priority) {
      classesArray.add(PRIORITY);
    }
    if (ca_predestroy) {
      classesArray.add(PREDESTROY);
    }
    if (ca_resource) {
      String[] s = RESOURCE.split(",");
      for (int i = 0; i < s.length; i++) {
        classesArray.add(s[i]);
      }
    }
    if (ca_resources) {
      classesArray.add(RESOURCES);
    }
    if (ca_securitydeclareroles) {
      classesArray.add(SECURITYDECLAREROLES);
    }
    if (ca_securitydenyall) {
      classesArray.add(SECURITYDENYALL);
    }
    if (ca_securitypermitall) {
      classesArray.add(SECURITYPERMITALL);
    }
    if (ca_securityrolesallowed) {
      classesArray.add(SECURITYROLESALLOWED);
    }
    if (ca_securityrunas) {
      classesArray.add(SECURITYRUNAS);
    }
    if (ca_sqldatasourcedefinition) {
      classesArray.add(SQLDATASOURCEDEFINITION);
    }
    if (ca_sqldatasourcedefinitions) {
      classesArray.add(SQLDATASOURCEDEFINITIONS);
    }
    return (String[]) classesArray.toArray(new String[classesArray.size()]);

  }

  /***** Boilerplate Code *****/

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    CAJSigTest theTests = new CAJSigTest();
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
   * @class.setup_props: ts_home; sigTestClasspath, Location of ca jar files;
   * ca.sig.generated; ca.sig.managedbean; ca.sig.postconstruct;
   * ca.sig.priority; ca.sig.predestroy; ca.sig.resource; ca.sig.resources;
   * ca.sig.securitydeclareroles; ca.sig.securitydenyall;
   * ca.sig.securitypermitall; ca.sig.securityrolesallowed;
   * ca.sig.securityrunas; ca.sig.sqldatasourcedefinition;
   * ca.sig.sqldatasourcedefinitions;
   */
  public void setup(String[] args, Properties p) throws Fault {
    super.setup(args, p);

    // read in the values from the ts.jte file
    ca_generated = Boolean.valueOf(p.getProperty("ca.sig.generated"))
        .booleanValue();
    ca_managedbean = Boolean.valueOf(p.getProperty("ca.sig.managedbean"))
        .booleanValue();
    ca_postconstruct = Boolean.valueOf(p.getProperty("ca.sig.postconstruct"))
        .booleanValue();
    ca_priority = Boolean.valueOf(p.getProperty("ca.sig.priority"))
        .booleanValue();
    ca_predestroy = Boolean.valueOf(p.getProperty("ca.sig.predestroy"))
        .booleanValue();
    ca_resource = Boolean.valueOf(p.getProperty("ca.sig.resource"))
        .booleanValue();
    ca_resources = Boolean.valueOf(p.getProperty("ca.sig.resources"))
        .booleanValue();
    ca_securitydeclareroles = Boolean
        .valueOf(p.getProperty("ca.sig.securitydeclareroles")).booleanValue();
    ca_securitydenyall = Boolean
        .valueOf(p.getProperty("ca.sig.securitydenyall")).booleanValue();
    ca_securitypermitall = Boolean
        .valueOf(p.getProperty("ca.sig.securitypermitall")).booleanValue();
    ca_securityrolesallowed = Boolean
        .valueOf(p.getProperty("ca.sig.securityrolesallowed")).booleanValue();
    ca_securityrunas = Boolean.valueOf(p.getProperty("ca.sig.securityrunas"))
        .booleanValue();
    ca_sqldatasourcedefinition = Boolean
        .valueOf(p.getProperty("ca.sig.sqldatasourcedefinition"))
        .booleanValue();
    ca_sqldatasourcedefinitions = Boolean
        .valueOf(p.getProperty("ca.sig.sqldatasourcedefinitions"))
        .booleanValue();
  }

  /*
   * @testName: signatureTest
   * 
   * @assertion: A CAJ platform must implement the required classes and APIs
   * specified in the CAJ Specification.
   * 
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   *
   */

  /*
   * Call the parent class's cleanup method.
   */

  /*
   * define which sig driver we will use
   */
  protected SignatureTestDriver getSigTestDriver() {

    if (driver == null) {
      driver = SignatureTestDriverFactory
          .getInstance(SignatureTestDriverFactory.SIG_TEST);
    }

    return driver;

  } // END getSigTestDriver

} // end class CAJSigTest
