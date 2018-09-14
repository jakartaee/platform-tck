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

/*
 * @(#)SimpleTag.java 1.1 10/17/02
 */

package com.sun.ts.tests.jsp.common.tags.tck;

import java.io.IOException;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Simple tag that accepts a single string attribute. This tag generates no
 * output. All work is done by TEI classes.
 */
public class SimpleTag extends TagSupport implements DynamicAttributes {

  private String _test = null;

  private String _methods = null;

  private String _echo = null;

  private Object _dynAttr = null;

  private Object _dynAttr2 = null;

  private Object _dynAttr3 = null;

  private Object _dynAttr4 = null;

  private JspFragment _fragAttr = null;

  public SimpleTag() {
    super();
  }

  public void release() {
    _test = null;
    _methods = null;
    super.release();
  }

  public int doEndTag() throws JspException {
    if (_echo != null) {
      try {
        pageContext.getOut().println(_echo);
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      }
    }
    return super.doEndTag();
  }

  public void setTest(String test) {
    _test = test;
  }

  public String getTest() {
    return _test;
  }

  public void setDynAttribute(Object attr) {
    _dynAttr = attr;
  }

  public void setDynAttribute2(Object attr) {
    _dynAttr2 = attr;
  }

  public void setDynAttribute3(Object attr) {
    _dynAttr3 = attr;
  }

  public void setDynAttribute4(Object attr) {
    _dynAttr4 = attr;
  }

  public void setEcho(String echo) {
    _echo = echo;
  }

  public Object getDynAttribute() {
    return _dynAttr;
  }

  public void setFragAttribute(JspFragment attr) {
    _fragAttr = attr;
  }

  public JspFragment getFragAttribute() {
    return _fragAttr;
  }

  public void setDynamicAttribute(String s, String s1, Object o)
      throws JspException {
    // no-op
  }
}
