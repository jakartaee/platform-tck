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

package com.sun.ts.tests.ejb30.misc.datasource.twojars;

import java.sql.Connection;
import java.util.Properties;

import javax.annotation.sql.DataSourceDefinition;
import javax.annotation.sql.DataSourceDefinitions;
import javax.ejb.EJB;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

@DataSourceDefinitions({
    @DataSourceDefinition(name = "java:global/datasource/twojars/appclient/globalds", description = "override it with <data-source> in application-client.xml", className = "@className@", portNumber = 8080, serverName = "@serverName@", databaseName = "@databaseName@", user = "@user@", password = "@password@", transactional = true) })
public class Client extends EETest {
  private static StringBuilder postConstructRecords = new StringBuilder();

  @EJB(beanName = "DataSourceBean")
  private static AppResRemoteIF dataSourceBean;

  @EJB(lookup = "java:global/ejb3_2standalone_component_ejb/DataSource2Bean")
  private static AppResRemoteIF dataSource2Bean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
  }

  public void cleanup() {
  }

  private static void nonPostConstruct() {
    ServiceLocator.lookupShouldFail("java:app/datasource/twojars/2/appds",
        postConstructRecords);
    Helper.getLogger().info(postConstructRecords.toString());

    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:global/datasource/twojars/appclient/globalds",
        "java:app/datasource/twojars/appclient/appds",

        "java:global/datasource/twojars/2/globalds");
  }

  /*
   * @testName: clientPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside appclient
   */
  public void clientPostConstruct() {
    nonPostConstruct();
    Helper.getLogger().info(postConstructRecords.toString());
  }

  /*
   * @testName: ejbPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside ejb
   */
  public void ejbPostConstruct() {
    Helper.getLogger()
        .info(dataSourceBean.getPostConstructRecords().toString());
  }

  /*
   * @testName: ejb2PostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside standalone ejb module
   */
  public void ejb2PostConstruct() {
    Helper.getLogger()
        .info(dataSource2Bean.getPostConstructRecords().toString());
  }

}
