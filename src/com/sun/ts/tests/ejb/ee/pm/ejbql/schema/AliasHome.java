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

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public interface AliasHome extends EJBHome {
  public Alias create(String id, String alias)
      throws RemoteException, CreateException;

  public Alias findByPrimaryKey(String key)
      throws RemoteException, FinderException;

  public Collection findAllAliases() throws RemoteException, FinderException;

  public Collection findAliasesByQuery2()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery3(String s, int i, int j)
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery4()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery5()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery6()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery7()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery8(String s)
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery9(String s)
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery10()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery11()
      throws RemoteException, FinderException;

  public Collection findAliasesByQuery12()
      throws RemoteException, FinderException;

  public Collection findCustomerAliasesByOrder()
      throws RemoteException, FinderException;

  public Collection selectNullAlias(String s) throws RemoteException;
}
