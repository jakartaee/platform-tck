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
package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2114;

import com.sun.ts.lib.util.*;
import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2114TestPort", serviceName = "W2JRLR2114TestService", targetNamespace = "http://w2jrlr2114testservice.org/W2JRLR2114TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2114TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2114.W2JRLR2114Test")

public class W2JRLR2114TestImpl implements W2JRLR2114Test {
  public com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2114.IncludedStringResponse echoIncludedStringTest(
      com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2114.IncludedStringRequest params) {
    TestUtil.logTrace("SameTargetNamespace test");
    TestUtil.logMsg("value=" + params.getStringValue());
    IncludedStringResponse r = new IncludedStringResponse();
    r.setStringValue(params.getStringValue());
    return r;
  }

}
