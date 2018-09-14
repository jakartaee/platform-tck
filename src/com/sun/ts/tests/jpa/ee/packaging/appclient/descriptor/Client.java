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

package com.sun.ts.tests.jpa.ee.packaging.appclient.descriptor;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Properties;

public class Client extends EETest {

  private static final Coffee cRef[] = new Coffee[5];

  private EntityManagerFactory emf;

  private EntityManager em;

  private EntityTransaction et;

  private TSNamingContext nctx = null;

  private static final String emfRef = "java:comp/env/persistence/MyPersistenceUnit";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  public void setup(final String[] args, final Properties p) throws Fault {
    try {
      TestUtil.logTrace("Obtain naming context");
      nctx = new TSNamingContext();
      if (nctx == null) {
        TestUtil.logErr("NCTX is null");
        throw new Fault("Setup Failed!");
      }
      emf = (EntityManagerFactory) nctx.lookup(emfRef);
      if (emf != null) {
        em = emf.createEntityManager();
      } else {
        TestUtil.logErr("EMF is null");
        throw new Fault("Setup Failed!");
      }
      if (em == null) {
        TestUtil.logErr("EM is null");
        throw new Fault("Setup Failed!");
      }
      removeTestData();
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:906; PERSISTENCE:SPEC:937;
   * PERSISTENCE:SPEC:843; JavaEE:SPEC:10056; JavaEE:SPEC:10057;
   * JavaEE:SPEC:10058; PERSISTENCE:SPEC:949; PERSISTENCE:SPEC:950;
   * 
   * @test_Strategy: In JavaEE application client containers, only
   * application-managed entity managers are required to be used. [JTA is not
   * required to be supported in application client containers.]
   *
   * In JavaEE environment, the root of a persistence unit may be an application
   * client jar file The persistence.xml resides in the META-INF directory of
   * the client.jar
   *
   * RESOURCE_LOCAL Transaction Type Defined
   *
   * The EntityManagerFactory is obtained via JNDI lookup.
   *
   * The persistence-unit-ref elements are used in the client deployment
   * descriptor.
   *
   * Entities are described via orm.xml descriptor which also resides in the
   * META-INF directory of the client.jar.
   *
   * Deploy the client.jar to the application server with the above content.
   * Create entities, persist them, then find.
   *
   */

  public void test1() throws Fault {

    TestUtil.logTrace("Begin test1");
    boolean pass = true;

    try {

      TestUtil.logTrace("getEntityTransaction");
      if (null != em) {
        et = em.getTransaction();
        if (et != null) {

          TestUtil.logTrace("createTestData");
          et.begin();
          TestUtil.logTrace("Create 5 Coffees");
          cRef[0] = new Coffee(1, "hazelnut", 1.0F);
          cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
          cRef[2] = new Coffee(3, "decaf", 3.0F);
          cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
          cRef[4] = new Coffee(5, "mocha", 5.0F);

          TestUtil.logTrace("Start to persist coffees ");
          for (Coffee c : cRef) {
            if (c != null) {
              em.persist(c);
              TestUtil.logTrace("persisted coffee " + c);
            }
          }
          et.commit();

          TestUtil.logTrace("Clearing the persistence context");
          em.clear();

          et.begin();
          for (Coffee c : cRef) {
            if (c != null) {
              Coffee newcoffee = em.find(Coffee.class, c.getId());
              if (newcoffee != null) {
                em.remove(newcoffee);
                TestUtil.logTrace("removed coffee " + newcoffee);
              } else {
                TestUtil
                    .logErr("find of coffee[" + c.getId() + "] returned null");
                pass = false;
              }
            }
          }
          et.commit();
        } else {
          TestUtil.logErr("EntityTransaction is null");
          pass = false;
        }
      } else {
        TestUtil.logErr("EntityManager is null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating test data:", e);
      pass = false;
    } finally {
      try {
        if (et.isActive()) {
          et.rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in rollback:", re);
      }
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    emf.getCache().evictAll();

  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (null == em) {
      em = emf.createEntityManager();
    }
    if (null == et) {
      et = em.getTransaction();
    }
    if (et.isActive()) {
      et.rollback();
    }
    try {
      et.begin();
      em.createNativeQuery("DELETE FROM COFFEE").executeUpdate();
      et.commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (et.isActive()) {
          et.rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }

  }

}
