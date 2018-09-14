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
package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import java.util.HashSet;
import javax.faces.application.Application;
import javax.faces.component.NamingContainer;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.SystemEvent;

/**
 * <p>
 * This is the base test Servlet for classes that implement
 * {@link javax.faces.component.UIComponent}. Any test that will be performing
 * inheritance testing on a component that implements UIComponent, will need to
 * extend this servlet.
 * </p>
 */
public abstract class BaseUIComponentTestServlet
    extends BasePartialStateHolderTestServlet {

  private Object foundComponent;

  // -------------------------------------------------------------- Public
  // Methods
  /**
   * <p>
   * Initialize this <code>Servlet</code>.
   * </p>
   * 
   * @param config
   *          this <code>Servlet</code>'s configuration
   * @throws ServletException
   *           if initialization fails
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ----------------------------------------------------------- Protected
  // Methods
  /**
   * <p>
   * The expected renderer type for the component under test.
   * </p>
   */
  private String rendererType;

  protected String getRendererType() {
    return rendererType;
  }

  protected void setRendererType(String rendererType) {
    this.rendererType = rendererType;
  }

  private Boolean rendersChildren;

  protected Boolean getRendersChildren() {
    return rendersChildren;
  }

  protected void setRendersChildren(Boolean rendersChildren) {
    this.rendersChildren = rendersChildren;
  }

  private String[] attributeNames;

  protected String[] getAttributeNames() {
    return attributeNames;
  }

  protected void setAttributeNames(String[] attributeNames) {
    this.attributeNames = attributeNames;
  }

  // ------------------------------------------------------------- Private
  // Methods
  /**
   * Comments to come later.
   */
  private Map<String, UIComponent> setupInvokeOnComponentTree() {
    UIViewRoot root = new UIViewRoot();
    UIForm form1 = new UIForm();
    UIPanel panel1 = new UIPanel();
    UIInput input1 = new UIInput();
    UIInput input2 = new UIInput();
    UIForm form2 = new UIForm();
    UIPanel panel2 = new UIPanel();
    UIInput input3 = new UIInput();
    UIInput input4 = new UIInput();

    root.setId("root");
    form1.setId("form1");
    panel1.setId("panel1");
    input1.setId("input1");
    input2.setId("input2");

    form2.setId("form2");
    panel2.setId("panel2");
    input3.setId("input1");
    input4.setId("input2");

    root.getChildren().add(form1);
    form1.getChildren().add(panel1);
    panel1.getChildren().add(input1);
    panel1.getChildren().add(input2);

    root.getChildren().add(form2);
    form2.getChildren().add(panel2);
    panel2.getChildren().add(input3);
    panel2.getChildren().add(input4);

    Map<String, UIComponent> result = new HashMap<String, UIComponent>();
    result.put("root", root);
    result.put("form1", form1);
    result.put("panel1", panel1);
    result.put("input1", input1);
    result.put("input2", input2);
    result.put("form2", form2);
    result.put("panel2", panel2);
    result.put("input3", input3);
    result.put("input4", input4);

    return result;
  }

  /**
   * <p>
   * Validate the Map methods designated to be implemented by
   * {@link UIComponent#getAttributes()} function as expected.
   * </p>
   * 
   * <p>
   * The Map returned <strong>MUST</strong> implement all required and optional
   * methods of the {@link Map} interface.
   * </p>
   * 
   * <ul>
   * <li>The <code>Map</code> implementation must implement the
   * <code>java.io.Serializable</code> interface.</li>
   * <li>Any attempt to add a <code>null</code> key or value must throw a
   * <code>NullPointerException</code>.</li>
   * <li>Any attempt to add a key that is not a String must throw a
   * <code>ClassCastException</code>.</li>
   * <li>If the attribute name specified as a key matches a property of this
   * {@link UIComponent}'s implementation class, the following methods will have
   * special behavior:
   * <ul>
   * <li><code>containsKey</code> - Return <code>false</code>.</li>
   * <li><code>get()</code> - If the property is readable, call the getter
   * method and return the returned value (wrapping primitive values in their
   * corresponding wrapper classes); otherwise throw
   * <code>IllegalArgumentException</code>.</li>
   * <li><code>put()</code> - If the property is writeable, call the setter
   * method to set the corresponding value (unwrapping primitive values in their
   * corresponding wrapper classes). If the property is not writeable, or an
   * attempt is made to set a property of primitive type to <code>null</code>,
   * throw <code>IllegalArgumentException</code>.</li>
   * <li><code>remove</code> - Throw <code>IllegalArgumentException</code>.</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param component
   *          the {@link UIComponent} that provides the {@link Map} under test.
   * @return a null if validation succeeds, otherwise an error.
   */
  private String validateAttributeMap(UIComponent component) {

    Map map = component.getAttributes();

    // Validate special requirements
    if (!(map instanceof Serializable)) {
      return "The Map does not implement java.io.Serializable";
    }

    try {
      map.put(null, "value");
      return "No Exception thrown if a null Key was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        return "Exception thrown when a null Key was added to the Map,"
            + " but it wasn't an instance of NullPointerException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      map.put("Key", null);
      return "No Exception thrown if a null Value was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        return "Exception thrown when a null Value was added to the Map,"
            + " but it wasn't an instance of NullPointerException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      map.put(new Object(), "value");
      return "No Exception thrown if a non-String Key was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof ClassCastException)) {
        return "Exception thrown when a null Value was added to the Map,"
            + " but it wasn't an instance of ClassCastException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    map.put("id", "compId");

    if (map.containsKey("id")) {
      return "Map.containsKey() failed to return false when the key "
          + "requested was a known property of the UIComponent.";
    }

    component.setId("myCompIdOrig");

    if (!"myCompIdOrig".equals(map.get("id"))) {
      return "Map.get() failed to return the ID of the component when the"
          + " specified key was 'id'.";
    }

    map.put("id", "newCompId");

    if (!"newCompId".equals(component.getId())) {
      return ("Map.put() using a Key that is a known property of the "
          + "component under test failed to set the corresponding "
          + "property of the component.");
    }

    try {
      map.remove("id");
      return "No Exception thrown when calling removed where the Key"
          + " corresponds to a properly of the UIComponent under test.";
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        return "An Exception was thrown when Map.remove() was called"
            + " where the Key corresponded to a property of the"
            + " UIComponent under test, but it was not an instance"
            + " of IllegalArgumentException" + JSFTestUtil.NL
            + "Exception received: " + e.getClass().getName();
      }
    }

    // validate methods from java.util.Map
    map.put("key", "value");

    if (!"value".equals(map.get("key"))) {
      return "Unabled to get the value for key 'key' after adding"
          + " the key and value via the 'put' operation.";
    }

    if (!(map.size() >= 1)) {
      return "Map.size() returned unexpected value.  Expected the map "
          + "to contain at least one element.  Size returned: " + map.size();
    }

    map.clear();

    if (map.size() != 0) {
      return "Map.clear() failed to clear the Map under test of all "
          + "entries";
    }

    if (map.containsKey("key")) {
      return "Map.containsKey() returned true when no such key exists";
    }

    map.put("key", "value");

    if (!map.containsKey("key")) {
      return "Map.containsKey() returned false when a known key exists"
          + " in the Map.";
    }

    map.remove("key");

    if (map.size() != 0) {
      return "Map.remove() failed to clear the specified entry from"
          + " the Map.";
    }

    if (map.containsValue("value")) {
      return "Map.containsValue() returned true when no such value " + "exists";
    }

    map.put("key", "value");

    if (!map.containsValue("value")) {
      return "Map.containsValue() returned false when a known value "
          + "exists in the Map.";
    }

    Set entrySet = map.entrySet();
    if (entrySet.size() == 1) {
      for (Iterator i = entrySet.iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        if (!"key".equals(entry.getKey())) {
          return "Map.Entry returned from Map.entrySet() contains an"
              + " invalid Key.";
        }
        if (!"value".equals(entry.getValue())) {
          return "Map.Entry returned from Map.entrySet() contains an"
              + " invalid Value.";
        }
      }
    } else {
      return "Expected the Set returned by Map.entrySet() to contain"
          + " one entry.  Number of Entries found: " + entrySet.size();
    }

    if (!map.equals(map)) {
      return "Map.equals() returned false when the same map was provided"
          + " as the argument.";
    }

    if (map.equals(new HashMap())) {
      return "Map.equals() returned true when this Map was compared to a "
          + "new HashMap instance.";
    }

    if (map.isEmpty()) {
      return "Map.isEmpty() returned true when a known entry is "
          + "known to exist.";
    }

    map.clear();

    if (!map.isEmpty()) {
      return "Map.isEmpty() returned false when all entries were cleared"
          + " prior to calling isEmpty().";
    }

    map.put("key", "value");

    Set keySet = map.keySet();

    if (keySet.size() == 1) {
      for (Iterator i = keySet.iterator(); i.hasNext();) {
        String key = (String) i.next();
        if (!"key".equals(key)) {
          return "The key found in the Set returned by Map.keySet() "
              + "was not the key expected to be found.";
        }
      }
    } else {
      return "Map.keySet() returned a set with more than 1 entry.";
    }

    map.clear();

    HashMap hashMap = new HashMap();
    hashMap.put("key", "value");

    map.putAll(hashMap);

    if (map.size() != 1 && !map.containsKey("key")
        && !map.containsValue("value")) {
      return "Map.putAll() failed to yeild the expected results."
          + JSFTestUtil.NL + "Size: " + map.size() + JSFTestUtil.NL
          + "Contains 'key': " + map.containsKey("key") + JSFTestUtil.NL
          + "Contains 'value': " + map.containsValue("value");
    }

    Collection values = map.values();
    if (values.size() == 1) {
      for (Iterator i = values.iterator(); i.hasNext();) {
        String value = (String) i.next();
        if (!"value".equals(value)) {
          return "The Collection returned by Map.values() didn't contain"
              + " the expected values.";
        }
      }
    } else {
      return "Map.values() returned a Collection with more than 1 entry.";
    }

    return null;
  }

  /**
   * The returned implementation must support all of the standard and optional
   * <code>Map</code> methods, plus support the following additional
   * requirements:
   * </p>
   * <ul>
   * <li>The <code>Map</code> implementation must implement the
   * <code>java.io.Serializable</code> interface.</li>
   * <li>Any attempt to add a <code>null</code> key or value must throw a
   * NullPointerException.</li>
   * <li>Any attempt to add a key that is not a String must throw a
   * ClassCastException.</li>
   * <li>Any attempt to add a value that is not a {@link UIComponent} must throw
   * a ClassCastException.</li>
   * <li>Whenever a new facet {@link UIComponent} is added:
   * <ul>
   * <li>The <code>parent</code> property of the component must be set to this
   * component instance.</li>
   * </ul>
   * </li>
   * <li>Whenever an existing facet {@link UIComponent} is removed:
   * <ul>
   * <li>The <code>parent</code> property of the facet must be set to
   * <code>null</code>.</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param component
   *          the {@link UIComponent} that provides the {@link Map} under test.
   * @return a null if validation succeeds, otherwise an error.
   */
  private String validateFacetMap(UIComponent component) {

    Map map = component.getFacets();

    // Validate special requirements
    if (!(map instanceof Serializable)) {
      return "The Map does not implement java.io.Serializable";
    }

    try {
      map.put(null, new UIOutput());
      return "No Exception thrown if a null Key was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        return "Exception thrown when a null Key was added to the Map,"
            + " but it wasn't an instance of NullPointerException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      map.put("Key", null);
      return "No Exception thrown if a null Value was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        return "Exception thrown when a null Value was added to the Map,"
            + " but it wasn't an instance of NullPointerException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      map.put(new Object(), new UIOutput());
      return "No Exception thrown if a non-String Key was added to the Map.";
    } catch (Exception e) {
      if (!(e instanceof ClassCastException)) {
        return "Exception thrown when a null Value was added to the Map,"
            + " but it wasn't an instance of ClassCastException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      map.put("key", "value");
      return "No Exception thrown if a non-UIComponent value was added "
          + "to the Map.";
    } catch (Exception e) {
      if (!(e instanceof ClassCastException)) {
        return "Exception thrown when a non-UIComponent value was "
            + "added to the Map, but it wasn't an instance of "
            + "ClassCastException." + JSFTestUtil.NL + "Exception Received: "
            + e.getClass().getName();
      }
    }

    UIComponent comp = new UIOutput();
    map.put("facet1", comp);

    if (!component.equals(comp.getParent())) {
      return "The parent property of the Facet added "
          + "to the component under test did was not set, or was "
          + "not the expected value." + JSFTestUtil.NL + "Expected: "
          + component + JSFTestUtil.NL + "Received: " + comp.getParent();
    }

    map.remove("facet1");
    if (comp.getParent() != null) {
      return "The parent propery of the Facet just removed from the "
          + "parent component was not null.";
    }

    comp = new UIOutput();

    // validate methods from java.util.Map
    map.put("key", comp);

    if (!comp.equals(map.get("key"))) {
      return "Unabled to get the value for key 'key' after adding"
          + " the key and value via the 'put' operation.";
    }

    if (!(map.size() >= 1)) {
      return "Map.size() returned unexpected value.  Expected the map "
          + "to contain at least one element.  Size returned: " + map.size();
    }

    map.clear();

    if (map.size() != 0) {
      return "Map.clear() failed to clear the Map under test of all "
          + "entries";
    }

    if (map.containsKey("key")) {
      return "Map.containsKey() returned true when no such key exists";
    }

    map.put("key", comp);

    if (!map.containsKey("key")) {
      return "Map.containsKey() returned false when a known key exists"
          + " in the Map.";
    }

    map.remove("key");

    if (map.size() != 0) {
      return "Map.remove() failed to clear the specified entry from"
          + " the Map.";
    }

    if (map.containsValue(comp)) {
      return "Map.containsValue() returned true when no such value " + "exists";
    }

    map.put("key", comp);

    if (!map.containsValue(comp)) {
      return "Map.containsValue() returned false when a known value "
          + "exists in the Map.";
    }

    Set entrySet = map.entrySet();
    if (entrySet.size() == 1) {
      for (Iterator i = entrySet.iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        if (!"key".equals(entry.getKey())) {
          return "Map.Entry returned from Map.entrySet() contains "
              + "an invalid Key.";
        }
        if (!comp.equals(entry.getValue())) {
          return "Map.Entry returned from Map.entrySet() contains "
              + "an invalid Value.";
        }
      }
    } else {
      return "Expected the Set returned by Map.entrySet() to contain"
          + " one entry.  Number of Entries found: " + entrySet.size();
    }

    if (!map.equals(map)) {
      return "Map.equals() returned false when the same map was "
          + "provided as the argument.";
    }

    if (map.equals(new HashMap())) {
      return "Map.equals() returned true when this Map was compared to "
          + "a new HashMap instance.";
    }

    if (map.isEmpty()) {
      return "Map.isEmpty() returned true when a known entry is "
          + "known to exist.";
    }

    map.clear();

    if (!map.isEmpty()) {
      return "Map.isEmpty() returned false when all entries were cleared"
          + " prior to calling isEmpty().";
    }

    map.put("key", comp);

    Set keySet = map.keySet();

    if (keySet.size() == 1) {
      for (Iterator i = keySet.iterator(); i.hasNext();) {
        String key = (String) i.next();
        if (!"key".equals(key)) {
          return "The key found in the Set returned by Map.keySet() "
              + "was not the key expected to be found.";
        }
      }
    } else {
      return "Map.keySet() returned a set with more than 1 entry.";
    }

    map.clear();

    HashMap hashMap = new HashMap();
    hashMap.put("key", comp);

    map.putAll(hashMap);

    if (map.size() != 1 && !map.containsKey("key")
        && !map.containsValue(comp)) {
      return "Map.putAll() failed to yeild the expected results."
          + JSFTestUtil.NL + "Size: " + map.size() + JSFTestUtil.NL
          + "Contains 'key': " + map.containsKey("key") + JSFTestUtil.NL
          + "Contains 'value': " + map.containsValue("value");
    }

    Collection values = map.values();
    if (values.size() == 1) {
      for (Iterator i = values.iterator(); i.hasNext();) {
        Object value = i.next();
        if (!comp.equals(value)) {
          return "The Collection returned by Map.values() didn't "
              + "contain the expected values.";
        }
      }
    } else {
      return "Map.values() returned a Collection with more than 1 entry.";
    }

    return null;
  }

  /**
   * The returned implementation must support all of the standard and optional
   * <code>List</code> methods, plus support the following additional
   * requirements:
   * </p>
   * <ul>
   * <li>The <code>List</code> implementation must implement the
   * <code>java.io.Serializable</code> interface.</li>
   * <li>Any attempt to add a <code>null</code> must throw a
   * NullPointerException</li>
   * <li>Any attempt to add an object that does not implement
   * {@link UIComponent} must throw a ClassCastException.</li>
   * 
   * <li>Any attempt to add a child {@link UIComponent} with a non-null
   * <code>componentId</code> that contains invalid characters, or begins with
   * {@link NamingContainer#SEPARATOR_CHAR}, or
   * {@link UIViewRoot#UNIQUE_ID_PREFIX} (i.e. other than letters, digits, '-',
   * or '_') must throw IllegalArgumentException.</li>
   * 
   * <li>Whenever a new child component is added:
   * <ul>
   * <li>The <code>parent</code> property of the child must be set to this
   * component instance.</li>
   * </ul>
   * </li>
   * <li>Whenever an existing child component is removed:
   * <ul>
   * <li>The <code>parent</code> property of the child must be set to
   * <code>null</code>.</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param component
   *          the {@link UIComponent} that will yield the {@link List} under
   *          test
   * @return null if all validations succeed, or a String containing an error
   *         message.
   */
  private String validateListMethods(UIComponent component) {
    StringBuffer sb = new StringBuffer(25);

    List list = component.getChildren();
    // Begin section for validating List methods
    UIComponent c0 = new UIInput();
    UIComponent c1 = new UIInput();
    UIComponent c2 = new UIInput();
    UIComponent c3 = new UIInput();
    c0.setId("index0");
    c1.setId("index1");
    c2.setId("index2");
    c3.setId("index3");

    list.clear();

    if (!list.isEmpty()) {
      sb.append("List.clear() failed to clear all of the elements from");
      sb.append("the List.");
      return sb.toString();
    }

    list.add(0, c0);

    if (list.isEmpty()) {
      sb.append("List.isEmpty() returned true after an element was added");
      sb.append("to the List");
      return sb.toString();
    }

    if (!"index0".equals(((UIComponent) list.get(0)).getId())) {
      sb.append(JSFTestUtil.FAIL + " Unabled to obtain Object from index 0");
      sb.append(" that was explicitly set to that index by add");
      sb.append("(int, Object)" + JSFTestUtil.NL);
      return sb.toString();
    }

    list.add(c1);

    if (!"index1".equals(((UIComponent) list.get(1)).getId())) {
      sb.append(JSFTestUtil.FAIL + " Unabled to obtain Object from index 1");
      sb.append(" that was added via add(Object)" + JSFTestUtil.NL);
      return sb.toString();
    }

    Collection c = new ArrayList();
    c.add(c2);
    c.add(c3);

    list.addAll(c);

    if (list.size() != 4) {
      sb.append("Called addAll(Collection) with a Set containing 2 ");
      sb.append("elements.  The resulting list should have had a size");
      sb.append(" of four." + JSFTestUtil.NL).append("Actual size: ")
          .append(list.size());
      return sb.toString();
    }

    if (!"index2".equals(((UIComponent) list.get(2)).getId())
        && !"index3".equals(((UIComponent) list.get(3)).getId())) {
      sb.append("Expected the values stored at index 2 and 3 to be");
      sb.append(
          " 'index2' and 'index3'" + JSFTestUtil.NL + "Value at index 2: ");
      sb.append(((UIComponent) list.get(2)).getId()).append('\n');
      sb.append("Value at index 3: ")
          .append(((UIComponent) list.get(3)).getId()).append('\n');
      return sb.toString();
    }

    list.addAll(0, c);

    if (list.size() != 4) {
      sb.append("Called addAll(int, Collection) with a Set containing 2 ");
      sb.append("elements to replace elements 0 and 1.  The resulting ");
      sb.append("list should have had a size");
      sb.append(" of four." + JSFTestUtil.NL).append("Actual size: ")
          .append(list.size());
      return sb.toString();
    }

    if (!"index2".equals(((UIComponent) list.get(0)).getId())
        && !"index3".equals(((UIComponent) list.get(1)).getId())) {
      sb.append("Expected the values stored at index 0 and 1 to be");
      sb.append(
          " 'index2' and 'index3'" + JSFTestUtil.NL + "Value at index 0: ");
      sb.append(((UIComponent) list.get(0)).getId()).append('\n');
      sb.append("Value at index 1: ")
          .append(((UIComponent) list.get(1)).getId()).append('\n');
      return sb.toString();
    }

    list.clear();

    if (!list.isEmpty()) {
      sb.append("List.clear() failed to clear all of the elements from");
      sb.append("the List.");
      return sb.toString();
    }

    list.add(c0);
    list.add(c1);

    if (!list.contains(c0)) {
      sb.append("List.contains(Object) returned false for an object");
      sb.append(" known to exist in the List.");
      return sb.toString();
    }

    if (list.contains(c3)) {
      sb.append("List.contains(Object) returned true for an object");
      sb.append(" known not to exist in the List.");
      return sb.toString();
    }

    if (!list.containsAll(list)) {
      sb.append("List.containsAll(Collection) failed to return true");
      sb.append(" when a Collection containing the same elements was passed.");
      return sb.toString();
    }

    c = new ArrayList();
    c.add(new UICommand());
    c.add(new UIOutput());

    if (list.containsAll(c)) {
      sb.append("List.containsAll(Collection) returned true when passed");
      sb.append(" a Collection with non matching elements");
      return sb.toString();
    }

    if (!list.equals(list)) {
      sb.append("List.equals() returned false when providing itself as");
      sb.append(" an argument.");
      return sb.toString();
    }

    if (list.equals(new ArrayList())) {
      sb.append("List.equals() returned true when providing a new");
      sb.append(" ArrayList with no elements.");
      return sb.toString();
    }

    list.hashCode();
    list.add(c1);

    if (list.indexOf(c1) != 1) {
      sb.append("List.indexOf(Object) failed to return the expected index");
      sb.append(" of '1'.  Index returned: ").append(list.indexOf(c1));
      return sb.toString();
    }

    if (list.indexOf("INNNNDEX") != -1) {
      sb.append("List.indexOf(Object) failed to return the expected value");
      sb.append(" of -1 when the Object supplied didn't exist in the list.");
      return sb.toString();
    }

    list.clear();
    list.add(c0);
    list.add(c1);
    list.add(c2);

    UIComponent[] values = { c0, c1, c2 };
    int count = 0;
    for (Iterator i = list.iterator(); i.hasNext();) {
      String val = ((UIComponent) i.next()).getId();
      if (!values[count].getId().equals(val)) {
        sb.append("Iterator returned by List.iterator() didn't return");
        sb.append(" the elements in the expected order." + JSFTestUtil.NL);
        sb.append("Index of failure: ").append(count).append('\n');
        sb.append("Expected value: ").append(values[count].getId())
            .append('\n');
        sb.append("Received: ").append(val);
        return sb.toString();
      }
      count++;
    }

    list.add(c3);
    if (list.lastIndexOf(c3) != 3) {
      sb.append("List.lastIndexOf(Object) failed to return the expected");
      sb.append(" result of 3." + JSFTestUtil.NL + "Recevied: ")
          .append(list.lastIndexOf(c1));
      return sb.toString();
    }

    if (list.lastIndexOf("INNNNDEX") != -1) {
      sb.append("List.lastIndexOf(Object) failed to return the expected value");
      sb.append(" of -1 when the Object supplied didn't exist in the list.");
      return sb.toString();
    }

    values = new UIComponent[] { c0, c1, c2, c3 };
    count = 0;
    for (ListIterator i = list.listIterator(); i.hasNext();) {
      String val = ((UIComponent) i.next()).getId();
      if (!values[count].getId().equals(val)) {
        sb.append(
            "ListIterator returned by List.listIterator() " + "didn't return");
        sb.append(" the elements in the expected order." + JSFTestUtil.NL);
        sb.append("Index of failure: ").append(count).append('\n');
        sb.append("Expected value: ").append(values[count].getId())
            .append('\n');
        sb.append("Received: ").append(val);
        return sb.toString();
      }
      count++;
    }

    values = new UIComponent[] { c2, c3 };
    count = 0;
    for (ListIterator i = list.listIterator(2); i.hasNext();) {
      Object val = ((UIComponent) i.next()).getId();
      if (!values[count].getId().equals(val)) {
        sb.append("ListIterator returned by List.listIterator(int) "
            + "didn't return");
        sb.append(" the elements in the expected order." + JSFTestUtil.NL);
        sb.append("Index of failure: ").append(count).append('\n');
        sb.append("Expected value: ").append(values[count].getId())
            .append('\n');
        sb.append("Received: ").append(val);
        return sb.toString();
      }
      count++;
    }

    Object ret = list.remove(0);

    if (!"index0".equals(((UIComponent) ret).getId())) {
      sb.append("Expected List.remove(int) to return the object removed");
      sb.append(" from the specified index." + JSFTestUtil.NL);
      sb.append("Expected: index0" + JSFTestUtil.NL);
      sb.append("Recevied: ").append(((UIComponent) ret).getId());
      return sb.toString();
    }

    if (list.size() != 3) {
      sb.append("List.remove(int) didn't remove the element at index 0");
      sb.append(" as doing so would have put the size of the List at 3.");
      sb.append("Current List size: ").append(list.size());
      return sb.toString();
    }

    if (!"index1".equals(((UIComponent) list.get(0)).getId())) {
      sb.append("List.remove(int) didn't shift the remaining elements."
          + JSFTestUtil.NL);
      sb.append("Expected Object at index 0: index1" + JSFTestUtil.NL);
      sb.append("Actual: ").append(((UIComponent) list.get(0)).getId());
      return sb.toString();
    }

    if (!list.remove(c1)) {
      sb.append("List.remove(Object) failed to return true as expected.");
    }

    if (list.size() != 2) {
      sb.append("List.remove(Object) didn't remove the element specified");
      sb.append(" as doing so would have put the size of the List at 2.");
      sb.append("Current List size: ").append(list.size());
      return sb.toString();
    }

    if (!"index2".equals(((UIComponent) list.get(0)).getId())) {
      sb.append("List.remove(int) didn't shift the remaining " + "elements."
          + JSFTestUtil.NL);
      sb.append("Expected Object at index 0: index2" + JSFTestUtil.NL);
      sb.append("Actual: ").append(((UIComponent) list.get(0)).getId());
      return sb.toString();
    }

    list.clear();

    list.add(c0);
    list.add(c1);
    list.add(c2);
    list.add(c3);
    c = new ArrayList();
    c.add(c1);
    c.add(c2);

    if (!list.removeAll(c)) {
      sb.append("List.removeAll(c) didn't return true as expected.");
      return sb.toString();
    }

    if (list.size() != 2) {
      sb.append("List.removeAll(Collection) failed to remove the number");
      sb.append(" of expected elements.  The List contained 4 elements,");
      sb.append(" and the Collection passed, contained 2." + JSFTestUtil.NL
          + "The result");
      sb.append("ing size should have been 2.  The current size: ");
      sb.append(list.size());
      return sb.toString();
    }

    if (!"index0".equals(((UIComponent) list.get(0)).getId())
        && !"index3".equals(((UIComponent) list.get(1)).getId())) {
      sb.append("List.removeAll(Collection) removed the wrong elements.");
      sb.append("Expected the remaining elements to be 'index0' and");
      sb.append(" 'index3'" + JSFTestUtil.NL);
      sb.append("Element at index 0: ")
          .append(((UIComponent) list.get(0)).getId()).append('\n');
      sb.append("Element at index 1: ")
          .append(((UIComponent) list.get(1)).getId());
      return sb.toString();
    }

    list.clear();
    list.add(c0);
    list.add(c1);
    list.add(c2);
    list.add(c3);

    c = new ArrayList();
    c.add(c1);
    c.add(c3);

    if (!list.retainAll(c)) {
      sb.append("List.retainAll(Collect) failed to return true as expected.");
      return sb.toString();
    }

    if (list.size() != 2) {
      sb.append("List.retainAll(Collection) failed to remove the number");
      sb.append(" of expected elements.  The List contained 4 elements,");
      sb.append(" and the Collection passed, contained 2." + JSFTestUtil.NL
          + "The result");
      sb.append("ing size should have been 2.  The current size: ");
      sb.append(list.size());
      return sb.toString();
    }

    if (!"index1".equals(((UIComponent) list.get(0)).getId())
        && !"index3".equals(((UIComponent) list.get(1)).getId())) {
      sb.append("List.retainAll(Collection) removed the wrong elements.");
      sb.append("Expected the remaining elements to be 'index1' and");
      sb.append(" 'index3'" + JSFTestUtil.NL);
      sb.append("Element at index 0: ")
          .append(((UIComponent) list.get(0)).getId()).append('\n');
      sb.append("Element at index 1: ")
          .append(((UIComponent) list.get(1)).getId());
      return sb.toString();
    }

    ret = list.set(1, c2);

    if (!"index3".equals(((UIComponent) ret).getId())) {
      sb.append("List.set(int, Object) failed to return the expected");
      sb.append(
          " value when a valid index replacement occurred." + JSFTestUtil.NL);
      sb.append("Expected: index3" + JSFTestUtil.NL);
      sb.append("Received: ").append(((UIComponent) ret).getId());
      return sb.toString();
    }

    if (!"index2".equals(((UIComponent) list.get(1)).getId())) {
      sb.append("List.set(int, Object) failed to set the value at");
      sb.append(" index 1." + JSFTestUtil.NL);
      sb.append("Expected: index2" + JSFTestUtil.NL);
      sb.append("Received: ").append(((UIComponent) list.get(1)).getId());
      return sb.toString();
    }

    list.clear();
    list.add(c0);
    list.add(c1);
    list.add(c2);
    list.add(c3);

    List subList = list.subList(1, 3);
    if (subList.size() != 2) {
      sb.append("List.subList(1, 3) failed to return a List containing two");
      sb.append(" elements." + JSFTestUtil.NL);
      sb.append("Number of elements returned: ").append(subList.size());
      return sb.toString();
    }

    if (!"index1".equals(((UIComponent) subList.get(0)).getId())
        && !"index2".equals(((UIComponent) subList.get(1)).getId())) {
      sb.append("The List returned by List.subList() failed to contain");
      sb.append(
          " the expected values of 'index1' and 'index2'." + JSFTestUtil.NL);
      sb.append("Element at index 0: ")
          .append(((UIComponent) subList.get(0)).getId()).append('\n');
      sb.append("Element at index 1: ")
          .append(((UIComponent) subList.get(1)).getId());
      return sb.toString();
    }

    String[] strValues = new String[] { "index0", "index1", "index2",
        "index3" };

    Object[] array = list.toArray();
    Object[] temp = new String[array.length];
    for (int i = 0; i < array.length; i++) {
      temp[i] = ((UIComponent) array[i]).getId();
    }
    array = temp;

    Arrays.sort(strValues);
    Arrays.sort(array);

    if (!Arrays.equals(array, strValues)) {
      sb.append("List.toArray() didn't return all of the expected values");
      sb.append("Expected a length of 4, received: ").append(array.length)
          .append('\n');
      sb.append(
          "Values expected: index0, index1, index2, index3" + JSFTestUtil.NL);
      sb.append("Values received: ").append(JSFTestUtil.getAsString(array));
      return sb.toString();
    }

    UIComponent[] strArray = (UIComponent[]) list
        .toArray(new UIComponent[list.size()]);
    temp = new String[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      temp[i] = (strArray[i]).getId();
    }
    array = temp;

    if (!Arrays.equals(array, strValues)) {
      sb.append("List.toArray(Object[]) didn't return all of the "
          + "expected values");
      sb.append("Expected a length of 4, received: ").append(array.length)
          .append('\n');
      sb.append(
          "Values expected: index0, index1, index2, index3" + JSFTestUtil.NL);
      sb.append("Values received: ").append(JSFTestUtil.getAsString(array));
      return sb.toString();
    }

    // Now check for Faces specific List behavior
    if (!(list instanceof java.io.Serializable)) {
      sb.append("List returned by getChildren() is not an instance of ");
      sb.append("java.io.Serializable.");
      return sb.toString();
    }

    try {
      list.add(null);
      return "No Exception thrown if a null value was added to the List.";
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        return "Exception thrown when a null value was added to the "
            + "List, but it wasn't an instance of " + "NullPointerException."
            + JSFTestUtil.NL + "Exception Received: " + e.getClass().getName();
      }
    }

    try {
      list.add("StringType");
      return "No Exception thrown if a non-UIComponent value was added "
          + "to the List.";
    } catch (Exception e) {
      if (!(e instanceof ClassCastException)) {
        return "Exception thrown when a non-UIComponent value was "
            + "added to the List, but it wasn't an instance of "
            + "ClassCastException." + JSFTestUtil.NL + "Exception Received: "
            + e.getClass().getName();
      }
    }

    UIInput input = new UIInput();
    input.setId("id");
    list.add(input);

    if (!input.getParent().equals(component)) {
      return "When child component was added to List, it didn't have its"
          + " parent component explicitly set to the Component where "
          + "the List originated from.";
    }

    list.remove(input);
    if (input.getParent() != null) {
      return "After removing a child component from the parents list, the"
          + " child still contained a reference to its previous parent. "
          + " The reference should have been set to null.";
    }
    return null;
  }

  /**
   * <p>
   * Using the {@link UIComponent}s and expression provided, call findComponent
   * on the <code>searchFrom</code> component using the specified
   * <code>expression</code>. If the component is not found, write a message to
   * the provided <code>Writer</code>.
   * 
   * @param searchFrom
   *          the <code>UIComponent</code> to perform the search from
   * @param searchFor
   *          the <code>UIComponent</code> to search for
   * @param expression
   *          the search expression
   * @param out
   *          the StringBuffer to write error messages to
   */
  private void assertFound(UIComponent searchFrom, UIComponent searchFor,
      String expression, StringBuffer out) {

    UIComponent comp = searchFrom.findComponent(expression);
    if (searchFor != comp) {
      out.append(JSFTestUtil.FAIL + " Unabled to find component '");
      out.append(searchFor.getId()).append("' relative to component '");
      out.append(searchFrom.getId()).append("' using expression '");
      out.append(expression).append("'." + JSFTestUtil.NL);
      out.append("Component found: ")
          .append(comp != null ? comp.getId() : "null");
      out.append(JSFTestUtil.NL);
    }
  }

  /**
   * <p>
   * Using the {@link UIComponent} and expression provided, assert that no
   * component is found when <code>findComponent</code> is called. If a
   * component is found, print an error message.
   * </p>
   * 
   * @param searchFrom
   *          the <code>UIComponent</code> to perform the search from
   * @param expression
   *          the search expression
   * @param out
   *          the StringBuffer to write error messages to
   */
  private void assertNotFound(UIComponent searchFrom, String expression,
      StringBuffer out) {
    UIComponent comp = searchFrom.findComponent(expression);
    if (comp != null) {
      out.append(JSFTestUtil.FAIL + " Expected findComponent() to return null");
      out.append(" when calling findComponent() from component '");
      out.append(searchFrom.getId()).append("' using expression '");
      out.append(expression).append("'." + JSFTestUtil.NL);
      out.append("Component found: ").append(comp.getId());
    }
  }

  // ------------------------------------------- Test Methods ----
  // UIComponent.getAttributes()
  public void uiComponentGetAttributesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String result = validateAttributeMap(createComponent());
    if (result != null) {
      out.println(JSFTestUtil.FAIL + " " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getClientId(FacesContext)
  public void uiComponentGetClientIdContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewRoot root = getFacesContext().getViewRoot();
    root.setViewId("/test");
    root.setId("root");
    UIComponent comp = createComponent();
    root.getChildren().add(comp);

    // Not much can be done here aside from ensuring the result is
    // not null.
    if (comp.getClientId(getFacesContext()) == null) {
      out.println(
          JSFTestUtil.FAIL + " getClientId(FacesContext) " + "returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getClientId()
  public void uiComponentGetClientIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewRoot root = getFacesContext().getViewRoot();
    root.setViewId("/test");
    root.setId("root");
    UIComponent comp = createComponent();
    root.getChildren().add(comp);

    // Not much can be done here aside from ensuring the result is
    // not null. (Client IDs themselves can be Implementation specific.)
    if (comp.getClientId() == null) {
      out.println(JSFTestUtil.FAIL + " getClientId() returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentComponent()
  public void uiComponentGetCurrentComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String expectedId = "myComp";
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId("myComp");

    String uicType = component.getClass().getSimpleName();

    component.encodeBegin(context);

    UIComponent mc = UIComponent.getCurrentComponent(context);
    String recId = mc.getId();

    if (!expectedId.equals(recId)) {
      out.println("Test FAILED. " + uicType + ".getCurrentComponent() "
          + "returned unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected ID: " + expectedId + JSFTestUtil.NL + "Receieved ID: "
          + recId);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentComponent()
  public void uiComponentGetCurrentComponentNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId("test_ID");
    String uicType = component.getClass().getSimpleName();

    /*
     * Push UIComponents onto the EL Data Structure.
     */
    component.encodeBegin(context);

    /*
     * Remove the Component from the Data Structure.
     */
    component.encodeEnd(context);
    if (null != UIComponent.getCurrentComponent(context)) {
      out.println("Test FAILED. " + uicType + ".getCurrentComponent() "
          + "returned unexpected UIComponent." + JSFTestUtil.NL
          + "Expected Call to UIComponent."
          + "getCurrentComponent(context) to return null." + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentComponent()
  public void uiComponentGetCurrentComponentNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    createComponent();

    try {
      UIComponent.getCurrentComponent(null);
      out.println(JSFTestUtil.FAIL + " UIComponent.getCurrentComponent(null) "
          + "failed to throw an exception when a null "
          + "FacesContext argument was passed.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. Unexpected Exception thrown for "
            + "UIComponent.getCurrentComponent(null)"
            + " it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentCompositeComponent()
  public void uiComponentGetCurrentCompositeComponentNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId("test_ID");
    String uicType = component.getClass().getSimpleName();

    /*
     * Push UIComponents onto the EL Data Structure.
     */
    component.encodeBegin(context);

    /*
     * Remove the Component from the Data Structure.
     */
    component.encodeEnd(context);
    if (null != UIComponent.getCurrentCompositeComponent(context)) {
      out.println("Test FAILED. " + uicType
          + ".getCurrentCompositeComponent() returned unexpected "
          + "UIComponent." + JSFTestUtil.NL + "Expected Call to UIComponent."
          + "getCurrentCompositeComponent(context) to return null."
          + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentComponent()
  public void uiComponentGetCurrentCompositeComponentNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    createComponent();

    try {
      UIComponent.getCurrentCompositeComponent(null);
      out.println(JSFTestUtil.FAIL + " UIComponent."
          + "getCurrentCompositeComponent(null) "
          + "failed to throw an exception when a null "
          + "FacesContext argument was passed.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. Unexpected Exception thrown for "
            + "UIComponent.getCurrentCompositeComponent(null)"
            + " it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.isCompositeComponent()
  public void uiComponentIsCompositeComponentNegTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId("test_ID");
    String uicType = component.getClass().getSimpleName();

    if (false != UIComponent.isCompositeComponent(component)) {
      out.println("Test FAILED. " + uicType
          + ".isCompositeComponent() returned unexpected " + "UIComponent."
          + JSFTestUtil.NL + "Expected Call to UIComponent."
          + "isCurrentCompositeComponent(context) to return false."
          + JSFTestUtil.NL);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getCurrentComponent()
  public void uiComponentIsCompositeComponentNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    createComponent();

    try {
      UIComponent.isCompositeComponent(null);
      out.println(
          JSFTestUtil.FAIL + " UIComponent." + "isCompositeComponent(null) "
              + "failed to throw an exception when a null "
              + "FacesContext argument was passed.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. Unexpected Exception thrown for "
            + "UIComponent.isCompositeComponent(null)"
            + " it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getClientId() throws NPE if FacesContext arg is null
  public void uiComponentGetClientIdNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      createComponent().getClientId(null);
      out.println(JSFTestUtil.FAIL + " getClientId() failed to throw an "
          + "exception when a null FacesContext argument was passed.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " getClientId() threw an Exception "
            + "when a null FacesContext argument was passed, but"
            + " it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.{get,set}Id
  public void uiComponentGetSetIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    String id = "compID";

    comp.setId(id);
    if (!id.equals(comp.getId())) {
      out.println(JSFTestUtil.FAIL + " getId() failed to return"
          + " the expected value '" + id + "'.");
      return;
    }

    comp.setId(null);
    if (comp.getId() != null) {
      out.println(JSFTestUtil.FAIL + " getId() failed to return null"
          + " after null was explicitly set via setId().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.queueEvent() throws NullPointerException
  public void uiComponentQueueEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "queueEvent",
        new Class<?>[] { FacesEvent.class }, new Object[] { null }, out);
  }

  // UIComponent.restoreAttachedState() throws NullPointerException
  public void uiComponentRestoreAttachedStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(),
        "restoreAttachedState",
        new Class<?>[] { FacesContext.class, Object.class },
        new Object[] { null, "test" }, out);
  }

  // UIComponent.saveAttachedState() throws NullPointerException
  public void uiComponentSaveAttachedStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "saveAttachedState",
        new Class<?>[] { FacesContext.class, Object.class },
        new Object[] { null, "test" }, out);
  }

  // UIComponent.setId(String) enforces certain restrictions
  public void uiComponentSetIdRestrictionsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();

    try {
      comp.setId("");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when setId() was"
          + " passed a zero-length String.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when setId() was"
            + " passed a zero-length String, but it wasn't an"
            + " instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      comp.setId("1__simple-ID");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when setId() was"
          + " passed an ID that starts with a numeric.");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when setId() was"
            + " passed an ID that starts with a numeric, but it wasn't an"
            + " instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      comp.setId("illegal#id");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when setId() was"
          + " passed an ID that contains an illegal character(#).");
      return;
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when setId() was"
            + " passed an ID contains an illegal character(#)"
            + ", but it wasn't an" + " instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      comp.setId("_this--is_a_val-idID");
    } catch (Exception e) {
      if (e instanceof IllegalArgumentException) {
        out.println(JSFTestUtil.FAIL + " IllegalArgumentException thrown "
            + "when setId() was passed a valid id "
            + "(_this--is_a_val-idID).");
        return;

      }
      throw new ServletException("Unexpected Exception: " + e.toString(), e);
    }

    try {
      comp.setId(null);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown "
          + "when setId() was passed null.");
      out.println("Exception received: " + e.getClass().getName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for UIComponent.subscribeToEvent
  public void uiComponentSubscribeToEventTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    UIComponent uic = createComponent();
    SystemEvent cse = new TCKSystemEvent(uic);

    ComponentSystemEventListener csel = new TCKComponentSystemEventlistener();

    if (application != null) {
      try {
        uic.subscribeToEvent(cse.getClass(), csel);
        out.println(JSFTestUtil.PASS);
      } catch (Exception e) {
        out.println("Test FAILED.");
        e.printStackTrace();
      }

    } else {
      out.println(JSFTestUtil.FAIL + " Unable to obtain Application instance.");
    }
  }

  // Test for UIComponent.subscribeToEvent
  public void uiComponentSubscribeToEventNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Application application = getApplication();

    UIComponent uic = createComponent();
    SystemEvent cse = new TCKSystemEvent(uic);
    ComponentSystemEventListener csel = new TCKComponentSystemEventlistener();

    boolean status = true;
    // UIComponent.subscribeToEvent(Class, null)
    if (application != null) {
      try {
        uic.subscribeToEvent(cse.getClass(), null);
        status = false;
        out.println("Test FAILED. Expected NullPointerException!"
            + JSFTestUtil.NL + "When ComponentSystemEventListener is null. "
            + "No Exception thrown for the following test case:"
            + JSFTestUtil.NL + "UIComponent.SubscribeToEvent(Class, null)");

      } catch (NullPointerException npe) {
        // Do Nothing Test passed.
      } catch (Exception e) {
        status = false;
        out.println("Test FAILED. UnExpected Exception!" + JSFTestUtil.NL
            + "Expected: NulPointerException" + JSFTestUtil.NL + "Received: "
            + e.getClass().getSimpleName());
        e.printStackTrace();
      }

      // UIComponent.subscribeToEvent(null, ComponentSystemEventlistener)
      try {
        uic.subscribeToEvent(null, csel);
        status = false;
        out.println("Test FAILED. Expected NullPointerException!"
            + JSFTestUtil.NL + "When SystemEvent is null. No Exception thrown "
            + "for the following test case:" + JSFTestUtil.NL
            + "UIComponent.SubscribeToEvent(null, "
            + "ComponentSystemEventlistener)");

      } catch (NullPointerException npe) {
        // Do Nothing Test passed.
      } catch (Exception e) {
        status = false;
        out.println("Test FAILED.");
        out.println("UnExpected Exception!" + JSFTestUtil.NL
            + "Expected: NulPointerException" + JSFTestUtil.NL + "Received: "
            + e.getClass().getSimpleName());
        e.printStackTrace();
      }

    } else {
      out.println(JSFTestUtil.FAIL + " Unable to obtain Application instance.");
    }

    // Did anything fail?
    if (status) {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIComponent.{get,set}Parent()
  public void uiComponentGetSetParentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    if (comp.getParent() != null) {
      out.println(JSFTestUtil.FAIL + " UIComponent.getParent() returned a non"
          + "-null result for a newly created component.");
      return;
    }

    UIViewRoot root = getFacesContext().getViewRoot();
    root.getChildren().add(comp);

    if (!root.equals(comp.getParent())) {
      out.println(JSFTestUtil.FAIL + " getParent() returned an unexpected "
          + "parent component.");
      out.println("Expected: " + root);
      out.println("Received: " + comp.getParent());
    }

    root.getChildren().remove(comp);
    UIComponent input = new UIInput();
    root.getChildren().add(input);
    input.getChildren().add(comp);

    if (!input.equals(comp.getParent())) {
      out.println(JSFTestUtil.FAIL + " getParent() returned an unexpected "
          + "parent component.");
      out.println("Expected: " + input);
      out.println("Received: " + comp.getParent());
    }

    comp.setParent(null);
    if (comp.getParent() != null) {
      out.println(JSFTestUtil.FAIL + " UIComponent.getParent() returned a non"
          + "-null result after calling setParent(null).");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.{is,set}Rendered()
  public void uiComponentIsSetRenderedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    comp.setRendered(true);
    if (!comp.isRendered()) {
      out.println(JSFTestUtil.FAIL + " isRendered() returned false after"
          + " calling setRendered(true).");
      return;
    }

    comp.setRendered(false);
    if (comp.isRendered()) {
      out.println(JSFTestUtil.FAIL + " isRendered() returned true after"
          + " calling setRendered(false).");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.isInView()
  public void uiComponentIsInViewTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewRoot root = getFacesContext().getViewRoot();
    UIComponent comp = createComponent();

    String ct = comp.getClass().getName();
    root.getChildren().add(comp);

    if (!comp.isInView()) {
      out.println("Test FAILED. " + ct + "isInView() returned false. "
          + "Expected UIcomponent to be in the View.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.isInView()
  public void uiComponentIsInViewNegTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    String ct = comp.getClass().getName();

    if (comp.isInView()) {
      out.println("Test FAILED. " + ct + "isInView() returned true. "
          + "Expected UIcomponent *NOT* to be in the View.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getChildren()
  public void uiComponentGetChildrenTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String result = validateListMethods(createComponent());

    if (result != null) {
      out.println(result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.{get,set}RendererType
  public void uiComponentGetSetRendererTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();

    // First check the default return value for the component
    // under test base on the rendererType variable. If null,
    // then expect the return type to be null, if non null,
    // make sure the values are equal.
    if (rendererType == null) {
      if (comp.getRendererType() != null) {
        out.println(JSFTestUtil.FAIL + " Expected getRendererType() to"
            + " return null for this component type.");
        out.println("Value received: " + comp.getRendererType());
        return;
      }
    } else {
      if (!rendererType.equals(comp.getRendererType())) {
        out.println(JSFTestUtil.FAIL + " Expected getRendererType() to"
            + " return '" + rendererType + "' for this component " + "type.");
        out.println("Value received: " + comp.getRendererType());
        return;
      }
    }

    // now ensure that the value can be overridden
    comp.setRendererType("string");

    if (!"string".equals(comp.getRendererType())) {
      out.println(JSFTestUtil.FAIL + " Expected getRendererType() to"
          + " return 'string' for this component "
          + "after having explicitly set it via setRendererType()");
      out.println("Value received: " + comp.getRendererType());
      return;
    }

    // ensure we can set null
    comp.setRendererType(null);
    if (comp.getRendererType() != null) {
      out.println(JSFTestUtil.FAIL + " Expeccted getRendererType() to return"
          + " null after having explicitly set it via setRendererType().");
      return;
    }

    // reset the renderer type
    comp.setRendererType(rendererType);

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getRendersChildren()
  public void uiComponentGetRendersChildrenTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    boolean expectRendersChildren;
    UIComponent comp = createComponent();

    if (rendersChildren != null) {
      expectRendersChildren = rendersChildren.booleanValue();
    } else {

      // Follow the default algorithm as outlined in the javadocs to
      // determine
      // if the component under test renders children, and then compare
      // the
      // return value of getRendersChildren() to what we discover.
      String rendererType = comp.getRendererType();
      String rendererFamily = comp.getFamily();
      if (rendererType != null) {
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
            .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
            getFacesContext().getViewRoot().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(rendererFamily, rendererType);
        expectRendersChildren = renderer.getRendersChildren();

      } else {
        expectRendersChildren = false;
      }
    }

    // test setup is complete, now validate.
    if (comp.getRendersChildren() != expectRendersChildren) {
      out.println(JSFTestUtil.FAIL + " Expected getRendersChildren()"
          + " to return '" + expectRendersChildren + "' but " + "recevied: "
          + comp.getRendersChildren());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getChildCount()
  public void uiComponentGetChildCountTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    if (comp.getChildCount() != 0) {
      out.println(JSFTestUtil.FAIL + " getChildCount() returned a value greater"
          + " than zero for a newly created component.");
      out.println("Value returned: " + comp.getChildCount());
      return;
    }

    UIComponent child1 = new UIInput();
    UIComponent child2 = new UIForm();
    UIComponent child2_1 = new UIInput();
    UIComponent child1_1 = new UIOutput();
    child1.setId("child1");
    child2.setId("child2");
    child2_1.setId("child2_1");
    child1_1.setId("child1_1");
    child2.getChildren().add(child2_1);
    child1.getChildren().add(child1_1);

    List<UIComponent> children = comp.getChildren();

    children.add(child1);
    children.add(child2);

    // children count should be 2
    if (comp.getChildCount() != 2) {
      out.println(JSFTestUtil.FAIL + " Expected getChildCount() to return 2.");
      out.println("Child count received: " + comp.getChildCount());
      return;
    }

    children.remove(child1);
    if (comp.getChildCount() != 1) {
      out.println(JSFTestUtil.FAIL + " Expected getChildCount() to return 1.");
      out.println("Child count received: " + comp.getChildCount());
      return;
    }

    child2.getChildren().remove(child2_1);
    if (comp.getChildCount() != 1) {
      out.println(JSFTestUtil.FAIL + " Expected getChildCount() to return 1.");
      out.println("Child count received: " + comp.getChildCount());
      return;
    }

    children.remove(child2);
    if (comp.getChildCount() != 0) {
      out.println(JSFTestUtil.FAIL + " Expected getChildCount() to return 0.");
      out.println("Child count received: " + comp.getChildCount());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getFacets()
  public void uiComponentGetFacetsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String result = validateFacetMap(createComponent());
    if (result != null) {
      out.println(JSFTestUtil.FAIL + " " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getFacet()
  public void uiComponentGetFacetTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    UIComponent output = new UIOutput();
    comp.getFacets().put("facet", output);

    if (!output.equals(comp.getFacet("facet"))) {
      out.println(JSFTestUtil.FAIL + " getFacet() either returned null or"
          + " returned an unexpected UIComponent.");
      out.println("Expected: " + output);
      out.println("Received: " + comp.getFacet("facet"));
      return;
    }

    if (comp.getFacet("TCKFACET_1_2_3") != null) {
      out.println(JSFTestUtil.FAIL + " getFacet() failed to return null when"
          + " requesting a Facet that did not exist.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getFacetCount()
  public void uiComponentGetFacetCountTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    UIComponent output = new UIOutput();
    comp.getFacets().put("facet", output);
    int golden = 1;
    int result = comp.getFacetCount();

    if (result < golden) {
      out.println(JSFTestUtil.FAIL + " getFacetCount() either returned null or"
          + " returned an unexpected Integer." + JSFTestUtil.NL + "Expected: "
          + golden + " |<" + JSFTestUtil.NL + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End uiComponentGetFacetCountTest

  // UIComponent.getFacetsAndChildren()
  public void uiComponentGetFacetsAndChildrenTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();

    // pre-test initialization
    UIComponent child1 = new UIOutput();
    child1.setId("child1");
    UIComponent child2 = new UIOutput();
    child2.setId("child2");
    UIComponent child3 = new UIOutput();
    child3.setId("child3");
    UIComponent child1_1 = new UIOutput();
    child1_1.setId("child1_1");

    UIComponent facet = new UIOutput();
    facet.setId("facet");
    UIComponent _facet = new UIOutput();
    _facet.setId("_facet");
    UIComponent facet1_1 = new UIOutput();
    facet1_1.setId("facet1_1");
    UIComponent facet1_2 = new UIOutput();
    facet1_2.setId("facet1_2");
    UIComponent facet2_1 = new UIOutput();
    facet2_1.setId("facet2_1");
    UIComponent facet3_1 = new UIOutput();
    facet3_1.setId("facet3_1");
    UIComponent facet1_1_1 = new UIOutput();
    facet1_1_1.setId("facet1_1_1");

    child1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1_2", facet1_2);
    child2.getFacets().put("facet2_1", facet2_1);
    child3.getFacets().put("facet3_1", facet3_1);
    child1_1.getFacets().put("facet_1_1_1", facet1_1_1);

    List<UIComponent> children = comp.getChildren();
    children.add(child1);
    children.add(child2);
    children.add(child3);
    child1.getChildren().add(child1_1);
    comp.getFacets().put("facet", facet);
    comp.getFacets().put("_facet", _facet);

    Iterator<UIComponent> iter = comp.getFacetsAndChildren();

    try {
      iter.remove();
      out.println(JSFTestUtil.FAIL + " Expected the Iterator returned by"
          + " getFacetsAndChildren() to *NOT* support the remove()"
          + " method.");
    } catch (Exception e) {
      ; // Exception thrown.

    }

    UIComponent facet1 = (UIComponent) iter.next();
    UIComponent facet2 = (UIComponent) iter.next();

    String id1 = facet1.getId();
    String id2 = facet2.getId();

    if (id1.equals(id2)) {
      out.println(JSFTestUtil.FAIL + " The ID for the first two components "
          + "of the Iterator were equal.  Expected two unique "
          + "values: 'facet' or '_facet'");
      out.println("ID for the first component: " + id1);
      out.println("ID for the second component: " + id2);
      return;
    }

    if ("facet".equals(id1) || "_facet".equals(id1)) {
      // found a match
    } else {
      out.println(JSFTestUtil.FAIL + " Expected the ID of the first component"
          + " in the Iterator to have an ID of 'facet' or '_facet'.");
      out.println("ID for the first component: " + id1);
      return;
    }

    if ("facet".equals(id2) || "_facet".equals(id2)) {
      // found a match
    } else {
      out.println(JSFTestUtil.FAIL + " Expected the ID of the second component"
          + " in the Iterator to have an ID of 'facet' or '_facet'.");
      out.println("ID for the second component: " + id2);
      return;
    }

    // the remainder of the components in the iterator should be children
    String[] childrenIds = new String[3];
    for (int i = 0; i < 3; i++) {
      childrenIds[i] = ((UIComponent) iter.next()).getId();
    }

    if (iter.hasNext()) {
      out.println(JSFTestUtil.FAIL + " Expected the Iterator to contain only"
          + " 3 additional elements after popping the two facets"
          + " from the Iterator.");
    }

    String[] expectedIds = { "child1", "child2", "child3" };

    Arrays.sort(childrenIds);
    Arrays.sort(expectedIds);

    if (!Arrays.equals(childrenIds, expectedIds)) {
      out.println(JSFTestUtil.FAIL + " Unable to find all of the expected"
          + " components in the Iterator.");
      out.println("Expected components with IDs in this order: "
          + JSFTestUtil.getAsString(expectedIds));
      out.println(
          "Components received: " + JSFTestUtil.getAsString(childrenIds));
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.getFamily()
  public void uiComponentGetFamilyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();

    if (!comp.getFamily().contains("javax.faces.")) {
      out.println(JSFTestUtil.FAIL
          + " Incorrect value returned from UIComponent.getFamily()"
          + JSFTestUtil.NL + "Expected FamilyName to have: javax.faces."
          + JSFTestUtil.NL + "Received: " + comp.getFamily());

    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIComponent.findComponent()
  public void uiComponentFindComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    out.println(
        "Set up a component hierarchy as follows (component ids in quotes)");
    out.println("\"a\" - UIViewRoot at head of hierarchy");
    out.println("\"a\" has children \"b\" and \"c\"");
    out.println("\"b\" has children \"d\" and \"g\"");
    out.println("d\" has children \"e\" and \"f\"");
    out.println("\"c\" has children \"h\" and \"i\"");
    out.println("Components \"b\" and \"d\" implement NamingContainer");

    UIViewRoot a = createViewRoot();
    a.setId("a");
    UIForm b = new UIForm();
    b.setId("b");
    UIPanel c = new UIPanel();
    c.setId("c");
    UINamingContainer d = new UINamingContainer();
    d.setId("d");
    UIPanel e = new UIPanel();
    e.setId("e");
    UIPanel f = new UIPanel();
    f.setId("f");
    UIPanel g = new UIPanel();
    g.setId("g");
    UIPanel h = new UIPanel();
    h.setId("h");
    UIPanel i = new UIPanel();
    i.setId("i");
    a.getChildren().add(b);
    a.getChildren().add(c);
    b.getChildren().add(d);
    b.getChildren().add(g);
    c.getChildren().add(h);
    c.getChildren().add(i);
    d.getChildren().add(e);
    d.getChildren().add(f);

    StringBuffer buf = new StringBuffer(128);

    // Positive relative searches from "a"
    assertFound(a, a, "a", buf);
    assertFound(a, b, "b", buf);
    assertFound(a, c, "c", buf);
    assertFound(a, d, "b:d", buf);
    assertFound(a, e, "b:d:e", buf);
    assertFound(a, f, "b:d:f", buf);
    assertFound(a, g, "b:g", buf);
    assertFound(a, h, "h", buf);
    assertFound(a, i, "i", buf);

    // Negative relative searches from "a"
    assertNotFound(a, "d", buf);
    assertNotFound(a, "e", buf);
    assertNotFound(a, "f", buf);
    assertNotFound(a, "g", buf);

    // Positive relative searches from "b"
    assertFound(b, b, "b", buf);
    assertFound(b, d, "d", buf);
    assertFound(b, e, "d:e", buf);
    assertFound(b, f, "d:f", buf);
    assertFound(b, g, "g", buf);

    // Negative relative searches from "b"
    assertNotFound(b, "a", buf);
    assertNotFound(b, "c", buf);
    assertNotFound(b, "e", buf);
    assertNotFound(b, "f", buf);
    assertNotFound(b, "h", buf);
    assertNotFound(b, "i", buf);

    // Positive relative searches from "c"
    assertFound(c, a, "a", buf);
    assertFound(c, b, "b", buf);
    assertFound(c, c, "c", buf);
    assertFound(c, d, "b:d", buf);
    assertFound(c, e, "b:d:e", buf);
    assertFound(c, f, "b:d:f", buf);
    assertFound(c, g, "b:g", buf);
    assertFound(c, h, "h", buf);
    assertFound(c, i, "i", buf);

    // Negative relative searches from "c"
    assertNotFound(c, "d", buf);
    assertNotFound(c, "e", buf);
    assertNotFound(c, "f", buf);
    assertNotFound(c, "g", buf);

    // Positive relative searches from "d"
    assertFound(d, d, "d", buf);
    assertFound(d, e, "e", buf);
    assertFound(d, f, "f", buf);

    // Negative relative searches from "d"
    assertNotFound(d, "a", buf);
    assertNotFound(d, "b", buf);
    assertNotFound(d, "c", buf);
    assertNotFound(d, "g", buf);
    assertNotFound(d, "h", buf);
    assertNotFound(d, "i", buf);

    // Positive relative searches from "e"
    assertFound(e, d, "d", buf);
    assertFound(e, e, "e", buf);
    assertFound(e, f, "f", buf);

    // Negative relative searches from "e"
    assertNotFound(e, "a", buf);
    assertNotFound(e, "b", buf);
    assertNotFound(e, "c", buf);
    assertNotFound(e, "g", buf);
    assertNotFound(e, "h", buf);
    assertNotFound(e, "i", buf);

    // Positive relative searches from "f"
    assertFound(f, d, "d", buf);
    assertFound(f, e, "e", buf);
    assertFound(f, f, "f", buf);

    // Negative relative searches from "f"
    assertNotFound(f, "a", buf);
    assertNotFound(f, "b", buf);
    assertNotFound(f, "c", buf);
    assertNotFound(f, "g", buf);
    assertNotFound(f, "h", buf);
    assertNotFound(f, "i", buf);

    // Positive relative searches from "g"
    assertFound(g, b, "b", buf);
    assertFound(g, d, "d", buf);
    assertFound(g, e, "d:e", buf);
    assertFound(g, f, "d:f", buf);
    assertFound(g, g, "g", buf);

    // Negative relative searches from "g"
    assertNotFound(g, "a", buf);
    assertNotFound(g, "c", buf);
    assertNotFound(g, "e", buf);
    assertNotFound(g, "f", buf);
    assertNotFound(g, "h", buf);
    assertNotFound(g, "i", buf);

    // Positive relative searches from "h"
    assertFound(h, a, "a", buf);
    assertFound(h, b, "b", buf);
    assertFound(h, c, "c", buf);
    assertFound(h, d, "b:d", buf);
    assertFound(h, e, "b:d:e", buf);
    assertFound(h, f, "b:d:f", buf);
    assertFound(h, g, "b:g", buf);
    assertFound(h, h, "h", buf);
    assertFound(h, i, "i", buf);

    // Negative relative searches from "h"
    assertNotFound(h, "d", buf);
    assertNotFound(h, "e", buf);
    assertNotFound(h, "f", buf);
    assertNotFound(h, "g", buf);

    // Positive relative searches from "i"
    assertFound(i, a, "a", buf);
    assertFound(i, b, "b", buf);
    assertFound(i, c, "c", buf);
    assertFound(i, d, "b:d", buf);
    assertFound(i, e, "b:d:e", buf);
    assertFound(i, f, "b:d:f", buf);
    assertFound(i, g, "b:g", buf);
    assertFound(i, h, "h", buf);
    assertFound(i, i, "i", buf);

    // Negative relative searches from "i"
    assertNotFound(i, "d", buf);
    assertNotFound(i, "e", buf);
    assertNotFound(i, "f", buf);
    assertNotFound(i, "g", buf);

    // Absolute searches from a - i
    UIComponent[] comps = { a, b, c, d, e, f, g, h, i };

    String[] exprs = { ":a", ":b", ":c", ":b:d", ":b:d:e", ":b:d:f", ":b:g",
        ":h", ":i" };

    for (int ii = 0; ii < comps.length; ii++) {
      UIComponent searchFrom = comps[ii];
      for (int j = 0; j < comps.length; j++) {
        assertFound(searchFrom, comps[j], exprs[j], buf);
      }
    }

    if (buf.length() > 0) {
      out.print(buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  // UIComponent.findComponent throws IAE if intermediate identifier in
  // expression identifies UIComponent that isnt' a NamingContainer
  public void uiComponentFindComponentIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    out.println(
        "Set up a component hierarchy as follows (component ids in quotes)");
    out.println("\"a\" - UIViewRoot at head of hierarchy");
    out.println("\"a\" has children \"b\" and \"c\"");
    out.println("\"b\" has children \"d\" and \"g\"");
    out.println("d\" has children \"e\" and \"f\"");
    out.println("\"c\" has children \"h\" and \"i\"");
    out.println("Components \"b\" and \"d\" implement NamingContainer");

    UIViewRoot a = createViewRoot();
    a.setId("a");
    UIForm b = new UIForm();
    b.setId("b");
    UIPanel c = new UIPanel();
    c.setId("c");
    UINamingContainer d = new UINamingContainer();
    d.setId("d");
    UIPanel e = new UIPanel();
    e.setId("e");
    UIPanel f = new UIPanel();
    f.setId("f");
    UIPanel g = new UIPanel();
    g.setId("g");
    UIPanel h = new UIPanel();
    h.setId("h");
    UIPanel i = new UIPanel();
    i.setId("i");
    a.getChildren().add(b);
    a.getChildren().add(c);
    b.getChildren().add(d);
    b.getChildren().add(g);
    c.getChildren().add(h);
    c.getChildren().add(i);
    d.getChildren().add(e);
    d.getChildren().add(f);

    String[] exprs = { "a:c:h", "a:c:i", ":a:c:h", ":a:c:i", "c:h", "c:i",
        ":c:h", ":c:i" };

    StringBuffer buf = new StringBuffer(128);

    for (int ii = 0; ii < exprs.length; ii++) {
      String expr = exprs[ii];
      try {
        a.findComponent(expr);
        buf.append(
            JSFTestUtil.FAIL + " No Exception thrown when findComponent()");
        buf.append(" was called with an expression where the intermediate");
        buf.append(" identifier didn't identify a NamingContainer.\n");
        buf.append("Search was relative to: '").append(a.getId()).append("'\n");
        buf.append("Expression: '").append(expr).append("'\n\n");
      } catch (Exception ex) {
        if (!(ex instanceof IllegalArgumentException)) {
          buf.append(
              JSFTestUtil.FAIL + " Exception thrown when findComponent()");
          buf.append(" was called with an expression where the intermediate");
          buf.append(" identifier didn't identify a NamingContainer, but it");
          buf.append(" wasn't an instance of IllegalArgumentException.\n");
          buf.append("Search was relative to: '").append(a.getId())
              .append("'\n");
          buf.append("Expression: '").append(expr).append("'\n");
          buf.append("Exception received: ").append(ex.getClass().getName());
        }
      }
    }

    if (buf.length() > 0) {
      out.print(buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIComponent.findComponent() throws NullPointerException
  public void uiComponentFindComponentNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent component = this.createComponent();

    try {
      component.findComponent(null);
      out.println("Test FAILED" + JSFTestUtil.NL
          + "Expected NullPointerException to be thrown when"
          + "expr is null.");
    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println("Test FAILED" + JSFTestUtil.NL
          + "Expected NullPointerException to be thrown when" + "expr is null."
          + JSFTestUtil.NL + "Instead received: " + JSFTestUtil.NL
          + e.toString());
    }

  }

  // UIComponent.InvokeOnComponent()
  public void uiComponentInvokeOnComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("Set up a component hierarchy as follows (component ids in "
        + "quotes)");
    out.println("\"root\" - UIViewRoot at head of hierarchy");
    out.println("\"root\" has children \"form1\" and \"form2\"");
    out.println("\"form1\" has child \"panel1\"");
    out.println("panel1\" has children \"input1\" and \"input2\"");
    out.println("\"form2\" has child \"panel2\"");
    out.println("panel2\" has children \"input1\" and \"input2\"");

    Map<String, UIComponent> tree = setupInvokeOnComponentTree();

    UIViewRoot rootTree = (UIViewRoot) tree.get("root");
    UIInput input1 = (UIInput) tree.get("input1");

    boolean result = false;

    FacesContext context = getFacesContext();

    result = rootTree.invokeOnComponent(context, input1.getClientId(context),
        new ContextCallback() {

          public void invokeContextCallback(FacesContext context,
              UIComponent component) {
            foundComponent = component;
          }
        });

    if (!(foundComponent.equals(input1))) {
      out.println(JSFTestUtil.FAIL + " Expected the Object returned by "
          + "InvokeOnComponent() to be named input1.");
      return;
    } else if (!result) {
      out.println(JSFTestUtil.FAIL + " Expect the value of \"result\" to "
          + "be true" + " Instead found: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.InvokeOnComponent() Negative Test.
  public void uiComponentInvokeOnComponentNegativeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    Map<String, UIComponent> tree = setupInvokeOnComponentTree();

    UIViewRoot root = (UIViewRoot) tree.get("root");

    foundComponent = null;
    boolean result = false;

    FacesContext context = getFacesContext();
    ContextCallback callback = new ContextCallback() {

      public void invokeContextCallback(FacesContext context,
          UIComponent component) {
        foundComponent = component;
      }
    };

    // Negative case 0, null pointers
    try {
      root.invokeOnComponent(context, "form:input7", null);

      out.println("Test FAILED. invokeOnComponent(context,"
          + " form:input7, null) Did not throw an" + "Exception");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. invokeOnComponent(context,"
            + " form:input7, null) threw an Exception"
            + " when a null Callback argument was passed, but it "
            + "wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      root.invokeOnComponent(context, null, callback);

      out.println("Test FAILED. invokeOnComponent(context,"
          + " null, callback) Did not throw an" + "Exception");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. invokeOnComponent(Context,"
            + " null, callback) threw an Exception"
            + " when a null clientId argument was passed, but it "
            + "wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      root.invokeOnComponent(null, "form:input7", callback);

      out.println("Test FAILED. invokeOnComponent(null,"
          + "form:input7, callback) Did not throw an Exception");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Test FAILED. invokeOnComponent(null,"
            + "form:input7, callback) threw an Exception"
            + " when a null FacesContext argument was passed, but"
            + " it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Negative case 1, not found component.
    result = root.invokeOnComponent(context, "form:input7", callback);

    if (result) {
      out.println(JSFTestUtil.FAIL + " Expected \"false\" returned by"
          + " InvokeOnComponent() when passing in \"form:input7\""
          + " as clientId arg. Instead received " + result);
      return;
    }

    // Negative case 2A, callback throws exception with found component
    try {
      result = root.invokeOnComponent(context, "form2:input2",
          new ContextCallback() {

            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
              foundComponent = component;
              // When else am I going to get the chance to throw
              // this exception?
              throw new IllegalStateException();
            }
          });
    } catch (Exception e) {
      if (!(e instanceof FacesException)) {
        out.println("Test FAILED. invokeOnComponent(facesContext,"
            + " form2:input2, Callback) threw an Exception,"
            + " but it wasn't an instance of FacesException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Negative case 2B, callback throws exception with not found component
    try {
      result = root.invokeOnComponent(context, "form2:input6",
          new ContextCallback() {

            public void invokeContextCallback(FacesContext context,
                UIComponent component) {
              foundComponent = component;
              // When else am I going to get the chance to throw
              // this exception?
              throw new IllegalStateException();
            }
          });
    } catch (Exception e) {
      if (!(e instanceof FacesException)) {
        out.println("Test FAILED. invokeOnComponent(facesContext,"
            + " form2:input6, Callback) threw an Exception,"
            + " but it wasn't an instance of FacesException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processSaveRestoreState()
  public void uiComponentProcessSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setTransient(false);
    TCKComponent child1 = new TCKComponent("child1");
    child1.setTransient(false);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setTransient(false);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setTransient(false);
    TCKComponent facet1_1 = new TCKComponent("facet1_1");
    facet1_1.setTransient(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    Object savedState = comp.processSaveState(getFacesContext());

    if (!(savedState instanceof Serializable)) {
      out.println(JSFTestUtil.FAIL + " Expected the Object returned by "
          + "processSaveState() to be Serializable.");
      return;
    }

    // validate the processSaveState was recursively called.
    if (!"/PSSchild1/SSchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PSSchild1/SSchild1");
      return;
    }

    if (!"/PSSchild1_1/SSchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PSSchild1_1/SSchild1_1");
      return;
    }
    if (!"/PSSfacet1/SSfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PSSfacet1/SSfacet1");
      return;
    }

    if (!"/PSSfacet1_1/SSfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PSSfacet1_1/SSfacet1_1");
      return;
    }

    child1.resetTraceLog();
    child1_1.resetTraceLog();
    facet1.resetTraceLog();
    facet1_1.resetTraceLog();

    // Check processRestoreState()
    comp.processRestoreState(getFacesContext(), savedState);
    // validate the processSaveState was recursively called.
    if (!"/PRSchild1/RSchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PRSchild1/RSchild1");
      return;
    }

    if (!"/PRSchild1_1/RSchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PRSchild1_1/RSchild1_1");
      return;
    }
    if (!"/PRSfacet1/RSfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PRSfacet1/RSfacet1");
      return;
    }

    if (!"/PRSfacet1_1/RSfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PRSfacet1_1/RSfacet1_1");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processSaveState(null) throws NullPointerException
  public void uiComponentProcessSaveStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();

    try {
      comp.processSaveState(null);
      out.println("Test FAILED  No Exception Thrown!");
    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println("Test FAILED" + JSFTestUtil.NL
          + "Expected: NullPointerException when context is null!"
          + JSFTestUtil.NL + "Received: " + JSFTestUtil.NL + e.toString());
    }

  }

  // UIComponent.processRestoreState(null) throws NullPointerException
  public void uiComponentProcessRestoreStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    Object savedState = comp.processSaveState(getFacesContext());

    try {
      comp.processRestoreState(null, savedState);
      out.println("Test FAILED  No Exception Thrown!");
    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println("Test FAILED" + JSFTestUtil.NL
          + "Expected: NullPointerException when context is null!"
          + JSFTestUtil.NL + "Received: " + JSFTestUtil.NL + e.toString());
    }

  }

  // UIComponent.processSaveState() skips transient components.
  public void uiComponentProcessSaveStateTransientTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setTransient(false);
    TCKComponent child1 = new TCKComponent("child1");
    child1.setTransient(false);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setTransient(true);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setTransient(false);
    TCKComponent facet1_1 = new TCKComponent("facet1_1");
    facet1_1.setTransient(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    // facet1_1 and child1_1 are marked transient.
    // child1_1 should have no trace for processSaveState() or
    // saveState(). Because facet1_1 is a facet of
    // child1_1 there should be no call to processSaveState() or
    // saveState().
    comp.processSaveState(getFacesContext());

    // validate the processSaveState was recursively called.
    if (!"/PSSchild1/SSchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PSSchild1/SSchild1");
      return;
    }

    if (!"".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected no trace.");
      return;
    }
    if (!"/PSSfacet1/SSfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PSSfacet1/SSfacet1");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processUpdates test
  public void uiComponentProcessUpdatesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);
    renderKit.addRenderer("TCK", "TCK", renderer);

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(false);
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(true);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(true);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);
    comp.getChildren().add(child2);

    comp.processUpdates(getFacesContext());

    // child1 and facet1_1 are instances of UIInput.
    // These should have updateModel called on them
    // during process updates. The other non-UIInput
    // components will not call updateModel.
    // Child2 shoudln't have UM in the trace as it's parent
    // renders children but isn't rendered itself.
    if (!"/PUchild1/UMchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PUchild1/UMchild1");
      return;
    }

    if (!"/PUchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PUchild1_1.");
      return;
    }
    if (!"/PUfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PUfacet1");
      return;
    }

    if (!"/PUfacet1_1/UMfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PUfacet1_1/UMfacet1_1");
      return;
    }

    if (!"/PUchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PUchild2");
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processUpdates() will not continue if a component's
  // rendered property is false.
  public void uiComponentProcessUpdatesNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(false);
    TCKInputComponent facet1 = new TCKInputComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    comp.processUpdates(getFacesContext());

    // child1_1 and facet1_1 have their rendered properties set to false.
    // This means that child1 will have processUpdates called against it,
    // but it will not call processUpdates() on facet1_1.
    if (!"/PUchild1/UMchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PUchild1/UMchild1");
      return;
    }

    if (!"/PUchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PUchild1_1.");
      return;
    }
    if (!"/PUfacet1/UMfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PUfacet1/UMfacet1");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace to be generated.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processUpdates() - if updateModel throws RuntimeException
  // call FacesContext.renderResponse()
  public void uiComponentProcessUpdatesRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    TCKInputComponent tckInput = new TCKInputComponent("in1", true);
    comp.getChildren().add(tckInput);

    try {
      comp.processUpdates(getFacesContext());
    } catch (Exception re) {
      if (!"UpdateModelRTE".equals(re.getMessage())) {
        out.println(JSFTestUtil.FAIL + " RuntimeException not rethrown up to"
            + " the calling component.");
        return;
      }
    }

    if (!getFacesContext().getRenderResponse()) {
      out.println(
          JSFTestUtil.FAIL + " RuntimeException thrown during updateModule()"
              + " but FacesContext.renderResponse() was not called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processUpdates(FacesContext) throws NullPointerException
  public void uiComponentProcessUpdatesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    TCKInputComponent tckInput = new TCKInputComponent("in1", true);
    comp.getChildren().add(tckInput);

    try {
      comp.processUpdates(null);
      out.println("Test FAILED  No Exception Thrown!");
    } catch (NullPointerException npe) {
      out.println("Test PASSED  Expected NullPointerException Thrown!");
    } catch (Exception e) {
      out.println("Test FAILED" + JSFTestUtil.NL
          + "Expected: NullPointerException when context is null!"
          + JSFTestUtil.NL + "Received: " + JSFTestUtil.NL + e.toString());
    }

  }

  // UIComponent.processValidators test
  public void uiComponentProcessValidatorsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);
    renderKit.addRenderer("TCK", "TCK", renderer);

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(false);
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(true);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(true);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);
    comp.getChildren().add(child2);

    comp.processValidators(getFacesContext());

    // child1 and facet1_1 are instances of UIInput.
    // These should have validate() called on them
    // during process validations. The other non-UIInput
    // components will not call validate()..
    // Child2 shoudln't have V in the trace as it's parent
    // renders children but isn't rendered itself.
    if (!"/PVchild1/Vchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PVchild1/Vchild1");
      return;
    }

    if (!"/PVchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PVchild1_1.");
      return;
    }
    if (!"/PVfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PVfacet1");
      return;
    }

    if (!"/PVfacet1_1/Vfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PVfacet1_1/Vfacet1_1");
      return;
    }

    if (!"/PVchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PVchild2");
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processValidators() throws NullPointerException
  public void uiComponentProcessValidatorsNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "processValidators",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, out);
  }

  // UIComponent.processValidators() will not continue if a component's
  // rendered property is false.
  public void uiComponentProcessValidatorsNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(false);
    TCKInputComponent facet1 = new TCKInputComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    comp.processValidators(getFacesContext());

    // child1_1 and facet1_1 have their rendered properties set to false.
    // This means that child1 will have processValidators called against it,
    // but it will not call processValidators() on facet1_1.
    if (!"/PVchild1/Vchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PVchild1/Vchild1");
      return;
    }

    if (!"/PVchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PVchild1_1.");
      return;
    }
    if (!"/PVfacet1/Vfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PVfacet1/Vfacet1");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace to be generated.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processValidators() - if validate() throws RuntimeException
  // call FacesContext.renderResponse()
  public void uiComponentProcessValidatorsRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    TCKInputComponent tckInput = new TCKInputComponent("in1", true);
    comp.getChildren().add(tckInput);

    try {
      comp.processValidators(getFacesContext());
    } catch (Exception re) {
      if (!"ValidateRTE".equals(re.getMessage())) {
        out.println(JSFTestUtil.FAIL + " RuntimeException not rethrown up to"
            + " the calling component.");
        return;
      }
    }

    if (!getFacesContext().getRenderResponse()) {
      out.println(
          JSFTestUtil.FAIL + " RuntimeException thrown during validate()"
              + " but FacesContext.renderResponse() was not called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processValidators() - if isValid() returns false
  // call FacesContext.renderResponse()
  public void uiComponentProcessValidatorsIsValidRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    TCKInputComponent tckInput = new TCKInputComponent("in1");
    tckInput.setInvalidOnValidate(true);
    comp.getChildren().add(tckInput);

    comp.processValidators(getFacesContext());

    if (!getFacesContext().getRenderResponse()) {
      out.println(JSFTestUtil.FAIL + " isValid() returned false "
          + " but FacesContext.renderResponse() was not called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processDecodes()
  public void uiComponentProcessDecodesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Setup a specialized RenderKit
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    TCKRenderer renderer = new TCKRenderer();
    renderer.setRendersChildren(true);
    renderKit.addRenderer("TCK", "TCK", renderer);

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    TCKComponent child2 = new TCKComponent("child2");
    child2.setRendererType("TCK");
    child2.setRendered(false);
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(true);
    TCKComponent facet1 = new TCKComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(true);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);
    comp.getChildren().add(child2);

    comp.processDecodes(getFacesContext());

    // Child2 shoudln't have D in the trace as it's parent
    // renders children but isn't rendered itself.
    if (!"/PDchild1/Dchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PDchild1/Dchild1");
      return;
    }

    if (!"/PDchild1_1/Dchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PDchild1_1/Dchild1_1");
      return;
    }
    if (!"/PDfacet1/Dfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PDfacet1/Dfacet1");
      return;
    }

    if (!"/PDfacet1_1/Dfacet1_1".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PDfacet1_1/Dfacet1_1");
      return;
    }

    if (!"/PDchild2".equals(child2.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected trace: /PDchild2");
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processDecodes() throws NullPointerException
  public void uiComponentProcessDecodesNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "processDecodes",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, out);
  }

  public void uiComponentPushComponentToELTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent compOne = createComponent();
    UIComponent compTwo = createComponent();
    compOne.setId("compOne");
    compTwo.setId("compTwo");
    String oneType = compOne.getClass().getSimpleName();
    String twoType = compTwo.getClass().getSimpleName();

    /*
     * Test that a call to push puts this UIComponent as the current component.
     */
    compOne.encodeBegin(context);
    String recId = UIComponent.getCurrentComponent(context).getId();

    if (!"compOne".equals(recId)) {
      out.println("Test FAILED. " + oneType + ".getCurrentComponent() "
          + "returned unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected ID: compOne" + JSFTestUtil.NL + "Receieved ID: " + recId);
      return;
    }

    /*
     * Add another Component to the stack and retest.
     */
    compTwo.encodeBegin(context);
    String recId2 = UIComponent.getCurrentComponent(context).getId();

    if (!"compTwo".equals(recId2)) {
      out.println("Test FAILED. " + twoType + ".getCurrentComponent() "
          + "returned unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected ID: compTwo" + JSFTestUtil.NL + "Receieved ID: "
          + recId2);
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // UIComponent.pushComponentToEL() throws NullPointerException
  public void uiComponentPushComponentToELNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "pushComponentToEL",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { null, createComponent() }, out);
  }

  public void uiComponentPopComponentFromELTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    String recId;

    UIComponent compOne = createComponent();
    UIComponent compTwo = createComponent();
    compOne.setId("compOne");
    compTwo.setId("compTwo");
    String oneType = compOne.getClass().getSimpleName();
    String twoType = compTwo.getClass().getSimpleName();

    /*
     * Push Two UIComponents onto the EL Data Structure.
     */
    compOne.encodeBegin(context);
    compTwo.encodeBegin(context);

    /*
     * Make sre that the current Component is compTwo.
     */
    recId = UIComponent.getCurrentComponent(context).getId();

    if (!"compTwo".equals(recId)) {
      out.println("Test FAILED. " + twoType + ".getCurrentComponent() "
          + "returned unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected ID: CompTwo" + JSFTestUtil.NL + "Receieved ID: " + recId);
      return;
    }

    /*
     * Call Pop to make the current component is set to compOne & not compTwo.
     */
    compTwo.encodeEnd(context);
    recId = UIComponent.getCurrentComponent(context).getId();

    if (!"compOne".equals(recId)) {
      out.println("Test FAILED. " + oneType + ".getCurrentComponent() "
          + "returned unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected ID: compOne" + JSFTestUtil.NL + "Receieved ID: " + recId);
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  public void uiComponentPopComponentFromELNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent compTwo = createComponent();

    compTwo.setId("compTwo");

    try {
      compTwo.encodeEnd(null);
      out.println("Test FAILED  No Exception Thrown!");
    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println("Test FAILED  Unexpected Excetion Thrown!" + JSFTestUtil.NL
          + "Expected: NullPointerException when FaceContext is null"
          + JSFTestUtil.NL + "Received: " + JSFTestUtil.NL + e.toString());
    }

  }

  // UIComponent.processDecodes() -- decode() not called if component is
  // not rendered
  public void uiComponentProcessDecodesNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // build a component tree.
    UIComponent comp = createComponent();
    comp.setRendered(true);
    TCKInputComponent child1 = new TCKInputComponent("child1");
    child1.setRendered(true);
    TCKComponent child1_1 = new TCKComponent("child1_1");
    child1_1.setRendered(false);
    TCKInputComponent facet1 = new TCKInputComponent("facet1");
    facet1.setRendered(true);
    TCKInputComponent facet1_1 = new TCKInputComponent("facet1_1");
    facet1_1.setRendered(false);

    child1_1.getFacets().put("facet1_1", facet1_1);
    child1.getFacets().put("facet1", facet1);
    child1.getChildren().add(child1_1);
    comp.getChildren().add(child1);

    comp.processDecodes(getFacesContext());

    // child1_1 and facet1_1 have their rendered properties set to false.
    // This means that child1 will have processDecodes called against it,
    // but it will not call decode() on facet1_1.
    if (!"/PDchild1/Dchild1".equals(child1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1.getTrace());
      out.println("Expected trace: /PDchild1/Dchild1");
      return;
    }

    if (!"/PDchild1_1".equals(child1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + child1_1.getTrace());
      out.println("Expected trace: /PDchild1_1.");
      return;
    }
    if (!"/PDfacet1/Dfacet1".equals(facet1.getTrace())) {
      out.println("Test FAILED.");
      out.println("Component trace: " + facet1.getTrace());
      out.println("Expected trace: /PDfacet1/Dfacet1");
      return;
    }

    if (!"".equals(facet1_1.getTrace())) {
      out.println("Test FAILED");
      out.println("Component trace: " + facet1_1.getTrace());
      out.println("Expected no trace to be generated.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.processDecodes() - if RuntimeException thrown by decode()
  // then FacesContext.renderResponse must be called
  public void uiComponentProcessDecodesRenderResponseTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent comp = createComponent();
    TCKInputComponent tckInput = new TCKInputComponent("in1", true);
    comp.getChildren().add(tckInput);

    try {
      comp.processDecodes(getFacesContext());
    } catch (Exception re) {
      if (!"DecodeRTE".equals(re.getMessage())) {
        out.println(JSFTestUtil.FAIL + " RuntimeException not rethrown up to"
            + " the calling component.");
        out.println("Exception received: " + re.toString());
        out.println("Exception message: " + re.getMessage());
        return;
      }
    }

    if (!getFacesContext().getRenderResponse()) {
      out.println(JSFTestUtil.FAIL + " RuntimeException thrown during decode()"
          + " but FacesContext.renderResponse() was not called.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeBegin()
  public void uiComponentEncodeBeginELTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    String expected = "comp";
    UIComponent component = createComponent();
    component.setId(expected);
    String uicType = component.getClass().getSimpleName();

    component.encodeBegin(context);

    UIComponent mc = UIComponent.getCurrentComponent(context);
    String recId = mc.getId();

    if (!expected.equals(recId)) {
      out.println("Test FAILED. " + uicType + ".encodeBegin() must " + "call "
          + uicType + ".pushComponentToEL()!" + JSFTestUtil.NL
          + "Unexpected UIComponent ID." + JSFTestUtil.NL + "Expected ID: "
          + expected + JSFTestUtil.NL + "Receieved ID: " + recId);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeBegin()
  public void uiComponentEncodeBeginTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(true);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    TCKRenderer renderer = new TCKRenderer();
    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }
    comp.setRendererType(sRendererType);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeBegin() delegates to
    // the underlying renderer
    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeBegin(getFacesContext());

    String trace = renderer.getTrace();

    if (!"/EB".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " encodeBegin() was not called on "
          + "the renderer of the component under test." + JSFTestUtil.NL
          + "Expected Renderer trace: /EB" + JSFTestUtil.NL + "Actual trace: "
          + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeBegin() - Component should not call through to renderer
  // if the rendered property is false.
  public void uiComponentEncodeBeginNotRenderedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(false);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();

    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    TCKRenderer renderer = new TCKRenderer();
    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }
    comp.setRendererType(sRendererType);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeBegin() does not delegate
    // to
    // the underlying renderer
    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeBegin(getFacesContext());

    String trace = renderer.getTrace();

    if (!"".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " encodeBegin() was called on the renderer"
          + " of the component under test when the rendered property "
          + "was false.");
      out.println("Expected no information returned in trace.");
      out.println("Actual trace: " + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeEnd()
  public void uiComponentEncodeEndELTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String testId = "comp";
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId(testId);
    String uicType = component.getClass().getSimpleName();

    // Put the current UIComponent on the EL Stack.
    component.encodeBegin(context);

    if (!testId.equals(component.getId())) {
      out.println("Test FAILED.");
      out.println(
          "Attempt to setup test with call to " + uicType + ".encodeBegin()");
      return;
    }

    /*
     * Remove the current UIComponent from the EL Stack. Stack should have no
     * UIComponent on it. A null should be returned.
     */
    component.encodeEnd(context);
    if (null != UIComponent.getCurrentComponent(context)) {
      out.println("Test FAILED. " + uicType + ".encodeEnd() must call "
          + uicType + ".popComponentFromEL()!" + JSFTestUtil.NL
          + "unexpected UIComponent ID." + JSFTestUtil.NL
          + "Expected component to return null!");
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // UIComponent.encodeAll() throws NullPointerException
  public void uiComponentEncodeAllNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String testId = "comp";
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");

    ResponseWriter rw = new TCKResponseWriter();
    context.setResponseWriter(rw);

    UIComponent component = createComponent();
    component.setId(testId);

    try {
      component.encodeAll(null);
      out.println("Test FAILED. No NullPointerException thrown when "
          + "FacesContext is null");
    } catch (NullPointerException npe) {
      out.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected Exception thrown when "
          + "context is null!" + JSFTestUtil.NL
          + "Expected: NullPointerException" + JSFTestUtil.NL + "Received: "
          + e.getClass().getSimpleName());
    }
  }

  // UIComponent.encodeEnd()
  public void uiComponentEncodeEndTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(true);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();

    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    TCKRenderer renderer = new TCKRenderer();
    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }
    comp.setRendererType(sRendererType);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeEnd() delegates to
    // the underlying renderer
    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeEnd(getFacesContext());

    String trace = renderer.getTrace();

    if (!"/EE".equals(trace)) {
      out.println(
          JSFTestUtil.FAIL + " encodeEnd() was not called on the renderer"
              + " of the component under test.");
      out.println("Expected Renderer trace: /EE");
      out.println("Actual trace: " + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeEnd -- Renderer.encodeEnd not called if component's
  // render property is false
  public void uiComponentEncodeEndNotRenderedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(false);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();

    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }

    TCKRenderer renderer = new TCKRenderer();
    comp.setRendererType(sRendererType);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeEnd() does not delegate to
    // the underlying renderer
    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeEnd(getFacesContext());

    String trace = renderer.getTrace();

    if (!"".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " encodeEnd() was called on the renderer"
          + " of the component under test when the rendered property "
          + "was false.");
      out.println("Expected no information returned in trace.");
      out.println("Actual trace: " + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeChildren()
  public void uiComponentEncodeChildrenTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(true);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();
    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    TCKRenderer renderer = new TCKRenderer();
    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }
    comp.setRendererType(sRendererType);
    renderer.setRendersChildren(true);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeChildren() delegates to
    // the underlying renderer.

    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeChildren(getFacesContext());

    String trace = renderer.getTrace();

    if (!"/EC".equals(trace)) {
      out.println(JSFTestUtil.FAIL + " encodeChildren() was not called on the "
          + "renderer of the component under test.");
      out.println("Expected Renderer trace: /EC");
      out.println("Actual trace: " + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeChildren() -- component will not delegate to renderer
  // when render property is false
  public void uiComponentEncodeChildrenNotRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    comp.setRendered(false);
    String sRendererType = comp.getRendererType();
    String sRendererFamily = comp.getFamily();

    RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);

    Renderer origRenderer = null;

    if (sRendererType != null) {
      origRenderer = renderKit.getRenderer(sRendererFamily, sRendererType);
    }

    TCKRenderer renderer = new TCKRenderer();
    if (sRendererType == null) {
      sRendererType = "TCKTemp";
    }
    comp.setRendererType(sRendererType);
    renderer.setRendersChildren(true);
    renderKit.addRenderer(sRendererFamily, sRendererType, renderer);

    // ensure that the call to UIComponent.encodeChildren() does not
    // delegate to the underlying renderer
    // NOTE: THIS TEST MUST ONLY BE CALLED FOR COMPONENTS THAT HAVE
    // RENDERERS
    comp.encodeChildren(getFacesContext());

    String trace = renderer.getTrace();

    if (!"".equals(trace)) {
      out.println(
          JSFTestUtil.FAIL + " encodeChildren() was called on the renderer"
              + " of the component under test when the rendered property "
              + "was false.");
      out.println("Expected no information returned in trace.");
      out.println("Actual trace: " + trace);
      return;
    }

    if ("TCKTemp".equals(sRendererType)) {
      comp.setRendererType(null);
    }

    if (origRenderer != null) {
      renderKit.addRenderer(sRendererFamily, sRendererType, origRenderer);
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeBegin() - throws NPE if context arg is null
  public void uiComponentEncodeBeginNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      createComponent().encodeBegin(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when encodeBegin() "
          + "was passed a null value for the context argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Exception thrown when encodeBegin() was passed a "
            + "null value for the context argument, but it wasn't"
            + " an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeEnd() - throws NPE if context arg is null
  public void uiComponentEncodeEndNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      createComponent().encodeEnd(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when encodeEnd() "
          + "was passed a null value for the context argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Exception thrown when encodeEnd() was passed a "
            + "null value for the context argument, but it wasn't"
            + " an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // UIComponent.encodeChildren() - throws NPE if context arg is null
  public void uiComponentEncodeChildrenNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      createComponent().encodeChildren(null);
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when encodeChidlren() "
              + "was passed a null value for the context argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println("Exception thrown when encodeChidlren() was passed a "
            + "null value for the context argument, but it wasn't"
            + " an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void componentAttributesTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String[] attrNames = getAttributeNames();
    UIComponent component = createComponent();
    out.println("COMPONENT: " + component.getClass().getName());
    boolean passed = true;
    for (int i = 0; i < attrNames.length; i++) {
      out.println("Processing attribute: " + attrNames[i]);
      PropertyDescriptor[] descriptors = null;
      try {
        descriptors = Introspector.getBeanInfo(component.getClass())
            .getPropertyDescriptors();
      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + " Unable to obtain PropertyDescriptors"
            + " for component: " + component.getClass().getName());
        passed = false;
      }

      boolean found = false;
      for (int ii = 0; ii < descriptors.length; ii++) {
        if (!descriptors[ii].getName().equals(attrNames[i])) {
          continue;
        }
        found = true;
        Object[] val;
        if (descriptors[ii].getPropertyType() == String.class) {
          val = new Object[] { "newValue" };
        } else if (descriptors[ii].getPropertyType() == Boolean.TYPE) {
          val = new Object[] { Boolean.TRUE };
        } else {
          val = new Object[] { Integer.valueOf(1) };
        }

        Method writeMethod = descriptors[ii].getWriteMethod();
        try {
          writeMethod.invoke(component, val);
        } catch (Exception e) {
          Throwable t;
          if (e instanceof InvocationTargetException) {
            t = ((InvocationTargetException) e).getTargetException();
          } else {
            t = e;
          }
          out.println(JSFTestUtil.FAIL + " Exception invoking write method "
              + " for attribute '" + attrNames[i] + "': " + t);
          passed = false;
        }

        Method readMethod = descriptors[ii].getReadMethod();

        try {
          Object result = readMethod.invoke(component, null);
          if (descriptors[ii].getPropertyType() == String.class) {
            if (!"newValue".equals(result)) {
              out.println(JSFTestUtil.FAIL + " Unexpected  value returned"
                  + " for property '" + attrNames[i] + "'.");
              out.println("Expected: newValue");
              out.println("Received: " + result);
            }
          } else if (descriptors[ii].getPropertyType() == Boolean.TYPE) {
            if (!Boolean.TRUE.equals(result)) {
              out.println(JSFTestUtil.FAIL + " Unexpected  value returned"
                  + " for property '" + attrNames[i] + "'.");
              out.println("Expected: " + Boolean.TRUE);
              out.println("Received: " + result);
            }
          } else if (descriptors[ii].getPropertyType() == Integer.TYPE) {
            if (!(Integer.valueOf(1).equals(result))) {
              out.println(JSFTestUtil.FAIL + " Unexpected  value returned"
                  + " for property '" + attrNames[i] + "'.");
              out.println("Expected: 1");
              out.println("Received: " + result);
            }
          } else {
            out.println(JSFTestUtil.FAIL + " Unexpected type for "
                + "attribute '" + attrNames[i] + "'");
          }
        } catch (IllegalArgumentException iae) {
          // TODO Auto-generated catch block
          iae.printStackTrace();
        } catch (IllegalAccessException iae) {
          out.println("Test FAILED.");
          iae.printStackTrace();
        } catch (InvocationTargetException ite) {
          out.println(JSFTestUtil.FAIL + " Exception invoking read method  for "
              + "attribute '" + attrNames[i] + "': " + ite.getTargetException()
              + JSFTestUtil.NL);
          ite.printStackTrace();
        }

        break;
      }

      if (!found) {
        out.println(
            JSFTestUtil.FAIL + " Unable to locate attribute '" + attrNames[i]
                + "' in class '" + component.getClass().getName() + "'");
        passed = false;
      }
      found = false;
    }

    if (passed) {
      out.println(JSFTestUtil.PASS);
    }
  }

  public void uiComponentGetSetValueExpressionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    request.setAttribute("bean", new SimpleBean());
    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{bean.action}",
        java.lang.String.class);

    UIComponent component = createComponent();

    // calling setValueExpression() with a non-literal ValueExpression
    // should result in getValueExpression() returning the ValueExpression
    // and no value being set in the components attribute map.
    component.setValueExpression("bean", expression);

    if (component.getValueExpression("bean") != expression) {
      out.println(JSFTestUtil.FAIL + " Expected getValueExpression() to return"
          + " the non-literal ValueExpression set via "
          + "setValueExpression().");
      out.println("Expected: " + expression);
      out.println("Received: " + component.getValueExpression("bean"));
      return;
    }

    ValueExpression literalExpr = factory.createValueExpression(
        getFacesContext().getELContext(), "literalValue",
        java.lang.String.class);

    // Literal expression - The evaluated value should be stored in the
    // component attribute map, and should not be returned by
    // getValueExpression()

    component.setValueExpression("literal", literalExpr);

    if (component.getValueExpression("literal") != null) {
      out.println(JSFTestUtil.FAIL + " Expected getValueExpression() to return"
          + " null when setValueExpression() was called with a "
          + "literal ValueExpression (isLiteralText() returns true).");
      return;
    }

    if (!"literalValue".equals(component.getAttributes().get("literal"))) {
      out.println(JSFTestUtil.FAIL + " Expected literal ValueExpression to be"
          + "evaluated and stored in the component's attribute map"
          + " under the key 'literal'.");
      out.println("Expected value: literalValue");
      out.println("Received: " + component.getAttributes().get("literal"));
      return;
    }

    // If null is passed as a ValueExpression for an existing key,
    // the ValueExpression should be removed from internal map.
    // getValueExpression() should then return null.

    component.setValueExpression("bean", null);

    if (component.getValueExpression("bean") != null) {
      out.println(JSFTestUtil.FAIL + " setValueExpression() did not remove"
          + " the ValueExpression associated with a particular key from"
          + " its internal Map when passing a null value for the "
          + "ValueExpression argument.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiComponentGetSetValueExpressionNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    StringBuffer buf = new StringBuffer(128);

    UIComponent component = createComponent();

    // getValueExpression throws NPE
    try {
      component.getValueExpression(null);
      buf.append(JSFTestUtil.FAIL + " No exception thrown when passin null"
          + " to getValueExpression().");
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append(JSFTestUtil.FAIL + " Exception thrown when passing null"
            + " to getValueExpression(), but it wasn't an instance"
            + " of NullPointerException." + JSFTestUtil.NL
            + "Exception received: " + e.getClass().getName());
      }
    }

    // setValueExpression() throw NPE
    try {
      ExpressionFactory factory = JSFTestUtil
          .getExpressionFactory(servletContext);

      ValueExpression literalExpr = factory.createValueExpression(
          getFacesContext().getELContext(), "literalValue",
          java.lang.String.class);

      component.setValueExpression(null, literalExpr);
      buf.append(JSFTestUtil.FAIL + " No exception thrown when passin null"
          + " to setValueExpression().");
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        buf.append(JSFTestUtil.FAIL + " Exception thrown when passing null"
            + " to setValueExpression(), but it wasn't an instance"
            + " of NullPointerException." + JSFTestUtil.NL
            + "Exception received: " + e.getClass().getName());
        return;
      }
    }

    if (buf.length() > 0) {
      out.println("Test FAILED" + JSFTestUtil.NL + buf.toString());
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }

  public void uiComponentSetValueExpressionNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    UIComponent component = createComponent();
    request.setAttribute("bean", new SimpleBean());
    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{bean.action}",
        java.lang.String.class);

    try {
      component.setValueExpression(null, expression);
      out.println(JSFTestUtil.FAIL + " No exception thrown when passin null"
          + " as the 'name' argument of setValueExpression().");
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when passing null"
            + " as the 'name' argument to setValueExpression(), but "
            + "it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiComponentSetValueExpressionIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent component = createComponent();
    request.setAttribute("bean", new SimpleBean());
    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{bean.action}",
        java.lang.String.class);

    // if 'id' or 'parent' are the values for the name argument,
    // then an IAE must be thrown
    try {
      component.setValueExpression("id", expression);
      out.println(JSFTestUtil.FAIL + " No exception thrown when passin 'id'"
          + " as the 'name' argument of setValueExpression().");
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when passing 'id'"
            + " as the 'name' argument to setValueExpression(), but "
            + "it wasn't an instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      component.setValueExpression("parent", expression);
      out.println(JSFTestUtil.FAIL + " No exception thrown when passin 'parent'"
          + " as the 'name' argument of setValueExpression().");
    } catch (Exception e) {
      if (!(e instanceof IllegalArgumentException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when passing 'parent'"
            + " as the 'name' argument to setValueExpression(), but "
            + "it wasn't an instance of IllegalArgumentException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiComponentVisitTreeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    UIComponent component = createComponent();
    String cName = component.getClass().getName();

    if (!component.visitTree(new TCKVisitContext(), new CompleteCallback())) {
      out.println(JSFTestUtil.FAIL + " expected " + cName + ".visitTree() "
          + "to return true.");
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  public void uiComponentVisitTreeNegTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    UIComponent component = createComponent();
    String cName = component.getClass().getName();

    if (component.visitTree(new TCKVisitContext(), new RejectCallback())) {
      out.println(JSFTestUtil.FAIL + " expected " + cName + ".visitTree() "
          + "to return false.");
    } else {
      out.println(JSFTestUtil.PASS);
    }
  }

  // UIComponent.decode(FacesContext) throws NullPointerException
  public void uiComponentDecodeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(), "decode",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, out);
  }

  // UIComponent.getContainerClientId(FacesContext) throws NullPointerException
  public void uiComponentGetContainerClientIdNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createComponent().getClass(),
        "getContainerClientId", new Class<?>[] { FacesContext.class },
        new Object[] { null }, out);
  }

  // UIComponent.getContainerClientId(FacesContext)
  public void uiComponentGetContainerClientIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewRoot root = context.getViewRoot();
    root.setViewId("/test");
    root.setId("root");
    UIComponent comp = createComponent();
    root.getChildren().add(comp);

    // Not much can be done here aside from ensuring the result is
    // not null.
    if (comp.getContainerClientId(context) == null) {
      out.println(JSFTestUtil.FAIL + " getContainerClientId() returned null.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------- protected Classes

  protected static class TCKRenderer extends Renderer {

    private StringBuffer traceLog;

    private boolean rendersChildren = false;

    private Renderer wrapped;

    /**
     * <p>
     * Creates a new <code>TCKRenderer</code> instance that calls into the
     * {@link Renderer} parent class.
     * </p>
     */
    public TCKRenderer() {
      traceLog = new StringBuffer();
    }

    /**
     * <p>
     * Creates a new <code>TCKRenderer</code> that wraps the provided
     * {@link Renderer}. All calls will be delegated to the wrapped instance.
     * </p>
     * 
     * @param wrapped
     *          the {@link Renderer} to delegate calls to
     */
    public TCKRenderer(Renderer wrapped) {
      this();
      this.wrapped = wrapped;
    }

    /**
     * <p>
     * Render the ending of the current state of the specified
     * {@link UIComponent}, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value to be
     * rendered.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the response we are creating
     * @param component
     *          {@link UIComponent} to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EE");
      if (wrapped != null) {
        wrapped.encodeEnd(context, component);
      } else {
        super.encodeEnd(context, component);
      }
    }

    /**
     * <p>
     * Render the child components of this {@link UIComponent}, following the
     * rules described for <code>encodeBegin()</code> to acquire the appropriate
     * value to be rendered. This method will only be called if the
     * <code>rendersChildren</code> property of this component is
     * <code>true</code>.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the response we are creating
     * @param component
     *          {@link UIComponent} whose children are to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EC");
      if (wrapped != null) {
        wrapped.encodeChildren(context, component);
      } else {
        super.encodeChildren(context, component);
      }
    }

    /**
     * <p>
     * Convert the component generated client id to a form suitable for
     * transmission to the client.
     * </p>
     * <p/>
     * <p>
     * The default implementation returns the argument <code>clientId</code>
     * unchanged.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the current request
     * @param clientId
     *          the client identifier to be converted to client a specific
     *          format.
     * @throws NullPointerException
     *           if <code>context</code> or <code>clientId</code> is
     *           <code>null</code>
     */
    public String convertClientId(FacesContext context, String clientId) {
      if (context == null || clientId == null) {
        throw new NullPointerException();
      }
      if (wrapped != null) {
        return wrapped.convertClientId(context, clientId);
      } else {
        return super.convertClientId(context, clientId);
      }
    }

    /**
     * <p>
     * Return a flag indicating whether this renderer is responsible for
     * rendering the children the component it is asked to render. The default
     * implementation returns <code>false</code>.
     * </p>
     */
    public boolean getRendersChildren() {
      if (wrapped != null) {
        return wrapped.getRendersChildren();
      }
      return rendersChildren;
    }

    /**
     * <p>
     * Decode the current state of the specified {@link UIComponent} from the
     * request contained in the specified {@link FacesContext}, and attempt to
     * convert this state information into an object of the type required for
     * this component (optionally using the registered {@link Converter} for
     * this component, if there is one).
     * </p>
     * <p/>
     * <p>
     * If conversion is successful:
     * </p>
     * <ul>
     * <li>Save the new local value of this component by calling
     * <code>setValue()</code> and passing the new value.</li>
     * <li>Set the <code>value</code> property of this component to
     * <code>true</code>.</li>
     * </ul>
     * <p/>
     * <p>
     * If conversion is not successful:
     * </p>
     * <ul>
     * <li>Save the state information (inside the component) in such a way that
     * encoding can reproduce the previous input (even though it was
     * syntactically or semantically incorrect).</li>
     * <li>Add an appropriate conversion failure error message by calling
     * <code>addMessage()</code> on the specified {@link FacesContext}.</li>
     * <li>Set the <code>valid</code> property of this component to
     * <code>false</code>.</li>
     * </ul>
     * <p/>
     * <p>
     * During decoding, events may be enqueued for later processing (by event
     * listeners that have registered an interest), by calling
     * <code>queueEvent()</code> on the associated {@link UIComponent}.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the request we are processing
     * @param component
     *          {@link UIComponent} to be decoded.
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is
     *           <code>null</code>
     */
    public void decode(FacesContext context, UIComponent component) {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/DC");
      if (wrapped != null) {
        wrapped.decode(context, component);
      } else {
        super.decode(context, component);
      }
    }

    /**
     * <p>
     * Render the beginning specified {@link UIComponent} to the output stream
     * or writer associated with the response we are creating. If the conversion
     * attempted in a previous call to <code>decode</code> for this component
     * failed, the state information saved during execution of
     * <code>decode()</code> should be utilized to reproduce the incorrect
     * input. If the conversion was successful, or if there was no previous call
     * to <code>decode()</code>, the value to be displayed should be acquired by
     * calling <code>component.currentValue()</code>, and rendering the value as
     * appropriate.
     * </p>
     * 
     * @param context
     *          {@link FacesContext} for the request we are processing
     * @param component
     *          {@link UIComponent} to be rendered
     * @throws IOException
     *           if an input/output error occurs while rendering
     * @throws NullPointerException
     *           if <code>context</code> or <code>component</code> is null
     */
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

      if (context == null || component == null) {
        throw new NullPointerException();
      }
      traceLog.append("/EB");
      if (wrapped != null) {
        wrapped.encodeBegin(context, component);
      } else {
        super.encodeBegin(context, component);
      }
    }

    /**
     * <p>
     * Returns the content of the current trace log.
     * </p>
     */
    public String getTrace() {
      return traceLog.toString();
    }

    /**
     * <p>
     * Sets the return value of {@link #getRendersChildren()}.
     * </p>
     */
    public void setRendersChildren(boolean rendersChildren) {
      this.rendersChildren = rendersChildren;
    }
  }

  protected static class TCKComponent extends UIComponentBase {

    private StringBuffer traceLog;

    private String id;

    public TCKComponent(String id) {
      super();
      traceLog = new StringBuffer();
      this.id = id;
      this.setId(id);
    }

    public String getFamily() {
      return "";
    }

    public void processDecodes(FacesContext context) {
      trace("/PD" + id);
      super.processDecodes(context);
    }

    public void processRestoreState(FacesContext context, Object state) {
      trace("/PRS" + id);
      super.processRestoreState(context, state);
    }

    public Object processSaveState(FacesContext context) {
      trace("/PSS" + id);
      return super.processSaveState(context);
    }

    public void processUpdates(FacesContext context) {
      trace("/PU" + id);
      super.processUpdates(context);
    }

    public void processValidators(FacesContext context) {
      trace("/PV" + id);
      super.processValidators(context);
    }

    public Object saveState(FacesContext context) {
      trace("/SS" + id);
      return super.saveState(context);
    }

    public void restoreState(FacesContext context, Object state) {
      trace("/RS" + id);
      super.restoreState(context, state);
    }

    public void decode(FacesContext context) {
      trace("/D" + id);
      super.decode(context);
    }

    public String getTrace() {
      return traceLog.toString();
    }

    public void resetTraceLog() {
      traceLog = new StringBuffer();
    }

    private void trace(String message) {
      traceLog.append(message);
    }
  }

  protected static class TCKInputComponent extends UIInput {

    private StringBuffer traceLog;

    private String id;

    private boolean throwRuntime;

    private boolean isInvalid;

    public TCKInputComponent(String id) {
      super();
      traceLog = new StringBuffer();
      this.id = id;
      this.setId(id);
    }

    public TCKInputComponent(String id, boolean throwRuntime) {
      this(id);
      this.throwRuntime = throwRuntime;
    }

    public void setInvalidOnValidate(boolean isInvalid) {
      this.isInvalid = isInvalid;
    }

    public void processDecodes(FacesContext context) {
      trace("/PD" + id);
      super.processDecodes(context);
    }

    public void processUpdates(FacesContext context) {
      trace("/PU" + id);
      super.processUpdates(context);
    }

    public void updateModel(FacesContext context) {
      trace("/UM" + id);
      if (throwRuntime) {
        throw new RuntimeException("UpdateModelRTE");
      }
      super.updateModel(context);
    }

    public void processValidators(FacesContext context) {
      trace("/PV" + id);
      super.processValidators(context);
    }

    public void validate(FacesContext context) {
      trace("/V" + id);
      if (throwRuntime) {
        throw new RuntimeException("ValidateRTE");
      }
      super.validate(context);
      if (isInvalid) {
        this.setValid(false);
      }
    }

    public void decode(FacesContext context) {
      trace("/D" + id);
      if (throwRuntime) {
        throw new RuntimeException("DecodeRTE");
      }
      super.decode(context);
    }

    public String getTrace() {
      return traceLog.toString();
    }

    public void resetTraceLog() {
      traceLog = new StringBuffer();
    }

    private void trace(String message) {
      traceLog.append(message);
    }

    public void callPushComponent(FacesContext context) {
      pushComponentToEL(context, null);
    }

    public void callPopComponent(FacesContext context) {
      popComponentFromEL(context);
    }
  }

  protected static class SimpleBean {

    public String getAction() {

      return "action";

    }
  } // END SimpleBean

  private static class TCKSystemEvent extends SystemEvent {

    public TCKSystemEvent(UIComponent component) {
      super(component);
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
      return false;
    }

    @Override
    public void processListener(FacesListener listener) {
    }
  }

  private static class TCKComponentSystemEventlistener
      implements ComponentSystemEventListener {

    public void processEvent(ComponentSystemEvent arg0)
        throws AbortProcessingException {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  protected static class TCKResponseWriter extends ResponseWriter {

    @Override
    public String getContentType() {
      return "ContentType";

    }

    @Override
    public String getCharacterEncoding() {
      return "Character Encoding";
    }

    @Override
    public void flush() throws IOException {
      System.out.println("Flushed!");
    }

    @Override
    public void startDocument() throws IOException {
      System.out.println("Start Document");
    }

    @Override
    public void endDocument() throws IOException {
      System.out.println("End Document");
    }

    @Override
    public void startElement(String arg0, UIComponent arg1) throws IOException {
      System.out.println("Start Element: " + arg0);
    }

    @Override
    public void endElement(String arg0) throws IOException {
      System.out.println("End Element: " + arg0);
    }

    @Override
    public void writeAttribute(String arg0, Object arg1, String arg2)
        throws IOException {

      if ((arg0 != null) && (arg1 != null)) {
        System.out.println("Attribute: " + arg0 + " = " + arg1.toString());
      }

    }

    @Override
    public void writeURIAttribute(String arg0, Object arg1, String arg2)
        throws IOException {

      if (arg0 == null) {
        System.out.println(arg0 + ":  arg1 is null.");
      } else {
        System.out.println(arg0 + ": " + arg1.toString());
      }
    }

    @Override
    public void writeComment(Object arg0) throws IOException {
      if (arg0 == null) {
        System.out.println("Comment: arg0 is null.");
      } else {
        System.out.println("Comment: " + arg0.toString());
      }
    }

    @Override
    public void writeText(Object arg0, String arg1) throws IOException {
      if (arg0 == null) {
        System.out.println("arg0 is null");
      } else {
        System.out.println(arg0.toString());
      }
    }

    @Override
    public void writeText(char[] arg0, int arg1, int arg2) throws IOException {

      String text = null;
      for (int i = 0; i != arg0.length; i++) {
        text = "" + arg0[i];
      }

      System.out.println("Write Text: " + text);
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer arg0) {
      return this;
    }

    @Override
    public void write(char[] arg0, int arg1, int arg2) throws IOException {
      String data = null;
      for (int i = 0; i != arg0.length; i++) {
        data = "" + arg0[i];
      }

      System.out.println("Write Text: " + data);
    }

    @Override
    public void close() throws IOException {
      System.out.println("Close!");
    }
  }

  protected static class TCKVisitContext extends VisitContext {

    private Collection<String> subTreeIds;

    private Collection<String> visitIds;

    @Override
    public FacesContext getFacesContext() {
      return FacesContext.getCurrentInstance();
    }

    @Override
    public Collection<String> getIdsToVisit() {
      List<UIComponent> children = this.getFacesContext().getViewRoot()
          .getChildren();
      visitIds = new ArrayList<String>();

      for (UIComponent comp : children) {
        visitIds.add(comp.getClientId());
      }

      return visitIds;
    }

    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {
      subTreeIds = new ArrayList<String>();
      for (UIComponent comp : component.getChildren()) {
        subTreeIds.add(comp.getClientId());
      }

      return subTreeIds;
    }

    @Override
    public VisitResult invokeVisitCallback(UIComponent component,
        VisitCallback callback) {

      return callback.visit(this, component);
    }

    @Override
    public Set<VisitHint> getHints() {
      Set hints = new HashSet();
      hints.add(VisitHint.EXECUTE_LIFECYCLE);

      return hints;
    }
  }

  protected static class RejectCallback implements VisitCallback {

    public VisitResult visit(VisitContext context, UIComponent component) {
      return VisitResult.REJECT;
    }
  }

  protected static class CompleteCallback implements VisitCallback {

    public VisitResult visit(VisitContext context, UIComponent component) {
      return VisitResult.COMPLETE;
    }
  }
}
