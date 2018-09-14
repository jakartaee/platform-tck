/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: TestServlet.java 62525 2011-04-15 12:14:31Z dougd $
 */

package com.sun.ts.tests.jsf.api.javax_faces.event.postrenderviewevent;

import com.sun.ts.tests.jsf.api.javax_faces.event.common.BaseComponentSystemEventTestServlet;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ComponentSystemEvent;

import javax.faces.event.PostRenderViewEvent;

public class TestServlet extends BaseComponentSystemEventTestServlet {

  private static final UIViewRoot uic = new UIViewRoot();

  @Override
  protected ComponentSystemEvent createEvent(UIComponent component) {
    return new PostRenderViewEvent((UIViewRoot) component);
  }

  @Override
  protected UIComponent getTestComponent() {
    return uic;
  }
  // ------------------------------------------------------------ test methods
}
