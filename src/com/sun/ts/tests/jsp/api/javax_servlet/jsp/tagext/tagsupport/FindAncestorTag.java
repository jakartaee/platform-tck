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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Simple tag to verify TagSupport.findAncestorWithClass().
 */
public class FindAncestorTag extends TagSupport {

  /**
   * Default constructor.
   */
  public FindAncestorTag() {
    super();
  }

  /**
   * Validate the behavior of TagSupport.findAncestorWithClass() behaves as
   * expected when test tag is nested within multiple parent tags.
   * 
   * @return Tag.EVAL_PAGE
   * @throws JspException
   *           if an error occurs
   */
  public int doEndTag() throws JspException {
    Tag tag = findAncestorWithClass(this, ParentTag.class);
    try {
      if (tag != null) {
        if (tag instanceof ParentTag) {
          String result = ((ParentTag) tag).getResult();
          if (result.equals("nested2")) {
            pageContext.getOut().println("Test PASSED");
          } else {
            pageContext.getOut()
                .println("Test FAILED.  Expected"
                    + "the returned ParentTag instance to return 'nested2"
                    + "after calling getResult() on the instance.  Received:"
                    + "  " + result);
          }
        } else {
          pageContext.getOut()
              .println("Test FAILED.  findAncestor"
                  + "WithClass() returned a non null value, but object returned"
                  + "was not an instance of ParentTag.  Received: "
                  + tag.getClass().getName());
        }
      } else {
        pageContext.getOut()
            .println("Test FAILED.  TagSupport.find"
                + "AncestorWithClass returned null when is should have returned"
                + "an actual Tag instance.");
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    } finally {
      pageContext.removeAttribute("nested1", PageContext.APPLICATION_SCOPE);
      pageContext.removeAttribute("nested2", PageContext.APPLICATION_SCOPE);
    }
    return EVAL_PAGE;
  }
}
