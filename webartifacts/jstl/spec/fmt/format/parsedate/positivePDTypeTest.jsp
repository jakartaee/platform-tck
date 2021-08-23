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
<tck:test testName="positivePDTypeTest">
    <c:set var="dte" value="Nov 21, 2000"/>
    <c:set var="dtim" value="3:45:02 AM"/>
    <c:set var="dt" value="Nov 21, 2000 3:45:02 AM"/> 
    <c:set var="tim" value="time"/>
    <c:set var="dat" value="date"/>
    <c:set var="bot" value="both"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>

    <!-- the type attribute specifies the the type of date
             information is contained in the value to be parsed. -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="r1"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                             type='<%= (String) pageContext.getAttribute("dat") %>' var="r2"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                             type="date" var="r3"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dtim") %>'
                             type='<%= (String) pageContext.getAttribute("tim") %>' var="r4"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dtim") %>'
                             type="time" var="r5"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>'
                             type='<%= (String) pageContext.getAttribute("bot") %>' var="r6"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>'
                             type="both" var="r7"/>
    date: <fmt:formatDate value="${r1}" type="date"/><br>
    date: <fmt:formatDate value="${r2}" type="date"/><br>
    date: <fmt:formatDate value="${r3}" type="date"/><br>
    time: <fmt:formatDate value="${r4}" type="time"/><br>
    time: <fmt:formatDate value="${r5}" type="time"/><br>
    both: <fmt:formatDate value="${r6}" type="both"/><br>
    both: <fmt:formatDate value="${r7}" type="both"/><br>
    
</tck:test>
