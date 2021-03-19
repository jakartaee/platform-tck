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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.R2712;

import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.lib.harness.EETest;

public class W2JDLR2712ClientTwo extends SOAPClient {

  public W2JDLR2712ClientTwo(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public W2JDLR2712ClientTwo(String webServerHost, int port, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {

    super(webServerHost, port, mode);
    stubContext.setNamespace("http://w2jdlr2712testservice.org/wsdl");
    stubContext.setService("W2JDLR2712TestService");
    stubContext.setPort("W2JDLR2712TestTwoPort");
    stubContext.setEndpointInterface(W2JDLR2712TestTwo.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsi.w2jdlr2712.endpoint.2";
  }

  protected String getWSDLURLProperty() {
    return "wsi.w2jdlr2712.wsdlloc.1";
  }

  public HelloResponse hello(HelloRequest item) throws Exception {
    return ((W2JDLR2712TestTwo) stubContext.getStub()).hello(item);
  }

}
