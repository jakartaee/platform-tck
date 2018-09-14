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

package com.sun.ts.tests.jsonb.defaultmapping.attributeorder;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.attributeorder.model.ExtendedContainer;
import com.sun.ts.tests.jsonb.defaultmapping.attributeorder.model.SimpleContainer;

import java.util.Properties;

/**
 * @test
 * @sources AttributeOrderMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.attributeorder.AttributeOrderMappingTest
 **/
public class AttributeOrderMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new AttributeOrderMappingTest();
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
   * @testName: testClassAttributeOrder
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.13-1; JSONB:SPEC:JSB-3.13-2
   *
   * @test_Strategy: Assert that declared fields are marshalled in
   * lexicographical order and unmarshalled in the order of appearance in the
   * JSON document
   */
  public void testClassAttributeOrder() throws Fault {
    String jsonString = jsonb.toJson(new SimpleContainer() {
      {
        setIntInstance(0);
        setStringInstance("Test String");
        setLongInstance(0L);
      }
    });
    String toMatch = "\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\}";
    if (!jsonString.matches(toMatch)) {
      System.out.append("serialized json string ").println(jsonString);
      System.out.append("does not match expected ").println(toMatch);
      throw new Fault("Failed to lexicographically order class attributes.");
    }

    SimpleContainer unmarshalledObject = jsonb.fromJson(
        "{ \"intInstance\" : 1, \"stringInstance\" : \"Test String\", \"longInstance\" : 0 }",
        SimpleContainer.class);
    if (unmarshalledObject.getIntInstance() != 3) {
      throw new Fault("Failed to set class attributes in order of appearance.");
    }
  }

  /*
   * @testName: testExtendedClassAttributeOrder
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.13-1; JSONB:SPEC:JSB-3.13-2
   *
   * @test_Strategy: Assert that declared fields of super class are marshalled
   * before declared fields of child class and all are unmarshalled in the order
   * of appearance in the JSON document
   */
  public Status testExtendedClassAttributeOrder() throws Fault {
    String jsonString = jsonb.toJson(new ExtendedContainer() {
      {
        setIntInstance(0);
        setStringInstance("Test String");
        setLongInstance(0L);
        setFloatInstance(0f);
        setShortInstance((short) 0);
      }
    });
    String toMatch = "\\{\\s*\"intInstance\"\\s*\\:\\s*0\\s*,\\s*\"longInstance\"\\s*\\:\\s*0\\s*,\\s*\"stringInstance\"\\s*\\:\\s*\"Test String\"\\s*\\,\\s*\"floatInstance\"\\s*\\:\\s*0.0\\s*,\\s*\"shortInstance\"\\s*\\:\\s*0\\s*}";
    if (!jsonString.matches(toMatch)) {
      System.out.append("serialized json string ").println(jsonString);
      System.out.append("does not match expected ").println(toMatch);
      throw new Fault("Failed to correctly order extended class attributes.");
    }

    jsonString = "{ \"intInstance\" : 1, \"shortInstance\" : 0, \"stringInstance\" : \"Test String\", \"floatInstance\" : 0.0, \"longInstance\" : 0 }";
    ExtendedContainer unmarshalledObject = jsonb.fromJson(jsonString,
        ExtendedContainer.class);
    if (unmarshalledObject.getIntInstance() != 5) {
      throw new Fault(
          "Failed to set extended class attributes in order of appearance.");
    }

    return Status.passed("OK");
  }
}
