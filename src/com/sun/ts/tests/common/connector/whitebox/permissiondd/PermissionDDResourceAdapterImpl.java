/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.permissiondd;

import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.HintsContext;
import javax.resource.spi.work.SecurityContext;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.transaction.xa.XAResource;
import javax.resource.spi.security.PasswordCredential;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.Util;

/**
 * This RA is used to assist with verifying the server supports permissions.xml
 * security enforcement within a rar file.
 */

@Connector(description = "CTS Test Resource Adapter with No DD", displayName = "whitebox-permissiondd.rar", vendorName = "Java Software", eisType = "TS EIS", version = "1.6", licenseDescription = "CTS License Required", licenseRequired = true, authMechanisms = @AuthenticationMechanism(credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential, authMechanism = "BasicPassword", description = "Basic Password Authentication"), reauthenticationSupport = false, securityPermissions = @SecurityPermission(description = "Security Perm description", permissionSpec = ""), transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction, requiredWorkContexts = {
    HintsContext.class, SecurityContext.class })
public class PermissionDDResourceAdapterImpl
    implements ResourceAdapter, java.io.Serializable {

  private transient BootstrapContext bsc;

  private transient PermissionDDWorkManager awm;

  private transient WorkManager wm;

  private transient Work work;

  private String serverSideUser = ""; // corresponds to ts.jte's 'user' property

  private String serverSidePwd = ""; // corresponds to ts.jte's 'password'
                                     // property

  private String eisUser = ""; // corresponds to ts.jte's 'user1' property

  private String eisPwd = ""; // corresponds to ts.jte's 'password' property

  @ConfigProperty(defaultValue = "PermissionDDResourceAdapterImpl")
  private String raName;

  /**
   * constructor
   **/
  public PermissionDDResourceAdapterImpl() {
    debug("enterred constructor...");

    this.serverSideUser = System.getProperty("j2eelogin.name");
    this.serverSidePwd = System.getProperty("j2eelogin.password");
    this.eisUser = System.getProperty("eislogin.name");
    this.eisPwd = System.getProperty("eislogin.password");

    debug("leaving constructor...");
  }

  //
  // Begin ResourceAdapter interface requirements
  //

  /* must implement for ResourceAdapter interface requirement */
  public void start(BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    debug("enterred start");

    ConnectorStatus.getConnectorStatus()
        .logState("PermissionDDResourceAdapterImpl.start called");

    this.bsc = bsc;
    this.wm = bsc.getWorkManager();

    this.awm = new PermissionDDWorkManager(bsc);
    awm.runTests();

    debug("leaving start");
  }

  /* must implement for ResourceAdapter interface requirement */
  public void stop() {
    debug("entered stop");
    debug("leaving stop");
  }

  /* must implement for ResourceAdapter interface requirement */
  public void endpointActivation(MessageEndpointFactory factory,
      ActivationSpec spec) throws NotSupportedException {

    debug("enterred endpointActivation");
    debug("leaving endpointActivation");
  }

  /* must implement for ResourceAdapter interface requirement */
  public void endpointDeactivation(MessageEndpointFactory ep,
      ActivationSpec spec) {
    debug("enterred endpointDeactivation");
    debug("leaving endpointDeactivation");
  }

  /* must implement for ResourceAdapter interface requirement */
  public XAResource[] getXAResources(ActivationSpec[] specs)
      throws ResourceException {

    debug("enterred getXAResources");
    debug("leaving getXAResources");

    throw new UnsupportedOperationException();
  }

  //
  // END ResourceAdapter interface requirements
  //

  /*
   * @name equals
   * 
   * @desc compares this object with the given object.
   * 
   * @param Object obj
   * 
   * @return boolean
   */
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof PermissionDDResourceAdapterImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    PermissionDDResourceAdapterImpl that = (PermissionDDResourceAdapterImpl) obj;

    if (!Util.isEqual(this.serverSideUser, that.getServerSideUser()))
      return false;

    if (!Util.isEqual(this.serverSidePwd, that.getServerSidePwd()))
      return false;

    if (!Util.isEqual(this.eisUser, that.getEisUser()))
      return false;

    if (!Util.isEqual(this.eisPwd, that.getEisPwd()))
      return false;

    if (!Util.isEqual(this.raName, that.getRaName()))
      return false;

    return true;
  }

  /*
   * @name hashCode
   * 
   * @desc gets the hashcode for this object.
   * 
   * @return int
   */
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }

  public void setRaName(String name) {
    this.raName = name;

    // this helps verify assertion Connector:SPEC:279
    String str = "setRAName called with raname=" + raName;
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  public String getRaName() {
    debug("PermissionDDResourceAdapterImpl.getRAName");
    return raName;
  }

  public void debug(String out) {
    Debug.trace("PermissionDDResourceAdapterImpl:  " + out);
  }

  public void setServerSideUser(String val) {
    this.serverSideUser = val;
  }

  public String getServerSideUser() {
    return this.serverSideUser;
  }

  public void setServerSidePwd(String val) {
    this.serverSidePwd = val;
  }

  public String getServerSidePwd() {
    return this.serverSidePwd;
  }

  public void setEisUser(String val) {
    this.eisUser = val;
  }

  public String getEisUser() {
    return this.eisUser;
  }

  public void setEisPwd(String val) {
    this.eisUser = val;
  }

  public String getEisPwd() {
    return this.eisPwd;
  }

}
