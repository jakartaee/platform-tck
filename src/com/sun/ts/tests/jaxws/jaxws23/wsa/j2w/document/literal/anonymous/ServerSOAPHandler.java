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

package com.sun.ts.tests.jaxws.jaxws23.wsa.j2w.document.literal.anonymous;

import com.sun.ts.lib.util.*;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class ServerSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    System.out.println("ServerSOAPHandler.checkInboundAction: [operation="
        + oper + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context, "testAnonymousResponsesAssertion")) {
      verifyAddressingHeadersForAnonymousResponsesAssertion(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "testNonAnonymousResponsesAssertion")) {
      verifyAddressingHeadersForNonAnonymousResponsesAssertion(context, action);
    }
  }

  private void verifyAddressingHeadersForAnonymousResponsesAssertion(
      SOAPMessageContext context, String action) {
    TestUtil.logMsg(
        "ServerSOAPHandler.VerifyAddressingHeadersForAnonymousResponsesAssertion");
    System.out.println(
        "ServerSOAPHandler.VerifyAddressingHeadersForAnonymousResponsesAssertion");
    if (!TestConstants.TEST_ANONYMOUS_RESPONSES_ASSERTION_IN_ACTION
        .equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.TEST_ANONYMOUS_RESPONSES_ASSERTION_IN_ACTION
          + ", Actual:" + action);
    }
    try {
      String to = getTo(context);
      TestUtil.logMsg("[To=" + to + "]");
      System.out.println("[To=" + to + "]");
    } catch (Exception e) {
    }
    String replyTo = null;
    try {
      replyTo = getReplyTo(context);
      TestUtil.logMsg("[ReplyTo=" + replyTo + "]");
      System.out.println("[ReplyTo=" + replyTo + "]");
    } catch (Exception e) {
    }
    if (replyTo != null) {
      if (!replyTo.equals(W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI)
          && !replyTo.equals(W3CAddressingConstants.WSA_NONE_ADDRESS)) {
        throw new AddressingPropertyException("Expected: wsa:ReplyTo="
            + W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI + " or "
            + W3CAddressingConstants.WSA_NONE_ADDRESS + ", Actual: wsa:ReplyTo="
            + replyTo);
      }
    }
    try {
      String messageID = getMessageId(context);
      TestUtil.logMsg("[MessageID=" + messageID + "]");
      System.out.println("[MessageID=" + messageID + "]");
    } catch (Exception e) {
      throw new AddressingPropertyException(
          "wsa:MessageID was not set (unexpected)");
    }
  }

  private void verifyAddressingHeadersForNonAnonymousResponsesAssertion(
      SOAPMessageContext context, String action) {
    TestUtil.logMsg(
        "ServerSOAPHandler.VerifyAddressingHeadersForNonAnonymousResponsesAssertion");
    System.out.println(
        "ServerSOAPHandler.VerifyAddressingHeadersForNonAnonymousResponsesAssertion");
    if (!TestConstants.TEST_NONANONYMOUS_RESPONSES_ASSERTION_IN_ACTION
        .equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.TEST_NONANONYMOUS_RESPONSES_ASSERTION_IN_ACTION
          + ", Actual:" + action);
    }
    try {
      String to = getTo(context);
      TestUtil.logMsg("[To=" + to + "]");
      System.out.println("[To=" + to + "]");
    } catch (Exception e) {
    }
    String replyTo;
    try {
      replyTo = getReplyTo(context);
      TestUtil.logMsg("[ReplyTo=" + replyTo + "]");
      System.out.println("[ReplyTo=" + replyTo + "]");
    } catch (Exception e) {
      throw new AddressingPropertyException(
          "wsa:ReplyTo was not set (unexpected)");
    }
    if (replyTo.equals(W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI))
      throw new AddressingPropertyException("Expected: wsa:ReplyTo=!"
          + W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI
          + ", Actual: wsa:ReplyTo=" + replyTo);
    try {
      String messageID = getMessageId(context);
      TestUtil.logMsg("[MessageID=" + messageID + "]");
      System.out.println("[MessageID=" + messageID + "]");
    } catch (Exception e) {
      throw new AddressingPropertyException(
          "wsa:MessageID was not set (unexpected)");
    }
  }

  protected String whichHandler() {
    return "ServerSOAPHandler";
  }
}
