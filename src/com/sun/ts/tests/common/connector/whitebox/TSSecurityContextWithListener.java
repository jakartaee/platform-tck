/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.work.WorkContextLifecycleListener;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;

public class TSSecurityContextWithListener extends TSSecurityContext
    implements WorkContextLifecycleListener {

  public TSSecurityContextWithListener(String userName, String password,
      String principalName, boolean translationRequired) {
    super(userName, password, principalName, translationRequired);
    debug("TSSecurityContextWithListener:  constructor");
  }

  @Override
  public void contextSetupComplete() {
    debug("Context setup completed " + this.toString());
    ConnectorStatus.getConnectorStatus().logState("Context setup completed");
  }

  @Override
  public void contextSetupFailed(String string) {
    debug("Context setup failed with the following message : " + string
        + " for security-inflow-context " + "   errorCode=" + this.toString());
  }

  public void debug(String message) {
    Debug.trace(message);
  }

}
