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

package com.sun.ts.tests.ejb30.bb.session.stateful.busiface;

import com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface1;
import com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface2;
import com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface1;
import com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface2;
import com.sun.ts.tests.ejb30.common.busiface.BusinessBeanBase;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.Stateful;

@Stateful(name = "AnnotatedInterfaceBean")
// @Remote({AnnotatedBusinessInterface1.class,
// AnnotatedBusinessInterface2.class})
// @Local({AnnotatedLocalBusinessInterface1.class,
// AnnotatedLocalBusinessInterface2.class})
public class AnnotatedInterfaceBean extends BusinessBeanBase
    implements AnnotatedLocalBusinessInterface1,
    AnnotatedLocalBusinessInterface2, AnnotatedBusinessInterface1,
    AnnotatedBusinessInterface2, java.io.Serializable {
  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  public AnnotatedInterfaceBean() {
  }

  @Remove
  public void remove() {
  }

  public void remove2() {
  }
}
