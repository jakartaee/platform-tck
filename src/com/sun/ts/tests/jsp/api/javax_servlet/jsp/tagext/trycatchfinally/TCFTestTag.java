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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.trycatchfinally;

import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;
import java.util.List;
import java.util.ArrayList;

public class TCFTestTag extends BodyTagSupport implements TryCatchFinally {

  private static final String DO_START_TAG = "doStartTag";

  private static final String DO_END_TAG = "doEndTag";

  private static final String DO_AFTER_BODY = "doAfterBody";

  private static final String DO_INIT_BODY = "doInitBody";

  private static final String ATTRIBUTE = "attribute";

  private static final String BODY = "body";

  private String _location = BODY;

  /**
   * Default constructor.
   */
  public TCFTestTag() {
    super();
  }

  /**
   * Returns the location the exception will be thrown from.
   * 
   * @return the location of the exception
   */
  public String getLocation() {
    return _location;
  }

  /**
   * Sets the location where an exception will occur.
   * 
   * @param location
   *          - the location
   */
  public void setLocation(String location) throws JspException {
    this._location = location;
    if (ATTRIBUTE.equals(location)) {
      _location = BODY;
      throw new JspException(ATTRIBUTE);
    }
  }

  /**
   * Default processing of the start tag returning EVAL_BODY_BUFFERED
   *
   * @return EVAL_BODY_BUFFERED
   * @throws JspException
   *           if an error occurred while processing this tag
   * @see BodyTag#doStartTag
   */
  public int doStartTag() throws JspException {
    if (DO_START_TAG.equals(_location)) {
      throw new JspException(DO_START_TAG);
    }
    return super.doStartTag();
  }

  /**
   * Default processing of the end tag returning EVAL_PAGE.
   *
   * @return EVAL_PAGE
   * @throws JspException
   *           if an error occurred while processing this tag
   * @see Tag#doEndTag
   */
  public int doEndTag() throws JspException {
    if (DO_END_TAG.equals(_location)) {
      throw new JspException(DO_END_TAG);
    }
    return super.doEndTag();
  }

  /**
   * Prepare for evaluation of the body just before the first body evaluation:
   * no action.
   *
   * @throws JspException
   *           if an error occurred while processing this tag
   * @see #setBodyContent
   * @see #doAfterBody
   * @see BodyTag#doInitBody
   */
  public void doInitBody() throws JspException {
    if (DO_INIT_BODY.equals(_location)) {
      throw new JspException(DO_INIT_BODY);
    }
    super.doInitBody();
  }

  /**
   * After the body evaluation: do not reevaluate and continue with the page. By
   * default nothing is done with the bodyContent data (if any).
   *
   * @return SKIP_BODY
   * @throws JspException
   *           if an error occurred while processing this tag
   * @see #doInitBody
   * @see BodyTag#doAfterBody
   */
  public int doAfterBody() throws JspException {
    if (DO_AFTER_BODY.equals(_location)) {
      throw new JspException(DO_AFTER_BODY);
    }
    return super.doAfterBody();
  }

  /**
   * Invoked if a Throwable occurs while evaluating the BODY inside a tag or in
   * any of the following methods: Tag.doStartTag(), Tag.doEndTag(),
   * IterationTag.doAfterBody() and BodyTag.doInitBody().
   *
   * <p>
   * This method is not invoked if the Throwable occurs during one of the setter
   * methods.
   *
   * <p>
   * This method may throw an exception (the same or a new one) that will be
   * propagated further the nest chain. If an exception is thrown, doFinally()
   * will be invoked.
   *
   * <p>
   * This method is intended to be used to respond to an exceptional condition.
   *
   * @param t
   *          The throwable exception navigating through this tag.
   */
  public void doCatch(Throwable t) throws Throwable {
    List list = (List) pageContext.getAttribute("cresults");
    if (list == null) {
      list = new ArrayList();
      pageContext.setAttribute("cresults", list);
    }
    list.add(t.getMessage());

  }

  /**
   * Invoked in all cases after doEndTag() for any class implementing Tag,
   * IterationTag or BodyTag. This method is invoked even if an exception has
   * occurred in the BODY of the tag, or in any of the following methods:
   * Tag.doStartTag(), Tag.doEndTag(), IterationTag.doAfterBody() and
   * BodyTag.doInitBody().
   *
   * <p>
   * This method is not invoked if the Throwable occurs during one of the setter
   * methods.
   *
   * <p>
   * This method should not throw an Exception.
   *
   * <p>
   * This method is intended to maintain per-invocation data integrity and
   * resource management actions.
   */
  public void doFinally() {
    List list = (List) pageContext.getAttribute("fresults");
    if (list == null) {
      list = new ArrayList();
      pageContext.setAttribute("fresults", list);
    }
    list.add(_location);
    _location = BODY;
  }

}
