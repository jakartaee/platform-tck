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
package com.sun.ts.tests.jsf.spec.render.grid;

import java.io.PrintWriter;
import java.util.Formatter;
import java.util.List;
import java.util.Iterator;

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
import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.BaseHtmlUnitClient;
import java.util.ArrayList;

public class URLClient extends BaseHtmlUnitClient {

  private static final String CONTEXT_ROOT = "/jsf_render_grid_web";

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
   * @testName: gridRenderEncodeBasicTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the basic rendering of a datatable that has not
   *                 header or footer facets defined at the table or column
   *                 level as well as no caption facet. Table grid1: - ensure
   *                 that the styleClass attribute is rendered correctly -
   *                 ensure no caption, header, or footers are rendered - ensure
   *                 the dataset was properly iterated through and displayed (no
   *                 class attribute rendered on the tr and td elements) Table
   *                 grid2: - same as grid1 above, accept the table has
   *                 rowClasses and columnClasses defined as ("odd,even").
   *                 Ensure the class attribute for each row and cell has the
   *                 expected value. Table grid6: - Tests for binding
   *                 attribute.Validate the following is rendered and correct:
   *                 The title attribute on the table element. That there are
   *                 two columns. That the is One row. That the class attribute
   *                 on the columns.
   * 
   * 
   * @since 1.2
   */
  public void gridRenderEncodeBasicTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestBasic.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestBasic_facelet.xhtml"));

