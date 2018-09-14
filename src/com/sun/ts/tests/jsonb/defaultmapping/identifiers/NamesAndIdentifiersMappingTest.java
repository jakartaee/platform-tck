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

package com.sun.ts.tests.jsonb.defaultmapping.identifiers;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.MappingTester;
import com.sun.ts.tests.jsonb.defaultmapping.identifiers.model.StringContainer;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;

/**
 * @test
 * @sources NamesAndIdentifiersMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.identifiers.NamesAndIdentifiersMappingTest
 **/
public class NamesAndIdentifiersMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new NamesAndIdentifiersMappingTest();
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
   * @testName: testSimpleMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.15-1; JSONB:SPEC:JSB-3.15-2;
   * JSONB:SPEC:JSB-3.15-3
   *
   * @test_Strategy: Assert that java field name can be correctly mapped to json
   * identifier and vice versa
   */
  public Status testSimpleMapping() throws Fault {
    return new MappingTester<>(StringContainer.class).test("Test String",
        "\"Test String\"");
  }

  /*
   * @testName:
   * testSimpleMappingNoCorrespondingIdentifierWithFailOnUnknownProperties
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.15-4
   *
   * @test_Strategy: Assert that error is reported if a Java identifier with
   * corresponding name as in json document cannot be found or is not accessible
   */
  public Status testSimpleMappingNoCorrespondingIdentifierWithFailOnUnknownProperties()
      throws Fault {
    try {
      JsonbBuilder
          .create(new JsonbConfig()
              .setProperty("jsonb.fail-on-unknown-properties", true))
          .fromJson("{ \"data\" : \"Test String\" }", StringContainer.class);
      throw new Fault(
          "A JsonbException is expected if a Java identifier with corresponding name as in json document cannot be found.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }
}
