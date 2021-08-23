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
package com.sun.ts.tests.jsf.api.jakarta_faces.event.ajaxbehaviorevent;

import com.sun.ts.tests.jsf.api.jakarta_faces.event.common.BaseBehaviorEventTestServlet;

import jakarta.faces.component.UICommand;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.AjaxBehavior;
import jakarta.faces.component.behavior.Behavior;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;

public class TestServlet extends BaseBehaviorEventTestServlet {

  private static final UICommand uic = new UICommand();

  private static final Behavior behavior = new AjaxBehavior();

  @Override
  protected BehaviorEvent createEvent(UIComponent component,
      Behavior behavior) {
    return new AjaxBehaviorEvent(component, behavior);
  }

  @Override
  protected Behavior getTestBehavior() {
    return behavior;
  }

  @Override
  protected UIComponent getTestComponent() {
    return uic;
  }

  // ------------------------------------------------------------- test methods
}
