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

package com.sun.ts.tests.jpa.core.criteriaapi.Join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.CreditCard;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.Department_;
import com.sun.ts.tests.jpa.common.schema30.Employee;
import com.sun.ts.tests.jpa.common.schema30.Employee_;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Order_;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.PluralJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.metamodel.PluralAttribute;

public class Client1IT extends Util {

  /* Test setup */
	@BeforeEach
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

  /*
   * @testName: joinStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1053; PERSISTENCE:JAVADOC:1055;
   * 
   * @test_Strategy:
   *
   */
  @Test
  public void joinStringIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("String Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.join("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.join("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Exception("joinStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinCollectionIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1057; PERSISTENCE:JAVADOC:1059;
   * 
   * @test_Strategy:
   *
   */
  @Test
  public void joinCollectionIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;

    TestUtil.logMsg("String Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.joinCollection("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders);
      try {
        order.joinCollection("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);

      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Exception("joinCollectionIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinSetIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1069; PERSISTENCE:JAVADOC:1027;
   * PERSISTENCE:JAVADOC:1071;
   * 
   * @test_Strategy:
   *
   */
  @Test
  public void joinSetIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("String Test");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      try {
        order.joinSet("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("String, JoinType Test");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join(Customer_.orders2);
      try {
        order.joinSet("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected exception");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Exception("joinSetIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinListIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1061; PERSISTENCE:JAVADOC:1063;
   * 
   * @test_Strategy:
   */
  @Test
  public void joinListIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing String");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join("orders3");
      try {
        order.joinList("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("Testing String, JoinType");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> customer = cquery.from(Customer.class);
      Join<Customer, Order> order = customer.join("orders3");
      try {
        order.joinList("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Exception("joinListIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: joinMapIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1065; PERSISTENCE:JAVADOC:1067
   * 
   * @test_Strategy:
   */
  @Test
  public void joinMapIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    TestUtil.logMsg("Testing String");

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);

      try {
        department.joinMap("doesnotexist");
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    TestUtil.logMsg("Testing String, JoinType");
    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
      Root<Employee> employee = cquery.from(Employee.class);
      Join<Employee, Department> department = employee
          .join(Employee_.department);
      try {
        department.joinMap("doesnotexist", JoinType.INNER);
        TestUtil.logErr("Did not throw IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);

    }

    if (!pass1 || !pass2) {
      throw new Exception("joinMapIllegalArgumentExceptionTest failed");
    }
  }
}
