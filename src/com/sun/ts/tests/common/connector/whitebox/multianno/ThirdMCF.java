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

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.*;

/*
 * This class shouldnt really get used.  The ra.xml should be specifying to
 * use an MCF other than this one.  (This is in order to assist with validating
 * assertions Connector:SPEC:272, Connector:SPEC:310, and Connector:SPEC:312,
 * Connector:SPEC:274.
 *
 */
public class ThirdMCF implements ManagedConnectionFactory,
    ResourceAdapterAssociation, Serializable, javax.resource.Referenceable {
  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private int count;

  private String password;

  private String user;

  private String userName;

  /*
   * @name ThirdMCF
   * 
   * @desc Default conctructor
   */
  public ThirdMCF() {
  }

  public String getUser() {
    return user;
  }

  public void setUser(String val) {
    user = val;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String val) {
    userName = val;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String val) {
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
        "ThirdMCF.createConnectionFactory", "cxManager", "TSEISDataSource");
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
        .logAPI("ThirdMCF.createConnectionFactory", "", "TSEISDataSource");
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
    String newStr1 = new String("ThirdMCF setResourceAdapter " + count);
    Debug.trace(newStr1);
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
          "ThirdMCF.createManagedConnection", "subject|info",
          "TSManagedConnection");
      TSConnection con = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        Debug.trace("ThirdMCF.createManagedConnection():  pc == null");
        Debug.trace("TSConnectionImpl.getConnection()");
        con = new TSConnectionImpl().getConnection();
      } else {
        Debug.trace("ThirdMCF.createManagedConnection():  pc != null");
        setUser(pc.getUserName());
        setUserName(pc.getUserName());
        setPassword(new String(pc.getPassword()));
        Debug.trace("TSConnectionImpl.getConnection(u,p)");
        con = new TSConnectionImpl().getConnection(pc.getUserName(),
            pc.getPassword());
      }
      return new TSManagedConnection(this, pc, null, con, false, true);
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
        "ThirdMCF.matchManagedConnection", "connectionSet|subject|info",
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
    ConnectorStatus.getConnectorStatus().logAPI("setLogWriter", "out", "");
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
    ConnectorStatus.getConnectorStatus().logAPI("getLogWriter", "", "");
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

    if ((obj == null) || !(obj instanceof ThirdMCF)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    ThirdMCF that = (ThirdMCF) obj;

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

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }

}
