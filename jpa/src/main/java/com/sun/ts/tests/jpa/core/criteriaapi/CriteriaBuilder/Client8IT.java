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
import com.sun.ts.tests.jpa.common.schema30.UtilTrimData;

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

public class Client8IT extends UtilTrimData {


  /*
   * @testName: trimExpTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:866
   *
   * @test_Strategy: Select trim(both from t.name) from Trim t where
   * trim(t.name)='David R. Vincent'
   *
   *
   */
  @SetupMethod(name = "setupTrimData")
  @Test
  public void trimExpTest() throws Exception {
    boolean pass = false;
    final String expected = " David R. Vincent ";
    final String expected2 = "David R. Vincent";

    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();
    /*
     * Trim tTrim = getEntityManager().find(Trim.class, "19");
     * TestUtil.logTrace("Trim(19):" + tTrim.toString()); if
     * (tTrim.getName().equals(expected)) {
     * TestUtil.logTrace("Received expected find result: " + tTrim.getName());
     * pass1 = true; } else {
     * TestUtil.logErr("Name returned by find does not match expected");
     * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName()
     * + "|"); }
     * 
     */
    CriteriaQuery<String> cquery = cb.createQuery(String.class);
    if (cquery != null) {
      Root<Trim> trim = cquery.from(Trim.class);

      // Get Metamodel from Root
      EntityType<Trim> trim_ = trim.getModel();

      cquery.where(cb.equal(
          cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
          cb.literal(expected.trim())));
      cquery.select(
          cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))));

      TypedQuery<String> tq = getEntityManager().createQuery(cquery);

      String result = tq.getSingleResult();

      if (result.equals(expected2)) {
        TestUtil.logTrace("Received expected result:|" + result + "|");
        pass = true;
      } else {
        TestUtil.logErr("Mismatch in received results - expected = |"
            + expected2 + "|, received = |" + result + "|");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("trimExpTest failed");

    }
  }

  /*
   * testName: trimLeadingExpTest assertion_ids: PERSISTENCE:JAVADOC:867
   *
   * test_Strategy: Select trim(leading from t.name) from Trim t where t.name= '
   * David R. Vincent '
   *
   *
   */
  /*
   * @SetupMethod(name = "setupTrimData") // TODO - once TRIM issues are
   * resolved, re-enable this test public void trimLeadingExpTest() throws Exception
   * { boolean pass = false; final String expected = " David R. Vincent "; final
   * String expected2 = "David R. Vincent             ";
   * 
   * CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
   * 
   * 
   * getEntityTransaction().begin();
   * 
   * Trim tTrim = getEntityManager().find(Trim.class, "19");
   * TestUtil.logTrace("Trim(19):" + tTrim.toString()); if
   * (!tTrim.getName().equals(expected)) {
   * TestUtil.logErr("Name returned by find does not match expected");
   * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName() +
   * "|"); }
   * 
   * 
   * CriteriaQuery<String> cquery = cb.createQuery(String.class); if (cquery !=
   * null) { Root<Trim> trim = cquery.from(Trim.class);
   * 
   * 
   * //Get Metamodel from Root EntityType<Trim> trim_ = trim.getModel();
   * 
   * cquery.where(cb.equal( trim.get(trim_.getSingularAttribute("name",
   * String.class)), cb.literal(expected)));
   * cquery.select(cb.trim(Trimspec.LEADING,
   * trim.get(trim_.getSingularAttribute("name", String.class))));
   * 
   * TypedQuery<String> tq = getEntityManager().createQuery(cquery);
   * 
   * String result = tq.getSingleResult();
   * 
   * if (result.equals(expected2)) {
   * TestUtil.logTrace("Received expected result:|" + result + "|"); pass =
   * true; } else {
   * TestUtil.logErr("Mismatch in received results - expected = |" + expected2 +
   * "|, received = |" + result + "|"); }
   * 
   * } else { TestUtil.logErr("Failed to get Non-null Criteria Query"); }
   * 
   * getEntityTransaction().commit();
   * 
   * if (!pass) { throw new Exception("trimLeadingExpTest failed");
   * 
   * } }
   */

  /*
   * @testName: trimTrailingCharExpTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:867
   *
   * @test_Strategy: Select trim(trailing from t.name) from Trim t where
   * trim(t.name)= 'David R. Vincent'
   *
   */
  @SetupMethod(name = "setupTrimData")
  @Test
  public void trimTrailingCharExpTest() throws Exception {
    boolean pass = false;
    final String expected = " David R. Vincent ";
    final String expected2 = " David R. Vincent";

    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

    getEntityTransaction().begin();

    /*
     * Trim tTrim = getEntityManager().find(Trim.class, "19");
     * TestUtil.logTrace("Trim(19):" + tTrim.toString()); if
     * (tTrim.getName().equals(expected)) {
     * TestUtil.logTrace("Received expected find result: " + tTrim.getName());
     * pass1 = true; } else {
     * TestUtil.logErr("Name returned by find does not match expected");
     * TestUtil.logErr("Expected:|" + expected + "|, actual:|" + tTrim.getName()
     * + "|"); }
     */

    CriteriaQuery<String> cquery = cb.createQuery(String.class);
    if (cquery != null) {
      Root<Trim> trim = cquery.from(Trim.class);

      // Get Metamodel from Root
      EntityType<Trim> trim_ = trim.getModel();

      cquery.where(cb.equal(
          cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
          cb.literal(expected.trim())));
      cquery.select(cb.trim(Trimspec.TRAILING,
          trim.get(trim_.getSingularAttribute("name", String.class))));

      TypedQuery<String> tq = getEntityManager().createQuery(cquery);

      String result = tq.getSingleResult();

      if (result.equals(expected2)) {
        TestUtil.logTrace("Received expected result:|" + result + "|");
        pass = true;
      } else {
        TestUtil.logErr("Mismatch in received results - expected = |"
            + expected2 + "|, received = |" + result + "|");
      }

    } else {
      TestUtil.logErr("Failed to get Non-null Criteria Query");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("trimTrailingCharExpTest failed");

    }
  }

}
