/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.ejb.wrappers;

import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJBException;

/**
 * Stateless wrapper that provides common methods for a Stateless
 * Session bean. This class is intended to be subclassed by the final
 * bean class that will provide the test logic (business methods).
 */
public class Stateless3xWrapper {

  protected TSNamingContext nctx = null;

  /*
   * Business methods.
   */

  /**
   * Initialize TS logging.
   *
   * @param props
   *          TS properties need by TestUtil
   *
   */
  public void initLogging(Properties props) {
    try {
      TestUtil.logTrace("[Stateless3xWrapper] initLogging()");
      TestUtil.init(props);
      TestUtil.logTrace("[Stateless3xWrapper] initLogging OK.");
    } catch (RemoteLoggingInitException e) {
      TestUtil.logMsg("initLogging failed.");
      throw new EJBException(e.getMessage());
    }
  }

  public void createNamingContext() {
    TestUtil.logTrace("[Stateless3xWrapper] createNamingContext()");

    try {
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("[Stateless3xWrapper] Cannot create Naming Context: " + e);
    }
  }
}
