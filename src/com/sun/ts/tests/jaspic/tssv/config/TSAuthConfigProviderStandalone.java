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

package com.sun.ts.tests.jaspic.tssv.config;

import com.sun.ts.tests.jaspic.tssv.util.TSFileHandler;
import com.sun.ts.tests.jaspic.tssv.util.TSXMLFormatter;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.AuthException;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.ClientAuthConfig;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;

/**
 * This class is an implementation of the AuthConfigProvider for use by the core
 * tests. This is used to support the scenario when a profile other than
 * Servlet/SOAP is being defined and used with JSR 196.
 *
 * At the time of this test creation, the 2 main supported profiles are SOAP and
 * Servlet. But it is very possible that someone may use JSR-196 to define their
 * own profiles. For that case, we need to have something we can use for the
 * core tests besides SOAP/Servlet tests. This is used to support that case.
 *
 * @author Sun Microsystems
 *
 */
public class TSAuthConfigProviderStandalone
    implements javax.security.auth.message.config.AuthConfigProvider {

  // this is a default string used for Persistent Registration description
  public static String DESC_KEY = "description_key";

  private static TSLogger logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);

  private HashMap serverAuthConfigMap = new HashMap();

  private static Map properties = null;

  private String description = null;

  // This will be called when a vendor registers TSAuthConfigProviderStandalone
  public TSAuthConfigProviderStandalone(Map props, AuthConfigFactory factory) {

    this(props, null, factory, null);

    logger.log(Level.INFO,
        "invoked TSAuthConfigProviderStandalone() constructor(2 args)");
  }

  /**
   * This constructor takes a TSLogger instance as a param.
   */
  public TSAuthConfigProviderStandalone(Map props, TSLogger tsLogger,
      AuthConfigFactory factory) {

    this(props, tsLogger, factory, null);

    logger.log(Level.INFO,
        "invoked TSAuthConfigProviderStandalone() constructor(3 args)");
  }

  /**
   * This constructor takes a TSLogger instance as a param.
   */
  public TSAuthConfigProviderStandalone(Map props, TSLogger tsLogger,
      AuthConfigFactory factory, String description) {
    properties = props;

    // For self registration
    if (factory != null) {
      factory.registerConfigProvider(this, null, null,
          "TSAuthConfigProviderStandalone self registration");
    }

    if (tsLogger != null) {
      this.logger = tsLogger;
    } else {
      initializeTSLogger();
    }

    if (description != null) {
      // this is usually the case for in-memory registration
      this.description = description;
    } else {
      // this is usually the case for persistent registration
      this.description = getDescFromProps(props);
      if (this.description == null) {
        this.description = "PERSIST_DEFAULT";
      }
    }

    logger.log(Level.INFO,
        "invoked TSAuthConfigProviderStandalone() constructor(4 args)");
  }

  private String getDescFromProps(Map props) {

    String strDesc = null;
    if (props != null) {
      strDesc = (String) props.get(DESC_KEY);
    }

    return strDesc;
  }

  /**
   * Get an instance of ClientAuthConfig from this provider.
   *
   * <p>
   * The implementation of this method returns a ClientAuthConfig instance that
   * describes the configuration of ClientAuthModules at a given message layer,
   * and for use in an identified application context.
   *
   * @param layer
   *          a String identifying the message layer for the returned
   *          ClientAuthConfig object. This argument must not be null.
   *
   * @param appContext
   *          a String that identifies the messaging context for the returned
   *          ClientAuthConfig object. This argument must not be null.
   *
   * @param handler
   *          a CallbackHandler to be passed to the ClientAuthModules
   *          encapsulated by ClientAuthContext objects derived from the
   *          returned ClientAuthConfig. This argument may be null, in which
   *          case the implementation may assign a default handler to the
   *          configuration.
   *
   * @return a ClientAuthConfig Object that describes the configuration of
   *         ClientAuthModules at the message layer and messaging context
   *         identified by the layer and appContext arguments. This method does
   *         not return null.
   *
   * @exception AuthException
   *              if this provider does not support the assignment of a default
   *              CallbackHandler to the returned ClientAuthConfig.
   *
   * @exception SecurityException
   *              if the caller does not have permission to retrieve the
   *              configuration.
   *
   *              The CallbackHandler assigned to the configuration must support
   *              the Callback objects required to be supported by the profile
   *              of this specification being followed by the messaging runtime.
   *              The CallbackHandler instance must be initialized with any
   *              application context needed to process the required callbacks
   *              on behalf of the corresponding application.
   */
  public ClientAuthConfig getClientAuthConfig(String layer, String appContext,
      CallbackHandler handler) throws AuthException {

    logger.log(Level.INFO,
        "WARNING:  shouldnt get into ClientAuthConfig() for servlet profile");

    // shouldnt get in here for servlet profile
    return (ClientAuthConfig) null;
  }

  /**
   * Get an instance of ServerAuthConfig from this provider.
   *
   * <p>
   * The implementation of this method returns a ServerAuthConfig instance that
   * describes the configuration of ServerAuthModules at a given message layer,
   * and for a particular application context.
   *
   * @param layer
   *          a String identifying the message layer for the returned
   *          ServerAuthConfig object. This argument must not be null.
   *
   * @param appContext
   *          a String that identifies the messaging context for the returned
   *          ServerAuthConfig object. This argument must not be null.
   *
   * @param handler
   *          a CallbackHandler to be passed to the ServerAuthModules
   *          encapsulated by ServerAuthContext objects derived from the
   *          returned ServerAuthConfig. This argument may be null, in which
   *          case the implementation may assign a default handler to the
   *          configuration.
   *
   * @return a ServerAuthConfig Object that describes the configuration of
   *         ServerAuthModules at a given message layer, and for a particular
   *         application context. This method does not return null.
   *
   * @exception AuthException
   *              if this provider does not support the assignment of a default
   *              CallbackHandler to the returned ServerAuthConfig.
   *
   * @exception SecurityException
   *              if the caller does not have permission to retrieve the
   *              configuration.
   *              <p>
   *              The CallbackHandler assigned to the configuration must support
   *              the Callback objects required to be supported by the profile
   *              of this specification being followed by the messaging runtime.
   *              The CallbackHandler instance must be initialized with any
   *              application context needed to process the required callbacks
   *              on behalf of the corresponding application.
   */
  public ServerAuthConfig getServerAuthConfig(String layer, String appContext,
      CallbackHandler handler) throws AuthException {

    logger.log(Level.INFO,
        "TSAuthConfigProviderStandalone.getServerAuthConfig() called");

    String logStr = "TSAuthConfigProviderStandalone.getServerAuthConfig"
        + " : layer=" + layer + " : appContext=" + appContext;
    logger.log(Level.INFO, logStr);
    try {
      if (handler == null) {
        // this is used to help verify assertion JASPI:SPEC:71 which
        // that we should NOT have a null cbh passed in
        String msg = "FAILURE: layer=" + layer + " appContext=" + appContext;
        msg += " getServerAuthConfig() received CallbackHandler=null";
        logger.log(Level.INFO, msg);
      } else {
        String msg = "layer=" + layer + " appContext=" + appContext;
        msg += " getServerAuthConfig() received CallbackHandler=non-null";
        logger.log(Level.INFO, msg);
      }

      ServerAuthConfig serverAuthConfig = new TSServerAuthConfig(layer,
          appContext, handler, properties, logger);
      serverAuthConfigMap.put(layer + appContext, serverAuthConfig);

      return serverAuthConfig;
    } catch (Exception e) {
      e.printStackTrace();
      throw new AuthException(e.getMessage());
    }
  }

  /**
   * Causes a dynamic configuration provider to update its internal state such
   * that any resulting change to its state is reflected in the corresponding
   * authentication context configuration objects previously created by the
   * provider within the current process context.
   *
   * @exception AuthException
   *              if an error occured during the refresh.
   *
   * @exception SecurityException
   *              if the caller does not have permission to refresh the
   *              provider.
   */
  public void refresh() {
  }

  private static void initializeTSLogger() {
    String logFileLocation = null;
    if (logger != null)
      return;
    else {
      try {
        logFileLocation = System.getProperty("log.file.location");
        if (logFileLocation != null) {
          logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
          boolean appendMode = true;

          // if log file already exists, just append to it
          TSFileHandler fileHandler = new TSFileHandler(
              logFileLocation + "/" + JASPICData.DEFAULT_LOG_FILE, appendMode);
          fileHandler.setFormatter(new TSXMLFormatter());
          logger.addHandler(fileHandler);
        } else {
          throw new RuntimeException("log.file.location not set");
        }
      } catch (Exception e) {
        throw new RuntimeException("TSLogger Initialization failed", e);
      }
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String val) {
    description = val;
  }
}
