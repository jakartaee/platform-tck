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
package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.addressingfeature;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsa.common.MapRequiredException;
import com.sun.ts.tests.jaxws.wsa.common.MapException;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;
import java.util.Iterator;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.Text;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    // figure out which testname i'm checking for and call appropropriate
    // verify*(...)
    if (Handler_Util.checkForMsg(context,
        "ClientEnabledREQServerEnabledNotREQ")) {
      verifyClientEnabledREQServerEnabledNotREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientNotEnabledServerEnabledNotREQ")) {
      verifyClientNotEnabledServerEnabledNotREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledREQServerEnabledREQ")) {
      verifyClientEnabledREQServerEnabledREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerNotEnabled")) {
      verifyClientEnabledNotREQServerNotEnabled(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerEnabledREQ")) {
      verifyClientEnabledNotREQServerEnabledREQ(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "ClientEnabledNotREQServerEnabledNotREQ")) {
      verifyClientEnabledNotREQServerEnabledNotREQ(context, action);
    }
  }

  private void verifyClientEnabledREQServerEnabledNotREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledREQServerEnabledNotREQ here...
    checkAddressingHeadersExist(context, action);
  }

  private void verifyClientNotEnabledServerEnabledNotREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientNotEnabledServerEnabledNotREQ here...
    checkAddressingHeadersDoNotExist(context, action);
  }

  private void verifyClientEnabledREQServerEnabledREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledREQServerEnabledREQ here...
    checkAddressingHeadersExist(context, action);
  }

  private void verifyClientEnabledNotREQServerNotEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerNotEnabled here...
    checkAddressingHeadersDoNotExist(context, action);
  }

  private void verifyClientEnabledNotREQServerEnabledREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerEnabledREQ here...
    checkAddressingHeadersMayExist(context, action);
  }

  private void verifyClientEnabledNotREQServerEnabledNotREQ(
      SOAPMessageContext context, String action) {
    // verify specific headers for ClientEnabledNotREQServerEnabledNotREQ
    // here...
    checkAddressingHeadersMayExist(context, action);
  }

  private void verifyAction(String action) {
    TestUtil.logMsg("ClientSOAPHandler.verifyAction: [action=" + action + "]");
    if (!TestConstants.ADD_NUMBERS_OUT_ACTION.equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.ADD_NUMBERS_OUT_ACTION + ", Actual:" + action);
    }
  }

  private void checkAddressingHeadersMayExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersMayExist");
    // If Addressing headers exist then check them otherwise don't
    if (action != null) {
      verifyAction(action);
      checkInboundToExist(context);
      checkInboundRelatesToExist(context);
    }
  }

  private void checkAddressingHeadersExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersExist");
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundRelatesToExist(context);
  }

  private void checkAddressingHeadersDoNotExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersDoNotExist");
    checkActionDoesNotExist(action);
    checkInboundToDoesNotExist(context);
    checkInboundRelatesToDoesNotExist(context);
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    String testName = (String) context.get("test.name");
    TestUtil.logMsg("ClientSOAPHandler.getAction(): testName=" + testName);
    if (testName == null) {
      return super.getAction(context);
    }
    /* Headers MAY be present on SOAPResponse */
    else if (testName.equals("ClientEnabledNotREQServerEnabledNotREQ")
        || testName.equals("ClientEnabledNotREQServerEnabledREQ")) {
      try {
        return super.getAction(context);
      } catch (Exception e) {
        return null;
      }
    }
    /* Headers MUST be present on SOAPResponse */
    else if (testName.equals("ClientEnabledREQServerEnabledNotREQ")
        || testName.equals("ClientEnabledREQServerEnabledREQ")) {
      return super.getAction(context);
    }
    /* Headers MUST NOT be present on SOAPResponse */
    else if (testName.equals("ClientNotEnabledServerEnabledNotREQ")
        || testName.equals("ClientEnabledNotREQServerNotEnabled")) {
      return super.getActionDoesNotExist(context);
    }
    /* Fault case just return null */
    else
      return null;
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