    for (HtmlPage page : pages) {
      // ----------------------------------------------------------- Grid1

      HtmlTable grid1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid1");

      if (!validateExistence("grid1", "table", grid1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      if (!"color: yellow;".equals(grid1.getAttribute("class"))) {
        formatter.format(
            "Expected the rendered value for the "
                + "styleClass attribute of the table with ID "
                + "containing 'grid1' to be '%s' but found '%s'%n",
            "color: yellow;", grid1.getAttribute("class"));
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(grid1).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'grid1'.  This grid had no caption " + "facet.%n");
      }

      // no header facet columns means no thead elements
      // should have been rendered
      HtmlTableHeader grid1Header = grid1.getHeader();
      if (grid1Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'grid1'.  This grid had no header " + "facets.%n");
      }
      validateTableBodyRows(grid1, "grid1", 6, 1, null, null, formatter);

      // ----------------------------------------------------------- Grid2

      HtmlTable grid2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid2");

      if (!validateExistence("grid2", "table", grid2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(grid2).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'grid2'.  This grid had no caption " + "facet.%n");
      }

      // no header facet columns means no thead elements
      // should have been rendered
      HtmlTableHeader grid2Header = grid2.getHeader();
      if (grid2Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'grid2'.  This grid had no header " + "facets.%n");
      }
      validateTableBodyRows(grid2, "grid2", 3, 2, null, null, formatter);

      // ----------------------------------------------------------- Grid3

      HtmlTable grid3 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid3");

      if (!validateExistence("grid3", "table", grid3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(grid3).length() != 0) {
        formatter.format("Caption incorrectly rendered for table containing"
            + " ID 'grid3'.  This grid had no caption facet.%n");
      }

      // no header facet columns means no thead elements
      // should have been rendered
      HtmlTableHeader grid3Header = grid3.getHeader();
      if (grid3Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'grid3'.  This grid had no header " + "facets.%n");
      }
      validateTableBodyRows(grid3, "grid3", 2, 3, null, null, formatter);

      // ----------------------------------------------------------- Grid4

      HtmlTable grid4 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid4");

      if (!validateExistence("grid4", "table", grid4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(grid4).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'grid4'.  This grid had no caption " + "facet.%n");
      }

      // no header facet columns means no thead elements
      // should have been rendered
      HtmlTableHeader grid4Header = grid4.getHeader();
      if (grid4Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containing ID 'grid4'.  This grid had no header " + "facets.%n");
      }
      validateTableBodyRows(grid4, "grid4", 1, 6, null, null, formatter);

      // ----------------------------------------------------------- Grid5

      HtmlTable grid5 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid5");

      if (!validateExistence("grid5", "table", grid5, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // no caption facet means no caption element should have been
      // rendered.
      if (workAroundCaptionTextBug(grid5).length() != 0) {
        formatter.format("Caption incorrectly rendered for table "
            + "containing ID 'grid5'.  This grid had no caption " + "facet.%n");
      }

      // no header facet for the three columns means no thead elements
      // should have been rendered
      HtmlTableHeader grid5Header = grid5.getHeader();
      if (grid5Header != null) {
        formatter.format("Header incorrectly rendered for table "
            + "containingID 'grid5'.  This grid had no header " + "facets.%n");
      }

      validateTableBodyRows(grid5, "grid5", 2, 3,
          new String[] { "odd", "even" },
          new String[] { "even", "odd", "even" }, formatter);

      // ----------------------------------------------------------- Grid6

      HtmlTable grid6 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid6");

      if (!validateExistence("grid6", "table", grid6, formatter)) {
        handleTestStatus(messages);
        return;
      }

      validateTableBodyRows(grid6, "grid6", 1, 2, null,
          new String[] { "odd", "even" }, formatter);

      handleTestStatus(messages);
    }
  } // END gridRenderEncodeBasicTest

  /**
   * @testName: gridRenderEncodeCaptionTest
   * @assertion_ids: PENDING
   * @test_Strategy: Ensure a caption is properly rendered when a caption is
   *                 specified as a child of the grid. Additionally ensure the
   *                 captionClass and captionStyle values are appropriately
   *                 rendered when they are and are not specified. Finally,
   *                 validate the rows are rendered as expected.
   * 
   * @since 1.2
   */
  public void gridRenderEncodeCaptionTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption.jsp"));
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestCaption_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlTable grid1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid1");

      if (!validateExistence("grid1", "table", grid1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(grid1).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'grid1' when caption facet was specified.%n");
      } else {
        if (!"Caption Text For Grid1".equals(grid1.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'grid1' to be 'Caption Text For Grid1', "
              + "received: '%s'%n", grid1.getCaptionText());
        }
      }

      // ensure no style or class attributes were rendered
      HtmlCaption caption = getCaption(grid1);
      if (caption.getAttribute("class").length() > 0) {
        formatter.format("Expected no class attribute to be rendered "
            + "on caption for table containing ID 'grid1'%n");
      }

      if (caption.getAttribute("style").length() != 0) {
        formatter.format("Expected no style attribute to be rendered "
            + "on caption for table containing ID 'grid1'%n");
      }

      validateTableBodyRows(grid1, "grid1", 2, 3, null, null, formatter);
      // next validate that the caption has the style attribute when
      // specified
      HtmlTable grid2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid2");

      if (!validateExistence("grid2", "table", grid2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(grid2).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'grid2' when caption facet was specified.%n");
      } else {
        if (!"Caption Text For Grid2".equals(grid2.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'grid2' to be 'Caption Text For Grid2', "
              + "received: '%s'%n", grid2.getCaptionText());
        }
      }

      // ensure style is rendered but class is not
      HtmlCaption caption2 = getCaption(grid2);

      if (caption2.getAttribute("style").length() == 0) {
        formatter.format("styleClass was specified in the JSP, but "
            + "no style attribute was found for the rendered "
            + "caption for the table containing the ID 'grid2'.%n");
      } else {
        if (!"Color: red;".equals(caption2.getAttribute("style"))) {
          formatter.format(
              "Expected style attribute of the "
                  + "rendered caption for the table containing the "
                  + "ID 'grid2' to be rendered with a value"
                  + " of 'Color: red;'.  Value found '%s'%n",
              caption2.getAttribute("style"));
        }
      }

      if (caption2.getAttribute("class").length() != 0) {
        formatter.format("Expected no class attribute to be "
            + "rendered on caption for table containing ID " + "'grid2'%n");
      }

      validateTableBodyRows(grid2, "grid2", 3, 2, null, null, formatter);

      // next validate that the caption has the class attribute when
      // specified
      HtmlTable grid3 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid3");

      if (!validateExistence("grid3", "table", grid3, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(grid3).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'grid3' when caption facet was specified.%n");
      } else {
        if (!"Caption Text For Grid3".equals(grid3.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'grid3' to be 'Caption Text For Grid3', "
              + "received: '%s'%n", grid3.getCaptionText());
        }
      }

      // ensure class is rendered but style is not
      HtmlCaption caption3 = getCaption(grid3);

      if (caption3.getAttribute("class").length() == 0) {
        formatter.format("captionClass was specified in the JSP, "
            + "but no class attribute was found for the rendered "
            + "caption with the table containing the ID 'grid3'.%n");
      } else {
        if (!"sansserif".equals(caption3.getAttribute("class"))) {
          formatter.format(
              "Expected class attribute to the "
                  + "rendered caption in the table"
                  + " containing the ID 'grid3' to have a value"
                  + " of 'sansserif'.  Value found '%s'%n",
              caption3.getAttribute("class"));
        }
      }

      if (caption3.getAttribute("style").length() != 0) {
        formatter.format("Expected no style attribute to be "
            + "rendered on caption for table containing " + "ID 'grid3'%n");
      }

      validateTableBodyRows(grid3, "grid3", 2, 3, null, null, formatter);

      // next validate that the caption has the class and style attributes
      // when specified
      HtmlTable grid4 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid4");

      if (!validateExistence("grid4", "table", grid4, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // caption facet means caption element should have been rendered.
      if (workAroundCaptionTextBug(grid4).length() == 0) {
        formatter.format("Caption not rendered for table containing"
            + " ID 'grid4' when caption facet was specified.%n");
      } else {
        if (!"Caption Text For Grid4".equals(grid4.getCaptionText())) {
          formatter.format("Expected caption for table containing "
              + "ID 'grid4' to be 'Caption Text For Grid4', "
              + "received: '%s'%n", grid4.getCaptionText());
        }
      }

      // ensure both class and style are rendered
      HtmlCaption caption4 = getCaption(grid4);

      if (caption4.getAttribute("class").length() == 0) {
        formatter.format("captionClass was specified in the JSP, "
            + "but no class attribute was found for the rendered "
            + "caption in the table" + " containing the ID 'grid4'.%n");
      } else {
        if (!"sansserif".equals(caption4.getAttribute("class"))) {
          formatter.format(
              "Expected class attribute of the " + "rendered caption in the"
                  + " table containing the ID 'grid4' to have a value"
                  + " of 'sansserif'.  Value found '%s'%n",
              caption4.getAttribute("class"));
        }
      }

      if (caption4.getAttribute("style").length() == 0) {
        formatter.format("styleClass was specified in the JSP, but "
            + "no style attribute was found for the rendered "
            + "caption in table containing the ID 'grid4'.%n");
      } else {
        if (!"Color: red;".equals(caption4.getAttribute("style"))) {
          formatter.format("Expected style attribute to be "
              + "rendered with a value of 'sansserif'.  "
              + "Value found '%s'%n", caption4.getAttribute("style"));
        }
      }
      validateTableBodyRows(grid4, "grid4", 3, 2, null, null, formatter);

      handleTestStatus(messages);
    }
  } // END gridRenderEncodeCaptionTest

  /**
   * @testName: gridRenderEncodeTableHeaderFooterTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the following: Grid1: - thead with single nested
   *                 tr/th element combination - th element has scope attribute
   *                 value of colgroup - th element has colspan of 3 as 3
   *                 columns are defined - th element has no class attribute
   *                 rendered - tfoot with single nested tr/td element
   *                 combination - td element has no scope attribute rendered -
   *                 td element has colspan of 3 as 3 columns are defined - td
   *                 element has no class attribute rendered Grid2: - the same
   *                 as above except the class attribute value is validated on
   *                 the rendered th and td elements as headerClass and
   *                 footerClass are defined for this table in the JSP
   * 
   * @since 1.2
   */
  public void gridRenderEncodeTableHeaderFooterTest() throws Fault {

    StringBuilder messages = new StringBuilder(128);
    Formatter formatter = new Formatter(messages);

    List<HtmlPage> pages = new ArrayList<HtmlPage>();
    pages.add(getPage(CONTEXT_ROOT + "/faces/encodetestTableHeaderFooter.jsp"));
    pages.add(getPage(
        CONTEXT_ROOT + "/faces/encodetestTableHeaderFooter_facelet.xhtml"));

    for (HtmlPage page : pages) {
      HtmlTable grid1 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid1");

      if (!validateExistence("grid1", "table", grid1, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // One table header should be available with a cell
      // spanning all three specified columns
      HtmlTableHeader grid1Header = grid1.getHeader();
      if (grid1Header == null) {
        formatter.format("Header not rendered for table containing ID 'grid1'"
            + " when header facet was specified.%n");
      } else {
        // should find a single row with a colspan of 3.
        if (grid1Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row for table "
                  + "containing ID 'grid1', but found '%s'%n",
              grid1Header.getRows().size());
        } else {
          List headerRows = grid1Header.getRows();
          HtmlTableRow row = (HtmlTableRow) headerRows.get(0);
          HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table header cell for "
                    + "table containing ID 'grid1' to have a "
                    + "colspan of 3 as three columns"
                    + " was specified.  Colspan received '%s'%n.",
                cell.getColumnSpan());
          } else {
            if (!"Header Text For Grid1".equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell for "
                      + "table containing ID 'grid1' to contain "
                      + "'Header Text For Grid1', " + "received: '%s'.%n",
                  cell.asText());
            }
          }

          // validate scope attribute for the th element of the header
          if (!"colgroup".equals(cell.getScopeAttribute())) {
            formatter.format("Expected the scope attribute of "
                + "the 'th' element to be 'colgroup', but " + "found '%s'%n",
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
      HtmlTableFooter grid1Footer = grid1.getFooter();
      if (grid1Footer == null) {
        formatter.format("Footer not rendered for table containing "
            + "ID 'grid1' when footer facet was specified.%n");
      } else {
        // should find a single row with a colspan of 3.
        if (grid1Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row for "
                  + "table containing ID 'grid1', but found '%s'%n",
              grid1Footer.getRows().size());
        } else {
          List footerRows = grid1Footer.getRows();
          HtmlTableRow row = (HtmlTableRow) footerRows.get(0);
          HtmlTableCell cell = row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table footer cell for "
                    + "table containing  ID 'grid1' to have a "
                    + "colspan of 3 as three columns"
                    + " was specified.  Colspan received '%s'%n.",
                cell.getColumnSpan());
          } else {
            if (!"Footer Text For Grid1".equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell for "
                      + "table containing ID 'grid1' to contain "
                      + "'Footer Text For Grid1', " + "received: '%s'.%n",
                  cell.asText());
            }
          }

          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("scope"))) {
            formatter.format("Expected no scope attribute to be "
                + "rendered on the table footer, but the "
                + "attribute was found.");
          }

          // validate no class attribute was rendered
          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("class"))) {
            formatter.format("Expected no class attribute to be "
                + "rendered for the footer since no footerClass"
                + " attribute was defined in the JSP");
          }
        }
      }

      validateTableBodyRows(grid1, "grid1", 1, 3, null, null, formatter);

      // ----------------------------------------------------------- Grid2

      HtmlTable grid2 = (HtmlTable) getElementOfTypeIncludingId(page, "table",
          "grid2");

      if (!validateExistence("grid2", "table", grid2, formatter)) {
        handleTestStatus(messages);
        return;
      }

      // One table header should be available with a cell
      // spanning all three specified columns
      HtmlTableHeader grid2Header = grid2.getHeader();
      if (grid2Header == null) {
        formatter.format("Header not rendered for table containing "
            + "ID 'grid2' when header facet was specified.%n");
      } else {
        // should find a single row with a colspan of 3.
        if (grid2Header.getRows().size() != 1) {
          formatter.format(
              "Expected a single table header row "
                  + "for table containing ID 'grid2', but found '%s'%n",
              grid2Header.getRows().size());
        } else {
          List headerRows = grid2Header.getRows();
          HtmlTableRow row = (HtmlTableRow) headerRows.get(0);
          HtmlTableHeaderCell cell = (HtmlTableHeaderCell) row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table header cell for "
                    + "table containing ID 'grid2' to have a "
                    + "colspan of 3 as three columns"
                    + " was specified.  Colspan received '%s'%n.",
                cell.getColumnSpan());
          } else {
            if (!"Header Text For Grid2".equals(cell.asText())) {
              formatter.format(
                  "Expected table header cell for "
                      + "table containing ID 'grid2' to contain "
                      + "'Header Text For Grid2', " + "received: '%s'.%n",
                  cell.asText());
            }
          }

          // validate scope attribute for the th element of the header
          if (!"colgroup".equals(cell.getScopeAttribute())) {
            formatter.format("Expected the scope attribute of "
                + "the 'th' element to be 'colgroup', but " + "found '%s'%n",
                cell.getScopeAttribute());
          }

          // validate class attribute was rendered
          if (!"sansserif".equals(cell.getAttribute("class"))) {
            formatter.format("Expected the class attribute of "
                + "the 'th' element to be 'sansserif', but " + "found '%s'%n",
                cell.getAttribute("class"));
          }
        }
      }

      // One table footer should be available with a cell
      // spanning all three specified columns
      HtmlTableFooter grid2Footer = grid2.getFooter();
      if (grid2Footer == null) {
        formatter.format("Footer not rendered for table containing "
            + "ID 'grid2' when footer facet was specified.%n");
      } else {
        // should find a single row with a colspan of 3.
        if (grid2Footer.getRows().size() != 1) {
          formatter.format(
              "Expected a single table footer row "
                  + "for table containing ID 'grid2', but found '%s'%n",
              grid2Footer.getRows().size());
        } else {
          List footerRows = grid2Footer.getRows();
          HtmlTableRow row = (HtmlTableRow) footerRows.get(0);
          HtmlTableCell cell = row.getCell(0);

          if (cell.getColumnSpan() != 3) {
            formatter.format(
                "Expected table footer cell for "
                    + "table containing ID 'grid2' to have a "
                    + "colspan of 3 as three columns"
                    + " was specified.  Colspan received '%s'%n.",
                cell.getColumnSpan());
          } else {
            if (!"Footer Text For Grid2".equals(cell.asText())) {
              formatter.format(
                  "Expected table footer cell for "
                      + "table containing ID 'grid2' to contain "
                      + "'Footer Text For Grid2', " + "received: '%s'.%n",
                  cell.asText());
            }
          }

          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(cell.getAttribute("scope"))) {
            formatter.format("Expected no scope attribute to "
                + "be rendered on the table footer, but the "
                + "attribute was found.");
          }

          // validate class attribute was rendered
          if (!"sansserif".equals(cell.getAttribute("class"))) {
            formatter.format(
                "Expected the class attribute of "
                    + "the 'td' element of the footer to be "
                    + "'sansserif', but found '%s'%n",
                cell.getAttribute("class"));
          }
        }
      }

      validateTableBodyRows(grid2, "grid2", 1, 3, null, null, formatter);

      handleTestStatus(messages);
    }
  } // END gridRenderEncodeTableHeaderFooterTest

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
      int expectedRows, int columns, String[] rowClasses,
      String[] columnClasses, Formatter formatter) {

    HtmlTableBody body = (HtmlTableBody) table.getBodies().get(0);
    if (body.getRows().size() != expectedRows) {
      formatter.format(
          "Incorrect number of rows rendered for table "
              + "containing ID '%s'.  Expected '%d', received " + "'%s%n",
          id, expectedRows, body.getRows().size());
    } else {
      String[] expectedValues = new String[] { "3", "7", "31", "127", "8191",
          "131071" };
      List rows = body.getRows();
      int totalCells = rows.size() * columns;
      int rowLength = (expectedValues.length > totalCells) ? totalCells
          : expectedValues.length / rows.size();
      for (int i = 0; i < rows.size(); i++) {
        HtmlTableRow row = (HtmlTableRow) rows.get(i);
        for (int j = 0; j < rowLength; j++) {
          int cellNo = i * rowLength + j;
          if (!expectedValues[cellNo].equals(row.getCell(j).asText())) {
            formatter.format(
                "Expected value at row '%s', column"
                    + " '%s' for table containing ID '%s' "
                    + "to be '%s', received '%s'.%n",
                i + 1, j + 1, id, expectedValues[cellNo],
                row.getCell(j).asText());
          }

          if (columnClasses != null) {
            if (!columnClasses[j]
                .equals(row.getCell(j).getAttribute("class"))) {
              formatter.format(
                  "Expected class attribute for"
                      + "cell(%s,%s) in table containing"
                      + " ID '%s' to be '%s', received" + " '%s'%n",
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
                      + " attribute was not defined in the JSP.%n",
                  i + 1, j + 1, id);
            }
          }
        }

        if (rowClasses != null) {
          if (!rowClasses[i].equals(row.getAttribute("class"))) {
            formatter.format(
                "Expected class attribute for row '%s',"
                    + " in table containing ID '%s' "
                    + " to be '%s', received '%s'%n",
                i + 1, id, rowClasses[i], row.getAttribute("class"));
          }
        } else {
          if (!HtmlElement.ATTRIBUTE_NOT_DEFINED
              .equals(row.getAttribute("class"))) {
            formatter.format("The class attribute for row '%s'"
                + " in table containing ID '%s' should"
                + " not have been rendered as the rowClasses"
                + " attribute was not defined in the JSP.%n", i + 1, id);
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
}
