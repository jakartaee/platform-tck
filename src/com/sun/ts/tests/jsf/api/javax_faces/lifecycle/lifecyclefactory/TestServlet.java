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

package com.sun.ts.tests.jsf.api.javax_faces.lifecycle.lifecyclefactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
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

  private LifecycleFactory getFactory() {
    return (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
  }

  // ------------------------------------------------------- Test Methods

  public void lifecycleFactoryGetLifecycleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = getFactory();
    Lifecycle lifecycle = factory
        .getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

    if (lifecycle == null) {
      out.println(JSFTestUtil.FAIL + " Using getLifecycle() to request"
          + " the default Lifecycle instance of the application"
          + " returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleFactoryGetLifecycleIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = getFactory();

    try {
      factory.getLifecycle("totallyinvalidlifecycleid");
      out.println(JSFTestUtil.FAIL + " No exception thrown when passing"
          + " a lifecycle ID for which there is no lifecycle registered.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception throw when passing"
            + " an unregistered lifecycle ID, but it wasn't an instance"
            + " of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleFactoryAddLifecycleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = getFactory();

    Lifecycle lifecycle = new TCKLifecycle();
    factory.addLifecycle("tcklifecycle", lifecycle);

    // make sure it can be obtained via a call to getLifecycle(String)
    Lifecycle retLife = factory.getLifecycle("tcklifecycle");
    if (lifecycle != retLife) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Added new lifecycle via call to "
          + "addLifecycle(String, Lifecycle), but the lifecycle was"
          + "not returned as expected by a call to " + "getLifecycle(String)");
      out.println("Expected: " + lifecycle.getClass().getSimpleName());
      out.println("Received: " + retLife.getClass().getSimpleName());
      return;
    }

    try {
      factory.addLifecycle("tcklifecycle", new TCKLifecycle());
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + " No Exception thrown when attempting"
          + "to add a Lifecycle using an ID that already exists.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + " Exception thrown when attempting"
            + " to add a Lifecycle using an ID that has already "
            + "been registerd, but it wasn't an instance of"
            + " IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleFactoryGetLifecycleIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = getFactory();

    Lifecycle lifecycle = new TCKLifecycle();
    factory.addLifecycle("tcklifecycle2", lifecycle);

    int count = 0;
    List<String> ids = new ArrayList<String>();

    List<String> expectedIds = new ArrayList<String>();
    expectedIds.add(LifecycleFactory.DEFAULT_LIFECYCLE);
    expectedIds.add("tcklifecycle2");

    for (Iterator<String> i = factory.getLifecycleIds(); i.hasNext();) {
      ids.add(i.next());
      count++;
    }

    if (count < 2) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected at least 2 registered" + " lifecycle IDs to be retured.");
      out.println("Actual count: " + count);
      return;
    }

    if (!ids.containsAll(expectedIds)) {
      out.println("The Iterator returned by getLifecycleIds() didn't"
          + " return all of the expected results." + JSFTestUtil.NL
          + "Expected Iterator to contain at least the following: " + "'"
          + LifecycleFactory.DEFAULT_LIFECYCLE + "', '" + "tcklifecycle2'"
          + JSFTestUtil.NL + "Lifecycle IDs recevied: "
          + JSFTestUtil.getAsString(factory.getLifecycleIds()));
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void lifecycleFactoryAddLifecycleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LifecycleFactory factory = getFactory();
    Lifecycle cycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

    // null lifecycleId
    JSFTestUtil.checkForNPE(factory, "addLifecycle",
        new Class<?>[] { String.class, Lifecycle.class },
        new Object[] { null, cycle }, pw);

    // null lifecycle
    JSFTestUtil.checkForNPE(factory, "addLifecycle",
        new Class<?>[] { String.class, Lifecycle.class },
        new Object[] { "abc", null }, pw);
  }

  public void lifecycleFactoryGetLifecycleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LifecycleFactory factory = getFactory();

    // null lifecycleId
    JSFTestUtil.checkForNPE(factory, "getLifecycle",
        new Class<?>[] { String.class }, new Object[] { null }, pw);
  }

  public void lifecycleFactoryGetWrappedNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = getFactory().getWrapped();

    if (factory == null) {
      out.println(JSFTestUtil.PASS);
      return;
    }

    out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
        + "The default of LifecycleFactory.getWrapped() should return "
        + "null!");
  }

  // --------------------------------------------------------- Private Classes

  private static class TCKLifecycle extends Lifecycle {

    public void render(FacesContext context) throws FacesException {
      // no-op
    }

    public void addPhaseListener(PhaseListener listener) {
      // no-op
    }

    public void execute(FacesContext context) throws FacesException {
      // no-op
    }

    public PhaseListener[] getPhaseListeners() {
      return new PhaseListener[0];
    }

    public void removePhaseListener(PhaseListener listener) {
      // no-op
    }
  }
}
