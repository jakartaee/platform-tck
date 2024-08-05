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

<%@ attribute name="x" required="true" %>
<%
    Object o = jspContext.getAttribute("x", PageContext.PAGE_SCOPE);
    if(o instanceof jakarta.servlet.jsp.tagext.JspFragment) {
        out.println("Test FAILED. attr x is a fragment type.");
    } else if(o instanceof java.lang.String) {
        out.println("Test PASSED. attr x is of type java.lang.String.");
    } else {
        out.println("Test FAILED. attr x is " + o);
    }
%>


