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
 * $Id: ClientSOAPHandler.java 52501 2007-01-24 02:29:49Z adf $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.anonymous;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.Text;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context, "testAnonymousResponsesAssertion")) {
      VerifyAddressingHeadersForAnonymousResponsesAssertion(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "testNonAnonymousResponsesAssertion")) {
      VerifyAddressingHeadersForNonAnonymousResponsesAssertion(context, action);
    }
  }

  private void VerifyAddressingHeadersForAnonymousResponsesAssertion(
      SOAPMessageContext context, String action) {
    TestUtil.logMsg(
        "ClientSOAPHandler.VerifyAddressingHeadersForAnonymousResponsesAssertion");
    if (!TestConstants.TEST_ANONYMOUS_RESPONSES_ASSERTION_OUT_ACTION
        .equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.TEST_ANONYMOUS_RESPONSES_ASSERTION_OUT_ACTION
          + ", Actual:" + action);
    }
    String to = null;
    try {
      to = getTo(context);
      TestUtil.logMsg("[To=" + to + "]");
    } catch (Exception e) {
    }
    if (to != null) {
      if (!to.equals(W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI)
          && !to.equals(W3CAddressingConstants.WSA_NONE_ADDRESS)) {
        throw new AddressingPropertyException("Expected: wsa:To="
            + W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI + " or "
            + W3CAddressingConstants.WSA_NONE_ADDRESS + ", Actual: wsa:To="
            + to);
      }
    }
    try {
      String relatesTo = getRelatesTo(context);
      TestUtil.logMsg("[RelatesTo=" + relatesTo + "]");
    } catch (Exception e) {
      throw new AddressingPropertyException(
          "wsa:RelatesTo was not set (unexpected)");
    }
  }

  private void VerifyAddressingHeadersForNonAnonymousResponsesAssertion(
      SOAPMessageContext context, String action) {
    TestUtil.logMsg(
        "ClientSOAPHandler.VerifyAddressingHeadersForNonAnonymousResponsesAssertion");
    if (!TestConstants.TEST_NONANONYMOUS_RESPONSES_ASSERTION_OUT_ACTION
        .equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.TEST_NONANONYMOUS_RESPONSES_ASSERTION_OUT_ACTION
          + ", Actual:" + action);
    }
    String to = null;
    try {
      to = getTo(context);
      TestUtil.logMsg("[To=" + to + "]");
    } catch (Exception e) {
    }
    if (to != null
        && to.equals(W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI)) {
      throw new AddressingPropertyException("Expected: wsa:To=!"
          + W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS_URI
          + ", Actual: wsa:To=" + to);
    }
    try {
      String relatesTo = getRelatesTo(context);
      TestUtil.logMsg("[RelatesTo=" + relatesTo + "]");
    } catch (Exception e) {
      throw new AddressingPropertyException(
          "wsa:RelatesTo was not set (unexpected)");
    }
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
