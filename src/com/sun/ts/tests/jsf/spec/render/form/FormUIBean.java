/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.render.form;

import java.io.Serializable;
import jakarta.faces.application.Application;
import jakarta.faces.component.html.HtmlForm;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;

@jakarta.inject.Named("greeting") @jakarta.enterprise.context.SessionScoped
public class FormUIBean implements Serializable {

  private static final long serialVersionUID = -2123380871451256327L;

  private HtmlForm myForm;

  public HtmlForm getMyForm() {
    return myForm;
  }

  public void setMyForm(HtmlForm myForm) {
    Application application = FacesContext.getCurrentInstance()
        .getApplication();
    HtmlInputText hello = (HtmlInputText) application
        .createComponent(HtmlInputText.COMPONENT_TYPE);

    hello.setId("hello_msg");
    hello.setValue("HELLO");

    myForm.setStyleClass("fancy");
    myForm.getChildren().add(hello);

    this.myForm = myForm;
  }
}
