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

package com.sun.ts.tests.compat12.ejb.jspejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.integration.util.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public class AccountBean implements EntityBean {

  private EntityContext ectx = null;

  // Instance variable
  private double balance;

  public Integer ejbCreate(int account, double balance, boolean newTable,
      Properties p) throws RemoteException, CreateException {
    DBSupport DB = null;
    TestUtil.logTrace("ejbCreate");
    if (p != null) {
      try {
        TestUtil.logMsg("initialize remote logging");
        TestUtil.init(p);
      } catch (RemoteLoggingInitException e) {
        throw new CreateException(e.getMessage());
      }
    }

    // Initialize DB Table based on flag value
    try {
      TestUtil.logTrace("initialize DBSupport");
      DB = new DBSupport();
      TestUtil.logTrace("initialize database table");
      DB.initDB(newTable, false);
    } catch (Exception e) {
      throw new CreateException("Unable to initialize DB. Exception: " + e);
    }
    try {
      DB.insert(account, balance);
      this.balance = balance;
    } catch (Exception e) {
      throw new CreateException("unable to insert new entity");
    }
    return new Integer(account);
  }

  public void ejbPostCreate(int account, double balance, boolean newTable,
      Properties p) throws RemoteException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) throws RemoteException {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
  }

  public void unsetEntityContext() throws RemoteException {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbActivate() throws RemoteException {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() throws RemoteException {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbRemove() throws RemoteException, RemoveException {
    TestUtil.logTrace("ejbRemove");
    DBSupport DB = null;
    try {
      TestUtil.logTrace("initialize DBSupport");
      DB = new DBSupport();
      DB.delete(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (Exception se) {
      throw new RemoveException("unable to remove entity from database");
    }
  }

  public void ejbLoad() throws RemoteException {
    TestUtil.logTrace("ejbLoad");
    DBSupport DB = null;
    try {
      DB = new DBSupport();
      this.balance = DB.balance(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (Exception e) {
      e.printStackTrace();
      throw new EJBException("unable to obtain balance");
    }
  }

  public void ejbStore() throws RemoteException {
    TestUtil.logTrace("ejbStore");
    DBSupport DB = null;
    try {
      DB = new DBSupport();
      DB.updateAccount(((Integer) ectx.getPrimaryKey()).intValue(), balance);
    } catch (Exception e) {
      e.printStackTrace();
      throw new EJBException("unable to update account balance");
    }
  }

  public Integer ejbFindTheBean(Integer key, Properties p)
      throws RemoteException, FinderException {
    TestUtil.logTrace("ejbFindTheBean");
    DBSupport DB = null;
    try {
      if (p != null) {
        TestUtil.logMsg("Initialize remote logging");
        TestUtil.init(p);
      }
      DB = new DBSupport();
      boolean foundKey = DB.keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (RemoteLoggingInitException e) {
      throw new FinderException(e.getMessage());
    } catch (Exception se) {
      throw new FinderException("unable to obtain primary key existance");
    }
  }

  public Integer ejbFindByPrimaryKey(Integer key)
      throws RemoteException, FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");
    DBSupport DB = null;
    try {
      DB = new DBSupport();
      boolean foundKey = DB.keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (Exception se) {
      throw new FinderException("unable to obtain primary key existance");
    }
  }

  // ===========================================================
  // Account interface (our business methods)

  public double balance() {
    return balance;
  }

  public double deposit(double v) {
    balance += v;
    return balance;
  }

  public double withdraw(double v) {
    balance -= v;
    return balance;
  }

  public String sayHello() throws RemoteException {
    return "Hello There World From AccountBean!!!";
  }

  // ===========================================================
}
