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
<%@ page import="java.util.TimeZone" %>
<tck:test testName="positivePDTimeZoneTest">
    <%
        pageContext.setAttribute("tz", TimeZone.getTimeZone("PST"));
    %>
    <c:set var="dte" value="Nov 21, 2000 3:45 AM"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>

    <!-- The time zone to be applied to the formatted value can
             be explicitly provided to the action.  This will effectively
             overried the timezone of the page -->
    <br>Page is using EST for the timezone.  The formatting action will use PST.  Value should be offset 3 hours.<br>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' type="both" timeStyle="short" var="rdef"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                       timeZone='<%= (TimeZone) pageContext.getAttribute("tz") %>' type="both" timeStyle="short" var="rpst1"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                       timeZone="PST" type="both" timeStyle="short" var="rpst2"/>
    No timeZone attribute: <fmt:formatDate value="${rdef}" timeZone="EST" type="both" timeStyle="short"/><br>
    <fmt:formatDate value="${rpst1}" timeZone="EST" type="both" timeStyle="short"/><br>
    <fmt:formatDate value="${rpst2}" timeZone="EST" type="both" timeStyle="short"/><br>
</tck:test>
