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
package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {

  // HttpServletResponse response;
  StringWriter writer;

  public BufferedResponseWrapper(HttpServletResponse response) {

    super(response);
    // this.response = response;

  }

  public PrintWriter getWriter() throws IOException {

    writer = new StringWriter();
    return new PrintWriter(writer);

  }

  public StringWriter getBufferedWriter() {

    if (writer == null) {
      return new StringWriter();
    }
    return writer;

  }

}
