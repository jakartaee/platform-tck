/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class PlainAccessTimeoutBeanBase implements AccessTimeoutIF {

  public Future<String> ping() {
    Helper.busyWait(PING_WAIT_MILLIS);
    return new AsyncResult<String>("ping result.");
  }

  public Future<String> beanClassLevel() {
    return error();
  }

  public Future<String> beanClassLevel2() {
    return error();
  }

  public Future<String> beanClassMethodLevel() {
    return error();
  }

  public Future<String> beanClassMethodLevelOverride() {
    return error();
  }

  public Future<String> beanSuperClassLevel() {
    return error();
  }

  public Future<String> beanSuperClassMethodLevel() {
    return error();
  }

  public Future<String> beanSuperClassMethodLevelOverride() {
    return error();
  }

  private Future<String> error() {
    throw new RuntimeException("Should not reach here.");
  }

}
