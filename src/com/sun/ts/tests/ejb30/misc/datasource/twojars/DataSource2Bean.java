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
package com.sun.ts.tests.ejb30.misc.datasource.twojars;

import java.sql.Connection;

import javax.annotation.sql.DataSourceDefinition;
import javax.annotation.sql.DataSourceDefinitions;
import jakarta.ejb.Remote;
import jakarta.ejb.Singleton;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@Singleton
@Remote(AppResRemoteIF.class)
@DataSourceDefinitions({
    @DataSourceDefinition(name = "java:global/datasource/twojars/2/globalds", description = "override it with <data-source> in ejb-jar.xml", className = "@className@", portNumber = 8080, serverName = "@serverName@", databaseName = "@databaseName@", user = "@user@", password = "@password@", isolationLevel = Connection.TRANSACTION_SERIALIZABLE) })
public class DataSource2Bean extends AppResBeanBase {

  private void nonPostConstruct() {
    ServiceLocator.lookupShouldFail(
        "java:app/datasource/twojars/appclient/appds", postConstructRecords);
    Helper.getLogger().info(postConstructRecords.toString());

    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:global/datasource/twojars/appclient/globalds",

        "java:app/datasource/twojars/2/appds",
        "java:global/datasource/twojars/2/globalds");
  }

  @Override
  public StringBuilder getPostConstructRecords() {
    nonPostConstruct();
    return super.getPostConstructRecords();
  }

}
