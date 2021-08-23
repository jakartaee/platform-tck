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
<tck:test testName="positivePDTimeZoneNullEmpytTest">
    <c:set var="dt" value="Nov 21, 2000 3:45 AM"/> 
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="MST"/>

    <!-- If timeZone is null or empty, it will be treated as
             if it was not present (only type short times is specified
             as any other style has no impact on the result). -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>'
                       timeZone='<%= null %>' type="both" timeStyle="short" var="rn1"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dt") %>'
                       timeZone="" type="both" timeStyle="short" var="re1"/>
    <fmt:formatDate value="${rn1}" timeZone="EST" type="both" timeStyle="short"/><br>
    <fmt:formatDate value="${re1}" timeZone="EST" type="both" timeStyle="short"/><br>
</tck:test>
