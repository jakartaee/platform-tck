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

import javax.faces.application.Application;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

public class DataTableUIBean {

  private HtmlDataTable books;

  public HtmlDataTable getBooks() {
    return books;
  }

  public void setBooks(HtmlDataTable hdt) {
    this.books = hdt;

    books.setId("books");
    books.setTitle("Books");
    books.setBgcolor("FFFF99");
    books.setBorder(2);

    books.getChildren().add(bookTableColumn("Authors"));
    books.getChildren().add(bookTableColumn("Titles"));
    books.getChildren().add(bookTableColumn("Dates"));

  }

  private HtmlColumn bookTableColumn(String colheader) {
    Application application = FacesContext.getCurrentInstance()
        .getApplication();
    HtmlColumn column = (HtmlColumn) application
        .createComponent(HtmlColumn.COMPONENT_TYPE);
    HtmlOutputText header = (HtmlOutputText) application
        .createComponent(HtmlOutputText.COMPONENT_TYPE);

    column.setId(colheader);
    header.setValue(colheader);
    column.setHeader(header);

    return column;
  }
}
