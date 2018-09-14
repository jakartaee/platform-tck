/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.javax_faces.view.viewdeclarationlangwrapper;

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
import javax.faces.view.ViewDeclarationLanguageWrapper;
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

  public void vdlWrapperGetComponentMetadataUSOETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdlWrapper = new TCKVDL(context, JSP_VIEWID);

    String methName = "getComponentMetadata";

    try {
      vdlWrapper.getComponentMetadata(context, resource);
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "()'"
          + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException to be thrown!");

    } catch (UnsupportedOperationException uoe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown for "
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", "
          + resource.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException" + JSFTestUtil.NL
          + "Received: " + e.getClass().getName());
    }

  }// End vdlWrapperGetComponentMetadataUSOETest

  public void vdlWrapperGetScriptComponentResourceUSOETest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    ResourceHandler handler = context.getApplication().getResourceHandler();
    Resource resource = handler.createResource("myComp.xhtml");

    String methName = "getScriptComponentResource";
    ViewDeclarationLanguage vdlWrapper = new TCKVDL(context, JSP_VIEWID);

    try {
      vdlWrapper.getScriptComponentResource(context, resource);
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "()'"
          + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException to be thrown!");

    } catch (UnsupportedOperationException uoe) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown for "
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", "
          + resource.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: UnsupportedOperationException" + JSFTestUtil.NL
          + "Received: " + e.getClass().getName());
    }

  }// End vdlWrapperGetScriptComponentResourceUSOETest

  public void vdlWrapperGetComponentMetadataNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdlWrapper = this.getVDL();

    String methName = "getComponentMetadata";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.getComponentMetadata(null, resource);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + resource.getClass().getSimpleName() + ")'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + resource.getClass().getSimpleName()
              + ")" + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // Resource as 'null'
    try {
      vdlWrapper.getComponentMetadata(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperGetComponentMetadataNPETest

  public void vdlWrapperGetScriptComponentResourceNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    Resource resource = this.getResource(context);
    ViewDeclarationLanguage vdlWrapper = this.getVDL();

    String methName = "getScriptComponentResource";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.getScriptComponentResource(null, resource);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + resource.getClass().getSimpleName() + ")'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + resource.getClass().getSimpleName()
              + ")" + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // Resource as 'null'
    try {
      vdlWrapper.getScriptComponentResource(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperGetScriptComponentResourceNPETest

  public void vdlWrapperRenderViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();
    UIViewRoot view = context.getViewRoot();

    String methName = "renderView";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.renderView(null, view);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + view.getClass().getSimpleName() + ")'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + view.getClass().getSimpleName()
              + ")" + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // UIViewRoot as 'null'
    try {
      vdlWrapper.renderView(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperRenderViewNPETest

  public void vdlWrapperRetargetMethodExpressionsNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();
    UIComponent component = new UIInput();

    String methName = "retargetMethodExpressions";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.retargetMethodExpressions(null, component);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + component.getClass().getSimpleName() + ")'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append("Unexpected Exception thrown for "
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + component.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL + "Received: "
          + e.getClass().getName() + JSFTestUtil.NL);
    }

    // Component as 'null'
    try {
      vdlWrapper.retargetMethodExpressions(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperRetargetMethodExpressionsNPETest

  public void vdlWrapperRetargetAttachedObjectsNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();
    UIComponent component = new UIInput();

    List<AttachedObjectHandler> handlers = new ArrayList<AttachedObjectHandler>();
    handlers.add(new TCKAttachedObjectHandler());

    String methName = "retargetAttachedObjects";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.retargetAttachedObjects(null, component, handlers);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + component.getClass().getSimpleName() + ",  "
          + handlers.getClass().getSimpleName() + ")'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append("Unexpected Exception thrown for "
          + vdlWrapper.getClass().getName() + "." + methName + "(null, "
          + component.getClass().getSimpleName() + ", "
          + handlers.getClass().getSimpleName() + ")" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL + "Received: "
          + e.getClass().getName() + JSFTestUtil.NL);
    }

    // Component as 'null'
    try {
      vdlWrapper.retargetAttachedObjects(context, null, handlers);
      buff.append(
          "No Exception thrown when calling '" + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ", null, " + handlers.getClass().getSimpleName() + ")'"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null, " + handlers.getClass().getSimpleName() + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName());
    }

    // AttachedObjectHandler as 'null'
    try {
      vdlWrapper.retargetAttachedObjects(context, component, null);
      buff.append(
          "No Exception thrown when calling " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ", null, " + handlers.getClass().getSimpleName() + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",  " + component.getClass().getSimpleName() + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperRetargetAttachedObjectsNPETest

  public void vdlWrapperRestoreViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();

    String methName = "restoreView";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.restoreView(null, context.getViewRoot().getViewId());
      buff.append(
          "No Exception thrown when calling '" + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")'"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // viewId as 'null'
    try {
      vdlWrapper.restoreView(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperRestoreViewNPETest

  public void vdlWrapperCreateViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();

    String methName = "createView";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.createView(null, FACELETS_VIEWID);
      buff.append(
          "No Exception thrown when calling '" + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")'"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // viewId as 'null'
    try {
      vdlWrapper.createView(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperCreateViewNPETest

  public void vdlWrapperGetViewMetadataNPETest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    StringBuffer buff = new StringBuffer(50);
    FacesContext context = getFacesContext();

    ViewDeclarationLanguage vdlWrapper = this.getVDL();

    String methName = "getViewMetadata";
    String expected = "NullPointerException";

    // FacesContext as 'null'
    try {
      vdlWrapper.createView(null, FACELETS_VIEWID);
      buff.append(
          "No Exception thrown when calling '" + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")'"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(null, " + FACELETS_VIEWID + ")"
              + JSFTestUtil.NL + "Expected: " + expected + JSFTestUtil.NL
              + "Received: " + e.getClass().getName() + JSFTestUtil.NL);
    }

    // viewId as 'null'
    try {
      vdlWrapper.createView(context, null);
      buff.append("No Exception thrown when calling '"
          + vdlWrapper.getClass().getName() + "." + methName + "("
          + context.getClass().getSimpleName() + ", null)'" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL);

    } catch (NullPointerException npe) {
      // do nothing test passed

    } catch (Exception e) {
      buff.append(
          "Unexpected Exception thrown for " + vdlWrapper.getClass().getName()
              + "." + methName + "(" + context.getClass().getSimpleName()
              + ",null)" + JSFTestUtil.NL + "Expected: " + expected
              + JSFTestUtil.NL + "Received: " + e.getClass().getName());
    }

    if (buff.length() > 0) {
      out.println("Test FAILED. For the following reason(s):" + JSFTestUtil.NL
          + buff.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End vdlWrapperGetViewMetadataNPETest

  // --------------------------------------------- private methods

  private ViewDeclarationLanguage getVDL() {
    ViewDeclarationLanguageWrapper vdlWrapper = new TCKVDL();

    return vdlWrapper;
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

  private static class TCKVDL extends ViewDeclarationLanguageWrapper {
    private FacesContext fc;

    private String vid;

    public TCKVDL() {
    }

    public TCKVDL(FacesContext context, String viewId) {
      this.fc = context;
      this.vid = viewId;
    }

    @Override
    public ViewDeclarationLanguage getWrapped() {
      return fc.getApplication().getViewHandler().getViewDeclarationLanguage(fc,
          vid);

    }

  }

}
