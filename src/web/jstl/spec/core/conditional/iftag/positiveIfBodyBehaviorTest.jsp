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
<tck:test testName="positiveIfBodyBehaviorTest">

    <% 
        pageContext.setAttribute("bTrue", new Boolean("true"));
        pageContext.setAttribute("bFalse", new Boolean("false"));
    %>
    <%-- body content with no var attribute --%>
    <c:if test="${bTrue}">
        PASSED<br>
    </c:if>
    <c:if test="${bFalse}">
        FAILED<br>
    </c:if>
    
    <%-- body content with the var attribute --%>
    <c:if test="${bTrue}" var="var0">
        PASSED<br>
    </c:if>
    <c:if test="${bFalse}" var="var1">
        FAILED<br>
    </c:if>
    <c:out value="${var0}" default="If Test FAILED"/><br>
    <c:out value="${var1}" default="If Test PASSED"/><br>
</tck:test>
