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
<tck:test testName="positiveLoopTagStatusTest">
    <%
        String[] sArray = { "one", "two", "three" };
        pageContext.setAttribute("sArray", sArray);
    %>
    <c:forEach varStatus="status" items="${sArray}"
               begin="0" end="2" step="1">
        <c:out value="${status.current}" default="forEach Test FAILED"/><br>
        <c:out value="${status.index}" default="forEach Test FAILED"/><br>
        <c:out value="${status.count}" default="forEach Test FAILED"/><br>
        <c:out value="${status.first}" default="forEach Test FAILED"/><br>
        <c:out value="${status.last}" default="forEach Test FAILED"/><br>
        <c:out value="${status.begin}" default="forEach Test FAILED"/><br>
        <c:out value="${status.end}" default="forEach Test FAILED"/><br>
        <c:out value="${status.step}" default="forEach Test FAILED"/><br>
    </c:forEach>

    <!-- Validate results when 'begin', 'end', and/or 'step' is not specified -->
    <c:forEach varStatus="status" items="1">
        <c:if test="${status.begin == null}">
            Begin not specified
        </c:if>
        <c:if test="${status.end == null}">
            End not specified
        </c:if>
        <c:if test="${status.step == null}">
            Step not specified
        </c:if>
    </c:forEach>

</tck:test>

