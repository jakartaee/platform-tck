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

import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.HintsContext;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.SecurityContext;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.transaction.xa.XAResource;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;

/**
 * This is a sample resource adapter that will use some ra.xml info. This RA is
 * used to assist with verifying the server supports annotations when there is
 * no ra.xml (Assertion 268) and the transaction support is Local.
 *
 */

@Connector(description = "CTS Test Resource Adapter with No DD", licenseDescription = "CTS License Required", licenseRequired = true, authMechanisms = @AuthenticationMechanism(credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential, authMechanism = "BasicPassword", description = "Basic Password Authentication"), reauthenticationSupport = false, securityPermissions = @SecurityPermission(), transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction, requiredWorkContexts = {
    HintsContext.class, TransactionContext.class, SecurityContext.class })
public class MDCompleteRAImpl implements ResourceAdapter, java.io.Serializable {

  private transient BootstrapContext bsc;

  private transient MDCompleteWorkManager mdwm;

  private transient WorkManager wm;

  private transient Work work;

  // this should cause the setter to get invoked
  @ConfigProperty(defaultValue = "BAD_RAName_value", description = "String value", ignore = false)
  String RAName;

  /**
   * constructor
   **/
  public MDCompleteRAImpl() {
    debug("enterred constructor...");

    debug("leaving constructor...");
  }

  //
  // Begin ResourceAdapter interface requirements
  //

  /* must implement for ResourceAdapter interface requirement */
  public void start(BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    debug("enterred start");

    debug("MDCompleteRAImpl.start called");

    this.bsc = bsc;
    this.wm = bsc.getWorkManager();

    this.mdwm = new MDCompleteWorkManager(bsc);
    mdwm.runTests();

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

    if ((obj == null) || !(obj instanceof MDCompleteRAImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    MDCompleteRAImpl that = (MDCompleteRAImpl) obj;

    if (!Util.isEqual(this.RAName, that.getRAName()))
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

  /*
   * this is the setter for the ConfigProperty annotation = RAName. According to
   * onnector 1.6 spec, section 18.5, this setter must be invoked since it
   * belongs to a ConfigProperty annotation for the ResourceAdapter JavaBean.
   */
  public void setRAName(String name) {
    this.RAName = name;

    // this helps verify assertion Connector:SPEC:279
    String str = "setRAName called with raname=" + RAName;
    debug(str);
    ConnectorStatus.getConnectorStatus().logState(str);
  }

  public String getRAName() {
    debug("MDCompleteRAImpl.getRAName");
    return RAName;
  }

  public void debug(String out) {
    Debug.trace("MDCompleteRAImpl:  " + out);
  }

}
