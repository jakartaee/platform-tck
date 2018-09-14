/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.entityManagerFactoryCloseExceptions;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import java.util.*;

public class Client extends PMClientBase {

  Properties props = null;

  public Client() {
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    this.props = p;
    try {
      super.setup(args, p);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    super.cleanup();
  }

  public void nullCleanup() throws Fault {
  }

  /*
   * @testName: exceptionsTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:536; PERSISTENCE:JAVADOC:538;
   * PERSISTENCE:JAVADOC:537; PERSISTENCE:JAVADOC:531; PERSISTENCE:JAVADOC:532;
   * PERSISTENCE:JAVADOC:533; PERSISTENCE:JAVADOC:534; PERSISTENCE:JAVADOC:535
   * 
   * @test_Strategy: Close the EntityManagerFactory, then call various methods
   */
  @CleanupMethod(name = "nullCleanup")
  public void exceptionsTest() throws Fault {
    int passCount = 0;
    Map<String, Object> myMap = new HashMap<String, Object>();
    myMap.put("some.cts.specific.property", "nothing.in.particular");

    EntityManagerFactory emf;
    TestUtil.logMsg("Getting EntityManagerFactory");
    if (isStandAloneMode()) {
      emf = getEntityManager().getEntityManagerFactory();
    } else {
      emf = getEntityManagerFactory();
    }
    if (emf != null) {
      if (emf.isOpen()) {
        TestUtil.logMsg("EMF is open, now closing it");
        emf.close();
      } else {
        TestUtil.logMsg("EMF is already closed");
      }

      TestUtil.logMsg("Testing getMetamodel() after close");
      try {
        emf.getMetamodel();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing emf.getProperties()");
      try {
        emf.getProperties();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing getPersistenceUnitUtil() after close");
      try {
        emf.getPersistenceUnitUtil();
        TestUtil.logErr("Did no throw IllegalStateException");
      } catch (IllegalStateException ise) {
        TestUtil.logTrace("Received expected IllegalStateException");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing close after close ");
      try {
        emf.close();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException e) {
        TestUtil.logTrace("IllegalStateException Caught as Expected.");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing createEntityManager() after close");
      try {
        emf.createEntityManager();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException e) {
        TestUtil.logTrace("IllegalStateException Caught as Expected.");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing createEntityManager(Map) after close");
      try {
        emf.createEntityManager(myMap);
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException e) {
        TestUtil.logTrace("IllegalStateException Caught as Expected.");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      TestUtil.logMsg("Testing getCache after close ");
      try {
        emf.getCache();
        TestUtil.logErr("IllegalStateException not thrown");
      } catch (IllegalStateException e) {
        TestUtil.logTrace("IllegalStateException Caught as Expected.");
        passCount++;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }

      try {
        emf.getCriteriaBuilder();
        TestUtil.logErr("IllegalStateException was not thrown");
      } catch (IllegalStateException ise) {
        passCount++;
        TestUtil.logTrace("Received expected IllegalStateException");
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception occurred", e);
      }
    } else {
      TestUtil.logErr("Could not obtain an EntityManagerFactory");
    }
    if (passCount != 8) {
      throw new Fault("exceptionsTest failed");
    }
  }

}
