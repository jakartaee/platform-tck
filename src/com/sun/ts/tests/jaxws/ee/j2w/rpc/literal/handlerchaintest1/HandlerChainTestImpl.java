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

package com.sun.ts.tests.jaxws.ee.j2w.rpc.literal.handlerchaintest1;

import jakarta.xml.ws.WebServiceException;
import jakarta.jws.HandlerChain;

@HandlerChain(name = "", file = "server-handler.xml")
@jakarta.jws.WebService(targetNamespace = "http://handlerchaintestservice.org/wsdl", serviceName = "HandlerChainTestService", portName = "HandlerChainTestPort", endpointInterface = "com.sun.ts.tests.jaxws.ee.j2w.rpc.literal.handlerchaintest1.HandlerChainTest")
public class HandlerChainTestImpl implements HandlerChainTest {
  public String helloWorld(String str) {
    return str;
  }

}
