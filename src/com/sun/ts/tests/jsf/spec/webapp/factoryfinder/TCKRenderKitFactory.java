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

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.util.Iterator;

/**
 * <p>
 * A simple wrapper class for FactoryFinder validation.
 * </p>
 */
public class TCKRenderKitFactory extends RenderKitFactory {

  /**
   * <p>
   * The @{link RenderKitFactory} instance provided by the JSF implementation
   * under test.
   * </p>
   */
  private RenderKitFactory factory;

  /**
   * Default Constructor.
   */
  public TCKRenderKitFactory() {

    System.out.println("[TCKRenderKitFactory] Default CTOR");

  }

  /**
   * <p>
   * Constructs a new TCKRenderKitFactory instance that wraps the default
   * {@link RenderKitFactory} instance of the implementation under test.
   * </p>
   * 
   * @param factory
   *          the {@link RenderKitFactory} of the implementation under test
   */
  public TCKRenderKitFactory(RenderKitFactory factory) {

    System.out.println("[TCKRenderKitFactory] Factory CTOR");
    this.factory = factory;

  } // END TCKRenderKitFactory

  // ----------------- Methods from javax.faces.application.RenderKitFactory

  /**
   * <p>
   * Register the specified {@link javax.faces.render.RenderKit} instance,
   * associated with the specified <code>renderKitId</code>, to be supported by
   * this {@link javax.faces.render.RenderKitFactory}, replacing any previously
   * registered {@link javax.faces.render.RenderKit} for this identifier.
   * </p>
   *
   * @param renderKitId
   *          Identifier of the {@link javax.faces.render.RenderKit} to register
   * @param renderKit
   *          {@link javax.faces.render.RenderKit} instance that we are
   *          registering
   *
   * @throws NullPointerException
   *           if <code>renderKitId</code> or <code>renderKit</code> is
   *           <code>null</code>
   */
  public void addRenderKit(String renderKitId, RenderKit renderKit) {

    factory.addRenderKit(renderKitId, renderKit);

  } // END addRenderKit

  /**
   * <p>
   * Return a {@link javax.faces.render.RenderKit} instance for the specified
   * render kit identifier, possibly customized based on dynamic characteristics
   * of the specified {@link javax.faces.context.FacesContext}, if
   * non-<code>null</code>. If there is no registered
   * {@link javax.faces.render.RenderKit} for the specified identifier, return
   * <code>null</code>. The set of available render kit identifiers is available
   * via the <code>getRenderKitIds()</code> method.
   * </p>
   *
   * @param context
   *          FacesContext for the request currently being processed, or
   *          <code>null</code> if none is available.
   * @param renderKitId
   *          Render kit identifier of the requested
   *          {@link javax.faces.render.RenderKit} instance
   *
   * @throws IllegalArgumentException
   *           if no {@link javax.faces.render.RenderKit} instance can be
   *           returned for the specified identifier
   * @throws NullPointerException
   *           if <code>renderKitId</code> is <code>null</code>
   */
  public RenderKit getRenderKit(FacesContext context, String renderKitId) {

    return factory.getRenderKit(context, renderKitId);

  } // END getRenderKit

  /**
   * <p>
   * Return an <code>Iterator</code> over the set of render kit identifiers
   * registered with this factory. This set must include the value specified by
   * <code>RenderKitFactory.HTML_BASIC_RENDER_KIT</code>.
   * </p>
   */
  public Iterator getRenderKitIds() {

    return factory.getRenderKitIds();

  } // END getRenderKitIds

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * Returns the wrapped RenderKitFactory instance passed to the constructor by
   * the JSF implementation under test.
   * </p>
   */
  public RenderKitFactory getWrappedInstance() {

    return factory;

  } // END getWrappedInstance

  /**
   * <p>
   * Returns the 'this' RenderKitFactory instance for testing purposes only..
   * </p>
   */
  public RenderKitFactory getWrapped() {
    return this;
  }

}
