/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.deploy.portcomplink.ejb.inter;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import javax.naming.InitialContext;

import com.sun.ts.tests.webservices12.deploy.portcomplink.ejb.intra.*;

@WebService(portName = "InterModuleSeiPort", serviceName = "InterModuleService", targetNamespace = "http://InterModuleService.org/wsdl", wsdlLocation = "META-INF/wsdl/InterModuleService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.deploy.portcomplink.ejb.inter.InterModuleSei")

@Stateless(name = "InterModuleEjb")
public class InterModuleEjbBean {

  public InterResponse sayInter(InterRequest input) {
    InterResponse response = new InterResponse();
    IntraRequest intra_input = null;
    IntraResponse intra_response = null;
    try {
      intra_input = new IntraRequest();
      intra_input.setArgument(input.getArgument());

      System.out.println(
          "Lookup: webservice java:comp/env/service/WSportcomplinkejb/intra");
      InitialContext ctx = new InitialContext();
      IntraModuleService svc = (IntraModuleService) ctx
          .lookup("java:comp/env/service/WSportcomplinkejb/intra");
      System.out.println("service=" + svc);
      System.out.println("Get port from service");
      IntraModuleSei port = (IntraModuleSei) svc.getPort(IntraModuleSei.class);
      System.out.println("port=" + port);
      intra_response = port.sayIntra(intra_input);
      response.setArgument("inter " + intra_response.getArgument());
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      throw new WebServiceException(e);
    }
    return response;
  }
}
