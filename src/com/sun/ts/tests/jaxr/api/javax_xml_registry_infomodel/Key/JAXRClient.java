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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Key;

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
   * @testName: key_getIdNullTest
   *
   * @assertion_ids: JAXR:JAVADOC:748;
   *
   * @assertion: getId - Returns the unique Id of this key. Default is a NULL
   * String.
   *
   * @test_Strategy: Create a Key. Call getId.
   */

  public void key_getIdNullTest() throws Fault {
    String testName = "key_getIdNullTest";
    String keyId = "urn:uuid:a2345678-1234-1234-123456789012";
    boolean pass = false;

    try {
      Key key = (Key) blm.createObject(LifeCycleManager.KEY);
      key.getId();
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: key_setGetIdTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:750;
   *
   * @assertion: getId - Returns the unique Id of this key. Default is a NULL
   * String. setId - Sets the unique id associated with this key.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Create a key. Call getKey().getId() on the organization.
   * Verify it returns the set key.
   *
   */
  public void key_setGetIdTest() throws Fault {
    String testName = "key_setGetIdTest";
    boolean pass = true;
    String test = "urn:uuid:a2345678-1234-1234-123456789012";
    try {
      Key key = (Key) blm.createObject(LifeCycleManager.KEY);
      key.setId(test);
      if (!(key.getId().equals(test)))
        throw new Fault(testName + "Error: wrong id returned");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test class
