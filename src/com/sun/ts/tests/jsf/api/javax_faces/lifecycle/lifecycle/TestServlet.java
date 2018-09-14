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
 * $Id$
 */

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecycle;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws javax.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  private Lifecycle getLifecycle() {
    LifecycleFactory factory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    return factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
  }

  // ------------------------------------------------ Test Methods

  public void lifecycleAddGetRemovePhaseListenersTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    TCKPhaseListener listener = new TCKPhaseListener("id", PhaseId.ANY_PHASE);

    Lifecycle lifecycle = getLifecycle();

    // clear the listeners out
    PhaseListener[] listeners = lifecycle.getPhaseListeners();
    for (int i = 0; i < listeners.length; i++) {
      lifecycle.removePhaseListener(listeners[i]);
    }

    lifecycle.addPhaseListener(listener);

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 1) {
      out.println(
          JSFTestUtil.FAIL + " Expected the number of listeners returned"
              + " by getPhaseListeners() to be 1.");
      out.println("Number received: " + listeners.length);
      return;
    }

    lifecycle
        .removePhaseListener(new TCKPhaseListener("id1", PhaseId.ANY_PHASE));

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 1) {
      out.println("Test FAILED[2].  Expected the number of listeners returned"
          + " by getPhaseListeners() to be 1.");
      out.println("Number received: " + listeners.length);
      return;
    }

    lifecycle.removePhaseListener(listener);

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 0) {
      out.println(
          JSFTestUtil.FAIL + " Expected the number of listeners returned"
              + " by getPhaseListeners() to be 0.");
      out.println("Number received: " + listeners.length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleAddPhaseListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Lifecycle lifecycle = getLifecycle();

    JSFTestUtil.checkForNPE(lifecycle, "addPhaseListener",
        new Class<?>[] { PhaseListener.class }, new Object[] { null }, pw);
  }

  public void lifecycleRemovePhaseListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Lifecycle lifecycle = getLifecycle();

    JSFTestUtil.checkForNPE(lifecycle, "removePhaseListener",
        new Class<?>[] { PhaseListener.class }, new Object[] { null }, pw);
  }

  public void lifecycleExecuteNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Lifecycle lifecycle = getLifecycle();

    JSFTestUtil.checkForNPE(lifecycle, "execute",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);
  }

  public void lifecycleRenderNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    Lifecycle lifecycle = getLifecycle();

    JSFTestUtil.checkForNPE(lifecycle, "render",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);
  }

  // ------------------------------------------------ Private Classes

  private static class TCKPhaseListener implements PhaseListener {

    private String id;

    private PhaseId phaseId;

    private StringBuffer log;

    private TCKPhaseListener() {
    }

    public TCKPhaseListener(String id, PhaseId phaseId) {
      this.id = id;
      this.phaseId = phaseId;
      log = new StringBuffer();
    }

    public void afterPhase(PhaseEvent event) {
      log.append("/A" + id + '@' + JSFTestUtil.getPhaseIdAsString(phaseId));
    }

    public void beforePhase(PhaseEvent event) {
      log.append("/B" + id + '@' + JSFTestUtil.getPhaseIdAsString(phaseId));
    }

    public PhaseId getPhaseId() {
      return phaseId;
    }

    public String getTrace() {
      return log.toString();
    }

    public void resetTrace() {
      log = new StringBuffer();
    }

  }
}
