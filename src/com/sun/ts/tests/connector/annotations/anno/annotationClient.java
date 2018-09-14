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

package com.sun.ts.tests.connector.annotations.anno;

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
 * These are tests used to validate functionality associated with connector annotations.
 */
public class annotationClient extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con1 = null;

  private TSConnection con2 = null;

  private String partialMDJndiName = null;

  private String annoJndiName = null;

  private String uname = null;

  private String password = null;

  private TSDataSource ds1 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    annotationClient theTests = new annotationClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-anno_no_md; whitebox-mixedmode; rauser1;
   * rapassword1;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    annoJndiName = p.getProperty("whitebox-anno_no_md");
    System.out.println("annoJndiName = : " + annoJndiName);

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    logMsg("Using: " + annoJndiName);

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

      ds1 = (TSDataSource) nctx.lookup(annoJndiName);
      if (ds1 == null) {
        TestUtil.logMsg("ds1 lookup failed and is null");
      } else {
        TestUtil.logMsg("ds1 lookup is not null");
      }

      TestUtil.logMsg("ds1 JNDI lookup: " + ds1);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testConnectorAnnotation
   *
   * @assertion_ids: Connector:SPEC:268; Connector:JAVADOC:306;
   * Connector:SPEC:267; Connector:JAVADOC:259; Connector:JAVADOC:123;
   * Connector:JAVADOC:123; Connector:JAVADOC:123; Connector:JAVADOC:149;
   * Connector:JAVADOC:165; Connector:JAVADOC:166; Connector:JAVADOC:167;
   * Connector:JAVADOC:168; Connector:JAVADOC:168; Connector:JAVADOC:170;
   * Connector:JAVADOC:171; Connector:JAVADOC:172; Connector:JAVADOC:173;
   * Connector:JAVADOC:174; Connector:JAVADOC:176; Connector:JAVADOC:177;
   * Connector:JAVADOC:178; Connector:JAVADOC:287; Connector:JAVADOC:288;
   * Connector:JAVADOC:295; Connector:JAVADOC:296; Connector:JAVADOC:124;
   * Connector:JAVADOC:126; Connector:JAVADOC:125;
   *
   * @test_Strategy: This is testing that the @Connector annotation is processed
   * when there is no DD (eg ra.xml). (no DD also means there is no DD value for
   * the DD element of "metadata-complete".) This also uses the authMechanism
   * anno.
   */
  public void testConnectorAnnotation() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    try {
      ds1.setLogFlag(true);
      con1 = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // our annotated RA should have called start when it was bootstrapped
    String toCheck1 = "AnnotatedResourceAdapterImpl.start called";

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
      throw new Fault("@Connection annotation not processed correctly.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con1);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con1);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
  }

  /*
   * @testName: testSetterMethodConfigPropAnno
   *
   * @assertion_ids: Connector:SPEC:278; Connector:JAVADOC:306;
   *
   * @test_Strategy: This is testing that the @ConfigProperty annotation is
   * processed.
   * 
   */
  public void testSetterMethodConfigPropAnno() throws Fault {

    Vector log = null;
    boolean b1 = false;
    String toCheck1 = "AnnotatedResourceAdapterImpl.setSetterMethodVal=NONDEFAULT";

    // Obtain connection, perform API verification
    try {
      ds1.setLogFlag(true);
      con1 = ds1.getConnection();
      log = ds1.getStateLog();
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
      throw new Fault(
          "@ConfigProperty on setter method not processed correctly.");
    }

  }

  /*
   * @testName: testConfigPropertyAnnotation
   *
   * @assertion_ids: Connector:SPEC:268; Connector:SPEC:279; Connector:SPEC:277;
   * Connector:JAVADOC:306; Connector:SPEC:280; Connector:JAVADOC:234;
   *
   * @test_Strategy: This is testing that the @ConfigProperty annotation is
   * processed for both the @Conenction and @ConenctionDefinition annos. This
   * also tests that Config tools properly used interospection to discover
   * config properties that were not defined in the DD.
   */
  public void testConfigPropertyAnnotation() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    String toCheck1 = "setRAName called with raname=AnnotatedResourceAdapterImpl";
    String toCheck2 = "AnnoManagedConnectionFactory factoryName=AnnoManagedConnectionFactory";

    // Obtain connection, perform API verification
    try {
      ds1.setLogFlag(true);
      con1 = ds1.getConnection();
      log = ds1.getStateLog();
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
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("@ConfigProperty annotation not processed correctly.");
    }

  }

  /*
   * @testName: testRAAccessibility
   *
   * @assertion_ids: Connector:SPEC:301;
   *
   * @test_Strategy: This is testing that the resource-ref'd annotation jar file
   * classes of the standalone rar are made available to this application. In
   * order to test this, we will attempt to submit a logmessage using the RA's
   * logging mechanism; then we will retrieve the log and ensure that (a) the
   * client side log succeeded and (b) we logged it to the same log file that
   * our RA is using.
   * 
   */
  public void testRAAccessibility() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    String toCheck1 = "testRAAccessibility:  accessibility test";
    String toCheck2 = "AnnotatedResourceAdapterImpl.start called";

    // lets directly test assertion 301
    ConnectorStatus.getConnectorStatus().logState(toCheck1);

    // Obtain connection, perform API verification
    try {
      ds1.setLogFlag(true);
      con1 = ds1.getConnection();
      log = ds1.getStateLog();
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
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("standalone RA classes are available to app.");
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    if (ds1 != null) {
      ds1.clearLog();
    }
    TestUtil.logMsg("Cleanup");
    try {
      TestUtil.logTrace("Closing connection in cleanup.");
      if (con1 != null) {
        con1.close();
      }
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
