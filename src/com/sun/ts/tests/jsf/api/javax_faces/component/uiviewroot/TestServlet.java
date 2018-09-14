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
package com.sun.ts.tests.jsf.api.javax_faces.component.uiviewroot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseListener;
import javax.faces.event.SystemEventListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.beans.AlbumBean;
import com.sun.ts.tests.jsf.common.event.TCKSystemEvent;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TestServlet extends BaseComponentTestServlet {

  private static final String TEST_VALUE = "Default_Test_Value";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType(null);
  }

  /**
   * <p>
   * Creates a new {@link javax.faces.component.UIComponent} instance.
   * </p>
   * 
   * @return a new {@link javax.faces.component.UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIViewRoot();
  }

  // ------------------------------------------- Test Methods

  // Test for UIViewRoot.addComponentResource(FacesContext, UIComponent)
  public void uiViewRootaddComponentResourceFCTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    UIComponent myComp = new CustomOutput();

    root.addComponentResource(context, myComp);
    List<UIComponent> children = root.getComponentResources(context, "head");

    if (testValue(out, children, TEST_VALUE)
        && testSize(out, children, 1, "head")) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIViewRoot.addComponentResource(FacesContext, UIComponent, String)
  public void uiViewRootaddComponentResourceFCTTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    UIComponent myComp = new CustomOutput();

    root.addComponentResource(context, myComp, "body");
    List<UIComponent> children = root.getComponentResources(context, "body");

    if (testValue(out, children, TEST_VALUE)
        && testSize(out, children, 1, "body")) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIViewRoot.addComponentResource(FacesContext, UIComponent, null)
  public void uiViewRootaddComponentResourceComponentTargetTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    UIComponent myComp = new CustomOutput();

    boolean tf = true;
    root.addComponentResource(context, myComp, null);
    List<UIComponent> children = root.getComponentResources(context, "head");

    if (!(testValue(out, children, TEST_VALUE)
        && testSize(out, children, 1, "head"))) {
      tf = false;
    }

    // Added target value to teh Component itself.
    myComp.getAttributes().put("target", "body");
    root.addComponentResource(context, myComp, null);
    List<UIComponent> childrenTwo = root.getComponentResources(context, "body");
    if (!(testValue(out, childrenTwo, TEST_VALUE)
        && testSize(out, childrenTwo, 1, "body "))) {
      tf = false;
    }

    if (tf) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Test for UIViewRoot.addComponentResource(FacesContext, UIComponent,
  // String)
  public void uiViewRootaddComponentResourceComponentNegTargetTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    UIComponent myComp = new CustomOutput();

    boolean tf = true;
    myComp.getAttributes().put("target", "head");
    root.addComponentResource(context, myComp, "body");
    List<UIComponent> bodyComp = root.getComponentResources(context, "body");
    List<UIComponent> headComp = root.getComponentResources(context, "head");

    if (!(headComp.isEmpty())) {
      if (!(testValue(out, headComp, "NONE"))) {
        tf = false;
      }
    }
    if (!(testSize(out, bodyComp, 1, "body"))) {
      tf = false;
    }

    if (tf) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // Test for UIViewRoot.getComponentResources(FacesContext, null)
  public void uiViewRootgetComponentResourcesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // UIViewRoot.getComponentResources(FacesContext, null)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "getComponentResources",
        new Class<?>[] { FacesContext.class, String.class },
        new Object[] { getFacesContext(), null }, out);

  }

  // Test for UIViewRoot.removeComponentResource(FacesContext, UIComponent)
  public void uiViewRootremoveComponentResourceTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    UIComponent myComp = new CustomOutput();

    // add the resource & test to make sure its there.
    root.addComponentResource(context, myComp);
    List<UIComponent> children = root.getComponentResources(context, "head");

    if (testValue(out, children, TEST_VALUE)
        && testSize(out, children, 1, "head")) {
      // do nothing the resource is has been added.
    } else {
      return;
    }

    // Remove the resource & test to make sure it is no longer available.
    root.removeComponentResource(context, myComp);
    children = root.getComponentResources(context, "head");
    if (children.isEmpty()) {
      out.println(JSFTestUtil.PASS);
    } else {
      testValue(out, children, "Empty");
    }

  }

  // Test for UIViewRoot.removeComponentResource(FacesContext, UIComponent)
  public void uiViewRootremoveComponentResourceNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // UIViewRoot.removeComponentResource(FacesContext, null)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "removeComponentResource",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { getFacesContext(), null }, out);
  }

  // UIViewRoot.isInView()
  public void uiViewRootIsInViewTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewRoot root = getFacesContext().getViewRoot();
    UIComponent comp = createComponent();

    String ct = comp.getClass().getName();
    root.getChildren().add(comp);

    if (!comp.isInView()) {
      out.println(JSFTestUtil.FAIL + ct + "isInView() returned false. "
          + "Expected UIcomponent to be in the View.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIViewRoot.isInView()
  public void uiViewRootIsInViewNegTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    String ct = comp.getClass().getName();

    if (!comp.isInView()) {
      out.println(JSFTestUtil.FAIL + ct + "isInView() returned false. "
          + "Expected UIcomponent to be in the View.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIViewRoot.addPhaseListeners(PhaseListener)
  // UIViewRoot.getPhaseListeners()
  public void uiViewRootAddGetPhaseListenersTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    List<PhaseListener> result = new ArrayList<PhaseListener>();

    PhaseListener myListener = new TCKPhaseListener();
    root.addPhaseListener(myListener);

    result = root.getPhaseListeners();

    if (!result.contains(myListener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected result from add/getPhaseListners!" + JSFTestUtil.NL
          + "We are missing a customer PhaseListener that should "
          + "have been added by this testcase.");
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiViewRootAddGetPhaseListenersTest

  // UIViewRoot.getViewId()
  // UIViewRoot.setViewId(String)
  public void uiViewRootSetGetViewIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    String myId = "myId";

    root.setViewId(myId);
    String result = root.getViewId();

    if (!myId.equals(result)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected result set/getViewId()!" + JSFTestUtil.NL + "Expected: "
          + myId + JSFTestUtil.NL + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiViewRootSetGetViewIdTest

  public void uiViewRootSubscribeToViewEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    List<SystemEventListener> listeners;
    SystemEventListener tckListener = new TCKSystemEventListener();

    // Added listener to UIViewRoot
    root.subscribeToViewEvent(TCKSystemEvent.class, tckListener);

    // Check to make sure listener is available.
    listeners = root.getViewListenersForEventClass(TCKSystemEvent.class);
    if (!listeners.contains(tckListener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected TCKSystemEventListener to be available!");
      return;
    }

    // Remove listener from UIViewRoot
    root.unsubscribeFromViewEvent(TCKSystemEvent.class, tckListener);

    // Check to make sure listener is NOT available.
    listeners = root.getViewListenersForEventClass(TCKSystemEvent.class);
    if (listeners.contains(tckListener)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected TCKSystemEventListener NOT to be available!");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End uiViewRootSubscribeToViewEventNPETest

  // UIViewRoot.unsubscribeFromViewEvent(Class,SystemEventListener)
  // throws NullPointerException
  public void uiViewRootUnsubscribeFromViewEventNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // UIViewRoot.unsubscribeFromViewEvent(null, SystemEventListener)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "unsubscribeFromViewEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { null, new TCKSystemEventListener() }, out);

    // UIViewRoot.unsubscribeFromViewEvent(Class, null)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "unsubscribeFromViewEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { new TCKSystemEvent(new UICommand()).getClass(), null },
        out);

  }// End uiViewRootUnsubscribeFromViewEventNPETest

  // UIViewRoot.subscribeToViewEvent(Class, SystemEventListener)
  // throws NullPointerException
  public void uiViewRootSubscribeToViewEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // UIViewRoot.subscribeToViewEvent(null, SystemEventListener)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "subscribeToViewEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { null, new TCKSystemEventListener() }, out);

    // UIViewRoot.subscribeToViewEvent(Class, null)
    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "subscribeToViewEvent",
        new Class<?>[] { Class.class, SystemEventListener.class },
        new Object[] { new TCKSystemEvent(new UICommand()).getClass(), null },
        out);

  }// End uiViewRootSubscribeToViewEventNPETest

  // UIViewRoot.createUniquId(FacesContext, String)
  public void uiViewRootCreateUniqueIDTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String tckSeed = "TCK_ID";
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setId(root.createUniqueId(context, tckSeed));
    String result = root.getId();
    String prefix = UIViewRoot.UNIQUE_ID_PREFIX;

    if (!result.contains(tckSeed)) {
      out.println(JSFTestUtil.FAIL + "Unexpected result from .getId()!"
          + JSFTestUtil.NL + "Expected ViewId to contain: " + tckSeed
          + JSFTestUtil.NL + "Received: " + root);

    } else if (!result.contains(prefix)) {
      out.println(JSFTestUtil.FAIL + "Unexpected result from .getId()!"
          + JSFTestUtil.NL + "Expected ViewId to contain: " + prefix
          + JSFTestUtil.NL + "Received: " + root);
    } else {
      out.println(JSFTestUtil.PASS);
    }
  } // End uiViewRootCreateUniqueIDTest

  // UIViewRoot.setLocale(Locale)
  // UIViewRoot.getLocale()
  public void uiViewRootSetGetLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "English";
    getFacesContext().getViewRoot().setLocale(Locale.ENGLISH);
    String result = getFacesContext().getViewRoot().getLocale()
        .getDisplayName();

    if (!golden.equalsIgnoreCase(result)) {
      out.println(JSFTestUtil.FAIL + "Inexpected result from set/getLocale!"
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  } // End uiViewRootSetGetLocaleTest

  // UIViewRoot.setRenderKitId(String)
  // UIViewRoot.getRenderKitId()
  public void uiViewRootSetGetRenderKitIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "tck_kit";
    getFacesContext().getViewRoot().setRenderKitId(golden);
    String result = getFacesContext().getViewRoot().getRenderKitId();

    if (!golden.equals(result)) {
      out.println(JSFTestUtil.FAIL
          + "Inexpected result from set/getRenderKitId!" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }
  } // End uiViewRootSetGetRenderKitIdTest

  // UIViewRoot.getViewListenersForEventClass(Class) throws
  // NullPointerException
  public void uiViewRootGetViewListenersForEventClassNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "getViewListenersForEventClass", new Class<?>[] { Class.class },
        new Object[] { null }, out);

  }// End uiViewRootGetViewListenersForEventClassNPETest

  // UIViewRoot.processApplication(FacesContext) throws NullPointerException
  public void uiViewRootProcessApplicationNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "processApplication", new Class<?>[] { FacesContext.class },
        new Object[] { null }, out);

  }// End uiViewRootProcessApplicationNPETest

  // UIViewRoot.processDecodes(FacesContext) throws NullPointerException
  public void uiViewRootProcessDecodesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "processDecodes", new Class<?>[] { FacesContext.class },
        new Object[] { null }, out);

  }// End uiViewRootProcessDecodesNPETest

  // UIViewRoot.processUpdates(FacesContext) throws NullPointerException
  public void uiViewRootProcessUpdatesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot",
        "processUpdates", new Class<?>[] { FacesContext.class },
        new Object[] { null }, out);

  }// End uiViewRootProcessUpdatesNPETest

  // UIViewRoot.queueEvent(FacesEvent) throws NullPointerException
  public void uiViewRootQueueEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE("javax.faces.component.UIViewRoot", "queueEvent",
        new Class<?>[] { FacesEvent.class }, new Object[] { null }, out);

  }// End uiViewRootQueueEventNPETest

  public void uiViewRootGetSetBeforePhaseListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIViewRoot root = getFacesContext().getViewRoot();
    MethodExpression me = createMethodExpression(request);

    root.setBeforePhaseListener(me);
    String result = root.getBeforePhaseListener().getExpressionString();
    String golden = me.getExpressionString();

    if (!golden.equals(result)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected result returned from getBeforePhaseListener()"
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End uiViewRootGetSetBeforePhaseListenerTest

  public void uiViewRootGetSetAfterPhaseListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIViewRoot root = getFacesContext().getViewRoot();
    MethodExpression me = createMethodExpression(request);

    root.setAfterPhaseListener(me);
    String result = root.getAfterPhaseListener().getExpressionString();
    String golden = me.getExpressionString();

    if (!golden.equals(result)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected result returned from getAfterPhaseListener()"
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End uiViewRootGetSetAfterPhaseListenerTest

  // ------------------------------------------------ private methods

  private MethodExpression createMethodExpression(HttpServletRequest request) {
    request.setAttribute("LP", new AlbumBean());
    ExpressionFactory factory = JspFactory.getDefaultFactory()
        .getJspApplicationContext(servletContext).getExpressionFactory();
    MethodExpression me = factory.createMethodExpression(
        getFacesContext().getELContext(), "#{LP.album}", java.lang.String.class,
        new Class[] {});

    return me;
  }

  private boolean testValue(PrintWriter out, List<UIComponent> components,
      String expValue) {
    // Validate the Value of the Component.
    Iterator<?> childIterator = components.listIterator();
    while (childIterator.hasNext()) {
      UIOutput uio = (UIOutput) childIterator.next();
      String resValue = (String) uio.getValue();

      if (!(expValue.equals(resValue))) {
        out.println("Test FAILED Incorrect value for " + "value attribute."
            + JSFTestUtil.NL + "Expected: " + expValue + JSFTestUtil.NL
            + "Received: " + resValue + JSFTestUtil.NL);
        return false;
      }

    }

    return true;
  }

  private boolean testSize(PrintWriter out, List<UIComponent> components,
      int expSize, String target) {
    int resultSize = components.size();

    if (components.isEmpty()) {
      out.println("Test FAILED no Resources rendered in the " + target
          + " of the rendered output.");
      return false;
    }

    if (expSize != resultSize) {
      out.println("Test FAILED wrong number of Resource objects"
          + JSFTestUtil.NL + "Expected: " + expSize + JSFTestUtil.NL
          + "Received: " + resultSize + JSFTestUtil.NL);
      return false;
    }

    return true;
  }
}
