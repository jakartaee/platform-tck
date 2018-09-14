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

package com.sun.ts.tests.common.connector.whitebox;

import java.io.*;
import java.util.Vector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import com.sun.ts.tests.common.connector.util.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.ActivationSpec;
import javax.transaction.xa.XAResource;
import java.lang.reflect.Method;

public class NoTxResourceAdapterImpl implements ResourceAdapter, Serializable {
  // IMPORTANT: for compliance, if you add non-transient member data
  // here, be sure to add respective entry to equals() method below.

  private transient TestWorkManager twm;

  private transient TestBootstrapContext tbs;

  private transient LocalTxMessageListener ml;

  private String RAName; // value from ra's xml file

  private Boolean useSecurityMapping = null; // value from ra's xml file

  private int counter = 0;

  private transient javax.transaction.xa.XAResource xaresource;

  private transient WorkManager wm;

  private int mefcount = 0;

  private transient MessageEndpointFactory mef1;

  private transient MessageEndpointFactory mef2;

  private transient BootstrapContext bsc;

  public NoTxResourceAdapterImpl() {
    ConnectorStatus.getConnectorStatus()
        .logState("NoTxResourceAdapterImpl Constructor");
    System.out.println("NoTxResourceAdapterImpl Constructor");
  }

  @Override
  public void start(final BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    // setup network endpoints
    counter++;
    this.bsc = bsc;
    System.out.println("NoTxResourceAdapter Started " + counter);
    String str1 = "NoTxResourceAdapter Started " + counter;
    ConnectorStatus.getConnectorStatus().logState(str1);

    // get WorkManager reference

    WorkManager wm = bsc.getWorkManager();

    if (bsc != null) {
      ConnectorStatus.getConnectorStatus()
          .logState("NoTxResourceAdapter BootstrapContext Not Null ");
    }

    if (wm != null) {
      ConnectorStatus.getConnectorStatus()
          .logState("NoTxResourceAdapter WorkManager Not Null ");
    }

    try {
      checkAssociation();
      bsc.getWorkManager().startWork(new Work() {
        public void run() {
          myStart(bsc);
        }

        public void release() {
        }

      });
    } catch (javax.resource.spi.work.WorkException we) {
      throw new ResourceAdapterInternalException();
    }

  }

  private void myStart(final BootstrapContext ctx) {
    wm = ctx.getWorkManager();
    // Create TestWorkManager object
    twm = new TestWorkManager(ctx);
    if (this.useSecurityMapping.booleanValue() == true) {
      // values from our RA xml file indicate we want to establish Case 2
      // security for the RA. This means we need security mappings.
      Debug.trace(
          "NoTxResourceAdapterImpl ; calling setUseSecurityMapping(true)");
      twm.setUseSecurityMapping(true);
    } else {
      // use Case 1 security thus do NO mapping of identities
      Debug.trace(
          "NoTxResourceAdapterImpl ; calling setUseSecurityMapping(false)");
      twm.setUseSecurityMapping(false);
    }
    twm.runTests();

    // Create TestBootstrap object
    tbs = new TestBootstrapContext(ctx);
    tbs.runTests();
  }

  @Override
  public void endpointActivation(MessageEndpointFactory mef,
      ActivationSpec as) {
  }

  @Override
  public void stop() {
    // Set the TestWorkManager to null upon resource adapter shutdown.
    // twm = null;
  }

  @Override
  public void endpointDeactivation(MessageEndpointFactory mef,
      ActivationSpec as) {

  }

  @Override
  public XAResource[] getXAResources(ActivationSpec[] as) {
    return null;
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

  private void chkUniqueMessageEndpointFactory() {
    if ((mef1 != null) && (!mef1.equals(mef2))) {
      Debug.trace("NoTx MessageEndpointFactory is Unique");
      Debug.trace("NoTx MessageEndpointFactory equals implemented correctly");
    }
  }

  /*
   * This method is used to assist in the verification process of assertion
   * Connector:SPEC:245 This method must be called befor the work instances
   * 'run' method is called. This method checks if the setResourceAdapter()
   * method was called and if so, then this method logs a message to indicate
   * that it was called prior to the 'run' method of the run method.
   */
  public void checkAssociation() {
    Vector vLog = ConnectorStatus.getConnectorStatus().getStateLogVector();
    String toCheck1 = "NoTxManagedConnectionFactory setResourceAdapter 1";

    for (int i = 0; i < vLog.size(); i++) {
      String str = (String) vLog.elementAt(i);
      if (str.startsWith(toCheck1)) {
        ConnectorStatus.getConnectorStatus().logState(
            "NoTxResourceAdapter - association exists between RA and work");
        break;
      }
    }

  }

  public void setRAName(String name) {
    ConnectorStatus.getConnectorStatus()
        .logState("NoTxResourceAdapter.setRAName");
    this.RAName = name;
  }

  public String getRAName() {
    Debug.trace("NoTxResourceAdapter.getRAName");
    return RAName;
  }

  public void setUseSecurityMapping(Boolean val) {
    this.useSecurityMapping = val;
  }

  public Boolean getUseSecurityMapping() {
    return this.useSecurityMapping;
  }

  public void setCounter(int val) {
    this.counter = val;
  }

  public int getCounter() {
    return this.counter;
  }

  public void setMefcount(int val) {
    this.mefcount = val;
  }

  public int getMefcount() {
    return this.mefcount;
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

    if ((obj == null) || !(obj instanceof NoTxResourceAdapterImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    NoTxResourceAdapterImpl that = (NoTxResourceAdapterImpl) obj;

    if (this.counter != that.getCounter()) {
      return false;
    }

    if (this.mefcount != that.getMefcount()) {
      return false;
    }

    if (!Util.isEqual(this.RAName, that.getRAName()))
      return false;

    if (this.getUseSecurityMapping().booleanValue() != that
        .getUseSecurityMapping().booleanValue()) {
      return false;
    }

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

}
