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
 */
/*
 * $Id$
 */

package com.sun.ts.tests.jws.handlerchain.server;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import com.sun.ts.tests.jws.common.Constants;
import com.sun.ts.tests.jws.common.Handler_Util;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public final class LogHandler_Interface
    implements SOAPHandler<SOAPMessageContext> {
  public void init(java.util.Map<String, Object> config) {
  }

  public void destroy() {
  }

  public void close(MessageContext context) {
  }

  public Set<QName> getHeaders() {
    return new TreeSet<QName>();
  }

  public boolean handleMessage(SOAPMessageContext sc) {
    try {
      if (Handler_Util.getDirection(sc).equals(Constants.INBOUND)) {
        System.out.println("*** handler request being called ***");
        SOAPMessage msg = sc.getMessage();
        String name = msg.getSOAPBody().getFirstChild().getFirstChild()
            .getFirstChild().getNodeValue();
        if (name != null && name.equals("Joe")) {
          System.out.println(
              "*** modifying soap message with (change Joe to Scott) ***");
          msg.getSOAPBody().getFirstChild().getFirstChild().getFirstChild()
              .setNodeValue("Scott");
          msg.saveChanges();
        }
      }
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public boolean handleFault(SOAPMessageContext sc) {
    return true;
  }
}
