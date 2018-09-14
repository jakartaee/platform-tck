/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.entityManagerFactory;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

public class Client extends PMClientBase {

  Properties props = null;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setupNoData(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setupNoData");
    this.props = p;
    try {
      super.setup(args, p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createOrderTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void setupMember(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      removeTestData();
      createMemberTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanupNoData() throws Fault {
    super.cleanup();
  }

  public void cleanup() throws Fault {
    removeTestData();
    TestUtil.logTrace("done cleanup, calling super.cleanup");
    super.cleanup();
  }

  /*
   * @testName: getMetamodelTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:340;
   * 
   * @test_Strategy: Get a MetaModel Object from the EntityManagerFactory an
   * make sure it is not null
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoData")
  public void getMetamodelTest() throws Fault {
    boolean pass = false;
    try {
      Metamodel mm = getEntityManager().getEntityManagerFactory()
          .getMetamodel();
      if (mm == null) {
        TestUtil.logErr("getMetamodel() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getMetamodelTest failed");
    }
  }

  /*
   * @testName: getPersistenceUnitUtil
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:341;
   * 
   * @test_Strategy: Get a PersistenceUnitUtil Object from the
   * EntityManagerFactory an make sure it is not null
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoData")
  public void getPersistenceUnitUtil() throws Fault {
    boolean pass = false;
    try {
      PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory()
          .getPersistenceUnitUtil();
      if (puu == null) {
        TestUtil.logErr("getPersistenceUnitUtil() returned a null result");
      } else {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getPersistenceUnitUtil failed");
    }
  }

  /*
   * @testName: getCriteriaBuilderTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:339; PERSISTENCE:SPEC:1702;
   *
   * @test_Strategy: access EntityManagerFactory.getCriteriaBuilder and verify
   * it can be used to create a query
   *
   */
  @SetupMethod(name = "setupNoData")
  @CleanupMethod(name = "cleanupNoData")
  public void getCriteriaBuilderTest() throws Fault {
    boolean pass = false;
    try {
      CriteriaBuilder cbuilder = getEntityManager().getEntityManagerFactory()
          .getCriteriaBuilder();
      if (cbuilder != null) {
        getEntityTransaction().begin();
        CriteriaQuery<Object> cquery = cbuilder.createQuery();
        if (cquery != null) {
          TestUtil.logTrace("Obtained Non-null Criteria Query");
          pass = true;
        } else {
          TestUtil.logErr("Failed to get Non-null Criteria Query");
        }

        getEntityTransaction().commit();
      } else {
        TestUtil.logErr("getCriteriaBuilder() returned null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass) {
      throw new Fault("getCriteriaBuilderTest failed");
    }
  }

  /*
   * @testName: addNamedQueryMaxResultTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
   * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
   * 
   * @test_Strategy: Test that max result of addNamedQuery is retained or can be
   * overridden
   */
  public void addNamedQueryMaxResultTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    boolean pass7 = false;
    try {
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      TestUtil.logTrace("Defining queries");
      Query nativeQuery = getEntityManager().createNativeQuery(
          "Select o.ID from PURCHASE_ORDER o ORDER BY o.ID ASC");
      nativeQuery.setMaxResults(1);
      getEntityManagerFactory().addNamedQuery("native_query", nativeQuery);

      Query query = getEntityManager()
          .createQuery("Select o.id from Order o ORDER BY o.id ASC");
      query.setMaxResults(1);
      getEntityManagerFactory().addNamedQuery("query", query);

      CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order.get(Order_.id));
      cquery.orderBy(cbuilder.asc(order.get("id")));
      TypedQuery<Integer> typedQuery = getEntityManager().createQuery(cquery);
      typedQuery.setMaxResults(1);
      getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing native query with different max result than the original");
        Query namedQuery = getEntityManager().createNamedQuery("native_query");
        boolean configOK = true;
        if (namedQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }
        namedQuery.setMaxResults(2);
        if (namedQuery.getMaxResults() == 2) {
          TestUtil.logTrace("Received expected Max Result after change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result after change:2, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }
        List lResult = namedQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        boolean foundTwo = false;
        if (iList.size() == 2) {
          for (int i : iList) {
            if (i == 1) {
              foundOne = true;
              TestUtil.logTrace("Found expected id:1");
            } else if (i == 2) {
              foundTwo = true;
              TestUtil.logTrace("Found expected id:2");
            } else {
              TestUtil.logErr("Received unexpected result:" + i);
            }

          }
        } else {
          TestUtil.logErr(
              "Did not get expected number of results, expected:2, actual:"
                  + iList.size());
          for (Integer i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && foundTwo && configOK) {
          pass1 = true;
        }

        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing native query verify original max result is still active");
        Query namedQuery = getEntityManager().createNamedQuery("native_query");
        boolean configOK = true;
        if (namedQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }

        List lResult = namedQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        if (iList.size() == 1) {
          int result = iList.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected Order");
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + iList.size());
          for (int i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass2 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing query with different max result than the original");

        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean configOK = true;
        if (namedQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }
        namedQuery.setMaxResults(2);
        if (namedQuery.getMaxResults() == 2) {
          TestUtil.logTrace("Received expected Max Result after change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result after change:2, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }
        List lResult = namedQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        boolean foundTwo = false;
        if (iList.size() == 2) {
          for (int i : iList) {
            if (i == 1) {
              foundOne = true;
              TestUtil.logTrace("Found expected id:1");
            } else if (i == 2) {
              foundTwo = true;
              TestUtil.logTrace("Found expected id:2");
            } else {
              TestUtil.logErr("Received unexpected result:" + i);
            }

          }
        } else {
          TestUtil.logErr(
              "Did not get expected number of results, expected:2, actual:"
                  + iList.size());
          for (Integer i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && foundTwo && configOK) {
          pass3 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query verify original max result is active");
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean configOK = true;
        if (namedQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }

        List lResult = namedQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        if (iList.size() == 1) {
          int result = iList.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + iList.size());
          for (int i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass4 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query definition can be replaced ");
        Query query2 = getEntityManager()
            .createQuery("Select o.id from Order o where o.id in (1,2) ");
        query2.setMaxResults(2);
        getEntityManagerFactory().addNamedQuery("query", query2);
        Query namedQuery = getEntityManager().createNamedQuery("query");

        boolean configOK = true;
        if (namedQuery.getMaxResults() == 2) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:2, actual:"
              + namedQuery.getMaxResults());
          configOK = false;
        }
        List lResult = namedQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }

        List<Integer> lExpected = new ArrayList<Integer>();
        lExpected.add(1);
        lExpected.add(2);
        if (iList.containsAll(lExpected) && (lExpected.containsAll(iList))
            && iList.size() == lExpected.size()) {
          TestUtil.logTrace("Received expected ids");
          if (configOK) {
            pass5 = true;
          }
        } else {
          TestUtil.logErr("Did not receive expected results:");
          for (Integer i : lExpected) {
            TestUtil.logErr("Expected:" + i);
          }
          for (Integer i : iList) {
            TestUtil.logErr("Expected:" + i);
          }
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing TypedQuery with max result different than the original");
        TypedQuery<Integer> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Integer.class);
        boolean configOK = true;
        if (namedTypeQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedTypeQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedTypeQuery.getMaxResults());
          configOK = false;
        }
        namedTypeQuery.setMaxResults(2);
        if (namedTypeQuery.getMaxResults() == 2) {
          TestUtil.logTrace("Received expected Max Result after change:"
              + namedTypeQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result after change:2, actual:"
              + namedTypeQuery.getMaxResults());
          configOK = false;
        }
        List<Integer> lResult = namedTypeQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        boolean foundTwo = false;
        if (iList.size() == 2) {
          for (int i : iList) {
            if (i == 1) {
              foundOne = true;
              TestUtil.logTrace("Found expected id:1");
            } else if (i == 2) {
              foundTwo = true;
              TestUtil.logTrace("Found expected id:2");
            } else {
              TestUtil.logErr("Received unexpected result:" + i);
            }

          }
        } else {
          TestUtil.logErr(
              "Did not get expected number of results, expected:2, actual:"
                  + iList.size());
          for (Integer i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && foundTwo && configOK) {
          pass6 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil
            .logMsg("Testing TypedQuery verify original max result is active");
        TypedQuery<Integer> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Integer.class);
        boolean configOK = true;
        if (namedTypeQuery.getMaxResults() == 1) {
          TestUtil.logTrace("Received expected Max Result before change:"
              + namedTypeQuery.getMaxResults());
        } else {
          TestUtil.logErr("Expected Max Result before change:1, actual:"
              + namedTypeQuery.getMaxResults());
          configOK = false;
        }

        List<Integer> lResult = namedTypeQuery.getResultList();
        List<Integer> iList = new ArrayList<Integer>();
        for (Object o : lResult) {
          iList.add(convertToInt(o));
        }
        boolean foundOne = false;
        if (iList.size() == 1) {
          int result = iList.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + iList.size());
          for (int i : iList) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass7 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7) {
      throw new Fault("addNamedQueryMaxResultTest failed");
    }
  }

  /*
   * @testName: addNamedQueryFlushModeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
   * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
   * 
   * @test_Strategy: Test that flush mode of addNamedQuery is retained or can be
   * overridden
   */

  public void addNamedQueryFlushModeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;
    boolean pass6 = false;
    boolean pass7 = false;
    try {
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      TestUtil.logTrace("Defining queries");
      Query nativeQuery = getEntityManager()
          .createNativeQuery("Select ID from PURCHASE_ORDER where ID=1");
      nativeQuery.setFlushMode(FlushModeType.AUTO);
      getEntityManagerFactory().addNamedQuery("native_query", nativeQuery);

      Query query = getEntityManager()
          .createQuery("select o.id from Order o where o.id=1");
      query.setFlushMode(FlushModeType.AUTO);
      getEntityManagerFactory().addNamedQuery("query", query);

      CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
      Root<Order> order = cquery.from(Order.class);
      cquery.select(order.get(Order_.id));
      cquery.where(cbuilder.equal(order.get(Order_.id), 1));
      TypedQuery<Integer> typedQuery = getEntityManager().createQuery(cquery);
      typedQuery.setFlushMode(FlushModeType.AUTO);
      getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing native query with different flush mode than the original");
        Query namedQuery = getEntityManager().createNamedQuery("native_query");
        boolean configOK = true;
        if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr("Expected flush mode before change:"
              + FlushModeType.AUTO + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }
        namedQuery.setFlushMode(FlushModeType.COMMIT);
        if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Received expected flush mode after change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr("Expected flush mode after change:"
              + FlushModeType.AUTO + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }
        List<Integer> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = convertToInt(lResult.get(0));
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass1 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing native query verify original flush mode is still active");
        Query namedQuery = getEntityManager().createNamedQuery("native_query");
        boolean configOK = true;
        if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr(
              "Expected flush mode before change:" + FlushModeType.AUTO.name()
                  + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }

        List<Integer> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = convertToInt(lResult.get(0));
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass2 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing query with different flush mode than the original");
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean configOK = true;
        if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr("Expected flush mode before change:"
              + FlushModeType.AUTO + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }
        namedQuery.setFlushMode(FlushModeType.COMMIT);
        if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Received expected flush mode after change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr("Expected flush mode after change:"
              + FlushModeType.AUTO + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }
        List<Integer> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = lResult.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass3 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query verify original flush mode is active");
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean configOK = true;
        if (namedQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr(
              "Expected flush mode before change:" + FlushModeType.AUTO.name()
                  + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }

        List<Integer> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = lResult.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass4 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query definition can be replaced ");
        Query query2 = getEntityManager()
            .createQuery("Select o.id from Order o where o.id = 2")
            .setFlushMode(FlushModeType.COMMIT);
        getEntityManagerFactory().addNamedQuery("query", query2);
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean configOK = true;
        if (namedQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedQuery.getFlushMode());
        } else {
          TestUtil.logErr(
              "Expected flush mode before change:" + FlushModeType.COMMIT.name()
                  + ", actual:" + namedQuery.getFlushMode());
          configOK = false;
        }

        List<Integer> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = lResult.get(0);
          if (result == 2) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:2, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass5 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing TypedQuery with flush mode different than the original");
        TypedQuery<Integer> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Integer.class);
        boolean configOK = true;
        if (namedTypeQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedTypeQuery.getFlushMode());
        } else {
          TestUtil
              .logErr("Expected flush mode before change:" + FlushModeType.AUTO
                  + ", actual:" + namedTypeQuery.getFlushMode());
          configOK = false;
        }
        namedTypeQuery.setFlushMode(FlushModeType.COMMIT);
        if (namedTypeQuery.getFlushMode().equals(FlushModeType.COMMIT)) {
          TestUtil.logTrace("Received expected flush mode after change:"
              + namedTypeQuery.getFlushMode());
        } else {
          TestUtil
              .logErr("Expected flush mode after change:" + FlushModeType.AUTO
                  + ", actual:" + namedTypeQuery.getFlushMode());
          configOK = false;
        }
        List<Integer> lResult = namedTypeQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = lResult.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass6 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil
            .logMsg("Testing TypedQuery verify original flush mode is active");
        TypedQuery<Integer> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Integer.class);
        boolean configOK = true;
        if (namedTypeQuery.getFlushMode().equals(FlushModeType.AUTO)) {
          TestUtil.logTrace("Received expected flush mode before change:"
              + namedTypeQuery.getFlushMode());
        } else {
          TestUtil.logErr(
              "Expected flush mode before change:" + FlushModeType.AUTO.name()
                  + ", actual:" + namedTypeQuery.getFlushMode());
          configOK = false;
        }

        List<Integer> lResult = namedTypeQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          int result = lResult.get(0);
          if (result == 1) {
            TestUtil.logTrace("Received expected id:" + result);
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result);
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (int i : lResult) {
            TestUtil.logErr("Ids received:" + i);
          }
        }
        if (foundOne && configOK) {
          pass7 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5 || !pass6 || !pass7) {
      throw new Fault("addNamedQueryFlushModeTest failed");
    }
  }

  /*
   * @testName: addNamedQueryLockModeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1527; PERSISTENCE:SPEC:1311;
   * PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.2;
   * 
   * @test_Strategy: Test that lock mode of addNamedQuery is retained or can be
   * overridden
   */
  @SetupMethod(name = "setupMember")
  public void addNamedQueryLockModeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    boolean pass5 = false;

    try {
      CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
      TestUtil.logTrace("Defining queries");
      Query query = getEntityManager()
          .createQuery("select m from Member m where m.memberId=1");
      query.setLockMode(LockModeType.NONE);
      getEntityManagerFactory().addNamedQuery("query", query);

      CriteriaQuery<Member> cquery = cbuilder.createQuery(Member.class);
      Root<Member> member = cquery.from(Member.class);
      cquery.select(member);
      cquery.where(cbuilder.equal(member.get(Member_.memberId), 1));
      TypedQuery<Member> typedQuery = getEntityManager().createQuery(cquery);
      typedQuery.setLockMode(LockModeType.NONE);
      getEntityManagerFactory().addNamedQuery("typed_query", typedQuery);

      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil
            .logMsg("Testing query with different lock mode than the original");
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean ok1 = false;
        LockModeType lmt = namedQuery.getLockMode();
        if (lmt != null) {
          if (lmt.equals(LockModeType.NONE)) {
            TestUtil.logTrace(
                "Received expected lock mode before change:" + lmt.name());
            ok1 = true;
          } else {
            TestUtil.logErr("Expected lock mode before change:"
                + LockModeType.NONE.name() + ", actual:" + lmt.name());
          }
        } else {
          TestUtil.logErr("getLockModeType returned null");
        }
        namedQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
        lmt = namedQuery.getLockMode();
        boolean ok2 = false;
        if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
          TestUtil.logTrace("Received LockModeType:" + lmt.name());
          ok2 = true;
        } else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
          TestUtil.logTrace("Received LockModeType:" + lmt + " inplace of "
              + LockModeType.PESSIMISTIC_READ.name());
          ok2 = true;
        } else {
          TestUtil.logErr("Expected lock mode after change:"
              + LockModeType.PESSIMISTIC_READ.name() + ", actual:"
              + lmt.name());
        }
        List<Member> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          Member result = lResult.get(0);
          if (result.getMemberId() == 1) {
            TestUtil.logTrace("Received expected id:" + result.getMemberId());
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result.getMemberId());
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (Member m : lResult) {
            TestUtil.logErr("Ids received:" + m.getMemberId());
          }
        }
        if (foundOne && ok1 && ok2) {
          pass1 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      } finally {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query verify original lock mode is active");
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean ok1 = false;
        LockModeType lmt = namedQuery.getLockMode();
        if (lmt != null) {
          if (lmt.equals(LockModeType.NONE)) {
            TestUtil.logTrace(
                "Received expected lock mode before change:" + lmt.name());
            ok1 = true;
          } else {
            TestUtil.logErr("Expected lock mode before change:"
                + LockModeType.NONE.name() + ", actual:" + lmt.name());
          }
        } else {
          TestUtil.logErr("getLockModeType returned null");
        }

        List<Member> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          Member result = lResult.get(0);
          if (result.getMemberId() == 1) {
            TestUtil.logTrace("Received expected id:" + result.getMemberId());
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result.getMemberId());
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (Member m : lResult) {
            TestUtil.logErr("Ids received:" + m.getMemberId());
          }
        }
        if (foundOne && ok1) {
          pass2 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      } finally {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg("Testing query definition can be replaced ");
        Query query2 = getEntityManager()
            .createQuery("select m from Member m where m.memberId=2")
            .setLockMode(LockModeType.PESSIMISTIC_READ);
        getEntityManagerFactory().addNamedQuery("query", query2);
        Query namedQuery = getEntityManager().createNamedQuery("query");
        boolean ok1 = false;
        LockModeType lmt = namedQuery.getLockMode();
        if (lmt != null) {
          if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
            TestUtil.logTrace("Received LockModeType:" + lmt.name());
            ok1 = true;
          } else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
            TestUtil.logTrace("Received LockModeType:" + lmt + " inplace of "
                + LockModeType.PESSIMISTIC_READ.name());
            ok1 = true;
          } else {
            TestUtil.logErr("Expected lock mode after change:"
                + LockModeType.PESSIMISTIC_READ.name() + ", actual:"
                + lmt.name());
          }
        } else {
          TestUtil.logErr("getLockModeType returned null");
        }
        List<Member> lResult = namedQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          Member result = lResult.get(0);
          if (result.getMemberId() == 2) {
            TestUtil.logTrace("Received expected id:" + result.getMemberId());
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:2, actual:" + result.getMemberId());
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:2, actual:"
                  + lResult.size());
          for (Member m : lResult) {
            TestUtil.logErr("Ids received:" + m.getMemberId());
          }
        }
        if (foundOne && ok1) {
          pass3 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      } finally {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil.logMsg(
            "Testing TypedQuery with lock mode different than the original");
        TypedQuery<Member> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Member.class);
        boolean ok1 = false;
        LockModeType lmt = namedTypeQuery.getLockMode();
        if (lmt != null) {
          if (lmt.equals(LockModeType.NONE)) {
            TestUtil.logTrace(
                "Received expected lock mode before change:" + lmt.name());
            ok1 = true;
          } else {
            TestUtil.logErr("Expected lock mode before change:"
                + LockModeType.NONE.name() + ", actual:" + lmt.name());
          }
        } else {
          TestUtil.logErr("getLockModeType returned null");
        }
        namedTypeQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
        lmt = namedTypeQuery.getLockMode();
        boolean ok2 = false;
        if (lmt.equals(LockModeType.PESSIMISTIC_READ)) {
          TestUtil.logTrace(
              "Received expected lock mode after change:" + lmt.name());
          ok2 = true;
        } else if (lmt.equals(LockModeType.PESSIMISTIC_WRITE)) {
          TestUtil.logTrace("Received LockModeType:" + lmt + " inplace of "
              + LockModeType.PESSIMISTIC_READ.name());
          ok2 = true;
        } else {
          TestUtil.logErr("Expected lock mode after change:"
              + LockModeType.PESSIMISTIC_READ.name() + ", actual:"
              + lmt.name());
        }

        List<Member> lResult = namedTypeQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          Member result = lResult.get(0);
          if (result.getMemberId() == 1) {
            TestUtil.logTrace("Received expected id:" + result.getMemberId());
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result.getMemberId());
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (Member m : lResult) {
            TestUtil.logErr("Ids received:" + m.getMemberId());
          }
        }
        if (foundOne && ok1 && ok2) {
          pass4 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      } finally {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
      try {
        getEntityTransaction().begin();
        TestUtil.logMsg("*********************************");
        TestUtil
            .logMsg("Testing TypedQuery verify original lock mode is active");
        TypedQuery<Member> namedTypeQuery = getEntityManager()
            .createNamedQuery("typed_query", Member.class);
        boolean ok1 = false;
        LockModeType lmt = namedTypeQuery.getLockMode();
        if (lmt != null) {
          if (lmt.equals(LockModeType.NONE)) {
            TestUtil.logTrace(
                "Received expected lock mode before change:" + lmt.name());
            ok1 = true;
          } else {
            TestUtil.logErr("Expected lock mode before change:"
                + LockModeType.NONE.name() + ", actual:" + lmt.name());
          }
        } else {
          TestUtil.logErr("getLockModeType returned null");
        }
        List<Member> lResult = namedTypeQuery.getResultList();
        boolean foundOne = false;
        if (lResult.size() == 1) {
          Member result = lResult.get(0);
          if (result.getMemberId() == 1) {
            TestUtil.logTrace("Received expected id:" + result.getMemberId());
            foundOne = true;
          } else {
            TestUtil.logErr("Expected id:1, actual:" + result.getMemberId());
          }
        } else {
          TestUtil.logErr(
              "Did not get correct number of results, expected:1, actual:"
                  + lResult.size());
          for (Member m : lResult) {
            TestUtil.logErr("Ids received:" + m.getMemberId());
          }
        }
        if (foundOne && ok1) {
          pass5 = true;
        }
        getEntityTransaction().commit();

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      } finally {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
      throw new Fault("addNamedQueryLockModeTest failed");
    }
  }

  private void createOrderTestData() {

    try {
      getEntityTransaction().begin();
      Order[] orders = new Order[5];
      orders[0] = new Order(1, 111);
      orders[1] = new Order(2, 222);
      orders[2] = new Order(3, 333);
      orders[3] = new Order(4, 444);
      orders[4] = new Order(5, 555);

      for (Order o : orders) {
        TestUtil.logTrace("Persisting order:" + o.toString());
        getEntityManager().persist(o);
      }
      getEntityManager().flush();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
  }

  private void createMemberTestData() {

    try {
      getEntityTransaction().begin();

      Member[] members = new Member[5];
      members[0] = new Member(1, "1");
      members[1] = new Member(2, "2");
      members[2] = new Member(3, "3");
      members[3] = new Member(4, "4");
      members[4] = new Member(5, "5");

      for (Member m : members) {
        TestUtil.logTrace("Persisting member:" + m.toString());
        getEntityManager().persist(m);
      }
      getEntityManager().flush();

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      clearCache();
      getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM MEMBER")
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
