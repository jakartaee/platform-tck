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

package com.sun.ts.tests.jaxrs.api.rs.ext.runtimedelegate.create;

import java.util.HashSet;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import com.sun.net.httpserver.HttpHandler;
import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 24421214489103680L;

  transient RuntimeDelegate delegate;

  public JAXRSClient() {
    delegate = RuntimeDelegate.getInstance();
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /* Run test */

  /*
   * @testName: createEndpointTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:284; JAXRS:JAVADOC:285; JAXRS:JAVADOC:286;
   * 
   * @test_Strategy: Create a configured instance of the supplied endpoint type.
   * How the returned endpoint instance is published is dependent on the type of
   * endpoint.
   * 
   * IllegalArgumentException - if application is null or the requested endpoint
   * type is not supported. UnsupportedOperationException - if the
   * implementation supports no endpoint types.
   * 
   * Note that these assertions are almost untestable, as it can either create
   * an instance or throw one exception or the other.
   */
  public void createEndpointTest() throws Fault {
    Application application = new Application() {
      public java.util.Set<java.lang.Class<?>> getClasses() {
        java.util.Set<java.lang.Class<?>> set = new HashSet<Class<?>>();
        set.add(Resource.class);
        return set;
      };
    };
    RuntimeDelegate delegate = RuntimeDelegate.getInstance();
    HttpHandler handler = null;
    try {
      handler = delegate.createEndpoint(application,
          com.sun.net.httpserver.HttpHandler.class);
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected", e);
      return;
    } catch (UnsupportedOperationException e) {
      logMsg("UnsupportedOperationException has been thrown as expected", e);
      return;
    }
    assertNotNull(handler,
        "HttpHandler end point should be created, or an exception thrown otherwise");
    logMsg("HttpHandler endpoint has been sucessfully created");
  }

  /*
   * @testName: createEndpointThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:285;
   * 
   * @test_Strategy: IllegalArgumentException - if application is null or the
   * requested endpoint type is not supported.
   */
  public void createEndpointThrowsIllegalArgumentExceptionTest() throws Fault {
    try {
      delegate.createEndpoint((Application) null,
          com.sun.net.httpserver.HttpHandler.class);
      fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected");
    }
  }

  /*
   * @testName: checkCreatedUriBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:289;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createUriBuilder makes no
   * exception and is not null
   * 
   */
  public void checkCreatedUriBuilderTest() throws Fault {
    UriBuilder builder = delegate.createUriBuilder();
    assertFault(builder != null, "UriBuilder has not been created");
  }

  /*
   * @testName: checkCreatedVariantListBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:290;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createVariantListBuilder makes
   * no exception and is not null
   * 
   */
  public void checkCreatedVariantListBuilderTest() throws Fault {
    VariantListBuilder builder = delegate.createVariantListBuilder();
    assertFault(builder != null, "VariantListBuilder has not been created");
  }

  /*
   * @testName: checkCreatedResponseBuilderTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:288;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createResponseBuilder makes no
   * exception and is not null
   * 
   */
  public void checkCreatedResponseBuilderTest() throws Fault {
    ResponseBuilder builder = delegate.createResponseBuilder();
    assertFault(builder != null, "ResponseBuilderTest has not been created");
  }

  /*
   * @testName: checkCreatedHeaderDelegateCookieTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegate<Cookie>
   * makes no exception and is not null
   * 
   */
  public void checkCreatedHeaderDelegateCookieTest() throws Fault {
    String cookieName = "cookieName";
    String cookieValue = "cookieValue";
    HeaderDelegate<Cookie> hdc = delegate.createHeaderDelegate(Cookie.class);
    assertFault(hdc != null, "HeaderDelegate<Cookie> has not been created");

    Cookie cookie = new Cookie(cookieName, cookieValue);
    String result = hdc.toString(cookie);
    Cookie serialized = hdc.fromString(result);
    assertFault(cookieName.equals(serialized.getName()),
        "HeaderDelegate<Cookie> fromString(),toString() failed");
  }

  /*
   * @testName: checkCreatedHeaderDelegateCacheControlTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that
   * RuntimeDelegate.createHeaderDelegate<CacheControl> makes no exception and
   * is not null
   * 
   */
  public void checkCreatedHeaderDelegateCacheControlTest() throws Fault {
    HeaderDelegate<CacheControl> hdcc = delegate
        .createHeaderDelegate(CacheControl.class);
    assertFault(hdcc != null,
        "HeaderDelegate<CacheControl> has not been created");

    CacheControl control = new CacheControl();
    control.setMaxAge(1000);
    control.setSMaxAge(500);
    control.setNoTransform(false);
    control.setPrivate(true);

    String toString = hdcc.toString(control);
    CacheControl serialized = hdcc.fromString(toString);

    assertFault(
        serialized.getMaxAge() == 1000 && serialized.getSMaxAge() == 500
            && !serialized.isNoTransform() && serialized.isPrivate(),
        "HeaderDelegate<CacheControl> fromString(),toString() failed");
  }

  /*
   * @testName: checkCreatedHeaderDelegateEntityTagTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegateEntityTag
   * makes no exception and is not null
   * 
   */
  public void checkCreatedHeaderDelegateEntityTagTest() throws Fault {
    String tagValue = "tagValue";
    HeaderDelegate<EntityTag> hdet = delegate
        .createHeaderDelegate(EntityTag.class);
    assertFault(hdet != null, "HeaderDelegate<EntityTag> has not been created");

    EntityTag tag = new EntityTag(tagValue);
    String toString = hdet.toString(tag);
    EntityTag serialized = hdet.fromString(toString);

    assertFault(tagValue.equals(serialized.getValue()),
        "HeaderDelegate<EntityTag> fromString(),toString() failed");
  }

  /*
   * @testName: checkCreatedHeaderDelegateNewCookieTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegate<NewCookie>
   * makes no exception and is not null
   * 
   */
  public void checkCreatedHeaderDelegateNewCookieTest() throws Fault {
    String cookieName = "cookieName";
    String cookieValue = "cookieValue";

    HeaderDelegate<NewCookie> hdnc = delegate
        .createHeaderDelegate(NewCookie.class);
    assertFault(hdnc != null, "HeaderDelegate<NewCookie> has not been created");

    NewCookie cookie = new NewCookie(cookieName, cookieValue);
    String result = hdnc.toString(cookie);
    NewCookie serialized = hdnc.fromString(result);
    assertFault(cookieName.equals(serialized.getName()),
        "HeaderDelegate<NewCookie> fromString(),toString() failed");

  }

  /*
   * @testName: checkCreatedHeaderDelegateMediaTypeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegate<MediaType>
   * makes no exception and is not null
   * 
   */
  public void checkCreatedHeaderDelegateMediaTypeTest() throws Fault {
    HeaderDelegate<MediaType> hdmt = delegate
        .createHeaderDelegate(MediaType.class);
    assertFault(hdmt != null, "HeaderDelegate<MediaType> has not been created");

    MediaType type = new MediaType("text", "html");
    String toString = hdmt.toString(type);
    MediaType serialized = hdmt.fromString(toString);
    assertFault(serialized.isCompatible(MediaType.TEXT_HTML_TYPE),
        "HeaderDelegate<MediaType> fromString(),toString() failed");
  }

  /*
   * @testName: checkCreatedHeaderDelegateNullPointerTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:287; JAXRS:JAVADOC:294; JAXRS:JAVADOC:296;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegate<MediaType>
   * makes no exception and is not null, but .toString(null) and
   * fromString(null) throw IAE
   */
  public void checkCreatedHeaderDelegateNullPointerTest() throws Fault {
    HeaderDelegate<MediaType> hdmt = delegate
        .createHeaderDelegate(MediaType.class);
    assertFault(hdmt != null, "HeaderDelegate<MediaType> has not been created");

    try {
      hdmt.fromString(null);
      throw new Fault(
          "HeaderDelegate.fromString(null) did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
    }

    try {
      hdmt.toString(null);
      throw new Fault(
          "HeaderDelegate.toString(null) did not throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
    }
  }

  /*
   * @testName: createHeaderDelegateThrowsIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:928;
   * 
   * @test_Strategy: Check that RuntimeDelegate.createHeaderDelegate
   * ((Class)null) throws IAE
   */
  public void createHeaderDelegateThrowsIllegalArgumentExceptionTest()
      throws Fault {
    try {
      delegate.createHeaderDelegate((Class<MediaType>) null);
      throw new Fault("IllegalArgumentException has not been thrown");
    } catch (IllegalArgumentException e) {
      logMsg("IllegalArgumentException has been thrown as expected");
    }

  }

}
