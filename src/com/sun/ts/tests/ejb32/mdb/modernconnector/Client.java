/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.mdb.modernconnector;

import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerRemote;

public class Client extends EETest {
  private static final String APP_NAME = "ejb32_mdb_modernconnector";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
  }

  public void cleanup() throws Fault {
  }

  /*
   * @testName: testModernConnector
   * 
   * @test_Strategy: Create a simple Connector called EventMonitorAdapter which
   * uses @LocalBean-like view of the MessageEndpoint proxy. Then create 2
   * modern MDBs based on this Connector and verify the messages from Connector
   * are properly consumed by the MDBs. The modern MDBs are applied with an
   * AroundInvoke interceptor so that we can validate the endpoint class
   * retrieved by Connector is properly proxied.
   */
  public void testModernConnector() throws Fault {
    TLogger.logMsg("Test starting");

    try {
      EventLoggerRemote elRemote = (EventLoggerRemote) new InitialContext()
          .lookup("java:global/" + APP_NAME + "/" + APP_NAME
              + "_ejb/EventLoggerBean!com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerRemote");

      TLogger.logMsg("awaitInvocations() starting");
      if (!elRemote.awaitInvocations()) {
        throw new Fault("awaitInvocations() returned false");
      }

      TLogger.logMsg("awaitInvocations() returned");
      final List<String> eventRecords = elRemote.getEventRecords();
      if (!eventRecords.contains(
          "One online typed high-priority event is processed by high-priority online monitor")
          || !eventRecords.contains(
              "One online typed normal-priority event is processed by normal-priority online monitor")
          || !eventRecords.contains(
              "One online typed low-priority event is processed by low-priority online monitor")
          || !eventRecords.contains(
              "One batch typed high-priority event is processed by high-priority batch monitor")
          || !eventRecords.contains(
              "One batch typed normal-priority event is processed by normal-priority batch monitor")
          || !eventRecords.contains(
              "One batch typed low-priority event is processed by low-priority batch monitor")
          || eventRecords.size() != 6) {
        throw new Fault(
            "Events were not properly consumed by MDBs as expected. Results are: "
                + eventRecords);
      }
    } catch (NamingException ex) {
      throw new Fault("Test failed with an unexpected Exception", ex);
    }

    TLogger.logMsg("Test passed");
  }
}
