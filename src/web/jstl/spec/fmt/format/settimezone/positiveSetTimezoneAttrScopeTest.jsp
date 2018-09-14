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
<tck:test testName="positiveTimezoneAttrScopeTest">

    <!-- If scope is specified and var is not, the
             javax.servlet.jsp.jstl.fmt.timeZone attribute will
             be set to the scope specified (implicit or explicit). -->
    <fmt:setTimeZone value="PST"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.timeZone" useConfig="true"/>
    <c:remove var="javax.serlvet.jsp.jstl.fmt.timeZone.page"/>
    <fmt:setTimeZone value="PST" scope="page"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.timeZone" useConfig="true"/>
    <c:remove var="javax.servlet.jsp.jstl.fmt.timeZone.page"/>
    <fmt:setTimeZone value="PST" scope="request"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.timeZone" inScope="request" useConfig="true"/>
    <c:remove var="javax.servlet.jsp.jstl.fmt.timeZone.request"/>
    <fmt:setTimeZone value="PST" scope="session"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.timeZone" inScope="session" useConfig="true"/>
    <c:remove var="javax.servlet.jsp.jstl.fmt.timeZone.session"/>
    <fmt:setTimeZone value="PST" scope="application"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.timeZone" inScope="application" useConfig="true"/>
    <c:remove var="javax.servlet.jsp.jstl.fmt.timeZone.applciation"/>
</tck:test>
