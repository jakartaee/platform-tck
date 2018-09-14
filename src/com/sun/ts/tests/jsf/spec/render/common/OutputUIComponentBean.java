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
package com.sun.ts.tests.jsf.spec.render.common;

import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;

public class OutputUIComponentBean {

  private HtmlOutputLabel label;

  private HtmlOutputLink link;

  private HtmlOutputText text;

  // For Output label binding test.
  public HtmlOutputLabel getLabel() {
    return label;
  }

  public void setLabel(HtmlOutputLabel label) {
    label.setStyleClass("text");
    label.setValue("This is an Output UIComponent");

    this.label = label;
  }

  // For Outputlink binding test.
  public HtmlOutputLink getLink() {
    return link;
  }

  public void setLink(HtmlOutputLink link) {
    link.setId("case_three");
    link.setStyleClass("text");
    link.setValue("encodetest.jsp");

    this.link = link;
  }

  // For OutputText binding test.
  public HtmlOutputText getText() {
    return text;
  }

  public void setText(HtmlOutputText text) {
    text.setStyleClass("text");

    this.text = text;
  }
}
