/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.bb.session.stateless.migration.twothree.override;

import com.sun.ts.tests.ejb30.common.migration.twothree.MigrationBeanBase;
import com.sun.ts.tests.ejb30.common.migration.twothree.ThreeLocalIF;
import javax.annotation.Resource;
import jakarta.ejb.EJBContext;
import jakarta.ejb.SessionContext;
import com.sun.ts.tests.ejb30.common.migration.twothree.ThreeIF;
import com.sun.ts.tests.ejb30.common.migration.twothree.TwoLocalHome;
import com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteHome;
import jakarta.ejb.Local;
import jakarta.ejb.LocalHome;
import jakarta.ejb.Remote;
import jakarta.ejb.RemoteHome;
import jakarta.ejb.Stateless;

//meta data declared in descriptor, too
@Stateless(name = "MigrationBean")
@Remote({ ThreeIF.class })
@Local({ ThreeLocalIF.class })
@RemoteHome(TwoRemoteHome.class)
@LocalHome(TwoLocalHome.class)
public class MigrationBean extends MigrationBeanBase implements ThreeIF {

  // injection declared in descriptor, too
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  protected EJBContext getEJBContext() {
    return sessionContext;
  }

  public MigrationBean() {
  }

  public void remove() {
  }

}
