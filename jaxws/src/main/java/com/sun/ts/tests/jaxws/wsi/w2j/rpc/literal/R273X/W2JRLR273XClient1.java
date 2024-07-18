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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R273X;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

public class W2JRLR273XClient1 extends SOAPClient {
  public W2JRLR273XClient1(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public W2JRLR273XClient1(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace(
        "http://w2jrlr273Xtestservice.org/W2JRLR273XTestService.wsdl");
    stubContext.setService("W2JRLR273XTestService");
    stubContext.setPort("W2JRLR273XTest1Port");
    stubContext.setEndpointInterface(W2JRLR273XTest1.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsi.w2jrlr273X.endpoint.1";
  }

  protected String getWSDLURLProperty() {
    return "wsi.w2jrlr273X.wsdlloc.1";
  }

  public FooBar echoFooBar(FooBar fb) throws Exception {
    FooBar fb1 = ((W2JRLR273XTest1) stubContext.getStub()).echoFooBar(fb);
    return fb1;
  }
}
