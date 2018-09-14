/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)SignatureTestTag.java	1.1 04/11/05
 */
package com.sun.ts.tests.jsf.spec.webapp.tldsig;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This is only used to associate a TagExtraInfo with.
 */
public class SignatureTestTag extends TagSupport {

  // -------------------------------------- Methods from javax.servlet.jsp.Tag

  public int doEndTag() throws JspException {
    List messages = (List) pageContext.getAttribute(
        "com.sun.tck.taglibsig.failedmessages", PageContext.APPLICATION_SCOPE);

    JspWriter out = pageContext.getOut();
    if (messages.size() > 0) {
      try {
        out.println("The following Taglibraries FAILED");
        out.println("----------------------------------------------\n");
        out.println();
        for (int i = 0, size = messages.size(); i < size; i++) {
          out.println(messages.get(i));
        }
        out.println('\n');
      } catch (IOException ioe) {
        throw new JspException(ioe);
      }
    }

    messages = (List) pageContext.getAttribute(
        "com.sun.tck.taglibsig.passedmessages", PageContext.APPLICATION_SCOPE);
    if (messages.size() > 0) {
      try {
        out.println("The following Taglibraries PASSED");
        out.println("----------------------------------------------\n");
        for (int i = 0, size = messages.size(); i < size; i++) {
          out.println(messages.get(i));
        }
        out.println('\n');
      } catch (IOException ioe) {
        throw new JspException(ioe);
      }
    }

    try {
      out.println("\n\nNOTE:  In order to see any changes to the"
          + " result, the test application must be redeployed");
    } catch (IOException ioe) {
      throw new JspException(ioe);
    }

    return super.doEndTag();

  } // END doEndTag

}
