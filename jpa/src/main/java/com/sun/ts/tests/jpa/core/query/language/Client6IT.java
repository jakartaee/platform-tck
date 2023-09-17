/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.query.language;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Country;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.SoftwareProduct;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class Client6IT extends Util {

  /* Run test */

  /*
   * @testName: resultContainsFetchReference
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1718;
   * 
   * @test_Strategy: SELECT d FROM Department d LEFT JOIN FETCH
   * d.lastNameEmployees WHERE d.id = 1
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  public void resultContainsFetchReference() throws Exception {
    boolean pass = false;
    List<Department> result;

    getEntityTransaction().begin();

    result = getEntityManager().createQuery(
        "SELECT d FROM Department d LEFT JOIN FETCH d.lastNameEmployees WHERE d.id = 1")
        .getResultList();

    List<Integer> expected = new ArrayList<Integer>();
    expected.add(deptRef[0].getId());

    if (result.size() == 1) {
      List<Integer> actual = new ArrayList<Integer>();
      actual.add(result.get(0).getId());
      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + result.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
    } else {
      TestUtil.logErr("More than 1 result got returned:");
      for (Department dept : result) {
        TestUtil.logErr("Dept:" + dept.toString());
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("resultContainsFetchReference test failed");

    }
  }

}
