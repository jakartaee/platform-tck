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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.Date" %>
<tck:test testName="positiveFormatLocalizationContextI18NTest">
    <%  
        Date date = new Date(883192294202L);
        pageContext.setAttribute("dte", date);
    %>
    <fmt:setTimeZone value="EST"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources1"/>
    <fmt:setLocale value="de_DE"/>
    <c:set var="dt" value="Nov 21, 2000 3:45:02 AM"/>
    <!-- EL: If the javax.servlet.jstl.fmt.localizationContext scoped attribute is available,
             this will be used in preference to the javax.servlet.jstl.fmt.locale
             attribute. -->
    <fmt:parseDate value="${dt}" type="both" var="p1"/>
    <fmt:formatDate value="${p1}" type="both"/>
    <fmt:parseNumber value="1,234"/>
    <fmt:formatNumber value="1234"/>

    <!-- EL: If the javax.servlet.jstl.fmt.localizationContext scoped attribute is available,
             this will be used in preference to the javax.servlet.jstl.fmt.locale
             attribute. -->
    <fmt_rt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>' type="both" var="p2"/>
    <fmt_rt:formatDate value='<%= (Date) pageContext.getAttribute("p2") %>' type="both"/>
    <fmt_rt:parseNumber value="1,234"/>
    <fmt_rt:formatNumber value="1234"/>
</tck:test>
