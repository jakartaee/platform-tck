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

package com.sun.ts.tests.jsonb.api.jsonbadapter;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.api.model.SimpleContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleContainerContainer;
import com.sun.ts.tests.jsonb.api.model.SimpleStringAdapter;

/**
 * @test
 * @sources JsonbAdapterTest.java
 * @executeClass com.sun.ts.tests.jsonb.api.JsonbAdapterTest
 **/
public class JsonbAdapterTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new JsonbAdapterTest();
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
   * @testName: testAdaptFromJson
   *
   * @assertion_ids: JSONB:JAVADOC:53
   *
   * @test_Strategy: Assert that JsonbAdapter.adaptFromJson method can be
   * configured during object deserialization to provide conversion logic from
   * adapted object to original
   */
  public Status testAdaptFromJson() throws Fault {
    Jsonb jsonb = JsonbBuilder
        .create(new JsonbConfig().withAdapters(new SimpleStringAdapter()));
    SimpleContainerContainer unmarshalledObject = jsonb.fromJson(
        "{ \"instance\" : { \"instance\" : \"Test String Adapted\" } }",
        SimpleContainerContainer.class);
    if (!"Test String".equals(unmarshalledObject.getInstance().getInstance())) {
      throw new Fault(
          "Failed to use JsonbAdapter.adaptFromJson method to provide conversion logic from adapted object to original during object deserialization.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testAdaptToJson
   *
   * @assertion_ids: JSONB:JAVADOC:55
   *
   * @test_Strategy: Assert that JsonbAdapter.adaptToJson method can be
   * configured during object serialization to provide conversion logic from
   * original object to adapted
   */
  public Status testAdaptToJson() throws Fault {
    Jsonb jsonb = JsonbBuilder
        .create(new JsonbConfig().withAdapters(new SimpleStringAdapter()));
    String jsonString = jsonb.toJson(new SimpleContainerContainer() {
      {
        setInstance(new SimpleContainer() {
          {
            setInstance("Test String");
          }
        });
      }
    });
    if (!jsonString.matches(
        "\\{\\s*\"instance\"\\s*:\\s*\\{\\s*\"instance\"\\s*:\\s*\"Test String Adapted\"\\s*}\\s*}")) {
      throw new Fault(
          "Failed to use JsonbAdapter.adaptToJson method to provide conversion logic from original object to adapted during object serialization.");
    }

    return Status.passed("OK");
  }
}
