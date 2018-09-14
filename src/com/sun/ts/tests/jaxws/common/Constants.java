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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class Constants {

  // Standard namespace URI's
  public static final String NSURI_SOAP11_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";

  public static final String NSURI_SOAP11_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

  public static final String NSURI_SOAP11_ACTOR_NEXT = "http://schema.xmlsoap.org/soap/actor/next";

  public static final String NSURI_SOAP12_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";

  public static final String NSURI_SOAP12_ENCODING = "http://www.w3.org/2003/05/soap-encoding";

  public static final String NSURI_SOAP12_ROLE_NEXT = "http://www.w3.org/2003/05/soap-envelope/role/next";

  public static final String NSURI_SOAP12_ROLE_NONE = "http://www.w3.org/2003/05/soap-envelope/role/none";

  public static final String NSURI_SOAP12_ROLE_ULTIMATE_RECEIVER = "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver";

  public static final String NSURI_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";

  public static final String NSURI_HTTP_BINDING = "http://www.w3.org/2004/08/wsdl/http";

  public static final String NSURI_SOAP11HTTP_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http";

  public static final String NSURI_SOAP12HTTP_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/";

  public static final String NSURI_SOAP11HTTP_MTOM_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true";

  public static final String NSURI_SOAP12HTTP_MTOM_BINDING = "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true";

  public static final String URI_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";

  public static final String URI_BINDING = "http://schemas.xmlsoap.org/wsdl/soap/";

  public static final String URI_BINDING_HTTP = "http://schemas.xmlsoap.org/wsdl/soap/http";

  public static final String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

  public static final String URI_HTTP = "http://schemas.xmlsoap.org/soap/http/";

  public static final String URI_SOAP_ACTOR_NEXT = "http://schema.xmlsoap.org/soap/actor/next";

  public static final String XSD = "http://www.w3.org/2001/XMLSchema";

  public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";

  public static final String NSURI_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";

  public static final String NSURI_SCHEMA_XSI = "http://www.w3.org/2001/XMLSchema-instance";

  // Standard SOAP Element Tags
  public static final String TAG_ENVELOPE = "Envelope";

  public static final String TAG_HEADER = "Header";

  public static final String TAG_BODY = "Body";

  // Standard Attribute Tags
  public static final String ATTR_ACTOR = "actor";

  public static final String ATTR_MUST_UNDERSTAND = "mustUnderstand";

  public static final String ATTR_ENCODING_STYLE = "encodingStyle";

  // Invalid URL's used for negative testing
  public static final String BAD_ABSOLUTE_URL = "bogus:/foobar";

  public static final String BAD_RELATIVE_URL = "/foobar";

  // Handler constants for message direction and message scope
  public final static String INBOUND = "Inbound";

  public final static String OUTBOUND = "Outbound";

  public final static String INOUTBOUND = "InOutbound";

  public final static String APPLICATION = "APPLICATION";

  public final static String HANDLER = "HANDLER";

  public final static String EXPECTED_HTTP_BINDING = NSURI_HTTP_BINDING;

  public final static String EXPECTED_SOAP11HTTP_BINDING = NSURI_SOAP11HTTP_BINDING;

  public final static String EXPECTED_SOAP12HTTP_BINDING = NSURI_SOAP12HTTP_BINDING;

  public final static String EXPECTED_SOAP11HTTP_MTOM_BINDING = NSURI_SOAP11HTTP_MTOM_BINDING;

  public final static String EXPECTED_SOAP12HTTP_MTOM_BINDING = NSURI_SOAP12HTTP_MTOM_BINDING;
}
