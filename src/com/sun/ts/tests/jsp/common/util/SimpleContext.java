/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.common.util;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Simple class to validate release is called on the PageContext object provide
 * to JspFactory.release.
 */

public class SimpleContext extends PageContext {

  /**
   * Flag to determine if release() has been called.
   */
  private boolean _releaseCalled = false;

  public SimpleContext() {
  }

  /**
   * No-op.
   * 
   * @param servlet
   * @param servletRequest
   * @param servletResponse
   * @param s
   * @param b
   * @param i
   * @param b1
   * @throws IOException
   * @throws IllegalStateException
   * @throws IllegalArgumentException
   */
  public void initialize(Servlet servlet, ServletRequest servletRequest,
      ServletResponse servletResponse, String s, boolean b, int i, boolean b1)
      throws IOException, IllegalStateException, IllegalArgumentException {
    return;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public ServletResponse getResponse() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public ServletRequest getRequest() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public javax.el.ELContext getELContext() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public Object getPage() {
    return null;
  }

  /**
   * Returns null
   * 
   * @return null
   */
  public HttpSession getSession() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public VariableResolver getVariableResolver() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public ExpressionEvaluator getExpressionEvaluator() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public JspWriter getOut() {
    return null;
  }

  /**
   * Returns null
   * 
   * @param i
   * @return null
   */
  public Enumeration getAttributeNamesInScope(int i) {
    return null;
  }

  /**
   * Returns 0.
   * 
   * @param s
   * @return 0
   */
  public int getAttributesScope(String s) {
    return 0;
  }

  /**
   * No-op.
   * 
   * @param s
   * @param i
   */
  public void removeAttribute(String s, int i) {
  }

  /**
   * No-op.
   * 
   * @param s
   */
  public void removeAttribute(String s) {
  }

  /**
   * Returns null.
   * 
   * @param s
   * @return null
   */
  public Object findAttribute(String s) {
    return null;
  }

  /**
   * Returns null.
   * 
   * @param s
   * @param i
   * @return null
   */
  public Object getAttribute(String s, int i) {
    return null;
  }

  /**
   * Returns null.
   * 
   * @param s
   * @return null
   */
  public Object getAttribute(String s) {
    return null;
  }

  /**
   * No-op.
   * 
   * @param s
   * @param o
   * @param i
   */
  public void setAttribute(String s, Object o, int i) {
  }

  /**
   * No-op.
   * 
   * @param s
   * @param o
   */
  public void setAttribute(String s, Object o) {
  }

  /**
   * No-op.
   * 
   * @param throwable
   * @throws ServletException
   * @throws IOException
   */
  public void handlePageException(Throwable throwable)
      throws ServletException, IOException {
  }

  /**
   * No-op
   * 
   * @param e
   * @throws ServletException
   * @throws IOException
   */
  public void handlePageException(Exception e)
      throws ServletException, IOException {
  }

  /**
   * No-op.
   * 
   * @param s
   * @param b
   * @throws ServletException
   * @throws IOException
   */
  public void include(String s, boolean b)
      throws ServletException, IOException {
  }

  /**
   * No-op
   * 
   * @param s
   * @throws ServletException
   * @throws IOException
   */
  public void include(String s) throws ServletException, IOException {
  }

  /**
   * No-op
   * 
   * @param s
   * @throws ServletException
   * @throws IOException
   */
  public void forward(String s) throws ServletException, IOException {
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public ServletContext getServletContext() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public ServletConfig getServletConfig() {
    return null;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  public Exception getException() {
    return null;
  }

  /**
   * Performs cleanup of PageContext when called by JspFactory.
   */
  public void release() {
    _releaseCalled = true;
  }

  public boolean isReleased() {
    return _releaseCalled;
  }
}
