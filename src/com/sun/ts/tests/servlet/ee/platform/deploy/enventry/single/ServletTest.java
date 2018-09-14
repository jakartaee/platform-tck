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

package com.sun.ts.tests.servlet.ee.platform.deploy.enventry.single;

import com.sun.ts.tests.assembly.util.shared.enventry.single.TestCode;
import com.sun.ts.tests.common.web.ServletWrapper;

import java.util.Properties;

/** Servlet test driver */
public class ServletTest extends ServletWrapper {

  public Boolean testCharacterEntry(Properties props) {
    boolean pass;

    pass = TestCode.testCharacterEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testStringEntry(Properties props) {
    boolean pass;

    pass = TestCode.testStringEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testBooleanEntry(Properties props) {
    boolean pass;

    pass = TestCode.testBooleanEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testByteEntry(Properties props) {
    boolean pass;

    pass = TestCode.testByteEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testShortEntry(Properties props) {
    boolean pass;

    pass = TestCode.testShortEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testIntegerEntry(Properties props) {
    boolean pass;

    pass = TestCode.testIntegerEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testLongEntry(Properties props) {
    boolean pass;

    pass = TestCode.testLongEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testFloatEntry(Properties props) {
    boolean pass;

    pass = TestCode.testFloatEntry(nctx);
    return Boolean.valueOf(pass);
  }

  public Boolean testDoubleEntry(Properties props) {
    boolean pass;

    pass = TestCode.testDoubleEntry(nctx);
    return Boolean.valueOf(pass);
  }

}
