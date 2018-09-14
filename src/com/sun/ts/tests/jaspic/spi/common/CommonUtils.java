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

import java.io.Serializable;
import java.util.*;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;

import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationEntry;
import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationXMLFileProcessor;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;

/**
 *
 * @author Oracle
 */
public class CommonUtils implements Serializable {

  public CommonUtils() {

  }

  public static AuthConfigFactory register(String logFileLocation,
      String providerConfigurationFileLocation,
      String vendorAuthConfigFactoryClass) {

    AuthConfigFactory acf = null;
    try {

      printVerticalIndent();

      // Get an instance of Vendor's AuthConfigFactory
      AuthConfigFactory vFactory = getAuthConfigFactory(
          vendorAuthConfigFactoryClass);

      // Set vendor's AuthConfigFactory
      AuthConfigFactory.setFactory(vFactory);

      // Get system default AuthConfigFactory
      acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        printIt("Default AuthConfigFactory class name = "
            + acf.getClass().getName());
      } else {
        printError("Default AuthConfigFactory is null"
            + " can't register TestSuite Providers with null");
        return null;
      }

      /**
       * Read the ProviderConfiguration XML file This file contains the list of
       * providers that needs to be loaded by the vendor's default
       * AuthConfigFactory
       */
      Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = readProviderConfigurationXMLFile(
          providerConfigurationFileLocation);

      ProviderConfigurationEntry pce = null;

      printVerticalIndent();
      Iterator<ProviderConfigurationEntry> iterator = providerConfigurationEntriesCollection
          .iterator();
      while (iterator.hasNext()) {
        // obtain each ProviderConfigurationEntry and register it
        // with vendor's default AuthConfigFactory
        pce = (ProviderConfigurationEntry) iterator.next();

        if (pce != null) {
          printIt("pce.getProviderClassName() = " + pce.getProviderClassName());
          printIt("pce.getMessageLayer() = " + pce.getMessageLayer());
          printIt("pce.getApplicationContextId() = "
              + pce.getApplicationContextId());
          printIt("pce.getRegistrationDescription() = "
              + pce.getRegistrationDescription());
          if (pce.getProperties() != null) {
            printIt("pce.getProperties.toString() = "
                + pce.getProperties().toString());
          } else {
            printIt("pce.getProperties() = null");
          }

          printIt(
              "Registering Provider " + pce.getProviderClassName() + " ...");
          Properties newProps = getCleanACPProps(pce.getProperties());
          acf.registerConfigProvider(pce.getProviderClassName(), newProps,
              pce.getMessageLayer(), pce.getApplicationContextId(),
              pce.getRegistrationDescription());
          printIt("Registration Successful");
        } else {
          printIt("WARNING:  pce was null and probably should not have been.");
        }
      }
      printVerticalIndent();

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      printIt(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      printIt("Error Registering TestSuite AuthConfig Providers");
      e.printStackTrace();
    }

    return acf;
  }

  private static Properties getCleanACPProps(Properties origProps) {

    if (origProps == null) {
      return null;
    }

    Properties props = new Properties();

    // loop thru passed in props and remove anything that is
    // not of type String since only Strings are allowed in our
    // calls to registerConfigProvider()
    Enumeration eProps = origProps.keys();
    while (eProps.hasMoreElements()) {
      String key = (String) eProps.nextElement();
      if (key != null) {
        Object val = origProps.get(key);
        if ((val != null) && ((val instanceof java.lang.String))) {
          // we found entry that is not String so remove it
          props.put(key, (String) val);
          printIt("added key=" + key + " with value = " + val);
        } else {
          printIt("found non-string value for key=" + key);
        }
      }
    }

    return props;
  }

