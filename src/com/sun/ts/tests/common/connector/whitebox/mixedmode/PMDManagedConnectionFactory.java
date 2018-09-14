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

package com.sun.ts.tests.common.connector.whitebox.mixedmode;

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;
import com.sun.ts.lib.util.*;

@ConnectionDefinition(connectionFactory = com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory.class, connectionFactoryImpl = com.sun.ts.tests.common.connector.whitebox.TSEISDataSource.class, connection = com.sun.ts.tests.common.connector.whitebox.TSConnection.class, connectionImpl = com.sun.ts.tests.common.connector.whitebox.TSEISConnection.class)

public class PMDManagedConnectionFactory implements ManagedConnectionFactory,
    ResourceAdapterAssociation, Serializable, javax.resource.Referenceable {
  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private int count;

  private String password;

  private String user;

  private String userName;

  @ConfigProperty(defaultValue = "PMDManagedConnectionFactory", description = "String value", ignore = false, supportsDynamicUpdates = false, confidential = false)
  String factoryName;

  @ConfigProperty(description = "String value", ignore = false)
  String noDefaultValue = "NO_DEFAULT_VAL";

  /*
   * @name PMDManagedConnectionFactory
   * 
   * @desc Default conctructor
   */
  public PMDManagedConnectionFactory() {
  }

  public void setFactoryName(String name) {
    // this helps verify assertion Connector:SPEC:307 and Connector:SPEC:267
    // and this behavior is described in connector 1.6 spec section 18.5
    String str = "PMDManagedConnectionFactory factoryname=" + name;
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);

    this.factoryName = name;

    // this helps verify assertion Connector:SPEC:277
    // and this behavior is described in connector 1.6 spec section 18.5
    str = "PMDManagedConnectionFactory noDefaultValue=" + this.noDefaultValue;
    ConnectorStatus.getConnectorStatus().logState(str);
    debug(str);
  }

  public String getFactoryName() {
    return factoryName;
  }

  public void setNoDefaultValue(String val) {
    this.noDefaultValue = val;
  }

  public String getNoDefaultValue() {
    return noDefaultValue;
  }

  public String getUser() {
    debug("PMDManagedConnectionFactory.getUser() returning:  " + user);
    return user;
  }

  public void setUser(String val) {
    debug("PMDManagedConnectionFactory.setUser() with val = " + val);
    user = val;
  }

  public String getUserName() {
    debug("PMDManagedConnectionFactory.getUserName() returning:  " + userName);
    return userName;
  }

  public void setUserName(String val) {
    debug("PMDManagedConnectionFactory.setUserName() with val = " + val);
    userName = val;
  }

  public String getPassword() {
    debug("PMDManagedConnectionFactory.getPassword() returning:  " + password);
    return password;
  }

  public void setPassword(String val) {
    debug("PMDManagedConnectionFactory.setPassword() with val = " + val);
    password = val;
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
        "PMDManagedConnectionFactory.createConnectionFactory", "cxManager",
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
    ConnectorStatus.getConnectorStatus().logAPI(
        "PMDManagedConnectionFactory.createConnectionFactory", "",
        "TSEISDataSource");
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
    String newStr1 = new String(
        "PMDManagedConnectionFactory setResourceAdapter " + count);
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
    debug("PMDManagedConnectionFactory.getResource");
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
          "PMDManagedConnectionFactory.createManagedConnection", "subject|info",
          "TSManagedConnection");
      TSConnection con = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        debug(
            "PMDManagedConnectionFactory.createManagedConnection():  pc == null");
        debug("TSConnectionImpl.getConnection()");
        con = new TSConnectionImpl().getConnection();
      } else {
        debug(
            "PMDManagedConnectionFactory.createManagedConnection():  pc != null");
        setUser(pc.getUserName());
        setUserName(pc.getUserName());
        setPassword(new String(pc.getPassword()));
        debug("TSConnectionImpl.getConnection(u,p)");
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
        "PMDManagedConnectionFactory.matchManagedConnection",
        "connectionSet|subject|info", "TSEISDataSource");

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

    if ((obj == null) || !(obj instanceof PMDManagedConnectionFactory)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    PMDManagedConnectionFactory that = (PMDManagedConnectionFactory) obj;

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

    if (!Util.isEqual(this.factoryName, that.getFactoryName()))
      return false;

    if (!Util.isEqual(this.noDefaultValue, that.getNoDefaultValue()))
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
    Debug.trace("PMDManagedConnectionFactory:  " + out);
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }
}
