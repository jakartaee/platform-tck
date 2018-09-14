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

package com.sun.ts.tests.webservices.handler.uniqueness;

import java.rmi.RemoteException;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.server.*;

public class ByeImpl implements Bye, ServiceLifecycle {
  private ServletEndpointContext ctx;

  private MessageContext context;

  public void init(Object c) {
    ctx = (ServletEndpointContext) c;
  }

  public void destroy() {
  }

  public int bye() throws RemoteException {
    if (ctx == null)
      return -1111;
    context = ctx.getMessageContext();
    if (context == null)
      return -1111;

    String s1 = (String) context.getProperty("ClientParam1");
    String s2 = (String) context.getProperty("ServerParam1");

    if (s1 == null)
      s1 = "0";
    else if (s1.equals("HelloPC"))
      s1 = "1";
    else if (s1.equals("ByePC"))
      s1 = "2";
    else
      s1 = "3";

    if (s2 == null)
      s2 = "0";
    else if (s2.equals("HelloPC"))
      s2 = "1";
    else if (s2.equals("ByePC"))
      s2 = "2";
    else
      s2 = "3";

    return Integer.parseInt(s1 + s2);
  }
}
