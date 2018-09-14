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

package com.sun.ts.tests.webservices13.servlet.WSWebServiceRefLookup.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jaxws.common.*;

@Stateful(name = "WSWebServiceRefLookupClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  private String urlString;

  @WebServiceRef(name = "service/wswsreflookupservice")
  EchoService service = null;

  Echo port = null;

  @WebServiceRef(lookup = "java:comp/env/service/wswsreflookupservice")
  EchoService service2 = null;

  Echo port2 = null;

  private void getPort() throws Exception {
    TestUtil.logMsg("EjbClient DEBUG: service=" + service);
    port = (Echo) service.getPort(Echo.class);
    TestUtil.logMsg("EjbClient DEBUG: Obtained port");
    TestUtil.logMsg("EjbClient DEBUG: port=" + port);
    getTargetEndpointAddress(port);
    TestUtil.logMsg("EjbClient DEBUG: service2=" + service2);
    TestUtil.logMsg("EjbClient DEBUG: Obtained port");
    port2 = (Echo) service2.getPort(Echo.class);
    TestUtil.logMsg("EjbClient DEBUG: port2=" + port2);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String urlString = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + urlString);
  }

  @PostConstruct
  public void postConstruct() {
    try {
      getPort();
    } catch (Exception e) {
    }
    System.out.println("DEBUG postConstruct()");
    System.out.println("EjbClient DEBUG: service=" + service);
    System.out.println("EjbClient DEBUG: port=" + port);
    System.out.println("EjbClient DEBUG: service2=" + service2);
    System.out.println("EjbClient DEBUG: port2=" + port2);
    if (service == null || service2 == null || port == null || port2 == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    harnessProps = p;
    try {
      TestUtil.init(p);
      urlString = harnessProps.getProperty("ENDPOINTURL");
      JAXWS_Util.setTargetEndpointAddress(port2, urlString);
      getTargetEndpointAddress(port2);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    }
  }

  public boolean testwsreflookup() {
    TestUtil.logMsg("testwsreflookup");
    boolean pass = true;
    return stringTest();
  }

  private boolean stringTest() {
    TestUtil.logMsg("stringTest");
    boolean pass = true;
    String request = "Mary";

    try {
      String response = port2.echoString(request);
      if (!JAXWS_Data.compareValues(request, response, "String"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
