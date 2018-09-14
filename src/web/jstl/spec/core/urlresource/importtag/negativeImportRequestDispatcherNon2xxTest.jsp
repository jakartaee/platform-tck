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
<tck:test testName="negativeImportRequestDispatcherNon2xxTest">

    <!-- If the resource invoked by RequestDispatcher.include()
             sets a response code other than 2xx, a JspTagException
             is thrown with the status code and path in the message -->
    Check the message:<br>
    <tck:catch var="ex" exception="javax.servlet.jsp.JspException"
               exceptionText="/">
       <c:import url="/ResponseScGone.jsp"/>
    </tck:catch>
    <c:out value="${ex}" default="Test FAILED" escapeXml="false"/>
    Check the status code:<br>
    <tck:catch var="scode" exception="javax.servlet.jsp.JspException"
               exceptionText="410">
        <c:import url="ResponseScGone.jsp"/>
    </tck:catch>
    <c:out value="${scode}" default="Test FAILED" escapeXml="false"/>
</tck:test>
