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

package com.sun.ts.tests.ejb30.misc.moduleName.twowars;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertNotEquals;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupShouldFail;

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase;
import com.sun.ts.tests.ejb30.common.helper.Helper;

@WebServlet(urlPatterns = "/TestServlet2", loadOnStartup = 1)
public class TestServlet2 extends TestServletBase {
  @EJB
  private AppResRemoteIF module2Bean;

  // See comments in TestServlet

  @Resource
  private ModuleMBean moduleMBean;

  // @EJB(lookup="java:global/ejb3_misc_moduleName_twowars/renamed_twowars_web/ModuleBean")
  // private AppResRemoteIF moduleBean;

  private void nonPostConstruct() {
    postConstructRecords = new StringBuilder();

    lookupShouldFail("java:app/two_standalone_component_web/ModuleMBean",
        postConstructRecords);
    lookupShouldFail("java:app/two_standalone_component_web/Module2Bean",
        postConstructRecords);

    lookupShouldFail(
        "java:global/ejb3_misc_moduleName_twowars/ejb3_misc_moduleName_twowars_web/ModuleBean",
        postConstructRecords);

    lookupShouldFail("java:global/two_standalone_component_web/Module2Bean",
        postConstructRecords);

    Helper.getLogger().info(postConstructRecords.toString());

    // assertNotEquals(null, null, moduleBean, postConstructRecords);
    assertNotEquals(null, null, moduleMBean, postConstructRecords);
    assertNotEquals(null, null, module2Bean, postConstructRecords);

    AppResRemoteIF lookupResult = null;
    String[] names = {

        "java:module/ModuleMBean", "java:module/Module2Bean",

        "java:app/renamed2_twowars_web/ModuleMBean",
        "java:app/renamed2_twowars_web/Module2Bean",

        "java:global/ejb3_misc_moduleName_twowars/renamed_twowars_web/ModuleBean",

        "java:global/renamed2_twowars_web/Module2Bean" };
    for (String name : names) {
      postConstructRecords.append("About to look up " + name);
      lookupResult = (AppResRemoteIF) lookupNoTry(name);
      assertNotEquals(null, null, lookupResult, postConstructRecords);
      lookupResult = null;
    }
  }

  public void servletPostConstruct2(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    nonPostConstruct();
    verifyRecords(request, response, postConstructRecords);
    verifyRecords(request, response, module2Bean.getPostConstructRecords());
  }
}
