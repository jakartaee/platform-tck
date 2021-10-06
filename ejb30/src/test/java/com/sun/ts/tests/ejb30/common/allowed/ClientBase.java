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

package com.sun.ts.tests.ejb30.common.allowed;

import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

public abstract class ClientBase extends EETest implements Constants {
  private AllowedIF allowedBean;

  private CallbackAllowedIF callbackAllowedBean;

  private SessionContextAllowedIF sessionContextAllowedBean;

  private SessionContextAllowedIF injectionAllowedBean;

  protected Properties props;

  protected SessionContextAllowedIF lookupInjectionAllowedBean()
      throws javax.naming.NamingException {
    return (SessionContextAllowedIF) ServiceLocator
        .lookup(injectionAllowedBeanName);
  }

  protected AllowedIF lookupAllowedBean() throws javax.naming.NamingException {
    return (AllowedIF) ServiceLocator.lookup(allowedBeanName);
  }

  protected CallbackAllowedIF lookupCallbackAllowedBean()
      throws javax.naming.NamingException {
    return (CallbackAllowedIF) ServiceLocator.lookup(callbackAllowedBeanName);
  }

  protected SessionContextAllowedIF lookupSessionContextAllowedBean()
      throws javax.naming.NamingException {
    return (SessionContextAllowedIF) ServiceLocator
        .lookup(sessionContextAllowedBeanName);
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  public void cleanup() throws Fault {
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (allowedBean != null) {
      try {
        allowedBean.remove();
        TLogger.log("allowedBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove allowedBean.");
      }
    }
    if (callbackAllowedBean != null) {
      try {
        callbackAllowedBean.remove();
        TLogger.log("callbackAllowedBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove callbackAllowedBean.");
      }
    }
    if (sessionContextAllowedBean != null) {
      try {
        sessionContextAllowedBean.remove();
        TLogger.log("sessionContextAllowedBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove sessionContextAllowedBean.");
      }
    }
  }

  public void checkResults(Properties results, Properties expected)
      throws Fault {
    if (results == null) {
      throw new Fault("results are null");
    }
    if (expected == null) {
      throw new Fault("expected results are null");
    }
    int resultsCount = results.size();
    int expectedCount = expected.size();
    TLogger.log("Expected results count: " + expectedCount);
    expected.list(System.out);
    TLogger.log(TLogger.NL);
    TLogger.log("Actual results count: " + resultsCount);
    results.list(System.out);
    if (expectedCount != resultsCount) {
      TLogger.log(TLogger.NL
          + "Continue to compare each element of expected and actual results.");
    }

    Enumeration keys = expected.keys();
    String reason = "";
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      String val = (String) expected.getProperty(key);
      String actual = results.getProperty(key);
      if (val.equals(actual)) {
        // good
      } else {
        reason += TLogger.NL + "Expected " + key + "=" + val + ", but actual "
            + actual;
      }
    }
    if (reason.length() > 0) {
      TLogger.log(TLogger.NL + reason);
      throw new Fault("failed in result check");
    }
  }

  /////////////////////////////////////////////////////////////////////////

  /*
   * testName: injectionMethod
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */

  public void injectionMethod() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    expected.setProperty(EJBContext_lookup, allowed);

    try {
      injectionAllowedBean = lookupInjectionAllowedBean();
      results = injectionAllowedBean.getResults();
    } catch (Exception e) {
      throw new Fault(e);
    }
    checkResults(results, expected);
  }
}
