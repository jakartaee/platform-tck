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
 * @(#)OrderEJB.java	1.19 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class OrderEJB implements EntityBean {

  // JNDI Names for Local Home Interfaces

  private static final String LineItemLocal = "java:comp/env/ejb/LineItemLocal";

  private static final String CreditCardLocal = "java:comp/env/ejb/CreditCardLocal";

  private static final String Order = "java:comp/env/ejb/Order";

  private static final String Product = "java:comp/env/ejb/Product";

  private static final String OrderLocal = "java:comp/env/ejb/OrderLocal";

  private static final String ProductLocal = "java:comp/env/ejb/ProductLocal";

  private static final String CustomerLocal = "java:comp/env/ejb/CustomerLocal";

  private static final String Customer = "java:comp/env/ejb/Customer";

  private EntityContext ectx = null;

  // ====================================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String i);

  public abstract double getTotalPrice();

  public abstract void setTotalPrice(double p);

  // ====================================================================
  // getters and setters for CMR fields

  // MANYx1
  public abstract CustomerLocal getCustomer();

  public abstract void setCustomer(CustomerLocal c);

  // 1x1
  public abstract CreditCardLocal getCreditCard();

  public abstract void setCreditCard(CreditCardLocal cc);

  // 1x1
  public abstract LineItemLocal getSampleLineItem();

  public abstract void setSampleLineItem(LineItemLocal l);

  // 1xMANY
  public abstract Collection getLineItems();

  public abstract void setLineItems(Collection c);

  // ===========================================================
  // methods to create instances of lightweight entity beans

  private LineItemLocal createLineItemLocal(String id, int quantity,
      Order order, Product product) throws Exception {
    TestUtil.logTrace("createLineItemLocal");
    TSNamingContext nctx = new TSNamingContext();
    LineItemLocalHome lineItemLocalHome = (LineItemLocalHome) nctx
        .lookup(LineItemLocal);
    String orderPK = (String) order.getPrimaryKey();
    OrderLocalHome orderLocalHome = (OrderLocalHome) nctx.lookup(OrderLocal);
    OrderLocal orderLocal = orderLocalHome.findByPrimaryKey(orderPK);

    String productPK = (String) product.getPrimaryKey();
    ProductLocalHome productLocalHome = (ProductLocalHome) nctx
        .lookup(ProductLocal);
    ProductLocal productLocal = productLocalHome.findByPrimaryKey(productPK);

    LineItemLocal lineItemLocal = lineItemLocalHome.create(id, quantity,
        orderLocal, productLocal);

    return lineItemLocal;
  }

  // ===========================================================
  // select methods

  public abstract LineItemLocal ejbSelectLineItem(String pkey)
      throws FinderException;

  public abstract Collection ejbSelectAllExpiredCreditCards()
      throws FinderException;

  public abstract Collection ejbSelectAllLineItems() throws FinderException;

  public abstract Collection ejbSelectSampleLineItems(LineItemLocal l)
      throws FinderException;

  public abstract Collection ejbSelectCreditCardBalances()
      throws FinderException;

  public abstract java.lang.String ejbSelectMinSingle() throws FinderException;

  public abstract int ejbSelectMaxSingle() throws FinderException;

  public abstract double ejbSelectAvgSingle() throws FinderException;

  public abstract Collection ejbSelectAllCreditCardBalances()
      throws FinderException;

  // =====================================================================

  public String ejbCreate(String id, Customer customer) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, Customer customer)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      TSNamingContext nctx = new TSNamingContext();
      String customerPK = (String) customer.getPrimaryKey();
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) nctx
          .lookup(CustomerLocal);
      CustomerLocal customerLocal = customerLocalHome
          .findByPrimaryKey(customerPK);
      setCustomer(customerLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
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

  public Collection ejbHomeSelectAllExpiredCreditCards()
      throws CreditCardException {
    TestUtil.logTrace("ejbHomeSelectAllExpiredCreditCards");
    try {
      Collection ccol = ejbSelectAllExpiredCreditCards();
      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("ejbHomeSelectAllExpiredCreditCards: " + e);
    }
  }

  public Collection ejbHomeSelectAllLineItems() throws LineItemException {
    TestUtil.logTrace("ejbHomeSelectAllLineItems");
    Vector v1 = new Vector();
    LineItemLocal liLeb = null;
    LineItemDVC liDvc = null;
    try {
      Collection ccol = ejbSelectAllLineItems();
      Iterator iterator = ccol.iterator();
      TSNamingContext nctx = new TSNamingContext();
      while (iterator.hasNext()) {
        liLeb = (LineItemLocal) iterator.next();
        String orderPK = (String) liLeb.getOrder().getPrimaryKey();
        OrderHome orderHome = (OrderHome) nctx.lookup(Order, OrderHome.class);
        Order order = orderHome.findByPrimaryKey(orderPK);

        String productPK = (String) liLeb.getProduct().getPrimaryKey();
        ProductHome productHome = (ProductHome) nctx.lookup(Product,
            ProductHome.class);
        Product product = productHome.findByPrimaryKey(productPK);

        liDvc = new LineItemDVC(liLeb.getId(), liLeb.getQuantity(), order,
            product);
        v1.add(liDvc);
      }
      return v1;
    } catch (FinderException e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("ejbHomeSelectAllLineItems: " + e);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("ejbHomeSelectAllLineItems: " + e);
    }
  }

  public Collection ejbHomeSelectSampleLineItems(LineItemDVC l)
      throws LineItemException {
    TestUtil.logTrace("ejbHomeSelectSampleLineItems");
    try {
      TSNamingContext nctx = new TSNamingContext();
      LineItemLocalHome lineItemLocalHome = (LineItemLocalHome) nctx
          .lookup(LineItemLocal);

      String liLebPK = (String) l.getId();
      LineItemLocal liLeb = lineItemLocalHome.findByPrimaryKey(liLebPK);
      Collection ccol = ejbSelectSampleLineItems(liLeb);
      return ccol;
    } catch (FinderException e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("ejbHomeSelectSampleLineItems: " + e);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("ejbHomeSelectSampleLineItems: " + e);
    }
  }

  public Collection ejbHomeSelectCreditCardBalances()
      throws CreditCardException {
    TestUtil.logTrace("ejbHomeSelectCreditCardBalances");
    try {
      Collection ccol = ejbSelectCreditCardBalances();
      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("ejbHomeSelectCreditCardBalances: " + e);
    }
  }

  public java.lang.String ejbHomeSelectMinSingle() {
    TestUtil.logTrace("ejbHomeSelectMinSingle");
    try {
      String s = ejbSelectMinSingle();
      return s;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectMinSingle: " + e);
    }
  }

  public int ejbHomeSelectMaxSingle() {
    TestUtil.logTrace("ejbHomeSelectMaxSingle");
    try {
      int i = ejbSelectMaxSingle();
      return i;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectMaxSingle: " + e);
    }
  }

  public double ejbHomeSelectAvgSingle() {
    TestUtil.logTrace("ejbHomeSelectAvgSingle");
    try {
      double d = ejbSelectAvgSingle();
      return d;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectAvgSingle: " + e);
    }
  }

  // ====================================================================
  // Miscellaneous Business Methods

  public void addLineItem(LineItemDVC p) throws LineItemException {
    try {
      LineItemLocal li = createLineItemLocal(p.getId(), p.getQuantity(),
          p.getOrder(), p.getProduct());
      Collection ccol = getLineItems();
      ccol.add(li);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("addLineItem: " + e);
    }
  }

  public void addSampleLineItem(LineItemDVC p) throws LineItemException {
    LineItemLocal liLeb = null;
    try {
      liLeb = createLineItemLocal(p.getId(), p.getQuantity(), p.getOrder(),
          p.getProduct());
      setSampleLineItem(liLeb);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("addSampleLineItem: " + e);
    }
  }

  public Collection getClientLineItems() throws LineItemException {
    Vector v1 = new Vector();
    LineItemLocal liLeb = null;
    LineItemDVC liDvc = null;

    try {
      Collection ccol = getLineItems();
      Iterator iterator = ccol.iterator();

      TSNamingContext nctx = new TSNamingContext();

      while (iterator.hasNext()) {
        liLeb = (LineItemLocal) iterator.next();
        String orderPK = (String) liLeb.getOrder().getPrimaryKey();
        OrderHome orderHome = (OrderHome) nctx.lookup(Order, OrderHome.class);
        Order order = orderHome.findByPrimaryKey(orderPK);

        String productPK = (String) liLeb.getProduct().getPrimaryKey();
        ProductHome productHome = (ProductHome) nctx.lookup(Product,
            ProductHome.class);
        Product product = productHome.findByPrimaryKey(productPK);

        liDvc = new LineItemDVC(liLeb.getId(), liLeb.getQuantity(), order,
            product);
        v1.add(liDvc);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new LineItemException("getClientLineItems: " + e);
    }
  }

  public CreditCardDVC getClientCreditCard() throws CreditCardException {
    Vector v1 = new Vector();
    CreditCardLocal ccLeb = null;
    CreditCardDVC ccDvc = null;

    try {
      TSNamingContext nctx = new TSNamingContext();
      ccLeb = getCreditCard();
      String orderPK = (String) ccLeb.getOrder().getPrimaryKey();
      OrderHome orderHome = (OrderHome) nctx.lookup(Order, OrderHome.class);
      Order order = orderHome.findByPrimaryKey(orderPK);

      String customerPK = (String) ccLeb.getCustomer().getPrimaryKey();
      CustomerHome customerHome = (CustomerHome) nctx.lookup(Customer,
          CustomerHome.class);
      Customer customer = customerHome.findByPrimaryKey(customerPK);

      ccDvc = new CreditCardDVC(ccLeb.getId(), ccLeb.getNumber(),
          ccLeb.getType(), ccLeb.getExpires(), ccLeb.getApproved(),
          ccLeb.getBalance(), order, customer);
      return ccDvc;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("getClientCreditCard: " + e);
    }
  }

  public Collection ejbHomeSelectAllCreditCardBalances()
      throws CreditCardException {
    TestUtil.logTrace("ejbHomeSelectAllCreditCardBalances");
    try {
      Collection ccol = ejbSelectAllCreditCardBalances();
      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("ejbHomeSelectAllCreditCardBalances: " + e);
    }
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
