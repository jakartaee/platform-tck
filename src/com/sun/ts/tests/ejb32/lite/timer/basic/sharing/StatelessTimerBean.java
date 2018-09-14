/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.lite.timer.basic.sharing;

import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.helper.Helper;

@Stateless()
@Local
@TransactionManagement(TransactionManagementType.BEAN)
public class StatelessTimerBean extends SharingTimerBeanBase
    implements TimerIF, TimedObject {
  @Resource
  private UserTransaction ut;

  public void createTimerRollback(long duration,
      java.io.Serializable timerInfo) {
    try {
      ut.begin();
      // timerService.createTimer(duration, timerInfo);
      timerService.createSingleActionTimer(duration,
          new TimerConfig(timerInfo, false));
      ut.rollback();
    } catch (Exception e) {
      Helper.getLogger().log(Level.FINE, null, e);
    }
  }
}
