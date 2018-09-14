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

package com.sun.ts.tests.ejb30.common.callback;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.ArrayList;
import java.util.List;
import org.omg.CORBA.ORB;

abstract public class ClientBase2NoAnnotation extends ClientBase {
  // @Resource
  private static ORB orbInClientBase2;

  protected static final String BASE2 = "BASE2";

  protected static final String BASE3 = "BASE3";

  protected static final String CLIENT = "CLIENT";

  protected static final String[] BASE2_BASE3_CLIENT = new String[] { BASE2,
      BASE3, CLIENT };

  private static final List postConstructCalls = new ArrayList();

  private static final List injectedFields = new ArrayList();

  public ClientBase2NoAnnotation() {
  }

  public static List getPostConstructCalls() {
    return postConstructCalls;
  }

  public static void addPostConstructCall(String s) {
    postConstructCalls.add(s);
  }

  /**
   * When this method is invoked, all injections, including those in this class,
   * and all of its subclasses, should have been completed.
   */
  // @PostConstruct
  private static void postConstructInBase2NoAnnotation() {
    addPostConstructCall(BASE2);
    // check injected fields
    if (orbInClientBase2 != null) {
      addInjectedField(orbInClientBase2);
    } else {
      TLogger.log("WARNING: ClientBase2.orbInClientBase2 has not been "
          + "initialized when checking inside ClientBase2.postConstructInBase2()");
    }
  }

  public static List getInjectedFields() {
    return injectedFields;
  }

  public static void addInjectedField(Object obj) {
    injectedFields.add(obj);
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: appclientPostConstructCallsCount
   * 
   * @test_Strategy:
   */
  public void appclientPostConstructCallsCount() throws Fault {
    int expectedAppclientPostConstructCallsCount = 3;
    int actual = getPostConstructCalls().size();
    if (expectedAppclientPostConstructCallsCount == actual) {
      TLogger.log("Got expected appclientPostConstructCallsCount:"
          + expectedAppclientPostConstructCallsCount);
    } else {
      throw new Fault("Expecting appclientPostConstructCallsCount "
          + expectedAppclientPostConstructCallsCount + ", but actual is "
          + actual);
    }
  }

  /*
   * testName: appclientPostConstructCallOrder
   * 
   * @test_Strategy:
   */
  public void appclientPostConstructCallOrder() throws Fault {
    List actual = getPostConstructCalls();
    Helper.compareResultList(BASE2_BASE3_CLIENT, actual);
  }

  /*
   * testName: appclientInjectionCompleteInPostConstruct
   * 
   * @test_Strategy:
   */
  public void appclientInjectionCompleteInPostConstruct() throws Fault {
    // ClientBase2.orbInClientBase2, ClientBase2.orbInClientBase3,
    // Client.orbInClient
    int expectedNumOfElements = 3;
    List actual = getInjectedFields();
    if (actual.size() == 3) {
      TLogger
          .log("Got expected " + expectedNumOfElements + " fields: " + actual);
    } else {
      throw new Fault("Expected " + expectedNumOfElements
          + " fields to be injected, but actual injected fields are " + actual);
    }
  }

}
