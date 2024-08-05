/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

abstract public class ClientBase2NoAnnotation extends ClientBase {

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
  public void appclientPostConstructCallsCount() throws Exception {
    int expectedAppclientPostConstructCallsCount = 3;
    int actual = getPostConstructCalls().size();
    if (expectedAppclientPostConstructCallsCount == actual) {
      TLogger.log("Got expected appclientPostConstructCallsCount:"
          + expectedAppclientPostConstructCallsCount);
    } else {
      throw new Exception("Expecting appclientPostConstructCallsCount "
          + expectedAppclientPostConstructCallsCount + ", but actual is "
          + actual);
    }
  }

  /*
   * testName: appclientPostConstructCallOrder
   * 
   * @test_Strategy:
   */
  public void appclientPostConstructCallOrder() throws Exception {
    List actual = getPostConstructCalls();
    Helper.compareResultList(BASE2_BASE3_CLIENT, actual);
  }

}
