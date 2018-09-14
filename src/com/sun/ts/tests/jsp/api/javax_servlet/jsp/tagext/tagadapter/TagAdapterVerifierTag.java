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
 * @(#)TagAdapterVerifierTag.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagadapter;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAdapter;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Simple tag to verify adpated SimpleTag instance is provided though a call to
 * Tag.getParent().
 */
public class TagAdapterVerifierTag extends TagSupport {

  /**
   * Default Constructor
   */
  public TagAdapterVerifierTag() {
    super();
    JspTestUtil.debug("[TagAdapterVerifierTag] new instance");
  }

  /**
   * Validates that the parent of this Classic tag handler is an instance of
   * SimpleTag adapted with a TagAdapter.
   * 
   * @return Tag.EVAL_PAGE
   * @throws JspException
   *           if an unexpected error occurs.
   */
  public int doEndTag() throws JspException {
    JspTestUtil.debug("[TagAdapterVerifierTag] in doEndTag()");
    Tag tag = this.getParent();
    try {
      if (tag != null) {
        if (tag instanceof TagAdapter) {
          SimpleTag simpleTag = (SimpleTag) ((TagAdapter) tag).getAdaptee();
          if (simpleTag != null) {
            pageContext.getOut().println("Test PASSED.");
          } else {
            pageContext.getOut().println("Test FAILED.  Received a null"
                + " value from TagAdapter.getAdaptee()");
          }
        } else {
          pageContext.getOut().println("Test FAILED.  Tag.getParent() didn't"
              + " returned the TagAdapter instance that was expected.");
        }
      } else {
        pageContext.getOut().println(
            "Test FAILED.  Received a null value " + "from Tag.getParent()");
      }
    } catch (IOException ioe) {
      throw new JspException("[TagAdapterVeriferTag] Unexpected IOException.",
          ioe);
    }
    JspTestUtil.debug("[TagAdapterVerifierTag] leaving doEndTag()");
    return EVAL_PAGE;
  }
}
