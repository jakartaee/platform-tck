/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagsupport;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.TagSupport;

public class ParentTag extends TagSupport {

  /**
   * Name of the application scoped object where the result of the method calls
   * is stored.
   */
  String _result = null;

  /**
   * Default constructor.
   */
  public ParentTag() {
    super();
  }

  /**
   * Gets the name of the application scoped list.
   * 
   * @return the name of the application scoped list
   */
  public String getResult() {
    return _result;
  }

  /**
   * Sets the name of the application scoped list.
   * 
   * @param result
   *          - the name of the list
   */
  public void setResult(String result) {
    _result = result;
  }

  /**
   * Simple tag that includes the evaluation of it's body.
   * 
   * @return Tag.EVAL_BODY_INCLUDE
   * @throws jakarta.servlet.jsp.JspException
   *           if an error occurs.
   */
  public int doStartTag() throws JspException {
    addMethodToList("doStartTag");
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Adds this method name to the method list and returns Tag.EVAL_PAGE.
   * 
   * @return Tag.EVAL_PAGE
   * @throws jakarta.servlet.jsp.JspException
   *           if an error occurs
   */
  public int doEndTag() throws JspException {
    addMethodToList("doEndTag");
    return EVAL_PAGE;
  }

  /**
   * Adds this method name to the method list and returns Tag.SKIP_BODY.
   * 
   * @return Tag.SKIP_BODY
   * @throws jakarta.servlet.jsp.JspException
   *           if an error occurs
   */
  public int doAfterBody() throws JspException {
    addMethodToList("doAfterBody");
    return SKIP_BODY;
  }

  /**
   * This will add an application scoped List to the page context based of the
   * name (_result). The list contains the methods called against this tag
   * handler by the container.
   * 
   * @param methodName
   *          - the method name to add to the list.
   */
  protected void addMethodToList(String methodName) {
    if (_result == null) {
      return;
    }
    List list = (List) pageContext.getAttribute(_result,
        PageContext.APPLICATION_SCOPE);
    if (list == null) {
      list = new ArrayList();
      list.add(methodName);
      pageContext.setAttribute(_result, list, PageContext.APPLICATION_SCOPE);
    } else {
      list.add(methodName);
    }
  }
}
