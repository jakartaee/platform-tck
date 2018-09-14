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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclient2ejbjars;

import com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;
import com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.InterceptorUsed;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import org.omg.CORBA.ORB;

//annotations NOT to be ignored
@Stateless(name = "StatelessAnnotationUsedRemoteCalculatorBean")
@Remote({ RemoteCalculator.class })
@ExcludeDefaultInterceptors

// BusinessInterceptorNotUsed is disabled in business method remoteAdd(int,
// int).
// Since interceptor methods in InterceptorNotUsed throws IllegalStateException,
// clients of this bean should only invoke remoteAdd(int, int), or
// remoteSubtract.
@Interceptors({ BusinessInterceptorNotUsed.class, InterceptorUsed.class })
// @Interceptors({BusinessInterceptorNotUsed.class})

public class StatelessAnnotationUsedRemoteCalculatorBean
    extends BaseRemoteCalculator implements RemoteCalculator {
  private int postConstructCallsCount;

  @Resource(name = "sessionContext") // NOT to be ignored
  private SessionContext sessionContext;

  @Resource(name = "orb") // NOT to be ignored
  private ORB orb;

  public StatelessAnnotationUsedRemoteCalculatorBean() {
  }

  @PostConstruct // NOT to be ignored
  public void postConstruct() {
    postConstructCallsCount++;
  }

  @PreDestroy // NOT to be ignored
  public void preDestroy() {
    postConstructCallsCount = 0;
  }

  /**
   * Returns a + b + postConstructCallsCount. Note that a and b may have been
   * modified by interceptor methods. This method also checks that resource
   * injections that are specified via annotations have been injected.
   */
  @ExcludeDefaultInterceptors
  @ExcludeClassInterceptors
  @Interceptors({ InterceptorUsed.class })
  public int remoteAdd(int a, int b) {
    if (sessionContext == null) {
      throw new IllegalStateException("sessionContext field should have been "
          + "injected, but its actual value is null.  The ejb-jar where "
          + "this bean is in has been marked as metadata-complete=false");
    }
    if (orb == null) {
      throw new IllegalStateException("orb field should have been "
          + "injected, but its actual value is null.  The ejb-jar where "
          + "this bean is in has been marked as metadata-complete=false");
    }
    return postConstructCallsCount + a + b;
  }

  @Override
  @ExcludeDefaultInterceptors
  @ExcludeClassInterceptors
  @Interceptors({ InterceptorUsed.class })
  public int remoteSubtract(int a, int b) {
    // do the same thing as remoteAdd(int, int)
    return postConstructCallsCount + a + b;
  }

}
