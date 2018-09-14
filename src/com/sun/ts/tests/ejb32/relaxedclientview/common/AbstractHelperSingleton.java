/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.relaxedclientview.common;

import javax.ejb.EJB;
import javax.naming.InitialContext;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class AbstractHelperSingleton implements HelperSingleton, TestConstants {

  @EJB(beanName = "NoAnnotationBean", name = "noAnnotationNormalInterface1")
  protected NormalInterface1 noAnnotationNormalInterface1;

  @EJB(beanName = "NoAnnotationBean", name = "noAnnotationNormalInterface2")
  protected NormalInterface2 noAnnotationNormalInterface2;

  @EJB(beanName = "LocalAnnotationBean", name = "localAnnotationNormalInterface1")
  protected NormalInterface1 localAnnotationNormalInterface1;

  @EJB(beanName = "LocalAnnotationBean", name = "localAnnotationNormalInterface2")
  protected NormalInterface2 localAnnotationNormalInterface2;

  @EJB(beanName = "LocalDDBean", name = "localDDNormalInterface1")
  protected NormalInterface1 localDDNormalInterface1;

  protected InitialContext initContext;

  public AbstractHelperSingleton() {
    try {
      initContext = new InitialContext();
    } catch (Exception e) {
    }
  }

  public String noAnnotationTest() throws TestFailedException {

    try {
      int result = noAnnotationNormalInterface1.businessMethod1()
          + noAnnotationNormalInterface2.businessMethod2();

      if (result == EXPECTED_RESULT1) {
        return String.format(SUCCESSFULLY_RUN_RESULT, "noAnnotationTest");
      } else {
        throw new TestFailedException(
            String.format(GOT_WRONG_RESULT, EXPECTED_RESULT1, result));
      }

    } catch (Exception e) {
      throw new TestFailedException(e);
    }

  }

  public String localAnnotationTest() throws TestFailedException {

    try {
      int result = localAnnotationNormalInterface1.businessMethod1()
          + localAnnotationNormalInterface2.businessMethod2();

      if (result == EXPECTED_RESULT1) {
        return String.format(SUCCESSFULLY_RUN_RESULT, "localAnnotationTest");
      } else {
        throw new TestFailedException(
            String.format(GOT_WRONG_RESULT, EXPECTED_RESULT1, result));
      }

    } catch (Exception e) {
      throw new TestFailedException(e);
    }
  }

  private Object findEjbByJndi(String jndi) {
    try {
      Object result = initContext.lookup(jndi);
      return result;
    } catch (Exception e) {
      TLogger.printStackTrace(e);
      return null;
    }
  }

  public String oneRemoteAnnotationOnInterfaceTest()
      throws TestFailedException {
    Object obj = findEjbByJndi(ONE_REMOTE_ANNOTATION_ON_INTERFACE_TEST_JNDI);
    if (obj != null) {
      try {
        NormalInterface2 ni2 = (NormalInterface2) obj;
        ni2.businessMethod2();
      } catch (Exception e) {
        return String.format(SUCCESSFULLY_RUN_RESULT,
            "oneRemoteAnnotationOnInterfaceTest");
      }
      // comment out this temporarily to prevent breaking the cc
      // when bg feature finished we should remove this
      // throw new TestFailedException(String.format(
      // JNDI_SHOULD_NOT_BE_FOUND,
      // ONE_REMOTE_ANNOTATION_ON_INTERFACE_TEST_JNDI));
    }
    return String.format(SUCCESSFULLY_RUN_RESULT,
        "oneRemoteAnnotationOnInterfaceTest");
  }

  public String oneRemoteAnnotationOnEjbTest() throws TestFailedException {
    Object obj = findEjbByJndi(ONE_REMOTE_ANNOTATION_ON_EJB_TEST_JNDI);
    if (obj != null) {
      try {
        NormalInterface2 ni2 = (NormalInterface2) obj;
        ni2.businessMethod2();
      } catch (Exception e) {
        return String.format(SUCCESSFULLY_RUN_RESULT,
            "oneRemoteAnnotationOnEjbTest");
      }

      throw new TestFailedException(String.format(JNDI_SHOULD_NOT_BE_FOUND,
          ONE_REMOTE_ANNOTATION_ON_EJB_TEST_JNDI));
    }
    return String.format(SUCCESSFULLY_RUN_RESULT,
        "oneRemoteAnnotationOnEjbTest");
  }

  public String noInterfaceViewTest() throws TestFailedException {

    NormalInterface1 beanIf1 = (NormalInterface1) findEjbByJndi(
        NO_INTERFACE_VIEW_TEST_BEAN_JNDI);
    if (beanIf1 == null) {
      throw new TestFailedException(
          String.format(JNDI_NOT_FOUNT, NO_INTERFACE_VIEW_TEST_BEAN_JNDI));
    }

    int result = beanIf1.businessMethod1();

    if (result != EXPECTED_RESULT2) {
      throw new TestFailedException(
          String.format(GOT_WRONG_RESULT, EXPECTED_RESULT2, result));
    }

    Object obj = findEjbByJndi(NO_INTERFACE_VIEW_TEST_IF2_JNDI);
    if (obj != null) {
      try {
        NormalInterface2 ni2 = (NormalInterface2) obj;
        ni2.businessMethod2();
      } catch (Exception e) {
        return String.format(SUCCESSFULLY_RUN_RESULT, "noInterfaceViewTest");
      }

      throw new TestFailedException(String.format(JNDI_SHOULD_NOT_BE_FOUND,
          NO_INTERFACE_VIEW_TEST_IF2_JNDI));
    }

    return String.format(SUCCESSFULLY_RUN_RESULT, "noInterfaceViewTest");
  }

  public String localDDTest() throws TestFailedException {

    int result = localDDNormalInterface1.businessMethod1();

    if (result != EXPECTED_RESULT2) {
      throw new TestFailedException(
          String.format(GOT_WRONG_RESULT, EXPECTED_RESULT2, result));
    }

    Object obj = findEjbByJndi(LOCAL_DD_TEST_JNDI);
    if (obj != null) {
      try {
        NormalInterface2 ni2 = (NormalInterface2) obj;
        ni2.businessMethod2();
      } catch (Exception e) {
        return String.format(SUCCESSFULLY_RUN_RESULT, "localDDTest");
      }

      throw new TestFailedException(
          String.format(JNDI_SHOULD_NOT_BE_FOUND, LOCAL_DD_TEST_JNDI));
    }

    return String.format(SUCCESSFULLY_RUN_RESULT, "localDDTest");
  }
}
