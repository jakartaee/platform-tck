/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.api.javax_el.functionmapper;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.el.FunctionMapper;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.elcontext.FuncMapperELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: functionMapperTest
   * 
   * @assertion_ids: EL:JAVADOC:35; EL:JAVADOC:67
   * @test_Strategy: Validate the behavior of ELContext.getFunctionMapper()
   *                 FunctionMapper.resolveFunction()
   */

  public void functionMapperTest() throws Fault {

    String expected = "public static java.lang.Integer "
        + "java.lang.Integer.valueOf"
        + "(java.lang.String) throws java.lang.NumberFormatException";

    FuncMapperELContext context = new FuncMapperELContext();
    FunctionMapper funcMapper = context.getFunctionMapper();
    TestUtil.logTrace("FunctionMapper is " + funcMapper.toString());

    if (funcMapper.resolveFunction("foo", "bar") != null) {
      TestUtil.logErr("Expected call to resolveFunction() to unassigned "
          + "function to return null" + TestUtil.NEW_LINE
          + "Instead call returned: "
          + funcMapper.resolveFunction("foo", "bar").getName()
          + TestUtil.NEW_LINE);

      throw new Fault("Resolved unassigned function");
    }

    Method method = funcMapper.resolveFunction("Int", "val");
    if (method == null) {
      TestUtil.logErr("Expected call to resolveFunction() to resolvable "
          + "function to return a non-null value" + TestUtil.NEW_LINE);

      throw new Fault("Incorrect resolution: null method");
    } else {
      String methodSignature = method.toString();
      if (!methodSignature.equals(expected)) {
        TestUtil.logErr("Method Signature of resolved function is " + "invalid"
            + TestUtil.NEW_LINE + "Expected value:" + expected
            + TestUtil.NEW_LINE);

        throw new Fault("Incorrect resolution: wrong method Signature");
      }
    }
  }
}
