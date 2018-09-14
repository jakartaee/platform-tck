/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class TagEntry {

  private static final AttributeEntry[] NO_DEFINED_ATTRIBUTES = {};

  private static final VariableEntry[] NO_DEFINED_VARIABLES = {};

  private static final String DEFAULT_BODY = "JSP";

  private Map attributes;

  private Map variables;

  private String name;

  private String body = DEFAULT_BODY;

  public TagEntry() {
    attributes = new HashMap();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    if (body != null) {
      this.body = body;
    }
  }

  public void addVariable(VariableEntry entry) {
    variables.put(entry.getNameGiven(), entry);
  }

  public VariableEntry getVariable(String name) {
    return (VariableEntry) attributes.get(name);
  }

  public VariableEntry[] getVariables() {
    if (variables.isEmpty()) {
      return NO_DEFINED_VARIABLES;
    } else {
      List list = new ArrayList();
      for (Iterator i = variables.values().iterator(); i.hasNext();) {
        list.add(i.next());
      }
      return (VariableEntry[]) list.toArray(new VariableEntry[list.size()]);
    }
  }

  public void addAttribute(AttributeEntry entry) {
    attributes.put(entry.getName(), entry);
  }

  public AttributeEntry getAttribute(String name) {
    return (AttributeEntry) attributes.get(name);
  }

  public AttributeEntry[] getAttributes() {
    if (attributes.isEmpty()) {
      return NO_DEFINED_ATTRIBUTES;
    } else {
      List list = new ArrayList();
      for (Iterator i = attributes.values().iterator(); i.hasNext();) {
        list.add(i.next());
      }
      return (AttributeEntry[]) list.toArray(new AttributeEntry[list.size()]);
    }
  }
}
