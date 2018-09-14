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
 * @(#)JAXRClient.java  1.21 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.RegistryEntry;

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

public class JAXRClient extends JAXRCommonClient {
  BusinessQueryManager bqm = null;

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
   * jaxrWebContext; webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
      super.cleanUpRegistry(); //
      debug.clear();
      bqm = rs.getBusinessQueryManager();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      super.cleanup();
      // super.cleanUpRegistry();
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
   * @testName: registryEntry_setgetExpiration
   *
   * @assertion_ids: JAXR:JAVADOC:538;JAXR:JAVADOC:540;JAXR:SPEC:51;
   *
   * @test_Strategy: Verify that level 0 providers throw an
   * UnsupportedCapabilityException.
   *
   */
  public void registryEntry_setgetExpiration() throws Fault {
    String testName = "registryEntry_setgetExpiration";
    String serviceName = "testService";
    Date setDate = null;
    int providerlevel = 0;
    String name = "TestOrganizationName";
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      setDate = new Date();
      Organization org = blm
          .createOrganization(blm.createInternationalString(name));
      Service service = blm.createService(serviceName);
      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      service.setExpiration(setDate);
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      Date retDate = service.getExpiration();
      if (retDate.compareTo(setDate) == 0) {
        TestUtil.logMsg("Good: expected expiration date was returned");

      } else
        throw new Fault(testName + " failed to return correct expiration date");
    } catch (UnsupportedCapabilityException uce) {
      if (providerlevel != 0) {
        TestUtil.logErr("Cleanup error: " + uce.toString());
        TestUtil.printStackTrace(uce);
        throw new Fault(
            testName + " failed - unexpected UnsupportedCapabilityException");
      }
    } catch (Exception e) {
      if (providerlevel == 0) {
        TestUtil.logErr(
            "Expected level 0 provider to throw UnsupportedCapabilityException");
      }
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test

  /*
   * @testName: registryEntry_setgetStability
   *
   * @assertion_ids: JAXR:JAVADOC:534;JAXR:JAVADOC:536;JAXR:SPEC:52;
   *
   * @test_Strategy: Verify that level 0 providers throw an
   * UnsupportedCapabilityException.
   *
   */
  public void registryEntry_setgetStability() throws Fault {
    String testName = "registryEntry_setgetStability";
    String serviceName = "testService";
    int providerlevel = 0;
    String name = "TestOrganizationName";
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      Organization org = blm
          .createOrganization(blm.createInternationalString(name));
      Service service = blm.createService(serviceName);
      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      service.setStability(RegistryEntry.STABILITY_STATIC);
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      int retStability = service.getStability();
      if (retStability == RegistryEntry.STABILITY_STATIC) {
        TestUtil.logMsg("Good: expected stability was returned");

      } else
        throw new Fault(testName + " failed to return correct stability");
    } catch (UnsupportedCapabilityException uce) {
      if (providerlevel != 0) {
        TestUtil.logErr("Cleanup error: " + uce.toString());
        TestUtil.printStackTrace(uce);
        throw new Fault(
            testName + " failed - unexpected UnsupportedCapabilityException");
      }
    } catch (Exception e) {
      if (providerlevel == 0) {
        TestUtil.logErr(
            "Expected level 0 provider to throw UnsupportedCapabilityException");
      }
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test

  /*
   * @testName: registryEntry_getAuditTrailTest
   *
   * @assertion_ids: JAXR:SPEC:53;JAXR:SPEC:4;
   *
   * @test_Strategy: getAuditTrail - Returns the complete audit trail of all
   * requests that effected a state change in this object as an ordered
   * Collection of AuditableEvent objects. Verify that level 0 providers throw
   * an UnsupportedCapabilityException.
   *
   */
  public void registryEntry_getAuditTrailTest() throws Fault {
    String testName = "registryEntry_getAuditTrailTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int providerlevel = 0;
    try {

      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "Level 0 Providers must throw an UnsupportedCapabilityException for getAuditTrail\n");
      debug.add(
          "This provider report a Capability Level of " + providerlevel + "\n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));

      Collection at = org.getAuditTrail();
      System.out.println("at size is: " + at.size());

      // getAuditTrail returns a Collection of AuditableEvent instances. The
      // Collection may be empty but not null.
      Iterator iter = at.iterator();
      while (iter.hasNext()) {
        AuditableEvent ae = (AuditableEvent) iter.next();
        System.out.println("ae tyep " + ae.getEventType());
      }
    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getAuditTrail threw UnsupportedCapabilityException as expected\n");
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

}
