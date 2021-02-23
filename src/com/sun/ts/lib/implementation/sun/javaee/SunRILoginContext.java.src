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

package com.sun.ts.lib.implementation.sun.javaee;

import com.sun.ts.lib.porting.TSLoginContextInterface;
import com.sun.ts.lib.util.TestUtil;
import com.sun.enterprise.security.auth.login.*;

/**
 * TSLoginContext provides the implementation specific code for allowing a
 * program to login as a specific user.
 */
public class SunRILoginContext implements TSLoginContextInterface {

  private int USERNAME_PASSWORD = 1;

  /**
   * Provides LoginContext needed to perform login.
   *
   */
  public SunRILoginContext() throws Exception {
  }

  /**
   * Performs login with username and password
   *
   * @param usr
   *          the username to login
   * @param pwd
   *          the password of user
   */
  public void login(String usr, String pwd) throws Exception {
    try {
      System.out.println("SunRI Login: " + usr + "/" + pwd);
      LoginContextDriver.doClientLogin(USERNAME_PASSWORD,
          new AuthDataCallbackHandler(usr, pwd));
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Performs Certificate based login
   *
   * Note: This method also uses keystore and keystore password from the TS
   * configuration file(ts.jte)
   *
   * @param alias
   *          - alias is used to pick up the certificate from keystore
   */
  public void login(String useralias) throws Exception {
    // This method is deprecated.
    // Handling certificates based on user alias is
    // done through system property javax.net.ssl.keyStore and
    // javax.net.ssl.keyStorePassword. Runtime uses these system properties to
    // pickup the right certificates for login.

  }

  /**
   * Performs Certificate based login
   *
   * @param alias
   *          - alias is used to pick up the certificate from keystore
   * @param keystore
   *          - keystore file
   * @param keyPass
   *          - keystore password
   */
  public void login(String useralias, String keystore, String keyPass)
      throws Exception {

    // This method is deprecated.
    // Handling certificates based on user alias is
    // done through system property javax.net.ssl.keyStore and
    // javax.net.ssl.keyStorePassword. Runtime uses these system properties to
    // pickup the right certificates for login.

  }

  /**
   * Performs logout
   */
  public Boolean logout() {
    Boolean result = new Boolean(false);
    try {
      LoginContextDriver.logout();
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

}
