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
import java.util.Vector;

/* This simple EL Resolver resolves only method expressions of
   the form ${vect.methodName}, where 'vect' is resolved to java.util.Vector
   and 'methodName' is one of its methods.
*/

public class VectELResolver extends ELResolver {

  public Object getValue(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && "vect".equals(property)) {
      context.setPropertyResolved(true);
      return new Vector();
    }
    return null;
  }

  public Class getType(ELContext context, Object base, Object property)
      throws ELException {
    if (context == null)
      throw new NullPointerException();

    if (base == null && "vect".equals(property))
      context.setPropertyResolved(true);

    // we never set a value
    return null;
  }

  public void setValue(ELContext context, Object base, Object property,
      Object value) {
    if (context == null)
      throw new NullPointerException();

    if (base == null && "vect".equals(property))
      context.setPropertyResolved(true);
  }

  public boolean isReadOnly(ELContext context, Object base, Object property) {
    if (context == null)
      throw new NullPointerException();

    if (base == null && "vect".equals(property))
      context.setPropertyResolved(true);
    return true;
  }

  public Iterator getFeatureDescriptors(ELContext context, Object base) {
    return null;
  }

  public Class getCommonPropertyType(ELContext context, Object base) {
    return null;
  }
}
