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

package com.sun.ts.tests.common.vehicle.ejb3share;

import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.RemoteStatus;
import java.util.Properties;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

abstract public class EJB3ShareBaseBean implements EJB3ShareIF {
  public static final String FINDER_TEST_NAME_KEY = "testName";

  public static final String STATELESS3 = "stateless3";

  public static final String STATEFUL3 = "stateful3";

  public static final String APPMANAGED = "appmanaged";

  public static final String APPMANAGEDNOTX = "appmanagedNoTx";

  protected EntityManager entityManager;

  protected EntityManagerFactory entityManagerFactory;

  protected SessionContext sessionContext;

  protected abstract String getVehicleType();

  protected EJB3ShareBaseBean() {
    super();
  }

  // ================== business methods ====================================
  public RemoteStatus runTest(String[] args, Properties props) {

    try {
      TestUtil.init(props);
    } catch (Exception e) {
      TestUtil.logErr("initLogging failed in " + getVehicleType() + " vehicle.",
          e);
    }

    String testName = getTestName(props);
    System.out.println(
        "===== Starting " + testName + " in " + getVehicleType() + " =====");
    RemoteStatus sTestStatus = null;

    try {
      // create an instance of the test client and run here
      Class c = Class.forName(props.getProperty("test_classname"));
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

  protected String getTestName(Properties props) {
    String testName = props.getProperty(FINDER_TEST_NAME_KEY);
    if (testName == null) {
      testName = "test";
    }
    return testName;
  }

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

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  abstract public void setSessionContext(SessionContext sessionContext);

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return entityManagerFactory;
  }

  public void setEntityManagerFactory(EntityManagerFactory emf) {
    // do nothing this gets overridden in subclass
  }

  abstract protected EntityTransaction getEntityTransaction();

  abstract public void setEntityManager(EntityManager entityManager);
}
