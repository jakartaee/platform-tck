/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.websocket.Extension;

/**
 * For comparable purposes, this implementation is either case sensitive, or
 * case insensitive. As the extension parameters appear in the http headers and
 * RFC 2616 says : Field names are case-insensitive, the default is case
 * insensitive.
 */
public class ExtensionImpl implements Extension, Comparable<Extension> {

  protected List<Parameter> list = new ArrayList<Parameter>();

  protected String name;

  protected boolean caseSensitive = false;

  public void caseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  /**
   * An Extension Implementation. For empty parameter list, use null
   * 
   * @param name
   *          Extension name, see {@link Extension#getName()}
   * @param params
   *          Extension Parameter list, see {@link Extension#getParameters()}
   */
  public ExtensionImpl(String name, Parameter... params) {
    super();
    this.name = name;
    addParameters(params);
  }

  public ExtensionImpl(Extension extension) {
    this(extension.getName(),
        extension.getParameters().toArray(new Parameter[0]));
  }

  public void addParameters(Parameter... params) {
    if (params != null)
      for (Parameter param : params)
        list.add(param);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Parameter> getParameters() {
    return list;
  }

  @Override
  public int compareTo(Extension o) {
    int cmp = caseSensitive ? name.compareTo(o.getName())
        : name.compareToIgnoreCase(o.getName());
    if (cmp != 0)
      return cmp;
    cmp = getParameters().size() - o.getParameters().size();
    if (cmp != 0)
      return (int) Math.signum(cmp);

    List<ExtensionParameterImpl> newThisList = getExtensionParameters(this);
    List<ExtensionParameterImpl> newOtherList = getExtensionParameters(o);
    Collections.sort(newThisList);
    Collections.sort(newOtherList);
    Iterator<ExtensionParameterImpl> i = newThisList.iterator();
    Iterator<ExtensionParameterImpl> j = newOtherList.iterator();
    while (i.hasNext() && cmp == 0) {
      ExtensionParameterImpl iItem = i.next();
      ExtensionParameterImpl jItem = j.next();
      cmp = iItem.compareTo(jItem);
    }
    return cmp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Extension)
      return compareTo((Extension) obj) == 0;
    return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder().append(getClass().getSimpleName())
        .append("(name=\"").append(name).append("\", parameters=[");
    for (Parameter p : getExtensionParameters(this))
      sb.append(p.toString()).append(",");
    sb.append("])");
    return sb.toString();
  }

  /**
   * Can pass VI of Extension to have tck impl of Extension.Parameter list
   */
  public List<ExtensionParameterImpl> getExtensionParameters(
      Extension extension) {
    List<ExtensionParameterImpl> params = new ArrayList<ExtensionParameterImpl>();
    for (Parameter item : extension.getParameters())
      params.add(new ExtensionParameterImpl(item, caseSensitive));
    Collections.sort(params);
    return params;
  }

  /**
   * Transform list of VI Extensions to List of TCK impl of Extension
   * 
   * @param extensions
   * @return
   */
  public static List<ExtensionImpl> transformToImpl(
      List<? extends Extension> extensions) {
    List<ExtensionImpl> list = new ArrayList<ExtensionImpl>();
    if (extensions != null)
      for (Extension ex : extensions)
        list.add(new ExtensionImpl(ex));
    // do not sort, the order of extensions is significant
    // (rfc6455#section-9.1)
    return list;
  }

  /**
   * For proper output, make sure the list contains ExtensionImpl items, see
   * {@link #transformToImpl(List)}, otherwise the output depends on overridden
   * {@linkplain #toString()} of given Extensions
   */
  public static String toString(List<? extends Extension> list) {
    StringBuilder sb = new StringBuilder().append("{");
    if (list != null)
      for (Extension ex : list)
        sb.append(ex.toString()).append(";");
    sb.append("}");
    return sb.toString();
  }
}
