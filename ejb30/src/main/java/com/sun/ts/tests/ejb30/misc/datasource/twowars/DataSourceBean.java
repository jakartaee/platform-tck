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
 * $Id: DataSourceBean.java 58944 2009-08-05 03:09:34Z cf126330 $
 */
package com.sun.ts.tests.ejb30.misc.datasource.twowars;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;

import com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@Stateless
@Remote(AppResRemoteIF.class)
@DataSourceDefinition(name="java:app/datasource/twowars/ejb/appds",
        className="org.apache.derby.jdbc.ClientDataSource",
        portNumber=1527,
        serverName="localhost",
        databaseName="derbyDB",
        user="cts1",
        password="cts1",
        properties={})
public class DataSourceBean extends AppResBeanBase {

  //We don't know which archive will be deployed and loaded first, the ear or the standalone war.
  //So verify the resources in a business method.
  private void nonPostConstruct() {
    ServiceLocator.lookupShouldFail("java:app/env/servlet2/appds", postConstructRecords);
    ServiceLocator.lookupShouldFail("java:comp/env/compdsservlet", postConstructRecords);
    ServiceLocator.lookupShouldFail("java:comp/env/defaultdsservlet", postConstructRecords);
    ServiceLocator.lookupShouldFail("java:module/env/moduledsservlet", postConstructRecords);

    Helper.getLogger().info(postConstructRecords.toString());

    DataSourceTest.verifyDataSource(postConstructRecords, false,
            "java:app/datasource/twowars/ejb/appds",
            "java:app/env/servlet/appds",
            "java:global/env/ts/datasource/servlet/globalds",
            "java:global/env/ts/datasource/servlet2/globalds"
    );
  }

  @Override
  public StringBuilder getPostConstructRecords() {
    nonPostConstruct();
    return super.getPostConstructRecords();
  }

}
