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
 * @(#)TestBeanEJB.java	1.11 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.homemethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public abstract class TestBeanEJB implements EntityBean {
  private TSNamingContext nctx = null;

  private EntityContext ectx = null;

  // ===========================================================
  // getters and setters for cmp fields

  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getFirst();

  public abstract void setFirst(String s);

  public abstract String getMiddle();

  public abstract void setMiddle(String s);

  public abstract String getLast();

  public abstract void setLast(String s);

  public abstract String getAccountNumber();

  public abstract void setAccountNumber(String s);

  public abstract String getPaymentType();

  public abstract void setPaymentType(String s);

  public abstract double getCardBalance();

  public abstract void setCardBalance(double d);

  public abstract String getCreditCardNumber();

  public abstract void setCreditCardNumber(String s);

  public abstract String getExpires();

  public abstract void setExpires(String s);

  public abstract String getStreet();

  public abstract void setStreet(String s);

  public abstract String getCity();

  public abstract void setCity(String s);

  public abstract String getState();

  public abstract void setState(String s);

  public abstract Integer getZip();

  public abstract void setZip(Integer i);

  public abstract String getName();

  public abstract void setName(String s);

  public abstract String getCode();

  public abstract void setCode(String s);

  public abstract String getHomePhone();

  public abstract void setHomePhone(String s);

  public abstract String getWorkPhone();

  public abstract void setWorkPhone(String s);

  // ===========================================================

  public Integer ejbCreate(int id, String first, String middle, String last,
      String accountNumber) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Obtain naming context");
      Integer pk = new Integer(id);
      nctx = new TSNamingContext();
      setId(pk);
      setFirst(first);
      setMiddle(middle);
      setLast(last);
      setAccountNumber(accountNumber);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(int id, String first, String middle, String last,
      String accountNumber) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public Integer ejbCreate(int id, String accountNumber, String paymentType,
      double cardBalance, String creditCardNumber, String expires)
      throws CreateException {
    TestUtil.logTrace("ejbCreate2");
    try {
      TestUtil.logMsg("Obtain naming context");
      Integer pk = new Integer(id);
      nctx = new TSNamingContext();
      setId(pk);
      setAccountNumber(accountNumber);
      setPaymentType(paymentType);
      setCardBalance(cardBalance);
      setCreditCardNumber(creditCardNumber);
      setExpires(expires);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(int id, String accountNumber, String paymentType,
      double cardBalance, String creditCardNumber, String expires)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate2");
  }

  public Integer ejbCreateHomeAddress(int id, String street, String city,
      String state, int zip) throws CreateException {
    TestUtil.logTrace("ejbCreateHomeAddress");
    Integer pk = new Integer(id);
    Integer zp = new Integer(zip);
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      setId(pk);
      setStreet(street);
      setCity(city);
      setState(state);
      setZip(zp);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreateHomeAddress(int id, String street, String city,
      String state, int zip) throws CreateException {
    TestUtil.logTrace("ejbPostCreateHomeAddress");
  }

  public Integer ejbCreateCountry(int id, String name, String code)
      throws CreateException {
    TestUtil.logTrace("ejbCreateCountry");
    Integer pk = new Integer(id);
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      setId(pk);
      setName(name);
      setCode(code);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreateCountry(int id, String name, String code)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreateCountry");
  }

  public Integer ejbCreatePhone(int id, String homePhone, String workPhone)
      throws CreateException {
    TestUtil.logTrace("ejbCreatePhone");
    Integer pk = new Integer(id);
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      setId(pk);
      setHomePhone(homePhone);
      setWorkPhone(workPhone);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ...........");
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreatePhone(int id, String homePhone, String workPhone)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreatePhone");
  }

  public void ejbHomeAddCardFee(Integer key, double fee) {
    try {
      TestUtil.logTrace("ejbHomeAddCardFee");
      TestBeanHome tHome = (TestBeanHome) ectx.getEJBHome();
      TestUtil.logTrace("Adding fee to ref with pk of: " + key);
      TestBean ref = tHome.findByPrimaryKey(key);
      double currentBal = ref.getCardBalance();
      TestUtil.logTrace(
          "Card balance before fee for pk: " + key + " is: " + currentBal);
      ref.setCardBalance(currentBal + fee);
      TestUtil.logTrace("Card balance after fee for pk: " + key + " is: "
          + ref.getCardBalance());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception occurred ejbHomeAddCardFee: " + e);
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

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  // ===========================================================
  // TestBean interface (our business methods)

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
