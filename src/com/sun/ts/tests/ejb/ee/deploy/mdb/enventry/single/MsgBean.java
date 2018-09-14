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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.single;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.MDBWrapper;
import com.sun.ts.tests.assembly.util.shared.enventry.single.TestCode;

public class MsgBean extends MDBWrapper {

  public Boolean testString(Properties props) {
    boolean pass = true;

    pass = TestCode.testStringEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testBoolean(Properties props) {
    boolean pass;

    pass = TestCode.testBooleanEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testByte(Properties props) {
    boolean pass;

    pass = TestCode.testByteEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testShort(Properties props) {
    boolean pass;

    pass = TestCode.testShortEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testInteger(Properties props) {
    boolean pass;

    pass = TestCode.testIntegerEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testLong(Properties props) {
    boolean pass;

    pass = TestCode.testLongEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testFloat(Properties props) {
    boolean pass;

    pass = TestCode.testFloatEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testDouble(Properties props) {
    boolean pass;

    pass = TestCode.testDoubleEntry(nctx);
    return new Boolean(pass);
  }

  public Boolean testAll(Properties props) {
    boolean pass;

    try {
      pass = TestCode.testStringEntry(nctx);
      if (!pass) {
        throw new Exception("testStringEntry failed!");
      }

      pass = TestCode.testBooleanEntry(nctx);
      if (!pass) {
        throw new Exception("testBooleanEntry failed!");
      }

      pass = TestCode.testByteEntry(nctx);
      if (!pass) {
        throw new Exception("testByteEntry failed!");
      }

      pass = TestCode.testShortEntry(nctx);
      if (!pass) {
        throw new Exception("testShortEntry failed!");
      }

      pass = TestCode.testIntegerEntry(nctx);
      if (!pass) {
        throw new Exception("testIntegerEntry failed!");
      }

      pass = TestCode.testLongEntry(nctx);
      if (!pass) {
        throw new Exception("testLongEntry failed!");
      }

      pass = TestCode.testFloatEntry(nctx);
      if (!pass) {
        throw new Exception("testFloatEntry failed!");
      }

      pass = TestCode.testDoubleEntry(nctx);
      if (!pass) {
        throw new Exception("testDoubleEntry failed!");
      }

    } catch (Exception e) {
      TestUtil.logErr("[MsgBean] Caught exception: ", e);
      pass = false;
    }

    return new Boolean(pass);
  }

}
