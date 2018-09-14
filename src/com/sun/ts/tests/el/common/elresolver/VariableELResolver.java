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

package com.sun.ts.tests.el.common.elresolver;

import java.beans.FeatureDescriptor;
import java.util.Hashtable;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;

public class VariableELResolver extends ELResolver {

  private Hashtable<Object, Object> varTable;

  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null) {
      throw new NullPointerException();
    }

    Object result = null;
    if (base == null) {
      context.setPropertyResolved(true);
      if (varTable != null) {
        result = varTable.get(property.toString());
      }
    }

    return result;
  }

  public Class<?> getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null) {
      throw new NullPointerException();
    }

    if (base == null) {
      context.setPropertyResolved(true);
    }

    return null;
  }

  public void setValue(ELContext context, Object base, Object property,
      Object value) {
    if (context == null) {
      throw new NullPointerException();
    }

    if (varTable == null) {
      varTable = new Hashtable<Object, Object>();
    }

    if (value == null) {
      varTable = new Hashtable<Object, Object>();
      varTable.clear();
      return;
    }

    if (base == null) {
      varTable.put(property.toString(), value);
      context.setPropertyResolved(true);
    } else {
      varTable.put(base, value);
      context.setPropertyResolved(true);
    }
  }

  public boolean isReadOnly(ELContext context, Object base, Object property) {
    if (context == null) {
      throw new NullPointerException();
    }

    if (base == null) {
      context.setPropertyResolved(true);
    }
    return false;
  }

  public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
      Object base) {
    return null;
  }

  public Class<Object> getCommonPropertyType(ELContext context, Object base) {
    if (base == null) {
      return Object.class;
    }
    return null;
  }

  // This method is not required by the ELResolver API. It is for
  // removing all entries from the the VariableELResolver's
  // static hashtable between test invocations.
  public void cleanup() {

    if (varTable != null) {
      varTable.clear();
    }

  }
}
