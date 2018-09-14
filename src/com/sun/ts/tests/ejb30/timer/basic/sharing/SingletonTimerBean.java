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
package com.sun.ts.tests.ejb30.timer.basic.sharing;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.Singleton;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class SingletonTimerBean extends SharingTimerBeanBase
    implements TimerIF, TimedObject {

  // override business methods to apply tx attr NEVER
  @Override
  public String accessTimers() throws TestFailedException {
    return super.accessTimers();
  }

  @Override
  public Timer createTimer(long duration, Serializable timerInfo) {
    return super.createTimer(duration, timerInfo);
  }

  @Override
  public Timer createTimer(Date expiration, long duration,
      Serializable timerInfo) {
    return super.createTimer(expiration, duration, timerInfo);
  }

  @Override
  public void cancelAllTimers() {
    super.cancelAllTimers();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void createTimerRollback(long duration,
      java.io.Serializable timerInfo) {
    timerService.createTimer(duration, timerInfo);
    ejbContext.setRollbackOnly();
  }
}
