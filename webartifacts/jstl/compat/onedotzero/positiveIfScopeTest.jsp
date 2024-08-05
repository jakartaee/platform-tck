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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveIfScopeTest">
    <%-- check scope behavior of EL tags --%>
    <%
        pageContext.setAttribute("bTrue", new Boolean("true"));
    %>
    <c:if test="${bTrue}" var="eVar0"/>
    <c:if test="${bTrue}" var="eVar1" scope="page"/>
    <c:if test="${bTrue}" var="eVar2" scope="request"/>
    <c:if test="${bTrue}" var="eVar3" scope="session"/>
    <c:if test="${bTrue}" var="eVar4" scope="application"/>
    <tck:checkScope varName="eVar0"/>
    <tck:checkScope varName="eVar1"/>
    <tck:checkScope varName="eVar2" inScope="request"/>
    <tck:checkScope varName="eVar3" inScope="session"/>
    <tck:checkScope varName="eVar4" inScope="application"/>
    <c:remove var="eVar4" scope="application"/>


    <%-- check scope behavior of RT tags --%>
    <c_rt:if test="<%= true %>" var="rVar0"/>
    <c_rt:if test="<%= true %>" var="rVar1" scope="page"/>
    <c_rt:if test="<%= true %>" var="rVar2" scope="request"/>
    <c_rt:if test="<%= true %>" var="rVar3" scope="session"/>
    <c_rt:if test="<%= true %>" var="rVar4" scope="application"/>
    <tck:checkScope varName="rVar0"/>
    <tck:checkScope varName="rVar1"/>
    <tck:checkScope varName="rVar2" inScope="request"/>
    <tck:checkScope varName="rVar3" inScope="session"/>
    <tck:checkScope varName="rVar4" inScope="application"/>
    <c:remove var="rVar4" scope="application"/>

</tck:test>
