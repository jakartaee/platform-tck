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

package com.sun.ts.lib.porting;

import java.net.URL;
import java.util.*;
import java.io.*;

/**
 * An implementation of the TSDeploymentInterface can be provided by a Java EE
 * implementation, to support their own deploy/undeploy semantics.
 * Implementations may choose to use the included JSR-88 impl of this interface
 * - com.sun.ts.lib.deliverable.cts.StandardDeployment14. If you use this impl,
 * then you can create an impl of com.sun.ts.lib.porting.TSDeploymentInterface2.
 * 
 * @author Kyle Grucci
 */
public interface TSDeploymentInterface {
  /**
   * Initializes a new TSDeployment instance. All output should be printed to
   * this PrintWriter. All properties in the ts.jte file are accessible to this
   * porting implementation class only via the TSPropertyManager class. Please
   * see Sun's implementation of this method for an example.
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(PrintWriter writer);

  /**
   * This method is called by the test harness to deploy an .ear file into your
   * Java EE implementation. We extract such info as the app earfile from the
   * provided deployment information. The following properties are available for
   * this method's use:
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
   *         can be appended to the classpath. your Java EE implementation
   *         returns the fully qualified path to a jar file, which contains the
   *         generated ejb stub classes, which are used by any appclient tests
   *         (tests whose client directly uses an ejb).
   */
  public String deploy(DeploymentInfo info) throws TSDeploymentException;

  /**
   * This method is called by test harness to undeploy an .ear file from your
   * Java EE implementation. We extract such info as host and app from these
   * props. The following properties are available for this method 's use:
   * 
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host to undeploy this app from
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void undeploy(Properties p) throws TSDeploymentException;

  /**
   * This method is called by the test harness to check whether or not an
   * application ear is deployed. This information is used to determine whether
   * or not the harness needs to undeploy it. The following properties are
   * available for this method's use:
   * 
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host where this app is deployed
   *
   * @param p
   *          Properties specific to the currently running test
   * @return True if the app is deployed. False if not.
   */
  public boolean isDeployed(Properties p) throws TSDeploymentException;

  /**
   * This method is called to deploy a connector (.rar file) to your Java EE
   * implementation. We extract such info as deployment_host and rar_file from
   * these props. The following properties are available for this method's use:
   * 
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to deploy it to
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void deployConnector(Properties p) throws TSDeploymentException;

  /**
   * This method is called to undeploy a connector (.rar file) from your Java EE
   * implementation. We extract such info as deployment_host and rar_file from
   * these props. The following properties are available for this method's use:
   * 
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to undeploy it from
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void undeployConnector(Properties p) throws TSDeploymentException;

  /**
   * This method is called to check to see if a given connector (.rar file) is
   * deployed on your Java EE implementation. We extract such info as
   * deployment_host and rar_file from these props. The following properties are
   * available for this method's use:
   * 
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to deploy it to
   *
   * @param p
   *          Properties specific to the currently running test
   * @return True if the app is deployed. False if not.
   */
  public boolean isConnectorDeployed(Properties p) throws TSDeploymentException;

  /**
   * This method is called by the test harness to get any additional test
   * specific arguments that must be passed to the application client container
   * class, which is specified in the ts.jte file in the given environment
   * (command.testExecuteAppClient property). The additional args should be
   * appended to the value of (p.getProperty("executeArgs");), and returned. The
   * following properties are available for this method's use:
   * 
   * executeArgs - the current executeArgs as specified in the jte file
   *
   * @param p
   *          Properties specific to the currently running test
   * @return This method should return a string which represents all executeArgs
   *         to be used.
   */
  public String getAppClientArgs(Properties p);

  /**
   * This method is called by the test harness to get the corba JNDI names to
   * use in an interop scenario. This method should return a hashtable of
   * mappings for any EJB references in the array of DeploymentInfo objects
   * where:
   *
   * key = original corbaname JNDI name in the RI format value = vendor
   * corbaname JNDI name in the Vendor format
   *
   * @param infoArray
   *          Array of DeploymentInfo objects for the ear files associated with
   *          a particular directory
   * @return Hashtable This method should return a Hashtable of mappings (see
   *         above)
   */
  public Hashtable getInteropJNDINames(DeploymentInfo[] infoArray);
}
