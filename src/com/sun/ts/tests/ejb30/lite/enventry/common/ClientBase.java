/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.enventry.common;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.lite.NumberEnum;

import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;

abstract public class ClientBase extends EJBLiteClientBase {
  protected EnvEntryBeanBase envEntryBean; // injected in subclass

  private StringBuilder postConstructRecords = new StringBuilder();

  private String myString;

  private boolean myBoolean;

  private byte myByte;

  private short myShort;

  private int myInt;

  private long myLong;

  private float myFloat;

  private double myDouble;

  private char myChar;

  private TimeUnit timeUnit;

  private NumberEnum numberEnum;

  private Class<Helper> helperClass;

  private Class<NumberEnum> numberEnumClass;

  private Class<?> envEntryBeanBaseClass;

  private Class testUtilClass;

  private String xmlString;

  private boolean xmlBoolean;

  private byte xmlByte;

  private short xmlShort;

  private int xmlInt;

  private long xmlLong;

  private float xmlFloat;

  private double xmlDouble;

  private char xmlChar;

  private NumberEnum xmlNumberEnum;

  private Class xmlHelperClass;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    if (getContainer() != null) {
      return;
    }
    myString = (String) lookupNoTry(envEntryBean.getLookupName("myString"));
    myBoolean = (Boolean) lookupNoTry(envEntryBean.getLookupName("myBoolean"));
    myByte = (Byte) lookupNoTry(envEntryBean.getLookupName("myByte"));
    myShort = (Short) lookupNoTry(envEntryBean.getLookupName("myShort"));
    myInt = (Integer) lookupNoTry(envEntryBean.getLookupName("myInt"));
    myLong = (Long) lookupNoTry(envEntryBean.getLookupName("myLong"));
    myFloat = (Float) lookupNoTry(envEntryBean.getLookupName("myFloat"));
    myDouble = (Double) lookupNoTry(envEntryBean.getLookupName("myDouble"));
    myChar = (Character) lookupNoTry(envEntryBean.getLookupName("myChar"));
    timeUnit = (TimeUnit) lookupNoTry(envEntryBean.getLookupName("timeUnit"));
    numberEnum = (NumberEnum) lookupNoTry(
        envEntryBean.getLookupName("numberEnum"));
    helperClass = (Class<Helper>) lookupNoTry(
        envEntryBean.getLookupName("helperClass"));
    numberEnumClass = (Class<NumberEnum>) lookupNoTry(
        envEntryBean.getLookupName("numberEnumClass"));
    envEntryBeanBaseClass = (Class<?>) lookupNoTry(
        envEntryBean.getLookupName("envEntryBeanBaseClass"));
    testUtilClass = (Class) lookupNoTry(
        envEntryBean.getLookupName("testUtilClass"));

    xmlString = (String) lookupNoTry(envEntryBean.getLookupName("xmlString"));
    xmlBoolean = (Boolean) lookupNoTry(
        envEntryBean.getLookupName("xmlBoolean"));
    xmlByte = (Byte) lookupNoTry(envEntryBean.getLookupName("xmlByte"));
    xmlShort = (Short) lookupNoTry(envEntryBean.getLookupName("xmlShort"));
    xmlInt = (Integer) lookupNoTry(envEntryBean.getLookupName("xmlInt"));
    xmlLong = (Long) lookupNoTry(envEntryBean.getLookupName("xmlLong"));
    xmlFloat = (Float) lookupNoTry(envEntryBean.getLookupName("xmlFloat"));
    xmlDouble = (Double) lookupNoTry(envEntryBean.getLookupName("xmlDouble"));
    xmlChar = (Character) lookupNoTry(envEntryBean.getLookupName("xmlChar"));
    xmlNumberEnum = (NumberEnum) lookupNoTry(
        envEntryBean.getLookupName("xmlNumberEnum"));
    xmlHelperClass = (Class) lookupNoTry(
        envEntryBean.getLookupName("xmlHelperClass"));

    verifyAnnotatedInjections();
    verifyDescriptorInjections();
  }

  /*
   * testName: ejbPostConstructRecords
   * 
   * @test_Strategy: verify all env-entry are injected properly by the time
   * post-construct method is invoked.
   */
  public void ejbPostConstructRecords() {
    appendReason(envEntryBean.getPostConstructRecords());
  }

  /*
   * testName: clientPostConstructRecords
   * 
   * @test_Strategy: verify all env-entry injected into ejb can also be looked
   * up from web client. In embeddable usage, this test is noop.
   */
  public void clientPostConstructRecords() {
    appendReason(postConstructRecords);
  }

  // copied as is from EnvEntryBeanBase
  private void verifyAnnotatedInjections() {
    Helper.assertEquals(null, "myString", myString, postConstructRecords);
    Helper.assertEquals(null, true, myBoolean, postConstructRecords);
    Helper.assertEquals(null, (byte) 1, myByte, postConstructRecords);
    Helper.assertEquals(null, (short) 1, myShort, postConstructRecords);
    Helper.assertEquals(null, 1, myInt, postConstructRecords);
    Helper.assertEquals(null, (long) 1, myLong, postConstructRecords);
    Helper.assertEquals(null, (float) 1, myFloat, postConstructRecords);
    Helper.assertEquals(null, (double) 1, myDouble, postConstructRecords);
    Helper.assertEquals(null, '1', myChar, postConstructRecords);

    Helper.assertEquals(null, TimeUnit.NANOSECONDS, timeUnit,
        postConstructRecords);
    Helper.assertEquals(null, NumberEnum.ONE, numberEnum, postConstructRecords);
    Helper.assertEquals(null, Helper.class, helperClass, postConstructRecords);
    Helper.assertEquals(null, NumberEnum.class, numberEnumClass,
        postConstructRecords);
    Helper.assertEquals(null, EnvEntryBeanBase.class, envEntryBeanBaseClass,
        postConstructRecords);
    Helper.assertEquals(null, TestUtil.class, testUtilClass,
        postConstructRecords);
  }

  // copied as is from EnvEntryBeanBase
  private void verifyDescriptorInjections() {
    Helper.assertEquals(null, "xmlString", xmlString, postConstructRecords);
    Helper.assertEquals(null, true, xmlBoolean, postConstructRecords);
    Helper.assertEquals(null, (byte) 1, xmlByte, postConstructRecords);
    Helper.assertEquals(null, (short) 1, xmlShort, postConstructRecords);
    Helper.assertEquals(null, 1, xmlInt, postConstructRecords);
    Helper.assertEquals(null, (long) 1, xmlLong, postConstructRecords);
    Helper.assertEquals(null, (float) 1, xmlFloat, postConstructRecords);
    Helper.assertEquals(null, (double) 1, xmlDouble, postConstructRecords);
    Helper.assertEquals(null, '1', xmlChar, postConstructRecords);

    Helper.assertEquals(null, NumberEnum.ONE, xmlNumberEnum,
        postConstructRecords);
    Helper.assertEquals(null, Helper.class, xmlHelperClass,
        postConstructRecords);
  }
}
