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

package com.sun.ts.tests.common.vehicle.ejb3share;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.SessionContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

/**
 * Abstract base class for EJB3 share beans. Provides common functionality for EJB3 share beans.
 */
abstract public class EJB3ShareBaseBean implements EJB3ShareIF {

    /**
     * The key for the test name in the properties.
     */
    public static final String FINDER_TEST_NAME_KEY = "testName";

    public static final String STATELESS3 = "stateless3";

    public static final String STATEFUL3 = "stateful3";

    public static final String APPMANAGED = "appmanaged";

    public static final String APPMANAGEDNOTX = "appmanagedNoTx";

    protected EntityManager entityManager;

    protected EntityManagerFactory entityManagerFactory;

    protected SessionContext sessionContext;

    /**
     * Returns the type of the vehicle.
     *
     * @return the vehicle type as a String
     */
    protected abstract String getVehicleType();

    /**
     * Default constructor for EJB3ShareBaseBean. Calls the superclass constructor.
     */
    protected EJB3ShareBaseBean() {
        super();
    }

    // ================== business methods ====================================
    /**
     * Runs the test with the given arguments and properties.
     *
     * @param args the arguments for the test
     * @param props the properties for the test
     * @return the RemoteStatus of the test run
     */
    public RemoteStatus runTest(String[] args, Properties props) {

        try {
            TestUtil.init(props);
        } catch (Exception e) {
            TestUtil.logErr("initLogging failed in " + getVehicleType() + " vehicle.", e);
        }

        String testName = getTestName(props);
        System.out.println("===== Starting " + testName + " in " + getVehicleType() + " =====");
        RemoteStatus sTestStatus = null;

        try {
            // create an instance of the test client and run here
            String testClassName = TestUtil.getProperty(props, "test_classname");
            Class c = Class.forName(testClassName);
            EETest testClient = (EETest) c.newInstance();

            initClient(testClient);
            sTestStatus = new RemoteStatus(testClient.run(args, props));
            if (sTestStatus.getType() == Status.PASSED)
                TestUtil.logMsg(testName + " in vehicle passed");
        } catch (Throwable e) {
            String fail = testName + " in vehicle failed";
            // e.printStackTrace();
            TestUtil.logErr(fail, e);
            sTestStatus = new RemoteStatus(Status.failed(fail));
        }
        return sTestStatus;
    }

    /**
     * Retrieves the test name from the properties.
     *
     * @param props the properties containing the test name
     * @return the test name as a String
     */
    protected String getTestName(Properties props) {
        String testName = TestUtil.getProperty(props, FINDER_TEST_NAME_KEY);
        if (testName == null) {
            testName = "test";
        }
        return testName;
    }

    /**
     * Initializes the test client.
     *
     * @param testClient the test client to be initialized
     */
    private void initClient(EETest testClient) {
        if (testClient instanceof UseEntityManager) {
            EntityManager em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("EntityManager is null");
            }
            UseEntityManager client2 = (UseEntityManager) testClient;
            EntityTransaction et = getEntityTransaction();
            client2.setEntityManager(em);
            client2.setEntityTransaction(et);
            client2.setInContainer(true);
        }

        if (testClient instanceof UseEntityManagerFactory) {
            EntityManagerFactory emf = getEntityManagerFactory();
            if (emf != null) {
                UseEntityManagerFactory client2 = (UseEntityManagerFactory) testClient;
                client2.setEntityManagerFactory(emf);
            }
        }

    }

    /**
     * Returns the session context.
     *
     * @return the SessionContext
     */
    public SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
     * Sets the session context.
     *
     * @param sessionContext the SessionContext to be set
     */
    abstract public void setSessionContext(SessionContext sessionContext);

    /**
     * Returns the entity manager.
     *
     * @return the EntityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Returns the entity manager factory.
     *
     * @return the EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Sets the entity manager factory.
     *
     * @param emf the EntityManagerFactory to be set
     */
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        // do nothing this gets overridden in subclass
    }

    /**
     * Returns the entity transaction.
     *
     * @return the EntityTransaction
     */
    abstract protected EntityTransaction getEntityTransaction();

    /**
     * Sets the entity manager.
     *
     * @param entityManager the EntityManager to be set
     */
    abstract public void setEntityManager(EntityManager entityManager);
}