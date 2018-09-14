/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common.BaseBehaviorTestServlet;
import com.sun.ts.tests.jsf.common.beans.AlbumBean;
import com.sun.ts.tests.jsf.common.listener.TCKBehaviorListener;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseBehaviorTestServlet {

  /**
   * <p>
   * Creates a new {@link javax.faces.component.UIComponent} instance.
   * </p>
   * 
   * @return a new {@link javax.faces.component.UIComponent} instance.
   */
  @Override
  protected ClientBehaviorBase createBehavior() {
    return new AjaxBehavior();
  }

  // ---------------------------- test methods ------------------------

  public void ajaxBehaviorBroadcastTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    // Ensure listeners are invoked in the proper order
    // and are invoked during the invoke application phase
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();

    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();
    UIComponent component = new UICommand();

    AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, behavior);

    TCKBehaviorListener.trace(null);

    // Register three listeners
    behavior.addAjaxBehaviorListener(TCKBehaviorListener.withID("AP0"));
    behavior.addAjaxBehaviorListener(TCKBehaviorListener.withID("AP1"));
    behavior.addAjaxBehaviorListener(TCKBehaviorListener.withID("AP2"));

    UIViewRoot root = new UIViewRoot();
    root.getChildren().add(component);
    component.queueEvent(event);
    root.processDecodes(context);
    root.processValidators(context);
    root.processApplication(context);

    String traceExpected = "/AP0@ANY_PHASE/AP1@ANY_PHASE/AP2@ANY_PHASE";
    String traceReceived = TCKBehaviorListener.trace();
    if (!traceExpected.equals(traceReceived)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Listeners not invoked in the expected "
          + "order or were invoked too many times." + JSFTestUtil.NL
          + "Listener trace expected: " + traceExpected + JSFTestUtil.NL
          + "Listener trace received: " + traceReceived);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }// End ajaxBehaviorBroadcastTest

  public void ajaxBehaviorAddListenerNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(this.createBehavior().getClass(),
        "addAjaxBehaviorListener",
        new Class<?>[] { AjaxBehaviorListener.class }, new Object[] { null },
        out);

  }// End ajaxBehaviorAddListenerNPETest

  public void ajaxBehaviorGetSetDelayTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();

    behavior.setDelay("1000");

    if (!"1000".equals(behavior.getDelay())) {
      out.println(JSFTestUtil.FAIL + " Unexpected value for Delay!"
          + JSFTestUtil.NL + "Expected: 1000" + JSFTestUtil.NL + "Received: "
          + behavior.getDelay());
    } else {
      behavior.setDelay("none");
      if (!"none".equals(behavior.getDelay())) {
        out.println(JSFTestUtil.FAIL + " Unexpected value for Delay!"
            + JSFTestUtil.NL + "Expected: none" + JSFTestUtil.NL + "Received: "
            + behavior.getDelay());
      } else {
        out.println(JSFTestUtil.PASS);
      }
    }

  }// End ajaxBehaviorGetSetDelayTest

  public void ajaxBehaviorGetSetExecuteTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();

    List<String> compIds = new ArrayList<String>();
    compIds.add("ID0");
    compIds.add("ID1");
    compIds.add("ID2");

    behavior.setExecute(compIds);

    Iterator<String> it = compIds.iterator();
    Collection<String> ids = behavior.getExecute();

    String received = "";
    boolean pf = true;

    while (it.hasNext()) {
      if (!ids.contains(it.next())) {
        pf = false;
      }
    }

    if (pf) {
      out.println(JSFTestUtil.PASS);

    } else {
      for (String result : ids) {
        received = received + ", " + result;
      }
      out.println(
          JSFTestUtil.FAIL + "Unexpected result from setExecute or getExecute!"
              + JSFTestUtil.NL + "Expected: ID0, ID1, ID2" + JSFTestUtil.NL
              + "Received: " + received);
    }

  }// End ajaxBehaviorGetSetExecuteTest

  public void ajaxBehaviorSetIsDisabledTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();

    behavior.setDisabled(true);
    boolean result = behavior.isDisabled();

    if (result) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + " Unexpected value for 'Disabled'!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + result);
    }

  }// End ajaxBehaviorSetIsDisabledTest

  public void ajaxBehaviorSetIsImmediateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();

    behavior.setImmediate(true);
    boolean result = behavior.isImmediate();

    if (result) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + " Unexpected value for 'Immediate'!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + result);
    }

  }// End ajaxBehaviorSetIsImmediateTest

  public void ajaxBehaviorGetSetValueExpressionNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // .getValue(null)
    JSFTestUtil.checkForNPE(createBehavior().getClass(), "getValueExpression",
        new Class<?>[] { String.class }, new Object[] { null }, out);

    // .setValue(null, ValueExpression)
    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);

    ValueExpression literalExpr = factory.createValueExpression(
        getFacesContext().getELContext(), "literalValue",
        java.lang.String.class);

    JSFTestUtil.checkForNPE(createBehavior().getClass(), "setValueExpression",
        new Class<?>[] { String.class, ValueExpression.class },
        new Object[] { null, literalExpr }, out);

  }// End ajaxBehaviorGetSetValueExpressionNPETest

  public void ajaxBehaviorAddRemoveBehaviorListenerNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // .addBehaviorListener()
    JSFTestUtil.checkForNPE(createBehavior().getClass(),
        "addAjaxBehaviorListener",
        new Class<?>[] { AjaxBehaviorListener.class }, new Object[] { null },
        out);

    // .removeBehaviorListener()
    JSFTestUtil.checkForNPE(createBehavior().getClass(),
        "removeAjaxBehaviorListener",
        new Class<?>[] { AjaxBehaviorListener.class }, new Object[] { null },
        out);

  }// End ajaxBehaviorAddRemoveBehaviorListenerNPETest

  public void ajaxBehaviorGetSetOnerrorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();
    String golden = "error.js";

    behavior.setOnerror(golden);
    String result = behavior.getOnerror();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value returned form getOnerror()" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);
    }

  }// End ajaxBehaviorGetSetOnerrorTest

  public void ajaxBehaviorGetSetOnventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();
    String golden = "TCKSystemEvent";

    behavior.setOnevent(golden);
    String result = behavior.getOnevent();

    if (golden.equals(result)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value returned form getOnevent()" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);
    }

  }// End ajaxBehaviorGetSetOnventTest

  public void ajaxBehaviorGetSetRenderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();
    String golden = "TCKComp";

    Collection<String> rend = new ArrayList<String>();
    rend.add(golden);

    behavior.setRender(rend);
    Collection<String> result = behavior.getRender();

    if (result.contains(golden)) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value returned form getRender()" + JSFTestUtil.NL
          + "Expected: " + golden + JSFTestUtil.NL + "Received: " + result);
    }

  }// End ajaxBehaviorGetSetRenderTest

  public void ajaxBehaviorGetSetValueExpressionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();
    request.setAttribute("lp", new AlbumBean());

    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{lp.album}",
        java.lang.String.class);

    behavior.setValueExpression("bean", expression);

    if (behavior.getValueExpression("bean") != expression) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected getValueExpression() to return "
          + "the non-literal ValueExpression set via " + "setValueExpression()."
          + JSFTestUtil.NL + "Expected: " + expression + JSFTestUtil.NL
          + "Received: " + behavior.getValueExpression("bean"));
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End ajaxBehaviorGetSetValueExpressionTest

  public void ajaxBehaviorIsSetResetValuesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    AjaxBehavior behavior = (AjaxBehavior) this.createBehavior();

    behavior.setResetValues(false);

    if (behavior.isResetValues()) {
      out.println(JSFTestUtil.FAIL + " Unexpected value for isResetValues!"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + behavior.isResetValues());
      return;

    }

    behavior.setResetValues(true);
    if (!behavior.isResetValues()) {
      out.println(JSFTestUtil.FAIL + " Unexpected value for setResetValues!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + behavior.isResetValues());
      return;
    }

    if (!behavior.isResetValuesSet()) {
      out.println(JSFTestUtil.FAIL + " Unexpected value for isResetValuesSet!"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + behavior.isResetValuesSet());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }// End ajaxBehaviorIsSetResetValuesTest

}// End TestServlet
