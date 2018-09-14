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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars;

import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.EJBContext;

//annotations to be ignored
@Stateless(name = "StatelessRemoteCalculatorBean")
@Remote({ RemoteCalculator.class })
@Interceptors({ InterceptorNotUsed.class })
@ExcludeDefaultInterceptors

public class StatelessRemoteCalculatorBean extends RemoteCalculatorBean0
    implements RemoteCalculator {

  @Resource(name = "sessionContext") // to be ignored
  private SessionContext sessionContext;

  public StatelessRemoteCalculatorBean() {
  }

  protected EJBContext getEJBContext() {
    return sessionContext;
  }

  @PostConstruct // to be ignored
  public void postConstruct() {
    throw new IllegalStateException("Should not get here. Annotations in the "
        + "bean class should not be processed, since the descriptor is "
        + "metadata-complete.");
  }

  @PreDestroy // to be ignored
  public void preDestroy() {
    throw new IllegalStateException("Should not get here. Annotations in the "
        + "bean class should not be processed, since the descriptor is "
        + "metadata-complete.");
  }

}
