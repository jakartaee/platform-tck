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
package com.sun.ts.tests.common.vehicle.puservlet;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

public class PUServletVehicle extends ServletVehicle {

  @PersistenceUnit(unitName = "CTS-EM-NOTX")
  EntityManagerFactory emf;

  protected RemoteStatus runTest() {
    RemoteStatus sTestStatus = new RemoteStatus(Status.passed(""));
    properties.put("persistence.unit.name", "CTS-EM-NOTX");

    EntityManager em = null;
    try {
      // call EETest impl's run method
      if (testObj instanceof UseEntityManager) {
        em = emf.createEntityManager();
        if (em == null) {
          throw new IllegalStateException("EntityManager is null");
        }
        UseEntityManager client2 = (UseEntityManager) testObj;
        EntityTransaction et = em.getTransaction();
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
        System.out
            .println("Test running in PersistenceUnit servlet  vehicle passed");
      } else {
        System.out
            .println("Test running in PersistenceUnit servlet vehicle failed");
      }
    } catch (Throwable e) {
      sTestStatus = new RemoteStatus(Status
          .failed("Test running in PersistenceUnit servlet vehicle failed"));
      e.printStackTrace();
    } finally {
      if (em != null) {
        if (em.isOpen())
          em.close();
      }
    }

    return sTestStatus;
  }
}
