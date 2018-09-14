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

package com.sun.ts.tests.jsf.api.javax_faces.render.renderkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
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

  public RenderKit getRenderKit() {
    RenderKitFactory factory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);

    return factory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
  }

  // ---------------------------------------------------- Test Methods

  public void renderKitAddGetRendererTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    TCKRenderer renderer = new TCKRenderer();
    RenderKit defaultKit = getRenderKit();

    defaultKit.addRenderer("TCK", "TCK", renderer);
    Renderer result = defaultKit.getRenderer("TCK", "TCK");

    if (result != renderer) {
      out.println(JSFTestUtil.FAIL + " Unexpected result returned"
          + " from getRenderer() when requesting a Renderer that"
          + " was added via addRenderer().");
      out.println("Expected: " + renderer);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitGetRendererTypesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    TCKRenderer renderer = new TCKRenderer();
    RenderKit defaultKit = getRenderKit();
    String type = "TCK";

    defaultKit.addRenderer(type, type, renderer);
    Iterator<String> itr = defaultKit.getRendererTypes(type);
    String result = null;

    while (itr.hasNext()) {
      result = itr.next();
    }

    if (!type.equals(result)) {
      out.println(JSFTestUtil.FAIL + " Unexpected result returned"
          + " from getRendererTypes() when requesting a Renderer "
          + "that was added via addRenderer().");
      out.println("Expected: " + type);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitAddGetClientBehaviorRendererTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ClientBehaviorRenderer renderer = new TCKcbr();
    RenderKit defaultKit = getRenderKit();

    defaultKit.addClientBehaviorRenderer("TCK", renderer);
    ClientBehaviorRenderer result = defaultKit.getClientBehaviorRenderer("TCK");

    if (result != renderer) {
      out.println(JSFTestUtil.FAIL + " Unexpected result returned"
          + " from getRenderer() when requesting a Renderer that"
          + " was added via addRenderer().");
      out.println("Expected: " + renderer);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitAddRendererNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    pw.println("Testing: addRenderer(null, 'TCK', new TCKRenderer())");
    JSFTestUtil.checkForNPE(defaultKit, "addRenderer",
        new Class<?>[] { String.class, String.class, Renderer.class },
        new Object[] { null, "TCK", new TCKRenderer() }, pw);

    pw.println("Testing: addRenderer('TCK', null, new TCKRenderer())");
    JSFTestUtil.checkForNPE(defaultKit, "addRenderer",
        new Class<?>[] { String.class, String.class, Renderer.class },
        new Object[] { "TCK", null, new TCKRenderer() }, pw);

    pw.println("Testing: addRenderer('TCK', 'TCK', null)");
    JSFTestUtil.checkForNPE(defaultKit, "addRenderer",
        new Class<?>[] { String.class, String.class, Renderer.class },
        new Object[] { "TCK", "TCK", null }, pw);

  }

  public void renderKitGetRendererNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    pw.println("Testing: getRenderer('TCK', null)");
    JSFTestUtil.checkForNPE(defaultKit, "getRenderer",
        new Class<?>[] { String.class, String.class },
        new Object[] { "TCK", null }, pw);

    pw.println("Testing: getRenderer(null, 'TCK')");
    JSFTestUtil.checkForNPE(defaultKit, "getRenderer",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, "TCK" }, pw);

  }

  public void renderKitAddClientBehaviorRendererNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    pw.println("Testing: addClientBehaviorRenderer('TCK', null)");
    JSFTestUtil.checkForNPE(defaultKit, "addClientBehaviorRenderer",
        new Class<?>[] { String.class, ClientBehaviorRenderer.class },
        new Object[] { "TCK", null }, pw);

    pw.println("Testing: getRenderer(null, 'TCK')");
    JSFTestUtil.checkForNPE(defaultKit, "addClientBehaviorRenderer",
        new Class<?>[] { String.class, ClientBehaviorRenderer.class },
        new Object[] { null, new TCKcbr() }, pw);

  }

  public void renderKitGetClientBehaviorRendererNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    pw.println("Testing: getClientBehaviorRenderer(null)");
    JSFTestUtil.checkForNPE(defaultKit, "getClientBehaviorRenderer",
        new Class<?>[] { String.class }, new Object[] { null }, pw);

  }

  public void renderKitCreateResponseStreamTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();

    RenderKit defaultKit = getRenderKit();

    ResponseStream stream = defaultKit.createResponseStream(bOut);

    if (stream == null) {
      out.println(JSFTestUtil.FAIL + " createResponseStream() returned null.");
      return;
    }

    stream.write("string".getBytes());
    stream.flush();

    if (bOut.toString().indexOf("string") < 0) {
      out.println(JSFTestUtil.FAIL + " Expected an invocation of method on"
          + " newly created ResponseStream would have written the bytes"
          + " to the underlying ByteArrayOutputStream().  Unexpected result"
          + " found.");
      out.println("Expected to find the term 'string' in the result.");
      out.println("Result: " + bOut.toString());
    }

    try {
      stream.close();
    } catch (IOException ioe) {
      // ignore
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitCreateResponseWriterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    OutputStreamWriter wOut = new OutputStreamWriter(bOut);

    RenderKit defaultKit = getRenderKit();

    ResponseWriter writer = defaultKit.createResponseWriter(wOut, "text/html",
        "ISO-8859-1");

    if (writer == null) {
      out.println(JSFTestUtil.FAIL + " createResponseStream() returned null.");
      return;
    }

    writer.write("string");
    writer.flush();
    wOut.flush();

    if (bOut.toString().indexOf("string") < 0) {
      out.println(JSFTestUtil.FAIL + " Expected an invocation of method on"
          + " newly created ResponseStream would have written the bytes"
          + " to the underlying ByteArrayOutputStream().  Unexpected result"
          + " found.");
      out.println("Expected to find the term 'string' in the result.");
      out.println("Result: " + bOut.toString());
      return;
    }

    try {
      writer.close();
    } catch (IOException ioe) {
      // ignore
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitCreateResponseWriterInvalidContentTypeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    try {
      defaultKit.createResponseWriter(new StringWriter(),
          "invalid/content-type", "ISO-8859-1");
      out.println("No Exception thrown when passing an invalid content"
          + " type to the createResponseWriter() method.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when passing an"
            + " invalid content type to createResponseWriter(), but"
            + " it wasn't an instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitCreateResponseWriterInvalidEncodingTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    try {
      defaultKit.createResponseWriter(new StringWriter(), "text/html",
          "noSuchEncoding");
      out.println("No Exception thrown when passing an invalid encoding"
          + " to the createResponseWriter() method.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when passing an"
            + " invalid encoding to createResponseWriter(), but"
            + " it wasn't an instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitGetResponseStateManagerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    RenderKit defaultKit = getRenderKit();

    ResponseStateManager manager = defaultKit.getResponseStateManager();

    if (manager == null) {
      out.println(
          JSFTestUtil.FAIL + " getResponseStateManager() returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void renderKitGetClientBehaviorRendererTypesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ClientBehaviorRenderer renderer = new TCKcbr();
    RenderKit defaultKit = getRenderKit();
    String type = "TCK";

    defaultKit.addClientBehaviorRenderer(type, renderer);
    Iterator<String> itr = defaultKit.getRendererTypes(type);
    String result = null;

    while (itr.hasNext()) {
      result = itr.next();
    }

    if (!type.equals(result)) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected result returned from "
              + "getClientBehaviorRendererTypes() when requesting "
              + "a Renderer that was added via "
              + "addClientBehaviorRendererRenderer()." + JSFTestUtil.NL
              + "Expected: " + type + JSFTestUtil.NL + "Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ----------------------------------------------------------Private Classes

  private static class TCKRenderer extends Renderer {
    public String convertClientId(FacesContext context, String clientId) {
      return super.convertClientId(context, clientId);
    }

    public void decode(FacesContext context, UIComponent component) {
      super.decode(context, component);
    }

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
      super.encodeBegin(context, component);
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
      super.encodeChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
      super.encodeEnd(context, component);
    }

    public boolean getRendersChildren() {
      return super.getRendersChildren();
    }
  }

  private class TCKcbr extends ClientBehaviorRenderer {
    // do nothing needed a ClientBehaviorRenderer type for NPE tests.
  }

}
