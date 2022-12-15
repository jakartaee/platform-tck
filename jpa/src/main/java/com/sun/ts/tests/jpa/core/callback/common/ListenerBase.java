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

package com.sun.ts.tests.jpa.core.callback.common;

import com.sun.ts.lib.util.TestUtil;

/**
 * An annotation-free class that holds the logics for recording entity lifecycle
 * calls. This is extended to form entity callback listener classes. This class
 * must not be specified as a entity callback listener.
 */
abstract public class ListenerBase {
  protected ListenerBase() {
    super();
  }

  public String getShortName() {
    String name = this.getClass().getName();
    name = name.substring(name.lastIndexOf('.') + 1);
    return name;
  }

  protected void prePersist(CallbackStatusIF b) {
    GenerictListenerImpl.logTrace("In ListenerBase.prePersist in class " + this,
        b);
    b.setPrePersistCalled(true);
    b.addPrePersistCall(getShortName());
    String testName = b.getTestName();
    if (Constants.prePersistRuntimeExceptionTest.equals(testName)) {
      TestUtil.logTrace("Throwing ArithmeticException in ListenerBase");
      throw new ArithmeticException("RuntimeException from PrePersist.");
    }
  }

  protected void postPersist(Object b) {
    CallbackStatusIF p = (CallbackStatusIF) b;
    GenerictListenerImpl.logTrace("In ListenerBase.postPersist." + this, p);
    if (!p.isPrePersistCalled()) {
      TestUtil.logTrace(
          "When calling postPersist, prePersist has not been called.");
      throw new IllegalStateException(
          "When calling postPersist, prePersist has not been called.");
    }
    p.setPostPersistCalled(true);
    p.addPostPersistCall(getShortName());
  }

  protected void preRemove(CallbackStatusIF b) {
    GenerictListenerImpl.logTrace("In ListenerBase.preRemove." + this, b);
    b.setPreRemoveCalled(true);
    b.addPreRemoveCall(getShortName());
  }

  protected void postRemove(Object b) {
    CallbackStatusIF p = (CallbackStatusIF) b;
    GenerictListenerImpl.logTrace("In ListenerBase.postRemove." + this, p);
    if (!p.isPreRemoveCalled()) {
      TestUtil
          .logTrace("When calling postRemove, preRemove has not been called.");
      throw new IllegalStateException(
          "When calling postRemove, preRemove has not been called.");
    }
    p.setPostRemoveCalled(true);
    p.addPostRemoveCall(getShortName());
  }

  protected void preUpdate(CallbackStatusIF b) {
    GenerictListenerImpl.logTrace("In ListenerBase.preUpdate." + this, b);
    b.setPreUpdateCalled(true);
    b.addPreUpdateCall(getShortName());
  }

  protected void postUpdate(Object b) {
    CallbackStatusIF p = (CallbackStatusIF) b;
    GenerictListenerImpl.logTrace("In ListenerBase.postUpdate." + this, p);
    if (!p.isPreUpdateCalled()) {
      TestUtil
          .logErr("When calling postUpdate, preUpdate has not been called.");
      throw new IllegalStateException(
          "When calling postUpdate, preUpdate has not been called.");
    }
    p.setPostUpdateCalled(true);
    p.addPostUpdateCall(getShortName());
  }

  protected void postLoad(CallbackStatusIF b) {
    GenerictListenerImpl.logTrace("In ListenerBase.postLoad." + this, b);
    b.setPostLoadCalled(true);
    b.addPostLoadCall(getShortName());
  }

}
