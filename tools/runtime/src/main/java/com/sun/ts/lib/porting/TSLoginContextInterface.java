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

/**
 * TSLoginContextInterface provides the interface that must be implemented to
 * provide the implementation specific login code to login as a specified user.
 */
public interface TSLoginContextInterface {
  /**
   * This method is used for login with username and password.
   *
   * @param usr
   *          - string username
   * @param pwd
   *          - string password
   */
  public void login(String usr, String pwd) throws Exception;

  /**
   * This login method is used for Certificate based login
   *
   * Note: This method also uses keystore and keystore password from the TS
   * configuration file
   *
   * @param alias
   *          - alias is used to pick up the certificate from keystore
   */
  public void login(String alias) throws Exception;

  /**
   * This login method is used for Certificate based login
   *
   * @param alias
   *          - alias is used to pick up the certificate from keystore
   * @param keystore
   *          - keystore file
   * @param keyPass
   *          - keystore password
   */
  public void login(String alias, String keystore, String keyPass)
      throws Exception;

  /**
   * This method is used for logout
   */
  public Boolean logout();

}
