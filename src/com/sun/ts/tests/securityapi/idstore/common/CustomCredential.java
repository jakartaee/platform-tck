/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.common;

import javax.security.enterprise.credential.Credential;

public class CustomCredential implements Credential {
  private final String caller;

  private final String password;

  public CustomCredential(String callerName, String pwd) {
    this.caller = callerName;
    this.password = pwd;
  }

  public String getCaller() {
    return caller;
  }

  public String getPassword() {
    return password;
  }

  public boolean compareTo(String callerName, String pwd) {
    return getCaller().equals(callerName) && getPassword().equals(pwd);
  }
}
