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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1011;

import jakarta.jws.WebService;

@WebService(portName = "W2JRLR1011TestTwoPort", serviceName = "W2JRLR1011TestService", targetNamespace = "http://w2jrlr1011testservice.org/W2JRLR1011TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR1011TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1011.W2JRLR1011TestTwo")

public class W2JRLR1011TestTwoImpl implements W2JRLR1011TestTwo {
  public NonNullString echoString(NonNullString str) {
    return str;
  }
}
