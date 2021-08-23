<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ tag import="com.sun.ts.tests.jsp.common.util.JspTestUtil" %>
<%@ variable name-given="begin" variable-class="java.lang.Integer" scope="AT_BEGIN" %>
<%@ variable name-given="end" variable-class="java.lang.Integer" scope="AT_END" %>
<%@ variable name-given="nested" variable-class="java.lang.Integer" scope="NESTED" %>

<%
    Integer b = (Integer) jspContext.getAttribute("begin");
    Integer n = (Integer) jspContext.getAttribute("nested");
    Integer e = (Integer) jspContext.getAttribute("end");

    if (b == null) {
        jspContext.setAttribute("begin", new Integer(3));
    } else {
        // error
        jspContext.getOut().println("Test FAILED.  Attribute 'begin' was " +
            " presentin the Tag's PageContext.\n  Available attributes: " +
            JspTestUtil.getAsString(jspContext.getAttributeNamesInScope(
                PageContext.PAGE_SCOPE)));
        return;
    }

    if (n == null) {
        jspContext.setAttribute("nested", new Integer(9));
    } else {
        jspContext.getOut().println("Test FAILED.  Attribute 'nested' was " +
            "present in the Tag's PageContext.");
        return;
    }

    if (e == null) {
        jspContext.setAttribute("end", new Integer(1));
    } else {
        jspContext.getOut().println("Test FAILED.  Attribute 'end' was " +
            "present in the Tag's PageContext.");
        return;
    }

%>
