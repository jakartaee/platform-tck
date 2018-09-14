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

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;

public class NoTxManagedConnectionFactory implements ManagedConnectionFactory,
    ResourceAdapterAssociation, Serializable, javax.resource.Referenceable {

  private ResourceAdapter resourceAdapter;

  private javax.naming.Reference reference;

  private int count;

  private String password;

  private String user;

  private String userName;

  /*
   * @name NoTxManagedConnectionFactory
   * 
   * @desc Default conctructor
   */
  public NoTxManagedConnectionFactory() {
  }

  public String getUser() {
    System.out
        .println("NoTxManagedConnectionFactory.getUser() returning:  " + user);
    return user;
  }

  public void setUser(String val) {
    System.out
        .println("NoTxManagedConnectionFactory.setUser() with val = " + val);
    user = val;
  }

  public String getUserName() {
    System.out.println(
        "NoTxManagedConnectionFactory.getUserName() returning:  " + userName);
    return userName;
  }

  public void setUserName(String val) {
    System.out.println(
        "NoTxManagedConnectionFactory.setUserName() with val = " + val);
    userName = val;
  }

  public String getPassword() {
    System.out.println(
        "NoTxManagedConnectionFactory.getPassword() returning:  " + password);
    return password;
  }

  public void setPassword(String val) {
    System.out.println(
        "NoTxManagedConnectionFactory.setPassword() with val = " + val);
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
  @Override
  public Object createConnectionFactory(ConnectionManager cxManager)
      throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "NoTxManagedConnectionFactory.createConnectionFactory", "cxManager",
        "TSEISDataSource");
    return new TSEISDataSource(this, cxManager);
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

  @Override
  public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
    count++;
    String newStr1 = "NoTxManagedConnectionFactory setResourceAdapter " + count;
    System.out.println(newStr1);
    ConnectorStatus.getConnectorStatus().logState(newStr1);
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

  @Override
  public ResourceAdapter getResourceAdapter() {
    return resourceAdapter;
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

  @Override
  public Object createConnectionFactory() throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "NoTxManagedConnectionFactory.createConnectionFactory", "",
        "TSEISDataSource");
    return new TSEISDataSource(this, null);
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

  @Override
  public ManagedConnection createManagedConnection(Subject subject,
      ConnectionRequestInfo info) throws ResourceException {

    try {
      TSConnection con = null;
      String userName = null;
      ConnectorStatus.getConnectorStatus().logAPI(
          "NoTxManagedConnectionFactory.createManagedConnection", "",
          "TSManagedConnection");
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        con = new TSConnectionImpl().getConnection();
      } else {
        con = new TSConnectionImpl().getConnection(pc.getUserName(),
            pc.getPassword());
      }
      return new TSManagedConnection(this, pc, null, con, false, false);
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
  @Override
  public ManagedConnection matchManagedConnections(Set connectionSet,
      Subject subject, ConnectionRequestInfo info) throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "NoTxManagedConnecitonFactory.matchManagedConnection",
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
   * @desc sets the log writer
   * 
   * @param PrinterWriter out
   * 
   * @exception ResourceException
   */
  @Override
  public void setLogWriter(PrintWriter out) throws ResourceException {

  }

  /*
   * @name getLogWriter
   * 
   * @desc gets the log writer
   * 
   * @return PrinterWriter out
   * 
   * @exception ResourceException
   */
  @Override
  public PrintWriter getLogWriter() throws ResourceException {
    return null;
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
  @Override
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof NoTxManagedConnectionFactory)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    NoTxManagedConnectionFactory that = (NoTxManagedConnectionFactory) obj;

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
   * @desc gets the hashcode for this object.
   * 
   * @return int
   */
  @Override
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
  @Override
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
  @Override
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
