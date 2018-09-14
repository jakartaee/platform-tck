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

/*
 * @(#)W2JRLR4003Client.java	1.3	03/08/14
 */

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R4003;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxrpc.wsi.constants.CTSConstants;

public class W2JRLR4003Client extends SOAPClient {
  public W2JRLR4003Client(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace(
        "http://w2jrlr4003testservice.org/W2JRLR4003TestService.wsdl");
    stubContext.setService("W2JRLR4003TestService");
    stubContext.setPortPrefix("W2JRLR4003Test");
    stubContext.setServiceInterface(W2JRLR4003Test.class);
  }

  protected String getEndpointURLProperty() {
    return CTSConstants.w2jrlr4003EndpointProperty;
  }

  protected String getWSDLURLProperty() {
    return CTSConstants.w2jrlr4003WSDLProperty;
  }

  public String echoStringUTF8(String str) throws Exception {
    NonNullStringUTF8 nns = new NonNullStringUTF8(str);
    nns = ((W2JRLR4003Test) stubContext.getStub()).echoStringUTF8(nns);
    return nns.getP1();
  }

  public String echoStringUTF16(String str) throws Exception {
    NonNullStringUTF16 nns = new NonNullStringUTF16(str);
    nns = ((W2JRLR4003Test) stubContext.getStub()).echoStringUTF16(nns);
    return nns.getP1();
  }

}
