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
import java.util.logging.Level;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 *
 * @author Raja Perumal
 */
public class ClientCallbackSupport {
  private static TSLogger logger = null;

  private static CallbackHandler callbackHandler = null;

  private static String profile = null;

  private static final String runtimeType = "ClientRuntime";

  /** Creates a new instance of ClientCallbackSupport */
  public ClientCallbackSupport(TSLogger tsLogger, CallbackHandler cbkHandler,
      String profile) {
    logger = tsLogger;
    callbackHandler = cbkHandler;
    this.profile = profile;
  }

  public boolean verify() {
    try {

      NameCallbackSupport();
      PasswordCallbackSupport();
      return true;

    } catch (Exception e) {
      return false;
    }
  }

  private void NameCallbackSupport() {
    if (callbackHandler != null) {
      try {
        NameCallback nameCallback = new NameCallback("Please enter your name :",
            "j2ee");
        nameCallback.setName("j2ee");

        Callback[] callbacks = new Callback[] { nameCallback };

        callbackHandler.handle(callbacks);
        String returnedName = nameCallback.getName();

        if (returnedName != null) {
          logMsg("Name returned from Name Callback =" + returnedName);
        }
        logMsg("CallbackHandler supports NameCallback");
      } catch (UnsupportedCallbackException usce) {
        logMsg("CallbackHandler failed to support NameCallback :"
            + usce.getMessage());
        usce.printStackTrace();
      } catch (IOException ioe) {
        logMsg("CallbackHandler failed to support NameCallback :"
            + ioe.getMessage());
        ioe.printStackTrace();
      }

    }

  }

  private void PasswordCallbackSupport() {
    if (callbackHandler != null) {
      try {
        PasswordCallback passwordCallback = new PasswordCallback(
            "Please enter your password :", false);
        passwordCallback.setPassword(new char[] { 'j', '2', 'e', 'e' });

        Callback[] callbacks = new Callback[] { passwordCallback };

        callbackHandler.handle(callbacks);
        char returnedPassword[] = passwordCallback.getPassword();

        if (returnedPassword != null) {
          logMsg("Password returned from Password Callback ="
              + new String(returnedPassword));
        }
        logMsg("CallbackHandler supports PasswordCallback");
      } catch (UnsupportedCallbackException usce) {
        logMsg("CallbackHandler failed to support PasswordCallback :"
            + usce.getMessage());
        usce.printStackTrace();
      } catch (IOException ioe) {
        logMsg("CallbackHandler failed to support PasswordCallback :"
            + ioe.getMessage());
        ioe.printStackTrace();
      }

    }

  }

  public void logMsg(String str) {
    if (logger != null) {
      logger.log(Level.INFO, "In " + profile + " : " + runtimeType + " " + str);
    } else {
      System.out.println("*** TSLogger Not Initialized properly ***");
      System.out.println("*** TSSVLogMessage : ***" + str);
    }
  }

}
