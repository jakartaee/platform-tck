/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync;

import jakarta.ejb.AfterBegin;
import jakarta.ejb.BeforeCompletion;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

/**
 * SessionSynchronization methods are annotated. afterCompletion method is not
 * implemented and there should be no callback calls for afterCompletion.
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AnnotatedBean extends SessionSyncBeanBase
    implements SessionSyncIF {

  @SuppressWarnings("unused")
  @AfterBegin()
  private void afterBeginAnnotated() {
    super.afterBegin();
  }

  @SuppressWarnings("unused")
  @BeforeCompletion
  private void beforeCompletionAnnotated() {
    super.beforeCompletion();
  }

}
