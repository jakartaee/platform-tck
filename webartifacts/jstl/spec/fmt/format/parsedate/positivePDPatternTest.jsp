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

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tck:test testName="positivePDPatternTest">
    <c:set var="pat" value="yyyy.MM.dd G 'at' HH:mm:ss z"/>
    <c:set var="dte" value="2000.11.21 AD at 03:45:02 EST"/>
    <fmt:setLocale value="en_US"/>
    <fmt:setTimeZone value="EST"/>

    <!-- the pattern attribute specifies a custom pattern
             to be applied when parsing the provided date. -->
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                       pattern='<%= (String) pageContext.getAttribute("pat") %>' var="r1"/>
    <fmt:parseDate value='<%= (String) pageContext.getAttribute("dte") %>'
                       pattern="yyyy.MM.dd G 'at' HH:mm:ss z" var="r2"/>
    <fmt:formatDate value="${r1}" timeZone="EST" pattern="${pat}"/><br>
    <fmt:formatDate value="${r2}" timeZone="EST" pattern="${pat}"/><br>
</tck:test>
