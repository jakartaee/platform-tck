/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.core.iteration.foreach;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_core_iter_foreach_web");
    setGoldenFileDir("/jstl/spec/core/iteration/foreach");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveVarTest
   * 
   * @assertion_ids: JSTL:SPEC:21.1; JSTL:SPEC:21.1.1
   * 
   * @testStrategy: Validated the behavior of the 'var' attribute. - the type
   * should be the type of the object in the underlying collection. - the
   * exported var has nested visibility meaning if the variable name reference
   * by var, previously existed, it should no longer exist after completion of
   * the action.
   */
  public void positiveVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveVarTest");
    invoke();
  }

  /*
   * @testName: positiveItemsPrimArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1.2
   * 
   * @testStrategy: Validate that arrays of all primitive types can be handled
   * by 'forEach' and that the values in the underlying array are wrapped with
   * its corresponding wrapper type.
   */
  public void positiveItemsPrimArrayTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsPrimArrayTest");
    invoke();
  }

  /*
   * @testName: positiveItemsObjArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1.1
   * 
   * @testStrategy: Validate that arrays of Object types can be handled by
   * 'forEach'.
   */
  public void positiveItemsObjArrayTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsObjArrayTest");
    invoke();
  }

  /*
   * @testName: positiveItemsCollectionTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.2
   * 
   * @testStrategy: Validate that 'forEach' can handle various Collection
   * objects.
   */
  public void positiveItemsCollectionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsCollectionTest");
    invoke();
  }

  /*
   * @testName: positiveItemsEnumerationTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.4
   * 
   * @testStrategy: Validate that 'forEach' can handle an Enumeration
   */
  public void positiveItemsEnumerationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsEnumerationTest");
    invoke();
  }

  /*
   * @testName: positiveItemsIteratorTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.3
   * 
   * @testStrategy: Validate that 'forEach' can handle an Iterator
   */
  public void positiveItemsIteratorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveItemsIteratorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveItemsMapTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.5.1; JSTL:SPEC:21.2.5.1.1;
   * JSTL:SPEC:21.2.5.1.2
   * 
   * @testStrategy: Validate that 'forEach' can handle Map objects
   */
  public void positiveItemsMapTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveItemsMapTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveItemsCommaSepStringTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.7
   * 
   * @testStrategy: Validate that 'forEach' can handle a comma-separated string
   */
  public void positiveItemsCommaSepStringTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsCommaSepStringTest");
    invoke();
  }

  /*
   * @testName: positiveItemsBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:21.5.1; JSTL:SPEC:21.5.1.1; JSTL:SPEC:21.5.3
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'begin'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'begin' attribute is specified
   */
  public void positiveItemsBeginTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsBeginTest");
    invoke();
  }

  /*
   * @testName: positiveItemsEndTest
   * 
   * @assertion_ids: JSTL:SPEC:21.6.1; JSTL:SPEC:21.6.3
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'end'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'end' attribute is specified
   */
  public void positiveItemsEndTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsEndTest");
    invoke();
  }

  /*
   * @testName: positiveItemsStepTest
   * 
   * @assertion_ids: JSTL:SPEC:21.7.1
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'step'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'step' attribute is specified
   */
  public void positiveItemsStepTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsStepTest");
    invoke();
  }

  /*
   * @testName: positiveVarStatusTest
   * 
   * @assertion_ids: JSTL:SPEC:21.4
   * 
   * @testStrategy: Validate that varStatus is properly exported with nested
   * scope and is of type javax.servlet.jsp.jstl. LoopTagStatus.
   */
  public void positiveVarStatusTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveVarStatusTest");
    invoke();
  }

  /*
   * @testName: positiveNoItemsIterationTest
   * 
   * @assertion_ids: JSTL:SPEC:21.3; JSTL:SPEC:21.5.2; JSTL:SPEC:21.6.2
   * 
   * @testStrategy: Validate tag behavior when no 'items' attribute is
   * specified.
   */
  public void positiveNoItemsIterationTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveNoItemsIterationTest");
    invoke();
  }

  /*
   * @testName: positiveBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:21.8
   * 
   * @testStrategy: Validate that the 'forEach' action can handle body content
   * as well as an empty body.
   */
  public void positiveBodyBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveItemsNullTest
   * 
   * @assertion_ids: JSTL:SPEC:21.12
   * 
   * @testStrategy: Validate that no iteration is performed by forEach if items
   * is null.
   */
  public void positiveItemsNullTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveItemsNullTest");
    invoke();
  }

  /*
   * @testName: negativeFEItemsTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.8
   * 
   * @testStrategy: Validate that a javax.servlet.jsp.JspException is thrown if
   * the EL expression passed to items evaluates to an incorrect type.
   */
  public void negativeFEItemsTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeFEItemsTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEBeginTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.5.4
   * 
   * @testStrategy: Validate that a javax.servlet.jsp.JspException is thrown if
   * the EL expression passed to begin evaluates to an incorrect type.
   */
  public void negativeFEBeginTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeFEBeginTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEEndTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.6.3
   * 
   * @testStrategy: Validate that a javax.servlet.jsp.JspException is thrown if
   * the EL expression passed to end evaluates to an incorrect type.
   */
  public void negativeFEEndTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeFEEndTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEStepTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.7.2
   * 
   * @testStrategy: Validate that a javax.servlet.jsp.JspException is thrown if
   * the EL expression passed to step evaluates to an incorrect type.
   */
  public void negativeFEStepTypeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeFEStepTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:21.13
   * 
   * @testStrategy: Validate that an exception caused by the body content is
   * propagated.
   */
  public void negativeFEExcBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeFEExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveForEachEndLTBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:21.15
   * 
   * @test_Strategy: Validate an end attribute value that is less than the begin
   * attribute value will result in the action not being executed.
   */
  public void positiveForEachEndLTBeginTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveForEachEndLTBeginTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest1
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Add some items to a Vector. In a c:forEach tag, reference
   * the Vector as a deferred value in the items attribute. In the body of the
   * tag, set each item to have application scope. Verify that the items can be
   * retrieved after the execution of the tag.
   */
  public void positiveForEachDeferredValueTest1() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest1");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest2
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Create a String containing several items delimited by
   * spaces. In a c:forEach tag, reference the String as a deferred value in the
   * items attribute. In the body of the tag, set each item to have application
   * scope. Verify that the items can be retrieved after the execution of the
   * tag.
   */
  public void positiveForEachDeferredValueTest2() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest2");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest3
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Create a HashMap containing several items. In a c:forEach
   * tag, reference the HashMap as a deferred value in the items attribute. In
   * the body of the tag, set each item to have application scope. Verify that
   * the items can be retrieved after the execution of the tag.
   */
  public void positiveForEachDeferredValueTest3() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest3");
    invoke();
  }
}
