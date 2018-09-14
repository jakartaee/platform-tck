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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.URIValidator;

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
   * testName: uriValidator_get_setValidateURITest
   *
   * @assertion_ids: JAXR:JAVADOC:346;JAXR:JAVADOC:348;
   *
   * @assertion: setValidateURI - sets whether to do URI validation for this
   * object. Default is true.
   *
   * getValidateURI - Gets whether to do URI validation for this object.
   *
   * @test_Strategy: Verify that the default for validation is set to true. Set
   * it to false. Verify getValidateURI returns false.
   *
   */
  public void uriValidator_get_setValidateURITest() throws Fault {
    String testName = "uriValidator_get_setValidateURITest";
    boolean pass = true;
    String uriDescription = "pass an invalid URI";
    String invalidURI = "this is not a valid uri";
    try {
      // create an ExternalLink instance
      ExternalLink el = (ExternalLink) blm
          .createObject(LifeCycleManager.EXTERNAL_LINK);
      // Default test....
      if (el.getValidateURI() != true) {
        debug.add("Error: Default not correctly set to true\n");
        pass = false;

      } else {
        debug.add("Default passes! correctly set to true\n");
      }
      // Now set it to false
      el.setValidateURI(false);
      // and verify with a get call
      if (el.getValidateURI() != false) {
        debug.add("Error: getValidateURI should have returned false\n");
        pass = false;
      } else {
        debug.add("setValidateURI passes! correctly set to false\n");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test class
