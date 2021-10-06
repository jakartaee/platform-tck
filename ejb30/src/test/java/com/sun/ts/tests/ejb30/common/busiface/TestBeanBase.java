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

package com.sun.ts.tests.ejb30.common.busiface;

abstract public class TestBeanBase implements TestIF {
  abstract protected BusinessLocalIF1 getLocalBean1();

  abstract protected BusinessLocalIF2 getLocalBean2();

  // not to use abstract since this class is extended by multiple test
  // directories that
  // may not have this kind of tests. Test directories that support these
  // tests should
  // override the two methods to return a meaningful value.
  protected AnnotatedLocalBusinessInterface1 getAnnotatedLocalInterfaceBean1() {
    return null;
  }

  protected AnnotatedLocalBusinessInterface2 getAnnotatedLocalInterfaceBean2() {
    return null;
  }

  abstract protected BusinessLocalIF1 getSerializableLocalBean();

  abstract protected BusinessLocalIF1 getExternalizableLocalBean();

  abstract protected BusinessLocalIF1 getTimedObjectLocalBean();

  abstract protected BusinessLocalIF1 getSessionSynchronizationLocalBean();

  abstract protected BusinessLocalIF1 getSessionBeanLocalBean();

  public String[] multipleInterfacesLocalTest2(String[] s) {
    final BusinessLocalIF2 b = getLocalBean2();
    final String[] result = b.businessMethodLocal2(s);
    b.remove();
    return result;
  }

  public String[] multipleInterfacesLocalTest1(String[] s) {
    final BusinessLocalIF1 b = getLocalBean1();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  public String[] multipleAnnotatedInterfacesLocalTest2(String[] s) {
    final AnnotatedLocalBusinessInterface2 b = getAnnotatedLocalInterfaceBean2();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  public String[] multipleAnnotatedInterfacesLocalTest1(String[] s) {
    final AnnotatedLocalBusinessInterface1 b = getAnnotatedLocalInterfaceBean1();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  public String[] singleInterfaceLocalExternalizableTest(String[] s) {
    final BusinessLocalIF1 b = getExternalizableLocalBean();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  public String[] singleInterfaceLocalSerializableTest(String[] s) {
    final BusinessLocalIF1 b = getSerializableLocalBean();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  public String[] singleInterfaceLocalSessionBeanTest(String[] s) {
    final BusinessLocalIF1 b = getSessionBeanLocalBean();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  // slsb only
  public String[] singleInterfaceLocalTimedObjectTest(String[] s) {
    final BusinessLocalIF1 b = getTimedObjectLocalBean();
    final String[] result = b.businessMethodLocal1(s);
    // no need to call remove, since it's for slsb only
    // b.remove();
    return result;

  }

  // sfsb only
  public String[] singleInterfaceLocalSessionSynchronizationTest(String[] s) {
    final BusinessLocalIF1 b = getSessionSynchronizationLocalBean();
    final String[] result = b.businessMethodLocal1(s);
    b.remove();
    return result;
  }

  // this method should be overridden by stateful bean with @Remove
  public void remove() {
  }

}
