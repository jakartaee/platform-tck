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

package com.sun.ts.tests.common.connector.whitebox.multianno;

import java.io.*;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.ActivationSpec;
import javax.transaction.xa.XAResource;
import java.lang.reflect.Method;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;

public class MAResourceAdapterImpl implements ResourceAdapter, Serializable {
  private String overRide = "default";

  private String raName;

  private int counter = 0;

  private transient WorkManager wm;

  private transient BootstrapContext bsc;

  public MAResourceAdapterImpl() {
    Debug.trace("MAResourceAdapterImpl Constructor ");
  }

  public void start(final BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    // setup network endpoints
    counter++;
    this.bsc = bsc;
    String str1 = new String("MAResourceAdapterImpl Started " + counter);
    ConnectorStatus.getConnectorStatus().logState(str1);

    // get WorkManager reference
    wm = bsc.getWorkManager();

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
    Debug.trace("MAResourceAdapterImpl.myStart ");
  }

  public void stop() {
    Debug.trace("MAResourceAdapterImpl.stop ");
  }

  public void endpointActivation(MessageEndpointFactory mef,
      ActivationSpec as) {
    Debug.trace("MAResourceAdapterImpl.endpointActivation ");
  }

  public XAResource[] getXAResources(ActivationSpec[] as) {
    Debug.trace("MAResourceAdapterImpl.getXAResources ");
    return null;
  }

  private Method getOnMessageMethod() {
    Debug.trace("MAResourceAdapterImpl.getOnMessageMethod ");
    Method onMessageMethod = null;
    return onMessageMethod;
  }

  private void chkUniqueMessageEndpointFactory() {
    Debug.trace("MAResourceAdapterImpl.chkUniqueMessageEndpointFactory");
  }

  public void checkAssociation() {
    Debug.trace("MAResourceAdapterImpl.checkAssociation");
  }

  public void endpointDeactivation(MessageEndpointFactory mef,
      ActivationSpec as) {
    Debug.trace("MAResourceAdapterImpl.endpointDeactivation ");
  }

  public void setRaName(String name) {
    Debug.trace("MAResourceAdapterImpl.setRAName");
    this.raName = name;
  }

  public String getRaName() {
    Debug.trace("MAResourceAdapterImpl.getRAName");
    return raName;
  }

  public void setOverRide(String val) {
    Debug.trace("MAResourceAdapterImpl.setOverRide = " + val);
    this.overRide = val;
  }

  public String getOverRide() {
    Debug.trace("MAResourceAdapterImpl.getOverRide");
    return overRide;
  }

  public void setCounter(int val) {
    this.counter = val;
  }

  public int getCounter() {
    return this.counter;
  }

  /*
   * @name equals
   * 
   * @desc Compares the given object to the ManagedConnectionFactory instance.
   * 
   * @param Object
   * 
   * @return boolean
   */
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof MAResourceAdapterImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    MAResourceAdapterImpl that = (MAResourceAdapterImpl) obj;

    if (this.counter != that.getCounter()) {
      return false;
    }

    if (!Util.isEqual(this.raName, that.getRaName()))
      return false;

    if (!Util.isEqual(this.overRide, that.getOverRide()))
      return false;

    return true;
  }

  /*
   * @name hashCode
   * 
   * @desc Gives a hash value to a ManagedConnectionFactory Obejct.
   * 
   * @return int
   */
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }

}
