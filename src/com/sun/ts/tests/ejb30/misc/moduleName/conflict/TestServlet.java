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

package com.sun.ts.tests.ejb30.misc.moduleName.conflict;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase;
import com.sun.ts.tests.ejb30.common.helper.Helper;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends TestServletBase {

  @EJB(beanName = "ModuleBean")
  private AppResCommonIF moduleBean;

  @EJB(beanName = "Module2Bean")
  private AppResRemoteIF module2Bean;

  private void nonPostConstruct() {
    postConstructRecords = new StringBuilder();
    String name1 = moduleBean.getName();
    String name2 = module2Bean.getName();

    Helper.assertNotEquals(null, name1, name2, postConstructRecords);
    Helper.assertNotEquals(null, null, name1, postConstructRecords);
  }

  public void servletPostConstruct(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    nonPostConstruct();
    verifyRecords(request, response, postConstructRecords);
  }

}
