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

package com.sun.ts.tests.jaspic.tssv.util;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.Set;
import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletRequest;

import com.sun.ts.lib.util.BASE64Decoder;

import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;

/**
 *
 * @author Raja Perumal
 */
public class ServerCallbackSupport {

  private static TSLogger logger = null;

  private static CallbackHandler callbackHandler = null;

  private static String profile = null;

  private static final String runtimeType = "ServerRuntime";

  private static MessageInfo messageInfo = null;

  private static Subject clientSubject = null;

  private static Subject serverSubject = null;

  // user corresponds to ts.jte user property (e.g. "j2ee")
  private static String user = System.getProperty("j2eelogin.name");

  // password corresponds to ts.jte password property (e.g. "j2ee")
  private static String passwd = System.getProperty("j2eelogin.password");

  /** Creates a new instance of ServerCallbackSupport */
  public ServerCallbackSupport(TSLogger tsLogger, CallbackHandler cbkHandler,
      String profile) {
    logger = tsLogger;
    callbackHandler = cbkHandler;
    this.profile = profile;
  }

  public ServerCallbackSupport(TSLogger tsLogger, CallbackHandler cbkHandler,
      String profile, MessageInfo msgInfo, Subject clientSubj,
      Subject serverSubj) {
    logger = tsLogger;
    callbackHandler = cbkHandler;
    this.profile = profile;
    this.messageInfo = msgInfo;
    this.clientSubject = clientSubj;
    this.serverSubject = serverSubj;
  }

