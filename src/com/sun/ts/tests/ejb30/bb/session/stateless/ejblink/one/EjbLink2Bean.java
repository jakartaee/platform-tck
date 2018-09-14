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

package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one;

import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF;
import com.sun.ts.tests.ejb30.common.ejblink.Constants;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.naming.NamingException;

@Stateless(name = "EjbLink2Bean")
@Remote({ EjbLinkIF.class })
public class EjbLink2Bean extends EjbLinkBeanBase
    implements EjbLinkIF, Constants {

  @Resource
  private SessionContext sessionContext;

  public EjbLink2Bean() {
  }

  public void remove() {
  }

  //////////////////////////////////////////////////////////////////////

  public void callTwo() throws TestFailedException {
    throw new IllegalStateException("Cannot call bean2 from bean2");
  }

  public void callOne() throws TestFailedException {
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(BEAN1_REF_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
    EjbLinkIF bean1 = (EjbLinkIF) obj;
    bean1.call();
  }

  public void callThree() throws TestFailedException {
    Object obj = null;
    try {
      obj = ServiceLocator.lookup(BEAN3_REF_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
    EjbLinkIF bean3 = (EjbLinkIF) obj;
    bean3.call();
  }
}
