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
<tck:test testName="negativeImportRequestDispatcherServletExceptionTest">

    <!-- If RequestDispatcher.include() method throws a ServletException
             and a root cause exists, a JspException is thrown with the
             root cause message as the exception message and the original
             root cause as the JspException root cause.  If no root
             cause is present, the exception text will be included in the
             message and the original exception as the root cause. -->
    <tck:catch var="riae" exception="javax.servlet.jsp.JspException"
               checkRootCause="true" rootException="java.lang.IllegalStateException"
               exceptionText="root message">
        <c:import url="ServletExceptionRootCause.jsp"/>
    </tck:catch>
    <c:out value="${riae}" default="Test FAILED" escapeXml="false"/><br>
    <br>
    <tck:catch var="rse" exception="javax.servlet.jsp.JspException"
               checkRootCause="true" rootException="javax.servlet.ServletException"
               exceptionText="thrown from included resource">
        <c:import url="ServletException.jsp"/>
    </tck:catch>
    <c:out value="${rse}" default="Test FAILED" escapeXml="false"/><br>
</tck:test>
