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

package com.sun.ts.tests.jsonp.api.jsonobjecttests;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import static com.sun.ts.tests.jsonp.api.common.JsonAssert.*;
import static com.sun.ts.tests.jsonp.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonObjectBuilder} API factory methods added in JSON-P 1.1.<br>
 */
public class CreateObjectBuilder {

  /**
   * Creates an instance of {@link JsonObjectBuilder} API factory methods added
   * in JSON-P 1.1 test.
   */
  CreateObjectBuilder() {
    super();
  }

  /**
   * Test {@link JsonObjectBuilder} factory method added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonObjectBuilder API factory methods added in JSON-P 1.1.");
    TestUtil
        .logMsg("JsonObjectBuilder API factory methods added in JSON-P 1.1.");
    testCreateFromMap(result);
    testCreateFromJsonObject(result);
    return result;
  }

  /**
   * Test {@link Json#createObjectBuilder(Map<String,Object>)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromMap(final TestResult result) {
    TestUtil.logMsg(" - Json#createObjectBuilder(Map<String,Object>)");
    final JsonObject check = createSimpleObjectWithStr();
    Map<String, Object> values = new HashMap<>(2);
    values.put(DEF_NAME, DEF_VALUE);
    values.put(STR_NAME, STR_VALUE);
    final JsonObjectBuilder builder = Json.createObjectBuilder(values);
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createObjectBuilder(Map<String,Object>)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test {@link Json#createObjectBuilder(JsonObject)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromJsonObject(final TestResult result) {
    TestUtil.logMsg(" - Json#createObjectBuilder(JsonObject)");
    final JsonObject check = createSimpleObjectWithStr();
    final JsonObjectBuilder builder = Json.createObjectBuilder(check);
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("reateObjectBuilder(JsonObject)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null || !assertEquals(check, out);
  }

}
