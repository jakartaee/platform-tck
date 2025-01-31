/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.vehicle.appmanagedNoTx;

import java.util.Properties;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper;

import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;

@Stateful(name = "AppManagedNoTxVehicleBean")
@Remote({ AppManagedNoTxVehicleIF.class })
public class AppManagedNoTxVehicleBean extends com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean
        implements AppManagedNoTxVehicleIF, java.io.Serializable {

    /**
     * Default constructor for AppManagedNoTxVehicleBean.
     * Calls the superclass constructor.
     */
    public AppManagedNoTxVehicleBean() {
        super();
    }

    /**
     * Returns the type of the vehicle.
     *
     * @return the vehicle type as a String
     */
    protected String getVehicleType() {
        return APPMANAGEDNOTX;
    }

    private EntityManagerFactory emf;

    // ================== business methods ====================================
    /**
     * Runs the test with the given arguments and properties.
     *
     * @param args the arguments for the test
     * @param props the properties for the test
     * @return the RemoteStatus of the test run
     */
    @Remove
    public RemoteStatus runTest(String[] args, Properties props) {
        props.put("persistence.unit.name", "CTS-EM-NOTX");
        try {
            setEntityManager(emf.createEntityManager());
            RemoteStatus retValue;
            retValue = super.runTest(args, props);
            return retValue;
        } finally {
            try {
                if (getEntityManager().isOpen()) {
                    getEntityManager().close();
                }
            } catch (Exception e) {
                System.out.println("Exception caught during em.close()" + e);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////

    /**
     * Sets the session context.
     *
     * @param sessionContext the SessionContext to be set
     */
    @Resource
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * Sets the EntityManagerFactory.
     *
     * @param emf the EntityManagerFactory to be set
     */
    @PersistenceUnit(unitName = "CTS-EM-NOTX")
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        this.entityManagerFactory = emf;
    }

    /**
     * Sets the EntityManager.
     *
     * @param entityManager the EntityManager to be set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Returns the EntityTransaction.
     *
     * @return the EntityTransaction
     */
    protected EntityTransaction getEntityTransaction() {
        return new EntityTransactionWrapper(getEntityManager().getTransaction());
    }
}