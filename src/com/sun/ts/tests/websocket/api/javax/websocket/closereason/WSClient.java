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

/*
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.javax.websocket.closereason;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import java.io.PrintWriter;
import java.util.Properties;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.CloseReason.CloseCodes;

public class WSClient extends ServiceEETest {

  final CloseCodes[] codes = { CloseReason.CloseCodes.CANNOT_ACCEPT,
      CloseReason.CloseCodes.CLOSED_ABNORMALLY,
      CloseReason.CloseCodes.GOING_AWAY, CloseReason.CloseCodes.NORMAL_CLOSURE,
      CloseReason.CloseCodes.NOT_CONSISTENT,
      CloseReason.CloseCodes.NO_EXTENSION,
      CloseReason.CloseCodes.NO_STATUS_CODE,
      CloseReason.CloseCodes.PROTOCOL_ERROR, CloseReason.CloseCodes.RESERVED,
      CloseReason.CloseCodes.SERVICE_RESTART,
      CloseReason.CloseCodes.TLS_HANDSHAKE_FAILURE,
      CloseReason.CloseCodes.TOO_BIG, CloseReason.CloseCodes.TRY_AGAIN_LATER,
      CloseReason.CloseCodes.UNEXPECTED_CONDITION,
      CloseReason.CloseCodes.VIOLATED_POLICY };

  final String[] codes_string = { "CANNOT_ACCEPT", "CLOSED_ABNORMALLY",
      "GOING_AWAY", "NORMAL_CLOSURE", "NOT_CONSISTENT", "NO_EXTENSION",
      "NO_STATUS_CODE", "PROTOCOL_ERROR", "RESERVED", "SERVICE_RESTART",
      "TLS_HANDSHAKE_FAILURE", "TOO_BIG", "TRY_AGAIN_LATER",
      "UNEXPECTED_CONDITION", "VIOLATED_POLICY" };

  final int[] codes_number = { 1003, 1006, 1001, 1000, 1007, 1010, 1005, 1002,
      1004, 1012, 1015, 1009, 1013, 1011, 1008 };

  final String[] tck_codes_reason = { "TCK_CANNOT_ACCEPT",
      "TCK_CLOSED_ABNORMALLY", "TCK_GOING_AWAY", "TCK_NORMAL_CLOSURE",
      "TCK_NOT_CONSISTENT", "TCK_NO_EXTENSION", "TCK_NO_STATUS_CODE",
      "TCK_PROTOCOL_ERROR", "TCK_RESERVED", "TCK_SERVICE_RESTART",
      "TCK_TLS_HANDSHAKE_FAILURE", "TCK_TOO_BIG", "TCK_TRY_AGAIN_LATER",
      "TCK_UNEXPECTED_CONDITION", "TCK_VIOLATED_POLICY" };

  public static void main(String[] args) {
    WSClient theTests = new WSClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  /* Run test */
  /*
   * @testName: getCodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:24;
   *
   * @test_Strategy: Test method CloseCodes.getCode()
   */
  public void getCodeTest() throws Fault {
    boolean passed = true;

    int size = codes_number.length;
    for (int i = 0; i < size; i++) {
      if (codes[i].getCode() != codes_number[i]) {
        passed = false;
        System.err.println("Expecting CloseCodes' number " + codes_number[i]
            + "; got " + codes[i].getCode());
      }
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: getCodeTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:22;
   *
   * @test_Strategy: Test method CloseCode.getCode()
   */
  public void getCodeTest1() throws Fault {
    boolean passed = true;

    int size = codes_number.length;
    for (int i = 0; i < size; i++) {
      if (((CloseCode) codes[i]).getCode() != codes_number[i]) {
        passed = false;
        System.err.println("Expecting CloseCode' number " + codes_number[i]
            + "; got " + codes[i].getCode());
      }
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: valueOfTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:25;
   *
   * @test_Strategy: Test method CloseCodes.valueOf(String)
   */
  public void valueOfTest() throws Fault {
    boolean passed = true;

    int size = codes_number.length;
    CloseReason closereason;

    for (int i = 0; i < size; i++) {
      if (!CloseReason.CloseCodes.valueOf(codes_string[i]).equals(codes[i])) {
        passed = false;
        System.err.println("Expecting CloseCodes " + codes[i] + "; got "
            + CloseReason.CloseCodes.valueOf(codes_string[i]));
      }
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: valuesTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:26;
   *
   * @test_Strategy: Test method CloseCodes.values()
   */
  public void valuesTest() throws Fault {
    boolean passed = true;

    CloseCodes[] close_codes = CloseReason.CloseCodes.values();

    int size = close_codes.length;
    boolean[] passed_array = new boolean[size];
    for (int i = 0; i < size; i++) {
      passed_array[i] = false;
      for (int j = 0; j < size; j++) {
        if (close_codes[i].equals(codes[j])) {
          break;
        }
      }
      passed_array[i] = true;
      System.out.println("Expected CloseCodes " + codes[i] + " returned");
      System.out.println("Expected CloseCodes " + codes[i] + " returned");

    }

    for (int i = 0; i < size; i++) {
      if (passed_array[i] == false) {
        passed = false;
      }
    }

    if (!passed) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:18; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20;
   *
   * @test_Strategy: Test constructor CloseReason( CloseCode, String )
   */
  public void constructorTest() throws Fault {
    boolean passed = true;

    int size = codes_number.length;
    CloseReason closereason;

    for (int i = 0; i < size; i++) {
      closereason = new CloseReason(codes[i], tck_codes_reason[i]);
      if (!closereason.getCloseCode().equals(codes[i])) {
        passed = false;
        System.err.println("Expected CloseCodes " + codes[i] + ", returned"
            + closereason.getCloseCode());
      }

      if (!closereason.getReasonPhrase().equals(tck_codes_reason[i])) {
        passed = false;
        System.err.println("Expected reason phrase " + tck_codes_reason[i]
            + ", returned" + closereason.getReasonPhrase());
      }
    }

    if (!passed) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: getCloseCodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:23;
   *
   * @test_Strategy: Test constructor CloseCodes.getCloseCode( int )
   */
  public void getCloseCodeTest() throws Fault {
    boolean passed = true;

    int size = codes_number.length;
    CloseReason closereason;
    CloseCode tmp;

    for (int i = 0; i < size; i++) {
      tmp = CloseReason.CloseCodes.getCloseCode(codes_number[i]);
      if (!tmp.equals(codes[i])) {
        passed = false;
        System.err
            .println("Expected CloseCode " + codes[i] + ", returned" + tmp);
      }
    }

    if (!passed) {
      throw new Fault("Test failed");
    }
  }

  public void cleanup() {
  }
}
