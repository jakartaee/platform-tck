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
<tck:test testName="positivePDLocalizationContextTest">
    <fmt:setTimeZone value="EST"/>
    <c:set var="dte" value="Nov 21, 2000"/>
    <c:set var="dtim" value="3:45:02 AM"/>
    <c:set var="dt" value="Nov 21, 2000 3:45:02 AM"/> 
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>

    <!-- Validate that the action is able to dermine the
             formatting locale based on the localizationContext configuration
             variable. -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>' var="r1"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dtim") %>' type="time" var="r2"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>' type="both" var="r3"/>
    <fmt:formatDate value="${r1}" timeZone="EST"/><br>
    <fmt:formatDate value="${r2}" timeZone="EST" type="time"/><br>
    <fmt:formatDate value="${r3}" timeZone="EST" type="both"/><br>

</tck:test>
