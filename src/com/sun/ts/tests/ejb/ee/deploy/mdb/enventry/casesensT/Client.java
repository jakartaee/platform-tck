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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.casesensT;

import java.util.Properties;
import javax.jms.Queue;
import javax.jms.Topic;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  Topic mdbT = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: jms_timeout; user; password;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);

      TestUtil.logTrace("[Client] Looking up MDB...");
      mdbT = (Topic) context.lookup("java:comp/env/jms/MDBTest");
    } catch (Exception e) {
      TestUtil.logErr("[Client] Setup failed! ", e);
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testEntryCaseSensitivity
   *
   * @assertion_ids: EJB:SPEC:872
   *
   * @test_Strategy: Deploy a bean with two String environment entries whose
   *                 name differ only by case and are assigned to two distinct
   *                 values. Check that we can lookup the two environment
   *                 entries. Check that their runtime values are distinct and
   *                 correspond the the ones specified in the DD.
   */
  public void testEntryCaseSensitivity() throws Fault {

    String testCase = "testEntryCaseSensitivity";
    int testNum = 1;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

}
