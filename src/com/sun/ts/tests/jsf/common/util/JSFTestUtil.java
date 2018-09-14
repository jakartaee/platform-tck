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

package com.sun.ts.tests.jsf.common.util;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspFactory;

public final class JSFTestUtil {

  public static final boolean DEBUG = true;

  public static final String NL = System.getProperty("line.separator", "\n");

  public static final String PASS = "Test PASSED";

  public static final String FAIL = "Test FAILED";

  public static final String NPE_MESS = FAIL + " Unexpected Exception Thrown!"
      + NL + "Expected: NullPointerException" + NL + "Received: ";

  public static final String FE_MESS = FAIL + " Unexpected Exception Thrown!"
      + NL + "Expected: FacesException" + NL + "Received: ";

  public static final String ISE_MESS = FAIL + " Unexpected Exception Thrown!"
      + NL + "Expected: IllegalStateException" + NL + "Received: ";

  public static final String APE_MESS = FAIL + " Unexpected Exception Thrown!"
      + NL + "Expected: AbortProcessingException" + NL + "Received: ";

  public static final String APP_NULL_MSG = FAIL + " :Unable to obtain "
      + "Application instance.";

  public static final String RESHANDLER_NULL_MSG = FAIL + NL
      + "Unable to obtain ResourceHandler instance.";

  private JSFTestUtil() {
    throw new IllegalStateException();
  }

  /**
   * Writes the provided message to System.out when <tt>debug</tt> is set.
   * 
   * @param message
   *          - the message to write to System.out
   */
  public static void debug(String message) {
    if (DEBUG) {
      System.out.println(message);
    }
  }

  /**
   * <p>
   * Compares the String values in an Enumeration against the provided String
   * array of values in a case sensitive manner.
   * </p>
   * 
   * <p>
   * This method will return <code>false</code> under the following
   * circumstances:
   * <ul>
   * <li>The number of elements in the {@link Iterator} and the number of
   * elements in the Array are not equal (only when the
   * <code>enforceSizes</code> parameter is <code>true</code></li>
   * <li>If the input {@link Iterator} or Array are null</li>
   * <li>If duplicates are found in the {@link Iterator} and
   * <code>allowDuplicates</code> is false</li>
   * </ul>
   * 
   * <p>
   * If none of the above occur, then <code>true<code> will be returned.
   * </p>
   * 
   * @param i
   *          - Iterator to validate
   * @param values
   *          - the values expected to be found in the Enumeration
   * @param enforceSizes
   *          - ensures that the number of elements in the Enumeration matches
   *          the number of elements in the array of values
   * @param allowDuplicates
   *          - If true, the method will true if duplicate elements are found in
   *          the Enumeration, if false, then false will be return if duplicate
   *          elements have been found.
   * 
   * @return true if all the expected values are found, otherwise false.
   */
  public static boolean checkIterator(Iterator<?> i, String[] values,
      boolean enforceSizes, boolean allowDuplicates) {
    List<Object> foundValues = null;

    if (i == null || !i.hasNext() || values == null) {
      return false;
    }

    if (!allowDuplicates) {
      foundValues = new ArrayList<Object>();
    }

    boolean valuesFound = true;
    Arrays.sort(values);
    int count = 0;
    while (i.hasNext()) {
      Object val;
      try {
        val = i.next();
        count++;
        if (!allowDuplicates) {
          if (foundValues.contains(val)) {
            debug("[JSFTestUtil] Duplicate values found in "
                + "Iterator when duplicates are not allowed."
                + "Values found in the Iterator: " + getAsString(i));
            valuesFound = false;
            break;
          }
          foundValues.add(val);
        }

      } catch (NoSuchElementException nsee) {
        debug("[JSFTestUtil] There were less elements in the "
            + "Iterator than expected");
        valuesFound = false;
        break;
      }
      debug("[JSFTestUtil] Looking for '" + val + "' in values: "
          + getAsString(values));
      if ((Arrays.binarySearch(values, val) < 0) && (enforceSizes)) {
        debug("[JSFTestUtil] Value '" + val + "' not found.");
        valuesFound = false;
      }
    }

    if (enforceSizes) {
      if (i.hasNext()) {
        // more elements than should have been.
        debug("[JSFTestUtil] There were more elements in the Iterator "
            + "than expected.");
        valuesFound = false;
      }
      if (count != values.length) {
        debug("[JSFTestUtil] There number of elements in the Iterator "
            + "did not match number of expected values."
            + "Expected number of Values=" + values.length
            + ", Actual number of Iterator elements=" + count);

        valuesFound = false;
      }
    }
    return valuesFound;
  }

