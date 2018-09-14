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

package com.sun.ts.tests.webservices12.servlet.WSMTOMSBFullDDsTest;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

import javax.xml.ws.*;
import javax.xml.ws.soap.SOAPBinding;

public class MTOMClientTwo extends SOAPClient {

  public MTOMClientTwo(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public MTOMClientTwo(String webServerHost, int webServerPort, int mode,
      javax.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://mtomtestservice.org/wsdl");
    stubContext.setService("MTOMTestService");
    stubContext.setPort("MTOMTestTwoPort");
    stubContext.setEndpointInterface(MTOMTestTwo.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsservletmtomsoapbindingwithfullddstest.endpoint.2";
  }

  protected String getWSDLURLProperty() {
    return "wsservletmtomsoapbindingwithfullddstest.wsdlloc.2";
  }

  public String mtomIn2(DataType3 d) throws Exception {
    MTOMTestTwo port = (MTOMTestTwo) stubContext.getStub();
    return port.mtomIn2(d);
  }

  public MTOMTestTwo returnPort() throws Exception {
    MTOMTestTwo port = (MTOMTestTwo) stubContext.getStub();
    return port;
  }
}
