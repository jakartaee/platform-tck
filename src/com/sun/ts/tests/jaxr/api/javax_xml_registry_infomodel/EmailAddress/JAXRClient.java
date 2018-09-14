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
 * @(#)JAXRClient.java	1.16 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.EmailAddress;

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
      super.cleanup();

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
   * @testName: emailAddress_getAddressTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:718;
   *
   * @assertion: getAddress - Returns the email address for this object.
   *
   * JAXR javadoc
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_getAddressTest() throws Fault {
    String testName = "emailAddress_getAddressTest";
    String setAddress = "1 Network Drive";
    try {
      // create an EmailAddress
      debug.add("Create email address: " + setAddress + "\n");
      EmailAddress ea = (EmailAddress) blm.createEmailAddress(setAddress);
      String getAddress = ea.getAddress();
      debug.add("Method returned email address: " + getAddress + "\n");
      if (!(getAddress.equals(setAddress)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_getAddressTest

  /*
   * @testName: emailAddress_getAddressNullTest
   *
   * @assertion: getAddress - Returns the email address for this object. Default
   * is a NULL String.
   *
   * JAXR javadoc
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_getAddressNullTest() throws Fault {
    String testName = "emailAddress_getAddressNullTest";
    String nullString = null;
    try {
      // create an EmailAddress
      debug.add("Create email address:  \n");
      EmailAddress ea = (EmailAddress) blm
          .createObject(LifeCycleManager.EMAIL_ADDRESS);
      String getAddress = ea.getAddress();
      if (getAddress == null) {
        debug.add("Method returned null default as expected \n");
      } else
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_getAddressNullTest

  /*
   * @testName: emailAddress_setAddressTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:720;
   *
   * @assertion: setAddress - Sets the email address for this object.
   *
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_setAddressTest() throws Fault {
    String testName = "emailAddress_setAddressTest";
    String setAddress = "123 Boston Road";
    try {
      // create an EmailAddress
      debug.add("Set email address: " + setAddress + "\n");
      EmailAddress ea = (EmailAddress) blm
          .createObject(LifeCycleManager.EMAIL_ADDRESS);
      ea.setAddress(setAddress);
      String getAddress = ea.getAddress();
      debug.add("Method returned email address: " + getAddress + "\n");
      if (!(getAddress.equals(setAddress)))
        throw new Fault(testName + " failed ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_setAddressTest

  /*
   * @testName: emailAddress_getTypeTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:722;
   *
   * @assertion: getType - The type for this object. The default is a NULL
   * String.
   *
   * JAXR javadoc
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_getTypeTest() throws Fault {
    String testName = "emailAddress_getTypeTest";
    String setAddress = "1 Network Drive";
    String setType = "email type";
    try {
      // create an EmailAddress
      debug.add("Create email type: " + setType + "\n");
      EmailAddress ea = (EmailAddress) blm.createEmailAddress(setAddress,
          setType);
      String getType = ea.getType();
      debug.add("Method returned email type: " + getType + "\n");
      if (!(getType.equals(setType)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_getTypeTest

  /*
   * @testName: emailAddress_getTypeNullTest
   *
   * @assertion: getType - The type for this object. The default is a NULL
   * String.
   *
   * JAXR javadoc
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_getTypeNullTest() throws Fault {
    String testName = "emailAddress_getTypeNullTest";
    String setAddress = "1 Network Drive";
    String nullString = null;
    try {
      // create an EmailAddress
      EmailAddress ea = (EmailAddress) blm
          .createObject(LifeCycleManager.EMAIL_ADDRESS);
      String getType = ea.getType();
      if (getType == null) {
        debug.add("Method returned null default as expected \n");
      } else
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_getTypeNullTest

  /*
   * @testName: emailAddress_setType
   *
   *
   * @assertion_ids: JAXR:JAVADOC:724;
   *
   * @assertion: setType - Sets the type for this object.
   *
   * JAXR javadoc
   *
   * @test_Strategy:
   *
   */
  public void emailAddress_setType() throws Fault {
    String testName = "emailAddress_setType";
    String setAddress = "1 Network Drive";
    String setType = "email type";
    try {
      // create an EmailAddress
      EmailAddress ea = (EmailAddress) blm
          .createObject(LifeCycleManager.EMAIL_ADDRESS);
      ea.setType(setType);
      String getType = ea.getType();
      debug.add("Method returned email type: " + getType + "\n");
      if (!(getType.equals(setType)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of emailAddress_setType

} // end of JAXRClient
