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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;

import java.math.BigInteger;

public class W2JRLR2010Client extends SOAPClient {
  public W2JRLR2010Client(String webServerHost, int webServerPort, int mode)
      throws EETest.Fault {
    this(webServerHost, webServerPort, mode, null);
  }

  public W2JRLR2010Client(String webServerHost, int webServerPort, int mode,
      jakarta.xml.ws.Service webServiceRef) throws EETest.Fault {
    super(webServerHost, webServerPort, mode);
    stubContext.setNamespace(
        "http://w2jrlr2010testservice.org/W2JRLR2010TestService.wsdl");
    stubContext.setService("W2JRLR2010TestService");
    stubContext.setPort("W2JRLR2010TestPort");
    stubContext.setEndpointInterface(W2JRLR2010Test.class);
    stubContext.setWebServiceRef(webServiceRef);
  }

  protected String getEndpointURLProperty() {
    return "wsi.w2jrlr2010.endpoint.1";
  }

  protected String getWSDLURLProperty() {
    return "wsi.w2jrlr2010.wsdlloc.1";
  }

  public String echoImportDirectlyUTF8Test(String str) throws Exception {
    ImportDirectlyUTF8Request srq = new ImportDirectlyUTF8Request();
    srq.setStringValue(str);
    ImportDirectlyUTF8Response srp = ((W2JRLR2010Test) stubContext.getStub())
        .echoImportDirectlyUTF8Test(srq);
    return srp.getStringValue();
  }

  public BigInteger echoImportDirectlyUTF16Test(BigInteger bi)
      throws Exception {
    ImportDirectlyUTF16Request irq = new ImportDirectlyUTF16Request();
    irq.setBigInteger(bi);
    ImportDirectlyUTF16Response irp = ((W2JRLR2010Test) stubContext.getStub())
        .echoImportDirectlyUTF16Test(irq);
    return irp.getBigInteger();
  }

  public String echoImportIndirectlyUTF8Test(String str) throws Exception {
    ImportIndirectlyUTF8Request srq = new ImportIndirectlyUTF8Request();
    srq.setStringValue(str);
    ImportIndirectlyUTF8Response srp = ((W2JRLR2010Test) stubContext.getStub())
        .echoImportIndirectlyUTF8Test(srq);
    return srp.getStringValue();
  }

  public BigInteger echoImportIndirectlyUTF16Test(BigInteger bi)
      throws Exception {
    ImportIndirectlyUTF16Request irq = new ImportIndirectlyUTF16Request();
    irq.setBigInteger(bi);
    ImportIndirectlyUTF16Response irp = ((W2JRLR2010Test) stubContext.getStub())
        .echoImportIndirectlyUTF16Test(irq);
    return irp.getBigInteger();
  }

}
