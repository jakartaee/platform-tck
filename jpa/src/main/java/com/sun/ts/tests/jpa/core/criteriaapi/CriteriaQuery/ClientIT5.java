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

import org.junit.jupiter.api.Test;

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

public class ClientIT5 extends Util {

  public void setupAData(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("setupData");
    try {
      super.setup();
      removeATestData();
      createATestData();
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }


  /*
   * @testName: DoubleOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.1;
   * PERSISTENCE:SPEC:1685;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void DoubleOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.equal(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1234.5;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1d));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.gt(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1234.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.lt(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1235.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / Double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)), 1));
      ParameterExpression<Double> param = cbuilder.parameter(Double.class);
      cquery.where(cbuilder.notEqual(
          a.get(A_.getSingularAttribute("basicBigDouble", Double.class)),
          param));
      double whereValue = 1235.0;
      p = getEntityManager().createQuery(cquery).setParameter(param, whereValue)
          .getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected Double type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1d));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass7 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / double operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();
      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicDouble", Double.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Double) {
        TestUtil.logTrace("Received expected double type");
        pass8 = true;
      } else {
        TestUtil
            .logErr("Result was not of type double:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8)
      throw new Exception("DoubleOperandResultTypeTests failed");
  }

  /*
   * @testName: FloatOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.2;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void FloatOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = pass7 = pass8 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1f));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / Float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1f));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected float type");
        pass7 = true;
      } else {
        TestUtil
            .logErr("Result was not of type float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / float operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .quot(a.get(A_.getSingularAttribute("basicFloat", Float.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Float) {
        TestUtil.logTrace("Received expected Float type");
        pass8 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Float:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7
        || !pass8)
      throw new Exception("FloatOperandResultTypeTests failed");
  }

  /*
   * @testName: BigDecimalOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.3;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void BigDecimalOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3, pass4;
    pass1 = pass2 = pass3 = pass4 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          new BigDecimal(1)));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing / BigDecimal operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.quot(
          a.get(A_.getSingularAttribute("basicBigDecimal", BigDecimal.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();

      if (p instanceof BigDecimal) {
        TestUtil.logTrace("Received expected BigDecimal type");
        pass4 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigDecimal:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    if (!pass1 || !pass2 || !pass3 || !pass4)
      throw new Exception("BigDecimalOperandResultTypeTests failed");
  }

  /*
   * @testName: BigIntegerOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.4;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void BigIntegerOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3;
    pass1 = pass2 = pass3 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass1 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          new BigInteger("1")));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass2 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * BigInteger operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigInteger", BigInteger.class)),
          1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof BigInteger) {
        TestUtil.logTrace("Received expected BigInteger type");
        pass3 = true;
      } else {
        TestUtil.logErr(
            "Result was not of type BigInteger:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3)
      throw new Exception("BigIntegerOperandResultTypeTests failed");
  }

  /*
   * @testName: LongOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.5;
   * 
   * @test_Strategy: Test various operands result in various types
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void LongOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3, pass4, pass5, pass6;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1L));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicBigLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected Long type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    try {
      TestUtil.logMsg("Testing + long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1L));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * long operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicLong", Long.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Long) {
        TestUtil.logTrace("Received expected long type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type long:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Exception("LongOperandResultTypeTests failed");
  }

  /*
   * @testName: ShortOperandResultTypeTests
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1677; PERSISTENCE:SPEC:1677.6;
   * 
   * @test_Strategy: Test various operands of integral type and verify the
   * result of the operation is of type Integer
   *
   */
  @SetupMethod(name = "setupAData")
  @Test
  public void ShortOperandResultTypeTests() throws Exception {
    boolean pass1, pass2, pass3, pass4, pass5, pass6;
    pass1 = pass2 = pass3 = pass4 = pass5 = pass6 = false;
    Object p;
    try {
      TestUtil.logMsg("Testing + Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.sum(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass1 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.diff(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * Short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder.prod(
          a.get(A_.getSingularAttribute("basicBigShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass3 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing + short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .sum(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass4 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing - short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .diff(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass5 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }
    try {
      TestUtil.logMsg("Testing * short operand:");
      getEntityTransaction().begin();
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      CriteriaQuery cquery = cbuilder.createQuery();
      Root<A> a = cquery.from(A.class);
      EntityType<A> A_ = a.getModel();

      cquery.select(cbuilder
          .prod(a.get(A_.getSingularAttribute("basicShort", Short.class)), 1));
      cquery.where(cbuilder
          .equal(a.get(A_.getSingularAttribute("id", String.class)), "9"));
      p = getEntityManager().createQuery(cquery).getSingleResult();
      if (p instanceof Integer) {
        TestUtil.logTrace("Received expected Integer type");
        pass6 = true;
      } else {
        TestUtil
            .logErr("Result was not of type Integer:" + p.getClass().getName());
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6)
      throw new Exception("ShortOperandResultTypeTests failed");
  }

  public void createATestData() {
    try {
      getEntityTransaction().begin();
      final Integer integer = 1234;
      final short basicShort = 12;
      final Short basicBigShort = basicShort;
      final float basicFloat = 12.3f;
      final Float basicBigFloat = basicFloat;
      final long basicLong = 1234l;
      final Long basicBigLong = basicLong;
      final double basicDouble = 1234.5;
      final Double basicBigDouble = basicDouble;
      final char[] charArray = { 'a', 'b', 'c' };
      final Character[] bigCharacterArray = { 'a', 'b', 'c' };
      final byte[] byteArray = "abc".getBytes();
      final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
      final BigInteger bigInteger = new BigInteger("12345");
      final BigDecimal bigDecimal = new BigDecimal(bigInteger);
      final Date date = new Date();
      final long timeInSeconds = date.getTime();
      final Time time = new Time(timeInSeconds);
      final Timestamp timeStamp = new Timestamp(timeInSeconds);
      final Calendar calendar = Calendar.getInstance();

      A aRef = new A("9", null, 9, integer, basicShort, basicBigShort,
          basicFloat, basicBigFloat, basicLong, basicBigLong, basicDouble,
          basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
          bigByteArray, bigInteger, bigDecimal, date, time, timeStamp,
          calendar);

      getEntityManager().persist(aRef);
      getEntityManager().flush();
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }

  private void removeATestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM A_BASIC")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