  /**
   * <p>
   * Returns the elements of the provided {@link Iterator} as a String in the
   * following format: <code>[n1,n2,n...]</code>.
   * </p>
   * 
   * @param i
   *          - an Iterator
   * 
   * @return - a printable version of the contents of the Iterator
   */
  public static String getAsString(Iterator<?> i) {
    return getAsString(getAsArray(i));
  }

  /**
   * <p>
   * Returns the elements contained in the String array in the following format:
   * <code>[n1,n2,n...]</code>.
   * </p>
   * 
   * @param sArray
   *          - an array of Objects
   * @return - a String based off the values in the array
   */
  public static String getAsString(Object[] sArray) {
    if (sArray == null) {
      return null;
    }
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    for (int i = 0; i < sArray.length; i++) {
      buf.append(sArray[i].toString());
      if ((i + 1) != sArray.length) {
        buf.append(",");
      }
    }
    buf.append("]");
    return buf.toString();
  }

  /**
   * <p>
   * Returns a String representation of the {@link Map} provided.
   * </p>
   * 
   * @param map
   *          input map
   * @return String representation of the Map
   */
  public static String getAsString(Map<String, Object> map) {
    StringBuffer sb = new StringBuffer(32);
    Set<Map.Entry<String, Object>> entrySet = map.entrySet();
    sb.append("Map Entries\n----------------\n");
    for (Map.Entry<String, Object> entry : entrySet) {
      sb.append(entry.getKey()).append(", ").append(entry.getValue());
      sb.append('\n');
    }
    sb.append('\n');
    return sb.toString();
  }

  /**
   * <p>
   * Returns the elements within the provided {@link Iterator} as an Array.
   * </p>
   * 
   * @param i
   *          - an Iterator
   * @return - the elements of the Iterator as an array of Objects
   */
  public static Object[] getAsArray(Iterator<?> i) {
    List<Object> list = new ArrayList<Object>();
    while (i.hasNext()) {
      list.add(i.next());
    }
    return list.toArray(new Object[list.size()]);
  }

  /**
   * <p>
   * Returns the elements of the passed {@link Iterator} as an array of Strings.
   * </p>
   * 
   * @param i
   *          an Iterator
   * @return the contents of the Iterator as an array of Strings
   */
  public static String[] getAsStringArray(Iterator<String> i) {
    List<String> list = new ArrayList<String>();
    while (i.hasNext()) {
      list.add(i.next());
    }
    return list.toArray(new String[list.size()]);
  }

  /**
   * <p>
   * Returns the elements of the {@link Enumeration} as an array of S trings.
   * </p>
   * 
   * @param e
   *          an Enumeration
   * @return the contexts of the Enumeration as an array of Strings
   */
  public static String[] getAsStringArray(Enumeration<?> e) {
    List<Object> list = new ArrayList<Object>();
    while (e.hasMoreElements()) {
      list.add(e.nextElement());
    }
    return list.toArray(new String[list.size()]);
  }

  /**
   * <p>
   * Attempts to load the specified <code>className</code> using the current
   * Thread's context class loader.
   * </p>
   * 
   * @param className
   *          fully qualified class name
   * @return the loaded Class or <code>null</code> if no Class could be found
   */
  public static Class<?> loadClass(String className) {
    Class<?> c = null;
    try {
      c = Thread.currentThread().getContextClassLoader().loadClass(className);
    } catch (Throwable t) {
      debug("[JSFTestUtil] Exception occurred trying to get class: "
          + t.toString());
      t.printStackTrace();
    }

    return c;
  }

  /**
   * <p>
   * Returns the ordinal value of a {@link FacesMessage.Severity} Object as a
   * String value.
   * </p>
   * <ul>
   * <li>FacesMessage.SEVERITY_INFO returns SEVERITY_INFO</li>
   * <li>FacesMessage.SEVERITY_ERROR returns SEVERITY_ERROR</li>
   * <li>FacesMessage.SEVERITY_FATAL returns SEVERITY_FATAL</li>
   * <li>FacesMessage.SEVERITY_WARN returns SEVERITY_WARN</li>
   * </ul>
   * 
   * @param severity
   *          the ordinal value of a FacesMessage.Severity Object
   * @return The String equivalent of the passed value.
   */
  public static String getSeverityAsString(int severity) {
    if (severity == FacesMessage.SEVERITY_INFO.getOrdinal())
      return "SEVERITY_INFO";
    else if (severity == FacesMessage.SEVERITY_ERROR.getOrdinal())
      return "SEVERITY_ERROR";
    else if (severity == FacesMessage.SEVERITY_FATAL.getOrdinal())
      return "SEVERITY_FATAL";
    else if (severity == FacesMessage.SEVERITY_WARN.getOrdinal())
      return "SEVERITY_WARN";
    else
      return "UNKNOWN_SEVERITY";
  }

