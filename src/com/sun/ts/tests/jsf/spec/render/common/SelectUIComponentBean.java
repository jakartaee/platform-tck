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

package com.sun.ts.tests.jsf.spec.render.common;

import java.io.Serializable;
import jakarta.faces.application.Application;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItem;
import jakarta.faces.context.FacesContext;

@jakarta.inject.Named("Answer") @jakarta.enterprise.context.SessionScoped
public class SelectUIComponentBean implements Serializable {

  private static final long serialVersionUID = -2564323472383456327L;

  private UIComponent yesNo;

  public UIComponent getYesNo() {
    return yesNo;
  }

  public void setYesNo(UIComponent answer) {
    Application application = FacesContext.getCurrentInstance()
        .getApplication();

    UISelectItem yes = (UISelectItem) application
        .createComponent(UISelectItem.COMPONENT_TYPE);
    UISelectItem no = (UISelectItem) application
        .createComponent(UISelectItem.COMPONENT_TYPE);

    answer.setId("ManySelectItems");

    // Setup the checkboxes.
    yes.setId("y");
    yes.setItemLabel("yes");
    yes.setItemValue("yes");

    no.setId("n");
    no.setItemLabel("no");
    no.setItemValue("no");

    // Added the selectItems to the Many UIComponent.
    answer.getChildren().add(no);
    answer.getChildren().add(yes);

    this.yesNo = answer;
  }
}
