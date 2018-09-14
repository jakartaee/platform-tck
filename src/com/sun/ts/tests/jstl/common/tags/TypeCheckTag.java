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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * TypeCheckTag.java Simple tag to validate exported variable types.
 */

public class TypeCheckTag extends TagSupport {

  /**
   * Stringified FQCN of Java type to validate
   */
  private String _type = null;

  /**
   * Variable name to export test result to
   */
  private String _varName = null;

  /**
   * Creates a new instance of ExceptionCheckTag
   */
  public TypeCheckTag() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets the type of the variable to test.
   * 
   * @param exceptionClass
   *          Exception class.
   */
  public void setType(String type) {
    this._type = type;
  }

  /**
   * Sets the name of the variable to export.
   * 
   * @param Name
   *          of the variable to export.
   */
  public void setVarName(String varName) {
    this._varName = varName;
  }

  /**
   * <code>doStartTag</code> has been overridden
   *
   * @return <code>SKIP_BODY</code>
   * @exception JspException
   *              if an error occurs
   */
  public int doStartTag() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
      try {
        Class clazz = Class.forName(_type);
        Object o = pageContext.findAttribute(_varName);
        if (clazz.isInstance(o)) {
          out.print(_varName + " is of type " + _type + "<br>");
        } else {
          out.print(
              _varName + " is not of the expected type: " + _type + "<br>");
          out.print("Type found: " + o.getClass().getName() + "<br>");
        }
      } catch (Exception e) {
        out.print("Specified type: " + _type + " not found!<br>");
        out.print(e.toString());
      }
    } catch (Exception e) {
      throw new JspException(e.toString());
    }
    return SKIP_BODY;
  }

  /**
   * <code>release</code> is called by the tag handler to release state. This
   * method is invoked by the JSP page implementation object.
   */
  public void release() {
    _type = null;
    _varName = null;
  }
}
