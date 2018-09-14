/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.client.client;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 7355465562476492891L;

  public JAXRSClient() {
    setClientAndWebTarget();
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  protected transient WebTarget target;

  protected transient Client client;

  /* Run test */

  /*
   * @testName: closeOnClientConfigTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientConfigTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "getConfiguration");
  }

  /*
   * @testName: closeOnClientInvocationWithLinkTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientInvocationWithLinkTest() throws Fault {
    client.close();
    Link link = Link.fromUri("cts").build();
    assertException(IllegalStateException.class, client, "invocation", link);
  }

  /*
   * @testName: closeOnClientTargetWithLinkTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientTargetWithLinkTest() throws Fault {
    client.close();
    Link link = Link.fromUri("cts").build();
    assertException(IllegalStateException.class, client, "target", link);
  }

  /*
   * @testName: closeOnClientTargetWithStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientTargetWithStringTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "target", "cts");
  }

  /*
   * @testName: closeOnClientTargetWithUriTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientTargetWithUriTest() throws Fault {
    client.close();
    URI uri = URI.create("cts");
    assertException(IllegalStateException.class, client, "target", uri);
  }

  /*
   * @testName: closeOnClientRegisterClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterClassTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        StringBeanEntityProvider.class);
  }

  /*
   * @testName: closeOnClientRegisterObjectTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterObjectTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        new StringBeanEntityProvider());
  }

  /*
   * @testName: closeOnClientRegisterClassWithContractsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterClassWithContractsTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        StringBeanEntityProvider.class,
        new Class[] { MessageBodyWriter.class });
  }

  /*
   * @testName: closeOnClientRegisterClassWithPriorityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterClassWithPriorityTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        StringBeanEntityProvider.class, 100);
  }

  /*
   * @testName: closeOnClientRegisterClassMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterClassMapTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, client, "register",
        StringBeanEntityProvider.class, contracts);
  }

  /*
   * @testName: closeOnClientRegisterObjectWithContractsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterObjectWithContractsTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        new StringBeanEntityProvider(),
        new Class[] { MessageBodyReader.class });
  }

  /*
   * @testName: closeOnClientRegisterObjectWithPriorityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterObjectWithPriorityTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, client, "register",
        new StringBeanEntityProvider(), 100);
  }

  /*
   * @testName: closeOnClientRegisterObjectWithMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientRegisterObjectWithMapTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, client, "register",
        new StringBeanEntityProvider(), contracts);
  }

  /*
   * @testName: closeOnClientPropertyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientPropertyTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, client, "property", "A", "B");
  }

  /*
   * @testName: closeOnClientTargetWithUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalTStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnClientTargetWithUriBuilderTest() throws Fault {
    client.close();
    Link link = Link.fromUri("cts").build();
    UriBuilder builder = UriBuilder.fromUri(link.getUri());
    assertException(IllegalStateException.class, client, "target", builder);
  }

  /*
   * @testName: closeOnWebTargetConfigTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetConfigTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "getConfiguration");
  }

  /*
   * @testName: closeOnWebTargetGetUriTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetGetUriTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "getUri");
  }

  /*
   * @testName: closeOnWebTargetGetUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetGetUriBuilderTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "getUriBuilder");
  }

  /*
   * @testName: closeOnWebTargetMatrixParamTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetMatrixParamTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "matrixParam", "cts",
        new Object[] { "tck" });
  }

  /*
   * @testName: closeOnWebTargetPathTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetPathTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "path", "cts");
  }

  /*
   * @testName: closeOnWebTargetQueryParamTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetQueryParamTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "queryParam", "cts",
        new Object[] { "tck" });
  }

  /*
   * @testName: closeOnWebTargetRegisterClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterClassTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        StringBeanEntityProvider.class);
  }

  /*
   * @testName: closeOnWebTargetRegisterObjectTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterObjectTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        new StringBeanEntityProvider());
  }

  /*
   * @testName: closeOnWebTargetRegisterClassWithContractsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterClassWithContractsTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        StringBeanEntityProvider.class,
        new Class[] { MessageBodyWriter.class });
  }

  /*
   * @testName: closeOnWebTargetRegisterClassWithPriorityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterClassWithPriorityTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        StringBeanEntityProvider.class, 100);
  }

  /*
   * @testName: closeOnWebTargetRegisterClassMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterClassMapTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, target, "register",
        StringBeanEntityProvider.class, contracts);
  }

  /*
   * @testName: closeOnWebTargetRegisterObjectWithContractsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterObjectWithContractsTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        new StringBeanEntityProvider(),
        new Class[] { MessageBodyReader.class });
  }

  /*
   * @testName: closeOnWebTargetRegisterObjectWithPriorityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterObjectWithPriorityTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "register",
        new StringBeanEntityProvider(), 100);
  }

  /*
   * @testName: closeOnWebTargetRegisterObjectWithMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRegisterObjectWithMapTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, target, "register",
        new StringBeanEntityProvider(), contracts);
  }

  /*
   * @testName: closeOnWebTargetRequestTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRequestTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "request");
  }

  /*
   * @testName: closeOnWebTargetRequestWithMediaTypeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRequestWithMediaTypeTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "request",
        new Object[] { new MediaType[] { MediaType.APPLICATION_XML_TYPE } });
  }

  /*
   * @testName: closeOnWebTargetRequestWithMediaTypeNameTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetRequestWithMediaTypeNameTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "request",
        new Object[] { new String[] { MediaType.APPLICATION_XML } });
  }

  /*
   * @testName: closeOnWebTargetResolveTemplateStringObjectTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplateStringObjectTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "resolveTemplate",
        "name", "value");
  }

  /*
   * @testName: closeOnWebTargetResolveTemplateStringObjectBooleanTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplateStringObjectBooleanTest()
      throws Fault {
    client.close();
    assertException(IllegalStateException.class, target, "resolveTemplate",
        "name", "value", true);
  }

  /*
   * @testName: closeOnWebTargetResolveTemplateFromEncodedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplateFromEncodedTest() throws Fault {
    client.close();
    assertException(IllegalStateException.class, target,
        "resolveTemplateFromEncoded", "name", "value");
  }

  /*
   * @testName: closeOnWebTargetResolveTemplatesMapTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplatesMapTest() throws Fault {
    client.close();
    Map<String, Object> map = new HashMap<String, Object>();
    assertException(IllegalStateException.class, target, "resolveTemplates",
        map);
  }

  /*
   * @testName: closeOnWebTargetResolveTemplatesMapBooleanTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplatesMapBooleanTest() throws Fault {
    client.close();
    Map<String, Object> map = new HashMap<String, Object>();
    assertException(IllegalStateException.class, target, "resolveTemplates",
        map, true);
  }

  /*
   * @testName: closeOnWebTargetPropertyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetPropertyTest() throws Fault {
    client.close();
    Map<Class<?>, Integer> contracts = new HashMap<Class<?>, Integer>();
    contracts.put(MessageBodyReader.class, 100);
    assertException(IllegalStateException.class, target, "property", "A", "B");
  }

  /*
   * @testName: closeOnWebTargetResolveTemplatesFromEncodedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:409;
   * 
   * @test_Strategy: Close client instance and all it's associated resources.
   * Subsequent calls have no effect and are ignored. Once the client is closed,
   * invoking any other method on the client instance would result in an
   * IllegalStateException being thrown. Calling this method effectively
   * invalidates all WebTarget resource targets produced by the client instance.
   * Invoking any method on such targets once the client is closed would result
   * in an IllegalStateException being thrown.
   */
  public void closeOnWebTargetResolveTemplatesFromEncodedTest() throws Fault {
    client.close();
    Map<String, Object> map = new HashMap<String, Object>();
    assertException(IllegalStateException.class, target,
        "resolveTemplatesFromEncoded", map);
  }

  /*
   * @testName: invocationFromLinkExceptionNoLinkTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:411;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.invocation( Link ) throws
   * NullPointerException in case argument is null.
   */
  public void invocationFromLinkExceptionNoLinkTest() throws Fault {
    String exceptionMessage = "NullPointerException successfully thrown when no link";
    String noExceptionMessage = "NullPointerException not thrown when no link";
    checkInvocationException(null, NullPointerException.class, exceptionMessage,
        noExceptionMessage);
  }

  /*
   * @testName: targetStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:413;
   * 
   * @test_Strategy: Build a new web resource target.
   */
  public void targetStringTest() throws Fault {
    // setClientAndWebTarget is called in constructor
    assertFault(target != null, "WebTarget is null");
  }

  /*
   * @testName: targetWithStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:413;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.target( String ) throws
   * IllegalArgumentException in case the supplied string is not a valid URI
   * template.
   */
  public void targetWithStringIllegalArgumentExceptionTest() throws Fault {
    String sWebTarget = ":cts:8080//tck:90090//jaxrs ";
    try {
      new URI(sWebTarget);
      throw new Fault("URI(" + sWebTarget + ") is valid");
    } catch (URISyntaxException e1) {
      logMsg("Uri is not a valid URI as expected:", e1);
    }
    try {
      target = client.target(sWebTarget);
      throw new Fault(
          "IllegalArgumentException was not thrown for target " + sWebTarget);
    } catch (IllegalArgumentException e) {
      TestUtil
          .logMsg("IllegalArgumentException was successfully thrown for target "
              + sWebTarget + " as expected");
    }
  }

  /*
   * @testName: targetWithStringNullPointerExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:413;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.target( String ) throws throws
   * NullPointerException in case the supplied argument is null.
   */
  public void targetWithStringNullPointerExceptionTest() throws Fault {
    String sWebTarget = null;
    try {
      target = client.target(sWebTarget);
      throw new Fault("NullPointerException was not thrown for null target");
    } catch (NullPointerException e) {
      TestUtil.logMsg(
          "NullPointerException was successfully thrown for null target as expected");
    }
  }

  /*
   * @testName: targetUriTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:416;
   * 
   * @test_Strategy: Build a new web resource target.
   */
  public void targetUriTest() throws Fault {
    URI uri = URI.create(getUrl("call"));
    target = client.target(uri);
    assertFault(target != null, "WebTarget is null");
  }

  /*
   * @testName: targetWithUriNullPointerExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:416;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.target( URI ) throws throws
   * NullPointerException in case the supplied argument is null.
   */
  public void targetWithUriNullPointerExceptionTest() throws Fault {
    URI uri = null;
    try {
      target = client.target(uri);
      throw new Fault("NullPointerException was not thrown for null target");
    } catch (NullPointerException e) {
      TestUtil.logMsg(
          "NullPointerException was successfully thrown for null target as expected");
    }
  }

  /*
   * @testName: targetUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:418;
   * 
   * @test_Strategy: Build a new web resource target.
   */
  public void targetUriBuilderTest() throws Fault {
    UriBuilder builder = UriBuilder.fromUri(getUrl("call"));
    target = client.target(builder);
    assertFault(target != null, "WebTarget is null");
  }

  /*
   * @testName: targetWithUriBuilderNullPointerExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:418;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.target( URI ) throws throws
   * NullPointerException in case the supplied argument is null.
   */
  public void targetWithUriBuilderNullPointerExceptionTest() throws Fault {
    UriBuilder builder = null;
    try {
      target = client.target(builder);
      throw new Fault("NullPointerException was not thrown for null target");
    } catch (NullPointerException e) {
      TestUtil.logMsg(
          "NullPointerException was successfully thrown for null target as expected");
    }
  }

  /*
   * @testName: targetLinkTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:420;
   * 
   * @test_Strategy: Build a new web resource target.
   */
  public void targetLinkTest() throws Fault {
    URI uri = UriBuilder.fromPath(getUrl("call")).build();
    Link link = Link.fromUri(uri).build();
    target = client.target(link);
    assertFault(target != null, "WebTarget is null");
  }

  /*
   * @testName: targetWithLinkNullPointerExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:420;
   * 
   * @test_Strategy: javax.ws.rs.client.Client.target( URI ) throws throws
   * NullPointerException in case the supplied argument is null.
   */
  public void targetWithLinkNullPointerExceptionTest() throws Fault {
    Link link = null;
    try {
      target = client.target(link);
      throw new Fault("NullPointerException was not thrown for null target");
    } catch (NullPointerException e) {
      TestUtil.logMsg(
          "NullPointerException was successfully thrown for null target as expected");
    }
  }

  // //////////////////////////////////////////////////////////////////////
  /** Check exception when calling Client#invocation() and log */
  protected static <T extends Exception> void checkInvocationException(
      Link link, Class<T> exception, String messageOnException,
      String messageNoException) throws Fault {
    Client client = ClientBuilder.newClient();
    try {
      client.invocation(link);
      throw new Fault(messageNoException);
    } catch (Exception e) {
      if (exception.isInstance(e))
        TestUtil.logMsg(messageOnException);
      else
        throw new Fault(e);
    }
  }

  protected void setClientAndWebTarget() {
    client = ClientBuilder.newClient();
    target = client.target("cts");
  }

  protected static void assertException(Class<? extends Exception> exception,
      Object object, String method, Object... args) throws Fault {

    Method m = getMethodByName(object.getClass(), method, args);
    assertFault(m != null, "No method", method, "for object",
        object.getClass().getName(), "found");
    System.out.println(m);
    try {
      m.invoke(object, args);
      assertFault(false, "Method", method, "did not throw",
          exception.getSimpleName());
    } catch (Exception e) {
      if (e.getCause() == null
          || !exception.isAssignableFrom(e.getCause().getClass()))
        throw new Fault(e);
      logMsg(exception.getName(), "has been successfully thrown", e.getCause());
    }
  }

  protected static Method getMethodByName(Class<?> clazz, String name,
      Object... args) {
    Method[] methods = clazz.getMethods();
    for (Method m : methods)
      if (m.getName().equals(name) && fitsMethodArguments(m, args))
        return m;
    return null;
  }

  protected static boolean fitsMethodArguments(Method method, Object... args) {
    if (method.getParameterTypes().length != args.length)
      return false;
    Class<?>[] argClass = method.getParameterTypes();
    for (int i = 0; i != argClass.length; i++) {
      if (args[i].getClass() == Class.class
          && argClass[i].getClass() != Class.class)
        return false;
      if (!argClass[i].isPrimitive()
          && !argClass[i].isAssignableFrom(args[i].getClass()))
        return false;
      if (argClass[i].isPrimitive()
          && (!(args[i] instanceof Number || args[i] instanceof Boolean)))
        return false;
    }
    return true;
  }

  protected String getUrl(String method) {
    StringBuilder url = new StringBuilder();
    url.append("http://").append(_hostname).append(":").append(_port);
    url.append("/").append(method);
    return url.toString();
  }
}
