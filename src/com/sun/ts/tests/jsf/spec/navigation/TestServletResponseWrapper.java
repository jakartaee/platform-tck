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
 *$Id$
 */

package com.sun.ts.tests.jsf.spec.navigation;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TestServletResponseWrapper extends HttpServletResponseWrapper {

  private boolean redirectSent = false;

  public TestServletResponseWrapper(HttpServletResponse response) {
    super(response);
  }

  // ---------------------------------- Methods from HttpServletRequestWrapper

  public void sendRedirect(String s) throws IOException {

    redirectSent = true;

  } // END sendRedirect

  // ---------------------------------------------------------- Public Methods

  public boolean getRedirectSent() {

    return redirectSent;

  } // END getRedirectSent

}
