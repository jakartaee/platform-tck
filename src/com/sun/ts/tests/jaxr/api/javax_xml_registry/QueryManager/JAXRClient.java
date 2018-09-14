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
 * @(#)JAXRClient.java	1.12 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry.QueryManager;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;
import com.sun.ts.tests.jaxr.common.JAXR_Util;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {
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
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      logMsg("in cleanup");
      if (conn != null) {
        logTrace("Cleanup is closing the connection");
        conn.close();
      }
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
   * @testName: queryManager_getRegistryService
   *
   * @assertion_ids: JAXR:JAVADOC:82
   *
   * @test_Strategy: getRegistryService
   * 
   *
   */
  public void queryManager_getRegistryService() throws Fault {
    String testName = "queryManager_getRegistryService";
    BusinessQueryManager bqm = null;
    try {
      bqm = rs.getBusinessQueryManager();
      RegistryService parentRs = bqm.getRegistryService();
      if (parentRs == null)
        throw new Fault(testName + " failed  - returned null parent!! ");
      if (!(parentRs instanceof RegistryService))
        throw new Fault(testName + " failed - RegistryService not returned!! ");

      debug.add(
          "Verify that getRegistryService returns the parent Registry service that created this \n");
      if (!(parentRs.equals(rs)))
        throw new Fault(testName + "Error: parent rs should be the same as rs");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

}
