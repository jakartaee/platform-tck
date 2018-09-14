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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.LocalizedString;

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
      super.cleanUpRegistry(); //
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      super.cleanup();
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
   * @testName: localizedString_getValue
   *
   * @assertion: getValue - Get the String value for this object. JAXR javadoc
   *
   * @assertion_ids: JAXR:JAVADOC:647
   *
   * @test_Strategy: create a LocalizedString with the String value. Verify that
   * LocalizedString.getValue returns the value set with the create method.
   *
   */
  public void localizedString_getValue() throws Fault {
    String testName = "localizedString_getValue";
    String value = "testValue";
    try {
      debug.add("Create a LocalizedString with the default locale and value of "
          + value + " \n");
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);

      debug
          .add("Verify that LocalizedString.getValue returns " + value + " \n");
      if (!(ls.getValue().equals(value))) {
        debug.add("Error: unexpected String returned for value \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method
  /*
   * @testName: localizedString_setValue
   *
   * @assertion: setValue - Set the String value for the specified object. JAXR
   * javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:653
   *
   * @test_Strategy: create a LocalizedString with the String value. Verify that
   * LocalizedString.getValue returns the value set with the
   * LocalizedString.setValue method.
   *
   */

  public void localizedString_setValue() throws Fault {
    String testName = "localizedString_setValue";
    String value = "testValue";
    String setValue = "fromSetValueMethod";
    try {
      debug.add("Create a LocalizedString with the default locale and value of "
          + value + " \n");
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);
      debug.add("Call LocalizedString.setValue(fromSetValueMethod) \n");
      ls.setValue(setValue);

      debug.add(
          "Verify that LocalizedString.getValue returns: " + setValue + "\n");
      if (!(ls.getValue().equals(setValue))) {
        debug.add("Error: unexpected String returned for value \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: localizedString_getLocale
   *
   * @assertion: getLocale - Get the Locale for this object. JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:646
   *
   * @test_Strategy: create a LocalizedString with a String value and the
   * Default Locale. Verify that LocalizedString.getLocale returns the Default
   * locale.
   *
   */
  public void localizedString_getLocale() throws Fault {
    String testName = "localizedString_getLocale";
    String value = "testValue";
    try {
      debug.add("Create a LocalizedString with the default locale and value of "
          + value + " \n");
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);

      debug.add(
          "Verify that LocalizedString.getLocale returns the default locale: "
              + Locale.getDefault().toString() + " \n");
      debug.add(
          "LocalizedString.getLocale: " + ls.getLocale().toString() + "\n");
      if (!(ls.getLocale().toString().equals(Locale.getDefault().toString()))) {
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
   * @testName: localizedString_setLocale
   *
   * @assertion: setLocale - Set the Locale for this object. JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:651
   *
   * @test_Strategy: create a LocalizedString with the String value and Default
   * Locale. Use LocalizedString.setLocale to change the locale to UK. Verify
   * with the getLocale method that setLocale worked.
   *
   */
  public void localizedString_setLocale() throws Fault {
    String testName = "localizedString_setLocale()";
    String value = "testValue";
    try {
      debug.add("Create a LocalizedString with the default locale and value of "
          + value + " \n");
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);
      debug.add("Set LocalizedString Locale to UK \n");
      Locale l = Locale.UK;
      ls.setLocale(l);

      debug.add("Verify that LocalizedString.getLocale returns: " + l.toString()
          + "\n");
      if (!(ls.getLocale().toString().equals(l.toString()))) {
        debug.add("Error: unexpected Locale returned from getLocale \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: localizedString_getCharsetName
   *
   * @assertion: getCharsetName() - Gets the canonical name for the charset for
   * this object. JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:643;JAXR:SPEC:113;
   *
   * @test_Strategy: create a LocalizedString with a String value, charsetName
   * and the Default Locale. Verify that LocalizedString.getCharsetName()
   * returns the String that was specified with the create call.
   *
   */
  public void localizedString_getCharsetName() throws Fault {
    String testName = "localizedString_getCharsetName";
    String charName = "US-ASCII";
    String value = "testValue";
    try {
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(), value,
          charName);
      debug.add(
          "In the create method set the char set name to " + charName + "\n");
      debug.add("ls.getCharsetName() should return " + charName + "\n");
      debug.add("ls.getCharsetName() returns: " + ls.getCharsetName() + "\n");

      if (!(ls.getCharsetName().equals(charName))) {
        debug.add("Error: unexpected String returned for charsetname \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: localizedString_getCharsetNameDefault
   *
   * @assertion: getCharsetName() - Gets the canonical name for the charset for
   * this object. Must return the default charset when there is no charset name
   * defined. JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:643
   *
   * @test_Strategy: create a LocalizedString with a String value and the
   * Default Locale. Verify that LocalizedString.getCharsetName() returns the
   * default charset
   *
   */
  public void localizedString_getCharsetNameDefault() throws Fault {
    String testName = "localizedString_getCharsetNameDefault";
    String value = "testValue";

    try {
      debug.add("Create a LocalizedString with the default locale and value of "
          + value + " \n");
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);
      debug.add(
          "Verify that ls.getcharsetName returns the default charsetName which is: "
              + LocalizedString.DEFAULT_CHARSET_NAME + "\n");

      debug.add("ls.getCharsetName() returns: " + ls.getCharsetName() + "\n");
      if (!(ls.getCharsetName().equals(LocalizedString.DEFAULT_CHARSET_NAME))) {

        debug.add("Error: unexpected String returned for charsetname \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: localizedString_setCharsetName
   *
   * @assertion: setCharsetName - Set the canonical name for the charset for
   * this object. JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:649
   *
   * @test_Strategy: create a LocalizedString with the String value and Default
   * Locale. Use LocalizedString.setCharsetName to change to "UTF-16"; Verify
   * with the getLocale method that setLocale worked.
   *
   */
  public void localizedString_setCharsetName() throws Fault {
    String testName = "localizedString_setCharsetName()";
    String charName = "UTF-16";
    String value = "testValue";
    try {
      LocalizedString ls = blm.createLocalizedString(Locale.getDefault(),
          value);
      ls.setCharsetName(charName);
      debug.add("Verify that LocalizedString.getCharsetName returns: "
          + charName + "\n");
      debug.add("LocalizedString.getCharsetName returned " + ls.getCharsetName()
          + "\n");
      if (!(ls.getCharsetName().equals(charName))) {
        debug.add(
            "Error: unexpected charsetName returned from getCharsetName\n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

} // end of class
