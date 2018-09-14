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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.DeclarativeQueryManager;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {

  // ================================================
  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod; providerCapability;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAlias2; jaxrAliasPassword; jaxrAlias2Password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
      super.cleanUpRegistry(); // clean "TS Default Organization"
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      super.cleanup(); // close connection
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      // print out messages
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }
  }

  /*
   * @testName: declarativeQueryManager_createQueryTest
   *
   * @assertion_ids: JAXR:SPEC:6;JAXR:SPEC:78;
   *
   * @test_Strategy: Creates a Query object given a queryType Verify that level
   * 0 providers throw an UnsupportedCapabilityException.
   *
   */

  public void declarativeQueryManager_createQueryTest() throws Fault {
    String testName = "declarativeQueryManager_createQueryTest";
    int providerlevel = 0;
    int failcount = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      DeclarativeQueryManager dqm = rs.getDeclarativeQueryManager();
      // declarative syntaxes supported are SQL-92
      // If the target registry does not support
      // SQLUnsupportedCapabilityException
      // declarative syntaxes SQL-92
      debug.add(" create a SQL-92 query ");
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      // get the ClassificationScheme for PhoneType
      ClassificationScheme phoneTypeClassificationScheme = bqm
          .findClassificationSchemeByName(null, "PhoneType");
      // query for all classification schemes.
      String queryString = "SELECT * FROM ClassificationScheme";
      Query query = dqm.createQuery(Query.QUERY_TYPE_SQL, queryString);
      if (providerlevel == 0)
        throw new Fault(
            testName + "Did not throw UnsupportedCapabilityException");

      try {
        BulkResponse br = dqm.executeQuery(query);
        if (!(JAXR_Util.checkBulkResponse("executeQuery", br, debug))) {
          debug.add("Error:    executeQuery failed \n");
          throw new Fault(testName + " due to errors, test did not complete!");
        }
        Collection css = br.getCollection();
        boolean found = false;
        // the classifications returned should contain PhoneType, verify that.
        if (css.contains(phoneTypeClassificationScheme)) {
          debug.add("Found my PhoneType - good\n");
        } else {
          debug.add("Did not Find my PhoneType - not good\n");
          failcount = failcount + 1;
        }
      } catch (UnsupportedCapabilityException uc) {
        // if SQL not supported then a UnsupportedCapabilityException should be
        // thrown.
        debug.add(
            " Call to createQuery threw UnsupportedCapabilityException - ok\n");
      } catch (Exception e) {
        debug.add(" Call to createQuery threw an unexpected exception: "
            + e.getMessage());
        failcount = failcount + 1;
      }

      if (failcount > 0)
        throw new Fault(testName + " failed \n");

    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to createQuery threw UnsupportedCapabilityException as expected\n");
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method
} // end of class
