/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)JAXRClient.java	1.22 03/05/16
 */

/*
 * @(#)JAXRClient.java	1.12 02/04/25
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry.Connection_asynch;

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
import javax.security.auth.x500.X500PrivateCredential;

public class JAXRClient extends JAXRCommonClient {
  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; providerCapability; authenticationMethod;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAlias2; jaxrAliasPassword; jaxrAlias2Password;
   */
  public void setup(String[] args, Properties p) throws Fault {

    try {
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
    }

  }

  /*
   * @testName: ConnectionsetSynchronousTest2
   *
   * 
   * @assertion_ids: JAXR:SPEC:157;
   *
   * @assertion: The JAXR client uses the setSynchronous method on a Connection
   * to dynamically alter whether it receives responses and exceptions from the
   * JAXR provider synchronously or not. The JAXR provider must use this
   * communication preference when processing requests on behalf of that client.
   *
   * @test_Strategy: Create a connection object. Use setSynchronous() to to set
   * synchronous to false. Verify that isSynchronous returns false.
   *
   */
  public void ConnectionsetSynchronousTest2() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionsetSynchronousTest2";
    boolean pass = true;
    try {

      conn.setSynchronous(false);
      if (conn.isSynchronous()) {
        logMsg(
            "Error synchronous was set to false, its: " + conn.isSynchronous());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed", e);
    }

    if (!pass)
      throw new Fault(testName + " failed");
  }

  // end of test class
}
