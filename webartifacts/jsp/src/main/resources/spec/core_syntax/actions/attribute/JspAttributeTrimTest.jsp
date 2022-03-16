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

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/attribute" prefix="attr" %>

<%
    pageContext.setAttribute("trimString", " not trimmed  ");
%>

<attr:attribute expected=" not trimmed ">
    <jsp:attribute name="attribute" trim="false"> not trimmed </jsp:attribute>
</attr:attribute>

<attr:attribute expected="trimmed">
    <jsp:attribute name="attribute" trim="true">
         trimmed
    </jsp:attribute>
</attr:attribute>

<attr:attribute expected="trimmed">
    <jsp:attribute name="attribute">
        trimmed
    </jsp:attribute>
</attr:attribute>

<%--
    Trimming should not occur for runtime values no matter the value of
    the trim attribute.
--%>

<attr:attribute expected=" not trimmed  ">
    <jsp:attribute name="attribute" trim="true">${trimString}</jsp:attribute>
</attr:attribute>

<attr:attribute expected=" not trimmed  ">
    <jsp:attribute name="attribute" trim="false">${trimString}</jsp:attribute>
</attr:attribute>

<attr:attribute expected=" not trimmed  ">
    <jsp:attribute name="attribute">${trimString}</jsp:attribute>
</attr:attribute>
