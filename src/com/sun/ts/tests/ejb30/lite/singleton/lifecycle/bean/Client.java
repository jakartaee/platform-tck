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
package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean;

import java.util.Vector;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

/**
 * singleton EJBs are used in many other test directories. This directory covers
 * requirements very specific to singleton, including equals, not equals,
 * hashCode, error handling, injection, jndi lookup.
 * 
 * These tests, as many others in ejb30/lite, extensively use no-interface
 * beans. Therefore, many no-interface requirements are covered here.
 */
public class Client extends EJBLiteClientBase {

  @EJB(name = "ejb/aSingleton", beanName = "ASingletonBean")
  private ASingletonBean aSingleton; // no interface

  @EJB(name = "ejb/bSingleton", beanName = "BSingletonBean")
  private BSingletonIF bSingleton; // 1 interface

  @EJB(name = "ejb/cSingleton", beanName = "CSingletonBean")
  private CSingletonIF cSingleton; // c interface 1

  @EJB(name = "ejb/c2Singleton", beanName = "CSingletonBean")
  private C2SingletonIF c2Singleton; // c interface 2

  @EJB(beanName = "StatefulBean")
  private StatefulBean stateful; // no interfaces

  /*
   * @testName: beanReferenceEqualsAndHashCodeA
   * 
   * @test_Strategy: All references to a no-interface singleton bean are equal.
   * Also compare their hashCode.
   */
  public void beanReferenceEqualsAndHashCodeA() {
    beanReferenceEqualsAndHashCode(aSingleton);
  }

  /*
   * @testName: beanReferenceEqualsAndHashCodeB
   * 
   * @test_Strategy: All references to a 1-interface singleton bean are equal.
   * Also compare their hashCode.
   */
  public void beanReferenceEqualsAndHashCodeB() {
    beanReferenceEqualsAndHashCode(bSingleton);
  }

  /*
   * @testName: beanReferenceEqualsAndHashCodeC2
   * 
   * @test_Strategy: All references to a singleton bean that are of the same
   * type are equal. Also compare their hashCode.
   */
  public void beanReferenceEqualsAndHashCodeC2() {
    beanReferenceEqualsAndHashCode(c2Singleton);
  }

  /*
   * @testName: beanReferenceEqualsAndHashCodeA_2
   * 
   * @test_Strategy: same as previous test, except using EJBContext.lookup.
   */
  public void beanReferenceEqualsAndHashCodeA_2() {
    beanReferenceEqualsAndHashCodeFromEJBContext(aSingleton);
  }

  /*
   * @testName: beanReferenceEqualsAndHashCodeB_2
   * 
   * @test_Strategy: same as previous test, except using EJBContext.lookup.
   */
  public void beanReferenceEqualsAndHashCodeB_2() {
    beanReferenceEqualsAndHashCodeFromEJBContext(bSingleton);
  }

  /*
   * @testName: beanReferenceEqualsAndHashCodeC2_2
   * 
   * @test_Strategy: same as previous test, except using EJBContext.lookup.
   */
  public void beanReferenceEqualsAndHashCodeC2_2() {
    beanReferenceEqualsAndHashCodeFromEJBContext(c2Singleton);
  }

  /*
   * @testName: beanReferencesNotEqual
   * 
   * @test_Strategy: references to different interface types are not equal.
   */
  public void beanReferencesNotEqual() {
    appendReason(
        "References to singletons of different interface types are not equal.");
    assertNotEquals(null, aSingleton, null);
    assertNotEquals(null, bSingleton, null);
    assertNotEquals(null, cSingleton, null);
    assertNotEquals(null, c2Singleton, null);
    assertNotEquals(null, c2Singleton, 1);
    assertNotEquals(null, c2Singleton, false);

    assertNotEquals(null, c2Singleton, cSingleton);
    assertNotEquals(null, cSingleton, c2Singleton);
    assertNotEquals(null, aSingleton, bSingleton);
  }

  /*
   * @testName: beanClassHashCodeA
   * 
   * @test_Strategy: create n threads to concurrently invoke ASingletonBean, get
   * its hashCode. These hashCode values must be the same.
   */
  public void beanClassHashCodeA() {
    beanClassHashCode(aSingleton, "ejb/aSingleton", "ASingletonBean", null);
  }

  /*
   * @testName: beanClassHashCodeB
   * 
   * @test_Strategy: create n threads to concurrently invoke BSingletonBean, get
   * its hashCode. These hashCode values must be the same.
   */
  public void beanClassHashCodeB() {
    beanClassHashCode(bSingleton, "ejb/bSingleton", "BSingletonBean", null);
  }

