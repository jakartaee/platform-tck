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

import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import java.util.Iterator;

public class TCKLifecycleFactory extends LifecycleFactory {

  /**
   * <p>
   * The @{link ApplicationFactory} instance provided by the JSF implementation
   * under test.
   * </p>
   */
  private LifecycleFactory factory;

  /**
   * Default constructor.
   */
  public TCKLifecycleFactory() {

    System.out.println("[TCKLifecycleFactory] Default CTOR");

  }

  /**
   * <p>
   * Constructs a new TCKLifecycleFactory instance that wraps the default
   * {@link LifecycleFactory} instance of the implementation under test.
   * </p>
   * 
   * @param factory
   *          the {@link LifecycleFactory} of the implementation under test
   */
  public TCKLifecycleFactory(LifecycleFactory factory) {

    System.out.println("[TCKLifecycleFactory] Factory CTOR");
    this.factory = factory;

  } // END TCKLifecycleFactory

  // --------------------- Methods from javax.faces.lifecycle.LifecycleFactory

  /**
   * <p>
   * Register a new {@link javax.faces.lifecycle.Lifecycle} instance, associated
   * with the specified <code>lifecycleId</code>, to be supported by this
   * <code>LifecycleFactory</code>. This method may be called at any time, and
   * makes the corresponding {@link javax.faces.lifecycle.Lifecycle} instance
   * available throughout the remaining lifetime of this web application.
   * </p>
   *
   * @param lifecycleId
   *          Identifier of the new {@link javax.faces.lifecycle.Lifecycle}
   * @param lifecycle
   *          {@link javax.faces.lifecycle.Lifecycle} instance that we are
   *          registering
   *
   * @throws IllegalArgumentException
   *           if a {@link javax.faces.lifecycle.Lifecycle} with the specified
   *           <code>lifecycleId</code> has already been registered
   * @throws NullPointerException
   *           if <code>lifecycleId</code> or <code>lifecycle</code> is
   *           <code>null</code>
   */
  public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {

    factory.addLifecycle(lifecycleId, lifecycle);

  } // END addLifecycle

  /**
   * <p>
   * Create (if needed) and return a {@link javax.faces.lifecycle.Lifecycle}
   * instance for the specified lifecycle identifier. The set of available
   * lifecycle identifiers is available via the <code>getLifecycleIds()</code>
   * method.
   * </p>
   * <p/>
   * <p>
   * Each call to <code>getLifecycle()</code> for the same
   * <code>lifecycleId</code>, from within the same web application, must return
   * the same {@link javax.faces.lifecycle.Lifecycle} instance.
   * </p>
   *
   * @param lifecycleId
   *          Lifecycle identifier of the requested
   *          {@link javax.faces.lifecycle.Lifecycle} instance
   *
   * @throws IllegalArgumentException
   *           if no {@link javax.faces.lifecycle.Lifecycle} instance can be
   *           returned for the specified identifier
   * @throws NullPointerException
   *           if <code>lifecycleId</code> is <code>null</code>
   */
  public Lifecycle getLifecycle(String lifecycleId) {

    return factory.getLifecycle(lifecycleId);

  } // END getLifecycle

  /**
   * <p>
   * Return an <code>Iterator</code> over the set of lifecycle identifiers
   * supported by this factory. This set must include the value specified by
   * <code>LifecycleFactory.DEFAULT_LIFECYCLE</code>.
   * </p>
   */
  public Iterator getLifecycleIds() {

    return factory.getLifecycleIds();

  } // END getLifecycleIds

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * Returns the wrapped {@link LifecycleFactory} instance passed to the
   * constructor by the JSF implementation under test.
   * </p>
   */
  public LifecycleFactory getWrappedInstance() {

    return factory;

  } // END getWrappedInstance

  /**
   * <p>
   * Returns the 'this' LifecycleFactory instance for testing purposes only..
   * </p>
   */
  public LifecycleFactory getWrapped() {
    return this;
  }
}
