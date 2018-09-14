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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R0007;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxrpc.wsi.constants.CTSConstants;

public class W2JRLR0007ClientOne extends SOAPClient {
  public W2JRLR0007ClientOne(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace(
        "http://w2jrlr0007testservice.org/W2JRLR0007TestService.wsdl");
    stubContext.setService("W2JRLR0007TestService");
    stubContext.setPortPrefix("W2JRLR0007TestOne");
    stubContext.setServiceInterface(W2JRLR0007TestOne.class);
  }

  protected String getEndpointURLProperty() {
    return CTSConstants.w2jrlr0007EndpointOneProperty;
  }

  protected String getWSDLURLProperty() {
    return CTSConstants.w2jrlr0007WSDLOneProperty;
  }

  public String echoString(String str) throws Exception {
    NonNullString nns = new NonNullString(str);
    nns = ((W2JRLR0007TestOne) stubContext.getStub()).echoString(nns);
    return nns.getP1();
  }
}
