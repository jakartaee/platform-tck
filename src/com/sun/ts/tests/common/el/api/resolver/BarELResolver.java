/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.el.api.resolver;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;
import java.util.Iterator;

public class BarELResolver extends ELResolver {

  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    Object result = null;
    if (context == null)
      throw new NullPointerException();

    if (base == null) {
      // Resolving first variable (e.g. ${Bar}).
      // We only handle "Bar"
      String propertyName = (String) property;
      if (propertyName.equals("Bar")) {
        result = "Foo";
        context.setPropertyResolved(true);
      }
    }
    return result;
  }

  public Class getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && property instanceof String
        && property.toString().equals("Bar")) {
      context.setPropertyResolved(true);
    }

    // we never set a value
    return null;
  }

  public void setValue(ELContext context, Object base, Object property,

      Object value) throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && property instanceof String
        && property.toString().equals("Bar")) {
      context.setPropertyResolved(true);
      throw new PropertyNotWritableException();
    }
  }

  public boolean isReadOnly(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && property instanceof String
        && property.toString().equals("Bar")) {
      context.setPropertyResolved(true);
    }

    return true;
  }

  public Iterator getFeatureDescriptors(ELContext context, Object base) {
    if (context == null)
      throw new NullPointerException();
    return null;
  }

  public Class getCommonPropertyType(ELContext context, Object base) {
    if (context == null)
      throw new NullPointerException();

    if (base == null)
      return String.class;
    return null;
  }
}
