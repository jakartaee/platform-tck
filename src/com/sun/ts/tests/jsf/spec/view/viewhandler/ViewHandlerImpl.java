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
 *  $Id$
 */
package com.sun.ts.tests.jsf.spec.view.viewhandler;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class ViewHandlerImpl extends ViewHandlerWrapper {

  protected ViewHandler viewHandler;

  public ViewHandlerImpl(ViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public ViewHandler getWrapped() {
    return viewHandler;
  }

  @Override
  public UIViewRoot createView(FacesContext context, String viewId) {
    UIViewRoot root = this.getWrapped().createView(context, viewId);
    if (!root.getViewId().endsWith(".jsp")) {
      throw new FacesException("createView: ViewId has been "
          + "manipulated before being passed to the ViewHandler."
          + JSFTestUtil.NL + "Expected viewId: /greetings.jsp" + JSFTestUtil.NL
          + "Received viewId: " + viewId + JSFTestUtil.NL);
    }
    return root;
  }

  @Override
  public UIViewRoot restoreView(FacesContext context, String viewId) {
    UIViewRoot root = this.getWrapped().restoreView(context, viewId);
    if (!root.getViewId().endsWith(".jsp")) {
      throw new FacesException("restoreView: ViewId has been "
          + "manipulated before being passed to the ViewHandler. "
          + JSFTestUtil.NL + "Expected viewId: /greetings.jsp" + JSFTestUtil.NL
          + "Received viewId: " + viewId + JSFTestUtil.NL);
    }
    return root;
  }
}
