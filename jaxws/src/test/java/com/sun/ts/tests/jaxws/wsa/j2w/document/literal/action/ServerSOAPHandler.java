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
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.action;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.j2w.document.literal.action.TestConstants;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class ServerSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkInboundAction: [operation=" + oper
        + ", action=" + action + "]");
    TestUtil.logMsg("Verify input action: [" + action + "]");
    System.out.println("ServerSOAPHandler.checkInboundAction: [operation="
        + oper + ", action=" + action + "]");
    System.out.println("Verify input action: [" + action + "]");
    if (action.equals("http://wrong.com/wrongaction"))
      return;
    if (oper.equals("addNumbersNoAction")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_IN_NOACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS_IN_NOACTION,
            action);
      }
    } else if (oper.equals("addNumbersEmptyAction")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_IN_EMPTYACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_IN_EMPTYACTION, action);
      }
    } else if (oper.equals("addNumbers")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_IN_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS_IN_ACTION,
            action);
      }
    } else if (oper.equals("addNumbers2")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_IN_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS2_IN_ACTION,
            action);
      }
    } else if (oper.equals("addNumbers3")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_IN_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS3_IN_ACTION,
            action);
      }
    } else if (oper.equals("addNumbersFault1")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT1_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT1_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault2")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT2_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT2_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault3")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT3_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT3_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault4")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT4_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT4_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault5")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT5_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT5_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault6")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT6_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT6_IN_ACTION, action);
      }
    } else if (oper.equals("addNumbersFault7")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT7_IN_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT7_IN_ACTION, action);
      }
    }
  }

  private void ThrowActionNotSupportedException(String expected,
      String actual) {
    throw new ActionNotSupportedException(
        "Expected:" + expected + ", Actual:" + actual);
  }

  protected String whichHandler() {
    return "ServerSOAPHandler";
  }
}
