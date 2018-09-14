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
 * @(#)JAXRClient.java	1.11 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.InternationalString;

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
      // super.cleanUpRegistry(); //
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      // super.cleanup();
      // super.cleanUpRegistry();
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
   * @testName: internationalString_addLocalizedString
   *
   *
   * @assertion_ids: JAXR:JAVADOC:664
   *
   * @test_Strategy: Create a Localized String for Locale.US and set the value
   * to "USLocale". Add this LocalizedString to an InternationalString. Verify
   * it was added by retrieving it with a call to getLocalizedString.
   * 
   *
   */
  public void internationalString_addLocalizedString() throws Fault {
    String testName = "internationalString_addLocalizedString";
    try {
      Locale l = Locale.US;
      String value = "USLocale";
      LocalizedString ls = blm.createLocalizedString(l, value);

      InternationalString is = blm.createInternationalString(Locale.CANADA,
          "Canada");
      debug.add(
          "Created an InternationalString Localized string count should be 1");
      debug.add("localized string count is " + is.getLocalizedStrings().size()
          + "\n");

      is.addLocalizedString(ls);
      debug.add(
          "Added a localized string. InternationalString Localized string count should be 2");
      debug.add("localized string count is " + is.getLocalizedStrings().size()
          + "\n");

      LocalizedString lsReturned = is.getLocalizedString(l,
          LocalizedString.DEFAULT_CHARSET_NAME);
      debug.add(
          "Check the value of the LocalizedString returned: " + value + "\n");
      debug.add("LocalizedString returned: " + lsReturned.getValue() + "\n");
      if (!(lsReturned.getValue().equals(value))) {
        debug.add(
            "Error: unexpected String returned for localestring returned  \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");

    }
  } // end of method

  /*
   * @testName: internationalString_getValue
   *
   *
   * @assertion_ids: JAXR:JAVADOC:655; JAXR:JAVADOC:660;JAXR:SPEC:112;
   *
   * @test_Strategy: Create a collection of LocalizedStrings with 4 locale value
   * pairs. Create an InternationalString and add the LocalizedString
   * collection. Call setValue to set the default locale to French. Verify with
   * a call to getValue.
   *
   */
  public void internationalString_getValue() throws Fault {

    String testName = "internationalString_getValue";

    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };
    // set the value for the default locale.

    try {
      Collection cls = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = blm.createLocalizedString(l[i], value[i]);
        cls.add(ls);
      }
      InternationalString is = blm.createInternationalString();

      is.addLocalizedStrings(cls);
      is.setValue(value[3]);
      if (!(is.getValue().equals(value[3]))) {
        debug.add("Error: unexpected String returned for default locale  \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method
  /*
   * @testName: internationalString_getValueLocale
   *
   *
   * @assertion_ids: JAXR:JAVADOC:657
   *
   * @test_Strategy: Verify that locale maps to the correct value. Create a
   * collection of Localized Strings. Add them to an InternationalString. Verify
   * that the mapping is correct.
   *
   */

  public void internationalString_getValueLocale() throws Fault {
    String testName = "internationalString_getValueLocale";
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };

    try {
      Collection cls = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = blm.createLocalizedString(l[i], value[i]);
        cls.add(ls);
      }
      InternationalString is = blm.createInternationalString();

      is.addLocalizedStrings(cls);
      debug.add("Verify that each locale returns the matching value\n");
      for (int i = 0; i < value.length; i++) {
        debug.add("Locale: " + l[i] + " maps to " + value[i] + "\n");
        debug.add("is.getValue[" + i + "] = " + is.getValue(l[i]) + "\n");
        if (!(is.getValue(l[i]).equals(value[i]))) {
          debug.add("Error: locale and value did not match \n");
          throw new Fault(testName + " failed ");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_getValueNull
   *
   * @assertion_ids: JAXR:JAVADOC:658
   *
   * @test_Strategy: Verify that getValue(locale) returns null if there is no
   * locale specific value. Create a LocalizedString with createObject. Add a
   * locale, but not a value. getValue should return null.
   *
   */
  public void internationalString_getValueNull() throws Fault {
    String testName = "internationalString_getValueNull";
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };

    try {
      Collection cls = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = (LocalizedString) blm
            .createObject(LifeCycleManager.LOCALIZED_STRING);
        ls.setLocale(l[i]);
        cls.add(ls);
      }
      InternationalString is = blm.createInternationalString();
      is.addLocalizedStrings(cls);

      debug.add(
          "No values have been mapped to the locales, values should return null \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("For Locale: " + l[i] + "\n");
        debug.add("is.getValue[" + i + "] = " + is.getValue(l[i]) + "\n");
        if (!(is.getValue(l[i]) == null)) {
          debug.add("Error: value was not null as expected \n");
          throw new Fault(testName + " failed ");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_setValueLocaleString
   *
   *
   * @assertion_ids: JAXR:JAVADOC:662
   *
   * @test_Strategy: Use createObject to create a collection of
   * LocalizedStrings. Set locales for each LocalizedString, but not values. Add
   * the collection of LocalizedStrings to an InternationalString. Verify the
   * values for each LocalizedString are null. Use
   * InternationalString(Locale,String) to set the values for each Locale and
   * verify with getValue(Locale).
   *
   */
  public void internationalString_setValueLocaleString() throws Fault {
    String testName = "internationalString_setValueLocaleString";
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };

    try {
      Collection cls = new ArrayList();
      debug.add(
          "Create a collection of LocalizedStrings that do not have locale or value specified\n");
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = (LocalizedString) blm
            .createObject(LifeCycleManager.LOCALIZED_STRING);
        ls.setLocale(l[i]);
        cls.add(ls);
      }
      InternationalString is = blm.createInternationalString();

      debug.add(
          "Add the collection of LocalizedStrings to an InternationalString \n");

      is.addLocalizedStrings(cls);
      debug.add("Verify that the value for each locale returns null \n");
      // debug.add("Verify that each locale returns the matching value\n");
      for (int i = 0; i < value.length; i++) {
        // debug.add("Locale: " + l[i] + " maps to " + value[i] + "\n");
        debug.add("Locale: " + l[i] + "\n");
        debug.add("is.getValue[" + i + "] = " + is.getValue(l[i]) + "\n");
        if (is.getValue(l[i]) != null) {
          debug.add("Error: value should be null - test did not complete \n");
          throw new Fault(testName + " failed ");
        }
      }
      debug.add(
          "For each Localized String in the InternationalString -  set the value \n");
      for (int i = 0; i < value.length; i++) {
        is.setValue(l[i], value[i]);
      }
      debug.add(
          "Verify the value with InternationalString.getValue(Locale)  \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("Locale: " + l[i] + " maps to " + value[i] + "\n");
        debug.add("is.getValue[" + i + "] = " + is.getValue(l[i]) + "\n");
        if (!(is.getValue(l[i]).equals(value[i]))) {
          debug.add("Error: locale and value did not match \n");
          throw new Fault(testName + " failed ");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_addLocalizedStrings
   *
   *
   * @assertion_ids: JAXR:JAVADOC:666
   *
   * @test_Strategy:
   *
   */
  public void internationalString_addLocalizedStrings() throws Fault {
    String testName = "internationalString_addLocalizedStrings";
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };

    try {
      debug.add("Create a collection of LocalizedStrings\n");
      Collection cls = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = blm.createLocalizedString(l[i], value[i]);
        cls.add(ls);
      }
      InternationalString is = blm.createInternationalString();
      debug.add("Add the collection to InternationalString\n");
      is.addLocalizedStrings(cls);
      debug.add(
          "Verify the LocalizedStrings were adding by checking with InternationalString.getValue(Locale)  \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("Locale: " + l[i] + " maps to " + value[i] + "\n");
        debug.add("is.getValue[" + i + "] = " + is.getValue(l[i]) + "\n");
        if (!(is.getValue(l[i]).equals(value[i]))) {
          debug.add("Error: locale and value did not match \n");
          throw new Fault(testName + " failed ");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_removeLocalizedString
   *
   * @assertion_ids: JAXR:JAVADOC:668
   *
   * @test_Strategy: Create a LocalizedString for Canada. Add it to and
   * InternationalString which contains one LocalizedString for US. Use
   * removeLocalizedString to remove Canada. Verify that it was removed from the
   * InternationalString with getLocalizedString.
   *
   */
  public void internationalString_removeLocalizedString() throws Fault {
    String testName = "internationalString_removeLocalizedString";
    String valueCanada = "Canada";
    Locale localeCanada = Locale.CANADA;
    String valueUS = "US";
    Locale localeUS = Locale.US;

    try {
      debug.add("Create a LocalizedString for Canada \n");
      LocalizedString ls = blm.createLocalizedString(localeCanada, valueCanada);

      InternationalString is = blm.createInternationalString(localeUS, valueUS);
      debug.add("Add the LocalizedString to InternationalString\n");
      is.addLocalizedString(ls);
      debug.add(
          "Verify that the InternationalString contains the LocalizedString for Canada \n");
      debug.add(
          "is.getValue(Locale.Canada) is :" + is.getValue(localeCanada) + "\n");
      if (!(is.getValue(localeCanada).equals(valueCanada))) {
        throw new Fault(testName + " failed  - test did not complete");
      }
      debug.add("Remove the LocalizedString for Canada\n");
      is.removeLocalizedString(ls);
      debug.add(
          "Canada locale removed from InternationalString, LocalizedString count should be 1 \n");
      debug.add(
          "LocalizedString count is " + is.getLocalizedStrings().size() + "\n");
      if (is.getLocalizedStrings().size() != 1) {
        throw new Fault(testName + " failed  - test did not complete");
      }

      debug.add("Verify that there is no Locale for Canada\n");
      debug.add(
          "is.getLocalizedString(localeCanada,LocalizedString.DEFAULT_CHARSET_NAME) returns "
              + is.getLocalizedString(localeCanada,
                  LocalizedString.DEFAULT_CHARSET_NAME)
              + "\n");
      if (is.getLocalizedString(localeCanada,
          LocalizedString.DEFAULT_CHARSET_NAME) != null) {
        throw new Fault(testName + " failed  ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_removeLocalizedStrings
   *
   * @assertion_ids: JAXR:JAVADOC:670
   *
   * @test_Strategy: Create LocalizedStrings for US,UK,Canada,and French. Add to
   * InternationalString. : Verify that they were added with getValue for each
   * Locale. Remove them and verify they were removed by getLocalizedStrings
   *
   */
  public void internationalString_removeLocalizedStrings() throws Fault {
    String testName = "internationalString_removeLocalizedStrings";
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };
    // set the value for the default locale.

    try {
      InternationalString is = blm.createInternationalString();
      Collection cls = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = blm.createLocalizedString(l[i], value[i]);
        is.addLocalizedString(ls);
        cls.add(ls);
      }
      debug.add("Created and added LocalizedStrings to InternationalString\n");
      debug.add("Verify that they were added \n");

      for (int i = 0; i < value.length; i++) {
        debug.add("Locale: " + l[i] + " maps to " + value[i] + "\n");
        debug.add("is.getValue(" + i + ") = " + is.getValue(l[i]) + "\n");
        if (!(is.getValue(l[i]).equals(value[i]))) {
          debug.add("Error: Failed to find a Locale\n");
          throw new Fault(testName + " failed - test did not complete ");
        }
      }
      debug.add("Remove the localized strings\n");
      is.removeLocalizedStrings(cls);

      debug.add("Verify the removal\n");
      debug.add("LocalizedString count should be o \n");
      debug.add(
          "LocalizedString count is " + is.getLocalizedStrings().size() + "\n");
      if (is.getLocalizedStrings().size() != 0) {
        throw new Fault(
            testName + " failed  - LocalizedString count should be 0!");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: internationalString_getLocalizedString
   *
   * @assertion_ids: JAXR:JAVADOC:672
   *
   * @test_Strategy: Create an InternationalString with a LocalizedString for
   * Canada. Set the value = "Canada". Call getLocalizedString and verify the
   * Locale by checking the value.
   *
   */
  public void internationalString_getLocalizedString() throws Fault {
    String testName = "internationalString_getLocalizedString";
    Locale l = Locale.CANADA;
    String value = "Canada";
    try {
      InternationalString is = blm.createInternationalString(l, value);
      debug.add(
          "Create an InternationalString with a LocalizedString for Canada \n");
      LocalizedString lsReturned = is.getLocalizedString(l,
          LocalizedString.DEFAULT_CHARSET_NAME);
      debug.add(
          "Check the value of the LocalizedString returned: " + value + "\n");
      debug.add("LocalizedString returned: " + lsReturned.getValue() + "\n");
      if (!(lsReturned.getValue().equals(value))) {
        debug.add(
            "Error: unexpected String returned for localestring returned  \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");

    }
  } // end of method

  /*
   * @testName: internationalString_getLocalizedStrings
   *
   *
   * @assertion_ids: JAXR:JAVADOC:674
   *
   * @test_Strategy: Create a collection of LocalizedStrings and add to
   * InternationalString Use getLocalizedStrings() to get back the collection.
   * Verify that all LocalizedStrings were returned by iterating thru the
   * collection and checking values.
   * 
   */
  public void internationalString_getLocalizedStrings() throws Fault {
    String testName = "internationalString_getLocalizedStrings";
    int foundCount = 0;
    String value[] = { "US", "UK", "Canada", "French" };
    Locale l[] = { Locale.US, Locale.UK, Locale.CANADA, Locale.FRENCH };
    try {
      Collection cls = new ArrayList();
      ArrayList values = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        LocalizedString ls = blm.createLocalizedString(l[i], value[i]);
        cls.add(ls);
        values.add(value[i]);
        debug.add("Creating LocalizedString for : " + value[i] + "\n");
      }
      InternationalString is = blm.createInternationalString();
      is.addLocalizedStrings(cls);
      Collection returnLs = is.getLocalizedStrings();
      Iterator iter = returnLs.iterator();
      while (iter.hasNext()) {
        LocalizedString ls = (LocalizedString) iter.next();
        if (values.contains(ls.getValue())) {
          debug.add("Found " + ls.getValue() + "\n");
          foundCount = foundCount + 1;
        }
      }
      if (!(foundCount == value.length)) {
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

} // end of class
