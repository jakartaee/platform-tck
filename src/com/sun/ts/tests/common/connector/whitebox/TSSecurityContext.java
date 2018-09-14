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

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.work.SecurityContext;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.Subject;
import java.security.Principal;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
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
public class TSSecurityContext extends SecurityContext {

  private String userName = null; // server side username

  private String password = null; // server side pwd

  private String eisPrincipalName = null; // eis principal name

  private String description = null;

  private String logOutString = null;

  private boolean translationRequired; // true if case 2 security where we need
                                       // to map identities

  private boolean useCPC = true;

  private boolean useGPC = false;

  private boolean usePVC = false;

  private boolean addPrinToExecSubject = false;

  private boolean expectFailure = false;

  public TSSecurityContext(String userName, String password,
      String eisPrincipalName, boolean translationRequired) {
    this.userName = userName;
    this.password = password;
    this.eisPrincipalName = eisPrincipalName;
    this.translationRequired = translationRequired;

    this.description = super.getDescription();

    debug("TSSecurityContext:  userName=" + userName + "  password=" + password
        + "  eisPrincipalName=" + eisPrincipalName + "  translationRequired="
        + translationRequired);
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

  public void setPassword(String val) {
    this.password = val;
  }

  public String getPassword() {
    return this.password;
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

  public void setAddPrinToExecSubject(boolean val) {
    this.addPrinToExecSubject = val;
  }

  public boolean getAddPrinToExecSubject() {
    return this.addPrinToExecSubject;
  }

  public void setDescription(String val) {
    this.description = val;
  }

  public String getDescription() {
    return this.description;
  }

  public void setLogOutString(String val) {
    this.logOutString = val;
  }

  public String getLogOutString() {
    return this.logOutString;
  }

  public boolean isTranslationRequired() {
    return translationRequired;
  }

  public void setExpectFailure(boolean val) {
    this.expectFailure = val;
  }

  public boolean getExpectFailure() {
    return this.expectFailure;
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
   * Mostly, this is here to verify that the 3 main callbacks are supported by
   * the vendors app server and that they can be used with a a couple different
   * scenarios (eg. calling some but not all callbacks, or using null
   * principals, etc)
   *
   * Related notes on testing callbacks: 1. must call CPC after PVC (CPC and PVC
   * should use same user identities) 2. PVC is for case-1 security only (not
   * case-2 of mapping) 3. It's not spec required but recommended we call CPC
   * after GPC though there is allowance for a spec optimization which would
   * allow calling GPC without CPC but its a somewhat controversial optimization
   * so for now, if you have a GPC, you should follow with a CPC 4. CPC can be
   * alone 5. if CPC is called, it basically trumps PVC/GPC so it shouldnt even
   * matter what is in PVC/GPC.
   *
   */
  public void doCallbackVerification(CallbackHandler callbackHandler,
      Subject execSubject, Subject serviceSubject) {

    List<Callback> callbacks = new ArrayList<Callback>();
    PasswordValidationCallback pvc = null;
    GroupPrincipalCallback gpc = null;
    CallerPrincipalCallback cpc = null;
    String[] gpcGroups = { "phakegrp1", "phakegrp2" };

    debug("doCallbackVerification():  translationRequired = "
        + translationRequired);

    if (addPrinToExecSubject && (userName != null)) {
      debug("doCallbackVerification():  adding principal: " + userName
          + " to execSubject.");
      execSubject.getPrincipals().add(new SimplePrincipal(userName, password));
    }

    if (useGPC) {
      // we are passing invalid grps to the GPC but it should not
      // matter if the CPC is specified after the GPC.
      gpc = new GroupPrincipalCallback(execSubject, gpcGroups);
      debug(
          "doCallbackVerification(): - GPC with groups={phakegrp1, phakegrp2}");
      callbacks.add(gpc);
    }

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
        // the spec is unclear about initializating a PasswordValidationCallback
        // with a null
        // username so we should probably use something like a fake user name.
        // worth noting that the javadoc allows for null pwd.
        pvc = new PasswordValidationCallback(execSubject, "fk_usr", null);
        debug("setting PVC with user=fk_usr  and  password=null");
      }
      callbacks.add(pvc);
    }

    if (usePVC || useCPC) {
      if (translationRequired && (eisPrincipalName != null)) {
        // lets translate/map EIS to server domain prins
        debug("translationRequired, setting CPC with principal : "
            + eisPrincipalName);
        cpc = new CallerPrincipalCallback(execSubject,
            new SimplePrincipal(eisPrincipalName));
      } else if (!translationRequired && (userName != null)) {
        debug(
            "No translationRequired, setting CPC with userName : " + userName);
        cpc = new CallerPrincipalCallback(execSubject, userName);
      } else {
        debug("setting CPC with null Principal");
        cpc = new CallerPrincipalCallback(execSubject, (Principal) null);
      }

      callbacks.add(cpc);
    }

    Callback callbackArray[] = new Callback[callbacks.size()];
    try {
      callbackHandler.handle(callbacks.toArray(callbackArray));

      // if we made it here, then no exceptions - we can assume success since
      // we got no unsupported callback exceptions.
      String sval = "";
      if (this.logOutString != null) {
        sval = this.logOutString;
      } else {
        sval = "setupSecurityContext callbackhandler supports required callback types.";
      }
      ConnectorStatus.getConnectorStatus().logState(sval);
      debug(sval);

      // we shouldn't need to check if authentication succeeded - but for debug
      // purposes we will keep this here?
      if (!translationRequired && usePVC && (pvc != null)
          && (!pvc.getResult())) {
        debug("doCallbackVerification():  PVC failed");
        // ConnectorStatus.getConnectorStatus().logState("PVC failed");
      } else if (!translationRequired && usePVC && (pvc != null)) {
        debug("doCallbackVerification():  PVC succeeded");
        // ConnectorStatus.getConnectorStatus().logState("PVC succeeded");
      }

    } catch (UnsupportedCallbackException e) {
      String sval = "";
      if (this.expectFailure && (this.logOutString != null)) {
        sval = "Expected Exception: " + this.logOutString;
      } else {
        sval = "doCallbackVerification(): callbackhandler does not support a required callback type!";
      }
      ConnectorStatus.getConnectorStatus().logState(sval);
      debug(sval);
      debug("UnsupportedCallbackException message is : " + e.getMessage());
      e.printStackTrace();

    } catch (Exception e) {
      String sval = "";
      if (this.expectFailure && (this.logOutString != null)) {
        sval = "Expected Exception: " + this.logOutString;
      } else {
        sval = "doCallbackVerification(): callbackhandler threw unexpected exception!";
      }
      ConnectorStatus.getConnectorStatus().logState(sval);
      debug(sval);
      e.printStackTrace();
      debug("doCallbackVerification():  exception occured : " + e.getMessage());
    }

  }

  /*
   *
   * The executionSubject arg must be non-null and it must not be read-only. It
   * is expected that this method will populate this executionSubject with
   * principals and credentials that would be flown into the application server.
   *
   * The serviceSubject argument may be null. If it is not null, it must not be
   * read-only. (from ee spec, section 16.4.1) Note: this differs from javadoc
   * comments and insuch case, spec takes precedence. The serviceSubject
   * represents the application server and it may be used by the Work
   * implementation to retrieve Principals and credentials necessary to
   * establish a connection to the EIS (in the cause of mutual-auth like
   * scenarios). If the Subject is not null, the Work implementation may collect
   * the server cred as necessary, by using the callback handler passed to them
   * .
   */
  @Override
  public void setupSecurityContext(CallbackHandler callbackHandler,
      Subject execSubject, Subject serviceSubject) {

    // validate args are spec compliant
    validateCallbackHandler(callbackHandler);
    validateExecSubject(execSubject);
    validateServiceSubject(serviceSubject);

    // now make sure the 3 callback types are supported by the App Server
    doCallbackVerification(callbackHandler, execSubject, serviceSubject);

  }

  /*
   * this method is used to perform a simple validation that the callbackHandler
   * is spec compliant per assertion Connector:SPEC:229
   */
  private void validateCallbackHandler(CallbackHandler callbackHandler) {
    String str = "";

    // assist with assertion Connector:SPEC:229
    if (callbackHandler != null) {
      str = "setupSecurityContext() called with non-null callbackHandler";
    } else {
      str = "setupSecurityContext() called with invalid (null) callbackHandler";
    }
    debug(str);
    ConnectorStatus.getConnectorStatus().logState(str);

  }

  /*
   * this method is used to perform a simple validation that the execSubject is
   * spec compliant per assertion Connector:SPEC:230
   */
  private void validateExecSubject(Subject execSubject) {
    String str = "";

    if ((execSubject != null) && (!execSubject.isReadOnly())) {
      str = "setupSecurityContext() called with valid executionSubject";
    } else {
      str = "ERROR:  setupSecurityContext() called with invalid executionSubject";
    }
    debug(str);
    ConnectorStatus.getConnectorStatus().logState(str);

  }

  /*
   * this method is used to perform a simple validation that the serviceSubject
   * is spec compliant per assertion Connector:SPEC:231
   */
  private void validateServiceSubject(Subject serviceSubject) {
    String str = "";

    if (serviceSubject == null) {
      // this is allowed according to jca spec setion 16.4.1
      str = "setupSecurityContext() called with valid serviceSubject";
    } else if ((serviceSubject != null) && (!serviceSubject.isReadOnly())) {
      // this is good: if serviceSubject != null, then it must not be readonly
      str = "setupSecurityContext() called with valid serviceSubject";
    } else if ((serviceSubject != null) && (serviceSubject.isReadOnly())) {
      // ohoh, serviceSubject !=null but it is readonly and this is not valid!
      str = "setupSecurityContext() called with invalid executionSubject";
    }
    debug(str);
    ConnectorStatus.getConnectorStatus().logState(str);

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
    Debug.trace(message);
  }

}
