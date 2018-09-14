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

import java.util.Map;
import java.util.logging.Level;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;

import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 *
 * @author Raja Perumal
 */
public class TSSendFailureServerAuthModule
    implements javax.security.auth.message.module.ServerAuthModule {
  private static TSLogger logger = null;

  private static Map options = null;

  /**
   * Creates a new instance of TSSendFailureServerAuthModule
   */
  public TSSendFailureServerAuthModule() {
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
    options = optns;

    // Get the reference to TSLogger from the Map "options"
    if (options.get("TSLogger") != null)
      logger = (TSLogger) options.get("TSLogger");

  }

  /**
   * Get the one or more Class objects representing the message types supported
   * by the module.
   *
   * @return an array of Class objects, with at least one element defining a
   *         message type supported by the module.
   */
  @Override
  public Class[] getSupportedMessageTypes() {
    logMsg("TSSendFailureServerAuthModule.getSupportedMessageTypes called");
    Class[] classarray = { javax.xml.soap.SOAPMessage.class };
    return classarray;
  }

  /**
   * Authenticate a received service request.
   *
   * This method is called to transform the mechanism specific request message
   * acquired by calling getRequestMessage (on messageInfo) into the validated
   * application message to be returned to the message processing runtime. If
   * the received message is a (mechanism specific) meta-message, the method
   * implementation must attempt to transform the meta-message into a
   * corresponding mechanism specific response message, or to the validated
   * application request message. The runtime will bind a validated application
   * message into the the corresponding service invocation.
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
   *          a Subject that represents the source of the service request. It is
   *          used by the method implementation to store Principals and
   *          credentials validated in the request.
   *
   * @param serviceSubject
   *          a Subject that represents the recipient of the service request, or
   *          null. It may be used by the method implementation as the source of
   *          Principals or credentials to be used to validate the request. If
   *          the Subject is not null, the method implementation may add
   *          additional Principals or credentials (pertaining to the recipient
   *          of the service request) to the Subject.
   *
   * @return an AuthStatus object representing the completion status of the
   *         processing performed by the method. The AuthStatus values that may
   *         be returned by this method are defined as follows:
   *
   *         <ul>
   *         <li>AuthStatus.SUCCESS when the application request message was
   *         successfully validated. The validated request message is available
   *         by calling getRequestMessage on messageInfo.
   *
   *         <li>AuthStatus.SEND_SUCCESS to indicate that validation/processing
   *         of the request message successfully produced the secured
   *         application response message (in messageInfo). The secured response
   *         message is available by calling getResponseMessage on messageInfo.
   *
   *         <li>AuthStatus.SEND_CONTINUE to indicate that message validation is
   *         incomplete, and that a preliminary response was returned as the
   *         response message in messageInfo.
   *
   *         When this status value is returned to challenge an application
   *         request message, the challenged request must be saved by the
   *         authentication module such that it can be recovered when the
   *         module's validateRequest message is called to process the request
   *         returned for the challenge.
   *
   *         <li>AuthStatus.SEND_FAILURE to indicate that message validation
   *         failed and that an appropriate failure response message is
   *         available by calling getResponseMessage on messageInfo.
   *         </ul>
   *
   * @exception AuthException
   *              when the message processing failed without establishing a
   *              failure response message (in messageInfo).
   */
  @Override
  public AuthStatus validateRequest(MessageInfo messageInfo,
      Subject clientSubject, Subject serviceSubject) throws AuthException {

    String msg = "TSSendFailureServerAuthModule.validateRequest called";
    logMsg(msg);

    SOAPMessage smsg = null;

    try {

      MessageFactory mf = MessageFactory.newInstance();
      smsg = mf.createMessage();
      if (smsg != null) {
        setSOAPFault(smsg);
      }

    } catch (SOAPException ex) {
      ex.printStackTrace();
    }

    // set the response message with SOAP Fault
    messageInfo.setResponseMessage(smsg);

    return AuthStatus.SEND_FAILURE;
  }

  /**
   * Secure a service response before sending it to the client.
   *
   * This method is called to transform the response message acquired by calling
   * getResponseMessage (on messageInfo) into the mechanism specific form to be
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
   * @param serviceSubject
   *          a Subject that represents the source of the service response, or
   *          null. It may be used by the method implementation to retrieve
   *          Principals and credentials necessary to secure the response. If
   *          the Subject is not null, the method implementation may add
   *          additional Principals or credentials (pertaining to the source of
   *          the service response) to the Subject.
   *
   * @return an AuthStatus object representing the completion status of the
   *         processing performed by the method. The AuthStatus values that may
   *         be returned by this method are defined as follows:
   *
   *         <ul>
   *         <li>AuthStatus.SEND_SUCCESS when the application response message
   *         was successfully secured. The secured response message may be
   *         obtained by calling by getResponseMessage on messageInfo.
   *
   *         <li>AuthStatus.SEND_CONTINUE to indicate that the application
   *         response message (within messageInfo) was replaced with a security
   *         message that should elicit a security-specific response (in the
   *         form of a request) from the peer.
   *
   *         This status value serves to inform the calling runtime that (in
   *         order to successfully complete the message exchange) it will need
   *         to be capable of continuing the message dialog by processing at
   *         least one additional request/response exchange (after having sent
   *         the response message returned in messageInfo).
   *
   *         When this status value is returned, the application response must
   *         be saved by the authentication module such that it can be recovered
   *         when the module's validateRequest message is called to process the
   *         elicited response.
   *
   *         <li>AuthStatus.SEND_FAILURE to indicate that a failure occured
   *         while securing the response message and that an appropriate failure
   *         response message is available by calling getResponseMeessage on
   *         messageInfo.
   *         </ul>
   *
   * @exception AuthException
   *              when the message processing failed without establishing a
   *              failure response message (in messageInfo).
   */
  @Override
  public AuthStatus secureResponse(MessageInfo messageInfo,
      Subject serviceSubject) throws AuthException {
    String msg = "TSSendFailureServerAuthModule.secureResponse called";
    logMsg(msg);

    SOAPMessage smsg = null;

    try {

      MessageFactory mf = MessageFactory.newInstance();
      smsg = mf.createMessage();
      if (smsg != null) {
        setSOAPFault(smsg);
      }

    } catch (SOAPException ex) {
      ex.printStackTrace();
    }

    // set the response message with SOAP Fault
    messageInfo.setResponseMessage(smsg);

    return AuthStatus.SEND_FAILURE;
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
    logMsg("TSSendFailureServerAuthModule.cleanSubject called");
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

  private void setSOAPFault(SOAPMessage smsg) {
    SOAPPart soap = smsg.getSOAPPart();
    if (soap != null) {
      try {
        SOAPEnvelope envelope = soap.getEnvelope();
        if (envelope != null) {
          SOAPBody body = envelope.getBody();
          if (body != null) {
            QName qname = new QName("Server");
            body.addFault(qname, "Error in Server");

          }
        }
      } catch (SOAPException se) {
        logger.log(Level.INFO, "Error adding SOAP Fault", se);

      }
    }
  }

}
