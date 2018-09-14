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
 * @(#)JTATestClient.java	1.3  03/05/16
 */

package com.sun.ts.tests.connector.xa.transaction.jta;

import java.io.*;
import java.util.*;
import javax.ejb.EJBHome;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

public class JTATestClient extends EETest {
  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private JTATest hr = null;

  private JTATestHome home = null;

  private StringBuffer logData = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    JTATestClient theTests = new JTATestClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * whitebox-xa; rauser1; rapassword1;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.

    props = p;

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.

    try {
      jc = new TSNamingContext();
      logMsg("Looked up home!!");
      home = (JTATestHome) jc.lookup("java:comp/env/ejb/JTATest",
          JTATestHome.class);
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * @testName: testXAResource1
   *
   * @assertion_ids: Connector:SPEC:54; Connector:JAVADOC:187;
   * Connector:JAVADOC:229;
   * 
   *
   * @test_Strategy: Check to see if getXAResource method on the
   * ManagedConnectionFactory was called. If it was then check if the
   * XAResource.start method was called to begin the transaction. This verfies
   * that the Transaction Manager is using the XAResource to perform the JTA
   * transaction.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testXAResource1() throws Fault {
    try {

      hr = home.create(props);

      boolean result = hr.testXAResource1();

      System.out.println("JTATestClient result " + result);

      if (result) {
        logMsg("Test Passed");
      } else {
        throw new Fault("Embedded resource adapter test failed");
      }

      // invoke method on the EJB
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: testXAResource2
   *
   * @assertion_ids: Connector:SPEC:46; Connector:SPEC:48;
   * 
   *
   * @test_Strategy: Check for the validity of the connection which is returned
   * by performing some queries to the TSeis. Check to see if TM calls
   * XAResourceImpl.commit without calling prepare.
   *
   */
  public void testXAResource2() throws Fault {
    try {

      hr = home.create(props);

      boolean result = hr.testXAResource2();

      System.out.println("JTATestClient result " + result);

      if (result) {
        logMsg("Test Passed");
      } else {
        throw new Fault("Embedded resource adapter test failed");
      }

      // invoke method on the EJB
      logMsg("Test passed;");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- none in this case */
  public void cleanup() throws Fault {
    logMsg("Cleanup ok;");
  }
}
