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

package com.sun.ts.tests.jsonb.defaultmapping.enums;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.MappingTester;
import com.sun.ts.tests.jsonb.defaultmapping.enums.model.EnumContainer;
import com.sun.ts.tests.jsonb.defaultmapping.enums.model.EnumContainer.Enumeration;

import java.util.Properties;

import static com.sun.ts.tests.jsonb.MappingTester.combine;

/**
 * @test
 * @sources EnumMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.enums.EnumMappingTest
 **/
public class EnumMappingTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new EnumMappingTest();
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
   * @testName: testEnum
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.9-1; JSONB:SPEC:JSB-3.9-2
   *
   * @test_Strategy: Assert that enum is correctly handled
   */
  public Status testEnum() throws Fault {
    MappingTester<Enumeration> enumMappingTester = new MappingTester<>(
        EnumContainer.class);
    return combine(enumMappingTester.test(Enumeration.ONE, "\"ONE\""),
        enumMappingTester.test(Enumeration.TWO, "\"TWO\""));
  }
}
