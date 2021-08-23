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

package com.sun.ts.tests.jsf.spec.navigation;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_nav_web";

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: navHandlerNullOutcomeTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the current view ID stored in the FacesContext isn't
   *                 changed if the outcome passed to handleNavigation() is
   *                 null.
   */
  public void navHandlerNullOutcomeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerNullOutcomeTest");
    invoke();
  }

  /**
   * @testName: navHandlerFromViewExactMatchTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the expected view ID is stored in the FacesContext
   *                 when an exact match is found in the navigation-rules.
   */
  public void navHandlerFromViewExactMatchTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerFromViewExactMatchTest");
    invoke();
  }

  /**
   * @testName: navHandlerFromViewPatternMatchTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the expected view ID is stored in the FacesContext
   *                 when a pattern match is performed on the navigation rules.
   *                 If the proper is is returned in this case, the
   *                 implementation correctly used the longest matching pattern.
   */
  public void navHandlerFromViewPatternMatchTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerFromViewPatternMatchTest");
    invoke();
  }

  /**
   * @testName: navHandlerFromViewAsteriskOnlyTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the expected view ID is stored in the FacesContext
   *                 when the from-view-id doesn't match any of the exact match
   *                 or pattern match rules and a navigation-rule with a
   *                 from-view-id of '*' exists.
   */
  public void navHandlerFromViewAsteriskOnlyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerFromViewAsteriskOnlyTest");
    invoke();
  }

  /**
   * @testName: navHandlerFromViewSearchOrderTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the search order of navigation rules: 1. Exact
   *                 Matches 2. Pattern Matches 3. Asterisk Match
   */
  public void navHandlerFromViewSearchOrderTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerFromViewSearchOrderTest");
    invoke();
  }

  /**
   * @testName: navHandlerNavigationCaseTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure the correct processing of navigation-cases within a
   *                 matching navigation rule: 1. a matching from-action and
   *                 from-outcome 2. a matching from-outcome 3. a matching
   *                 from-action 4. navigation case present with no from-outcome
   *                 or from-action
   */
  public void navHandlerNavigationCaseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerNavigationCaseTest");
    invoke();
  }

  /**
   * @testName: navHandlerRedirectTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure HttpServletRequest.sendRedirect is called with the
   *                 expected context-relative path if the 'redirect' element is
   *                 found within a matching navigation case. Additionally
   *                 ensure FacesContext. responseComplete() is called.
   */
  public void navHandlerRedirectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerRedirectTest");
    invoke();
  }

  /**
   * @testName: navHandlerNoNavCaseGoToNextRuleTest
   * @assertion_ids: PENDING: add assertion ID(s)
   * @test_Strategy: Ensure that the implementation continues to process
   *                 navigation-rules if the first matching rule doesn't contain
   *                 a matching navigation-case.
   */
  public void navHandlerNoNavCaseGoToNextRuleTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "navHandlerNoNavCaseGoToNextRuleTest");
    invoke();
  }

} // end of URLClient
