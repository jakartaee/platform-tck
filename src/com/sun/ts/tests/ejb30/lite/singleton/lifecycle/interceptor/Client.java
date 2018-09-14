/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import java.util.Vector;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.EJBException;

/**
 * Interceptors of singleton beans have the same lifecycle behavior as that
 * singleton. Use identityHashCode to verify different instances. Though
 * identityHashCode is not guaranteed to be unique, but it's reasonable and
 * common practice for them to be unique.
 */
public class Client extends EJBLiteClientBase {
  private static final int NUM_OF_CONCURRENT_REQUESTS = 1000;

  private static final String[] INTERCEPTORS = new String[] {
      Interceptor0.class.getSimpleName(), Interceptor1.class.getSimpleName(),
      Interceptor2.class.getSimpleName(), Interceptor3.class.getSimpleName() };

  @EJB(name = "ejb/aSingleton", beanName = "ASingletonBean")
  private ASingletonBean aSingleton; // no interface

  @EJB(name = "ejb/cSingleton", beanName = "CSingletonBean")
  private CSingletonIF cSingleton; // c interface 1

  @EJB(name = "ejb/c2Singleton", beanName = "CSingletonBean")
  private C2SingletonIF c2Singleton; // c interface 2

  /*
   * @testName: sameInterceptorInstanceA
   * 
   * @test_Strategy: Only 1 instance of each interceptor is associated with
   * ASingletonBean.
   */
  public void sameInterceptorInstanceA() {
    for (String i : INTERCEPTORS) {
      sameInterceptorInstance(aSingleton, i);
    }
  }

  /*
   * @testName: sameInterceptorInstanceC
   * 
   * @test_Strategy: Only 1 instance of each interceptor is associated with
   * CSingletonBean.
   */
  public void sameInterceptorInstanceC() {
    for (String i : INTERCEPTORS) {
      sameInterceptorInstance(cSingleton, i, c2Singleton.identityHashCode(i));
    } // use c2's interceptor as the expected id for c

  }

  /*
   * @testName: noDestructionAfterSystemExceptionA
   * 
   * @test_Strategy: interceptors are not destructed after system exception.
   */
  public void noDestructionAfterSystemExceptionA() {
    for (String i : INTERCEPTORS) {
      noDestructionAfterSystemException(aSingleton, i);
    }
  }

  /*
   * @testName: noDestructionAfterSystemExceptionC
   * 
   * @test_Strategy: interceptors are not destructed after system exception.
   */
  public void noDestructionAfterSystemExceptionC() {
    for (CommonSingletonIF b : new CommonSingletonIF[] { c2Singleton,
        cSingleton }) {
      for (String i : INTERCEPTORS) {
        noDestructionAfterSystemException(b, i);
      }
    }
  }

  /*
   * @testName: differentInterceptorInstance
   * 
   * @test_Strategy: interceptors are not shared between beans
   */
  public void differentInterceptorInstance() {
    appendReason(
        "Check different interceptor instances of the same type associated with different singletons.");
    for (String i : INTERCEPTORS) {
      assertNotEquals(null, aSingleton.identityHashCode(i),
          cSingleton.identityHashCode(i));
      assertNotEquals(null, aSingleton.identityHashCode(i),
          c2Singleton.identityHashCode(i));
    }
  }

  private void noDestructionAfterSystemException(final CommonSingletonIF b,
      final String interceptorName) {
    int id1 = b.identityHashCode(interceptorName);
    try {
      b.error(interceptorName);
    } catch (EJBException e) {
      Throwable cause = e.getCausedByException();
      if (cause instanceof IllegalStateException) {
        throw new RuntimeException(
            "Expecting a RuntimeException from one of the interceptors, but got IllegalStateException from bean class"
                + cause.getMessage());
      } else {
        appendReason(
            "Got expected RuntimeException from one of the interceptors");
      }
    }
    int id2 = b.identityHashCode(interceptorName);
    assertEquals("Check ids for interceptor " + interceptorName, id1, id2);
  }

  private void sameInterceptorInstance(final CommonSingletonIF b,
      final String interceptorName, final int... expectedVals) {
    Thread[] threads = new Thread[NUM_OF_CONCURRENT_REQUESTS];
    final Vector<Integer> ids = new Vector<Integer>();
    for (int i = 0; i < NUM_OF_CONCURRENT_REQUESTS; i++) {
      threads[i] = new Thread(new Runnable() {
        public void run() {
          ids.add(b.identityHashCode(interceptorName));
        }
      });
      threads[i].start();
    }
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException ex) {
        Helper.getLogger().log(Level.SEVERE, null, ex);
      }
    }
    assertEquals("Checking # of ids. ", NUM_OF_CONCURRENT_REQUESTS, ids.size());

    int expected;
    if (expectedVals.length == 0) {
      expected = b.identityHashCode(interceptorName);
    } else {
      expected = expectedVals[0];
    }

    for (int i : ids) {
      if (i != expected) {// only report failure
        assertEquals("Compare id for interceptor " + interceptorName, expected,
            i);
      }
    }
  }
}
