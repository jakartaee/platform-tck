/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.criteriaapi.CriteriaQuery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address_;
import com.sun.ts.tests.jpa.common.schema30.Country;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.Department_;
import com.sun.ts.tests.jpa.common.schema30.HardwareProduct;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Order_;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class Client7IT extends Util {


	@Deployment(testable = false, managed = false)
 	public static JavaArchive createDeployment() throws Exception {

 		String pkgNameWithoutSuffix = Client7IT.class.getPackageName();
 		String pkgName = Client7IT.class.getPackageName() + ".";
 		String[] classes = { pkgName + "A"};
 		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery7.jar", pkgNameWithoutSuffix, classes);
  }

  /*
   * @testName: resultContainsFetchReference
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1718;
   * 
   * @test_Strategy: SELECT d FROM Department d LEFT JOIN FETCH
   * d.lastNameEmployees WHERE d.id = 1
   */
  @SetupMethod(name = "setupDepartmentEmployeeData")
  @Test
  public void resultContainsFetchReference() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery<Department> cquery = cbuilder.createQuery(Department.class);
    if (cquery != null) {

      Root<Department> d = cquery.from(Department.class);
      d.fetch(Department_.lastNameEmployees, JoinType.LEFT);
      cquery.where(cbuilder.equal(d.get(Department_.id), 1)).select(d);
      cquery.distinct(true);
      Query q = getEntityManager().createQuery(cquery);
      List<Department> result = q.getResultList();

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
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("resultContainsFetchReference test failed");

    }
  }
}
