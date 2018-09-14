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

package com.sun.ts.tests.jstl.spec.fmt.i18n.resourcelookup;

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

    setContextRoot("/jstl_fmt_reslook_web");
    setGoldenFileDir("/jstl/spec/fmt/i18n/resourcelookup");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveResourceBundleLookupTest
   * 
   * @assertion_ids: JSTL:SPEC:26; JSTL:SPEC:26.2; JSTL:SPEC:26.2.1;
   * JSTL:SPEC:26.2.1.1; JSTL:SPEC:26.2.2; JSTL:SPEC:26.2.4; JSTL:SPEC:29.1.3
   * 
   * @testStrategy: Validate that the resource bundle lookup algorithm works as
   * specified when using the fmt:bundle action. This test is based on the 4
   * examples listed in section 8.3.3 of the JSTL specification.
   */
  public void positiveResourceBundleLookupTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatBundleResourceLookup.jsp?res=AlgoResources2&fall=fr_CA");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-GB, fr-CA");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en_GB message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatBundleResourceLookup.jsp?res=AlgoResources3&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: de, fr");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatBundleResourceLookup.jsp?res=AlgoResources4&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Accept-Language: ja, en-GB, en-US, en-CA, fr");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en_GB message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatBundleResourceLookup.jsp?res=AlgoResources5&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: fr, sw");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: sw message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatBundleResourceLookup.jsp?res=AlgoResources&fall=ja");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: default message");
    invoke();
  }

  /*
   * @testName: positiveResourceSetBundleLookupTest
   * 
   * @assertion_ids: JSTL:SPEC:26; JSTL:SPEC:26.2; JSTL:SPEC:26.2.1;
   * JSTL:SPEC:26.2.1.1; JSTL:SPEC:26.2.2; JSTL:SPEC:26.2.4; JSTL:SPEC:92.2
   * 
   * @testStrategy: Validate that the resource bundle lookup algorithm works as
   * specified when using the fmt:bundle action. This test is based on the 4
   * examples listed in section 8.3.3 of the JSTL specification.
   */
  public void positiveResourceSetBundleLookupTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatSetBundleResourceLookup.jsp?res=AlgoResources2&fall=fr_CA");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-GB, fr-CA");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en_GB message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatSetBundleResourceLookup.jsp?res=AlgoResources3&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: de, fr");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatSetBundleResourceLookup.jsp?res=AlgoResources4&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Accept-Language: ja, en-GB, en-US, en-CA, fr");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: en_GB message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatSetBundleResourceLookup.jsp?res=AlgoResources5&fall=en");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: fr, sw");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: sw message");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetResourceBundleLookupTest");
    TEST_PROPS.setProperty(REQUEST,
        "formatSetBundleResourceLookup.jsp?res=AlgoResources&fall=ja");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "message: default message");
    invoke();
  }
}
