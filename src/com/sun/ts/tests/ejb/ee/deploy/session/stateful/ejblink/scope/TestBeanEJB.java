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
 * @(#)TestBeanEJB.java	1.9 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.ejblink.scope;

import jakarta.ejb.EJBException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.StatefulWrapper;

public class TestBeanEJB extends StatefulWrapper {

  private static final String prefix = "java:comp/env/ejb/";

  private static final String lookup1 = prefix + "Bean_SameJAR";

  private static final String lookup2 = prefix + "Bean_OtherJAR";

  /* Expected values */
  private static final String bean1RefName = "Scorsese";

  private static final String bean2RefName = "Besson";

  public boolean testSimpleLinkScope() {

    ReferencedBeanHome home = null;
    ReferencedBean bean = null;
    ReferencedBean2Home home2 = null;
    ReferencedBean2 bean2 = null;
    String bean1Name;
    String bean2Name;
    boolean pass = false;

    try {
      TestUtil.logTrace("TestBean: looking up " + lookup1);
      home = (ReferencedBeanHome) nctx.lookup(lookup1,
          ReferencedBeanHome.class);
      bean = home.create(props);
      bean1Name = bean.whoAreYou();
      TestUtil.logTrace("TestBean: " + lookup1 + " is " + bean1Name);
      bean.remove();

      TestUtil.logTrace("TestBean: looking up " + lookup2);
      home2 = (ReferencedBean2Home) nctx.lookup(lookup2,
          ReferencedBean2Home.class);
      bean2 = home2.create(props);
      bean2Name = bean2.whoAreYou();
      TestUtil.logTrace("TestBean: " + lookup2 + " is " + bean2Name);
      bean2.remove();

      pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);

      if (!pass) {
        TestUtil.logErr("TestBean: Expected " + lookup1 + " name to be "
            + bean1RefName + " and " + lookup2 + " name to be " + bean2RefName);
      }
    } catch (Exception e) {

      TestUtil.logErr(
          "TestBean: Caught exception in " + "whoIsYourPartner(): " + e, e);
      throw new EJBException(e.getMessage());
    }

    return pass;
  }

}
