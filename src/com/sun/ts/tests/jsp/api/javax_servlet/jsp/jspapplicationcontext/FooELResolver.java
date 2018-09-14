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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.jspapplicationcontext;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import java.util.Iterator;

public class FooELResolver extends ELResolver {
  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    Object result = null;

    if (base == null) {
      // Resolving first variable (e.g. ${Foo}).
      // We only handle "Foo"
      String propertyName = (String) property;
      if (propertyName.equals("Foo")) {
        result = "Test PASSED";
        context.setPropertyResolved(true);
      } else {
        System.out.println(
            "FooELResolver: no match: propertyName is " + propertyName);
      }
    } else {
      System.out.println("FooELResolver: base is non-null");
    }

    return result;
  }

  public Class getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base instanceof String && base.toString().equals("Foo")) {
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
        && property.toString().equals("Foo")) {
      context.setPropertyResolved(true);
    }
  }

  public boolean isReadOnly(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && property instanceof String
        && property.toString().equals("Foo")) {
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
