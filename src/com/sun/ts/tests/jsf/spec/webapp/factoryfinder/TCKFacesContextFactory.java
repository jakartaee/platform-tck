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

package com.sun.ts.tests.jsf.spec.webapp.factoryfinder;

import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;

public class TCKFacesContextFactory extends FacesContextFactory {

  /**
   * <p>
   * The @{link ApplicationFactory} instance provided by the JSF implementation
   * under test.
   * </p>
   */
  private FacesContextFactory factory;

  /**
   * Default constructor.
   */
  public TCKFacesContextFactory() {

    System.out.println("[TCKFacesContextFactory] Default CTOR");

  }

  /**
   * <p>
   * Constructs a new TCKFacesContextFactory instance that wraps the default
   * {@link FacesContextFactory} instance of the implementation under test.
   * </p>
   * 
   * @param factory
   *          the {@link FacesContextFactory} of the implementation under test
   */
  public TCKFacesContextFactory(FacesContextFactory factory) {

    System.out.println("[TCKFacesContextFactory] Factory CTOR");
    this.factory = factory;

  } // END TCKFacesContextFactory

  // -------------------- Methods from javax.faces.context.FacesContextFactory

  /**
   * <p>
   * Create (if needed) and return a {@link javax.faces.context.FacesContext}
   * instance that is initialized for the processing of the specified request
   * and response objects, utilizing the specified
   * {@link javax.faces.lifecycle.Lifecycle} instance, for this web application.
   * </p>
   * <p/>
   * <p>
   * The implementation of this method must ensure that calls to the
   * <code>getCurrentInstance()</code> method of
   * {@link javax.faces.context.FacesContext}, from the same thread that called
   * this method, will return the same {@link javax.faces.context.FacesContext}
   * instance until the <code>release()</code> method is called on that
   * instance.
   * </p>
   *
   * @param context
   *          In servlet environments, the <code>ServletContext</code> that is
   *          associated with this web application
   * @param request
   *          In servlet environments, the <code>ServletRequest</code> that is
   *          to be processed
   * @param response
   *          In servlet environments, the <code>ServletResponse</code> that is
   *          to be processed
   * @param lifecycle
   *          The {@link javax.faces.lifecycle.Lifecycle} instance being used to
   *          process this request
   *
   * @throws javax.faces.FacesException
   *           if a {@link javax.faces.context.FacesContext} cannot be
   *           constructed for the specified parameters
   * @throws NullPointerException
   *           if any of the parameters are <code>null</code>
   */
  public FacesContext getFacesContext(Object context, Object request,
      Object response, Lifecycle lifecycle) throws FacesException {

    return factory.getFacesContext(context, request, response, lifecycle);

  } // END getFacesContext

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * Returns the wrapped {@link FacesContextFactory} instance passed to the
   * constructor by the JSF implementation under test.
   * </p>
   */
  public FacesContextFactory getWrappedInstance() {

    return factory;

  } // END getWrappedInstance

  /**
   * <p>
   * Returns the 'this' FacesContextFactory instance for testing purposes only..
   * </p>
   */
  public FacesContextFactory getWrapped() {
    return this;
  }
}
