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

package com.sun.ts.tests.jaxws.sharedclients;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.Constants;
import com.sun.ts.lib.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceFeature;
import java.net.URL;

public class StubContext {

  /**
   * The test name property name.
   */
  public static final String TEST_NAME = "testName";

  /**
   * The web server host property name.
   */
  public static final String WEB_SERVER_HOST = "webServerHost";

  /**
   * The web server port property name.
   */
  public static final String WEB_SERVER_PORT = "webServerPort";

  /**
   * The secure web server port property name.
   */
  public static final String SECURE_WEB_SERVER_PORT = "secureWebServerPort";

  /**
   * The monitor server port property name.
   */
  public static final String MONITOR_PORT = "monitorPort";

  /**
   * The WSI home property name.
   */
  public static final String WSI_HOME = "wsiHome";

  private int mode;

  private String endpointURL;

  private String wsdllocURL;

  private String namespace;

  private String service;

  private String port;

  private Class endpointInterface;

  private QName serviceName;

  private QName portName;

  private Object stub;

  private jakarta.xml.ws.Service webServiceRef;

  private WebServiceFeature[] wsf;

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public String getEndpointURL() {
    return endpointURL;
  }

  public void setEndpointURL(String endpointURL) {
    this.endpointURL = endpointURL;
  }

  public String getWsdllocURL() {
    return wsdllocURL;
  }

  public void setWsdllocURL(String wsdllocURL) {
    this.wsdllocURL = wsdllocURL;
  }

  public WebServiceFeature[] getWSF() {
    return wsf;
  }

  public void setWSF(WebServiceFeature[] w) {
    this.wsf = w;
  }

  /**
   * Initializes the stub.
   *
   * @throws java.lang.Exception
   */
  protected void initStub() throws Exception {
    if (stub == null) {
      if (mode == SOAPClient.MODE_STANDALONE) {
        createJAXWSStub();
        JAXWS_Util.setTargetEndpointAddress(stub, getEndpointURL());
      } else {
        createJavaEEStub();
      }
    }
  }

  protected void createJAXWSStub() throws Exception {
    TestUtil.logMsg("entering createJAXWSStub()");
    try {
      serviceName = new QName(namespace, service);
      portName = new QName(namespace, port);
      Class siClass = Class
          .forName(endpointInterface.getPackage().getName() + "." + service);
      stub = JAXWS_Util.getPort(new URL(getWsdllocURL()), serviceName, siClass,
          portName, endpointInterface, wsf);
      JAXWS_Util.setSOAPLogging(stub); // For Debug only
    } catch (Exception e) {
      TestUtil.logMsg(
          "StubContext.createJAXWSStub() could not get stub (caught exception)");
      TestUtil.printStackTrace(e);
      throw e;
    }
  }

  protected void createJavaEEStub() throws Exception {
    TestUtil.logMsg("entering createJavaEEStub()");
    try {
      TestUtil.logMsg("webServiceRef=" + webServiceRef);
      if (webServiceRef == null)
        throw new Exception("webServiceRef is null");
      TestUtil.logMsg(
          "Getting port from WebServiceRef for " + endpointInterface.getName());
      stub = webServiceRef.getPort(endpointInterface);
      TestUtil.logMsg("port=" + stub);
      JAXWS_Util.dumpTargetEndpointAddress(stub);
      // JAXWS_Util.setSOAPLogging(stub); // For Debug only
    } catch (Exception e) {
      TestUtil.logMsg(
          "StubContext.createJavaEEStub() could not get stub (caught exception)");
      TestUtil.printStackTrace(e);
      throw e;
    }
  }

  /**
   * @return String
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return QName
   */
  public QName getPortName() {
    return portName;
  }

  /**
   * @return String
   */
  public String getPort() {
    return port;
  }

  /**
   * @return String
   */
  public String getService() {
    return service;
  }

  /**
   * @return Class
   */
  public Class getEndpointInterface() {
    return endpointInterface;
  }

  /**
   * @return QName
   */
  public QName getServiceName() {
    return serviceName;
  }

  /**
   * @return Stub
   */
  public Object getStub() throws Exception {
    initStub();
    return stub;
  }

  /**
   * Sets the namespace.
   * 
   * @param namespace
   *          The namespace to set
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Sets the portName.
   * 
   * @param portName
   *          The portName to set
   */
  public void setPortName(QName portName) {
    this.portName = portName;
  }

  /**
   * Sets the port.
   * 
   * @param port
   *          The port to set
   */
  public void setPort(String port) {
    this.port = port;
  }

  /**
   * Sets the service.
   * 
   * @param service
   *          The service to set
   */
  public void setService(String service) {
    this.service = service;
  }

  /**
   * Sets the endpointInterface.
   * 
   * @param endpointInterface
   *          The endpointInterface to set
   */
  public void setEndpointInterface(Class endpointInterface) {
    this.endpointInterface = endpointInterface;
  }

  /**
   * Sets the serviceName.
   * 
   * @param serviceName
   *          The serviceName to set
   */
  public void setServiceName(QName serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * Sets the webServiceRef.
   * 
   * @param webServiceRef
   *          The webServiceRef to set
   */
  public void setWebServiceRef(jakarta.xml.ws.Service webServiceRef) {
    this.webServiceRef = webServiceRef;
  }
}
