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

package com.sun.ts.tests.jpa.core.criteriaapi.Root;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.Department_;
import com.sun.ts.tests.jpa.common.schema30.Employee;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.UtilCustomerData;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)


public class Client2IT extends UtilCustomerData {
	
	
	 @Deployment(testable = false, managed = false)
	 	public static JavaArchive createDeployment() throws Exception {

	 		String pkgNameWithoutSuffix = Client2IT.class.getPackageName();
	 		String pkgName = Client2IT.class.getPackageName() + ".";
	 		String[] classes = {};
	 		return createDeploymentJar("jpa_core_criteriaapi_root2.jar", pkgNameWithoutSuffix, classes);
	 }


 
  /*
   * @testName: joinStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1144;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void joinStringTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join("work");
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("joinStringTest failed");
    }
  }

  /*
   * @testName: joinStringJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1146;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void joinStringJoinTypeTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join("work", JoinType.INNER);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("joinStringJoinTypeTest failed");
    }
  }

  /*
   * @testName: joinSingularAttributeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1134;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void joinSingularAttributeTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join(Customer_.work);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("joinSingularAttributeTest failed");
    }
  }

  /*
   * @testName: joinSingularAttributeJoinTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1135;
   * 
   * @test_Strategy: This query is defined on a one-many relationship. Verify
   * the results were accurately returned.
   *
   * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
   */
  @SetupMethod(name = "setupCustomerData")
  @Test
  public void joinSingularAttributeJoinTypeTest() throws Exception {
    boolean pass = false;
    String expectedPKs[];

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Address> address = customer.join(Customer_.work,
          JoinType.INNER);
      Expression e = cbuilder.literal("4");
      cquery.where(address.get("id").in(e)).select(customer);
      TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
      List<Customer> clist = tquery.getResultList();

      expectedPKs = new String[1];
      expectedPKs[0] = "2";

      if (!checkEntityPK(clist, expectedPKs)) {
        TestUtil
            .logErr("Did not get expected results.  Expected 1 reference, got: "
                + clist.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass) {
      throw new Exception("joinSingularAttributeJoinTypeTest failed");
    }
  }
}
