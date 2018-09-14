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

package com.sun.ts.tests.jsonb.defaultmapping.ignore;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.defaultmapping.ignore.model.StringContainer;

import java.util.Properties;

/**
 * @test
 * @sources MustIgnoreMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.ignore.MustIgnoreMappingTest
 **/
public class MustIgnoreMappingTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new MustIgnoreMappingTest();
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
   * @testName: testIgnoreUnknownAttribute
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.18-1
   *
   * @test_Strategy: Assert that unknown attributes are ignored during
   * unmarshalling
   */
  public Status testIgnoreUnknownAttribute() throws Fault {
    try {
      StringContainer unmarshalledObject = jsonb.fromJson(
          "{ \"instance\" : \"Test String\", \"newInstance\" : 0 }",
          StringContainer.class);
      if (!"Test String".equals(unmarshalledObject.getInstance())) {
        throw new Fault(
            "Failed to deserialize into a class with less attributes than exist in the JSON string.");
      }
    } catch (Exception x) {
      throw new Fault(
          "An exception is not expected when coming across a non existent attribute during deserialization.");
    }

    return Status.passed("OK");
  }
}
