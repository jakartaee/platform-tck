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

// XXX.  Will probably have to tokenize the function-signature values
// to avoid whitespace issues...

public class FunctionEntry {

  public static final String NO_FUNCTION_NAME = "no function name";

  public static final String NO_FUNCTION_SIGNATURE = "no function signature";

  private String name = NO_FUNCTION_NAME;

  private String functionSignature = NO_FUNCTION_SIGNATURE;

  public FunctionEntry() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  public String getFunctionSignature() {
    return functionSignature;
  }

  public void setFunctionSignature(String functionSignature) {
    if (functionSignature != null) {
      this.functionSignature = functionSignature;
    }
  }
}
