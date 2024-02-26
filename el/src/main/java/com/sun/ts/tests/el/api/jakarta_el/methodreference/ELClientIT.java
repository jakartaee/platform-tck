/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
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
package com.sun.ts.tests.el.api.jakarta_el.methodreference;

import java.util.Properties;


import com.sun.ts.tests.el.common.util.Fault;
import com.sun.ts.tests.el.common.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.VarMapperELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.MethodsBean;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import jakarta.el.MethodNotFoundException;
import jakarta.el.MethodReference;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.ValueExpression;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private static final String NL = System.getProperty("line.seperator", "\n");

  private Properties testProps;

  public ELClientIT(){
    this.testProps = System.getProperties();
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
   * @testName: methodReferenceTest
   * 
   * @assertion_ids: EL:JAVADOC:87; EL:JAVADOC:88; EL:JAVADOC:89
   * @test_Strategy: Validate the behavior of MethodReference class methods:
   *                 MethodReference.getBase() MethodReference.getMethodInfo()
   *                 MethodReference.getAnnotations()
   *                 MethodReference.getEvaluatedParameters
   */
  @Test
  public void methodReferenceTest() throws Fault {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      ELContext context = (new VarMapperELContext(testProps)).getELContext();
      
      MethodsBean bean = new MethodsBean();
      ValueExpression ve = expFactory.createValueExpression(bean, MethodsBean.class);
      context.getVariableMapper().setVariable("bean", ve);

      // case 1: non-null return value
      MethodExpression mexp1 = expFactory.createMethodExpression(
          context, "#{bean.targetF('aaa',1234)}", String.class, null);
      MethodInfo minfo1 = mexp1.getMethodInfo(context);
      MethodReference mref1 = mexp1.getMethodReference(context);
      pass1 = ExpressionTest.testMethodReference(mref1, bean, minfo1,
          new Class<?>[] { Deprecated.class },
          new Object[] { "aaa", Long.valueOf(1234) },
          buf);

      // case 2: NPE
      try {
        mexp1.getMethodReference(null);
        pass2 = false;
        buf.append("Did not get expected NullPointerException for null context." + NL);
      } catch (NullPointerException npe) {
        pass2 = true;
      }

      // case 3: PNFE
      MethodExpression mexp3 = expFactory.createMethodExpression(
          context, "#{noSuchBean.method()}", String.class, null);
      try {
        mexp3.getMethodReference(context);
        pass3 = false;
        buf.append("Did not get expected PropertyNotFoundException return for unknown bean." + NL);
      } catch (PropertyNotFoundException pnfe) {
        pass3 = true;
      }

      // case 4: MNFE
      MethodExpression mexp4 = expFactory.createMethodExpression(
          context, "#{bean.noSuchMethod()}", String.class, null);
      try {
        mexp4.getMethodReference(context);
        pass4 = false;
        buf.append("Did not get expected MethodNotFoundException return for unknown bean." + NL);
      } catch (MethodNotFoundException mnfe) {
        pass4 = true;
      }
      
    } catch (Exception ex) {
      logger.log(Logger.Level.ERROR, "Test getMethodReference threw an Exception!" + ELTestUtil.NL +
          "Received: " + ex.toString() + ELTestUtil.NL);
      ex.printStackTrace();
      throw new Fault(ex);
    }

    if (!(pass1 && pass2 && pass3 && pass4))
      throw new Fault(ELTestUtil.FAIL + NL + buf.toString());
  }
}
