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

package com.sun.ts.tests.jpa.core.override.attributeoverride;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

  private static final int ENTITY_ID = 3039;

  private static final String NAME = "Cheese";

  private static final String PUBLISHER = "Johnson";

  private static final int COST = 20;

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
      TestUtil.logErr("Exception:test failed ", e);
    }
  }

  /*
   * @testName: testNoAttributeOverrideAnnotation
   * 
   * @assertion_ids: PERSISTENCE:SPEC:594; PERSISTENCE:SPEC:596;
   * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
   * PERSISTENCE:SPEC:600; PERSISTENCE:SPEC:601;
   * 
   * @test_Strategy: LawBook is an entity which extends a class Book. A column
   * "name" is overriden in Orm.xml as "BOOK_NAME". The following test tests for
   * the same.
   */
  public void testNoAttributeOverrideAnnotation() throws Fault {

    LawBook book = new LawBook();
    getEntityTransaction().begin();
    book.setCategory("Motivational");
    book.setId(ENTITY_ID);
    book.setName(NAME);
    book.setPublisher(PUBLISHER);
    book.setCost(COST);
    getEntityManager().persist(book);
    getEntityManager().flush();
    try {
      List result = getEntityManager()
          .createQuery("SELECT b FROM LawBook b where b.name= " + ":name")
          .setParameter("name", NAME).getResultList();
      if (result.size() == 1) {
        TestUtil.logTrace("test Overriding Attributes passed");
      } else {
        throw new Fault(
            "Expected the size to be 1 " + " but it is -" + result.size());
      }
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testNoAttributeOverrideAnnotation"
              + e);
    } finally {
      getEntityManager().remove(book);
      getEntityTransaction().commit();
    }
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
      getEntityManager().createNativeQuery("DELETE FROM LAWBOOK")
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
