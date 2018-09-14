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

package com.sun.ts.tests.ejb30.common.migration.threetwo;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

@Stateful(name = "StatefulThreeTestBean")
@Remote(ThreeTestIF.class)
public class StatefulThreeTestBean extends ThreeTestBeanBase
    implements ThreeTestIF {

  @EJB(name = "twoRemoteHome")
  private TwoRemoteHome twoRemoteHome;

  // need to override it in ejb-jar.xml to provide the target ejb type info
  @EJB(name = "twoLocalHome")
  // private TwoLocalHome twoLocalHome;
  private Object twoLocalHome;

  @Resource(name = "sctx")
  private SessionContext sctx;

  public StatefulThreeTestBean() {
  }

  public void remove() {
  }

  protected TwoRemoteHome getTwoRemoteHome() {
    return (TwoRemoteHome) (sctx.lookup("twoRemoteHome"));
  }

  protected TwoLocalHome getTwoLocalHome() {
    return (TwoLocalHome) (sctx.lookup("twoLocalHome"));
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sctx;
  }
}
