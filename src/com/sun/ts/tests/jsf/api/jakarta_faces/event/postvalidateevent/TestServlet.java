/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.jakarta_faces.event.postvalidateevent;

import com.sun.ts.tests.jsf.api.jakarta_faces.event.common.BaseComponentSystemEventTestServlet;

import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.PostValidateEvent;

public class TestServlet extends BaseComponentSystemEventTestServlet {

  private static final UICommand uic = new UICommand();

  @Override
  protected ComponentSystemEvent createEvent(UIComponent component) {
    return new PostValidateEvent(component);
  }

  @Override
  protected UIComponent getTestComponent() {
    return uic;
  }

  // ------------------------------------------------------------ test methods

}
