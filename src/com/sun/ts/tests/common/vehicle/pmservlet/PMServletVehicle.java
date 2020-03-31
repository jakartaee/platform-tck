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
package com.sun.ts.tests.common.vehicle.pmservlet;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory;
import com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.transaction.UserTransaction;

@PersistenceContexts({
    @PersistenceContext(name = "persistence/CTS-EM", unitName = "CTS-EM"),
    @PersistenceContext(name = "persistence/CTS-EM2", unitName = "CTS-EM2") })
public class PMServletVehicle extends ServletVehicle {

  private static final String EM_LOOKUP_NAME = "java:comp/env/persistence/CTS-EM";

  private UserTransaction ut;

  @PersistenceUnit(unitName = "CTS-EM")
  EntityManagerFactory emf;

  public EntityTransaction getEntityTransaction() {
    try {
      TSNamingContext nctx = new TSNamingContext();
      ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");
    } catch (Exception e) {
      TestUtil.logMsg("Naming service exception: " + e.getMessage());
      e.printStackTrace();
    }
    return new UserTransactionWrapper(ut);
  }

  private Object lookup(String lookupName) throws IllegalStateException {
    Object result = null;
    try {
      Context context = new InitialContext();
      result = context.lookup(lookupName);
    } catch (NamingException e) {
      throw new IllegalStateException("Failed to lookup:" + lookupName, e);
    }
    return result;
  }

  protected RemoteStatus runTest() {
    properties.put("persistence.unit.name", "CTS-EM");

    RemoteStatus sTestStatus = new RemoteStatus(Status.passed(""));

    try {
      // call EETest impl's run method
      if (testObj instanceof UseEntityManager) {

        // lookup EntityManager for each http request,
        // so it's not shared by multiple threads
        EntityManager em = (EntityManager) lookup(EM_LOOKUP_NAME);

        if (em == null) {
          throw new IllegalStateException("EntityManager is null");
        }
        UseEntityManager client2 = (UseEntityManager) testObj;
        EntityTransaction et = getEntityTransaction();
        client2.setEntityManager(em);
        client2.setEntityTransaction(et);
        client2.setInContainer(true);
      }

      if (testObj instanceof UseEntityManagerFactory) {

        if (emf == null) {
          throw new IllegalStateException("EntityManagerFactory is null");
        }
        UseEntityManagerFactory client2 = (UseEntityManagerFactory) testObj;
        client2.setEntityManagerFactory(emf);
      }

      sTestStatus = new RemoteStatus(testObj.run(arguments, properties));

      if (sTestStatus.getType() == Status.PASSED) {
        System.out.println("Test running in pmservlet vehicle passed");
      } else {
        System.out.println("Test running in pmservlet vehicle failed");
      }
    } catch (Throwable e) {
      sTestStatus = new RemoteStatus(
          Status.failed("Test running in pmservlet vehicle failed"));
      e.printStackTrace();
    }

    return sTestStatus;
  }
}
