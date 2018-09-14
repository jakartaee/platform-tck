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

package com.sun.ts.tests.jaspic.tssv.util;

import java.security.Principal;
import javax.security.auth.Subject;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.callback.CallbackHandler;

import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory;
import com.sun.ts.tests.jaspic.tssv.config.TSRegistrationListener;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author Raja Perumal
 */
public class SimulateRuntime {

  /**
   * SimulateRuntime to load TestSuite JSR 196 AuthConfigFactory
   */
  public static void main(String[] args) {

    Map properties = new HashMap();
    String logFileLocation = args[0];
    String providerConfigurationFileLocation = args[1];

    try {

      // Set jvm option for system variable log.file.location
      // Note: This is typically set in server runtime using jvm options.
      System.setProperty("log.file.location", logFileLocation);

      // Set jvm option for provider.configuration.file
      // Note: This is typically set in server runtime using jvm options.
      System.setProperty("provider.configuration.file",
          providerConfigurationFileLocation);

      // 1) Set TestSuite's AuthConfigFactory class (TSAuthConfigFactory)
      // in JAVA_HOME/jre/lib/security/java.security
      // authconfigprovider.factory=com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory
      //
      // 2) Alternate way to set TestSuite's AuhtConfigFactory in the runtime is
      // AuthConfigFactory acf = new
      // com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory();
      // AuthConfigFactory.setFactory(acf);
      //
      // In this RuntimeSimulation we use the first method i.e using
      // java.security file
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      // create a Registration listener
      RegistrationListener rlis = new TSRegistrationListener();

      CallbackHandler cbh = (CallbackHandler) new AuthDataCallbackHandler();

      // Get AuthConfigProvider
      AuthConfigProvider acp = acf.getConfigProvider(JASPICData.LAYER_SOAP,
          "JSR196Context", rlis);

      // Get AuthConfigProvider
      AuthConfigProvider acp2 = acf.getConfigProvider(JASPICData.LAYER_SOAP,
          "JSR196Context-2", rlis);

      // Get ServerAuthConfig
      ServerAuthConfig serverAuthConfig = acp2
          .getServerAuthConfig(JASPICData.LAYER_SOAP, "JSR196Context", cbh);

      // Get ServerAuthContext
      MessageInfo msgInfo = getSOAPMessageInfo();
      Subject defaultSubject = getDefaultSubject();
      String operation = serverAuthConfig.getAuthContextID(msgInfo);
      ServerAuthContext serverAuthContext = serverAuthConfig
          .getAuthContext(operation, defaultSubject, properties);

      // Call validateRequest
      serverAuthContext.validateRequest(msgInfo, defaultSubject,
          defaultSubject);

      // Call SecureResponse
      serverAuthContext.secureResponse(msgInfo, defaultSubject);

      // Get ClientAuthConfig
      ClientAuthConfig clientAuthConfig = acp2
          .getClientAuthConfig(JASPICData.LAYER_SOAP, "JSR196Context", cbh);

      // Get ClientAuthContext
      operation = clientAuthConfig.getAuthContextID(msgInfo);
      ClientAuthContext clientAuthContext = clientAuthConfig
          .getAuthContext(operation, defaultSubject, properties);

      // Call secureRequest
      clientAuthContext.secureRequest(msgInfo, defaultSubject);

      // Call validateResponse
      clientAuthContext.validateResponse(msgInfo, defaultSubject,
          defaultSubject);

      // Get AuthConfigProviderServlet
      AuthConfigProvider servletACP;
      servletACP = acf.getConfigProvider(JASPICData.LAYER_SERVLET,
          "/spitests_servlet_web/ModTestServlet", rlis);

      if (servletACP == null) {
        String err = "SimulateRuntime: servletACP == null!";
        System.out.println(err);
        throw new Exception(err);
      }

      ServerAuthConfig sac;
      ServerAuthContext saContext;
      sac = servletACP.getServerAuthConfig(JASPICData.LAYER_SERVLET,
          "/spitests_servlet_web/ModTestServlet", cbh);
      operation = getServletProfileOperation();
      Subject subj = getDefaultSubject();
      if (sac == null) {
        System.out.println("SimulateRuntime: sac == null!");
      } else {
        saContext = sac.getAuthContext(operation, subj, properties);
      }

      // Get AuthConfigProviderServlet
      AuthConfigProvider servletACP2;
      servletACP2 = acf.getConfigProvider(JASPICData.LAYER_SERVLET,
          "/spitests_servlet_web/SecondaryTestServlet", rlis);

    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("Simulation Over, Created Log file :" + logFileLocation
        + "/" + JASPICData.DEFAULT_LOG_FILE);
  }

  /*
   * Acceptable Servlet profile operationID should resemble a String in the
   * format of: request.getServletPath() + request.getPathInfo() + "%20" +
   * request.getMethod()
   *
   */
  public static String getServletProfileOperation() {
    String servletPath = "/ModTestServlet";
    String method = "POST"; // GET, POST, or PUT
    return servletPath + "%20 " + method;
  }

  public static Subject getDefaultSubject() {
    Subject subject = new Subject();
    String username = "j2ee"; // XXXX: fix hardcode?
    String password = "j2ee"; // XXXX: fix hardcode?
    Principal principal = new SimplePrincipal(username, password);

    // add a Principal and credential to the Subject
    subject.getPrincipals().add(principal);
    return subject;
  }

  private static MessageInfo getSOAPMessageInfo() {
    Map map = new HashMap();
    map.put("javax.xml.ws.wsdl.service", "http://hostname.port/someuri");

    // Create a dummy MessageInfo object with some dummy uri
    return new TSSOAPMessageInfo(map);
  }

}
