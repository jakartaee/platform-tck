/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.common.migration.threetwo;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

abstract public class ClientBase extends EETest {
  protected Properties props;

  private ThreeTestIF threeTestBean;

  abstract protected ThreeTestIF getTestBean();

  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    threeTestBean = getTestBean();
  }

  public void cleanup() throws Exception {
  }

  protected void removeBeans() {
    if (threeTestBean != null) {
      try {
        threeTestBean.remove();
        TLogger.log("Successfully removed threeTestBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing threeTestBean " + e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: callRemoteTest
   * 
   * @test_Strategy:
   *
   */
  public void callRemoteTest() throws Exception {
    try {
      threeTestBean.callRemote();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: callLocalTest
   * 
   * @test_Strategy:
   *
   */
  public void callLocalTest() throws Exception {
    try {
      threeTestBean.callLocal();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: callRemoteSameTxContextTest
   * 
   * @test_Strategy:
   *
   */
  public void callRemoteSameTxContextTest() throws Exception {
    try {
      threeTestBean.callRemoteSameTxContext();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }

  /*
   * testName: callLocalSameTxContextTest
   * 
   * @test_Strategy:
   *
   */
  public void callLocalSameTxContextTest() throws Exception {
    try {
      threeTestBean.callLocalSameTxContext();
    } catch (TestFailedException e) {
      throw new Exception(e);
    }
  }
}
