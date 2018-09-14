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
package com.sun.ts.tests.jsf.api.javax_faces.view.viewdeclarationlang;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  private static final String JSP_VIEWID = "/root.jsp";

  private static final String FACELETS_VIEWID = "/root.xhtml";

  /**
   * <code>init</code> initializes the servlet.
   * 
   * @param config
   *          - <code>ServletConfig</code>
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void vdlGetComponentMetadataUSOETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdl = this.getVDL(context, JSP_VIEWID);

    String methName = "getComponentMetadata";

    try {
      vdl.getComponentMetadata(context, resource);
      out.println(JSFTestUtil.FAIL + " " + JSFTestUtil.NL
          + "No Exception thrown when calling '" + vdl.getClass().getName()
          + "." + methName + "()'" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException to be thrown!");

    } catch (UnsupportedOperationException uoe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown for "
          + vdl.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", "
          + resource.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException" + JSFTestUtil.NL
          + "Received: " + e.getClass().getName());
    }

  }// End vdlGetComponentMetadataUSOETest

  public void vdlGetScriptComponentResourceUSOETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    ResourceHandler handler = context.getApplication().getResourceHandler();
    Resource resource = handler.createResource("myComp.xhtml");

    String methName = "getScriptComponentResource";
    ViewDeclarationLanguage vdl = this.getVDL(context, JSP_VIEWID);

    try {
      vdl.getScriptComponentResource(context, resource);
      out.println(JSFTestUtil.FAIL + " " + JSFTestUtil.NL
          + "No Exception thrown when calling '" + vdl.getClass().getName()
          + "." + methName + "()'" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException to be thrown!");

    } catch (UnsupportedOperationException uoe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown for "
          + vdl.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", "
          + resource.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException" + JSFTestUtil.NL
          + "Received: " + e.getClass().getName());
    }

  }// End vdlGetScriptComponentResourceUSOETest

  public void vdlGetComponentMetadataNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "getComponentMetadata",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { null, resource }, pw);

    // Resource as 'null'
    JSFTestUtil.checkForNPE(vdl, "getComponentMetadata",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { context, null }, pw);

  }// End vdlGetComponentMetadataNPETest

  public void vdlGetScriptComponentResourceNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "getScriptComponentResource",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { null, resource }, pw);

    // Resource as 'null'
    JSFTestUtil.checkForNPE(vdl, "getScriptComponentResource",
        new Class<?>[] { FacesContext.class, Resource.class },
        new Object[] { context, null }, pw);

  }// End vdlGetScriptComponentResourceNPETest

  public void vdlRenderViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);
    UIViewRoot view = context.getViewRoot();

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "renderView",
        new Class<?>[] { FacesContext.class, UIViewRoot.class },
        new Object[] { null, view }, pw);

    // UIViewRoot as 'null'
    JSFTestUtil.checkForNPE(vdl, "renderView",
        new Class<?>[] { FacesContext.class, UIViewRoot.class },
        new Object[] { context, null }, pw);

  }// End vdlRenderViewNPETest

  public void vdlRetargetMethodExpressionsNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);
    UIComponent component = new UIInput();

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "retargetMethodExpressions",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { null, component }, pw);

    // Component as 'null'
    JSFTestUtil.checkForNPE(vdl, "retargetMethodExpressions",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { context, null }, pw);

  }// End vdlRetargetMethodExpressionsNPETest

  public void vdlRetargetAttachedObjectsNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);
    UIComponent component = new UIInput();

    List<AttachedObjectHandler> handlers = new ArrayList<AttachedObjectHandler>();
    handlers.add(new TCKAttachedObjectHandler());

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "retargetAttachedObjects",
        new Class<?>[] { FacesContext.class, UIComponent.class, List.class },
        new Object[] { null, component, handlers }, pw);

    // Component as 'null'
    JSFTestUtil.checkForNPE(vdl, "retargetAttachedObjects",
        new Class<?>[] { FacesContext.class, UIComponent.class, List.class },
        new Object[] { context, null, handlers }, pw);

    // AttachedObjectHandler as 'null'
    JSFTestUtil.checkForNPE(vdl, "retargetAttachedObjects",
        new Class<?>[] { FacesContext.class, UIComponent.class, List.class },
        new Object[] { context, component, null }, pw);

  }// End vdlRetargetAttachedObjectsNPETest

  public void vdlRestoreViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    String vid = context.getViewRoot().getViewId();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "restoreView",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { null, vid }, pw);

    // viewId as 'null'
    JSFTestUtil.checkForNPE(vdl, "restoreView",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { context, null }, pw);

  }// End vdlRestoreViewNPETest

  public void vdlCreateViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "createView",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { null, FACELETS_VIEWID }, pw);

    // viewId as 'null'
    JSFTestUtil.checkForNPE(vdl, "createView",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { context, null }, pw);

  }// End vdlCreateViewNPETest

  public void vdlGetViewMetadataNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ViewDeclarationLanguage vdl = this.getVDL(context, FACELETS_VIEWID);

    // FacesContext as 'null'
    JSFTestUtil.checkForNPE(vdl, "getViewMetadata",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { null, FACELETS_VIEWID }, pw);

    // viewId as 'null'
    JSFTestUtil.checkForNPE(vdl, "getViewMetadata",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { context, null }, pw);

  }// End vdlGetViewMetadataNPETest

  public void vdlGetViewMetaDataTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String root = "myRoot.xhtml";

    ViewDeclarationLanguage vdl = this.getVDL(context, root);
    ViewMetadata vm = vdl.getViewMetadata(context, root);
    String result = vm.getViewId();

    if (root.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + " Unexpected value found!" + JSFTestUtil.NL
          + "Expected: " + root + JSFTestUtil.NL + "Received: " + result);

    }

  }// End vdlGetViewMetaDataTest

  public void vdlGetIdTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String root = "myRoot.xhtml";

    ViewDeclarationLanguage vdl = this.getVDL(context, root);

    String result = vdl.getId();

    if (ViewDeclarationLanguage.FACELETS_VIEW_DECLARATION_LANGUAGE_ID
        .equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value found!"
          + JSFTestUtil.NL + "Expected: " + root + JSFTestUtil.NL + "Received: "
          + result);

    }

  }// End vdlGetIdTest

  public void vdlViewExistsTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    String root = "myRoot.xhtml";

    ViewDeclarationLanguage vdl = this.getVDL(context, root);

    if (vdl.viewExists(context, root)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "View did not exists!"
          + JSFTestUtil.NL + "Expected: " + root + " to exist.");

    }

  }// End vdlViewExistsTest

  // --------------------------------------------- private methods

  private ViewDeclarationLanguage getVDL(FacesContext context, String viewId) {
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, viewId);

    return vdl;
  }

  private Resource getResource(FacesContext context) {
    ResourceHandler handler = context.getApplication().getResourceHandler();
    Resource resource = handler.createResource("myComp.xhtml");

    return resource;
  }

  // --------------------------------------------- private classes

  private static class TCKAttachedObjectHandler
      implements AttachedObjectHandler {

    @Override
    public void applyAttachedObject(FacesContext arg0, UIComponent arg1) {
      // Do nothing this is a Class for test only.

    }

    @Override
    public String getFor() {
      // Do nothing this is a Class for test only.
      return null;
    }

  }

}
