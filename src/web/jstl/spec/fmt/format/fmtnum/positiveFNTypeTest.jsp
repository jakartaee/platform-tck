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
<tck:test testName="positiveFNTypeTest">
    <c:set var="num" value="number"/>
    <c:set var="cur" value="currency"/>
    <c:set var="per" value="percent"/>
    <fmt:setLocale value="en_US"/>

    <!-- The type attribute controls how the value is to be
             formatted (number, currency, percent).  If not
             specified, the default is number -->
    <fmt:formatNumber value="123456"/>
    <fmt:formatNumber value="123456" type="number"/>
    <fmt:formatNumber value="123456" type='<%= (String) pageContext.getAttribute("num") %>'/>
    <fmt:formatNumber value="123456" type="currency"/>
    <fmt:formatNumber value="123456" type='<%= (String) pageContext.getAttribute("cur") %>'/>
    <fmt:formatNumber value="123456" type="percent"/>
    <fmt:formatNumber value="123456" type='<%= (String) pageContext.getAttribute("per") %>'/>
</tck:test>
