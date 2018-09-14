/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.view.common;

import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;

public class TCKViewRoot extends UIViewRoot {
  private boolean vutreecalled = Boolean.FALSE;

  private boolean savevucalled = Boolean.FALSE;

  private boolean restorevucalled = Boolean.FALSE;

  // Does not matter what we return here!
  public boolean visitTree(VisitContext context, VisitCallback callback) {
    this.vutreecalled = Boolean.TRUE;

    return Boolean.TRUE;
  }

  // Does not matter what we return here!
  public Object saveView(FacesContext context) {
    this.savevucalled = Boolean.TRUE;

    return Boolean.TRUE;
  }

  // Does not matter what we return here!
  public UIViewRoot restoreView(FacesContext context, String viewId,
      String renderKitId) {
    this.restorevucalled = Boolean.TRUE;

    return context.getViewRoot();
  }

  /**
   * Validate if the visitTree method has been called.
   * 
   * @return - true of visitTree method has been called
   */
  public boolean getVutree() {
    return this.vutreecalled;
  }

  /**
   * Reset the default value so it appears that visitTree method has not been
   * called.
   */
  public void resetVutree() {
    vutreecalled = Boolean.FALSE;
  }

  /**
   * Validate if the saveView method has been called.
   * 
   * @return - true id saveView method has been called
   */
  public boolean getSaveVu() {
    return this.savevucalled;
  }

  /**
   * Validate if the restoreView method has been called.
   * 
   * @return - true id restoreView method has been called
   */
  public boolean getRestoreVu() {
    return this.restorevucalled;
  }
}
