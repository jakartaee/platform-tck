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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.WebServiceException;

import jakarta.xml.soap.*;

import java.util.*;

import jakarta.jws.WebService;

@WebService(targetNamespace = "http://marshalltestservice.org/MarshallTestService.wsdl", portName = "MarshallTestPort4", serviceName = "MarshallTestService", wsdlLocation = "WEB-INF/wsdl/WSW2JDLMarshallTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.marshalltest.OneWayTest")

public class OneWayTestImpl implements OneWayTest {

  public void oneWayMethod(OneWayMessage v) {
    TestUtil.logTrace("oneWayMethod");
    TestUtil.logMsg("OneWayMessage: " + v.getStringValue());
  }
}
