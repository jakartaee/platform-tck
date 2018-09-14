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

import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.module.ClientAuthModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;
import java.util.logging.Level;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;

/**
 *
 * @author Raja Perumal
 */
public class TSClientAuthContext
    implements javax.security.auth.message.config.ClientAuthContext {

  private static ClientAuthModule clientAuthModule = null;

  private static TSLogger logger = null;

  private static String messageLayer = null;

  private static String appContext = null;

  private static CallbackHandler handler = null;

  private static String operation = null;

  private static Subject clientSubject = null;

  private static Map properties = null;

  public TSClientAuthContext() {

  }

  public TSClientAuthContext(String messageLayer, String appContext,
      CallbackHandler handler, String operation, Subject clientSubject,
      Map properties, TSLogger tsLogger) throws AuthException {

    this(messageLayer, appContext, handler, operation, clientSubject,
        properties);
    logger = tsLogger;
    ClientAuthModule cam = null;
    logger.log(Level.INFO, "TSClientAuthContext called");

    // pass TSlogger to TSServerAuthModule through properties
    properties.put("TSLogger", logger);

    if (messageLayer.equals(JASPICData.LAYER_SOAP)) {
      if (appContext.indexOf("SendSuccessHello") > -1) {
        cam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSSendSuccessClientAuthModule();
        cam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("SendFailureHello") > -1) {
        cam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSSendFailureClientAuthModule();
        cam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("FailureHello") > -1) {
        cam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSFailureClientAuthModule();
        cam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("AuthExceptionHello") > -1) {
        cam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSAuthExceptionClientAuthModule();
        cam.initialize(null, null, handler, properties);

      } else {
        cam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSClientAuthModule();
        cam.initialize(null, null, handler, properties);
      }
    } else if (messageLayer.equals(JASPICData.LAYER_SERVLET)) {
      cam = new com.sun.ts.tests.jaspic.tssv.module.servlet.TSClientAuthModule();
      cam.initialize(null, null, handler, properties);
    }

    clientAuthModule = cam;

  }

  private TSClientAuthContext(String layer, String appContxt,
      CallbackHandler hndler, String operatn, Subject cliSubject, Map props)
      throws AuthException {

    messageLayer = layer;
    appContext = appContxt;
    handler = hndler;
    operation = operatn;
    clientSubject = cliSubject;
    properties = props;

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

    logger.log(Level.INFO, "TSClientAuthContext.secureRequest called");

    return clientAuthModule.secureRequest(messageInfo, clientSubject);
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

    logger.log(Level.INFO, "TSClientAuthContext.validateResponse called");
    return clientAuthModule.validateResponse(messageInfo, clientSubject,
        serviceSubject);
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
    clientAuthModule.cleanSubject(messageInfo, subject);

  }
}
