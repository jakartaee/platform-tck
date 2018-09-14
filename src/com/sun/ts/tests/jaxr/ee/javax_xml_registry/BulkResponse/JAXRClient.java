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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.BulkResponse;

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
  Locale tsLocale = new Locale("en", "US");

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
   * @testName: bulkResponse_isPartialResponse
   *
   * @assertion_ids: JAXR:JAVADOC:306
   *
   * @test_Strategy: Create and save an Organization. Verify that isPartial is
   * false since result set is small.
   *
   */
  public void bulkResponse_isPartialResponse() throws Fault {
    String testName = "bulkResponse_isPartialResponse";
    Collection orgKeys = null;
    try {
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      orgKeys = br.getCollection();
      // don't expect a partial response since result set is small
      debug.add("Don't expect a partial response since result set is small \n");
      debug.add(
          "BulkResponse.isPartial returns " + br.isPartialResponse() + "\n");
      if (!(br.isPartialResponse() == false))
        throw new Fault(
            testName + " failed - did not expect a partial response");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);

    }
  }

  /*
   * @testName: bulkResponse_getCollection
   *
   * @assertion_ids: JAXR:JAVADOC:302;
   *
   * @test_Strategy: Create and save an organizaiton. Verify that getCollection
   * returns a Collection containing the key for the saved organization.
   * 
   *
   */
  public void bulkResponse_getCollection() throws Fault {
    String testName = "bulkResponse_getCollection";
    Key savekey = null;
    BusinessQueryManager bqm = null;
    Collection orgKeys = null;
    boolean pass = false;
    try {
      bqm = rs.getBusinessQueryManager();
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      // use returned key to retrieve object
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      Collection myOrgs = br.getCollection();
      Iterator iter = myOrgs.iterator();
      while (iter.hasNext()) {
        Organization o = (Organization) iter.next();
        if (o.getName().getValue(tsLocale)
            .equals(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME)) {
          debug.add("Success:  used key to retrieve my organization \n");
          pass = true;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);

    }
    if (!pass)
      throw new Fault(testName + " failed  ");

  }

  /*
   * @testName: bulkResponse_getExceptions
   *
   * @assertion_ids: JAXR:JAVADOC:304
   *
   * @test_Strategy: Create and save an Organization. Verify that if no
   * exceptions are encountered null is returned from br.getExceptions()
   * 
   *
   */
  public void bulkResponse_getExceptions() throws Fault {
    String testName = "bulkResponse_getExceptions";
    Collection orgKeys = null;
    try {
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      orgKeys = br.getCollection();
      Collection exceptions = br.getExceptions();
      debug.add(
          "Returns null if result is available or exceptions in case of partial commit \n");

      if (exceptions == null) {
        debug.add(
            "No exceptions  - null collection returned from br.getExceptions -  OK \n");
      } else if (exceptions.size() > 0) {
        debug.add("Exceptions returned count = " + exceptions.size() + "\n");
      } else
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);

    }

  }

}
