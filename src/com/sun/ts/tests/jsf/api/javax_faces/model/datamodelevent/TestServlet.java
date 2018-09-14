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

package com.sun.ts.tests.jsf.api.javax_faces.model.datamodelevent;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.model.DataModel;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModelEvent;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {
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

  // ---------------------------------------------------------------- Test
  // Methods

  public void dataModelEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String[] data = { "string" };
    DataModel model = new ArrayDataModel(data);

    DataModelEvent event = new DataModelEvent(model, 0, "string1");

    if (event.getDataModel() != model) {
      out.println(JSFTestUtil.FAIL + " getDataModel() didn't return"
          + "the expected 'model'." + JSFTestUtil.NL + "Expected: "
          + model.toString() + "Received: " + event.getDataModel().toString());

      return;
    }

    if (event.getRowIndex() != 0) {
      out.println(
          JSFTestUtil.FAIL + " Unexpected v alue returned from getRowIndex()!"
              + JSFTestUtil.NL + "Row index received: " + event.getRowIndex());

      return;
    }

    if (event.getRowData() != "string1") {
      out.println(
          JSFTestUtil.FAIL + " getRowData() returned an unexpected value."
              + JSFTestUtil.NL + "Expected: " + "string1" + JSFTestUtil.NL
              + "Received: " + event.getRowData());

      return;
    }

    event = new DataModelEvent(model, -1, null);

    if (event.getDataModel() != model) {
      out.println(JSFTestUtil.FAIL
          + " getDataModel() didn't return the expected value." + JSFTestUtil.NL
          + "Expected: " + model.toString() + JSFTestUtil.NL + "Received: "
          + event.getDataModel().toString());

      return;
    }

    if (event.getRowIndex() != -1) {
      out.println(JSFTestUtil.FAIL + " Expected getRowIndex() to return -1."
          + JSFTestUtil.NL + "Row index received: " + event.getRowIndex());

      return;
    }

    if (event.getRowData() != null) {
      out.println(
          JSFTestUtil.FAIL + " getRowData() returned an unexpected value."
              + JSFTestUtil.NL + "Expected: null" + JSFTestUtil.NL
              + "Received: " + event.getRowData());

      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
