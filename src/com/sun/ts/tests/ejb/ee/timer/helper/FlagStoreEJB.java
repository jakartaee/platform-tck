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
 * @(#)FlagStoreEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.timer.helper;

import com.sun.ts.tests.ejb.ee.timer.common.*;
import com.sun.ts.tests.common.ejb.wrappers.CMP20Wrapper;
import com.sun.ts.lib.util.TestUtil;

import javax.ejb.*;
import java.util.Properties;

public abstract class FlagStoreEJB extends CMP20Wrapper {

  public abstract boolean getRequiredAccessed();

  public abstract void setRequiredAccessed(boolean accessed);

  public abstract boolean getRequiresNewAccessed();

  public abstract void setRequiresNewAccessed(boolean accessed);

  public FlagStoreEJB() {
    TestUtil.logTrace("FlagStoreEJB no arg constructor");
  }

  public Integer ejbCreate(Properties props, int id, String brandName,
      float price, boolean requiredAccess, boolean requiresNewAccess)
      throws CreateException {

    Integer pk = new Integer(id);

    try {
      setRequiredAccessed(requiredAccess);
      setRequiresNewAccessed(requiresNewAccess);
      TestUtil.logTrace("[FlagstoreBean] initialize logging...");
      TestUtil.init(props);
      setId(pk);
      setBrandName(brandName);
      setPrice(price);
    } catch (Exception e) {
      TestUtil.logErr("FlagStoreEJB Caught exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(Properties props, int id, String brandName,
      float price, boolean requiredAccess, boolean requiresNewAccess) {

    TestUtil.logTrace("[FlagstoreBean] ejbPostCreate");
  }

  // business methods are used to store and retrieve flags
  // that indicates whether this bean has been accessed
  public void setRequiredAccessFlag(boolean flag) {
    setRequiredAccessed(flag);
  }

  public boolean getRequiredAccessFlag() {
    return getRequiredAccessed();
  }

  public void setRequiresNewAccessFlag(boolean flag) {
    setRequiresNewAccessed(flag);
  }

  public boolean getRequiresNewAccessFlag() {
    return getRequiresNewAccessed();
  }
}
