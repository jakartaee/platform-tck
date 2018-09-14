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

package com.sun.ts.tests.jsonb.defaultmapping.bignumbers;

import java.math.BigDecimal;
import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;

/**
 * @test
 * @sources BigNumbersMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.bignumbers.BigNumbersMappingTest
 **/
public class BigNumbersMappingTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new BigNumbersMappingTest();
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
   * @testName: testBigNumberMarshalling
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.16-1
   *
   * @test_Strategy: Assert that numbers of greater magnitude or precision than
   * IEEE 754 are serialized as strings as specified by IJSON RFC 7493 (default
   * to JSON-B)
   */
  @SuppressWarnings("unused")
  public void testBigNumberMarshalling() throws Fault {
    String jsonString = jsonb.toJson(new Object() {
      public Number number = new BigDecimal("0.10000000000000001");
    });
    if (!jsonString
        .matches("\\{\\s*\"number\"\\s*:\\s*\"0.10000000000000001\"\\s*\\}")) {
      throw new Fault(
          "Failed to correctly marshal number of greater precision than IEEE 754 as string.");
    }
  }
}
