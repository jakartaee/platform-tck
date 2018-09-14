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

package com.sun.ts.lib.harness;

import java.io.*;
import java.util.*;
import com.sun.javatest.*;
import com.sun.javatest.finder.*;
import com.sun.ts.lib.util.TestUtil;

/**
 * A adapter between html test description and cts tag test description. It is
 * designed specific for jaxp/api/xml_schema tests. If applied to other test
 * directories, at least change the executeClass and testDir value.
 *
 * @created November 1, 2002
 */
public class TSHTMLTestFinder extends HTMLTestFinder {
  public void foundTestDescription(Map entries, File file, int line) {
    VehicleVerifier vehicleVerifier = VehicleVerifier.getInstance(file);
    String[] vehicles = vehicleVerifier.getVehicleSet();
    for (int i = 0; i < vehicles.length; i++) {
      TestDescription td = createTestDescription(entries, file, vehicles[i]);
      // System.out.println("### TestDesciption: " + td.toString());
      foundTestDescription(td);
    }
  }

  private TestDescription createTestDescription(Map map, File file,
      String vehicle) {
    Map result = new Hashtable(13);
    String id = (String) map.get("id");
    if (id == null) {
      id = (String) map.get("name");
    }
    if (id == null) {
      System.out.println("### id or name is null");
      Thread.dumpStack();
    }
    id = new StringBuffer(50).append(id).append("_from_").append(vehicle)
        .toString();
    // String executeClass = (String) map.get("executeClass");
    String executeClass = "com.sun.ts.tests.jaxp.api.xml_schema.XmlSchemaRunner";
    String executeArgs = (String) map.get("executeArgs");
    if (id != null) {
      result.put("testName", id);
      result.put("id", id);
    }
    if (executeClass != null) {
      result.put("classname", executeClass);
    }
    if (executeArgs != null) {
      result.put("testArgs", executeArgs);
    }
    result.put("service_eetest", "yes");

    String testDir = file.getParent();
    // result.put("test_directory", TestUtil.getRelativePath(testDir));
    result.put("test_directory", "com/sun/ts/tests/jaxp/api/xml_schema");

    File root = this.getRoot();
    return new TestDescription(root, file, result);
  }

}
