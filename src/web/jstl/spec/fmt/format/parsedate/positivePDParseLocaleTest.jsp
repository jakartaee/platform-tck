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
<%@ page import="java.util.Locale" %>
<tck:test testName="positivePDParseLocaleTest">
    <%
        pageContext.setAttribute("loc", new Locale("en", "US"));
    %>
    <c:set var="dte" value="Nov 21, 2000 3:45:02 AM"/>
    <c:set var="us" value="en_US"/>
    <fmt:setLocale value="de_DE"/>
    <fmt:setTimeZone value="EST"/>

    <!-- parseLocale specifies the locale specific formatting pattern
             to apply when parsing the provided value.  The presence of this
             attribute will override the page locale. Also validate that
             parseLocale attribute can accept both Strings and Locale objects. -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                      parseLocale='<%= (String) pageContext.getAttribute("us") %>' type="both"/><br>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                      parseLocale='<%= (Locale) pageContext.getAttribute("loc") %>' type="both"/><br>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' parseLocale="en_US" type="both"/><br>
</tck:test>
