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
package com.sun.ts.tests.ejb30.assembly.appres.appclientejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResLocalIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class AppResBean extends AppResBeanBase
    implements AppResRemoteIF, AppResLocalIF {
  @EJB(lookup = "java:app/env/hello")
  private HelloRemoteIF hello;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    AppResTest.beanPostConstruct(myString, getPostConstructRecords(), false,
        false);
    DataSourceTest.verifyDataSource(getPostConstructRecords(), false,
        "java:app/env/appds",
        "java:global/env/ejb3_assembly_appres_appclientejb/globalds");
    AppResTest.verifyInjections(getPostConstructRecords(), hello);
  }

  @SuppressWarnings("unused")
  @Resource(lookup = "java:app/env/myString")
  private void setMyString(String s) {
    this.myString = s;
  }
}
