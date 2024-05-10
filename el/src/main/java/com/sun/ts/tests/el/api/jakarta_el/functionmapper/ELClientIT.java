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
 * $Id$
 */

package com.sun.ts.tests.el.api.jakarta_el.functionmapper;

import java.lang.reflect.Method;
import java.util.Properties;


import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.elcontext.FuncMapperELContext;

import jakarta.el.FunctionMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());
  
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
   * @testName: functionMapperTest
   * 
   * @assertion_ids: EL:JAVADOC:35; EL:JAVADOC:67
   * @test_Strategy: Validate the behavior of ELContext.getFunctionMapper()
   *                 FunctionMapper.resolveFunction()
   */
  @Test
  public void functionMapperTest() throws Exception {

    String expected = "public static java.lang.Integer "
        + "java.lang.Integer.valueOf"
        + "(java.lang.String) throws java.lang.NumberFormatException";

    FuncMapperELContext context = new FuncMapperELContext();
    FunctionMapper funcMapper = context.getFunctionMapper();
    logger.log(Logger.Level.TRACE, "FunctionMapper is " + funcMapper.toString());

    if (funcMapper.resolveFunction("foo", "bar") != null) {
      logger.log(Logger.Level.ERROR, "Expected call to resolveFunction() to unassigned "
          + "function to return null" + ELTestUtil.NL
          + "Instead call returned: "
          + funcMapper.resolveFunction("foo", "bar").getName()
          + ELTestUtil.NL);

      throw new Exception("Resolved unassigned function");
    }

    Method method = funcMapper.resolveFunction("Int", "val");
    if (method == null) {
      logger.log(Logger.Level.ERROR, "Expected call to resolveFunction() to resolvable "
          + "function to return a non-null value" + ELTestUtil.NL);

      throw new Exception("Incorrect resolution: null method");
    } else {
      String methodSignature = method.toString();
      if (!methodSignature.equals(expected)) {
        logger.log(Logger.Level.ERROR, "Method Signature of resolved function is " + "invalid"
            + ELTestUtil.NL + "Expected value:" + expected
            + ELTestUtil.NL);

        throw new Exception("Incorrect resolution: wrong method Signature");
      }
    }
  }
}
