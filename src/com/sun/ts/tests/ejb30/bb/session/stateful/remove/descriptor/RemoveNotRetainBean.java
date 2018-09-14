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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor;

import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF;
import com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.Init;
import javax.annotation.Resource;

//@Stateful(name="RemoveNotRetainBean")
//@Remote({RemoveNotRetainIF.class})
//@Local({RemoveLocalIF.class, RemoveLocal2IF.class})
//@RemoteHome(TwoRemoteHome.class)
//@LocalHome(TwoLocalHome.class)
public class RemoveNotRetainBean implements RemoveNotRetainIF {
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  public RemoveNotRetainBean() {
  }

  public void ejbCreate() throws CreateException {
  }

  @Init
  public void create() {
    // do nothing since our stateful beans do not need
    // any specific initialization.
  }

  // @Remove(retainIfException=false)
  public void remove() throws TestFailedException {
    throw new TestFailedException(
        "This bean should be removed despite this exception");
  }

  // @Remove
  public void remove2() throws AtUncheckedAppException {
    throw new AtUncheckedAppException(
        "This bean should be removed despite this exception");
  }

  public void hi() {
  }

  // @Remove(retainIfException=true)
  public void alwaysRemoveAfterSystemException() {
    throw new IllegalArgumentException(
        "Ignore this exception. " + "This bean should be removed after this "
            + "system exception, even though retainIfException is set to true");
  }

  // @Remove(retainIfException=true)
  public void remove(boolean retainIfException) throws TestFailedException {
    throw new TestFailedException(
        "This bean should not be removed after this exception. "
            + "This test is not suitable for annotated bean; it should be used"
            + " with a bean using descriptor to specify overloaded remove-method.");
  }

  // @Remove(retainIfException=true)
  public void remove2(boolean retainIfException, boolean retainIfException2)
      throws AtUncheckedAppException, UncheckedAppException {
    throw new AtUncheckedAppException(
        "This bean should not be removed after this exception. "
            + "This test is not suitable for annotated bean; it should be used"
            + " with a bean using descriptor to specify overloaded remove-method.");
  }

}
