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

package com.sun.ts.tests.webservices.handlerEjb.uniqueness;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import javax.xml.rpc.handler.*;

public class Handler1 extends HandlerBase {
  private HandlerInfo handlerinfo;

  private java.util.Map config;

  private Object instance;

  public void init(HandlerInfo hi) {
    super.init(hi);

    handlerinfo = hi;
    config = hi.getHandlerConfig();
    instance = this;

    Tracker.checkHandlerInfo(handlerinfo);
    Tracker.checkConfig(config);
    Tracker.checkInstance(instance);
  }

  public void destroy() {
    Tracker.removeHandlerInfo(handlerinfo);
    Tracker.removeConfig(config);
    Tracker.removeInstance(instance);

    super.destroy();
  }

  public boolean handleRequest(MessageContext context) {
    boolean result = false;
    try {
      preinvoke();
      String clientparam = (String) config.get("ClientParam1");
      String serverparam = (String) config.get("ServerParam1");

      if (clientparam != null && !clientparam.equals(""))
        context.setProperty("ClientParam1", clientparam);
      if (serverparam != null && !serverparam.equals(""))
        context.setProperty("ServerParam1", serverparam);

      result = super.handleRequest(context);
    } finally {
      postinvoke();
    }
    return result;
  }
}
