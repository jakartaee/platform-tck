/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.sharedclients.rpclitclient;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

public class J2WRLSharedClient extends SOAPClient {
  public J2WRLSharedClient(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public J2WRLSharedClient(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.sun.ts.tests.jaxws.wsi.clients.Client#getEndpointURLProperty()
   */
  protected String getEndpointURLProperty() {
    return "wsi.j2wrlshared.endpoint.1";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.sun.ts.tests.jaxws.wsi.clients.Client#getWSDLURLProperty()
   */
  protected String getWSDLURLProperty() {
    return "wsi.j2wrlshared.wsdlloc.1";
  }
}
