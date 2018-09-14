/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.mdcomplete;

import javax.resource.spi.work.SecurityContext;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.Subject;
import java.security.Principal;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.SimplePrincipal;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.sun.ts.tests.common.connector.util.*;

/*
 * This is used to facilitate testing of the SecurityContext class.
 * The things to note/remember about this class are:
 *  - there are two types of security scenarios RA's utilize for 
 *    passing creds:  Case-1 and Case-2 security.
 *    Case-1:  use creds that are expected to exist on the Appserver
 *    Case-2:  set up mappings in appserver (to map EIS creds to AS creds)
 *             then the RA can flow EIS creds to AS and let AS handle mappings.
 *  - a RA can NOT do case-1 and case-2 at the same time.  a RA must be 
 *    configured to do only ONE case at a time.  (the configuration is usually
 *    done by AS ina proprietary way.  For RI(GFv3), Case-1 is default, and 
 *    Case-2 is done by specifying mapping in domain.xml.
 *  - CPC *must* be called after GPC and PVC 
 *  - PVC *should* have same creds as CPC
 *  - due to spec optimization, GPC can be called without CPC but this 
 *    is somewhat controversial and not recommended.
 * 
 */
public class TSNestedSecurityContext extends SecurityContext {

  private String userName; // server side username

  private String password; // server side pwd

  private String eisPrincipalName; // eis principal name

  private boolean translationRequired;

  private Subject subject;

  private String description;

  private String sicName;

  protected boolean expectPVCSuccess = true;

  // unlike TSSecurityContext, we want these all enabled by default
  private boolean useCPC = true;

  private boolean useGPC = true;

  private boolean usePVC = true;

  public TSNestedSecurityContext(String userName, String password,
      String eisPrincipalName, boolean translationRequired) {
    this(userName, password, eisPrincipalName, translationRequired, true);
  }

  public TSNestedSecurityContext(String userName, String password,
      String eisPrincipalName, boolean translationRequired,
      boolean expectSuccess) {
    this.userName = userName;
    this.password = password;
    this.eisPrincipalName = eisPrincipalName;
    this.translationRequired = translationRequired;

    this.sicName = super.getName();
    this.description = super.getDescription();
    this.expectPVCSuccess = expectSuccess;
  }

  public void setCallbacks(boolean bCPC, boolean bGPC, boolean bPVC) {
    this.useCPC = bCPC;
    this.useGPC = bGPC;
    this.usePVC = bPVC;
  }

