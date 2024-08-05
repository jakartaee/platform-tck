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

import java.util.Properties;



import com.sun.ts.tests.el.common.elcontext.VarMapperELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private Properties testProps;

  public ELClientIT() {
    this.testProps=System.getProperties();
  }

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
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
    logger.log(Logger.Level.TRACE, "VariableMapper is " + varMapper.toString());

    if (varMapper.resolveVariable("foo") != null) {
      pass = false;
      buf.append("Expected call to resolveVariable() to unassigned "
          + "variable to return null" + ELTestUtil.NL
          + "Instead call returned " + varMapper.resolveVariable("foo")
          + ELTestUtil.NL);
    }

    ValueExpression vexp1 = expFactory.createValueExpression(context, "${bar}",
        String.class);
    ValueExpression vexp2 = varMapper.setVariable("foo", vexp1);

    if (vexp2 != null) {
      pass = false;
      buf.append("Expected call to setVariable() to return null "
          + "for previously unassigned variable" + ELTestUtil.NL
          + "Instead return value was " + vexp2 + ELTestUtil.NL);
    }

    ValueExpression vexp3 = varMapper.resolveVariable("foo");

    if (!vexp3.equals(vexp1)) {
      pass = false;
      buf.append("Expected call to resolveVariable() to assigned "
          + "variable to return " + vexp1.toString() + ELTestUtil.NL
          + "Instead call returned " + vexp3.toString() + ELTestUtil.NL);
    }

    ValueExpression vexp4 = varMapper.setVariable("foo", null);

    if (!vexp4.equals(vexp1)) {
      pass = false;
      buf.append("Expected call to resolveVariable() to assigned "
          + "variable to return " + vexp1.toString() + ELTestUtil.NL
          + "Instead call returned " + vexp4.toString() + ELTestUtil.NL);
    }

    ValueExpression vexp5 = varMapper.resolveVariable("foo");

    if (vexp5 != null) {
      pass = false;
      buf.append("Expected call to resolveVariable() to return null"
          + " after unassignment" + ELTestUtil.NL
          + "Instead return value was " + vexp5 + ELTestUtil.NL);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
  }
}
