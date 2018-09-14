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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxrpc.sharedclients;

import com.sun.ts.tests.jaxrpc.common.JAXRPC_Util;
import com.sun.ts.tests.jaxrpc.common.Constants;
import com.sun.ts.lib.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.Stub;
import javax.xml.rpc.Service;

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

  private String portPrefix;

  private Class serviceEndpointInterface;

  private QName serviceName;

  private QName portName;

  private Stub stub;

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

  /**
   * Initializes the stub.
   *
   * @throws java.lang.Exception
   */
  protected void initStub() throws Exception {
    if (stub == null) {
      if (mode == SOAPClient.MODE_JAXRPC) {
        createJAXRPCStub();
        stub._setProperty(Constants.ENDPOINT_PROPERTY, getEndpointURL());
      } else {
        createJavaEEStub();
      }
    }
  }

  protected void createJAXRPCStub() throws Exception {
    serviceName = new QName(namespace, service);
    portName = new QName(namespace, portPrefix + "Port");
    stub = (Stub) JAXRPC_Util.getStub(
        serviceEndpointInterface.getPackage().getName() + "." + service,
        "get" + portName.getLocalPart());
  }

  protected void createJavaEEStub() throws Exception {
    Context context = new InitialContext();
    TestUtil.logMsg("lookup java:comp/env/service/" + service);
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) context
        .lookup("java:comp/env/service/" + service);
    stub = (Stub) svc.getPort(getServiceInterface());
    JAXRPC_Util.setSOAPLogging(stub, System.out); // For Debug only
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
  public String getPortPrefix() {
    return portPrefix;
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
  public Class getServiceInterface() {
    return serviceEndpointInterface;
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
  public Stub getStub() throws Exception {
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
   * Sets the portPrefix.
   * 
   * @param portPrefix
   *          The portPrefix to set
   */
  public void setPortPrefix(String portPrefix) {
    this.portPrefix = portPrefix;
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
   * Sets the serviceEndpointInterface.
   * 
   * @param serviceEndpointInterface
   *          The serviceEndpointInterface to set
   */
  public void setServiceInterface(Class serviceEndpointInterface) {
    this.serviceEndpointInterface = serviceEndpointInterface;
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
}
