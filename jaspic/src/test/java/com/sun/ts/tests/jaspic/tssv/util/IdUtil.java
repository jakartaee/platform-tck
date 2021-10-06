/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.tssv.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

/**
 *
 * @author Raja Perumal
 */
public class IdUtil {

  private static ProviderConfigurationXMLFileProcessor configFileProcessor = null;

  private static TSLogger logger = null;

  public IdUtil() {
    initializeTSLogger();
  }

  /*
   * 1) Reads the provider configuration XML file, 2) Examines providers 3) If
   * possible return a non-null ApplicationContextId for a given layer 4) If no
   * suitable ApplicationContextId found it returns an empty string
   */
  public static String getAppContextId(String msgLayer) {
    String providerConfigFileLocation = System
        .getProperty("provider.configuration.file");

    try {
      // Given the provider configuration xml file
      // This reader parses the xml file and stores the configuration
      // entries as a collection.
      configFileProcessor = new ProviderConfigurationXMLFileProcessor(
          providerConfigFileLocation);

      // Retrieve the ProviderConfigurationEntries collection
      Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = configFileProcessor
          .getProviderConfigurationEntriesCollection();

      ProviderConfigurationEntry pce = null;
      String appContextId = null;

      Iterator<ProviderConfigurationEntry> iterator = providerConfigurationEntriesCollection
          .iterator();
      while (iterator.hasNext()) {
        // obtain each ProviderConfigurationEntry and register it
        // with TSAuthConfigFactory
        pce = (ProviderConfigurationEntry) iterator.next();

        if (pce != null) {
          appContextId = pce.getApplicationContextId();
          String pceMsgLayer = pce.getMessageLayer();

          if (msgLayer.equalsIgnoreCase(pceMsgLayer)
              && !appContextId.equalsIgnoreCase("null")) {
            return appContextId;
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      if ((e.getMessage() != null) && (!e.getMessage().equals(""))) {
        logger.log(Level.SEVERE, e.getMessage());
      } else {
        logger.log(Level.SEVERE, "Error in getAppContextId()");
      }
    }

    // if here, we must have had an app contextId that was null or
    // unidentifyable msgLayer
    return "";
  }

  private static void initializeTSLogger() {
    String logFileLocation = null;
    if (logger != null) {
      return;
    } else {
      try {
        logFileLocation = System.getProperty("log.file.location");
        System.out.println("logFileLocation = " + logFileLocation);
        if (logFileLocation != null) {
          logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
          boolean appendMode = true;

          // create a new file
          TSFileHandler fileHandler = new TSFileHandler(
              logFileLocation + "/" + JASPICData.DEFAULT_LOG_FILE, appendMode);
          fileHandler.setFormatter(new TSXMLFormatter());
          logger.addHandler(fileHandler);
        } else {
          throw new RuntimeException("log.file.location not set");
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("TSLogger Initialization failed", e);
      }
    }
  }
}
