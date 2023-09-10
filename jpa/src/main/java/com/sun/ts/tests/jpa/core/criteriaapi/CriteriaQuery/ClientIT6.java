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

public class ClientIT6 extends Util {

  /*
   * @testName: typeTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:1738
   * 
   * @test_Strategy: test path.type()
   *
   * Select p from Product p where TYPE(p) = HardwareProduct
   */
  @SetupMethod(name = "setupProductData")
  @Test
  public void typeTest() throws Exception {
    boolean pass = false;
    List<Integer> expected = new ArrayList<Integer>();
    for (Product p : hardwareRef) {
      expected.add(Integer.valueOf(p.getId()));
    }
    Collections.sort(expected);
    List<Integer> actual = new ArrayList<Integer>();

    CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

    try {
      getEntityTransaction().begin();
      CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
      Root<Product> product = cquery.from(Product.class);
      cquery.where(
          cbuilder.and(cbuilder.equal(product.type(), HardwareProduct.class)));
      cquery.select(product);
      Query q = getEntityManager().createQuery(cquery);

      List<Product> result = q.getResultList();
      for (Product p : result) {
        actual.add(Integer.parseInt(p.getId()));
      }

      Collections.sort(actual);

      if (!checkEntityPK(actual, expected)) {
        TestUtil.logErr("Did not get expected results. Expected "
            + expected.size() + " references, got: " + actual.size());
      } else {
        TestUtil.logTrace("Expected results received");
        pass = true;
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception queryTest47: ", e);
    }

    if (!pass) {
      throw new Exception("typeTest failed");
    }
  }

}
