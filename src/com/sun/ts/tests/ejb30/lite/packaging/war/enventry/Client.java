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
package com.sun.ts.tests.ejb30.lite.packaging.war.enventry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

/**
 * env-entry declared in all web.xml and ejb-jar.xml. Verify that env-entry
 * declared in web.xml are accessible in EJB; env-entry declared in all EJBs are
 * accessible in web; env-entry declared in one EJB are accessible to other
 * EJBs; the default name for @Resource injections are bound to FQC/fieldName,
 * not fieldName.
 */
public class Client extends EJBLiteClientBase {

  @EJB
  private OneBean one;

  @EJB
  private TwoBean two;

  @EJB
  private ThreeBean three;

  @Resource(description = "declared in web.xml")
  private String myString;

  @Resource(name = "myChar")
  private char myChar;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myString", description = "declared in ejb-jar.xml#OneBean")
  private String myStringFromOne;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myChar")
  private char myCharFromOne;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myString", description = "declared in ejb-jar.xml#TwoBean")
  private String myStringFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myChar")
  private char myCharFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myString", description = "declared in ejb-jar.xml#ThreeBean")
  private String myStringFromThree;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myChar")
  private char myCharFromThree;

  @Resource(lookup = "java:module/ModuleName")
  private String moduleNameInjected;

  @Resource(lookup = "java:app/AppName")
  private String appNameInjected;

  // injected in web.xml
  private boolean myBooleanTrue2;

  /*
   * @testName: injectedIntoClient
   * 
   * @test_Strategy: verify env-entry declared in web.xml and ejb-jar.xml and
   * injected into Client class.
   */
  public void injectedIntoClient() {
    verify();
  }

  /*
   * @testName: injectedIntoOneBean
   * 
   * @test_Strategy: verify env-entry declared in web.xml and ejb-jar.xml and
   * injected into OneBean.
   */
  public void injectedIntoOneBean() {
    appendReason(one.getInjectionRecords());
    assertEquals("", true, one.getInjectionStatus());
  }

  /*
   * @testName: injectedIntoTwoBean
   * 
   * @test_Strategy: verify env-entry declared in web.xml and ejb-jar.xml and
   * injected into TwoBean.
   */
  public void injectedIntoTwoBean() {
    appendReason(two.getInjectionRecords());
    assertEquals("", true, two.getInjectionStatus());
  }

  /*
   * @testName: injectedIntoThreeBean
   * 
   * @test_Strategy: verify env-entry declared in web.xml and ejb-jar.xml and
   * injected into ThreeBean.
   */
  public void injectedIntoThreeBean() {
    appendReason(three.getInjectionRecords());
    assertEquals("", true, three.getInjectionStatus());
  }

  /*
   * @testName: injectedIntoOneBeanInterceptors
   * 
   * @test_Strategy: verify env-entry declared in web.xml and ejb-jar.xml and
   * injected into 4 interceptors associated with OneBean. Interceptor0 is
   * declared as the default interceptor for all beans in ejb-jar.xml.
   * Interceptor1 and Interceptor2 are class-level interceptors via annotation.
   * Interceptor3 is a method-level interceptor annotated on
   * BeanBase.getInjectionStatus and BeanBase.getInjectionRecords methods.
   * 
   * This test verifies that: 1. env-entry declared in web.xml and ejb-jar.xml
   * are visible to interceptors, including default interceptors and class-level
   * interceptors. 2. injections in interceptors should have ocurred when
   * PostConstruct is called. 3. PostConstruct on the superclass of an
   * interceptor is called before PostConstruct on interceptor class itself. 4.
   * default name for an injection field is <FQN>/<field-name>, used in
   * InterceptorBase.myStringComponent. 5. env-entry injections into
   * method-level interceptors are performed properly.
   * 
   * This test does not verify the order of interception (i.e., if Interceptor0
   * is triggered before Interceptor1) at request time. The focus of these tests
   * is that injections into interceptors and their superclass are supported,
   * and that superclass PostConstruct is called before subclass PostConstruct.
   * 
   * For method-level interceptors there is no way to verify injection result in
   * a PostConstruct method.This can only be done in the context of a business
   * method.
   * 
   */
  public void injectedIntoOneBeanInterceptors() {
    injectedIntoInterceptors(one);
  }

  /*
   * @testName: injectedIntoTwoBeanInterceptors
   * 
   * @test_Strategy: see previous test
   */
  public void injectedIntoTwoBeanInterceptors() {
    injectedIntoInterceptors(two);
  }

  /*
   * @testName: injectedIntoThreeBeanInterceptors
   * 
   * @test_Strategy: see previous test
   */
  public void injectedIntoThreeBeanInterceptors() {
    injectedIntoInterceptors(three);
  }

  /*
   * @testName: appNameModuleName
   * 
   * @test_Strategy:
   */
  public void appNameModuleName() {
    String lookup = "java:module/ModuleName";
    String expected = getModuleName();
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual);
    assertEquals("Check moduleNameInjected ", expected, moduleNameInjected);

    lookup = "java:app/AppName";
    actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual);
    assertEquals("Check appNameInjected ", expected, appNameInjected);
  }

  /*
   * @testName: appNameModuleNameFromEJB
   * 
   * @test_Strategy:
   */
  public void appNameModuleNameFromEJB() {
    BeanBase[] beans = { one, two, three };
    for (BeanBase b : beans) {
      appendReason(b.appNameModuleName(getModuleName()));
    }
  }

  private void verify() {
    assertEquals("Check myString from web.xml: ", " web.xml ${java.home} ",
        myString);
    assertEquals("Check myString from OneBean: ", " OneBean ${java.home} ",
        myStringFromOne);
    assertEquals("Check myString from TwoBean: ", " TwoBean ${java.home} ",
        myStringFromTwo);
    assertEquals("Check myString from  ThreeBean: ", " ThreeBean ${java.home} ",
        myStringFromThree);

    assertEquals("Check myChar from web.xml: ", ' ', myChar);
    assertEquals("Check myChar from OneBean: ", ' ', myCharFromOne);
    assertEquals("Check myChar from TwoBean: ", '2', myCharFromTwo);
    assertEquals("Check myChar from ThreeBean: ", '3', myCharFromThree);

    assertEquals("Check myBooleanTrue2 from web.xml: ", true, myBooleanTrue2);
  }

  private void injectedIntoInterceptors(BeanBase b) {
    List<List<String>> records = new ArrayList<List<String>>();
    b.getInjectionRecordsForInterceptors(records);
    appendReason(records);
    assertEquals("4 record-lists for 4 interceptors ", 4, records.size());
    for (List<String> list : records) {
      assertEquals("2 records for each interceptor ", 2, list.size());
    }
  }
}
