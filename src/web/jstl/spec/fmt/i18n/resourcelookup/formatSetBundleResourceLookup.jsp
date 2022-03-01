<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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

<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="jakarta.servlet.jsp.jstl.core.Config" %>
<tck:test testName="formatBundleResourceLookup">
    <!-- Lookups are performed in the order of the preferred locales provided
         by the client -->
    <tck:config configVar="fallback" value='<%= request.getParameter("fall") %>'
                op="set"/>
    <fmt:setBundle var="ctxbundle" basename="com.sun.ts.tests.jstl.common.resources.${param.res}"/>
    <fmt:message bundle="${ctxbundle}" key="mkey" var="fmessage"/>
    <%
        response.addHeader("message", (String) pageContext.getAttribute("fmessage"));
    %>
</tck:test>
