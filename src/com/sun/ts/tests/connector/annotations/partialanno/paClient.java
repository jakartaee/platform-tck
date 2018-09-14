/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.connector.annotations.partialanno;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.util.*;

/*
 * These are the tests that are used to validate behavior
 * with RA's that utilize combinations of annotations and DD's.
 */
public class paClient extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con2 = null;

  private String partialMDJndiName = null;

  private String multiAnnoJndiName = null;

  private String uname = null;

  private String password = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    paClient theTests = new paClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-mixedmode; whitebox-multianno; rauser1;
   * rapassword1;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    partialMDJndiName = p.getProperty("whitebox-mixedmode");
    System.out.println("partialMDJndiName = : " + partialMDJndiName);

    multiAnnoJndiName = p.getProperty("whitebox-multianno");
    System.out.println("multiAnnoJndiName = : " + multiAnnoJndiName);

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      logMsg("Performing jndi lookup using: " + partialMDJndiName);
      nctx = new TSNamingContext();
      ds2 = (TSDataSource) nctx.lookup(partialMDJndiName);
      if (ds2 == null) {
        TestUtil.logMsg("ds2 lookup failed and is null");
      } else {
        TestUtil.logMsg("ds2 lookup is not null");
      }
      TestUtil.logMsg("ds2 JNDI lookup: " + ds2);

      logMsg("Performing jndi lookup using: " + multiAnnoJndiName);
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(multiAnnoJndiName);
      if (ds1 == null) {
        TestUtil.logMsg("ds1 lookup failed and is null");
      } else {
        TestUtil.logMsg("ds1 lookup is not null");
      }
      TestUtil.logMsg("ds1 JNDI lookup: " + ds2);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testMixedModeConfigPropertyMCF
   *
   * @assertion_ids: Connector:SPEC:264; Connector:SPEC:307; Connector:SPEC:277;
   * Connector:SPEC:280; Connector:SPEC:268; Connector:JAVADOC:234;
   * Connector:JAVADOC:145; Connector:JAVADOC:146; Connector:JAVADOC:147;
   * Connector:JAVADOC:148; Connector:JAVADOC:140; Connector:JAVADOC:142;
   * Connector:JAVADOC:143; Connector:JAVADOC:139;
   *
   * @test_Strategy: This is testing that a RA with annotations and a partial DD
   * will take the DD' config property and set it within the (annotated)
   * ManagedConnectionFactory by checking that the setter for the config
   * property was called. (as described in connector 1.6 spec section 18.5) The
   * partial DD contains an element of metadata-complete=false for this
   * particular test. This also tests that Config tools properly used
   * interospection to discover config properties that were not defined in the
   * DD.
   * 
   */
  public void testMixedModeConfigPropertyMCF() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "PMDManagedConnectionFactory factoryname=PMDManagedConnectionFactory";

    // Obtain connection, perform API verification
    try {
      ds2.setLogFlag(true);
      con2 = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Mixed mode config prop Failure.");
    }
  }

  /*
   * @testName: testNoDefaultVallAnnoElement
   *
   * @assertion_ids: Connector:SPEC:264; Connector:SPEC:307; Connector:SPEC:277;
   * Connector:SPEC:280; Connector:SPEC:268;
   *
   * @test_Strategy: This is testing that a RA with annotations and a partial DD
   * will take the DD' config property and set it within the (annotated)
   * ManagedConnectionFactory by checking that the setter for the config
   * property was called. (as described in connector 1.6 spec section 18.5)
   *
   * This particular test is focused on testing Connector:SPEC:277 which is
   * where no defaultValue element (in a ConfigProperty annotation is not used -
   * but instead, the config proeprty field is directly set and that set value
   * must be used since no defaultValue is specified.
   *
   * The partial DD contains an element of metadata-complete=false for this
   * particular test. This also tests that Config tools properly used
   * interospection to discover config properties that were not defined in the
   * DD.
   * 
   */
  public void testNoDefaultVallAnnoElement() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "PMDManagedConnectionFactory noDefaultValue=NO_DEFAULT_VAL";

    // Obtain connection, perform API verification
    try {
      ds2.setLogFlag(true);
      con2 = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Mixed mode config prop Failure.");
    }
  }

  /*
   * @testName: testMixedModeConfigPropertyRA
   *
   * @assertion_ids: Connector:SPEC:264; Connector:SPEC:279; Connector:SPEC:267;
   *
   * @test_Strategy: This is testing that a RA with annotations and a partial DD
   * will take the DD's config property and set it within the (annotated)
   * resource adapter JavaBean by checking that the setter for the config
   * property was called. (as described in connector 1.6 spec section 18.5)
   * 
   */
  public void testMixedModeConfigPropertyRA() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "setRAName called with raname=PartialMDResourceAdapter";

    // Obtain connection, perform API verification
    try {
      ds2.setLogFlag(true);
      con2 = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Mixed mode config prop used correctly.");
    }
  }

  /*
   * @testName: testMixedModeConfigPropertyOverride
   *
   * @assertion_ids: Connector:SPEC:271; Connector:SPEC:272;
   *
   * @test_Strategy: This is testing that a RA with annotations and a partial DD
   * that both have a ConfigProperty setting for the same named property (ie
   * overRide) will use the default value specified in the DD as that is
   * supposed to have precedence. (this is per connector 1.6 spec section
   * 18.3.2)
   * 
   */
  public void testMixedModeConfigPropertyOverride() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "PMDResourceAdapterImpl overRide=VAL_FROM_DD";

    // Obtain connection, perform API verification
    try {
      ds2.setLogFlag(true);
      con2 = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Mixed mode config prop used correctly.");
    }
  }

  /*
   * @testName: testMixedModeConfigPropertyNoOverride
   *
   * @assertion_ids: Connector:SPEC:273;
   *
   * @test_Strategy: This is testing that a RA with annotations and a partial DD
   * that have a configprop in the DD but NO matching prop in the anno - then
   * the DD valu must be used. (this is per connector 1.6 spec section 18.3.2)
   * 
   */
  public void testMixedModeConfigPropertyNoOverride() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "PMDResourceAdapterImpl overRide=VAL_FROM_DD";

    // Obtain connection, perform API verification
    try {
      ds2.setLogFlag(true);
      con2 = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Mixed mode config prop from DD used correctly.");
    }
  }

  /*
   * @testName: testDDOverridesAnno
   *
   * @assertion_ids: Connector:SPEC:312; Connector:SPEC:274;
   *
   * @test_Strategy: This is testing that If the JavaBean class specified in the
   * resourceadapter-class element is annotated with the Connector annotation,
   * the application server must use the information in the deployment
   * descriptor to override the values specified in the annotation.
   */
  public void testDDOverridesAnno() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "MAResourceAdapterImpl Started";

    // Obtain connection, perform API verification
    try {
      ds1.setLogFlag(true);
      con2 = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    TestUtil
        .logMsg("looking up following string in conenctor log: " + toCheck1);
    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testDDOverridesAnno passed.");
    } else {
      throw new Fault("Failure - DD did not override annotation.");
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    if (ds2 != null) {
      ds2.clearLog();
    }
    TestUtil.logMsg("Cleanup");
    try {
      TestUtil.logTrace("Closing connection in cleanup.");
      if (con2 != null) {
        con2.close();
      }
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
