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
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.DistributableWorkManager;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.ActivationSpec;
import javax.transaction.xa.XAResource;
import java.lang.reflect.Method;
import com.sun.ts.tests.common.connector.util.*;

public class LocalTxResourceAdapterImpl
    implements ResourceAdapter, Serializable {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  // IMPORTANT: for compliance, if you add non-transient member data
  // here, be sure to add respective entry to equals() method below.

  private transient TestWorkManager twm;

  private transient TestBootstrapContext tbs;

  private transient LocalTxMessageListener ml;

  private String RAName; // value from ra's xml file

  private Boolean useSecurityMapping = null; // value from ra's xml file

  private int counter = 0;

  private transient LocalTxMessageWork work1;

  private transient LocalTxMessageWork1 work2;

  private transient LocalTxMessageWork2 work3;

  private transient WorkManager wm;

  private int mefcount = 0;

  private transient MessageEndpointFactory mef1;

  private transient MessageEndpointFactory mef2;

  private transient BootstrapContext bsc;

  private String sicUser = ""; // this should correspond to ts.jte's 'user'
                               // property

  private String sicPwd = ""; // this should correspond to ts.jte's 'password'
                              // property

  private String eisUser = ""; // this should correspond to ts.jte's 'user1'
                               // property

  private String eisPwd = ""; // this should correspond to ts.jte's 'password'
                              // property

  public LocalTxResourceAdapterImpl() {
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxResourceAdapterImpl Constructor ");
    Debug.trace("LocalTxResourceAdapterImpl Constructor ");

    this.sicUser = System.getProperty("j2eelogin.name");
    this.sicPwd = System.getProperty("j2eelogin.password");
    this.eisUser = System.getProperty("eislogin.name");
    this.eisPwd = System.getProperty("eislogin.password");
  }

  @Override
  public void start(final BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    // setup network endpoints
    counter++;
    this.bsc = bsc;
    Debug.trace("LocalTxResourceAdapter Started " + counter);
    String str1 = "LocalTxResourceAdapter Started " + counter;
    ConnectorStatus.getConnectorStatus().logState(str1);

    // get WorkManager reference

    wm = bsc.getWorkManager();

    if (bsc != null) {
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTxResourceAdapter BootstrapContext Not Null ");
    }

    if (wm != null) {
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTxResourceAdapter WorkManager Not Null ");

      if (wm instanceof DistributableWorkManager) {
        Debug.trace("wm supports DistributableWorkManager");
        ConnectorStatus.getConnectorStatus()
            .logState("wm supports DistributableWorkManager");
      } else {
        Debug.trace("wm Does NOT support DistributableWorkManager");
        ConnectorStatus.getConnectorStatus()
            .logState("wm Does NOT support DistributableWorkManager");
      }

    }
    try {
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
          " LocalTxResourceAdapterImpl ; calling setUseSecurityMapping(true)");
      ConnectorStatus.getConnectorStatus().logState(
          " LocalTxResourceAdapterImpl ; calling setUseSecurityMapping(true)");
      twm.setUseSecurityMapping(true);
    } else {
      // use Case 1 security thus do NO mapping of identities
      Debug.trace(
          " LocalTxResourceAdapterImpl ; calling setUseSecurityMapping(false)");
      ConnectorStatus.getConnectorStatus().logState(
          " LocalTxResourceAdapterImpl ; calling setUseSecurityMapping(false)");
      twm.setUseSecurityMapping(false);
    }
    twm.runTests();

    // Create TestBootstrap object
    tbs = new TestBootstrapContext(ctx);
    tbs.runTests();
  }

  @Override
  public void stop() {
    // Set the TestWorkManager to null upon resource adapter shutdown.

    if (work1 != null) {
      work1.stop();
    }
    if (work2 != null) {
      work2.stop();
    }

    if (work3 != null) {
      work3.stop();
    }
  }

  @Override
  public void endpointActivation(MessageEndpointFactory mef,
      ActivationSpec as) {
    try {
      mefcount++;

      // check if endpointActivation has been called
      Debug.trace("LocalTxResourceAdapter.endpointActivation called");
      Method onMessagexa = getOnMessageMethod();
      boolean de = mef.isDeliveryTransacted(onMessagexa);

      // For MDB with Not Supported transaction attribute
      if (!de) {
        mef1 = mef;
        String destinationName = ((LocalTxActivationSpec) as)
            .getDestinationName();
        Debug.trace("LocalTxResourceAdapter preparing work1");

        if (mef1 != null) {
          Debug.trace("mef1 is not null");
        }

        work1 = new LocalTxMessageWork(destinationName, mef1);
        work1.setBootstrapContext(bsc);

        // perform some msging to test SIC
        TSSecurityContext sic = new TSSecurityContextWithListener(sicUser,
            sicPwd, eisUser, this.useSecurityMapping.booleanValue());
        work1.addWorkContext(sic);

        Debug.trace("LocalTxResourceAdapter work1 created");
        wm.scheduleWork(work1, WorkManager.INDEFINITE, null, null);
        Debug.trace("LocalTxResourceAdapter work1 scheduled");
      } else // For MDB with Required transaction attribute
      {
        // Endpoint requires a tranaction but no incoming transaction
        mef2 = mef;
        Debug.trace("LocalTxResourceAdapter preparing work2");
        String destinationName = ((LocalTxActivationSpec) as)
            .getDestinationName();
        Debug.trace("Before Destination name");
        Debug.trace("Destination name is " + destinationName);

        if (mef2 != null) {
          Debug.trace("mef2 is not null");
        }

        work2 = new LocalTxMessageWork1(destinationName, mef2);

        Debug.trace("LocalTxResourceAdapter work2 created");
        wm.scheduleWork(work2, WorkManager.INDEFINITE, null, null);
        Debug.trace("LocalTxResourceAdapter work2 scheduled");

        // Endpoint requires a tranaction and there is an incoming transaction
        work3 = new LocalTxMessageWork2(destinationName, mef2);
        XidImpl myid = new XidImpl();
        ExecutionContext ec = new ExecutionContext();
        int idcount = myid.getFormatId();
        Debug.trace("XID getting used [ " + idcount + " ]");
        ec.setXid(myid);
        ml = new LocalTxMessageListener(myid, this.bsc);
        wm.scheduleWork(work3, WorkManager.INDEFINITE, ec, ml);
      }

      if (mefcount == 2) {
        chkUniqueMessageEndpointFactory();
      }

    } catch (Throwable ex) {
      ex.printStackTrace();
    }

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
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTx MessageEndpointFactory is Unique");

      // Also checking if the equals on the MEF is implemented correctly.
      // Normally MEF equals should not be over ridden but if it is
      // it should be implemented correctly.
      ConnectorStatus.getConnectorStatus().logState(
          "LocalTx MessageEndpointFactory equals implemented correctly");
    }
  }

  @Override
  public void endpointDeactivation(MessageEndpointFactory mef,
      ActivationSpec as) {
    mefcount--;

    if ((mef1 != null) && (mef1.equals(mef))) {
      mef1 = null;

    } else if ((mef2 != null) && (mef2.equals(mef))) {
      mef2 = null;

    } else {
      // possible issue so dump some debugging/trace info
      String str = "WARNING:  LocalTxResourceAdapterImpl.endpointDeactivation():  ";
      str += "unexpected MEF passed in";
      Debug.trace(str);
      if (mef == null) {
        Debug.trace("NULL MEF passed into endpointDeactivation()");
      } else {
        Debug.trace("Unrecognize mef passed into endpointDeactivation()");
      }
    }
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

    if ((obj == null) || !(obj instanceof LocalTxResourceAdapterImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    LocalTxResourceAdapterImpl that = (LocalTxResourceAdapterImpl) obj;

    if (this.counter != that.getCounter()) {
      return false;
    }

    if (this.mefcount != that.getMefcount()) {
      return false;
    }

    if (!Util.isEqual(this.sicUser, that.getSicUser()))
      return false;

    if (!Util.isEqual(this.sicPwd, that.getSicPwd()))
      return false;

    if (!Util.isEqual(this.eisUser, that.getEisUser()))
      return false;

    if (!Util.isEqual(this.eisPwd, that.getEisPwd()))
      return false;

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

  public void setRAName(String name) {
    ConnectorStatus.getConnectorStatus()
        .logState("LocalTxResourceAdapter.setRAName");
    this.RAName = name;
  }

  public String getRAName() {
    Debug.trace("LocalTxResourceAdapter.getRAName");
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

  public void setSicUser(String val) {
    this.sicUser = val;
  }

  public String getSicUser() {
    return this.sicUser;
  }

  public void setSicPwd(String val) {
    this.sicPwd = val;
  }

  public String getSicPwd() {
    return this.sicPwd;
  }

  public void setEisUser(String val) {
    this.eisUser = val;
  }

  public String getEisUser() {
    return this.eisUser;
  }

  public void setEisPwd(String val) {
    this.eisPwd = val;
  }

  public String getEisPwd() {
    return this.eisPwd;
  }

}
