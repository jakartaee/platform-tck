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

package com.sun.ts.tests.jsonb.defaultmapping.nullvalue;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.nullvalue.model.NullArrayContainer;
import com.sun.ts.tests.jsonb.defaultmapping.nullvalue.model.NullValueContainer;

import java.util.Properties;

/**
 * @test
 * @sources NullValueMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.nullvalue.NullValueMappingTest
 **/
public class NullValueMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new NullValueMappingTest();
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
   * @testName: testNullAttributeValue
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.14.1-1; JSONB:SPEC:JSB-3.14.1-2;
   * JSONB:SPEC:JSB-3.14.1-3
   *
   * @test_Strategy: Assert that fields with null value are ignored during
   * marshalling and that during unmarshalling missing attributes are not set,
   * maintaining original value, and null attributes are correctly unmarshalled
   */
  public Status testNullAttributeValue() throws Fault {
    String jsonString = jsonb.toJson(new NullValueContainer() {
      {
        setInstance(null);
      }
    });
    if (!jsonString.matches("\\{\\s*\\}")) {
      throw new Fault("Failed to ignore displaying property with null value.");
    }

    NullValueContainer unmarshalledObject = jsonb.fromJson("{ }",
        NullValueContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance())) {
      throw new Fault(
          "Failed to ignore calling setter of absent property during unmarshalling.");
    }

    unmarshalledObject = jsonb.fromJson("{ \"instance\" : null }",
        NullValueContainer.class);
    if (unmarshalledObject.getInstance() != null) {
      throw new Fault("Failed to set property to null.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testNullArrayValue
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.14.2-1; JSONB:SPEC:JSB-3.14.2-2;
   * JSONB:SPEC:JSB-3.14.2-3
   *
   * @test_Strategy: Assert that a null array value is marked as null during
   * marshalling and a null value is set to the appropriate array index during
   * unmrashalling of an array containing a null value
   */
  public Status testNullArrayValue() throws Fault {
    String jsonString = jsonb.toJson(new NullArrayContainer() {
      {
        setInstance(new String[] { "Test 1", null, "Test 2" });
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\[\\s*\"Test 1\"\\s*,\\s*null\\s*,\\s*\"Test 2\"\\s*\\]\\s*\\}")) {
      throw new Fault("Failed to correctly display null array value.");
    }

    NullArrayContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : [ \"Test 1\", null, \"Test 2\" ] }",
        NullArrayContainer.class);
    Object[] instance = unmarshalledObject.getInstance();
    if (instance.length != 3 || !"Test 1".equals(instance[0])
        || instance[1] != null || !"Test 2".equals(instance[2])) {
      throw new Fault("Failed to correctly set null array value.");
    }

    return Status.passed("OK");
  }
}
