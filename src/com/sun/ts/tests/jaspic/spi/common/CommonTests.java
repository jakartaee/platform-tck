/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.spi.common;

import java.util.*;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;

import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactoryForStandalone;
import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigProviderStandalone;
import com.sun.ts.tests.jaspic.tssv.config.TSRegistrationListener;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;

/**
 * This contains generic methods/tests which are expected to be used by tests in
 * each of the supported profiles (eg SERVLET, SOAP, Standalone, etc)
 *
 * @author Oracle
 */
public class CommonTests {
  private static final String ACPClass = "com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigProviderStandalone";

  private static final String ACPLayer = "STANDALONE_LAYER";

  private static final String ACPAppContext = "STANDALONE_CONTEXT";

  private static final String ACPDesc = "Some description";

  private PrintWriter out;

  public CommonTests() {

  }

  public CommonTests(PrintWriter out) {
    this.out = out;
  }

  public void setOut(PrintWriter out) {
    this.out = out;
  }

  public PrintWriter getOut() {
    return this.out;
  }

  /*
   * this tests the following: - get current ACF - verify that
   * removeRegistration(arg) will return FALSE when invalid arg supplied.
   */
  public void _ACF_testFactoryRemoveRegistration() throws Exception {
    try {
      // get current AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();
      if (acf == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        printIt("FAILURE - Could not get current AuthConfigFactory.");
        throw new Exception("_ACF_testFactoryRemoveRegistration : FAILED");
      }

      // based on javadoc, calling removeRegistration with invalid registration
      // must return false. It must NOT return true nor throw exception.
      boolean rval = acf
          .removeRegistration("SomePhakeRegistrationThatWontExist");

      if (rval == true) {
        printIt(
            "FAILED:  calling removeRegistration() on invalid registration returned true.");
        throw new Exception("_ACF_testFactoryRemoveRegistration : FAILED");
      }

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      printIt(
          "FAILED:  calling removeRegistration() with invalid registration threw exception.");
      throw ex;
    }

    printIt("_ACF_testFactoryRemoveRegistration : passed");
  }

  /*
   * this tests the following: - get current ACF - verify that
   * getRegistrationIDs(acp) NEVER returns null hint: must return empty array
   * even if unrecognized acp.
   */
  public void _ACF_testFactoryGetRegistrationIDs() throws Exception {
    try {
      // get current AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();
      if (acf == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        printIt("FAILURE - Could not get current AuthConfigFactory.");
        throw new Exception("_ACF_testFactoryGetRegistrationIDs : FAILED");
      }

      String[] ids = acf.getRegistrationIDs(null);

      if (ids == null) {
        printIt("FAILED:  calling getRegistrationIDs(null) returned null.");
        throw new Exception("_ACF_testFactoryGetRegistrationIDs : FAILED");
      }

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      printIt("FAILED:  calling getRegistrationIDs(null) threw exception.");
      throw ex;
    }

    printIt("_ACF_testFactoryGetRegistrationIDs : passed");
  }

  /*
   * this tests the following: - get current ACF - verify
   * getRegistrationContext(string) returns NULL for unrecognized string.
   */
  public void _ACF_testFactoryGetRegistrationContext() throws Exception {
    try {
      // get current AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();
      if (acf == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        printIt("FAILURE - Could not get current AuthConfigFactory.");
        throw new Exception("_ACF_testFactoryGetRegistrationContext : FAILED");
      }

      AuthConfigFactory.RegistrationContext rcontext = acf
          .getRegistrationContext("SomePhakeRegistrationThatWontExist");

      if (rcontext != null) {
        printIt(
            "FAILED:  calling getRegistrationContext() on invalid context returned non-null.");
        throw new Exception("_ACF_testFactoryGetRegistrationContext : FAILED");
      }

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      printIt(
          "FAILED:  calling getRegistrationContext() with invalid registration threw exception.");
      throw ex;
    }

    printIt("_ACF_testFactoryGetRegistrationContext : passed");
  }

