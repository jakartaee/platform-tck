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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 *
 * @author Raja Perumal
 */
public class AuthDataCallbackHandler implements CallbackHandler {

  private String user;

  private String password;

  // Default constructor gets the user and password from the environment
  // using system property j2eelogin.name and j2eelogin.password
  public AuthDataCallbackHandler() {
    user = System.getProperty("j2eelogin.name");
    password = System.getProperty("j2eelogin.password");
  }

  public AuthDataCallbackHandler(String usr, String pwd) {
    user = usr;
    password = pwd;

  }

  public void handle(Callback[] callbacks) {
    for (Callback cb : callbacks) {
      if (cb instanceof NameCallback) {
        NameCallback nc = (NameCallback) cb;
        nc.setName(user);
      } else if (cb instanceof PasswordCallback) {
        PasswordCallback pc = (PasswordCallback) cb;
        if (password != null) {
          pc.setPassword(password.toCharArray());
        } else {
          pc.setPassword(null);
        }
      }
    }
  }

}
