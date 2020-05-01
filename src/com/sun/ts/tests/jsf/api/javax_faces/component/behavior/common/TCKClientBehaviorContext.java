/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common;

import java.util.Collection;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.context.FacesContext;

public class TCKClientBehaviorContext extends ClientBehaviorContext {

  @Override
  public FacesContext getFacesContext() {
    return null;
  }

  @Override
  public UIComponent getComponent() {
    return null;
  }

  @Override
  public String getEventName() {
    return null;
  }

  @Override
  public String getSourceId() {
    return null;
  }

  @Override
  public Collection<Parameter> getParameters() {
    return null;
  }

}
