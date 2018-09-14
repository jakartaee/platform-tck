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
package com.sun.ts.tests.ejb.ee.pm.ejbql.tx;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import com.sun.ts.lib.util.*;

/**
 * The TxCommonBean is an entity EJB. Most of the business methods of the
 * TxCommonBean class do not access the database. Instead, these business
 * methods update the instance variables, which are written to the database when
 * the EJB Container calls ejbStore().
 */

public interface TxCommonBean extends EJBObject {

  public String getBrandName() throws RemoteException;

  public void setBrandName(String bName) throws RemoteException;

  public float getPrice() throws RemoteException;

  public void setPrice(float p) throws RemoteException;

  public void updateBrandName(String newBrandName) throws RemoteException;
}
