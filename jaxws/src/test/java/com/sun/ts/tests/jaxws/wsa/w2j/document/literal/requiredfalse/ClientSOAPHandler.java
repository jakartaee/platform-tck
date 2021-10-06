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

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.requiredfalse;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import com.sun.ts.lib.util.*;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", action=" + action + "]");
    if (oper.equals("addNumbersResponse")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers2Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers3Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS3_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers4Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS4_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers5Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS5_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers6Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS6_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers7Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS7_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    } else if (oper.equals("addNumbers8Response")) {
      if (!action.equals(TestConstants.ADD_NUMBERS8_OUT_ACTION)) {
        throw new ActionNotSupportedException(action);
      }
    }
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
