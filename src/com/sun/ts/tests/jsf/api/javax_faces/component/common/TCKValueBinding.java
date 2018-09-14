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

package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

public class TCKValueBinding extends ValueBinding implements StateHolder {

  private ValueBinding binding;

  private String ref;

  public TCKValueBinding() {
    super();
  }

  public TCKValueBinding(ValueBinding binding, String ref) {
    this.binding = binding;
    this.ref = ref;
  }

  public Class getType(FacesContext context) throws PropertyNotFoundException {
    return binding.getType(context);
  }

  public Object getValue(FacesContext context)
      throws EvaluationException, PropertyNotFoundException {
    return binding.getValue(context);
  }

  public boolean isReadOnly(FacesContext context)
      throws PropertyNotFoundException {
    return binding.isReadOnly(context);
  }

  public void setValue(FacesContext context, Object value)
      throws EvaluationException, PropertyNotFoundException {
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

  public void setTransient(boolean newTransientValue) {
    // ignore
  }

  public void restoreState(FacesContext context, Object state) {
    Object values[] = (Object[]) state;
    ref = (String) values[0];
    binding = (ValueBinding) values[1];
  }

  public String getRef() {
    return ref;
  }
}
