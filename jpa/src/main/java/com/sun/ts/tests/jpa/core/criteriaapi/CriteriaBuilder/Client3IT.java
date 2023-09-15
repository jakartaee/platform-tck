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
import com.sun.ts.tests.jpa.common.schema30.UtilAliasData;

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

public class Client3IT extends UtilAliasData {

  /*
   * @testName: tupleGetIntIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:603; PERSISTENCE:JAVADOC:1164;
   * PERSISTENCE:SPEC:1303;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c Call Tuple.get() using a tuple element that does not
   * exist
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetIntIllegalArgumentExceptionTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(new Selection[] {
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME") });

      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

      List<Tuple> result = tq.getResultList();

      for (Tuple t : result) {
        TestUtil.logMsg("Testing invalid index");
        try {
          t.get(99);
          TestUtil.logErr(
              "Did not get expected IllegalArgumentException for invalid index:"
                  + t.get(99));
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Got expected IllegalArgumentException");
          if (getEntityTransaction().getRollbackOnly() != true) {
            pass = true;
          } else {
            TestUtil.logErr(
                "Transaction was marked for rollback and should not have been");
          }
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception", ex);
        }
        break;
      }

    } else {

      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("tupleGetIntIllegalArgumentExceptionTest failed");
    }
  }

 
  /*
   * @testName: tupleGetElementsGetTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:429; PERSISTENCE:JAVADOC:434;
   * PERSISTENCE:JAVADOC:1168; PERSISTENCE:JAVADOC:1169; PERSISTENCE:SPEC:1321;
   * PERSISTENCE:SPEC:1763;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c where c.name is not nulls
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetElementsGetTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME"));

      cquery.where(cbuilder.isNotNull(
          customer.get(Customer_.getSingularAttribute("name", String.class))));

      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);
      List<Tuple> result = tq.getResultList();

      for (Tuple t : result) {
        String expId = (String) t.get("ID");
        String expName = (String) t.get("NAME");
        TestUtil.logTrace("Received:" + expId + ", " + expName);

        List<TupleElement<?>> lte = t.getElements();
        for (TupleElement<?> te : lte) {
          String alias = te.getAlias();
          String type = te.getJavaType().getName();
          if (alias.equals("ID")) {
            String actId = (String) t.get(te);
            if (actId.equals(expId)) {
              TestUtil.logTrace("Received expected id:" + actId);
              pass1 = true;

            } else {
              TestUtil
                  .logErr("Expected id:" + expId + ", actual:|" + actId + "|");
            }
            if (type.equals("java.lang.String")) {
              pass2 = true;

              TestUtil.logTrace("Received expected Java Type for ID:" + type);
            } else {
              TestUtil
                  .logErr("Expected java type of ID: java.lang.String, actual:|"
                      + type + "|");
            }

          } else if (alias.equals("NAME")) {
            String actName = (String) t.get(te);
            if (actName.equals(expName)) {
              TestUtil.logTrace("Received expected name:" + actName);
              pass3 = true;

            } else {
              TestUtil.logErr(
                  "Expected name:|" + expName + "|, actual:|" + actName + "|");
            }

            if (type.equals("java.lang.String")) {
              TestUtil.logTrace("Received expected Java Type for NAME:" + type);
              pass4 = true;

            } else {
              TestUtil.logErr(
                  "Expected java type of NAME: java.lang.String, actual:|"
                      + type + "|");
            }

          } else {
            TestUtil.logErr("Received unexpected TupleElement:" + alias);
          }
        }
      }
    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Exception("tupleGetElementsGetTest failed");
    }
  }

 
  /*
   * @testName: tupleGetStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:431
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetStringTest() throws Exception {
    boolean pass1 = true;
    boolean pass2 = false;

    List<Integer> expected = new ArrayList<Integer>();
    for (Customer c : customerRef) {
      expected.add(Integer.valueOf(c.getId()));
    }
    Collections.sort(expected);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME"));

      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

      List<Tuple> result = tq.getResultList();

      List<Integer> actual = new ArrayList<Integer>();

      for (Tuple t : result) {
        Integer id = Integer.valueOf((String) t.get(0));
        String name = (String) t.get(1);
        if (name != null) {
          if (customerRef[id - 1].getName().equals(name)) {
            actual.add(id);
          } else {
            TestUtil.logErr("Expected name:|" + customerRef[id - 1].getName()
                + "|, actual:|" + name + "|");
            pass1 = false;
          }
        } else {
          if (customerRef[id - 1].getName() == null) {
            actual.add(id);
          } else {
            TestUtil.logErr("Expected name:" + customerRef[id - 1].getName()
                + ", actual:null");
            pass1 = false;
          }
        }

      }
      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass2 = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Exception("tupleGetStringTest failed");
    }
  }

  /*
   * @testName: tupleGetStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:601; PERSISTENCE:SPEC:1303;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c Call Tuple.get() using a tuple element that does not
   * exist
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetStringIllegalArgumentExceptionTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME"));

      cquery.where(cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "1"));
      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

      List<Tuple> result = tq.getResultList();

      Tuple t = result.get(0);
      TestUtil.logMsg("Testing valid alias");
      TestUtil.logTrace("value:" + t.get("NAME"));

      TestUtil.logMsg("Testing invalid alias");
      try {
        t.get("doesnotexist");
        TestUtil.logErr(
            "Did not get expected IllegalArgumentException for TupleElement:t.get(\"doesnotexist\")");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Got expected IllegalArgumentException");
        if (getEntityTransaction().getRollbackOnly() != true) {
          pass = true;
        } else {
          TestUtil.logErr(
              "Transaction was marked for rollback and should not have been");
        }
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception:" + e);

      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("tupleGetStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: tupleGetStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:600; PERSISTENCE:SPEC:1303;
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c Call Tuple.get() using a tuple type that does not
   * match
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetStringClassIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME"));

      cquery.where(cbuilder.equal(
          customer.get(Customer_.getSingularAttribute("id", String.class)),
          "1"));
      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

      List<Tuple> result = tq.getResultList();

      Tuple t = result.get(0);
      TestUtil.logMsg("Testing valid index");
      TestUtil.logTrace("value:" + t.get("NAME"));
      TestUtil.logMsg("Testing a name that does not exist");

      try {
        t.get("doesnotexist", String.class);
        TestUtil.logErr(
            "Did not throw IllegalArgumentException for TupleElement: t.get(\"doesnotexist\", String.class)");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Got expected IllegalArgumentException");
        if (getEntityTransaction().getRollbackOnly() != true) {
          pass1 = true;
        } else {
          TestUtil.logErr(
              "Transaction was marked for rollback and should not have been");
        }
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception:" + e);
      }
      TestUtil.logMsg("Testing invalid type");

      try {
        t.get("ID", Date.class);
        TestUtil.logErr(
            "Did not throw IllegalArgumentException for TupleElement:t.get(\"ID\", Date.class)");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Got expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception:" + e);
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2) {
      throw new Exception("tupleGetStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: tupleGetStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:430
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleGetStringClassTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = true;
    boolean pass3 = false;

    List<Integer> expected = new ArrayList<Integer>();
    for (Customer c : customerRef) {
      expected.add(Integer.valueOf(c.getId()));
    }
    Collections.sort(expected);

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      cquery.multiselect(
          customer.get(Customer_.getSingularAttribute("id", String.class))
              .alias("ID"),
          customer.get(Customer_.getSingularAttribute("name", String.class))
              .alias("NAME"));

      TypedQuery<Tuple> tq = getEntityManager().createQuery(cquery);

      List<Tuple> result = tq.getResultList();

      List<Integer> actual = new ArrayList<Integer>();

      pass1 = true;
      for (Tuple t : result) {
        Integer id = Integer.valueOf((String) t.get(0));
        String name = (String) t.get(1);
        if (name != null) {
          if (customerRef[id - 1].getName().equals(name)) {
            actual.add(id);
          } else {
            TestUtil.logErr("Expected name:|" + customerRef[id - 1].getName()
                + "|, actual:|" + name + "|");
            pass2 = false;
          }
        } else {
          if (customerRef[id - 1].getName() == null) {
            actual.add(id);
          } else {
            TestUtil.logErr("Expected name:" + customerRef[id - 1].getName()
                + ", actual:null");
            pass2 = false;
          }
        }

      }
      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass3 = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass1 || !pass2 || !pass3) {
      throw new Exception("tupleGetStringClassTest failed");
    }
  }

  /*
   * @testName: tupleElementGetAliasTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:436; PERSISTENCE:JAVADOC:1102;
   * PERSISTENCE:JAVADOC:1103
   *
   * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
   * c.name from Customer c
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void tupleElementGetAliasTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();
      TestUtil.logTrace("Use Tuple Query");

      Path<String> idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      String id = idPath.alias("IDID").getAlias();
      if (id.equals("IDID")) {
        TestUtil.logTrace("id=" + id);
        pass1 = true;
      } else {
        TestUtil.logErr("Expected id value:IDID, actual value:" + id);
      }
      Bindable b = idPath.getModel();
      Bindable.BindableType bbt = b.getBindableType();
      if (bbt.equals(Bindable.BindableType.SINGULAR_ATTRIBUTE)) {
        TestUtil.logTrace("Received expected model:" + bbt.name());
        pass2 = true;

      } else {
        TestUtil.logErr(
            "Expected model:" + Bindable.BindableType.SINGULAR_ATTRIBUTE.name()
                + ", actual:" + bbt.name());
      }

      Path p = idPath.getParentPath();
      Class parent = p.getJavaType();
      if (parent.getName().equals(Customer.class.getName())) {
        TestUtil.logTrace("Received expected parent:" + parent.getName());
        pass3 = true;

      } else {
        TestUtil.logErr("Expected parent class:" + Customer.class.getName()
            + ", actual:" + parent.getName());
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass1 || !pass2 || !pass3) {
      throw new Exception("tupleElementGetAliasTest failed");
    }
  }



  /*
   * @testName: isEmpty
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:781
   *
   * @test_Strategy: Select c fRoM Customer c where c.aliases IS EMPTY
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void isEmpty() throws Exception {
    boolean pass = false;

    String[] expected = new String[7];
    expected[0] = customerRef[5].getId();
    expected[1] = customerRef[14].getId();
    expected[2] = customerRef[15].getId();
    expected[3] = customerRef[16].getId();
    expected[4] = customerRef[17].getId();
    expected[5] = customerRef[18].getId();
    expected[6] = customerRef[19].getId();

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {

      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isEmpty(customer.<Set<String>> get("aliases")));

      TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
      List<Customer> result = tq.getResultList();

      List<Integer> actual = new ArrayList<Integer>();
      for (Customer c : result) {
        actual.add(Integer.parseInt(c.getId()));
      }
      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isEmpty test failed");
    }
  }

  /*
   * @testName: isNotEmpty
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:785
   *
   * @test_Strategy: Select Distinct c fRoM Customer c where c.aliases IS NOT
   * EMPTY
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void isNotEmpty() throws Exception {
    boolean pass = false;

    int j = 0;
    String[] expected = new String[13];
    for (int i = 0; i < 14; i++) {
      if (i != 5) {
        expected[j++] = customerRef[i].getId();
      }
    }

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {

      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isNotEmpty(customer.<Set<String>> get("aliases")));
      cquery.select(customer).distinct(true);

      TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
      List<Customer> result = tq.getResultList();

      List<Integer> actual = new ArrayList<Integer>();
      for (Customer c : result) {
        actual.add(Integer.parseInt(c.getId()));
      }
      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isNotEmpty test failed");
    }
  }

  /*
   * @testName: sizeCollectionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:846; PERSISTENCE:SPEC:1742;
   *
   * @test_Strategy: Select size(c.aliases) from Customer c where c.id ="3"
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void sizeCollectionTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
    if (cquery != null) {

      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      cquery.select(cbuilder.size(customer.<Collection<Alias>> get("aliases")));
      cquery.where(cbuilder.equal(customer.get("id"), "3"));

      TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
      Integer result = tq.getSingleResult();
      Integer expectedSize = 2;

      if (result.intValue() == expectedSize.intValue()) {
        TestUtil.logTrace("Successfully returned expected results");
        pass = true;
      } else {
        TestUtil.logErr("test returned:" + result.intValue() + "expected: "
            + expectedSize.intValue());
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("sizeCollectionTest failed");
    }
  }

  /*
   * @testName: sizeExpTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:845
   *
   * @test_Strategy: Select size(c.aliases) fRoM Customer c where c.id ="3"
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void sizeExpTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
    if (cquery != null) {

      Root<Customer> customer = cquery.from(Customer.class);

      Expression<Collection<Alias>> aliases = customer
          .<Collection<Alias>> get("aliases");

      // Get Metamodel from Root
      cquery.select(cbuilder.size(aliases));
      cquery.where(cbuilder.equal(customer.get("id"), "3"));

      TypedQuery<Integer> tq = getEntityManager().createQuery(cquery);
      Integer result = tq.getSingleResult();
      Integer expectedSize = 2;

      if (result.intValue() == expectedSize.intValue()) {
        TestUtil.logTrace(
            "Successfully returned expected results:" + result.intValue());
        pass = true;
      } else {
        TestUtil.logErr("Expected: " + expectedSize.intValue() + ", actual:"
            + result.intValue());
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("sizeExpTest failed");
    }
  }

  /*
   * @testName: isMember
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:784
   *
   * @test_Strategy: Select c FROM Customer c WHERE "aef" MEMBER OF c.aliases
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void isMember() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Alias> cqa = cbuilder.createQuery(Alias.class);
    Root<Alias> aliasRoot = cqa.from(Alias.class);
    cqa = cqa.where(cbuilder.equal(aliasRoot.get("alias"), "aef"));
    Alias alias = getEntityManager().createQuery(cqa).getSingleResult();

    getEntityTransaction().begin();
    CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);
      cquery.where(cbuilder.isMember(alias,
          customer.<Collection<Alias>> get("aliases")));

      TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);

      List<Customer> result = tq.getResultList();
      if (result.size() == 1) {
        if (result.get(0).equals(customerRef[0])) {
          TestUtil.logTrace(
              "Successfully returned expected results" + result.toString());
          pass = true;
        } else {
          TestUtil.logErr("expected customer:" + customerRef[0].toString());
          TestUtil.logErr("actual customer:" + result.toString());
        }
      } else {
        TestUtil
            .logErr("Expected number customers: 1, actual:" + result.size());
        for (Customer c : result) {
          TestUtil.logErr("Received customer:" + c.toString());
        }
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isMember test failed");

    }
  }

  /*
   * @testName: isNotMember
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:786
   *
   * @test_Strategy: Select Distinct a FROM Alias a WHERE a.customerNoop NOT
   * MEMBER OF a.customersNoop
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void isNotMember() throws Exception {
    boolean pass = false;

    String[] expected = new String[30];
    for (int i = 0; i < 30; i++) {
      expected[i] = aliasRef[i].getId();
    }

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
    if (cquery != null) {
      Root<Alias> alias = cquery.from(Alias.class);

      // Get Metamodel from Root
      cquery.where(cbuilder.isNotMember(alias.<Customer> get("customerNoop"),
          alias.<Collection<Customer>> get("customersNoop")));

      cquery.distinct(true);

      TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

      List<Alias> result = tq.getResultList();

      List<Integer> actual = new ArrayList<Integer>();
      for (Alias a : result) {
        actual.add(Integer.parseInt(a.getId()));
      }
      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.length + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("isNotMember test failed");

    }

  }

  /*
   * @testName: expressionAliasTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:958;
   *
   * @test_Strategy:
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void expressionAliasTest() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

    CriteriaQuery cquery = cbuilder.createQuery();
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      // Get Metamodel from Root
      EntityType<Customer> Customer_ = customer.getModel();

      Expression idPath = customer
          .get(Customer_.getSingularAttribute("id", String.class));
      String id = idPath.alias("IDID").getAlias();
      if (id.equals("IDID")) {
        TestUtil.logTrace("id=" + id);
        pass = true;
      } else {
        TestUtil.logErr("Expected id value:IDID, actual value:" + id);
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    if (!pass) {
      throw new Exception("expressionAliasTest failed");
    }
  }

}
