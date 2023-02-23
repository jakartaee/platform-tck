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

package com.sun.ts.tests.jaxrs.jaxrs21.platform.providers.jsonb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.IOException;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.core.MediaType;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;


/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 * 
 */
/**
 * @since 2.1
 */
@ExtendWith(ArquillianExtension.class)
public class JAXRSClientIT extends JaxrsCommonClient {

  private static final long serialVersionUID = 21L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_jaxrs21_platform_providers_jsonb_web/resource");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    //TestUtil.logMsg("STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    //TestUtil.logMsg("FINISHED TEST : "+testInfo.getDisplayName());
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    //InputStream inStream = JAXRSClient.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/jaxrs21/platform/providers/jsonb/web.xml.template");
    // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
    //String webXml = editWebXmlString(inStream);

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_jaxrs21_platform_providers_jsonb_web.war");
    archive.addClasses(Resource.class, TSAppConfig.class);
    //archive.setWebXML(new StringAsset(webXml));
    archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "web.xml.template", "web.xml"); //can use if the web.xml.template doesn't need to be modified.    
    
    return archive;
  }


  /*
   * @testName: serverJsonBStringReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy: String is defined in 4.2.4 to have a MBW<String>, whereas
   * JSONB has MBW<Object> for which it fails to be chosen and String MBW is
   * used. This is intentional for JAXRS spec not to allow other MBW to send
   * Strings
   */
  @Test
  public void serverJsonBStringReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tostring"));
    setProperty(Property.SEARCH_STRING, Resource.MESSAGE);
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBCharReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy: For CHAR and other than String data types, JSONB MBW is
   * used, since unlike String, they have text/plain MBW in 4.2.4.
   */
  @Test
  public void serverJsonBCharReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tochar"));
    setProperty(Property.SEARCH_STRING,
        jsonb.toJson(Resource.MESSAGE.charAt(0)));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBByteReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBByteReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tobyte"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(Byte.MAX_VALUE));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBShortReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBShortReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "toshort"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(Short.MAX_VALUE));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBIntReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBIntReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "toint"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(Integer.MAX_VALUE));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBLongReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBLongReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tolong"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(Long.MAX_VALUE));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBNumberReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBNumberReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tonumber"));
    setProperty(Property.SEARCH_STRING,
        jsonb.toJson(BigDecimal.valueOf(Long.MAX_VALUE)));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBBigIntegerReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBBigIntegerReturnTest() throws Exception {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tobiginteger"));
    setProperty(Property.SEARCH_STRING,
        jsonb.toJson(BigInteger.valueOf(Long.MAX_VALUE)));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBURIReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBURIReturnTest()
      throws Exception, JsonbException, URISyntaxException {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "touri"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(new URI(Resource.URL)));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  /*
   * @testName: serverJsonBURLReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:130;
   * 
   * @test_Strategy:
   */
  @Test
  public void serverJsonBURLReturnTest()
      throws Exception, JsonbException, MalformedURLException {
    Jsonb jsonb = JsonbBuilder.create();
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tourl"));
    setProperty(Property.SEARCH_STRING, jsonb.toJson(new URL(Resource.URL)));
    setProperty(Property.REQUEST_HEADERS, buildAccept(getJsonType()));
    invoke();
  }

  // ////////////////////////////////////////////////////////////////////////
  private static MediaType getJsonType() {
    return MediaType.APPLICATION_JSON_TYPE;
  }

}
