/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.mergetests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;

// $Id$
/**
 * RFC 7396: JavaScript Object Notation (JSON) Merge Patch compatibility
 * tests.<br>
 * Checks scenario described in
 * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
 * Introduction</a>}: If the patch is anything other than an object, the result
 * will always be to replace the entire target with the entire patch.
 */
public class MergeNonObject extends MergeCommon {

  /**
   * Creates an instance of RFC 7396 non object patch test.
   */
  MergeNonObject() {
    super();
  }

  /**
   * Test RFC 7396: Non object patch. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 7396: Non object patch");
    TestUtil.logMsg("Testing RFC 7396: Non object patch");
    testStringOnEmptyObject(result);
    testStringOnSimpleObject(result);
    testStringOnSimpleArray(result);
    testIntOnEmptyObject(result);
    testIntOnSimpleObject(result);
    testIntOnSimpleArray(result);
    testBoolOnEmptyObject(result);
    testBoolOnSimpleObject(result);
    testBoolOnSimpleArray(result);
    testArrayOnEmptyObject(result);
    testArrayOnCompoundObject(result);
    testArrayOnSimpleArray(result);
    return result;
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnEmptyObject(final TestResult result) {
    TestUtil.logMsg(" - for String on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonValue patch = Json.createValue(STR_VALUE);
    final JsonValue check = Json.createValue(STR_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnSimpleObject(final TestResult result) {
    TestUtil.logMsg(" - for String on simple JSON object");
    final JsonObject in = createSimpleObjectWithStr();
    final JsonValue patch = Json.createValue(STR_VALUE);
    final JsonValue check = Json.createValue(STR_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnSimpleArray(final TestResult result) {
    TestUtil.logMsg(" - for String on simple JSON array");
    final JsonArray in = createStringArray2();
    final JsonValue patch = Json.createValue(STR_VALUE);
    final JsonValue check = Json.createValue(STR_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnEmptyObject(final TestResult result) {
    TestUtil.logMsg(" - for int on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonValue patch = Json.createValue(INT_VALUE);
    final JsonValue check = Json.createValue(INT_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnSimpleObject(final TestResult result) {
    TestUtil.logMsg(" - for int on simple JSON object");
    final JsonObject in = createSimpleObjectWithInt();
    final JsonValue patch = Json.createValue(INT_VALUE);
    final JsonValue check = Json.createValue(INT_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnSimpleArray(final TestResult result) {
    TestUtil.logMsg(" - for int on simple JSON array");
    final JsonArray in = createIntArray2();
    final JsonValue patch = Json.createValue(INT_VALUE);
    final JsonValue check = Json.createValue(INT_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnEmptyObject(final TestResult result) {
    TestUtil.logMsg(" - for boolean on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonValue patch = toJsonValue(BOOL_VALUE);
    final JsonValue check = toJsonValue(BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnSimpleObject(final TestResult result) {
    TestUtil.logMsg(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObjectWithBool();
    final JsonValue patch = toJsonValue(BOOL_VALUE);
    final JsonValue check = toJsonValue(BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnSimpleArray(final TestResult result) {
    TestUtil.logMsg(" - for boolean on simple JSON array");
    final JsonArray in = createBoolArray2();
    final JsonValue patch = toJsonValue(BOOL_VALUE);
    final JsonValue check = toJsonValue(BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnEmptyObject(final TestResult result) {
    TestUtil.logMsg(" - for JsonArray on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonArray patch = createStringArray1();
    final JsonArray check = createStringArray1();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnCompoundObject(final TestResult result) {
    TestUtil.logMsg(" - for JsonArray on compound JSON object");
    final JsonObject in = createCompoundObject();
    final JsonValue patch = createStringArray2();
    final JsonValue check = createStringArray2();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnSimpleArray(final TestResult result) {
    TestUtil.logMsg(" - for JsonArray on simple JSON array");
    final JsonArray in = createBoolArray2();
    final JsonValue patch = createIntArray2();
    final JsonValue check = createIntArray2();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }
}
