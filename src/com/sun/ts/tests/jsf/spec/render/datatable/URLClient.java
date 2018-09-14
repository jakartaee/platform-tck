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
package com.sun.ts.tests.jsf.spec.render.datatable;

import java.io.PrintWriter;
import java.util.Formatter;
import java.util.List;
import java.util.Iterator;
import java.util.TreeMap;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlCaption;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_dtable_web";

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
   * @testName: dtableRenderEncodeBasicTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the basic rendering of a datatable that has not
   *                 header or footer facets defined at the table or column
   *                 level as well as no caption facet. Table data1: - ensure no
   *                 caption, header, or footers are rendered - ensure the
   *                 dataset was properly iterated through and displayed (no
   *                 class attribute rendered on the tr and td elements) Table
   *                 data2: - same as data1 above, accept the table has
   *                 rowClasses and columnClasses defined as ("odd,even").
   *                 Ensure the class attribute for each row and cell has the
   *                 expected value.
   * 
   *                 Table data3: - Use the binging attribute to tie into a
   *                 Backing Bean. Make sure that the follow attributes are
   *                 rendered. - bgcolor - border - title
   * 
   * @since 1.2
   */
  public void dtableRenderEncodeBasicTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestBasic.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestBasic_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ----------------------------------------------------------- Data1

      HtmlTable data1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data1");

      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(data1).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'data1'. This DataTable had no "
            + "caption facet. %n");
      }

      // no header facet for the three columns means no thead elements
      // should have been rendered
      HtmlTableHeader data1Header = data1.getHeader();
      if (data1Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'data1'.  This DataTable had no "
            + "header facets. %n");
      }

      validateTableBodyRows(data1, "data1", null, null, formatter);

      // ---------------------------------------------------------- Data 2

      HtmlTable data2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data2");

      if (!validateExistence("data2", "table", data2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(data2).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'data2'.  This DataTable had no "
            + "caption facet. %n");
      }

      // no header facet for the three columns means no thead elements
      // should have been rendered
      HtmlTableHeader data2Header = data2.getHeader();
      if (data2Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'data2'.  This DataTable had no "
            + "header facets. %n");
      }

      validateTableBodyRows(data2, "data2",
          new String[] { "odd", "even", "odd" },
          new String[] { "even", "odd", "" }, formatter);
      // ---------------------------------------------------------- Data 3

      HtmlTable data3 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data3");

      if (!validateExistence("data3", "table", data3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // test for title attribute.
      if (!"Books".equals(data3.getAttribute("title"))) {
        formatter.format("Unexpected value for title attribute for "
            + "the rendered table element. Expected '%s'%n,"
            + " but received '%s'%n", "Books", data3.getAttribute("title"));
      }

      // test for bgcolor attribute.
      if (!"FFFF99".equals(data3.getBgcolorAttribute())) {
        formatter.format(
            "Unexpected value for bgcolor attribute for "
                + "the rendered table element. Expected '%s'%n,"
                + " but received '%s'%n",
            "FFFF99", data3.getBgcolorAttribute());
      }

      // test for border attribute.
      if (!"2".equals(data3.getBorderAttribute())) {
        formatter.format("Unexpected value for border attribute for "
            + "the rendered table element. Expected '%s'%n,"
            + " but received '%s'%n", "2", data3.getBorderAttribute());
      }

      handleTestStatus(messages);
    }
  } // END dtableRenderEncodeBasicTest

  /**
   * @testName: dtableRenderEncodeCaptionTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure a caption is properly rendered when a caption is
   *                 specified as a child of the data table. Additionally ensure
   *                 the captionClass and captionStyle values are appropriately
   *                 rendered when they are and are not specified. Finally,
   *                 validate the rows are rendered as expected.
   * 
   * @since 1.2
   */
  public void dtableRenderEncodeCaptionTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption_facelet.xhtml"));

    for (HtmlPage page : pages) {

      HtmlTable data1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data1");

      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(data1).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'data1' when caption facet was specified. %n");
      } else {
        if (!"Caption Text For Data1".equals(data1.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'data1' to be 'Caption Text For Data1', "
              + "received: '%s' %n", data1.getCaptionText());
        }
      }

      // ensure no style or class attributes were rendered
      HtmlCaption caption = getCaption(data1);

      if (caption.getAttribute("class").length() != 0) {
        formatter.format("Expected no class attribute to be "
            + "rendered on caption" + " for table containing ID 'data1' %n");
      }

      if (caption.getAttribute("style").length() != 0) {
        formatter.format("Expected no style attribute to be "
            + "rendered on caption" + " for table containing ID 'data1' %n");
      }

      validateTableBodyRows(data1, "data1", null, null, formatter);

      // next validate that the caption has the style attribute when
      // specified
      HtmlTable data2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data2");

      if (!validateExistence("data2", "table", data2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(data2).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'data2' when caption facet was specified. %n");
      } else {
        if (!"Caption Text For Data2".equals(data2.getCaptionText())) {
          formatter.format(
              "Expected caption for table containing " + "ID 'data2'"
                  + " to be 'Caption Text For Data2', " + "received: '%s' %n",
              data2.getCaptionText());
        }
      }

      // ensure style is rendered but class is not
      HtmlCaption caption2 = getCaption(data2);

      if (caption2.getAttribute("style").length() == 0) {
        formatter.format("styleClass was specified in the JSP, but "
            + "no style" + " attribute was found for the rendered caption for "
            + "the table containing the ID 'data2'. %n");
      } else {
        if (!"Color: red;".equals(caption2.getAttribute("style"))) {
          formatter.format(
              "Expected style attribute of the " + "rendered caption for the"
                  + " table containing the ID 'data2' to be rendered "
                  + "with a value" + " of 'Color: red;'.  Value found '%s' %n",
              caption2.getAttribute("style"));
        }
      }

      if (caption2.getAttribute("class").length() != 0) {
        formatter.format("Expected no class attribute to be rendered "
            + "on caption" + " for table containing ID 'data2' %n");
      }

      validateTableBodyRows(data2, "data2", null, null, formatter);

      // next validate that the caption has the class attribute when
      // specified
      HtmlTable data3 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data3");

      if (!validateExistence("data3", "table", data3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(data3).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'data3' when caption facet was specified. %n");
      } else {
        if (!"Caption Text For Data3".equals(data3.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'data3' to be 'Caption Text For Data3', "
              + "received: '%s' %n", data3.getCaptionText());
        }
      }

      // ensure class is rendered but style is not
      HtmlCaption caption3 = getCaption(data3);

      if (caption3.getAttribute("class").length() == 0) {
        formatter.format("captionClass was specified in the JSP, "
            + "but no class attribute was found for the rendered "
            + "caption with the table containing the ID 'data3'. %n");
      } else {
        if (!"sansserif".equals(caption3.getAttribute("class"))) {
          formatter.format(
              "Expected class attribute to the "
                  + "rendered caption in the table"
                  + " containing the ID 'data3' to have a value"
                  + " of 'sansserif'.  Value found '%s' %n",
              caption3.getAttribute("class"));
        }
      }

      if (caption3.getAttribute("style").length() != 0) {
        formatter.format("Expected no style attribute to be "
            + "rendered on caption for table containing ID " + "'data3' %n");
      }

      validateTableBodyRows(data3, "data3", null, null, formatter);

      // next validate that the caption has the class and style attributes
      // when specified
      HtmlTable data4 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data4");

      if (!validateExistence("data4", "table", data4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(data4).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'data4' when caption facet was specified. %n");
      } else {
        if (!"Caption Text For Data4".equals(data4.getCaptionText())) {
          formatter.format(
              "Expected caption for table " + "containing ID 'data4'"
                  + " to be 'Caption Text For Data4', " + "received: '%s' %n",
              data4.getCaptionText());
        }
      }

      // ensure both class and style are rendered
      HtmlCaption caption4 = getCaption(data4);

      if (caption4.getAttribute("class").length() == 0) {
        formatter
            .format("captionClass was specified in the JSP, " + "but no class"
                + " attribute was found for the rendered caption in "
                + "the table containing the ID 'data4'. %n");
      } else {
        if (!"sansserif".equals(caption4.getAttribute("class"))) {
          formatter.format(
              "Expected class attribute of the " + "rendered caption in the"
                  + " table containing the ID 'data4' to have a value"
                  + " of 'sansserif'.  Value found '%s' %n",
              caption4.getAttribute("class"));
        }
      }

      if (caption4.getAttribute("style").length() == 0) {
        formatter.format("styleClass was specified in the JSP, "
            + "but no style attribute was found for the rendered "
            + "caption in table containing the ID 'data4'. %n");
      } else {
        if (!"Color: red;".equals(caption4.getAttribute("style"))) {
          formatter.format("Expected style attribute to be "
              + "rendered with a value of 'sansserif'. "
              + "Value found '%s' %n", caption4.getAttribute("style"));
        }
      }
      validateTableBodyRows(data4, "data4", null, null, formatter);

      handleTestStatus(messages);
    }
  } // END dtableRenderEncodeCaptionTest

  /**
   * @testName: dtableRenderEncodeColGrpTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure a that a colgroup is render properly when specified
   *                 with a facet tag.
   * 
   *                 Validate: - Three "colgroups are rendered with proper align
   *                 attribute" - All are render before the "Table Header" &
   *                 "Table Footer"
   * 
   * @since 2.0
   */
  public void dtableRenderEncodeColGrpTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption_facelet.xhtml"));

    for (HtmlPage page : pages) {

      HtmlTable data5 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data5");

      if (!validateExistence("data5", "table", data5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      HtmlTableColumnGroup column1 = (HtmlTableColumnGroup) getElementOfTypeIncludingId(
          page, "colgroup", "lefty");
      HtmlTableColumnGroup column2 = (HtmlTableColumnGroup) getElementOfTypeIncludingId(
          page, "colgroup", "middle");
      HtmlTableColumnGroup column3 = (HtmlTableColumnGroup) getElementOfTypeIncludingId(
          page, "colgroup", "righty");

      if (!"left".equals(column1.getAlignAttribute())) {
        formatter.format("Unexpected result for Align attribute for "
            + "Column ID: %s %n" + "Expected: %s %n" + "Receieved: "
            + column1.getAlignAttribute(), "lefty", "left");
      }

      if (!"center".equals(column2.getAlignAttribute())) {
        formatter.format("Unexpected result for Align attribute for "
            + "Column ID: %s %n" + "Expected: %s %n" + "Receieved: "
            + column2.getAlignAttribute() + "%n", "middle", "center");
      }

      if (!"right".equals(column3.getAlignAttribute())) {
        formatter.format("Unexpected result for Align attribute for "
            + "Column ID: %s %n" + "Expected: %s %n" + "Receieved: "
            + column2.getAlignAttribute(), "righty", "right");
      }

      handleTestStatus(messages);
    }

  } // END dtableRenderEncodeColGrpTest

  /**
   * @testName: dtableRenderEncodeTableHeaderFooterTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following: Data1: - thead with single nested
   *                 tr/th element combination - th element has scope attribute
   *                 value of colgroup - th element has colspan of 3 as 3
   *                 columns are defined - th element has no class attribute
   *                 rendered - tfoot with single nested tr/td element
   *                 combination - td element has no scope attribute rendered -
   *                 td element has colspan of 3 as 3 columns are defined - td
   *                 element has no class attribute rendered Data2: - the same
   *                 as above except the class attribute value is validated on
   *                 the rendered th and td elements as headerClass and
   *                 footerClass are defined for this table in the JSP
   * 
   * @since 1.2
   */
  public void dtableRenderEncodeTableHeaderFooterTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestTableHeaderFooter.jsp"));
    pages.add(getPage(
        CONTEXT_ROOT + "/faces/encodetestTableHeaderFooter_facelet.xhtml"));

    for (HtmlPage page : pages) {

      HtmlTable data1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data1");

      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // One table header should be available with a cell
      // spanning all three specified columns
      HtmlTableHeader data1Header = data1.getHeader();
      if (data1Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'data1' when header facet was specified. %n");
      } else {
        // should find a single row with a colspan of 3.
        if (data1Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row for "
                  + "table containing ID 'data1', but found '%s' %n",
              data1Header.getRows().size());
        } else {
          List headerRows = data1Header.getRows();
          HtmlTableRow row = (HtmlTableRow) headerRows.get(0);
          HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table header cell for "
                    + "table containing ID 'data1' to have a "
                    + "colspan of 3 as three columns was "
                    + "specified.  Colspan received '%s' %n.",
                cell.getColumnSpan());
          } else {
            if (!"Header Text For Data1".equals(cell.asText())) {
              formatter.format("Expected table header cell "
                  + "for table containing ID 'data1' to "
                  + "contain 'Header Text For Data1', " + "received: '%s'. %n",
                  cell.asText());
            }
          }

          // validate scope attribute for the th element of the header
          if (!"colgroup".equals(cell.getScopeAttribute())) {
            formatter.format("Expected the scope attribute of "
                + "the 'th' element to be 'colgroup', but " + "found '%s' %n",
                cell.getScopeAttribute());
          }

          // validate no class attribute was rendered
          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("class"))) {
            formatter.format("Expected no class attribute to be "
                + "rendered on the 'th' element since no "
                + "headerClass attribute was defined in the JSP");
          }
        }
      }

      // One table footer should be available with a cell
      // spanning all three specified columns
      HtmlTableFooter data1Footer = data1.getFooter();
      if (data1Footer == null) {
        formatter.format("Footer not rendered for table containing "
            + "ID 'data1' when footer facet was specified. %n");
      } else {
        // should find a single row with a colspan of 3.
        if (data1Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row for "
                  + "table containing ID 'data1', but found '%s' %n",
              data1Footer.getRows().size());
        } else {
          List footerRows = data1Footer.getRows();
          HtmlTableRow row = (HtmlTableRow) footerRows.get(0);
          HtmlTableCell cell = row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table footer cell for "
                    + "table containing ID 'data1' to have a "
                    + "colspan of 3 as three columns was "
                    + "specified. Colspan received '%s' %n.",
                cell.getColumnSpan());
          } else {
            if (!"Footer Text For Data1".equals(cell.asText())) {
              formatter.format("Expected table footer cell for "
                  + "table containing ID 'data1' to "
                  + "contain 'Footer Text For Data1', " + "received: '%s'. %n",
                  cell.asText());
            }
          }

          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("scope"))) {
            formatter.format("Expected no scope attribute to "
                + "be rendered on the table footer, but "
                + "the attribute was found.");
          }

          // validate no class attribute was rendered
          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("class"))) {
            formatter.format("Expected no class attribute to "
                + "be rendered for the footer since no "
                + "footerClass attribute was defined in the JSP");
          }
        }
      }

      validateTableBodyRows(data1, "data1", null, null, formatter);

      // ---------------------------------------------------------- Data 2

      HtmlTable data2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data2");

      if (!validateExistence("data2", "table", data2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // One table header should be available with a cell
      // spanning all three specified columns
      HtmlTableHeader data2Header = data2.getHeader();
      if (data2Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'data2' when header facet was specified. %n");
      } else {
        // should find a single row with a colspan of 3.
        if (data2Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row "
                  + "for table containing ID 'data2', but found '%s'%n",
              data2Header.getRows().size());
        } else {
          List headerRows = data2Header.getRows();
          HtmlTableRow row = (HtmlTableRow) headerRows.get(0);
          HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table header cell for "
                    + "table containing ID 'data2' to have a "
                    + "colspan of 3 as three columns was "
                    + "specified. Colspan received '%s' %n.",
                cell.getColumnSpan());
          } else {
            if (!"Header Text For Data2".equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell for "
                      + "table containing ID 'data2' to contain "
                      + "'Header Text For Data2', " + "received: '%s'.%n",
                  cell.asText());
            }
          }

          // validate scope attribute for the th element of the header
          if (!"colgroup".equals(cell.getScopeAttribute())) {
            formatter.format("Expected the scope attribute of "
                + "the 'th' element to be 'colgroup', but " + "found '%s' %n",
                cell.getScopeAttribute());
          }

          // validate class attribute was rendered
          if (!"sansserif".equals(cell.getAttribute("class"))) {
            formatter.format("Expected the class attribute "
                + "of the 'th' element to be 'sansserif', "
                + "but found '%s' %n", cell.getAttribute("class"));
          }
        }
      }

      // One table footer should be available with a cell
      // spanning all three specified columns
      HtmlTableFooter data2Footer = data2.getFooter();
      if (data2Footer == null) {
        formatter.format("Footer not rendered for table containing "
            + "ID 'data2' when footer facet was specified. %n");
      } else {
        // should find a single row with a colspan of 3.
        if (data2Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row "
                  + "for table containing ID 'data2', but " + "found '%s' %n",
              data2Footer.getRows().size());
        } else {
          List footerRows = data2Footer.getRows();
          HtmlTableRow row = (HtmlTableRow) footerRows.get(0);
          HtmlTableCell cell = row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table footer cell for "
                    + "table containing ID 'data2' to have a "
                    + "colspan of 3 as three columns was "
                    + "specified. Colspan received '%s' %n.",
                cell.getColumnSpan());
          } else {
            if (!"Footer Text For Data2".equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell for "
                      + "table containing ID 'data2' to contain "
                      + "Footer Text For Data2', received: " + "'%s'. %n",
                  cell.asText());
            }
          }

          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("scope"))) {
            formatter.format("Expected no scope attribute to be "
                + "rendered on the table footer, but the "
                + "attribute was found.");
          }

          // validate class attribute was rendered
          if (!"sansserif".equals(cell.getAttribute("class"))) {
            formatter.format(
                "Expected the class attribute of "
                    + "the 'td' element of the footer to be "
                    + "'sansserif', but found '%s' %n",
                cell.getAttribute("class"));
          }
        }
      }

      validateTableBodyRows(data2, "data2", null, null, formatter);

      handleTestStatus(messages);
    }
  } // END dtableRenderEncodeTableHeaderFooterTest

  /**
   * @testName: dtableRenderEncodeColumnHeaderFooterTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following: Data1: - thead with three nested
   *                 tr/th element combinations - each th element has scope
   *                 attribute value of col - each th element has no class
   *                 attribute rendered - tfoot with three nested tr/td element
   *                 combinations - each td element has no scope attribute
   *                 rendered - each td element has no class attribute rendered
   *                 Data2: - the same as above except the class attribute value
   *                 is validated on the rendered th and td elements as
   *                 headerClass and footerClass are defined for this table in
   *                 the JSP Data3: - the same as Data1, except headerClass and
   *                 footerClass are defined on the table as well as two of the
   *                 columns. Validate that the class specified on the column
   *                 level overrides the table level definition
   * 
   * @since 1.2
   */
  public void dtableRenderEncodeColumnHeaderFooterTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages
        .add(getPage(CONTEXT_ROOT + "/faces/encodetestColumnHeaderFooter.jsp"));
    pages.add(getPage(
        CONTEXT_ROOT + "/faces/encodetestColumnHeaderFooter_facelet.xhtml"));

    for (HtmlPage page : pages) {

      HtmlTable data1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data1");
      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // There should be one thead containing one tr and three th's
      HtmlTableHeader data1Header = data1.getHeader();
      if (data1Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'data1' when header facet was specified on the "
            + "specified columns %n");
      } else {
        // should find 1 header rows
        if (data1Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row for "
                  + "table containing ID 'data1', but found '%s' %n",
              data1Header.getRows().size());
        } else {
          String[] headerCellValues = new String[] { "Name Header", "",
              "Age Header" };
          HtmlTableRow row = (HtmlTableRow) data1Header.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(i);
            if (!headerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell(%s) "
                      + "for table containing ID 'data1' to "
                      + "contain '%s', received: '%s'. %n",
                  i, headerCellValues[i], cell.asText());
            }

            // validate the scope attribute is rendered on each
            // column header as expected
            if (!"col".equals(cell.getScopeAttribute())) {
              formatter.format("Expected table header cell(%s) "
                  + "for table containing ID 'data1' to have "
                  + "a scope attribute value of 'col', " + "but found '%s' %n",
                  i, cell.getScopeAttribute());
            }
          }
        }
      }

      // There should be one footer containing one row and three td's
      HtmlTableFooter data1Footer = data1.getFooter();
      if (data1Footer == null) {
        formatter.format("Footers not rendered for table containing "
            + "ID 'data1' when footer facet was specified on the "
            + "specified columns %n");
      } else {
        // should find 1 header rows
        if (data1Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row for "
                  + "table containing ID 'data1', but found '%s' %n",
              data1Footer.getRows().size());
        } else {
          String[] footerCellValues = new String[] { "", "Gender Footer",
              "Age Footer" };
          HtmlTableRow row = (HtmlTableRow) data1Footer.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableCell cell = row.getCell(i);
            if (!footerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell(%s) "
                      + "for table containing ID 'data1' to "
                      + "contain '%s', received: '%s'. %n",
                  i, footerCellValues[i], cell.asText());
            }

            // validate no scope attribute is rendered
            if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
                .equals(cell.getAttribute("scope"))) {
              formatter.format("Expected no scope attribute to "
                  + "be rendered for footer cell(%s) in table"
                  + " containing ID 'data1' %n", i);
            }
          }
        }
      }

      validateTableBodyRows(data1, "data1", null, null, formatter);

      // ---------------------------------------------------------- Data 2

      // this table has headerClass and footerClass defined on the
      // dataTable tag. This should result in class being rendered
      // for each header and footer row.
      HtmlTable data2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data2");
      if (!validateExistence("data2", "table", data2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // There should be one thead containing one tr and three th's
      HtmlTableHeader data2Header = data2.getHeader();
      if (data2Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'data2' when header facet was specified on the "
            + "specified columns %n");
      } else {
        // should find 1 header rows
        if (data2Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row "
                  + "for table containing ID 'data2', but found '%s'%n",
              data2Header.getRows().size());
        } else {
          String[] headerCellValues = new String[] { "Name Header", "",
              "Age Header" };
          HtmlTableRow row = (HtmlTableRow) data2Header.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(i);
            if (!headerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell(%s) "
                      + "for table containing ID 'data2' to "
                      + "contain '%s', received: '%s'. %n",
                  i, headerCellValues[i], cell.asText());
            }

            // validate the scope attribute is rendered on each
            // column header as expected
            if (!"col".equals(cell.getScopeAttribute())) {
              formatter.format("Expected table header cell(%s) "
                  + "for table containing ID 'data2' to have "
                  + "a scope attribute value of 'col', " + "but found '%s' %n",
                  i, cell.getScopeAttribute());
            }

            if (!"sansserif".equals(cell.getAttribute("class"))) {
              formatter.format("Expected class attribute for "
                  + "header cell (%s) to be 'columnClass', "
                  + "but found '%s' %n", i, cell.getAttribute("class"));
            }
          }
        }
      }

      // There should be one footer containing one row and three td's
      HtmlTableFooter data2Footer = data2.getFooter();
      if (data2Footer == null) {
        formatter.format("Footers not rendered for table containing "
            + "ID 'data2' when footer facet was specified on "
            + "the specified columns %n");
      } else {
        // should find 1 header rows
        if (data2Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row for "
                  + "table containing ID 'data2', but found '%s' %n",
              data2Footer.getRows().size());
        } else {
          String[] footerCellValues = new String[] { "", "Gender Footer",
              "Age Footer" };
          HtmlTableRow row = (HtmlTableRow) data2Footer.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableCell cell = row.getCell(i);
            if (!footerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell(%s) "
                      + "for table containing ID 'data2' to "
                      + "contain '%s', received: '%s'. %n",
                  i, footerCellValues[i], cell.asText());
            }

            // validate no scope attribute is rendered
            if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
                .equals(cell.getAttribute("scope"))) {
              formatter.format("Expected no scope attribute to "
                  + "be rendered for footer cell(%s) in table"
                  + " containing ID 'data2' %n", i);
            }

            if (!"sansserif".equals(cell.getAttribute("class"))) {
              formatter.format("Expected class attribute for "
                  + "footer cell (%s) to be 'columnClass', "
                  + "but found '%s' %n", i, cell.getAttribute("class"));
            }
          }
        }
      }

      validateTableBodyRows(data2, "data2", null, null, formatter);

      // ---------------------------------------------------------- Data 3

      // this table has headerClass and footerClass defined on the
      // dataTable and column tags. The result is the column level
      // style classes will override the dataTable level classes
      HtmlTable data3 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data3");
      if (!validateExistence("data3", "table", data3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // There should be one thead containing one tr and three th's
      HtmlTableHeader data3Header = data3.getHeader();
      if (data3Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'data3' when header facet was specified on the "
            + "specified columns %n");
      } else {
        // should find 1 header rows
        if (data3Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row for "
                  + "table containing ID 'data3', but found '%s' %n",
              data3Header.getRows().size());
        } else {
          String[] headerCellValues = new String[] { "Name Header", "",
              "Age Header" };
          String[] headerClassValues = new String[] { "columnClass",
              "sansserif", "sansserif" };
          HtmlTableRow row = (HtmlTableRow) data3Header.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(i);
            if (!headerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell(%s) "
                      + "for table containing ID 'data3' to "
                      + "contain '%s', received: '%s'. %n",
                  i, headerCellValues[i], cell.asText());
            }

            // validate the scope attribute is rendered on each
            // column header as expected
            if (!"col".equals(cell.getScopeAttribute())) {
              formatter.format("Expected table header cell(%s) "
                  + "for table containing ID 'data3' to have "
                  + "a scope attribute value of 'col', " + "but found '%s' %n",
                  i, cell.getScopeAttribute());
            }

            if (!headerClassValues[i].equals(cell.getAttribute("class"))) {
              formatter.format(
                  "Expected class attribute for "
                      + "header cell (%s) to be '%s', but " + "found '%s' %n",
                  i, headerClassValues[i], cell.getAttribute("class"));
            }
          }
        }
      }

      // There should be one footer containing one row and three td's
      HtmlTableFooter data3Footer = data3.getFooter();
      if (data3Footer == null) {
        formatter.format("Footers not rendered for table containing "
            + "ID 'data3' when footer facet was specified on the "
            + "specified columns %n");
      } else {
        // should find 1 header rows
        if (data3Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row for "
                  + "table containing ID 'data3', but found '%s' %n",
              data3Footer.getRows().size());
        } else {
          String[] footerCellValues = new String[] { "", "Gender Footer",
              "Age Footer" };
          String[] footerClassValues = new String[] { "sansserif", "sansserif",
              "columnClass" };
          HtmlTableRow row = (HtmlTableRow) data3Footer.getRows().get(0);
          List headerCells = row.getCells();
          for (int i = 0, size = headerCells.size(); i < size; i++) {
            HtmlTableCell cell = row.getCell(i);
            if (!footerCellValues[i].equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell(%s) "
                      + "for table containing ID 'data3' to "
                      + "contain '%s', received: '%s'. %n",
                  i, footerCellValues[i], cell.asText());
            }

            // validate no scope attribute is rendered
            if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
                .equals(cell.getAttribute("scope"))) {
              formatter.format("Expected no scope attribute to "
                  + "be rendered for footer cell(%s) in table"
                  + " containing ID 'data3' %n", i);
            }

            if (!footerClassValues[i].equals(cell.getAttribute("class"))) {
              formatter.format(
                  "Expected class attribute for "
                      + "footer cell (%s) to be '%s', but " + "found '%s' %n",
                  i, footerClassValues[i], cell.getAttribute("class"));
            }
          }
        }
      }

      validateTableBodyRows(data3, "data3", null, null, formatter);

      handleTestStatus(messages);
    }
  } // END dtableRenderEncodeColumnHeaderFooterTest

  /**
   * @testName: dtableRenderPassthroughTest
   * @assertion_ids: PENDING
   * @test_Strategy: PENDING
   * 
   * @since 1.2
   */
  public void dtableRenderPassthroughTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    TreeMap<String, String> control = new TreeMap<String, String>();
    control.put("bgcolor", "blue");
    control.put("border", "1");
    control.put("cellpadding", "1");
    control.put("cellspacing", "1");
    control.put("dir", "LTR");
    control.put("frame", "box");
    control.put("lang", "en");
    control.put("onclick", "js1");
    control.put("ondblclick", "js2");
    control.put("onkeydown", "js3");
    control.put("onkeypress", "js4");
    control.put("onkeyup", "js5");
    control.put("onmousedown", "js6");
    control.put("onmousemove", "js7");
    control.put("onmouseout", "js8");
    control.put("onmouseover", "js9");
    control.put("onmouseup", "js10");
    control.put("rules", "all");
    control.put("style", "Color: red;");
    control.put("summary", "DataTableSummary");
    control.put("title", "DataTableTitle");
    control.put("width", "100%");

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/passthroughtest.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/passthroughtest_facelet.xhtml"));

    for (HtmlPage page : pages) {

      // Facelet Specific PassThrough options
      if (page.getTitleText().contains("facelet")) {
        control.put("foo", "bar");
        control.put("singleatt", "singleAtt");
        control.put("manyattone", "manyOne");
        control.put("manyatttwo", "manyTwo");
        control.put("manyattthree", "manyThree");
      }

      HtmlTable data1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "data1");
      if (!validateExistence("data1", "table", data1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateAttributeSet(control, data1, new String[] { "id" }, formatter);

      handleTestStatus(messages);
    }
  } // END dtableRenderPassthroughTest

  // --------------------------------------------------------- Utility Methods
  private static String workAroundCaptionTextBug(HtmlTable table) {

    // The javadocs for HtmlTable.getCaptionText() states that if
    // no caption is specified, that an empty string is returned.
    // However, in 1.7, null is what is actually returned.
    String captionText = table.getCaptionText();
    if (captionText == null) {
      captionText = "";
    }

    return captionText;

  } // END workAroundCaptionTextBug

  private static void validateTableBodyRows(HtmlTable table, String id,
      String[] rowClasses, String[] columnClasses, Formatter formatter) {

    HtmlTableBody body = (HtmlTableBody) table.getBodies().get(0);
    if (body.getRows().size() != 3) {
      formatter.format(
          "Incorrect number of rows rendered for table "
              + "containing ID '%s'.  Expected '3', received " + "'%s %n",
          id, body.getRows().size());
    } else {
      String[][] expectedValues = new String[][] { { "Anna", "f", "28" },
          { "Cort", "m", "7" }, { "Cade", "m", "4" } };

      List rows = body.getRows();
      for (int i = 0; i < expectedValues.length; i++) {
        HtmlTableRow row = (HtmlTableRow) rows.get(i);
        for (int j = 0; j < expectedValues[i].length; j++) {
          if (!expectedValues[i][j].equals(row.getCell(j).asText())) {
            formatter.format(
                "Expected value at row '%s', column"
                    + " '%s' for table containing ID '%s' "
                    + "to be '%s', received '%s'. %n",
                i + 1, j + 1, id, expectedValues[i][j],
                row.getCell(j).asText());
          }

          if (columnClasses != null) {
            if (!columnClasses[j]
                .equals(row.getCell(j).getAttribute("class"))) {
              formatter.format(
                  "Expected class attribute for"
                      + "cell(%s,%s) in table containing"
                      + " ID '%s' to be '%s', received" + " '%s' %n",
                  i + 1, j + 1, id, columnClasses[j],
                  row.getCell(j).getAttribute("class"));
            }
          } else {
            if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
                .equals(row.getCell(j).getAttribute("class"))) {
              formatter.format(
                  "The class attribute for cell(%s,%s)"
                      + " in table containing ID '%s' should"
                      + " not have been rendered as the columnClasses"
                      + " attribute was not defined in the JSP. %n",
                  i + 1, j + 1, id);
            }
          }
        }

        if (rowClasses != null) {
          if (!rowClasses[i].equals(row.getAttribute("class"))) {
            formatter.format(
                "Expected class attribute for row '%s',"
                    + " in table containing ID '%s' "
                    + " to be '%s', received '%s' %n",
                i + 1, id, rowClasses[i], row.getAttribute("class"));
          }
        } else {
          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(row.getAttribute("class"))) {
            formatter.format("The class attribute for row '%s'"
                + " in table containing ID '%s' should"
                + " not have been rendered as the rowClasses"
                + " attribute was not defined in the JSP. %n", i + 1, id);
          }
        }
      }
    }

  } // END validateTableBodyRows

  private static HtmlCaption getCaption(HtmlTable table) {

    for (Iterator i = table.getChildElements().iterator(); i.hasNext();) {
      Object element = i.next();
      if (element instanceof HtmlCaption) {
        return (HtmlCaption) element;
      }
    }

    return null;

  } // END getCaption
} // END URLClient
