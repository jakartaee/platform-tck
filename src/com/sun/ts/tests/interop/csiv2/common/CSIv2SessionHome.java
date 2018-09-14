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
 * Session bean home interface for CSIv2 testing.
 */
public interface CSIv2SessionHome extends EJBHome {
  /**
   * @param recording
   *          true if we are recording the current method invocation and if we
   *          should throw a CreateException. false if we should actually create
   *          the bean.
   */
  public CSIv2Session create(Properties p, boolean recording)
      throws CreateException, RemoteException;
}
