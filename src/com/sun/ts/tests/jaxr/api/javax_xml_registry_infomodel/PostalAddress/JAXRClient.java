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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.PostalAddress;

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
  String streetNumber = "47";

  String street = "Amsden";

  String city = "Arlington";

  String stateOrProvince = "MA";

  String country = "USA";

  String postalCode = "02146";

  String type = "home";

  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod;
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
   * @testName: postalAddress_getCityTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:549;
   *
   * @assertion: getCity - The city default is an empty string.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getCityTest() throws Fault {
    String testName = "postalAddress_getCityTest";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retCity = pa.getCity();
      debug.add("Method returned city: " + retCity + "\n");
      if (!(retCity.equals(city)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getCityTest

  /*
   * @testName: postalAddress_getCountryTest
   *
   * @assertion_ids: JAXR:JAVADOC:561;
   *
   * @assertion: getCountry - The Country
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getCountryTest() throws Fault {
    String testName = "postalAddress_getCountryTest";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retCountry = pa.getCountry();
      debug.add("Method returned country: " + retCountry + "\n");
      if (!(retCountry.equals(country)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getCountryTest

  /*
   * @testName: postalAddress_getPostalCode
   *
   * @assertion_ids: JAXR:JAVADOC:557;
   *
   * @assertion: getPostalCode - The postal or zip code Default is an empty
   * String.
   *
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getPostalCode() throws Fault {
    String testName = "postalAddress_getPostalCode";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retPostalCode = pa.getPostalCode();
      debug.add("Method returned postal code: " + retPostalCode + "\n");
      if (!(retPostalCode.equals(postalCode)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getPostalCode

  /*
   * @testName: postalAddress_getStateOrProvince
   *
   *
   * @assertion_ids: JAXR:JAVADOC:553;
   *
   * @assertion: getStateOrProvince- The state or province. Default is an empty
   * String.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getStateOrProvince() throws Fault {
    String testName = "postalAddress_getStateOrProvince";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retStateOrProvince = pa.getStateOrProvince();
      debug
          .add("Method returned StateOrProvince: " + retStateOrProvince + "\n");
      if (!(retStateOrProvince.equals(stateOrProvince)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getStateOrProvince

  /*
   * @testName: postalAddress_getStreet
   *
   *
   * @assertion_ids: JAXR:JAVADOC:541;
   *
   * @assertion: getStreet - The street name. Default is an empty String.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getStreet() throws Fault {
    String testName = "postalAddress_getStreet";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retStreet = pa.getStreet();
      debug.add("Method returned street: " + retStreet + "\n");
      if (!(retStreet.equals(street)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getStreet

  /*
   * @testName: postalAddress_getStreetNumber
   *
   * @assertion_ids: JAXR:JAVADOC:545;
   *
   * @assertion: getStreetNumber - The street number. Default is an empty
   * String.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getStreetNumber() throws Fault {
    String testName = "postalAddress_getStreetNumber";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retStreetNumber = pa.getStreetNumber();
      debug.add("Method returned streetNumber: " + retStreetNumber + "\n");
      if (!(retStreetNumber.equals(streetNumber)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getStreetNumber

  /*
   * @testName: postalAddress_getType
   *
   * @assertion_ids: JAXR:JAVADOC:565;
   *
   * @assertion: getType - The type of address.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_getType() throws Fault {
    String testName = "postalAddress_getType";
    try {
      // create an EmailAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      String retType = pa.getType();
      debug.add("Method returned type: " + retType + "\n");
      if (!(retType.equals(type)))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of postalAddress_getType

  /*
   * @testName: postalAddress_setCityTest
   *
   * @assertion_ids: JAXR:JAVADOC:551;
   *
   * @assertion: setCity - Sets the city. default is an empty string.
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setCityTest() throws Fault {
    String testName = "postalAddress_setCityTest";
    String nullString = "";
    int errorCount = 0;
    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      // default for city is a NULL String
      if (!(pa.getCity().equals(nullString))) {
        errorCount = errorCount + 1;
        debug.add("Error: Default for getCity should be a NULL String\n");
      }

      debug
          .add("Set city to " + city + " and then verify by calling getCity\n");
      pa.setCity(city);
      String retCity = pa.getCity();
      debug.add("Method returned city: " + retCity + "\n");
      if (!(retCity.equals(city))) {
        debug.add("Error: returned city did not match what was set\n");
        errorCount = errorCount + 1;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (errorCount > 0)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setCityTest

  /*
   * @testName: postalAddress_setCountryTest
   *
   * @assertion_ids: JAXR:JAVADOC:563;
   *
   * @assertion: setCountry - Sets the Country
   *
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setCountryTest() throws Fault {
    String testName = "postalAddress_setCountryTest";
    int errorCount = 0;
    String nullString = "";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      // default for country is a NULL String

      if (!(pa.getCountry().equals(nullString))) {
        errorCount = errorCount + 1;
        debug.add("Error: Default for getCountry should be a NULL String\n");
      }
      debug.add("Set country to " + country
          + " and then verify by calling getCountry\n");
      pa.setCountry(country);
      String retCountry = pa.getCountry();
      debug.add("Method returned country: " + retCountry + "\n");
      if (!(retCountry.equals(country))) {
        debug.add("Error: returned country did not match what was set\n");
        errorCount = errorCount + 1;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (errorCount > 0)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setCountryTest

  /*
   * @testName: postalAddress_setPostalCodeTest
   *
   * @assertion_ids: JAXR:JAVADOC:559;
   *
   * @assertion: setPostalCode - Sets the postal or zip code
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setPostalCodeTest() throws Fault {
    String testName = "postalAddress_setPostalCodeTest";
    boolean pass = false;
    String postalCode = "02321";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      debug.add("Set PostalCode to " + postalCode
          + " and then verify by calling getPostalCode\n");
      pa.setPostalCode(postalCode);
      String retPostalCode = pa.getPostalCode();
      debug.add("Method returned postalcode : " + retPostalCode + "\n");
      if (!(retPostalCode.equals(postalCode))) {
        debug.add("Error: returned country did not match what was set\n");
      } else
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setPostalCode

  /*
   * @testName: postalAddress_setStateOrProvinceTest
   *
   * @assertion_ids: JAXR:JAVADOC:555;
   *
   * @assertion: setStateOrProvince - The state or province
   *
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setStateOrProvinceTest() throws Fault {
    String testName = "postalAddress_setStateOrProvinceTest";
    boolean pass = false;
    String stateOrProvince = "NH";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      debug.add("Set  stateOrProvince to " + stateOrProvince
          + " and then verify by calling getStateOrProvince\n");
      pa.setStateOrProvince(stateOrProvince);
      String retStateOrProvince = pa.getStateOrProvince();
      debug
          .add("Method returned StateOrProvince: " + retStateOrProvince + "\n");
      if (!(retStateOrProvince.equals(stateOrProvince))) {
        debug.add(
            "Error: returned stateOrProvince did not match what was set\n");
      } else
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setStateOrProvinceTest

  /*
   * @testName: postalAddress_setStreetTest
   *
   * @assertion_ids: JAXR:JAVADOC:543;
   *
   * @assertion: setStreet - Sets the street name
   *
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setStreetTest() throws Fault {
    String testName = "postalAddress_setStreetTest";
    boolean pass = false;
    String street = "Main";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      debug.add("Set  street to " + street
          + " and then verify by calling getStreet\n");
      pa.setStreet(street);
      String retStreet = pa.getStreet();
      debug.add("Method returned street: " + retStreet + "\n");
      if (!(retStreet.equals(street))) {
        debug.add("Error: returned street did not match what was set\n");
      } else
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setStreetTest

  /*
   * @testName: postalAddress_setStreetNumberTest
   *
   * @assertion_ids: JAXR:JAVADOC:547;
   *
   * @assertion: setStreetNumber - Sets the street number
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setStreetNumberTest() throws Fault {
    String testName = "postalAddress_setStreetNumberTest";
    boolean pass = false;
    String streetNumber = "1245";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      debug.add("Set  streetNumber to " + streetNumber
          + " and then verify by calling getStreetNumber\n");
      pa.setStreetNumber(streetNumber);
      String retStreetNumber = pa.getStreetNumber();
      debug.add("Method returned streetNumber: " + retStreetNumber + "\n");
      if (!(retStreetNumber.equals(streetNumber))) {
        debug.add("Error: returned streetNumber did not match what was set\n");
      } else
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setStreetNumberTest

  /*
   * @testName: postalAddress_setTypeTest
   *
   * @assertion_ids: JAXR:JAVADOC:567;
   *
   * @assertion: setType - Sets the street number
   *
   * @test_Strategy:
   *
   */
  public void postalAddress_setTypeTest() throws Fault {
    String testName = "postalAddress_setTypeTest";
    boolean pass = false;
    String type = "regional office";

    try {
      PostalAddress pa = (PostalAddress) blm
          .createObject(LifeCycleManager.POSTAL_ADDRESS);
      debug.add(
          "Set  type to " + type + " and then verify by calling getType\n");
      pa.setType(type);
      String retType = pa.getType();
      debug.add("Method returned type: " + retType + "\n");
      if (!(retType.equals(type))) {
        debug.add("Error: returned type did not match what was set\n");
      } else
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of postalAddress_setTypeTest

} // end of JAXRClient
