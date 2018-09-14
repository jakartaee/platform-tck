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

package com.sun.ts.tests.jsf.api.javax_faces.model.common;

import javax.faces.FacesException;
import javax.servlet.jsp.jstl.sql.Result;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <p>
 * Mock object that implements enough of
 * <code>javax.servlet.jsp.jstl.sql.ResultSet</code> to exercise the
 * <code>ResultDataModel</code> functionality. It wraps an array of JavaBeans
 * objects that are passed to the constructor.
 * </p>
 *
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - The <code>SortedMap</code> objects
 * returned by <code>getRows()</code> do <strong>NOT</strong> support
 * case-insensitive key comparisons, as required by the JSTL specification.
 * Therefore, key values in value reference expressions will be matched case
 * sensitively.
 * </p>
 */

public class TCKResult implements Result {

  // ------------------------------------------------------------ Constructors

  /**
   * <p>
   * Construct a new <code>TCKResult</code> instance wrapping the specified
   * array of beans.
   * </p>
   *
   * @param beans
   *          Array of beans representing the content of the result set
   */
  public TCKResult(Object beans[]) {

    if (beans == null) {
      throw new NullPointerException();
    }
    this.beans = beans;

  }

  // ------------------------------------------------------ Instance Variables

  // Array of beans representing our underlying data
  private Object beans[] = null;

  // ----------------------------------------------------- Implemented Methods

  public SortedMap[] getRows() {

    TreeMap results[] = new TreeMap[beans.length];
    for (int i = 0; i < results.length; i++) {
      try {
        results[i] = getReadableValues((beans[i]));
      } catch (Exception e) {
        Throwable t = null;
        if (e instanceof InvocationTargetException) {
          t = ((InvocationTargetException) e).getTargetException();
        }
        if (t != null) {
          t.printStackTrace();
          throw new FacesException(t);
        } else {
          throw new FacesException(e);
        }
      }
    }
    return (results);

  }

  public int getRowCount() {

    return (beans.length);

  }

  // --------------------------------------------------- Unimplemented Methods

  public Object[][] getRowsByIndex() {
    throw new UnsupportedOperationException();
  }

  public String[] getColumnNames() {
    throw new UnsupportedOperationException();
  }

  public boolean isLimitedByMaxRows() {
    throw new UnsupportedOperationException();
  }

  // --------------------------------------------------------- Private Methods

  public static TreeMap getReadableValues(Object bean) throws Exception {
    BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    TreeMap map = new TreeMap();
    for (int i = 0; i < descriptors.length; i++) {
      Method method = descriptors[i].getReadMethod();
      if (method == null || descriptors[i].getName().equals("exception")) {
        continue;
      } else {
        Object ret = method.invoke(bean, (Object[]) null);
        map.put(descriptors[i].getName(), ret);
      }
    }
    return map;
  }

}
