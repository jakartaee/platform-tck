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

package com.sun.ts.tests.common.vehicle.stateless3;

import com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.*;

@Stateless(name = "Stateless3VehicleBean")
@PersistenceContext(name = "STATELESS3EM", unitName = "CTS-EM")
@Remote({ Stateless3VehicleIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class Stateless3VehicleBean
    extends com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean
    implements Stateless3VehicleIF {

  @PersistenceUnit(name = "STATELESS3EMF", unitName = "CTS-EM")
  EntityManagerFactory emf;

  public Stateless3VehicleBean() {
    super();
  }

  protected String getVehicleType() {
    return STATELESS3;
  }

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @PostConstruct
  public void init() {
    try {
      System.out.println("In PostContruct");
      EntityManager entityManager = (EntityManager) sessionContext
          .lookup("STATELESS3EM");
      EntityManagerFactory emf = (EntityManagerFactory) sessionContext
          .lookup("STATELESS3EMF");
      setEntityManager(entityManager);
      setEntityManagerFactory(emf);
    } catch (Exception e) {
      System.out.println("ERROR: "
          + " In PostConstruct: Exception caught while setting EntityManager");
      e.printStackTrace();
    }
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void setEntityManagerFactory(EntityManagerFactory emf) {
    this.entityManagerFactory = emf;
  }

  protected EntityTransaction getEntityTransaction() {
    return new UserTransactionWrapper(sessionContext.getUserTransaction());
  }
}
