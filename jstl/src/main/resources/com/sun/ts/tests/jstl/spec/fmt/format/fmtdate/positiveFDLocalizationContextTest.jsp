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

<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.Date" %>
<tck:test testName="positiveFDLocalizationContextTest">
    <%  
        Date date = new Date(883192294202L);
        pageContext.setAttribute("dte", date);
    %>
    <fmt:setTimeZone value="EST"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>

    <!-- Validate that the action is able to dermine the
             formatting locale based on the localizationContext configuration
             variable. -->
    <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>'/>
    <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="time"/>
    <fmt:formatDate value='<%= (Date) pageContext.getAttribute("dte") %>' type="both"/>
</tck:test>
