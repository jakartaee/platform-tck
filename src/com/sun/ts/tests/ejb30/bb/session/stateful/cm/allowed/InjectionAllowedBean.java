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

package com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed;

import com.sun.ts.tests.ejb30.common.allowed.InjectiontAllowedBeanBase;
import com.sun.ts.tests.ejb30.common.allowed.Operations;
import com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedLocalIF;
import java.util.Properties;
import javax.annotation.Resource;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;

@Stateful(name = "InjectionAllowedBean")
@Remote({ SessionContextAllowedIF.class })
@Local({ SessionContextAllowedLocalIF.class })
// @Interceptors({com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor.class})
public class InjectionAllowedBean extends InjectiontAllowedBeanBase
    implements SessionContextAllowedIF, SessionContextAllowedLocalIF {

  @Override
  @Resource(name = "ejbContext")
  public void setSessionContext(SessionContext sc) {
    super.setSessionContext(sc);
  }

  @Resource(name = "myBoolean")
  @Override
  public void setMyBoolean(boolean b) {
    super.setMyBoolean(b);
  }
}
