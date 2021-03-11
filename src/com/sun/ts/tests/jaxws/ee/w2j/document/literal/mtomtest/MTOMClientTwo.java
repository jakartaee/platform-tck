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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.mtomtest;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.SOAPBinding;
import java.awt.Image;

public class MTOMClientTwo extends SOAPClient {

  public MTOMClientTwo(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public MTOMClientTwo(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://mtomtestservice.org/wsdl");
    stubContext.setService("MTOMTestService");
    stubContext.setPort("MTOMTestTwoPort");
    stubContext.setEndpointInterface(MTOMTestTwo.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "w2jdlmtomtest.endpoint.2";
  }

  protected String getWSDLURLProperty() {
    return "w2jdlmtomtest.wsdlloc.2";
  }

  public void mtomInOut2(Holder<String> s, Holder<Image> h) throws Exception {
    MTOMTestTwo port = (MTOMTestTwo) stubContext.getStub();
    SOAPBinding binding = (SOAPBinding) ((BindingProvider) port).getBinding();
    binding.setMTOMEnabled(true);
    port.mtomInOut2(s, h);
  }
}