  /**
   * <p>
   * Return a <code>String</code> representation of the <code>PhaseId</code>
   * ordinal.
   * </p>
   */
  public static String getPhaseIdAsString(PhaseId phaseId) {
    int ordinal = phaseId.getOrdinal();
    if (ordinal == PhaseId.ANY_PHASE.getOrdinal())
      return "ANY_PHASE";
    else if (ordinal == PhaseId.APPLY_REQUEST_VALUES.getOrdinal())
      return "APPLY_REQUEST_VALUES";
    else if (ordinal == PhaseId.INVOKE_APPLICATION.getOrdinal())
      return "INVOKE_APPLICATION";
    else if (ordinal == PhaseId.PROCESS_VALIDATIONS.getOrdinal())
      return "PROCESS_VALIDATIONS";
    else if (ordinal == PhaseId.RENDER_RESPONSE.getOrdinal())
      return "RENDER_RESPONSE";
    else if (ordinal == PhaseId.RESTORE_VIEW.getOrdinal())
      return "RESTORE_VIEW";
    else if (ordinal == PhaseId.UPDATE_MODEL_VALUES.getOrdinal())
      return "UPDATE_MODEL_VALUES";
    else
      return "UNKNOWN_PHASE";
  }

  /**
   * <p>
   * Convenience method to obtain an EL <code>ExpressionFactory</code>.
   * </p>
   */
  public static ExpressionFactory getExpressionFactory(ServletContext ctx) {

    return JspFactory.getDefaultFactory().getJspApplicationContext(ctx)
        .getExpressionFactory();

  } // END getExpressionFactory

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
   * @param pw
   *          - The PrintWrite that you want the results written to.
   */
  public static void checkForNPE(String className, String methName,
      Class<?>[] argTypes, Object[] params, PrintWriter pw) {

    try {
      JSFTestUtil.checkForNPE(Class.forName(className), methName, argTypes,
          params, pw);

    } catch (ClassNotFoundException cnfe) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      cnfe.printStackTrace();
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
   * @param pw
   *          - The PrintWrite that you want the results written to.
   */
  public static void checkForNPE(Class<?> clazz, String methName,
      Class<?>[] argTypes, Object[] params, PrintWriter pw) {

    try {
      JSFTestUtil.checkForNPE(clazz.newInstance(), methName, argTypes, params,
          pw);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
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
   * @param pw
   *          - The PrintWrite that you want the results written to.
   */
  public static void checkForNPE(Object object, String methName,
      Class<?>[] argTypes, Object[] params, PrintWriter pw) {
    String className = object.getClass().getName();
    Method[] methods = null;

    try {
      methods = object.getClass().getMethods();
      Method execMeth = object.getClass().getMethod(methName, argTypes);
      execMeth.invoke(object, params);

      pw.println(JSFTestUtil.FAIL + " No Exception thrown!" + JSFTestUtil.NL
          + "Expected a NullPointerException " + "to be thrown "
          + JSFTestUtil.NL + "when testing: " + className + "." + methName);

    } catch (InvocationTargetException ite) {
      if (ite.getCause() instanceof NullPointerException) {
        pw.println(JSFTestUtil.PASS);

      } else {
        pw.println(JSFTestUtil.NPE_MESS
            + ite.getCause().getClass().getSimpleName() + JSFTestUtil.NL
            + "When testing: " + className + "." + methName);
        ite.getCause().printStackTrace();
      }

    } catch (NoSuchMethodException nsme) {
      pw.println(JSFTestUtil.NPE_MESS + nsme.getClass().getSimpleName()
          + JSFTestUtil.NL);

      JSFTestUtil.printMethods(className, methods, params, pw);

    } catch (Throwable t) {
      pw.println(JSFTestUtil.NPE_MESS + t.getClass().getSimpleName()
          + JSFTestUtil.NL + "When testing: " + className + "." + methName);
      t.printStackTrace();
    }
  } // End checkForNPE

  /**
   * Checks to see if a FacesException is thrown from a given method.
   * 
   * @param object
   *          - The Object you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * @param pw
   *          - The PrintWrite that you want the results written to.
   */
  public static void checkForFE(Object object, String methName,
      Class<?>[] argTypes, Object[] params, PrintWriter pw) {
    String className = object.getClass().getName();
    Method[] methods = null;

    try {
      methods = object.getClass().getMethods();
      Method execMeth = object.getClass().getMethod(methName, argTypes);
      execMeth.invoke(object, params);

      pw.println(JSFTestUtil.FAIL + " No Exception thrown!" + JSFTestUtil.NL
          + "Expected a FacesException " + "to be thrown " + JSFTestUtil.NL
          + "when testing: " + className + "." + methName);

    } catch (InvocationTargetException ite) {
      if (ite.getCause() instanceof FacesException) {
        pw.println(JSFTestUtil.PASS);

      } else {
        pw.println(JSFTestUtil.FE_MESS
            + ite.getCause().getClass().getSimpleName() + JSFTestUtil.NL
            + "When testing: " + className + "." + methName);
        ite.getCause().printStackTrace();
      }

    } catch (NoSuchMethodException nsme) {
      pw.println(JSFTestUtil.FE_MESS + nsme.getClass().getSimpleName()
          + JSFTestUtil.NL);

      JSFTestUtil.printMethods(className, methods, params, pw);

    } catch (Throwable t) {
      pw.println(JSFTestUtil.FE_MESS + t.getClass().getSimpleName()
          + JSFTestUtil.NL + "When testing: " + className + "." + methName);
      t.printStackTrace();
    }
  } // End checkForFE

  /**
   * Checks to see if a IllegalStateException is thrown from a given method.
   * 
   * @param object
   *          - The Object you want to test.
   * @param methName
   *          - The method that you want to test.
   * @param argTypes
   *          - The type value of the arguments for the method under test.
   * @param params
   *          - The parameters you are feeding into the method under test.
   * @param pw
   *          - The PrintWrite that you want the results written to.
   */
  public static void checkForISE(Object object, String methName,
      Class<?>[] argTypes, Object[] params, PrintWriter pw) {
    String className = object.getClass().getName();
    Method[] methods = null;

    try {
      methods = object.getClass().getMethods();
      Method execMeth = object.getClass().getMethod(methName, argTypes);
      execMeth.invoke(object, params);

      pw.println(JSFTestUtil.FAIL + " No Exception thrown!" + JSFTestUtil.NL
          + "Expected a IllegalStateException " + "to be thrown "
          + JSFTestUtil.NL + "when testing: " + className + "." + methName);

    } catch (InvocationTargetException ite) {
      if (ite.getCause() instanceof IllegalStateException) {
        pw.println(JSFTestUtil.PASS);

      } else {
        pw.println(JSFTestUtil.ISE_MESS
            + ite.getCause().getClass().getSimpleName() + JSFTestUtil.NL
            + "When testing: " + className + "." + methName);
        ite.getCause().printStackTrace();
      }

    } catch (NoSuchMethodException nsme) {
      pw.println(JSFTestUtil.ISE_MESS + nsme.getClass().getSimpleName()
          + JSFTestUtil.NL);

      JSFTestUtil.printMethods(className, methods, params, pw);

    } catch (Throwable t) {
      pw.println(JSFTestUtil.ISE_MESS + t.getClass().getSimpleName()
          + JSFTestUtil.NL + "When testing: " + className + "." + methName);
      t.printStackTrace();
    }
  } // End checkForFE

  // ------------------------------------------------------ private methods

  private static void printMethods(String className, Method[] methods,
      Object[] params, PrintWriter pw) {
    pw.println("Existing methods in the " + className + " Object Are: "
        + JSFTestUtil.NL);

    Class<?>[] parms = null;
    for (int i = 0; i < methods.length; i++) {
      parms = methods[i].getParameterTypes();

      pw.print("Method: " + methods[i].getName() + "(");

      int plength = parms.length;
      for (int ii = 0; ii < plength; ii++) {
        pw.print(parms[ii].getSimpleName());

        if ((ii + 1) != plength) {
          pw.print(", ");
        }
      }

      parms = null;
      pw.print(")");
      pw.print(JSFTestUtil.NL);
    }
  }

}
