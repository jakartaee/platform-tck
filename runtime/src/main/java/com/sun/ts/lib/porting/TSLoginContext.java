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

/*
 * $Id$
 */
package com.sun.ts.lib.porting;

import com.sun.ts.lib.util.*;

/**
 * TSLoginContext provides the implementation specific code for allowing a
 * program to login as a specific user. This class is implemented as a wrapper
 * class around Sun's login implementation code.
 */
public class TSLoginContext implements TSLoginContextInterface {
  private TSLoginContextInterface ctsLogin = null;

  private String sClass = "porting.ts.login.class.1";

  /**
   * Provides the LoginContext needed to perform a login.
   */
  public TSLoginContext() throws Exception {
    // move initialization to login method, to make sure that TestUtil's
    // properties are already initialized
  }

  /**
   * Provides the LoginContext needed to perform a login.
   */
  public TSLoginContext(String sClassName) throws Exception {
    sClass = sClassName;
  }

  /**
   * Performs the login functionality.
   *
   * @param usr
   *          the username to login
   * @param pwd
   *          the password of user
   */
  public void login(String usr, String pwd) throws Exception {
    if (ctsLogin == null) {
      init();
    }
    ctsLogin.login(usr, pwd);
  }

  /**
   * This login method is used for certificate based login
   *
   * Note: This method also uses keystore and keystore password from the TS
   * configuration file
   *
   * @param useralias
   *          - alias is used to pick up the certificate from keystore
   */

  public void login(String useralias) throws Exception {
    if (ctsLogin == null) {
      init();
    }
    ctsLogin.login(useralias);
  }

  /**
   * This login method is used for certificate based login
   *
   * @param useralias
   *          - alias used to pickup the certificate from keystore
   * @param keystore
   *          - keystore file
   * @param keyPass
   *          - keystore password
   */

  public void login(String useralias, String keystore, String keyPass)
      throws Exception {
    if (ctsLogin == null) {
      init();
    }
    ctsLogin.login(useralias, keystore, keyPass);
  }

  /**
   * Performs logout.
   */
  public Boolean logout() {
    if (ctsLogin == null) {
      init();
    }
    return ctsLogin.logout();
  }

  private void init() {
    try {
      // create and initialize a new instance of the Login class
      Class c = Class.forName(TestUtil.getProperty(sClass));
      ctsLogin = (TSLoginContextInterface) c.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      // throw e;
    }
  }
}
