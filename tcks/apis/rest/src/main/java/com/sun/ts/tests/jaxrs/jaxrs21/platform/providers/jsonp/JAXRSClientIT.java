/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.platform.providers.jsonp;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import java.io.IOException;

import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.ws.rs.core.MediaType;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * @since 2.1
 */
@ExtendWith(ArquillianExtension.class)
@Tag("jaxrs")
@Tag("platform")
@Tag("web")
public class JAXRSClientIT extends JaxrsCommonClient {

  private static final long serialVersionUID = 21L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_jaxrs21_platform_providers_jsonp_web/resource");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_jaxrs21_platform_providers_jsonp_web.war");
    archive.addClasses(Resource.class, TSAppConfig.class);
    archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "web.xml.template", "web.xml"); //can use if the web.xml.template doesn't need to be modified.    
    return archive;

  }


  // public static void main(String[] args) {
  //   new JAXRSClient().run(args);
  // }

  /*
   * @testName: serverJsonStringReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:129;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonStringReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tostring"));
    setProperty(Property.SEARCH_STRING, Resource.MESSAGE);
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    System.out.println(TEST_PROPS.get(Property.REQUEST_HEADERS));
    invoke();
  }

  // @Override
  // protected com.sun.javatest.Status run(String[] args) {
  //   if (args.length == 0)
  //     args = new String[] { "-p", "install/jaxrs/bin/ts.jte", "-t",
  //         "serverJsonStringReturnTest", "-vehicle", "standalone" };
  //   return super.run(args);
  // }

  /*
   * @testName: serverJsonNumberReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:129;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonNumberReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tonumber"));
    setProperty(Property.SEARCH_STRING, String.valueOf(Long.MIN_VALUE));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonStringArgumentTest
   * 
   * @assertion_ids: JAXRS:SPEC:129;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonStringArgumentTest() throws Exception {
    String entity = getClass().getName();
    JsonString json = Json.createValue(entity);
    setRequestContentEntity(json);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "fromstring"));
    setProperty(Property.REQUEST_HEADERS, buildContentType(getJsonType()));
    setProperty(Property.SEARCH_STRING, entity);
    invoke();
  }

  /*
   * @testName: serverJsonNumberArgumentTest
   * 
   * @assertion_ids: JAXRS:SPEC:129;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonNumberArgumentTest() throws Exception {
    JsonNumber number = Json.createValue(Long.MIN_VALUE);
    setRequestContentEntity(number);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "fromnumber"));
    setProperty(Property.REQUEST_HEADERS, buildContentType(getJsonType()));
    setProperty(Property.SEARCH_STRING, String.valueOf(Long.MIN_VALUE));
    invoke();
  }

  // ////////////////////////////////////////////////////////////////////////
  private static MediaType getJsonType() {
    return MediaType.APPLICATION_JSON_TYPE;
  }

}
