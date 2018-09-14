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

/*
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

public class VariableEntry {

  public static final String NO_VARIABLE_NAME = "no variable name";

  private String nameGiven = NO_VARIABLE_NAME;

  private String declare = "false";

  private String variableClass = "java.lang.String";

  private String scope = "AT_BEGIN";

  public VariableEntry() {
  }

  public String getNameGiven() {
    return nameGiven;
  }

  public void setNameGiven(String nameGiven) {
    if (nameGiven != null) {
      this.nameGiven = nameGiven;
    }
  }

  public String getDeclare() {
    return declare;
  }

  public void setDeclare(String declare) {
    if (declare != null) {
      this.declare = declare;
    }
  }

  public String getVariableClass() {
    return variableClass;
  }

  public void setVariableClass(String variableClass) {
    if (variableClass != null) {
      this.variableClass = variableClass;
    }
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    if (scope != null) {
      this.scope = scope;
    }
  }
}
