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

@SuppressWarnings("unused")
public class LocalTxManagedConnectionFactory
    implements ManagedConnectionFactory, ResourceAdapterAssociation,
    Serializable, javax.resource.Referenceable {

  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private int count;

  private String password;

  private String user;

  private String userName;

  private String TSRValue;

  /*
   * @name LocalTxManagedConnectionFactory
   * 
   * @desc Default conctructor
   */
  public LocalTxManagedConnectionFactory() {
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

  @Override
  public Object createConnectionFactory(ConnectionManager cxManager)
      throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "LocalTxManagedConnectionFactory.createConnectionFactory", "cxManager",
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

  @Override
  public Object createConnectionFactory() throws ResourceException {
    ConnectorStatus.getConnectorStatus().logAPI(
        "LocalTxManagedConnectionFactory.createConnectionFactory", "",
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
  @Override
  public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
    count++;
    String newStr1 = "LocalTxManagedConnectionFactory setResourceAdapter "
        + count;
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

      ConnectorStatus.getConnectorStatus().logAPI(
          "LocalTxManagedConnectionFactory.createManagedConnection",
          "subject|info", "TSManagedConnection");
      TSConnection con = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
      if (pc == null) {
        System.out.println(
            "LocalTxManagedConnectionFactory.createManagedConnection():  pc == null");
        System.out.println("TSConnectionImpl.getConnection()");
        con = new TSConnectionImpl().getConnection();
      } else {
        System.out.println(
            "LocalTxManagedConnectionFactory.createManagedConnection():  pc != null");
        setUser(pc.getUserName());
        setUserName(pc.getUserName());
        setPassword(new String(pc.getPassword()));
        System.out.println(
            "LocalTxManagedConnectionFactory.createManagedConnection() with pc.getUserName()="
                + pc.getUserName() + " pc.getPassword()="
                + new String(pc.getPassword()));
        con = new TSConnectionImpl().getConnection(pc.getUserName(),
            pc.getPassword());
      }
      TSManagedConnection tcon = new TSManagedConnection(this, pc, null, con,
          false, true);

      // just send some info to the log to assist with API assertion checks
      APIAssertionTest.checkMetaDataAPI(tcon.getMetaData());
      APIAssertionTest.checkManagedConnectionAPI(tcon);
      return tcon;
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
        "LocalTxManagedConnectionFactory.matchManagedConnection",
        "connectionSet|subject|info", "TSEISDataSource");
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
  @Override
  public void setLogWriter(PrintWriter out) throws ResourceException {
    ConnectorStatus.getConnectorStatus()
        .logAPI("LocalTxManagedConnectionFactory.setLogWriter", "out", "");
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
  @Override
  public PrintWriter getLogWriter() throws ResourceException {
    ConnectorStatus.getConnectorStatus()
        .logAPI("LocalTxManagedConnectionFactory.getLogWriter", "", "");
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
  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || !(obj instanceof LocalTxManagedConnectionFactory)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    LocalTxManagedConnectionFactory that = (LocalTxManagedConnectionFactory) obj;

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

    return true;
  }

  /*
   * @name hashCode
   * 
   * @desc Gives a hash value to a ManagedConnectionFactory Obejct.
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

  public void setTSRValue(String name) {
    this.TSRValue = name;
  }

  public String getTSRValue() {
    return TSRValue;
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }

}
