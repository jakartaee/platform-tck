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

package com.sun.ts.tests.jaxrpc.common;

import javax.xml.rpc.soap.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class ServerHandler4 extends HandlerBase {
  public boolean handleRequest(MessageContext context)
      throws SOAPFaultException {
    try {
      preinvoke();
      boolean throwFault = true;
      if (throwFault)
        throw new SOAPFaultException(new QName("ItsMyFault"),
            "ServerHandler4.handleRequest fault string", "actor string", null);
    } finally {
      postinvoke();
    }
    return true;
  }

  public boolean handleResponse(MessageContext context)
      throws SOAPFaultException {
    try {
      preinvoke();
      boolean throwFault = true;
      if (throwFault)
        throw new SOAPFaultException(new QName("ItsMyFault"),
            "ServerHandler4.handleResponse fault string", "actor string", null);
    } finally {
      postinvoke();
    }
    return true;
  }
}
