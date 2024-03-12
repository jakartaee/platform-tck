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
<tck:test testName="positivePDValueTest">
    <c:set var="dte" value="Nov 21, 2000"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>
    <!-- EL: Validate the the action can properly parse a date
             provided as a dynamic or static value. -->
    <fmt:parseDate value="${dte}" var="e1"/>
    <fmt:parseDate value="Nov 21, 2000" var="e2"/>
    <fmt:formatDate value="${e1}"/><br>
    <fmt:formatDate value="${e2}"/><br>

    <!-- RT: Validate the the action can properly parse a date
             provided as a dynamic or static value. -->
    <fmt_rt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="r1"/>
    <fmt_rt:parseDate value="Nov 21, 2000" var="r2"/>
    <fmt:formatDate value="${r1}"/>
    <fmt:formatDate value="${r2}"/>
</tck:test>
