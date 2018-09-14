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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.List;

public class ContainerInteractionTag extends TagSupport {

  /**
   * Name of the application scoped where method results are stored.
   */
  private String _result = null;

  /**
   * Default constructor.
   */
  public ContainerInteractionTag() {
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
   * Return value for doStartTag().
   */
  protected String _doStartTag = null;

  /**
   * Return value for doEndTag().
   */
  protected String _doEndTag = null;

  /**
   * Return value for doAfterBody().
   */
  protected String _doAfterBody = null;

  /**
   * The number of times to iterate over the body content minus the intial
   * evaluation.
   */
  private int _bodyCount = 0;

  /**
   * Returns the current configured return value for doStartTag().
   * 
   * @return the current configured return value for doStartTag()
   */
  public String getDoStartTag() {
    return _doStartTag;
  }

  /**
   * Configures the return value for doStartTag().
   * 
   * @param doStartTag
   *          - the return value
   */
  public void setDoStartTag(String doStartTag) {
    _doStartTag = doStartTag;
  }

  /**
   * Returns the current configured return value for doEndTag().
   * 
   * @return the current configured return value for doEndTag()
   */
  public String getDoEndTag() {
    return _doEndTag;
  }

  /**
   * Configures the return value for doEndTag().
   * 
   * @param doEndTag
   *          - the return value
   */
  public void setDoEndTag(String doEndTag) {
    _doEndTag = doEndTag;
  }

  /**
   * Returns the number of times the body content will be evaluated minus the
   * initial evaluation.
   * 
   * @return the number of times the body content will be evaluated minus the
   *         initial evaluation.
   */
  public Integer getBodyCount() {
    return Integer.valueOf(++_bodyCount);
  }

  /**
   * Sets the number of times the body will be evaluated after the initial
   * evaluation.
   * 
   * @param bodyCount
   *          - the body evaluation count
   */
  public void setBodyCount(Integer bodyCount) {
    _bodyCount = bodyCount.intValue() - 1;
  }

  /**
   * Returns the configured value for doAfterBody().
   * 
   * @return the current configured value for doAfterBody()
   */
  public String getDoAfterBody() {
    return _doAfterBody;
  }

  /**
   * Configures the return value for doAfterBody();
   * 
   * @param doAfterBody
   *          - the return value for doAfterBody()
   */
  public void setDoAfterBody(String doAfterBody) {
    _doAfterBody = doAfterBody;
  }

  /**
   * Validate container behavior when returning either EVAL_BODY_INCLUDE,
   * SKIP_BODY, or SKIP_PAGE.
   * 
   * @return an int value based on the doStartTag attribute.
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    addMethodToList("doStartTag");
    int retValue = SKIP_BODY;
    if ("EVAL_BODY_INCLUDE".equals(_doStartTag)) {
      retValue = EVAL_BODY_INCLUDE;
    }
    return retValue;
  }

  /**
   * Validate container behavior when returning either SKIP_PAGE or EVAL_PAGE.
   * 
   * @return an int value based on the doEndTag attribute.
   * @throws JspException
   *           if an error occurs
   */
  public int doEndTag() throws JspException {
    addMethodToList("doEndTag");
    int retValue = EVAL_PAGE;
    if ("SKIP_PAGE".equals(_doEndTag)) {
      retValue = SKIP_PAGE;
    }
    return retValue;
  }

  /**
   * Validate container behavior when returning either EVAL_BODY_AGAIN or
   * SKIP_BODY.
   * 
   * @return an int value based on the doAfterBody attribute.
   * @throws JspException
   *           - if an error occurs.
   */
  public int doAfterBody() throws JspException {
    addMethodToList("doAfterBody");
    int retValue = SKIP_BODY;
    if (_bodyCount != 0) {
      _bodyCount--;
      retValue = EVAL_BODY_AGAIN;
    }
    return retValue;
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
    if (_result != null) {
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

  /**
   * Utility method to return the int value based on an Integer based
   * PageContext attribute.
   * 
   * @param varName
   *          - the PageContext attribute to retrieve the int value from
   * @return an int representation of the Integer attribute
   */
  protected int getIntValue(String varName) {
    Integer i = (Integer) pageContext.findAttribute(varName);
    return (i != null) ? i.intValue() : 0;
  }
}
