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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejblink.casesensT;

import java.util.Properties;
import javax.jms.Topic;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Topic mdbT;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: jms_timeout; user; password;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);
      mdbT = (Topic) context.lookup("java:comp/env/jms/MDBTest");
    } catch (Exception e) {
      TestUtil.logErr("[Client] Setup failed!", e);
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testEjblinkCaseSensitivity
   *
   * @assertion_ids: EJB:SPEC:872
   *
   * @test_Strategy: Have two session beans whose ejb-name's differ only by case
   *                 and whose identity is defined by a String environment entry
   *                 ('myName').
   *
   *                 A message driven bean references these two beans using
   *                 ejb-link elements. Check that the MDB can lookup the two
   *                 beans and check each bean identity based on their value for
   *                 the 'myName' environment entry. Check that this identity
   *                 correspond to the references specified in the DD (validate
   *                 that the EJB references were resolved correctly).
   *
   */
  public void testEjblinkCaseSensitivity() throws Fault {

    String testCase = "testEjblinkCaseSensitivity";
    int testNum = 1;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed!");
        throw new Exception(testCase + " failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed!", e);
      throw new Fault(testCase + " failed!", e);
    }
  }
}
