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

package com.sun.ts.tests.jpa.ee.entityManager;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.*;

public class Client extends PMClientBase {

  Properties props = null;

  Map map = new HashMap<String, Object>();

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();

    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
      map.putAll(getEntityManager().getProperties());
      map.put("foo", "bar");
      displayMap(map);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  /*
   * @testName: createEntityManagerSynchronizationTypeMapTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3318; PERSISTENCE:SPEC:1801;
   * PERSISTENCE:SPEC:1804; PERSISTENCE:SPEC:1883.2;
   * 
   * @test_Strategy: Create an EntityManagerFactory via SynchronizationType,Map
   */
  public void createEntityManagerSynchronizationTypeMapTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Test UNSYNCHRONIZED");
      EntityManager em1 = getEntityManagerFactory()
          .createEntityManager(SynchronizationType.UNSYNCHRONIZED, map);
      if (em1 != null) {
        TestUtil.logTrace("Received non-null EntityManager");
        pass1 = true;
      } else {
        TestUtil.logErr("Received null EntityManager");
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    try {
      TestUtil.logMsg("Test SYNCHRONIZED");
      EntityManager em2 = getEntityManagerFactory()
          .createEntityManager(SynchronizationType.SYNCHRONIZED, map);
      if (em2 != null) {
        TestUtil.logTrace("Received non-null EntityManager");
        pass2 = true;
      } else {
        TestUtil.logErr("Received null EntityManager");
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("createEntityManagerSynchronizationTypeMapTest failed");
    }
  }

  /*
   * @testName: createEntityManagerSynchronizationTypeTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:3322;
   * 
   * @test_Strategy: Create an EntityManagerFactory via SynchronizationType
   */
  public void createEntityManagerSynchronizationTypeTest() throws Fault {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Test UNSYNCHRONIZED");
      EntityManager em1 = getEntityManagerFactory()
          .createEntityManager(SynchronizationType.UNSYNCHRONIZED);
      if (em1 != null) {
        TestUtil.logTrace("Received non-null EntityManager");
        pass1 = true;
      } else {
        TestUtil.logErr("Received null EntityManager");
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    try {
      TestUtil.logMsg("Test SYNCHRONIZED");
      EntityManager em2 = getEntityManagerFactory()
          .createEntityManager(SynchronizationType.SYNCHRONIZED);
      if (em2 != null) {
        TestUtil.logTrace("Received non-null EntityManager");
        pass2 = true;
      } else {
        TestUtil.logErr("Received null EntityManager");
      }
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass1 || !pass2) {
      throw new Fault("createEntityManagerSynchronizationTypeTest failed");
    }
  }

  /*
   * @testName: joinTransactionTransactionRequiredExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:489
   * 
   * @test_Strategy: Call EntityManager.joinTransaction() method when no
   * transaction exists
   */
  public void joinTransactionTransactionRequiredExceptionTest() throws Fault {
    boolean pass = false;
    try {

      getEntityManager().joinTransaction();
      TestUtil.logErr("TransactionRequiredException not thrown");
    } catch (TransactionRequiredException e) {
      TestUtil.logTrace("TransactionRequiredException Caught as Expected.");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Fault("joinTransactionTransactionRequiredExceptionTest failed");
    }
  }
}
