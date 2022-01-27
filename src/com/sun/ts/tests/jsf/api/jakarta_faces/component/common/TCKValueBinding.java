/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.component.common;

import jakarta.faces.component.StateHolder;
import jakarta.faces.context.FacesContext;
import jakarta.el.ELException;
import jakarta.el.ELContext;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.ValueExpression;

public class TCKValueBinding extends ValueExpression implements StateHolder {

  private ValueExpression binding;

  private String ref;

  public TCKValueBinding() {
    super();
  }

  public TCKValueBinding(ValueExpression binding, String ref) {
    this.binding = binding;
    this.ref = ref;
  }

  public Class getExpectedType() {
    return Object.class;
  }


  public Class getType(ELContext context) throws PropertyNotFoundException {
    return binding.getType(context);
  }

  public Object getValue(ELContext context)
      throws ELException, PropertyNotFoundException {
    return binding.getValue(context);
  }

  public boolean isReadOnly(ELContext context)
      throws PropertyNotFoundException {
    return binding.isReadOnly(context);
  }

  public void setValue(ELContext context, Object value)
      throws ELException, PropertyNotFoundException {
    binding.setValue(context, value);
  }

  public Object saveState(FacesContext context) {
    Object values[] = new Object[2];
    values[0] = ref;
    values[1] = binding;
    return (values);
  }

  public boolean isTransient() {
    return false;
  }

  public boolean isLiteralText() {
    return false;
  }
  

  public void setTransient(boolean newTransientValue) {
    // ignore
  }

  public void restoreState(FacesContext context, Object state) {
    Object values[] = (Object[]) state;
    ref = (String) values[0];
    binding = (ValueExpression) values[1];
  }

  public void restoreState(ELContext context, Object state) {
    Object values[] = (Object[]) state;
    ref = (String) values[0];
    binding = (ValueExpression) values[1];
  }

  public String getRef() {
    return ref;
  }

  public String getExpressionString() {
    return binding.getExpressionString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((binding == null) ? 0 : binding.hashCode());
    result = prime * result + ((ref == null) ? 0 : ref.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TCKValueBinding other = (TCKValueBinding) obj;
    if (binding == null) {
      if (other.binding != null)
        return false;
    } else if (!binding.equals(other.binding))
      return false;
    if (ref == null) {
      if (other.ref != null)
        return false;
    } else if (!ref.equals(other.ref))
      return false;
    return true;
  }


}
