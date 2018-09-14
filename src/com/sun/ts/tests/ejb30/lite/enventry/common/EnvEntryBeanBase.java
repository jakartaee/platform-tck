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
package com.sun.ts.tests.ejb30.lite.enventry.common;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.NumberEnum;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

abstract public class EnvEntryBeanBase {
  @Resource
  private SessionContext sctx;

  private StringBuilder postConstructRecords = new StringBuilder();

  // injected in subclass
  protected String myString;

  protected String compSameName;

  protected String moduleSameName;

  protected String appSameName;

  protected boolean myBoolean;

  protected byte myByte;

  protected short myShort;

  protected int myInt;

  protected long myLong;

  protected float myFloat;

  protected double myDouble;

  protected char myChar;

  protected TimeUnit timeUnit;

  protected NumberEnum numberEnum;

  protected Class<Helper> helperClass;

  protected Class<NumberEnum> numberEnumClass;

  protected Class<?> envEntryBeanBaseClass;

  protected Class testUtilClass;

  // env-entry injection with descriptors, omitting env-entry-value and
  // env-entry-type. Injection fields have default values
  // @Resource(name="defaultString")
  private String defaultString = "defaultString";

  // @Resource(name="defaultBoolean")
  private boolean defaultBoolean = false;

  // @Resource(name="defaultByte")
  private byte defaultByte = (byte) 0;

  // @Resource(name="defaultShort")
  private short defaultShort = (short) 0;

  // @Resource(name="defaultInt")
  private int defaultInt = 0;

  // @Resource(name="defaultLong")
  private long defaultLong = 0;

  // @Resource(name="defaultFloat")
  private float defaultFloat = 0;

  // @Resource(name="defaultDouble")
  private double defaultDouble = 0;

  // @Resource(name="defaultChar")
  private char defaultChar = 0;

  // @Resource(name="defaultNumberEnum")
  private NumberEnum defaultNumberEnum = NumberEnum.ONE;

  // @Resource(name="defaultHelperClass")
  private Class defaultHelperClass = Helper.class;

  // env-entry injection with descriptors, omitting env-entry-type.
  // @Resource(name="xmlString")
  private String xmlString;

  // @Resource(name="xmlBoolean")
  private boolean xmlBoolean;

  // @Resource(name="xmlByte")
  private byte xmlByte;

  // @Resource(name="xmlShort")
  private short xmlShort;

  // @Resource(name="xmlInt")
  private int xmlInt;

  // @Resource(name="xmlLong")
  private long xmlLong;

  // @Resource(name="xmlFloat")
  private float xmlFloat;

  // @Resource(name="xmlDouble")
  private double xmlDouble;

  // @Resource(name="xmlChar")
  private char xmlChar;

  // @Resource(name="xmlNumberEnum")
  private NumberEnum xmlNumberEnum;

