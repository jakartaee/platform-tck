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
import jakarta.enterprise.inject.Produces;
import jakarta.faces.flow.Flow;
import jakarta.faces.flow.builder.FlowBuilder;
import jakarta.faces.flow.builder.FlowDefinition;
import jakarta.faces.flow.builder.FlowBuilderParameter;

public class MaintainCustomerRecordJavaFlowDefinition implements Serializable {

  private static final long serialVersionUID = -5610441904980215032L;

  public MaintainCustomerRecordJavaFlowDefinition() {
  }

  @Produces
  @FlowDefinition
  public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
    String flowId = "maintain-customer-record-java";
    flowBuilder.id("", flowId);
    flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml");

    flowBuilder.switchNode("router1").markAsStartNode()
        .defaultOutcome("view-customer").switchCase()
        .condition("#{flowScope.customerId == null}")
        .fromOutcome("create-customer");
    flowBuilder.viewNode("create-customer",
        "/" + flowId + "/" + "create-customer.xhtml");
    flowBuilder.viewNode("view-customer",
        "/" + flowId + "/" + "view-customer.xhtml");
    flowBuilder.viewNode("maintain-customer-record",
        "/" + flowId + "/" + "maintain-customer-record");
    flowBuilder.methodCallNode("upgrade-customer")
        .expression("#{maintainCustomerBeanJava.upgradeCustomer}")
        .defaultOutcome("view-customer");
    flowBuilder.initializer("#{maintainCustomerBeanJava.initializeFlow}");
    flowBuilder.finalizer("#{maintainCustomerBeanJava.cleanUpFlow}");
    flowBuilder.returnNode("success").fromOutcome("/complete");
    flowBuilder.returnNode("errorOccurred").fromOutcome("error");

    return flowBuilder.getFlow();
  }

}