  /*
   * @testName: beanClassHashCodeC2
   * 
   * @test_Strategy: create n threads to concurrently invoke C2SingletonBean,
   * get its hashCode. These hashCode values must be the same.
   */
  public void beanClassHashCodeC2() {
    beanClassHashCode(c2Singleton, "ejb/c2Singleton", "CSingletonBean",
        CSingletonIF.class);
  }

  /*
   * @testName: beanClassHashCodeCC2
   * 
   * @test_Strategy: create n threads to concurrently invoke C2SingletonBean,
   * get its hashCode. These hashCode values must be the same as that of
   * CSingletonIF.
   */
  public void beanClassHashCodeCC2() {
    beanClassHashCode(cSingleton, "ejb/c2Singleton", "CSingletonBean",
        C2SingletonIF.class);
  }

  /*
   * @testName: noDestructionAfterSystemException
   * 
   * @test_Strategy: System exceptions from singleton business methods do not
   * cause the bean destruction.
   */
  public void noDestructionAfterSystemException() {
    noDestructionAfterSystemException(aSingleton);
    noDestructionAfterSystemException(bSingleton);
    noDestructionAfterSystemException(cSingleton);
    noDestructionAfterSystemException(c2Singleton);
  }

  private void noDestructionAfterSystemException(CommonSingletonIF b) {
    int expected = b.identityHashCode();
    try {
      b.error();
      throw new RuntimeException("Expecting EJBException, but got none.");
    } catch (EJBException ignore) {
    }
    assertEquals("noDestructionAfterSystemException", expected,
        b.identityHashCode());
  }

  private void beanClassHashCode(final CommonSingletonIF b,
      final String lookupName, final String beanName,
      final Class<?> beanInterface) {
    CommonSingletonIF resultInSameThread = (CommonSingletonIF) lookup(
        lookupName, beanName, beanInterface);
    Helper.getLogger()
        .info("When look up in parent thread: " + resultInSameThread);
    final int numOfClients = 50;
    final Vector<Integer> hashCodes = new Vector<Integer>();
    Thread[] threads = new Thread[numOfClients];

    for (int i = 0; i < threads.length; i++) {
      final CommonSingletonIF sg = (CommonSingletonIF) lookup(lookupName,
          beanName, beanInterface);
      threads[i] = new Thread(new Runnable() {
        public void run() {
          try {
            hashCodes.add(sg.identityHashCode());
          } catch (Exception ex) {
            Helper.getLogger().info("Failed to look up " + lookupName
                + " in a spawned thread. " + ex);
            Helper.getLogger().log(Level.FINER, null, ex);
          }
        }
      });

      threads[i].start();
    } // end for

    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
    }
    int expected = b.identityHashCode();
    appendReason("Compare hashCodes from multiple invocations: " + hashCodes);
    assertEquals("check hashCodes.size", numOfClients, hashCodes.size());
    for (int i = 0, j = hashCodes.size(); i < j; i++) {
      assertEquals(null, expected, hashCodes.get(i));
    }
  }

  private void beanReferenceEqualsAndHashCode(CommonSingletonIF single) {
    CommonSingletonIF fromA = aSingleton
        .getSingletonReference(single.getClass());
    CommonSingletonIF fromB = bSingleton
        .getSingletonReference(single.getClass());
    CommonSingletonIF fromC = cSingleton
        .getSingletonReference(single.getClass());
    CommonSingletonIF fromS = stateful.getSingletonReference(single.getClass());

    appendReason(
        "Compare references to a no-interface singleton bean and hashCode.");
    beanReferenceEqualsAndHashCode0(single, fromA, fromB, fromC, fromS);
  }

  private void beanReferenceEqualsAndHashCodeFromEJBContext(
      CommonSingletonIF single) {
    CommonSingletonIF fromA = aSingleton
        .getSingletonReferenceFromEJBContext(single.getClass());
    CommonSingletonIF fromB = bSingleton
        .getSingletonReferenceFromEJBContext(single.getClass());
    CommonSingletonIF fromC = cSingleton
        .getSingletonReferenceFromEJBContext(single.getClass());
    CommonSingletonIF fromS = stateful
        .getSingletonReferenceFromEJBContext(single.getClass());

    appendReason(
        "Compare references to a no-interface singleton bean and hashCode.");
    beanReferenceEqualsAndHashCode0(single, fromA, fromB, fromC, fromS);
  }

  private void beanReferenceEqualsAndHashCode0(CommonSingletonIF original,
      CommonSingletonIF... compareTos) {
    for (CommonSingletonIF compareTo : compareTos) {
      assertEquals(null, original, compareTo);
      assertEquals(null, original.hashCode(), compareTo.hashCode());
    }
  }

}
