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

package com.sun.ts.tests.jsf.api.javax_faces.factoryfinderrelease;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.FactoryFinder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {
  public void getFactoriesISETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    // release the factory instances allocated in the getFactoryTest
    PrintWriter pw = response.getWriter();
    try {
      FactoryFinder.releaseFactories();
      FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
      pw.println("Test FAILED" + JSFTestUtil.NL + "Expected an "
          + "IllegalStateException to be thrown, no Exception thrown "
          + "at all!.");
    } catch (IllegalStateException ise) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("Test FAILED" + JSFTestUtil.NL + "Unexpected Exception thrown!"
          + JSFTestUtil.NL + "Expected: IllegalStateException" + JSFTestUtil.NL
          + "Received: " + JSFTestUtil.NL + e.toString());
    }
  }

}
