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

import java.util.logging.Level;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.AuthException;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.AuthConfigFactory;

import java.util.Map;
import java.util.HashMap;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.AuthDataCallbackHandler;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSXMLFormatter;
import com.sun.ts.tests.jaspic.tssv.util.TSFileHandler;

/**
 *
 * @author Raja Perumal
 */
public class TSAuthConfigProvider
    implements javax.security.auth.message.config.AuthConfigProvider {
  private static TSLogger logger = null;

  private static Map properties = null;

  private HashMap clientAuthConfigMap = new HashMap();

  private HashMap serverAuthConfigMap = new HashMap();

  // This will be called when a vendor registers TSAuthConfigProvider
  public TSAuthConfigProvider(Map props, AuthConfigFactory factory) {
    properties = props;

    // For self registration
    if (factory != null) {
      factory.registerConfigProvider(this, null, null,
          "TSAuthConfig Provider self registration");
    }

    if (logger == null) {
      initializeTSLogger();
    }
  }

  // TSAuthConfigFactory invokes this constructor with TSLogger
  public TSAuthConfigProvider(Map props, AuthConfigFactory factory,
      TSLogger tsLogger) {
    properties = props;

    // For self registration
    if (factory != null) {
      factory.registerConfigProvider(this, null, null,
          "TSAuthConfig Provider self registration");
    }

    if (tsLogger != null)
      logger = tsLogger;

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
    String logStr = "TSAuthConfigProvider.getClientAuthConfig called for "
        + "layer=" + layer + " : " + "appContext=" + appContext;

    logger.log(Level.INFO, logStr);

    try {
      if (handler == null) {
        // instantiate a default callback handler that gets
        // username and password from the environment using
        // system property j2eelogin.name j2eelogin.password
        handler = new AuthDataCallbackHandler();
      } else {
        // even if we receive vendor callbackhandler replace it with our own
        // callbackhandler for jaspic test.
        System.out.println(
            "Received callbackHandler =" + handler.getClass().getName());
        handler = new AuthDataCallbackHandler();
      }

      ClientAuthConfig clientAuthConfig = new TSClientAuthConfig(layer,
          appContext, handler, properties, logger);
      clientAuthConfigMap.put(layer + appContext, clientAuthConfig);
      return clientAuthConfig;
    } catch (Exception e) {
      throw new AuthException(e.getMessage());
    }
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
    String logStr = "TSAuthConfigProvider.getServerAuthConfig called for "
        + "layer=" + layer + " : " + "appContext=" + appContext;

    logger.log(Level.INFO, logStr);
    try {

      if ((!layer.equals(JASPICData.LAYER_SERVLET)) && (handler == null)) {
        // instantiate a default callback handler that gets
        // username and password from the environment using
        // system property j2eelogin.name j2eelogin.password
        handler = new AuthDataCallbackHandler();
      } else if ((layer.equals(JASPICData.LAYER_SERVLET))
          && (handler == null)) {
        // this is used to help verify assertion JASPI:SPEC:71 which
        // that we should NOT have a null cbh passed in
        String msg = "FAILURE: layer=" + layer + " appContext=" + appContext;
        msg += " getServerAuthConfig() received CallbackHandler=null";
        logger.log(Level.INFO, msg);
      }

      ServerAuthConfig serverAuthConfig = new TSServerAuthConfig(layer,
          appContext, handler, properties, logger);
      serverAuthConfigMap.put(layer + appContext, serverAuthConfig);
      return serverAuthConfig;
    } catch (Exception e) {
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
}
