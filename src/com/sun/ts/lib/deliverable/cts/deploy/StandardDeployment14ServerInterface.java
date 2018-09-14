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

package com.sun.ts.lib.deliverable.cts.deploy;

import java.util.Map;
import java.util.Properties;
import com.sun.ts.lib.porting.DeploymentInfo;

public interface StandardDeployment14ServerInterface extends java.rmi.Remote {
  public void init(Properties jteProps, Map extraProps)
      throws java.rmi.RemoteException;

  public String deploy(DeploymentInfo info) throws java.rmi.RemoteException;

  public void undeploy(Properties p) throws java.rmi.RemoteException;

  public boolean isDeployed(Properties p) throws java.rmi.RemoteException;
}
