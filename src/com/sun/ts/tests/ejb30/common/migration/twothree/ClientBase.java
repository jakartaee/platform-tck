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

package com.sun.ts.tests.ejb30.common.migration.twothree;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.CreateException;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import java.rmi.RemoteException;
import java.util.Properties;

/*
 * Client accesses TwoTestBean, which calls MigrationBean through its adapted
 * client view (TwoRemoteHome and TwoLocalHome);
 * Client accesses ThreeTestBean, which calls MigrationBean throught its ejb3-
 * style business interface (ThreeIF and ThreeLocalIF). This is to make sure
 * that a bean with adapted client view can still be accessed via its declared
 * business interface.
 */
abstract public class ClientBase extends EETest {
  protected Properties props;

  abstract protected TwoTestRemoteHome getTwoTestRemoteHome();

  abstract protected ThreeTestIF getThreeTestBean();

  private TwoTestRemoteIF twoTestBean;

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    createTestBean();
  }

  public void cleanup() throws Fault {
  }

  protected void removeBeans() {
    if (twoTestBean != null) {
      try {
        twoTestBean.remove();
        TLogger.log("Successfully removed twoTestBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing twoTestBean " + e);
      }
    }

    // ThreeTestBean is stateless and no need to remove.
  }

  protected void createTestBean() throws Fault {
    try {
      twoTestBean = getTwoTestRemoteHome().create();
      if (!twoTestBean.isIdentical(twoTestBean)) {
        throw new Fault("twoTestBean.isIdentical(twoTestBean) returned false");
      }
      TLogger.log(
          "twoTestBean.isIdentical(twoTestBean) returned true, as expected.");
    } catch (CreateException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: callRemoteTest
   * 
   * @test_Strategy:
   *
   */
  public void callRemoteTest() throws Fault {
    try {
      twoTestBean.callRemote();
    } catch (RemoteException e) {
      throw new Fault(e);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: callLocalTest
   * 
   * @test_Strategy:
   *
   */
  public void callLocalTest() throws Fault {
    try {
      twoTestBean.callLocal();
    } catch (RemoteException e) {
      throw new Fault(e);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: callRemoteSameTxContextTest
   * 
   * @test_Strategy:
   *
   */
  public void callRemoteSameTxContextTest() throws Fault {
    try {
      twoTestBean.callRemoteSameTxContext();
    } catch (RemoteException e) {
      throw new Fault(e);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: callLocalSameTxContextTest
   * 
   * @test_Strategy:
   *
   */
  public void callLocalSameTxContextTest() throws Fault {
    try {
      twoTestBean.callLocalSameTxContext();
    } catch (RemoteException e) {
      throw new Fault(e);
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: callThreeRemoteTest
   * 
   * @test_Strategy:
   *
   */
  public void callThreeRemoteTest() throws TestFailedException {
    ThreeTestIF threeTestBean = getThreeTestBean();
    threeTestBean.callRemote();
  }

  /*
   * testName: callThreeLocalTest
   * 
   * @test_Strategy:
   *
   */
  public void callThreeLocalTest() throws TestFailedException {
    ThreeTestIF threeTestBean = getThreeTestBean();
    threeTestBean.callLocal();
  }
}
