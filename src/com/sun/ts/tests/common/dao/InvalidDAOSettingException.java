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

package com.sun.ts.tests.common.dao;

import java.util.Properties;

/**
 * 
 */
public class InvalidDAOSettingException extends RuntimeException
    implements java.io.Serializable {

  String property;

  String value;

  String message;

  InvalidDAOSettingException(String property, String value, String message) {
    super(message);
    this.property = property;
    this.value = value;
    this.message = message;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("Invalid DAO Setting ( ");
    sb.append(property);
    sb.append(" / ");
    sb.append(value);
    sb.append(" : ");
    sb.append(super.toString());

    return sb.toString();
  }

}
