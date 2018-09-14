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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ExtrinsicObject;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import javax.activation.DataHandler;

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
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }
  }

  /*
   * @testName: extrinsicObjectTest
   *
   * @assertion_ids: JAXR:SPEC:8; JAXR:JAVADOC:682; JAXR:JAVADOC:684;
   * 
   * @test_Strategy: Verify that level 0 providers throw an
   * UnsupportedCapabilityException.
   */

  public void extrinsicObjectTest() throws Fault {
    String testName = "extrinsicObjectTest";
    int providerlevel = 0;
    String mimeType = "text/html";
    int failcount = 0;

    try {
      String externalURI = baseuri + "jaxrTestPage1.html";
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      java.net.URL myUrl = new URL(externalURI);

      DataHandler repositoryItem = new DataHandler(myUrl);
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("The capability level for this JAXR provider is: "
          + providerlevel + "\n");
      ExtrinsicObject eo = null;
      eo = (ExtrinsicObject) blm
          .createObject(LifeCycleManager.EXTRINSIC_OBJECT);
      if (providerlevel == 0) {
        throw new Fault(testName
            + " level 0 providers should throw an UnsupportedCapabilityException");
      }
      eo.setRepositoryItem(repositoryItem);
      DataHandler handler = eo.getRepositoryItem();
      if (handler.getContentType().equals(mimeType)) {
        debug.add("good -  repository item returned correct mimeType \n");
      } else {
        debug.add("not good - unexpected result from getRepositoryItem \n");
        failcount = failcount + 1;
      }

      eo.setMimeType(mimeType);
      if (eo.getMimeType().equals(mimeType)) {
        debug.add("good mime type returned  " + mimeType + " as expected\n");
      } else {
        debug.add("getMimeType did not return expected value, method returned: "
            + eo.getMimeType() + "\n");
        failcount = failcount + 1;
      }
      eo.setOpaque(true);
      if (eo.isOpaque())
        debug.add("good -  getOpaque returned true as expected \n");
      else {
        debug.add("not good - unexpected result from getOpaque \n");
        failcount = failcount + 1;
      }
      eo.setOpaque(false);
      if (!(eo.isOpaque()))
        debug.add("good -  getOpaque returned false as expected \n");
      else {
        debug.add("not good - unexpected result from getOpaque \n");
        failcount = failcount + 1;
      }
      eo.setOpaque(true);
      Key eoKey = eo.getKey();
      List l = new ArrayList();
      l.add(eo);
      blm.saveObjects(l);

      // get it back from the registry.
      ExtrinsicObject retEo = (ExtrinsicObject) bqm
          .getRegistryObject(eoKey.getId());
      DataHandler dh = retEo.getRepositoryItem();
      if (dh.getContentType().equals(mimeType)) {
        debug.add("good -  repository item returned correct mimeType \n");
      } else {
        debug.add("not good - unexpected result from getRepositoryItem \n");
        failcount = failcount + 1;
      }
      debug.add(" Now test eo returned from the registry. \n");
      if (retEo.isOpaque())
        debug.add("good -  getOpaque returned true as expected \n");
      else {
        debug.add("not good - unexpected result from getOpaque \n");
        failcount = failcount + 1;
      }
      if (retEo.getMimeType().equals(mimeType)) {
        debug.add("good mime type returned  " + mimeType + " as expected\n");
      } else {
        debug.add("getMimeType did not return expected value, method returned: "
            + retEo.getMimeType() + "\n");
        failcount = failcount + 1;
      }

      List eoKeys = new ArrayList();
      eoKeys.add(eoKey);
      blm.deleteObjects(eoKeys);

      if (failcount > 0)
        throw new Fault(testName + " failed \n");

    } catch (UnsupportedCapabilityException uce) {
      TestUtil.printStackTrace(uce);
      if (providerlevel == 0)
        debug.add("UnsupportedCapabilityException was thrown as expected!!\n");
      else
        throw new Fault(testName
            + " failed - level 1 providers should support createExtrinsicObject");
    } catch (Exception e) {
      if (providerlevel == 0) {
        TestUtil.logErr(
            "Expected level 0 provider to throw UnsupportedCapabilityException");
      }
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  }
}
