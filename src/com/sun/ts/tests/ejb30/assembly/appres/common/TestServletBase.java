/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.assembly.appres.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

public class TestServletBase extends HttpTCKServlet {
  protected StringBuilder postConstructRecords;

  protected StringBuilder getPostConstructRecords() {
    return postConstructRecords;
  }

  protected void setPostConstructRecords(StringBuilder postConstructRecords) {
    this.postConstructRecords = postConstructRecords;
  }

  protected void verifyRecords(HttpServletRequest request,
      HttpServletResponse response, StringBuilder sb)
      throws IOException, ServletException {
    PrintWriter pw = response.getWriter();
    String result = sb.toString();
    Helper.assertNotEquals(null, 0, result.length());
    pw.println(result);
    pw.println(Data.PASSED);
  }
}
