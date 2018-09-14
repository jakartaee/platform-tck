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

package com.sun.ts.tests.servlet.ee.platform.cdi.listener;

import com.sun.ts.tests.servlet.ee.platform.cdi.TCKTestBean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public final class SRListener implements ServletRequestListener {

  @Inject
  TCKTestBean ttb;

  @Inject
  BeanManager bm;

  // Public Methods
  @Override
  public void requestDestroyed(ServletRequestEvent event) {
  }

  @Override
  public void requestInitialized(ServletRequestEvent event) {
    boolean passed = true;

    if (ttb == null) {
      passed = false;
      System.out
          .println("Injection of TCKTestBean in ServletRequestListener failed");
    }

    if (bm == null) {
      passed = false;
      System.out
          .println("Injection of BeanManager in ServletRequestListener failed");
    }

    if (passed) {
      event.getServletContext().setAttribute("TEST_LOG_SR",
          "Test PASSED from ServletRequestListener");
    } else {
      event.getServletContext().setAttribute("TEST_LOG_SR",
          "Test FAILED from ServletRequestListener");
    }
  }
}
