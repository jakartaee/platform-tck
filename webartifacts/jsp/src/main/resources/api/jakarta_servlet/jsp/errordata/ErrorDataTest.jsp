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
                 java.io.IOException" %>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    private static final ErrorData getErrorDataInstance() {
        return new ErrorData(new RuntimeException("runtime"), 501, "/uri", "TestServlet");
    }
%>

<%!
    public void errorDataConstructorTest(HttpServletRequest req,
                                         HttpServletResponse res,
                                         JspWriter out)
    throws ServletException, IOException {
        ErrorData data = getErrorDataInstance();
        if (data != null) {
            out.print("Test PASSED");
        } else {
            out.print("Test FAILED.  Constructor of new ErrorData object failed.");
        }
    }
%>

<%!
    public void errorDataGetThrowableTest(HttpServletRequest req,
                                          HttpServletResponse res,
                                          JspWriter out)
    throws ServletException, IOException {
        ErrorData data = getErrorDataInstance();
        Throwable t = null;

        if (data != null) {
            t = data.getThrowable();
        } else {
            out.println("Test FAILED.  ErrorData instance was null.");
            return;
        }

        if (t != null) {
            String message = t.getMessage();
            if (message != null && message.equals("runtime")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected throwable message of 'runtime'");
                out.println("Message returned: " + message);
            }
        } else {
            out.println("Test FAILED.  ErrorData.getThrowable() returned null.");
        }
    }
%>

<%!
    public void errorDataGetStatusCodeTest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        ErrorData data = getErrorDataInstance();
        int code = 0;

        if (data != null) {
            code = data.getStatusCode();
        } else {
            out.println("Test FAILED.  ErrorData instance was null.");
            return;
        }

        if (code == 501) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Expected a status code of 501.");
            out.println("Status code recieved: " + code);
        }
    }
%>

<%!
    public void errorDataGetRequestURITest(HttpServletRequest req,
                                           HttpServletResponse res,
                                           JspWriter out)
    throws ServletException, IOException {
        ErrorData data = getErrorDataInstance();
        String uri = null;

        if (data != null) {
            uri = data.getRequestURI();
        } else {
            out.println("Test FAILED.  ErrorData instance was null.");
            return;
        }

        if (uri != null) {
            if (uri.equals("/uri")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected a value of '/uri'.");
                out.println("Received: " + uri);
            }
        } else {
            out.println("Test FAILED.  ErrorData.getRequestURI() returned null.");
        }
    }
%>

<%!
    public void errorDataGetServletNameTest(HttpServletRequest req,
                                            HttpServletResponse res,
                                            JspWriter out)
    throws ServletException, IOException {
        ErrorData data = getErrorDataInstance();
        String name = null;

        if (data != null) {
            name = data.getServletName();
        } else {
            out.println("Test FAILED.  ErrorData instance was null.");
            return;
        }

        if (name != null) {
            if (name.equals("TestServlet")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Expected a value of 'TestServlet'");
                out.println("Received: " + name);
            }
        } else {
            out.println("Test FAILED. ErrorData.getServletName() returned null.");
        }
    }
%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
