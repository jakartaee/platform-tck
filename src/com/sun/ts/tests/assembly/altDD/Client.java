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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.assembly.altDD;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/";

  private static final String entryLookup = prefix + "myCountry";

  private static final String beanLookup = prefix + "ejb/myPainter";

  /* Expected values for bean name */
  private static final String entryNameRef = "France";

  private static final String beanNameRef = "Gaughin";

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;

      logTrace("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();
      logTrace("[Client] Setup completed!");
    } catch (Exception e) {
      logErr("[Client] Failed to obtain Naming Context:" + e);
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testAppClient
   *
   * @assertion_ids: JavaEE:SPEC:10260
   *
   * @test_Strategy: Package an application containing:
   *
   *                 - An application client jar file including its own DD: DD2.
   *                 DD2 declares one String environment entry, named
   *                 'myCountry' whose value is 'Spain'.
   *
   *                 - An alternate DD: DD4. DD4 is almost identical to DD2.
   *                 Nevertheless it changes the value for the 'myCountry'
   *                 environment entry: the new value is 'France'.
   *
   *                 - An application DD including the application client module
   *                 and using an alt-dd element to define DD4 as an alternate
   *                 DD for the application client.
   *
   *                 We check that:
   *
   *                 - We can deploy the application.
   *
   *                 - The application client can lookup the 'ejb/myCountry'
   *                 environment entry.
   *
   *                 - The runtime value is 'France', validating the use of DD4
   *                 at deployment time.
   */
  public void testAppClient() throws Fault {
    PainterBeanHome home = null;
    PainterBean bean = null;
    String entryValue;
    boolean pass = false;

    try {
      logTrace("[Client] Looking up " + entryLookup);
      entryValue = (String) nctx.lookup(entryLookup);

      pass = entryValue.equals(entryNameRef);
      if (!pass) {
        logErr("[Client] Expected " + entryLookup + " name to be "
            + entryNameRef + ", not " + entryValue);

        throw new Fault("Alternative DD test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("Alternative DD test failed!" + e, e);
    }
  }

  /**
   * @testName: testEJB
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: Package an application containing:
   *
   *                 - An ejb-jar file including its own DD: DD1. This ejb-jar
   *                 contains 2 beans sharing the same Home and Remote
   *                 interfaces. According to DD1:
   *
   *                 . The two ejb-name's are Bean1 and Bean2.
   *
   *                 . Bean1 declares a String environment entry named myName
   *                 whose value is 'Dali '
   * 
   *                 . Bean2 declares a String environment entry named myName
   *                 whose value is 'Picasso'
   *
   *                 - An application client jar file including its own DD: DD2.
   *                 DD2 declares one EJB reference using ejb-ref-name
   *                 'ejb/myPainter' and an ejb-link element targeting Bean1.
   *
   *                 - An alternate DD: DD3. DD3 is almost identical to DD1.
   *                 Nevertheless it changes the values for the myName
   *                 environment entries: Bean1 is 'Gaughin' and Bean2 is
   *                 'Matisse'.
   *
   *                 - An application DD including the EJB jar module and the
   *                 application jar module, but also using an alt-dd element to
   *                 define DD3 as an alternate DD for the ejb-jar.
   *
   *                 We check that:
   *
   *                 - We can deploy the application.
   *
   *                 - The application client can lookup 'ejb/myPainter' and
   *                 create a Bean instance.
   *
   *                 - The client can call a business method on that instance
   *                 that return the value of the myName environment entry in
   *                 the bean environment.
   *
   *                 - The returned value is 'Matisse', validating the use of
   *                 DD3 at deployment time.
   */
  public void testEJB() throws Fault {
    PainterBeanHome home = null;
    PainterBean bean = null;
    String nameValue;
    boolean pass = false;

    try {
      logTrace("[Client] Looking up " + beanLookup);
      home = (PainterBeanHome) nctx.lookup(beanLookup, PainterBeanHome.class);
      bean = home.create();
      bean.initLogging(props);

      logTrace("[Client] Checking referenced EJB...");
      nameValue = bean.whoAreYou();

      pass = nameValue.equals(beanNameRef);
      if (!pass) {
        logErr("[Client] Expected " + beanLookup + " name to be " + beanNameRef
            + ", not " + nameValue);

        throw new Fault("Alternative DD test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("Alternative DD test failed!" + e, e);
    } finally {
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing bean...");
          bean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logTrace("[Client] Ignoring exception on " + " bean remove: " + e);
      }
    }
  }

  public void cleanup() {
    logTrace("[Client] Cleanup.");
  }

}
