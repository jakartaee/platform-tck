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
 * @(#)JAXRClient.java	1.14 03/05/16
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.TelephoneNumber;

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
  private static final int SETAREACODE = 0;

  private static final int SETCOUNTRYCODE = 1;

  private static final int SETEXTENSION = 2;

  private static final int SETURL = 3;

  private static final int GETAREACODE = 4;

  private static final int GETCOUNTRYCODE = 5;

  private static final int GETEXTENSION = 6;

  private static final int GETURL = 7;

  private static final int NUM_METHODS = 8;

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
   * @testName: telephoneNumber_setGetNumberTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:354;JAXR:JAVADOC:366;
   *
   * @assertion: setNumber - The telephone number suffix not including the
   * country or area code getNumber - The telephone number suffix not including
   * the country or area code. Default is an empty String.
   *
   *
   * @test_Strategy: Set the telephone number with a call to the setNumber
   * method. Verify with the getNumber method.
   *
   */
  public void telephoneNumber_setGetNumberTest() throws Fault {
    String testName = "telephoneNumber_setGetNumberTest";
    String telno = "123-1234";
    boolean pass = false;
    try {
      TelephoneNumber number = blm.createTelephoneNumber();
      debug.add("Setting the number to " + telno + "\n");
      number.setNumber(telno);
      String retNo = number.getNumber();
      debug.add("getNumber returned " + retNo + "\n");
      if (retNo.equals(telno))
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
   * @testName: telephoneNumber_setGetTypeTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:360;JAXR:JAVADOC:372;
   *
   * @assertion: setType - The type of telephone number (e.g. fax etc.). Any
   * String will do. getType - The type of telephone number (e.g. fax etc.). Any
   * String would do.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Set the type with a call to the setType method. Verify with
   * the getType method.
   *
   */
  public void telephoneNumber_setGetTypeTest() throws Fault {
    String testName = "telephoneNumber_setGetTypeTest";
    String type = "fax";
    boolean pass = false;
    try {
      TelephoneNumber number = blm.createTelephoneNumber();
      debug.add("Setting the type to " + type + "\n");
      number.setType(type);
      String rettype = number.getType();
      debug.add("getType returned " + rettype + "\n");
      if (rettype.equals(type))
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
   * @testName: TelephoneNumber_UnsupportedCapabilityException_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:62;JAXR:SPEC:63;JAXR:SPEC:64;JAXR:SPEC:65;
   * JAXR:SPEC:66;JAXR:SPEC:67;JAXR:SPEC:68;JAXR:SPEC:69;
   *
   * @assertion: A JAXR provider must implement all methods that are assigned a
   * capability level that is greater than the capability level declared by the
   * JAXR provider, to throw an UnsupportedCapabilityException. JAXR
   * Specification version 1, section 2.10.1.3
   *
   *
   * @test_Strategy: This is a level 1 method. Level 0 providers should throw an
   * UnsupportedCapabilityException Level 1 providers should support it. Verify.
   *
   *
   */
  public void TelephoneNumber_UnsupportedCapabilityException_Test()
      throws Fault {
    String testName = "TelephoneNumber_UnsupportedCapabilityException_Test";
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "This provider reports capability level = " + providerlevel + "\n");
      // create a telephone number.
      TelephoneNumber nm = blm.createTelephoneNumber();
      String areaCode = "302";
      String countryCode = "IT";
      String extension = "1234";
      String url = "url";

      for (int method = 0; method < NUM_METHODS; method++) {

        try {
          switch (method) {
          case SETAREACODE:
            debug.add("Calling setAreaCode\n");
            nm.setAreaCode(areaCode);
            break;
          case SETCOUNTRYCODE:
            debug.add("Calling setCountryCode\n");
            nm.setCountryCode(countryCode);
            break;
          case SETEXTENSION:
            debug.add("Calling setExtension\n");
            nm.setExtension(extension);
            break;
          case SETURL:
            debug.add("Calling setUrl\n");
            nm.setUrl(url);
            break;
          case GETAREACODE:
            debug.add("Calling getAreaCode\n");
            String retAreaCode = nm.getAreaCode();
            if (providerlevel > 0) {
              if (!(nm.getAreaCode().equals(areaCode))) {
                debug.add(
                    " level 1 provider -  failed to get a match for areacode\n");
                failcount = failcount + 1;
              }
            }
            break;
          case GETCOUNTRYCODE:
            debug.add("Calling getCountryCode\n");
            String retCountryCode = nm.getCountryCode();
            if (providerlevel > 0) {
              if (!(nm.getCountryCode().equals(countryCode))) {
                debug.add(
                    " level 1 provider -  failed to get a match for countrycode\n");
                failcount = failcount + 1;
              }
            }
            break;
          case GETEXTENSION:
            debug.add("Calling getExtension\n");
            String retExtension = nm.getExtension();
            if (providerlevel > 0) {
              if (!(nm.getExtension().equals(extension))) {
                debug.add(
                    " level 1 provider -  failed to get a match for extension\n");
                failcount = failcount + 1;
              }
            }
            break;
          case GETURL:
            debug.add("Calling getUrl\n");
            String retUrl = nm.getUrl();
            if (providerlevel > 0) {
              if (!(nm.getUrl().equals(url))) {
                debug.add(
                    " level 1 provider -  failed to get a match for url\n");
                failcount = failcount + 1;
              }
            }
            break;
          default:
            throw new Fault("Test Error - Unknown method: " + method);

          } // end of switch....
          if (providerlevel == 0) {
            debug.add(
                "Error: UnsupportedCapabilityException expected for capability level 0\n");
            failcount = failcount + 1;
          }
        } catch (UnsupportedCapabilityException ue) {
          // Provider level must be 0 to get here
          if (providerlevel == 0) {
            debug.add("Good it threw an UnsupportedCapabilityException\n");
            TestUtil.printStackTrace(ue);
          } else {
            debug.add(
                "Error: UnsupportedCapabilityException not expected for this capability level\n");
            failcount = failcount + 1;
          }
        } catch (Exception ee) {
          debug.add("Error threw an unexpected exception \n");
          TestUtil.printStackTrace(ee);
          failcount = failcount + 1;
        }
      } // end of for loop.................

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    if (failcount > 0)
      throw new Fault(testName + " failed ");
  } // end

} // end of test class
