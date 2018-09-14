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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocalizationContext" %>
<tck:test testName="positiveBundleResponseSettersTest">

    <c:if test="${param.action == 'bundle' and param.type == 'rt'}">
        <fmt:bundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>
    </c:if>

    <c:if test="${param.action == 'locale' and param.type == 'rt'}">
        <fmt:setLocale value="en"/>
    </c:if>

    <c:if test="${param.action == 'setbundle' && param.type == 'rt'}">
        <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                       var="tBundle" scope="application"/>
    </c:if>

    <c:if test="${param.action == 'message' and param.type == 'rt'}">
        <fmt:message key="mkey"
                        bundle='<%= (LocalizationContext) pageContext.getAttribute("tBundle", PageContext.APPLICATION_SCOPE) %>'/>
        <c:remove var="tBundle" scope="application"/>
    </c:if>
</tck:test>
