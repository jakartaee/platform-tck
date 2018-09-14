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

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Tag to verify that the JSP implementation object properly initialized any
 * attributes present with the in tag as well as setting the pageContext and
 * parent
 */
public class InitializationTag extends TagSupport {

  /**
   * The first attribute.
   */
  private String _attribute1 = null;

  /**
   * The second attribute.
   */
  private String _attribute2 = null;

  /**
   * The third attribute.
   */
  private String _attribute3 = null;

  /**
   * The parent of this tag instance.
   */
  private Tag _parent = null;

  /**
   * The pageContext
   */
  private PageContext _context = null;

  /**
   * Default Constructor.
   */
  public InitializationTag() {
  }

  /**
   * Returns the value of _attribute1.
   * 
   * @return _attribute1
   */
  public String getAttribute1() {
    return _attribute1;
  }

  /**
   * Sets the value for _attribute1.
   * 
   * @param attribute1
   *          - the value for _attribute1
   */
  public void setAttribute1(String attribute1) {
    _attribute1 = attribute1;
  }

  /**
   * Returns the value of _attribute2.
   * 
   * @return _attribute2
   */
  public String getAttribute2() {
    return _attribute2;
  }

  /**
   * Sets the value for _attribute2.
   * 
   * @param attribute2
   *          - the value for _attribute2
   */
  public void setAttribute2(String attribute2) {
    _attribute2 = attribute2;
  }

  /**
   * Returns the value for _attribute3.
   * 
   * @return the value for _attribute3
   */
  public String getAttribute3() {
    return _attribute3;
  }

  /**
   * Sets the value for _attribute3.
   * 
   * @param attribute3
   *          - the value for _attribute3
   */
  public void setAttribute3(String attribute3) {
    _attribute3 = attribute3;
  }

  /**
   * Set the current page context. This method is invoked by the JSP page
   * implementation object prior to doStartTag().
   * <p>
   * This value is *not* reset by doEndTag() and must be explicitly reset by a
   * page implementation if it changes between calls to doStartTag().
   *
   * @param pc
   *          The page context for this tag handler.
   */
  public void setPageContext(PageContext pc) {
    _context = pc;
  }

  /**
   * Set the parent (closest enclosing tag handler) of this tag handler. Invoked
   * by the JSP page implementation object prior to doStartTag().
   * <p>
   * This value is *not* reset by doEndTag() and must be explicitly reset by a
   * page implementation.
   *
   * @param t
   *          The parent tag, or null.
   */
  public void setParent(Tag t) {
    _parent = t;
  }

  /**
   * Get the parent (closest enclosing tag handler) for this tag handler.
   * 
   * @return the current parent, or null if none.
   * @see javax.servlet.jsp.tagext.TagSupport#findAncestorWithClass
   */
  public Tag getParent() {
    return _parent;
  }

  /**
   * Validates that the tag initialization occurs before calling doStartTag().
   * 
   * @return Tag.SKIP_BODY
   * @throws JspException
   *           if an error occurred while processing this tag
   */
  public int doStartTag() throws JspException {
    if (_context == null) {
      throw new JspException("Test FAILED.  setPageContext() was not"
          + " called by the container before calling doStartTag().");
    }
    JspWriter out = _context.getOut();
    try {
      if ("attribute1".equals(_attribute1)) {
        if ("attribute2".equals(_attribute2)) {
          if ("attribute3".equals(_attribute3)) {
            Tag tag = this.getParent();
            if (tag != null) {
              if (tag instanceof ParentTag) {
                out.println("Test PASSED");
              } else {
                out.println("Test FAILED.  getParent() returned an "
                    + "unexpected value.  Expected: com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport.ParentTag"
                    + ".  Received: " + tag.getClass().getName());
              }
            } else {
              out.println(
                  "Test FAILED.  setParent() was not called by the container"
                      + " before it called doStartTag().");
            }
          } else {
            out.println("Test FAILED.  Attribute3 was not properly"
                + " initialized before the container called doStartTag().");
          }
        } else {
          out.println("Test FAILED.  Attribute2 was not properly"
              + " initialized before the container called doStartTag().");
        }
      } else {
        out.println("Test FAILED.  Attribute1 was not properly"
            + " initialized before the container called doStartTag().");
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    return SKIP_BODY;
  }

  public int doEndTag() throws JspException {
    return EVAL_PAGE;
  }
}
