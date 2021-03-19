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

import com.sun.ts.lib.util.*;
import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2010TestPort", serviceName = "W2JRLR2010TestService", targetNamespace = "http://w2jrlr2010testservice.org/W2JRLR2010TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2010TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.W2JRLR2010Test")

public class W2JRLR2010TestImpl implements W2JRLR2010Test {
  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportDirectlyUTF8Response echoImportDirectlyUTF8Test(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportDirectlyUTF8Request params) {
    TestUtil.logTrace("UTF-8 test");
    TestUtil.logMsg("value=" + params.getStringValue());
    ImportDirectlyUTF8Response r = new ImportDirectlyUTF8Response();
    r.setStringValue(params.getStringValue());
    return r;
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportDirectlyUTF16Response echoImportDirectlyUTF16Test(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportDirectlyUTF16Request params) {
    TestUtil.logTrace("UTF-16 test");
    TestUtil.logMsg("value=" + params.getBigInteger());
    ImportDirectlyUTF16Response r = new ImportDirectlyUTF16Response();
    r.setBigInteger(params.getBigInteger());
    return r;
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportIndirectlyUTF8Response echoImportIndirectlyUTF8Test(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportIndirectlyUTF8Request params) {
    TestUtil.logTrace("UTF-8 test");
    TestUtil.logMsg("value=" + params.getStringValue());
    ImportIndirectlyUTF8Response r = new ImportIndirectlyUTF8Response();
    r.setStringValue(params.getStringValue());
    return r;
  }

  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportIndirectlyUTF16Response echoImportIndirectlyUTF16Test(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010.ImportIndirectlyUTF16Request params) {
    TestUtil.logTrace("UTF-16 test");
    TestUtil.logMsg("value=" + params.getBigInteger());
    ImportIndirectlyUTF16Response r = new ImportIndirectlyUTF16Response();
    r.setBigInteger(params.getBigInteger());
    return r;
  }

}
