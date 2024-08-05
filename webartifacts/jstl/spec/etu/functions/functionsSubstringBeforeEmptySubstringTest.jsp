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
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="string1" value="This is first String." />
<c:choose>
    <c:when test="${'This is ' == fn:substringBefore(string1, 'first')}">
        Test PASSED
    </c:when>
    <c:otherwise>
        Test FAILED.  Expected fn:substringBefore(string1, 'first') to return
        'This is'.  Actual return value: ${fn:substringBefore(string1, 'first')}
    </c:otherwise>
</c:choose>
