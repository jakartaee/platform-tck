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
package com.sun.ts.tests.jsf.spec.templating.repeat;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_templating_repeat_web";

  private List<String> testColors;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /**
   * @testName: templateUIRepeatVarTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that the "var" attribute is handled correctly.
   * 
   * @since 2.0
   */
  public void templateUIRepeatVarTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = (getPage(CONTEXT_ROOT + "/faces/repeatVar.xhtml"));

    testColors = new ArrayList<String>();

    testColors.add("Red");
    testColors.add("Green");
    testColors.add("Blue");
    testColors.add("Violet");
    testColors.add("Pink");

    testList(page, "Color:", testColors, 5, formatter, true);

    handleTestStatus(messages);

  }

  /**
   * @testName: templateUIRepeatOffsetTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that the "offset" attribute is handled correctly.
   * 
   * @since 2.0
   */
  public void templateUIRepeatOffsetTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    testColors = new ArrayList<String>();

    testColors.add("Violet");
    testColors.add("Pink");

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/repeatOffset.xhtml");

    testList(page, "Color:", testColors, 2, formatter, true);

    handleTestStatus(messages);

  }

  /**
   * @testName: templateUIRepeatVarStatusTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that the "varStatus" attribute is handled
   *                 correctly.
   * 
   *                 Test the following attributes: case 1 - "index" case 2 -
   *                 "first" case 3 - "last" case 4 - "begin" case 5 - "end"
   *                 case 6 - "step" case 7 - "odd" case 8 - "even"
   * 
   * @since 2.0
   */
  public void templateUIRepeatVarStatusTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    testColors = new ArrayList<String>();

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/repeatVarStat.xhtml");

    // -------------------------------------------------------------- case 1
    List<String> index = new ArrayList<String>();

    index.add("0");
    index.add("1");
    index.add("2");
    index.add("3");
    index.add("4");

    testList(page, "VSIndex", index, 5, formatter, true);

    // -------------------------------------------------------------- case 2
    List<String> first = new ArrayList<String>();

    first.add("true");
    first.add("false");
    first.add("false");
    first.add("false");
    first.add("false");

    testList(page, "VSFirst", first, 5, formatter, true);

    // -------------------------------------------------------------- case 3
    List<String> last = new ArrayList<String>();

    last.add("false");
    last.add("false");
    last.add("false");
    last.add("false");
    last.add("true");

    testList(page, "VSLast", last, 5, formatter, true);

    // Commented out due to the fact that the spec is not clear on begin and
    // end attributes for varstatus!
    // // --------------------------------------------------------------
    // case 4
    // List<String> begin = new ArrayList<String>();
    //
    // begin.add("5");
    //
    // testList(page, "isBeginNum", begin, 1, formatter, true);
    //
    // // --------------------------------------------------------------
    // case 5
    // List<String> end = new ArrayList<String>();
    //
    // end.add("1");
    //
    // testList(page, "isEndNum", end, 1, formatter, true);

    // -------------------------------------------------------------- case 6
    List<String> step = new ArrayList<String>();

    step.add("2");
    step.add("2");
    step.add("2");

    testList(page, "VSStep", step, 3, formatter, true);

    // -------------------------------------------------------------- case 7
    List<String> odd = new ArrayList<String>();

    odd.add("false");
    odd.add("true");
    odd.add("false");
    odd.add("true");
    odd.add("false");

    testList(page, "VSOdd", odd, 5, formatter, true);

    // -------------------------------------------------------------- case 8
    List<String> even = new ArrayList<String>();

    even.add("true");
    even.add("false");
    even.add("true");
    even.add("false");
    even.add("true");

    testList(page, "VSEven", even, 5, formatter, true);

    handleTestStatus(messages);
  }
} // END URLClient
