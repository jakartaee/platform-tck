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

package com.sun.ts.tests.ejb30.common.helloejbjar;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TimerService;

@Singleton
public class HelloBean extends HelloBeanBase
    implements HelloRemoteIF, HelloLocalIF {
  // timerService not used. Just to verify this resource is available when
  // helloBean is injected to and referenced from various containers.
  @Resource
  private TimerService timerService;

  // implement 2 intf are needed so appserver can infer its remote and local
  // intf.
  public HelloBean() {
  }

}
