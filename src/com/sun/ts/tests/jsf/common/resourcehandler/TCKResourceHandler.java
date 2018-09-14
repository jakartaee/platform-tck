/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.resourcehandler;

import java.io.IOException;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

public class TCKResourceHandler extends ResourceHandler {

  @Override
  public Resource createResource(String arg0) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Resource createResource(String arg0, String arg1) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Resource createResource(String arg0, String arg1, String arg2) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleResourceRequest(FacesContext arg0) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isResourceRequest(FacesContext arg0) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getRendererTypeForResourceName(String arg0) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean libraryExists(String arg0) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
