/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.component.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.component.ActionSource2;
import jakarta.faces.event.MethodExpressionActionListener;
import jakarta.faces.context.FacesContext;
import jakarta.el.MethodExpression;
import jakarta.el.ELContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.PhaseId;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * Base tests for methods defined in {@link ActionListener}.
 * </p>
 */
public abstract class BaseActionSourceTestServlet
    extends BaseEditableValueHolderTestServlet {

  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // ActionSource.{get,add,remove}ActionListener(s)()
  public void actionSourceAddGetRemoveActListTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ActionSource2 source = (ActionSource2) createComponent();

    ActionListener[] listeners = source.getActionListeners();

    if (listeners == null) {
      out.println("Test FAILED[1].  getActionListeners() returned null.");
      return;
    }

    // remove any existing listeners
    for (int i = 0; i < listeners.length; i++) {
      source.removeActionListener(listeners[i]);
    }

    listeners = source.getActionListeners();

    if (listeners == null) {
      out.println("Test FAILED[2].  getActionListeners() returned null.");
      return;
    }

    if (listeners.length != 0) {
      out.println(JSFTestUtil.FAIL + " Expected getActionListeners() to return"
          + "a empty array after removing all ActionListeners.");
      out.println("Array length: " + listeners.length);
      return;
    }

    ActionListener listenerA1 = new TCKActionListener("A1");
    ActionListener listenerA2 = new TCKActionListener("A2");

    source.addActionListener(listenerA1);
    source.addActionListener(listenerA2);

    listeners = source.getActionListeners();

    if (listeners == null) {
      out.println("Test FAILED[3].  getActionListeners() returned null.");
      return;
    }

    // Have to check greater than or equal to 2 as a default listener
    // could be present. Will remove everything later and then check
    // for exact
    if (listeners.length != 2) {
      out.println(JSFTestUtil.FAIL + " Expected getActionListeners() to return"
          + " an array with 2 elements after adding two Listeners.");
      out.println("Array length: " + listeners.length);
      return;
    }

    List lListeners = Arrays.asList(listeners);
    if (!lListeners.contains(listenerA1)) {
      out.println(JSFTestUtil.FAIL + " Unable to find listenerA1 in the array"
          + " returned by getActionListeners().");
      return;
    }

    if (!lListeners.contains(listenerA2)) {
      out.println("Test FAILED[1].  Unable to find listenerA2 in the array"
          + " returned by getActionListeners().");
      return;
    }

    source.removeActionListener(listenerA1);

    lListeners = Arrays.asList(source.getActionListeners());
    if (lListeners.contains(listenerA1)) {
      out.println(
          JSFTestUtil.FAIL + " removeActionListener() was called passing"
              + " listenerA1, but getActionListeners() still returned"
              + " listenerA1.");
      return;
    }

    if (!lListeners.contains(listenerA2)) {
      out.println("Test FAILED[2].  Unable to find listenerA2 in the array"
          + " returned by getActionListeners().");
      return;
    }

    source.removeActionListener(listenerA2);

    if (source.getActionListeners().length != 0) {
      out.println(JSFTestUtil.FAIL + " getActionListeners() returned a "
          + "non-emtpy array after removing all test listeners.");
      out.println("Array length: " + source.getActionListeners().length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ActionSource.addActionListener() throws NullPointerException if
  // ActionListener is null.
  public void actionSourceAddActListNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ActionSource2 source = (ActionSource2) createComponent();

    JSFTestUtil.checkForNPE(source.getClass(), "addActionListener",
        new Class<?>[] { ActionListener.class }, new Object[] { null }, out);
  }

  // ActionSource.removeActionListener() throws NullPointerException if
  // ActionListener is null.
  public void actionSourceRemoveActListNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ActionSource2 source = (ActionSource2) createComponent();

    JSFTestUtil.checkForNPE(source.getClass(), "removeActionListener",
        new Class<?>[] { ActionListener.class }, new Object[] { null }, out);
  }

  // ActionSource.{set,get}Action()
  public void actionSourceGetSetActionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ActionSource2 source = (ActionSource2) createComponent();
    request.setAttribute("actionSource", "value");
    FacesContext context = getFacesContext();

    ELContext elcontext = context.getELContext();
    MethodExpression binding = getApplication().getExpressionFactory().createMethodExpression(elcontext,
    "#{requestScope.actionSource}", null, new Class[] { });

    source.setActionExpression(binding);
    Object result = source.getActionExpression();
    if (binding != result) {
      out.println(JSFTestUtil.FAIL + " getAction() failed to return the value"
          + " set via setAction().");
      out.println("Expected: " + binding);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ActionSource.{set,get}ActionListener()
  public void actionSourceGetSetActListTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    request.setAttribute("actionListener", new ActionListener() {
      public void processAction(ActionEvent event)
          throws AbortProcessingException {
        // no-op
      }
    });

    FacesContext context = getFacesContext();
    ELContext elcontext = context.getELContext();
    MethodExpression binding = getApplication().getExpressionFactory().createMethodExpression(elcontext,
    "#{requestScope.actionListener.processAction}", null, new Class[] { ActionEvent.class });

    ActionSource2 source = (ActionSource2) createComponent();

    MethodExpressionActionListener listener = new MethodExpressionActionListener(binding);
    source.addActionListener(listener);
    ActionListener[] result = source.getActionListeners();
    if (result[0] != listener) {
      out.println(JSFTestUtil.FAIL + " getActionListener() failed to return "
          + "the value set via setActionListener().");
      out.println("Expected: " + binding);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ActionSource.{set,is}Immediate
  public void actionSourceSetIsImmediateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ActionSource2 source = (ActionSource2) createComponent();

    if (source.isImmediate()) {
      out.println(JSFTestUtil.FAIL + " Expected isImmediate() to return"
          + " false as the default value");
      return;
    }

    source.setImmediate(true);
    if (!source.isImmediate()) {
      out.println(JSFTestUtil.FAIL + " Expected isImmediate() to return"
          + " true after having explicitly set it as such via"
          + " setImmediate()");
      return;
    }

    source.setImmediate(false);
    if (source.isImmediate()) {
      out.println(JSFTestUtil.FAIL + " Expected isImmediate() to return"
          + " false after having explicitly set it as such via"
          + " setImmediate()");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------------- Private
  // Classes

  private static class TCKActionListener implements ActionListener {

    PhaseId phaseId;

    String id;

    public TCKActionListener() {
      this.phaseId = PhaseId.ANY_PHASE;
    }

    public TCKActionListener(String id) {
      this(id, PhaseId.ANY_PHASE);
    }

    public TCKActionListener(String id, PhaseId phaseId) {
      this.id = id;
      this.phaseId = phaseId;
    }

    public String getId() {
      return (this.id);
    }

    public PhaseId getPhaseId() {
      return (this.phaseId);
    }

    public void processAction(ActionEvent event) {
      trace(getId());
    }

    @Override
    public boolean equals(Object otherObj) {
      if (!(otherObj instanceof TCKActionListener)) {
        return false;
      }
      TCKActionListener other = (TCKActionListener) otherObj;
      if ((null != id && null == other.id)
          || (null == id && null != other.id)) {
        return false;
      }
      boolean idsAreEqual = true;
      if (null != id) {
        idsAreEqual = id.equals(other.id);
      }
      boolean result = idsAreEqual && other.phaseId == this.phaseId;
      return result;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 41 * hash + (this.phaseId != null ? this.phaseId.hashCode() : 0);
      hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
      return hash;
    }

    // ---------------------------------------------------- Static Trace Methods

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

}
