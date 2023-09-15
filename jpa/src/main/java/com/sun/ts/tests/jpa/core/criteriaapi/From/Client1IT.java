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

package com.sun.ts.tests.jpa.core.criteriaapi.From;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.Department;
import com.sun.ts.tests.jpa.common.schema30.Department_;
import com.sun.ts.tests.jpa.common.schema30.Employee;
import com.sun.ts.tests.jpa.common.schema30.Order;
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
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

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
   * @assertion_ids: PERSISTENCE:JAVADOC:998; PERSISTENCE:JAVADOC:1000;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.join("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.join("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1002; PERSISTENCE:JAVADOC:1004;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinCollection("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinCollection("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1014; PERSISTENCE:JAVADOC:1016;
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinSet("doesnotexist");
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
      From<Customer, Customer> customer = cquery.from(Customer.class);
      try {
        customer.joinSet("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1006; PERSISTENCE:JAVADOC:1008;
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinList("doesnotexist");
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinList("doesnotexist", JoinType.INNER);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1010; PERSISTENCE:JAVADOC:1012
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
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinMap("doesnotexist");
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
      Root<Customer> root = cquery.from(Customer.class);
      try {
        root.joinMap("doesnotexist", JoinType.INNER);
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

  /*
   * @testName: fromGetCorrelationParentIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:984;
   * 
   * @test_Strategy:
   */
  @Test
  public void fromGetCorrelationParentIllegalStateExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
      From<Customer, Customer> customer = cquery.from(Customer.class);
      boolean isCorr = customer.isCorrelated();
      if (!isCorr) {
        TestUtil.logTrace("isCorrelated() return false");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Expected isCorrelated() to return false, actual:" + isCorr);
      }
      try {
        customer.getCorrelationParent();
        TestUtil.logErr("Did not throw IllegalStateException");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception", e);
    }

    if (!pass1 || !pass2) {
      throw new Exception(
          "fromGetCorrelationParentIllegalStateExceptionTest failed");
    }
  }

}
