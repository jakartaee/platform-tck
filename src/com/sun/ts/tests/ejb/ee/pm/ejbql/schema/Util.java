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

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

public class Util {
  private static boolean debug = false;

  public static boolean checkEJBs(Collection c, int refType, String pks[]) {
    Customer cref = null;
    Order oref = null;
    Alias aref = null;
    Product pref = null;
    boolean foundPK = false;
    String cpks2[] = new String[c.size()];
    String cpks = "(";
    String epks = "(";
    TestUtil.logTrace("Util.checkEJBs");
    try {
      if (pks.length == 0)
        epks = "()";
      if (c.size() == 0)
        cpks = "()";
      for (int i = 0; i < pks.length; i++) {
        if (i + 1 != pks.length)
          epks = epks + pks[i] + ", ";
        else
          epks = epks + pks[i] + ")";
      }
      int k = 0;
      Iterator iterator = c.iterator();
      while (iterator.hasNext()) {
        if (refType == Schema.CUSTOMERREF) {
          cref = (Customer) PortableRemoteObject.narrow(iterator.next(),
              Customer.class);
          cpks = cpks + cref.getId();
          cpks2[k] = cref.getId();
        } else if (refType == Schema.ORDERREF) {
          oref = (Order) PortableRemoteObject.narrow(iterator.next(),
              Order.class);
          cpks = cpks + oref.getId();
          cpks2[k] = oref.getId();
        } else if (refType == Schema.ALIASREF) {
          aref = (Alias) PortableRemoteObject.narrow(iterator.next(),
              Alias.class);
          cpks = cpks + aref.getId();
          cpks2[k] = aref.getId();
        } else {
          pref = (Product) PortableRemoteObject.narrow(iterator.next(),
              Product.class);
          cpks = cpks + pref.getId();
          cpks2[k] = pref.getId();
        }
        if (++k != c.size())
          cpks = cpks + ", ";
        else
          cpks = cpks + ")";
      }
      if (checkWrongSize(c, pks.length)) {
        TestUtil.logErr("Wrong ejb's returned, expected " + "PKs of " + epks
            + ", got PKs of " + cpks);
        return false;
      }
      if (checkDuplicates(cpks2)) {
        TestUtil.logErr("Duplicate ejb's returned, expected " + "PKs of " + epks
            + ", got PKs of " + cpks);
        return false;
      }
      iterator = c.iterator();
      while (iterator.hasNext()) {
        if (refType == Schema.CUSTOMERREF)
          cref = (Customer) PortableRemoteObject.narrow(iterator.next(),
              Customer.class);
        else if (refType == Schema.ORDERREF)
          oref = (Order) PortableRemoteObject.narrow(iterator.next(),
              Order.class);
        else if (refType == Schema.ALIASREF)
          aref = (Alias) PortableRemoteObject.narrow(iterator.next(),
              Alias.class);
        else
          pref = (Product) PortableRemoteObject.narrow(iterator.next(),
              Product.class);
        foundPK = false;
        for (int j = 0; j < pks.length; j++) {
          if (refType == Schema.CUSTOMERREF) {
            if (cref.getId().equals(pks[j])) {
              foundPK = true;
              break;
            }
          } else if (refType == Schema.ORDERREF) {
            if (oref.getId().equals(pks[j])) {
              foundPK = true;
              break;
            }
          } else if (refType == Schema.ALIASREF) {
            if (aref.getId().equals(pks[j])) {
              foundPK = true;
              break;
            }
          } else {
            if (pref.getId().equals(pks[j])) {
              foundPK = true;
              break;
            }
          }
        }
        if (!foundPK) {
          TestUtil.logErr("Wrong ejb's returned, expected PKs of " + epks
              + ", got PKs of " + cpks);
          return false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkEJBs: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkAddressDVCs(Collection c, AddressDVC a[]) {
    TestUtil.logTrace("Util.checkAddressDVCs");
    try {
      boolean foundPK = false;
      String cpks = "(";
      String epks = "(";
      if (a.length == 0)
        epks = "()";
      if (c.size() == 0)
        cpks = "()";
      for (int i = 0; i < a.length; i++) {
        if (i + 1 != a.length)
          epks = epks + a[i].getId() + ", ";
        else
          epks = epks + a[i].getId() + ")";
      }
      int k = 0;
      Iterator iterator = c.iterator();
      String pks[] = new String[c.size()];
      while (iterator.hasNext()) {
        AddressDVC advc = (AddressDVC) iterator.next();
        cpks = cpks + advc.getId();
        pks[k] = advc.getId();
        if (++k != c.size())
          cpks = cpks + ", ";
        else
          cpks = cpks + ")";
      }
      if (checkWrongSize(c, a.length)) {
        TestUtil.logErr("Wrong AddressDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      if (checkDuplicates(pks)) {
        TestUtil.logErr("Duplicate AddressDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      k = 0;
      iterator = c.iterator();
      while (iterator.hasNext()) {
        AddressDVC advc = (AddressDVC) iterator.next();
        foundPK = false;
        for (int j = 0; j < a.length; j++) {
          if (advc.getId().equals(a[j].getId())) {
            foundPK = true;
            break;
          }
        }
        if (!foundPK) {
          TestUtil.logErr("Wrong AddressDVC's returned, expected " + "PKs of "
              + epks + ", got PKs of " + cpks);
          return false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkAddressDVCs: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkLineItemDVCs(Collection c, LineItemDVC a[]) {
    TestUtil.logTrace("Util.checkLineItemDVCs");
    try {
      boolean foundPK = false;
      String cpks = "(";
      String epks = "(";
      if (a.length == 0)
        epks = "()";
      if (c.size() == 0)
        cpks = "()";
      for (int i = 0; i < a.length; i++) {
        if (i + 1 != a.length)
          epks = epks + a[i].getId() + ", ";
        else
          epks = epks + a[i].getId() + ")";
      }
      int k = 0;
      Iterator iterator = c.iterator();
      String pks[] = new String[c.size()];
      while (iterator.hasNext()) {
        LineItemDVC ldvc = (LineItemDVC) iterator.next();
        cpks = cpks + ldvc.getId();
        pks[k] = ldvc.getId();
        if (++k != c.size())
          cpks = cpks + ", ";
        else
          cpks = cpks + ")";
      }
      if (checkWrongSize(c, a.length)) {
        TestUtil.logErr("Wrong LineItemDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      if (checkDuplicates(pks)) {
        TestUtil.logErr("Duplicate LineItemDVC's returned, expected "
            + "PKs of " + epks + ", got PKs of " + cpks);
        return false;
      }
      k = 0;
      iterator = c.iterator();
      while (iterator.hasNext()) {
        LineItemDVC ldvc = (LineItemDVC) iterator.next();
        foundPK = false;
        for (int j = 0; j < a.length; j++) {
          if (ldvc.getId().equals(a[j].getId())) {
            foundPK = true;
            break;
          }
        }
        if (!foundPK) {
          TestUtil.logErr("Wrong LineItemDVC's returned, expected " + "PKs of "
              + epks + ", got PKs of " + cpks);
          return false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkLineItemDVCs: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkCreditCardDVCs(Collection c, CreditCardDVC a[]) {
    TestUtil.logTrace("Util.checkCreditCardDVCs");
    try {
      boolean foundPK = false;
      String cpks = "(";
      String epks = "(";
      if (a.length == 0)
        epks = "()";
      if (c.size() == 0)
        cpks = "()";
      for (int i = 0; i < a.length; i++) {
        if (i + 1 != a.length)
          epks = epks + a[i].getId() + ", ";
        else
          epks = epks + a[i].getId() + ")";
      }
      int k = 0;
      Iterator iterator = c.iterator();
      String pks[] = new String[c.size()];
      while (iterator.hasNext()) {
        CreditCardDVC advc = (CreditCardDVC) iterator.next();
        cpks = cpks + advc.getId();
        pks[k] = advc.getId();
        if (++k != c.size())
          cpks = cpks + ", ";
        else
          cpks = cpks + ")";
      }
      if (checkWrongSize(c, a.length)) {
        TestUtil.logErr("Wrong CreditCardDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      if (checkDuplicates(pks)) {
        TestUtil.logErr("Duplicate CreditCardDVC's returned, expected "
            + "PKs of " + epks + ", got PKs of " + cpks);
        return false;
      }
      k = 0;
      iterator = c.iterator();
      while (iterator.hasNext()) {
        CreditCardDVC advc = (CreditCardDVC) iterator.next();
        foundPK = false;
        for (int j = 0; j < a.length; j++) {
          if (advc.getId().equals(a[j].getId())) {
            foundPK = true;
            break;
          }
        }
        if (!foundPK) {
          TestUtil.logErr("Wrong CreditCardDVC's returned, "
              + "expected PKs of " + epks + ", got PKs of " + cpks);
          return false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkCreditCardDVCs: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkPhoneDVCs(Collection c, PhoneDVC a[]) {
    TestUtil.logTrace("Util.checkPhoneDVCs");
    try {
      boolean foundPK = false;
      String cpks = "(";
      String epks = "(";
      if (a.length == 0)
        epks = "()";
      if (c.size() == 0)
        cpks = "()";
      for (int i = 0; i < a.length; i++) {
        if (i + 1 != a.length)
          epks = epks + a[i].getId() + ", ";
        else
          epks = epks + a[i].getId() + ")";
      }
      int k = 0;
      Iterator iterator = c.iterator();
      String pks[] = new String[c.size()];
      while (iterator.hasNext()) {
        PhoneDVC advc = (PhoneDVC) iterator.next();
        cpks = cpks + advc.getId();
        pks[k] = advc.getId();
        if (++k != c.size())
          cpks = cpks + ", ";
        else
          cpks = cpks + ")";
      }
      if (checkWrongSize(c, a.length)) {
        TestUtil.logErr("Wrong PhoneDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      if (checkDuplicates(pks)) {
        TestUtil.logErr("Duplicate PhoneDVC's returned, expected " + "PKs of "
            + epks + ", got PKs of " + cpks);
        return false;
      }
      k = 0;
      iterator = c.iterator();
      while (iterator.hasNext()) {
        PhoneDVC advc = (PhoneDVC) iterator.next();
        foundPK = false;
        for (int j = 0; j < a.length; j++) {
          if (advc.getId().equals(a[j].getId())) {
            foundPK = true;
            break;
          }
        }
        if (!foundPK) {
          TestUtil.logErr("Wrong PhoneDVC's returned, expected " + "PKs of "
              + epks + ", got PKs of " + cpks);
          return false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkPhoneDVCs: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkEJB(Customer a1, String a2) {
    TestUtil.logTrace("Util.checkEJB");
    try {
      if (!a1.getId().equals(a2)) {
        TestUtil.logErr("Wrong ejb returned, expected " + "PK of " + a2
            + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkEJB: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkEJB(Order a1, String a2) {
    TestUtil.logTrace("Util.checkEJB");
    try {
      if (!a1.getId().equals(a2)) {
        TestUtil.logErr("Wrong ejb returned, expected " + "PK of " + a2
            + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkEJB: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkEJB(Alias a1, String a2) {
    TestUtil.logTrace("Util.checkEJB");
    try {
      if (!a1.getId().equals(a2)) {
        TestUtil.logErr("Wrong ejb returned, expected " + "PK of " + a2
            + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkEJB: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkEJB(Product a1, String a2) {
    TestUtil.logTrace("Util.checkEJB");
    try {
      if (!a1.getId().equals(a2)) {
        TestUtil.logErr("Wrong ejb returned, expected " + "PK of " + a2
            + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkEJB: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkAddressDVC(AddressDVC a1, AddressDVC a2) {
    TestUtil.logTrace("Util.checkAddressDVC");
    try {
      if (!a1.getId().equals(a2.getId())) {
        TestUtil.logErr("Wrong AddressDVC returned, expected " + "PK of "
            + a2.getId() + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkAddressDVC: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkLineItemDVC(LineItemDVC a1, LineItemDVC a2) {
    TestUtil.logTrace("Util.checkLineItemDVC");
    try {
      if (!a1.getId().equals(a2.getId())) {
        TestUtil.logErr("Wrong LineItemDVC returned, expected " + "PK of "
            + a2.getId() + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkLineItemDVC: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkCreditCardDVC(CreditCardDVC a1, CreditCardDVC a2) {
    TestUtil.logTrace("Util.checkCreditCardDVC");
    try {
      if (!a1.getId().equals(a2.getId())) {
        TestUtil.logErr("Wrong CreditCardDVC returned, expected " + "PK of "
            + a2.getId() + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkCreditCardDVC: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public static boolean checkPhoneDVC(PhoneDVC a1, PhoneDVC a2) {
    TestUtil.logTrace("Util.checkPhoneDVC");
    try {
      if (!a1.getId().equals(a2.getId())) {
        TestUtil.logErr("Wrong PhoneDVC returned, expected " + "PK of "
            + a2.getId() + ", got PK of " + a1.getId());
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception in Util.checkPhoneDVC: " + e);
      if (debug)
        TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  private static boolean checkWrongSize(Collection c, int s) {
    TestUtil.logTrace("Util.checkWrongSize");
    if (c.size() != s) {
      TestUtil.logErr("Wrong collection size returned (expected " + s + ", got "
          + c.size() + ")");
      return true;
    }
    return false;
  }

  private static boolean checkDuplicates(String s[]) {
    TestUtil.logTrace("Util.checkDuplicates");
    boolean duplicates = false;
    for (int i = 0; i < s.length; i++) {
      for (int j = 0; j < s.length; j++) {
        if (i == j)
          continue;
        if (s[i].equals(s[j])) {
          duplicates = true;
          break;
        }
      }
    }
    if (duplicates) {
      TestUtil.logErr("Wrong collection contents returned "
          + "(contains duplicate entries)");
      return true;
    }
    return false;
  }
}
