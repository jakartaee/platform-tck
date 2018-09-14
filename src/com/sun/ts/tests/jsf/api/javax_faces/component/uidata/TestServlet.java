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

package com.sun.ts.tests.jsf.api.javax_faces.component.uidata;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValidator;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.TCKValueChangeListener;
import com.sun.ts.tests.jsf.common.resolver.TCKELResolver;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseComponentTestServlet {

  protected DataModel<?> testDM;

  protected List<TCKDataBean> beans;

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendersChildren(Boolean.TRUE);
    setRendererType("javax.faces.Table");
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIData();
  }

  /**
   * <p>
   * Initialize a the objects used for the Model tier.
   * </p>
   */
  protected void initDataModel() {
    beans = new ArrayList<TCKDataBean>();
    for (int i = 0; i < 10; i++) {
      TCKDataBean bean = new TCKDataBean();
      bean.setCommand("command" + i);
      bean.setInput("input" + i);
      bean.setOutput("output" + i);
      beans.add(bean);
    }
    testDM = new TCKDataModel(beans);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  // UIData.{get,set}First()
  public void uiDataGetSetFirstTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    initDataModel();

    UIData data = (UIData) createComponent();

    data.setValue(testDM);

    int result = data.getFirst();
    if (result != 0) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getFirst() to return"
          + " 0 when called agains a newly created component.");
      out.println("Value returned: " + result);
      return;
    }

    data.setFirst(5);
    result = data.getFirst();
    if (result != 5) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getFirst() to return"
          + " the value as set by UIData.setFirst(), in this case" + " 5.");
      out.println("Value received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.{get,set}Footer()
  public void uiDataGetSetFooterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "Testing";
    UIData data = (UIData) createComponent();
    UIOutput footNotes = new UIOutput();

    footNotes.setId(golden);
    data.setFooter(footNotes);

    String result = data.getFooter().getId();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from UIData.getFooter()."
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Recieved: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiDataGetSetFooterTest

  // UIData.setFooter() throws NullPointerException
  public void uiDataSetFooterNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "setFooter",
        new Class<?>[] { UIComponent.class }, new Object[] { null }, out);

  }// End uiDataSetFooterNPETest

  // UIData.{get,set}Header()
  public void uiDataGetSetHeaderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "Testing";
    UIData data = (UIData) createComponent();
    UIOutput header = new UIOutput();

    header.setId(golden);
    data.setHeader(header);

    String result = data.getHeader().getId();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from UIData.getHeader()."
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Recieved: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiDataGetSetHeaderTest

  // UIData.setHeader() throws NullPointerException
  public void uiDataSetHeaderNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "setHeader",
        new Class<?>[] { UIComponent.class }, new Object[] { null }, out);

  }// End uiDataSetHeaderNPETest

  // UIData.queueEvent() throws NullPointerException
  public void uiDataQueueEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIViewRoot root = getFacesContext().getViewRoot();
    UIData data = (UIData) this.createComponent();

    root.getChildren().add(data);

    try {
      data.queueEvent(null);
      out.println(JSFTestUtil.FAIL + "No Exception thrown!" + JSFTestUtil.NL
          + "Expected a NullPointerException " + "to be thrown "
          + JSFTestUtil.NL + "when testing: UIData.queueEvent().");

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + JSFTestUtil.NPE_MESS);
      e.printStackTrace();
    }

  }// End uiDataQueueEventNPETest

  // UIData.queueEvent() throws IllegalStateException
  public void uiDataQueueEventISETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIData data = (UIData) createComponent();
    UIComponent root = getApplication()
        .createComponent(UIOutput.COMPONENT_TYPE);

    root.getChildren().add(data);

    JSFTestUtil.checkForISE(data, "queueEvent",
        new Class<?>[] { FacesEvent.class },
        new Object[] { new TCKFacesEvent(data) }, out);

  }// End uiDataQueueEventISETest

  // UIData.setFirst() throws IAE if argument is negative
  public void uiDataSetFirstIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    initDataModel();

    UIData data = (UIData) createComponent();

    data.setValue(testDM);

    try {
      data.setFirst(-1);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when UIData."
          + "setFirst() was called with a negative integer " + "argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when UIData."
            + "setFirst() was called with a negative integer"
            + " argument, but it wasn't an instance of"
            + " IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.isRowAvailable()
  public void uiDataIsRowAvailableTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    TCKDataModel model = new TCKDataModel();

    data.setValue(model);

    // Model with no wrapped data source, isRowAvailable() must return
    // false.
    if (data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.isRowAvailable() to "
          + "return false if the DataModel object didn't wrap"
          + " an underlying data source.");
      return;
    }

    initDataModel();

    data.setValue(testDM);
    data.setFirst(0);
    data.setRowIndex(0);

    if (!data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.isRowAvailable() to"
          + " return true when row index was within a valid range.");
      return;
    }

    // next, set the row index out of range. isRowAvailable() should
    // return false.
    data.setRowIndex(11);
    if (data.isRowAvailable()) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.isRowAvailable() to"
          + " return false when specified row index was out of" + " range.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.getRowCount()
  public void uiDataGetRowCountTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    TCKDataModel model = new TCKDataModel();

    data.setValue(model);

    // There is no backing data in the DataModel instance,
    // so getRowCount() should return 01
    int result = data.getRowCount();
    if (result != -1) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getRowCount() to"
          + " return -1 if no data was available on the Model " + "tier.");
      out.println("Row count received: " + result);
      return;
    }

    initDataModel();
    data.setValue(testDM);

    // backing DataModel instance has 10 objects, therefore getRowCount()
    // should return 10.
    result = data.getRowCount();
    if (result != 10) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getRowCount() to "
          + "return 10.");
      out.println("Row count received: " + result);
      return;
    }

    // make sure the rowcount is handled dynamically
    beans.remove(9);
    result = data.getRowCount();
    if (result != 9) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getRowCount() to "
          + "return 9.");
      out.println("Row count received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.getRowData()
  public void uiDataGetRowDataTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);

    // using the same indexes, getRowData() should return the first
    // Object from the DataModel
    Object result = data.getRowData();
    if (result == null) {
      out.println("Test FAILED[1].  Expected UIData.getRowData() to "
          + "returna non-null result.");
      return;
    }

    TCKDataBean bean = (TCKDataBean) result;
    if (bean != beans.get(0)) {
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

    bean = (TCKDataBean) result;
    if (bean != beans.get(5)) {
      out.println("Test FAILED[1].  The Object returned by UIData."
          + "getRowData() at index 5 was not expected the Object.");
      out.println("Expected: " + beans.get(5));
      out.println("Recevied: " + bean);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.getRowData throws IAE if the current index is out of range
  public void uiDataGetRowDataIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);
    data.setFirst(0);
    data.setRowIndex(11);

    try {
      data.getRowData();
      out.println(JSFTestUtil.FAIL + " No Exception thrown when UIData."
          + "getRowData() was called and the row index was out" + " of range.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when UIData."
            + "getRowData() was called and the row index was "
            + "out of range, but it wasn't an intance of "
            + "IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.{get,set}RowIndex()
  public void uiDataGetSetRowIndexTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();
    String attrName = "com.sun.ts.jsftck.rowidx";

    initDataModel();

    data.setValue(testDM);

    // no index has been specified, so getRowIndex() should return -1
    if (!checkRowIndex(data, -1, out)) {
      return;
    }

    // blur some testing boundries a bit here. If 'var' is specified,
    // then the data value at the index specified will be placed in
    // a request scoped variable with the value of 'var' as the key.
    data.setVar(attrName);
    data.setRowIndex(0);

    Object ret = request.getAttribute(attrName);
    if (!beans.get(0).equals(ret)) {
      out.println(JSFTestUtil.FAIL + " The object stored in Request scope"
          + " after calling setRowIndex() was not the expected" + " Object.");
      out.println("Expected: " + beans.get(0));
      out.println("Received: " + ret);
      return;
    }

    if (!checkRowIndex(data, 0, out)) {
      return;
    }

    // If 'var' is specified and we set the rowIndex to -1, then remove
    // the request scoped object associated with that name.
    data.setRowIndex(-1);

    ret = request.getAttribute(attrName);
    if (ret != null) {
      out.println(JSFTestUtil.FAIL + " Expected the request scope variable "
          + "'" + attrName + "'to be removed when setRowIndex("
          + ") was called passing -1.");
      out.println("Object received: " + ret);
      return;
    }

    if (!checkRowIndex(data, -1, out)) {
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.setRowIndex throws IAE if arg passed is less than -1
  public void uiDataSetRowIndexIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);

    try {
      data.setRowIndex(-2);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when UIData."
          + "setRowIndex() was called with a row index that was"
          + " less than -1");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when UIData."
            + "setRowIndex() was called with a row index that "
            + "was less than -1, but it wasn't an intance of "
            + "IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.{get,set}Rows()
  public void uiDataGetSetRowsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);

    data.setRows(5);
    int result = data.getRows();
    if (result != 5) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getRows() to return"
          + " the value as set by UIData.setRows().");
      out.println("Expected: 5");
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.setRows() throws IAE if arg is a negative integer
  public void uiDataSetRowsIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);

    try {
      data.setRows(-1);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when UIData."
          + "setRows() was called with a negative integer " + "argument");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when UIData."
            + "setRows() was called with a negative integer"
            + "argument, but it wasn't an intance of "
            + "IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.setValueExpression() throws NullPointerException
  public void uiDataSetValueExpressionNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "setValueExpression",
        new Class<?>[] { String.class, ValueExpression.class },
        new Object[] { null, new TCKVallueExpr() }, out);

  }// End uiDataSetValueExpressionNPETest

  // UIData.{get,set}Var()
  public void uiDataGetSetVarTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();

    initDataModel();

    data.setValue(testDM);

    data.setVar("var");
    String result = data.getVar();
    if (!"var".equals(result)) {
      out.println(JSFTestUtil.FAIL + " Expected UIData.getVar() to return"
          + " the value as set by UIData.setVar().");
      out.println("Expected: 'var'");
      out.println("Received: '" + result + "'");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiDataGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIData data = (UIData) createComponent();
    data.setValue("value");

    if (!"value".equals(data.getValue())) {
      out.println(JSFTestUtil.FAIL + " Value returned by getValue() was"
          + " not the value as set by setValue().");
      out.println("Expected: value");
      out.println("Received: " + data.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiDataPositiveInvokeOnComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    final PrintWriter out = response.getWriter();
    out.println("beginning uiDataPositiveInvokeOnComponentTest");
    initDataModel();

    // Create UIData component
    UIData data = (UIData) createComponent();
    data.setFirst(3);
    data.setRows(5);
    data.setVar("foo");
    data.setValue(testDM);
    if (data.getValue() == null || testDM != data.getValue()) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + " Bad value for data.getValue()");
      return;
    }

    // build component tree
    UIViewRoot viewRoot = new UIViewRoot();
    viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    viewRoot.setViewId("/view");
    getFacesContext().setViewRoot(viewRoot);

    UIForm form1 = new UIForm();
    form1.setId("form1");
    viewRoot.getChildren().add(form1);
    setupTree(form1, data);

    // find the data component
    UIData data1 = (UIData) viewRoot.findComponent("form1:data");

    boolean found = false;
    data1.setRowIndex(3);

    if (!checkRowIndex(data1, 3, out)) {
      return;
    }

    found = viewRoot.invokeOnComponent(getFacesContext(),
        "form1:data:4:commandHeader", new ContextCallback() {

          public void invokeContextCallback(FacesContext context,
              UIComponent component) {
            out.println("entering invokeContextCallback()");

            UIData data = (UIData) getNamingContainer(component);
            UIForm form = (UIForm) getNamingContainer(data);

            if (!checkRowIndex(data, 4, out)) {
              return;
            }

            if (!"form1".equals(form.getId())) {
              out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
                  + "Incorrect form id" + JSFTestUtil.NL + "Expected: form1"
                  + JSFTestUtil.NL + "Computed: " + form.getId());
            }

            if (!"commandHeader".equals(component.getId())) {
              out.println(
                  JSFTestUtil.FAIL + JSFTestUtil.NL + "Incorrect component id"
                      + JSFTestUtil.NL + "Expected: commandHeader"
                      + JSFTestUtil.NL + "Computed: " + component.getId());
            }
          }
        });

    if (!checkRowIndex(data1, 3, out)) {
      return;
    }

    if (!found) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Call to invokeOnComponent() returned false");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.processDecodes() throws NullPointerException
  public void uiDataProcessDecodesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIViewRoot root = getFacesContext().getViewRoot();
    UIData data = (UIData) this.createComponent();

    root.getChildren().add(data);

    try {
      data.processDecodes(null);

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + JSFTestUtil.NPE_MESS);
      e.printStackTrace();
    }

  }// End uiDataProcessDecodesNPETest

  public void uiDataNPEInvokeOnComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("beginning uiDataNPEInvokeOnComponentTest");
    initDataModel();

    // Create UIData component
    UIData data = (UIData) createComponent();
    data.setFirst(3);
    data.setRows(5);
    data.setVar("foo");
    data.setValue(testDM);
    if (data.getValue() == null || testDM != data.getValue()) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + " Bad value for data.getValue()");
      return;
    }

    // build component tree
    UIViewRoot viewRoot = new UIViewRoot();
    viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    viewRoot.setViewId("/view");
    getFacesContext().setViewRoot(viewRoot);

    UIForm form1 = new UIForm();
    form1.setId("form1");
    viewRoot.getChildren().add(form1);
    setupTree(form1, data);

    int npesCaught = 0;

    try {
      viewRoot.invokeOnComponent(null, "form1:data:4:commandHeader",
          new ContextCallback() {

            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
            }
          });
      out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
      out.println("null FacesContext argument did not throw an exception");

    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
        out.println("null FacesContext argument did not throw a");
        out.println(
            "NullPointerException, instead threw " + e.getClass().getName());
      } else {
        ++npesCaught;
      }
    }

    try {
      viewRoot.invokeOnComponent(getFacesContext(), null,
          new ContextCallback() {

            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
            }
          });
      out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
      out.println("null clientId argument did not throw an exception");

    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
        out.println("null clientId argument did not throw a");
        out.println(
            "NullPointerException, instead threw " + e.getClass().getName());
      } else {
        ++npesCaught;
      }
    }

    try {
      viewRoot.invokeOnComponent(getFacesContext(),
          "form1:data:4:commandHeader", null);
      out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
      out.println("null callback argument did not throw an exception");

    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + "invokeOnComponent() called with");
        out.println("null callback argument did not throw a");
        out.println(
            "NullPointerException, instead threw " + e.getClass().getName());
      } else {
        ++npesCaught;
      }
    }

    if (npesCaught == 3)
      out.println(JSFTestUtil.PASS);
  }

  private Object foundComponent;

  public void uiDataFEInvokeOnComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    out.println("beginning uiDataFEInvokeOnComponentTest");
    initDataModel();

    // Create UIData component
    UIData data = (UIData) createComponent();
    data.setFirst(3);
    data.setRows(5);
    data.setVar("foo");
    data.setValue(testDM);
    if (data.getValue() == null || testDM != data.getValue()) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + " Bad value for data.getValue()");
      return;
    }

    // build component tree
    UIViewRoot viewRoot = new UIViewRoot();
    viewRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    viewRoot.setViewId("/view");
    getFacesContext().setViewRoot(viewRoot);

    UIForm form1 = new UIForm();
    form1.setId("form1");
    viewRoot.getChildren().add(form1);
    setupTree(form1, data);

    // foundComponent = null;
    FacesContext context = getFacesContext();
    boolean result = false;

    // Negative case 1, not found component.
    result = viewRoot.invokeOnComponent(context, "form1:input7",
        new ContextCallback() {
          public void invokeContextCallback(FacesContext context,
              UIComponent component) {
            foundComponent = component;
          }
        });

    if (result) {
      out.println(JSFTestUtil.FAIL + " Expected \"false\" returned by"
          + " InvokeOnComponent() when passing in \"form:input7\""
          + " as clientId arg. Instead received " + result);
      return;
    }

    // Negative case 2A, callback throws exception with found component
    try {
      result = viewRoot.invokeOnComponent(context, "form1:data:4:commandHeader",
          new ContextCallback() {
            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
              foundComponent = component;
              // When else am I going to get the chance to throw
              // this exception?
              throw new IllegalStateException();
            }
          });
    } catch (Exception e) {
      if (!(e instanceof FacesException)) {
        out.println(JSFTestUtil.FAIL + "invokeOnComponent(facesContext,"
            + " form2:input2, Callback) threw an Exception,"
            + " but it wasn't an instance of FacesException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Negative case 2B, callback throws exception with not found component
    try {
      result = viewRoot.invokeOnComponent(context, "form2:input6",
          new ContextCallback() {
            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
              foundComponent = component;
              // When else am I going to get the chance to throw
              // this exception?
              throw new IllegalStateException();
            }
          });
    } catch (Exception e) {
      if (!(e instanceof FacesException)) {
        out.println(JSFTestUtil.FAIL + "invokeOnComponent(facesContext,"
            + " form2:input6, Callback) threw an Exception,"
            + " but it wasn't an instance of FacesException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIData.createUniqueId()
  public void uiDataCreateUniqueIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    initDataModel();
    String seed = "tckId";
    UIData data = (UIData) createComponent();
    String uniqueId = data.createUniqueId(context, seed);

    if (!uniqueId.contains(seed)) {
      out.println(JSFTestUtil.FAIL + "Test Value not found in ID!"
          + JSFTestUtil.NL + "Expected Component ID to contain: " + seed
          + JSFTestUtil.NL + "Received: " + uniqueId);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End uiDataCreateUniqueIdTest

  // ------------------------------------------------------------- Private
  // Classes

  private static class TCKDataActionListener implements ActionListener {

    // ---------------------------------------------------------- Public
    // Methods

    public PhaseId getPhaseId() {
      return (PhaseId.ANY_PHASE);
    }

    public void processAction(ActionEvent event) {

      trace(
          event.getComponent().getClientId(FacesContext.getCurrentInstance()));

    }

    // ---------------------------------------------------- Static Trace
    // Methods

    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
      if (text == null) {
        trace.setLength(0);
      } else {
        trace.append('/');
        trace.append(text);
      }
    }

    // Retrieve the current trace log
    public static String trace() {
      return (trace.toString());
    }

  }

  private static class TCKDataValidator implements Validator {

    // ---------------------------------------------------------- Public
    // Methods

    public void validate(FacesContext context, UIComponent component,
        Object value) {
      trace(component.getClientId(context));
      Object vlu;

      vlu = (value == null) ? "" : value;

      trace((String) vlu);
      if ("bad".equals(vlu)) {
        trace("ERROR");
        context.addMessage(component.getClientId(context), new FacesMessage(
            FacesMessage.SEVERITY_ERROR, component.getClientId(context), null));
        ((UIInput) component).setValid(false);
      }

    }

    // ---------------------------------------------------- Static Trace
    // Methods

    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
      if (text == null) {
        trace.setLength(0);
      } else {
        trace.append('/');
        trace.append(text);
      }
    }

    // Retrieve the current trace log
    public static String trace() {
      return (trace.toString());
    }

  }

  // --------------------------- private classes

  /**
   * <p>
   * <strong>ListDataModel</strong> is a convenience implementation of
   * {@link DataModel} that wraps an <code>List</code> of Java objects.
   * </p>
   */

  public static class TCKDataModel extends DataModel {

    private int index;

    private List list;

    /**
     * <p>
     * Construct a new {@link TCKDataModel} with no specified wrapped data.
     * </p>
     */
    public TCKDataModel() {
      this(null);
    }

    /**
     * <p>
     * Construct a new {@link TCKDataModel} wrapping the specified list.
     * </p>
     * 
     * @param list
     *          List to be wrapped (if any)
     */
    public TCKDataModel(List list) {
      setWrappedData(list);
    }

    public boolean isRowAvailable() {

      if (list == null) {
        return (false);
      } else if ((index >= 0) && (index < list.size())) {
        return (true);
      } else {
        return (false);
      }

    }

    public int getRowCount() {

      if (list == null) {
        return (-1);
      }
      return (list.size());

    }

    public Object getRowData() {

      if (list == null) {
        return (null);
      } else if (!isRowAvailable()) {
        throw new IllegalArgumentException();
      } else {
        return (list.get(index));
      }

    }

    public int getRowIndex() {

      return (index);

    }

    public void setRowIndex(int rowIndex) {

      if (rowIndex < -1) {
        throw new IllegalArgumentException();
      }
      int old = index;
      index = rowIndex;
      if (list == null) {
        return;
      }
      DataModelListener[] listeners = getDataModelListeners();
      if ((old != index) && (listeners != null)) {
        Object rowData = null;
        if (isRowAvailable()) {
          rowData = getRowData();
        }
        DataModelEvent event = new DataModelEvent(this, index, rowData);

        for (int i = 0; i < listeners.length; i++) {
          listeners[i].rowSelected(event);
        }
      }

    }

    public Object getWrappedData() {

      return (this.list);

    }

    public void setWrappedData(Object data) {

      if (data == null) {
        list = null;
        return;
      }
      list = (List) data;
      index = 0;

    }

  }// End TCKDataModel

  public static class TCKDataBean implements Serializable {

    private String command;

    public String getCommand() {
      return (this.command);
    }

    public void setCommand(String command) {
      this.command = command;
    }

    private String input;

    public String getInput() {
      return (this.input);
    }

    public void setInput(String input) {
      this.input = input;
    }

    private String output;

    public String getOutput() {
      return (this.output);
    }

    public void setOutput(String output) {
      this.output = output;
    }

    public String toString() {
      return (command + "|" + input + "|" + output);
    }

  }

  private void setupTree(UIComponent root, UIData data) {

    // Attach our UIData to the view root
    // UIData data = new UIData();
    data.setId("data");
    root.getChildren().add(data);

    // Set up columns with facets and fields for each property
    UIColumn column;
    UICommand command;
    UIInput input;
    UIOutput output;
    UIOutput label;
    UIOutput constant;

    column = new UIColumn();
    column.setId("commandColumn");
    label = new UIOutput();
    label.setId("commandHeader");
    label.setValue("Command Header");
    column.getFacets().put("header", label);

    label = new UIOutput();
    label.setId("commandFooter");
    label.setValue("Command Footer");
    column.getFacets().put("footer", label);
    command = new UICommand();
    command.setId("command");
    command.setValueBinding("value",
        getApplication().createValueBinding("#{foo.command}"));
    column.getChildren().add(command);
    data.getChildren().add(column);
    command.addActionListener(new TCKDataActionListener());

    column = new UIColumn();
    column.setId("inputColumn");
    label = new UIOutput();
    label.setId("inputHeader");
    label.setValue("Input Header");
    column.getFacets().put("header", label);

    label = new UIOutput();
    label.setId("inputFooter");
    label.setValue("Input Footer");
    column.getFacets().put("footer", label);
    input = new UIInput();
    input.setId("input");
    input.setValueBinding("value",
        getApplication().createValueBinding("#{foo.input}"));
    column.getChildren().add(input);
    data.getChildren().add(column);

    input.addValidator(new TCKValidator());
    input.addValueChangeListener(new TCKValueChangeListener());

    column = new UIColumn();
    column.setId("outputColumn");
    label = new UIOutput();
    label.setId("outputHeader");
    label.setValue("Output Header");
    column.getFacets().put("header", label);

    label = new UIOutput();
    label.setId("outputFooter");
    label.setValue("Output Footer");
    column.getFacets().put("footer", label);

    output = new UIOutput();
    output.setId("output");
    output.setValueBinding("value",
        getApplication().createValueBinding("#{foo.output}"));
    column.getChildren().add(output);
    data.getChildren().add(column);

    column = new UIColumn();
    column.setId("constantColumn");
    label = new UIOutput();
    label.setId("constantHeader");
    label.setValue("Constant Header");
    column.getFacets().put("header", label);

    label = new UIOutput();
    label.setId("constantFooter");
    label.setValue("Constant Footer");
    column.getFacets().put("footer", label);
    constant = new UIOutput();
    constant.setId("constant");
    constant.setValue("Constant Value");
    column.getChildren().add(constant);
    data.getChildren().add(column);

  }

  private UIComponent getNamingContainer(UIComponent start) {
    UIComponent namingContainer = start.getParent();
    while (namingContainer != null) {
      if (namingContainer instanceof NamingContainer) {
        return namingContainer;
      }
      namingContainer = namingContainer.getParent();
    }
    return null;
  }

  // UIData.broadcast(FacesContext) throws NullPointerException
  public void uiDataBroadcastNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIData command = (UIData) createComponent();
    command.setRendererType(null);

    try {
      command.broadcast(null);
      out.println(JSFTestUtil.FAIL
          + "Expected a NullPointerException to be thrown and it wasn't!");

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + "Wrong Exception Thrown!" + JSFTestUtil.NL
          + "Expected: NullPointerException" + JSFTestUtil.NL + "Received: "
          + e.toString());
    }

  }

  public void uiDatatVisitTreeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "visitTree",
        new Class<?>[] { VisitContext.class, VisitCallback.class },
        new Object[] { null, new TCKVisitCallback() }, out);

    JSFTestUtil.checkForNPE(createComponent().getClass(), "visitTree",
        new Class<?>[] { VisitContext.class, VisitCallback.class },
        new Object[] { VisitContext.createVisitContext(getFacesContext()),
            null },
        out);

  } // End uiDatatVisitTreeNPETest

  // UIData.{is,set}RowStatePreserved()
  public void uiDataIsSetRowStatePreservedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    initDataModel();

    boolean resultOne;
    boolean resultTwo;
    String result;
    UIData data = (UIData) createComponent();

    data.setRowStatePreserved(true);
    resultOne = data.isRowStatePreserved();

    data.setRowStatePreserved(false);
    resultTwo = data.isRowStatePreserved();

    result = (resultOne && (!resultTwo)) ? JSFTestUtil.PASS : JSFTestUtil.FAIL;

    out.println(result);

  } // End uiDataIsSetRowStatePreservedTest

  // ------------------------------------- private implementations

  private boolean checkRowIndex(UIData data, int expected, PrintWriter out) {
    if (data.getRowIndex() != expected) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for getRowIndex() after setting row " + "index."
          + JSFTestUtil.NL + "Expected value: " + expected + JSFTestUtil.NL
          + "Computed value: " + data.getRowIndex());
      return false;
    }

    return true;
  }

  private static class TCKVisitCallback implements VisitCallback {

    @Override
    public VisitResult visit(VisitContext context, UIComponent target) {
      // TODO Auto-generated method stub
      return null;
    }

  }

  private static class TCKFacesEvent extends FacesEvent {

    public TCKFacesEvent(UIComponent component) {
      super(component);
    }

    public boolean isAppropriateListener(FacesListener listener) {
      return false;
    }

    public void processListener(FacesListener listener) {

    }
  }

  private static class TCKVallueExpr extends ValueExpression {

    @Override
    public Class<?> getExpectedType() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Class<?> getType(ELContext arg0) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object getValue(ELContext arg0) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isReadOnly(ELContext arg0) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void setValue(ELContext arg0, Object arg1) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean equals(Object arg0) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public String getExpressionString() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public int hashCode() {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean isLiteralText() {
      // TODO Auto-generated method stub
      return false;
    }

  }

}
