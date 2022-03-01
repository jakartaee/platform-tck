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

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveBeginTest">
    <%
        String tString = "0|1|2|3|4|5|6|7|8|9";
        pageContext.setAttribute("str", tString);
        int iBegin = 5;
    %>

    <!-- support of 'begin' with items - begin = 5 : dynamic -->
    <c:forTokens var="eVar" items='<%= tString %>' delims="|" begin='<%= iBegin %>'>
        <c:out value="${eVar}" default="forEach Test FAILED"/>
    </c:forTokens>
    <!-- support of 'begin' with items - begin = 5 : static-->
    <c:forTokens var="eVar" items='<%= tString %>' delims="|" begin="5">
        <c:out value="${eVar}" default="forEach Test FAILED"/>
    </c:forTokens>

</tck:test>
