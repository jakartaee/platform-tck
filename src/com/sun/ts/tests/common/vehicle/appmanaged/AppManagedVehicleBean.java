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

package com.sun.ts.tests.common.vehicle.appmanaged;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.util.Properties;

@Stateful(name = "AppManagedVehicleBean")
@Remote({ AppManagedVehicleIF.class })
public class AppManagedVehicleBean
    extends com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean
    implements AppManagedVehicleIF, java.io.Serializable {

  public AppManagedVehicleBean() {
    super();
  }

  protected String getVehicleType() {
    return APPMANAGED;
  }

  private EntityManagerFactory emf;

  // ================== business methods ====================================
  @Remove
  public RemoteStatus runTest(String[] args, Properties props) {
    props.put("persistence.unit.name", "CTS-EM");
    try {
      setEntityManager(emf.createEntityManager());
      RemoteStatus retValue;
      retValue = super.runTest(args, props);
      return retValue;
    } finally {
      try {
        if (getEntityManager().isOpen()) {
          getEntityManager().close();
        }
      } catch (Exception e) {
        System.out.println("Exception caught during em.close()" + e);
      }
    }
  }
  /////////////////////////////////////////////////////////////////////////

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @PersistenceUnit(unitName = "CTS-EM")
  public void setEntityManagerFactory(EntityManagerFactory emf) {
    this.emf = emf;
    this.entityManagerFactory = emf;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  protected EntityTransaction getEntityTransaction() {
    return new NoopTransactionWrapper();
  }
}
