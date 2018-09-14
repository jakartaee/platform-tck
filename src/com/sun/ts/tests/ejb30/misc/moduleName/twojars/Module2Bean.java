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
package com.sun.ts.tests.ejb30.misc.moduleName.twojars;

import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupShouldFail;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;

@Singleton
public class Module2Bean extends AppResBeanBase implements AppResRemoteIF {
  @EJB
  private AppResRemoteIF module2Bean;

  @Resource
  private ModuleMBean moduleMBean;

  private void nonPostConstruct() {
    lookupShouldFail("java:app/ejb3_misc_moduleName_twojars_client/ModuleMBean",
        postConstructRecords);
    lookupShouldFail("java:app/ejb3_misc_moduleName_twojars_ejb/ModuleMBean",
        postConstructRecords);

    lookupShouldFail("java:app/ejb3_misc_moduleName_twojars_client/ModuleBean",
        postConstructRecords);
    lookupShouldFail("java:app/ejb3_misc_moduleName_twojars_ejb/ModuleBean",
        postConstructRecords);
    lookupShouldFail(
        "java:global/ejb3_misc_moduleName_twojars/ejb3_misc_moduleName_twojars_ejb/ModuleBean",
        postConstructRecords);

    lookupShouldFail("java:app/two_standalone_component_ejb/ModuleMBean",
        postConstructRecords);

    lookupShouldFail("java:app/two_standalone_component_ejb/Module2Bean",
        postConstructRecords);
    lookupShouldFail("java:global/two_standalone_component_ejb/Module2Bean",
        postConstructRecords);

    Helper.getLogger().info(postConstructRecords.toString());

    Helper.assertNotEquals(null, null, module2Bean, postConstructRecords);
    Helper.assertNotEquals(null, null, moduleMBean, postConstructRecords);

    AppResRemoteIF lookupResult = null;
    String[] names = { "java:module/ModuleMBean", "java:module/Module2Bean",
        "java:app/renamed2_twojars_ejb/ModuleMBean",
        "java:app/renamed2_twojars_ejb/Module2Bean",
        "java:global/renamed2_twojars_ejb/Module2Bean" };
    for (String name : names) {
      postConstructRecords.append("About to look up " + name);
      lookupResult = (AppResRemoteIF) lookupNoTry(name);
      Helper.assertNotEquals(null, null, lookupResult, postConstructRecords);
      lookupResult = null;
    }

    String expected = "renamed2_twojars_ejb";
    String lookup = "java:module/ModuleName";
    String value = (String) lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, expected, value,
        postConstructRecords);

    lookup = "java:app/AppName";
    value = (String) lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, expected, value,
        postConstructRecords);

    Helper.assertEquals("Compare to ModuleName from ModuleMBean", expected,
        moduleMBean.getModuleName(), postConstructRecords);
    Helper.assertEquals("Compare to AppName from ModuleMBean", expected,
        moduleMBean.getAppName(), postConstructRecords);
  }

  @Override
  public StringBuilder getPostConstructRecords() {
    nonPostConstruct();
    return super.getPostConstructRecords();
  }

}
