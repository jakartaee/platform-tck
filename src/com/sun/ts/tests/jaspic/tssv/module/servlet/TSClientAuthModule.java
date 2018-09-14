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

package com.sun.ts.tests.jaspic.tssv.module.servlet;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;

import java.util.logging.Level;

/**
 * This is a placeholder file that should not end up getting used by the servlet
 * profile. Ideally we should be able to search the log file for strings from
 * this class and we should not see any of those entries getting logged.
 *
 * @author Sun Microsystems
 */
public class TSClientAuthModule
    implements javax.security.auth.message.module.ClientAuthModule {
  private TSLogger logger = null;

  /**
   * Creates a new instance of ClientAuthModuleImpl
   */
  public TSClientAuthModule() {
    logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    logMsg("TSClientAuthModule() constructor called.");
  }

  public TSClientAuthModule(TSLogger log) {
    if (log != null) {
      logger = log;
    } else {
      logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    }
    logMsg("TSClientAuthModule(TSLogger) constructor called.");
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

    logMsg("TSClientAuthModule.initialize() called.");
  }

  /**
   * Get the one or more Class objects representing the message types supported
   * by the module.
   *
   * @return an array of Class objects where each element defines a message type
   *         supported by the module. A module should return an array containing
   *         at least one element. An empty array indicates that the module will
   *         attempt to support any message type. This method never returns
   *         null.
   */
  @Override
  public Class[] getSupportedMessageTypes() {
    logMsg("TSClientAuthModule.getSupportedMessageTypes() called.");
    Class[] classarray = { javax.servlet.http.HttpServletRequest.class,
        javax.servlet.http.HttpServletResponse.class };
    return classarray;
  }

  /**
   * Secure a service request message before sending it to the service.
   * <p>
   * This method is called to transform the request message acquired by calling
   * getRequestMessage (on messageInfo) into the mechanism specific form to be
   * sent by the runtime.
   * <p>
   * This method conveys the outcome of its message processing either by
   * returning an AuthStatus value or by throwing an AuthException.
   *
   * @param messageInfo
   *          a contextual object that encapsulates the client request and
   *          server response objects, and that may be used to save state across
   *          a sequence of calls made to the methods of this interface for the
   *          purpose of completing a secure message exchange.
   *
   * @param clientSubject
   *          a Subject that represents the source of the service request, or
   *          null. It may be used by the method implementation as the source of
   *          Principals or credentials to be used to secure the request. If the
   *          Subject is not null, the method implementation may add additional
   *          Principals or credentials (pertaining to the source of the service
   *          request) to the Subject.
   *
   * @return an AuthStatus object representing the completion status of the
   *         processing performed by the method. The AuthStatus values that may
   *         be returned by this method are defined as follows:
   *         <ul>
   *         <li>AuthStatus.SUCCESS when the application request message was
   *         successfully secured. The secured request message may be obtained
   *         by calling by getRequestMessage on messageInfo.
   *
   *         <li>AuthStatus.SEND_CONTINUE to indicate that the application
   *         request message (within messageInfo) was replaced with a security
   *         message that should elicit a security-specific response from the
   *         peer security system. This status value also indicates that the
   *         application message has not yet been secured.
   *
   *         This status value serves to inform the calling runtime that (in
   *         order to successfully complete the message exchange) it will need
   *         to be capable of continuing the message dialog by conducting at
   *         least one additional request/response exchange after having
   *         received the security-specific response elicited by sending the
   *         security message.
   *
   *         When this status value is returned, the corresponding invocation of
   *         <code>validateResponse</code> must be able to obtain the original
   *         application request message.
   *
   *         <li>AuthStatus.FAILURE to indicate that a failure occured while
   *         securing the request message, and that an appropriate failure
   *         response message is available by calling getResponseMessage on
   *         messageInfo.
   *         </ul>
   *
   * @exception AuthException
   *              when the message processing failed without establishing a
   *              failure response message (in messageInfo).
   */
  @Override
  public AuthStatus secureRequest(MessageInfo messageInfo,
      Subject clientSubject) throws AuthException {

    logMsg("TSClientAuthModule.secureRequest() called.");
    return AuthStatus.SUCCESS;
  }

  /**
   * Validate a received service response.
   * <p>
   * This method is called to transform the mechanism specific response message
   * acquired by calling getResponseMessage (on messageInfo) into the validated
   * application message to be returned to the message processing runtime. If
   * the response message is a (mechanism specific) meta-message, the method
   * implementation must attempt to transform the meta-message into the next
   * mechanism specific request message to be sent by the runtime.
   * <p>
   * This method conveys the outcome of its message processing either by
   * returning an AuthStatus value or by throwing an AuthException.
   *
   * @param messageInfo
   *          a contextual object that encapsulates the client request and
   *          server response objects, and that may be used to save state across
   *          a sequence of calls made to the methods of this interface for the
   *          purpose of completing a secure message exchange.
   *
   * @param clientSubject
   *          a Subject that represents the recipient of the service response,
   *          or null. It may be used by the method implementation as the source
   *          of Principals or credentials to be used to validate the response.
   *          If the Subject is not null, the method implementation may add
   *          additional Principals or credentials (pertaining to the recipient
   *          of the service request) to the Subject.
   *
   * @param serviceSubject
   *          a Subject that represents the source of the service response, or
   *          null. If the Subject is not null, the method implementation may
   *          add additional Principals or credentials (pertaining to the source
   *          of the service response) to the Subject.
   *
   * @return an AuthStatus object representing the completion status of the
   *         processing performed by the method. The AuthStatus values that may
   *         be returned by this method are defined as follows:
   *         <ul>
   *         <li>AuthStatus.SUCCESS when the application response message was
   *         successfully validated. The validated message is available by
   *         calling getResponseMessage on messageInfo.
   *
   *         <li>AuthStatus.SEND_CONTINUE to indicate that response validation
   *         is incomplete, and that a continuation request was returned as the
   *         request message within messageInfo.
   *
   *         This status value serves to inform the calling runtime that (in
   *         order to successfully complete the message exchange) it will need
   *         to be capable of continuing the message dialog by conducting at
   *         least one additional request/response exchange.
   *
   *         <li>AuthStatus.FAILURE to indicate that validation of the response
   *         failed, and that a failure response message has been established in
   *         messageInfo.
   *         </ul>
   *
   * @exception AuthException
   *              when the message processing failed without establishing a
   *              failure response message (in messageInfo).
   */
  @Override
  public AuthStatus validateResponse(MessageInfo messageInfo,
      Subject clientSubject, Subject serviceSubject) throws AuthException {
    logMsg("TSClientAuthModule.validateResponse() called.");
    return AuthStatus.SUCCESS;
  }

  /**
   * Remove implementation specific principals and credentials from the subject.
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

    logMsg("TSClientAuthModule.cleanSubject() called.");
  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

}
