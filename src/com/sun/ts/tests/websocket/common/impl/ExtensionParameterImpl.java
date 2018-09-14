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

import javax.websocket.Extension.Parameter;

/**
 * For comparable purposes, this implementation is either case sensitive, or
 * case insensitive. As the extension parameters appear in the http headers and
 * RFC 2616 says : Field names are case-insensitive, the default is case
 * insensitive.
 */
public class ExtensionParameterImpl
    implements Parameter, Comparable<Parameter> {

  public ExtensionParameterImpl(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  /**
   * @param caseSensitive
   *          states whether compares case sensitively or not
   */
  public ExtensionParameterImpl(String name, String value,
      boolean caseSensitive) {
    this(name, value);
  }

  public ExtensionParameterImpl(Parameter param, boolean caseSensitive) {
    this(param.getName(), param.getValue(), caseSensitive);
  }

  protected String name;

  protected String value;

  protected boolean caseSensitive = false;

  public void caseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public int compareTo(Parameter o) {
    int cmp1 = caseSensitive ? name.compareTo(o.getName())
        : name.compareToIgnoreCase(o.getName());
    int cmp2 = caseSensitive ? value.compareTo(o.getValue())
        : value.compareToIgnoreCase(o.getValue());
    return cmp1 == 0 ? cmp2 : cmp1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Parameter)
      return compareTo((Parameter) obj) == 0;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder().append(getClass().getSimpleName())
        .append("(name=\"").append(name).append("\", value=\"").append(value)
        .append("\")");
    return sb.toString();
  }

}
