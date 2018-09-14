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

package com.sun.ts.tests.jaxrs.platform.container.completioncallback;

import javax.ws.rs.container.CompletionCallback;

public class SecondSettingCompletionCallback implements CompletionCallback {

  private static String throwableName;

  public static final String NULL = "NULL";

  public static final String OUTOFORDER = "SecondSettingCompletionCallback is not second";

  public static final String NONAME = "No name has been set yet";

  @Override
  public void onComplete(Throwable throwable) {
    throwableName = throwable == null ? NULL : throwable.getClass().getName();
    // We can check the order of execution of registered completionCallbacks
    // in the order they were registered.
    if (!SettingCompletionCallback.getLastThrowableName().equals(throwableName))
      throwableName = throwableName + OUTOFORDER;
  }

  public static final String getLastThrowableName() {
    return throwableName;
  }

  public static final void resetLastThrowableName() {
    throwableName = NONAME;
  }

}
