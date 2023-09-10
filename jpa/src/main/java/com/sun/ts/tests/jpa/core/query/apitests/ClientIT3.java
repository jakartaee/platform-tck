/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.query.apitests;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.Query;

public class ClientIT3 extends PMClientBase {

  private final Employee empRef[] = new Employee[21];

  private final Date d1 = getSQLDate("2000-02-14");

  private final java.util.Date dateId = getUtilDate("2009-01-10");

  final Department deptRef[] = new Department[5];

  private static final DecimalFormat df = new DecimalFormat();

  public ClientIT3() {
  }


 @BeforeEach
  public void setupDataTypes2() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
      createDataTypes2Data();
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Exception("Setup failed:", e);

    }
  }

 @AfterEach
  public void cleanupNoData() throws Exception {
    TestUtil.logTrace("in cleanupNoData");
    super.cleanup();
  }

  /*
   * BEGIN Test Cases
   */

  /*
   * @testName: queryAPITest28
   * 
   * @assertion_ids: PERSISTENCE:SPEC:527;
   * 
   * @test_Strategy: Usage of Time literal in Query
   *
   */
 @Test
  public void queryAPITest28() throws Exception {

    Collection<Time> result;

    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = true;
    java.sql.Time timeValue = getTimeData("10:30:15");
    TestUtil.logTrace("time Value = " + timeValue.toString());

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2: " + dateId);
      DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        TestUtil.logTrace("DataType Entity is not null, setting TimeData ");
        dataTypes2.setTimeData(timeValue);
        pass1 = true;
      } else {
        TestUtil.logErr("Null returned during initial find");
      }

      getEntityManager().merge(dataTypes2);
      doFlush();
      clearCache();

      TestUtil.logTrace("Make sure update occurred");
      TestUtil.logTrace("FIND D2 again:");
      dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        if (dataTypes2.getTimeData().equals(timeValue)) {
          TestUtil.logTrace("Update occurred properly:" + dataTypes2);
          pass2 = true;
        } else {
          TestUtil.logErr("Update did not occur properly");
        }
      } else {
        TestUtil.logErr("Find returned null after update");
      }

      TestUtil.logTrace("Retrieving all results first");

      Collection<DataTypes2> cDataTypes2 = getEntityManager()
          .createQuery("select d from DataTypes2 d").getResultList();
      for (DataTypes2 d : cDataTypes2) {
        TestUtil.logTrace("result:" + d.toString());
      }

      TestUtil.logTrace("Check results when testing for Time");
      result = getEntityManager()
          .createQuery(
              "select d.timeData from DataTypes2 d where d.timeData = :time")
          .setParameter("time", timeValue).getResultList();

      int result_size = result.size();
      TestUtil.logTrace("Result Size = " + result_size);

      if (result_size == 1) {
        pass3 = true;
        TestUtil.logTrace("Received expected result size");
        for (Time t : result) {
          TestUtil.logTrace("time=" + t);
          if (t.equals(timeValue)) {
            TestUtil.logTrace("Received expected Time ");
          } else {
            pass4 = false;
            TestUtil.logErr("Received unexpected Time = " + t.toString());
          }
        }

      } else {
        TestUtil.logErr("Expected 1 result, instead got: " + result_size);
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass1 || !pass2 || !pass3 || !pass4) {
      throw new Exception("queryAPITest28 failed");
    }
  }

  /*
   * @testName: queryAPITest29
   * 
   * @assertion_ids: PERSISTENCE:SPEC:527;
   * 
   * @test_Strategy: Usage of TimeStamp literal in Query
   *
   */
 @Test
  public void queryAPITest29() throws Exception {

    TestUtil.logTrace("Begin queryAPITest29");
    Query q;
    Collection<Timestamp> result;
    int result_size = 0;

    boolean pass1 = false;
    boolean pass2 = true;

    java.sql.Timestamp tsValue = getTimestampData("2006-11-11");
    TestUtil.logTrace("timestamp Value = " + tsValue.toString());

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("FIND D2");

      DataTypes2 dataTypes2 = getEntityManager().find(DataTypes2.class, dateId);

      if (null != dataTypes2) {
        TestUtil.logTrace("DataType Entity is not null, setting TimeData ");
        dataTypes2.setTsData(tsValue);
      }

      getEntityManager().merge(dataTypes2);
      doFlush();
      clearCache();

      TestUtil.logTrace("Check results");
      if ((null != dataTypes2)) {
        // && (dataTypes2.getTimeData().equals(timeValue))

        q = getEntityManager().createQuery(
            "select d.tsData from DataTypes2 d where d.tsData = '2006-11-11 10:10:10'");

        result = q.getResultList();
        result_size = result.size();
        TestUtil.logTrace("Result Size = " + result_size);

        if (result_size == 1) {
          pass1 = true;
          TestUtil.logTrace("Received expected result size");

          for (Timestamp t : result) {
            TestUtil.logTrace("time=" + t);
            if (t.equals(tsValue)) {
              TestUtil.logTrace("Received expected TimeStamp ");
            } else {
              TestUtil
                  .logErr("Received unexpected TimeStamp = " + t.toString());
              pass2 = false;
            }
          }
        } else {
          TestUtil.logErr("Did not get expected results. " + " Expected "
              + tsValue + " , got: " + dataTypes2.getTsData());
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);

    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

    if (!pass1 || !pass2) {
      throw new Exception("queryAPITest29 failed");
    }
  }

  private void createDataTypes2Data() throws Exception {
    TestUtil.logTrace("createDataTypes2Data");
    try {

      getEntityTransaction().begin();

      DataTypes2 dT2 = new DataTypes2(dateId);
      dT2.setDateData(dateId);
      dT2.setTimeData(getTimeData("01:01:01"));
      getEntityManager().persist(dT2);

      java.util.Date d = getUtilDate("2010-02-11");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("02:02:02"));
      getEntityManager().persist(dT2);

      d = getUtilDate("2011-03-12");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("03:03:03"));
      getEntityManager().persist(dT2);

      d = getUtilDate("2012-04-01");
      dT2 = new DataTypes2(d);
      dT2.setDateData(d);
      dT2.setTimeData(getTimeData("04:04:04"));
      getEntityManager().persist(dT2);

      getEntityTransaction().commit();
      TestUtil.logTrace("Created TestData");

    } catch (Exception re) {
      TestUtil.logErr("Unexpected Exception in createTestData:", re);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr(
            "Unexpected Exception in createTestData while rolling back TX:",
            re);
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
      getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM INSURANCE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM DATATYPES2")
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
