/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.provider.jsonp;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.provider.PrintingErrorHandler;

import jakarta.ws.rs.core.MediaType;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.api.Deployment;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;


/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
@ExtendWith(ArquillianExtension.class)
public class JAXRSClientIT extends JaxrsCommonClient {

  private static final long serialVersionUID = 7441792527287072853L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_platform_provider_jsonp_web/resource");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    InputStream inStream = JAXRSClientIT.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/platform/provider/jsonp/web.xml.template");
    String webXml = editWebXmlString(inStream);

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_provider_jsonp_web.war");
    archive.addClasses(
      TSAppConfig.class, 
      Resource.class, 
      PrintingErrorHandler.class
      );

    archive.setWebXML(new StringAsset(webXml));
    return archive;
  }


  /*
   * @testName: serverJsonArrayReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:107;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonArray
   */
  @Test
  public void serverJsonArrayReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "toarray"));
    bufferEntity(true);
    invoke();
    assertResponseContainsRow(0);
    assertResponseContainsRow(1);
  }

  /*
   * @testName: serverJsonStructureReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:33; JAXRS:SPEC:33.1; JAXRS:SPEC:75;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonStructure
   */
  @Test
  public void serverJsonStructureReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "tostructure"));
    bufferEntity(true);
    invoke();
    assertResponseContainsRow(0);
    assertResponseContainsRow(1);
  }

  /*
   * @testName: serverJsonObjectReturnTest
   * 
   * @assertion_ids: JAXRS:SPEC:107;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonObject
   */
  @Test
  public void serverJsonObjectReturnTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "toobject"));
    bufferEntity(true);
    invoke();
    assertResponseContainsRow(0);
  }

  /*
   * @testName: serverJsonArrayArgumentTest
   * 
   * @assertion_ids: JAXRS:SPEC:107;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonArray
   */
  @Test
  public void serverJsonArrayArgumentTest() throws Exception {
    String entity = Resource.createArray().toString();
    setProperty(Property.REQUEST, buildRequest(Request.POST, "fromarray"));
    setProperty(Property.REQUEST_HEADERS, buildContentType(getJsonType()));
    setProperty(Property.CONTENT, entity);
    setProperty(Property.SEARCH_STRING, entity);
    invoke();
  }

  /*
   * @testName: serverJsonStructureArgumentTest
   * 
   * @assertion_ids: JAXRS:SPEC:107;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonStructure
   */
  @Test
  public void serverJsonStructureArgumentTest() throws Exception {
    String entity = Resource.createArray().toString();
    setProperty(Property.REQUEST, buildRequest(Request.POST, "fromstructure"));
    setProperty(Property.REQUEST_HEADERS, buildContentType(getJsonType()));
    setProperty(Property.CONTENT, entity);
    setProperty(Property.SEARCH_STRING, entity);
    invoke();
  }

  /*
   * @testName: serverJsonObjectArgumentTest
   * 
   * @assertion_ids: JAXRS:SPEC:107;
   * 
   * @test_Strategy: implementations MUST support entity providers for the
   * following types: JsonObject
   */
  @Test
  public void serverJsonObjectArgumentTest() throws Exception {
    String entity = Resource.createObject(1).toString();
    setProperty(Property.REQUEST, buildRequest(Request.POST, "fromobject"));
    setProperty(Property.REQUEST_HEADERS, buildContentType(getJsonType()));
    setProperty(Property.CONTENT, entity);
    setProperty(Property.SEARCH_STRING, entity);
    invoke();
  }

  // ////////////////////////////////////////////////////////////////////////
  private void assertResponseContainsRow(int id) throws Exception {
    String content = getResponseBody();
    assertContains(content, Resource.LABEL[0]);
    assertContains(content, Resource.TYPE[id]);
    assertContains(content, Resource.LABEL[1]);
    assertContains(content, Resource.PHONE[id]);
    logMsg("Found", Resource.createObject(id).toString(), "as expected");
  }

  private static void assertContains(String where, String what) throws Exception {
    assertContains(where, what, what, "has not been found in", where);
  }

  private static MediaType getJsonType() {
    return new MediaType("application", "json");
  }

}
