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

package com.sun.ts.tests.ejb30.misc.threebeans;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
@Local(ThreeLocalIF.class)
@Remote(ThreeRemoteIF.class)
public class ThreeBean extends CommonBeanBase {
  @EJB
  private OneRemoteIF oneRemote;

  @EJB
  private OneLocalIF oneLocal;

  @EJB
  private TwoRemoteIF twoRemote;

  @EJB
  private TwoLocalIF twoLocal;

  // @EJB(beanName="FourBean")
  // private ThreeRemoteIF fourRemote;
  //
  // @EJB(beanName="FourBean")
  // private ThreeLocalIF fourLocal;

  @Override
  protected void verifyInjectedEJB() {
    if (twoRemote == null)
      throw new IllegalStateException("twoRemote was not injected.");
    if (twoLocal == null)
      throw new IllegalStateException("twoLocal was not injected.");
    if (oneRemote == null)
      throw new IllegalStateException("oneRemote was not injected.");
    if (oneLocal == null)
      throw new IllegalStateException("oneLocal was not injected.");
    // if(fourRemote== null)
    // throw new IllegalStateException("fourRemote was not injected.");
    // if(fourLocal == null)
    // throw new IllegalStateException("fourLocal was not injected.");
  }

  @Override
  @Remove(retainIfException = false)
  public String getShortName() {
    return super.getShortName();
  }

  @Override
  protected String getBeanName() {
    return "ThreeBean";
  }

}
