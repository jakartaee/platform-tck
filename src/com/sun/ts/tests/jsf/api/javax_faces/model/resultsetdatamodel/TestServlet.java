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

package com.sun.ts.tests.jsf.api.javax_faces.model.resultsetdatamodel;

import com.sun.ts.tests.jsf.api.javax_faces.model.common.BaseModelTestServlet;
import com.sun.ts.tests.jsf.api.javax_faces.model.common.TCKResultSet;
import com.sun.ts.tests.jsf.api.javax_faces.model.common.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.model.DataModel;
import javax.faces.model.ResultSetDataModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestServlet extends BaseModelTestServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws javax.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public DataModel createDataModel() {
    return new ResultSetDataModel();
  }

  public void initDataModel(DataModel model) {
    List list = new ArrayList();
    for (int i = 0; i < 10; i++) {
      list.add(new TestBean());
    }
    setBeansList(list);
    TCKResultSet resultSet = new TCKResultSet(
        list.toArray(new TestBean[list.size()]));

    // DEBUG
    // try {
    // ResultSetMetaData rsmd = resultSet.getMetaData();
    // for (int i = rsmd.getColumnCount(), counter = 0; counter < i; counter++)
    // {
    // System.out.println("COLUMN NAME: " + rsmd.getColumnName(counter + 1));
    // }
    // } catch (Exception e) {
    // System.out.println("ERROR: " + e.toString());
    // }

    model.setWrappedData(resultSet);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  public void resultSetDataModelCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = new ResultSetDataModel(
        new TCKResultSet(new String[] { "string1", "string2" }));

    int curRow = model.getRowIndex();

    if (curRow != 0) {
      out.println(JSFTestUtil.FAIL + " Expected getRowIndex() to return 0"
          + " when called against DataModel instance created by"
          + " passing data to wrap to constructor.");
      out.println("Row index returned: " + curRow);
      return;
    }

    if (!model.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected isRowAvailable() to return"
          + " true when called against DataModel instance created"
          + " by passing data to wrap to constructor.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelGetSetWrappedDataTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = createDataModel();

    ResultSet result = new TCKResultSet(new String[] { "string1" });

    model.setWrappedData(result);

    Object ret = model.getWrappedData();

    if (!result.equals(ret)) {
      out.println(JSFTestUtil.FAIL + " The value returned from getWrappedData()"
          + " was not the same as what was set via setWrappedData().");
      out.println("Expected: " + result);
      out.println("Received: " + ret);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelSetWrappedDataCCETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = createDataModel();

    try {
      model.setWrappedData("invalid");
      out.println(JSFTestUtil.FAIL + " No exception thrown when attempting"
          + " to call setWrappedData() with an invalid type.");
      return;
    } catch (Exception e) {
      if (!(e instanceof ClassCastException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when calling"
            + " setWrappedData() with an invalid type, but it wasn't"
            + " an instance of ClassCastException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // Validate specialized behavior of map returned by
  // ResultSetDataModel.getRowData()
  public void resultSetDataModelGetRowDataSpecializedMapTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = createDataModel();
    initDataModel(model);
    model.setRowIndex(1);

    Map map = (Map) model.getRowData();

    if (map == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain row data at index 1.");
      return;
    }

    // column name comparisons done in case-insensitive manner
    if (!map.containsKey("BOOLprop")) {
      out.println(JSFTestUtil.FAIL + " Unable to find key 'BOOLprop'"
          + " in Map returned by ResultSetDataModel.getRowData()");
      return;
    }

    if (!map.containsKey("boolPrOP")) {
      out.println(JSFTestUtil.FAIL + " Unable to find key 'boolPrOP'"
          + " in Map returned by ResultSetDataModel.getRowData()");
      return;
    }

    // The clear and remove methods must throw UnsupportedOperationExceptions
    try {
      map.clear();
      out.println(JSFTestUtil.FAIL + " No Exception thrown when calling clear()"
          + " on the Map returned by ResultSetDataModel.getRowData().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when calling clear()"
            + " on the Map returned by ResultSetDataModel."
            + "getRowData(), but it wasn't an instance of"
            + " UnsupportedOperationException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      map.remove("someProperty");
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when calling remove()"
              + " on the Map returned by ResultSetDataModel.getRowData().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when calling remove()"
            + " on the Map returned by ResultSetDataModel."
            + "getRowData(), but it wasn't an instance of"
            + " UnsupportedOperationException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // The Set returned by entrySet() must throw UnsupportedOperationException
    // for any attempt to add or remove entries.
    // Any updates to existing values must write through to the underlying
    // DataModel instance and underlying data
    Set entrySet = map.entrySet();
    HashMap m = new HashMap();
    m.put("some", "stuff");
    Map.Entry en = (Map.Entry) m.entrySet().iterator().next();

    try {
      entrySet.add(en);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when adding a new"
          + " Object to the Set obtained by entrySet().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when adding a new"
            + " Object to the Set obtained by entrySet(), but it"
            + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    try {
      entrySet.remove("New String");
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when attempting to remove"
              + " an Object from the Set obtained by entrySet().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(
            JSFTestUtil.FAIL + " Exception thrown when attempting to remove"
                + " an Object from the Set obtained by entrySet(), but it"
                + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    Float pi = new Float(3.14);
    // Make sure we can write through...
    for (Iterator entries = entrySet.iterator(); entries.hasNext();) {
      Map.Entry entry = (Map.Entry) entries.next();
      if ("floatProp".equalsIgnoreCase((String) entry.getKey())) {
        entry.setValue(pi);
      }
    }

    if (!pi.equals(map.get("floatProp"))) {
      out.println(JSFTestUtil.FAIL + " Expected the value of 'floatProp'"
          + " to be '3.14' after updating the Entry in the Set");
      out.println("Value recevied: " + map.get("floatProp"));
      return;
    }

    if (pi.floatValue() != (((TestBean) beans.get(1)).getFloatProp())) {
      out.println(JSFTestUtil.FAIL + " Expected updating the EntrySet to write"
          + " through to the underlying result set, but this was"
          + " not the case.");
      out.println("Expected: " + pi.floatValue());
      out.println("Received: " + ((TestBean) beans.get(1)).getFloatProp());
      return;
    }

    // the Set returned by keySet() must throw UnsupportedOperationException
    // when attempting to add or remove keys from Set.
    Set keySet = map.keySet();

    try {
      keySet.add("New String");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when adding a new"
          + " Object to the Set obtained by keySet().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when adding a new"
            + " Object to the Set obtained by keySet(), but it"
            + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    try {
      keySet.remove("New String");
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when attempting to remove"
              + " an Object from the Set obtained by keySet().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(
            JSFTestUtil.FAIL + " Exception thrown when attempting to remove"
                + " an Object from the Set obtained by keySet(), but it"
                + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    // The put() method must throw IllegalArgumentException if the
    // containsKey() returns false, otherwise it must write through.
    try {
      map.put("nosuchkeyexists", "value");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when calling put()"
          + " with a non-existent key.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when calling put()"
            + " with a non-existent key, but it wasn't an instance"
            + " of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      map.put("charProp", new Character('*'));
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception calling put() with"
          + " a known good key.");
      out.println("Exception: " + e);
      return;
    }

    char result = ((TestBean) beans.get(1)).getCharProp();
    if (result != '*') {
      out.println(JSFTestUtil.FAIL + " Expected the result of the put() "
          + "operation to write through to the underlying "
          + "ResultSet, but this was not the case.");
      out.println("Expected value for charProp: '*'");
      out.println("Received: " + result);
      return;
    }

    // the Collection returned by values() must throw an
    // UnsupportedOperationException
    // when any attempt is made to add or remove values either directly
    // to/from the collection, or indirectly via an Iterator returned by
    // the collection.
    Collection c = map.values();

    try {
      c.add("New String");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when adding a new"
          + " Object to the Collection obtained by values().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when adding a new"
            + " Object to the Collection obtained by values(), but it"
            + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    try {
      c.remove("New String");
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when attempting to remove"
              + " an Object from the Collection obtained by values().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(
            JSFTestUtil.FAIL + " Exception thrown when attempting to remove"
                + " an Object from the Collection obtained by values(), but it"
                + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    Iterator i = c.iterator();

    try {
      i.remove();
      out.println(JSFTestUtil.FAIL
          + " No Exception thrown when attempting to remove"
          + " an Object from the Iterator obtained from Collection.iterator().");
      return;
    } catch (Exception e) {
      if (!(e instanceof UnsupportedOperationException)) {
        out.println(JSFTestUtil.FAIL
            + " Exception thrown when attempting to remove"
            + " an Object from the Iterator obtained from Collection.iterator(), but it"
            + " wasn't an instance of UnsupportedOperationException.");
        out.println("Exception recevied: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void resultSetDataModelGetRowDataMapTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = createDataModel();
    initDataModel(model);
    model.setRowIndex(1);

    Map map = (Map) model.getRowData();

    // validate method of Map instance.

    // containsKey()
    if (!map.containsKey("boolProp")) {
      out.println(JSFTestUtil.FAIL + " containsKey() returned false for a key"
          + " that is known to exist in the Map.");
      return;
    }

    if (map.containsKey("nosuchkey")) {
      out.println(JSFTestUtil.FAIL + " containsKey() returned true for a key"
          + " that is know not to exist in the Map.");
      return;
    }

    // containsValue()
    if (!map.containsValue(Boolean.FALSE)) {
      out.println(JSFTestUtil.FAIL + " containsValue() returned false for a "
          + "value that is known to exist in the Map.");
      return;
    }

    if (map.containsValue(Boolean.TRUE)) {
      out.println(
          JSFTestUtil.FAIL + " containsValue() returned true for a value"
              + " that is known not to exist in the Map.");
      return;
    }

    // equals()
    if (!map.equals(map)) {
      out.println(JSFTestUtil.FAIL + " equals() returned false when testing "
          + "equality on identical Map instances.");
      return;
    }

    if (map.equals(new HashMap())) {
      out.println(JSFTestUtil.FAIL + " equals() returned true when testing"
          + " equality between two different Map instances.");
      return;
    }

    // get()
    if (!map.get("boolProp").equals(Boolean.FALSE)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value returned when calling"
          + " Map.get(\"boolProp\")");
      out.println("Expected: " + Boolean.FALSE);
      out.println("Recevied: " + map.get("boolProp"));
      return;
    }

    if (map.size() != 21) {
      out.println(JSFTestUtil.FAIL + " Expected map.size() to return '22'.");
      out.println("Size received: " + map.size());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
