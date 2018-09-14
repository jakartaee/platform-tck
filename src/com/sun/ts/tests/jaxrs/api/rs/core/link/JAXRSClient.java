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

package com.sun.ts.tests.jaxrs.api.rs.core.link;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -373684847397542733L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:799;
   * 
   * @test_Strategy: see what happens when new Link() is used.
   */
  public void constructorTest() throws Fault {
    Link link = new Link() {

      @Override
      public URI getUri() {
        return null;
      }

      @Override
      public UriBuilder getUriBuilder() {
        return null;
      }

      @Override
      public String getRel() {
        return null;
      }

      @Override
      public List<String> getRels() {
        return null;
      }

      @Override
      public String getTitle() {
        return null;
      }

      @Override
      public String getType() {
        return null;
      }

      @Override
      public Map<String, String> getParams() {
        return null;
      }

      @Override
      public String toString() {
        return "";
      }
    };
    // check no Exception is thrown
    assertFault(link != null, "new Link() is null");
    logMsg("new Link() call iss successfull", link.toString());
  }

  /*
   * @testName: fromMethodTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1037;
   * 
   * @test_Strategy: Convenience method to build a link from a resource method.
   * Note that path created from resource is relative to the application's root
   * resource.
   */
  public void fromMethodTest() throws Fault {
    Link link = linkFromResource("consumesAppJson");
    String resource = link.toString();
    assertContains(resource, "<consumesappjson>");
    logMsg("Link", resource, "has been created");
  }

  /*
   * @testName: fromResourceMethodLinkUsedInInvocationTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:787;
   * 
   * @test_Strategy: Generate a link by introspecting a resource method.
   */
  public void fromResourceMethodLinkUsedInInvocationTest() throws Fault {
    final String linkName = "link";
    Client client = ClientBuilder.newClient();
    client.register(new ClientRequestFilter() {
      @Override
      public void filter(ClientRequestContext ctx) throws IOException {
        String uri = ctx.getUri().toASCIIString();
        Link.Builder builder = Link
            .fromMethod(Resource.class, "consumesAppJson").rel(linkName);
        Link link = builder.build();
        Response response = Response.ok(uri).links(link).build();
        ctx.abortWith(response);
      }
    });
    // Phase 1, ask for a link;
    WebTarget target = client.target(url() + "resource/get");
    Response response = target.request().get();
    String entity = response.readEntity(String.class);
    assertFault(response.hasLink(linkName), "No link received");
    assertContains(url() + "resource/get", entity);

    // Phase 2, use the link, check the correctness
    assertFault(response.hasLink(linkName), "No link received");
    Link link = response.getLink(linkName);
    response = client.invocation(link).post(null);
    entity = response.readEntity(String.class);
    assertFault(response.hasLink(linkName), "No link received");
    assertContains(url() + "resource/consumesappjson", entity);
    logMsg("Opaque Link has been used in Client.invocation() sucessfully");
  }

  /*
   * @testName: fromResourceMethodThrowsIllegalArgumentExceptionNoMethodTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1037
   * 
   * @test_Strategy: IllegalArgumentException if any argument is null or no
   * method is found
   */
  public void fromResourceMethodThrowsIllegalArgumentExceptionNoMethodTest()
      throws Fault {
    try {
      linkFromResource("nonexistingMethod");
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException iae) {
      logMsg("IllegalArgumentException has been successfully thrown");
    }
  }

  /*
   * @testName: fromResourceMethodThrowsIllegalArgumentExceptionNullMethodTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1037;
   * 
   * @test_Strategy: IllegalArgumentException if any argument is null or no
   * method is found
   */
  public void fromResourceMethodThrowsIllegalArgumentExceptionNullMethodTest()
      throws Fault {
    try {
      linkFromResource(null);
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException iae) {
      logMsg("IllegalArgumentException has been successfully thrown");
    }
  }

  /*
   * @testName: fromResourceMethodThrowsIllegalArgumentExceptionNullClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1037;
   * 
   * @test_Strategy: IllegalArgumentException if any argument is null or no
   * method is found
   */
  public void fromResourceMethodThrowsIllegalArgumentExceptionNullClassTest()
      throws Fault {
    try {
      Link.fromMethod(null, "anymethod");
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException iae) {
      logMsg("IllegalArgumentException has been successfully thrown");
    }
  }

  /*
   * @testName: fromUriTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:788;
   * 
   * @test_Strategy: Create a new instance initialized from an existing URI.
   */
  public void fromUriTest() throws Fault {
    URI uri = uri(Request.GET.name());
    Builder builder = Link.fromUri(uri);
    Link link = builder.build();
    assertContains(link.toString(), url());
    assertContains(link.toString(), "resource");
    assertContains(link.toString(), "get");
    logMsg("Link", link, "has been created from URI", uri);
  }

  /*
   * @testName: fromUriThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:788;
   * 
   * @test_Strategy: throws IllegalArgumentException is uri is null
   */
  public void fromUriThrowsIllegalArgumentExceptionTest() throws Fault {
    try {
      Link.fromUri((URI) null);
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgument has been thrown as expected:", e);
      return;
    }
    throw new Fault("IllegalArgumentException has not been thrown");
  }

  /*
   * @testName: fromUriStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:790;
   * 
   * @test_Strategy: Create a new instance initialized from an existing URI.
   */
  public void fromUriStringTest() throws Fault {
    URI uri = uri(Request.GET.name());
    Builder builder = Link.fromUri(uri.toASCIIString());
    Link link = builder.build();
    assertContains(link.toString(), url());
    assertContains(link.toString(), "resource");
    assertContains(link.toString(), "get");
    logMsg("Link", link, "has been created from URI", uri.toASCIIString());
  }

  /*
   * @testName: fromUriStringThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:790;
   * 
   * @test_Strategy: throws IllegalArgumentException is uri is null
   */
  public void fromUriStringThrowsIllegalArgumentExceptionTest() throws Fault {
    try {
      Link.fromUri((String) null);
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgument has been thrown as expected:", e);
      return;
    }
    throw new Fault("IllegalArgumentException has not been thrown");
  }

  /*
   * @testName: getParamsFromResourceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:792;
   * 
   * @test_Strategy: Returns an immutable map that includes all the link
   * parameters defined on this link. If defined, this map will include entries
   * for "rel", "title" and "type".
   */
  public void getParamsFromResourceTest() throws Fault {
    String title = "Title";
    String value;
    Link link = Link.fromMethod(Resource.class, "producesSvgXml").title(title)
        .build();
    Map<String, String> params = link.getParams();
    System.out.println(params);
    value = params.get("type");
    assertNull(value, "Unexpected media type in link found", value);
    value = params.get("title");
    assertContains(value, title);
    logMsg(params, "found as expected");
  }

  /*
   * @testName: getParamsFromBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:792;
   * 
   * @test_Strategy: Returns an immutable map that includes all the link
   * parameters defined on this link. If defined, this map will include entries
   * for "rel", "title" and "type".
   */
  public void getParamsFromBuilderTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    builder.rel("RELREL").title("TITLETITLE").type("TYPETYPE").param("NEWPARAM",
        "NEWPARAMVALUE");
    Link link = builder.build();

    String value;
    Map<String, String> params = link.getParams();
    value = params.get("title");
    assertContains(value, "titletitle");
    value = params.get("rel");
    assertContains(value, "RELREL");
    value = params.get("type");
    assertContains(value, "typetype");
    value = params.get("NEWPARAM");
    assertContains(value, "newparamvalue");

    logMsg(params, "found as expected");
  }

  /*
   * @testName: getRelTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:793;
   * 
   * @test_Strategy: Returns the value associated with the link "rel" param, or
   * null if this param is not specified.
   */
  public void getRelTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    builder.rel("RELREL").title("TITLETITLE").type("TYPETYPE").param("NEWPARAM",
        "NEWPARAMVALUE");
    Link link = builder.build();
    String rel = link.getRel();
    assertFault(rel.contains("RELREL"), "#getRel did NOT return expected",
        "RELREL");
    logMsg("#getRel() return expected rel");
  }

  /*
   * @testName: getRelIsEmptyListTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:793;
   * 
   * @test_Strategy: Returns the value associated with the link "rel" param, or
   * null if this param is not specified.
   */
  public void getRelIsEmptyListTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    Link link = builder.build();
    String rel = link.getRel();
    assertFault(rel == null, "#getRel is NOT null");
    logMsg("#getRel() returns expected null");
  }

  /*
   * @testName: getTitleTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:794;
   * 
   * @test_Strategy: Returns the value associated with the link "title" param,
   * or null if this param is not specified.
   */
  public void getTitleTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    builder.rel("RELREL").title("TITLETITLE").type("TYPETYPE");
    Link link = builder.build();
    String title = link.getTitle();

    assertFault(title != null, "#getTitle did NOT return expected title");
    assertContains(title, "TITLETITLE");
  }

  /*
   * @testName: getTitleIsNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:794;
   * 
   * @test_Strategy: Returns the value associated with the link "title" param,
   * or null if this param is not specified.
   */
  public void getTitleIsNullTest() throws Fault {
    Link link = linkFromResource("get");
    String title = link.getTitle();
    assertFault(title == null, "#getTitle is unexpected", title);
    logMsg("#getTitle( returns null as expected");
  }

  /*
   * @testName: getTypeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:795;
   * 
   * @test_Strategy: Returns the value associated with the link "type" param, or
   * null if this param is not specified.
   */
  public void getTypeTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    builder.rel("RELREL").title("TITLETITLE").type("TYPETYPE");
    Link link = builder.build();
    String type = link.getType();

    assertFault(type != null, "#getType() did NOT return expected title");
    assertContains(type, "TYPETYPE");
  }

  /*
   * @testName: getTypeIsNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:795;
   * 
   * @test_Strategy: Returns the value associated with the link "type" param, or
   * null if this param is not specified.
   */
  public void getTypeIsNullTest() throws Fault {
    Link link = linkFromResource("get");
    String type = link.getType();
    assertFault(type == null, "#getType is unexpected", type);
    logMsg("#getType( returns null as expected");
  }

  /*
   * @testName: getUriTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:796;
   * 
   * @test_Strategy: Returns the underlying URI associated with this link.
   */
  public void getUriTest() throws Fault {
    URI uri = uri("get");
    Builder builder = Link.fromUri(uri);
    Link link = builder.build();
    URI uriFromLink = link.getUri();

    assertFault(uri.equals(uriFromLink), "#getUri()", uriFromLink,
        "differes from original uri", uri);
    logMsg("Original URI", uri, "equals obtained", uriFromLink);
  }

  /*
   * @testName: getUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:797;
   * 
   * @test_Strategy: Convenience method that returns a
   * javax.ws.rs.core.UriBuilder initialized with this link's underlying URI.
   */
  public void getUriBuilderTest() throws Fault {
    URI uri = uri("get");
    Builder builder = Link.fromUri(uri);
    Link link = builder.build();
    UriBuilder uriBuilder = link.getUriBuilder();
    assertFault(uriBuilder != null, "#getUriBuilder is null");
    URI uriFromLink = uriBuilder.build();

    assertFault(uri.equals(uriFromLink), "#getUri()", uriFromLink,
        "differes from original uri", uri);
    logMsg("Original URI", uri, "equals obtained", uriFromLink);
  }

  /*
   * @testName: serializationFromResourceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:800; JAXRS:JAVADOC:801;
   * 
   * @test_Strategy: Returns a string representation as a link header (RFC 5988)
   * 
   * Simple parser to convert link header string representations into a link
   * (valueOf)
   */
  public void serializationFromResourceTest() throws Fault {
    Method[] methods = Resource.class.getDeclaredMethods();
    for (Method method : methods) {
      logMsg("Serialization for method", method);
      String name = method.getName();
      Link link = linkFromResource(name);
      String string = link.toString();
      Link fromValueOf = Link.valueOf(string);
      assertEquals(link.toString(), fromValueOf.toString(), "links", link,
          fromValueOf, "are not equal");
      logMsg("serialization works for method", name);
    }
    logMsg(
        "Serialization with #toString() of Resource method links is sucessfull");
  }

  /*
   * @testName: valueOfThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:801;
   * 
   * @test_Strategy: throws IllegalArgumentException if a syntax error is found
   */
  public void valueOfThrowsIllegalArgumentExceptionTest() throws Fault {
    try {
      Link.valueOf("</>>");
      throw new Fault("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      logMsg("Link#vaueOf() throws IllegalArgumentException as expected");
    }
  }

  /*
   * @testName: getRelsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:956;
   * 
   * @test_Strategy: Returns the value associated with the link "rel" param as a
   * list of strings or the empty list if "rel" is not defined.
   */
  public void getRelsTest() throws Fault {
    String[] rels = { "RELREL", "REL", "relrelrel", "RELRELREL" };
    Builder builder = Link.fromUri(uri("get"));
    for (int i = 0; i != rels.length; i++)
      builder = builder.rel(rels[i]);
    Link link = builder.build();
    assertNotNull(link.getRels(), "#getRels is null");
    assertEqualsInt(link.getRels().size(), 4, "Unexpected #getRels size",
        link.getRels().size(), "Should be 4");
    String list = ";" + JaxrsUtil.iterableToString(";", link.getRels()) + ";";
    for (int i = 0; i != rels.length; i++)
      assertContains(list, ";" + rels[i] + ";", "Relation", rels[i],
          "has not been found in #getRels", list);
    logMsg("#getRel() return expected rels", list);
  }

  /*
   * @testName: getRelsIsEmptyTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:956;
   * 
   * @test_Strategy: Returns the value associated with the link "rel" param as a
   * list of strings or the empty list if "rel" is not defined.
   */
  public void getRelsIsEmptyTest() throws Fault {
    Builder builder = Link.fromUri(uri("get"));
    Link link = builder.build();
    assertNotNull(link.getRels(), "#getRels is null");
    assertEqualsInt(link.getRels().size(), 0, "Unexpected #getRels size",
        link.getRels().size(), "Should be 0");
    logMsg("#getRel() return empty list as expected");
  }

  /*
   * @testName: fromResourceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1039;
   * 
   * @test_Strategy: Convenience method to build a link from a resource.
   */
  public void fromResourceTest() throws Fault {
    Link link = Link.fromResource(Resource.class).build();
    String resource = link.toString();
    assertContains(resource, "<resource>");
    assertNotContains(resource, "type");
    logMsg("Link", resource, "has been created");
  }

  /*
   * @testName: fromResourceWithMediaTypeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1004;
   * 
   * @test_Strategy: Convenience method to build a link from a resource.
   */
  public void fromResourceWithMediaTypeTest() throws Fault {
    Link link = Link.fromResource(ResourceWithProduces.class).build();
    String resource = link.toString();
    assertContains(resource, "<producesresource>");
    assertNotContains(resource, "type=\"" + MediaType.TEXT_HTML + "\"");
    logMsg("Link", resource, "has been created");
  }

  /*
   * @testName: fromResourceThrowsIAEWhenNullClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1039;
   * 
   * @test_Strategy: IllegalArgumentException - if any argument is null or no
   * method is found.
   */
  public void fromResourceThrowsIAEWhenNullClassTest() throws Fault {
    try {
      Link link = Link.fromResource((Class<?>) null).build();
      logMsg("Unexpectedly thrown no exception and created link", link);
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected", e);
    }
  }

  /*
   * @testName: fromUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1005;
   * 
   * @test_Strategy: Create a new builder instance initialized from a URI
   * builder.
   */
  public void fromUriBuilderTest() throws Fault {
    String segment = "goto/label/ten/";
    Link link = Link.fromUri(uri(segment)).build();
    UriBuilder builder = link.getUriBuilder();
    Builder fromBuilder = Link.fromUriBuilder(builder);

    String sBuilder = builder.build().toASCIIString();
    String sFromBuilder = fromBuilder.build().getUri().toASCIIString();
    assertContains(sFromBuilder, sBuilder, "Original builder", sBuilder,
        "not found in #fromUriBuilder", sFromBuilder);
    logMsg("#fromUriBuilder", sFromBuilder, "contains the original", sBuilder);
  }

  /*
   * @testName: fromPathTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1038;
   * 
   * @test_Strategy: Convenience method to build a link from a path. Equivalent
   * to fromUriBuilder(UriBuilder.fromPath(path)).
   */
  public void fromPathTest() throws Fault {
    String path = "somewhere/somehow";
    UriBuilder builder = UriBuilder.fromPath(path);
    Link.Builder linkBuilder = Link.fromUriBuilder(builder);
    Link.Builder fromPathBuilder = Link.fromPath(path);
    String fromUriBuilderString = linkBuilder.build().toString();
    String fromPathString = fromPathBuilder.build().toString();
    assertEquals(fromUriBuilderString, fromPathString, "fromUriBuilder()=",
        fromUriBuilderString, "differs from Link.fromPath()", fromPathString);
    logMsg(
        "fromUriBuilder(UriBuilder.fromPath(path)) is equivalent to fromPath(path)",
        "The link is", fromPathString);
  }

  /*
   * @testName: fromPathWithUriTemplateParamsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1038;
   * 
   * @test_Strategy: Convenience method to build a link from a path. Equivalent
   * to fromUriBuilder(UriBuilder.fromPath(path)).
   */
  public void fromPathWithUriTemplateParamsTest() throws Fault {
    String path = "somewhere/somehow/{p1}/{p2}";
    String param1 = "param1", param2 = "param2";
    UriBuilder builder = UriBuilder.fromPath(path);
    Link.Builder linkBuilder = Link.fromUriBuilder(builder);
    Link.Builder fromPathBuilder = Link.fromPath(path);
    String fromUriBuilderString = linkBuilder.build(param1, param2).toString();
    String fromPathString = fromPathBuilder.build(param1, param2).toString();
    assertEquals(fromUriBuilderString, fromPathString,
        "fromUriBuilder(UriBuilder.fromPath(,", path, "))=",
        fromUriBuilderString, "differs from Link.fromPath(", path, ")",
        fromPathString);
    logMsg("fromUriBuilder(UriBuilder.fromPath(", path,
        ")) is equivalent to fromPath(", path, ")", "The link is",
        fromPathString);
  }

  /*
   * @testName: fromPathThrowsIAETest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1038;
   * 
   * @test_Strategy: Throws: java.lang.IllegalArgumentException - if path is
   * null.
   */
  public void fromPathThrowsIAETest() throws Fault {
    try {
      UriBuilder.fromPath((String) null);
      fault("fromPath(null) does not throws IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      logMsg("fromPath(null) throws IllegalArgumentException as expected");
    }
  }

  /*
   * @testName: fromLinkTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:783;
   * 
   * @test_Strategy: Throws: java.lang.IllegalArgumentException - if path is
   * null.
   */
  public void fromLinkTest() throws Fault {
    Link link = RuntimeDelegate.getInstance().createLinkBuilder().baseUri(url())
        .rel("relation relation2").title("titleX").param("param1", "value1")
        .param("param2", "value2").type(MediaType.APPLICATION_OCTET_STREAM)
        .build();
    Link fromLink = Link.fromLink(link).build();
    assertEquals(link, fromLink, "fromLink(link)=", fromLink,
        "differes from original Link", link);
    logMsg("fromLink() is equal to original link", link, "as expected");
  }

  // ///////////////////////////////////////////////////////////////////
  protected static Link linkFromResource(String method) {
    Builder builder = Link.fromMethod(Resource.class, method);
    Link link = builder.build();
    return link;
  }

  protected void assertContains(String string, String substring) throws Fault {
    super.assertContainsIgnoreCase(string, substring, string,
        "does not contain expected", substring);
    logMsg("Found expected", substring);
  }

  protected void assertNotContains(String string, String substring)
      throws Fault {
    assertTrue(!string.toLowerCase().contains(substring.toLowerCase()), string,
        "does unexpectedly contain", substring);
    logMsg(substring, "is not in", string, "as expected");
  }

  protected URI uri(String method) throws Fault {
    URI uri = null;
    try {
      uri = new URI(url() + "resource/" + method);
    } catch (URISyntaxException e) {
      throw new Fault(e);
    }
    return uri;
  }

  protected static String url() {
    return "http://oracle.com:888/";
  }

}
