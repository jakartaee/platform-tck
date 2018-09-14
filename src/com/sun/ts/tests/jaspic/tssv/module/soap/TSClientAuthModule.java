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

package com.sun.ts.tests.jaspic.tssv.module.soap;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Level;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.CommonCallbackSupport;
import com.sun.ts.tests.jaspic.tssv.util.ClientCallbackSupport;
import javax.xml.namespace.QName;

/**
 *
 * @author Raja Perumal
 */
public class TSClientAuthModule
    implements javax.security.auth.message.module.ClientAuthModule {
  private static TSLogger logger = null;

  private static CallbackHandler callbackHandler = null;

  private static Map options = null;

  /**
   * Creates a new instance of ClientAuthModuleImpl
   */
  public TSClientAuthModule() {
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
  public void initialize(MessagePolicy reqPolicy, MessagePolicy resPolicy,
      CallbackHandler handler, Map optns) throws AuthException {
    callbackHandler = handler;
    options = optns;

    // Get the reference to TSLogger from the Map "options"
    if (options.get("TSLogger") != null)
      logger = (TSLogger) options.get("TSLogger");
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
    Class[] classarray = { javax.xml.soap.SOAPMessage.class };
    logMsg("TSClientAuthModule.getSupportedMessageTypes called");
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

    String msg = "";
    if (clientSubject != null) {
      msg = "TSClientAuthModule.secureRequest called with client Subject :"
          + getPrincipalNameFromSubject(clientSubject);
    } else
      msg = "TSClientAuthModule.secureRequest called with null client Subject";

    logMsg(msg);
    logMessageTypes(messageInfo, "secureRequest");

    // Check Callback Handler support for SOAP Client runtime
    ClientCallbackSupport clientCallbackSupport = new ClientCallbackSupport(
        logger, callbackHandler, "SOAP");

    clientCallbackSupport.verify();

    // Check CommonCallbacks support for SOAP client runtime
    CommonCallbackSupport commonCallbacks = new CommonCallbackSupport(logger,
        callbackHandler, "SOAP", "ClientRuntime");

    commonCallbacks.verify();

    // Log the value for key javax.xml.ws.wsdl.service in messageInfoMap
    Map messageInfoMap = messageInfo.getMap();

    QName qName = (QName) messageInfoMap.get("javax.xml.ws.wsdl.service");

    if (qName != null) {
      String qNameToString = qName.toString();

      msg = "TSClientAuthModule.secureRequest messageInfo :"
          + "javax.xml.ws.wsdl.service=" + qNameToString;
    } else {
      msg = "TSClientAuthModule.secureRequest messageInfo :"
          + "** ERROR ** No value found for key javax.xml.ws.wsdl.service in MessageInfoMap"
          + " : Expected a QName";

    }
    logMsg(msg);

    return AuthStatus.SEND_SUCCESS;
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
    String msg = "";
    if (clientSubject != null) {
      msg = "TSClientAuthModule.validateResponse called with client Subject :"
          + getPrincipalNameFromSubject(clientSubject);
    } else
      msg = "TSClientAuthModule.validateResponse called with null client Subject";

    if (serviceSubject != null) {
      msg = msg + " with serviceSubject :"
          + getPrincipalNameFromSubject(serviceSubject);
    } else
      msg = msg + " with null serviceSubject";

    logMsg(msg);
    logMessageTypes(messageInfo, "validateResponse");

    // Log the value for key javax.xml.ws.wsdl.service in messageInfoMap
    Map messageInfoMap = messageInfo.getMap();

    QName qName = (QName) messageInfoMap.get("javax.xml.ws.wsdl.service");

    if (qName != null) {
      String qNameToString = qName.toString();

      msg = "TSClientAuthModule.validateResponse messageInfo :"
          + "javax.xml.ws.wsdl.service=" + qNameToString;
    } else {
      msg = "TSClientAuthModule.validateResponse messageInfo :"
          + "** ERROR ** No value found for key javax.xml.ws.wsdl.service in MessageInfoMap"
          + " : Expected a QName ";

    }
    logMsg(msg);

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
    logMsg("TSClientAuthModule.cleanSubject called");

    // remove the contents of the subject and return an empty subject
    subject = null;

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

  private void logMessageTypes(MessageInfo messageInfo, String methodName) {
    String msg = null;

    Object requestMessage = messageInfo.getRequestMessage();
    Object responseMessage = messageInfo.getResponseMessage();

    if (requestMessage != null) {
      if (requestMessage instanceof javax.xml.soap.SOAPMessage) {
        msg = methodName
            + " : MessageInfo.getRequestMessage() is of type javax.xml.soap.SOAPMessage";
        logMsg(msg);
      } else {
        msg = methodName + " : MessageInfo.getRequestMessage() is of type "
            + requestMessage.getClass().getName();
        logMsg(msg);

      }
    }

    if (responseMessage != null) {
      if (responseMessage instanceof javax.xml.soap.SOAPMessage) {
        msg = methodName
            + " : MessageInfo.getResponseMessage() is of type javax.xml.soap.SOAPMessage";
        logMsg(msg);
      } else {
        msg = methodName + " : MessageInfo.getResponseMessage() is of type "
            + responseMessage.getClass().getName();
        logMsg(msg);

      }
    }

  }

}
