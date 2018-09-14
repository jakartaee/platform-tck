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

package com.sun.ts.tests.ejb30.common.helloejbjar;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public class HelloBeanBase implements HelloRemoteIF, HelloLocalIF {
  public static final Logger logger = Logger
      .getLogger(HelloBeanBase.class.getName());

  private List<String> records = new ArrayList<String>();

  @Resource(lookup = "java:app/AppName")
  private String appName;

  @Resource(lookup = "java:module/ModuleName")
  private String moduleName;

  public HelloBeanBase() {
  }

  public void hello() {
  }

  public Object getMessage() {
    return toString();
  }

  public int add(int a, int b) {
    return a + b;
  }

  public void addRecord(String rec) {
    if (records.contains(rec)) {
      logger.warning("Record already exists in HelloBeanBase: " + rec);
    } else {
      records.add(rec);
    }
  }

  public List<String> getAndClearRecords() {
    List<String> r = new ArrayList<String>(records);
    records.clear();
    return r;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    System.out.println(
        String.format("appName: %s, moduleName: %s", appName, moduleName));
    String nameExpected = "ejb3_common_helloejbjar_standalone_component_ejb";
    if (!nameExpected.equals(appName)) {
      throw new RuntimeException(String.format(
          "Expecting appName %s, but actual %s", nameExpected, appName));
    }
    if (!nameExpected.equals(moduleName)) {
      throw new RuntimeException(String.format(
          "Expecting moduleName %s, but actual %s", nameExpected, moduleName));
    }

  }

}
