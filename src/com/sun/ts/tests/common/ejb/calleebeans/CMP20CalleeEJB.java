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

package com.sun.ts.tests.common.ejb.calleebeans;

import java.util.Properties;
import javax.ejb.CreateException;
import com.sun.ts.tests.common.ejb.wrappers.CMP20Wrapper;
import com.sun.ts.lib.util.TestUtil;

public abstract class CMP20CalleeEJB extends CMP20Wrapper {

  /** Modify arg, and call CMP20Wrapper create method */
  public Integer ejbCreate(Properties props, int id, String brandName,
      float price, SimpleArgument arg) throws CreateException {

    try {
      TestUtil.init(props);
      TestUtil.logTrace("[CMP20Callee] ejbCreate()");
      super.ejbCreate(props, id, brandName, price);
      logArgStatus("create input", arg);
      arg.modify();
      logArgStatus("create output", arg);
    } catch (Exception e) {
      TestUtil.logErr("[CMP20Callee] Caught exception: ", e);
      throw new CreateException(e.getMessage());
    }

    return null;
  }

  public void ejbPostCreate(Properties props, int id, String brandName,
      float price, SimpleArgument arg) throws CreateException {

    TestUtil.logTrace("[CMP20Callee] ejbPostCreate()");
  }

  public void call(Properties props, SimpleArgument arg) {
    logArgStatus("input", arg);
    arg.modify();
    logArgStatus("output", arg);
  }

  public void logArgStatus(String msg, SimpleArgument arg) {
    TestUtil.logTrace("[CMP20Callee] " + msg + " arg = " + arg.getValue());
  }

}
