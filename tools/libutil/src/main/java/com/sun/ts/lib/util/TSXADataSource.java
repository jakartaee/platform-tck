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
 * @(#)TSXADataSource.java	1.6 04/10/01
 */

package com.sun.ts.lib.util;

import java.util.*;
import java.lang.reflect.*;
import javax.sql.XADataSource;

public class TSXADataSource {

  private static final char PROPS_DELIM = ':';

  private static final char PAIR_DELIM = '=';

  private static final char LITERAL_DELIM = '\'';

  private static final String XA_CLASS_NAME = "XADataSourceName";

  private static TSXADataSource instance = new TSXADataSource();

  private TSXADataSource() {
  }

  public static TSXADataSource instance() {
    return instance;
  }

  private String lowerFirstChar(String str) {
    String result = str;
    if (str == null || str.length() == 0) {
      return str;
    }
    char firstChar = str.charAt(0);
    if (Character.isLowerCase(firstChar)) {
      result = Character.toString(Character.toUpperCase(firstChar))
          + result.substring(1);
    }
    return result;
  }

  private void setProperties(Map props, Class clazz, Object target)
      throws Exception {
    try {
      Iterator iter = props.keySet().iterator();
      while (iter.hasNext()) {
        String originalPropName = (String) iter.next();
        String propName = lowerFirstChar(originalPropName);
        String methodName = "set" + propName;
        Method m = findMethod(methodName, clazz);
        String propValue = (String) props.get(originalPropName);
        // Get the parameter type supported by the setter
        Class[] parameters = m.getParameterTypes();
        Object[] values = new Object[1];
        // Now Convert the type to a wrapper object as needed
        values[0] = convertType(parameters[0], propValue);
        m.invoke(target, values);
        System.err.println("$$$$$ TSXADataSource.setProperties \"["
            + originalPropName + ", " + propValue + "]\"");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private Map getProps(String arg) throws Exception {
    Map result = new HashMap();
    boolean inValue = false;
    boolean inQuote = false;
    StringBuffer nameBuffer = new StringBuffer();
    StringBuffer valueBuffer = new StringBuffer();
    int numChars = arg.length();
    for (int i = 0; i < numChars; i++) {
      char current = arg.charAt(i);
      if (Character.isWhitespace(current)) {
        continue;
      }
      if (current == PAIR_DELIM) {
        inValue = true;
      } else if (current == LITERAL_DELIM) {
        inQuote = !inQuote;
      } else if (current == PROPS_DELIM && !inQuote) {
        inValue = false;
        result.put(nameBuffer.toString(), valueBuffer.toString());
        nameBuffer.delete(0, nameBuffer.length());
        valueBuffer.delete(0, valueBuffer.length());
      } else {
        if (inQuote || inValue) {
          valueBuffer.append(current);
        } else {
          nameBuffer.append(current);
        }
      }
    }
    result.put(nameBuffer.toString(), valueBuffer.toString());
    System.err.println("$$$$$ getProps()" + result);
    return result;
  }

  /**
   * Find a specific method within a Class
   *
   * @param methodName
   *          the name of the method to search for in clazz
   * @param clazz
   *          The Class to search for 'methodName' in
   * @return the returned Method.
   * @throws <code>Exception</code>,
   *           in case methodName cannot be found in clazz
   */
  private Method findMethod(String methodName, Class clazz) throws Exception {
    Method[] methods = clazz.getMethods();
    Method result = null;
    for (int i = 0; i < methods.length; i++) {
      if (methods[i].getName().equalsIgnoreCase(methodName)) {
        result = methods[i];
        System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.err.println("$$$$$ findMethod() found method: " + result);
        System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
        break;
      }
    }
    if (result == null) {
      throw new Exception("$$$$ Could not find method " + methodName
          + " in Class: " + clazz.getName());
    }
    return result;
  }

  /**
   * Converts the type from String to the Class type.
   *
   * @param type
   *          Class name to which the conversion is required.
   * @param parameter
   *          String value to be converted.
   * @return Converted value.
   * @throws <code>NumberFormatException</code>,
   *           in case of the mismatch of parameter values.
   */
  private Object convertType(Class type, String parameter)
      throws NumberFormatException {
    try {
      String typeName = type.getName();
      if (typeName.equals("java.lang.String")
          || typeName.equals("java.lang.Object")) {
        return parameter;
      }

      if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
        return new Integer(parameter);
      }

      if (typeName.equals("short") || typeName.equals("java.lang.Short")) {
        return new Short(parameter);
      }

      if (typeName.equals("byte") || typeName.equals("java.lang.Byte")) {
        return new Byte(parameter);
      }

      if (typeName.equals("long") || typeName.equals("java.lang.Long")) {
        return new Long(parameter);
      }

      if (typeName.equals("float") || typeName.equals("java.lang.Float")) {
        return new Float(parameter);
      }

      if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
        return new Double(parameter);
      }

      if (typeName.equals("java.math.BigDecimal")) {
        return new java.math.BigDecimal(parameter);
      }

      if (typeName.equals("java.math.BigInteger")) {
        return new java.math.BigInteger(parameter);
      }

      if (typeName.equals("boolean") || typeName.equals("java.lang.Boolean")) {
        return new Boolean(parameter);
      }

      return parameter;
    } catch (NumberFormatException nfe) {
      System.err.println("$$$$$ NumberFormatException Encountered");
      throw nfe;
    }
  }

  public XADataSource getXADataSource(String xaProps, String className)
      throws Exception {
    System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
    System.err.println("xaProps \"" + xaProps + "\"");
    System.err.println("className \"" + className + "\"");
    System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$");
    Map props = getProps(xaProps);
    Class clazz = Class.forName(className);
    XADataSource target = (XADataSource) clazz.newInstance();
    setProperties(props, clazz, target);
    return target;
  }

}
