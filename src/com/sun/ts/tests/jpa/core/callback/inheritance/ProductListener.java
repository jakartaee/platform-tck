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

package com.sun.ts.tests.jpa.core.callback.inheritance;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.core.callback.common.CallbackStatusIF;

import javax.persistence.*;

/**
 * None of the callbacks will be called, since subclass entities all have their
 * own callbacks. This listener is intended to be used by Product entity.
 */
public class ProductListener {

  public ProductListener() {
    super();
  }

  @PrePersist
  public void prePersist(CallbackStatusIF b) {
    TestUtil.logTrace("In ProductListener.prePersist");
    b.setPrePersistCalled(true);
    b.addPrePersistCall(this.getClass().getSimpleName());
  }

  @PostPersist
  public void postPersist(Object b) {
    ((CallbackStatusIF) b).setPostPersistCalled(true);
    ((CallbackStatusIF) b).addPostPersistCall(this.getClass().getSimpleName());
  }

  @PreRemove
  public void preRemove(CallbackStatusIF b) {
    TestUtil.logTrace("In ProductListener.preRemove");
    b.setPreRemoveCalled(true);
    b.addPreRemoveCall(this.getClass().getSimpleName());
  }

  @PostRemove
  public void postRemove(Object b) {
    TestUtil.logTrace("In PartProductListener.postRemove.");
    ((CallbackStatusIF) b).setPostRemoveCalled(true);
    ((CallbackStatusIF) b).addPostRemoveCall(this.getClass().getSimpleName());
  }

  @PreUpdate
  public void preUpdate(CallbackStatusIF b) {
    TestUtil.logTrace("In PartProductListener.preUpdate.");
    b.setPreUpdateCalled(true);
    b.addPreUpdateCall(this.getClass().getSimpleName());
  }

  @PostUpdate
  public void postUpdate(Object b) {
    TestUtil.logTrace("In PartProductListener.postUpdate.");
    ((CallbackStatusIF) b).setPostUpdateCalled(true);
    ((CallbackStatusIF) b).addPostUpdateCall(this.getClass().getSimpleName());
  }

  @PostLoad
  public void postLoad(CallbackStatusIF b) {
    TestUtil.logTrace("In PartProductListener.postLoad.");
    b.setPostLoadCalled(true);
    b.addPostLoadCall(this.getClass().getSimpleName());
  }

}
