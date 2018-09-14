/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.composite.valueholder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "titleCase")
public class TitleConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component,
      String value) {

    if (value == null || value.isEmpty()) {
      return value;
    }

    System.out.println("(asObject)Before String: " + value);

    String old = value.substring(0, 1);
    return value.replaceFirst(old, old.toUpperCase());
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component,
      Object value) {

    if (value == null || value.toString().isEmpty()) {
      return null;
    }

    System.out.println("(asString)Before String: " + value);
    String old = value.toString().substring(0, 1);
    return value.toString().replaceFirst(old, old.toUpperCase());
  }
}
