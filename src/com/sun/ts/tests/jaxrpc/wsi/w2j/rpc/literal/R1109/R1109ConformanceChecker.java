/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1109;

import com.sun.ts.tests.jaxrpc.common.RequestConformanceChecker;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.*;

public class R1109ConformanceChecker extends RequestConformanceChecker{

  public void test(SOAPMessageContext context) throws SOAPException{
    test(context.getMessage().getMimeHeaders());
  }

  private void test(MimeHeaders headers){
    String [] soapAction = headers.getHeader(com.sun.ts.tests.jaxrpc.wsi.constants.SOAPConstants.SOAP_ACTION_HEADER_NAME);
    if(!(soapAction[0].startsWith("\"") || soapAction[0].startsWith("\'"))
        || !(soapAction[0].endsWith("\"") || soapAction[0].endsWith("\'"))){
      response = "failed. The value of SOAPAction http header must" +
                    " be quoted (BP-R1109).";
    }
  }
}
