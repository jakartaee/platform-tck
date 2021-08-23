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

package com.sun.ts.tests.jaspic.spi.soap;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaspic.spi.common.CommonTests;
import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory;
import com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactoryForStandalone;
import com.sun.ts.tests.jaspic.tssv.config.TSRegistrationListener;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationEntry;
import com.sun.ts.tests.jaspic.tssv.util.ProviderConfigurationXMLFileProcessor;
import com.sun.ts.tests.jaspic.util.LogFileProcessor;
import com.sun.ts.tests.jaspic.util.WebServiceUtils;

import jakarta.security.auth.message.config.AuthConfigFactory;
import jakarta.security.auth.message.config.AuthConfigProvider;
import jakarta.security.auth.message.config.RegistrationListener;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Raja Perumal
 */
public class Client extends EETest {
  @WebServiceRef(name = "service/HelloService")
  static HelloService service;

  private Hello port;

  private Properties props = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String logicalHostName = "server";

  private String PROTOCOL = "http";

  private String urlString = null;

  private int portnum = 8000;

  private String platformMode = null;

  private transient LogFileProcessor logProcessor = null;

  private transient CommonTests commonTests;

  // server side application context identifier
  private String expectedAppContextId = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String NAMESPACEURI = "http://soap.spi.jaspic.tests.ts.sun.com/";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private transient ProviderConfigurationXMLFileProcessor configFileProcessor = null;

  private Collection providerConfigurationEntriesCollection = null;

  private String soapAppContext = "localhost /Hello_web/Hello";

  private String servletAppContext = null;

  private String logFileLocation;

  private String providerConfigFileLocation;

