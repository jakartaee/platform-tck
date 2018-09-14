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

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.ConnectionRequestInfo;
import com.sun.ts.tests.common.connector.util.*;

public class TSConnectionRequestInfo implements ConnectionRequestInfo {

  private String user;

  private String password;

  /*
   * @name TSConnectionRequestInfo
   * 
   * @desc TSConnectionRequestInfo constructor
   * 
   * @param String, String
   */
  public TSConnectionRequestInfo(String user, String password) {
    this.user = user;
    this.password = password;
  }

  /*
   * @name getUser
   * 
   * @desc Gets the user name
   * 
   * @return String
   */
  public String getUser() {
    return user;
  }

  /*
   * @name getPassword
   * 
   * @desc Gets the Password
   * 
   * @return String
   */
  public String getPassword() {
    return password;
  }

  /*
   * @name equals
   * 
   * @desc Compares the given object with ConnectionRequestInfo.
   * 
   * @param Object
   * 
   * @return boolean
   */
  @Override
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof TSConnectionRequestInfo)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    TSConnectionRequestInfo that = (TSConnectionRequestInfo) obj;

    if (!Util.isEqual(this.password, that.getPassword()))
      return false;

    if (!Util.isEqual(this.user, that.getUser()))
      return false;

    return true;
  }

  /*
   * @name hashCode
   * 
   * @desc Returns the Object hashcode.
   * 
   * @return int
   */
  @Override
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }

  /*
   * @name isEqual
   * 
   * @desc Compares two Objects.
   * 
   * @return boolean
   */
  private boolean isEqual(Object o1, Object o2) {
    if (o1 == null) {
      return (o2 == null);
    } else {
      return o1.equals(o2);
    }
  }

}
