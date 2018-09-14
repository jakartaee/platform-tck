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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.el;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class EchoTag extends BodyTagSupport {

  private String _echo = null;

  private String _static = null;

  public void setEcho(String echo) {
    _echo = echo;
  }

  public void setStatic(String staticString) {
    _static = staticString;
  }

  public int doAfterBody() throws JspException {
    BodyContent body = this.getBodyContent();
    try {
      body.writeOut(body.getEnclosingWriter());
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    return SKIP_BODY;
  }

  public int doEndTag() throws JspException {
    if (_echo != null) {
      try {
        pageContext.getOut().println("Expression from attribute: " + _echo);
        pageContext.getOut().println("String from attribute: " + _static);
      } catch (IOException ioe) {
        throw new JspException("Unexpected IOException!", ioe);
      } finally {
        _echo = null;
      }
    }
    return EVAL_PAGE;
  }
}
