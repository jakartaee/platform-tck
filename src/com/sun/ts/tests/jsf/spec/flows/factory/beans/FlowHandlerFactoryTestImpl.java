/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.flows.factory.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.flow.FlowHandlerFactoryWrapper;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple wrapped flow handler factory.
 */
@Named
@ApplicationScoped
public class FlowHandlerFactoryTestImpl extends FlowHandlerFactoryWrapper {

  public FlowHandlerFactoryTestImpl() {
  }

  private FlowHandlerFactory wrapped;

  @Inject
  private AppBean appBean;

  /**
   * Constructor.
   *
   * @param wrapped
   *          the wrapped flow handler factory.
   */
  public FlowHandlerFactoryTestImpl(FlowHandlerFactory wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public FlowHandlerFactory getWrapped() {
    return this.wrapped;
  }

  /**
   * Add a message to the context every time the createFlowHandler method is
   * called so we can verify later that the factory is actually being used.
   *
   * @param context
   *          the Faces context.
   * @return the flow handler.
   */
  @Override
  public FlowHandler createFlowHandler(FacesContext context) {
    System.out.println("createFlowHandler");
    FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
        .put("flowHandlerFactoryWrapped", true);
    String id = (null != appBean) ? appBean.getId() : "null";
    FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
        .put("appBean", id);
    return getWrapped().createFlowHandler(context);
  }
}
