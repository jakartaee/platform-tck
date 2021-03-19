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

package com.sun.ts.tests.jaxws.sharedclients.simpleclient;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

public class SimpleTestClient extends SOAPClient {

  public SimpleTestClient(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public SimpleTestClient(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://simpletestservice.org/wsdl");
    stubContext.setService("SimpleTest");
    stubContext.setPort("SimpleEndpointPort");
    stubContext.setEndpointInterface(SimpleEndpoint.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsi.simple.endpoint.1";
  }

  protected String getWSDLURLProperty() {
    return "wsi.simple.wsdlloc.1";
  }

  public String helloWorld() throws Exception {
    TestUtil
        .logMsg("STUB CLASS: " + stubContext.getStub().getClass().getName());
    return ((SimpleEndpoint) stubContext.getStub()).helloWorld();
  }

  public String arrayOperationFromClient(String[] array) throws Exception {
    StringArray strArr = new StringArray();
    for (int i = 0; i < array.length; i++)
      strArr.getItem().add(array[i]);
    return ((SimpleEndpoint) stubContext.getStub())
        .arrayOperationFromClient(strArr);
  }
}
