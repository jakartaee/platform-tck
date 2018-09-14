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
public class StandardDeployment14SeparateVM extends StandardDeployment14
    implements TSDeploymentInterface {
  private StandardDeployment14ServerInterface deploymentServer = null;

  private Map extraParameters = new HashMap();

  /**
   * Initializes a new TSDeployment instance. All output should be printed to
   * this PrintWriter. All properties in the ts.jte file are accessible to this
   * porting implementation class only via the PropertyManagerInterface.
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(PrintWriter out) {
    try {
      TestUtil.logHarnessDebug("StandardDeployment14SeparateVM:  in init");
      iPortingSet = TSDeployment.iPortingSet;
      extraParameters.put("porting_set", new Integer(iPortingSet));

      TestUtil.logHarness(
          "StandardDeployment14:  Using porting " + "set #" + iPortingSet);
      String portingString = String.valueOf(iPortingSet);

      this.log = out;
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();

      String portClass = "porting.ts.deploy2.class." + iPortingSet;
      TestUtil.logHarness("Using " + portClass);
      dep2 = TSDeployment2.getDeploymentInstance(log, portClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getDeploymentServer() throws TSDeploymentException {
    String sHost = "";
    String sPort = "2002";

    // lookup server here or in suitesynchronizer?
    try {
      sHost = InetAddress.getLocalHost().getHostName();
      try {
        sPort = propMgr.getProperty("harness.deployment.server.port");
        TestUtil.logHarnessDebug("harness.deployment.server.port = " + sPort);
      } catch (PropertyNotSetException pe) {
        log.print(pe.getMessage());
        log.print("error looking up property:  sun.porting_server.port");
        log.print("Defaulting to port #:  " + sPort);
      }

      // Do a lookup of our porting server
      deploymentServer = (StandardDeployment14ServerInterface) Naming.lookup(
          "rmi://" + sHost + ":" + sPort + "/StandardDeployment14Server");

      deploymentServer.init(propMgr.getJteProperties(), extraParameters);

    } catch (java.net.MalformedURLException me) {
      log.print(me.getMessage());
      me.printStackTrace();
      throw new TSDeploymentException(me.getMessage(), me);
    } catch (java.net.UnknownHostException ue) {
      log.print(ue.getMessage());
      log.print("unknown host:  " + sHost);
      ue.printStackTrace();
      throw new TSDeploymentException(ue.getMessage(), ue);
    } catch (NotBoundException nbe) {
      log.print(nbe.getMessage());
      nbe.printStackTrace();
      throw new TSDeploymentException(nbe.getMessage(), nbe);
    } catch (java.rmi.RemoteException e) {
      TestUtil.logHarness("Failed to deploy using "
          + "StandardDeploymentServer: " + e.getMessage());
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage(), e);
    }
  }

  /**
   * This method is called by the test harness to deploy an .ear file We extract
   * such info as the app earfile from the provided deployment information. The
   * following properties are available for this method's use:
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
   *         can be appended to the classpath.
   */
  public String deploy(DeploymentInfo info) throws TSDeploymentException {
    String classpath = null;

    try {
      if (deploymentServer == null)
        getDeploymentServer();

      // send deploy info across to proxy
      classpath = deploymentServer.deploy(info);
    } catch (java.rmi.RemoteException e) {
      TestUtil.logHarness("Failed to deploy using "
          + "StandardDeploymentServer:  " + e.getMessage());
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }

    return classpath;

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
  public boolean isDeployed(Properties p) throws TSDeploymentException {
    boolean bIsInstalled = false;

    try {
      if (deploymentServer == null)
        getDeploymentServer();

      // send deploy info across to proxy
      bIsInstalled = deploymentServer.isDeployed(p);
    } catch (java.rmi.RemoteException e) {
      TestUtil.logHarness("Failed to check if app was deployed using"
          + "StandardDeploymentServer:  " + e.getMessage());
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }

    return bIsInstalled;
  }

  /**
   * This method is called by test harness to undeploy an .ear file from Sun's
   * J2EE reference implementation. We extract such info as host and app from
   * these props. The following properties are available for this method 's use:
   * 
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host to undeploy this app from
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void undeploy(Properties p) throws TSDeploymentException {
    try {
      if (deploymentServer == null)
        getDeploymentServer();

      // undeploy app
      deploymentServer.undeploy(p);
    } catch (java.rmi.RemoteException e) {
      TestUtil.logHarness("Failed to undepoy using "
          + "StandardDeploymentServer:  " + e.getMessage());
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }
  }
}
