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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.R2712;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;

@WebService(portName = "W2JDLR2712TestOnePort", serviceName = "W2JDLR2712TestService", targetNamespace = "http://w2jdlr2712testservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/W2JDLR2712TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.document.literal.R2712.W2JDLR2712TestOne")

public class W2JDLR2712TestOneImpl implements W2JDLR2712TestOne {

  public HelloResponse hello(HelloRequest req) {
    System.out.println(req.getString());
    HelloResponse resp = new HelloResponse();
    resp.setString(req.getString());
    return resp;
  }
}
