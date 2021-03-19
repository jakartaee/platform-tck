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
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.*;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", action=" + action + "]");
    TestUtil.logMsg("Verify output action: [" + action + "]");
    if (oper.equals("addNumbersNoActionResponse")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_OUT_NOACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS_OUT_NOACTION,
            action);
      }
    } else if (oper.equals("addNumbersEmptyActionResponse")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_OUT_EMPTYACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_OUT_EMPTYACTION, action);
      }
    } else if (oper.equals("addNumbersResponse")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbers2Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS2_OUT_ACTION,
            action);
      }
    } else if (oper.equals("addNumbers3Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_OUT_ACTION)) {
        ThrowActionNotSupportedException(TestConstants.ADD_NUMBERS3_OUT_ACTION,
            action);
      }
    }
  }

  @Override
  protected void checkFaultActions(String requestName, String detailName,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkFaultActions: [input=" + requestName
        + ", detailName=" + detailName + ", action=" + action + "]");
    TestUtil.logMsg("Verify fault action: [" + action + "]");
    if (requestName.equals("addNumbers4"))
      return;
    if (requestName.equals("addNumbersFault1")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT1_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT1_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault2")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT2_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT2_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault2")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT2_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT2_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault3")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT3_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT3_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault3")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT3_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT3_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault4")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT4_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT4_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault4")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT4_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT4_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault5")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT5_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT5_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault5")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT5_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT5_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault6")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT6_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT6_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault6")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT6_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT6_TOOBIGNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault7")
        && detailName.equals("AddNumbersException")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_FAULT7_ADDNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT7_ADDNUMBERS_ACTION, action);
      }
    } else if (requestName.equals("addNumbersFault7")
        && detailName.equals("TooBigNumbersException")) {
      if (!action
          .equals(TestConstants.ADD_NUMBERS_FAULT7_TOOBIGNUMBERS_ACTION)) {
        ThrowActionNotSupportedException(
            TestConstants.ADD_NUMBERS_FAULT7_TOOBIGNUMBERS_ACTION, action);
      }
    }
    super.checkFaultActions(requestName, detailName, action);
  }

  @Override
  protected String getOperationName(SOAPBody soapBody) throws SOAPException {
    String opName = super.getOperationName(soapBody);
    if (!opName.startsWith("addNumbersFault"))
      return opName;

    if (opName.equals("addNumbersFault1"))
      return opName;

    if (opName.equals("addNumbersFault2")) {
      soapBody.getFirstChild().getFirstChild().getNodeValue();
    }
    return opName;
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
