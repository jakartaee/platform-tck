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
 * $Id: Client.java 51063 2006-08-11 19:56:36Z adf $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.typesubstitution;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;

import java.util.Properties;
import java.util.List;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.typesubstitution.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaj2wdltypesubstitution.endpoint.1";

  private static final String WSDLLOC_URL = "wsaj2wdltypesubstitution.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://typesubstitution/wsdl";

  private static final String SERVICE_NAME = "CarDealerService";

  private static final String PORT_NAME = "CarDealerPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private URL wsdlurl = null;

  CarDealer port = null;

  static CarDealerService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (CarDealer) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        CarDealerService.class, PORT_QNAME, CarDealer.class);
    TestUtil.logMsg("port=" + port);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (CarDealer) service.getCarDealerPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);

      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;

      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (CarDealerService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: getCars
   *
   * @assertion_ids: JAXWS:SPEC:2076;
   *
   * @test_Strategy
   *
   */
  public void getCars() throws Fault {
    TestUtil.logMsg("getCars");
    boolean pass = true;

    try {
      List<Car> cars = port.getSedans();
      Iterator<Car> i = cars.iterator();
      int ncars = 0;
      while (i.hasNext()) {
        Car car = i.next();
        ncars++;
        String make = car.getMake();
        String model = car.getModel();
        String year = car.getYear();
        String color;
        TestUtil.logMsg("Make=" + make);
        TestUtil.logMsg("Model=" + model);
        TestUtil.logMsg("Year=" + year);
        if (car instanceof Toyota) {
          Toyota t = (Toyota) car;
          color = t.getColor();
          TestUtil.logMsg("Color=" + color);
          if (!make.equals("Toyota") && !model.equals("Camry")
              && !year.equals("1998") && !color.equals("white")) {
            TestUtil.logErr("data mismatch expected Toyota Camry 1998 white");
            pass = false;
          } else
            TestUtil.logMsg("Toyota car matches");
        } else if (car instanceof Ford) {
          Ford t = (Ford) car;
          color = t.getColor();
          TestUtil.logMsg("Color=" + color);
          if (!make.equals("Ford") && !model.equals("Mustang")
              && !year.equals("1999") && !color.equals("red")) {
            TestUtil.logErr("data mismatch expected Ford Mustang 1999 red");
            pass = false;
          } else
            TestUtil.logMsg("Ford car matches");
        } else {
          TestUtil.logErr("data mismatch - no car of this type expected");
          pass = false;
        }
      }
      TestUtil.logMsg("List returned " + ncars + " cars");
      if (ncars != 2) {
        TestUtil.logErr("expected only 2 cars");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getCars failed", e);
    }

    if (!pass)
      throw new Fault("getCars failed");
  }

}
