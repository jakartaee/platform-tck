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

//Do not annotate this interface.  The appropriate @Remote annotations are used on bean classes
public interface TestIF {
  public String[] multipleInterfacesLocalTest1(String[] s);

  public String[] multipleInterfacesLocalTest2(String[] s);

  public String[] multipleAnnotatedInterfacesLocalTest1(String[] s);

  public String[] multipleAnnotatedInterfacesLocalTest2(String[] s);

  public String[] singleInterfaceLocalSerializableTest(String[] s);

  public String[] singleInterfaceLocalExternalizableTest(String[] s);

  public String[] singleInterfaceLocalSessionBeanTest(String[] s);

  // slsb only
  public String[] singleInterfaceLocalTimedObjectTest(String[] s);

  // sfsb only
  public String[] singleInterfaceLocalSessionSynchronizationTest(String[] s);

  public void remove();
}
