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

import javax.transaction.xa.XAResource;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.Util;

import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.AuthenticationMechanism;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.ConfigProperty;
import jakarta.resource.spi.Connector;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterInternalException;
import jakarta.resource.spi.SecurityPermission;
import jakarta.resource.spi.TransactionSupport;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import jakarta.resource.spi.work.HintsContext;
import jakarta.resource.spi.work.SecurityContext;
import jakarta.resource.spi.work.Work;
import jakarta.resource.spi.work.WorkManager;

/**
 * This is a sample resource adapter that will use no ra.xml info. This RA is used to assist with verifying the server
 * supports annotations when there is no ra.xml (Assertion 268) and the transaction support is Local.
 *
 */
@Connector(description = "CTS Test Resource Adapter with No DD", displayName = "whitebox-anno_no_md.rar", vendorName = "Java Software", eisType = "TS EIS", version = "1.6", licenseDescription = "CTS License Required", licenseRequired = true, authMechanisms = @AuthenticationMechanism(credentialInterface = AuthenticationMechanism.CredentialInterface.PasswordCredential, authMechanism = "BasicPassword", description = "Basic Password Authentication"), reauthenticationSupport = false, securityPermissions = @SecurityPermission(description = "Security Perm description"), transactionSupport = TransactionSupport.TransactionSupportLevel.LocalTransaction, requiredWorkContexts = {
        HintsContext.class, SecurityContext.class })
public class AnnotatedResourceAdapterImpl implements ResourceAdapter, java.io.Serializable {

    private transient BootstrapContext bsc;

    private transient AnnoWorkManager awm;

    private transient WorkManager wm;

    private transient Work work;

    private String serverSideUser = ""; // corresponds to ts.jte's 'user' property

    private String serverSidePwd = ""; // corresponds to ts.jte's 'password' property

    private String eisUser = ""; // corresponds to ts.jte's 'user1' property

    private String eisPwd = ""; // corresponds to ts.jte's 'password' property

    @ConfigProperty(defaultValue = "AnnotatedResourceAdapterImpl")
    private String raName;

    /**
     * Constructor for AnnotatedResourceAdapterImpl.
     */
    public AnnotatedResourceAdapterImpl() {
        debug("enterred constructor...");

        this.serverSideUser = TestUtil.getSystemProperty("j2eelogin.name");
        this.serverSidePwd = TestUtil.getSystemProperty("j2eelogin.password");
        this.eisUser = TestUtil.getSystemProperty("eislogin.name");
        this.eisPwd = TestUtil.getSystemProperty("eislogin.password");
        debug("AnnotatedResourceAdapterImpl, eisUser: "+eisUser);

        debug("leaving constructor...");
    }

    //
    // Begin ResourceAdapter interface requirements
    //

    /**
     * Starts the resource adapter.
     *
     * @param bsc the bootstrap context
     * @throws ResourceAdapterInternalException if an internal error occurs
     */
    public void start(BootstrapContext bsc) throws ResourceAdapterInternalException {
        debug("enterred start");

        ConnectorStatus.getConnectorStatus().logState("AnnotatedResourceAdapterImpl.start called");

        this.bsc = bsc;
        this.wm = bsc.getWorkManager();

        this.awm = new AnnoWorkManager(bsc);
        awm.runTests();

        debug("leaving start");
    }

    /**
     * Stops the resource adapter.
     */
    public void stop() {
        debug("entered stop");
        debug("leaving stop");
    }

    /**
     * Activates an endpoint.
     *
     * @param factory the message endpoint factory
     * @param spec the activation spec
     * @throws NotSupportedException if the operation is not supported
     */
    public void endpointActivation(MessageEndpointFactory factory, ActivationSpec spec) throws NotSupportedException {
        debug("enterred endpointActivation");
        debug("leaving endpointActivation");
    }

    /**
     * Deactivates an endpoint.
     *
     * @param ep the message endpoint factory
     * @param spec the activation spec
     */
    public void endpointDeactivation(MessageEndpointFactory ep, ActivationSpec spec) {
        debug("enterred endpointDeactivation");
        debug("leaving endpointDeactivation");
    }

    /**
     * Gets the XA resources.
     *
     * @param specs the activation specs
     * @return an array of XA resources
     * @throws ResourceException if a resource error occurs
     */
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        debug("enterred getXAResources");
        debug("leaving getXAResources");

        throw new UnsupportedOperationException();
    }

    //
    // END ResourceAdapter interface requirements
    //

    /**
     * Compares this object with the given object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AnnotatedResourceAdapterImpl)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        AnnotatedResourceAdapterImpl that = (AnnotatedResourceAdapterImpl) obj;

        if (!Util.isEqual(this.serverSideUser, that.getServerSideUser()))
            return false;

        if (!Util.isEqual(this.serverSidePwd, that.getServerSidePwd()))
            return false;

        if (!Util.isEqual(this.eisUser, that.getEisUser()))
            return false;

        if (!Util.isEqual(this.eisPwd, that.getEisPwd()))
            return false;

        if (!Util.isEqual(this.raName, that.getRaName()))
            return false;

        return true;
    }

    /**
     * Gets the hashcode for this object.
     *
     * @return the hashcode
     */
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    /**
     * Sets the resource adapter name.
     *
     * @param name the new resource adapter name
     */
    public void setRaName(String name) {
        this.raName = name;

        // this helps verify assertion Connector:SPEC:279
        String str = "setRAName called with raname=" + raName;
        ConnectorStatus.getConnectorStatus().logState(str);
        debug(str);
    }

    /**
     * Gets the resource adapter name.
     *
     * @return the resource adapter name
     */
    public String getRaName() {
        debug("AnnotatedResourceAdapterImpl.getRAName");
        return raName;
    }

    /**
     * Debugging method.
     *
     * @param out the debug message
     */
    public void debug(String out) {
        Debug.trace("AnnotatedResourceAdapterImpl:  " + out);
    }

    /**
     * Sets the server-side user.
     *
     * @param val the new server-side user
     */
    public void setServerSideUser(String val) {
        this.serverSideUser = val;
    }

    /**
     * Gets the server-side user.
     *
     * @return the server-side user
     */
    public String getServerSideUser() {
        return this.serverSideUser;
    }

    /**
     * Sets the server-side password.
     *
     * @param val the new server-side password
     */
    public void setServerSidePwd(String val) {
        this.serverSidePwd = val;
    }

    /**
     * Gets the server-side password.
     *
     * @return the server-side password
     */
    public String getServerSidePwd() {
        return this.serverSidePwd;
    }

    /**
     * Sets the EIS user.
     *
     * @param val the new EIS user
     */
    public void setEisUser(String val) {
        this.eisUser = val;
    }

    /**
     * Gets the EIS user.
     *
     * @return the EIS user
     */
    public String getEisUser() {
        return this.eisUser;
    }

    /**
     * Sets the EIS password.
     *
     * @param val the new EIS password
     */
    public void setEisPwd(String val) {
        this.eisPwd = val;
    }

    /**
     * Gets the EIS password.
     *
     * @return the EIS password
     */
    public String getEisPwd() {
        return this.eisPwd;
    }

}