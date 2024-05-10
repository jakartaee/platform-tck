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

<%@ tag import="java.util.Enumeration"%>

<%
    // check to make sure the page scope of wrapper context is emtpy.
    Enumeration e = jspContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
    if (e.hasMoreElements()) {
        out.println("Test FAILED.  The JSP Context Wrapper did not have a clean " +
            "page scope.");
        out.println("Attribute names found:");
        for (String name = (String) e.nextElement();
            e.hasMoreElements();
            name = (String) e.nextElement()) {
            out.println(name);
        }
    } else {
        String reqScope = (String) jspContext.getAttribute("requestScopeAttr", PageContext.REQUEST_SCOPE);
        String sessScope = (String) jspContext.getAttribute("sessionScopeAttr", PageContext.SESSION_SCOPE);
        String applScope = (String) jspContext.getAttribute("applicationScopeAttr", PageContext.APPLICATION_SCOPE);
        if ("requestScope".equals(reqScope)) {
            if ("sessionScope".equals(sessScope)) {
                if ("applicationScope".equals(applScope)) {
                    out.println("Wrapper Test PASSED");
                } else {
                    out.println("Test FAILED.  Invoking context application scope not " +
                        "totally available to the wrapping context.  Expected 'applicationScope'," +
                        " received: " + applScope);
                }
            } else {
                out.println("Test FAILED.  Invoking context session scope not " +
                    "totally available to the wrapping context.  Expected 'sessionScope'," +
                    " received: " + sessScope);
            }
        } else {
            out.println("Test FAILED.  Invoking context request scope not " +
                "totally available to the wrapping context.  Expected 'requestScope'," +
                " received: " + reqScope);
        }
        jspContext.setAttribute("requestScopeAttr", "requestScopeMod", PageContext.REQUEST_SCOPE);
        jspContext.setAttribute("sessionScopeAttr", "sessionScopeMod", PageContext.SESSION_SCOPE);
        jspContext.setAttribute("applicationScopeAttr", "applicationScopeMod", PageContext.APPLICATION_SCOPE);
        jspContext.removeAttribute("requestScopeRem");
        jspContext.removeAttribute("sessionScopeRem");
        jspContext.removeAttribute("applicationScopeRem");
        jspContext.setAttribute("pageScopeRem", "pageScopeRem");
        jspContext.removeAttribute("pageScopeRem");
        jspContext.setAttribute("pageAddAttr", "pageAdd");
        jspContext.setAttribute("reqAddAttr", "reqAdd", PageContext.REQUEST_SCOPE);
        jspContext.setAttribute("sessAddAttr", "sessAdd", PageContext.SESSION_SCOPE);
        jspContext.setAttribute("applAddAttr", "applAdd", PageContext.APPLICATION_SCOPE);
    }
%>
