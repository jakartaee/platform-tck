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

import java.util.*;
import java.io.*;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.status.ProgressObject;

/**
 * This is the TSDeploymentInterface2. The TSDeploymentInterface2 interface
 * allows for implementation specific functionality for those operations not
 * defined by JSR-88. You only need to provide an impl of this interface if you
 * use the JSR-88 impl of com.sun.ts.lib.porting.TSDeploymentInterface.
 * 
 * @author Kyle Grucci
 */
public interface TSDeploymentInterface2 {
  /**
   * Initializes a new TSDeploymentInterface2 instance. All output should be
   * printed to this PrintWriter. All properties in the ts.jte file are
   * accessible to this porting implementation class only via the
   * TSPropertyManager class. Please see Oracle's implementation of this method
   * for an example (SunRIDeployment2.java).
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(PrintWriter writer);

  /**
   * This method is called by the Standard Deployment implementation of
   * TSDeploymentInterface to get the classpath to any generated classes that
   * must be accessible to application clients.
   * 
   * @param targetIDs
   *          The list of deployment target IDs. These are the targets where the
   *          current application is being deployed.
   * @param info
   *          The deployment info object that contains the S1AS runtime
   *          infomation.
   * @return String This method should return a string which is formatted such
   *         that it can be appended to the classpath. Your Java EE
   *         implementation returns the fully qualified path to a jar file,
   *         which contains the generated ejb stub classes, which are used by
   *         any appclient tests (tests whose client directly uses an ejb).
   */
  public String getClientClassPath(TargetModuleID[] targetIDs,
      DeploymentInfo info, DeploymentManager manager)
      throws TSDeploymentException;

  /**
   * This method should return a Deployment Plan for a given Java EE
   * implementation. The Deployment Plan is constructed from deployment-time
   * information passed to this method via the DeploymentInfo object. The
   * DeploymentInfo object is an objectized version of Sun's RI runtime.xml.
   * Much of this information is needed to deploy the apps to your server. For
   * detailed info on the elements contained in the DeploymentInfo object,
   * please see the sun-ri 1.4 DTD that's included in the Java EE RI.
   *
   * Note that the returned Deployment Plan may have additional data which is
   * not present in the DeploymentInfo object. The returned InputStream or File
   * is passed to the JSR-88 API via the DeploymentManager. distribute method.
   *
   * @return A Deployment Plan as an InputStream or a File
   */
  public Object getDeploymentPlan(DeploymentInfo info)
      throws TSDeploymentException;

  /**
   * This method is called to create a connection factory in your Java EE
   * implementation. We extract such info as deployment_host and rar_file from
   * these props. The following properties in 'p' are available for this
   * method's use:
   * 
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to deploy it to rar_module_id - the module id
   * returned from deploying the rar file which requires this connection factory
   * to be created
   *
   * The connection factory to be created can be looked up via the
   * PropertyManager, using the rar_file name as the key. The key maps to a
   * value in the ts.jte file. For more information, please refer to the Sun RI
   * impl of this interface - located in src/com/sun/ts/lib/implementation/sun/
   * javaee/SunRIDeployment2.java.
   *
   * @param targetIDs
   *          List of target module IDs where the connector will be deployed to.
   * @param p
   *          Properties specific to the currently running test
   */
  public void createConnectionFactory(TargetModuleID[] targetIDs, Properties p)
      throws TSDeploymentException;

  /**
   * This method is called to remove a connection factory from your Java EE
   * implementation. We extract such info as deployment_host and rar_file from
   * these props. The following properties are available for this method's use:
   * 
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to undeploy it from
   *
   * The connection factory to be removed can be looked up via the
   * PropertyManager, using the rar_file name as the key. The key maps to a
   * value in the ts.jte file. For more information, please refer to the Sun RI
   * impl of this interface - located in src/com/sun/ts/lib/implementation/sun/
   * javaee/SunRIDeployment2.java.
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void removeConnectionFactory(TargetModuleID[] targetIDs, Properties p)
      throws TSDeploymentException;

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
   * This method is called by the test harness to get any property values which
   * must be set in one component's runtime info according to rules defined by
   * the opposite server's implementation in an interop scenario. Currently this
   * method should return a hashtable of mappings for any EJB references in the
   * array of DeploymentInfo objects where:
   *
   * key = original corbaname JNDI name in the RI format value = vendor
   * corbaname JNDI name in the Vendor format
   *
   * or it will return mappings for WSDL publish locations so that a client
   * component's runtime info can be updated prior to deployment to the secon
   * server.
   *
   * key = original corbaname JNDI name in the RI format value = vendor
   * corbaname JNDI name in the Vendor format
   *
   * In reality, it can be used generically to handle any string format that
   * must be retrieved from one server and applied to the runtimeinfo prior to
   * deploying to another server.
   *
   * @param infoArray
   *          Array of DeploymentInfo objects for the ear files associated with
   *          a particular directory
   * @return Hashtable This method should return a Hashtable of mappings (see
   *         above)
   */
  public Hashtable getDependentValues(DeploymentInfo[] infoArray);

  /**
   * This method allows vendors to choose which targets, of the available
   * targets, they would like to deploy on.
   *
   * @param targets
   *          Available deployment targets
   * @param info
   *          The deployment info object containing all the S1AS runtime info
   * @return Target[] List of targets to deploy the application on.
   */
  public Target[] getTargetsToUse(Target[] targets, DeploymentInfo info);

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to distribute. They may
   * also wish to invoke some vendor specific code based on the state of the
   * specified progress object. Vendors can choose to implement this method as a
   * no-op.
   *
   * @param po
   *          The progress object returned from the distribute call.
   */
  public void postDistribute(ProgressObject po);

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to start. They may also
   * wish to invoke some vendor specific code based on the state of the
   * specified progress object. Vendors can choose to implement this method as a
   * no-op.
   *
   * @param po
   *          The progress object returned from the start call.
   */
  public void postStart(ProgressObject po);

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to stop. They may also
   * wish to invoke some vendor specific code based on the state of the
   * specified progress object. Vendors can choose to implement this method as a
   * no-op.
   *
   * @param po
   *          The progress object returned from the stop call.
   */
  public void postStop(ProgressObject po);

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to undeploy. They may
   * also wish to invoke some vendor specific code based on the state of the
   * specified progress object. Vendors can choose to implement this method as a
   * no-op.
   *
   * @param po
   *          The progress object returned from the undeploy call.
   */
  public void postUndeploy(ProgressObject po);

}
