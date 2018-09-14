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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement;

import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocal2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocalIF;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.CreateException;
import javax.ejb.Init;
import javax.ejb.Remove;

//@Stateful(name="RemoveBean")
//@Remote({RemoveIF.class, Remove2IF.class})
//@Local({RemoveLocalIF.class, RemoveLocal2IF.class})
//@RemoteHome(TwoRemoteHome.class)
//@LocalHome(TwoLocalHome.class)
public class RemoveBean
    implements RemoveIF, Remove2IF, RemoveLocal2IF, RemoveLocalIF {
  public RemoveBean() {
  }

  public void ejbCreate() throws CreateException {

  }

  @Init
  public void create() {
    // do nothing since our stateful beans do not need
    // any specific initialization.
  }

  // @Remove
  public void remove() {
  }

  public void remove(String s) {
    // this is not a remove-method
  }

  @Remove
  public void remove2() {
  }

  // @Remove(retainIfException=true)
  public void retain() throws TestFailedException {
    throw new TestFailedException("Not to remove this bean");
  }

  @Remove(retainIfException = true)
  public void retain2() throws UncheckedAppException {
    throw new UncheckedAppException("Not to remove this bean");
  }

  public void hi() {
  }

  //////////////////////////////////////////////////////////////////////

  public String from2RemoteClient() {
    return "from2RemoteClient";
  }

  public void remoteSameTxContext() {

  }

  public String from2LocalClient() {
    return "from2LocalClient";
  }

  public void localSameTxContext() {

  }

}
