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
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.connector.util.*;

public class XAManagedConnectionFactory implements ManagedConnectionFactory,
    ResourceAdapterAssociation, Serializable, javax.resource.Referenceable {

  private javax.naming.Reference reference;

  private ResourceAdapter resourceAdapter;

  private String TSRValue;

  private int count;

  private String password;

  private String user;

  private String userName;

  /*
   * @name XAManagedConnectionFactory
   * 
   * @desc Default conctructor
   */
  public XAManagedConnectionFactory() {
  }

  public String getUser() {
    System.out
        .println("XAManagedConnectionFactory.getUser() returning:  " + user);
    return user;
  }

  public void setUser(String val) {
    System.out
        .println("XAManagedConnectionFactory.setUser() with val = " + val);
    user = val;
  }

  public String getUserName() {
    System.out.println(
        "XAManagedConnectionFactory.getUserName() returning:  " + userName);
    return userName;
  }

  public void setUserName(String val) {
    System.out
        .println("XAManagedConnectionFactory.setUserName() with val = " + val);
    userName = val;
  }

  public String getPassword() {
    System.out.println(
        "XAManagedConnectionFactory.getPassword() returning:  " + password);
    return password;
  }

  public void setPassword(String val) {
    System.out
        .println("XAManagedConnectionFactory.setPassword() with val = " + val);
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

  public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
    count++;
    String newStr1 = "XAManagedConnectionFactory setResourceAdapter " + count;
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
  public Object createConnectionFactory() throws ResourceException {
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
      ResourceException rex = new ResourceException("Exception");
      rex.initCause(ex);
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
      ResourceException rex = new ResourceException("Exception");
      rex.initCause(ex);
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

    if ((obj == null) || !(obj instanceof XAManagedConnectionFactory)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    XAManagedConnectionFactory that = (XAManagedConnectionFactory) obj;

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

  public void setTSRValue(String name) {
    ConnectorStatus.getConnectorStatus()
        .logState("XAManagedConnectionFactory.setTSRValue");
    Debug.trace(
        "XAManagedConnectionFactory.setTSRValue called with value = " + name);
    this.TSRValue = name;
  }

  public String getTSRValue() {
    ConnectorStatus.getConnectorStatus()
        .logState("XAManagedConnectionFactory.getTSRValue");
    Debug.trace("XAManagedConnectionFactory.getTSRValue");
    return TSRValue;
  }

  public void lookupTSR(String lookup) {
    ConnectorStatus.getConnectorStatus()
        .logState("XAManagedConnectionFactory.lookupTSR");
    try {
      Debug.trace("lookupTSR() called with lookup name = " + lookup);
      TSNamingContext ncxt = new TSNamingContext();
      String newStr = "java:".concat(lookup);
      Object obj = (Object) ncxt.lookup(newStr);
      if (obj != null) {
        ConnectorStatus.getConnectorStatus().logState("TSR Lookup Successful");
        Debug.trace("TSR Lookup Successful");
      } else {
        Debug.trace("TSR Null");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int val) {
    this.count = val;
  }

}
