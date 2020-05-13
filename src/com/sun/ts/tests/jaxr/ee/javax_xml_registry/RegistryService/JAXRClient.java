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
 * @(#)JAXRClient.java	1.17 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.ee.javax_xml_registry.RegistryService;

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

import jakarta.xml.registry.*;
import jakarta.xml.registry.infomodel.*;
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
   * @testName: registryService_getBulkResponse
   *
   * @assertion_ids: JAXR:JAVADOC:65;
   *
   * @test_Strategy: Get a requestId by generating a BulkResponse. on a
   * saveOrganization with connection set to asynch. invoke getBulkResponse with
   * the requestId. Verify that a bulk response is returned from
   * rs.getBulkResponse Verify that the new BulkResponse returned from registry
   * server matches the requestid of the original BulkResponse.
   *
   */
  public void registryService_getBulkResponse() throws Fault {
    String testName = "registryService_getBulkResponse";
    Collection orgKeys = null;
    try {
      conn.setSynchronous(false);
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      orgKeys = br.getCollection();
      // get the requestId
      String requestId = br.getRequestId();
      br = rs.getBulkResponse(requestId);
      if (!(br instanceof BulkResponse))
        throw new Fault(testName + " failed - null bulkresponse!");
      if (!(br.getRequestId().equals(requestId)))
        throw new Fault(testName + " failed - requestid did not match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }

  }

  /*
   * @testName: registryService_getBulkResponseSubsequentCall
   *
   * @assertion_ids: JAXR:JAVADOC:66;
   *
   * @test_Strategy: Get a requestId by generating a BulkResponse. on a
   * saveOrganization with connection set to asynch. invoke getBulkResponse with
   * the requestId. Do it a second time. Verify that the subsequent call throws
   * an InvalidRequestException.
   *
   */
  public void registryService_getBulkResponseSubsequentCall() throws Fault {
    String testName = "registryService_getBulkResponseSubsequentCall";
    boolean pass = false;
    Collection orgKeys = null;
    try {
      conn.setSynchronous(false);
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      orgKeys = br.getCollection();
      // get the requestId
      conn.setSynchronous(true);
      String requestId = br.getRequestId();
      br = rs.getBulkResponse(requestId);
      try {
        br = rs.getBulkResponse(requestId);
      } catch (InvalidRequestException ir) {
        TestUtil.printStackTrace(ir);
        debug.add(
            "Second request to get BulkResponse threw InvalidRequestException as expected\n");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
    if (!pass)
      throw new Fault(testName
          + " failed - did not throw InvalidRequestException as expected");

  }

}
