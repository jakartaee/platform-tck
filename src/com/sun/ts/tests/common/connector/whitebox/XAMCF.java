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
import javax.naming.Context;
import com.sun.ts.tests.common.connector.util.*;

/*
 * This class is used exclusively by the compat tests which make use of the
 * ra-compat-xxxx.xml DD which are connector 1.0 versions of the DD files.
 * These are simple tests used for backwards compat testing to verify the 
 * DD v1.0 still works.
 * This was taken from the older com/sun/ts/tests/compat13 dir structure.
 *
 */
public class XAMCF implements ManagedConnectionFactory, Serializable,
    javax.resource.Referenceable {

  private javax.naming.Reference reference;

  transient private Context ic;

  /*
   * @name XAMCF
   * 
   * @desc Default conctructor
   */
  public XAMCF() {
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
    ConnectorStatus.getConnectorStatus().logAPI("XAMCF.createConnectionFactory",
        "cxManager", "TSEISDataSource");
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
    ConnectorStatus.getConnectorStatus().logAPI("XAMCF.createConnectionFactory",
        "", "TSEISDataSource");
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
  public ManagedConnection createManagedConnection(Subject subject,
      ConnectionRequestInfo info) throws ResourceException {

    try {
      TSXAConnection xacon = null;
      TSConnection con = null;
      String userName = null;
      PasswordCredential pc = Util.getPasswordCredential(this, subject, info);

      ConnectorStatus.getConnectorStatus()
          .logAPI("XAMCF.createManagedConnection", "", "TSManagedConnection");
      if (pc == null) {
        xacon = new TSXAConnectionImpl();
        con = xacon.getConnection();
        System.out.println("xacon.getConnection");
      } else {
        xacon = new TSXAConnectionImpl();
        System.out.println("xacon.getConnection(u,p)");
        con = xacon.getConnection(pc.getUserName(), pc.getPassword());
      }
      if (con == null) {
        System.out.println("Connection is null");
      }
      return new TSManagedConnection(this, pc, xacon, con, true, true);
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
  public ManagedConnection matchManagedConnections(Set connectionSet,
      Subject subject, ConnectionRequestInfo info) throws ResourceException {

    ConnectorStatus.getConnectorStatus().logAPI("XAMCF.matchManagedConnections",
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
  public void setLogWriter(PrintWriter out) throws ResourceException {

    try {
      // getXADataSource().setLogWriter(out);
    } catch (Exception ex) {
      ResourceException rex = new ResourceException("Exception", ex);
      throw rex;
    }
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
    try {
      return null;
    } catch (Exception ex) {
      ResourceException rex = new ResourceException("Exception", ex);
      throw rex;
    }
  }

  /*
   * @name equals
   * 
   * @desc compares the given object with this object.
   * 
   * @return boolean
   */
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof XAMCF)) {
      ConnectorStatus.getConnectorStatus().logAPI("!XAMCF.equals", "obj",
          "boolean");
      return false;
    }

    // the original code assumed that if 'obj' instance of XAMCF, then
    // these objs were equal and a log msg was printed out.
    // we are logging true just by virtue of fact we made it here and we do
    // not want to change the integrity of any existing test.
    ConnectorStatus.getConnectorStatus().logAPI("XAMCF.equals", "obj",
        "boolean");

    if (obj == this) {
      return true;
    }

    XAMCF that = (XAMCF) obj;

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

}
