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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

import com.sun.ts.tests.jaxws.common.*;

public class SwaTestClient1 extends SOAPClient {

  public SwaTestClient1(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public SwaTestClient1(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {

    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://SwaTestService.org/wsdl");
    stubContext.setService("WSIDLSwaTestService");
    stubContext.setPort("SwaTestOnePort");
    stubContext.setEndpointInterface(SwaTest1.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsidlswatest.endpoint.1";
  }

  protected String getWSDLURLProperty() {
    return "wsidlswatest.wsdlloc.1";
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponse echoMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequest request,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach1,
      jakarta.xml.ws.Holder<jakarta.activation.DataHandler> attach2)
      throws Exception {
    return ((SwaTest1) stubContext.getStub()).echoMultipleAttachments(request,
        attach1, attach2);
  }
}
