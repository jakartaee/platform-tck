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

package com.sun.ts.tests.jsf.api.javax_faces.model.resultdatamodel;

import com.sun.ts.tests.jsf.api.javax_faces.model.common.BaseModelTestServlet;
import com.sun.ts.tests.jsf.api.javax_faces.model.common.TCKResult;
import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.model.DataModel;
import javax.faces.model.ResultDataModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
    return new ResultDataModel();
  }

  public void initDataModel(DataModel model) {
    List list = new ArrayList();
    for (int i = 0; i < 10; i++) {
      list.add(new TestBean());
    }
    setBeansList(list);
    TCKResult result = new TCKResult(list.toArray());
    model.setWrappedData(result);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  public void resultDataModelCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = new ResultDataModel(
        new TCKResult(new String[] { "string1", "string2" }));

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

    Result result = new TCKResult(new String[] { "string1" });

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
    Object expected = null;

    try {
      expected = TCKResult.getReadableValues(beans.get(0));
    } catch (Exception e) {
      throw new ServletException(e);
    }

    if (!bean.equals(expected)) {
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
          + "return a non-null result.");
      return;
    }

    bean = result;
    try {
      expected = TCKResult.getReadableValues(beans.get(5));
    } catch (Exception e) {
      throw new ServletException(e);
    }

    if (!bean.equals(expected)) {
      out.println("Test FAILED[1].  The Object returned by UIData."
          + "getRowData() at index 5 was not expected the Object.");
      out.println("Expected: " + beans.get(5));
      out.println("Recevied: " + bean);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
