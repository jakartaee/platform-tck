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
<tck:test testName="positiveImportCharEncodingNullEmptyTest">
    <!-- If charEncoding is null or empty, the action will
             behave as if it wasn't specified. -->
    <c:import var="rorig" url="import.txt"/>
    <c:import var="rimp1" url="import.txt" charEncoding='<%= null %>'/>
    <c:import var="rimp2" url="import.txt" charEncoding=""/>

    <c:choose>
        <c:when test="${rorig == rimp1 && rorig == rimp2}">
            <pre>Test PASSED.  The result of the import with charEncoding
                 specified as null or empty, is the same as if it was
                 not specified.</pre>
        </c:when>
        <c:otherwise>
            <pre>Test FAILED.  The result of the import with charEncoding
                 specified as null or empty, is not the same as if it
                 was not specified
                 Not Specified: <c:out value="${rorig}" default="Nothing Imported"/>
                 Null: <c:out value="${rimp1}" default="Nothing Imported"/>
                 Empty: <c:out value="${rimp2}" default="Nothing Imported"/></pre>
        </c:otherwise>
    </c:choose>
</tck:test>
