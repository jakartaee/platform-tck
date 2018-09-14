/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations;

import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.Holder;

@WebService(portName = "Echo2Port", serviceName = "EchoService", targetNamespace = "http://Echo.org", wsdlLocation = "WEB-INF/wsdl/EchoService.wsdl", endpointInterface = "com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations.Echo2")
@Addressing(enabled = false)
public class Echo2Impl {

  public String echo(String string, Holder<String> testName) {
    return string;
  }
}
