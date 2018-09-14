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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodytagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class BodySynchronizationTag extends BodyContainerInteractionTag {

  /**
   * Default constructor.
   */
  public BodySynchronizationTag() {
    super();
  }

  /**
   * Adds two attributes to the PageContext to be synchronized after
   * doStartTag() has been called.
   * 
   * @return an int value based on what the tag has been configured to return
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    JspTestUtil.debug("[BodySynchronizationTag] in doStartTag()");
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("nested",
        Integer.valueOf(getIntValue("nested") + 1));
    return super.doStartTag();
  }

  /**
   * Adds two attributes to the PageContext to be synchronized after
   * doInitBody() has been called.
   * 
   * @throws JspException
   *           if an error occurs
   */
  public void doInitBody() throws JspException {
    JspTestUtil.debug("[BodySynchronizationTag] in doInitBody()");
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("nested",
        Integer.valueOf(getIntValue("nested") + 1));
    super.doInitBody();
  }

  /**
   * Adds two attributes to the PageContext to be synchronized after doEndTag()
   * has been called.
   * 
   * @return an int value based on what the tag has been configured to return
   * @throws JspException
   *           if an error occurs
   */
  public int doEndTag() throws JspException {
    JspTestUtil.debug("[BodySynchronizationTag] in doEndTag()");
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("end", Integer.valueOf(getIntValue("nested") + 1));
    pageContext.removeAttribute("nested");
    return super.doEndTag();
  }

  /**
   * Adds two attributes to the PageContext to be synchronized after
   * doAfterBody() has been called.
   * 
   * @return an int value based on what the tag has been configured to return
   * @throws JspException
   *           if an error occurs
   */
  public int doAfterBody() throws JspException {
    JspTestUtil.debug("[BodySynchronizationTag] in doAfterBody()");
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("nested",
        Integer.valueOf(getIntValue("nested") + 1));
    if (_content != null) {
      try {
        _content.writeOut(_content.getEnclosingWriter());
        JspTestUtil.debug(
            "[BodySynchronizationTag] bodyContent: " + _content.getString());
      } catch (IOException ioe) {
        throw new JspException("Unexpcted IOException!", ioe);
      }
    }
    return super.doAfterBody();
  }

  /**
   * Called by container to set the body content of the action.
   * 
   * @param content
   *          - the body content
   */
  public void setBodyContent(BodyContent content) {
    super.setBodyContent(content);
  }
}