  // @Resource(name="xmlHelperClass")
  private Class xmlHelperClass;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    verifyAnnotatedInjections();
    verifyDefaultValueInjections();
    verifyDescriptorInjections();
  }

  private void verifyAnnotatedInjections() {
    String s = "Verify injected fields:";
    assertEquals(s, "myString", myString, postConstructRecords);
    assertEquals(s, "compSameName2", compSameName, postConstructRecords);
    assertEquals(s, "moduleSameName", moduleSameName, postConstructRecords);
    assertEquals(s, "appSameName", appSameName, postConstructRecords);
    assertEquals(s, true, myBoolean, postConstructRecords);
    assertEquals(s, (byte) 1, myByte, postConstructRecords);
    assertEquals(s, (short) 1, myShort, postConstructRecords);
    assertEquals(s, 1, myInt, postConstructRecords);
    assertEquals(s, (long) 1, myLong, postConstructRecords);
    assertEquals(s, (float) 1, myFloat, postConstructRecords);
    assertEquals(s, (double) 1, myDouble, postConstructRecords);
    assertEquals(s, '1', myChar, postConstructRecords);

    assertEquals(s, TimeUnit.NANOSECONDS, timeUnit, postConstructRecords);
    assertEquals(s, NumberEnum.ONE, numberEnum, postConstructRecords);
    assertEquals(s, Helper.class, helperClass, postConstructRecords);
    assertEquals(s, NumberEnum.class, numberEnumClass, postConstructRecords);
    assertEquals(s, EnvEntryBeanBase.class, envEntryBeanBaseClass,
        postConstructRecords);
    assertEquals(s, TestUtil.class, testUtilClass, postConstructRecords);

    s = "Verify injected fields with SessionContext lookup:";
    assertEquals(s, "myString", sctx.lookup(getLookupName("myString")),
        postConstructRecords);
    assertEquals(s, "compSameName2", sctx.lookup("java:comp/env/sameName2"),
        postConstructRecords);
    assertEquals(s, "moduleSameName", sctx.lookup("java:module/env/sameName"),
        postConstructRecords);
    assertEquals(s, "appSameName", sctx.lookup("java:app/env/sameName"),
        postConstructRecords);
    assertEquals(s, true, sctx.lookup(getLookupName("myBoolean")),
        postConstructRecords);
    assertEquals(s, (byte) 1, sctx.lookup(getLookupName("myByte")),
        postConstructRecords);
    assertEquals(s, (short) 1, sctx.lookup(getLookupName("myShort")),
        postConstructRecords);
    assertEquals(s, 1, sctx.lookup(getLookupName("myInt")),
        postConstructRecords);
    assertEquals(s, (long) 1, sctx.lookup(getLookupName("myLong")),
        postConstructRecords);
    assertEquals(s, (float) 1, sctx.lookup(getLookupName("myFloat")),
        postConstructRecords);
    assertEquals(s, (double) 1, sctx.lookup(getLookupName("myDouble")),
        postConstructRecords);
    assertEquals(s, '1', sctx.lookup(getLookupName("myChar")),
        postConstructRecords);

    assertEquals(s, TimeUnit.NANOSECONDS,
        sctx.lookup(getLookupName("timeUnit")), postConstructRecords);
    assertEquals(s, NumberEnum.ONE, sctx.lookup(getLookupName("numberEnum")),
        postConstructRecords);
    assertEquals(s, Helper.class, sctx.lookup(getLookupName("helperClass")),
        postConstructRecords);
    assertEquals(s, NumberEnum.class,
        sctx.lookup(getLookupName("numberEnumClass")), postConstructRecords);
    assertEquals(s, EnvEntryBeanBase.class,
        sctx.lookup(getLookupName("envEntryBeanBaseClass")),
        postConstructRecords);
    assertEquals(s, TestUtil.class, sctx.lookup(getLookupName("testUtilClass")),
        postConstructRecords);

    s = "Verify injected fields with naming context lookup:";
    assertEquals(s, "myString", lookupNoTry(getLookupName("myString")),
        postConstructRecords);
    assertEquals(s, "compSameName2", lookupNoTry("java:comp/env/sameName2"),
        postConstructRecords);
    assertEquals(s, "moduleSameName", lookupNoTry("java:module/env/sameName"),
        postConstructRecords);
    assertEquals(s, "appSameName", lookupNoTry("java:app/env/sameName"),
        postConstructRecords);
    assertEquals(s, true, lookupNoTry(getLookupName("myBoolean")),
        postConstructRecords);
    assertEquals(s, (byte) 1, lookupNoTry(getLookupName("myByte")),
        postConstructRecords);
    assertEquals(s, (short) 1, lookupNoTry(getLookupName("myShort")),
        postConstructRecords);
    assertEquals(s, 1, lookupNoTry(getLookupName("myInt")),
        postConstructRecords);
    assertEquals(s, (long) 1, lookupNoTry(getLookupName("myLong")),
        postConstructRecords);
    assertEquals(s, (float) 1, lookupNoTry(getLookupName("myFloat")),
        postConstructRecords);
    assertEquals(s, (double) 1, lookupNoTry(getLookupName("myDouble")),
        postConstructRecords);
    assertEquals(s, '1', lookupNoTry(getLookupName("myChar")),
        postConstructRecords);

    assertEquals(s, TimeUnit.NANOSECONDS,
        lookupNoTry(getLookupName("timeUnit")), postConstructRecords);
    assertEquals(s, NumberEnum.ONE, lookupNoTry(getLookupName("numberEnum")),
        postConstructRecords);
    assertEquals(s, Helper.class, lookupNoTry(getLookupName("helperClass")),
        postConstructRecords);
    assertEquals(s, NumberEnum.class,
        lookupNoTry(getLookupName("numberEnumClass")), postConstructRecords);
    assertEquals(s, EnvEntryBeanBase.class,
        lookupNoTry(getLookupName("envEntryBeanBaseClass")),
        postConstructRecords);
    assertEquals(s, TestUtil.class, lookupNoTry(getLookupName("testUtilClass")),
        postConstructRecords);
  }

  private void verifyDefaultValueInjections() {
    String s = "Verify injections to fields that have default values:";
    assertEquals(s, "defaultString", defaultString, postConstructRecords);
    expectingNamingException("defaultString");
    assertEquals(s, false, defaultBoolean, postConstructRecords);
    expectingNamingException("defaultBoolean");
    assertEquals(s, (byte) 0, defaultByte, postConstructRecords);
    expectingNamingException("defaultByte");
    assertEquals(s, (short) 0, defaultShort, postConstructRecords);
    expectingNamingException("defaultShort");
    assertEquals(s, 0, defaultInt, postConstructRecords);
    expectingNamingException("defaultInt");
    assertEquals(s, (long) 0, defaultLong, postConstructRecords);
    expectingNamingException("defaultLong");
    assertEquals(s, (float) 0, defaultFloat, postConstructRecords);
    expectingNamingException("defaultFloat");
    assertEquals(s, (double) 0, defaultDouble, postConstructRecords);
    expectingNamingException("defaultDouble");
    assertEquals(s, (char) 0, defaultChar, postConstructRecords);
    expectingNamingException("defaultChar");

    assertEquals(s, NumberEnum.ONE, defaultNumberEnum, postConstructRecords);
    expectingNamingException("defaultNumberEnum");
    assertEquals(s, Helper.class, defaultHelperClass, postConstructRecords);
    expectingNamingException("defaultHelperClass");
  }

  private void verifyDescriptorInjections() {
    String s = "Verify injections with descriptor:";
    assertEquals(s, "xmlString", xmlString, postConstructRecords);
    assertEquals(s, true, xmlBoolean, postConstructRecords);
    assertEquals(s, (byte) 1, xmlByte, postConstructRecords);
    assertEquals(s, (short) 1, xmlShort, postConstructRecords);
    assertEquals(s, 1, xmlInt, postConstructRecords);
    assertEquals(s, (long) 1, xmlLong, postConstructRecords);
    assertEquals(s, (float) 1, xmlFloat, postConstructRecords);
    assertEquals(s, (double) 1, xmlDouble, postConstructRecords);
    assertEquals(s, '1', xmlChar, postConstructRecords);

    assertEquals(s, NumberEnum.ONE, xmlNumberEnum, postConstructRecords);
    assertEquals(s, Helper.class, xmlHelperClass, postConstructRecords);

    s = "Verify injected fields with SessionContext lookup:";
    assertEquals(s, "xmlString", sctx.lookup(getLookupName("xmlString")),
        postConstructRecords);
    assertEquals(s, true, sctx.lookup(getLookupName("xmlBoolean")),
        postConstructRecords);
    assertEquals(s, (byte) 1, sctx.lookup(getLookupName("xmlByte")),
        postConstructRecords);
    assertEquals(s, (short) 1, sctx.lookup(getLookupName("xmlShort")),
        postConstructRecords);
    assertEquals(s, 1, sctx.lookup(getLookupName("xmlInt")),
        postConstructRecords);
    assertEquals(s, (long) 1, sctx.lookup(getLookupName("xmlLong")),
        postConstructRecords);
    assertEquals(s, (float) 1, sctx.lookup(getLookupName("xmlFloat")),
        postConstructRecords);
    assertEquals(s, (double) 1, sctx.lookup(getLookupName("xmlDouble")),
        postConstructRecords);
    assertEquals(s, '1', sctx.lookup(getLookupName("xmlChar")),
        postConstructRecords);

    assertEquals(s, NumberEnum.ONE, sctx.lookup(getLookupName("xmlNumberEnum")),
        postConstructRecords);
    assertEquals(s, Helper.class, sctx.lookup(getLookupName("xmlHelperClass")),
        postConstructRecords);

    s = "Verify injected fields with naming context lookup:";
    assertEquals(s, "xmlString", lookupNoTry(getLookupName("xmlString")),
        postConstructRecords);
    assertEquals(s, true, lookupNoTry(getLookupName("xmlBoolean")),
        postConstructRecords);
    assertEquals(s, (byte) 1, lookupNoTry(getLookupName("xmlByte")),
        postConstructRecords);
    assertEquals(s, (short) 1, lookupNoTry(getLookupName("xmlShort")),
        postConstructRecords);
    assertEquals(s, 1, lookupNoTry(getLookupName("xmlInt")),
        postConstructRecords);
    assertEquals(s, (long) 1, lookupNoTry(getLookupName("xmlLong")),
        postConstructRecords);
    assertEquals(s, (float) 1, lookupNoTry(getLookupName("xmlFloat")),
        postConstructRecords);
    assertEquals(s, (double) 1, lookupNoTry(getLookupName("xmlDouble")),
        postConstructRecords);
    assertEquals(s, '1', lookupNoTry(getLookupName("xmlChar")),
        postConstructRecords);

    assertEquals(s, NumberEnum.ONE, lookupNoTry(getLookupName("xmlNumberEnum")),
        postConstructRecords);
    assertEquals(s, Helper.class, lookupNoTry(getLookupName("xmlHelperClass")),
        postConstructRecords);
  }

  protected void expectingNamingException(String shortName) {
    try {
      Object obj = ServiceLocator.lookup(getLookupName(shortName));
      throw new RuntimeException("Expecting NamingException, but got " + obj);
    } catch (javax.naming.NamingException e) {
      postConstructRecords.append("Got expected " + e);
    }
  }

  /**
   * May be overridden to return lookup names in different namespace. The
   * default impl is to return a lookup name in component namespace.
   */
  public String getLookupName(String s) {
    if (s == null) {
      return null;
    }
    return "java:comp/env/" + s;
  }

  public String getPostConstructRecords() {
    return postConstructRecords.toString();
  }
}
