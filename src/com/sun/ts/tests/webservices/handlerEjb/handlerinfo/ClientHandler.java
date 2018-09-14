/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handlerEjb.handlerinfo;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import javax.xml.rpc.handler.*;
import javax.xml.namespace.QName;

public class ClientHandler extends HandlerBase {
  private QName[] headers;

  public void init(HandlerInfo hi) {
    super.init(hi);
    headers = hi.getHeaders();

    String buf = "";
    java.util.Map cfg = hi.getHandlerConfig();
    String s1 = (String) cfg.get("ClientParam1");
    String s2 = (String) cfg.get("ClientParam2");
    if (s1 == null || !s1.equals("value1") || s2 == null
        || !s2.equals("value2")) {
      buf += "\n****** Init-params ******";
      buf += "\ninit-param1: value = " + s1;
      buf += "\ninit-param2: value = " + s2;
      throw new RuntimeException("Mismatch init-param;" + buf);
    }
  }

  public boolean handleRequest(MessageContext context) {
    QName[] qns = getHeaders();
    if (qns.length != 2)
      throw new RuntimeException("Wrong header count: " + qns.length);

    String buf = "";
    if (qns[0].getNamespaceURI().equals("http://HandlerInfo.org/Client1")
        && qns[0].getLocalPart().equals("header1")) {
      if (!qns[1].getNamespaceURI().equals("http://HandlerInfo.org/Client2")
          && !qns[1].getLocalPart().equals("header2")) {
        buf += "\n****** Header1 ******";
        buf += "\nnamespaceURL = " + qns[0].getNamespaceURI();
        buf += "\nlocalPart = " + qns[0].getLocalPart();
        buf += "\n****** Header2 ******";
        buf += "\nnamespaceURL = " + qns[1].getNamespaceURI();
        buf += "\nlocalPart = " + qns[1].getLocalPart();
        throw new RuntimeException("Header mismacth;" + buf);
      }
    } else if (qns[0].getNamespaceURI().equals("http://HandlerInfo.org/Client2")
        && qns[0].getLocalPart().equals("header2")) {
      if (!qns[1].getNamespaceURI().equals("http://HandlerInfo.org/Client1")
          && !qns[1].getLocalPart().equals("header1")) {
        buf += "\n****** Header1 ******";
        buf += "\nnamespaceURL = " + qns[0].getNamespaceURI();
        buf += "\nlocalPart = " + qns[0].getLocalPart();
        buf += "\n****** Header2 ******";
        buf += "\nnamespaceURL = " + qns[1].getNamespaceURI();
        buf += "\nlocalPart = " + qns[1].getLocalPart();
        throw new RuntimeException("Header mismacth;" + buf);
      }
    } else {
      buf += "\n****** Header1 ******";
      buf += "\nnamespaceURL = " + qns[0].getNamespaceURI();
      buf += "\nlocalPart = " + qns[0].getLocalPart();
      buf += "\n****** Header2 ******";
      buf += "\nnamespaceURL = " + qns[1].getNamespaceURI();
      buf += "\nlocalPart = " + qns[1].getLocalPart();
      throw new RuntimeException("Header mismacth;" + buf);
    }

    return super.handleRequest(context);
  }

  public QName[] getHeaders() {
    super.getHeaders(); // returns null, but will print trace
    return headers;
  }
}
