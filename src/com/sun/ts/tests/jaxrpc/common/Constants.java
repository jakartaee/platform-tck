/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class Constants {

  // Standard namespace URI's
  public static final String ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";

  public static final String URI_ENVELOPE = ENVELOPE;

  public static final String ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

  public static final String URI_ENCODING = ENCODING;

  public static final String XSD = "http://www.w3.org/2001/XMLSchema";

  public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";

  public static final String TRANSPORT_HTTP = "http://schemas.xmlsoap.org/soap/http/";

  public static final String URI_HTTP = TRANSPORT_HTTP;

  public static final String URI_SOAP_ACTOR_NEXT = "http://schema.xmlsoap.org/soap/actor/next";

  // Standard SOAP Element Tags
  public static final String TAG_ENVELOPE = "Envelope";

  public static final String TAG_HEADER = "Header";

  public static final String TAG_BODY = "Body";

  public static final String ATTR_ACTOR = "actor";

  public static final String ATTR_MUST_UNDERSTAND = "actor";

  public static final String ATTR_ENCODING_STYLE = "encodingStyle";

  // Standard Properties for Call Interface
  public static final String CALL_USERNAME_PROPERTY = "javax.xml.rpc.security.auth.username";

  public static final String CALL_PASSWORD_PROPERTY = "javax.xml.rpc.security.auth.password";

  public static final String CALL_OPERATION_STYLE_PROPERTY = "javax.xml.rpc.soap.operation.style";

  public static final String CALL_SOAPACTION_USE_PROPERTY = "javax.xml.rpc.soap.http.soapaction.use";

  public static final String CALL_SOAPACTION_URI_PROPERTY = "javax.xml.rpc.soap.http.soapaction.uri";

  public static final String CALL_ENCODINGSTYLE_URI_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";

  // Standard Properties for Stub Configuration
  public static final String STUB_USERNAME_PROPERTY = "javax.xml.rpc.security.auth.username";

  public static final String STUB_PASSWORD_PROPERTY = "javax.xml.rpc.security.auth.password";

  public static final String ENDPOINT_PROPERTY = "javax.xml.rpc.service.endpoint.address";

  // Invalid Property values for Stub Configuration used for negative testing
  public static final String INVALID_PROPERTY = "javax.xml.rpc.foo.bar";

  public static final String NULL_PROPERTY = null;

  // Invalid URL's used for negative testing
  public static final String BAD_ABSOLUTE_URL = "bogus:/foobar";

  public static final String BAD_RELATIVE_URL = "/foobar";
}
