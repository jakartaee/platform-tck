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

import com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport.ContainerInteractionTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyContent;

public class BodyContainerInteractionTag extends ContainerInteractionTag
    implements BodyTag {

  protected BodyContent _content = null;

  /**
   * Default constructor.
   */
  public BodyContainerInteractionTag() {
    super();
  }

  /**
   * Validate container behavior when returning either EVAL_BODY_INCLUDE,
   * SKIP_BODY, SKIP_PAGE, or EVAL_BODY_BUFFERED.
   * 
   * @return an int value based on the doStartTag attribute.
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    int retValue = 0;
    if ("EVAL_BODY_BUFFERED".equals(_doStartTag)) {
      addMethodToList("doStartTag");
      retValue = EVAL_BODY_BUFFERED;
    } else {
      retValue = super.doStartTag();
    }
    return retValue;
  }

  /**
   * Validate the container calls setBodyContent on tag instances when
   * doStartTag() returns EVAL_BODY_BUFFERED.
   * 
   * @param content
   *          - the body content.
   */
  public void setBodyContent(BodyContent content) {
    _content = content;
    addMethodToList("setBodyContent");
  }

  /**
   * Validate the container calls doInitBody() before the evaluation of the
   * body.
   * 
   * @throws JspException
   *           if an error occurs
   */
  public void doInitBody() throws JspException {
    addMethodToList("doInitBody");
  }

}
