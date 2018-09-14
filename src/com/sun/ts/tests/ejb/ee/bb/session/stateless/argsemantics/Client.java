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

package com.sun.ts.tests.ejb.ee.bb.session.stateless.argsemantics;

import java.util.Properties;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "EntityBeanTest";

  private static final String beanLookup = "java:comp/env/ejb/Caller";

  private CallerBean bean = null;

  private CallerBeanHome beanHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     generateSQL;
   */
  public void setup(String[] args, Properties props) throws Fault {
    try {
      logMsg("[Client] setup()");
      this.props = props;

      logTrace("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("[Client] Looking up " + beanLookup);
      beanHome = (CallerBeanHome) nctx.lookup(beanLookup, CallerBeanHome.class);
      logTrace("[Client] Create EJB instance...");
      bean = (CallerBean) beanHome.create();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testStatefulRemote
   *
   * @assertion_ids: EJB:SPEC:906
   *
   * @test_Strategy:
   * 
   *                 This is applicable to : - a Session Stateful Callee bean
   *                 defining a remote client view only (No local view). - a
   *                 Stateless Caller bean, Calling this Callee Bean home or
   *                 remote interface.
   * 
   *                 We package in the same ejb-jar: - a Session Stateful Callee
   *                 bean defining a remote client view only (No local view). -
   *                 a Stateless Caller bean
   *
   *                 Remote Home arg semantics verification:
   *
   *                 - We set a non-remote object 'arg' (of type SimpleArgument)
   *                 to an initial value. This SimpleArgument class is just a
   *                 data structure holding an int.
   *
   *                 - The Caller bean calls the Callee Home create(...) method,
   *                 passing this 'arg' object as an argument.
   * 
   *                 - The Callee create(..) method modifies the value of the
   *                 argument (should be a copy of 'arg')
   *
   *                 - When we return from the create method, the Caller check
   *                 that the argument value is still set to the initial value.
   *
   *                 Remote interface arg semantics verif/loication: Same
   *                 strategy but the Caller call a business method on the
   *                 Callee remote interface.
   */
  public void testStatefulRemote() throws Fault {
    boolean pass;

    try {
      pass = bean.testStatefulRemote(props);
    } catch (Exception e) {
      throw new Fault("testStatefulRemote failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.cleanUpBean();
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testStatefulRemote failed");
    }

  }

  /**
   * @testName: testStatefulLocal
   *
   * @assertion_ids: EJB:SPEC:907.2; EJB:SPEC:1
   *
   * @test_Strategy: This is applicable to : - a Session Stateful Callee bean
   *                 defining a local client view only (No remote view).
   *
   *                 - a Stateless Caller bean, Calling this Callee Bean local
   *                 home or local interface.
   * 
   *                 We package in the same ejb-jar: - a Session Stateful Callee
   *                 bean defining a local client view only (No remote view). -
   *                 a Stateless Caller bean
   *
   *                 Local Home arg semantics verification:
   *
   *                 - We set a non-remote object 'arg' (of type SimpleArgument)
   *                 to an initial value. This SimpleArgument class is just a
   *                 data structure holding an int.
   *
   *                 - The Caller bean calls the Callee local home create(...)
   *                 method, passing this 'arg' object as an argument.
   * 
   *                 - The Callee create(..) method modifies the value of the
   *                 argument (should be a reference to original 'arg')
   *
   *                 - When we return from the create method, the Caller check
   *                 that the argument value is not set to the initial value,
   *                 and reflect the changes made by the Callee.
   *
   *                 Local interface arg semantics verification:
   *
   *                 Same strategy but the Caller call a business method on the
   *                 Callee local interface.
   */
  public void testStatefulLocal() throws Fault {
    boolean pass;

    try {
      pass = bean.testStatefulLocal(props);
    } catch (Exception e) {
      throw new Fault("testStatefulLocal failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.cleanUpBean();
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testStatefulLocal failed");
    }

  }

  /**
   * @testName: testStatefulBoth
   *
   * @assertion_ids: EJB:SPEC:906; EJB:SPEC:907; EJB:SPEC:907.2
   *
   *
   * @test_Strategy: This is applicable to :
   *
   *                 - a Session Stateful Callee bean defining a remote AND a
   *                 local client view.
   *
   *                 - a Stateless Caller bean, Calling this Callee Bean home,
   *                 local home, remote, or local interface.
   * 
   *                 The test strategy is a cumulated version of the two
   *                 previous tests ('testStatefulRemote' and
   *                 'testStatefulLocal') on the Callee bean defining a local
   *                 and a remote client view.
   */
  public void testStatefulBoth() throws Fault {
    boolean pass;

    try {
      pass = bean.testStatefulBoth(props);
    } catch (Exception e) {
      throw new Fault("testStatefulBoth failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.cleanUpBean();
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testStatefulBoth failed");
    }

  }

  /**
   * @testName: testCMP20Remote
   *
   * @assertion_ids: EJB:SPEC:906
   * 
   * @test_Strategy:
   * 
   *                 This is applicable to : - a CMP 2.0 Callee bean defining a
   *                 remote client view only (No local view). - a Stateless
   *                 Caller bean, Calling this CMP 2.0 Bean home or remote
   *                 interface.
   * 
   *                 We package in the same ejb-jar: - a CMP 2.0 Callee bean
   *                 defining a remote client view only (No local view). - a
   *                 Stateless Caller bean
   *
   *                 Remote Home arg semantics verification:
   *
   *                 - We set a non-remote object 'arg' (of type SimpleArgument)
   *                 to an initial value. This SimpleArgument class is just a
   *                 data structure holding an int.
   *
   *                 - The Caller bean calls the Callee Home create(...) method,
   *                 passing this 'arg' object as an argument.
   * 
   *                 - The Callee create(..) method modifies the value of the
   *                 argument (should be a copy of 'arg')
   *
   *                 - When we return from the create method, the Caller check
   *                 that the argument value is still set to the initial value.
   *
   *                 Remote interface arg semantics verification: Same strategy
   *                 but the Caller call a business method on the Callee remote
   *                 interface.
   */
  public void testCMP20Remote() throws Fault {
    boolean pass;

    try {
      pass = bean.testCMP20Remote(props);
    } catch (Exception e) {
      throw new Fault("testCMP20Remote failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testCMP20Remote failed");
    }

  }

  /**
   * @testName: testCMP20Local
   *
   * @assertion_ids: EJB:SPEC:1; EJB:SPEC:907.2
   *
   * @test_Strategy:
   *
   *                 This is applicable to : - a CMP 2.0 Callee bean defining a
   *                 local client view only (No remote view).
   *
   *                 - a Stateless Caller bean, Calling this CMP 2.0 Bean local
   *                 home or local interface.
   * 
   *                 We package in the same ejb-jar:
   *
   *                 - a CMP 2.0 Callee bean defining a local client view only
   *                 (No remote view).
   *
   *                 - a Stateless Caller bean
   *
   *                 Local Home arg semantics verification:
   *
   *                 - We set a non-remote object 'arg' (of type SimpleArgument)
   *                 to an initial value. This SimpleArgument class is just a
   *                 data structure holding an int.
   *
   *                 - The Caller bean calls the Callee local home create(...)
   *                 method, passing this 'arg' object as an argument.
   * 
   *                 - The Callee create(..) method modifies the value of the
   *                 argument (should be a reference to original 'arg')
   *
   *                 - When we return from the create method, the Caller check
   *                 that the argument value is not set to the initial value,
   *                 and reflect the changes made by the Callee.
   *
   *                 Local interface arg semantics verification:
   *
   *                 Same strategy but the Caller call a business method on the
   *                 Callee local interface.
   */
  public void testCMP20Local() throws Fault {
    boolean pass;

    try {
      pass = bean.testCMP20Local(props);
    } catch (Exception e) {
      throw new Fault("testCMP20Local failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testCMP20Local failed");
    }

  }

  /**
   * @testName: testCMP20Both
   *
   * @assertion_ids: EJB:SPEC:907; EJB:SPEC:907.1
   *
   * @test_Strategy:
   *
   *                 This is applicable to :
   *
   *                 - a CMP 2.0 Callee bean defining a remote AND a local
   *                 client view.
   *
   *                 - a Stateless Caller bean, Calling the CMP 2.0 Bean home,
   *                 local home, remote, or local interface.
   * 
   *                 The test strategy is a cumulated version of the two
   *                 previous tests ('testCMP20Remote' and 'testCMP20Local') on
   *                 the Callee bean defining a local and a remote client view.
   */
  public void testCMP20Both() throws Fault {
    boolean pass;

    try {
      pass = bean.testCMP20Both(props);
    } catch (Exception e) {
      throw new Fault("testCMP20Both failed", e);
    } finally {
      if (null != bean) {
        try {
          bean.remove();
        } catch (Exception e) {
          TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
        }
      }
    }

    if (!pass) {
      throw new Fault("testCMP20Both failed");
    }

  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
