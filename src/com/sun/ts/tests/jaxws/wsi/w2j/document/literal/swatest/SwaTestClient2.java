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

public class SwaTestClient2 extends SOAPClient {

  public SwaTestClient2(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public SwaTestClient2(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {

    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace("http://SwaTestService.org/wsdl");
    stubContext.setService("WSIDLSwaTestService");
    stubContext.setPort("SwaTestTwoPort");
    stubContext.setEndpointInterface(SwaTest2.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsidlswatest.endpoint.2";
  }

  protected String getWSDLURLProperty() {
    return "wsidlswatest.wsdlloc.1";
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString putMultipleAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestPut request,
      jakarta.activation.DataHandler attach1,
      jakarta.activation.DataHandler attach2) throws Exception {
    return ((SwaTest2) stubContext.getStub()).putMultipleAttachments(request,
        attach1, attach2);
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.OutputResponseString echoNoAttachments(
      com.sun.ts.tests.jaxws.wsi.w2j.document.literal.swatest.InputRequestString request)
      throws Exception {
    return ((SwaTest2) stubContext.getStub()).echoNoAttachments(request);
  }
}
