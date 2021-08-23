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
package com.sun.ts.tests.jsf.api.jakarta_faces.component.uioutput;

import com.sun.ts.tests.jsf.api.jakarta_faces.component.common.BaseComponentTestServlet;

import jakarta.faces.application.ResourceDependencies;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIOutput;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ComponentSystemEventListener;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PostAddToViewEvent;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class TestServlet extends BaseComponentTestServlet {

  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType("jakarta.faces.Text");
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIOutput();
  }
  // --------------------------------------------------------- Private Classes

  @ListenerFor(systemEventClass = PostAddToViewEvent.class, sourceClass = CustomOutput.class)
  @ResourceDependencies({
      @ResourceDependency(name = "#{'hello.js'}", library = "test", target = "#{'body'}"),
      @ResourceDependency(name = "black-n-blue.css", library = "#{'test'}") })

  public static final class CustomOutput extends UIOutput
      implements ComponentSystemEventListener {

    private boolean processEventInvoked;

    private ComponentSystemEvent event;

    @Override
    public void processEvent(ComponentSystemEvent event)
        throws AbortProcessingException {
      processEventInvoked = true;
      this.event = event;
    }

    public void reset() {
      processEventInvoked = false;
      event = null;
    }

    public boolean isProcessEventInvoked() {
      return processEventInvoked;
    }

    public ComponentSystemEvent getEvent() {
      return event;
    }
  }
}
