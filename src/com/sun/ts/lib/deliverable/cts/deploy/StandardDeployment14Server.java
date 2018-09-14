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

package com.sun.ts.lib.deliverable.cts.deploy;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.io.*;
import java.net.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.deliverable.cts.deploy.*;

/**
 * This is an implementation of the TSDeploymentInterface. An implementation of
 * this class must be supplied by any J2EE implementation wishing to have
 * JavaTest (the harness which drives the TS tests) automatically deploy and
 * undeploy test applications. Providing this functionality enables the entire
 * test suite to be run without having to manually deploy/undeploy the J2EE test
 * applications prior to running the tests. This particular implementation uses
 * the deploy/undeploy semantics of Sun's J2EE reference implementation.
 * 
 * @author Kyle Grucci
 */

public class StandardDeployment14Server extends UnicastRemoteObject
    implements StandardDeployment14ServerInterface

{

  private static StandardDeployment14Server deploymentServer = null;

  private PropertyManagerInterface propMgr = null;

  private static String sPort = "2002";

  private static int iPort = 2002;

  private StandardDeployment14 standardDeploy = null;

  public StandardDeployment14Server() throws java.rmi.RemoteException {
  }

  public static void main(String args[]) {
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("-port"))
        sPort = args[1];
    }

    // SetSecurityManager
    System.setSecurityManager(new RMISecurityManager());

    // Create a new instance
    try {
      deploymentServer = new StandardDeployment14Server();
      iPort = Integer.parseInt(sPort);
    } catch (NumberFormatException nfe) {
      nfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Start the RMIRegistry on port 2002 and bind myself
    try {
      Registry registry = LocateRegistry.createRegistry(iPort);
      TestUtil.logHarness(
          "StandardDeployment14Server:  Registry has been created on port:  "
              + iPort);
      registry.rebind("StandardDeployment14Server", deploymentServer);
      TestUtil.logHarness(
          "StandardDeployment14Server:  deploymentServer has been bound to the Registry");
    } catch (Exception e) {
      TestUtil.logHarness("Failed to bind deploymentServer");
      e.printStackTrace();
    }
  }

  /**
   * Initializes a new TSDeployment instance. All output should be printed to
   * this PrintWriter. All properties in the ts.jte file are accessible to this
   * porting implementation class only via the TSPropertyManager class. Please
   * see Sun's implementation of this method (below) for an example.
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(Properties jteProps, Map extraParameters)
      throws RemoteException {
    try {
      TestUtil.logHarnessDebug("StandardDeployment14Server:  in init");

      jteProps.setProperty("s1as.targets", "server");
      propMgr = DeliverableFactory.getDeliverableInstance()
          .createPropertyManager(jteProps);

      if (standardDeploy == null) {
        standardDeploy = new StandardDeployment14();
      }

      Integer portingSetInteger = (Integer) extraParameters.get("porting_set");
      TSDeployment.iPortingSet = portingSetInteger.intValue();
      standardDeploy.init(new PrintWriter(System.out));
    } catch (Exception e) {
      e.printStackTrace();
    }

    TestUtil.logHarnessDebug("StandardDeployment14Server:  leaving init");
  }

  /**
   * This method is called by the test harness to deploy an .ear file into Sun
   * 's J2EE reference implementation. We extract such info as the app earfile
   * from the provided deployment information. The following properties are
   * available for this method's use:
   * <p>
   * generateSQL - "true" if SQL is to be generated for CMP beans
   * <p>
   * <p>
   * deployment_host - the host where this app is to be deployed
   * <p>
   *
   * All additional information is queryable from the DeploymentInfo interface.
   * 
   * @param info
   *          Object containing necessary deployment info.
   * @return This method should return a string which is formatted such that it
   *         can be appended to the classpath. This implementation returns the
   *         fully qualified path to a jar file, which contains the generated
   *         ejb stub classes, which are used by any appclient tests (tests
   *         whose client directly uses an ejb).
   */
  public String deploy(DeploymentInfo info) throws RemoteException {
    try {
      return standardDeploy.deploy(info);
    } catch (TSDeploymentException e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    }
  }

  /**
   * This method is called by the test harness to check whether or not an
   * application ear is deployed. This information is used to determine whether
   * or not the harness needs to undeploy it. The following properties are
   * available for this method's use:
   * 
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host where this app is deployed
   *
   * @param p - Properties specific to the currently running test
   * @return True if the app is deployed. False if not.
   */
  public boolean isDeployed(Properties p) throws RemoteException {
    try {
      return standardDeploy.isDeployed(p);
    } catch (TSDeploymentException e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    }
  }

  /**
   * This method is called by test harness to undeploy an .ear file from Sun's
   * J2EE reference implementation. We extract such info as host and app from
   * these props. The following properties are available for this method 's use:
   * 
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host to undeploy this app from
   *
   * @param p - Properties specific to the currently running test
   */
  public void undeploy(Properties p) throws RemoteException {
    try {
      standardDeploy.undeploy(p);
      return;
    } catch (TSDeploymentException e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage(), e);
    }
  }

}
