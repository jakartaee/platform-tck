/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.flows.intermediate.beans;

import java.io.Serializable;
import java.util.Map;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.FlowHandler;

public class MaintainCustomerBean implements Serializable {
  public MaintainCustomerBean() {
    super();
  }

  public String createCustomer() {
    // Logic to create a new customer object.
    FacesContext context = FacesContext.getCurrentInstance();
    CustomerBean customer = new CustomerBean();
    FlowHandler flowHandler = context.getApplication().getFlowHandler();
    Map<Object, Object> flowScope = flowHandler.getCurrentFlowScope();
    if (null == flowScope) {
      throw new IllegalStateException("Must have a flow handler");
    }
    flowScope.put("customerId", customer.getCustomerId());
    flowScope.put("customerIdValue", customer);
    return "router1";
  }

  public String fetchCustomer() {
    // Logic to fetch a customer.
    return "success";
  }

  public void upgradeCustomer() {
    FacesContext context = FacesContext.getCurrentInstance();
    FlowHandler flowHandler = context.getApplication().getFlowHandler();
    Map<Object, Object> flowScope = flowHandler.getCurrentFlowScope();
    if (null == flowScope) {
      throw new IllegalStateException("Must have a flow handler");
    }
    CustomerBean customer = (CustomerBean) flowScope.get("customerIdValue");
    customer.setUpgraded(true);

  }

  public void initializeFlow() {
    FacesContext context = FacesContext.getCurrentInstance();
    Map<String, Object> requestMap = context.getExternalContext()
        .getRequestMap();
    requestMap.put("initializerMessage", "Initializer called");

  }

  public void cleanUpFlow() {
    Map<String, Object> requestMap = FacesContext.getCurrentInstance()
        .getExternalContext().getRequestMap();
    requestMap.put("finalizerMessage", "Finalizer called");
  }
}
