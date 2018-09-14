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
 * @(#)JAXRClient.java	1.3 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.12 02/04/25
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry.CapabilityProfile;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {
  int level = 0;

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
      level = Integer.parseInt(p.getProperty("providerCapability"));
      super.setup(args, p);

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
      TestUtil.logTrace(debug.toString());
      debug.clear();
    }

  }

  /*
   * @testName: CapabilityProfile_getVersionTest
   *
   * @assertion_ids: JAXR:JAVADOC:248;JAXR:SPEC:3;
   *
   * @test_Strategy: Call getVersion. Verify it returns the correct version
   * number.
   *
   */
  public void CapabilityProfile_getVersionTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "CapabilityProfile_getVersionTest";
    String jaxrSpecVersion = "1.0";
    try {
      // get the capability profile
      CapabilityProfile cp = rs.getCapabilityProfile();
      debug.add("Expecting version " + jaxrSpecVersion + "\n");
      debug.add("Specification version returned" + cp.getVersion());
      String version = cp.getVersion();
      if (version.indexOf(jaxrSpecVersion) == -1) {
        throw new Fault(
            testName + " Error: version " + jaxrSpecVersion + " not found ");
      } else
        debug.add("Test passed\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: CapabilityProfile_getCapabilityLevelTest
   *
   * @assertion_ids: JAXR:JAVADOC:250;JAXR:SPEC:1;
   *
   * @test_Strategy: Call getCapabilityLevel. THe user must specify the jaxr
   * provider capability level in the ts.jte file. Verify that the level
   * returned matches the level specified in the ts.jte file.
   *
   */
  public void CapabilityProfile_getCapabilityLevelTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "CapabilityProfile_getCapabilityLevelTest";
    int cpLevel;
    try {
      CapabilityProfile cp = rs.getCapabilityProfile();
      cpLevel = cp.getCapabilityLevel();
      debug.add("The expected capability level is " + level + "\n");
      debug.add("The capability level returned from getCapabilityLevel is "
          + cpLevel + "\n");
      if (level != cpLevel) {
        throw new Fault(testName + " failed");
      } else
        debug.add("Test passed\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

} // end of file
