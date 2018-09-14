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

package com.sun.ts.tests.jstl.spec.core.general.set;

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

    setContextRoot("/jstl_core_gen_set_web");
    setGoldenFileDir("/jstl/spec/core/general/set");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveSetTest
   * 
   * @assertion_ids: JSTL:SPEC:13.1; JSTL:SPEC:13.1.1; JSTL:SPEC:13.3
   * 
   * @testStrategy: Validate that the dynamic and static values provided to the
   * 'value' attribute are made available to the test page by using <c:out> to
   * print the values.
   */
  public void positiveSetTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetTest");
    invoke();
  }

  /*
   * @testName: positiveSetBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:13.2
   * 
   * @testStrategy: Validate the value of the exported var can be set within the
   * body of the <c:set> action. Verify the result using <c:out>
   */
  public void positiveSetBodyBehaviorTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:13.4.1; JSTL:SPEC:13.4.2; JSTL:SPEC:13.4.3;
   * JSTL:SPEC:13.4.4; JSTL:SPEC:13.5
   * 
   * @testStrategy: Validated the different scope behaviors (default and
   * explicitly set scopes) by exporting different vairables to the assorted
   * scopes and then print the values using PageContext.getAttribute(String,
   * int).
   */
  public void positiveSetScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetNullValueTest
   * 
   * @assertion_ids: JSTL:SPEC:13.7
   * 
   * @testStrategy: Validate the following: - if setting a scoped variable and
   * value is null, the variable referenced by var and scope is removed. This is
   * validated by setting a scoped variable, and then calling remove using that
   * var name. Then validate that the variable is indeed removed by using an out
   * action with that variable (which is now null. - If setting a property in a
   * Map and value is null, the key/value is removed from the map. - If setting
   * a property in a Bean and value is null, will return null when calling the
   * get method for that propery after the set completes.
   */
  public void positiveSetNullValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetNullValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetPropertyTest
   * 
   * @assertion_ids: JSTL:SPEC:13.10.1
   * 
   * @testStrategy: Validate that the set action can set properties of JavaBeans
   * and set key/values in Map instances using the target and property
   * attributes.
   */
  public void positiveSetPropertyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropertyTest");
    invoke();
  }

  /*
   * @testName: negativeSetBodyContextExcTest
   * 
   * @assertion_ids: JSTL:SPEC:13.9
   * 
   * @testStrategy: Validate that if the body content of the action is thrown,
   * that it is properly propagated.
   */
  public void negativeSetBodyContextExcTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeSetBodyContentExcTest");
    invoke();
  }

  /*
   * @testName: negativeSetTargetNullInvalidObjTest
   * 
   * @assertion_ids: JSTL:SPEC:13.10.2; JSTL:SPEC:13.10.3
   * 
   * @testStrategy: Validate that a javax.servet.jsp.JspException is thrown if
   * the target attribute is null, or is provided an object that is no a
   * JavaBean or an instance of java.util.Map.
   */
  public void negativeSetTargetNullInvalidObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeSetTargetNullInvalidObjTest");
    invoke();
  }

  /*
   * @testName: positiveSetDeferredValueTest
   * 
   * @assertion_ids: JSTL:SPEC:13.18
   * 
   * @test_Strategy: In a c:set tag, assign a deferred value to a variable. In a
   * second tag, set the variable to have application scope. Verify that the
   * value can be retrieved after the execution of the tag.
   */
  public void positiveSetDeferredValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetDeferredValueTest");
    invoke();
  }
}
