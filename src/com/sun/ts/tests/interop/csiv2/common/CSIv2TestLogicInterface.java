/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.interop.csiv2.common;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

/**
 * Centralized interface for all CSIv2 test logic. This interfaces is extended
 * by the the CSIv2SessionBean remote interfaces, and is implemented by the
 * CSIv2AppClient and the CSIV2Servlet classes. These classes all delegate to
 * CSIv2TestLogicImpl.
 */
public interface CSIv2TestLogicInterface {
  /**
   * Accepts a dynamic array of Strings. This method writes all appropriate log
   * information, retrieves the first String in the array, looks up the bean
   * reference bound to the corresponding JNDI name, and performs an invocation.
   * If the chain array is empty or null, invoke writes all log appropriate
   * information and returns.
   *
   * @param An
   *          array of JNDI names (Strings) indicating which bean to invoke
   *          next, in the sequence.
   * @param An
   *          array of properties from TS Test Harness.
   */
  public void invoke(ArrayList chain, Properties p) throws RemoteException;
}
