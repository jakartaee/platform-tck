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
package com.sun.ts.tests.interop.csiv2.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import javax.ejb.*;
import java.util.*;

/**
 * CSIv2 Application Client
 */
public class CSIv2AppClient implements CSIv2TestLogicInterface {
  private CSIv2TestLogicImpl csiv2TestLogicImpl;

  public CSIv2AppClient() {
    csiv2TestLogicImpl = new CSIv2TestLogicImpl();
  }

  /**
   * @see CSIv2TestLogicInterface for javadocs.
   */
  public void invoke(ArrayList chain, Properties p) {
    TestUtil.logMsg("CSIv2AppClient.invoke()");
    csiv2TestLogicImpl.invoke(chain, p);
  }
}
