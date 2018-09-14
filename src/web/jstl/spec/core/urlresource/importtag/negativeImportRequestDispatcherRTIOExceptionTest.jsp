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
<tck:test testName="negativeImportRequestDispatcherRTIOExceptionTest">

    <!-- If RequestDispather.include() method throws an IOException
             or RuntimeException, a JspException must be thrown
             with the message of the original exception included in the
             message, and the original Exception as the root cuase. -->
    IOException:<br>
    <tck:catch var="rioe" exception="javax.servlet.jsp.JspException"
               exceptionText="thrown from included resource"
               checkRootCause="true" rootException="java.io.IOException">
        <c:import url="IOException.jsp"/>
    </tck:catch>
    <c:out value="${rioe}" default="Test FAILED" escapeXml="false"/><br>
    ServletException:<br>
    <tck:catch var="rrte" exception="javax.servlet.jsp.JspException"
               exceptionText="thrown from included resource"
               checkRootCause="true" rootException="java.lang.RuntimeException">
        <c:import url="RuntimeException.jsp"/>
    </tck:catch>
    <c:out value="${rrte}" default="Test FAILED" escapeXml="false"/><br>
</tck:test>
