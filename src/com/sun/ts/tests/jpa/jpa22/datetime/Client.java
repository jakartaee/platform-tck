/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.jpa22.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  private static final long serialVersionUID = 22L;

  String schemaGenerationDir = null;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  @Override
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("Setup: JPA 2.2 Java 8 date and time types test");
    try {
      super.setup(args, p);

      Properties props = getPersistenceUnitProperties();
      props.put("javax.persistence.schema-generation.database.action",
          "drop-and-create");
      props.put("javax.persistence.schema-generation.create-database-schemas",
          "true");
      displayProperties(props);
      TestUtil.logMsg(" - executing persistence schema generation");
      Persistence.generateSchema(getPersistenceUnitName(), props);
      clearEMAndEMF();
    } catch (Fault e) {
      TestUtil.logErr("caught Exception: ", e);
      throw new Fault(" ! JPA 2.2 Java 8 date and time types test setup failed",
          e);
    }
    verifySchema();
  }

  @Override
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup: JPA 2.2 Java 8 date and time types test");
    Properties props = getPersistenceUnitProperties();
    props.put("javax.persistence.schema-generation.database.action", "drop");
    displayProperties(props);
    TestUtil.logMsg(" - executing persistence schema cleanup");
    Persistence.generateSchema(getPersistenceUnitName(), props);
    closeEMAndEMF();
    super.cleanup();
  }

  /** Default LocalDate constant. */
  private static final LocalDate LOCAL_DATE_DEF = LocalDate.of(1970, 1, 1);

  /** LocalDate constant. */
  private static final LocalDate LOCAL_DATE = LocalDate.now();

  /** Default LocalTime constant. */
  private static final LocalTime LOCAL_TIME_DEF = LocalTime.of(0, 0, 0);

  /** LocalTime constant. */
  private static final LocalTime LOCAL_TIME = LocalTime.now();

  /** Default LocalDateTime constant. */
  private static final LocalDateTime LOCAL_DATE_TIME_DEF = LocalDateTime
      .of(1970, 1, 1, 0, 0, 0);

  /** LocalDateTime constant. */
  private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

  /** Default OffsetTime constant. */
  private static final OffsetTime OFFSET_TIME_DEF = OffsetTime
      .of(LOCAL_TIME_DEF, ZoneOffset.ofHours(1));

  /** OffsetTime constant. */
  private static final OffsetTime OFFSET_TIME = OffsetTime.of(LOCAL_TIME,
      ZoneOffset.ofHours(0));

  /** Default OffsetDateTime constant. */
  private static final OffsetDateTime OFFSET_DATE_TIME_DEF = OffsetDateTime
      .of(LOCAL_DATE_TIME_DEF, ZoneOffset.ofHours(1));

  /** OffsetDateTime constant. */
  private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime
      .of(LOCAL_DATE_TIME, ZoneOffset.ofHours(0));

  private static final DateTimeEntity[] entities = {
      new DateTimeEntity(1L, LOCAL_DATE, LOCAL_TIME_DEF, LOCAL_DATE_TIME_DEF,
          OFFSET_TIME_DEF, OFFSET_DATE_TIME_DEF),
      new DateTimeEntity(2L, LOCAL_DATE_DEF, LOCAL_TIME, LOCAL_DATE_TIME_DEF,
          OFFSET_TIME_DEF, OFFSET_DATE_TIME_DEF),
      new DateTimeEntity(3L, LOCAL_DATE_DEF, LOCAL_TIME_DEF, LOCAL_DATE_TIME,
          OFFSET_TIME_DEF, OFFSET_DATE_TIME_DEF),
      new DateTimeEntity(4L, LOCAL_DATE_DEF, LOCAL_TIME_DEF,
          LOCAL_DATE_TIME_DEF, OFFSET_TIME, OFFSET_DATE_TIME_DEF),
      new DateTimeEntity(5L, LOCAL_DATE_DEF, LOCAL_TIME_DEF,
          LOCAL_DATE_TIME_DEF, OFFSET_TIME_DEF, OFFSET_DATE_TIME) };

  /*
   * @testName: dateTimeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:320; PERSISTENCE:SPEC:2118.19;
   * PERSISTENCE:SPEC:2118.5;
   * 
   * @test_Strategy: Test new JPA 2.2 date and time types: java.time.LocalDate,
   * java.time.LocalTime, java.time.LocalDateTime, java.time.OffsetTime and
   * java.time.OffsetDateTime
   * 
   * @throws com.sun.ts.lib.harness.EETest.Fault when test failed
   */
  public void dateTimeTest() throws Fault {
    TestUtil.logMsg("Test: JPA 2.2 Java 8 date and time types");
    verifySchema();
    boolean createResult = createEntities();
    boolean allFindResult = true;
    boolean[] findResults = new boolean[entities.length];
    for (int i = 0; i < entities.length; i++) {
      findResults[i] = findEntityById(entities[i]);
    }
    boolean localDateResult = queryEntities("DateTimeEntity.findByLocalDate",
        "date", LOCAL_DATE, entities[0]);
    boolean localTimeResult = queryEntities("DateTimeEntity.findByLocalTime",
        "time", LOCAL_TIME, entities[1]);
    boolean localDateTimeResult = queryEntities(
        "DateTimeEntity.findByLocalDateTime", "dateTime", LOCAL_DATE_TIME,
        entities[2]);
    boolean offsetTimeResult = queryEntities("DateTimeEntity.findByOffsetTime",
        "time", OFFSET_TIME, entities[3]);
    boolean offsetDateTimeResult = queryEntities(
        "DateTimeEntity.findByOffsetDateTime", "dateTime", OFFSET_DATE_TIME,
        entities[4]);
    boolean localDateRangeResult = queryEntitiesRange(
        "DateTimeEntity.findLocalDateRange", "min",
        LOCAL_DATE.minus(10, ChronoUnit.DAYS), "max",
        LOCAL_DATE.plus(10, ChronoUnit.DAYS), entities[0]);
    boolean localTimeRangeResult = queryEntitiesRange(
        "DateTimeEntity.findLocalTimeRange", "min",
        LOCAL_TIME.minus(10, ChronoUnit.MINUTES), "max",
        LOCAL_TIME.plus(10, ChronoUnit.MINUTES), entities[1]);
    boolean localDateTimeRangeResult = queryEntitiesRange(
        "DateTimeEntity.findLocalDateTimeRange", "min",
        LOCAL_DATE_TIME.minus(10, ChronoUnit.DAYS), "max",
        LOCAL_DATE_TIME.plus(10, ChronoUnit.DAYS), entities[2]);
    boolean offsetTimeRangeResult = queryEntitiesRange(
        "DateTimeEntity.findOffsetTimeRange", "min",
        OFFSET_TIME.minus(10, ChronoUnit.MINUTES), "max",
        OFFSET_TIME.plus(10, ChronoUnit.MINUTES), entities[3]);
    boolean offsetDateTimeRangeResult = queryEntitiesRange(
        "DateTimeEntity.findOffsetDateTimeRange", "min",
        OFFSET_DATE_TIME.minus(10, ChronoUnit.DAYS).minus(10,
            ChronoUnit.MINUTES),
        "max",
        OFFSET_DATE_TIME.plus(10, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES),
        entities[4]);
    TestUtil.logMsg(
        "--------------------------------------------------------------------------------");
    TestUtil.logMsg(" - JPA 2.2 Java 8 date and time types test results:");
    logTestResult("Entities creation", createResult);
    for (int i = 0; i < entities.length; i++) {
      logTestResult("Find by ID=" + entities[i].getId().toString(),
          findResults[i]);
      allFindResult = allFindResult && findResults[i];
    }
    logTestResult("Query DateTimeEntity.findByLocalDate", localDateResult);
    logTestResult("Query DateTimeEntity.findByLocalTime", localTimeResult);
    logTestResult("Query DateTimeEntity.findByLocalDateTime",
        localDateTimeResult);
    logTestResult("Query DateTimeEntity.findByOffsetTime", offsetTimeResult);
    logTestResult("Query DateTimeEntity.findByOffsetDateTime",
        offsetDateTimeResult);
    logTestResult("Query DateTimeEntity.findLocalDateRange",
        localDateRangeResult);
    logTestResult("Query DateTimeEntity.findLocalTimeRange",
        localTimeRangeResult);
    logTestResult("Query DateTimeEntity.findLocalDateTimeRange",
        localDateTimeRangeResult);
    logTestResult("Query DateTimeEntity.findOffsetTimeRange",
        offsetTimeRangeResult);
    logTestResult("Query DateTimeEntity.findOffsetDateTimeRange",
        offsetDateTimeRangeResult);
    TestUtil.logMsg(
        "--------------------------------------------------------------------------------");
    if (!(createResult && allFindResult && localDateResult && localTimeResult
        && localDateTimeResult && offsetTimeResult && offsetDateTimeResult)) {
      throw new Fault(
          "dateTimeTest (JPA 2.2 Java 8 date and time types test) failed");
    }
  }

  /**
   * Create entities with Java 8 date and time types.
   * 
   * @return value of {@code true} when entities were created successfully or
   *         {@code false} otherwise
   */
  private boolean createEntities() {
    TestUtil.logMsg(" - creating test entities");
    try {
      getEntityTransaction().begin();
      for (DateTimeEntity e : entities) {
        getEntityManager().persist(e);
      }
      getEntityTransaction().commit();
      return true;
    } catch (Exception e) {
      TestUtil.logErr("caught Exception: ", e);
      return false;
    } finally {
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
    }
  }

  /**
   * Find entity by primary key.
   * 
   * @param expected
   *          entity to find and compare to (must contain proper ID)
   * @return value of {@code true} when entity was found and matches
   *         {@code expected} argument or {@code false} otherwise
   */
  private boolean findEntityById(DateTimeEntity expected) {
    TestUtil.logMsg(" - executing find by ID=" + expected.getId().toString());
    try {
      DateTimeEntity result = getEntityManager().find(DateTimeEntity.class,
          expected.getId());
      if (result == null) {
        TestUtil.logErr("no result returned for " + expected.toString());
        return false;
      }
      if (!expected.equals(result)) {
        TestUtil.logErr("returned entity does not match expected");
        TestUtil.logErr(" - expected: " + expected.toString());
        TestUtil.logErr(" - returned: " + result.toString());
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("caught Exception: ", e);
      return false;
    }
  }

  /**
   * Find entity by Java 8 date and time type value.
   * 
   * @param queryName
   *          named query identifier (named queries are defined in
   *          DateTimeEntity class)
   * @param paramName
   *          name of query parameter to set
   * @param paramValue
   *          query parameter value to set
   * @param expected
   *          expected returned entity
   * @return value of {@code true} when exactly one entity was found and matches
   *         {@code expected} argument or {@code false} otherwise
   */
  private boolean queryEntities(String queryName, String paramName,
      Object paramValue, DateTimeEntity expected) {
    TestUtil.logMsg(" - executing query " + queryName + ": " + paramName + "="
        + paramValue.toString());
    try {
      TypedQuery<DateTimeEntity> query = getEntityManager()
          .createNamedQuery(queryName, DateTimeEntity.class);
      query.setParameter(paramName, paramValue);
      List<DateTimeEntity> result = query.getResultList();
      if (result == null || result.isEmpty()) {
        TestUtil.logErr("no result returned for query " + queryName + " with "
            + paramName + "=" + paramValue.toString());
        return false;
      }
      if (result.size() > 1) {
        TestUtil.logErr("too many results (" + Integer.toString(result.size())
            + ") returned for query " + queryName + " with " + paramName + "="
            + paramValue.toString());
        return false;
      }
      DateTimeEntity returned = result.get(0);
      if (!expected.equals(returned)) {
        TestUtil.logErr("returned entity does not match expected");
        TestUtil.logErr(" - expected: " + expected.toString());
        TestUtil.logErr(" - returned: "
            + (returned != null ? returned.toString() : "null"));
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("caught Exception: ", e);
      return false;
    }
  }

  /**
   * Find entity by Java 8 date and time type value.
   * 
   * @param queryName
   *          named query identifier (named queries are defined in
   *          DateTimeEntity class)
   * @param paramName
   *          name of query parameter to set
   * @param paramValue
   *          query parameter value to set
   * @param expected
   *          expected returned entity
   * @return value of {@code true} when exactly one entity was found and matches
   *         {@code expected} argument or {@code false} otherwise
   */
  private boolean queryEntitiesRange(String queryName, String minName,
      Object minValue, String maxName, Object maxValue,
      DateTimeEntity expected) {
    TestUtil.logMsg(" - executing query " + queryName + ": " + minName + "="
        + minValue.toString() + ", " + maxName + "=" + maxValue.toString());
    try {
      TypedQuery<DateTimeEntity> query = getEntityManager()
          .createNamedQuery(queryName, DateTimeEntity.class);
      query.setParameter(minName, minValue);
      query.setParameter(maxName, maxValue);
      List<DateTimeEntity> result = query.getResultList();
      if (result == null || result.isEmpty()) {
        TestUtil.logErr("no result returned for query " + queryName + " with "
            + minName + "=" + minValue.toString() + ", " + maxName + "="
            + maxValue.toString());
        return false;
      }
      if (result.size() > 1) {
        TestUtil.logErr("too many results (" + Integer.toString(result.size())
            + ") returned for query " + queryName + " with " + minName + "="
            + minValue.toString() + ", " + maxName + "=" + maxValue.toString());
        return false;
      }
      DateTimeEntity returned = result.get(0);
      if (!expected.equals(returned)) {
        TestUtil.logErr("returned entity does not match expected");
        TestUtil.logErr(" - expected: " + expected.toString());
        TestUtil.logErr(" - returned: "
            + (returned != null ? returned.toString() : "null"));
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("caught Exception: ", e);
      return false;
    }
  }

  /**
   * Verify generated schema. Java 8 date and time support is not finished so
   * using dummy entity containing only ID to verify that schema exists.
   * 
   * @return verification result
   */
  private void verifySchema() throws Fault {
    TestUtil.logMsg(" - executing persistence schema check");
    try {
      getEntityTransaction().begin();
      DummyEntity e = new DummyEntity();
      getEntityManager().persist(e);
      getEntityTransaction().commit();
      TestUtil.logTrace("   - stored " + e.toString());
      DummyEntity result = getEntityManager().find(DummyEntity.class,
          e.getId());
      if (result == null) {
        TestUtil.logErr("   ! no entity was found");
        throw new Fault("dateTimeTest: Schema verification failed");
      }
      getEntityTransaction().begin();
      result = getEntityManager().merge(result);
      getEntityManager().remove(result);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("caught Exception: ", e);
      if (getEntityTransaction().isActive()) {
        getEntityTransaction().rollback();
      }
      throw new Fault("dateTimeTest: Schema verification failed");
    }
  }

  @Override
  public EntityTransaction getEntityTransaction() {
    if (isStandAloneMode() && !super.getEntityTransaction().isActive()) {
      EntityTransaction et = getEntityManager().getTransaction();
      setEntityTransaction(et);
    }
    return super.getEntityTransaction();
  }

  /**
   * Log test result message.
   * 
   * @param name
   *          test name
   * @param result
   *          test result
   */
  private void logTestResult(String name, boolean result) {
    StringBuilder sb = new StringBuilder();
    sb.append("   ").append(result ? '+' : '-').append(' ');
    sb.append(name).append(": ");
    sb.append(result ? "PASSED" : "FAILED");
    TestUtil.logMsg(sb.toString());
  }

}
