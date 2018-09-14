/*
 * Copyright (c)  2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.tssv.module.servlet;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.ServerCallbackSupport;
import com.sun.ts.tests.jaspic.tssv.util.CommonCallbackSupport;
import com.sun.ts.tests.jaspic.tssv.util.SimplePrincipal;

import java.util.logging.Level;

import javax.servlet.http.HttpServletResponseWrapper;

public class TSServletWrapperSAM
    implements javax.security.auth.message.module.ServerAuthModule {
  private TSLogger logger = null;

  private static MessagePolicy requestPolicy = null;

  private static MessagePolicy responsePolicy = null;

  private static CallbackHandler callbackHandler = null;

  private static Map options = null;

  private CallbackHandler handler;

  private Class<?>[] supportedMessageTypes = new Class[] {
      HttpServletRequest.class, HttpServletResponse.class };

  @Override
  public AuthStatus validateRequest(MessageInfo messageInfo,
      Subject clientSubject, Subject serviceSubject) throws AuthException {

    doServerCallbackChecks(messageInfo, clientSubject, serviceSubject);

    // Wrap the request - the resource to be invoked should get to see this
    TSRequestWrapper tswrap = new TSRequestWrapper(
        (HttpServletRequest) messageInfo.getRequestMessage());
    tswrap.setOptionsMap(messageInfo.getMap());
    messageInfo.setRequestMessage(tswrap);

    // Wrap the response - the resource to be invoked should get to see this
    messageInfo.setResponseMessage(new TSResponseWrapper(
        (HttpServletResponse) messageInfo.getResponseMessage()));

    return AuthStatus.SUCCESS;
  }

  @Override
  public Class<?>[] getSupportedMessageTypes() {
    return supportedMessageTypes;
  }

  @Override
  public AuthStatus secureResponse(MessageInfo messageInfo,
      Subject serviceSubject) throws AuthException {

    HttpServletRequest request = (HttpServletRequest) messageInfo
        .getRequestMessage();

    // Unwrap the request
    if (request instanceof TSRequestWrapper) {
      messageInfo.setRequestMessage(((TSRequestWrapper) request).getRequest());
    } else {
      logMsg("Incorrect request type : " + request.getClass().getName());
    }

    HttpServletResponse response = (HttpServletResponse) messageInfo
        .getResponseMessage();

    if (response instanceof TSResponseWrapper) {
      messageInfo
          .setResponseMessage(((TSResponseWrapper) response).getResponse());
    } else {
      logMsg("Incorrect response type : " + response.getClass().getName());
    }

    return AuthStatus.SEND_SUCCESS;
  }

  /**
   * Creates a new instance of TSServletWrapperSAM
   */
  public TSServletWrapperSAM() {
    logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    logMsg("TSServletWrapperSAM() constructor called");
  }

  public TSServletWrapperSAM(TSLogger log) {
    if (log != null) {
      logger = log;
    } else {
      logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    }
    logMsg("TSServletWrapperSAM(TSLogger) constructor called");
  }

  /**
   * Initialize this module with request and response message policies to
   * enforce, a CallbackHandler, and any module-specific configuration
   * properties.
   * 
   * <p>
   * The request policy and the response policy must not both be null.
   * 
   * @param requestPolicy
   *          the request policy this module must enforce, or null.
   * 
   * @param responsePolicy
   *          the response policy this module must enforce, or null.
   * 
   * @param handler
   *          CallbackHandler used to request information.
   * 
   * @param options
   *          a Map of module-specific configuration properties.
   * 
   * @exception AuthException
   *              if module initialization fails, including for the case where
   *              the options argument contains elements that are not supported
   *              by the module.
   */
  @Override
  public void initialize(MessagePolicy requestPolicy,
      MessagePolicy responsePolicy, CallbackHandler handler, Map options)
      throws AuthException {

    if ((options != null) && (options.get("TSLogger") != null)) {
      logger = (TSLogger) options.get("TSLogger");
    }

    this.requestPolicy = requestPolicy;
    this.responsePolicy = responsePolicy;
    callbackHandler = handler;
    this.options = options;

    // perform some checking to support assertion JASPI:SPEC:87
    verifyRequestPolicy(requestPolicy);

    logger.log(Level.INFO,
        "CBH for HttpServlet supports type: " + handler.getClass().getName());
  }

  /*
   * This is a convenience method that will do some verification on the request
   * policy to see if it complies with assertion JASPI:SPEC:87. If there are any
   * problems found, appropriate log statements will be made and searched for
   * later on in the Client code.
   */
  private void verifyRequestPolicy(MessagePolicy requestPolicy) {

    String errStr = "Layer=" + JASPICData.LAYER_SERVLET;
    errStr += " requestPolicy=invalid in TSServletWrapperSAM.initialize()";

    if (requestPolicy == null) {
      // we should never have a null requestpolicy here
      logger.log(Level.SEVERE, errStr);
    } else {
      MessagePolicy.TargetPolicy[] tp = requestPolicy.getTargetPolicies();
      if (tp.length < 1) {
        // must return an array containing at least one TargetPolicy
        logger.log(Level.INFO, errStr);
      } else {
        for (int ii = 0; ii < tp.length; ii++) {
          MessagePolicy.ProtectionPolicy pp = tp[ii].getProtectionPolicy();
          if ((pp != null) && (!isProtectionPolicyIDValid(pp.getID()))) {
            String str = "Layer=" + JASPICData.LAYER_SERVLET;
            str += " Invalid ProtectionPolicy.getID()";
            logger.log(Level.INFO, str);
          }
        }
      }
    }
  }

  /*
   * (spec section 3.7.4) For servlet profile, calling the getID() method on the
   * ProtectionPolicy must return one of the following values:
   * ProtectionPolicy.AUTHENTICATE_SENDER ProtectionPolicy.AUTHENTICATE_CONTENT
   */
  public boolean isProtectionPolicyIDValid(String strId) {
    boolean bval = false;

    if ((strId.equals(MessagePolicy.ProtectionPolicy.AUTHENTICATE_CONTENT))
        || (strId.equals(MessagePolicy.ProtectionPolicy.AUTHENTICATE_SENDER))) {
      bval = true;
    }
    return bval;
  }

  /**
   * Get the one or more Class objects representing the message types supported
   * by the module.
   * 
   * @return an array of Class objects, with at least one element defining a
   *         message type supported by the module.
   */
  /*
   * @Override public Class[] getSupportedMessageTypes() {
   * logMsg("TSServletWrapperSAM.getSupportedMessageTypes called"); Class[]
   * classarray = {
   * com.sun.ts.tests.jaspic.tssv.module.servlet.TSRequestWrapper.class,
   * com.sun.ts.tests.jaspic.tssv.module.servlet.TSResponseWrapper.class };
   * return classarray; }
   */

  private String getRequestURI(MessageInfo messageInfo) {
    String requestURI = null;
    Object reqObj = messageInfo.getRequestMessage();
    if ((reqObj != null)
        && (reqObj instanceof javax.servlet.http.HttpServletRequest)) {
      requestURI = ((HttpServletRequest) reqObj).getRequestURI();
    }
    return requestURI;
  }

  /*
   * Convenience method to dump messageInfo logging out.
   */
  private void logMessageTypes(MessageInfo messageInfo, String methodName) {
    String msg;

    String requestURI = "";

    if (messageInfo != null) {
      Object reqObj = messageInfo.getRequestMessage();
      if (reqObj != null) {
        // if here, we want to see if our reqObj is type
        // HttpServletRequest
        // and if so print out a log msg stating so. (jsr-196 expects
        // the
        // reqObj to be type HttpServletRequest)
        msg = methodName + ": MessageInfo.getRequestMessage() is of type ";
        if (reqObj instanceof javax.servlet.http.HttpServletRequest) {
          msg = msg + "javax.servlet.http.HttpServletRequest";
          requestURI = ((HttpServletRequest) reqObj).getRequestURI();

          // related to assertion JASPI:SPEC:95 , this block of code
          // may be needed if we need to identify which servlet
          // invoked this
          if (requestURI != null) {
            // we want to know which servlet/jsp/html page generated
            // this action and we want to include that in the log so
            // we
            // can check if the log gets generated for both servlets
            // and
            // for static html - which is a jsr-196 requirement.
            String msg2 = msg + " for requestURI=" + requestURI;
            logMsg(msg2);
          }
        } else {
          msg = msg + messageInfo.getClass().getName();
        }
        logMsg(msg);
      }

      Object respObj = messageInfo.getResponseMessage();
      if (respObj != null) {
        // if here, we want to see if our respObj is type
        // HttpServletResponse
        // and if so print out a log msg stating so. (jsr-196 expects
        // the
        // respObj to be type HttpServletResponse)
        msg = methodName + ": MessageInfo.getResponseMessage() is of type ";
        if (respObj instanceof javax.servlet.http.HttpServletResponse) {
          msg = msg + "javax.servlet.http.HttpServletResponse";
          /*
           * if (requestURI != null) { // this should have been obtained from
           * the requestObj above msg = msg +" for requestURI=" + requestURI; }
           */
        } else {
          msg = msg + messageInfo.getClass().getName();
        }
        logMsg(msg);
      }

    } else {
      msg = "TSServletWrapperSAM." + methodName
          + " called with null MessageInfo object.";
      logMsg(msg);
    }

  }

  /**
   * Remove method specific principals and credentials from the subject.
   * 
   * @param messageInfo
   *          a contextual object that encapsulates the client request and
   *          server response objects, and that may be used to save state across
   *          a sequence of calls made to the methods of this interface for the
   *          purpose of completing a secure message exchange.
   * 
   * @param subject
   *          the Subject instance from which the Principals and credentials are
   *          to be removed.
   * 
   * @exception AuthException
   *              if an error occurs during the Subject processing.
   */
  @Override
  public void cleanSubject(MessageInfo messageInfo, Subject subject)
      throws AuthException {

    logMsg("TSServletWrapperSAM.cleanSubject called");
    subject = null;
    // subject = new Subject();
  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

  public String getPrincipalNameFromSubject(Subject sub) {
    Principal principal = null;
    String concatPrincipalName = "";
    Set principalSet = sub.getPrincipals();

    Iterator iterator = principalSet.iterator();
    while (iterator.hasNext()) {
      principal = (Principal) iterator.next();
      concatPrincipalName += principal.getName();
    }

    return concatPrincipalName;
  }

  private void doServerCallbackChecks(MessageInfo messageInfo,
      Subject clientSubject, Subject serviceSubject) {

    HttpServletRequest request = (HttpServletRequest) messageInfo
        .getRequestMessage();
    String servletPath = request.getContextPath() + request.getServletPath();
    String msg = "";

    ServerCallbackSupport serverCallbacks = new ServerCallbackSupport(logger,
        callbackHandler, JASPICData.LAYER_SERVLET, messageInfo, clientSubject,
        serviceSubject);

    // instead of calling all callbacks in one method, lets call them
    // all individually so ithat we can check return values of each.
    // serverCallbacks.verify();

    if (serverCallbacks.verifyCPCCallback()) {
      msg = "TSServletWrapperSAM.validateRequest(): verifyCPCCallback returned true";
    } else {
      msg = "TSServletWrapperSAM.validateRequest(): verifyCPCCallback returned false";
    }
    msg += " for servletPath = " + servletPath;
    logMsg(msg);

    if (serverCallbacks.verifyGPCCallback()) {
      msg = "TSServletWrapperSAM.validateRequest(): verifyGPCCallback returned true";
    } else {
      msg = "TSServletWrapperSAM.validateRequest(): verifyGPCCallback returned false";
    }
    msg += " for servletPath = " + servletPath;
    logMsg(msg);

    if (serverCallbacks.verifyPVCCallback()) {
      msg = "TSServletWrapperSAM.validateRequest(): verifyPVCCallback returned true";
    } else {
      msg = "TSServletWrapperSAM.validateRequest(): verifyPVCCallback returned false";
    }
    msg += " for servletPath = " + servletPath;
    logMsg(msg);

    return;
  }

}
