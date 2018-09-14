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
package com.sun.ts.tests.jsf.api.javax_faces.component.annotation;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends BaseComponentTestServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType("javax.faces.Text");
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  protected UIComponentBase createComponent() {
    return new UIOutput();
  }

  // --------------------------------------------------------- Test Methods

  // Validate ResourceDependecy ResourceType.
  public void uiOutputRDTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();

    Application application = getApplication();
    application.addComponent("myOut",
        "com.sun.ts.tests.jsf.api.javax_faces.component.annotation."
            + "CustomOutput");
    application.createComponent("myOut");

    List children = root.getComponentResources(context, "body");

    // Make sure two resources are available.
    int expectedSize = 2;
    int resultSize = children.size();
    if (expectedSize != resultSize) {
      out.println("Test FAILED wrong number of Resource objects"
          + JSFTestUtil.NL + "Expected: " + expectedSize + JSFTestUtil.NL
          + "Received: " + resultSize + JSFTestUtil.NL);
      return;
    }

    // Test to make sure the Class type is UIOutput.
    Iterator childIterator = children.listIterator();
    while (childIterator.hasNext()) {
      String resClass = childIterator.next().getClass().getName();
      String expClass = "javax.faces.component.UIOutput";
      if (!(expClass.equals(resClass))) {
        out.println("Test FAILED Incorrect Class Instance for "
            + "Resource reference." + JSFTestUtil.NL + "Expected: " + expClass
            + JSFTestUtil.NL + "Received: " + resClass + JSFTestUtil.NL);
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // Validate Library & Target Attributes
  public void uiOutputRDAttributeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();

    Application application = getApplication();
    application.addComponent("myOut",
        "com.sun.ts.tests.jsf.api.javax_faces.component.annotation."
            + "CustomOutput");
    application.createComponent("myOut");

    List children = root.getComponentResources(context, "body");

    UIOutput compOne = (UIOutput) children.get(0);
    UIOutput compTwo = (UIOutput) children.get(1);

    TestValue one = new TestValue("hello.js", "test", "body");
    TestValue two = new TestValue("black-n-blue.css", "test", "body");

    this.checkAttributes(compOne, one, out);
    this.checkAttributes(compTwo, two, out);

  }

  // ---------------------------------------------------- private methods
  private void checkAttributes(UIComponent component, TestValue testvalues,
      PrintWriter out) {

    Map<String, Object> atts = component.getAttributes();

    String resName = (String) atts.get("name");
    String resLib = (String) atts.get("library");
    String resTarget = (String) atts.get("target");

    String expName = testvalues.getExpName();
    String expLib = testvalues.getExpLib();
    String expTarget = testvalues.getExpTarget();

    // name
    if (!(expName.equals(resName))) {
      out.println("Test FAILED Unexpected value for 'name' " + "Attribute."
          + JSFTestUtil.NL + "Expected: " + expName + JSFTestUtil.NL
          + "Received: " + resName + JSFTestUtil.NL);
      return;
    }
    // library
    if (!(expLib.equals(resLib))) {
      out.println("Test FAILED Unexpected value for 'library' " + "Attribute."
          + JSFTestUtil.NL + "Expected: " + expLib + JSFTestUtil.NL
          + "Received: " + resLib + JSFTestUtil.NL);
      return;
    }
    // target
    if (!(expTarget.equals(resTarget))) {
      out.println("Test FAILED Unexpected value for 'target' " + "Attribute."
          + JSFTestUtil.NL + "Expected: " + expTarget + JSFTestUtil.NL
          + "Received: " + resTarget + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------ private classes
  private static class TestValue {

    private String expName;

    private String expLib;

    private String expTarget;

    public TestValue(String name, String library, String target) {
      this.expName = name;
      this.expLib = library;
      this.expTarget = target;
    }

    /**
     * @return the expName
     */
    public String getExpName() {
      return expName;
    }

    /**
     * @param expName
     *          the expName to set
     */
    public void setExpName(String expName) {
      this.expName = expName;
    }

    /**
     * @return the expLib
     */
    public String getExpLib() {
      return expLib;
    }

    /**
     * @param expLib
     *          the expLib to set
     */
    public void setExpLib(String expLib) {
      this.expLib = expLib;
    }

    /**
     * @return the expTarget
     */
    public String getExpTarget() {
      return expTarget;
    }

    /**
     * @param expTarget
     *          the expTarget to set
     */
    public void setExpTarget(String expTarget) {
      this.expTarget = expTarget;
    }
  }
}
