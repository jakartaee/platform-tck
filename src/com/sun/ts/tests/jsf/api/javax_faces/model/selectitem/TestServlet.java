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

package com.sun.ts.tests.jsf.api.javax_faces.model.selectitem;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.model.SelectItem;

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

  // --------------------------------------------------- Test Methods

  public void selectItemCtor0Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      new SelectItem();
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item;
    try {
      item = new SelectItem("value");
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    if (!"value".equals(item.getLabel())) {
      out.println(
          JSFTestUtil.FAIL + " getLabel() didn't return the default value.");
      out.println("Expected: value");
      out.println("Received: " + item.getLabel());
      return;
    }

    if (item.getDescription() != null) {
      out.println(JSFTestUtil.FAIL
          + " getDescription() didn't return the default value.");
      out.println("Expected: null");
      out.println("Received: " + item.getDescription());
      return;
    }

    if (item.isDisabled()) {
      out.println(
          JSFTestUtil.FAIL + " isDisabled() didn't return the default value.");
      out.println("Expected: false");
      out.println("Received: " + item.isDisabled());
      return;
    }

    if (!item.isEscape()) {
      out.println(
          JSFTestUtil.FAIL + " isEscape() didn't return the default value.");
      out.println("Expected: true");
      out.println("Received: " + item.isEscape());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item;
    try {
      item = new SelectItem("value", "label");
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    if (!"label".equals(item.getLabel())) {
      out.println(JSFTestUtil.FAIL + " getLabel() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: label");
      out.println("Received: " + item.getLabel());
      return;
    }

    if (item.getDescription() != null) {
      out.println(JSFTestUtil.FAIL
          + " getDescription() didn't return the default value.");
      out.println("Expected: null");
      out.println("Received: " + item.getDescription());
      return;
    }

    if (item.isDisabled()) {
      out.println(
          JSFTestUtil.FAIL + " isDisabled() didn't return the default value.");
      out.println("Expected: false");
      out.println("Received: " + item.isDisabled());
      return;
    }

    if (!item.isEscape()) {
      out.println(
          JSFTestUtil.FAIL + " isEscape() didn't return the default value.");
      out.println("Expected: true");
      out.println("Received: " + item.isEscape());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemCtor3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item;
    try {
      item = new SelectItem("value", "label", "description");
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    if (!"label".equals(item.getLabel())) {
      out.println(JSFTestUtil.FAIL + " getLabel() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: label");
      out.println("Received: " + item.getLabel());
      return;
    }

    if (!"description".equals(item.getDescription())) {
      out.println(JSFTestUtil.FAIL + " getDescription() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: description");
      out.println("Received: " + item.getDescription());
      return;
    }

    if (item.isDisabled()) {
      out.println(
          JSFTestUtil.FAIL + " isDisabled() didn't return the default value.");
      out.println("Expected: false");
      out.println("Received: " + item.isDisabled());
      return;
    }

    if (!item.isEscape()) {
      out.println(
          JSFTestUtil.FAIL + " isEscape() didn't return the default value.");
      out.println("Expected: true");
      out.println("Received: " + item.isEscape());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemCtor4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item;
    try {
      item = new SelectItem("value", "label", "description", true);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    if (!"label".equals(item.getLabel())) {
      out.println(JSFTestUtil.FAIL + " getLabel() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: label");
      out.println("Received: " + item.getLabel());
      return;
    }

    if (!"description".equals(item.getDescription())) {
      out.println(JSFTestUtil.FAIL + " getDescription() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: description");
      out.println("Received: " + item.getDescription());
      return;
    }

    if (!item.isDisabled()) {
      out.println(JSFTestUtil.FAIL + " Expected isDisabled to return true as"
          + " provided to the ctor.");
      return;
    }

    if (!item.isEscape()) {
      out.println(
          JSFTestUtil.FAIL + " isEscape() didn't return the default value.");
      out.println("Expected: true");
      out.println("Received: " + item.isEscape());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemCtor5Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item;
    try {
      item = new SelectItem("value", "label", "description", true, false);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception: " + e);
      return;
    }

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    if (!"label".equals(item.getLabel())) {
      out.println(JSFTestUtil.FAIL + " getLabel() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: label");
      out.println("Received: " + item.getLabel());
      return;
    }

    if (!"description".equals(item.getDescription())) {
      out.println(JSFTestUtil.FAIL + " getDescription() didn't return the value"
          + " provided to the ctor.");
      out.println("Expected: description");
      out.println("Received: " + item.getDescription());
      return;
    }

    if (!item.isDisabled()) {
      out.println(JSFTestUtil.FAIL + " Expected isDisabled to return true as"
          + " provided to the ctor.");
      return;
    }

    if (item.isEscape()) {
      out.println(JSFTestUtil.FAIL + " Expected isEscape to return false as"
          + " provided to the ctor.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item = new SelectItem();
    item.setValue("value");

    if (!"value".equals(item.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() didn't return the value"
          + " provided to setValue().");
      out.println("Expected: value");
      out.println("Received: " + item.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemGetSetLabelTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item = new SelectItem();
    item.setLabel("label");

    if (!"label".equals(item.getLabel())) {
      out.println(JSFTestUtil.FAIL + " getLabel() didn't return the value"
          + " provided to setLabel().");
      out.println("Expected: label");
      out.println("Received: " + item.getLabel());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemGetSetDescriptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item = new SelectItem();
    item.setDescription("description");

    if (!"description".equals(item.getDescription())) {
      out.println(JSFTestUtil.FAIL + " getDescription() didn't return the value"
          + " provided to setDescription().");
      out.println("Expected: description");
      out.println("Received: " + item.getDescription());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemIsSetDisabledTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item = new SelectItem();

    if (item.isDisabled()) {
      out.println(JSFTestUtil.FAIL + " Expected isDisabled() to return false"
          + " on a newly created instance.");
      return;
    }

    item.setDisabled(true);

    if (!item.isDisabled()) {
      out.println(JSFTestUtil.FAIL + " Expected isDisabled to return true "
          + "after having set it as such via setDisabled().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemIsSetEscapeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItem item = new SelectItem();

    item.setEscape(true);

    if (!item.isEscape()) {
      out.println(JSFTestUtil.FAIL + " Expected isDisabled to return true "
          + "after having set it as such via setDisabled().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }
}
