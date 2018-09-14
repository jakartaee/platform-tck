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

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
@Local(ThreeLocalIF.class)
@Remote(ThreeRemoteIF.class)
public class FourBean extends CommonBeanBase {

  // Injection of stateful (self or other) into stateful bean caused
  // StackOverflow.
  // 6633835 (Cyclic injection of stateful EJBs causes deployment failure)
  // Comment it out (also see ThreeBean) since it is not required by spec

  // @EJB(beanName="ThreeBean")
  // private ThreeLocalIF threeLocal;
  //
  // @EJB(beanName="ThreeBean")
  // private ThreeRemoteIF threeRemote;

  @Override
  protected void verifyInjectedEJB() {
    // if(threeLocal == null)
    // throw new IllegalStateException("threeLocal was not injected.");
    // if(threeRemote == null)
    // throw new IllegalStateException("threeRemote was not injected.");
  }

  @Override
  @Remove(retainIfException = false)
  public String getShortName() {
    return super.getShortName();
  }

  @Override
  protected String getBeanName() {
    return "FourBean";
  }
}
