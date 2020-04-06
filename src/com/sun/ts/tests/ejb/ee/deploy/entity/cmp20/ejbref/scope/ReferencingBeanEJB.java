/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)ReferencingBeanEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.ejbref.scope;

import java.util.Properties;
import jakarta.ejb.EJBException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.CMP20Wrapper;

public abstract class ReferencingBeanEJB extends CMP20Wrapper {

  private static final String lookupName = "java:comp/env/ejb/Partner";

  public String whoIsYourPartner(Properties props, int pkey) {

    ReferencedBeanHome home = null;
    ReferencedBean bean = null;
    String partnerName;

    try {
      TestUtil.logTrace("ReferencingBean: looking up " + lookupName);
      home = (ReferencedBeanHome) nctx.lookup(lookupName,
          ReferencedBeanHome.class);
      bean = home.create(props, pkey, "expresso" + pkey, 8);
      partnerName = bean.whoAreYou();
      TestUtil.logTrace("ReferencingBean: my partner is " + partnerName);
      bean.remove();
    } catch (Exception e) {
      TestUtil.logErr(
          "ReferencingBean: Caught exception in " + "whoIsYourPartner(): " + e,
          e);
      throw new EJBException(e.getMessage());
    }

    return partnerName;
  }

}
