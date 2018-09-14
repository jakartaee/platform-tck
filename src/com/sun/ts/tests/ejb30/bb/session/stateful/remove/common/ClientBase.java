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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.common;

import com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException;
import com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.rmi.NoSuchObjectException;
import javax.ejb.CreateException;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.Handle;
import javax.ejb.NoSuchEJBException;
import com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteHome;
import com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF;
import javax.ejb.RemoveException;

abstract public class ClientBase extends EETest {
  protected Properties props;

  private RemoveIF removeBean;

  private Remove2IF removeBean2;

  private RemoveNotRetainIF removeNotRetainBean;

  private TestIF testBean;

  abstract protected RemoveIF getRemoveBean();

  abstract protected Remove2IF getRemoveBean2();

  abstract protected RemoveNotRetainIF getRemoveNotRetainBean();

  abstract protected TestIF getTestBean();

  abstract protected TwoRemoteHome getTwoRemoteHome();

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    removeBean = getRemoveBean();
    removeBean2 = getRemoveBean2();
    testBean = getTestBean();
    removeNotRetainBean = getRemoveNotRetainBean();
  }

  public void cleanup() throws Fault {
    removeBeans();
  }

  protected void removeBeans() {
    if (removeBean != null) {
      try {
        removeBean.remove();
        TLogger.log("Successfully removed removeBean.");
      } catch (Exception e) {
        TLogger.log("Exception while removing removeBean " + e);
      }
    }
    if (removeBean2 != null) {
      try {
        removeBean2.remove();
        TLogger.log("Successfully removed removeBean2.");
      } catch (Exception e) {
        TLogger.log("Exception while removing removeBean2 " + e);
      }
    }
    // no need to remove stateless bean
    // if(testBean != null) {
    // try {
    // testBean.remove();
    // TLogger.log("Successfully removed testBean.");
    // } catch (Exception e) {
    // TLogger.log("Exception while removing testBean " + e);
    // }
    // }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: removeBean
   * 
   * @test_Strategy:
   *
   */
  public void removeBean() throws Fault {
    removeBean.remove();
    try {
      removeBean.remove();
      throw new Fault("Expecting javax.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
      removeBean = null;
    }
  }

  /*
   * testName: removeBean2
   * 
   * @test_Strategy:
   *
   */
  public void removeBean2() throws Fault {
    // removeBean2 is of type Remove2IF, which implements java.rmi.Remote
    try {
      removeBean2.remove();
    } catch (RemoteException e) {
      throw new Fault(e);
    }
    try {
      removeBean2.remove();
      throw new Fault(
          "Expecting java.rmi.NoSuchObjectException, but got none.");
    } catch (NoSuchObjectException e) {
      TLogger.log("Got expected exception: " + e.toString());
      removeBean2 = null;
    } catch (RemoteException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanremoveBean
   * 
   * @test_Strategy:
   *
   */
  public void testBeanremoveBean() throws Fault {
    try {
      testBean.removeBean();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanremoveBeanRemote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  public void testBeanremoveBeanRemote() throws Fault {
    try {
      testBean.removeBeanRemote();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanremoveBean2
   * 
   * @test_Strategy:
   *
   */
  public void testBeanremoveBean2() throws Fault {
    try {
      testBean.removeBean2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanremoveBean2Remote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */
  public void testBeanremoveBean2Remote() throws Fault {
    try {
      testBean.removeBean2Remote();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: retainBean
   * 
   * @test_Strategy:
   *
   */
  public void retainBean() throws Fault {
    try {
      removeBean.retain();
    } catch (TestFailedException e) {
      // expected. the bean is not removed
    }
    try {
      removeBean.retain2();
    } catch (AtUncheckedAppException e) {
      // expected. the bean is not removed
    } catch (UncheckedAppException e) {
      // expected. the bean is not removed
    }
    try {
      removeBean.remove();
      TLogger.log("removeBean removed successfully");
    } catch (NoSuchEJBException e) {
      throw new Fault("Failed to remove bean, which should be there when this"
          + "method was called");
    } finally {
      removeBean = null;
    }
  }

  /*
   * testName: retainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  public void retainBeanOverloaded() throws Fault {
    removeBean.remove("This is not a remove-method. Not to remove bean.");
    removeBean.hi();
    try {
      removeBean.remove();
      TLogger.log("removeBean removed successfully");
    } catch (NoSuchEJBException e) {
      throw new Fault("Failed to remove bean, which should be there when this"
          + "method was called. But got " + e);
    } finally {
      removeBean = null;
    }
  }

  /*
   * testName: retainBean2
   * 
   * @test_Strategy:
   *
   */
  public void retainBean2() throws Fault {
    // removeBean2.retain(); does not exit any more
    // try {
    // removeBean2.retain();
    // } catch (TestFailedException e) {
    // //expected. the bean is not removed
    // } catch (RemoteException e) {
    // throw new Fault(e);
    // }
    try {
      removeBean2.retain2();
    } catch (AtUncheckedAppException e) {
      // expected. the bean is not removed
    } catch (UncheckedAppException e) {
      // expected. the bean is not removed
    } catch (RemoteException e) {
      throw new Fault(e);
    }
    try {
      removeBean2.remove();
      TLogger.log("removeBean2 removed successfully");
    } catch (RemoteException e) {
      throw new Fault(e);
    } finally {
      removeBean2 = null;
    }
  }

  /*
   * testName: testBeanretainBean
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBean() throws Fault {
    try {
      testBean.retainBean();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanretainBeanOverloaded
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBeanOverloaded() throws Fault {
    try {
      testBean.retainBeanOverloaded();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanretainBeanRemote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF.
   *
   */
  public void testBeanretainBeanRemote() throws Fault {
    try {
      testBean.retainBeanRemote();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanretainBean2
   * 
   * @test_Strategy:
   *
   */
  public void testBeanretainBean2() throws Fault {
    try {
      testBean.retainBean2();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanretainBean2Remote
   * 
   * @test_Strategy: client remotely invokes testBean, which remotely invokes
   * RemoveBean via RemoteIF2.
   *
   */
  public void testBeanretainBean2Remote() throws Fault {
    try {
      testBean.retainBean2Remote();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: removeNotRetainBean
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBean() throws Fault {
    try {
      removeNotRetainBean.remove();
    } catch (TestFailedException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    }
    try {
      removeNotRetainBean.remove();
      throw new Fault("Expecting javax.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (TestFailedException e) {
      throw new Fault("Unexpected exception", e);
    } finally {
      removeNotRetainBean = null;
    }
  }

  /*
   * testName: removeNotRetainBean2
   * 
   * @test_Strategy:
   *
   */
  public void removeNotRetainBean2() throws Fault {
    try {
      removeNotRetainBean.remove2();
    } catch (AtUncheckedAppException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    } catch (UncheckedAppException e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + "despite the exception");
    }
    try {
      removeNotRetainBean.remove2();
      throw new Fault("Expecting javax.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (AtUncheckedAppException e) {
      throw new Fault("Unexpected exception", e);
    } catch (UncheckedAppException e) {
      throw new Fault("Unexpected exception", e);
    } finally {
      removeNotRetainBean = null;
    }
  }

  /*
   * testName: alwaysRemoveAfterSystemException
   * 
   * @test_Strategy: a bean must always be removed after a system exception,
   * even though the remove method is retainIfException true.
   *
   */
  public void alwaysRemoveAfterSystemException() throws Fault {
    try {
      removeNotRetainBean.alwaysRemoveAfterSystemException();
    } catch (Exception e) {
      TLogger.log("Got expected exception, and bean should have been removed"
          + " after the system exception: " + e);
    }
    try {
      removeNotRetainBean.alwaysRemoveAfterSystemException();
      throw new Fault("Expecting javax.ejb.NoSuchEJBException, but got none.");
    } catch (NoSuchEJBException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } finally {
      removeNotRetainBean = null;
    }
  }

  /*
   * testName: removeTwoRemoteHome
   * 
   * @test_Strategy:
   *
   */
  public void removeTwoRemoteHome() throws Fault {
    TwoRemoteHome beanHome = getTwoRemoteHome();
    TwoRemoteIF bean = null;
    try {
      bean = beanHome.create();
      bean.remove();
    } catch (RemoveException e) {
      throw new Fault(e);
    } catch (CreateException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }
    try {
      bean.remove();
      throw new Fault(
          "Expecting java.rmi.NoSuchObjectException, but got none.");
    } catch (NoSuchObjectException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (RemoveException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: removeTwoRemoteHomeHandle
   * 
   * @test_Strategy:
   *
   */
  public void removeTwoRemoteHomeHandle() throws Fault {
    TwoRemoteHome beanHome = getTwoRemoteHome();
    TwoRemoteIF bean = null;
    try {
      bean = beanHome.create();
      beanHome.remove(bean);
    } catch (RemoveException e) {
      TLogger.log("Got expected exception " + e.toString());
    } catch (CreateException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }

    try {
      Handle handle = bean.getHandle();
      beanHome.remove(handle);
      TLogger.log("Successfully removed bean handler " + handle);
    } catch (RemoveException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }

    try {
      bean.remove();
      throw new Fault(
          "Expecting java.rmi.NoSuchObjectException, but got none.");
    } catch (NoSuchObjectException e) {
      TLogger.log("Got expected exception: " + e.toString());
    } catch (RemoveException e) {
      throw new Fault(e);
    } catch (RemoteException e) {
      throw new Fault(e);
    }
  }

  /*
   * testName: testBeanRemoveTwoLocal
   * 
   * @test_Strategy:
   *
   */
  public void testBeanRemoveTwoLocal() throws Fault {
    try {
      testBean.removeTwoLocal();
    } catch (TestFailedException e) {
      throw new Fault(e);
    }
  }

}
