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

package com.sun.ts.tests.jsf.spec.webapp.factoryfinder;

import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;

/**
 * <p>
 * A simple wrapper class for FactoryFinder validation.
 * </p>
 */
public class TCKApplicationFactory extends ApplicationFactory {

  /**
   * <p>
   * The @{link ApplicationFactory} instance provided by the JSF implementation
   * under test.
   * </p>
   */
  private ApplicationFactory factory;

  /**
   * Default constructor.
   */
  public TCKApplicationFactory() {

    System.out.println("[TCKApplicationFactory] Default CTOR");

  }

  /**
   * <p>
   * Constructs a new TCKApplicationFactory instance that wraps the default
   * {@link ApplicationFactory} instance of the implementation under test.
   * </p>
   * 
   * @param factory
   *          the {@link ApplicationFactory} of the implementation under test
   */
  public TCKApplicationFactory(ApplicationFactory factory) {

    System.out.println("[TCKApplicationFactory] Factory CTOR");
    this.factory = factory;

  } // END TCKApplicationFactory

  // ----------------- Methods from jakarta.faces.application.ApplicationFactory

  /**
   * <p>
   * Create (if needed) and return an
   * {@link jakarta.faces.application.Application} instance for this web
   * application.
   * </p>
   */
  public Application getApplication() {

    return factory.getApplication();

  } // END getApplication

  /**
   * <p>
   * Replace the {@link jakarta.faces.application.Application} instance that will
   * be returned for this web application.
   * </p>
   *
   * @param application
   *          The replacement {@link jakarta.faces.application.Application}
   *          instance
   *
   * @throws NullPointerException
   *           if <code>application</code> is <code>null</code>.
   */
  public void setApplication(Application application) {

    factory.setApplication(application);

  } // END setApplication

  // ---------------------------------------------------------- Public Methods

  /**
   * <p>
   * Returns the wrapped ApplicationFactory instance passed to the constructor
   * by the JSF implementation under test.
   * </p>
   */
  public ApplicationFactory getWrappedInstance() {

    return factory;

  } // END getWrappedInstance

  /**
   * <p>
   * Returns the 'this' ApplicationFactory instance for testing purposes only..
   * </p>
   */
  public ApplicationFactory getWrapped() {
    return this;
  }
}
