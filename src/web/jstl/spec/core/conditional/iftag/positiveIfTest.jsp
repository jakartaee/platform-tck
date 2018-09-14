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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveIfTest">
    <% 
        pageContext.setAttribute("bTrue", new Boolean("true"));
        pageContext.setAttribute("bFalse", new Boolean("false"));
        pageContext.setAttribute("iOne", new Integer("1"));
    %>
    <c:if test="${bTrue == true}" var="vTrue"/>
    <c:if test="${bFalse == true}" var="vFalse"/>
    <c:if test="${iOne > 0}" var="iCheckTrue"/>
    <c:if test="${iOne < 0}" var="iCheckFalse"/>

    <c:out value="${vTrue}" default="IF Test FAILED"/><br>
    <c:out value="${vFalse}" default="IF Test FAILED"/><br>
    <c:out value="${iCheckTrue}" default="IF Test FAILED"/><br>
    <c:out value="${iCheckFalse}" default="IF Test FAILED"/><br>

    <c:if test="${bTrue}" var="vTrue2"/>
    <c:if test="${vFalse}" var="vFalse2"/>

    <c:out value="${vTrue}" default="IF Test FAILED"/><br>
    <c:out value="${vFalse}" default="IF Test FAILED"/><br>

    <c:if test='<%= true %>' var="sTrue"/>
    <c:if test='<%= false %>' var="sFalse"/>

    <c:out value="${sTrue}" default="IF Test FAILED"/><br>
    <c:out value="${sFalse}" default="IF Test FAILED"/><br>
</tck:test>
