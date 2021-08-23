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
<tck:test testName="positivePDScopeTest">
    <c:set var="dte" value="Nov 21, 2000 3:45:02 AM"/>
    <fmt:setLocale value="en_US"/>

    <!-- The presence of the scope attribute will cause var to be
             exported to the available scopes of the PageContext.
             If scope is not specified, var will be exported to the
             page context by default. -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="riPage"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="rePage" scope="page"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="reRequest" scope="request"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="reSession" scope="session"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="reApplication" scope="application"/>
    <tck:checkScope varName="riPage"/>
    <tck:checkScope varName="rePage"/>
    <tck:checkScope varName="reRequest" inScope="request"/>
    <tck:checkScope varName="reSession" inScope="session"/>
    <tck:checkScope varName="reApplication" inScope="application"/>
    <c:remove var="reApplication" scope="application"/>
</tck:test>
