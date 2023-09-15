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

public class Client2IT extends Util {

 
  /*
   * @testName: selectIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:931
   *
   * @test_Strategy:
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void selectIllegalArgumentException() throws Exception {
    boolean pass = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
    CriteriaQuery cquery = cbuilder.createQuery();
    if (cquery != null) {
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating select using selection items with the same alias");
      try {
        CompoundSelection<java.lang.Object[]> c = cbuilder.array(
            customer.get("id").alias("SAMEALIAS"),
            customer.get("name").alias("SAMEALIAS"));

        cquery.select(c);

        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    if (!pass) {
      throw new Exception("selectIllegalArgumentException failed");

    }
  }

  /*
   * @testName: multiselectIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:925; PERSISTENCE:JAVADOC:927
   *
   * @test_Strategy: Create a multiselect using selection items with the same
   * alias and verify exception is thrown
   *
   */
  @SetupMethod(name = "setupAliasData")
  @Test
  public void multiselectIllegalArgumentExceptionTest() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    TestUtil.logMsg("Testing multiselect invalid item");
    CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection array of items that do not exist");
      try {
        cquery.multiselect(customer.get("doesnotexist").alias("ALIAS1"),
            customer.get("doesnotexist2").alias("ALIAS2"));
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("Received expected IllegalArgumentException");
        pass1 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }

    TestUtil.logMsg("Testing multiselect selection[]");
    cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection array of items with the same alias");
      Selection[] selection = { customer.get("id").alias("SAMEALIAS"),
          customer.get("name").alias("SAMEALIAS") };

      try {
        cquery.multiselect(selection);
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("received expected IllegalArgumentException");
        pass2 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }
    TestUtil.logMsg("Testing multiselect List");
    cquery = cbuilder.createTupleQuery();
    if (cquery != null) {
      TestUtil.logTrace("Obtained Non-null Criteria Query");
      Root<Customer> customer = cquery.from(Customer.class);

      TestUtil.logTrace(
          "Creating multiselect using selection items with the same alias");
      try {
        List list = new ArrayList();
        list.add(customer.get("id").alias("SAMEALIAS"));
        list.add(customer.get("name").alias("SAMEALIAS"));

        cquery.multiselect(list);
        TestUtil.logErr("Did not thrown IllegalArgumentException");
      } catch (IllegalArgumentException iae) {
        TestUtil.logTrace("received expected IllegalArgumentException");
        pass3 = true;
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected exception", e);
      }
    }
    if (!pass1 || !pass2 || !pass3) {
      throw new Exception("multiselectIllegalArgumentExceptionTest failed");
    }
  }
}
