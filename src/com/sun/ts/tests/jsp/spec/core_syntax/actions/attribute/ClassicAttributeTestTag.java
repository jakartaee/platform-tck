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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.attribute;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class ClassicAttributeTestTag extends TagSupport
    implements DynamicAttributes {

  /**
   * JspFragment instance.
   */
  Object _fragment = null;

  /**
   * Holder for value set by setDynamicAttribute.
   */
  Object _dynamic = null;

  /**
   * Sets _fragment. This should be an instance of JspFragment.
   * 
   * @param o
   *          - a JspFragment instance
   */
  public void setFragment(Object o) {
    _fragment = o;
  }

  /**
   * Sets a dynamic attribute.
   * 
   * @param s
   *          - the URL of the attribute
   * @param s1
   *          - the attribute name
   * @param o
   *          - the attribute value
   * @throws JspException
   *           if an unexpected error occurs
   */
  public void setDynamicAttribute(String s, String s1, Object o)
      throws JspException {
    _dynamic = o;
  }

  /**
   * If _fragment is not null, it validates the the attribute set was indeed a
   * JspFragment. If _dynamic is not null it validates the attribute is a String
   * instance. If in either case the tests fails, a detailed message will be
   * displayed.
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    if (_fragment != null) {
      JspWriter out = pageContext.getOut();
      try {
        if (_fragment instanceof JspFragment) {
          out.println("Test PASSED");
        } else {
          out.println("Object passed to the fragment attribute was not"
              + "an instance of JspFragment.  Actual type: "
              + _fragment.getClass().getName());
        }
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      }
    } else if (_dynamic != null) {
      JspWriter out = pageContext.getOut();
      try {
        if (_dynamic instanceof String) {
          out.println("Test PASSED");
        } else {
          out.println("Object passed to the fragment attribute was not"
              + "an instance of String.");
        }
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      }
    }
    return SKIP_BODY;
  }
}
