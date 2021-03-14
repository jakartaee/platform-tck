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

package com.sun.ts.tests.jaxws.sharedclients.faultclient;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

public class FaultTestClient extends SOAPClient {

  public FaultTestClient(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public FaultTestClient(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://faulttestservice.org/wsdl");
    stubContext.setService("FaultTest");
    stubContext.setPort("SoapFaultTestPort");
    stubContext.setEndpointInterface(SoapFaultTest.class);
    stubContext.setWebServiceRef(webServiceRef);

  }

  public String getEndpointURLProperty() {
    return "wsi.fault.endpoint.1";
  }

  public String getWSDLURLProperty() {
    return "wsi.fault.wsdlloc.1";
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoChildren()
      throws Exception {
    return ((SoapFaultTest) stubContext.getStub())
        .alwaysThrowsSOAPFaultExceptionDetailNoChildren();
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedChildren()
      throws Exception {
    return ((SoapFaultTest) stubContext.getStub())
        .alwaysThrowsSOAPFaultExceptionDetailQualifiedChildren();
  }

  public String alwaysThrowsSOAPFaultExceptionDetailUnqualifiedChildren()
      throws Exception {
    return ((SoapFaultTest) stubContext.getStub())
        .alwaysThrowsSOAPFaultExceptionDetailUnqualifiedChildren();
  }

  public String alwaysThrowsSOAPFaultExceptionDetailNoAttributes()
      throws Exception {
    return ((SoapFaultTest) stubContext.getStub())
        .alwaysThrowsSOAPFaultExceptionDetailNoAttributes();
  }

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes()
      throws Exception {
    return ((SoapFaultTest) stubContext.getStub())
        .alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes();
  }
}
