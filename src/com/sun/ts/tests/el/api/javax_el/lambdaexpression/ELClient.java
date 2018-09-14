/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.api.javax_el.lambdaexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.LambdaExpression;
import javax.el.StandardELContext;
import javax.el.ExpressionFactory;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
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
    TestUtil.logTrace("Cleanup method called");
  }

  /**
   * @testName: invokeNPETest
   * 
   * @assertion_ids: EL:JAVADOC:406
   * 
   * @test_Strategy: Throws a NullPointerException if elContext is null.
   */

  public void invokeNPETest() throws Fault {
    List<String> params = new ArrayList<String>();
    params.add("one");
    LambdaExpression le = new LambdaExpression(params, null);

    ELTestUtil.checkForNPE(le, "invoke",
        new Class<?>[] { ELContext.class, Object[].class },
        new Object[] { null, new Object[] { "one" } });
  }// End invokeNPETest

  /**
   * @testName: invokeELETest
   * 
   * @assertion_ids: EL:JAVADOC:408
   * 
   * @test_Strategy: Throws a ELException if not enough arguments are provided.
   */

  public void invokeELETest() throws Fault {
    StringBuffer buff = new StringBuffer();
    boolean pass = false;
    List<String> params = new ArrayList<String>();
    params.add("one");

    LambdaExpression le = new LambdaExpression(params, null);
    try {
      le.invoke(new StandardELContext(ExpressionFactory.newInstance()),
          new Object[] {});
      buff.append(ELTestUtil.FAIL + " No Exception thrown!" + ELTestUtil.NL
          + "Expected an ELException to be thrown!");
    } catch (ELException ele) {
      TestUtil.logMsg(ELTestUtil.PASS);
      pass = true;
    } catch (Exception e) {
      TestUtil.logMsg(ELTestUtil.FAIL + ELTestUtil.NL + "Expected: ELException"
          + ELTestUtil.NL + "Received: " + e.getClass().getSimpleName());
    }

    if (!pass)
      throw new Fault(ELTestUtil.FAIL);
  }// End invokeELETest

}
