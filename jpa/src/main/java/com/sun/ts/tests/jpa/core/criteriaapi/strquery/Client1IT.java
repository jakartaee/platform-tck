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

package com.sun.ts.tests.jpa.core.criteriaapi.strquery;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Alias;
import com.sun.ts.tests.jpa.common.schema30.CreditCard;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Phone;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.Util;
import com.sun.ts.tests.jpa.core.criteriaapi.Root.Client2IT;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class Client1IT extends Util {
	
	 @Deployment(testable = false, managed = false)
	 	public static JavaArchive createDeployment() throws Exception {

	 		String pkgNameWithoutSuffix = Client1IT.class.getPackageName();
	 		String pkgName = Client1IT.class.getPackageName() + ".";
	 		String[] classes = {};
	 		return createDeploymentJar("jpa_core_criteriaapi_strquery.jar", pkgNameWithoutSuffix, classes);
	 }


  /* Test setup */
@BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("Entering Setup");
    try {
      super.setup();
      getEntityManager();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /* Run test */
  /*
   * @testName: joinTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1035; PERSISTENCE:JAVADOC:1036;
   * PERSISTENCE:JAVADOC:1037
   * 
   * @test_Strategy:
   */
  @Test
  public void joinTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      TestUtil.logMsg("Testing default getJoinType");
      JoinType jt = customer.join("aliases").getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected:" + jt.name());
        pass1 = true;
      } else {
        TestUtil.logErr("Expected:" + JoinType.INNER.name() + ", actual:" + jt);
      }
      cquery = null;
      TestUtil.logMsg("Testing INNER getJoinType");
      cquery = cbuilder.createQuery(Customer.class);
      customer = cquery.from(Customer.class);
      jt = customer.join("aliases", JoinType.INNER).getJoinType();
      if (jt.equals(JoinType.INNER)) {
        TestUtil.logTrace("Received expected:" + jt.name());
        pass2 = true;
      } else {
        TestUtil.logErr("Expected:" + JoinType.INNER.name() + ", actual:" + jt);
      }
      cquery = null;
      TestUtil.logMsg("Testing LEFT getJoinType");
      cquery = cbuilder.createQuery(Customer.class);
      customer = cquery.from(Customer.class);
      jt = customer.join("aliases", JoinType.LEFT).getJoinType();
      if (jt.equals(JoinType.LEFT)) {
        TestUtil.logTrace("Received expected:" + jt.name());
        pass3 = true;
      } else {
        TestUtil.logErr("Expected:" + JoinType.LEFT.name() + ", actual:" + jt);
      }
      cquery = null;
      TestUtil.logMsg("Testing INNER getAttribute");
      cquery = cbuilder.createQuery(Customer.class);
      customer = cquery.from(Customer.class);
      Attribute attr = customer.join("aliases").getAttribute();
      if (attr.getName().equals("aliases")) {
        TestUtil.logTrace("Received expected:" + attr.getName());
        pass4 = true;
      } else {
        TestUtil.logErr("Expected:aliases, actual:" + attr.getName());
      }
      cquery = null;
      TestUtil.logMsg("Testing getParent");
      cquery = cbuilder.createQuery(Customer.class);
      customer = cquery.from(Customer.class);
      From from = customer.join("aliases").getParent();
      if (from.getClass().getName().equals(customer.getClass().getName())) {
        TestUtil.logTrace("Received expected:" + from.getClass().getName());
        pass5 = true;
      } else {
        TestUtil.logErr("Expected:" + customer.getClass().getName()
            + ", actual:" + from.getClass().getName());
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
      throw new Exception("joinTest failed");
    }
  }

  /*
   * @testName: fetchStringAndStringJoinTypeIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:979; PERSISTENCE:JAVADOC:1022;
   * PERSISTENCE:JAVADOC:981; PERSISTENCE:JAVADOC:1024;
   * 
   * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch an attribute that
   * does not exist .
   */
  @Test
  public void fetchStringAndStringJoinTypeIllegalArgumentException()
      throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    TestUtil.logMsg("Testing String");

    try {
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.fetch("doesnotexist");
      TestUtil.logErr("did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    TestUtil.logMsg("Testing String, JoinType");

    try {
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      customer.fetch("doesnotexist", JoinType.INNER);
      TestUtil.logErr("did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception:", e);
    }

    if (!pass1 || !pass2) {
      throw new Exception(
          "fetchStringAndStringJoinTypeIllegalArgumentException failed");
    }
  }

  
}
