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

package com.sun.ts.tests.jsonb.defaultmapping.interfaces;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.TypeContainer;
import com.sun.ts.tests.jsonb.defaultmapping.interfaces.model.InterfaceContainer;

import java.util.Properties;

/**
 * @test
 * @sources InterfaceMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.interfaces.InterfaceMappingTest
 **/
public class InterfaceMappingTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  private final Jsonb jsonb = JsonbBuilder.create();

  public static void main(String[] args) {
    EETest t = new InterfaceMappingTest();
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
   * @testName: testDeserializationToInterface
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-2
   *
   * @test_Strategy: Assert that an error is reported when unmarshalling to an
   * arbitrary interface
   */
  public Status testDeserializationToInterface() throws Fault {
    try {
      jsonb.fromJson("{ \"instance\" : \"Test String\" }", TypeContainer.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class to an interface.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }

  /*
   * @testName: testInterfaceField
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.10-2
   *
   * @test_Strategy: Assert that an error is reported when unmarshalling to an
   * arbitrary interface
   */
  public Status testInterfaceField() throws Fault {
    try {
      jsonb.fromJson("{ \"instance\" : { \"instance\" : \"Test String\" } }",
          InterfaceContainer.class);
      throw new Fault(
          "An exception is expected when unmarshalling a class with an interface property.");
    } catch (JsonbException x) {
      return Status.passed("OK");
    }
  }
}
