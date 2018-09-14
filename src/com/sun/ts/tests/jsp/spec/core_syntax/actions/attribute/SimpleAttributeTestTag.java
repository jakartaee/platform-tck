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

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class SimpleAttributeTestTag extends SimpleTagSupport
    implements DynamicAttributes {

  Object _fragment = null;

  Object _dynamic = null;

  public void setFragment(Object o) {
    _fragment = o;
  }

  public void doTag() throws JspException, IOException {
    if (_fragment != null) {
      JspWriter out = getJspContext().getOut();
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
      JspWriter out = getJspContext().getOut();
      try {
        if (_dynamic instanceof String) {
          out.println("Test PASSED");
        } else {
          out.println("Object passed to the fragment attribute was not"
              + "an instance of String.  Actual type: "
              + _fragment.getClass().getName());
        }
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      }
    }
  }

  public void setDynamicAttribute(String s, String s1, Object o)
      throws JspException {
    _dynamic = o;
  }
}
