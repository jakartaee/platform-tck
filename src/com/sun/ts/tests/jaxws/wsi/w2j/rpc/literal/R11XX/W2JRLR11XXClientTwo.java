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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R11XX;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

public class W2JRLR11XXClientTwo extends SOAPClient {
  public W2JRLR11XXClientTwo(String webServerHost, int port, int mode)
      throws EETest.Fault {
    this(webServerHost, port, mode, null);
  }

  public W2JRLR11XXClientTwo(String webServerHost, int port, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, port, mode);
    stubContext.setNamespace("http://soapactionservice.org/wsdl/");
    stubContext.setService("W2JRLR11XXTestService");
    stubContext.setPort("W2JRLR11XXTestTwoPort");
    stubContext.setEndpointInterface(W2JRLR11XXTest.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsi.w2jrlr11XX.endpoint.2";
  }

  protected String getWSDLURLProperty() {
    return "wsi.w2jrlr11XX.wsdlloc.1";
  }

  public String echoString(String s) throws Exception {
    return ((W2JRLR11XXTest) stubContext.getStub()).echoString(s);
  }
}
