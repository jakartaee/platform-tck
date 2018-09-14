/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.common.allowed;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.util.Properties;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import static com.sun.ts.tests.ejb30.common.allowed.Constants.EJBContextLookupName;

public abstract class InjectiontAllowedBeanBase
    extends SessionContextAllowedBeanBase
    implements SessionContextAllowedIF, SessionContextAllowedLocalIF {
  protected boolean myBoolean;

  @Override
  public void setSessionContext(SessionContext sc) {
    this.sessionContext = sc;
  }

  public void setMyBoolean(boolean b) {
    myBoolean = b;
    this.results = null;
    SessionContext sctxNotNull = null;
    if (sessionContext != null) {
      TLogger.log("SessionContext has been injected, no need to look it up.");
      sctxNotNull = this.sessionContext;
    } else {
      TLogger.log("SessionContext has not been injected, need to look it up.");
      try {
        InitialContext ic = new InitialContext();
        sctxNotNull = (SessionContext) ic.lookup(EJBContextLookupName);
      } catch (NamingException ex) {
        TLogger.log("Failed to look up SessionContext with name "
            + EJBContextLookupName, ex.getMessage());
      }
    }
    this.results = runOperations(sctxNotNull);
  }

  public Properties runOperations(SessionContext sctx) {
    Operations op = Operations.getInstance();
    Properties results = new Properties();
    op.runGetEJBHome(sctx, results);
    op.runGetEJBLocalHome(sctx, results);
    op.runJndiAccess(sctx, results);
    op.runEJBContextLookup(sctx, results);
    return results;
  }

}
