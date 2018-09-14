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

import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.AuthException;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.http.HttpServletRequest;

import javax.security.auth.Subject;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;

/**
 *
 * @author Raja Perumal
 *
 *         Important: It is very likely that the logged messages of this class
 *         are being searched for in the logfile by the client code. Because of
 *         this, refrain from changing log messages in this file.
 *
 */
public class TSServerAuthConfig
    implements javax.security.auth.message.config.ServerAuthConfig {
  private static String messageLayer = null;

  private static String appContext = null;

  private static CallbackHandler handler = null;

  private static TSLogger logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);

  private static Map properties = null;

  private static Map authMandatoryMap;

  private TSServerAuthConfig(String layer, String applicationCtxt,
      CallbackHandler cbkHandler, Map props) {
    messageLayer = layer;
    appContext = applicationCtxt;
    handler = cbkHandler;
    properties = props;
  }

  public TSServerAuthConfig(String layer, String applicationCtxt,
      CallbackHandler cbkHandler, Map props, TSLogger tsLogger) {

    this(layer, applicationCtxt, cbkHandler, props);

    if (tsLogger != null) {
      logger = tsLogger;
    }

    String str = "TSServerAuthConfig called for layer=" + layer
        + " : appContext=" + applicationCtxt;
    logger.log(Level.INFO, str);
  }

  /**
   * Get the message layer name of this authentication context configuration
   * object.
   *
   * @return the message layer name of this configuration object, or null if the
   *         configuration object pertains to an unspecified message layer.
   */
  @Override
  public String getMessageLayer() {
    logger.log(Level.INFO, "getMessageLayer called");
    return messageLayer;
  }

  /**
   * Get the application context identifier of this authentication context
   * configuration object.
   *
   * @return the String identifying the application context of this
   *         configuration object or null if the configuration object pertains
   *         to an unspecified application context.
   */
  @Override
  public String getAppContext() {
    logger.log(Level.INFO, "getAppContext called");
    return appContext;
  }

  /**
   * Get the authentication context identifier corresponding to the request and
   * response objects encapsulated in messageInfo.
   *
   * @param messageInfo
   *          a contextual Object that encapsulates the client request and
   *          server response objects.
   *
   * @return the auth context identifier related to the encapsulated request and
   *         response objects, or null.
   *
   * @throws IllegalArgumentException
   *           if the type of the message objects incorporated in messageInfo
   *           are not compatible with the message types supported by this
   *           authentication context configuration object.
   *
   */

  @Override
  public String getAuthContextID(MessageInfo messageInfo) {
    logger.log(Level.INFO, "getAuthContextID called");
    String rval = null;

    if (messageLayer.equals(JASPICData.LAYER_SOAP)) {

      rval = getOpName((SOAPMessage) messageInfo.getRequestMessage());

    } else if (messageLayer.equals(JASPICData.LAYER_SERVLET)) {
      HttpServletRequest request = (HttpServletRequest) messageInfo
          .getRequestMessage();
      rval = request.getServletPath() + " " + request.getMethod();

      dumpServletProfileKeys(messageInfo, "getAuthContextID",
          JASPICData.LAYER_SERVLET);
    } else {
      rval = null;
    }

    String logMsg = "getAuthContextID() called for layer=" + messageLayer;
    logMsg += " shows AuthContextId=" + rval;
    logger.log(Level.INFO, logMsg);

    return rval;
  }

  /*
   * This is a convenience method that will likely only be called once.
   * Currently there is only one key to dump from the map, but his may change in
   * the future.
   */
  private void dumpServletProfileKeys(MessageInfo msgInfo, String callerMethod,
      String messageLayer) {

    Map map = msgInfo.getMap();

    // for debugging only
    // dumpAllKeyValues(map);

    // lets pull out some servlet info that we can use to help uniquely
    // identify the source of this request
    HttpServletRequest request = (HttpServletRequest) msgInfo
        .getRequestMessage();
    String servletName = request.getServletPath();

    // see assertion JASPI:SPEC:306 (section 3.8.1.1) for details on this
    // jsr-196 states the following key must exist for servlet profile
    String strKey = "javax.security.auth.message.MessagePolicy.isMandatory";
    if (map != null) {
      String keyVal = (String) map.get(strKey);
      String msg = "dumpServletProfileKeys() called with attrs: ";
      msg += " layer=" + messageLayer;
      msg += " servletName=" + servletName;
      msg += " callerMethod=" + callerMethod;
      msg += " key=" + strKey;

      if (keyVal == null) {
        msg += " value=Invalid";
        addMandatoryStatusToMap(msgInfo, messageLayer, appContext, false);
      } else {
        // We dont care if Boolean.valueOf(keyVal).booleanValue() is true
        // or false. We only care that it is a boolean value -which means
        // this is considered a valid key-value pair.
        msg += " value=Valid";
        addMandatoryStatusToMap(msgInfo, messageLayer, appContext, true);
      }

      logger.log(Level.INFO, msg);
    }
  }

  private void addMandatoryStatusToMap(MessageInfo msgInfo, String layer,
      String appContext, boolean isMandatory) {
    if (authMandatoryMap == null) {
      authMandatoryMap = new Hashtable();
    }

    // see if entry already exists and if not we need to add the entry
    Boolean booleanObj = (Boolean) authMandatoryMap.get(layer + appContext);
    if (booleanObj == null) {
      authMandatoryMap.put(layer + appContext, new Boolean(isMandatory));
    }
  }

  private boolean getMandatoryStatusFromMap(String layer, String appContext) {
    boolean bval = false;

    if (layer.equals("JASPICData.LAYER_SERVLET")) {
      if (authMandatoryMap != null) {
        Boolean booleanObj = (Boolean) authMandatoryMap.get(layer + appContext);
        bval = booleanObj.booleanValue();
      } else {
        String msg = "Could Not properly determine isMandatory status.";
        msg += "  Will return false.";
        logger.log(Level.INFO, msg);
        bval = false;
      }
    }

    return bval;
  }

  /*
   * for debug purposes only
   */
  private void dumpAllKeyValues(Map map) {

    if (map == null) {
      logger.log(Level.INFO, "map is null");
      return;
    }

    Set keys = map.keySet();
    Iterator iterator = keys.iterator();
    while (iterator.hasNext()) {
      String msg = "";
      String key = (String) iterator.next();
      if (key != null) {
        msg = "key=" + key;
        String val = (String) map.get(key);
        if (val != null) {
          msg += " value=" + val;
        } else {
          msg += " value=null";
        }
        logger.log(Level.INFO, msg);
      }
    }
  }

  private String getOpName(SOAPMessage message) {
    if (message == null) {
      return null;
    }

    String rvalue = null;

    // first look for a SOAPAction header.
    // this is what .net uses to identify the operation

    MimeHeaders headers = message.getMimeHeaders();
    if (headers != null) {
      String[] actions = headers.getHeader("SOAPAction");
      if (actions != null && actions.length > 0) {
        rvalue = actions[0];
        if (rvalue != null && rvalue.equals("\"\"")) {
          rvalue = null;
        }
      }
    }

    // if that doesn't work then we default to trying the name
    // of the first child element of the SOAP envelope.

    if (rvalue == null) {
      Name name = getName(message);
      if (name != null) {
        rvalue = name.getLocalName();
      }
    }

    return rvalue;
  }

  private Name getName(SOAPMessage message) {
    Name rvalue = null;
    SOAPPart soap = message.getSOAPPart();
    if (soap != null) {
      try {
        SOAPEnvelope envelope = soap.getEnvelope();
        if (envelope != null) {
          SOAPBody body = envelope.getBody();
          if (body != null) {
            Iterator it = body.getChildElements();
            while (it.hasNext()) {
              Object o = it.next();
              if (o instanceof SOAPElement) {
                rvalue = ((SOAPElement) o).getElementName();
                break;
              }
            }
          }
        }
      } catch (SOAPException se) {
        if (logger.isLoggable(Level.FINE)) {
          logger.log(Level.FINE, "WSS: Unable to get SOAP envelope", se);
        }
      }
    }

    return rvalue;
  }

  /**
   * Causes a dynamic anthentication context configuration object to update the
   * internal state that it uses to process calls to its
   * <code>getAuthContext</code> method.
   *
   * @exception AuthException
   *              if an error occured during the update.
   *
   * @exception SecurityException
   *              if the caller does not have permission to refresh the
   *              configuration object.
   */
  @Override
  public void refresh() {
    logger.log(Level.INFO, "refresh called");
  }

  /**
   * Used to determine whether the authentication context configuration object
   * encapsulates any protected authentication contexts.
   *
   * @return true if the configuration object encapsulates at least one
   *         protected authentication context. Otherwise, this method returns
   *         false.
   */
  @Override
  public boolean isProtected() {
    // To verify protected code path, always return true.
    return true;
  }

  /**
   * Get a ServerAuthContext instance from this ServerAuthConfig. This method
   * should get called by the MPR.
   *
   * <p>
   * The implementation of this method returns a ServerAuthContext instance that
   * encapsulates the ServerAuthModules used to validate requests and secure
   * responses associated with the given <i>operation</i>.
   *
   * <p>
   * Specifically, this method accesses this ServerAuthConfig object with the
   * argument <i>operation</i> to determine the ServerAuthModules that are to be
   * encapsulated in the returned ServerAuthContext instance.
   *
   * <P>
   * The ServerAuthConfig object establishes the request and response
   * MessagePolicy objects that are passed to the encapsulated modules when they
   * are initialized by the returned ServerAuthContext instance. It is the
   * modules' responsibility to enforce these policies when invoked.
   *
   * @param operation
   *          an operation identifier used to index the provided <i>config</i>,
   *          or null. This value must be identical to the value returned by the
   *          <code>getOperation</code> method for all <code>MessageInfo</code>
   *          objects passed to the <code>validateRequest</code> method of the
   *          returned ServerAuthContext.
   *
   * @param serviceSubject
   *          a Subject that represents the source of the service response to be
   *          secured by the acquired authentication context. The principal
   *          and/or credentials of the Subject may be used to select or acquire
   *          the authentication context. If the Subject is not null, additional
   *          Principals or credentials (pertaining to the source of the
   *          response) may be added to the Subject. A null value may be passed
   *          to for this parameter.
   *
   * @param properties
   *          a Map object that may be used by the caller to augment the
   *          properties that will be passed to the encapsulated modules at
   *          module initialization. The null value may be passed for this
   *          parameter.
   *
   * @return a ServerAuthContext instance that encapsulates the
   *         ServerAuthModules used to secure and validate requests/responses
   *         associated with the given <i>operation</i>, or null (indicating
   *         that no modules are configured).
   *
   * @exception AuthException
   *              if this operation fails.
   */
  @Override
  public ServerAuthContext getAuthContext(String operation,
      Subject serviceSubject, Map properties) throws AuthException {

    String logStr = "TSServerAuthConfig.getAuthContext:  layer=" + messageLayer
        + " : appContext=" + appContext;
    logger.log(Level.INFO, logStr);

    logger.log(Level.INFO,
        "TSServerAuthConfig.getAuthContext:  layer=" + messageLayer
            + " : appContext=" + appContext + " operationId=" + operation);

    if (serviceSubject != null) {
      properties.put(JASPICData.SVC_SUBJECT_KEY, serviceSubject);
      logger.log(Level.INFO,
          "found a non-null serviceSubject in getAuthContext()");
    }

    // Copy properties that are passed in this method to this.properties
    //
    // Note: this.properties is obtained from the Provider which gets those
    // properties during provider registration time from the factory.
    try {
      if ((this.properties != null) && (properties != null)) {
        this.properties.putAll(properties);
      }
    } catch (Exception ex) {
      logger.log(Level.INFO, "Exception : " + ex.getMessage());
    }

    checkIf115Compatible(properties);

    // validate operation is correct for the message layer type
    validateOperationId(operation, messageLayer);

    try {
      boolean bIsMand = getMandatoryStatusFromMap(messageLayer, appContext);
      ServerAuthContext sac = new TSServerAuthContext(messageLayer, appContext,
          handler, operation, serviceSubject, this.properties, bIsMand, logger);

      logStr = "TSServerAuthConfig.getAuthContext: returned non-null ServerAuthContext";
      logger.log(Level.INFO, logStr);

      // For SOAP layer
      logStr = "TSServerAuthConfig.getAuthContext: returned non-null"
          + " ServerAuthContext for operationId=" + operation;
      logger.log(Level.INFO, logStr);
      return sac;
    } catch (Exception e) {
      logger.log(Level.SEVERE,
          "Got AuthException in TSServerAuthConfig.getAuthContext");
      logger.log(Level.SEVERE, e.getMessage());
      throw new AuthException(e.getMessage());
    }
  }

  /*
   * this is used to help us verify JASPI:SPEC:300 This is a convenience method
   * that will likely only be called once. Currently there is only one key to
   * dump from the props, but this may change in the future.
   */
  private void checkIf115Compatible(Map properties) {

    String msg = "layer=" + messageLayer + " appContext=" + appContext;

    // jsr-196 states the following key must exist if JSR-115 is supported
    // but it must NOT exist if 115 is NOT supported.
    String strKey = "javax.security.jacc.PolicyContext";
    msg += " Key=" + strKey;
    if (properties != null) {
      String keyVal = (String) properties.get(strKey);

      if (keyVal == null) {
        msg += " does NOT exist thus Not 115 compatible";
      } else if (keyVal != null) {
        msg += " does exist thus 115 compatible";
        logger.log(Level.INFO, "key=" + strKey + "  value=" + keyVal); // XXXX -
                                                                       // debugging
      }
    } else {
      msg += " does NOT exist thus Not 115 compatible";
    }
    logger.log(Level.INFO, msg);
  }

  /*
   * The goal of this method is to help us verify if we have a properly
   * constructed operationID. This method will log messages indicating if we do
   * or do not have a valid operationID.
   */
  private void validateOperationId(String operation, String layer) {
    String MNAME = "TSServerAuthConfig.validateOperationId() : ";
    String sProfile;
    String lstr;
    boolean bPassed = true;

    operation = operation.trim();
    if (operation == null) {
      logger.log(Level.SEVERE,
          MNAME + " there was a null operationId and should not be!");
    }

    if (layer.equals(JASPICData.LAYER_SERVLET)) {
      // name should consist of following format
      // <request URI minus the context path><blank><http-method ==
      // (GET|POST|PUT)>
      // there should ONLY be one whitspace
      sProfile = "HttpServlet profile";
      if (operation.startsWith("http:")) {
        // should not start with a context path
        lstr = MNAME + sProfile + " should not start with a context path";
        logger.log(Level.SEVERE, lstr);
        bPassed = false;
      }

      if ((operation.indexOf(" ") != operation.lastIndexOf(" "))) {
        // if here then we have more than one whitespace and should not!
        lstr = MNAME + sProfile + " found more than one whitespace.";
        logger.log(Level.SEVERE, lstr);
        bPassed = false;
      }

      int ii = operation.indexOf(" ");
      String httpMethod = operation.substring(ii);
      httpMethod = httpMethod.trim();
      if (!(httpMethod.equalsIgnoreCase("GET")
          || httpMethod.equalsIgnoreCase("POST")
          || httpMethod.equalsIgnoreCase("PUT"))) {
        // it should be one of these three supported http methods
        lstr = MNAME + sProfile + " invalid http method type :" + httpMethod
            + ".  Must be  POST|GET|PUT";
        logger.log(Level.SEVERE, lstr);
        bPassed = false;
      }

      if (bPassed) {
        logger.log(Level.INFO, MNAME + sProfile + " : PASSED");
      } else {
        logger.log(Level.SEVERE, MNAME + sProfile + " : FAILED");
      }

    } else if (layer.equals(JASPICData.LAYER_SOAP)) {
      // Nothing to verify here for SOAP profile
    }

  }

}
