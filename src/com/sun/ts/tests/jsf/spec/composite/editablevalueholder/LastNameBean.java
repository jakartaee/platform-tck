/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.composite.editablevalueholder;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.event.ValueChangeListener;

import java.io.Serializable;

@jakarta.inject.Named("lastname") @jakarta.enterprise.context.SessionScoped
public class LastNameBean implements ValueChangeListener, Serializable {

  private static final long serialVersionUID = -2564031838083638087L;

  public ValueChangeListener getValueChangeListener() {
    ValueChangeListener lname = new LastNameBean();
    return lname;
  }

  @Override
  public void processValueChange(ValueChangeEvent arg0)
      throws AbortProcessingException {
    FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
        .put("changed", "TRUE");
  }

  protected String lastname = "";

  public String getLastName() {
    return lastname;
  }

  public void setLastName(String lastname) {
    this.lastname = lastname;
  }

}
