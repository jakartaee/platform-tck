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
<tck:test testName="positivePNParseLocaleTest">
    <% 
        pageContext.setAttribute("loc", new Locale("en","US"));
    %>
    <c:set var="us" value="en_US"/>
    <fmt:setLocale value="fr_FR"/>

    <!-- Validate the behavior of the parseNumber action when using
             the parseLocale attribute.  Pass in both String and Locale 
             objects. This will also validate that the presence of the
             parseLocale attribute will override that of the page. -->
    Number: <fmt:parseNumber value="1,234" parseLocale='<%= (String) pageContext.getAttribute("us") %>'/>
    Number: <fmt:parseNumber value="1,234" parseLocale='<%= (Locale) pageContext.getAttribute("loc") %>' type="number"/>
    Currency: <fmt:parseNumber value="$1,234.00" parseLocale="en_US" type="currency"/>
    Percent: <fmt:parseNumber value="1.5%" parseLocale="en_US" type="percent"/>
</tck:test>
