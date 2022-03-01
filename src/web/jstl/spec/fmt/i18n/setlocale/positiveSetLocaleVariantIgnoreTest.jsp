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

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="java.util.Locale" %>
<tck:test testName="positiveSetLocaleVariantIgnoreTest">
    <% 
        Locale loc = new Locale("en", "US");
        pageContext.setAttribute("loc", loc);
    %>

    <!-- If value is of type java.util.Locale, then variant,
             if supplied, will be ignored. -->
    <fmt:setLocale value='<%= (Locale) pageContext.getAttribute("loc") %>' variant="INVALID"/>
    <tck:config var="locVar" configVar="locale" op="get"/>
    <c:out value="${locVar}" default="Test FAILED"/>
</tck:test>
