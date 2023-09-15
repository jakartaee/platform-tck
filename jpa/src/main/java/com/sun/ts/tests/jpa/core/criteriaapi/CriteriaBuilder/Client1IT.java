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

package com.sun.ts.tests.jpa.core.criteriaapi.CriteriaBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.schema30.Address;
import com.sun.ts.tests.jpa.common.schema30.Alias;
import com.sun.ts.tests.jpa.common.schema30.Customer;
import com.sun.ts.tests.jpa.common.schema30.Customer_;
import com.sun.ts.tests.jpa.common.schema30.HardwareProduct;
import com.sun.ts.tests.jpa.common.schema30.LineItem;
import com.sun.ts.tests.jpa.common.schema30.Order;
import com.sun.ts.tests.jpa.common.schema30.Order_;
import com.sun.ts.tests.jpa.common.schema30.Product;
import com.sun.ts.tests.jpa.common.schema30.ShelfLife;
import com.sun.ts.tests.jpa.common.schema30.SoftwareProduct;
import com.sun.ts.tests.jpa.common.schema30.SoftwareProduct_;
import com.sun.ts.tests.jpa.common.schema30.Spouse;
import com.sun.ts.tests.jpa.common.schema30.Trim;
import com.sun.ts.tests.jpa.common.schema30.Util;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client1IT extends Util {

@BeforeEach
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      getEntityManager();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: createQuery
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:756; PERSISTENCE:SPEC:1701;
   * PERSISTENCE:SPEC:1703; PERSISTENCE:SPEC:1704;
   * 
   * @test_Strategy:
   *
   */
	@Test
  public void createQuery() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery cquery = cbuilder.createQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      pass = true;
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("createQuery Test  failed");
    }
  }

  /*
   * @testName: createQuery2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:757; PERSISTENCE:SPEC:1703;
   * PERSISTENCE:SPEC:1704;
   *
   * @test_Strategy:
   *
   */
	@Test
  public void createQuery2() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      pass = true;
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("createQuery2 Test  failed");
    }
  }

  /*
   * @testName: createTuple
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:758; PERSISTENCE:SPEC:1703;
   * PERSISTENCE:SPEC:1704;
   *
   * @test_Strategy:
   *
   */
	@Test
  public void createTuple() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query Tuple");
      pass = true;
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query Tuple");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("createTuple Test  failed");
    }
  }

  /*
   * @testName: tupleSelectionArrayIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:873
   * 
   * @test_Strategy:
   */
  @Test
  public void tupleSelectionArrayIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;

    try {
      CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

      getEntityTransaction().begin();
      CriteriaQuery<Tuple> cquery = qbuilder.createTupleQuery();
      if (cquery != null) {
        TestUtil.logTrace("Obtained Non-null Criteria Query");
        Root<Customer> cust = cquery.from(Customer.class);

        Selection[] s = { cust.get("id"), cust.get("name") };

        TestUtil.logMsg("Testing tuple");
        try {
          qbuilder.tuple(qbuilder.tuple(s));
          TestUtil.logErr("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass1 = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }

        TestUtil.logMsg("Testing array");
        try {
          qbuilder.tuple(qbuilder.array(s));
          TestUtil.logErr("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass2 = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }

      } else {
        TestUtil.logErr("Failed to get Non-null Criteria Query");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass1 || !pass2) {
      throw new Exception("tupleSelectionArrayIllegalArgumentExceptionTest failed");
    }
  }


  /*
   * @testName: literalIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:807
   *
   * @test_Strategy:
   */
  @Test
  public void literalIllegalArgumentExceptionTest() throws Exception {
    boolean pass = false;

    try {
      CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
      cbuilder.literal(null);
      TestUtil.logErr("Did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception:", e);
    }

    if (!pass) {
      throw new Exception("literalIllegalArgumentExceptionTest failed");
    }
  }




  /*
   * @testName: trimspecTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:915; PERSISTENCE:JAVADOC:916
   *
   * @test_Strategy:
   *
   *
   */
  @Test
  public void trimspecTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = true;
    Collection<CriteriaBuilder.Trimspec> expected = new ArrayList();
    expected.add(CriteriaBuilder.Trimspec.BOTH);
    expected.add(CriteriaBuilder.Trimspec.LEADING);
    expected.add(CriteriaBuilder.Trimspec.TRAILING);

    try {
      CriteriaBuilder.Trimspec[] ts = CriteriaBuilder.Trimspec.values();
      if (ts.length == 3) {
        for (CriteriaBuilder.Trimspec tspec : ts) {
          pass1 = true;
          if (expected.contains(tspec)) {
            TestUtil.logMsg("Testing valueOf:" + tspec);
            CriteriaBuilder.Trimspec.valueOf(tspec.toString());
          } else {
            pass2 = false;
            TestUtil.logErr("values() returned incorrect value:" + tspec);
          }
        }
      } else {
        TestUtil.logErr("Expected number of values:3, actual:" + ts.length);
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass1 || !pass2) {
      throw new Exception("trimspecTest failed");

    }
  }

  /*
   * @testName: createCriteriaDeleteTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1676; PERSISTENCE:SPEC:1701;
   *
   * @test_Strategy:
   */
  @Test
  public void createCriteriaDeleteTest() throws Exception {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaDelete<Product> cd = cbuilder.createCriteriaDelete(Product.class);
      if (cd != null) {
        TestUtil.logTrace("Obtained Non-null CriteriaDelete");
        pass = true;
      } else {
        TestUtil.logErr("Failed to get Non-null CriteriaDelete");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception", ex);
    }

    if (!pass) {
      throw new Exception("createCriteriaDeleteTest test failed");
    }
  }

  /*
   * @testName: createCriteriaUpdateTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1677; PERSISTENCE:SPEC:1701;
   *
   * @test_Strategy:
   */
  @Test
  public void createCriteriaUpdateTest() throws Exception {
    boolean pass = false;
    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    try {
      CriteriaUpdate<Product> cd = cbuilder.createCriteriaUpdate(Product.class);
      if (cd != null) {
        TestUtil.logTrace("Obtained Non-null CriteriaUpdate");
        pass = true;
      } else {
        TestUtil.logErr("Failed to get Non-null CriteriaUpdate");
      }
    } catch (Exception ex) {
      TestUtil.logErr("Received unexpected exception", ex);
    }

    if (!pass) {
      throw new Exception("createCriteriaUpdateTest test failed");
    }
  }

  /*
   * @testName: coalesceTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:747
   * 
   * @test_Strategy:
   */
  @Test
  public void coalesceTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    getEntityTransaction().begin();
    CriteriaBuilder.Coalesce col = cbuilder.coalesce();
    if (col != null) {
      TestUtil.logTrace("Obtained Non-null Coalesce");
      pass = true;
    } else {
      TestUtil.logErr("Failed to get Non-null Coalesce");

    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("coalesceTest failed");
    }
  }

}
