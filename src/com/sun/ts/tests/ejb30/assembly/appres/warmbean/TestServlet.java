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

package com.sun.ts.tests.ejb30.assembly.appres.warmbean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResManagedBean;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase2;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends TestServletBase2 {
  @Resource
  private AppResManagedBean appResManagedBean;

  @Resource(type = AppResManagedBean.class, lookup = "java:module/test-managed-bean")
  private AppResCommonIF testManagedBean;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct2() {
    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:app/env/appds",
        "java:global/env/ejb3_assembly_appres_warmbean/globalds");
  }

  public void clientPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    verifyRecords(request, response, postConstructRecords);
  }

  public void mbeanPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    verifyRecords(request, response,
        appResManagedBean.getPostConstructRecords());
    verifyRecords(request, response, testManagedBean.getPostConstructRecords());
  }

  public void ejbPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    verifyRecords(request, response,
        AppResTest.getAppResBeanLocal().getPostConstructRecords());
  }
}
