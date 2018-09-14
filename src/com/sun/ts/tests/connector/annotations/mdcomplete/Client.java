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

package com.sun.ts.tests.connector.annotations.mdcomplete;

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
public class Client extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con2 = null;

  private String completeMDJndiName = null;

  private String uname = null;

  private String password = null;

  private TSDataSource ds2 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-mdcomplete; rauser1; rapassword1;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    completeMDJndiName = p.getProperty("whitebox-mdcomplete");
    System.out.println("completeMDJndiName = : " + completeMDJndiName);

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    logMsg("Using: " + completeMDJndiName);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      nctx = new TSNamingContext();

      ds2 = (TSDataSource) nctx.lookup(completeMDJndiName);
      if (ds2 == null) {
        TestUtil.logMsg("ds2 lookup failed and is null");
      } else {
        TestUtil.logMsg("ds2 lookup is not null");
      }

      TestUtil.logMsg("ds2 JNDI lookup: " + ds2);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testMDCompleteConfigProp
   *
   * @assertion_ids: Connector:SPEC:266; Connector:JAVADOC:140;
   * Connector:JAVADOC:142; Connector:JAVADOC:144; Connector:JAVADOC:139;
   * Connector:JAVADOC:143;
   *
   * @test_Strategy: This is testing that a RA with annotations and a
   * metadata-complete DD will take the DD's config property and set it within
   * the ManagedConnectionFactory. The DD takes precedence and this is confirmed
   * by checking that the setter for the config property was called. The DD
   * (which has metadata-complete = true) should have all its attrs used and any
   * duplicate annotation attrs should NOT get used. This is another variant of
   * testing assertion 266.
   */
  public void testMDCompleteConfigProp() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    String toCheck1 = "setRAName called with raname=MDCompleteRA";
    String toCheck2 = "setRAName called with raname=BAD_RAName_value";

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
        // we should find this
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        // we should NOT find this!
        b2 = true;
      }
    }

    if (b1 && !b2) {
      TestUtil.logMsg("testMDCompleteConfigProp called correctly");
    } else {
      throw new Fault("Metadata-complete config prop Failure.");
    }
  }

  /*
   * @testName: testMDCompleteMCFAnno
   *
   * @assertion_ids: Connector:SPEC:266;
   *
   * @test_Strategy: This is testing that an RA with that has ra.xml with
   * metadata-complete DD will use the DD's specified MCF and will NOT use an
   * annotated MCF that is bundled in the same jar. Thus the metadata-complete
   * DD gets its MCF used and the other is ignored. This is another variant of
   * testing assertion 266.
   */
  public void testMDCompleteMCFAnno() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    String toCheck1 = "MDCompleteMCF constructor";
    String toCheck2 = "MDAnnotatedMCF constructor";

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
        // we should find this
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        // we should NOT find this!
        b2 = true;
      }
    }

    if (b1 && !b2) {
      TestUtil.logMsg("testMDCompleteMCFAnno called correctly");
    } else {
      throw new Fault(
          "Annotated MCF erroneously called despite metadata-complete=true.");
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
