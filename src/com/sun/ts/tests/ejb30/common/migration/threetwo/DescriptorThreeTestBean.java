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

package com.sun.ts.tests.ejb30.common.migration.threetwo;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestBeanBase;
import com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestIF;
import com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalHome;
import com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteHome;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

//@Stateless(name="ThreeTestBean")
//@Remote(ThreeTestIF.class)
public class DescriptorThreeTestBean extends ThreeTestBeanBase
    implements ThreeTestIF {
  private static final String TWO_REMOTE_HOME_NAME = "java:comp/env/twoRemoteHome";

  private static final String TWO_LOCAL_HOME_NAME = "java:comp/env/twoLocalHome";

  // @EJB(name="twoRemoteHome")
  // ejb-ref declared in descriptor, but not injected
  private TwoRemoteHome twoRemoteHome;

  // @EJB(name="twoLocalHome")
  // ejb-ref declared in descriptor, but not injected
  private TwoLocalHome twoLocalHome;

  // @Resource
  private SessionContext sctx;

  public DescriptorThreeTestBean() {
  }

  public void remove() {
  }

  protected TwoRemoteHome getTwoRemoteHome() {
    if (twoRemoteHome == null) {
      try {
        Object obj = ServiceLocator.lookup(TWO_REMOTE_HOME_NAME);
        twoRemoteHome = (TwoRemoteHome) PortableRemoteObject.narrow(obj,
            TwoRemoteHome.class);
      } catch (NamingException e) {
        throw new IllegalStateException(e);
      }
    }
    return twoRemoteHome;
  }

  protected TwoLocalHome getTwoLocalHome() {
    if (twoLocalHome == null) {
      try {
        Object obj = ServiceLocator.lookup(TWO_LOCAL_HOME_NAME);
        twoLocalHome = (TwoLocalHome) PortableRemoteObject.narrow(obj,
            TwoLocalHome.class);
      } catch (NamingException e) {
        throw new IllegalStateException(e);
      }
    }
    return twoLocalHome;
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sctx;
  }

}
