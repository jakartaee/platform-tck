/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.composite.actionsource;

import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

public class BeanOne implements ActionListener {

  public ActionListener getTestOneEventListener() {
    ActionListener result = new BeanOne();
    return result;
  }

  @Override
  public void processAction(ActionEvent e) {
    FacesContext context = FacesContext.getCurrentInstance();
    context.getExternalContext().getRequestMap().put("actionCalled", "PASSED");
  }

  private boolean isTransient;

  public boolean isTransient() {
    return isTransient;
  }

  public void setTransient(boolean isTransient) {
    this.isTransient = isTransient;
  }

  public Object saveState(FacesContext context) {
    return null;
  }

  public void restoreState(FacesContext context, Object stateObj) {
  }
}
