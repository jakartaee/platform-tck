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

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.*;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;
import java.io.*;
import javax.security.auth.Subject;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;

/*
 * This class is used exclusively by the compat tests which make use of the
 * ra-compat-xxxx.xml DD which are connector 1.0 versions of the DD files.
 * These are simple tests used for backwards compat testing to verify the 
 * DD v1.0 still works.
 * This was taken from the older com/sun/ts/tests/compat13 dir structure.
 *
 */
public class NoTxMCF implements ManagedConnectionFactory, Serializable,
    javax.resource.Referenceable {

  private String url;

  private javax.naming.Reference reference;

  /*
   * @name NoTxMCF
   * 
   * @desc Default conctructor
   */
  public NoTxMCF() {
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
        "NoTxMCF.createConnectionFactory", "cxManager", "TSEISDataSource");
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
    ConnectorStatus.getConnectorStatus()
        .logAPI("NoTxMCF.createConnectionFactory", "", "TSEISDataSource");
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
      ConnectorStatus.getConnectorStatus()
          .logAPI("NoTxMCF.createManagedConnection", "", "TSManagedConnection");
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
          "Exception: " + ex.getMessage(), ex);
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
        "NoTxMCF.matchManagedConnections", "connectionSet|subject|info",
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

    if ((obj == null) || !(obj instanceof NoTxMCF)) {
      return false;
    }

    // this method historically was logging the following so we
    // want to continue just to be sure we dont break anything
    ConnectorStatus.getConnectorStatus().logAPI("NoTxMCF.equals", "obj",
        "boolean");

    if (obj == this) {
      return true;
    }

    NoTxMCF that = (NoTxMCF) obj;

    if (!Util.isEqual(this.url, that.getUrl()))
      return false;

    if ((this.reference != null)
        && !(this.reference.equals(that.getReference()))) {
      return false;
    } else if ((this.reference == null) && !(that.getReference() == null)) {
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

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String val) {
    this.url = val;
  }

}
