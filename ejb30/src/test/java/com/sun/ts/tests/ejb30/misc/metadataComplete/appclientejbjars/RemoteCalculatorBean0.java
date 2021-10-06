/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars;


import com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJBContext;
import jakarta.ejb.EJBException;

abstract public class RemoteCalculatorBean0 extends BaseRemoteCalculator
    implements RemoteCalculator {

  public RemoteCalculatorBean0() {
  }

  abstract protected EJBContext getEJBContext();

  @PostConstruct // to be ignored
  public void postConstruct0() {
    throw new IllegalStateException("Should not get here. Annotations in the "
        + "bean class should not be processed, since the descriptor is "
        + "metadata-complete.");
  }

  @PreDestroy // to be ignored
  public void preDestroy0() {
    throw new IllegalStateException("Should not get here. Annotations in the "
        + "bean class should not be processed, since the descriptor is "
        + "metadata-complete.");
  }

  @Override
  public int remoteAdd(int a, int b) {
    if (getEJBContext() != null) {
      throw new EJBException("SessionContext is not null: " + getEJBContext()
          + ".  It should not be injected since this ejb-jar is marked"
          + " as metadata-complete.");
    }
    int retValue;
    retValue = super.remoteAdd(a, b);
    return retValue;
  }

}
