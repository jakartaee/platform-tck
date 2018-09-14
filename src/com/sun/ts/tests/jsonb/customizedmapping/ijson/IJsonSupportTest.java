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

/*
 * $Id$
 */

package com.sun.ts.tests.jsonb.customizedmapping.ijson;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.BinaryDataContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.DateContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.CalendarContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.DurationContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.GregorianCalendarContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.InstantContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.LocalDateContainer;
import com.sun.ts.tests.jsonb.customizedmapping.ijson.model.LocalDateTimeContainer;

/**
 * @test
 * @sources IJsonSupportTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.ijson.IJsonSupportTest
 **/
public class IJsonSupportTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new IJsonSupportTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: testStrictNonObjectOrArrayTopLevel
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1
   *
   * @test_Strategy: Assert that top level JSON texts that are neither objects
   * or arrays are restricted during marshalling if JsonbConfig.withStrictIJSON
   * is used
   */
  public Status testStrictNonObjectOrArrayTopLevel() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    try {
      jsonb.toJson("Test String");
      throw new Fault(
          "Failed to restrict serialization of top-level JSON texts that are neither objects nor arrays when JsonbConfig.withStrictIJSON is used.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testStrictBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using
   * BASE_64_URL binary data encoding if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictBinaryDataEncoding() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString
        .matches("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}")) {
      throw new Fault(
          "Failed to correctly marshal binary data using BASE_64_URL binary data encoding when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictDate
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.Date is serialized in the same format
   * as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictDate() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));
    final Calendar instance = Calendar.getInstance();
    instance.clear();
    instance.set(1970, 0, 1);
    instance.setTimeZone(TimeZone.getTimeZone("UTC"));
    String jsonString = jsonb.toJson(new DateContainer() {
      {
        setInstance(instance.getTime());
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.util.Date in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictCalendar
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.Calendar is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictCalendar() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    Calendar calendarProperty = Calendar.getInstance();
    calendarProperty.clear();
    calendarProperty.set(1970, 0, 1);
    calendarProperty.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

    String jsonString = jsonb.toJson(new CalendarContainer() {
      {
        setInstance(calendarProperty);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+01:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.util.Calendar in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictGregorianCalendar
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.util.GregorianCalendar is serialized in
   * the same format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON
   * is used
   */
  public Status testStrictGregorianCalendar() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.set(1970, 0, 1, 0, 0, 0);
    gregorianCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

    String jsonString = jsonb.toJson(new GregorianCalendarContainer() {
      {
        setInstance(gregorianCalendar);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+01:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.util.GregorianCalendar in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictLocalDate
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.LocalDate is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictLocalDate() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    String jsonString = jsonb.toJson(new LocalDateContainer() {
      {
        setInstance(LocalDate.of(1970, 1, 1));
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.time.LocalDate in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictLocalDateTime
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.LocalDate is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictLocalDateTime() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    String jsonString = jsonb.toJson(new LocalDateTimeContainer() {
      {
        setInstance(LocalDateTime.of(1970, 1, 1, 1, 1, 1));
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T01:01:01Z\\+00:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.time.LocalDate in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictInstant
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-2; JSONB:SPEC:JSB-4.4.1-3
   *
   * @test_Strategy: Assert that java.time.Instant is serialized in the same
   * format as java.time.ZonedDateTime if JsonbConfig.withStrictIJSON is used
   */
  public Status testStrictInstant() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    String jsonString = jsonb.toJson(new InstantContainer() {
      {
        setInstance(Instant.ofEpochMilli(0));
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\"1970-01-01T00:00:00Z\\+00:00\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.time.Instant in the same format as java.time.ZonedDateTime when JsonbConfig.withStrictIJSON is used.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testStrictDuration
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.4-1; JSONB:SPEC:JSB-4.4.1-1;
   * JSONB:SPEC:JSB-4.4.1-4
   *
   * @test_Strategy: Assert that java.time.Duration is serialized in the same
   * format as in Appendix A of RFC 3339
   */
  public Status testStrictDuration() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withStrictIJSON(true));

    String jsonString = jsonb.toJson(new DurationContainer() {
      {
        setInstance(Duration.ofDays(1).plus(Duration.ofHours(1))
            .plus(Duration.ofSeconds(1)));
      }
    });
    if (!jsonString.matches("\\{\\s*\"instance\"\\s*:\\s*\"PT25H1S\"\\s*}")) {
      throw new Fault(
          "Failed to serialize java.time.Duration in the same format as in Appendix A of RFC 3339.");
    }

    return Status.passed("OK");
  }
}
