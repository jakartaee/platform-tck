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
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.Subject;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.logging.Level;

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
public class TSServerAuthContext
    implements javax.security.auth.message.config.ServerAuthContext {

  private static ServerAuthModule serverAuthModule = null;

  private static String messageLayer = null;

  private static String appContext = null;

  private static CallbackHandler handler = null;

  private static String operation = null;

  private static Subject clientSubject = null;

  private static Map properties = null;

  private static TSLogger logger = null;

  private static String soapUPTokenAppContext = "HelloService HelloPort";

  private static String servletUPTokenAppContext = "spitests_servlet_web";

  private static MessageInfo messageInfoFromVerifyReq = null;

  private static boolean isMandatory = false;

  /** Creates a new instance of ServerAuthContext */
  public TSServerAuthContext() {
  }

  public TSServerAuthContext(String messageLayer, String appContext,
      CallbackHandler handler, String operation, Subject clientSubject,
      Map properties, boolean isAuthMandatory, TSLogger tsLogger)
      throws AuthException {
    this(messageLayer, appContext, handler, operation, clientSubject,
        properties);
    logger = tsLogger;
    logger.log(Level.INFO, "TSServerAuthContext called");
    String logStr = "TSServerAuthContext called for messageLayer="
        + messageLayer + " : appContext=" + appContext;
    logger.log(Level.INFO, logStr);
    logStr += " : operation=" + operation;
    logger.log(Level.INFO, logStr);

    ServerAuthModule sam = null;
    properties.put("TSLogger", logger);

    isMandatory = isAuthMandatory;

    // Create a TargetPolicy for AUTHENTICATE_SENDER
    MessagePolicy.TargetPolicy msgTargetPolicy =

        new MessagePolicy.TargetPolicy(null,
            new MessagePolicy.ProtectionPolicy() {
              public String getID() {
                return MessagePolicy.ProtectionPolicy.AUTHENTICATE_SENDER;
              }
            });
    MessagePolicy.TargetPolicy[] msgTargetPolicies = { msgTargetPolicy };
    MessagePolicy requestMessagePolicy = new MessagePolicy(msgTargetPolicies,
        isMandatory);

    // Create a TargetPolicy for AUTHENTICATE_CONTENT
    MessagePolicy.TargetPolicy msgTargetPolicyContent =

        new MessagePolicy.TargetPolicy(null,
            new MessagePolicy.ProtectionPolicy() {
              public String getID() {
                return MessagePolicy.ProtectionPolicy.AUTHENTICATE_CONTENT;
              }
            });
    MessagePolicy.TargetPolicy[] msgTargetPoliciesContent = {
        msgTargetPolicyContent };
    MessagePolicy responseMessagePolicy = new MessagePolicy(
        msgTargetPoliciesContent, isMandatory);

    // Note: We could also choose auth modules based on appContext but
    // MessageLayer seems to be the ideal candidate for choosing
    // auth modules.
    if (messageLayer.equals(JASPICData.LAYER_SOAP)) {
      sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSServerAuthModule();

      if (appContext.equals(soapUPTokenAppContext)) {

        sam.initialize(requestMessagePolicy, responseMessagePolicy, handler,
            properties);
      } else if (appContext.indexOf("SendSuccessHello") > -1) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSSendSuccessServerAuthModule();
        sam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("SendFailureHello") > -1) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSSendFailureServerAuthModule();
        sam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("FailureHello") > -1) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSFailureServerAuthModule();
        sam.initialize(null, null, handler, properties);

      } else if (appContext.indexOf("AuthExceptionHello") > -1) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSAuthExceptionServerAuthModule();
        sam.initialize(null, null, handler, properties);

      } else {
        sam = new com.sun.ts.tests.jaspic.tssv.module.soap.TSServerAuthModule();
        sam.initialize(null, null, handler, properties);
      }

    } else if (messageLayer.equals(JASPICData.LAYER_SERVLET)) {
      System.out.println("AppContext =" + appContext);

      if (appContext.contains(servletUPTokenAppContext)
          && operation.contains("WrapperServlet")) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.servlet.TSServletWrapperSAM();
        sam.initialize(requestMessagePolicy, responseMessagePolicy, handler,
            properties);

      } else if (appContext.contains(servletUPTokenAppContext)) {
        sam = new com.sun.ts.tests.jaspic.tssv.module.servlet.TSServerAuthModule();
        sam.initialize(requestMessagePolicy, responseMessagePolicy, handler,
            properties);
      } else {
        sam = new com.sun.ts.tests.jaspic.tssv.module.servlet.TSServerAuthModule();
        sam.initialize(requestMessagePolicy, null, handler, properties);
      }
    }

    serverAuthModule = sam;
  }

  /*
   * This should be private so that we can be sure people pass a valid TSLogger
   * into the public constructor.
   */
  private TSServerAuthContext(String layer, String appCtxt,
      CallbackHandler hndlr, String optn, Subject cliSubject, Map props)
      throws AuthException {
    messageLayer = layer;
    appContext = appCtxt;
    handler = hndlr;
    operation = optn;
    clientSubject = cliSubject;
    properties = props;
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
   *         When paththis status value is returned to challenge an application
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
    Object reqObj = null;

    String msg = "TSServerAuthContext.validateRequest called";

    // this msg used to verify assertion: JASPI:SPEC:50
    logger.log(Level.INFO, msg);

    // this msg used to verify assertion: JASPI:SPEC:88
    msg += " for layer=" + messageLayer;
    logger.log(Level.INFO, msg);

    // the following msg used to verify assertion: JASPI:SPEC:89
    if (messageInfo != null) {
      reqObj = messageInfo.getRequestMessage();
      if (reqObj != null) {
        // if here, we want to see if our reqObj is type HttpServletRequest
        if (reqObj instanceof javax.servlet.http.HttpServletRequest) {
          String contextPath = ((HttpServletRequest) reqObj).getContextPath();
          String servletPath = ((HttpServletRequest) reqObj).getServletPath();

          // note: we are leaving off the pathInfo since we only want
          // to log contextPath and servletPath info so it's
          // more of a pseudo requestURI we are validating but
          // note that this will match our client so should be fine.
          String requestURI = contextPath + servletPath;
          msg += " for requestURI=" + requestURI;
        } else {
          msg += " reqObj instanceof=" + reqObj.getClass().getName();
        }
      } else {
        msg += " reqObj=NULL";
      }
    }

    // used to assist with the verification of assertion JASPI:SPEC:52
    String subjStr = messageLayer + " profile: ";
    if (clientSubject != null) {
      subjStr += "TSServerAuthContext.validateRequest called with non-null client Subject";
    } else {
      subjStr += "TSServerAuthContext.validateRequest called with null client Subject";
    }
    logger.log(Level.INFO, subjStr);

    // used to assist with the verification of assertion JASPI:SPEC:52
    verifyClientSubject(clientSubject);

    // used to assist with the verification of assertion JASPI:SPEC:53 and
    // JASPI:SPEC:313
    verifyServiceSubject(serviceSubject);

    AuthStatus rval = serverAuthModule.validateRequest(messageInfo,
        clientSubject, serviceSubject);

    logger.log(Level.INFO, msg);

    dumpAuthStatusString(rval, msg);

    // save off the MessageInfo object instance so that we can verify the
    // same one is used in the call to SecureRequest (JASPI:SPEC:60)
    messageInfoFromVerifyReq = messageInfo;

    return rval;
  }

  public void dumpAuthStatusString(AuthStatus rval, String msg) {

    if (msg == null) {
      msg = "";
    }

    if (rval == AuthStatus.SUCCESS) {
      msg += " AuthStatus=AuthStatus.SUCCESS";
    } else if (rval == AuthStatus.FAILURE) {
      msg += " AuthStatus=AuthStatus.FAILURE";
    } else if (rval == AuthStatus.SEND_SUCCESS) {
      msg += " AuthStatus=AuthStatus.SEND_SUCCESS";
    } else if (rval == AuthStatus.SEND_FAILURE) {
      msg += " AuthStatus=AuthStatus.SEND_FAILURE";
    } else if (rval == AuthStatus.SEND_CONTINUE) {
      msg += " AuthStatus=AuthStatus.SEND_CONTINUE";
    } else {
      msg += " AuthStatus=" + rval;
    }

    logger.log(Level.INFO, msg);
  }

  /*
   * This verifies the client subject object for validateRequest() calls. This
   * is used to assist with the verification of assertion JASPI:SPEC:52 by
   * logging information about the state of the clientSubject The spec (section
   * 2.1.5) states that clientSubject can not be null and that it can not be
   * read-only.
   * 
   */
  public void verifyClientSubject(Subject clientSubject) {
    String msg = "";

    if (clientSubject == null) {
      // this is failure against the spec
      msg = "FAILURE detected - ClientSubjects should not be null.";
    } else {
      msg = "Valid ClientSubjects - it was not null.";
    }
    logger.log(Level.INFO, msg);

    if (clientSubject != null) {
      if (clientSubject.isReadOnly()) {
        // this is failure against the spec
        msg = "FAILURE detected - ClientSubjects should not be read-only.";
      } else {
        msg = "Valid ClientSubjects - it was not read-only.";
      }
      logger.log(Level.INFO, msg);
    }

  }

  /*
   * This verifies the service subject object for validateRequest() calls. This
   * is used to assist with the verification of assertion JASPI:SPEC:53 by
   * logging information regarding wether or not the serviceSubject passed into
   * validateRequest was the same as what was used to acquire the
   * ServerAuthContext.
   */
  public void verifyServiceSubject(Subject serviceSubject) {
    String msg = "";
    String key = JASPICData.SVC_SUBJECT_KEY;
    Subject value = (Subject) properties.get(key);

    if (value != null) {
      msg = "got a non-null subject out of the map";
      logger.log(Level.INFO, msg);

      if (serviceSubject == null) {
        msg = "FAILURE detected - ServiceSubjects should be the same and are not.";
      } else if (value.equals(serviceSubject)) {
        msg = "ServiceSubjects correctly matched.";
      } else {
        // if here, serviceSubjects dont match but should
        msg = "FAILURE detected - ServiceSubjects should be the same and are not.";
      }
      logger.log(Level.INFO, msg);

      if ((serviceSubject != null) && (value.equals(serviceSubject))) {
        // to help verify assertion JASPI:SPEC:313 (per spec section 2.1.5.2):
        // If a non-null Subject was used to call the ServerAuthContext
        // the same Subject must be passed as the serviceSubject in this
        // call. If a non-null serviceSubject is used in this call, it
        // must not be read-only
        if (serviceSubject.isReadOnly()) {
          // should not get here.
          msg = "FAILURE detected - ServiceSubjects should not be read-only.";
          logger.log(Level.INFO, msg);
        } else {
          msg = "Valid ServiceSubjects - it was not read-only.";
          logger.log(Level.INFO, msg);
        }
      }
    }
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
    AuthStatus rval = AuthStatus.SUCCESS;
    Object reqObj = null;
    Object respObj = null;
    String msg = "TSServerAuthContext.secureResponse called";

    // this msg used to verify assertion: JASPI:SPEC:130
    logger.log(Level.INFO, msg);

    msg = "secureResponse called for layer=" + messageLayer;

    try {
      if (messageInfo != null) {

        verifyMessageInfoObjsMatch(messageInfo); // JASPI:SPEC:60

        reqObj = messageInfo.getRequestMessage();
        respObj = messageInfo.getResponseMessage();
        if (reqObj != null) {
          // if here, we want to see if our reqObj is type HttpServletRequest
          if (reqObj instanceof javax.servlet.http.HttpServletRequest) {
            String contextPath = ((HttpServletRequest) reqObj).getContextPath();
            String servletPath = ((HttpServletRequest) reqObj).getServletPath();

            // note: we are leaving off the pathInfo since we only want
            // to log contextPath and servletPath info so it's
            // more of a pseudo requestURI we are validating but
            // note that this will match our client so should be fine.
            String requestURI = contextPath + servletPath;
            msg += " for requestURI=" + requestURI;
          } else {
            msg += " reqObj instanceof=" + reqObj.getClass().getName();
          }
        } else {
          msg += " reqObj=NULL";
        }
      }
      logger.log(Level.INFO, msg);

      // used to assist with the verification of assertion JASPI:SPEC:61
      verifySecureRespServiceSubject(serviceSubject);

      rval = serverAuthModule.secureResponse(messageInfo, serviceSubject);
      msg = "";
      dumpAuthStatusString(rval, msg);

      if (rval.equals(AuthStatus.SUCCESS)) {
        // explicitly set return code but remember that we must be sure the
        // response we stuff into the MessageInfo is wrapped in a
        // HttpServletResponseWrapper object (jsr-196 spec section 3.8.3.4)
        if (respObj != null) {
          ((HttpServletResponse) respObj).setStatus(HttpServletResponse.SC_OK);
          HttpServletResponseWrapper respWrapper = null;
          if (!(reqObj instanceof javax.servlet.http.HttpServletResponseWrapper)) {
            respWrapper = new HttpServletResponseWrapper(
                (HttpServletResponse) respObj);
            respWrapper.setStatus(HttpServletResponse.SC_OK);
            messageInfo.setResponseMessage(respWrapper);
          } else {
            messageInfo.setResponseMessage((HttpServletResponse) respObj);
          }

          logger.log(Level.INFO,
              "Set the responseObjects return status to OK==200");
        } else {
          logger.log(Level.INFO,
              "ResponseObject == null so we could NOT set response status");
        }
      } else {
        logger.log(Level.INFO,
            "authStatus != SUCCESS so not setting response object status == 200");
      }
    } catch (AuthException ex) {
      logger.log(Level.INFO, "Got AuthException");
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      logger.log(Level.INFO, "Got generic Exception");
      ex.printStackTrace();
    }

    return rval;
  }

  /*
   * This is used to assist with verifying assertion: JASPI:SPEC:60 This is a
   * convenience method that is used to verify if the passed in messageInfo
   * object (which should be from secureRequest()) matches our previously saved
   * off messageInfo object (which should be from our last call to
   * validateRequest() - which we saved off in a static var.
   */
  public void verifyMessageInfoObjsMatch(MessageInfo messageInfo) {
    String err = "FAILURE:  MessageInfo object in secureRequest does not";
    err += " match the messageInfo object from validateRequest";

    String msg = "MessageInfo object from secureRequest matches the ";
    msg += " messageInfo object from validateRequest";

    // verify the passed in messageInfo
    if ((messageInfo == null) && (messageInfoFromVerifyReq == null)) {
      logger.log(Level.INFO, msg);
    } else if ((messageInfo == null) || (messageInfoFromVerifyReq == null)) {
      logger.log(Level.INFO, err);
      if (messageInfo == null) {
        msg = "Failure: secureRequest had null obj but validateRequest did not";
        logger.log(Level.INFO, msg);
      } else {
        msg = "Failure: validateRequest had null obj but secureResponse did not";
        logger.log(Level.INFO, msg);
      }
    } else if (!messageInfoFromVerifyReq.equals(messageInfo)) {
      logger.log(Level.INFO, err);
      logger.log(Level.INFO,
          "FAILURE: messageInfo objects non-null but don't match");
    } else {
      logger.log(Level.INFO, msg);
    }

    return;
  }

  /*
   * This verifies the service subject object for secureResponse() calls. Spec
   * says: (re: Pt 2 in MPR) "If a non-null serviceSubject is used in this call,
   * it must not be read-only and the same serviceSubject must be passed in the
   * call to secureResponse for the corresponding response (if there is one)."
   * 
   */
  public void verifySecureRespServiceSubject(Subject serviceSubject) {
    String msg = "";
    String key = JASPICData.SVC_SUBJECT_KEY;
    Subject value = (Subject) properties.get(key);

    if (value != null) {
      msg = "got a non-null subject out of the map";
      logger.log(Level.INFO, msg);

      if (serviceSubject == null) {
        msg = "FAILURE detected - SecureResponse ServiceSubjects should be the same and are not.";
      } else if (value.equals(serviceSubject)) {
        msg = "SecureResponse ServiceSubjects correctly matched.";
      } else {
        // if here, serviceSubjects dont match but should
        msg = "FAILURE detected - SecureResponse ServiceSubjects should be the same and are not.";
      }
      logger.log(Level.INFO, msg);

      if ((serviceSubject != null) && (value.equals(serviceSubject))) {
        // to help verify assertion JASPI:SPEC:313 (per spec section 2.1.5.2):
        // If a non-null Subject was used to call the ServerAuthContext
        // the same Subject must be passed as the serviceSubject in this
        // call. If a non-null serviceSubject is used in this call, it
        // must not be read-only
        if (serviceSubject.isReadOnly()) {
          // should not get here.
          msg = "FAILURE detected - SecureResponse ServiceSubjects should not be read-only.";
          logger.log(Level.INFO, msg);
        } else {
          msg = "Valid SecureResponse ServiceSubjects - it was not read-only.";
          logger.log(Level.INFO, msg);
        }
      }
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
    logger.log(Level.INFO, "TSServerAuthContext.cleanSubject called");
    serverAuthModule.cleanSubject(messageInfo, subject);
  }

}
