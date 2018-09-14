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

package com.sun.ts.tests.servlet.ee.platform.deploy.resref.single;

import com.sun.ts.tests.assembly.util.shared.resref.single.TestCode;
import com.sun.ts.tests.common.web.ServletWrapper;

import java.util.Properties;

/** Servlet test driver */
public class ServletTest extends ServletWrapper {

  public Boolean testDatasource(Properties props) {
    boolean pass;

    pass = TestCode.testDatasource(nctx);
    return new Boolean(pass);
  }

  public Boolean testSession(Properties props) {
    boolean pass;

    pass = TestCode.testSession(nctx);
    return new Boolean(pass);
  }

  public Boolean testURL(Properties props) {
    boolean pass;

    pass = TestCode.testURL(nctx);
    return new Boolean(pass);
  }

  public Boolean testQueue(Properties props) {
    boolean pass;

    pass = TestCode.testQueue(nctx);
    return new Boolean(pass);
  }

  public Boolean testTopic(Properties props) {
    boolean pass;

    pass = TestCode.testTopic(nctx);
    return new Boolean(pass);
  }

  public Boolean testAll(Properties props) {
    boolean pass;

    pass = TestCode.testAll(nctx);
    return new Boolean(pass);
  }

}
