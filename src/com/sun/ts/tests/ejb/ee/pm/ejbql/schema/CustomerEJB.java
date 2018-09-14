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
 * @(#)CustomerEJB.java	1.28 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public abstract class CustomerEJB implements EntityBean {

  // JNDI Names for Local Home Interfaces

  private static final String AddressLocal = "java:comp/env/ejb/AddressLocal";

  private static final String PhoneLocal = "java:comp/env/ejb/PhoneLocal";

  private static final String AliasLocal = "java:comp/env/ejb/AliasLocal";

  private static final String Alias = "java:comp/env/ejb/Alias";

  private static final String CreditCardLocal = "java:comp/env/ejb/CreditCardLocal";

  private static final String Customer = "java:comp/env/ejb/Customer";

  private static final String OrderLocal = "java:comp/env/ejb/OrderLocal";

  private static final String CustomerLocal = "java:comp/env/ejb/CustomerLocal";

  private static final String SpouseLocal = "java:comp/env/ejb/SpouseLocal";

  private static final String InfoLocal = "java:comp/env/ejb/InfoLocal";

  private static final String Order = "java:comp/env/ejb/Order";

  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

  // ===========================================================
  // getters and setters for CMP fields

  public abstract String getId();

  public abstract void setId(String v);

  public abstract String getName();

  public abstract void setName(String v);

  public abstract Country getCountry();

  public abstract void setCountry(Country v);

  // ===========================================================
  // getters and setters for CMR fields

  // 1x1
  public abstract AddressLocal getHome();

  public abstract void setHome(AddressLocal v);

  // 1x1
  public abstract AddressLocal getWork();

  public abstract void setWork(AddressLocal v);

  // 1x1
  public abstract SpouseLocal getSpouse();

  public abstract void setSpouse(SpouseLocal v);

  // 1xMANY
  public abstract Collection getCreditCards();

  public abstract void setCreditCards(Collection v);

  // 1xMANY
  public abstract Collection getOrders();

  public abstract void setOrders(Collection v);

  // MANYxMANY
  public abstract Collection getAliases();

  public abstract void setAliases(Collection v);

  // MAMYxMANY
  public abstract Collection getAliasesNoop();

  public abstract void setAliasesNoop(Collection v);

  // ===========================================================
  // methods to create instances of lightweight entity beans

  private AddressLocal createAddressLocal(String id, String street, String city,
      String state, String zip) throws Exception {
    TestUtil.logTrace("createAddressLocal");
    TSNamingContext nctx = new TSNamingContext();
    AddressLocalHome addressLocalHome = (AddressLocalHome) nctx
        .lookup(AddressLocal);
    AddressLocal addressLocal = addressLocalHome.create(id, street, city, state,
        zip);
    return addressLocal;
  }

  private AddressLocal createAddressLocal(String id, String street, String city,
      String state, String zip, Collection phones) throws Exception {
    TestUtil.logTrace("createAddressLocal");
    TSNamingContext nctx = new TSNamingContext();
    AddressLocalHome addressLocalHome = (AddressLocalHome) nctx
        .lookup(AddressLocal);
    AddressLocal addressLocal = addressLocalHome.create(id, street, city, state,
        zip, phones);
    return addressLocal;
  }

  private SpouseLocal createSpouseLocal(String id, String first, String maiden,
      String last, String sNumber, InfoDVC infoDVC) throws Exception {
    TestUtil.logTrace("createSpouseLocal");
    TSNamingContext nctx = new TSNamingContext();

    TestUtil.logMsg("createSpouseLocal: Converting infoDVC to infoLocal");

    InfoLocalHome infoLocalHome = (InfoLocalHome) nctx.lookup(InfoLocal);

    InfoLocal infoLocal = infoLocalHome.create(infoDVC.getId(),
        infoDVC.getStreet(), infoDVC.getCity(), infoDVC.getState(),
        infoDVC.getZip());

    TestUtil.logMsg("createSpouseLocal: creating SpouseLocal");
    SpouseLocalHome spouseLocalHome = (SpouseLocalHome) nctx
        .lookup(SpouseLocal);

    SpouseLocal spouseLocal = spouseLocalHome.create(id, first, maiden, last,
        sNumber, infoLocal);

    return spouseLocal;
  }

  private CreditCardLocal createCreditCardLocal(String id, String number,
      String type, String expires, boolean approved, double balance,
      Order order, Customer customer) throws Exception {
    TestUtil.logTrace("createCreditCardLocal");
    TSNamingContext nctx = new TSNamingContext();

    TestUtil.logMsg("createCreditCardLocal: Converting order to orderlocal");
    OrderLocal orderLocal = null;
    if (order != null) {
      TestUtil.logMsg("createCreditCardLocal: Converting order to orderlocal");
      String orderPK = (String) order.getPrimaryKey();
      OrderLocalHome orderLocalHome = (OrderLocalHome) nctx.lookup(OrderLocal);
      orderLocal = orderLocalHome.findByPrimaryKey(orderPK);
    } else
      TestUtil.logMsg("order reference is null");

    TestUtil
        .logMsg("createCreditCardLocal: Converting customer to customerlocal");
    CustomerLocal customerLocal = null;
    if (customer != null) {
      String customerPK = (String) customer.getPrimaryKey();
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) nctx
          .lookup(CustomerLocal);
      customerLocal = customerLocalHome.findByPrimaryKey(customerPK);
    } else
      TestUtil.logMsg("customer reference is null");

    TestUtil.logMsg("createCreditCardLocal: Creating creditCardLocal");
    CreditCardLocalHome creditCardLocalHome = (CreditCardLocalHome) nctx
        .lookup(CreditCardLocal);
    CreditCardLocal creditCardLocal = creditCardLocalHome.create(id, number,
        type, expires, approved, balance, orderLocal, customerLocal);

    return creditCardLocal;
  }

  // ===========================================================
  // select methods

  public abstract AddressLocal ejbSelectHomeAddress() throws FinderException;

  public abstract Collection ejbSelectAllWorkAddresses() throws FinderException;

  public abstract CreditCardLocal ejbSelectCreditCard(String pkey)
      throws FinderException;

  public abstract Set ejbSelectHomeZipCodesByCity(String city)
      throws FinderException;

  public abstract Collection ejbSelectAllHomeZipCodesByCity(String city)
      throws FinderException;

  public abstract Collection ejbSelectCustomersByAlias(String s)
      throws FinderException;

  public abstract Collection ejbSelectCustomersByAlias(String s, String t)
      throws FinderException;

  public abstract Collection ejbSelectPhonesByArea(String area)
      throws FinderException;

  public abstract Set ejbSelectCustomerAddressBySet(String addr)
      throws FinderException;

  public abstract Collection ejbSelectCustomerAddressByCollection(String addr)
      throws FinderException;

  public abstract Collection ejbSelectCustomersByWorkZipCode()
      throws FinderException;

  public abstract Collection ejbSelectCustomersByNotNullWorkZipCode()
      throws FinderException;

  public abstract String ejbSelectCustomerByHomeAddress()
      throws FinderException;

  public abstract long ejbSelectAllHomeCities() throws FinderException;

  public abstract long ejbSelectNotNullHomeCities() throws FinderException;

  public abstract Collection ejbSelectCustomersByQuery42()
      throws FinderException;

  // ===========================================================

  public String ejbCreate(String id, String name, AddressDVC home,
      AddressDVC work, Country country) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setName(name);
      setCountry(country);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String name, AddressDVC home,
      AddressDVC work, Country country) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    try {
      AddressLocal homeAddr = createAddressLocal(home.getId(), home.getStreet(),
          home.getCity(), home.getState(), home.getZip(), home.getPhones());
      AddressLocal workAddr = createAddressLocal(work.getId(), work.getStreet(),
          work.getCity(), work.getState(), work.getZip(), work.getPhones());
      setHome(homeAddr);
      setWork(workAddr);
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
    if (getHome() != null) {
      Collection col = getHome().getPhones();
      Iterator iterator = col.iterator();
      while (iterator.hasNext()) {
        PhoneLocal p = (PhoneLocal) iterator.next();
        p.remove();
      }
      getHome().remove();
    }
    if (getWork() != null) {
      Collection col = getWork().getPhones();
      Iterator iterator = col.iterator();
      while (iterator.hasNext()) {
        PhoneLocal p = (PhoneLocal) iterator.next();
        p.remove();
      }
      getWork().remove();
    }
    Collection col = getCreditCards();
    Iterator iterator = col.iterator();
    while (iterator.hasNext()) {
      CreditCardLocal c = (CreditCardLocal) iterator.next();
      c.remove();
    }
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

  public AddressDVC ejbHomeSelectHomeAddress() throws AddressException {
    TestUtil.logTrace("ejbHomeSelectHomeAddress");
    try {
      AddressLocal aLeb = ejbSelectHomeAddress();
      Vector v1 = new Vector();
      Collection pCol = aLeb.getPhones();
      Iterator iterator = pCol.iterator();
      while (iterator.hasNext()) {
        PhoneLocal pLeb = (PhoneLocal) iterator.next();
        PhoneDVC pDvc = new PhoneDVC(pLeb.getId(), pLeb.getArea(),
            pLeb.getNumber());
        v1.add(pDvc);
      }
      AddressDVC aDvc = new AddressDVC(aLeb.getId(), aLeb.getStreet(),
          aLeb.getCity(), aLeb.getState(), aLeb.getZip(), v1);
      return aDvc;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("ejbHomeSelectHomeAddress: " + e);
    }
  }

  public Collection ejbHomeSelectAllWorkAddresses() throws AddressException {
    TestUtil.logTrace("ejbHomeSelectAllWorkAddresses");
    try {
      Collection ccol = ejbSelectAllWorkAddresses();
      Iterator iterator = ccol.iterator();
      Vector v1 = new Vector();
      while (iterator.hasNext()) {
        AddressLocal aLeb = (AddressLocal) iterator.next();
        Collection ccol2 = aLeb.getPhones();
        Iterator iterator2 = ccol2.iterator();
        Vector v2 = new Vector();
        while (iterator2.hasNext()) {
          PhoneLocal pLeb = (PhoneLocal) iterator2.next();
          PhoneDVC pDvc = new PhoneDVC(pLeb.getId(), pLeb.getArea(),
              pLeb.getNumber());
          v2.add(pDvc);
        }
        AddressDVC aDvc = new AddressDVC(aLeb.getId(), aLeb.getStreet(),
            aLeb.getCity(), aLeb.getState(), aLeb.getZip(), v2);
        v1.add(aDvc);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("ejbHomeSelectAllWorkAddresses: " + e);
    }
  }

  public Set ejbHomeSelectHomeZipCodesByCity(String city)
      throws AddressException {
    TestUtil.logTrace("ejbHomeSelectHomeZipCodesByCity");
    try {
      Set s1 = ejbSelectHomeZipCodesByCity(city);

      return s1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("ejbHomeSelectHomeZipCodesByCity: " + e);
    }
  }

  public Collection ejbHomeSelectAllHomeZipCodesByCity(String city)
      throws AddressException {
    TestUtil.logTrace("ejbHomeSelectAllHomeZipCodesByCity");
    try {
      Collection ccol = ejbSelectAllHomeZipCodesByCity(city);

      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("ejbHomeSelectAllHomeZipCodesByCity: " + e);
    }
  }

  public Collection ejbHomeSelectCustomersByAlias(String s) {
    TestUtil.logTrace("ejbHomeSelectCustomersByAlias - one arg");
    try {
      Collection ccol = ejbSelectCustomersByAlias(s);

      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomersByAlias: " + e);
    }
  }

  public Collection ejbHomeSelectCustomersByAlias(String s, String t) {
    TestUtil.logTrace("ejbHomeSelectCustomersByAlias - two args");
    try {
      Collection ccol = ejbSelectCustomersByAlias(s, t);

      return ccol;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomersByAlias: " + e);
    }
  }

  public Collection ejbHomeSelectPhonesByArea(String area)
      throws PhoneException {
    Vector v1 = new Vector();
    TestUtil.logTrace("ejbHomeSelectPhonesByArea");
    try {
      Collection ccol = ejbSelectPhonesByArea(area);
      Iterator iterator = ccol.iterator();
      while (iterator.hasNext()) {
        CustomerLocal cLeb = (CustomerLocal) iterator.next();
        String customerPK = (String) cLeb.getPrimaryKey();
        CustomerHome customerHome = (CustomerHome) ectx.getEJBHome();
        Customer customer = customerHome.findByPrimaryKey(customerPK);
        v1.add(customer);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new PhoneException("ejbHomeSelectPhonesByArea: " + e);
    }
  }

  public Set ejbHomeSelectCustomerAddressBySet(String state) {
    Set s1 = null;
    TestUtil.logTrace("ejbHomeSelectCustomerAddressBySet");
    try {
      s1 = ejbSelectCustomerAddressBySet(state);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomerAddressBySet:" + e);
    }
    return s1;
  }

  public Collection ejbHomeSelectCustomerAddressByCollection(String state) {
    Collection ccol = null;
    TestUtil.logTrace("ejbHomeSelectCustomerAddressByCollection");
    try {
      ccol = ejbSelectCustomerAddressByCollection(state);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomerAddressByCollection:" + e);
    }
    return ccol;
  }

  public Customer ejbHomeGetCustomerByHomePhoneNumber(String phone) {
    TestUtil.logTrace("ejbHomeGetCustomerByHomePhoneNumber");
    try {
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) ectx
          .getEJBLocalHome();
      CustomerLocal cLeb = customerLocalHome
          .findCustomerByHomePhoneNumber(phone);
      String customerPK = (String) cLeb.getPrimaryKey();
      CustomerHome customerHome = (CustomerHome) ectx.getEJBHome();
      Customer customer = customerHome.findByPrimaryKey(customerPK);
      return customer;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeGetCustomerByHomePhoneNumber:" + e);
    }
  }

  public Collection ejbHomeGetCustomersByWorkCity(String city) {
    Vector v1 = new Vector();
    TestUtil.logTrace("ejbHomeGetCustomersByWorkCity");
    try {
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) ectx
          .getEJBLocalHome();
      Collection cCol = customerLocalHome.findCustomersByWorkCity(city);
      Iterator iterator = cCol.iterator();
      while (iterator.hasNext()) {
        CustomerLocal cLeb = (CustomerLocal) iterator.next();
        String customerPK = (String) cLeb.getPrimaryKey();
        CustomerHome customerHome = (CustomerHome) ectx.getEJBHome();
        Customer customer = customerHome.findByPrimaryKey(customerPK);
        v1.add(customer);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeGetCustomersByWorkCity:" + e);
    }
  }

  public Customer ejbHomeGetCustomerByQuery29(String street, String city,
      String state, String zip) {
    TestUtil.logTrace("ejbHomeGetCustomerByQuery29");
    try {
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) ectx
          .getEJBLocalHome();
      CustomerLocal cLeb = customerLocalHome.findCustomerByQuery29(street, city,
          state, zip);
      String customerPK = (String) cLeb.getPrimaryKey();
      CustomerHome customerHome = (CustomerHome) ectx.getEJBHome();
      Customer customer = customerHome.findByPrimaryKey(customerPK);
      return customer;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeGetCustomerByQuery29:" + e);
    }
  }

  public Collection ejbHomeGetCustomersByQuery32(String city) {

    Vector v1 = new Vector();
    TestUtil.logTrace("ejbHomeGetCustomersByQuery32");
    try {
      CustomerLocalHome customerLocalHome = (CustomerLocalHome) ectx
          .getEJBLocalHome();
      Collection cCol = customerLocalHome.findCustomersByQuery32(city);
      Iterator iterator = cCol.iterator();
      while (iterator.hasNext()) {
        CustomerLocal cLeb = (CustomerLocal) iterator.next();
        String customerPK = (String) cLeb.getPrimaryKey();
        CustomerHome customerHome = (CustomerHome) ectx.getEJBHome();
        Customer customer = customerHome.findByPrimaryKey(customerPK);
        v1.add(customer);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeGetCustomersByQuery32:" + e);
    }
  }

  public Collection ejbHomeSelectCustomersByWorkZipCode() {
    TestUtil.logTrace("ejbHomeSelectCustomersByWorkZipCode");
    try {
      Collection c = ejbSelectCustomersByWorkZipCode();
      return c;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomersByWorkZipCode: " + e);
    }
  }

  public Collection ejbHomeSelectCustomersByNotNullWorkZipCode() {
    TestUtil.logTrace("ejbHomeSelectCustomersByNotNullWorkZipCode");
    try {
      Collection c = ejbSelectCustomersByNotNullWorkZipCode();
      return c;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(
          "ejbHomeSelectCustomersByNotNullWorkZipCode: " + e);
    }
  }

  public String ejbHomeSelectCustomerByHomeAddress() {
    TestUtil.logTrace("ejbHomeSelectCustomerByHomeAddress");
    try {
      String s = ejbSelectCustomerByHomeAddress();
      return s;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectCustomerByHomeAddress: " + e);
    }
  }

  public long ejbHomeSelectAllHomeCities() {
    TestUtil.logTrace("ejbHomeSelectAllHomeCities");
    try {
      long l = ejbSelectAllHomeCities();
      return l;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectAllHomeCities: " + e);
    }
  }

  public long ejbHomeSelectNotNullHomeCities() {
    TestUtil.logTrace("ejbHomeSelectNotNullHomeCities");
    try {
      long l = ejbSelectNotNullHomeCities();
      return l;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("ejbHomeSelectNotNullHomeCities: " + e);
    }
  }

  public String ejbHomeGetSpouseInfo() {
    TestUtil.logTrace("getSpouseInfo");
    try {

      TSNamingContext nctx = new TSNamingContext();

      SpouseLocalHome spouseLocalHome = (SpouseLocalHome) nctx
          .lookup(SpouseLocal);
      String s = spouseLocalHome.selectSpouseInfo();
      TestUtil.logTrace("RETURNING STRING - NOT NULL");
      return s;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("getSpouseInfo:" + e);
    }
  }

  public Collection ejbHomeSelectCustomersByQuery42() throws AddressException {
    TestUtil.logTrace("ejbHomeSelectCustomersByQuery42");
    try {
      Collection ccol = ejbSelectCustomersByQuery42();
      Iterator iterator = ccol.iterator();
      Vector v1 = new Vector();
      while (iterator.hasNext()) {
        AddressLocal aLeb = (AddressLocal) iterator.next();
        Collection ccol2 = aLeb.getPhones();
        Iterator iterator2 = ccol2.iterator();
        Vector v2 = new Vector();
        while (iterator2.hasNext()) {
          PhoneLocal pLeb = (PhoneLocal) iterator2.next();
          PhoneDVC pDvc = new PhoneDVC(pLeb.getId(), pLeb.getArea(),
              pLeb.getNumber());
          v2.add(pDvc);
        }
        AddressDVC aDvc = new AddressDVC(aLeb.getId(), aLeb.getStreet(),
            aLeb.getCity(), aLeb.getState(), aLeb.getZip(), v2);
        v1.add(aDvc);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("ejbHomeSelectCustomersByQuery42: " + e);
    }
  }

  // ===========================================================
  // Customer interface business methods

  public void addOrder(Order order) {
    try {
      TSNamingContext nctx = new TSNamingContext();

      String orderPK = (String) order.getPrimaryKey();
      OrderLocalHome orderLocalHome = (OrderLocalHome) nctx.lookup(OrderLocal);
      OrderLocal orderLocal = orderLocalHome.findByPrimaryKey(orderPK);
      getOrders().add(orderLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("addOrder:" + e);
    }
  }

  public void addAlias(Alias alias) {
    try {
      TSNamingContext nctx = new TSNamingContext();

      String aliasPK = (String) alias.getPrimaryKey();
      AliasLocalHome aliasLocalHome = (AliasLocalHome) nctx.lookup(AliasLocal);
      AliasLocal aliasLocal = aliasLocalHome.findByPrimaryKey(aliasPK);
      getAliases().add(aliasLocal);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("addAlias:" + e);
    }
  }

  public void ejbHomeAddSpouseEntry(SpouseDVC s) throws SpouseException {
    try {
      SpouseLocal sLeb = createSpouseLocal(s.getId(), s.getFirstName(),
          s.getMaidenName(), s.getLastName(), s.getSocialSecurityNumber(),
          s.getInfo());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new SpouseException("addSpouseEntry:" + e);
    }
  }

  public void addSpouse(SpouseDVC s) throws SpouseException {
    try {
      SpouseLocal sLeb = createSpouseLocal(s.getId(), s.getFirstName(),
          s.getMaidenName(), s.getLastName(), s.getSocialSecurityNumber(),
          s.getInfo());
      setSpouse(sLeb);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new SpouseException("addSpouse:" + e);
    }
  }

  public void addCreditCard(CreditCardDVC p) throws CreditCardException {
    try {
      Collection cardCol = getCreditCards();
      CreditCardLocal cc = createCreditCardLocal(p.getId(), p.getNumber(),
          p.getType(), p.getExpires(), p.getApproved(), p.getBalance(),
          p.getOrder(), p.getCustomer());
      cardCol.add(cc);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("addCreditCard: " + e);
    }
  }

  public AddressDVC getClientHomeAddress() throws AddressException {
    try {
      Vector v = new Vector();
      PhoneLocal pLeb = null;
      PhoneDVC pDvc = null;
      Collection ccol = getHome().getPhones();
      Iterator iterator = ccol.iterator();
      while (iterator.hasNext()) {
        pLeb = (PhoneLocal) iterator.next();
        pDvc = new PhoneDVC(pLeb.getId(), pLeb.getArea(), pLeb.getNumber());
        v.add(pDvc);
      }
      AddressDVC homeAddr = new AddressDVC(getHome().getId(),
          getHome().getStreet(), getHome().getCity(), getHome().getState(),
          getHome().getZip(), v);
      return homeAddr;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("getClientHomeAddress: " + e);
    }
  }

  public AddressDVC getClientWorkAddress() throws AddressException {
    try {
      Vector v = new Vector();
      PhoneLocal pLeb = null;
      PhoneDVC pDvc = null;
      Collection ccol = getWork().getPhones();
      Iterator iterator = ccol.iterator();
      while (iterator.hasNext()) {
        pLeb = (PhoneLocal) iterator.next();
        pDvc = new PhoneDVC(pLeb.getId(), pLeb.getArea(), pLeb.getNumber());
        v.add(pDvc);
      }
      AddressDVC workAddr = new AddressDVC(getWork().getId(),
          getWork().getStreet(), getWork().getCity(), getWork().getState(),
          getWork().getZip(), v);
      return workAddr;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new AddressException("getClientWorkAddress: " + e);
    }
  }

  public Collection getClientCreditCards() throws CreditCardException {
    Vector v1 = new Vector();
    CreditCardLocal ccLeb = null;
    CreditCardDVC ccDvc = null;

    try {
      TSNamingContext nctx = new TSNamingContext();
      Collection ccol = getCreditCards();
      Iterator iterator = ccol.iterator();
      while (iterator.hasNext()) {
        ccLeb = (CreditCardLocal) iterator.next();
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
        v1.add(ccDvc);
      }
      return v1;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreditCardException("getClientCreditCards: " + e);
    }
  }

  public Collection getClientOrders() {
    Vector v1 = new Vector();
    try {

      TSNamingContext nctx = new TSNamingContext();

      Collection ccol = getOrders();
      Iterator iterator = ccol.iterator();

      while (iterator.hasNext()) {
        OrderLocal oLoc = (OrderLocal) iterator.next();
        String orderPK = (String) oLoc.getPrimaryKey();
        OrderHome orderHome = (OrderHome) nctx.lookup(Order, OrderHome.class);
        Order order = orderHome.findByPrimaryKey(orderPK);
        v1.add(order);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("getClientOrders:" + e);
    }
    return v1;
  }

  public Collection getClientAliases() {
    Vector v1 = new Vector();
    try {

      TSNamingContext nctx = new TSNamingContext();

      Collection ccol = getAliases();
      Iterator iterator = ccol.iterator();

      while (iterator.hasNext()) {
        AliasLocal aLoc = (AliasLocal) iterator.next();
        String aliasPK = (String) aLoc.getPrimaryKey();
        AliasHome aliasHome = (AliasHome) nctx.lookup(Alias, AliasHome.class);
        Alias alias = aliasHome.findByPrimaryKey(aliasPK);
        v1.add(alias);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("getClientAliases" + e);
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
