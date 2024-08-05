<%--

    Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.

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
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="negativeFNUnableToParseValueTest">

    <!-- If the action is unable to parse the String value provided
             when attempting to format it, an JspException is thrown, with
             unparsable value present in the exception text and the original
             exception as the root cause. -->
    <tck:catch var="ex" exception="jakarta.servlet.jsp.JspException" exceptionText="abcd"
               checkRootCause="true" rootException="java.lang.IllegalArgumentException">
        <fmt:formatNumber value="abcd"/>
    </tck:catch>
    <c:out value="${ex}" default="Test FAILED" escapeXml="false"/>
</tck:test>
