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

package com.sun.ts.tests.jsp.spec.tagfiles.semantics;

import java.io.IOException;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.SkipPageException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import jakarta.servlet.jsp.tagext.TryCatchFinally;

public class MySimpleTag extends SimpleTagSupport implements TryCatchFinally {

  public void doTag() throws JspException {
    try {
      JspWriter out = getJspContext().getOut();
      out.println("Test PASSED. MySimpleTag.doTag");
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    throw new SkipPageException("from MySimpleTag.doTag.");
  }

  public void doCatch(Throwable t) throws Throwable {
    try {
      JspWriter out = getJspContext().getOut();
      out.println("MySimpleTag.doCatch");
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }

  }

  public void doFinally() {

  }
}