  public void setUserName(String val) {
    this.userName = val;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setDescription(String val) {
    this.description = val;
  }

  public String getDescription() {
    return this.description;
  }

  public void setName(String val) {
    this.sicName = val;
  }

  public String getName() {
    return this.sicName;
  }

  public void setUseCPC(boolean val) {
    this.useCPC = val;
  }

  public boolean getUseCPC() {
    return this.useCPC;
  }

  public void setUseGPC(boolean val) {
    this.useGPC = val;
  }

  public boolean getUseGPC() {
    return this.useGPC;
  }

  public void setUsePVC(boolean val) {
    this.usePVC = val;
  }

  public boolean getUsePVC() {
    return this.usePVC;
  }

  public boolean isTranslationRequired() {
    return translationRequired;
  }

  /*
   * This is used to help verify assertion Connector:SPEC:229, which states a
   * couple requirements with the following being focused on within this method:
   * "The following conditions are applicable to the application server provider
   * while calling the setupSecurityContext method: the CallbackHandler
   * implementation passed as the argument handler to setupSecurityContext must
   * support the following JSR-196 Callbacks: CallerPrincipalCallback,
   * GroupPrincipalCallback, and PasswordValidationCallback"
   *
   */
  public void doCallbackVerification(CallbackHandler callbackHandler,
      Subject execSubject, Subject serviceSubject, Principal principal) {
    List<Callback> callbacks = new ArrayList<Callback>();

    debug("in doCallbackVerification() " + " translationRequired="
        + translationRequired + " expectPVCSuccess=" + expectPVCSuccess);

    GroupPrincipalCallback gpc = null;
    String[] gpcGroups = { "phakegrp1", "phakegrp2" };
    if (useGPC) {
      // we are passing invalid grps to the GPC but it should not
      // matter if the CPC is specified after the GPC.
      debug("doCallbackVerification():  initializing PVC");
      gpc = new GroupPrincipalCallback(execSubject, gpcGroups);
      debug("GPC with groups={phakegrp1, phakegrp2} ");
      callbacks.add(gpc);
    }

    PasswordValidationCallback pvc = null;
    if (usePVC && !translationRequired) {
      // per JCA 1.6 spec (16.4.2) PVC can be used in Case-1 only.
      // NOTE: PVC user should match that of CPC
      debug("doCallbackVerification():  initializing PVC");
      char[] pwd = null;
      if (password != null) {
        // if password is supplied - use that first
        pwd = password.toCharArray();
      }

      if (userName != null) {
        // if userName is supplied - use that first
        pvc = new PasswordValidationCallback(execSubject, userName, pwd);
        debug("setting PVC with user [ " + userName + " ] + password [ "
            + password + " ]");
      } else {
        // null username - likely a problem if here.
        pvc = new PasswordValidationCallback(execSubject, null, pwd);
        debug("setting PVC with user=null  + password [ " + password + " ]");
      }
      callbacks.add(pvc);
    }

    CallerPrincipalCallback cpc = null;
    if (usePVC || useCPC) {
      execSubject.getPrincipals().add(new SimplePrincipal(userName, password));
      debug("setting CPC with userName : " + userName + "   pwd = " + password);
      cpc = new CallerPrincipalCallback(execSubject, userName);

      callbacks.add(cpc);
    }

    Callback callbackArray[] = new Callback[callbacks.size()];
    try {
      callbackHandler.handle(callbacks.toArray(callbackArray));

      // if we made it here, then no exceptions - we can assume success since
      // we got no unsupported callback exceptions.
      String sval = "setupSecurityContext callbackhandler supports required callback types.";
      debug(sval);

      if ((pvc != null) && (!pvc.getResult())) {
        sval = "Password validation callback failure for userName = "
            + userName;
        debug(sval);
        if (this.expectPVCSuccess) {
          String str = "ERROR: got unexpected PVC failed for user " + userName;
          debug(str);
          throw new Error(str);
        } else {
          // NOTE: if here - we expected to fail and it happened - this is good!
          String str = "TSNestedSecurityContext expected PVC failure and got it.";
          ConnectorStatus.getConnectorStatus().logState(str);
          debug(str);
        }
      } else {
        // this file is designed so that we *should* be getting a pvc failure
        // back but
        // if we are in here, it implies we had something go wrong.
        if (pvc == null) {
          debug("ERROR :  pvc = null but should have be non-null");
          debug("usePVC = " + usePVC);
        } else {
          debug("ERROR :  pvc.getResult()=" + pvc.getResult());
          debug("usePVC = " + usePVC);
        }
      }

    } catch (UnsupportedCallbackException e) {
      String sval = "setupSecurityContext() callbackhandler does not support a required callback type!";
      debug("doCallbackVerification():  " + sval);
      debug("UnsupportedCallbackException message is : " + e.getMessage());
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
      debug("doCallbackVerification():  exception occured : " + e.getMessage());
    }

  }

  public void setupSecurityContext(CallbackHandler callbackHandler,
      Subject execSubject, Subject serviceSubject) {

    // validate args are spec compliant
    validateCallbackHandler(callbackHandler);
    validateExecSubject(execSubject);
    validateServiceSubject(serviceSubject);

    Principal principal = null;
    if (translationRequired && (eisPrincipalName != null)) {
      // add eis principal that needs a security mapping in app server domain
      principal = new SimplePrincipal(eisPrincipalName);
      debug(
          "setupSecurityContext():  translationRequired && (eisPrincipalName != null)");
    } else if (!translationRequired && (userName != null)) {
      // add principal that exists in App Server Security domain
      principal = new SimplePrincipal(userName);
      debug(
          "setupSecurityContext():  !translationRequired && (userName != null)");
    }

    // assist with assertion Connector:SPEC:229
    if (callbackHandler != null) {
      String str = "setupSecurityContext() called with non-null callbackHandler";
      debug(str);

      // now make sure the 3 callback types are supported by the App Server
      doCallbackVerification(callbackHandler, execSubject, serviceSubject,
          principal);

    } else {
      debug(
          "setupSecurityContext() called with invalid (null) callbackHandler");
    }

  }

  /*
   * this method is used to perform a simple validation that the callbackHandler
   * is spec compliant per assertion Connector:SPEC:229
   */
  private void validateCallbackHandler(CallbackHandler callbackHandler) {

    // assist with assertion Connector:SPEC:229
    if (callbackHandler != null) {
      String str = "setupSecurityContext() called with non-null callbackHandler";
      debug(str);
    } else {
      String str = "setupSecurityContext() called with invalid (null) callbackHandler";
      debug(str);
    }
  }

  /*
   * this method is used to perform a simple validation that the execSubject is
   * spec compliant per assertion Connector:SPEC:230
   */
  private void validateExecSubject(Subject execSubject) {

    if ((execSubject != null) && (!execSubject.isReadOnly())) {
      String str = "setupSecurityContext() called with valid executionSubject";
      debug(str);
    } else {
      String str = "ERROR:  setupSecurityContext() called with invalid executionSubject";
      debug(str);
    }

  }

  /*
   * this method is used to perform a simple validation that the serviceSubject
   * is spec compliant per assertion Connector:SPEC:231
   */
  private void validateServiceSubject(Subject serviceSubject) {

    if ((serviceSubject != null) && (!serviceSubject.isReadOnly())) {
      // this is good: if serviceSubject != null, then it must not be readonly
      String str = "setupSecurityContext() called with valid serviceSubject";
      debug(str);
    } else if ((serviceSubject != null) && (serviceSubject.isReadOnly())) {
      // ohoh, serviceSubject !=null but it is readonly and this is not valid!
      String str = "setupSecurityContext() called with invalid executionSubject";
      debug(str);
    } else if (serviceSubject == null) {
      // invalid serviceSubject called - according to API doc -it cant be null
      String str = "ERROR - setupSecurityContext() called with null serviceSubject.";
    } else {
      // this is also a valid serviceSubject for our setupSecurityContext()
      String str = "setupSecurityContext() called with valid serviceSubject";
      debug(str);
    }

  }

  public Subject getSubject() {
    if (translationRequired) {
      if (subject == null) {
        // setting translation required for principal
        subject = new Subject();
        subject.getPrincipals().add(new SimplePrincipal(eisPrincipalName));
      }
      return subject;
    } else {
      return null;
    }
  }

  public String toString() {
    StringBuffer toString = new StringBuffer("{");
    toString.append("userName : " + userName);
    toString.append(", password : " + password);
    toString.append(", eisPrincipalName : " + eisPrincipalName);
    toString.append(", translationRequired : " + translationRequired);
    toString.append("}");
    return toString.toString();
  }

  public void debug(String message) {
    Debug.trace(" in TSNestedSecurityContext: " + message);
  }

}