  /*
   * this tests the following: - get vendors ACF - verify detachListener()
   * returns non-NULL for listener that is not found tied to ACP with the passed
   * in layer & appContext. unrecognized string.
   */
  public void _ACF_testFactoryDetachListener(String vendorsClassName)
      throws Exception {

    try {
      // change ACF to use vendors ACF instead of CTS ACF
      printIt("getting Auth Config Factory for:  " + vendorsClassName);
      AuthConfigFactory vendorsAcf = CommonUtils
          .getAuthConfigFactory(vendorsClassName);
      AuthConfigFactory.setFactory(vendorsAcf);
      String vendorACFClass = vendorsAcf.getClass().getName();
      printIt("vendorACFClass = " + vendorACFClass);

      MyRegistrationListener listener = new MyRegistrationListener("phakeLayer",
          "phakeAppContext");
      String[] listeners = vendorsAcf.detachListener(listener, "phakeLayer",
          "phakeAppContext");

      if (listener == null) {
        printIt("FAILED:  coult not instantiate valid MyRegistrationListener.");
        throw new Exception("_ACF_testFactoryDetachListener : FAILED");
      }

      if (listeners == null) {
        // should NOT get here since we expect non-null returned...it may be
        // empty
        // but should never be null
        printIt(
            "FAILED:  calling detachListener() on unfound listener returned null.");
        throw new Exception("_ACF_testFactoryDetachListener : FAILED");
      }

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      msg += "  Also, ensure caller has permission to detach the listener from the factory";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      printIt(
          "FAILED:  calling detachListener() with unfound listener threw exception.");
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("_ACF_testFactoryDetachListener : passed");
  }

  /*
   * this tests the following: - get current (CTS default) ACF - switch ACF to
   * use differetn (CTS) ACF - verify newly set ACF is correctly recognized (via
   * getFactory() calls) - rest ACF back to (CTS default)default
   */
  public void _ACF_getFactory() throws Exception {

    try {
      // get current AuthConfigFactory
      AuthConfigFactory currentAcf = AuthConfigFactory.getFactory();
      if (currentAcf == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        printIt("FAILURE - Could not get current AuthConfigFactory.");
        throw new Exception("ACF_getFactory : FAILED");
      }
      String currentACFClass = currentAcf.getClass().getName();
      printIt("ACF_getFactory.currentACFClass = " + currentACFClass);

      // set our ACF to a new/different AuthConfigFactory
      TSAuthConfigFactoryForStandalone newAcf = new TSAuthConfigFactoryForStandalone();
      AuthConfigFactory.setFactory(newAcf);
      String newACFClass = newAcf.getClass().getName();
      printIt("ACF_getFactory.newACFClass = " + newACFClass);

      // verify our calls to getFactory() are getting the newly set factory
      // instance and not the original ACF instance
      AuthConfigFactory testAcf = AuthConfigFactory.getFactory();
      if (testAcf == null) {
        printIt("FAILURE - Could not get newly set AuthConfigFactory.");
        throw new Exception("ACF_getFactory : FAILED");
      }
      String newlySetACFClass = testAcf.getClass().getName();
      printIt("ACF_getFactory.newlySetACFClass = " + newlySetACFClass);

      // Verify ACF's were set correctly
      if (!newlySetACFClass.equals(newACFClass)) {
        printIt("FAILURE - our current ACF does not match our newly set ACF");
        throw new Exception("ACF_getFactory : FAILED");
      } else {
        String msg = "success - newlySetACFClass == newACFClass == "
            + newACFClass;
        printIt(msg);
      }

      // restore original factory class
      AuthConfigFactory.setFactory(currentAcf);

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("ACF_getFactory : passed");
  }

  /*
   * This is more comprehensive than ACF_getFactory as this tests the following:
   * - get current (CTS default) ACF - verify we are starting with correct
   * default - switch ACF to use different (CTS) ACF - verify newly set ACF is
   * correctly recognized by checking a few different things (ACF factory
   * classname, etc... - rest ACF back to (CTS default)default
   */
  public void _ACFSwitchFactorys(String vendorsClassName) throws Exception {

    try {
      // get current (CTS) AuthConfigFactory
      AuthConfigFactory ctsACF = AuthConfigFactory.getFactory();
      if (ctsACF == null) {
        // this could fail due to permissions or because the system can't
        // find ACF (check for setting of -Djava.security.properties)
        printIt("FAILURE - Could not get current AuthConfigFactory.");
        throw new Exception("ACFSwitchFactorys : FAILED");
      }

      // verify we are starting with CTS ACF as the default ACF
      String startingACFClass = ctsACF.getClass().getName();
      printIt("startingACFClass = " + startingACFClass);
      if (!JASPICData.TSSV_ACF.equals(startingACFClass)) {
        printIt(
            "ERROR:  we are not starting with expected default ACF class of: "
                + JASPICData.TSSV_ACF);
        throw new Exception("ACFSwitchFactorys : FAILED");
      }

      // change ACF to use vendors ACF instead of CTS ACF
      printIt("getting Auth Config for:  " + vendorsClassName);
      AuthConfigFactory vendorsAcf = CommonUtils
          .getAuthConfigFactory(vendorsClassName);
      AuthConfigFactory.setFactory(vendorsAcf);
      String vendorACFClass = vendorsAcf.getClass().getName();
      printIt("vendorACFClass = " + vendorACFClass);

      // first verify we were able to change our ACF to use vendors
      if (vendorACFClass.equals(JASPICData.TSSV_ACF)) {
        // ohoh - nothing was changed we still have CTS default ACF
        String str = "FAILURE - Could not set vendors AuthConfigFactory.";
        printIt(str);
        throw new Exception("ACFSwitchFactorys : FAILED");
      }

      // next - verify our calls to getFactory() are getting a non-null factory
      AuthConfigFactory testAcf = AuthConfigFactory.getFactory();
      if (testAcf == null) {
        String str = "FAILURE - Could not get vendors AuthConfigFactory.";
        printIt(str);
        throw new Exception("ACFSwitchFactorys : FAILED");
      } else {
        String str = "Successfully set vendors ACF.";
        printIt(str);
      }

      String newlySetACFClass = testAcf.getClass().getName(); // should be
                                                              // confirming
                                                              // Impls ACF
      printIt("newlySetACFClass = " + newlySetACFClass);

      // next - verify getFactory() returns the expected/correct vendor class
      // name
      if (!newlySetACFClass.equals(vendorACFClass)) {
        String str = "FAILURE - calling getFactory().  ";
        str += "did not return expected/vendor ACF classname of: "
            + vendorACFClass;
        printIt(str);
        throw new Exception("ACFSwitchFactorys : FAILED");
      } else {
        String msg = "newlySetACFClass == vendorACFClass == " + vendorACFClass;
        printIt(msg);
      }

      printIt("ACFSwitchFactorys : PASSED");
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  ensure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      // unknown exception
      String msg = "got unknown exception:  " + ex.getMessage();
      printIt(msg);
      ex.printStackTrace();
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("ACFSwitchFactorys : passed");
  }

  /*
   * this tests that the ACF we currently expect is set to our CTS default ACF.
   * The only way this should be, is if the runtime correctly read the ACF from
   * the security files property of: authconfigprovider.factory
   */
  public void _testACFComesFromSecFile() throws Exception {

    // verify our call to getFactory() gets the default ACF instance
    AuthConfigFactory testAcf = AuthConfigFactory.getFactory();
    if (testAcf == null) {
      printIt("FAILURE - Could not get AuthConfigFactory.");
      throw new Exception("testACFComesFromSecFile : FAILED");
    }
    String defaultACFClass = testAcf.getClass().getName();
    printIt("testACFComesFromSecFile.defaultACFClass = " + defaultACFClass);

    // Verify ACF matches the ACF that *should* exist in security prop file
    if (!defaultACFClass.equals(JASPICData.TSSV_ACF)) {
      printIt("FAILURE - our default ACF does not match our default ACF");
      throw new Exception("testACFComesFromSecFile : FAILED");
    }

    printIt("testACFComesFromSecFile passed");
  }

  /*
   * This is verifying that the invocation of removeRegistration() with an
   * invalid and non-existant regId will return false. This assists with testing
   * assertion JASPIC:SPEC:345
   */
  public void _ACFRemoveRegistrationWithBadId() {

    try {
      Random rand = new Random();

      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      // create a unique registration Id
      String uniqueRegId = "someMadeUpandNonExistantRegId";
      uniqueRegId += String.valueOf(new Integer(rand.nextInt()).toString());
      boolean bval = acf.removeRegistration(uniqueRegId);
      if (bval == true) {
        printIt("FAILURE - acf.removeRegistration(invalidId) returned true");
      }
    } catch (Exception ex) {
      String str = "FAILURE - acf.removeRegistration(invalidId) exception msg: ";
      str += ex.getMessage();
      printIt(str);
      ex.printStackTrace();
    }

    printIt("ACFRemoveRegistrationWithBadId passed");
  }

  /*
   * private convenience method to check the description of the ACF's
   * registrationContext.
   */
  private boolean verifyDescriptionSetOK(AuthConfigFactory acf, String regID,
      String desc) {
    boolean bval = false;
    try {
      AuthConfigFactory.RegistrationContext acfReg;
      acfReg = acf.getRegistrationContext(regID);
      if ((acfReg != null) && (desc.equals(acfReg.getDescription()))) {
        bval = true;
      }
    } catch (Exception ex) {
    }
    return bval;
  }

  /*
   * This is one of our more comprehensive tests and it touches on several
   * aspects of registration.
   *
   * This is used to test that we can register a provider using the two
   * different ACF registerProvider() calls. (one call does persistent
   * registration such that the registration will persist between jvm recycles
   * but the other is an in-memory registration that will not persist between
   * jvm recycles. The passed in arg: usePersistRegistration determines which
   * registration method we are testing.
   * 
   * This test does the following: - get current (CTS default) ACF -
   * persistently register the CTS ACP's within *vendors* ACF - register a CTS
   * provider (based on passed in arg of usePersistRegistration) in vendors ACF
   * - JASPIC:SPEC:341 or JASPIC:SPEC:342 - verify our ACP registered okay -
   * re-register the same ACP - verify re-registering ACP did NOT add another
   * regId AND that the newly register ACP re-used same regID AND that the
   * description also got replaced - per assertions of: JASPIC:SPEC:340 and
   * JASPIC:SPEC:343 - unregister our CTS provider - restore CTS default ACF
   *
   */
  public void _ACFRegisterOnlyOneACP(String logFileLocation,
      String providerConfigFilePath, String vendorACFClass,
      boolean usePersistRegistration) throws Exception {

    // (persistently) register providers in vendor factory
    printIt("CommonTests._ACFRegisterOnlyOneACP(): logFileLocation = "
        + logFileLocation);
    printIt("CommonTests._ACFRegisterOnlyOneACP(): providerConfigFilePath = "
        + providerConfigFilePath);
    printIt("CommonTests._ACFRegisterOnlyOneACP(): vendorACFClass = "
        + vendorACFClass);

    AuthConfigFactory vendorACF = CommonUtils.register(logFileLocation,
        providerConfigFilePath, vendorACFClass);
    try {
      if (vendorACF == null) {
        throw new Exception("Failed trying to register ACPs with vendors ACF");
      }

      // this is to ensure we do NOT have a previously registered standalone
      // provider
      // which could be due to a previously incomplete or faulty run. Also due
      // to RI bug
      // we have to add the provider, then remove it to ensure its not there.
      // String clearId = CommonUtils.getRegisteredProviderID(ACPLayer,
      // ACPAppContext, null);
      String clearId = vendorACF.registerConfigProvider(ACPClass, null,
          ACPLayer, ACPAppContext, ACPDesc);
      vendorACF.removeRegistration(clearId);

      // get the list of vendor registered provider ID's
      String[] origRegIDs = vendorACF.getRegistrationIDs(null);
      int defaultLength = origRegIDs.length;

      // dump registered provider info - befor we try to add another ACP
      printIt(
          "Dumping providers which should only contain those in ProviderConfiguration.xml");
      CommonUtils.dumpProviders(vendorACF, origRegIDs);

      // try to register a standalone provider for the 1st time
      String regID1 = null;
      if (usePersistRegistration) {
        // use persistent reigstration - JASPIC:SPEC:341
        regID1 = vendorACF.registerConfigProvider(ACPClass, null, ACPLayer,
            ACPAppContext, ACPDesc);
      } else {
        // use in-memory (ie non-persistent) registration - JASPIC:SPEC:342
        regID1 = vendorACF.registerConfigProvider(null, ACPLayer, ACPAppContext,
            ACPDesc);
      }

      String[] firstRegIDs = vendorACF.getRegistrationIDs(null);
      // now dump registered provider info - after adding 1 more ACP
      printIt(
          "Dumping providers which should show a newly added standalone provider.");
      CommonUtils.dumpProviders(vendorACF, firstRegIDs);

      // verify that our 1st attempt at adding the an ACP did register ok
      if ((regID1 == null) || (firstRegIDs.length <= defaultLength)) {
        printIt("regID1 = " + regID1);
        printIt("firstRegIDs.length = " + firstRegIDs.length
            + "   defaultLength = " + defaultLength);
        String err = "Could not register provider in vendors ACF class.";
        err += "  Problematic provider = " + ACPClass;
        throw new Exception(err);
      } else {
        printIt("a new entry was registered");
      }

      // try to re-register the same standalone provider
      // note: we have different description - which is okay
      // this tests JASPIC:SPEC:340 / JASPIC:SPEC:343
      String changedDesc = "some other description";
      String regID2 = null;
      if (usePersistRegistration) {
        // use persistent registration
        regID2 = vendorACF.registerConfigProvider(ACPClass, null, ACPLayer,
            ACPAppContext, changedDesc);
      } else {
        // use in-memory (ie non-persistent) registration
        regID2 = vendorACF.registerConfigProvider(null, ACPLayer, ACPAppContext,
            changedDesc);
      }

      String[] secondRegIDs = vendorACF.getRegistrationIDs(null);
      // now dump registered provider info - after re-adding the same ACP
      String msg = "Dumping providers which should show the standalone ";
      msg += "provider with slighly different description.";
      printIt(msg);
      CommonUtils.dumpProviders(vendorACF, secondRegIDs);

      // verify that our 2nd attempt at adding the same ACP correctly replaced
      // the
      // same provider (based on appcontext and msg layer) and did NOT add
      // another entry
      if ((regID2 == null) || (firstRegIDs.length != secondRegIDs.length)) {
        String err = "Failure during re-registering of ACP.";
        err += "  Attempts to re-register should replace the provider not add more copies of it.";
        err += "  Problematic provider = " + ACPClass;
        throw new Exception(err);
      } else {
        printIt("the re-registered provider was replaced.");
      }

      // per assertion JASPIC:SPEC:340/JASPIC:SPEC:343 - regID1 and regID2 MUST
      // be the same after a re-registering
      // of same ACP based on javadoc reference for registerConfigProvider
      if (!regID1.equals(regID2)) {
        printIt("regID1 = " + regID1 + "   regID2 = " + regID2);
        String err = "Failure during re-registering of ACP - regID was invalid.";
        err += "  During a re-registration, the registration ID must be the same";
        throw new Exception(err);
      }

      // verify our re-registered provider did replace the ACP and the
      // Description
      // (per assertion JASPIC:SPEC:340 / JASPIC:SPEC:343)
      if (!verifyDescriptionSetOK(vendorACF, regID2, changedDesc)) {
        String err = "Failure during verification of ACP re-registration related ";
        err += "to description overwrite";
        throw new Exception(err);
      }

      // unregister our standalone provider -
      vendorACF.removeRegistration(regID2);

      String[] finalRegIDs = vendorACF.getRegistrationIDs(null);
      // now dump registered provider info - after re-adding the same ACP
      printIt(
          "Dumping providers which should show only providers from ProviderConfiguration.xml.");
      CommonUtils.dumpProviders(vendorACF, finalRegIDs);

    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("AuthConfigFactory ACFRegisterOnlyOneACP passed");
  }

  /*
   * this tests the ability to unregister a provider. First, it must register a
   * provider, then once registered, the ability to unregister is then tested.
   */
  public void _ACFUnregisterACP(String logFileLocation,
      String providerConfigFilePath, String vendorACFClass) throws Exception {
    try {
      // register providers in vendor factory
      AuthConfigFactory vendorACF = CommonUtils.register(logFileLocation,
          providerConfigFilePath, vendorACFClass);
      if (vendorACF == null) {
        throw new Exception("Failed trying to register ACPs with vendors ACF");
      }

      // get list of vendors registered provider ID's
      String[] origRegIDs = vendorACF.getRegistrationIDs(null);

      // dump registered provider info - befor we try to register a provider
      printIt("Dumping providers from ProviderConfiguration.xml.");
      CommonUtils.dumpProviders(vendorACF, origRegIDs);

      // try to register a standalone provider for the 1st time
      String regID1 = vendorACF.registerConfigProvider(null, ACPLayer,
          ACPAppContext, ACPDesc);

      String[] firstRegIDs = vendorACF.getRegistrationIDs(null);
      // now dump registered provider info - after registering a provider
      printIt("Dumping providers to show that a provider was added.");
      CommonUtils.dumpProviders(vendorACF, firstRegIDs);

      // verify that our 1st attempt at adding the an ACP did register ok
      if ((regID1 == null) || (firstRegIDs.length <= origRegIDs.length)) {
        printIt("regID1 = " + regID1);
        printIt("firstRegIDs.length = " + firstRegIDs.length
            + "   origRegIDs.length = " + origRegIDs.length);
        String err = "Error registering provider in vendors ACF class.";
        err += "  Problematic provider = " + ACPClass;
        throw new Exception(err);
      } else {
        printIt("a new entry was registered");
      }

      // now unregister our standalone provider
      boolean wasFound = vendorACF.removeRegistration(regID1);

      String[] finalRegIDs = vendorACF.getRegistrationIDs(null);
      // now dump provider info - after unregistering provider
      printIt(
          "Dumping providers to only show providers in ProviderConfiguration.xml.");
      CommonUtils.dumpProviders(vendorACF, finalRegIDs);

      // verify removeRegistration returned proper boolean
      if (!wasFound) {
        String err = "ERROR: removeRegistration could not find the regID to remove.";
        throw new Exception(err);
      } else {
        printIt("removeRegistration found the regID to remove");
      }

      // verify registry has proper number of items in it
      if (finalRegIDs.length >= firstRegIDs.length) {
        String err = "ERROR: removeRegistration had issue removing registry entry.";
        throw new Exception(err);
      }

      // verify we can't access our regId anymore since it should be
      // unregistered
      if (true == vendorACF.removeRegistration(regID1)) {
        String err = "ERROR: removeRegistration still thinks regID is valid but it should not be";
        throw new Exception(err);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("AuthConfigFactory ACFRegisterOnlyOneACP passed");
  }

  /*
   * basic registration test to verify we can register the CTS providers within
   * the vendors ACF. (registration is persistent rgistration)
   */
  public void _AuthConfigFactoryRegistration(String logFileLocation,
      String providerConfigFilePath, String vendorACFClass) throws Exception {

    try {
      // get default ACF being used
      AuthConfigFactory defaultACF = AuthConfigFactory.getFactory();
      String defaultACFClass = defaultACF.getClass().getName();
      printIt("ACFUnregisterACP.defaultACFClass = " + defaultACFClass);

      // register providers in vendor factory
      AuthConfigFactory vendorACF = CommonUtils.register(logFileLocation,
          providerConfigFilePath, vendorACFClass);

      if (vendorACF == null) {
        throw new Exception("Failed trying to register ACPs with vendors ACF");
      }

      printIt("vendorACFClass = " + vendorACFClass);

      // first verify we were able to change our ACF to use vendors
      if (vendorACFClass.equals(JASPICData.TSSV_ACF)) {
        // ohoh - nothing was changed we still have CTS default ACF
        String str = "FAILURE - Could not set vendors AuthConfigFactory.";
        printIt(str);
        throw new Exception("AuthConfigFactoryRegistration : FAILED");
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }

    printIt("AuthConfigFactoryRegistration passed");
  }

  /*
   * This will test the ability to get the config provider when there are combos
   * of null layers or app contexts. This should return the provider for the
   * specified app context and null layer. This will also verify the ability to
   * register provider a provider that has non-null appcontext and null layer
   * with a listener. Then a verification that the notification works okay is
   * done.
   * 
   * Precedence rules are as follows: 1 - The provider specifically registered
   * for the values passed as the layer and appContext arguments shall be
   * selected. 2 - If no provider is selected according to the preceding rule,
   * the provider specifically registered for the value passed as the appContext
   * argument and for all (that is, null) layers shall be selected. 3 - If no
   * provider is selected according to the preceding rules, the provider
   * specifically registered for the value passed as the layer argument and for
   * all (that is, null) appContexts shall be selected. 4 - If no provider is
   * selected according to the preceding rules, the provider registered for all
   * (that is, null) layers and for all (that is, null) appContexts shall be
   * selected. 5 - If no provider is selected according to the preceding rules,
   * the factory shall terminate its search for a registered provider.
   *
   */
  public void _ACFTestPrecedenceRules(String vendorACFClass,
      boolean isPersistent) throws Exception {

    String layer[] = { null, "phakeMsgLayer1" };
    String context[] = { null, "ctxt1" };
    AuthConfigFactory acf = null;
    String regId = null;
    String[] regIds = new String[layer.length * context.length];

    try {
      if (vendorACFClass != null) {
        acf = CommonUtils.getAuthConfigFactory(vendorACFClass);
      } else {
        // error - we need valid vendor acf
        throw new Exception("ACFTestPrecedenceRules : FAILED");
      }

      /*
       * loop thru and create/register a new acp for each of the
       * msglayer/appcontext combinations. Verify that the call to
       * getConfigProvider() returns appropriate vals - even for case of null
       * msg layer or null appcontext.
       */
      int count = 0;
      for (int ii = 0; ii < layer.length; ii++) {
        for (int jj = 0; jj < context.length; jj++) {
          String desc = null;
          if (isPersistent) {
            // do persistent registration
            desc = "acp1";
            regId = acf.registerConfigProvider(ACPClass, null, layer[ii],
                context[jj], "acp1");
          } else {
            // do in-memory registration
            desc = "acp1";
            TSAuthConfigProviderStandalone acp1;
            acp1 = new TSAuthConfigProviderStandalone((Map) null, null, null,
                "acp1");
            regId = acf.registerConfigProvider(acp1, layer[ii], context[jj],
                "acp1");
          }
          String outStr = "regId=" + regId + " for layer=" + layer[ii] + " "
              + " ctxt=" + context[jj];
          printIt(outStr);

          // test precedence rule 1
          boolean okay = testPrecedenceRule1(acf, regId, layer[ii], context[jj],
              desc);
          if (!okay) {
            printIt("Failed getConfiProvider() precedence rule 1.");
            throw new Exception("ACFTestPrecedenceRules : FAILED");
          }

          regIds[count] = regId;
          count++;
        }
      }

      // test precedence rule 2
      // This is when only specifying appContext and null layer
      // which should return all matching appContexts
      boolean okay = testPrecedenceRule2(acf, isPersistent);
      if (!okay) {
        printIt("Failed getConfiProvider() precedence rule 2.");
        throw new Exception("ACFTestPrecedenceRules : FAILED");
      }

      // test precedence rule 3
      // This is when only specifying msgLayer and null appContext
      // which should return the provider registered with that msgLayer
      okay = testPrecedenceRule3(acf, isPersistent);
      if (!okay) {
        printIt("Failed getConfiProvider() precedence rule 3.");
        throw new Exception("ACFTestPrecedenceRules : FAILED");
      }

      // test precedence rule 4
      // This is when we are trying to retrieve a registered acp
      // that does not have matching context or layer - in which case
      // acp registered with null layer and null context should be
      // returned - if there was an acp registered with null/null)
      okay = testPrecedenceRule4(acf, isPersistent);
      if (!okay) {
        printIt("Failed getConfiProvider() precedence rule 4.");
        throw new Exception("ACFTestPrecedenceRules : FAILED");
      }

      // cleanup ACPs that we registered in this method
      okay = removeRegisteredACPs(acf, regIds);
      if (!okay) {
        // we expect to get warning about unregistering ACPs here
        printIt(
            "Okay to ignore previous WARNING from call to removeRegisteredACPs");
      }

      // test precedence rule 5
      // This is when we are trying to call getCOnfigProvider()
      // with non-registered layer/context values.
      // This verifies that no providers could be found and
      // so factory must terminate its search for registerd acp.
      okay = testPrecedenceRule5(acf, isPersistent);
      if (!okay) {
        printIt("Failed getConfiProvider() precedence rule 5.");
        throw new Exception("ACFTestPrecedenceRules : FAILED");
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }
    printIt("ACFTestPrecedenceRules passed");
  }

  /*
   * test is verifying we can get the correct provider back with the specifying
   * of a non-existing msg layuer and context
   */
  private boolean testPrecedenceRule5(AuthConfigFactory acf,
      boolean isPersistent) throws Exception {
    boolean rval = true;
    String MSG_LAYER = "phakeMsgLayer5"; // non-existant msg layer
    String APP_CTXT = "phakeContext5"; // non-existant context

    AuthConfigProvider gottenAcp = acf.getConfigProvider(MSG_LAYER, APP_CTXT,
        null);
    if (gottenAcp != null) {
      printIt("Error - precedence rule 5 failure");
      String str = ((TSAuthConfigProviderStandalone) gottenAcp)
          .getDescription();
      printIt("ERROR - precedence rule 5 returned acp desc = " + str);
      rval = false;
    }

    return rval;
  }

  /*
   * This will register two ACP's using same class but differ layer and
   * contexts. One acp with have null/null and the other acp will have non-null
   * layer/context. This then verifies it gets the correct ACP back (based on
   * description).
   */
  private boolean testPrecedenceRule4(AuthConfigFactory acf,
      boolean isPersistent) throws Exception {
    boolean rval = true;
    TSAuthConfigProviderStandalone acp4, acp4b;
    acp4 = new TSAuthConfigProviderStandalone((Map) null, null, null, "acp4");
    acp4b = new TSAuthConfigProviderStandalone((Map) null, null, null, "acp4b");
    String layer = null;
    String context = null;
    String layer2 = "layer4";
    String context2 = "context 4";
    String rId1 = null, rId2 = null;
    Properties props;
    String strDesc1 = "acp4";
    String strDesc2 = "acp4b";

    if (isPersistent) {
      // do persistent registration
      props = setDescriptionProp(strDesc1);
      rId1 = acf.registerConfigProvider(ACPClass, props, layer, context,
          strDesc1);
      props = setDescriptionProp(strDesc2);
      rId2 = acf.registerConfigProvider(ACPClass, props, layer2, context2,
          strDesc2);
    } else {
      // do in-memory registration
      rId1 = acf.registerConfigProvider(acp4, layer, context, strDesc1);
      rId2 = acf.registerConfigProvider(acp4b, layer2, context2, strDesc2);
    }

    AuthConfigProvider gottenAcp = acf.getConfigProvider(layer, context, null);
    if (gottenAcp == null) {
      printIt("Error - precedence rule 4 failure");
      return false;
    }

    // make sure we get back provider registerd with null/null
    AuthConfigFactory.RegistrationContext rc = acf.getRegistrationContext(rId1);
    String str = rc.getDescription();
    String str2 = ((TSAuthConfigProviderStandalone) gottenAcp).getDescription();
    boolean bMatch = (str != null) && (str.equals(strDesc1)) && (str2 != null)
        && (str2.equals(strDesc1));
    if (!bMatch) {
      String err = "ERROR - precedence rule 4 returned wrong acp";
      err += "  rc.getDescription() = " + str + "gottenAcp.desc=" + str2;
      printIt(err);
      rval = false;
    }

    // also - test calling with invalid layer/context gets the provider
    // that was registered with null layer and null context
    // This should return provider registered with null/null
    AuthConfigProvider gottenAcp2 = acf.getConfigProvider("nonExistantLayer",
        "nonExistantContext", null);
    if (gottenAcp2 == null) {
      printIt("Error - precedence rule 4b failure");
      rval = false;
    }

    // since gottenAcp2 should have returned the provider that was
    // registered with null/null which should have desc = strDesc1
    if (gottenAcp2 != null) {
      str = ((TSAuthConfigProviderStandalone) gottenAcp2).getDescription();
    } else {
      String err = "ERROR - unexpected null ACP encountered.  (gottenAcp2 == null)";
      printIt(err);
      rval = false;
    }

    // call to getRegContext should return desc that corresponds
    // to the acp we registered with rId2
    rc = acf.getRegistrationContext(rId2);
    str2 = rc.getDescription(); // should match strDesc2

    bMatch = (str2 != null) && (str2.equals(strDesc2)) && (str != null)
        && (str.equals(strDesc1));
    if (!bMatch) {
      String err = "ERROR - precedence rule 4 (2nd check) returned wrong acp";
      err += "  rc.getDescription() = " + str2 + "gottenAcp2.description="
          + str;
      printIt(err);
      rval = false;
    }

    // do some cleanup
    acf.removeRegistration(rId1);
    acf.removeRegistration(rId2);

    return rval;
  }

  /*
   * test is verifying we can get the correct provider back with the specifying
   * of the unique msg layer and null/no appContext.
   */
  private boolean testPrecedenceRule3(AuthConfigFactory acf,
      boolean isPersistent) throws Exception {
    boolean rval = true;
    TSAuthConfigProviderStandalone acp3, acp3a;
    String MSG_LAYER = "phakeMsgLayer1"; // unique msg layer
    String APP_CTXT = "context3";
    String rId = null;
    String rId2 = null;
    Properties props;
    String strDesc1 = "acp3";
    String strDesc2 = "acp3a";

    acp3 = new TSAuthConfigProviderStandalone((Map) null, null, null, strDesc1);
    acp3a = new TSAuthConfigProviderStandalone((Map) null, null, null,
        strDesc2);

    if (isPersistent) {
      // do persistent registration
      props = setDescriptionProp(strDesc1);
      rId = acf.registerConfigProvider(ACPClass, props, MSG_LAYER, null,
          strDesc1);
      props = setDescriptionProp(strDesc2);
      rId2 = acf.registerConfigProvider(ACPClass, props, MSG_LAYER, APP_CTXT,
          strDesc2);
    } else {
      // do in-memory registration
      rId = acf.registerConfigProvider(acp3, MSG_LAYER, null, strDesc1);
      rId2 = acf.registerConfigProvider(acp3a, MSG_LAYER, APP_CTXT, strDesc2);
    }

    // we should get back acp registered with matching layer and null appcontext
    AuthConfigProvider gottenAcp = acf.getConfigProvider(MSG_LAYER, null, null);
    if (gottenAcp == null) {
      printIt("Error - precedence rule 3 failure");
      return false;
    }

    // make sure we didnt accidentally get back wrong provider
    // we should get back acp with description = strDesc1
    AuthConfigFactory.RegistrationContext rc = acf.getRegistrationContext(rId);
    String str = rc.getDescription();
    String str2 = ((TSAuthConfigProviderStandalone) gottenAcp).getDescription();
    boolean bMatch = (str != null) && (str.equals(strDesc1)) && (str2 != null)
        && (str2.equals(strDesc1));
    if (!bMatch) {
      printIt("ERROR - precedence rule 3 returned wrong acp");
      rval = false;
    }

    // do some cleanup
    acf.removeRegistration(rId);
    acf.removeRegistration(rId2);

    return rval;
  }

  /*
   * This verifies we can get the correct provider back with the specifying of
   * the unique appContext and null/no msglayer.
   */
  private boolean testPrecedenceRule2(AuthConfigFactory acf,
      boolean isPersistent) throws Exception {
    boolean rval = true;
    TSAuthConfigProviderStandalone acp2, acp2a;
    String rId = null;
    String rId2 = null;
    Properties props;
    String strDesc1 = "acp2";
    String strDesc2 = "acp2a";

    acp2 = new TSAuthConfigProviderStandalone((Map) null, null, null, strDesc1);
    acp2a = new TSAuthConfigProviderStandalone((Map) null, null, null,
        strDesc2);

    if (isPersistent) {
      // do persistent registration
      props = setDescriptionProp(strDesc1);
      rId = acf.registerConfigProvider(ACPClass, props, null, "ctxt2",
          strDesc1);
      props = setDescriptionProp(strDesc2);
      rId2 = acf.registerConfigProvider(ACPClass, props, "nonexistLayer",
          "ctxt2", strDesc2);
    } else {
      // do in-memory registration
      rId = acf.registerConfigProvider(acp2, null, "ctxt2", strDesc1);
      rId2 = acf.registerConfigProvider(acp2a, "nonexistLayer", "ctxt2",
          strDesc2);
    }

    AuthConfigProvider gottenAcp = acf.getConfigProvider(null, "ctxt2", null);
    if (gottenAcp == null) {
      printIt("Error - precedence rule 2 failure");
      return false;
    }

    // make sure we didnt accidentally get back wrong provider (where strDesc1
    // is description for correct acp and anything else is wrong acp
    AuthConfigFactory.RegistrationContext rc = acf.getRegistrationContext(rId);
    String str = rc.getDescription();
    String str2 = ((TSAuthConfigProviderStandalone) gottenAcp).getDescription();
    boolean bMatch = (str != null) && (str.equals(strDesc1)) && (str2 != null)
        && (str2.equals(strDesc1));
    if (!bMatch) {
      String err = "ERROR - precedence rule 2 returned wrong acp ";
      err += "  rc.getDescription()= " + str;
      err += "  gottenAcp.getDescription()=" + str2;
      printIt(err);
      rval = false;
    }

    // do some cleanup
    acf.removeRegistration(rId);
    acf.removeRegistration(rId2);

    return rval;
  }

  private boolean testPrecedenceRule1(AuthConfigFactory acf, String regId,
      String layer, String context, String desc) throws Exception {
    boolean rval = true;

    AuthConfigProvider gottenAcp;
    gottenAcp = acf.getConfigProvider(layer, context, null);
    if (gottenAcp == null) {
      printIt("Error - no ACP found in call to getConfigProvider() with layer="
          + layer + "and context=" + context);
      rval = false;
    } else {
      AuthConfigFactory.RegistrationContext rc;
      rc = acf.getRegistrationContext(regId);
      String str = rc.getDescription();
      boolean bMatch = (str != null) && (str.equals(desc));
      if (!bMatch) {
        printIt("ERROR getConfigProvider() returned wrong acp=" + str);
        rval = false;
      }

      // next, confirm the getConfigProvider() above returned a valid
      // regId that matches current layer & context info
      boolean ok = verifyGetConfigProvider(acf, gottenAcp, regId, layer,
          context);
      if (!ok) {
        printIt("ERROR no registration found for regIds=" + regId);
        rval = false;
      }
    }

    return rval;
  }

  /*
   * This is a convenience method that sets a property which can be used by the
   * TSAuthConfigProviderStandalone class which looks for this property during
   * instantiation by a persistent registration. Said differently, we use this
   * property to set the description when we are creating a new/persistent ACP
   * registration.
   */
  private Properties setDescriptionProp(String val) {
    Properties props = new Properties();
    props.setProperty(TSAuthConfigProviderStandalone.DESC_KEY, val);
    return props;
  }

  /*
   * This is used to verify the RegistrationContext value for isPersistent()
   * matches what we expect to be set.
   * 
   */
  public boolean verifyRegContextPersitentVal(AuthConfigFactory acf,
      String regId, boolean expectedPersistent) throws Exception {
    boolean rval = false;

    AuthConfigFactory.RegistrationContext rc;
    rc = acf.getRegistrationContext(regId);

    if (rc == null) {
      // error - cant find context for regId but should be able to
      printIt(
          "ERROR could not get RegistrationContext in verifyRegContextPersitentVal");
      return false;
    }

    if (rc.isPersistent() == expectedPersistent) {
      rval = true;
    }

    return rval;
  }

  /*
   * This is used to verify that the passed in regId contains the expected msg
   * layer and appcontext values. It also verifies that the passed in regId
   * corresponds with the passed in acp.
   */
  public boolean verifyGetConfigProvider(AuthConfigFactory acf,
      AuthConfigProvider acp, String regId, String expectedLayer,
      String expectedContext) throws Exception {
    boolean rval = true;
    boolean bLayer = false;
    boolean bContext = false;

    AuthConfigFactory.RegistrationContext rc;
    rc = acf.getRegistrationContext(regId);

    if (rc == null) {
      // error - cant find context for regId but should be able to
      printIt(
          "ERROR could not get RegistrationContext in verifyGetConfigProvider");
      return false;
    }

    if (((expectedLayer != null) && expectedLayer.equals(rc.getMessageLayer()))
        || ((expectedLayer == null) && (rc.getMessageLayer() == null))) {
      bLayer = true;
    }

    if (((expectedContext != null)
        && expectedContext.equals(rc.getAppContext()))
        || ((expectedContext == null) && (rc.getAppContext() == null))) {
      bContext = true;
    }

    // make sure our regId is registered for our acp
    String[] regIds = acf.getRegistrationIDs(acp);
    boolean regIdMatch = false;
    for (int ii = 0; ii < regIds.length; ii++) {
      if ((regId != null) && (regId.equals(regIds[ii]))) {
        regIdMatch = true;
      }
    }

    if (bLayer && bContext && regIdMatch) {
      rval = true;
    }

    return rval;
  }

  /*
   * This will test the ability to notify listeners for acps that are registered
   * with a combo of profile layers and app contexts. This assists with testing:
   * JASPIC:SPEC:336, JASPIC:SPEC:338, JASPIC:SPEC:339, JASPIC:SPEC:344,
   * JASPIC:SPEC:337
   */
  public void _ACFTestNotifyOnUnReg(String vendorACFClass, boolean isPersistent)
      throws Exception {

    String profileLayer[] = { null, "HttpServlet", "phakeProfile" };
    String context[] = { null, "ctxt2", "ctxt3" };
    AuthConfigFactory acf = null;

    try {
      if (vendorACFClass != null) {
        acf = CommonUtils.getAuthConfigFactory(vendorACFClass);
      } else {
        // error - we need valid vendor acf
        throw new Exception("ACFTestNotifyOnUnReg : FAILED");
      }

      // create/register new providers for each layer-context combination
      String[] regIds = createAndRegisterACPs(acf, isPersistent, profileLayer,
          context);

      // add listeners for each layer-context combo to ACPs - JASPIC:SPEC:337
      TSRegistrationListener tlistener[] = addListenersToACP(acf, profileLayer,
          context);

      // at this point, our listeners likely have not have been called,
      // but to be sure - lets reset our listeners 'notified' flag
      for (int ii = 0; ii < tlistener.length; ii++) {
        tlistener[ii].resetNotifyFlag();
      }

      // assert JASPIC:SPEC:344
      // remove registered ACPs using the passed in list of regIds. This
      // must generate notifications to listeners associated with those ACPs
      boolean ok = removeRegisteredACPs(acf, regIds);
      if (!ok) {
        // one of registered ids could not be found and/or unregistered
        throw new Exception("ACFTestNotifyOnUnReg : FAILED");
      }

      // verify notifications occurred during unregistration JASPIC:SPEC:338
      ok = verifyNotifications(acf, tlistener, profileLayer, context);
      if (!ok) {
        throw new Exception("ACFTestNotifyOnUnReg : FAILED");
      }

      // do some cleanup and verify if it went okay
      ok = detachAllListeners(acf, tlistener, profileLayer, context);
      if (!ok) {
        // got error during detach
        throw new Exception("ACFTestNotifyOnUnReg : FAILED");
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      // restore original (CTS) factory class
      CommonUtils.resetDefaultACF();
    }
    printIt("ACFTestNotifyOnUnReg passed");
  }

  /*
   * This verifies notifications for the combinations of passed in layer and
   * contexts. returns false if an error is encountered.
   */
  public boolean verifyNotifications(AuthConfigFactory acf,
      TSRegistrationListener[] tlistener, String[] layer, String[] context)
      throws Exception {
    boolean bval = true;

    // verify notifications occurred during unregistration
    for (int kk = 0; kk < tlistener.length; kk++) {
      for (int ii = 0; ii < layer.length; ii++) {
        for (int jj = 0; jj < context.length; jj++) {
          boolean bPassed = false;
          bPassed = tlistener[kk].check(layer[ii], context[jj]);
          String str = "layer=" + layer[ii] + "  and context=" + context[jj];
          if (!bPassed) {
            printIt(" Failed listener for: " + str);
            bval = false;
          }
        }
      }
    }

    return bval;
  }

  /*
   * This detaches all listeners and also checks that the call to detachListenre
   * never returns null. used in testing assert JASPIC:SPEC:339 returns false if
   * an error is encountered.
   */
  public boolean detachAllListeners(AuthConfigFactory acf,
      TSRegistrationListener[] tlistener, String[] layer, String[] context)
      throws Exception {
    boolean bval = true;

    for (int kk = 0; kk < tlistener.length; kk++) {
      for (int ii = 0; ii < layer.length; ii++) {
        for (int jj = 0; jj < context.length; jj++) {
          // now, detach listeners
          String rval[] = acf.detachListener(tlistener[kk], layer[ii],
              context[jj]);
          if (rval == null) {
            // should NEVER be null (per api doc)
            String str = "layer=" + layer[ii] + "  and context=" + context[jj];
            printIt("Failed detachListener for: " + str);
            bval = false;
          }
        }
      }
    }

    return bval;
  }

  /*
   * This takes the passed in list or ACP regIds and unregisteres those ACp's
   * from the passed in ACF. This returns false if a registration is deemed
   * invalid and cant be removed.
   */
  public boolean removeRegisteredACPs(AuthConfigFactory acf, String[] regIds)
      throws Exception {

    boolean bval = true;

    // remove registrations with listeners - should call notify on listeners
    for (int ii = 0; ii < regIds.length; ii++) {
      printIt(" REMOVING PROVIDERS TO INVOKE NOTIFY() for regId=" + regIds[ii]);
      boolean result = false;

      result = acf.removeRegistration(regIds[ii]);
      if (!result) {
        printIt("WARNING: no registration found for regIds=" + regIds[ii]);
        bval = false;
      }
    }

    return bval;
  }

  /*
   * This method will create new listeners for the passed in layer/context
   * combinations. After creating each listener, it is added to the ACP that
   * matches the context/layer for that new listener. This returns an array or
   * newly created listeners that were added to ACP's.
   */
  public TSRegistrationListener[] addListenersToACP(AuthConfigFactory acf,
      String[] layer, String[] context) throws Exception {

    int theSize = layer.length * context.length; // num listeners
    TSRegistrationListener[] tlistener = new TSRegistrationListener[theSize];
    AuthConfigProvider tmpAcp[] = new AuthConfigProvider[theSize];

    // Create listeners, then add them to ACPs
    int count = 0;
    for (int ii = 0; ii < layer.length; ii++) {
      for (int jj = 0; jj < context.length; jj++) {
        tlistener[count] = new TSRegistrationListener(layer[ii], context[jj]);
        tmpAcp[ii] = acf.getConfigProvider(layer[ii], context[jj],
            tlistener[count]);
        if (tmpAcp[ii] == null) {
          printIt(" no ACP found in call to getConfigProvider() with layer="
              + layer[ii] + "and context=" + context[jj]);
        }
        count++;
      }
    }

    return tlistener;
  }

  /*
   * This method creates and register a new ACP for each combination of the
   * passed in layer and contexts. So if there are 3 layers and 3 contexts
   * passed into this method, there will be 3 * 3 = 9 different ACPs registered
   * in the passed in ACF. use the passed in "isPersistent" flag to specify if
   * we want our registeration to be persistent or in-memory only type of
   * registration.
   */
  public String[] createAndRegisterACPs(AuthConfigFactory acf,
      boolean isPersistent, String[] layer, String[] context) throws Exception {

    TSAuthConfigProviderStandalone acp;
    String[] regIds = new String[layer.length * context.length];

    try {
      // register new acp's
      int count = 0;
      for (int ii = 0; ii < layer.length; ii++) {
        for (int jj = 0; jj < context.length; jj++) {
          String desc = ACPDesc + " layer=" + ii + " " + " ctxt=" + jj;
          if (isPersistent) {
            // do persistent registration
            regIds[count] = acf.registerConfigProvider(ACPClass, null,
                layer[ii], context[jj], desc);
          } else {
            // do in-memory registration
            acp = new TSAuthConfigProviderStandalone((Map) null, null);
            regIds[count] = acf.registerConfigProvider(acp, layer[ii],
                context[jj], desc);
          }

          // JASPIC:SPEC:336
          boolean bb = verifyRegContextPersitentVal(acf, regIds[count],
              isPersistent);
          if (!bb) {
            // failure - our reg context did not have setting that matches the
            // type of registration we used
            String ss = new String(
                "ERROR - RegistrationContext.isPersistent() mismatch");
            printIt(ss);
            throw new Exception(ss);
          }

          String str = "regId=" + regIds[count] + " for layer=" + ii + " "
              + " ctxt=" + jj;
          printIt(str);
          count++;
        }
      }

    } catch (Exception ex) {
      printIt("Got Exception in createAndRegisterACPs()");
      throw ex;
    }

    return regIds;
  }

  public void printIt(String str) {
    if (out == null) {
      System.out.println(str);
    } else {
      // lets print them both so we get info in server.log and client side log
      out.println(str);
      System.out.println(str);
    }
  }

  // --------------------------------------------------------------------
  // Nested Class: MyRegistrationListener
  //
  public class MyRegistrationListener implements RegistrationListener {
    String theProfileLayer = null;

    String appContext = null;

    boolean notified = false;

    public MyRegistrationListener() {
    }

    public MyRegistrationListener(String theProfileLayer, String appContext) {
      this.theProfileLayer = theProfileLayer;
      this.appContext = appContext;
    }

    public String getProfileLayer() {
      return this.theProfileLayer;
    }

    public void setProfileLayer(String val) {
      this.theProfileLayer = val;
    }

    public String getAppContext() {
      return this.appContext;
    }

    public void setAppContext(String val) {
      this.appContext = val;
    }

    public void resetNotifyFlag() {
      notified = false;
    }

    public boolean notified() {
      return notified;
    }

    public boolean check(String layer, String context) {
      return true;
    }

    @Override
    public void notify(String layer, String context) {
      boolean bLayersMatch = (theProfileLayer == layer)
          || theProfileLayer.equals(layer);
      boolean bContextsMatch = (appContext == context)
          || appContext.equals(context);

      if (bLayersMatch && bContextsMatch) {
        // successful notification
      } else {
        // error - notify had problem
      }
    }
  } // end Nested Class MyRegistrationListener

} // end Outer Class CommonTests
