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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.dynamicattributes;

import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class DynamicAttributesTag extends TagSupport
    implements DynamicAttributes {

  boolean _throwException = false;

  boolean _setDynCalled = false;

  /**
   * Default constructor.
   */
  public DynamicAttributesTag() {
    super();
  }

  /**
   * Sets the _throwException instance variable. If true, when
   * setDynamicAttribute() is called by the container, a JspException will be
   * thrown.
   * 
   * @param throwException
   */
  public void setThrowException(boolean throwException) {
    _throwException = throwException;
  }

  /**
   * Validates that setDynamicAttributes is properly called and will cause a
   * test failure if setDynamicAttribute throws a JspException and doStartTag()
   * is called.
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    try {
      if (!_setDynCalled) {
        pageContext.getOut().println("Test FAILED.  setDynamicAttributes()"
            + " was not called prior to attempting to invoke doStartTag().");
      }
      if (_throwException) {
        pageContext.getOut().println("Test FAILED.  setDynamicAttributes()"
            + " threw an Exception but the container incorrectly called doStartTag()");
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    return SKIP_BODY;
  }

  /**
   * Called by the container if a tag has an attribute present and not declared
   * by the TLD. If _throwException is true, a JspException will be thrown by
   * this method.
   * 
   * @param uri
   *          - the namespace of the attribute (if any)
   * @param localName
   *          - the attribute name
   * @param value
   *          - the attribute value
   * @throws JspException
   *           if _throwException is true
   */
  public void setDynamicAttribute(String uri, String localName, Object value)
      throws JspException {
    _setDynCalled = true;
    if (_throwException) {
      throw new JspException("JspException");
    } else {
      try {
        if ("dynamic".equals(localName)) {
          if ("dynValue".equals(value)) {
            pageContext.getOut().println("Test PASSED");
          } else {
            pageContext.getOut()
                .println("Test FAILED.  Expected"
                    + " a dynamic attribute value of 'dynValue'.  "
                    + "Received: " + ((String) value));
          }
        } else {
          pageContext.getOut()
              .println("Test FAILED.  Expected"
                  + " a dynamic attribute with a local name of 'dynamic'"
                  + ".  Received: " + localName);
        }
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      }
    }
  }
}
