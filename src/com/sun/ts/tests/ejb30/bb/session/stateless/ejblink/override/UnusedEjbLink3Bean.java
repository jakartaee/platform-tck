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
@Local

/*
 * This bean has the same name as EjbLink3Bean. This bean is packaged into
 * one_ejb.jar. All references to EjbLink3Bean are to the bean whose class name
 * is EjbLink3Bean, not to this one. It is added to one_ejb.jar to verify beans
 * in different ejb jars within the same ear can share names.
 */
public class UnusedEjbLink3Bean extends EjbLinkBeanBase implements EjbLinkIF {

  @Resource
  private SessionContext sessionContext;

  public UnusedEjbLink3Bean() {
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

  public void callOneLocal() throws TestFailedException {
    throw new IllegalStateException("Should not get here");
  }

  public void call() throws TestFailedException {
    throw new IllegalStateException("Should not get here");
  }

  public void localCall(int[] f) throws TestFailedException {
    throw new IllegalStateException("Should not get here");
  }

}
