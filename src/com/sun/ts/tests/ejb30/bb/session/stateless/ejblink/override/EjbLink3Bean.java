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
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.Local;

@Stateless(name = "EjbLink3Bean")
@Local({})

/*
 * This bean is to be packaged in the same ejb-jar as EjbLink2Bean. bean2 calls
 * bean3.call(), but this bean does not call any other beans.
 */
public class EjbLink3Bean extends EjbLinkBeanBase implements EjbLinkIF {

  @Resource
  private SessionContext sessionContext;

  public EjbLink3Bean() {
  }

  public void remove() {
  }

  //////////////////////////////////////////////////////////////////////

  public void callThree() throws TestFailedException {
    throw new IllegalStateException("Cannot call bean3 from bean3");
  }

  public void callTwo() throws TestFailedException {
    throw new IllegalStateException("Cannot call bean2 from bean3");
  }

  public void callOne() throws TestFailedException {
    throw new IllegalStateException("Cannot call bean1 from bean3");
  }
}
