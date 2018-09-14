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
 * @(#)SignatureInitListener.java	1.1 04/11/05
 */

package com.sun.ts.tests.jsf.spec.webapp.tldsig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A <code>ServletContextListener</code> implementation that will store the
 * <code>ServletContext</code> within the <code>ServletContextHolder</code>
 * class.
 *
 * @see ServletContextHolder
 */
public class SignatureInitListener implements ServletContextListener {

  // ------------------------------------- Methods from ServletContextListener

  public void contextInitialized(ServletContextEvent servletContextEvent) {

    System.out.println("SignatureInitListener: contextInitialized");
    ServletContextHolder
        .setServletContext(servletContextEvent.getServletContext());
  } // END contextInitialized

  public void contextDestroyed(ServletContextEvent servletContextEvent) {

    System.out.println("SignatureInitListener: contextDestroyed");
    ServletContextHolder.setServletContext(null);

  } // END contextDestoryed

}
