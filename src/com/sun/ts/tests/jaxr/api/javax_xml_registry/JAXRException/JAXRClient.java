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

package com.sun.ts.tests.jaxr.api.javax_xml_registry.JAXRException;

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
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {

  // ================================================
  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

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

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      super.cleanup(); // close connection
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
   * @testName: jaxrException_ConstructorTest1
   *
   *
   * @assertion_ids: JAXR:JAVADOC:21;
   *
   * @assertion: JAXRException Constructs a JAXRException object with no reason
   * or embedded Throwable
   *
   * @test_Strategy: Create and verify JAXRException
   *
   */
  public void jaxrException_ConstructorTest1() throws Fault {
    String testName = "jaxrException_ConstructorTest1()";
    try {
      debug.add("Create and verify a JAXRException instance\n");
      JAXRException e = new JAXRException();
      if (!(e instanceof JAXRException))
        throw new Fault(testName + "failed to create JAXRException instance");
      else
        debug.add("JAXRException created successfully\n");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: jaxrException_ConstructorTest2
   *
   *
   * @assertion_ids: JAXR:JAVADOC:22;
   *
   * @assertion: JAXRException(reason) - Constructs a JAXRException object with
   * the given String as the reason for the exception being thrown
   *
   * @test_Strategy: Create and verify JAXRException, verify reason.
   *
   */
  public void jaxrException_ConstructorTest2() throws Fault {
    String testName = "jaxrException_ConstructorTest2";
    String reason = "test reason";
    boolean pass = true;
    try {
      debug.add("Create and verify a JAXRException(reason)\n");
      JAXRException e = new JAXRException(reason);
      if (!(e instanceof JAXRException)) {
        pass = false;
        debug.add(testName + " failed to create JAXRException instance\n");
      } else
        debug.add("JAXRException created successfully\n");
      if (!(e.getMessage().equals(reason))) {
        pass = false;
        debug.add(testName + " failed to return correct reason\n");
      } else
        debug.add("JAXRException returned reason successfully\n");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");
  } // end of test method

  /*
   * @testName: jaxrException_ConstructorTest3
   *
   *
   * @assertion_ids: JAXR:JAVADOC:23;
   *
   * @assertion: JAXRException(reason, cause) - Constructs a JAXRException
   * object with the given String as the reason for the exception being thrown
   * and the given Throwable object as an embedded Throwable.
   *
   * @test_Strategy: Create and verify JAXRException, verify reason and cause.
   *
   */
  public void jaxrException_ConstructorTest3() throws Fault {
    String testName = "jaxrException_ConstructorTest3()";
    String reason = "test reason";
    String cause = "test cause";
    boolean pass = true;
    try {
      Throwable tCause = new Throwable(cause);
      debug.add("Create and verify a JAXRException(reason,cause)\n");
      JAXRException e = new JAXRException(reason, tCause);
      if (!(e instanceof JAXRException)) {
        pass = false;
        debug.add(testName + " failed to create  JAXRException instance\n");
      } else
        debug.add("JAXRException created successfully\n");
      if (!(e.getMessage().equals(reason))) {
        pass = false;
        debug.add(testName + " failed to return correct reason\n");
      } else
        debug.add("  JAXRException returned reason successfully\n");
      Throwable t = e.getCause();
      debug.add("Cause is: " + t.getMessage() + "\n");
      if (!(t.getMessage().equals(cause))) {
        pass = false;
        debug.add(testName + " failed to return valid Throwable\n");
      }
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + " failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * @testName: jaxrException_ConstructorTest4
   *
   *
   * @assertion_ids: JAXR:JAVADOC:24;
   *
   * @assertion: JAXRException(cause) - Constructs a JAXRException object
   * initialized with the given Exception object.
   *
   * @test_Strategy: Create and verify JAXRException, verify cause.
   *
   */
  public void jaxrException_ConstructorTest4() throws Fault {
    String testName = "jaxrException_ConstructorTest4";
    String cause = "test cause";
    boolean pass = true;
    try {
      Throwable tCause = new Throwable(cause);
      debug.add("Create and verify a JAXRException(cause)\n");
      JAXRException e = new JAXRException(tCause);

      if (!(e instanceof JAXRException)) {
        pass = false;
        debug.add(testName + "failed to create JAXRException instance\n");
      } else
        debug.add(" JAXRException created successfully\n");

      Throwable t = e.getCause();
      debug.add("Cause is: " + t.getMessage() + "\n");
      if (!(t.getMessage().equals(cause))) {
        pass = false;
        debug.add(testName + " failed to return valid Throwable\n");
      }
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * @testName: jaxrException_getCauseTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:26;
   *
   * @assertion: getCause - Returns the Throwable embedded in this JAXRException
   * if there is one. Otherwise, this method returns null.
   *
   * @test_Strategy: Create a jaxrException with no throwable. Verify that
   * getCause returns null.
   *
   */
  public void jaxrException_getCauseTest() throws Fault {
    String testName = "jaxrException_getCauseTest";
    boolean pass = true;
    try {
      JAXRException e = new JAXRException();
      if (!(e instanceof JAXRException)) {
        pass = false;
        debug.add(testName + "failed to create JAXRException instance\n");
      } else
        debug.add(" JAXRException created successfully\n");
      Throwable t = e.getCause();
      if (t != null) {
        debug.add("Error: cause should have been null \n");
        pass = false;
      } else
        debug.add("getCause returned null as expected\n");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * @testName: jaxrException_getMessageTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:25;
   *
   * @assertion: getMessage - Returns the detail message for this JAXRException
   * object.
   *
   * @test_Strategy: Create a jaxrException with a warning message. Verify that
   * it is returned wit getMessage
   *
   */
  public void jaxrException_getMessageTest() throws Fault {
    String testName = "jaxrException_getMessageTest";
    boolean pass = true;
    String warningMessage = " test getMessage returns JAXRException reason";
    try {
      JAXRException e = new JAXRException(warningMessage);
      if (!(e.getMessage().equals(warningMessage))) {
        pass = false;
        debug
            .add("Error: JAXRException reason not returned with getMessage!\n");
      } else
        debug.add("JAXRException reason returned successfully\n");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * @testName: jaxrException_getMessageThrowableTest
   *
   * @assertion: getMessage - Returns the detail message for this JAXRException
   * object. If there is an embedded Throwable, and if the JAXRException object
   * has no detail message of its own, this method will return the detail
   * message from the embedded Throwable.
   *
   * @test_Strategy: Create a jaxrException without a warning message. Verify
   * that the detail message from the embedded Throwable is returned.
   *
   */
  public void jaxrException_getMessageThrowableTest() throws Fault {
    String testName = "jaxrException_getMessageThrowableTest";
    boolean pass = true;
    String cause = "detail message from embedded throwable";
    try {
      Throwable tCause = new Throwable(cause);
      JAXRException e = new JAXRException(tCause);
      debug.add("e.getMessage returns: " + e.getMessage() + "\n");

      if (!(e.getMessage().endsWith(cause))) {
        pass = false;
        debug.add("Error: JAXRException cause not returned with getMessage!\n");
      } else
        debug.add("JAXRException cause returned successfully\n");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * @testName: jaxrException_initCauseTest
   *
   * @assertion_ids: JAXR:JAVADOC:27;
   *
   * @assertion: JAXRException.initCause(cause) -
   *
   * @test_Strategy: Create and verify JAXRException with an initial cause.
   *
   */
  public void jaxrException_initCauseTest() throws Fault {
    String testName = "jaxrException_initCauseTest()";
    String reason = "test reason";
    String cause = "test cause";
    boolean pass = true;
    try {
      Throwable tCause = new Throwable(cause);
      debug.add("Create a JAXRException()\n");
      JAXRException e = new JAXRException();
      if (!(e instanceof JAXRException)) {
        pass = false;
        debug.add(testName + " failed to create  JAXRException instance\n");
      } else
        debug.add("JAXRException created successfully\n");

      debug.add("Call initCause method and initialize the cause\n");
      e.initCause(tCause);
      //
      Throwable t = e.getCause();
      debug.add("Cause is: " + t.getMessage() + "\n");
      if (!(t.getMessage().equals(cause))) {
        pass = false;
        debug.add(testName + " failed to return valid Throwable\n");
      }
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + " failed", ue);
    }
    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

  /*
   * testName: jaxrException_getStatusTest
   *
   * @assertion_ids: JAXR:JAVADOC:29;
   *
   * @assertion: JAXRException.getStatus -
   *
   * @test_Strategy:
   *
   */
  public void jaxrException_getStatusTest() throws Fault {
    String testName = "jaxrException_getStatusTest()";
    boolean pass = false;
    Collection savedKeys = new ArrayList();

    try {
      // Processing stops on first SaveException encountered.
      Collection orgs = new ArrayList();
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);

      org.setName(blm.createInternationalString("org_for_getStatus_test"));
      org.setDescription(blm.createInternationalString("Description: Testorg"));

      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs);
      Collection ex = br.getExceptions();
      if (ex != null) {
        if (ex.size() > 0) {
          Iterator iter = ex.iterator();
          while (iter.hasNext()) {
            RegistryException re = (RegistryException) iter.next();
            debug.add("unexpected registry exception, its status is "
                + br.getStatus() + "\n");
          }
        }
      }

      Collection o = br.getCollection();
      Collection keys = new ArrayList();
      if (o != null) {
        if (o.size() > 0) {
          Iterator iter = o.iterator();
          while (iter.hasNext()) {
            javax.xml.registry.infomodel.Key theKey = (javax.xml.registry.infomodel.Key) iter
                .next();
            keys.add(theKey);
            savedKeys.add(theKey);
          }
        }
      }
      javax.xml.registry.infomodel.Key myKey = blm.createKey("myKey");
      keys.add(myKey);
      // cause a failure ..........
      br = blm.deleteOrganizations(keys);
      // note: about getStatus from BulkResponse - it is not possible to
      // to reliably generate a partial response here.
      ex = br.getExceptions();
      if (ex != null) {
        debug.add("expected registry exception, its status is " + br.getStatus()
            + "\n");
        if (ex.size() > 0) {
          Iterator iter = ex.iterator();
          while (iter.hasNext()) {
            RegistryException re = (RegistryException) iter.next();
            // status should indicate that there was a problem
            if (re.getStatus() != JAXRResponse.STATUS_SUCCESS)
              pass = true;
            else
              debug
                  .add("Test failed with incorrect status = " + re.getStatus());
          }
        }
      }
    } catch (JAXRException je) {
      pass = true;
      TestUtil.logMsg("Caught Allowed JAXRException: " + je.getMessage());
      TestUtil.printStackTrace(je);
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + " failed", ue);
    } finally {
      // clean up - get rid of published orgs
      try {
        debug.add(" Cleanup: Remove test organization\n");
        blm.deleteOrganizations(savedKeys);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete registry object\n");
      }
    }

    if (!pass)
      throw new Fault(testName + " failed");

  } // end of test method

} // end of class
