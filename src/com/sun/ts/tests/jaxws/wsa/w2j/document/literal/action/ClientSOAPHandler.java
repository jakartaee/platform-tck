/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: ClientSOAPHandler.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.action;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", action=" + action + "]");
    TestUtil.logMsg("Verify output action: [" + action + "]");
    if (oper.equals("addNumbersResponse2")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS2_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbersResponse3")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS3_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbersResponse4")) {
      if (!action.equals(TestConstants.ADD_NUMBERS4_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS4_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbersResponse5")) {
      if (!action.equals(TestConstants.ADD_NUMBERS5_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS5_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbersReeponse6")) {
      if (!action.equals(TestConstants.ADD_NUMBERS6_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS6_OUT_ACTION,
            action);
      }
    }
  }

  @Override
  protected void checkFaultActions(String requestName, String detailName,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkFaultActions: [input=" + requestName
        + ", fault=" + detailName + ", action=" + action + "]");
    TestUtil.logMsg("Verify fault action: [" + action + "]");
    if (requestName.equals("addNumbers")
        && detailName.equals("AddNumbersFault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers")
        && detailName.equals("TooBigNumbersFault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers2")
        && detailName.equals("AddNumbers2Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS2_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers2")
        && detailName.equals("TooBigNumbers2Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS2_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers3")
        && detailName.equals("AddNumbers3Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS3_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers3")
        && detailName.equals("TooBigNumbers3Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS3_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers4")
        && detailName.equals("AddNumbers4Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS4_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS4_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers4")
        && detailName.equals("TooBigNumbers4Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS4_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS4_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers5")
        && detailName.equals("AdNumbers5Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS5_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS5_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbers6")
        && detailName.equals("AddNumbers6Fault")) {
      if (!action.equals(TestConstants.ADD_NUMBERS6_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS6_ADDNUMBERS_ACTION, action);
      }
    }
  }

  private void ThrowActionNotSupportedException(String expected,
      String actual) {
    throw new ActionNotSupportedException(
        "Expected:" + expected + ", Actual:" + actual);
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
