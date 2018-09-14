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

package com.sun.ts.tests.jsp.spec.jspdocument.general;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class EchoTag extends SimpleTagSupport {

  private String _echo = null;

  private String _static = null;

  public void setEcho(String echo) {
    _echo = echo;
  }

  public void setStatic(String staticString) {
    _static = staticString;
  }

  public void doTag() throws JspException {
    JspWriter out = null;
    JspFragment body = null;
    try {
      out = getJspContext().getOut();
      body = getJspBody();
      if (body != null) {
        body.invoke(null);
      }
      if (_echo != null) {
        out.println("Expression from attribute: " + _echo);
      }
      if (_static != null) {
        out.println("String from attribute: " + _static);
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
  }
}
