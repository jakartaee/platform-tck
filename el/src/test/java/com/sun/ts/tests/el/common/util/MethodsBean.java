/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
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
package com.sun.ts.tests.el.common.util;

public class MethodsBean {

  public String targetA(CharSequence arg0) {
    return "CharSequence";
  }

  public String targetA(String arg0) {
    return "String";
  }

  public String targetA(Long arg0) {
    return "Long";
  }

  public String targetB(CharSequence arg0) {
    return "CharSequence";
  }

  public String targetB(Long arg0) {
    return "Long";
  }
  
  public String targetC(CharSequence arg0, CharSequence arg1) {
    return "CharSequence-CharSequence";
  }

  public String targetC(String arg0, String... varArgs) {
    return "String-Strings";
  }
  
  public String targetD(Long arg0, Long arg1) {
    return "Long-Long";
  }

  public String targetD(String arg0, String... varArgs) {
    return "String-Strings";
  }
  
  public String targetE(Long arg0, Long arg1) {
    return "Long-Long";
  }

  public String targetE(String arg0, String arg1) {
    return "String-String";
  }
  
  @Deprecated
  public String targetF(String arg0, Long arg1) {
    return "String-Long";
  }
}
