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

package com.sun.ts.tests.jsf.spec.render.commandbutton;

import java.io.Serializable;
import jakarta.faces.component.html.HtmlCommandButton;

@jakarta.inject.Named("status") @jakarta.enterprise.context.SessionScoped
public class CommandButtonUIBean implements Serializable {

  private static final long serialVersionUID = -2574855687654356327L;

  private HtmlCommandButton onoff;

  public HtmlCommandButton getOnoff() {
    return onoff;
  }

  public void setOnoff(HtmlCommandButton onoff) {
    onoff.setValue("onoff");
    onoff.setStyleClass("blue");
    onoff.setTitle("onoff");

    this.onoff = onoff;
  }

}
