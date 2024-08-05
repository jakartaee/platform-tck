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
<tck:test testName="positiveCWOTest">

    <!-- Simple validation that when's body content is processed
         if the test evaluates to true -->
    <c:choose>
        <c:when test="<%= true %>">
            PASSED<br>
        </c:when>
    </c:choose>

    <!-- Validate that the first when that evaluates to true processes
         its body content -->
    <c:choose>
        <c:when test="<%= false %>">
            FAILED<br>
        </c:when>
        <c:when test="<%= true %>">
            PASSED<br>
        </c:when>
        <c:when test="<%= true %>">
            FAILED<br>
        </c:when>
    </c:choose>

    <!-- Validate otherwise is processed if no when case evaluates to true -->
    <c:choose>
        <c:when test="<%= false %>">
            FAILED<br>
        </c:when>
        <c:when test="<%= false %>">
            FAILED<br>
        </c:when>
        <c:otherwise>
            PASSED<br>
        </c:otherwise>
    </c:choose>
</tck:test>
