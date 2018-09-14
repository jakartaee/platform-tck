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
package com.sun.ts.tests.ejb30.lite.packaging.embed.classloader.annotated;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;

import javax.ejb.embeddable.EJBContainer;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest;
import com.sun.ts.lib.util.TestUtil;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client extends EJBLiteClientBase {
  private Map<String, Object> containerInitProps = new HashMap<String, Object>();

  private String databaseName;

  private String driverName;

  private String databaseURL;

  private String databaseUser;

  private String databasePassword;

  private static final String APP_NAME = "root";

  /*
   * @class.setup_props: all.props;
   * 
   */

  @Override
  public Map<String, Object> getContainerInitProperties() {

    try {
      databaseName = TestUtil.getProperty("jdbc.db");

      driverName = TestUtil.getProperty(databaseName + ".driver");
      databaseURL = TestUtil.getProperty(databaseName + ".url");
      databaseUser = TestUtil.getProperty(databaseName + ".user");
      databasePassword = TestUtil.getProperty(databaseName + ".passwd");

      TestUtil.logMsg("Driver class Name =" + driverName);
      TestUtil.logMsg("Database url =" + databaseURL);
      TestUtil.logMsg("Database User = " + databaseUser);
      TestUtil.logMsg("Database Password =" + databasePassword);

    } catch (Exception e) {
      TestUtil.logErr("Error reading properties");
    }

    containerInitProps.put(EJBContainer.MODULES, getAdditionalModules());
    containerInitProps.put(EJBContainer.APP_NAME, APP_NAME);
    return containerInitProps;
  }

  private URLClassLoader createClassLoader() {
    File[] modules = getAdditionalModules();
    if (modules == null || modules.length == 0) {
      Helper.getLogger()
          .warning("additionalModules testArgs is null or empty: " + modules);
      return null;
    }
    URL[] urls = new URL[modules.length];

    for (int i = 0; i < modules.length; i++) {
      try {
        urls[i] = modules[i].toURI().toURL();
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    appendReason("Creating a classloader with additionalModule URLs: ",
        Arrays.toString(urls));

    ClassLoader parent = Thread.currentThread().getContextClassLoader();
    if (parent == null) {
      parent = getClass().getClassLoader();
    }
    URLClassLoader cl = new URLClassLoader(urls, parent);
    return cl;
  }

  @Override
  public void setContextClassLoader() {
    URLClassLoader cl = createClassLoader();
    if (cl != null) {
      Thread.currentThread().setContextClassLoader(cl);
    }
  }

  /*
   * @testName: additionalModuleJar
   * 
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_two_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_three_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules.
   * 
   */
  public void additionalModuleJar() throws Exception {
    additionalModule(getModuleName(), "ejbembed_two_ejb", "ejbembed_three_ejb");
  }

  /*
   * @testName: additionalModuleDir
   * 
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/23/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules.
   */
  public void additionalModuleDir() throws Exception {
    additionalModule(getModuleName(), "23", "23");
  }

  /*
   * @testName: additionalModuleJarDir
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_three_ejb.jar"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/2/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: use a custom ContextClassLoader to find additional ejb
   * modules. Also javax.ejb.embeddable.modules property is set to include the
   * file location of the additional ejb modules.
   * 
   * 
   */
  public void additionalModuleJarDir() throws Exception {
    additionalModule(getModuleName(), "2", "ejbembed_three_ejb");
  }

  protected void additionalModule(String moduleName1, String moduleName2,
      String moduleName3) throws Exception {
    appendReason("Created EJBContainer provider ", getContainer(),
        " with init properties: ");
    appendReason(EJBContainer.APP_NAME + " = "
        + getContainerInitProperties().get(EJBContainer.APP_NAME));
    appendReason(EJBContainer.MODULES + " = "
        + getContainerInitProperties().get(EJBContainer.MODULES));
    File[] modPaths = (File[]) getContainerInitProperties()
        .get(EJBContainer.MODULES);

    for (File f : modPaths) {
      appendReason(f.getAbsolutePath());
    }

    LocalIF oneBean = (LocalIF) lookup(
        GlobalJNDITest.getGlobalJNDIName(APP_NAME, moduleName1, "OneBean"),
        null, null);
    LocalIF twoBean = (LocalIF) lookup(
        GlobalJNDITest.getGlobalJNDIName(APP_NAME, moduleName2, "TwoBean"),
        null, null);
    LocalIF threeBean = (LocalIF) lookup(
        GlobalJNDITest.getGlobalJNDIName(APP_NAME, moduleName3, "ThreeBean"),
        null, null);

    final List<String> expected2 = Arrays.asList("Setup called");
    assertEquals(null, expected2, oneBean.setupOneBean(databaseURL,
        databaseUser, databasePassword, driverName));

    final List<String> expected = Arrays.asList("OneBean", "TwoBean",
        "ThreeBean");
    assertEquals(null, expected, oneBean.call123());
    assertEquals(null, expected, twoBean.call123());
    assertEquals(null, expected, threeBean.call123());

    appendReason(oneBean.lookupJNDINames(APP_NAME, moduleName1, moduleName2,
        moduleName3));
    appendReason(twoBean.lookupJNDINames(APP_NAME, moduleName1, moduleName2,
        moduleName3));
    appendReason(threeBean.lookupJNDINames(APP_NAME, moduleName1, moduleName2,
        moduleName3));
  }

  /*
   * @testName: autoCloseTest
   * 
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/23/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: Extract From EJB 3.2 Spec : "During the implicit or
   * explicit processing of the close() method, the embeddable container calls
   * the PreDestroy methods of any singleton session bean instances in the
   * application"
   * 
   * 1) The test uses a Singleton (OneBean.java) 2) OneBean's PreDestroy method
   * uses a database utility and writes a message into database 3) Test client
   * reads that message from database and verifies autoclose feature of embedded
   * ejb container
   */
  public void autoCloseTest() throws Exception {

    appendReason("Created EJBContainer provider ", getContainer(),
        " with init properties: ");
    appendReason(EJBContainer.APP_NAME + " = "
        + getContainerInitProperties().get(EJBContainer.APP_NAME));
    appendReason(EJBContainer.MODULES + " = "
        + getContainerInitProperties().get(EJBContainer.MODULES));
    File[] modPaths = (File[]) getContainerInitProperties()
        .get(EJBContainer.MODULES);

    for (File f : modPaths) {
      appendReason(f.getAbsolutePath());
    }

    LocalIF oneBean = (LocalIF) lookup(
        GlobalJNDITest.getGlobalJNDIName(APP_NAME, getModuleName(), "OneBean"),
        null, null);

    // This is to invoke setupOneBean() to initialize oneBean with database
    // values

    final List<String> expected2 = Arrays.asList("Setup called");
    assertEquals(null, expected2, oneBean.setupOneBean(databaseURL,
        databaseUser, databasePassword, driverName));

    // Verify whether @PreDestroy called

    TSDbUtil tsDbUtil = new TSDbUtil(databaseURL, databaseUser,
        databasePassword, driverName);
    Connection connection = tsDbUtil.getConnection();
    String result = tsDbUtil.readFromDatabase(connection, "OneBean");

    TestUtil.logMsg("Message read from Database =" + result);

    if (result != null && result.equals("PreDestroy called")) { // This confirms
                                                                // the
                                                                // successful
                                                                // write during
                                                                // PreDestroy

      TestUtil.logMsg("autoCloseTest Passed");
      tsDbUtil.deleteRecordFromDatabase(connection, "OneBean");
    } else {
      throw new Fault("autoCloseTest Failed for Embeddable EJB Container");
    }

  }

  /*
   * testName: autoCloseTestWithArrayList
   * 
   * 
   * @testArgs: -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/23/"
   * -additionalModule
   * "${ts.home}/dist/com/sun/ts/tests/ejb30/lite/packaging/embed/classloader/annotated/ejbembed_vehicle_ejb.jar"
   * 
   * @test_Strategy: Extract From EJB 3.2 Spec : "During the implicit or
   * explicit processing of the close() method, the embeddable container calls
   * the PreDestroy methods of any singleton session bean instances in the
   * application"
   * 
   * 1) The test client passes CopyOnWriteArrayList to a Singleton
   * (OneBean.java) 2) OneBean's PreDestroy method uses writes a message into
   * the ArrayList 3) The test client reads the message from ArrayList to verify
   * autoclose feature of embedded ejb container
   */
  public void autoCloseTestWithArrayList() throws Exception {

    appendReason("Created EJBContainer provider ", getContainer(),
        " with init properties: ");
    appendReason(EJBContainer.APP_NAME + " = "
        + getContainerInitProperties().get(EJBContainer.APP_NAME));
    appendReason(EJBContainer.MODULES + " = "
        + getContainerInitProperties().get(EJBContainer.MODULES));
    File[] modPaths = (File[]) getContainerInitProperties()
        .get(EJBContainer.MODULES);

    for (File f : modPaths) {
      appendReason(f.getAbsolutePath());
    }

    LocalIF oneBean = (LocalIF) lookup(
        GlobalJNDITest.getGlobalJNDIName(APP_NAME, getModuleName(), "OneBean"),
        null, null);

    final List<String> expected2 = Arrays.asList("Setup called");
    assertEquals(null, expected2, oneBean.setupOneBean(databaseURL,
        databaseUser, databasePassword, driverName));

    // This is to invoke setupOneBeanWithArrayList ()
    CopyOnWriteArrayList arrayList = new CopyOnWriteArrayList();
    final List<String> expectedOutput = Arrays
        .asList("setupOneBeanWithArrayList called");
    assertEquals(null, expectedOutput,
        oneBean.setupOneBeanWithArrayList(arrayList));

    if (!arrayList.isEmpty()) {
      String tobeChecked = "OneBean PreDestroy called";
      boolean result = arrayList.contains(tobeChecked);
      if (result) {
        TestUtil.logMsg("autoCloseTestWithArrayList Passed");
      } else {
        TestUtil.logErr("autoCloseTestWithArrayList Failed");
        throw new Fault("autoCloseTestWithArrayList Failed");
      }
    } else {
      TestUtil.logErr("ArrayList empty : autoCloseTestWithArrayList Failed");
      throw new Fault("autoCloseTestWithArrayList Failed");
    }

  }
}