  private String vendorACFClass;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: log.file.location; provider.configuration.file;
   * vendor.authconfig.factory; webServerHost; webServerPort; platform.mode;
   * user; password; logical.hostname.soap;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);
      hostname = props.getProperty("webServerHost");
      platformMode = props.getProperty("platform.mode");
      logicalHostName = props.getProperty("logical.hostname.soap");
      portnum = Integer.parseInt(props.getProperty("webServerPort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum,
          "/Hello_web/Hello");

      // Get app-context-id from ProviderConfiguration.xml file
      expectedAppContextId = IdUtil.getAppContextId("soap");

      logFileLocation = props.getProperty("log.file.location");
      TestUtil.logMsg("Log file location = " + logFileLocation);

      providerConfigFileLocation = props
          .getProperty("provider.configuration.file");
      TestUtil.logMsg(
          "TestSuite Provider ConfigFile = " + providerConfigFileLocation);

      vendorACFClass = props.getProperty("vendor.authconfig.factory");
      TestUtil.logMsg("Vendor AuthConfigFactory class = " + vendorACFClass);

      // If there is no matching app-context-id then try with default id
      if (expectedAppContextId.equals("")) {
        expectedAppContextId = logicalHostName + " " + "/Hello_web/Hello";
      }

      sayHelloProtected();

      // create LogProcessor
      logProcessor = new LogFileProcessor(props);

      // retrieve logs based on application Name
      logProcessor.fetchLogs("pullAllLogRecords|fullLog");

      // this is class that holds generic tests in it
      commonTests = new CommonTests();

    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }

    TestUtil.logMsg("setup ok");
  }

  /*
   * sayHelloProtected
   *
   * This method is invoked first by all tests, as a result of this client and
   * server soap runtime generates corresponding messages in the log and the
   * other tests verify those messages.
   */
  private void sayHelloProtected() throws Fault {

    try {
      Hello port = null;

      if (platformMode.equals("jakartaEE")) {
        port = (Hello) getJavaEEPort();
      } else {
        port = (Hello) getStandAlonePort();
      }

      BindingProvider bindingProvider = (BindingProvider) port;
      Map<String, Object> map = bindingProvider.getRequestContext();

      TestUtil.logMsg(
          "Setting the target endpoint address on WS port: " + urlString);
      map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);

      TestUtil.logMsg("Invoking sayHelloProtected on Hello port");
      String text = port.sayHelloProtected("Raja");
      TestUtil.logMsg("Got Output : " + text);

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("Test sayHelloProtected failed");
    }

  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: GetConfigProvider
   *
   * @assertion_ids: JASPIC:SPEC:8; JASPIC:SPEC:14; JASPIC:SPEC:116;
   *                 JASPIC:SPEC:117; JASPIC:JAVADOC:77; JASPIC:JAVADOC:79;
   *                 JASPIC:JAVADOC:80; JASPIC:JAVADOC:84; JASPIC:JAVADOC:85;
   *                 JASPIC:JAVADOC:91; JASPIC:SPEC:110;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether AuthConfigFactory.getConfigProvider is
   *                 called in the server.
   *
   *                 Description The runtime must invoke
   *                 AuthConfigFactory.getConfigProvider to obtain the
   *                 AuthConfigProvider. The runtime must also specify
   *                 appropriate(non-null) layer and application context
   *                 identifiers in its call to getConfigProvider.
   *
   */
  public void GetConfigProvider() throws Fault {
    boolean verified = false;

    String args[] = { "TSAuthConfigFactory.getConfigProvider called",
        "getConfigProvider called for Layer : SOAP and AppContext :"
            + expectedAppContextId };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("GetConfigProvider failed : "
          + "AuthConfigFactory.getConfigProvider not called");
    } else
      TestUtil.logMsg("TSAuthConfigFactory.getConfigProvider called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: GetFactory
   *
   * @assertion_ids: JASPIC:SPEC:7; JASPIC:SPEC:10; JASPIC:JAVADOC:77;
   *                 JASPIC:JAVADOC:80; JASPIC:JAVADOC:84; JASPIC:JAVADOC:79;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether AuthConfigFactory.getConfigProvider is
   *                 called in the server.
   *
   *                 Description The runtime must invoke
   *                 AuthConfigFactory.getConfigProvider to obtain the
   *                 AuthConfigProvider. By calling getConfigProvider, we can
   *                 assume getFactory() was called.
   *
   */
  public void GetFactory() throws Fault {
    boolean verified = false;

    String args[] = { "TSAuthConfigFactory.getFactory called Indirectly" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault(
          "GetFactory failed : " + "AuthConfigFactory.getFactory not called");
    } else
      TestUtil.logMsg("TSAuthConfigFactory.getFactory called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ServerAuthConfig
   *
   * @assertion_ids: JASPIC:SPEC:11; JASPIC:SPEC:13 ; JASPIC:SPEC:16;
   *                 JASPIC:JAVADOC:95
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether provider.getServerAuthConfig is called in
   *                 the server.
   *
   *                 Description The runtime must invoke
   *                 AuthConfigProvider.getServerAuthConfig() to obtain the
   *                 AuthConfig. The runtime must also specify
   *                 appropriate(non-null) layer(i.e for this test case "SOAP"
   *                 layer) and application context identifiers in its call to
   *                 getServerAuthConfig.
   *
   */
  public void ServerAuthConfig() throws Fault {
    boolean verified = false;

    String args[] = {
        "TSAuthConfigProvider.getServerAuthConfig called for layer=SOAP"
            + " : appContext=" + expectedAppContextId };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ServerAuthConfig failed : "
          + "AuthConfigProvider.getServerAuthConfig not called");
    } else
      TestUtil.logMsg("TSAuthConfigProvider.getServerAuthConfig called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ClientAuthConfig
   *
   * @assertion_ids: JASPIC:SPEC:11; JASPIC:SPEC:12 ; JASPIC:SPEC:16;
   *                 JASPIC:JAVADOC:92; JASPIC:JAVADOC:93; JASPIC:SPEC:110;
   *                 JASPIC:SPEC:111
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether AuthConfigProvider.ClientAuthConfig is
   *                 called in the server.
   *
   *                 Description The runtime must invoke
   *                 AuthConfigProvider.getClientAuthConfig() to obtain the
   *                 AuthConfig. The runtime must also specify
   *                 appropriate(non-null) layer(i.e for this test case "SOAP"
   *                 layer) and application context identifiers in its call to
   *                 getClientAuthConfig.
   *
   */
  public void ClientAuthConfig() throws Fault {
    boolean verified = false;

    String srchStrPrefix = "TSAuthConfigProvider.getClientAuthConfig called for layer=SOAP : appContext=";

    String args[] = { "service/HelloService",
        "http://" + hostname + ":" + portnum + "/Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ClientAuthConfig failed : "
          + "AuthConfigProvider.getClientAuthConfig not called");
    } else
      TestUtil.logMsg("TSAuthConfigProvider.getClientAuthConfig called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: SecureRequest
   *
   * @assertion_ids: JASPIC:SPEC:35; JASPIC:JAVADOC:5; JASPIC:SPEC:36;
   *                 JASPIC:JAVADOC:75
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ClientAuthContext.secureRequest() is called
   *
   *                 Description The runtime must invoke
   *                 ClientAuthContext.secureRequest()
   *
   */
  public void SecureRequest() throws Fault {
    boolean verified = false;

    String args[] = { "TSClientAuthContext.secureRequest called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("SecureRequest failed : "
          + "ClientAuthContext.secureRequest not called");
    } else
      TestUtil.logMsg("ClientAuthContext.secureRequest called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ValidateResponse
   *
   * @assertion_ids: JASPIC:SPEC:42
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ClientAuthContext.validateResponse() is
   *                 called
   *
   *                 Description The runtime must invoke
   *                 ClientAuthContext.validateResponse()
   *
   */
  public void ValidateResponse() throws Fault {
    boolean verified = false;

    String args[] = { "TSClientAuthContext.validateResponse called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("validateResponse failed : "
          + "ClientAuthContext.validateResponse not called");
    } else
      TestUtil.logMsg("ClientAuthContext.validateResponse called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ValidateRequest
   *
   * @assertion_ids: JASPIC:SPEC:50; JASPIC:JAVADOC:16; JASPIC:JAVADOC:17;
   *                 JASPIC:JAVADOC:23; JASPIC:SPEC:23; JASPIC:SPEC:19
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ServerAuthContext.validateRequest() is
   *                 called
   *
   *                 Description The runtime must invoke
   *                 ServerAuthContext.validateRequest()
   *
   */
  public void ValidateRequest() throws Fault {
    boolean verified = false;

    String args[] = { "TSServerAuthContext.validateRequest called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ValidateRequest failed : "
          + "ServerAuthContext.validateRequest not called");
    } else
      TestUtil.logMsg("ServerAuthContext.validateRequest called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: SecureResponse
   *
   * @assertion_ids: JASPIC:SPEC:59; JASPIC:JAVADOC:16; JASPIC:JAVADOC:17;
   *                 JASPIC:JAVADOC:23; JASPIC:JAVADOC:26
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ServerAuthContext.secureResponse() is called
   *
   *                 Description The runtime must invoke
   *                 ServerAuthContext.secureResponse()
   *
   */
  public void SecureResponse() throws Fault {
    boolean verified = false;

    String args[] = { "TSServerAuthContext.secureResponse called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("SecureResponse failed : "
          + "ServerAuthContext.secureResponse not called");
    } else
      TestUtil.logMsg("ServerAuthContext.secureResponse called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ClientAuthContext
   *
   * @assertion_ids: JASPIC:SPEC:34; JASPIC:JAVADOC:72; JASPIC:JAVADOC:97;
   *                 JASPIC:JAVADOC:98
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ClientAuthConfig.getAuthContext() is called
   *
   *                 Description The runtime must invoke
   *                 clientAuthConfig().getAuthContext() to obtain the
   *                 ClientAuthContext.
   *
   */
  public void ClientAuthContext() throws Fault {
    boolean verified = false;

    String srchStrPrefix = "TSClientAuthConfig.getAuthContext:  layer=SOAP : appContext=";

    String args[] = { "service/HelloService",
        "http://" + hostname + ":" + portnum + "/Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ClientAuthContext failed : "
          + "clientAuthConfig.getAuthContext not called");
    } else
      TestUtil.logMsg("clientAuthConfig.getAuthContext called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ServerAuthContext
   *
   * @assertion_ids: JASPIC:SPEC:34; JASPIC:JAVADOC:100; JASPIC:SPEC:153;
   *                 JASPIC:SPEC:156; JASPIC:JAVADOC:101
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether ServerAuthConfig.getAuthContext() is called
   *
   *                 Description The runtime must invoke
   *                 serverAuthConfig().getAuthContext() to obtain the
   *                 ServerAuthContext.
   *
   */
  public void ServerAuthContext() throws Fault {
    boolean verified = false;
    String args[] = {
        "TSServerAuthConfig.getAuthContext:  layer=SOAP : appContext="
            + expectedAppContextId };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ServerAuthContext failed : "
          + "serverAuthConfig.getAuthContext not called");
    } else
      TestUtil.logMsg("serverAuthConfig.getAuthContext called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: MessageInfo
   *
   * @assertion_ids: JASPIC:SPEC:35; JASPIC:SPEC:44; JASPIC:JAVADOC:5;
   *                 JASPIC:SPEC:112; JASPIC:JAVADOC:9; JASPIC:JAVADOC:10;
   *                 JASPIC:JAVADOC:11; JASPIC:JAVADOC:28; JASPIC:SPEC:23;
   *                 JASPIC:SPEC:19; JASPIC:SPEC:36; JASPIC:SPEC:37;
   *                 JASPIC:SPEC:43; JASPIC:SPEC:51; JASPIC:SPEC:113;
   *                 JASPIC:SPEC:131; JASPIC:SPEC:132;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the messageInfo passed to secureRequest()
   *                 validateRequest(), secureResponse() and validateResponse()
   *                 contiains right values for getRequestMessage() and
   *                 getResponseMessage() as per the spec.
   *
   *                 3. clientSubject - a Subject that represents the source of
   *                 the service request, or null.
   *
   *                 Description The MessageInfo argument used in any call made
   *                 by the message processing runtime to secureRequest,
   *                 validateResponse, validateRequest, or secureResponse must
   *                 have been initialized such that the non-null objects
   *                 returned by the getRequestMessage and getResponseMessage
   *                 methods of the MessageInfo are an instanceof
   *                 jakarta.xml.soap.SOAPMessage.
   *
   *
   */
  public void MessageInfo() throws Fault {
    boolean verified = false;

    String args[] = {
        "secureRequest : MessageInfo.getRequestMessage() is of type jakarta.xml.soap.SOAPMessage",
        "validateRequest : MessageInfo.getRequestMessage() is of type jakarta.xml.soap.SOAPMessage",
        "secureResponse : MessageInfo.getRequestMessage() is of type jakarta.xml.soap.SOAPMessage",
        "secureResponse : MessageInfo.getResponseMessage() is of type jakarta.xml.soap.SOAPMessage",
        "validateResponse : MessageInfo.getRequestMessage() is of type jakarta.xml.soap.SOAPMessage",
        "validateResponse : MessageInfo.getResponseMessage() is of type jakarta.xml.soap.SOAPMessage" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("MessageInfo failed : "
          + "The request and response messages contains incorrect values");
    } else
      TestUtil.logMsg("MessageInfo contains right values");
  }

  /**
   * @testName: NameAndPasswordCallbackSupport
   *
   * @assertion_ids: JASPIC:SPEC:123; JASPIC:JAVADOC:103
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether CallbackHandler for client runtime supports
   *                 NameCallback and PasswordCallback
   *
   *                 Description Unless the client runtime is embedded in a
   *                 server runtime (e.g.; an invocation of a web service by a
   *                 servlet running in a Servlet container), The
   *                 CallbackHandler passed to ClientAuthModule.initialize must
   *                 support the following callbacks: NameCallback,
   *                 PasswordCallback
   *
   */
  public void NameAndPasswordCallbackSupport() throws Fault {
    boolean verified = false;
    String args[] = {
        "In SOAP : ClientRuntime CallbackHandler supports NameCallback",
        "In SOAP : ClientRuntime CallbackHandler supports PasswordCallback" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("NameAndPasswordCallbackSupport failed");
    } else
      TestUtil.logMsg(
          "CallbackHandler supports NameCallback " + "and PasswordCallback");
  }

  /**
   * @testName: ClientRuntimeCommonCallbackSupport
   *
   * @assertion_ids: JASPIC:SPEC:114; JASPIC:JAVADOC:35; JASPIC:JAVADOC:36;
   *                 JASPIC:JAVADOC:49; JASPIC:JAVADOC:51; JASPIC:JAVADOC:54;
   *                 JASPIC:JAVADOC:63; JASPIC:JAVADOC:65; JASPIC:JAVADOC:68;
   *                 JASPIC:JAVADOC:69; JASPIC:JAVADOC:71;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether CallbackHandler for client runtime supports
   *                 CertStoreCallback, PrivateKeyCallback, SecretKeyCallback,
   *                 TrustStoreCallback
   *
   *                 Description
   *
   *                 The CallbackHandler passed to the initialize method of an
   *                 authentication module should support the following
   *                 callbacks, and it must be possible to configure the runtime
   *                 such that the CallbackHandler passed at module
   *                 initialization module supports the following callbacks (in
   *                 addition to any others required to be supported by the
   *                 applicable internal profile): CertStoreCallback,
   *                 PrivateKeyCallback, SecretKeyCallback, TrustStoreCallback
   *
   *
   */
  public void ClientRuntimeCommonCallbackSupport() throws Fault {
    boolean verified = false;
    String args[] = {
        "In SOAP : ClientRuntime CallbackHandler supports CertStoreCallback",
        "In SOAP : ClientRuntime CallbackHandler supports PrivateKeyCallback",
        "In SOAP : ClientRuntime CallbackHandler supports SecretKeyCallback",
        "In SOAP : ClientRuntime CallbackHandler supports TrustStoreCallback" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ClientRuntimeCommonCallbackSupport failed");
    } else
      TestUtil.logMsg("SOAP Client Runtime CallbackHandler supports "
          + "CertStoreCallback, PrivateKeyCallback, SecretKeyCallback"
          + " and TrustStoreCallback ");
  }

  /**
   * @testName: ServerRuntimeCommonCallbackSupport
   *
   * @assertion_ids: JASPIC:SPEC:114; JASPIC:JAVADOC:35; JASPIC:JAVADOC:36;
   *                 JASPIC:JAVADOC:49; JASPIC:JAVADOC:51; JASPIC:JAVADOC:54;
   *                 JASPIC:JAVADOC:63; JASPIC:JAVADOC:65; JASPIC:JAVADOC:68;
   *                 JASPIC:JAVADOC:69; JASPIC:JAVADOC:71; JASPIC:JAVADOC:106
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether CallbackHandler for server runtime supports
   *                 CertStoreCallback, PrivateKeyCallback, SecretKeyCallback,
   *                 TrustStoreCallback
   *
   *                 Description
   *
   *                 The CallbackHandler passed to the initialize method of an
   *                 authentication module should support the following
   *                 callbacks, and it must be possible to configure the runtime
   *                 such that the CallbackHandler passed at module
   *                 initialization module supports the following callbacks (in
   *                 addition to any others required to be supported by the
   *                 applicable internal profile): CertStoreCallback,
   *                 PrivateKeyCallback, SecretKeyCallback, TrustStoreCallback
   *
   *
   */
  public void ServerRuntimeCommonCallbackSupport() throws Fault {
    boolean verified = false;
    String args[] = {
        "In SOAP : ServerRuntime CallbackHandler supports CertStoreCallback",
        "In SOAP : ServerRuntime CallbackHandler supports PrivateKeyCallback",
        "In SOAP : ServerRuntime CallbackHandler supports SecretKeyCallback",
        "In SOAP : ServerRuntime CallbackHandler supports TrustStoreCallback" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ServerRuntimeCommonCallbackSupport failed");
    } else
      TestUtil.logMsg("SOAP ServerRuntime CallbackHandler supports "
          + "CertStoreCallback, PrivateKeyCallback, SecretKeyCallback"
          + " and TrustStoreCallback ");
  }

  /**
   * @testName: ServerRuntimeCallbackSupport
   *
   * @assertion_ids: JASPIC:SPEC:114; JASPIC:SPEC:149; JASPIC:JAVADOC:38;
   *                 JASPIC:JAVADOC:39; JASPIC:JAVADOC:40; JASPIC:JAVADOC:42;
   *                 JASPIC:JAVADOC:43; JASPIC:JAVADOC:44; JASPIC:JAVADOC:46;
   *                 JASPIC:JAVADOC:30; JASPIC:JAVADOC:41; JASPIC:JAVADOC:45
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether CallbackHandler for server runtime supports
   *                 CallerPrincipalCallback, GroupPrincipalCallback and
   *                 PasswordValidationCallback
   *
   *                 Description
   *
   *                 The CallbackHandler passed to the
   *                 ServerAuthModule.initialize must support the following
   *                 callbacks,
   *
   *                 CallerPrincipalCallback, GroupPrincipalCallback,
   *                 PasswordValidationCallback
   *
   *
   */
  public void ServerRuntimeCallbackSupport() throws Fault {
    boolean verified = false;
    String args[] = {
        "In SOAP : ServerRuntime CallbackHandler supports CallerPrincipalCallback",
        "In SOAP : ServerRuntime CallbackHandler supports GroupPrincipalCallback",
        "In SOAP : ServerRuntime CallbackHandler supports PasswordValidationCallback" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ServerRuntimeCallbackSupport failed");
    } else
      TestUtil.logMsg("SOAP ServerRuntime CallbackHandler supports "
          + "CallerPrincipalCallback, GroupPrincipalCallback,"
          + " and PasswordValidationCallback ");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: OperationId
   *
   * @assertion_ids: JASPIC:SPEC:121; JASPIC:SPEC:125; JASPIC:JAVADOC:73
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the operationId is "sayHelloProtected"
   *
   *                 Description
   *
   *                 If getOperation returns a non-null operation identifier,
   *                 then the operation identifier must be the String value
   *                 corresponding to the operation name appearing in the
   *                 service description (i.e., WSDL).
   *
   *                 When its getOperation method is called, any authentication
   *                 context configuration object obtained for the SOAP layer,
   *                 must attempt to derive the corresponding operation
   *                 identifier value from the request message (within
   *                 messageInfo).
   *
   *
   */
  public void OperationId() throws Fault {
    boolean verified = false;

    String args[] = {
        "getAuthContextID() called for layer=SOAP shows AuthContextId="
            + "sayHelloProtected" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("OperationId failed");
    } else
      TestUtil.logMsg("Valid OperationId returned");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACPClientAuthConfig
   *
   * @assertion_ids: JASPIC:SPEC:124; JASPIC:JAVADOC:77
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the arguments(layer and appcontextId) passed
   *                 to obtain AuthConfigProvider is same as the arguments used
   *                 in calling getClientAuthConfig.
   *
   *                 Description
   *
   *                 If a non-null AuthConfigProvider is returned (by the call
   *                 to getConfigProvider), the messaging runtime must call
   *                 getClientAuthConfig on the provider to obtain the
   *                 authentication context configuration object pertaining to
   *                 the application context at the layer. The layer and
   *                 appContext arguments of the call to getClientAuthConfig
   *                 must be the same as those used to acquire the provider.
   *
   *
   */
  public void ACPClientAuthConfig() throws Fault {
    boolean verified = false;
    String srchStrPrefix = "TSAuthConfigFactory.getConfigProvider returned non-null provider for"
        + " Layer : SOAP and AppContext :";

    String args[] = { "service/HelloService",
        "http://" + hostname + ":" + portnum + "/Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ACPClientAuthConfig failed");
    } else
      TestUtil.logMsg("ACPClientAuthConfig : Same layer and appContextId used");

    srchStrPrefix = "TSAuthConfigProvider.getClientAuthConfig called for "
        + "layer=SOAP : appContext=";

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ACPClientAuthConfig failed");
    } else
      TestUtil.logMsg("ACPClientAuthConfig : Same layer and appContextId used");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACPAuthContext
   *
   * @assertion_ids: JASPIC:SPEC:125; JASPIC:SPEC:150; JASPIC:JAVADOC:5;
   *                 JASPIC:JAVADOC:28; JASPIC:JAVADOC:73; JASPIC:JAVADOC:79
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the arguments(operation) passed to obtain
   *                 getAuthContext is same as defined in Section 4.7.1
   *
   *                 Description The authentication context identifier used in
   *                 the call to getAuthContext must be equivalent to the value
   *                 that would be acquired by calling getAuthContextID with the
   *                 MessageInfo that will be used in the corresponding call to
   *                 secureRequest (by a client runtime) or validateRequest (by
   *                 a server runtime).
   *
   */
  public void ACPAuthContext() throws Fault {
    boolean verified = false;

    String args[] = {

        "TSAuthConfigFactory.getConfigProvider returned non-null provider for"
            + " Layer : SOAP and AppContext :" + expectedAppContextId,

        "TSServerAuthConfig.getAuthContext:  layer=SOAP" + " : appContext="
            + expectedAppContextId + " operationId=sayHelloProtected"

    };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ACPAuthContext failed");
    } else
      TestUtil.logMsg("Calls to getAuthContext contains valid server side"
          + " Application Context Identifiers");

    String srchStrPrefix = "TSClientAuthConfig.getAuthContext:  layer=SOAP"
        + " : appContext=";

    String cArgs[] = { "service/HelloService",
        "http://" + hostname + ":" + portnum + "/Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(cArgs,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ACPAuthContext failed");
    } else
      TestUtil.logMsg("Calls to getAuthContext contains valid operationId");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: CACRequestResponse
   *
   * @assertion_ids: JASPIC:SPEC:130; JASPIC:JAVADOC:7; JASPIC:JAVADOC:9;
   *                 JASPIC:SPEC:19; JASPIC:SPEC:23; JASPIC:SPEC:129;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether for a non-null ClientAuthContext,
   *                 secureRequest and validateResponse are called properly.
   *
   *                 Description
   *
   *                 If the client runtime obtained a non-null ClientAuthContext
   *                 by using the operation identifier corresponding to the
   *                 request message, then at point (1) in the message
   *                 processing model, the runtime must call secureRequest on
   *                 the ClientAuthContext, and at point (4) the runtime must
   *                 call validateResponse on the ClientAuthContext.
   *
   *
   */
  public void CACRequestResponse() throws Fault {
    boolean verified = false;

    String args[] = {

        "TSClientAuthConfig.getAuthContext: returned non-null"
            + " ClientAuthContext for operationId=sayHelloProtected",

        "TSClientAuthContext.secureRequest called",

        "TSClientAuthContext.validateResponse called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("CACRequestResponse failed");
    } else
      TestUtil.logMsg("CAC secureRequest and validateResponse called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ClientRuntimeMessageInfoMap
   *
   * @assertion_ids: JASPIC:SPEC:133; JASPIC:JAVADOC:7
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether the Map in messageInfo object passed to
   *                 secureRequest and validateResponse contains the right value
   *                 for key jakarta.xml.ws.wsdl.service
   *
   *                 Description This profile requires that the message
   *                 processing runtime establish the following key-value pairs
   *                 within the Map of the MessageInfo passed in the calls to
   *                 secureRequest and validateResponse
   *                 Key=jakarta.xml.ws.wsdl.service Value= the value of the
   *                 qualified service name, represented as a
   *                 javax.xml.namespace.QName
   *
   */
  public void ClientRuntimeMessageInfoMap() throws Fault {
    boolean verified = false;

    QName expectedQName = new QName("http://soap.spi.jaspic.tests.ts.sun.com/",
        "HelloService");

    String args[] = {

        "TSClientAuthModule.secureRequest messageInfo :"
            + "jakarta.xml.ws.wsdl.service=" + expectedQName.toString(),

        "TSClientAuthModule.validateResponse messageInfo :"
            + "jakarta.xml.ws.wsdl.service=" + expectedQName.toString()

    };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ClientRuntimeMessageInfoMap failed");
    } else
      TestUtil.logMsg("ClientRuntimeMessageInfoMap contains right values");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACPServerAuthConfig
   *
   * @assertion_ids: JASPIC:SPEC:150; JASPIC:JAVADOC:79; JASPIC:JAVADOC:94
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify Whether the arguments(layer and appcontextId) passed
   *                 to obtain AuthConfigProvider is same as the arguments used
   *                 in calling getServerAuthConfig.
   *
   *                 Description
   *
   *                 If a non-null AuthConfigProvider is returned (by the call
   *                 to getConfigProvider), the messaging runtime must call
   *                 getServerAuthConfig on the provider to obtain the
   *                 authentication context configuration object pertaining to
   *                 the application context at the layer. The layer and
   *                 appContext arguments of the call to getServerAuthConfig
   *                 must be the same as those used to acquire the provider.
   *
   *
   */
  public void ACPServerAuthConfig() throws Fault {
    boolean verified = false;

    String args[] = {
        "TSAuthConfigFactory.getConfigProvider returned non-null provider for"
            + " Layer : SOAP and AppContext :" + expectedAppContextId,

        "TSAuthConfigProvider.getServerAuthConfig called for "
            + "layer=SOAP : appContext=" + expectedAppContextId };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("ACPServerAuthConfig failed");
    } else
      TestUtil.logMsg("ACPServerAuthConfig : Same layer and appContextId used");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: SACRequestResponse
   *
   * @assertion_ids: JASPIC:SPEC:130; JASPIC:JAVADOC:13; JASPIC:JAVADOC:16;
   *                 JASPIC:JAVADOC:17; JASPIC:JAVADOC:23; JASPIC:JAVADOC:26;
   *                 JASPIC:JAVADOC:28; JASPIC:SPEC:155;
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether for a non-null ServerAuthContext,
   *                 validateRequest and secureResponse are called properly.
   *
   *                 Description
   *
   *                 If the server runtime obtained a non-null ServerAuthContext
   *                 by using the operation identifier corresponding to the
   *                 request message, then at point (2) in the message
   *                 processing model, the runtime must call validateRequest on
   *                 the ClientAuthContext, and at point (3) the runtime must
   *                 call secureResponse on the ServerAuthContext.
   *
   *
   */
  public void SACRequestResponse() throws Fault {
    boolean verified = false;

    String args[] = {

        "TSServerAuthConfig.getAuthContext: returned non-null"
            + " ServerAuthContext for operationId=sayHelloProtected",

        "TSServerAuthContext.validateRequest called",

        "TSServerAuthContext.secureResponse called" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("SACRequestResponse failed");
    } else
      TestUtil.logMsg("SAC validateRequest and secureResponse called");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ClientAppContextId
   *
   * @assertion_ids: JASPIC:SPEC:208
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether for the client side appilcation context
   *                 Identifier is correctly used by the runtime.
   *
   *                 Description A Client application context Identifier must be
   *                 the String value composed by concatenating the client scope
   *                 identifier, a blank separator character, and the client
   *                 reference to the service. The clien scope identifier is not
   *                 testable but we can check the client reference to the
   *                 service.
   *
   */
  public void ClientAppContextId() throws Fault {
    boolean verified = false;
    String srchStrPrefix = "TSAuthConfigProvider.getClientAuthConfig called for "
        + "layer=SOAP : appContext=";

    String args[] = {

        "service/HelloService",

        "http://" + hostname + ":" + portnum + "/Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ClientAppContextId failed");
    } else
      TestUtil.logMsg("Found right ClientAppContextId");

  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ServerAppContextId
   *
   * @assertion_ids: JASPIC:SPEC:209
   *
   * @test_Strategy: 1. Register TSSV with the AppServer. (See User guide for
   *                 Registering TSSV with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify whether for the server side appilcation context
   *                 Identifier is correctly used by the runtime.
   *
   *                 Description A server application context Identifier shall
   *                 be the String value composed by concatenating a logical
   *                 hostname a blank separator character, and the path
   *                 component of the service endpoint URI corresponding to the
   *                 webservice.
   */
  public void ServerAppContextId() throws Fault {
    boolean verified = false;
    String srchStrPrefix = "TSAuthConfigProvider.getServerAuthConfig called for "
        + "layer=SOAP : appContext=";

    String args[] = {

        logicalHostName + " /Hello_web/Hello" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContainsOneOfSubString(args,
        srchStrPrefix);

    if (!verified) {
      throw new Fault("ServerAppContextId failed");
    } else
      TestUtil.logMsg("Found right ServerAppContextId");

  }

  public Object getJavaEEPort() throws Exception {
    TestUtil.logMsg("Get Hello Port from HelloService");
    Object port = service.getPort(Hello.class);
    return port;
  }

  public Object getStandAlonePort() throws Exception {
    URL wsdlurl = new URL(urlString + "?WSDL");
    return WebServiceUtils.getPort(wsdlurl, SERVICE_QNAME, HelloService.class,
        PORT_QNAME, Hello.class);
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: AuthConfigFactoryRegistration
   *
   * @assertion_ids: JASPIC:JAVADOC:80
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. Use the system properties to read the TestSuite
   *                 providers defined in ProviderConfigruation.xml file and
   *                 register them with vendor's authconfig factory.
   *
   *
   *                 Description
   *
   *
   */
  public void AuthConfigFactoryRegistration() throws Fault {
    boolean verified = false;

    verified = register(logFileLocation, providerConfigFileLocation,
        vendorACFClass);

    if (!verified) {
      throw new Fault("AuthConfigFactoryRegistration failed : ");
    } else
      TestUtil.logMsg("TestSuite Providers registration successful");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACFGetFactory
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:335; JASPIC:SPEC:7;
   *
   * @test_Strategy: This s mainly concerned with testing the runtimes handling
   *                 of ACF as follows: - get current (CTS) ACF - switch to use
   *                 different (CTS) ACF - verify calls to ACF use the
   *                 newer/expected ACF - restore original ACF
   */
  public void ACFGetFactory() throws Fault {
    try {
      commonTests._ACF_getFactory();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFGetFactory failed : ");
    }

    TestUtil.logMsg("ACFGetFactory passed.");

  }

  /**
   *
   * @keywords: jaspic_soap
   *
   * @testName: ACFSwitchFactorys
   *
   * @assertion_ids:
   *
   *
   * @test_Strategy: This test does the following: - gets current (CTS) factory
   *                 - sets the vendors ACF thus replacing the CTS ACF - verify
   *                 ACF's were correctly set - reset factory back to the
   *                 original CTS factory
   *
   *                 1. Use the static setFactory method to set an ACF and this
   *                 should always work. and use the getFactory to verify it
   *                 worked.
   *
   *                 Description
   *
   */
  public void ACFSwitchFactorys() throws Fault {
    try {
      commonTests._ACFSwitchFactorys(vendorACFClass);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFSwitchFactorys failed : ");
    }
    TestUtil.logMsg("ACFSwitchFactorys() passed");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: testACFComesFromSecFile
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:330;
   *
   * @test_Strategy: This is calling a method on the server(actually servlet)
   *                 side that will invoke getFactory() to verify a non-null
   *                 facotry instance is returned. It will also verify that the
   *                 authconfigprovider.factory security property is properly
   *                 set/used.
   *
   */
  public void testACFComesFromSecFile() throws Fault {
    try {
      commonTests._testACFComesFromSecFile();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("testACFComesFromSecFile failed : ");
    }
    TestUtil.logMsg("testACFComesFromSecFile() passed");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACFPersistentRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:329; JASPIC:SPEC:331; JASPIC:SPEC:332;
   *                 JASPIC:SPEC:340; JASPIC:SPEC:341;
   *
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register another
   *                 persistent ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - try to
   *                 re-register (persistently) standalone provider - verify 2nd
   *                 attempt at added standalone provider REPLACED the first but
   *                 it should NOT have added another nor failed. - also confirm
   *                 that regID for standalone ACP stayed the same after 1st and
   *                 2nd attempt to register standalone ACP - verify that the
   *                 2nd re-registering of ACP replaced the ACP AND the
   *                 description. - unregister standalone ACP and setFactory
   *                 back to CTS default
   *
   */
  public void ACFPersistentRegisterOnlyOneACP() throws Fault {
    try {
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFileLocation, vendorACFClass, true);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFPersistentRegisterOnlyOneACP failed : ");
    }
    TestUtil.logMsg("ACFPersistentRegisterOnlyOneACP() passed");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: ACFInMemoryRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:334; JASPIC:SPEC:342; JASPIC:SPEC:343;
   *
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register
   *                 (in-memory) ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - try to
   *                 re-register (in-memory) standalone provider - verify 2nd
   *                 attempt at added standalone provider REPLACED the first but
   *                 it should NOT have added another nor failed. - also confirm
   *                 that regID for standalone ACP stayed the same after 1st and
   *                 2nd attempt to register standalone ACP - verify that the
   *                 2nd re-registering of ACP replaced the ACP AND the
   *                 description. - unregister standalone ACP and setFactory
   *                 back to CTS default
   *
   */
  public void ACFInMemoryRegisterOnlyOneACP() throws Fault {
    try {
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFileLocation, vendorACFClass, false);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFInMemoryRegisterOnlyOneACP failed : ");
    }
    TestUtil.logMsg("ACFInMemoryRegisterOnlyOneACP() passed");
  }

  /**
   * @keywords: jaspic_soap
   * 
   * @testName: ACFUnregisterACP
   * 
   * @assertion_ids: JASPIC:SPEC:344;
   * 
   * @test_Strategy: This will make a server (actually servlet) side method call
   *                 that will do the following: - load vendors ACF -
   *                 (persistent) register of CTS ACP's in the vendors ACF - get
   *                 list of vendors registered provider ID's - register
   *                 (in-memory) ACP (this is standalone ACP profile) - verify
   *                 another regId was added for standalone ACP - unregister the
   *                 in-memory ACP we just registered - verify
   *                 removeRegistration() method call returned proper boolean -
   *                 verify expected # of registry eentries - verify 2nd call to
   *                 removeRegistration() with regId that was previously removed
   *                 and should expect return val of false
   *
   */
  public void ACFUnregisterACP() throws Fault {
    try {
      commonTests._ACFUnregisterACP(logFileLocation, providerConfigFileLocation,
          vendorACFClass);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFUnregisterACP failed : ");
    }
    TestUtil.logMsg("ACFUnregisterACP() passed");
  }

  /**
   *
   * @keywords: jaspic_soap
   *
   * @testName: ACFRemoveRegistrationWithBadId
   *
   * @assertion_ids: JASPIC:SPEC:345;
   *
   *
   * @test_Strategy: This test verifies we get a return value of False when
   *                 invoking ACF.removeRegistration(some_bad_id);
   *
   *                 1. Use the static setFactory method to get an ACF and then
   *                 attempt to invoke removeRegistration() with an invalid (ie
   *                 non-existant) regId. This should return False (per
   *                 javadoc).
   *
   *                 Description
   *
   */
  public void ACFRemoveRegistrationWithBadId() throws Fault {
    try {
      commonTests._ACFRemoveRegistrationWithBadId();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("ACFRemoveRegistrationWithBadId failed : ");
    }
    TestUtil.logMsg("ACFRemoveRegistrationWithBadId() passed");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: getRegistrationContextId
   *
   * @assertion_ids: JASPIC:JAVADOC:77
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. Use the system properties to read the TestSuite
   *                 providers defined in ProviderConfigruation.xml file and
   *                 register them with vendor's authconfig factory.
   *
   *
   *                 Description This will use an appContext value that was used
   *                 to register a provider, and it will see if it can use the
   *                 AuthConfigFactory.RegistrationContext API to try and access
   *                 the same appContext value that was used during the
   *                 registration process.
   *
   */
  public void getRegistrationContextId() throws Fault {
    boolean verified = false;
    String appContext = "localhost /Hello_web/Hello";

    // register providers in vendor factory
    verified = register(logFileLocation, providerConfigFileLocation,
        vendorACFClass);
    if (!verified) {
      throw new Fault("getRegistrationContextId failed : ");
    }

    // verify we can access a given provider (any provider) appcontext id
    boolean bVerified = false;

    AuthConfigFactory acf = null;

    try {
      acf = AuthConfigFactory.getFactory();
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory.";
      msg = msg + " Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();
    }

    if (acf == null) {
      throw new Fault("getRegistrationContextId failed : could not get acf");
    }

    String[] regIDs = acf.getRegistrationIDs(null);
    for (int ii = 0; ii < regIDs.length; ii++) {
      // loop through the ACF's registration ids

      if (regIDs[ii] != null) {
        AuthConfigFactory.RegistrationContext acfReg;
        acfReg = acf.getRegistrationContext(regIDs[ii]);
        if (acfReg != null) {
          TestUtil.logMsg("appContext = " + appContext);
          TestUtil.logMsg("acfReg.getAppContext() = " + acfReg.getAppContext());
          TestUtil.logMsg("layer = " + acfReg.getMessageLayer());
          String str = acfReg.getAppContext();
          if ((str != null) && (str.equals(appContext))) {
            // we found our provider info
            TestUtil
                .logMsg("Found it : RegistrationID for our ACP=" + regIDs[ii]);
            bVerified = true;
            break;
          }
        }
      }
    }

    if (!bVerified) {
      String msg = "Could not find appContext=" + appContext;
      msg += " in the ACF's list of registration id info";
      TestUtil.logMsg(msg);
      throw new Fault("getRegistrationContextId failed : ");
    }

    TestUtil.logMsg("TestSuite Providers registration successful");
  }

  /**
   * @keywords: jaspic_soap
   *
   * @testName: AuthConfigFactoryVerifyPersistence
   *
   * @assertion_ids: JASPIC:JAVADOC:75
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. Load vendor's AuthConfigFactory and make sure the
   *                 registered providers return properly for the right message
   *                 layer and appContextId
   *
   *                 Note: We test the persistance behaviour for vendor's
   *                 AuthConfigFactory by registering providers from a persisted
   *                 file, then we verify the registrations went correctly.
   *
   *                 Description
   *
   *
   */
  public void AuthConfigFactoryVerifyPersistence() throws Fault {

    boolean verified = false;

    try {

      // register providers in vendor factory
      verified = register(logFileLocation, providerConfigFileLocation,
          vendorACFClass);
      if (!verified) {
        throw new Fault("getRegistrationContextId failed : ");
      }

      // Get system default AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        TestUtil.logMsg("Default AuthConfigFactory class name = "
            + acf.getClass().getName());

        verified = verifyRegistrations(acf);

      } else {
        TestUtil.logErr("Default AuthConfigFactory is null"
            + " can't verify registrations for TestSuite Providers");
      }
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory.";
      msg = msg + " Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!verified) {
      throw new Fault("AuthConfigFactoryPersistence failed : ");
    } else
      TestUtil.logMsg("AuthConfigFactory Persistence works");
  }

  public boolean register(String logFileLocation,
      String providerConfigFileLocation, String vendorACFClass) {

    try {

      printVerticalIndent();

      // Get an instance of Vendor's AuthConfigFactory
      AuthConfigFactory vFactory = getVendorAuthConfigFactory(vendorACFClass);

      // Set vendor's AuthConfigFactory
      AuthConfigFactory.setFactory(vFactory);

      // Get system default AuthConfigFactory
      AuthConfigFactory acf = AuthConfigFactory.getFactory();

      if (acf != null) {
        TestUtil.logMsg("Default AuthConfigFactory class name = "
            + acf.getClass().getName());

      } else {
        TestUtil.logErr("Default AuthConfigFactory is null"
            + " can't register TestSuite Providers with null");
        // System.exit(1);
        return false;
      }

      /**
       * Read the ProviderConfiguration XML file This file contains the list of
       * providers that needs to be loaded by the vendor's default
       * AuthConfigFactory
       */
      providerConfigurationEntriesCollection = readProviderConfigurationXMLFile();

      ProviderConfigurationEntry pce = null;

      printVerticalIndent();
      Iterator iterator = providerConfigurationEntriesCollection.iterator();
      while (iterator.hasNext()) {
        // obtain each ProviderConfigurationEntry and register it
        // with vendor's default AuthConfigFactory
        pce = (ProviderConfigurationEntry) iterator.next();

        if (pce != null) {
          TestUtil.logMsg(
              "Registering Provider " + pce.getProviderClassName() + " ...");
          acf.registerConfigProvider(pce.getProviderClassName(),
              pce.getProperties(), pce.getMessageLayer(),
              pce.getApplicationContextId(), pce.getRegistrationDescription());
          TestUtil.logMsg("Registration Successful");
        }
      }
      printVerticalIndent();

      // Check whether the providers are registered for the right message layer
      // and appContext
      // verifyRegistrations(acf);

    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();

    } catch (Exception e) {
      TestUtil.logMsg("Error Registering TestSuite AuthConfig Providers");
      e.printStackTrace();
    }

    return true;

  }

  /**
   * This method instantiates and ruturns a AuthConfigFactory based on the
   * specified className
   */
  private AuthConfigFactory getVendorAuthConfigFactory(String className) {

    AuthConfigFactory vFactory = null;

    if (className != null) {
      try {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Class clazz = Class.forName(className, true, loader);

        vFactory = (AuthConfigFactory) clazz.newInstance();
        TestUtil.logMsg("Instantiated Vendor's AuthConfigFactory");

      } catch (Exception e) {
        TestUtil.logMsg("Error instantiating vendor's "
            + "AuthConfigFactory class :" + className);
        e.printStackTrace();

      }
    }

    return vFactory;
  }

  private void printVerticalIndent() {
    TestUtil
        .logMsg("**********************************************************");
    TestUtil.logMsg("\n");

  }

  private boolean verifyRegistrations(AuthConfigFactory acf) {
    TestUtil.logMsg("Verifying Provider Registrations ...");

    try {

      // create a Registration listener
      RegistrationListener rlis = new TSRegistrationListener();

      // Get AuthConfigProvider for soap layer
      AuthConfigProvider acp = acf.getConfigProvider(JASPICData.LAYER_SOAP,
          soapAppContext, rlis);

      if (acp != null) {
        if (acp.getClass().getName().equals(
            "com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigProvider")) {
          TestUtil.logMsg("TSAuthConfigProvider registered for"
              + " message layer=SOAP" + " and appContextId=" + soapAppContext);
        } else {
          TestUtil.logMsg("Wrong provider registerd for "
              + " message layer=SOAP" + " and appContextId=" + soapAppContext);
          return false;

        }

      } else {
        TestUtil.logMsg("Error : No AuthConfigprovider registerd for"
            + " message layer=SOAP" + " and appContextId=" + soapAppContext);
        return false;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;

  }

  /*
   * Read the provider configuration XML file and registers each provider with
   * Vendor's default AuthConfigFactory
   */
  private Collection readProviderConfigurationXMLFile() {
    Collection providerConfigurationEntriesCollection = null;

    TestUtil.logMsg(
        "Reading TestSuite Providers from :" + providerConfigFileLocation);
    try {
      // Given the provider configuration xml file
      // This reader parses the xml file and stores the configuration
      // entries as a collection.
      configFileProcessor = new ProviderConfigurationXMLFileProcessor(
          providerConfigFileLocation);

      // Retrieve the ProviderConfigurationEntries collection
      providerConfigurationEntriesCollection = configFileProcessor
          .getProviderConfigurationEntriesCollection();

      TestUtil.logMsg("TestSuite Providers read successfully "
          + "from ProviderConfiguration.xml file");

      return providerConfigurationEntriesCollection;

    } catch (Exception e) {
      TestUtil.logMsg("Error loading Providers");
      e.printStackTrace();
    }
    return null;

  }

  /*
   * This is used to make sure we have the correct Factory class being used.
   *
   */
  private void setFactoryClass(String acfClass) {

    try {
      AuthConfigFactory acf = AuthConfigFactory.getFactory();
      Object acfObj = (Object) acf;
      if (!(acfObj instanceof com.sun.ts.tests.jaspic.tssv.config.TSAuthConfigFactory)) {
        if (acf != null) {
          String curClass = acf.getClass().getName();

          TestUtil.logMsg("Changing factory to class: " + acfClass
              + " from class: " + curClass);
        }
        AuthConfigFactory correctAcf = new TSAuthConfigFactory();
        AuthConfigFactory.setFactory(correctAcf);
      }
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();
    }
  }

  /**
   *
   * @keywords: jaspic_soap
   *
   * @testName: AuthConfigFactorySetFactory
   *
   * @assertion_ids: JASPIC:SPEC:7; JASPIC:SPEC:10; JASPIC:JAVADOC:87;
   *                 JASPIC:JAVADOC:80
   *
   * @test_Strategy: 1. Use the static setFactory method to set an ACF and this
   *                 should always work.
   *
   *                 Description This method uses the getFactory() method to get
   *                 the current factory, then it uses the setFactory() to
   *                 change the current ACF. This method then verifies that the
   *                 ACF's were indeed changed. We know that the setFactory()
   *                 works as it is used in the bootstrap process - but this is
   *                 an additional level of testing that allows us to set the
   *                 factory in a slightly different manner than the bootstrap
   *                 (eg reading it out of the java.security file.
   */
  public void AuthConfigFactorySetFactory() throws Fault {
    try {
      // get current AuthConfigFactory
      AuthConfigFactory currentAcf = AuthConfigFactory.getFactory();
      if (currentAcf == null) {
        TestUtil.logMsg("FAILURE - Could not get current AuthConfigFactory.");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      }
      String currentACFClass = currentAcf.getClass().getName();
      TestUtil.logMsg("currentACFClass = " + currentACFClass);

      // set our ACF to a new/different AuthConfigFactory
      TSAuthConfigFactoryForStandalone newAcf = new TSAuthConfigFactoryForStandalone();
      AuthConfigFactory.setFactory(newAcf);
      String newACFClass = newAcf.getClass().getName();
      TestUtil.logMsg("newACFClass = " + newACFClass);

      // verify our calls to getFactory() are getting the newly set factory
      // instance and not the original ACF instance
      AuthConfigFactory testAcf = AuthConfigFactory.getFactory();
      if (testAcf == null) {
        TestUtil.logMsg("FAILURE - Could not get newly set AuthConfigFactory.");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      }
      String newlySetACFClass = testAcf.getClass().getName();
      TestUtil.logMsg("newlySetACFClass = " + newlySetACFClass);

      // Verify ACF's were set correctly
      if (!newlySetACFClass.equals(newACFClass)) {
        TestUtil.logMsg(
            "FAILURE - our current ACF does not match our newly set ACF");
        throw new Fault("AuthConfigFactorySetFactory : FAILED");
      } else {
        String msg = "newlySetACFClass == newACFClass == " + newACFClass;
        TestUtil.logMsg(msg);
      }

      // restore original factory class
      AuthConfigFactory.setFactory(currentAcf);

      TestUtil.logMsg("AuthConfigFactorySetFactory : PASSED");
    } catch (SecurityException ex) {
      // if here we may not have permission to invoke ACF.getFactory...
      String msg = "SecurityException:  make sure you have permission to call ACF.getFactory";
      msg = msg
          + "or ACF.setFactory().  Check your server side security policies.";
      TestUtil.logMsg(msg);
      ex.printStackTrace();
    }
  }

}
