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

package com.sun.ts.tests.jsf.api.jakarta_faces.render.renderkitfactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.FactoryFinder;
import jakarta.faces.context.ResponseStream;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.RenderKit;
import jakarta.faces.render.RenderKitFactory;
import jakarta.faces.render.Renderer;
import jakarta.faces.render.ResponseStateManager;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws jakarta.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  private RenderKitFactory getFactory() {
    return (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  public void renderKitFactoryAddGetRenderKitTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    RenderKitFactory factory = getFactory();

    TCKRenderKit renderKit = new TCKRenderKit();
    factory.addRenderKit("TCKRenderKit", renderKit);

    RenderKit result = factory.getRenderKit(getFacesContext(), "TCKRenderKit");

    if (result != renderKit) {
      out.println(JSFTestUtil.FAIL + " getRenderKit() didn't return the"
          + " expected value.");
      out.println("Expected: " + renderKit);
      out.println("Received: " + result);
      return;
    }

    // getRenderKit() should return null if there is no RenderKit
    // mapped to the specific identifier.
    if (factory.getRenderKit(getFacesContext(),
        "norenderkitwouldpossiblybemappedtothisid") != null) {
      out.println(JSFTestUtil.FAIL + " Expected getRenderKit(String) to return"
          + " null in the case that the provided render kit ID had no"
          + " associated RenderKit.");
      out.println("RenderKit received: " + factory.getRenderKit(
          getFacesContext(), "norenderkitwouldpossiblybemappedtothisid"));
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitFactoryGetRenderKitIdsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    RenderKitFactory factory = getFactory();
    factory.addRenderKit("TCKRenderKit", new TCKRenderKit());
    int count = 0;
    List ids = new ArrayList();

    List expectedIds = new ArrayList();
    expectedIds.add(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    expectedIds.add("TCKRenderKit");

    for (Iterator i = factory.getRenderKitIds(); i.hasNext();) {
      ids.add(i.next());
      count++;
    }

    if (count < 2) {
      out.println(JSFTestUtil.FAIL + " Expected at least 2 registered"
          + " RenderKit IDs to be retured.");
      out.println("Actual count: " + count);
      return;
    }

    if (!ids.containsAll(expectedIds)) {
      out.println("The Iterator returned by getRenderKitIds() didn't"
          + " return all of the expected results.");
      out.println("Expected Iterator to contain at least the following: " + "'"
          + RenderKitFactory.HTML_BASIC_RENDER_KIT + "', '" + "TCKRenderKit'.");
      out.println("Lifecycle IDs recevied: "
          + JSFTestUtil.getAsString(factory.getRenderKitIds()));
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // --------------------------------------------------------- Private Classes

  private static class TCKRenderKit extends RenderKit {

    public void addRenderer(String family, String rendererType,
        Renderer renderer) {
      // no-op
    }

    public Renderer getRenderer(String family, String rendererType) {
      return null;
    }

    public ResponseStream createResponseStream(OutputStream out) {
      return null;
    }

    public ResponseWriter createResponseWriter(Writer writer,
        String contentTypeList, String characterEncoding) {
      return null;
    }

    public ResponseStateManager getResponseStateManager() {
      return null;
    }
  }

}
