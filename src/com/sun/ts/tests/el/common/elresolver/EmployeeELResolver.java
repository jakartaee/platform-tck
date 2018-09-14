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

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import java.util.Iterator;

/* This simple EL Resolver resolves only method expressions of
 * the form ${worker.methodName}, where 'worker' is resolved to
 * com.sun.ts.tests.el.spec.language.EmployeeBean and 'methodName' is one of its
 * methods.
 */

public class EmployeeELResolver extends ELResolver {

  private static final String FIRST_NAME = "Ricky";

  private static final String LAST_NAME = "Bobby";

  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    Object result = null;
    String propertyName = property.toString();

    if (base == null) {
      // Resolving first variable (e.g. ${worker}).
      if ("worker".equals(propertyName)) {
        result = "worker has no name!";
      }

    } else if ((base instanceof String) && ("firstName".equals(property))) {

      result = EmployeeELResolver.FIRST_NAME;

    } else if ((base instanceof String) && ("lastName".equals(property))) {

      result = EmployeeELResolver.LAST_NAME;
    }

    if (result != null)
      context.setPropertyResolved(true);
    return result;
  }

  public Class<?> getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    String propertyName = property.toString();
    if ((base == null && propertyName.equals("worker"))
        || (base instanceof String && base.equals("worker")))
      context.setPropertyResolved(true);

    // we never set a value
    return null;
  }

  public void setValue(ELContext context, Object base, Object property,
      Object value) {
    if (context == null)
      throw new NullPointerException();

    if ((base == null && "worker".equals(property))
        || (base instanceof String && base.equals("worker")))
      context.setPropertyResolved(true);
  }

  public boolean isReadOnly(ELContext context, Object base, Object property) {
    if (context == null)
      throw new NullPointerException();

    String propertyName = property.toString();
    if ((base == null && propertyName.equals("worker"))
        || (base instanceof String && base.equals("worker")))
      context.setPropertyResolved(true);
    return true;
  }

  public Iterator getFeatureDescriptors(ELContext context, Object base) {
    return null;
  }

  public Class<?> getCommonPropertyType(ELContext context, Object base) {
    if (base == null)
      return Object.class;
    return null;
  }
}
