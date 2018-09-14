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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class AttributeTag extends TagSupport {
  private static final String NL = System.getProperty("line.separator", "\n");

  /**
   * The attribute as evaluated by the container.
   */
  private String _attribute = null;

  /**
   * What the test expects to be the result of the evaluation by the container.
   */
  private String _expected = null;

  /**
   * Sets the attribute as evaluated by the container.
   * 
   * @param attribute
   *          - the evaluated attribute value
   */
  public void setAttribute(String attribute) {
    _attribute = attribute;
  }

  /**
   * Sets the expected result of the attribute evaluation.
   * 
   * @param expected
   *          - the result the test expects
   */
  public void setExpected(String expected) {
    _expected = expected;
  }

  /**
   * Prints 'Test PASSED' if the evaluated result is the same as the expected
   * result, otherwise 'Test FAILED' will be printed along with an explanation.
   * 
   * @return EVAL_PAGE
   * @throws JspException
   *           if an unexpected error occurs
   */
  public int doEndTag() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
      if (_expected.equals(_attribute)) {
        out.println("Test PASSED");
      } else {
        out.println("Test FAILED.  Expected the attribute value to be:" + " '"
            + _expected + "'." + NL + "Received: '" + _attribute + "'.");
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    return EVAL_PAGE;
  }
}
