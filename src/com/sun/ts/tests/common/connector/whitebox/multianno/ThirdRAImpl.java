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
import javax.resource.spi.*;
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
import javax.resource.spi.work.HintsContext;
import javax.resource.spi.work.SecurityContext;
import com.sun.ts.tests.common.connector.whitebox.*;

/*
 * This class shouldnt really get used.  The ra.xml should be specifying to
 * use a RAImple other than this one.  (This is in order to assist with validating
 * assertions Connector:SPEC:272, Connector:SPEC:310, and Connector:SPEC:312,
 * Connector:SPEC:274.
 *
 */
@Connector(description = "CTS test RA specified in DD is used", displayName = "ThirdRAImpl", vendorName = "Java Software", eisType = "TS EIS", version = "1.0", licenseDescription = "CTS License Required", licenseRequired = true, reauthenticationSupport = false, transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction, requiredWorkContexts = {
    HintsContext.class, SecurityContext.class })

public class ThirdRAImpl implements ResourceAdapter, Serializable {
  private String raName;

  private int counter = 0;

  private transient WorkManager wm;

  private transient BootstrapContext bsc;

  public ThirdRAImpl() {
    Debug.trace("ThirdRAImpl Constructor ");
  }

  public void start(final BootstrapContext bsc)
      throws ResourceAdapterInternalException {
    // setup network endpoints
    counter++;
    this.bsc = bsc;
    String str1 = new String("ThirdRAImpl Started " + counter);
    Debug.trace(str1);

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
    Debug.trace("ThirdRAImpl.myStart ");
  }

  public void stop() {
    Debug.trace("ThirdRAImpl.stop ");
  }

  public void endpointActivation(MessageEndpointFactory mef,
      ActivationSpec as) {
  }

  public XAResource[] getXAResources(ActivationSpec[] as) {
    Debug.trace("ThirdRAImpl.getXAResources ");
    return null;
  }

  private Method getOnMessageMethod() {
    Method onMessageMethod = null;
    return onMessageMethod;
  }

  private void chkUniqueMessageEndpointFactory() {
  }

  public void checkAssociation() {
  }

  public void endpointDeactivation(MessageEndpointFactory mef,
      ActivationSpec as) {
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

    if ((obj == null) || !(obj instanceof ThirdRAImpl)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    ThirdRAImpl that = (ThirdRAImpl) obj;

    if (this.counter != that.getCounter()) {
      return false;
    }

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
  }

  public String getRaName() {
    return raName;
  }

  public void setCounter(int val) {
    this.counter = val;
  }

  public int getCounter() {
    return this.counter;
  }

}
