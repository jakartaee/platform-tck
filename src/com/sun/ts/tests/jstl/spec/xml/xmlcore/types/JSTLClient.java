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

package com.sun.ts.tests.jstl.spec.xml.xmlcore.types;

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

    setContextRoot("/jstl_xml_types_web");
    setGoldenFileDir("/jstl/spec/xml/xmlcore/types");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveJavaToXPathTypesTest
   * 
   * @assertion_ids: JSTL:SPEC:65; JSTL:SPEC:65.1; JSTL:SPEC:65.2;
   * JSTL:SPEC:65.3; JSTL:SPEC:65.4; JSTL:SPEC:65.5
   * 
   * @testStrategy: Validate that XPath variables of Java types, can be properly
   * used in XPath expressions. The supported type mappings are: Java XPath
   * java.lang.Boolean boolean java.lang.Number number java.lang.String string
   * Object exported by parse, set, or forEach node-set
   */
  public void positiveJavaToXPathTypesTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveJavaToXPathTypesTest");
    invoke();
  }

  /*
   * @testName: positiveXPathToJavaTypesTest
   * 
   * @assertion_ids: JSTL:SPEC:66; JSTL:SPEC:66.1; JSTL:SPEC:66.2;
   * JSTL:SPEC:66.3; JSTL:SPEC:66.4
   * 
   * @testStrategy: Validate that the result of an XPath expression yeilds the
   * correct type based of the specified XPath to Java type mapping: XPath Java
   * boolean java.lang.Boolean number java.lang.Number string java.lang.String
   * node-set Implementation specified (test will check for java.lang.Object)
   */
  public void positiveXPathToJavaTypesTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveXPathToJavaTypesTest");
    invoke();
  }

}
