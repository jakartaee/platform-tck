/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.enventry.singleton;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Singleton;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.NumberEnum;
import com.sun.ts.tests.ejb30.lite.enventry.common.EnvEntryBeanBase;

@Singleton
@SuppressWarnings({ "unused", "unchecked" })
public class EnvEntryBean extends EnvEntryBeanBase {

  @Resource(name = "myString")
  private void setMyString(String myString) {
    this.myString = myString;
  }

  @Resource(lookup = "java:comp/env/sameName2")
  private void setCompSameName(String s) {
    this.compSameName = s;
  }

  @Resource(lookup = "java:module/env/sameName")
  private void setModuleSameName(String s) {
    this.moduleSameName = s;
  }

  @Resource(lookup = "java:app/env/sameName")
  private void setAppSameName(String s) {
    this.appSameName = s;
  }

  @Resource(name = "myBoolean")
  private void setMyBoolean(Boolean myBoolean) {
    this.myBoolean = myBoolean;
  }

  @Resource(name = "myByte")
  private void setMyByte(Byte myByte) {
    this.myByte = myByte;
  }

  @Resource(name = "myShort")
  private void setMyShort(Short myShort) {
    this.myShort = myShort;
  }

  @Resource(name = "myInt")
  private void setMyInt(Integer myInt) {
    this.myInt = myInt;
  }

  @Resource(name = "myLong")
  private void setMyLong(Long myLong) {
    this.myLong = myLong;
  }

  @Resource(name = "myFloat")
  private void setMyFloat(Float myFloat) {
    this.myFloat = myFloat;
  }

  @Resource(name = "myDouble")
  private void setMyDouble(Double d) {
    myDouble = d;
  }

  @Resource(name = "myChar")
  private void setMyChar(char myChar) {
    this.myChar = myChar;
  }

  @Resource(name = "timeUnit")
  private void setTimeUnit(TimeUnit t) {
    this.timeUnit = t;
  }

  @Resource(name = "numberEnum")
  private void setNumberEnum(NumberEnum e) {
    this.numberEnum = e;
  }

  @Resource(name = "helperClass", type = java.lang.Class.class)
  private void setHelperClass(Class<Helper> c) {
    this.helperClass = c;
  }

  @Resource(name = "numberEnumClass", type = java.lang.Class.class)
  private void setNumberEnumClass(Class<NumberEnum> c) {
    this.numberEnumClass = c;
  }

  @Resource(name = "envEntryBeanBaseClass", type = java.lang.Class.class)
  private void setEnvEntryBeanBaseClass(Class<?> c) {
    this.envEntryBeanBaseClass = c;
  }

  @Resource(name = "testUtilClass", type = java.lang.Class.class)
  private void setTestUtilClass(Class c) {
    this.testUtilClass = c;
  }
}
