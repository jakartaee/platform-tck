/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ContextService.contextPropagate_servlet;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.BASE64Encoder;
import com.sun.ts.lib.util.TestUtil;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

@WebServlet("/ProxyCreatorServlet")
public class ProxyCreatorServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String action = req.getParameter("action");
    Object proxy = true;
    String result = null;
    URL url = new URL("http://" + req.getServerName() + ":"
        + req.getServerPort() + "/ContextPropagate_servlet2_web/TestServlet");
    ;
    TSURL ctsurl = new TSURL();
    if ("createJNDIWork".equals(action)) {
      try {
        proxy = Util.lookupDefaultContextService().createContextualProxy(
            new TestJNDIRunnableWork(), Runnable.class, TestWorkInterface.class,
            Serializable.class);
      } catch (NamingException e) {
        throw new ServletException(e);
      }
    }

    if ("createClassloaderWork".equals(action)) {
      try {
        proxy = Util.lookupDefaultContextService().createContextualProxy(
            new TestClassloaderRunnableWork(), Runnable.class,
            TestWorkInterface.class, Serializable.class);
      } catch (NamingException e) {
        throw new ServletException(e);
      }
    }

    Properties p = new Properties();
    p.setProperty("proxy", proxyToString(proxy));
    URLConnection urlConn = TestUtil.sendPostData(p, url);
    result = TestUtil.getResponse(urlConn);
    resp.getWriter().write(result);
  }

  private String proxyToString(Object proxy) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bout);
    out.writeObject(proxy);
    out.close();
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(bout.toByteArray());
  }
}
