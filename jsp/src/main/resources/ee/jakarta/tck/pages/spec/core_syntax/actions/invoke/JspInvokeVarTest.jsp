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

<%@ page contentType="text/plain" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<tags:JspInvokeVarTag>
    <jsp:attribute name="attr1">
        attribute value
    </jsp:attribute>
</tags:JspInvokeVarTag>

<%
    Object o = pageContext.getAttribute("pageVar", PageContext.REQUEST_SCOPE);
    if (o != null) {
        if (o instanceof java.lang.String) {
            if (o.equals("attribute value")) {
                out.println("Test PASSED");
            } else {
                out.println("Test FAILED.  The exported result of jsp:invoke" +
                    " was not the expected value of 'attribute value'.  " +
                    "Received: " + o);
            }
        } else {
            out.println("Test FAILED.  The exported result of jsp:invoke" +
                " using the 'var' attribute of the jsp:invoke action was not" +
                " of type 'java.lang.String'.  Received: " + o.getClass().getName());
        }
    } else {
        out.println("Test FAILED.  Unabled to find the page scoped variable, " +
            "'pageVar', that should have been exported by the jsp:invoke action.");
    }
%>
