/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.war.datasource.stateful;

import static com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.verifyDataSource;

import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.sql.DataSource;

import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.ComponentBase;

@DataSourceDefinition(name = "java:module/env/moduleds", description = "override with <data-source> in ejb-jar.xml", className = "jdbc.ClientDataSource", portNumber = 8080, serverName = "x", databaseName = "x", user = "x", password = "x",

    initialPoolSize = 1, isolationLevel = Connection.TRANSACTION_SERIALIZABLE, loginTimeout = 300, maxIdleTime = 1000, maxPoolSize = 2, minPoolSize = 1, transactional = true, url = "jdbc:derby://${derby.server}:${derby.port}/${derby.dbName};create=true")
public class Interceptor1 extends ComponentBase {
  @Resource(lookup = "java:app/env/appds")
  private DataSource appds;

  @Resource(lookup = "java:app/env/appds2")
  private DataSource appds2;

  @Resource(lookup = "java:module/env/moduleds")
  private DataSource moduleds;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct(InvocationContext inv) {
    // transactional is overridden to false in descriptor. So connections
    // will not participate in any transaction, if one is active.
    boolean c = true;
    getPostConstructRecords()
        .append(String.format("In postConstruct of %s%n", this));

    verifyDataSource(getPostConstructRecords(), c, "java:app/env/appds",
        "java:app/env/appds2", "java:module/env/moduleds");
    verifyDataSource(getPostConstructRecords(), c, appds, appds2, moduleds);

    verifyDataSource(getPostConstructRecords(), c, moduleds);
    try {
      inv.proceed();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    if (methodName.equals("getPostConstructRecordsFromInterceptor")) {
      return getPostConstructRecords();
    }
    return inv.proceed();
  }
}
