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

package com.sun.ts.tests.jsf.api.javax_faces.event.methodexpressionvaluechangelistener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  public void mevChangeListenerCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Boolean pf = true;
    ArrayList<MethodExpressionValueChangeListener> meValueListener = new ArrayList<MethodExpressionValueChangeListener>();

    request.setAttribute("bean", new SimpleBean());

    ExpressionFactory factory = getFacesContext().getApplication()
        .getExpressionFactory();

    try {
      MethodExpression me = factory.createMethodExpression(
          getFacesContext().getELContext(), "#{bean.action}",
          java.lang.String.class, new Class[] {});

      MethodExpression metwo = factory.createMethodExpression(
          getFacesContext().getELContext(), "#{bean.action}",
          java.lang.String.class, new Class[] {});

      meValueListener.add(new MethodExpressionValueChangeListener());
      meValueListener.add(new MethodExpressionValueChangeListener(me));
      meValueListener.add(new MethodExpressionValueChangeListener(me, metwo));

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL);
      e.printStackTrace();
    }

    Iterator<MethodExpressionValueChangeListener> i = meValueListener
        .iterator();

    while (i.hasNext()) {
      if (null == i.next()) {
        pf = false;
      }
    }

    if (!pf) {
      out.println(JSFTestUtil.FAIL);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End MEVChangeListenerCtorTest

  public void mevChangeListenerProcessValueChgNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(MethodExpressionValueChangeListener.class,
        "processValueChange", new Class[] { ValueChangeEvent.class },
        new Object[] { null }, out);

  } // End mevChangeListenerProcessValueChgTest

  // ------------------------------------------------- Private Classes

  public class SimpleBean {

    public String action(String s) {
      String res = "defaultValue";

      if (!(s == null)) {
        res = s;
      }

      return res;
    }

  } // END SimpleBean
}
