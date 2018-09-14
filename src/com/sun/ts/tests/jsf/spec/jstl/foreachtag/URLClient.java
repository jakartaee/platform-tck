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
 * $Id: URLClient.java 56144 2008-11-03 20:26:49Z dougd $
 */
package com.sun.ts.tests.jsf.spec.jstl.foreachtag;

import java.io.PrintWriter;
import java.util.Formatter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;
import java.util.List;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_jstl_foreachtag_web";

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
   * @testName: jstlCoreForEachTagTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate "c:foreach" tag in the following.
   * 
   *                 case:1 Using a String Array render all Elements. case:2
   *                 Using a ArrayList render all Elements. case:3 Using a
   *                 Vector render all Elements. case:4 Using a LinkedList
   *                 render all Elements. case:5 Using a HashSet render all
   *                 Elements. case:6 Using a TreeSet render all Elements.
   *                 case:7 Testing "Begin" Attribute. case:8 Testing "End"
   *                 Attribute. case:9 Testing "Step" Attribute. case:10 Testing
   *                 VarStatus attribute, (count) case:11 Testing VarStatus
   *                 attribute, (current) case:12 Testing VarStatus attribute,
   *                 (index) case:13 Testing VarStatus attribute, (last)
   * 
   * 
   * @since 2.0
   */
  public void jstlCoreForEachTagTest() throws Fault {
    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    HtmlPage page = getPage(CONTEXT_ROOT + "/faces/foreachtag_facelet.xhtml");

    // -------------------------------------------------------------- case 1
    List<String> fnames = new ArrayList<String>();
    fnames.add("Geddy");
    fnames.add("Alex");
    fnames.add("Neil");

    testList(page, "Firstname:", fnames, 3, formatter);

    // -------------------------------------------------------------- case 2
    List<String> lnames = new ArrayList<String>();
    lnames.add("Lee");
    lnames.add("Lifeson");
    lnames.add("Peart");

    testList(page, "Lastname:", lnames, 3, formatter);

    // -------------------------------------------------------------- case 3
    List<String> albums = new ArrayList<String>();
    albums.add("Exit Stage Left");
    albums.add("Hemispheres");
    albums.add("Farewell To Kings");

    testList(page, "Album:", albums, 3, formatter);

    // -------------------------------------------------------------- case 4
    List<String> songs = new ArrayList<String>();
    songs.add("Freewill");
    songs.add("2112");
    songs.add("Subdivisions");

    testList(page, "Song:", songs, 3, formatter);

    // -------------------------------------------------------------- case 5
    List<String> dates = new ArrayList<String>();
    dates.add("1971");
    dates.add("1972");
    dates.add("1973");

    testList(page, "Released:", dates, 3, formatter);

    // -------------------------------------------------------------- case 6
    List<String> ratings = new ArrayList<String>();
    ratings.add("8");
    ratings.add("9");
    ratings.add("10");

    testList(page, "Rating:", ratings, 3, formatter);

    // -------------------------------------------------------------- case 7
    List<String> third = new ArrayList<String>();
    third.add("Peart");

    testList(page, "Begin:", third, 1, formatter);

    // -------------------------------------------------------------- case 8
    List<String> first = new ArrayList<String>();
    first.add("Lee");

    testList(page, "End:", first, 1, formatter);

    // -------------------------------------------------------------- case 9
    List<String> oneAndthree = new ArrayList<String>();
    oneAndthree.add("Lee");
    oneAndthree.add("Peart");

    testList(page, "Step:", oneAndthree, 2, formatter);

    // ------------------------------------------------------------- case 10
    List<String> count = new ArrayList<String>();

    count.add("1");
    count.add("2");
    count.add("3");

    testList(page, "VSCo", count, 3, formatter, true);

    // ------------------------------------------------------------- case 11
    List<String> current = new ArrayList<String>();

    current.add("Lee");
    current.add("Lifeson");
    current.add("Peart");

    testList(page, "VSCu", current, 3, formatter, true);

    // ------------------------------------------------------------- case 12
    List<String> index = new ArrayList<String>();

    index.add("0");
    index.add("1");
    index.add("2");

    testList(page, "VSI", index, 3, formatter, true);

    // ------------------------------------------------------------- case 13
    List<String> last = new ArrayList<String>();

    last.add("false");
    last.add("false");
    last.add("true");

    testList(page, "VSL", last, 3, formatter, true);

    // ------------------------------------------ Finally Handle Messages.
    handleTestStatus(messages);

  } // END jstlCoreForEachTagTest //

}
