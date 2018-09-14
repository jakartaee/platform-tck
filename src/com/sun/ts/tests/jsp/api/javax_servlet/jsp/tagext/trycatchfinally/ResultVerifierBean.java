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

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.PageContext;
import java.util.List;
import java.util.Arrays;

public class ResultVerifierBean {

  /**
   * JSP PageContext.
   */
  private PageContext _context = null;

  /**
   * Default constructor.
   */
  public ResultVerifierBean() {
  }

  public void setResult(String result) {
    // No-Op...just trying to be a good bean
  }

  public String getResult() {
    return checkResults();
  }

  /**
   * Returns the PageContext of this bean.
   * 
   * @return the PageContext
   */
  public PageContext getContext() {
    return _context;
  }

  /**
   * Sets the PageContext of this bean.
   * 
   * @param context
   *          - the PageContext
   */
  public void setContext(PageContext context) {
    _context = context;
  }

  /**
   * Validates the results of the Lists stored in the PageContext.
   */
  private String checkResults() {
    final int SIZE = 5;
    final String[] EXPECTED = { "doStartTag", "doEndTag", "body", "doInitBody",
        "doAfterBody" };
    String message = "Test PASSED";
    List catchList = (List) _context.getAttribute("cresults");
    List finallyList = (List) _context.getAttribute("fresults");

    if (catchList == null || finallyList == null) {
      message = "Test FAILED.  Either one or both lists were null.";
    } else {
      if (catchList.size() != SIZE || finallyList.size() != SIZE) {
        message = "Test FAILED.  Either one or both lists had an incorrect size."
            + "this inicates that the method call sequence for TryCatchFinally"
            + " was performed too many times, or not enough.\n"
            + "doCatch invoked for: "
            + JspTestUtil.getAsString(
                (String[]) catchList.toArray(new String[catchList.size()]))
            + "\n" + "doFinally invoked for: " + JspTestUtil.getAsString(
                (String[]) finallyList.toArray(new String[finallyList.size()]));
      } else {
        if (catchList.contains("attribute")) {
          if (finallyList.contains("attribute")) {
            message = "Test FAILED.  doCatch/doFinally invoked when"
                + " attribute setter method threw an Exception";
          } else {
            message = "Test FAILED.  doCatch invoked when attribute "
                + "setter method threw an Exception.";
          }
        } else if (finallyList.contains("attribute")) {
          message = "Test FAILED.  doFinally invoked when attribute"
              + " setter method threw an Exception.";
        } else {
          String[] ccatch = (String[]) catchList.toArray(new String[SIZE]);
          String[] ffinally = (String[]) finallyList.toArray(new String[SIZE]);
          Arrays.sort(ccatch);
          Arrays.sort(ffinally);
          for (int i = 0; i < SIZE; i++) {
            if (Arrays.binarySearch(ccatch, EXPECTED[i]) < 0) {
              message = "Test FAILED.  doCatch not invoked when"
                  + " Exception thrown from " + EXPECTED[i];
              break;
            }
          }

          for (int i = 0; i < SIZE; i++) {
            if (Arrays.binarySearch(ffinally, EXPECTED[i]) < 0) {
              message = "Test FAILED.  doFinally not invoked when"
                  + " Exception thrown from " + EXPECTED[i];
              break;
            }
          }
        }
      }
    }
    return message;
  }
}
