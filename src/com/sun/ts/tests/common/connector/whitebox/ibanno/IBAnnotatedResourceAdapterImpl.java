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

package com.sun.ts.tests.common.connector.whitebox.ibanno;

import java.lang.reflect.Method;

import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.ResourceException;
import javax.transaction.xa.XAResource;
import javax.resource.spi.security.PasswordCredential;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.XidImpl;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.Util;

/**
 * This is a sample resource adapter that will use no ra.xml info. This RA is
 * used to assist with verifying the server supports annotations when there is
 * no ra.xml (Assertion 268) and the transaction support is Local. It is also
 * testing inbound messaging support.
 *
 */

@Connector(description = "CTS Test Resource Adapter with No DD", displayName = "whitebox-anno_no_md.rar", vendorName = "Java Software", eisType = "TS EIS", version = "1.6", licenseDescription = "CTS License Required", licenseRequired = true, authMechanisms = @AuthenticationMechanism(credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential, authMechanism = "BasicPassword", description = "Basic Password Authentication"), reauthenticationSupport = false, securityPermissions = @SecurityPermission(), transactionSupport = TransactionSupport.TransactionSupportLevel.XATransaction, requiredWorkContexts = {
    TransactionContext.class })
public class IBAnnotatedResourceAdapterImpl
    implements ResourceAdapter, java.io.Serializable {

  private transient BootstrapContext bsc;

  private transient IBAnnoWorkManager awm;

  private transient WorkManager wm;

  private transient Work work;

  private transient MessageEndpointFactory mef2;

  private transient IBAnnoMessageWork1 work1;

  private transient IBAnnoMessageWork2 work2;

  private transient IBAnnoMessageListener ml;

  @ConfigProperty(defaultValue = "IBAnnotatedResourceAdapterImpl")
  private String raName;

  /**
   * constructor
   **/
  public IBAnnotatedResourceAdapterImpl() {
    debug("enterred IBAnnotatedResourceAdapterImpl() constructor...");
  }

  //
  // Begin ResourceAdapter interface requirements
  //

  /* must implement for ResourceAdapter interface requirement */
  public void start(BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    debug("enterred start");

    this.bsc = bsc;
    this.wm = bsc.getWorkManager();
    this.awm = new IBAnnoWorkManager(bsc);
    awm.runTests();

    debug("leaving start");
  }

  /* must implement for ResourceAdapter interface requirement */
  public void stop() {
    debug("entered stop");
  }

  /* must implement for ResourceAdapter interface requirement */
  public void endpointActivation(MessageEndpointFactory mef,
      ActivationSpec as) {
    try {
      debug("IBAnnotatedResourceAdapterImpl.endpointActivation()");

      // check if endpointActivation has been called
      Method onMessagexa = getOnMessageMethod();
      boolean de = mef.isDeliveryTransacted(onMessagexa);

      if (!de) {
        // For MDB with Not Supported transaction attribute
        // we should not get here since our mdb is msginflow_mdb2.ear which
        // has transaction set to required in the dd.
        debug(
            "should NOT have found mdb with unsupported transaction attribute!");
      } else {
        // For MDB with Required transaction attribute
        // Endpoint requires a tranaction but no incoming transaction
        String str2 = "IBAnnotatedResourceAdapterImpl Required transaction";
        ConnectorStatus.getConnectorStatus().logState(str2);
        mef2 = mef;
        debug("IBAnnoResourceAdapter preparing work1");
        String destinationName = ((IBAnnoActivationSpecChild) as)
            .getAnnoDestinationName();
        debug("Destination name is " + destinationName);

        logMEFActivationInfo(mef);

        // help verify assertion Connector:SPEC:282
        ResourceAdapter ra = ((IBAnnoActivationSpecChild) as)
            .getResourceAdapter();
        if (ra != null) {
          ConnectorStatus.getConnectorStatus().logState(
              "IBAnnoActivationSpecChild.getResourceAdapter() not null.");
        } else {
          debug(
              "IBAnnoActivationSpecChild.getResourceAdapter() = null, failed assertion Connector:SPEC:282");
        }

        // lets verify the child activation spec inherits @configProp info from
        // parent
        String childPropname = ((IBAnnoActivationSpecChild) as).getPropName();
        String strp = "IBAnnoActivationSpecChild.propName = " + childPropname;
        debug(strp);
        ConnectorStatus.getConnectorStatus().logState(strp);

        // now setup work inst
        work1 = new IBAnnoMessageWork1(destinationName, mef2);
        debug("IBAnnoResourceAdapter work1 created");
        wm.scheduleWork(work1, wm.INDEFINITE, null, null);
        debug("IBAnnoResourceAdapter work1 scheduled");

        // Endpoint requires a tranaction and there is an incoming transaction
        work2 = new IBAnnoMessageWork2(destinationName, mef2);
        XidImpl myid = new XidImpl();
        ExecutionContext ec = new ExecutionContext();
        int idcount = myid.getFormatId();
        debug("XID getting used [ " + idcount + " ]");
        ec.setXid(myid);
        ml = new IBAnnoMessageListener(myid, this.bsc);
        wm.scheduleWork(work2, wm.INDEFINITE, ec, ml);

      }

    } catch (Throwable ex) {
      ex.printStackTrace();
    }

  }

  /* must implement for ResourceAdapter interface requirement */
  public void endpointDeactivation(MessageEndpointFactory ep,
      ActivationSpec spec) {
    debug("enterred endpointDeactivation");

    if ((mef2 != null) && (mef2.equals(ep))) {
      mef2 = null;
    } else {
      // print some warnings - may or may not be issue
      if (mef2 == null) {
        debug("WARNING:  endpointDeactivation()  mef2 == null");
      } else {
        debug("WARNING:  endpointDeactivation()  mef2 != ep!");
      }
    }

    debug("leaving endpointDeactivation");
  }

  private void logMEFActivationInfo(MessageEndpointFactory mef) {
    try {
      Debug.trace("enterred logMEFActivationInfo()");
      if (mef != null) {
        String str = "IBAnnotatedResourceAdapterImpl.endpointActivation() getEndpointClass() returned: ";
        Class clazz = mef.getEndpointClass();
        if (clazz != null) {
          // should be getting class name of
          // com.sun.ts.tests.connector.mdb.JCAMessageBean
          str = str + clazz.getName();
        } else {
          // should not get here
          str = str + "null from class.getName()";
        }
        Debug.trace(str);
        ConnectorStatus.getConnectorStatus().logState(str);

        String activationName = mef.getActivationName();
        str = "IBAnnotatedResourceAdapterImpl.endpointActivation() getActivationName() returned ";
        if (activationName != null) {
          // should get here...this could be any unique name
          str = str + "nonNull name " + activationName;
        } else {
          // should not get here
          str = str + "null from mef.getActivationName()";
        }
        Debug.trace(str);
        ConnectorStatus.getConnectorStatus().logState(str);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private Method getOnMessageMethod() {

    Method onMessageMethod = null;
    try {
      Class msgListenerClass = TSMessageListenerInterface.class;
      Class[] paramTypes = { java.lang.String.class };
      onMessageMethod = msgListenerClass.getMethod("onMessage", paramTypes);

    } catch (NoSuchMethodException ex) {
      ex.printStackTrace();
    }
    return onMessageMethod;
  }

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

    if ((obj == null) || !(obj instanceof IBAnnotatedResourceAdapterImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    IBAnnotatedResourceAdapterImpl that = (IBAnnotatedResourceAdapterImpl) obj;

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

  /* must implement for ResourceAdapter interface requirement */
  public XAResource[] getXAResources(ActivationSpec[] specs)
      throws ResourceException {

    debug("IBAnno getXAResources called");

    return null;
  }

  //
  // END ResourceAdapter interface requirements
  //

  public void setRaName(String name) {
    this.raName = name;
  }

  public String getRaName() {
    return raName;
  }

  public void debug(String out) {
    Debug.trace("IBAnnotatedResourceAdapterImpl:  " + out);
  }

}