  /**
   * This method instantiates and ruturns a AuthConfigFactory based on the
   * specified className
   */
  public static AuthConfigFactory getAuthConfigFactory(String className) {

    AuthConfigFactory vFactory = null;

    if (className != null) {
      try {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = Class.forName(className, true, loader);
        vFactory = (AuthConfigFactory) clazz.newInstance();
        printIt("Instantiated Vendor's AuthConfigFactory");
      } catch (Exception e) {
        printIt("Error instantiating vendor's " + "AuthConfigFactory class :"
            + className);
        e.printStackTrace();
      }
    }

    return vFactory;
  }

  /**
   * This method resets the default ACF.
   * 
   */
  public static void resetDefaultACF() throws Exception {
    try {
      printIt("resetting ACF back to CTS default:  " + JASPICData.TSSV_ACF);
      AuthConfigFactory origAcf = getAuthConfigFactory(JASPICData.TSSV_ACF);
      AuthConfigFactory.setFactory(origAcf);
    } catch (Exception ex) {
      printIt(
          "Exception while trying to restore original factory class in ACFSwitchFactorys");
      throw ex;
    }
  }

  /*
   * Read the provider configuration XML file and registers each provider with
   * Vendor's default AuthConfigFactory
   */
  public static Collection<ProviderConfigurationEntry> readProviderConfigurationXMLFile(
      String acpCfgFile) {
    Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = null;

    printIt("Reading TestSuite Providers from :" + acpCfgFile);
    try {
      // Given the provider configuration xml file
      // This reader parses the xml file and stores the configuration
      // entries as a collection.
      ProviderConfigurationXMLFileProcessor configFileProcessor = new ProviderConfigurationXMLFileProcessor(
          acpCfgFile);

      // Retrieve the ProviderConfigurationEntries collection
      providerConfigurationEntriesCollection = configFileProcessor
          .getProviderConfigurationEntriesCollection();

      printIt("TestSuite Providers read successfully "
          + "from ProviderConfiguration.xml file");

      return providerConfigurationEntriesCollection;

    } catch (Exception e) {
      printIt("Error loading Providers");
      e.printStackTrace();
    }
    return null;

  }

  public static void printVerticalIndent() {
    printIt("**********************************************************");
    printIt("\n");
  }

  public static void dumpProviders(AuthConfigFactory acf, String[] regIDs) {

    printIt("****************************************************************");
    printIt("*******  Dumping AuthConfigFactory registered providers  *******");
    for (int ii = 0; ii < regIDs.length; ii++) {
      if (regIDs[ii] != null) {
        printIt("registrationID[" + ii + "] = " + regIDs[ii]);
        AuthConfigFactory.RegistrationContext rc = acf
            .getRegistrationContext(regIDs[ii]);
        printIt("AppContext = " + rc.getAppContext());
        printIt("Message layer = " + rc.getMessageLayer());
        printIt("Description = " + rc.getDescription());
        printIt("isPersistent = " + rc.isPersistent());
      }
    }
    printIt("****************************************************************");
    printIt(" ");
  }

  public static String getRegisteredProviderID(String msgLayer,
      String appContext, RegistrationListener rl) {
    String regID = null;

    AuthConfigFactory acf = AuthConfigFactory.getFactory();
    AuthConfigProvider acp = acf.getConfigProvider(msgLayer, appContext, rl);

    String[] allIDs = acf.getRegistrationIDs(acp);
    for (int ii = 0; ii < allIDs.length; ii++) {
      AuthConfigFactory.RegistrationContext rc = acf
          .getRegistrationContext(allIDs[ii]);
      if (rc != null) {
        String mlayer = rc.getMessageLayer();
        String appctxt = rc.getAppContext();
        if (msgLayer.equals(mlayer) && appContext.equals(appctxt)) {
          regID = allIDs[ii];
          break;
        }
      } else {
        printIt("no registrationContext for regID = " + allIDs[ii]);
      }
    }

    return regID;
  }

  public static void printIt(String str) {
    System.out.println(str);
  }

  public static void printError(String str) {
    System.err.println(str);
  }

}
