<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page import="com.sun.ts.tests.jsp.common.util.JspTestUtil,
                 java.io.IOException,
                 com.sun.ts.tests.jsp.common.util.SimpleContext" %>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    public void jspFactoryGetDefaultFactoryTest(HttpServletRequest req,
                                                HttpServletResponse res,
                                                JspWriter out)
    throws ServletException, IOException {
        JspFactory factory = JspFactory.getDefaultFactory();
        if (factory != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  JspFactory.getDefaultFactory() " +
                "returned null.");
        }
    }
%>

<%!
    public void jspFactoryGetPageContextTest(HttpServletRequest req,
                                             HttpServletResponse res,
                                             JspWriter out)
    throws ServletException, IOException {
        JspFactory factory = JspFactory.getDefaultFactory();
        if (factory != null) {
            PageContext pc = factory.getPageContext(this, req, res, "",
                true, 2048, true);
            if (pc != null) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  JspFactory.getPageContext() returned" +
                    " null.");
            }
        } else {
            out.println("Test FAILED.  JspFactory.getDefaultFactory() " +
                "returned null.");
        }
    }
%>

<%!
    public void jspFactoryGetEngineInfoTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        JspFactory factory = JspFactory.getDefaultFactory();
        if (factory != null) {
            JspEngineInfo info = factory.getEngineInfo();
            if (info != null) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  JspFactory.getEngineInfo() " +
                    "returned null.");
            }
        } else {
            out.println("Test FAILED.  JspFactory.getDefaultFactory() " +
                "returned null.");
        }
    }
%>

<%-- Test invocation --%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
