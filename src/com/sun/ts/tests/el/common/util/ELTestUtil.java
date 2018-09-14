/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:  $
 */
package com.sun.ts.tests.el.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.MethodNotFoundException;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;

public final class ELTestUtil {

  public static final String NL = System.getProperty("line.separator", "\n");

  public static final String PASS = "Test PASSED";

  public static final String FAIL = "Test FAILED";

  private ELTestUtil() {
    throw new IllegalStateException();
  }

  /**
   * Checks to see if a NullPointerException is thrown from a given
   * method(methName) from a given Class(className). use this method for any
   * none Abstract classes you wish to test.
   * 
   * @param className
   *          - The Class that has the method under test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  public static void checkForNPE(String className, String methName,
      Class<?>[] argTypes, Object[] params) throws Fault {

    try {
      checkForNPE(Class.forName(className), methName, argTypes, params);

    } catch (ClassNotFoundException cnfe) {
      throw new Fault(FAIL + NL + cnfe.toString());
    }

  } // End checkForNPE

  /**
   * Checks to see if a NullPointerException is thrown from a given
   * method(methName) from a given Class. use this method for any Abstract
   * classes you wish to test. You can pass in the instantiated class that you
   * want to test.
   * 
   * @param Class
   *          - The class you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  public static void checkForNPE(Class<?> clazz, String methName,
      Class<?>[] argTypes, Object[] params) throws Fault {

    try {
      checkForNPE(clazz.newInstance(), methName, argTypes, params);

    } catch (Exception e) {
      throw new Fault(FAIL + NL + e.toString());
    }

  } // End checkForNPE

  /**
   * Checks to see if a NullPointerException is thrown from a given
   * method(methName) from a given Class. use this method for any Abstract
   * classes you wish to test. You can pass in the instantiated class that you
   * want to test.
   * 
   * @param object
   *          - The Object you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  public static void checkForNPE(Object object, String methName,
      Class<?>[] argTypes, Object[] params) throws Fault {

    checkForException(object, methName, argTypes, params,
        new NullPointerException());

  } // End checkForNPE

  /**
   * Checks to see if a ClassNotFoundException is thrown
   * 
   * @param object
   *          - The Object you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  public static void checkForCNFE(Object object, String methName,
      Class<?>[] argTypes, Object[] params) throws Fault {

    checkForException(object, methName, argTypes, params,
        new ClassNotFoundException());

  } // End checkForCNFE

  /**
   * Checks to see if a NoSuchMethodException is thrown
   * 
   * @param object
   *          - The Object you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  public static void checkForNSME(Object object, String methName,
      Class<?>[] argTypes, Object[] params) throws Fault {

    checkForException(object, methName, argTypes, params,
        new NoSuchMethodException());

  } // End checkForCNFE

  // ------------------------------------ private methods

  /*
   * Checks to see if a NoSuchMethodException is thrown
   * 
   * @param object - The Object you want to test.
   * 
   * @param methName - The method that you want to test.
   * 
   * @param argTypes - The type value of the arguments for the method under
   * test.
   * 
   * @param params - The parameters you are feeding into the method under test.
   * 
   * @throws Fault
   */
  private static void checkForException(Object object, String methName,
      Class<?>[] argTypes, Object[] params, Object exceptionType) throws Fault {

    StringBuffer buff = new StringBuffer();
    String className = object.getClass().getName();
    String exceptionMess = buildMess(exceptionType.getClass().getSimpleName());

    try {
      Method execMeth = object.getClass().getMethod(methName, argTypes);
      execMeth.invoke(object, params);

      buff.append(FAIL + " No Exception thrown!" + NL + "Expected a "
          + exceptionType.getClass().getSimpleName() + " to be thrown!" + NL
          + "when testing: " + className + "." + methName);

    } catch (InvocationTargetException ite) {
      if (exceptionType.getClass().isInstance(ite.getCause())) {
        TestUtil.logMsg(PASS);
        return;

      } else {
        buff.append(exceptionMess + ite.getCause().getClass().getSimpleName()
            + NL + "When testing: " + className + "." + methName + NL
            + ite.getCause().toString());
      }

    } catch (Throwable t) {
      buff.append(exceptionMess + t.getClass().getSimpleName() + NL
          + "When testing: " + className + "." + methName + NL + t.toString());
    }

    throw new Fault(FAIL + NL + buff.toString());

  } // End checkForException

  private static String buildMess(String exceptionName) {
    return FAIL + " Unexpected Exception Thrown!" + NL + "Expected: "
        + exceptionName + NL + "Received: ";
  }
}
