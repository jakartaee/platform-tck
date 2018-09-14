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
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.SecurityException;
import javax.resource.NotSupportedException;
import javax.transaction.xa.XAResource;
import java.io.*;
import java.util.*;
import javax.security.auth.Subject;
import com.sun.ts.tests.common.connector.util.*;

public class TSManagedConnection implements ManagedConnection {

  private TSXAConnection xacon;

  private TSConnection con;

  private TSConnectionEventListener jdbcListener;

  private PasswordCredential passCred;

  private ManagedConnectionFactory mcf;

  private PrintWriter logWriter;

  private boolean supportsXA;

  private boolean supportsLocalTx;

  private boolean destroyed;

  private Set connectionSet; // set of TSEISConnection

  public TSManagedConnection(ManagedConnectionFactory mcf,
      PasswordCredential passCred, TSXAConnection xacon, TSConnection con,
      boolean supportsXA, boolean supportsLocalTx) {
    this.mcf = mcf;
    this.passCred = passCred;
    this.xacon = xacon;
    this.con = con;
    this.supportsXA = supportsXA;
    this.supportsLocalTx = supportsLocalTx;
    connectionSet = new HashSet();
    jdbcListener = new TSConnectionEventListener(this);
    if (xacon != null) {
      xacon.addConnectionEventListener(jdbcListener);
    }

  }

  // XXX should throw better exception
  private void throwResourceException(Exception ex) throws ResourceException {

    ResourceException re = new ResourceException(
        "Exception: " + ex.getMessage());
    re.initCause(ex);
    throw re;
  }

  /*
   * @name getConnection
   * 
   * @desc Gets a connection to the underlying EIS.
   *
   * @param Subject, ConnectionRequestInfo
   * 
   * @return Object
   * 
   * @exception ResourceException
   * 
   * @see
   */
  @Override
  public Object getConnection(Subject subject,
      ConnectionRequestInfo connectionRequestInfo) throws ResourceException {

    PasswordCredential pc = Util.getPasswordCredential(mcf, subject,
        connectionRequestInfo);
    if (!Util.isPasswordCredentialEqual(pc, passCred)) {
      throw new SecurityException(
          "Principal does not match. Reauthentication not supported");
    }
    checkIfDestroyed();
    TSEISConnection jdbcCon = new TSEISConnection(this, this.supportsLocalTx);
    addTSConnection(jdbcCon);
    return jdbcCon;
  }

  /*
   * @name destroy
   * 
   * @desc destroys connection to the underlying EIS.
   * 
   * @exception ResourceException
   */
  @Override
  public void destroy() throws ResourceException {
    try {
      if (destroyed)
        return;
      destroyed = true;
      Iterator it = connectionSet.iterator();
      while (it.hasNext()) {
        TSEISConnection jdbcCon = (TSEISConnection) it.next();
        jdbcCon.invalidate();
        ConnectorStatus.getConnectorStatus()
            .logAPI("TSManagedConnection.destroy", "", "");
      }
      connectionSet.clear();
      con.close();
      if (xacon != null)
        xacon.close();
    } catch (Exception ex) {
      throwResourceException(ex);
    }
  }

  /*
   * @name cleanup
   * 
   * @desc recycles the connection from the connection pool which is being
   * handed over to the new client.
   * 
   * @exception ResourceException
   */
  public void cleanup() throws ResourceException {
    try {
      checkIfDestroyed();
      Iterator it = connectionSet.iterator();
      while (it.hasNext()) {
        TSEISConnection jdbcCon = (TSEISConnection) it.next();
        jdbcCon.invalidate();
        ConnectorStatus.getConnectorStatus()
            .logAPI("TSManagedConnection.cleanup", "", "");
      }
      connectionSet.clear();
      if (xacon != null) {
        con.close();
        con = xacon.getConnection();
      } else if (con != null) {
        con.setAutoCommit(true);
      }
    } catch (Exception ex) {
      throwResourceException(ex);
    }
  }

  /*
   * @name associateConnection
   * 
   * @desc Used by the container to change the association of an
   * application-level connection handle with a ManagedConneciton instance.
   * 
   * @param Object
   * 
   * @exception ResourceException
   */
  @Override
  public void associateConnection(Object connection) throws ResourceException {

    checkIfDestroyed();
    if (connection instanceof TSEISConnection) {
      TSEISConnection jdbcCon = (TSEISConnection) connection;
      jdbcCon.associateConnection(this);
      ConnectorStatus.getConnectorStatus()
          .logAPI("TSManagedConnection.associateConnection", "connection", "");
    } else {
      throw new IllegalStateException(
          "Invalid connection object: " + connection);
    }
  }

  /*
   * @name addConnectionEventListener
   * 
   * @desc Used by the container to change the association of an
   * application-level connection handle with a ManagedConneciton instance.
   * 
   * @param ConnectionEventListener
   */
  @Override
  public void addConnectionEventListener(ConnectionEventListener listener) {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSManagedConnection.addConnectionEventListener", "listener", "");
    jdbcListener.addConnectorListener(listener);
  }

  /*
   * @name removeConnectionEventListener
   * 
   * @desc Removes an already registered connection event listener from the
   * ManagedConnection instance.
   * 
   * @param ConnectionEventListener
   */
  @Override
  public void removeConnectionEventListener(ConnectionEventListener listener) {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSManagedConnection.removeConnectionEventListener", "listener", "");
    jdbcListener.removeConnectorListener(listener);
  }

