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

package com.sun.ts.tests.ejb30.common.migration.twothree;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;

@Stateless(name = "ThreeTestBean")
@Remote
public class ThreeTestBean implements ThreeTestIF {

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  @EJB(name = "migrationBeanThreeRemote")
  private ThreeIF migrationBeanThreeRemote;

  @EJB(name = "migrationBeanThreeLocal")
  private ThreeLocalIF migrationBeanThreeLocal;

  protected EJBContext getEJBContext() {
    return sessionContext;
  }

  public ThreeTestBean() {
  }

  public void remove() {
  }

  public void callRemote() throws TestFailedException {
    String expected = "from3Client";
    String reason = null;
    String result = migrationBeanThreeRemote.from3Client();
    if (expected.equals(result)) {
      // good
    } else {
      reason = "Expecting return value " + expected + ", actual " + result;
      throw new TestFailedException(reason);
    }
  }

  public void callLocal() throws TestFailedException {
    String expected = "from3Client";
    String reason = null;
    String result = migrationBeanThreeLocal.from3Client();
    if (expected.equals(result)) {
      // good
    } else {
      reason = "Expecting return value " + expected + ", actual " + result;
      throw new TestFailedException(reason);
    }
  }

}
