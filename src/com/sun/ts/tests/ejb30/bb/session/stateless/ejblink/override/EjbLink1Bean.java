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

package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override;

import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF;
import com.sun.ts.tests.ejb30.common.ejblink.Constants;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.NamingException;

@Stateless(name = "EjbLink1Bean")
@Remote({ EjbLinkIF.class })
@Local(EjbLinkLocalIF.class)
public class EjbLink1Bean extends EjbLinkBeanBase
    implements EjbLinkIF, EjbLinkLocalIF, Constants {

  @Resource
  private SessionContext sessionContext;

  // this ejb-ref is overridden in one_ejb.xml
  @EJB(name = "ejb/EjbLink2Bean", beanName = "no-such-bean-name", description = "should be overridden by ejb-jar.xml")
  private EjbLinkIF bean2;

  public EjbLink1Bean() {
  }

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
    try {
      obj = ServiceLocator.lookup(BEAN2_REF_NAME);
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
    EjbLinkIF bean2 = (EjbLinkIF) obj;
    bean2.call();
  }
}
