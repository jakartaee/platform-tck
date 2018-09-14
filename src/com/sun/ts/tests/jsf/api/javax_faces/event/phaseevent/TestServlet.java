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

package com.sun.ts.tests.jsf.api.javax_faces.event.phaseevent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  public void phaseEventGetPhaseIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    LifecycleFactory factory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    PhaseEvent pe = new PhaseEvent(context, PhaseId.ANY_PHASE,
        factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));

    String golden = PhaseId.ANY_PHASE.getName();
    String result = pe.getPhaseId().getName();
    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL
          + " Unexpected result from PhaseEvent.getPhaseId!" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);
    }

  } // End phaseEventGetPhaseIdTest

  public void phaseEventGetFacesContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String summary = "TCKTest";
    FacesContext context = getFacesContext();
    context.addMessage("cid", new FacesMessage(summary));

    LifecycleFactory factory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    PhaseEvent pe = new PhaseEvent(context, PhaseId.ANY_PHASE,
        factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));

    FacesContext myContext = pe.getFacesContext();
    List<FacesMessage> messages = myContext.getMessageList("cid");
    Iterator<FacesMessage> i = messages.iterator();

    while (i.hasNext()) {
      FacesMessage fm = i.next();

      if (summary.equals(fm.getSummary())) {
        out.println(JSFTestUtil.PASS);
        return;

      } else {
        out.println(JSFTestUtil.FAIL
            + " Unable to find Message added to FacesContext!");

      }
    }
  } // End phaseEventGetFacesContextTest

} // End TestServlet
