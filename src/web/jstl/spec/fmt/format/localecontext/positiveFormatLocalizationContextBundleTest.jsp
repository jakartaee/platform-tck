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
<%@ page import="java.util.Date" %>
<tck:test testName="positiveFormatLocalizationContextBundleTest">
    <%  
        Date date = new Date(883192294202L);
        pageContext.setAttribute("dte", date);
    %>
    <fmt:setTimeZone value="EST"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.AlgoResources6"/>
    <c:set var="dt" value="Nov 21, 2000 3:45:02 AM"/>

    <!-- The localization context for formatting actions can
             be provided by wrapping these actions in a fmt:bundle
             action.  The Locale of the selected resource bundle
             will be used as the locale for the formatting action.
             Additionally, if the action is wrapped by the bundle action
             the javax.servlet.jsp.jstl.fmt.localizationContext config variable
             will not be considered. -->
    <fmt:bundle basename="com.sun.ts.tests.jstl.common.resources.Resources1">
        <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>' type="both" var="p2"/>
        <fmt:formatDate value='<%= (Date) pageContext.getAttribute("p2") %>' type="both"/>
        <fmt:parseNumber value="1,234"/>
        <fmt:formatNumber value="1234"/>
    </fmt:bundle>
</tck:test>
