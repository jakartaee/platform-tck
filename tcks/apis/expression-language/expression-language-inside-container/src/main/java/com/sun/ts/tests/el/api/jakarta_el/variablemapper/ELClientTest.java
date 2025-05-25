/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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
 * $Id: $
 */

package com.sun.ts.tests.el.api.jakarta_el.variablemapper;

import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.elcontext.VarMapperELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.System.Logger;
import java.util.Properties;

@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientTest extends ServiceEETest {

  private static final Logger logger = System.getLogger(ELClientTest.class.getName());

  private Properties testProps = System.getProperties();

  public static void main(String[] args) {
    ELClientTest theTests = new ELClientTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Exception {
  }

  /**
   * @testName: variableMapperTest
   * 
   * @assertion_ids: EL:JAVADOC:37; EL:JAVADOC:116; EL:JAVADOC:117
   * @test_Strategy: Validate the behavior of ELContext.getVariableMapper()
   *                 VariableMapper.resolveVariable()
   *                 VariableMapper.setVariable()
   */
  @Test
  public void variableMapperTest() throws Exception {

    StringBuffer buf = new StringBuffer();

    boolean pass = true;

    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    VarMapperELContext context = new VarMapperELContext(testProps);
    VariableMapper varMapper = context.getVariableMapper();
    TestUtil.logTrace("VariableMapper is " + varMapper.toString());

    if (varMapper.resolveVariable("foo") != null) {
      pass = false;
      buf.append("Expected call to resolveVariable() to unassigned "
          + "variable to return null" + TestUtil.NEW_LINE
          + "Instead call returned " + varMapper.resolveVariable("foo")
          + TestUtil.NEW_LINE);
    }

    ValueExpression vexp1 = expFactory.createValueExpression(context, "${bar}",
        String.class);
    ValueExpression vexp2 = varMapper.setVariable("foo", vexp1);

    if (vexp2 != null) {
      pass = false;
      buf.append("Expected call to setVariable() to return null "
          + "for previously unassigned variable" + TestUtil.NEW_LINE
          + "Instead return value was " + vexp2 + TestUtil.NEW_LINE);
    }

    ValueExpression vexp3 = varMapper.resolveVariable("foo");

    if (!vexp3.equals(vexp1)) {
      pass = false;
      buf.append("Expected call to resolveVariable() to assigned "
          + "variable to return " + vexp1.toString() + TestUtil.NEW_LINE
          + "Instead call returned " + vexp3.toString() + TestUtil.NEW_LINE);
    }

    ValueExpression vexp4 = varMapper.setVariable("foo", null);

    if (!vexp4.equals(vexp1)) {
      pass = false;
      buf.append("Expected call to resolveVariable() to assigned "
          + "variable to return " + vexp1.toString() + TestUtil.NEW_LINE
          + "Instead call returned " + vexp4.toString() + TestUtil.NEW_LINE);
    }

    ValueExpression vexp5 = varMapper.resolveVariable("foo");

    if (vexp5 != null) {
      pass = false;
      buf.append("Expected call to resolveVariable() to return null"
          + " after unassignment" + TestUtil.NEW_LINE
          + "Instead return value was " + vexp5 + TestUtil.NEW_LINE);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
  }
}
