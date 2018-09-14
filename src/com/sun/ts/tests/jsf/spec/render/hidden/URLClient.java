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
package com.sun.ts.tests.jsf.spec.render.hidden;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_hidden_web";

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
   * @testName: hiddenRenderEncodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the proper encoding of an html hidden input field:
   *                 - name and id attributes have the same value - the rendered
   *                 value of the value attribute is the value of the component
   * 
   * @since 1.2
   */
  public void hiddenRenderEncodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ------------------------------------------------------------
      // Form1
      HtmlHiddenInput hidden1 = (HtmlHiddenInput) getInputIncludingId(page,
          "hidden1");

      if (!validateExistence("hidden1", "input", hidden1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // ensure the name and id attribute values are equal
      if (!hidden1.getId().equals(hidden1.getNameAttribute())) {
        formatter.format("Expected the rendered values for the 'id' and"
            + " 'name' attributes to be equal for hidden input"
            + " containing ID 'hidden1'.  The expected "
            + " value for the name attribute was '%s', but found" + " '%s' %n",
            hidden1.getId(), hidden1.getNameAttribute());
      }

      if (!"value".equals(hidden1.getValueAttribute())) {
        formatter.format(
            "Expected the rendered value of the value "
                + "attribute to be 'value', but found '%s' %n",
            hidden1.getValueAttribute());
      }

      // ------------------------------------------------------------
      // Form2
      HtmlHiddenInput hidden2 = (HtmlHiddenInput) getInputIncludingId(page,
          "Invisible");

      if (!validateExistence("Invisible", "input", hidden2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // ensure the name and id attribute values are equal
      if (!hidden2.getId().equals(hidden2.getNameAttribute())) {
        formatter.format(
            "Expected the rendered values for the 'id' and"
                + " 'name' attributes to be equal for hidden input"
                + " containing ID '%s' %n." + "Id Attribute Received:'%s' %n"
                + "Name Attribute Received: '%s' %n",
            "Invisible", hidden2.getId(), hidden2.getNameAttribute());
      }

      if (!"Invisible".equals(hidden2.getValueAttribute())) {
        formatter.format(
            "Expected the rendered value of the value "
                + "attribute to be 's', but found '%s' %n",
            "Invisible", hidden2.getValueAttribute());
      }

      handleTestStatus(messages);
    }
  } // END hiddenRenderEncodeTest

  /**
   * @testName: hiddenRenderDecodeTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure the value of the hidden field is updated by
   *                 submitting the form. The value after the post back should
   *                 be 'newSubmittedValue'. This is validated by getting the
   *                 value of the component after the post-back (can't validate
   *                 that (setSubmittedValue() is called since after the
   *                 validations are processed setValue() will have been called
   *                 and the submittedValue reset to null).
   * 
   * @since 1.2
   */
  public void hiddenRenderDecodeTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/decodetest_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlHiddenInput hidden1 = (HtmlHiddenInput) getInputIncludingId(page,
          "hidden1");

      if (!validateExistence("hidden1", "input", hidden1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      hidden1.setValueAttribute("newSubmittedValue");

      HtmlSubmitInput button1 = (HtmlSubmitInput) getInputIncludingId(page,
          "button1");

      try {
        page = (HtmlPage) button1.click();
      } catch (IOException e) {
        formatter.format("Unexpected exception clicking button1: %s %n", e);
        handleTestStatus(messages);
        return;
      }

      HtmlInput shadow = (HtmlInput) getInputIncludingId(page, "result");
      String result = shadow.getValueAttribute();

      if (!"newSubmittedValue".equals(result)) {
        formatter.format("Unexpected submitted value for hidden1.  Expected"
            + " 'test', but found '%s' %n", result);
      }

      handleTestStatus(messages);
    }
  } // END hiddenRenderDecodeTest
} // END URLClient
