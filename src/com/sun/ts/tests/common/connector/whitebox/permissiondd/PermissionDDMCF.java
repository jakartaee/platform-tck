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

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import javax.resource.spi.SecurityException;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;

@ConnectionDefinitions({
    @ConnectionDefinition(connectionFactory = com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory.class, connectionFactoryImpl = com.sun.ts.tests.common.connector.whitebox.TSEISDataSource.class, connection = com.sun.ts.tests.common.connector.whitebox.TSConnection.class, connectionImpl = com.sun.ts.tests.common.connector.whitebox.TSEISConnection.class) })
public class PermissionDDMCF implements ManagedConnectionFactory,
    ResourceAdapterAssociation, javax.resource.Referenceable, Serializable {
  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private int count;

  private String tsrValue;

  private String password;

  private String user;

  private String userName;

  private String setterMethodVal = "DEFAULT";

  @ConfigProperty(defaultValue = "10", type = Integer.class, description = "Integer value", ignore = false)
  private Integer integer;

  @ConfigProperty()
  private String factoryName = "PermissionDDMCF";

  /*
   * @name PermissionDDMCF
   * 
   * @desc Default conctructor
   */
  public PermissionDDMCF() {
    // this helps verify assertion Connector:SPEC:279 and Connector:SPEC:277
    String str = "PermissionDDMCF factoryName=" + factoryName;
    ConnectorStatus.getConnectorStatus().logState(str);

    // lets make sure we can call and set setSetterMethodVal()
    setSetterMethodVal("NONDEFAULT");

    debug(str);
  }

  /*
   * used to help test assertion Connector:SPEC:278
   */
  @ConfigProperty()
  public void setSetterMethodVal(String val) {
    setterMethodVal = val;
    String str = "PermissionDDResourceAdapterImpl.setSetterMethodVal="
        + setterMethodVal;
    ConnectorStatus.getConnectorStatus().logState(str);
  }

  public String getSetterMethodVal() {
    return setterMethodVal;
  }

  public void setFactoryName(String name) {
    this.factoryName = name;
  }

  public String getFactoryName() {
    return factoryName;
  }

  public Integer getInteger() {
    return this.integer;
  }

  public void setInteger(Integer val) {
    this.integer = val;
  }

  public String getUser() {
    debug("PermissionDDMCF.getUser() returning:  " + user);
    return user;
  }

  public void setUser(String val) {
    debug("PermissionDDMCF.setUser() with val = " + val);
    user = val;
  }

  public String getUserName() {
    debug("PermissionDDMCF.getUserName() returning:  " + userName);
    return userName;
  }

  public void setUserName(String val) {
    debug("PermissionDDMCF.setUserName() with val = " + val);
    userName = val;
  }

  public String getPassword() {
    debug("PermissionDDMCF.getPassword() returning:  " + password);
    return password;
  }

  public void setPassword(String val) {
    debug("PermissionDDMCF.setPassword() with val = " + val);
    password = val;
  }

  public String getTsrValue() {
    debug("PermissionDDMCF getTsrValue called" + tsrValue);
    return tsrValue;
  }

  public void setTsrValue(String name) {
    debug("PermissionDDMCF setTsrValue called" + name);
    this.tsrValue = name;
  }

  public void lookupTSR(String lookup) {
    try {
      TSNamingContext ncxt = new TSNamingContext();
      String newStr = "java:".concat(lookup);
      Object obj = (Object) ncxt.lookup(newStr);
      if (obj != null) {
        debug("TSR NOT Null");
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
    String newStr1 = "PermissionDDMCF setResourceAdapter " + count;
    debug(newStr1);
    this.resourceAdapter = ra;
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
    debug("PermissionDDMCF.getResource");
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

      TSConnection con = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        debug("PermissionDDMCF.createManagedConnection():  pc == null");
        debug("TSConnectionImpl.getConnection()");
        con = new TSConnectionImpl().getConnection();
      } else {
        debug("PermissionDDMCF.createManagedConnection():  pc != null");
        setUser(pc.getUserName());
        setUserName(pc.getUserName());
        setPassword(new String(pc.getPassword()));
        debug("TSConnectionImpl.getConnection(u,p)");
        con = new TSConnectionImpl().getConnection(pc.getUserName(),
            pc.getPassword());
      }

      ManagedConnection mcon = new TSManagedConnection(this, pc, null, con,
          false, true);
      dumpConnectionMetaData(mcon);

      return mcon;
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(
          "Exception: " + ex.getMessage());
      re.initCause(ex);
      throw re;
    }

  }

  public void dumpConnectionMetaData(ManagedConnection mcon) {

    String hdr = "PermissionDDMCF: ";
    String out;
    boolean bLocal = false;
    boolean bXA = false;

    try {
      ManagedConnectionMetaData mdata = mcon.getMetaData();

      out = hdr + "displayName=" + mdata.getEISProductName();
      debug(out);

      out = hdr + "version=" + mdata.getEISProductVersion();
      debug(out);

      // get transaction type
      try {
        mcon.getLocalTransaction();
        bLocal = true;
      } catch (ResourceException ex) {
        System.out.println(hdr + "not a localTransaction type");
      }
      try {
        mcon.getXAResource();
        bXA = true;
      } catch (ResourceException ex) {
        System.out.println(hdr + "not a XAResource type");
      }

      out = hdr + "transactionSupport=";
      if (bLocal) {
        out = out + "LocalTransaction";
      } else if (bXA) {
        out = out + "XATransaction";
      } else {
        // assume default case of noTx
        out = out + "NoTransaction";
      }
      debug(out);
    } catch (ResourceException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
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

    PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
    Iterator it = connectionSet.iterator();

    while (it.hasNext()) {
      Object obj = it.next();
      if (obj instanceof TSManagedConnection) {
        TSManagedConnection mc = (TSManagedConnection) obj;
        ManagedConnectionFactory mcf = mc.getManagedConnectionFactory();
        if (Util.isPasswordCredentialEqual(mc.getPasswordCredential(), pc)
            && (mcf != null) && mcf.equals(this)) {
          return mc;
        }
      }
    }

    System.out.println("matchManagedConnections: couldnt find match");
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

    if ((obj == null) || !(obj instanceof PermissionDDMCF)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    PermissionDDMCF that = (PermissionDDMCF) obj;

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

    if ((this.integer != null) && (!this.integer.equals(that.getInteger()))) {
      return false;
    } else if ((this.integer == null) && !(that.getInteger() == null)) {
      return false;
    }

    if (!Util.isEqual(this.password, that.getPassword()))
      return false;

    if (!Util.isEqual(this.user, that.getUser()))
      return false;

    if (!Util.isEqual(this.userName, that.getUserName()))
      return false;

    if (!Util.isEqual(this.tsrValue, that.getTsrValue()))
      return false;

    if (!Util.isEqual(this.setterMethodVal, that.getSetterMethodVal()))
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
    Debug.trace("PermissionDDMCF:  " + out);
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }

}
