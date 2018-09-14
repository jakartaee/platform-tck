/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.override.callbacklistener;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.override.util.CallBackCounts;

import java.util.Properties;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {

      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: postLoad
   * 
   * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
   * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
   * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
   * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
   * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
   * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
   * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
   * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
   * 
   * @test_Strategy: CallBack methods are tested using callback listeners.
   */
  public void postLoad() throws Fault {
    boolean pass3 = false;
    final Long ID = 1L;

    CallBackCounts.clearCountsMap();
    OverridenCallBack entity = new OverridenCallBack();
    entity.setId(ID);
    getEntityTransaction().begin();
    getEntityManager().persist(entity);
    getEntityManager().flush();
    getEntityManager().refresh(entity);
    getEntityManager().remove(entity);
    getEntityTransaction().commit();
    TestUtil.logTrace("persisted entity" + entity);
    try {
      pass3 = checkLoadCallBacks();

      if (pass3) {
        TestUtil.logTrace("testOverrideCallBackMethods Passed");
      } else {
        throw new Fault("Test failed while testing postLoad method");
      }
    } catch (Exception e) {
      throw new Fault("Exception thrown while testing postLoad" + e);
    }
  }

  /*
   * @testName: preAndPostPersist
   * 
   * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
   * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
   * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
   * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
   * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
   * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
   * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
   * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
   * 
   * @test_Strategy: CallBack methods are tested using callback listeners.
   */
  public void preAndPostPersist() throws Fault {
    boolean pass1 = false;
    final Long ID = 1L;

    CallBackCounts.clearCountsMap();
    OverridenCallBack entity = new OverridenCallBack();
    entity.setId(ID);
    getEntityTransaction().begin();
    getEntityManager().persist(entity);
    getEntityManager().flush();
    getEntityManager().remove(entity);
    getEntityTransaction().commit();
    TestUtil.logTrace("persisted entity" + entity);
    try {
      pass1 = checkPersistCallBacks();

      if (pass1) {
        TestUtil.logTrace("testOverrideCallBackMethods Passed");
      } else {
        throw new Fault("Test failed while testing prepersist and "
            + "postpersist methods");
      }
    } catch (Exception e) {
      throw new Fault("Exception thrown while testing preAndPostPersist" + e);
    }
  }

  /*
   * @testName: preAndPostRemove
   * 
   * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:698;
   * PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:702;
   * PERSISTENCE:SPEC:703; PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707;
   * PERSISTENCE:SPEC:708; PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710;
   * PERSISTENCE:SPEC:711; PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713;
   * PERSISTENCE:SPEC:714; PERSISTENCE:SPEC:715; PERSISTENCE:SPEC:716;
   * PERSISTENCE:SPEC:717; PERSISTENCE:SPEC:718; PERSISTENCE:SPEC:719;
   * PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:721;
   * 
   * @test_Strategy: CallBack methods are tested using callback listeners.
   */
  public void preAndPostRemove() throws Fault {
    boolean pass2 = false;
    final Long ID = 1L;

    CallBackCounts.clearCountsMap();
    OverridenCallBack entity = new OverridenCallBack();
    entity.setId(ID);
    getEntityTransaction().begin();
    getEntityManager().persist(entity);
    getEntityManager().flush();
    getEntityManager().remove(entity);
    getEntityManager().flush();
    getEntityTransaction().commit();
    TestUtil.logTrace("persisted entity" + entity);
    try {
      pass2 = checkRemoveCallBacks();

      if (pass2) {
        TestUtil.logTrace("testOverrideCallBackMethods Passed");
      } else {
        throw new Fault(
            "Test failed while testing preremove and " + "postremove methods");
      }
    } catch (Exception e) {
      throw new Fault("Exception thrown while testing preAndPostRemove" + e);
    }
  }

  private boolean checkPersistCallBacks() throws Fault {
    boolean result = false;
    if (test("prePersistFromXML", 1) && test("postPersistFromXML", 1)) {
      result = true;
    }
    return result;

  }

  private boolean checkRemoveCallBacks() throws Fault {
    boolean result = false;
    if (test("preRemoveFromXML", 1) && test("postRemoveFromXML", 1)) {
      result = true;
    }
    return result;
  }

  private boolean checkLoadCallBacks() throws Fault {
    boolean result = false;
    if (test("postLoadFromXML", 1)) {
      result = true;
    }
    return result;
  }

  private boolean test(final String callBackName, final int expectedCount)
      throws Fault {

    String thisTestId = callBackName;
    boolean pass = false;

    int count = CallBackCounts.getCount(callBackName);
    if (count == expectedCount) {
      TestUtil.logTrace("test passed in test method" + thisTestId);
      pass = true;

    } else {
      TestUtil.logErr("test failed in test method" + thisTestId);
      TestUtil.logTrace("in CallBackName =" + callBackName);
      TestUtil.logTrace("in expectedCount =" + expectedCount);
      TestUtil.logTrace("in ActualCount =" + count);
    }

    return pass;

  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM NOENTITYLISTENER_TABLE")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
