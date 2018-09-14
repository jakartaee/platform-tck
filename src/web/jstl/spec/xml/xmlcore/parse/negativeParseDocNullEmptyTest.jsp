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
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="negativeParseDocNullEmptyTest">

    <!-- If the doc attribute value is null or emtpy,
              a JspException is thrown. -->
    <tck:catch var="rex1" exception="javax.servlet.jsp.JspException"><br>
        <x:parse var="doc" doc="${null}"/>
    </tck:catch>
    <c:out value="${rex1}" default="Test FAILED" escapeXml="false"/><br>
    
    <tck:catch var="rex2" exception="javax.servlet.jsp.JspException">
        <x:parse var="doc" doc=""/>
    </tck:catch>
    <c:out value="${rex2}" default="Test FAILED" escapeXml="false"/>
</tck:test>