  public boolean verify() {
    try {
      CallerPrincipalCallbackSupport();
      GroupPrincipalCallbackSupport();
      PasswordValidationCallbackSupport();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean verifyCPCCallback() {
    boolean bval = CallerPrincipalCallbackSupport();
    logMsg("verifyCPCCallback returning " + Boolean.toString(bval));
    return bval;
  }

  public boolean verifyGPCCallback() {
    boolean bval = GroupPrincipalCallbackSupport();
    logMsg("verifyGPCCallback returning " + Boolean.toString(bval));
    return bval;
  }

  public boolean verifyPVCCallback() {
    boolean bval = PasswordValidationCallbackSupport();
    logMsg("verifyPVCCallback returning " + Boolean.toString(bval));
    return bval;
  }

  private boolean CallerPrincipalCallbackSupport() {
    boolean bval = false;
    HttpServletRequest request = null;

    try {
      request = (HttpServletRequest) messageInfo.getRequestMessage();
    } catch (Exception ex) {
    }

    if (callbackHandler != null) {
      try {
        // note: we should be able to have a subject that has NO
        // principals for the case of optional authen. Which means
        // we should not have to explicitly set the principal here.
        // however, for the case of mandatory authen, we will want
        // to create a CPC using a username as opposed to a null principal.

        CallerPrincipalCallback callerPrincipalCallback = null;
        if (profile.equals(JASPICData.LAYER_SERVLET)) {
          Principal principal = null;
          if (messageInfo != null) {
            HttpServletRequest req = (HttpServletRequest) messageInfo
                .getRequestMessage();
            String username = getServletUsername(req);
            String principalName = getPrincipalNameFromSubject(clientSubject);
            String nameToLog = null;

            // better to call cbh with a null principal when the policy is Not
            // mandatory
            // and with a legitimate principal when the policy is mandatory
            // (unless testing send-failure or send-continue - which we are
            // not!)
            boolean bIsMandatory = isServletAuthMandatory(messageInfo);
            if (bIsMandatory) {
              logMsg(
                  "CallerPrincipalCallbackSupport() Authentication mandatory");
              if (username != null) {
                logMsg(
                    "CallerPrincipalCallbackSupport() auth mandatory, username != null");
                callerPrincipalCallback = new CallerPrincipalCallback(
                    clientSubject, username);
                nameToLog = username;
              } else if (principalName != null) {
                logMsg(
                    "CallerPrincipalCallbackSupport() auth mandatory, principalName != null");
                callerPrincipalCallback = new CallerPrincipalCallback(
                    clientSubject, principalName);
                nameToLog = principalName;
              } else {
                logMsg(
                    "CallerPrincipalCallbackSupport() auth mandatory, username and principalName both == null");
              }
            } else {
              logMsg(
                  "CallerPrincipalCallbackSupport() Authentication NOT mandatory");
              callerPrincipalCallback = new CallerPrincipalCallback(
                  clientSubject, (Principal) null);
            }

            // now for some simple invocations to ensure we can call the API's
            // these lines serve no other purpose than to validate we can
            // invoke the api's in order to satisfy the javadoc assertions of:
            // JSR-196:JAVADOC:32, JSR-196:JAVADOC:33, JSR-196:JAVADOC:34
            String cpcbkName = callerPrincipalCallback.getName();
            Principal cpcbkPrin = callerPrincipalCallback.getPrincipal();
            Subject cpcbkSubj = callerPrincipalCallback.getSubject();

            String msg = "CallerPrincipalCallback called for profile="
                + profile;
            if (request != null) {
              String servletPath = request.getContextPath()
                  + request.getServletPath();
              msg += " for servletPath=" + servletPath;
            } else {
              msg += " messageInfo contained null request";
            }
            logMsg(msg);

            // this helps test JASPIC:SPEC:103
            if (clientSubject == null) {
              msg += " subject=null";
            } else {
              msg += " subject=non-null";
            }
            msg += " principal set = " + nameToLog;
            logMsg(msg);

          } else {
            // uses a null principal
            callerPrincipalCallback = new CallerPrincipalCallback(clientSubject,
                principal);
          }
        } else {
          // should not get into here.
          logMsg(
              "WARNING:  ServerCallbackSupport.CallerPrincipalCallbackSupport() - profile != servlet.");
          Subject subject = new Subject();
          callerPrincipalCallback = new CallerPrincipalCallback(subject,
              (Principal) null);
        }

        Callback[] callbacks = new Callback[] { callerPrincipalCallback };

        callbackHandler.handle(callbacks);

        logMsg("CallbackHandler supports CallerPrincipalCallback");
        bval = true; // if here assume successful authen

      } catch (UnsupportedCallbackException usce) {
        logMsg("CallbackHandler failed to support CallerPrincipalCallback :"
            + usce.getMessage());
        usce.printStackTrace();

      } catch (Exception ex) {
        // failed CPC authentication for unknown reason
        String servletPath = "";
        if (request != null) {
          servletPath = request.getContextPath() + request.getServletPath();
        } else {
          servletPath = "WARNING:  can't determine servletpath";
        }
        logMsg("CPC Exception failure for servletPath=" + servletPath);
        ex.printStackTrace();
      }

    } else {
      String msg = "CallerPrincipalCallback has a null callbackHandler";
      msg += "  profile=" + profile;
      if (profile.equals(JASPICData.LAYER_SERVLET) && (messageInfo != null)) {
        // HttpServletRequest request =
        // (HttpServletRequest)messageInfo.getRequestMessage();
        if (request != null) {
          String servletPath = request.getContextPath()
              + request.getServletPath();
          msg += " for servletPath=" + servletPath;
        } else {
          msg += " messageInfo contained null request";
        }
        logMsg(msg);

        if (clientSubject == null) {
          msg += " subject=null";
        } else {
          msg += " subject=non-null";
        }
        String principalConcatString = getPrincipalNameFromSubject(
            clientSubject);
        msg += " principal set = " + principalConcatString;

      }
      logMsg(msg);
    }

    return bval;
  }

  /*
   * This is a convenience method. It is used to help pull out the username from
   * the request. This method will only pull out a username if there was a
   * client side servlet request made where Basic auth was used. This will only
   * succeed if: 1. We have a ServletRequest that was populated correctly 2. The
   * client side used BASE64Encoder() to encode user/pwd info 3. The user/pwd
   * info were encoded in a format similar to the the following: String authData
   * = username+":"+password BASE64Encode.encode(authData.getBytes())
   *
   * This returns just the decoded username.
   *
   */
  private String getServletUsername(HttpServletRequest req) {
    String username = null;
    String authorization = req.getHeader("authorization");
    BASE64Decoder decoder = new BASE64Decoder();

    if ((authorization != null) && (authorization.startsWith("Basic "))) {
      try {
        String authStr = authorization.substring(6).trim();
        String value = new String(decoder.decodeBuffer(authStr));
        logMsg("decoded (request) authorization string of: " + value);

        // at this point value should be in the form of <username>:<pwd>
        if (value != null) {
          username = value.substring(0, value.indexOf(":"));
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    return username;
  }

  /*
   * Convenience method to get our context info. (This currently only works for
   * servlet profile since thats all we need it for at this point.)
   *
   */
  private String getServletContext(MessageInfo mInfo, String profile) {
    String sContext = "";
    if (profile.equals(JASPICData.LAYER_SERVLET) && (mInfo != null)) {
      HttpServletRequest req = (HttpServletRequest) mInfo.getRequestMessage();
      if (req != null) {
        sContext = req.getContextPath() + req.getServletPath();
      }
    }

    logger.log(Level.INFO, "getServletContext() returning " + sContext);

    return sContext;
  }

  /*
   * This is a convenience method that is used to determine if Authentication is
   * mandatory. Based on the answer, there are certain requirements that will
   * need to be met wrt setting of principals.
   */
  private boolean isServletAuthMandatory(MessageInfo msgInfo) {
    boolean bval = false;
    Map map = msgInfo.getMap();

    // lets pull out some context info that we can use to help uniquely
    // identify the source of this request
    HttpServletRequest request = (HttpServletRequest) msgInfo
        .getRequestMessage();

    String servletName = request.getServletPath();

    // see assertion JASPI:SPEC:306 for details on this
    // jsr-196 states the following key must exist for servlet profile
    String strKey = "javax.security.auth.message.MessagePolicy.isMandatory";
    String msg;
    if (map != null) {
      String keyVal = (String) map.get(strKey);
      msg = "isServletAuthMandatory() called with attrs: ";
      msg += " layer=" + JASPICData.LAYER_SERVLET;
      msg += " servletName=" + servletName;
      msg += " key=" + strKey;

      if (keyVal == null) {
        msg += " value=NULL";
        bval = false; // assume false if we cant determine
      } else if (Boolean.valueOf(keyVal).booleanValue() == true) {
        msg += " value=Valid";
        bval = true;
      } else {
        // assume false
        msg += " value=false";
        bval = false;
      }
      logger.log(Level.FINE, msg);
    } else {
      msg = "FAILURE:  No map in MessageInfo thus no key=" + strKey;
      logger.log(Level.SEVERE, msg);
    }

    return bval;
  }

  public String getPrincipalNameFromSubject(Subject sub) {
    Principal principal = null;

    if (sub == null) {
      return (String) null;
    }

    String concatPrincipalName = "";
    Set principalSet = sub.getPrincipals();

    Iterator iterator = principalSet.iterator();
    while (iterator.hasNext()) {
      principal = (Principal) iterator.next();
      concatPrincipalName += principal.getName();
    }

    return concatPrincipalName;
  }

  private boolean GroupPrincipalCallbackSupport() {
    boolean bval = false;
    ;
    boolean isAuthMandatory = false;
    String strServletContext = "";
    String principalName = "";

    if (callbackHandler != null) {
      try {
        // note: we should be able to have a subject that has NO
        // principals for the case of optional authen. Which means
        // we should not have to explicitly set the principal here.
        // however, for the case of mandatory authen, we will want
        // to create a CPC using a username as opposed to a null principal.

        Subject subject = new Subject();
        String strArray[] = { "Administrator" };
        GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(
            subject, strArray);

        CallerPrincipalCallback callerPrincipalCallback = null;
        if (profile.equals(JASPICData.LAYER_SERVLET)) {
          if (messageInfo != null) {
            HttpServletRequest req = (HttpServletRequest) messageInfo
                .getRequestMessage();
            String username = getServletUsername(req);
            principalName = getPrincipalNameFromSubject(clientSubject);
            strServletContext = req.getServletPath();

            // better to call cbh with a null principal when the policy is Not
            // mandatory
            // and with a legitimate principal when the policy is mandatory
            // (unless testing send-failure or send-continue - which we are
            // not!)
            boolean bIsMandatory = isServletAuthMandatory(messageInfo);
            if (bIsMandatory) {
              isAuthMandatory = true;
              debug("GroupPrincipalCallbackSupport() Authentication mandatory");
              if (username != null) {
                debug(
                    "GroupPrincipalCallbackSupport() auth mandatory, username != null");
                callerPrincipalCallback = new CallerPrincipalCallback(
                    clientSubject, username);
              } else if (principalName != null) {
                debug(
                    "GroupPrincipalCallbackSupport() auth mandatory, principalName != null");
                callerPrincipalCallback = new CallerPrincipalCallback(
                    clientSubject, principalName);
              } else {
                logMsg(
                    "GroupPrincipalCallbackSupport() auth mandatory, username and principalName both == null");
              }
            } else {
              debug(
                  "GroupPrincipalCallbackSupport() Authentication NOT mandatory");
              callerPrincipalCallback = new CallerPrincipalCallback(subject,
                  (Principal) null);
            }
          } else {
            // uses a null principal
            debug(
                "GroupPrincipalCallbackSupport(): messageInfo == null, using null principal");
            callerPrincipalCallback = new CallerPrincipalCallback(clientSubject,
                (Principal) null);
          }
        } else {
          // if here, we were erroneously called by non-servlet profile
          debug(
              "WARNING:  ServerCallbackSupport.CallerPrincipalCallbackSupport() - profile != servlet.");
          callerPrincipalCallback = new CallerPrincipalCallback(subject,
              (Principal) null);
        }

        Callback[] callbacks = new Callback[] { groupPrincipalCallback,
            callerPrincipalCallback };
        callbackHandler.handle(callbacks);

        // this string will be searched for on client side
        String theMessage = "GroupPrincipalCallbackSupport():";
        theMessage += " successfully called callbackHandler.handle(callbacks)";
        theMessage += " for servlet: " + strServletContext;
        theMessage += " with isServletAuthMandatory = " + isAuthMandatory;
        logMsg(theMessage);

        logMsg("CallbackHandler supports GroupPrincipalCallback");

        bval = true; // if here assume successful authen

      } catch (UnsupportedCallbackException usce) {
        logMsg("CallbackHandler failed to support GroupPrincipalCallback :"
            + usce.getMessage());
        usce.printStackTrace();
      } catch (IOException ioe) {
        logMsg("CallbackHandler failed to support GroupPrincipalCallback :"
            + ioe.getMessage());
        ioe.printStackTrace();
      }
    }

    return bval;
  }

  private boolean PasswordValidationCallbackSupport() {
    boolean bval = false;

    if (callbackHandler != null) {
      try {
        Subject subject = new Subject();
        String username = user; // e.g. "j2ee";
        char[] password = passwd.toCharArray(); // e.g. {'j','2','e','e'};

        PasswordValidationCallback passwordValidationCallback = new PasswordValidationCallback(
            subject, username, password);

        CallerPrincipalCallback cpc = new CallerPrincipalCallback(subject,
            (Principal) null);

        Callback[] callbacks = new Callback[] { passwordValidationCallback,
            cpc };

        callbackHandler.handle(callbacks);
        Subject returnedSubject = passwordValidationCallback.getSubject();
        boolean result = passwordValidationCallback.getResult();
        String userName = passwordValidationCallback.getUsername();
        char[] returnedPassword = passwordValidationCallback.getPassword();
        passwordValidationCallback.clearPassword();

        // logMsg("PasswordValidation callback returned subject
        // ="+returnedSubject);
        // logMsg("PasswordValidation callback returned password
        // ="+returnedPassword);
        logMsg("PasswordValidation callback returned result =" + result);
        logMsg("CallbackHandler supports PasswordValidationCallback");

        bval = result;

      } catch (UnsupportedCallbackException usce) {
        logMsg("CallbackHandler failed to support PasswordValidationCallback :"
            + usce.getMessage());
        usce.printStackTrace();
      } catch (IOException ioe) {
        logMsg("CallbackHandler failed to support PasswordValidationCallback :"
            + ioe.getMessage());
        ioe.printStackTrace();
      }
    }

    return bval;
  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, "In " + profile + " : " + runtimeType + " " + str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
