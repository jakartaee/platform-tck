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

package com.sun.ts.tests.common.connector.whitebox.mdcomplete;

import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.ExecutionContext;

/**
 * This class is to be used to test assertion Connector:SPEC:214. The server
 * should not have knowledge of this work context class and so attempts to
 * submit this context class to the appserver via a WorkInst should cause the
 * server to throw a proper error code.
 */
public class UnknownWorkContext extends ExecutionContext
    implements WorkContext {

  private static final String id = "UnknownWorkContext";

  public String getDescription() {
    return "UnknownWorkContext";
  }

  public String getName() {
    return "UnknownWorkContext";
  }

}
