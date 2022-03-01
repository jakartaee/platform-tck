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
<tck:test testName="negativeIfExcBodyContentTest">
    <!-- Validate exceptions caused by the body content are propagated. -->
    <tck:catch var="rex" exception="java.lang.IllegalArgumentException">
        <c:if test='<%= true %>'>
            <tck:throwit/>
        </c:if>
    </tck:catch>
    <c:out value="${rex}" default="Test FAILED" escapeXml="false"/>
</tck:test>
