/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.viewhandler;

import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class TCKViewHandler extends ViewHandler {

  @Override
  public Locale calculateLocale(FacesContext context) {
    return null;
  }

  @Override
  public String calculateRenderKitId(FacesContext context) {
    return null;
  }

  @Override
  public UIViewRoot createView(FacesContext context, String viewId) {
    return null;
  }

  @Override
  public String getActionURL(FacesContext context, String viewId) {
    return null;
  }

  @Override
  public String getResourceURL(FacesContext context, String path) {
    return null;
  }

  @Override
  public void renderView(FacesContext context, UIViewRoot viewToRender)
      throws IOException, FacesException {
    return;
  }

  @Override
  public UIViewRoot restoreView(FacesContext context, String viewId) {
    return null;
  }

  @Override
  public void writeState(FacesContext context) throws IOException {
    return;
  }

  public String getWebsocketURL(FacesContext context, String channel) {
    return null;
  }
}
