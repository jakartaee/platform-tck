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

public class StatBean {
  private String formOneOutput = "";

  private String formTwoOutput = "";

  private String text = "oldValue";

  /** Creates a new instance of StatBean */
  public StatBean() {
  }

  public String getForm1() {
    return "";
  }

  public void setForm1(String form1) {
    this.setFormOneOutput(form1);
    this.setFormTwoOutput("");
  }

  public String getForm2() {
    return "";
  }

  public void setForm2(String form2) {
    this.setFormOneOutput("");
    this.setFormTwoOutput(form2);
  }

  public String getFormOneOutput() {
    return formOneOutput;
  }

  public void setFormOneOutput(String formOneOutput) {
    this.formOneOutput = formOneOutput;
  }

  public String getFormTwoOutput() {
    return formTwoOutput;
  }

  public void setFormTwoOutput(String formTwoOutput) {
    this.formTwoOutput = formTwoOutput;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
