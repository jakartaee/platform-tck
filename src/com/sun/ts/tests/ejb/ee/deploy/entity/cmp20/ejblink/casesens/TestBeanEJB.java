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
 * @(#)TestBeanEJB.java	1.6 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.ejblink.casesens;

import java.util.Properties;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.CMP20Wrapper;

public abstract class TestBeanEJB extends CMP20Wrapper {

  private static final String prefix = "java:comp/env/ejb/";

  /* These lookups differ only by case */
  private static final String bean1Lookup = prefix + "Samba";

  private static final String bean2Lookup = prefix + "Bossa";

  private static final String bean1RefName = "Bahia";

  private static final String bean2RefName = "Rio";

  /*
   * Business methods.
   */

  /**
   * Check that two ejblink entries whose names differ only by case are
   * associated with different runtime values (as specified in DD).
   */
  public boolean testCaseSensitivity(java.util.Properties props) {
    CaseBeanHome home1 = null;
    CaseBeanHome home2 = null;
    CaseBean bean1 = null;
    CaseBean bean2 = null;
    String bean1Name;
    String bean2Name;
    boolean pass = false;

    try {

      TestUtil.logTrace("TestBean: Looking up '" + bean1Lookup + "'...");
      home1 = (CaseBeanHome) nctx.lookup(bean1Lookup, CaseBeanHome.class);
      bean1 = home1.create(props, 2, "expresso", 10);
      bean1Name = bean1.whoAreYou();
      TestUtil.logTrace(bean1Lookup + "name is '" + bean1Name + "'");
      bean1.remove();

      TestUtil.logTrace("TestBean: Looking up '" + bean2Lookup + "'...");
      home2 = (CaseBeanHome) nctx.lookup(bean2Lookup, CaseBeanHome.class);
      bean2 = home2.create(props, 3, "capuccino", 10);
      bean2Name = bean2.whoAreYou();
      TestUtil.logTrace(bean2Lookup + " name is '" + bean2Name + "'");
      bean2.remove();

      pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);

      if (!pass) {
        TestUtil.logErr("TestBean: " + bean1Lookup + "name is '" + bean1Name
            + "' expected '" + bean1RefName + "'");
        TestUtil.logErr("TestBean: " + bean2Lookup + "name is '" + bean2Name
            + "' expected '" + bean2RefName + "'");
      }

    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception in " + "testCase(): " + e, e);
      pass = false;
    }

    return pass;
  }

}
