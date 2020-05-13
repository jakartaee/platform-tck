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
 * @(#)JAXRClient.java	1.2 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.internal.implementation.sjsas.jaxr.ee.javax_xml_registry.RegistryServicects;

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
   * registryURL; queryManagerURL; authenticationMethod; authenticationMethod;
   * providerCapability; jaxrConnectionFactoryLookup;
   * jaxrSecurityCredentialType; jaxrJNDIResource; jaxrAlias; jaxrAlias2;
   * jaxrAliasPassword; jaxrAlias2Password;
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
      super.cleanUpRegistry();
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
   * @testName: registryService_makeRegistrySpecificRequest
   *
   * @assertion_ids: JAXR:JAVADOC:70;
   *
   * @test_Strategy: Publish an organization. Find it with an XML request for
   * find_business. Verify that the business is returned in the response
   *
   */
  public void registryService_makeRegistrySpecificRequest() throws Fault {
    String testName = "registryService_makeRegistrySpecificRequest";

    String request = "<find_business generic=\"2.0\" xmlns=\"urn:uddi-org:api_v2\">"
        + "<name xml:lang=\"en\">" + JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME
        + "</name></find_business>";
    Collection keys = null;

    try {
      // publish
      Organization org = blm.createOrganization(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug)))
        throw new Fault(
            testName + "Error saving organizaiton,  test did not complete\n");
      keys = br.getCollection();
      debug.add("This is the request sent:\n        " + request + "\n");
      String response = rs.makeRegistrySpecificRequest(request);
      if (response == null)
        throw new Fault(testName
            + " failed - makeRegistrySpecificRequest returned a null response ");
      debug.add("the response was: " + response + "\n");
      if (response.indexOf(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME) == -1)
        throw new Fault(testName + " failed - did not find business");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      debug.add("cleanup registry - remove test organization\n");
      try {
        blm.deleteOrganizations(keys);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Warning: could not remove the organization\n");
      }
    }

  }

}
