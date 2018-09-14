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

package com.sun.ts.tests.jsonb.defaultmapping.arrays;

import java.util.Arrays;
import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.arrays.model.MultiDimensionalArrayContainer;
import com.sun.ts.tests.jsonb.defaultmapping.arrays.model.PrimitiveArrayContainer;

/**
 * @test
 * @sources ArraysMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.arrays.ArraysMappingTest
 **/
public class ArraysMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new ArraysMappingTest();
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
   * @testName: testPrimitiveArray
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.12-1; JSONB:SPEC:JSB-3.12-2
   *
   * @test_Strategy: Assert that a simple array of primitives is handled
   * correctly
   */
  public Status testPrimitiveArray() throws Fault {
    int[] instance = { Integer.MIN_VALUE, Integer.MAX_VALUE };
    String jsonString = jsonb.toJson(new PrimitiveArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString
        .matches("\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*" + Integer.MIN_VALUE
            + "\\s*,\\s*" + Integer.MAX_VALUE + "\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to marshal object with int[] attribute value.");
    }

    PrimitiveArrayContainer unmarshalledObject = jsonb
        .fromJson("{ \"instance\" : [ " + Integer.MIN_VALUE + ", "
            + Integer.MAX_VALUE + " ] }", PrimitiveArrayContainer.class);
    if (!Arrays.equals(instance, unmarshalledObject.getInstance())) {
      throw new Fault("Failed to unmarshal object with int[] attribute value.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testMultiDimensionalArray
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.12-1; JSONB:SPEC:JSB-3.12-2;
   * JSONB:SPEC:JSB-3.14.2-1; JSONB:SPEC:JSB-3.14.2-2; JSONB:SPEC:JSB-3.14.2-3
   *
   * @test_Strategy: Assert that a multi-dimensional array is serialized and
   * deserialized as a multi-dimensional array and null array values are
   * correctly serialized and deserialized
   */
  public Status testMultiDimensionalArray() throws Fault {
    Integer[][] instance = { { 1, null, 3 },
        { Integer.MIN_VALUE, Integer.MAX_VALUE } };
    String jsonString = jsonb.toJson(new MultiDimensionalArrayContainer() {
      {
        setInstance(instance);
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*\\:\\s*\\[\\s*\\[\\s*1\\s*,\\s*null\\s*,\\s*3\\s*\\]\\s*,\\s*\\[\\s*"
            + Integer.MIN_VALUE + "\\s*,\\s*" + Integer.MAX_VALUE
            + "\\s*\\]\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to marshal object with int[][] attribute value.");
    }

    MultiDimensionalArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ [ 1, null, 3 ], [ " + Integer.MIN_VALUE + ", "
            + Integer.MAX_VALUE + " ] ] }",
        MultiDimensionalArrayContainer.class);
    if (!Arrays.deepEquals(instance, unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to unmarshal object with int[][] attribute value.");
    }

    return Status.passed("OK");
  }
}
