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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.*;
import javax.annotation.Resources;
import javax.ejb.EJB;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import javax.annotation.Resource;
import javax.ejb.EJBs;
import javax.naming.NamingException;
import org.omg.CORBA.ORB;

@Resources({ @Resource(name = "typeLevelOrbNotInjected", type = ORB.class) })
@EJBs({
    @EJB(name = "typeLevelBeanNotInjected", beanInterface = RemoteCalculator.class, beanName = "StatelessRemoteCalculatorBean") })
public class Client extends EETest {
  // injected in descriptor
  protected static RemoteCalculator statelessBean;

  // injected in descriptor
  static RemoteCalculator statefulBean;

  @EJB(name = "statelessBeanNotInjected", beanName = "StatelessRemoteCalculatorBean")
  static RemoteCalculator statelessBeanNotInjected;

  @EJB(name = "statefulBeanNotInjected", beanName = "StatefulRemoteCalculatorBean")
  static RemoteCalculator statefulBeanNotInjected;

  @Resource(name = "orbNotInjected")
  static ORB orbNotInjected;

  protected Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  private void operation(RemoteCalculator bean) throws Fault {
    int op1 = 2;
    int op2 = 3;

    // default interceptor adds 100 to each param.
    int expected = op1 + op2 + 100 + 100;
    int result = 0;
    try {
      result = bean.remoteAdd(op1, op2);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    if (result == expected) {
      TLogger.log("Got expected result: " + result);
    } else {
      throw new Fault("Expected " + expected + ", actual " + result);
    }
  }

  public void cleanup() throws Fault {
  }

  /*
   * @testName: annotationNotProcessedForStateless
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void annotationNotProcessedForStateless() throws Fault {
    operation(statelessBean);
  }

  /*
   * @testName: annotationNotProcessedForStateful
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void annotationNotProcessedForStateful() throws Fault {
    operation(statefulBean);
  }

  /*
   * @testName: annotationNotProcessedForAppclient
   * 
   * @assertion_ids: EJB:JAVADOC:14; EJB:JAVADOC:15; EJB:JAVADOC:17
   * 
   * @test_Strategy:
   */
  public void annotationNotProcessedForAppclient() throws Fault {
    if (statelessBeanNotInjected != null) {
      throw new Fault("Field statelessBeanNotInjected must not be injected,"
          + " since a full application client descriptor is used. "
          + "This field has been initialized to " + statelessBeanNotInjected);
    }
    if (statefulBeanNotInjected != null) {
      throw new Fault("Field statefulBeanNotInjected must not be injected,"
          + " since a full application client descriptor is used. "
          + "This field has been initialized to " + statefulBeanNotInjected);
    }
    if (orbNotInjected != null) {
      throw new Fault("Field orbNotInjected must not be injected,"
          + " since a full application client descriptor is used. "
          + "This field has been initialized to " + orbNotInjected);
    }
    String lookupName = "java:comp/env/typeLevelBeanNotInjected";
    Object lookupValue = null;
    try {
      lookupValue = ServiceLocator.lookup(lookupName);
      throw new Fault(lookupName + " must not be injected "
          + "at the application client type level, since a full "
          + "application client descriptor is used. "
          + " The value looked up is " + lookupValue);
    } catch (NamingException e) {
      TLogger.log("Got expected javax.naming.NamingException when looking up "
          + lookupName);
    }

    lookupName = "java:comp/env/typeLevelOrbNotInjected";
    try {
      lookupValue = ServiceLocator.lookup(lookupName);
      throw new Fault(lookupName + " must not be injected "
          + "at the application client type level, since a full "
          + "application client descriptor is used. "
          + " The value looked up is " + lookupValue);
    } catch (NamingException e) {
      TLogger.log("Got expected javax.naming.NamingException when looking up "
          + lookupName);
    }

  }
}
