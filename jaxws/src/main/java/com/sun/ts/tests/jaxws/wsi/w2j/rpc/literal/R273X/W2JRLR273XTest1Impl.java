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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R273X;

import jakarta.jws.WebService;

@WebService(portName = "W2JRLR273XTest1Port", serviceName = "W2JRLR273XTestService", targetNamespace = "http://w2jrlr273Xtestservice.org/W2JRLR273XTestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR273XTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R273X.W2JRLR273XTest1")

public class W2JRLR273XTest1Impl implements W2JRLR273XTest1 {
  public FooBar echoFooBar(FooBar fb) {
    return fb;
  }
}
