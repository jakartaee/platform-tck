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
 * @(#)SimpleAncestor.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class SimpleAncestor extends SimpleTagSupport {

  /**
   * Default constructor.
   */
  public SimpleAncestor() {
    super();
  }

  /**
   * Validates the behavior of getAncestorWithClass().
   * 
   * @throws JspException
   *           - not thrown by this test
   * @throws IOException
   *           - not thrown by this test
   */
  public void doTag() throws JspException, IOException {
    JspTestUtil.debug("[SimpleAncestor] in doTag()");
    JspTag tag = SimpleTagSupport.findAncestorWithClass(this, SimpleTag.class);
    if (tag != null) {
      String lvl = ((SimpleParentTag) tag).getLevel();
      if ("nested3".equals(lvl)) {
        JspTag tag2 = SimpleTagSupport.findAncestorWithClass(this,
            ClassicParent.class);
        if (tag2 != null) {
          String lvl2 = ((ClassicParent) tag2).getLevel();
          if ("nested2".equals(lvl2)) {
            this.getJspContext().getOut().println("Test PASSED");
          } else {
            this.getJspContext().getOut()
                .println("Test FAILED.  The appropriate"
                    + "ancestor type was returned, but the actual instance returned was"
                    + "incorrect.  The attribute 'level' for the expected instance was "
                    + "'nested2', received: " + lvl2);
          }
        } else {
          this.getJspContext().getOut().println("Test FAILED.  Unabled to find"
              + " ancestor with class 'com.sun.ts.tests.jsp.api.javax_servlet"
              + ".jsp.tagext.simpletagsupport.ClassicParent'.");
        }
      } else {
        this.getJspContext().getOut().println("Test FAILED.  The appropriate"
            + "ancestor type was returned, but the actual instance returned was"
            + "incorrect.  The attribute 'level' for the expected instance was "
            + "'nested3', received: " + lvl);
      }
    } else {
      this.getJspContext().getOut().println("Test FAILED.  Unable to find"
          + " ancestor with class 'javax.servlet.jsp.tagext.SimpleTag',");
    }
  }
}
