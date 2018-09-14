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

package com.sun.ts.tests.jpa.core.entityTransaction;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.io.FileInputStream;
import java.util.*;

public class Client extends PMClientBase {

  Properties props = null;

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
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    // Nothing to cleanup
    TestUtil.logTrace("done cleanup, calling super.cleanup");
    super.cleanup();
  }

  /*
   * @testName: beginIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:539; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call begin twice and verify IllegalStateException is thrown
   */
  public void beginIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      EntityTransaction t = getEntityTransaction();
      t.begin();
      if (t.isActive()) {
        try {
          t.begin();
          TestUtil.logErr("IllegalStateException was not thrown");
        } catch (IllegalStateException ise) {
          TestUtil.logTrace("IllegalStateException Caught as Expected.");
          pass = true;
        }
      } else {
        TestUtil
            .logErr("isActive() returned false when a transaction was active");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("beginIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: commitIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:540; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call commit without an active transaction verify
   * IllegalStateException is thrown
   */
  public void commitIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      EntityTransaction t = getEntityTransaction();
      if (!t.isActive()) {
        try {
          t.commit();
          TestUtil.logErr("IllegalStateException was not thrown");
        } catch (IllegalStateException ise) {
          TestUtil.logTrace("IllegalStateException Caught as Expected.");
          pass = true;
        }
      } else {
        TestUtil.logErr(
            "isActive() returened true when no transaction  was active");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("commitIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: getRollbackOnlyIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:542; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call getRollbackOnly without an active transaction verify
   * IllegalStateException is thrown
   */
  public void getRollbackOnlyIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      EntityTransaction t = getEntityTransaction();
      if (!t.isActive()) {
        try {
          t.getRollbackOnly();
          TestUtil.logErr("IllegalStateException was not thrown");
        } catch (IllegalStateException ise) {
          TestUtil.logTrace("IllegalStateException Caught as Expected.");
          pass = true;
        }
      } else {
        TestUtil
            .logErr("isActive() returned true when no transaction  was active");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("getRollbackOnlyIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: rollbackIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:544; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call rollback without an active transaction verify
   * IllegalStateException is thrown
   */
  public void rollbackIllegalStateExceptionTest() throws Fault {
    boolean pass = false;

    try {
      EntityTransaction t = getEntityTransaction();
      if (!t.isActive()) {
        try {
          t.rollback();
          TestUtil.logErr("IllegalStateException was not thrown");
        } catch (IllegalStateException ise) {
          TestUtil.logTrace("IllegalStateException Caught as Expected.");
          pass = true;
        }
      } else {
        TestUtil
            .logErr("isActive() returned true when no transaction  was active");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }
    if (!pass) {
      throw new Fault("rollbackIllegalStateExceptionTest failed");
    }
  }

  /*
   * @testName: setRollbackOnlyIllegalStateExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:546; PERSISTENCE:SPEC:608;
   * 
   * @test_Strategy: Call setRollbackOnly without an active transaction verify
   * IllegalStateException is thrown
   */
  public void setRollbackOnlyIllegalStateExceptionTest() throws Fault {
    boolean pass = false;
    try {
      EntityTransaction t = getEntityTransaction();
      if (!t.isActive()) {
        try {
          t.setRollbackOnly();
          TestUtil.logErr("IllegalStateException was not thrown");
        } catch (IllegalStateException ise) {
          TestUtil.logTrace("IllegalStateException Caught as Expected.");
          pass = true;
        }
      } else {
        TestUtil
            .logErr("isActive() returned true when no transaction  was active");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("setRollbackOnlyIllegalStateExceptionTest failed");
    }
  }

}
