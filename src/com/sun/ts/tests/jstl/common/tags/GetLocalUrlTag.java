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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.net.URL;
import java.net.MalformedURLException;

public class GetLocalUrlTag extends javax.servlet.jsp.tagext.TagSupport {

  /**
   * Variable name for the result of the action.
   */
  private String _var = null;

  /**
   * Path for requested resource.
   */
  private String _path = null;

  /** Creates new GetLocalUrl */
  public GetLocalUrlTag() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets the name of the exported variable.
   *
   * @param var
   *          variable name to associate the URL result with.
   */
  public void setVar(String var) {
    _var = var;
  }

  /**
   * Sets the path of the requested resource.
   *
   * @param path
   *          Path of the requested resource.
   */
  public void setPath(String path) {
    _path = path;
  }

  /**
   * Creates a new URL based on the server where the tag is running and the path
   * as specified by the user.
   *
   * @return EVAL_PAGE
   */
  public int doEndTag() throws javax.servlet.jsp.JspException {
    ServletRequest req = pageContext.getRequest();
    if (req != null) {
      StringBuffer sb = new StringBuffer(50);
      sb.append("http://").append(req.getServerName()).append(":");
      sb.append(req.getServerPort()).append(_path);

      String url = sb.toString();

      // validate the URL built is correct.
      try {
        new URL(url);
      } catch (MalformedURLException mfe) {
        throw new JspException(mfe);
      }
      pageContext.setAttribute(_var, url);
    }
    return EVAL_PAGE;
  }

  /**
   * Resets tag state.
   */
  public void release() {
    _var = null;
    _path = null;
  }
}
