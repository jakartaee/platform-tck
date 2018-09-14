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

<%
    JspContext invokingContext =
        (JspContext) request.getAttribute("INVOKING_CONTEXT");

    JspContext tagContext = this.getJspContext();

    if (invokingContext != null) {
        if (tagContext != null) {
            if (tagContext instanceof PageContext) {
                if (tagContext != invokingContext) {
                    if (jspContext == tagContext) {
                        out.println("Test PASSED");
                    } else {
                        out.println("Test FAILED.  The jspContext scripting" +
                            " variable is not the same context as that returned " +
                            "by getJspContext()");
                    }
                } else {
                    out.println("Test FAILED.  The JSP Context Wrapper is the " +
                        "same object as the invoking JSP Context.");
                }
            } else {
                out.println("Test FAILED.  The JSP Context Wrapper returned by" +
                    " getJspContext() should have been an instance of PageContext\n" +
                    " as the invoking JSP Context is Servlet-based.");
            }
        } else {
            out.println("Test FAILED.  The invocation of getJspContext() " +
                "returned null.");
        }
    } else {
        out.println("Test FAILED.  Unable to find the invoking PageContext in the" +
            " request object.");
    }
%>
