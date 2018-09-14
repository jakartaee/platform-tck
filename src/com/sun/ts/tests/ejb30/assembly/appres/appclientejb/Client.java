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

import static com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.getHelloBeanRemote;
import static com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.verifyDataSource;
import static com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.verifyMyString;
import static com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.verifyPersistenceUnit;
import static com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.verifyQueue;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Queue;
import javax.sql.DataSource;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest;

public class Client extends EETest {
  private static StringBuilder postConstructRecords = new StringBuilder();

  @Resource(lookup = "java:app/env/myString")
  private static String myString;

  @EJB(lookup = "java:app/env/hello")
  private static HelloRemoteIF hello;

  @EJB(lookup = "java:app/env/AppResBean-remote")
  private static AppResRemoteIF appResBeanRemote;

  @Resource(lookup = "java:app/env/db1")
  private static DataSource db1;

  @Resource(lookup = "java:app/env/receiveQueue")
  private static Queue receiveQueue;

  @Resource(lookup = "java:app/env/appds")
  private static DataSource appds;

  @Resource(lookup = "java:global/env/ejb3_assembly_appres_appclientejb/globalds")
  private static DataSource globalds;

  @Resource
  private static Validator validator;

  @Resource
  private static ValidatorFactory validatorFactory;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
  }

  public void cleanup() {
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private static void postConstruct() {
    int a = 1, b = 1, c = a + b;
    verifyDataSource(postConstructRecords);
    verifyQueue(postConstructRecords);
    verifyMyString(postConstructRecords);
    verifyPersistenceUnit(postConstructRecords);
    // verifyValidatorAndFactory(postConstructRecords);

    assertEquals(null, "myString", myString, postConstructRecords);
    assertEquals(null, c, getHelloBeanRemote().add(a, b), postConstructRecords);

    DataSourceTest.verifyDataSource(postConstructRecords, false,
        "java:app/env/appds",
        "java:global/env/ejb3_assembly_appres_appclientejb/globalds");
    AppResTest.verifyInjections(postConstructRecords, hello, appResBeanRemote,
        db1, receiveQueue, appds, globalds, validator, validatorFactory);
  }

  private static AppResRemoteIF getAppResBeanRemote() {
    // String s =
    // "java:global/ejb3_assembly_appres_appclientejb/ejb3_assembly_appres_appclientejb_ejb/AppResBean!com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF";
    String s = "java:app/env/AppResBean-remote";
    return (AppResRemoteIF) lookupNoTry(s);
  }

  /*
   * @testName: clientPostConstruct
   * 
   * @test_Strategy: declare resources in application.xml and inject them into
   * appclient.
   */
  public void clientPostConstruct() {
    Helper.getLogger().info(postConstructRecords.toString());
  }

  /*
   * @testName: ejbPostConstruct
   * 
   * @test_Strategy: declare resources in application.xml and inject them into
   * AppResBean.
   */
  public void ejbPostConstruct() {
    Helper.getLogger()
        .info(getAppResBeanRemote().getPostConstructRecords().toString());
  }

}
