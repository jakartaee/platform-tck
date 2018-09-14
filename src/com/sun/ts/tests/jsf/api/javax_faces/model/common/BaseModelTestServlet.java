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

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public abstract class BaseModelTestServlet extends HttpTCKServlet {

  protected List beans;

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

  public abstract DataModel createDataModel();

  public abstract void initDataModel(DataModel model);

  protected void setBeansList(List beans) {
    this.beans = beans;
  }

  // ------------------------------------------ Test Methods

  public void dataModelAddGetRemoveDataModelListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    TCKDataListener listener = new TCKDataListener();

    DataModelListener[] listeners = data.getDataModelListeners();

    if (listeners.length != 0) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "DataModel.getDataModelListeners() returned a "
          + "non-empty array of listeners.");
      return;
    }

    data.addDataModelListener(listener);

    listeners = data.getDataModelListeners();

    if (listeners.length != 1) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "DataModel.getDataModelListeners() returned an "
          + "unexpected value after adding a single " + "DataModelListener."
          + JSFTestUtil.NL + "Expected: 1" + JSFTestUtil.NL + "Received: "
          + listeners.length);
      return;
    }

    data.removeDataModelListener(new TCKDataListener());
    listeners = data.getDataModelListeners();

    if (listeners.length != 1) {
      out.println(JSFTestUtil.FAIL + " DataModel.getDataModelListeners()"
          + " returned an unexpected value after attempting to remove"
          + " a listener that wasn't already added to the DataModel");
      out.println("Expected: 1");
      out.println("Received: " + listeners.length);
      return;
    }

    data.removeDataModelListener(listener);
    listeners = data.getDataModelListeners();

    if (listeners.length != 0) {
      out.println(JSFTestUtil.FAIL + " DataModel.getDataModelListeners()"
          + " returned an unexpected value after removing a single"
          + " DataModelListener.");
      out.println("Expected: 0");
      out.println("Received: " + listeners.length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelIsRowAvailableTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    // Model with no wrapped data source, isRowAvailable() must return
    // false.
    if (data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.isRowAvailable() to "
          + "return false if the DataModel object didn't wrap"
          + " an underlying data source.");
      return;
    }

    data = createDataModel();
    initDataModel(data);

    data.setRowIndex(0);

    if (!data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.isRowAvailable() to"
          + " return true when row index was within a valid range.");
      return;
    }

    // next, set the row inex out of range. isRowAvailable() should
    // return false.
    data.setRowIndex(11);
    if (data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.isRowAvailable() to"
          + " return false when specified row index was out of" + " range.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelGetRowCountTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    // There is no backing data in the DataModel instance,
    // so getRowCount() should return 01
    int result = data.getRowCount();
    if (result != -1) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.getRowCount() to"
          + " return -1 if no data was available on the Model " + "tier.");
      out.println("Row count received: " + result);
      return;
    }

    data = createDataModel();
    initDataModel(data);

    // backing DataModel instance has 10 objects, therefore getRowCount()
    // should return 10.
    result = data.getRowCount();
    if (result != 10) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.getRowCount() to "
          + "return 10.");
      out.println("Row count received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelGetSetRowIndexTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    // no index has been specified, so getRowIndex() should return -1
    int result = data.getRowIndex();
    if (result != -1) {
      out.println(
          JSFTestUtil.FAIL + " Expected DataModel.getRowIndex() to return"
              + " -1 when no backing data has been specified.");
      out.println("Value received: " + result);
      return;
    }

    data.setRowIndex(0);

    if (data.getRowIndex() != 0) {
      out.println(JSFTestUtil.FAIL + " DataModel.getRowIndex() didn't return"
          + " the value as set by DataModel.setRowIndex().");
      out.println("Expected: 0");
      out.println("Received: " + data.getRowIndex());
    }

    data.setRowIndex(-1);

    if (data.getRowIndex() != -1) {
      out.println(JSFTestUtil.FAIL + " DataModel.getRowIndex() didn't return"
          + " the value as set by DataModel.setRowIndex().");
      out.println("Expected: -1");
      out.println("Received: " + data.getRowIndex());
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelSetRowIndexIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();
    initDataModel(data);

    try {
      data.setRowIndex(-2);
      out.println("No Exception thrown when trying to set the row index"
          + " to a value less then -1.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when trying"
            + " to set the row index to a value less than 1"
            + ", but it wasn't an instance of" + " IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelRemoveDataModelListenerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    DataModel data = createDataModel();

    JSFTestUtil.checkForNPE(data, "removeDataModelListener",
        new Class<?>[] { DataModelListener.class }, new Object[] { null }, pw);

  }

  public void dataModelAddDataModelListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    DataModel data = createDataModel();

    JSFTestUtil.checkForNPE(data, "addDataModelListener",
        new Class<?>[] { DataModelListener.class }, new Object[] { null }, pw);

  }

  public void dataModelGetRowDataTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    data.setRowIndex(0);
    // no backing data, should return null
    Object result = data.getRowData();
    if (result != null) {
      out.println("Test FAILED[1].  Expected DataModel.getRowData() to "
          + "return a null result.");
      return;
    }

    initDataModel(data);
    result = data.getRowData();
    Object bean = result;
    if (!bean.equals(beans.get(0))) {
      out.println("Test FAILED[1].  The Object returned by UIData."
          + "getRowData() at index 0 was not the expected Object.");
      out.println("Expected: " + beans.get(0));
      out.println("Recevied: " + bean);
      return;
    }

    // move the current row index up a few notches and verify we get
    // the expected result
    data.setRowIndex(5);
    result = data.getRowData();
    if (result == null) {
      out.println("Test FAILED[2].  Expected UIData.getRowData() to "
          + "returna non-null result.");
      return;
    }

    bean = result;
    if (!bean.equals(beans.get(5))) {
      out.println("Test FAILED[1].  The Object returned by UIData."
          + "getRowData() at index 5 was not expected the Object.");
      out.println("Expected: " + beans.get(5));
      out.println("Recevied: " + bean);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelGetRowDataIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();
    initDataModel(data);

    data.setRowIndex(11);
    try {
      data.getRowData();
      out.println("No Exception thrown when trying to retrieve a data"
          + " row when the index is out of the valid row range.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when trying"
            + " to retrieve a data row when the index is out of the"
            + " valid row range, but it wasn't an instance of"
            + " IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void dataModelListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    DataModel data = createDataModel();

    TCKDataListener listener1 = new TCKDataListener("l1");
    TCKDataListener listener2 = new TCKDataListener("l2");

    data.addDataModelListener(listener1);
    data.addDataModelListener(listener2);

    TCKDataListener.resetTrace();

    initDataModel(data);

    String trace = TCKDataListener.getTrace();
    if (!"/l1/l2".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " Unexpected listener trace after"
          + " calling setWrappedData() which should have set the row"
          + " index to 0 and caused an event to fire.");
      out.println("Exepcted: /l1/l2");
      out.println("Received: " + trace);
      return;
    }

    data = createDataModel();
    data.addDataModelListener(listener1);
    data.addDataModelListener(listener2);
    initDataModel(data);
    TCKDataListener.resetTrace();

    data.setRowIndex(2);
    trace = TCKDataListener.getTrace();
    if (!"/l1/l2".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " Unexpected listener trace after"
          + " calling setRowIndex() which should have cause an"
          + " event to fire.");
      out.println("Exepcted: /l1/l2");
      out.println("Received: " + trace);
      return;
    }

    // If the row index is set to the same value, no event should
    // be fired
    TCKDataListener.resetTrace();
    data.setRowIndex(2);

    trace = TCKDataListener.getTrace();

    if (trace.length() != 0) {
      out.println(JSFTestUtil.FAIL + " Didn't expect any trace information to"
          + " be returned from the listeners when the row index value"
          + " is set, but the value hasn't changed.");
      out.println("Trace recieved: " + trace);
      return;
    }

    // if there is no backing data, then the index is stored, but no
    // event is fired
    data = createDataModel();
    TCKDataListener.resetTrace();
    data.addDataModelListener(listener1);
    data.addDataModelListener(listener2);

    data.setRowIndex(1);

    trace = TCKDataListener.getTrace();

    if (trace.length() != 0) {
      out.println(JSFTestUtil.FAIL + " Didn't expect any trace information to"
          + " be returned from the listeners when there was no backing"
          + " data installed in the DataModel instance.");
      out.println("Trace recieved: " + trace);
      return;
    }

    if (data.getRowIndex() != 1) {
      out.println(JSFTestUtil.FAIL + " Expected the row index to be stored"
          + " even though no backing data had been set.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // --------------------------------------------------------- Private Classes

  private static class TCKDataListener implements DataModelListener {

    private static StringBuffer log;

    private String id;

    public TCKDataListener() {
      log = new StringBuffer();
    }

    public TCKDataListener(String id) {
      this.id = id;
      log = new StringBuffer();
    }

    /**
     * <p>
     * Notification that a particular row index, with the associated row data,
     * has been selected for processing.
     * </p>
     * 
     * @param event
     *          The {@link javax.faces.model.DataModelEvent} we are processing
     */
    public void rowSelected(DataModelEvent event) {
      System.out.println("ROW SELECTED");
      log.append("/" + id);
    }

    public static String getTrace() {
      return log.toString();
    }

    public static void resetTrace() {
      log = new StringBuffer();
    }
  }
}
