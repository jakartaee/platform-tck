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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.jspidconsumer;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspIdConsumer;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class SameJspIdTag extends SimpleTagSupport implements JspIdConsumer {

  public static final int NUM_INVOC = 5;

  private static int currentInvoc = 0;

  private String jspId;

  private static String[] idArray = new String[NUM_INVOC];

  private JspWriter out;

  public void setJspId(String id) {
    jspId = id;
  }

  public void doTag() throws JspException, IOException {

    out = getJspContext().getOut();
    currentInvoc = (currentInvoc == NUM_INVOC) ? 1 : currentInvoc + 1;
    try {
      out.println("SameJspIdTag: current Invocation = " + currentInvoc);
      out.println("SameJspIdTag: JspId is " + jspId);
      idArray[currentInvoc - 1] = jspId;

      if (currentInvoc != NUM_INVOC)
        return;
      else {
        for (int i = 0; i < NUM_INVOC; ++i) {
          if (!idArray[i].equals(jspId)) {
            out.println("SameJspIdTag: different jsp ids found");
            out.println("Test FAILED");
            return;
          }
        }
        out.println("Test PASSED");
      }

    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "SameJspIdTag");
    }
  }
}
