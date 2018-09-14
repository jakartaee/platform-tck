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

package com.sun.ts.tests.jpa.core.callback.inheritance;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.core.callback.common.Constants;
import com.sun.ts.tests.jpa.core.callback.common.EntityCallbackClientBase;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;
import java.util.Arrays;

public class Client extends EntityCallbackClientBase {
  private PricedPartProduct p1;

  private PricedPartProduct_2 p2;

  public Client() {
    super();
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

  private PricedPartProduct_2 newPricedPartProduct_2(final String testName) {
    PricedPartProduct_2 product = new PricedPartProduct_2();
    product.setTestName(testName);
    product.setId(testName);
    product.setName(testName);
    product.setPartNumber(1L);
    product.setPrice(1D);
    product.setQuantity(1);
    return product;
  }

  private PricedPartProduct newPricedPartProduct(final String testName) {
    PricedPartProduct product = new PricedPartProduct();
    product.setTestName(testName);
    product.setId(testName);
    product.setName(testName);
    product.setPartNumber(1L);
    product.setPrice(1D);
    product.setQuantity(1);
    return product;
  }

  /*
   * @testName: prePersistTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:708;
   * PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:997; PERSISTENCE:SPEC:999;
   * PERSISTENCE:SPEC:998
   * 
   * @test_Strategy:
   */
  public void prePersistTest() throws Fault {
    String reason;
    final String testName = Constants.prePersistTest;
    try {
      getEntityTransaction().begin();
      p1 = newPricedPartProduct(testName);
      getEntityManager().persist(p1);

      if (p1.isPrePersistCalled()) {
        reason = "PricedPartProduct: prePersist was called.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct: prePersist was not called.";
        throw new Fault(reason);
      }
      getEntityTransaction().rollback();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during prePersistTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: prePersistTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:708
   * 
   * @test_Strategy:
   */
  public void prePersistTest2() throws Fault {
    String reason;
    final String testName = Constants.prePersistTest2;
    try {
      getEntityTransaction().begin();
      p2 = newPricedPartProduct_2(testName);
      getEntityManager().persist(p2);

      if (p2.isPrePersistCalled()) {
        reason = "PricedPartProduct_2: prePersist was called.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct_2: prePersist was not called.";
        throw new Fault(reason);
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during prePersistTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: preRemoveTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:708;
   * 
   * @test_Strategy:
   */
  public void preRemoveTest() throws Fault {
    String reason;
    final String testName = Constants.preRemoveTest;
    try {
      getEntityTransaction().begin();
      p1 = newPricedPartProduct(testName);
      getEntityManager().persist(p1);
      getEntityManager().remove(p1);

      if (p1.isPreRemoveCalled()) {
        reason = "PricedPartProduct: preRemove was called.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct: preRemove was not called.";
        throw new Fault(reason);
      }
      p1 = null;
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during prePersistTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: preRemoveTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:708
   * 
   * @test_Strategy:
   */
  public void preRemoveTest2() throws Fault {
    String reason;
    final String testName = Constants.preRemoveTest2;
    try {
      getEntityTransaction().begin();
      p2 = newPricedPartProduct_2(testName);
      getEntityManager().persist(p2);
      getEntityManager().remove(p2);

      if (p2.isPreRemoveCalled()) {
        reason = "PricedPartProduct: preRemove was called.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct: preRemove was not called.";
        throw new Fault(reason);
      }
      p2 = null;
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during prePersistTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: preUpdateTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:716;
   * 
   * @test_Strategy:
   */
  public void preUpdateTest() throws Fault {
    final String testName = Constants.preUpdateTest;
    try {
      getEntityTransaction().begin();
      p1 = newPricedPartProduct(testName);
      getEntityManager().persist(p1);
      getEntityManager().flush();
      p1.setPrice(2D);
      getEntityManager().persist(p1);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during preUpdateTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: preUpdateTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:716
   * 
   * @test_Strategy:
   */
  public void preUpdateTest2() throws Fault {
    final String testName = Constants.preUpdateTest2;
    try {
      getEntityTransaction().begin();
      p2 = newPricedPartProduct_2(testName);
      getEntityManager().persist(p2);
      getEntityManager().flush();
      p2.setPrice(2D);
      getEntityManager().persist(p2);
      getEntityManager().flush();

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during preUpdateTest2", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: postLoadTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:719;
   * 
   * @test_Strategy:
   */
  public void postLoadTest() throws Fault {
    String reason;
    final String testName = Constants.postLoadTest;
    try {
      getEntityTransaction().begin();
      p1 = newPricedPartProduct(testName);
      getEntityManager().persist(p1);
      getEntityManager().flush();
      getEntityManager().refresh(p1);
      Query q = getEntityManager()
          .createQuery("select distinct p from PricedPartProduct p");
      java.util.List results = q.getResultList();
      // for(int i = 0, n = results.size(); i < n; i++) {
      //
      // }
      TestUtil.logTrace(results.toString());

      if (p1.isPostLoadCalled()) {
        reason = "PricedPartProduct: postLoad was called after the query result was returned.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct: postLoad was not called even after the query result was returned.";
        throw new Fault(reason);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception caught during postLoadTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: postLoadTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:700; PERSISTENCE:SPEC:719
   * 
   * @test_Strategy:
   */
  public void postLoadTest2() throws Fault {
    String reason;
    final String testName = Constants.postLoadTest2;
    try {
      getEntityTransaction().begin();
      p2 = newPricedPartProduct_2(testName);
      getEntityManager().persist(p2);
      getEntityManager().flush();
      getEntityManager().refresh(p2);
      Query q = getEntityManager()
          .createQuery("select p.id from PricedPartProduct_2 p");
      java.util.List results = q.getResultList();
      // for(int i = 0, n = results.size(); i < n; i++) {
      //
      // }
      TestUtil.logTrace(results.toString());

      if (p2.isPostLoadCalled()) {
        reason = "PricedPartProduct_2: postLoad was called after the query result was returned.";
        TestUtil.logTrace(reason);
      } else {
        reason = "PricedPartProduct_2: postLoad was not called even after the query result was returned.";
        throw new Fault(reason);
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Exception caught during postLoadTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  /*
   * @testName: findProductTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:735; PERSISTENCE:JAVADOC:27
   * 
   * @test_Strategy:
   */
  public void findProductTest() throws Fault {
    final String testName = "findProductTest";
    try {
      getEntityTransaction().begin();
      p1 = newPricedPartProduct(testName);
      getEntityManager().persist(p1);
      getEntityManager().flush();
      Object o = getEntityManager().find(Product.class, testName);
      TestUtil.logTrace(
          "finding object using Product.class and id '" + testName + "'");

      if (o instanceof PricedPartProduct) {
        TestUtil.logTrace("Found object of type " + PricedPartProduct.class);
      } else if (o instanceof Product) {
        TestUtil.logTrace("Found object of type " + Product.class);
      } else {
        TestUtil.logTrace("The object found is neither "
            + PricedPartProduct.class + ", nor " + Product.class);
      }

      final Object oo = getEntityManager().find(PricedPartProduct.class,
          testName);
      TestUtil.logTrace("finding object using PricedPartProduct.class and id '"
          + testName + "'");
      if (oo instanceof PricedPartProduct) {
        TestUtil.logTrace("Found object of type " + PricedPartProduct.class);
      } else if (oo instanceof Product) {
        TestUtil.logTrace("Found object of type " + Product.class);
      } else {
        TestUtil.logTrace("The object found is neither "
            + PricedPartProduct.class + ", nor " + Product.class);
      }

      if (o == oo) {
        TestUtil.logTrace("The two entities are identical");
      } else if (o.equals(oo)) {
        TestUtil.logTrace("The two entities are equal");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during findProductTest", e);
      throw new Fault(e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Exception caught while rolling back TX", re);
      }
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
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
      getEntityManager().createNativeQuery("DELETE FROM PRICED_PRODUCT_TABLE")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE")
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
