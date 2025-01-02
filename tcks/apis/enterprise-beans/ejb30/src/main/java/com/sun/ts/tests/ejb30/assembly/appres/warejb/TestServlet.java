/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.assembly.appres.warejb;

import java.io.IOException;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase2;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends TestServletBase2 {

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct2() {
    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:app/env/appds",
        "java:global/env/ejb3_assembly_appres_warejb/globalds");
  }

  public void clientPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    verifyRecords(request, response, postConstructRecords);
  }

  public void ejbPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    verifyRecords(request, response,
        AppResTest.getAppResBeanLocal().getPostConstructRecords());
  }

}
