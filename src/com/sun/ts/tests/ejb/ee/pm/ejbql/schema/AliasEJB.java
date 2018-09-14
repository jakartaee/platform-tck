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
 * @(#)AliasEJB.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class AliasEJB implements EntityBean {

  private static final String CustomerLocal = "java:comp/env/ejb/CustomerLocal";

  private static final String Customer = "java:comp/env/ejb/Customer";

  private EntityContext ectx = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getAlias();

  public abstract void setAlias(String v);

  // ===========================================================
  // getters and setters for CMR fields

  // ONExONE
  public abstract CustomerLocal getCustomerNoop();

  public abstract void setCustomerNoop(CustomerLocal v);

  // ONExMANY
  public abstract Collection getCustomersNoop();

  public abstract void setCustomersNoop(Collection v);

  // MANYxMANY
  public abstract Collection getCustomers();

  public abstract void setCustomers(Collection v);

  // ===========================================================
  // select methods

  public abstract Collection ejbSelectNullAlias(String aName)
      throws FinderException;

  // =========================================================
  public String ejbCreate(String id, String alias) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setAlias(alias);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String alias) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public Collection ejbHomeSelectNullAlias(String aName) {
    TestUtil.logTrace("ejbHomeSelectNullAlias");
    try {
      Collection s = ejbSelectNullAlias(aName);
      return s;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectNullAlias: " + e);
    }
  }

  // ===========================================================
  // Alias interface business methods

  public void addCustomer(Customer customer) {
    try {
      TSNamingContext nctx = new TSNamingContext();
      String customerPK = (String) customer.getPrimaryKey();
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) nctx
          .lookup(CustomerLocal);
      CustomerLocal customerLocal = customerLocalHome
          .findByPrimaryKey(customerPK);
      getCustomers().add(customerLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public Collection getClientCustomers() {
    Vector v1 = new Vector();
    try {

      TSNamingContext nctx = new TSNamingContext();

      Collection custCol = getCustomers();
      Iterator iterator = custCol.iterator();

      while (iterator.hasNext()) {
        CustomerLocal cLoc = (CustomerLocal) iterator.next();
        String customerPK = (String) cLoc.getPrimaryKey();
        CustomerHome customerHome = (CustomerHome) nctx.lookup(Customer,
            CustomerHome.class);
        Customer customer = customerHome.findByPrimaryKey(customerPK);
        v1.add(customer);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return v1;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
