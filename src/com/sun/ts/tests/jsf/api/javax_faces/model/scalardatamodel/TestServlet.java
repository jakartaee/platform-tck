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

package com.sun.ts.tests.jsf.api.javax_faces.model.scalardatamodel;

import com.sun.ts.tests.jsf.api.javax_faces.model.common.BaseModelTestServlet;
import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.model.DataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

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
    return new ScalarDataModel();
  }

  public void initDataModel(DataModel model) {
    TestBean bean = new TestBean();
    List list = new ArrayList();
    list.add(bean);
    setBeansList(list);
    model.setWrappedData(bean);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  public void scalarDataModelCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = new ScalarDataModel(new TestBean());

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

    TestBean bean = new TestBean();

    model.setWrappedData(bean);

    Object ret = model.getWrappedData();

    if (!bean.equals(ret)) {
      out.println(JSFTestUtil.FAIL + " The value returned from getWrappedData()"
          + " was not the same as what was set via setWrappedData().");
      out.println("Expected: " + bean);
      out.println("Received: " + ret);
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

    // ScalarDataModels only have a single backing object, not a collection,
    // so this should be 1.
    result = data.getRowCount();
    if (result != 1) {
      out.println(JSFTestUtil.FAIL + " Expected DataModel.getRowCount() to "
          + "return 1.");
      out.println("Row count received: " + result);
      return;
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
    if (!bean.equals(beans.get(0))) {
      out.println("Test FAILED[1].  The Object returned by UIData."
          + "getRowData() at index 0 was not the expected Object.");
      out.println("Expected: " + beans.get(0));
      out.println("Recevied: " + bean);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
