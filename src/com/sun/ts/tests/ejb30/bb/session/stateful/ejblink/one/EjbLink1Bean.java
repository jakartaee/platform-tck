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

package com.sun.ts.tests.ejb30.bb.session.stateful.ejblink.one;

import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF;
import com.sun.ts.tests.ejb30.common.ejblink.Constants;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.naming.NamingException;

@Stateful(name = "EjbLink1Bean")
@Remote({ EjbLinkIF.class })
@Local(EjbLinkLocalIF.class)
public class EjbLink1Bean extends EjbLinkBeanBase
    implements EjbLinkIF, EjbLinkLocalIF, Constants {

  @Resource
  private SessionContext sessionContext;

  public EjbLink1Bean() {
  }

  @Remove
  public void remove() {
  }

  //////////////////////////////////////////////////////////////////////
  public void callOne() throws TestFailedException {
    throw new IllegalStateException("Cannot call bean1 from bean1");
  }

  public void callThree() throws TestFailedException {
    throw new IllegalStateException("Cannot call local bean3 from bean1");
  }

  public void callTwo() throws TestFailedException {
    Object obj = null;
    EjbLinkIF bean2 = null;
    try {
      obj = ServiceLocator.lookup(BEAN2_REF_NAME);
      bean2 = (EjbLinkIF) obj;
      bean2.call();
    } catch (NamingException e) {
      throw new TestFailedException(e);
    } finally {
      if (bean2 != null) {
        bean2.remove();
      }
    }
  }
}
