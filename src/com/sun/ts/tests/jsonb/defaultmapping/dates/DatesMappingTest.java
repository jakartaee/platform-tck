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

package com.sun.ts.tests.jsonb.defaultmapping.dates;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.function.BiPredicate;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.MappingTester;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.CalendarContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.DateContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.DurationContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.GregorianCalendarContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.InstantContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.LocalDateContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.LocalDateTimeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.LocalTimeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.OffsetDateTimeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.OffsetTimeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.PeriodContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.SimpleTimeZoneContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.TimeZoneContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.ZoneIdContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.ZoneOffsetContainer;
import com.sun.ts.tests.jsonb.defaultmapping.dates.model.ZonedDateTimeContainer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

/**
 * @test
 * @sources DatesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.dates.DatesMappingTest
 **/
public class DatesMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private static final String OFFSET_HOURS = getHoursFromUTCRegExp(
      new GregorianCalendar(1970, 0, 1));

  private <T extends TimeZone> BiPredicate<T, T> timezoneTest(
      final boolean canBeSaving) {
    return (a, b) -> {
      // Depending on when the timezone is created, it can differ by one hour
      // when serialized/deserialized
      long diff = Math.abs(a.getRawOffset() - b.getRawOffset());
      return a.useDaylightTime() && canBeSaving
          ? diff == 0 || diff == a.getDSTSavings()
          : diff == 0;
    };
  }

  public static void main(String[] args) {
    EETest t = new DatesMappingTest();
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
   * @testName: testDate
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1;
   *
   * @test_Strategy: Assert that serializing and deserializing Date result in
   * the same value;
   */
  @SuppressWarnings("deprecation")
  public void testDate() throws Fault {
    Date date = new Date(70, 0, 1);
    Jsonb jsonb = JsonbBuilder.create();
    Date mixin = jsonb.fromJson(
        jsonb.toJson(jsonb.fromJson(jsonb.toJson(date), Date.class)),
        Date.class);
    if (date.getTime() != mixin.getTime())
      throw new Fault(
          "Serializing and deserializing Date results in different value");
  }

  /*
   * @testName: testDateNoTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-3;
   * JSONB:SPEC:JSB-3.5.1-1; JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that Date with no time is still marshalled as and
   * unmarshalled from ISO_DATE_TIME
   */
  @SuppressWarnings("deprecation")
  public Status testDateNoTimeMapping() throws Fault {
    // Date takes time zone from the default timezone which is set by user
    // environment
    Date date = new Date(70, 0, 1);

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    String toMatch = dtf.format(calendar.toZonedDateTime()).replace("]", "\\]")
        .replace("[", "\\[").replace("+", "\\+");
    return new MappingTester<>(DateContainer.class) //
        .setMarshallExpectedRegExp("\"" + toMatch + "\"")
        .setUnmarshallTestPredicate((a, b) -> {
          Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
          orig.clear();
          orig.set(1970, 0, 1);
          return b.equals(orig.getTime());
        }).test(date, "\"1970-01-01T00:00:00\"");
  }

  /*
   * @testName: testCalendarNoTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.1-1;
   * JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that Calendar with no time is marshalled as and
   * unmarshalled from ISO_DATE
   */
  public Status testCalendarNoTimeMapping() throws Fault {
    Calendar calendarProperty = Calendar.getInstance();
    calendarProperty.clear();
    return new MappingTester<>(CalendarContainer.class)//
        .setMarshallExpectedRegExp("\"1970-01-01" + OFFSET_HOURS + "\"") //
        .setUnmarshallTestPredicate((a, b) -> {
          Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
          orig.clear();
          orig.set(1970, 0, 1);
          return b.getTime().equals(orig.getTime())
              && getHoursFromUTC(orig).equals(getHoursFromUTC(b));
        }).test(calendarProperty, "\"1970-01-01\"");
  }

  /*
   * @testName: testGregorianCalendarNoTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-2;
   * JSONB:SPEC:JSB-3.5.1-1; JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that GregorianCalendar with no time is marshalled as
   * and unmarshalled from ISO_DATE
   */
  public Status testGregorianCalendarNoTimeMapping() throws Fault {
    GregorianCalendar calendar = new GregorianCalendar(1970, 0, 1);
    for (int i = Calendar.DATE + 1; i != Calendar.MILLISECOND + 1; i++)
      calendar.clear(i);
    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
    String toMatch = "\""
        + dtf.format(calendar.toZonedDateTime()).replace("+", "\\+") + "\"";
    return new MappingTester<>(GregorianCalendarContainer.class) //
        .setMarshallExpectedRegExp(toMatch) //
        .setUnmarshallTestPredicate((a, b) -> {
          Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
          orig.clear();
          orig.set(1970, 0, 1);
          return b.getTime().equals(orig.getTime())
              && getHoursFromUTC(orig).equals(getHoursFromUTC(b));
        }).test(calendar, "\"1970-01-01\"");
  }

  /*
   * @testName: testDateWithTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-3;
   * JSONB:SPEC:JSB-3.5.1-2; JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that Date with time information is marshalled as and
   * unmarshalled from ISO_DATE_TIME
   */
  @SuppressWarnings("deprecation")
  public Status testDateWithTimeMapping() throws Fault {
    Date date = new Date(70, 0, 1, 0, 0, 0);
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    String toMatch = dtf.format(calendar.toZonedDateTime()).replace("]", "\\]")
        .replace("[", "\\[");
    return new MappingTester<>(DateContainer.class) //
        .setMarshallExpectedRegExp("\"" + toMatch + "\"")
        .setUnmarshallTestPredicate((a, b) -> {
          Calendar orig = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
          orig.clear();
          orig.set(1970, 0, 1);
          return b.equals(orig.getTime());
        }).test(date, "\"1970-01-01T00:00:00\"");
  }

  /*
   * @testName: testCalendarWithTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.1-2;
   * JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that Calendar with time information is marshalled as
   * and unmarshalled from ISO_DATE_TIME
   */
  public Status testCalendarWithTimeMapping() throws Fault {
    Calendar calendarProperty = Calendar.getInstance();
    calendarProperty.set(1970, 0, 1, 1, 0, 0);
    calendarProperty.set(Calendar.MILLISECOND, 0);
    calendarProperty.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    return new MappingTester<>(CalendarContainer.class)
        .setMarshallExpectedRegExp(
            "\"1970-01-01T01:00:00(\\.\\d{1,3})?\\+01:00\\[Europe/Paris\\]\"")
        .setUnmarshallTestPredicate((a, b) -> {
          return a.getTime().equals(b.getTime())
              && getHoursFromUTC(calendarProperty).equals(getHoursFromUTC(b));
        }).test(calendarProperty,
            "\"1970-01-01T01:00:00.00+01:00[Europe/Paris]\"");
  }

  /*
   * @testName: testGregorianCalendarWithTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5-2;
   * JSONB:SPEC:JSB-3.5.1-2; JSONB:SPEC:JSB-3.5.1-3
   *
   * @test_Strategy: Assert that GregorianCalendar with time information is
   * marshalled as and unmarshalled from ISO_DATE_TIME
   */
  public Status testGregorianCalendarWithTimeMapping() throws Fault {
    GregorianCalendar calendar = GregorianCalendar.from(ZonedDateTime
        .of(LocalDateTime.of(1970, 1, 1, 1, 0, 0), ZoneId.of("GMT")));
    return new MappingTester<>(GregorianCalendarContainer.class).test(calendar,
        "\"1970-01-01T01:00:00Z[GMT]\"");
  }

  /*
   * @testName: testShortTimeZoneMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
   * JSONB:SPEC:JSB-3.5.2-3
   *
   * @test_Strategy: Assert that java.util.TimeZone is correctly handled
   */
  public Status testShortTimeZoneMapping() throws Fault {
    return new MappingTester<>(TimeZoneContainer.class) //
        .setUnmarshallTestPredicate(timezoneTest(false)) //
        .test(TimeZone.getTimeZone("GMT+10"), "\"GMT+10:00\"");
  }

  /*
   * @testName: testLongTimeZoneMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
   * JSONB:SPEC:JSB-3.5.2-3
   *
   * @test_Strategy: Assert that java.util.TimeZone is correctly handled
   */
  public Status testLongTimeZoneMapping() throws Fault {
    return new MappingTester<>(TimeZoneContainer.class) //
        .setUnmarshallTestPredicate(timezoneTest(true)) //
        .test(TimeZone.getTimeZone("America/Los_Angeles"),
            "\"America/Los_Angeles\"");
  }

  /*
   * @testName: testSimpleTimeZoneMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.2-1;
   * JSONB:SPEC:JSB-3.5.2-3
   *
   * @test_Strategy: Assert that java.util.SimpleTimeZone is correctly handled
   */
  public Status testSimpleTimeZoneMapping() throws Fault {
    return new MappingTester<>(SimpleTimeZoneContainer.class) //
        .setUnmarshallTestPredicate(timezoneTest(false)) //
        .test(new SimpleTimeZone(75 * 60 * 1000, "GMT+01:15"), "\"GMT+01:15\"");
  }

  /*
   * @testName: testInstantMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-1;
   * JSONB:SPEC:JSB-3.5.3-2; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.Instant is correctly marshalled as
   * and unmarshalled from ISO_INSTANT
   */
  public Status testInstantMapping() throws Fault {
    return new MappingTester<>(InstantContainer.class)
        .test(Instant.ofEpochMilli(0), "\"1970-01-01T00:00:00Z\"");
  }

  /*
   * @testName: testDurationMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-8
   *
   * @test_Strategy: Assert that Duration is correctly marshalled as and
   * umarshalled from ISO 8601 seconds based representation
   */
  public Status testDurationMapping() throws Fault {
    return new MappingTester<>(DurationContainer.class)
        .test(Duration.ofHours(1), "\"PT1H\"");
  }

  /*
   * @testName: testDurationWithSecondsMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-8;
   * JSONB:SPEC:JSB-3.5.3-9
   *
   * @test_Strategy: Assert that Duration is correctly marshalled as and
   * umarshalled from ISO 8601 seconds based representation
   */
  public Status testDurationWithSecondsMapping() throws Fault {
    return new MappingTester<>(DurationContainer.class)
        .test(Duration.ofHours(1).plus(Duration.ofSeconds(1)), "\"PT1H1S\"");
  }

  /*
   * @testName: testPeriodMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-10;
   * JSONB:SPEC:JSB-3.5.3-11
   *
   * @test_Strategy: Assert that Period is correctly marshalled as and
   * umarshalled from ISO 8601 period representation
   */
  public Status testPeriodMapping() throws Fault {
    return new MappingTester<>(PeriodContainer.class).test(Period.of(1, 1, 1),
        "\"P1Y1M1D\"");
  }

  /*
   * @testName: testZeroDaysPeriodMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-10;
   * JSONB:SPEC:JSB-3.5.3-11; JSONB:SPEC:JSB-3.5.3-12
   *
   * @test_Strategy: Assert that a zero length Period is correctly marshalled as
   * and umarshalled from "P0D"
   */
  public Status testZeroDaysPeriodMapping() throws Fault {
    return new MappingTester<>(PeriodContainer.class).test(Period.of(0, 0, 0),
        "\"P0D\"");
  }

  /*
   * @testName: testLocalDateMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.LocalDate is correctly marshalled as
   * and umarshalled from ISO_LOCAL_DATE
   */
  public Status testLocalDateMapping() throws Fault {
    return new MappingTester<>(LocalDateContainer.class)
        .test(LocalDate.of(2000, 1, 1), "\"2000-01-01\"");
  }

  /*
   * @testName: testLocalTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.LocalTime is correctly marshalled as
   * and umarshalled from ISO_LOCAL_TIME
   */
  public Status testLocalTimeMapping() throws Fault {
    return new MappingTester<>(LocalTimeContainer.class)
        .test(LocalTime.of(1, 1, 1), "\"01:01:01\"");
  }

  /*
   * @testName: testLocalDateTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.LocalDateTime is correctly marshalled
   * as and umarshalled from ISO_LOCAL_DATE_TIME
   */
  public Status testLocalDateTimeMapping() throws Fault {
    return new MappingTester<>(LocalDateTimeContainer.class)
        .test(LocalDateTime.of(2000, 1, 1, 1, 1, 1), "\"2000-01-01T01:01:01\"");
  }

  /*
   * @testName: testZonedDateTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.ZonedDateTime is correctly marshalled
   * as and umarshalled from ISO_ZONED_DATE_TIME
   */
  public Status testZonedDateTimeMapping() throws Fault {
    return new MappingTester<>(ZonedDateTimeContainer.class).test(
        ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 0, ZoneId.of("Europe/Paris")),
        "\"2000-01-01T01:01:01+01:00[Europe/Paris]\"");
  }

  /*
   * @testName: testZoneIdMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-4;
   * JSONB:SPEC:JSB-3.5.3-5
   *
   * @test_Strategy: Assert that java.time.ZoneId is correctly handled
   */
  public Status testZoneIdMapping() throws Fault {
    return new MappingTester<>(ZoneIdContainer.class).test(ZoneId.of("UTC"),
        "\"UTC\"");
  }

  /*
   * @testName: testZoneOffsetMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-6;
   * JSONB:SPEC:JSB-3.5.3-7
   *
   * @test_Strategy: Assert that java.time.ZoneOffset is correctly handled
   */
  public Status testZoneOffsetMapping() throws Fault {
    return new MappingTester<>(ZoneOffsetContainer.class)
        .test(ZoneOffset.of("+01:00"), "\"+01:00\"");
  }

  /*
   * @testName: testOffsetDateTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.OffsetDateTime is correctly
   * marshalled as and umarshalled from ISO_OFFSET_DATE_TIME
   */
  public Status testOffsetDateTimeMapping() throws Fault {
    return new MappingTester<>(OffsetDateTimeContainer.class)
        .test(OffsetDateTime.of(LocalDateTime.of(2000, 1, 1, 1, 1, 1),
            ZoneOffset.of("+01:00")), "\"2000-01-01T01:01:01+01:00\"");
  }

  /*
   * @testName: testOffsetTimeMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-1; JSONB:SPEC:JSB-3.5.3-3
   *
   * @test_Strategy: Assert that java.time.OffsetTime is correctly marshalled as
   * and umarshalled from ISO_OFFSET_TIME
   */
  public Status testOffsetTimeMapping() throws Fault {
    return new MappingTester<>(OffsetTimeContainer.class).test(
        OffsetTime.of(LocalTime.of(1, 1, 1), ZoneOffset.of("+01:00")),
        "\"01:01:01+01:00\"");
  }

  /*
   * @testName: testUnmarshallingUnknownFormat
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5-4
   *
   * @test_Strategy: Assert that an error is reported if the date/time string
   * does not correspond to the expected datetime format
   */
  public Status testUnmarshallingUnknownFormat() throws Fault {
    try {
      JsonbBuilder.create().fromJson(
          "{ \"instance\" : \"01/01/1970 00:00:00\" }", DateContainer.class);
      throw new Fault(
          "An exception is expected if the date/time string does not correspond to the expected datetime format.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testUnmarshallingDeprecatedTimezoneIds
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.5.2-2
   *
   * @test_Strategy: Assert that error is reported for deprecated three-letter
   * time zone IDs as specified in java.util.Timezone
   */
  public void testUnmarshallingDeprecatedTimezoneIds() throws Fault {
    try {
      JsonbBuilder.create().fromJson("{ \"instance\" : \"CST\" }",
          TimeZoneContainer.class);
      throw new Fault(
          "An exception is expected for deprecated three-letter time zone IDs.");
    } catch (JsonbException x) {

    }

    try {
      JsonbBuilder.create().fromJson("{ \"instance\" : \"CST\" }",
          SimpleTimeZoneContainer.class);
      throw new Fault(
          "An exception is expected for deprecated three-letter time zone IDs.");
    } catch (JsonbException x) {

    }
  }

  private static String getHoursFromUTCRegExp(Calendar calendar) {
    String hours = getHoursFromUTC(calendar);
    if ("+00:00".equals(hours))
      return "(Z|\\+00:00)";
    else
      return "\\" + hours;
  }

  private static String getHoursFromUTC(Calendar calendar) {
    int offsetInTenMins = getMinutesFromUTC(calendar);
    String offset = String.format("%s%02d:%02d",
        offsetInTenMins >= 0 ? "+" : "-", Math.abs(offsetInTenMins / 60),
        Math.abs((offsetInTenMins) % 60));
    return offset;
  }

  private static int getMinutesFromUTC(Calendar calendar) {
    TimeZone tz = TimeZone.getDefault();
    Calendar def = (Calendar) calendar.clone();
    def.get(Calendar.HOUR); // setFields
    return tz.getOffset(def.getTimeInMillis()) / 60000;
  }
}
