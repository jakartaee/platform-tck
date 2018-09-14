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

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupByShortNameNoTry;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.annotation.PreDestroy;
import java.util.concurrent.CopyOnWriteArrayList;

abstract public class BeanBase implements LocalIF {

  private StringBuilder postConstructRecords;

  protected CopyOnWriteArrayList arrayList;

  protected String databaseURL;

  protected String databaseUser;

  protected String databasePassword;

  protected String driverClassName;

  @EJB(name = "oneBean", beanName = "OneBean")
  protected LocalIF oneBean;

  @EJB(name = "twoBean", beanName = "TwoBean")
  protected LocalIF twoBean;

  @EJB(name = "threeBean", beanName = "ThreeBean")
  protected LocalIF threeBean;

  public List<String> call123() {
    if (postConstructRecords == null) {
      throw new RuntimeException("postConstructRecords is null.");
    }

    List<String> records = new ArrayList<String>();
    records.add(oneBean.getName());
    records.add(twoBean.getName());
    records.add(threeBean.getName());
    return records;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    postConstructRecords = new StringBuilder();

    LocalIF b1 = (LocalIF) lookupByShortNameNoTry("oneBean");
    assertEquals(null, oneBean, b1, postConstructRecords);
    LocalIF b2 = (LocalIF) lookupByShortNameNoTry("twoBean");
    assertEquals(null, twoBean, b2, postConstructRecords);
    LocalIF b3 = (LocalIF) lookupByShortNameNoTry("threeBean");
    assertEquals(null, threeBean, b3, postConstructRecords);
  }

  public StringBuilder getPostConstructRecords() {
    return postConstructRecords;
  }

  public StringBuilder lookupJNDINames(String appName, String moduleName1,
      String moduleName2, String moduleName3) {
    StringBuilder sb = new StringBuilder();
    LocalIF b1 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getGlobalJNDIName(appName, moduleName1, "OneBean"));
    assertEquals(null, oneBean, b1, sb);
    LocalIF b2 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getGlobalJNDIName(appName, moduleName2, "TwoBean"));
    assertEquals(null, twoBean, b2, sb);
    LocalIF b3 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getGlobalJNDIName(appName, moduleName3, "ThreeBean"));
    assertEquals(null, threeBean, b3, sb);

    b1 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getAppJNDIName(moduleName1, "OneBean"));
    assertEquals(null, oneBean, b1, sb);
    b2 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getAppJNDIName(moduleName2, "TwoBean"));
    assertEquals(null, twoBean, b2, sb);
    b3 = (LocalIF) lookupNoTry(
        GlobalJNDITest.getAppJNDIName(moduleName3, "ThreeBean"));
    assertEquals(null, threeBean, b3, sb);

    return sb;
  }

  public List<String> setupOneBean(String url, String user, String password,
      String driverName) {

    databaseURL = url;
    databaseUser = user;
    databasePassword = password;
    driverClassName = driverName;

    return Arrays.asList("Setup called");

  }

  public List<String> setupOneBeanWithArrayList(
      CopyOnWriteArrayList copyOnWriteArrayList) {

    arrayList = copyOnWriteArrayList;

    return Arrays.asList("setupOneBeanWithArrayList called");
  }

}
