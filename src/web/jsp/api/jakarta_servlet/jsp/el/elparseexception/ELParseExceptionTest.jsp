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
                 javax.servlet.jsp.el.ELParseException" %>
<%@ page contentType="text/plain" %>

<%-- Begin test definitions --%>

<%!
    public void elParseExceptionDefaultCtorTest(HttpServletRequest req,
                                                HttpServletResponse res,
                                                JspWriter out)
    throws ServletException, IOException {
        ELParseException epe = new ELParseException();
        if (epe != null) {
            out.println("Test PASSED");
        } else {
            out.println("Test FAILED.  Unable to create an ELParseException" +
                " with the default constructor.");
        }

    }
%>

<%!
    public void elParseExceptionMessageCtorTest(HttpServletRequest req,
                                                HttpServletResponse res,
                                                JspWriter out)
    throws ServletException, IOException {
        ELParseException epe = new ELParseException("message");
        if (epe != null) {
            String epeMess = epe.getMessage();
            if (epeMess.equals("message")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  Unexpected message returned by " +
                    "ELParseException.getMessage().  \nExpected 'message'");
                out.println("Received: " + epeMess);
            }
        } else {
            out.println("Test FAILED.  Unable to create an ELParseException" +
                " with the default constructor.");
        }
    }
%>

<% JspTestUtil.invokeTest(this, request, response, out); %>
