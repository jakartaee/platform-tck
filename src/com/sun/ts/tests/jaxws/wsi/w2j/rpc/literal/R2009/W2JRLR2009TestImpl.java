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
package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2009;

import com.sun.ts.lib.util.*;
import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2009TestPort", serviceName = "W2JRLR2009TestService", targetNamespace = "http://w2jrlr2009testservice.org/W2JRLR2009TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2009TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2009.W2JRLR2009Test")

public class W2JRLR2009TestImpl implements W2JRLR2009Test {
  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2009.IncludedUTF16IntegerResponse echoIncludedUTF16IntegerTest(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2009.IncludedUTF16IntegerRequest params) {
    TestUtil.logTrace("UTF-16 test");
    TestUtil.logMsg("value=" + params.getBigInteger());
    IncludedUTF16IntegerResponse r = new IncludedUTF16IntegerResponse();
    r.setBigInteger(params.getBigInteger());
    return r;
  }

}
