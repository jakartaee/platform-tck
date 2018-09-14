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

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;
import com.sun.ts.lib.util.*;
import javax.resource.spi.TransactionSupport.*;
import com.sun.ts.tests.common.connector.whitebox.*;

public class MDCompleteMCF
    implements ManagedConnectionFactory, ResourceAdapterAssociation,
    TransactionSupport, Serializable, javax.resource.Referenceable {
  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private int count;

  private String TSRValue;

  private String password;

  private String user;

  private String userName;

  @ConfigProperty(description = "String value", ignore = false)
  String factoryName = "MDCompleteMCF";

  /*
   * @name MDCompleteMCF
   * 
   * @desc Default conctructor
   */
  public MDCompleteMCF() {
    ConnectorStatus.getConnectorStatus().logState("MDCompleteMCF constructor");
  }

  /*
   * @name getTransactionSupport
   * 
   * @desc this is required for interface TransacationSupport
   */
  public TransactionSupportLevel getTransactionSupport() {

    // this is used to assist with assertion: Connector:SPEC:206 and
    // Connector:SPEC:316.
    String str = "MDCompleteMCF.getTransactionSupport called";
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
    return TransactionSupport.TransactionSupportLevel.NoTransaction;
  }

  public void setFactoryName(String name) {
    debug("MDCompleteMCF.setFactoryName");

    // this helps verify assertion Connector:SPEC:307 and Connector:SPEC:277
    // and this behavior is described in connector 1.6 spec section 18.5
    String str = "MDCompleteMCF factoryname=" + name;
    debug(str);

    this.factoryName = name;
  }

  public String getFactoryName() {
    debug("MDCompleteMCF.getFactoryName");
    return factoryName;
  }

  public String getUser() {
    debug("MDCompleteMCF.getUser() returning:  " + user);
    return user;
  }

  public void setUser(String val) {
    debug("MDCompleteMCF.setUser() with val = " + val);
    user = val;
  }

  public String getUserName() {
    debug("MDCompleteMCF.getUserName() returning:  " + userName);
    return userName;
  }

  public void setUserName(String val) {
    debug("MDCompleteMCF.setUserName() with val = " + val);
    userName = val;
  }

  public String getPassword() {
    debug("MDCompleteMCF.getPassword() returning:  " + password);
    return password;
  }

  public void setPassword(String val) {
    debug("MDCompleteMCF.setPassword() with val = " + val);
    password = val;
  }

  public String getTSRValue() {
    debug("MDCompleteMCF.getTSRValue");
    return TSRValue;
  }

  public void setTSRValue(String name) {
    debug("MDCompleteMCF.setTSRValue");
    this.TSRValue = name;
  }

  public void lookupTSR(String lookup) {
    debug("MDCompleteMCF.lookupTSR");
    try {
      TSNamingContext ncxt = new TSNamingContext();
      String newStr = "java:".concat(lookup);
      Object obj = (Object) ncxt.lookup(newStr);
      if (obj != null) {
        debug("TSR Lookup Successful");
      } else {
        debug("TSR Null");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * @name createConnectionFactory
   * 
   * @desc Creates a new connection factory instance
   * 
   * @param ConnectionManager
   * 
   * @return Object
   * 
   * @exception ResourceException
   */
  public Object createConnectionFactory(ConnectionManager cxManager)
      throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "MDCompleteMCF.createConnectionFactory", "cxManager",
        "TSEISDataSource");
    return new TSEISDataSource(this, cxManager);
  }

  /*
   * @name createConnectionFactory
   * 
   * @desc Creates a new connection factory instance
   * 
   * @return Object
   * 
   * @exception ResourceException
   */
  public Object createConnectionFactory() throws ResourceException {
    ConnectorStatus.getConnectorStatus()
        .logAPI("MDCompleteMCF.createConnectionFactory", "", "TSEISDataSource");
    return new TSEISDataSource(this, null);
  }

  /*
   * @name setResourceAdapter
   * 
   * @desc sets the Resource Adapter for this ManagedConnectionFactory
   * 
   * @return
   * 
   * @exception ResourceException
   */
  public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
    count++;
    String newStr1 = new String("MDCompleteMCF setResourceAdapter " + count);
    debug(newStr1);
    this.resourceAdapter = ra;

    // if we made it here, we assume no exceptions thrown thus validating
    // Connector:SPEC:226
    ConnectorStatus.getConnectorStatus().logState(newStr1);
  }

  /*
   * @name getResourceAdapter
   * 
   * @desc gets the Resource Adapter for this ManagedConnectionFactory
   * 
   * @return Object
   * 
   * @exception ResourceException
   */
  public ResourceAdapter getResourceAdapter() {
    debug("MDCompleteMCF.getResource");
    return resourceAdapter;
  }

  /*
   * @name createManagedConnection
   * 
   * @desc Creates a new managed connection to the underlying EIS
   *
   * @param Subject, ConnectionRequestInfo
   * 
   * @return ManagedConnection
   * 
   * @exception ResourceException
   */
  public ManagedConnection createManagedConnection(Subject subject,
      ConnectionRequestInfo info) throws ResourceException {

    try {

      ConnectorStatus.getConnectorStatus().logAPI(
          "MDCompleteMCF.createManagedConnection", "subject|info",
          "TSManagedConnection");
      TSConnection con = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        debug("MDCompleteMCF.createManagedConnection():  pc == null");
        con = new TSConnectionImpl().getConnection();
      } else {
        debug("MDCompleteMCF.createManagedConnection():  pc != null");
        setUser(pc.getUserName());
        setUserName(pc.getUserName());
        setPassword(new String(pc.getPassword()));
        con = new TSConnectionImpl().getConnection(pc.getUserName(),
            pc.getPassword());
      }

      ManagedConnection mcon = new TSManagedConnection(this, pc, null, con,
          false, true);

      return mcon;
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(
          "Exception: " + ex.getMessage());
      re.initCause(ex);
      throw re;
    }

  }

  /*
   * @name matchManagedConnection
   * 
   * @desc Return the existing connection from the connection pool
   * 
   * @param Set, Subject, ConnectionRequestInfo
   * 
   * @return ManagedConnection
   * 
   * @exception ResourceException
   */
  public ManagedConnection matchManagedConnections(Set connectionSet,
      Subject subject, ConnectionRequestInfo info) throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "MDCompleteMCF.matchManagedConnection", "connectionSet|subject|info",
        "TSEISDataSource");

    PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
    Iterator it = connectionSet.iterator();

    while (it.hasNext()) {
      Object obj = it.next();
      if (obj instanceof TSManagedConnection) {
        TSManagedConnection mc = (TSManagedConnection) obj;
        ManagedConnectionFactory mcf = mc.getManagedConnectionFactory();
        if (Util.isPasswordCredentialEqual(mc.getPasswordCredential(), pc)
            && mcf.equals(this)) {
          return mc;
        }
      }
    }

    return null;
  }

  /*
   * @name setLogWriter
   * 
   * @desc Sets the Print Writer
   * 
   * @param PrintWriter
   * 
   * @exception ResourceException
   */
  public void setLogWriter(PrintWriter out) throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI("MDCompleteMCF.setLogWriter",
        "out", "");
  }

  /*
   * @name getLogWriter
   * 
   * @desc Gets the Print Writer
   * 
   * @return PrintWriter
   * 
   * @exception ResourceException
   */
  public PrintWriter getLogWriter() throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI("MDCompleteMCF.getLogWriter",
        "", "");
    return null;
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

    if ((obj == null) || !(obj instanceof MDCompleteMCF)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    MDCompleteMCF that = (MDCompleteMCF) obj;

    if ((this.reference != null)
        && !(this.reference.equals(that.getReference()))) {
      return false;
    } else if ((this.reference == null) && !(that.getReference() == null)) {
      return false;
    }

    if ((this.resourceAdapter != null)
        && !(this.resourceAdapter.equals(that.getResourceAdapter()))) {
      return false;
    } else if ((this.resourceAdapter == null)
        && !(that.getResourceAdapter() == null)) {
      return false;
    }

    if (this.count != that.getCount()) {
      return false;
    }

    if (!Util.isEqual(this.password, that.getPassword()))
      return false;

    if (!Util.isEqual(this.user, that.getUser()))
      return false;

    if (!Util.isEqual(this.userName, that.getUserName()))
      return false;

    if (!Util.isEqual(this.TSRValue, that.getTSRValue()))
      return false;

    if (!Util.isEqual(this.factoryName, that.getFactoryName()))
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

  /*
   * @name getReference
   * 
   * @desc Gives the reference of the class
   * 
   * @return javax.naming.Reference
   */
  public javax.naming.Reference getReference() {
    javax.naming.Reference ref;

    ref = this.reference;
    return ref;
  }

  /*
   * @name setReference
   * 
   * @desc sets the reference of the class
   * 
   * @param javax.naming.Reference
   */
  public void setReference(javax.naming.Reference ref) {
    this.reference = ref;
  }

  public void debug(String out) {
    Debug.trace("MDCompleteMCF:  " + out);
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }

}
