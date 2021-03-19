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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.restful.server;

import java.io.ByteArrayInputStream;
import java.util.StringTokenizer;

import jakarta.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import jakarta.xml.ws.Provider;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.WebServiceProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.ServiceMode;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.http.HTTPException;
import jakarta.xml.ws.http.HTTPBinding;
import jakarta.xml.ws.BindingType;
import java.util.Hashtable;

@WebServiceProvider
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class TokensImpl implements Provider<Source> {

  @Resource
  protected WebServiceContext wsContext;

  private Hashtable tokens;

  private String method, query, path;

  public TokensImpl() {
    tokens = new Hashtable();
    tokens.put("1", Integer.valueOf(25));
    tokens.put("2", Integer.valueOf(5));
    tokens.put("3", Integer.valueOf(50));
    tokens.put("4", Integer.valueOf(75));
    tokens.put("5", Integer.valueOf(125));
    tokens.put("6", Integer.valueOf(225));
    tokens.put("7", Integer.valueOf(20));
    tokens.put("8", Integer.valueOf(10));
    tokens.put("9", Integer.valueOf(1));
    tokens.put("10", Integer.valueOf(2));
  }

  public Source invoke(Source source) {
    try {
      MessageContext mc = wsContext.getMessageContext();
      method = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);
      query = (String) mc.get(MessageContext.QUERY_STRING);
      path = (String) mc.get(MessageContext.PATH_INFO);
      System.out.println("Request Method = " + method);
      System.out.println("Query String = " + query);
      System.out.println("PathInfo = " + path);
      if (method.equals("GET")) {
        return doGet();
      } else if (method.equals("PUT")) {
        return doPut();
      } else if (method.equals("DELETE")) {
        return doDelete();
      } else if (method.equals("POST")) {
        return doPost();
      } else
        throw new HTTPException(400);
    } catch (Exception e) {
      e.printStackTrace();
      throw new HTTPException(500);
    }
  }

  private Source doPut() {
    System.out.println("Processing PUT request ...");
    if (query != null && query.contains("token=")) {
      return doPutSource(query);
    } else if (path != null && path.contains("/token")) {
      return doPutSource(path);
    } else {
      throw new HTTPException(404);
    }
  }

  private Source doPutSource(String str) {
    StringTokenizer st = new StringTokenizer(str, "=&/");
    st.nextToken();
    String key = st.nextToken();
    st.nextToken();
    String value = st.nextToken();
    tokens.put(key, new Integer(value));
    String body = "<ns:tokenPutResponse xmlns:ns=\"http://tokens.org\"><ns:return>"
        + "token" + key + "=" + value + " PUT"
        + "</ns:return></ns:tokenPutResponse>";
    Source source = new StreamSource(new ByteArrayInputStream(body.getBytes()));
    return source;
  }

  private Source doDelete() {
    System.out.println("Processing DELETE request ...");
    if (query != null && query.contains("token=")) {
      return doDeleteSource(query);
    } else if (path != null && path.contains("/token")) {
      return doDeleteSource(path);
    } else {
      throw new HTTPException(404);
    }
  }

  private Source doDeleteSource(String str) {
    StringTokenizer st = new StringTokenizer(str, "=&/");
    st.nextToken();
    String key = st.nextToken();
    tokens.remove(key);
    String body = "<ns:tokenDeleteResponse xmlns:ns=\"http://tokens.org\"><ns:return>"
        + "token=" + key + " DELETE" + "</ns:return></ns:tokenDeleteResponse>";
    Source source = new StreamSource(new ByteArrayInputStream(body.getBytes()));
    return source;
  }

  private Source doPost() {
    System.out.println("Processing POST request ...");
    return null;
  }

  private Source doGet() {
    System.out.println("Processing GET request ...");
    if (query != null && query.contains("token=")) {
      return doGetSource(query);
    } else if (path != null && path.contains("/token")) {
      return doGetSource(path);
    } else {
      throw new HTTPException(404);
    }
  }

  private Source doGetSource(String str) {
    StringTokenizer st = new StringTokenizer(str, "=&/");
    st.nextToken();
    String key = st.nextToken();
    Integer value = (Integer) tokens.get(key);
    String response;
    if (value == null)
      response = "token=" + key + " not found";
    else
      response = "" + value;
    String body = "<ns:tokenGetResponse xmlns:ns=\"http://tokens.org\"><ns:return>"
        + response + "</ns:return></ns:tokenGetResponse>";
    Source source = new StreamSource(new ByteArrayInputStream(body.getBytes()));
    return source;
  }

}
