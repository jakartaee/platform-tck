/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.annotated;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSConnectionImpl;
import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSManagedConnection;
import com.sun.ts.tests.common.connector.whitebox.Util;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConfigProperty;
import jakarta.resource.spi.ConnectionDefinition;
import jakarta.resource.spi.ConnectionDefinitions;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.EISSystemException;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ManagedConnectionMetaData;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterAssociation;
import jakarta.resource.spi.security.PasswordCredential;

/**
 * AnnoManagedConnectionFactory is a managed connection factory that provides connection management for the underlying
 * EIS (Enterprise Information System). It implements the ManagedConnectionFactory, ResourceAdapterAssociation, and
 * Referenceable interfaces, and is Serializable.
 */
@ConnectionDefinitions({
        @ConnectionDefinition(connectionFactory = com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory.class, connectionFactoryImpl = com.sun.ts.tests.common.connector.whitebox.TSEISDataSource.class, connection = com.sun.ts.tests.common.connector.whitebox.TSConnection.class, connectionImpl = com.sun.ts.tests.common.connector.whitebox.TSEISConnection.class) })
public class AnnoManagedConnectionFactory
        implements ManagedConnectionFactory, ResourceAdapterAssociation, jakarta.resource.Referenceable, Serializable {
    private javax.naming.Reference reference;

    private ResourceAdapter resourceAdapter;

    private int count;

    private String tsrValue;

    private String password;

    private String user;

    private String userName;
    private String eisPrincipalName;

    private String setterMethodVal = "DEFAULT";

    @ConfigProperty(defaultValue = "10", type = Integer.class, description = "Integer value", ignore = false)
    private Integer integer;

    @ConfigProperty()
    private String factoryName = "AnnoManagedConnectionFactory";

    /**
     * Default constructor.
     */
    public AnnoManagedConnectionFactory() {
        // this helps verify assertion Connector:SPEC:279 and Connector:SPEC:277
        String str = "AnnoManagedConnectionFactory factoryName=" + factoryName;
        ConnectorStatus.getConnectorStatus().logState(str);

        // lets make sure we can call and set setSetterMethodVal()
        setSetterMethodVal("NONDEFAULT");

        debug(str);
    }

    /**
     * Sets the setter method value.
     *
     * @param val the new value
     */
    @ConfigProperty()
    public void setSetterMethodVal(String val) {
        setterMethodVal = val;
        String str = "AnnotatedResourceAdapterImpl.setSetterMethodVal=" + setterMethodVal;
        ConnectorStatus.getConnectorStatus().logState(str);
    }

    /**
     * Gets the setter method value.
     *
     * @return the setter method value
     */
    public String getSetterMethodVal() {
        return setterMethodVal;
    }

    /**
     * Sets the factory name.
     *
     * @param name the new factory name
     */
    public void setFactoryName(String name) {
        this.factoryName = name;
    }

    /**
     * Gets the factory name.
     *
     * @return the factory name
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * Gets the integer value.
     *
     * @return the integer value
     */
    public Integer getInteger() {
        return this.integer;
    }

    /**
     * Sets the integer value.
     *
     * @param val the new integer value
     */
    public void setInteger(Integer val) {
        this.integer = val;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public String getUser() {
        debug("AnnoManagedConnectionFactory.getUser() returning:  " + user);
        return user;
    }

    /**
     * Sets the user.
     *
     * @param val the new user
     */
    public void setUser(String val) {
        debug("AnnoManagedConnectionFactory.setUser() with val = " + val);
        user = val;
    }

    public String getEisPrincipalName() {
        return eisPrincipalName;
    }

    public void setEisPrincipalName(String eisPrincipalName) {
        this.eisPrincipalName = eisPrincipalName;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        debug("AnnoManagedConnectionFactory.getUserName() returning:  " + userName);
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param val the new user name
     */
    public void setUserName(String val) {
        debug("AnnoManagedConnectionFactory.setUserName() with val = " + val);
        userName = val;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        debug("AnnoManagedConnectionFactory.getPassword() returning:  " + password);
        return password;
    }

    /**
     * Sets the password.
     *
     * @param val the new password
     */
    public void setPassword(String val) {
        debug("AnnoManagedConnectionFactory.setPassword() with val = " + val);
        password = val;
    }

    /**
     * Gets the TSR value.
     *
     * @return the TSR value
     */
    public String getTsrValue() {
        debug("AnnoManagedConnectionFactory getTsrValue called" + tsrValue);
        return tsrValue;
    }

    /**
     * Sets the TSR value.
     *
     * @param name the new TSR value
     */
    public void setTsrValue(String name) {
        debug("AnnoManagedConnectionFactory setTsrValue called" + name);
        this.tsrValue = name;
    }

    /**
     * Looks up the TSR.
     *
     * @param lookup the lookup string
     */
    public void lookupTSR(String lookup) {
        try {
            TSNamingContext ncxt = new TSNamingContext();
            String newStr = "java:".concat(lookup);
            Object obj = ncxt.lookup(newStr);
            if (obj != null) {
                debug("TSR NOT Null");
            } else {
                debug("TSR Null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new connection factory instance.
     *
     * @param cxManager the connection manager
     * @return the connection factory instance
     * @throws ResourceException if a resource exception occurs
     */
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new TSEISDataSource(this, cxManager);
    }

    /**
     * Creates a new connection factory instance.
     *
     * @return the connection factory instance
     * @throws ResourceException if a resource exception occurs
     */
    public Object createConnectionFactory() throws ResourceException {
        return new TSEISDataSource(this, null);
    }

    /**
     * Sets the resource adapter for this ManagedConnectionFactory.
     *
     * @param ra the resource adapter
     * @throws ResourceException if a resource exception occurs
     */
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        count++;
        String newStr1 = "AnnoManagedConnectionFactory setResourceAdapter " + count;
        debug(newStr1);
        this.resourceAdapter = ra;
    }

    /**
     * Gets the resource adapter for this ManagedConnectionFactory.
     *
     * @return the resource adapter
     */
    public ResourceAdapter getResourceAdapter() {
        debug("AnnoManagedConnectionFactory.getResource");
        return resourceAdapter;
    }

    /**
     * Creates a new managed connection to the underlying EIS.
     *
     * @param subject the subject
     * @param info the connection request info
     * @return the managed connection
     * @throws ResourceException if a resource exception occurs
     */
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo info) throws ResourceException {

        try {

            TSConnection con = null;
            PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
            if (pc == null) {
                debug("AnnoManagedConnectionFactory.createManagedConnection():  pc == null");
                debug("TSConnectionImpl.getConnection()");
                con = new TSConnectionImpl().getConnection();
            } else {
                debug("AnnoManagedConnectionFactory.createManagedConnection():  pc != null");
                setUser(pc.getUserName());
                setUserName(pc.getUserName());
                setPassword(new String(pc.getPassword()));
                debug("TSConnectionImpl.getConnection(u,p)");
                con = new TSConnectionImpl().getConnection(getEisPrincipalName(), pc.getPassword());
            }

            ManagedConnection mcon = new TSManagedConnection(this, pc, null, con, false, true);
            dumpConnectionMetaData(mcon);

            return mcon;
        } catch (Exception ex) {
            ResourceException re = new EISSystemException("Exception: " + ex.getMessage(), ex);
            throw re;
        }

    }

    /**
     * Dumps the connection metadata.
     *
     * @param mcon the managed connection
     */
    public void dumpConnectionMetaData(ManagedConnection mcon) {

        String hdr = "AnnoManagedConnectionFactory: ";
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

    /**
     * Returns the existing connection from the connection pool.
     *
     * @param connectionSet the connection set
     * @param subject the subject
     * @param info the connection request info
     * @return the managed connection
     * @throws ResourceException if a resource exception occurs
     */
    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject, ConnectionRequestInfo info)
            throws ResourceException {

        PasswordCredential pc = Util.getPasswordCredential(this, subject, info);
        Iterator it = connectionSet.iterator();

        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof TSManagedConnection mc) {
                ManagedConnectionFactory mcf = mc.getManagedConnectionFactory();
                if (Util.isPasswordCredentialEqual(mc.getPasswordCredential(), pc) && (mcf != null) && mcf.equals(this)) {
                    return mc;
                }
            }
        }

        System.out.println("matchManagedConnections: couldnt find match");
        return null;
    }

    /**
     * Sets the log writer.
     *
     * @param out the print writer
     * @throws ResourceException if a resource exception occurs
     */
    public void setLogWriter(PrintWriter out) throws ResourceException {
    }

    /**
     * Gets the log writer.
     *
     * @return the print writer
     * @throws ResourceException if a resource exception occurs
     */
    public PrintWriter getLogWriter() throws ResourceException {
        return null;
    }

    /**
     * Compares the given object to the ManagedConnectionFactory instance.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj) {

        if ((obj == null) || !(obj instanceof AnnoManagedConnectionFactory that)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        if ((this.reference != null) && !(this.reference.equals(that.getReference()))) {
            return false;
        } else if ((this.reference == null) && !(that.getReference() == null)) {
            return false;
        }

        if ((this.resourceAdapter != null) && !(this.resourceAdapter.equals(that.getResourceAdapter()))) {
            return false;
        } else if ((this.resourceAdapter == null) && !(that.getResourceAdapter() == null)) {
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

        return Util.isEqual(this.factoryName, that.getFactoryName());
    }

    /**
     * Gives a hash value to a ManagedConnectionFactory object.
     *
     * @return the hash value
     */
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    /**
     * Gives the reference of the class.
     *
     * @return the reference
     */
    public javax.naming.Reference getReference() {
        javax.naming.Reference ref;

        ref = this.reference;
        return ref;
    }

    /**
     * Sets the reference of the class.
     *
     * @param ref the new reference
     */
    public void setReference(javax.naming.Reference ref) {
        this.reference = ref;
    }

    /**
     * Debugging method.
     *
     * @param out the debug message
     */
    public void debug(String out) {
        Debug.trace("AnnoManagedConnectionFactory:  " + out);
    }

    /**
     * Gets the count.
     *
     * @return the count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Sets the count.
     *
     * @param val the new count
     */
    public void setCount(int val) {
        this.count = val;
    }

}