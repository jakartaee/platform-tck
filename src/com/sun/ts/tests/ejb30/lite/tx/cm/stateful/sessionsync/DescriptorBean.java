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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.sessionsync;

import javax.ejb.Stateful;

/**
 * SessionSynchronization methods are specified in ejb-jar.xml. afterCompletion
 * method is not implemented and there should be no callback calls for
 * afterCompletion.
 */
@Stateful
public class DescriptorBean extends SessionSyncBeanBase
    implements SessionSyncIF {

  @SuppressWarnings("unused")
  private void afterBeginAnnotated() {
    super.afterBegin();
  }

  @SuppressWarnings("unused")
  private void afterCompletionAnnotated(boolean arg0) {
    super.afterCompletion(arg0);
  }

  @SuppressWarnings("unused")
  private void beforeCompletionAnnotated() {
    super.beforeCompletion();
  }

}
