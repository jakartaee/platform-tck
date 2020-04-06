/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResLocalIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResManagedBean;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AppResBean extends AppResBeanBase
    implements AppResRemoteIF, AppResLocalIF {

  @EJB(lookup = "java:app/env/hello")
  private HelloRemoteIF hello;

  @EJB(lookup = "java:app/env/AppResBean-remote")
  private AppResRemoteIF appResBeanRemote;

  @EJB(lookup = "java:app/env/AppResBean-local")
  private AppResLocalIF appResBeanLocal;

  @Resource
  private AppResManagedBean appResManagedBean;

  @Resource(type = AppResManagedBean.class, lookup = "java:module/test-managed-bean")
  private AppResCommonIF testManagedBean;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.assertEquals(null, AppResManagedBean.NAME,
        appResManagedBean.getName(), getPostConstructRecords());
    Helper.assertEquals(null, AppResManagedBean.NAME, testManagedBean.getName(),
        getPostConstructRecords());

    AppResTest.beanPostConstruct(myString, getPostConstructRecords(), true,
        true);
    DataSourceTest.verifyDataSource(getPostConstructRecords(), false,
        "java:app/env/appds",
        "java:global/env/ejb3_assembly_appres_warmbean/globalds");
    AppResTest.verifyInjections(getPostConstructRecords(), hello,
        appResBeanRemote, appResBeanLocal);
  }

  @SuppressWarnings("unused")
  @Resource(name = "java:app/env/myString")
  private void setMyString(String s) {
    this.myString = s;
  }
}
