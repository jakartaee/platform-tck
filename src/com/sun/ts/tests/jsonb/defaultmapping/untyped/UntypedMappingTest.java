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

package com.sun.ts.tests.jsonb.defaultmapping.untyped;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.SimpleMappingTester;

/**
 * @test
 * @sources UntypedMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.untyped.UntypedMappingTest
 **/
public class UntypedMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new UntypedMappingTest();
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
   * @testName: testObjectMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.6-1; JSONB:SPEC:JSB-3.6-2
   *
   * @test_Strategy: Assert that object, string, number, boolean and null JSON
   * values are correctly mapped to java.util.Map<String,Object> implementation
   * with predictable iteration order, with java.lang.String,
   * java.math.BigDecimal, java.lang.Boolean and null values
   */
  public void testObjectMapping() throws Fault {
    Jsonb jsonb = JsonbBuilder.create();
    @SuppressWarnings("unused")
    String jsonString = jsonb.toJson(new Object() {
      private String stringProperty = "Test String";

      public String getStringProperty() {
        return stringProperty;
      }

      private Number numericProperty = 0;

      public Number getNumericProperty() {
        return numericProperty;
      }

      private boolean booleanProperty = false;

      public boolean getBooleanProperty() {
        return booleanProperty;
      }

      private Object nullProperty = null;

      public Object getNullProperty() {
        return nullProperty;
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"booleanProperty\"\\s*:\\s*false\\s*\\,\\s*\"numericProperty\"\\s*:\\s*0[\\.0]?+\\s*,\\s*\"stringProperty\"\\s*:\\s*\"Test String\"\\s*}")) {
      throw new Fault(
          "Failed to correctly marshal object with String, Number, Boolean and null fields.");
    }

    Object unmarshalledObject = jsonb.fromJson(
        "{ \"numericProperty\" : 0.0, \"booleanProperty\" : false, \"stringProperty\" : \"Test String\" }",
        Object.class);
    if (!Map.class.isInstance(unmarshalledObject)
        || !new LinkedHashMap<String, Object>() {
          private static final long serialVersionUID = UntypedMappingTest.serialVersionUID;
          {
            put("numericProperty", BigDecimal.valueOf(0.0d));
            put("booleanProperty", false);
            put("stringProperty", "Test String");
          }
        }.equals(unmarshalledObject)) {
      throw new Fault(
          "Failed to correctly unmarshal object with string, number, boolean and null JSON values into a predictable order Map<String,Object> with java.lang.String, java.math.BigDecimal, java.lang.Boolean and null values.");
    }
  }

  /*
   * @testName: testArrayMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.6-1
   *
   * @test_Strategy: Assert that JSON arrays are correctly handled as
   * java.util.List<Object>
   */
  public Status testArrayMapping() throws Fault {
    return new SimpleMappingTester<>(List.class).test(
        Arrays.asList("Test String"), "\\[\\s*\"Test String\"s*\\]",
        "[ \"Test String\" ]", Arrays.asList("Test String"));
  }
}
