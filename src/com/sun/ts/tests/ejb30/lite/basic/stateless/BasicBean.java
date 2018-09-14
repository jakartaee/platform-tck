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

package com.sun.ts.tests.ejb30.lite.basic.stateless;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanBase;

@Stateless
public class BasicBean extends BasicBeanBase {

  // just to verify both SessionContext and EJBContext can be injected
  // They are also in the superclass BasicBeanBase, which should cause no
  // conflict
  @SuppressWarnings("unused")
  @Resource
  private SessionContext sessionContext;

  @SuppressWarnings("unused")
  @Resource
  private EJBContext ejbContext;

}