  /*
   * @name getXAResource
   * 
   * @desc Returns an javax.transaction.xa.XAresource instance.
   * 
   * @return XAResource
   * 
   * @exception ResourceException
   */
  @Override
  public XAResource getXAResource() throws ResourceException {
    if (!supportsXA) {
      throw new NotSupportedException("XA transaction not supported");
    }
    try {
      checkIfDestroyed();
      ConnectorStatus.getConnectorStatus().logAPI(
          "TSManagedConnection.getXAResource", "", "xacon.getXAResource");
      return xacon.getXAResource(this);
    } catch (Exception ex) {
      throwResourceException(ex);
      return null;
    }
  }

  /*
   * @name getLocalTransaction
   * 
   * @desc Returns an javax.resource.spi.LocalTransaction instance.
   * 
   * @return LocalTransaction
   * 
   * @exception ResourceException
   */
  @Override
  public LocalTransaction getLocalTransaction() throws ResourceException {
    if (!supportsLocalTx) {
      throw new NotSupportedException("Local transaction not supported");
    } else {
      checkIfDestroyed();
      ConnectorStatus.getConnectorStatus().logAPI(
          "TSManagedConnection.getLocalTransaction", "",
          "LocalTransactionImpl");
      return new LocalTransactionImpl(this);
    }
  }

  /*
   * @name getMetaData
   * 
   * @desc Gets the metadata information for this connection's underlying EIS
   * resource manager instance.
   * 
   * @return ManagedConnectionMetaData
   * 
   * @exception ResourceException
   */
  @Override
  public ManagedConnectionMetaData getMetaData() throws ResourceException {
    checkIfDestroyed();
    return new MetaDataImpl(this);
  }

  /*
   * @name setLogWriter
   * 
   * @desc Sets the log writer for this ManagedConnection instance.
   * 
   * @param PrintWriter
   * 
   * @exception ResourceException
   */
  @Override
  public void setLogWriter(PrintWriter out) throws ResourceException {
    this.logWriter = out;
  }

  /*
   * @name getLogWriter
   * 
   * @desc Gets the log writer for this ManagedConnection instance.
   * 
   * @return PrintWriter
   * 
   * @exception ResourceException
   */
  @Override
  public PrintWriter getLogWriter() throws ResourceException {
    return logWriter;
  }

  /*
   * @name getTSConnection
   * 
   * @desc Returns the Jdbc Connection.
   * 
   * @return PrintWriter
   * 
   * @exception ResourceException
   */
  public TSConnection getTSConnection() throws ResourceException {
    checkIfDestroyed();
    return con;
  }

  /*
   * @name isDestroyed
   * 
   * @desc Checks if the connection is destroyed.
   * 
   * @return boolean
   */

  boolean isDestroyed() {
    return destroyed;
  }

  /*
   * @name getPasswordCredential
   * 
   * @desc Gets the PasswordCredential.
   * 
   * @return String
   */
  public PasswordCredential getPasswordCredential() {
    return passCred;

  }

  /*
   * @name sendEvent
   * 
   * @desc Send an Event .
   * 
   * @param int, Exception
   */
  void sendEvent(int eventType, Exception ex) {
    ConnectorStatus.getConnectorStatus().logAPI("TSManagedConnection.sendEvent",
        "eventType|ex", "");
    jdbcListener.sendEvent(eventType, ex, null);
  }

  /*
   * @name sendEvent
   * 
   * @desc Send an Event .
   * 
   * @param int, Exception, Object
   */
  void sendEvent(int eventType, Exception ex, Object connectionHandle) {
    ConnectorStatus.getConnectorStatus().logAPI("TSManagedConnection.sendEvent",
        "eventType|ex|connectionHandle", "");
    jdbcListener.sendEvent(eventType, ex, connectionHandle);
  }

  /*
   * @name removeTSConnection
   * 
   * @desc Removes a connection from the connection pool.
   * 
   * @param TSEISConnection
   */
  public void removeTSConnection(TSEISConnection jdbcCon) {
    ConnectorStatus.getConnectorStatus()
        .logAPI("TSManagedConnection.removeTSConnection", "jdbcCon", "");
    connectionSet.remove(jdbcCon);
  }

  /*
   * @name addTSConnection
   * 
   * @desc Add a connection to the connection pool.
   * 
   * @param TSEISConnection
   */
  public void addTSConnection(TSEISConnection jdbcCon) {
    ConnectorStatus.getConnectorStatus()
        .logAPI("TSManagedConnection.addTSConnection", "jdbcCon", "");
    connectionSet.add(jdbcCon);
  }

  /*
   * @name checkIfDestroyed
   * 
   * @desc Checks if the connection is destroyed.
   * 
   * @param TSEISConnection
   */
  private void checkIfDestroyed() throws ResourceException {
    if (destroyed) {
      throw new IllegalStateException("Managed connection is closed");
    }
  }

  /*
   * @name getManagedConnectionFactory
   * 
   * @desc Gets a managed connection factory instance.
   * 
   * @return ManagedConnectionFactory
   */
  public ManagedConnectionFactory getManagedConnectionFactory() {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSManagedConnection.getManagedConnectionFactory", "",
        "ManagedConnectionFactory");
    return mcf;
  }
}
