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

package com.sun.ts.tests.el.spec.emptyoperator;

import com.sun.javatest.Status;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.NameValuePair;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Properties;

public class ELClient extends ServiceEETest {

  Properties testProps;

  private final boolean[] deferred = { true, false };

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
  }

  /*
   * @testName: elEmptyNullTest
   * 
   * @assertion_ids: EL:SPEC:25.1
   * 
   * @test_Strategy: Validate that if "null" is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  public void elEmptyNullTest() throws Fault {

    boolean pass = false;

    String[] symbols = { "$", "#" };
    boolean expectedResult = true;

    try {
      for (String prefix : symbols) {
        String expr = prefix + "{empty null}";
        Object result = ExprEval.evaluateValueExpression(expr, null,
            Object.class);

        if (result == null) {
          TestUtil.logTrace("result is null");
        } else {
          TestUtil.logTrace("result is " + result.toString());
        }

        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Fault(e);
    }

  }

  /*
   * @testName: elEmptyStringTest
   * 
   * @assertion_ids: EL:SPEC:25.2; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a String is passed with the Empty
   * operator, the correct Boolean result is returned.
   */
  public void elEmptyStringTest() throws Fault {

    this.testEmptyOperator("", true);

    this.testEmptyOperator("something", false);

  }

  /*
   * @testName: elEmptyArrayTest
   * 
   * @assertion_ids: EL:SPEC:25.3; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Array is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  public void elEmptyArrayTest() throws Fault {

    this.testEmptyOperator(new String[0], true);

    String[] testArray = { "Apple" };
    this.testEmptyOperator(testArray, false);

  }

  /*
   * @testName: elEmptyMapTest
   * 
   * @assertion_ids: EL:SPEC:25.4; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Map is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  public void elEmptyMapTest() throws Fault {

    HashMap testMap = new HashMap();

    this.testEmptyOperator(testMap, true);

    testMap.put("Fruit", "Apple");
    this.testEmptyOperator(testMap, false);

    // Clean out the Map.
    testMap.clear();
  }

  /*
   * @testName: elEmptyCollectionTest
   * 
   * @assertion_ids: EL:SPEC:25.5; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Collection is passed with the Empty
   * operator, the correct Boolean result is returned.
   */
  public void elEmptyCollectionTest() throws Fault {

    ArrayList testCollection = new ArrayList();

    this.testEmptyOperator(testCollection, true);

    testCollection.add("Apple");
    this.testEmptyOperator(testCollection, false);

    // Clean out the Collection.
    testCollection.clear();
  }

  // ---------------------------------------------------------- private methods

  // Test Empty operator.
  private void testEmptyOperator(Object testVal, boolean expectedResult)
      throws Fault {

    boolean pass = false;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(testVal);

    try {
      for (boolean tf : deferred) {
        String expr = ExprEval.buildElExpr(tf, "empty");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Fault(e);
    }
  }

}
