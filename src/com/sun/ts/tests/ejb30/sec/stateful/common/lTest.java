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

package com.sun.ts.tests.ejb30.sec.stateful.common;

public interface lTest {
  public boolean IsCallerB1(String caller);

  public boolean InRole(String role);

  public boolean EjbNotAuthz();

  public boolean EjbIsAuthz();

  public boolean EjbSecRoleRef(String role);

  public boolean EjbSecRoleRef1(String role);

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2);

  public boolean EjbSecRoleRefScope(String role);

  public boolean checktest1();

  public boolean excludetest1();
}
