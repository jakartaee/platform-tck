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

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecyclewrapper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.LifecycleWrapper;
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

  // ------------------------------------------------------- Test Methods

  public void lifecycleWpprAddGetRemovePhaseListenersTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    TCKPhaseListener listener = new TCKPhaseListener("id", PhaseId.ANY_PHASE);

    Lifecycle lifecycle = new TCKLifecycleWrapper().getWrapped();

    // clear the listeners out
    PhaseListener[] listeners = lifecycle.getPhaseListeners();
    for (int i = 0; i < listeners.length; i++) {
      lifecycle.removePhaseListener(listeners[i]);
    }

    lifecycle.addPhaseListener(listener);

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 1) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected the number of listeners returned "
          + "by getPhaseListeners() to be 1. " + JSFTestUtil.NL
          + "Number received: " + listeners.length);
      return;
    }

    lifecycle
        .removePhaseListener(new TCKPhaseListener("id1", PhaseId.ANY_PHASE));

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 1) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected the number of listeners "
          + "returned by getPhaseListeners() to be 1." + JSFTestUtil.NL
          + "Number received: " + listeners.length);
      return;
    }

    lifecycle.removePhaseListener(listener);

    listeners = lifecycle.getPhaseListeners();
    if (listeners.length != 0) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected the number of listeners returned "
          + "by getPhaseListeners() to be 0. " + JSFTestUtil.NL
          + "Number received: " + listeners.length);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleWpprAddPhaseListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Lifecycle lifecycle = new TCKLifecycleWrapper().getWrapped();

    try {
      lifecycle.addPhaseListener(null);
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No exception thrown when attemtping to "
          + "add a null PhaseListener to the current Lifecycle.");
      return;

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Wrong Exception thrown when attempting to "
          + "add a null PhaseListener to the current Lifecycle. "
          + JSFTestUtil.NL + "Expected: NullPointerException." + JSFTestUtil.NL
          + "Received: " + e.getClass().getName());
    }

  }

  public void lifecycleWpprRemovePhaseListenerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Lifecycle lifecycle = new TCKLifecycleWrapper().getWrapped();

    try {
      lifecycle.removePhaseListener(null);
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No exception thrown when attemtping to "
          + "provide a null argument to remove a PhaseListener.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Exception thrown when attempting to "
            + "provide a null argument to remove a PhaseListener "
            + "from the the current Lifecycle, but it wasn't an "
            + "instance of NullPointerException." + JSFTestUtil.NL
            + "Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleWpprRenderNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Lifecycle lifecycle = new TCKLifecycleWrapper().getWrapped();

    try {
      lifecycle.render(null);
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No exception thrown when attemtping to "
          + "render while passing in a null for FacesContext.");
      return;

    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Wrong Exception thrown when attempting to "
          + "render while passing in a null for FacesContext. " + JSFTestUtil.NL
          + "Expected: NullPointerException." + JSFTestUtil.NL + "Received: "
          + e.getClass().getName());
    }

  }
  // --------------------------------------------------------- Private Classes

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

  private static class TCKLifecycleWrapper extends LifecycleWrapper {

    @Override
    public Lifecycle getWrapped() {
      LifecycleFactory factory = (LifecycleFactory) FactoryFinder
          .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

      return factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }

  }
}
