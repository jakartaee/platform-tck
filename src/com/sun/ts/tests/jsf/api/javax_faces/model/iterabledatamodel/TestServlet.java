/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: TestServlet.java 67940 2012-10-03 16:24:42Z dougd $
 */

package com.sun.ts.tests.jsf.api.javax_faces.model.iterabledatamodel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.IterableDataModel;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.model.common.BaseModelTestServlet;
import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

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
    return new IterableDataModel();
  }

  public void initDataModel(DataModel model) {
    List list = new ArrayList();
    for (int i = 0; i < 10; i++) {
      list.add(new TestBean());
    }
    setBeansList(list);
    model.setWrappedData(list);
  }

  // ------------------------------------------- Test Methods

  public void iterableDataModelCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = new IterableDataModel(
        Arrays.asList(new String[] { "string1", "string2" }));

    int curRow = model.getRowIndex();

    if (curRow != 0) {
      out.println(JSFTestUtil.FAIL + "! Expected getRowIndex() to return 0"
          + " when called against DataModel instance created by"
          + " passing data to wrap to constructor." + JSFTestUtil.NL
          + "Row index returned: " + curRow);
      return;
    }

    if (!model.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + "! Expected isRowAvailable() to return"
          + " true when called against DataModel instance created"
          + " by passing data to wrap to constructor.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void iterableDataModelGetSetWrappedDataTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    DataModel model = createDataModel();

    List<String> list = new ArrayList<String>();
    list.add("string1");
    list.add("string2");

    model.setWrappedData(list);

    List<String> ret = (List<String>) model.getWrappedData();

    if (!list.equals(ret)) {
      out.println(
          JSFTestUtil.FAIL + "! The value returned from getWrappedData()"
              + " was not the same as what was set via setWrappedData()."
              + JSFTestUtil.NL + "Expected: " + list + JSFTestUtil.NL
              + "Received: " + ret);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void iterableDataModelSetWrappedDataCCETest(HttpServletRequest request,
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
        out.println(JSFTestUtil.FAIL + "! Exception thrown when calling"
            + " setWrappedData() with an invalid type, but it wasn't"
            + " an instance of ClassCastException." + JSFTestUtil.NL
            + "Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

}
