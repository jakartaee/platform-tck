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

package com.sun.ts.tests.ejb30.misc.sameejbclass;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

//@Stateless
public class SameEJBClassBean implements SameEJBClassIF {
  @Resource(name = "myChar")
  private Character myChar;

  private boolean myCharInjected;

  private String reason;

  @PostConstruct
  private void checkEnvEntry0() {
    Helper.getLogger().fine("In PostConstruct method of bean instance " + this);
    if (myChar != null) {
      if (myChar == '1' || myChar == '2' || myChar == '3' || myChar == '4') {
        myCharInjected = true;
        reason = "env-entry myChar has been correctly injected: " + myChar;
      } else {
        reason = "env-entry myChar has been incorrectly injected.  Expecting "
            + "1,2,3 or 4, but actual " + myChar;
      }
    } else {
      reason = "env-entry myChar has not been injected.";
    }
  }

  public Dog testDTO(int i, String s, Dog dog) {
    dog.setName(s);
    return dog;
  }

  public Dog testDTO(int i, String s, Object obj) {
    Dog dog = (Dog) obj;
    dog.setName(s);
    return dog;
  }

  public String checkEnvEntry(String beanName) throws TestFailedException {
    Helper.getLogger()
        .fine("Injected myChar=" + myChar + ", bean instance=" + this);
    if (myCharInjected) {
      return reason;
    }
    throw new TestFailedException("Inside " + beanName + ", " + reason);
  }
}
