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
<%@ page import="javax.xml.transform.*,javax.xml.transform.stream.*,java.io.*" %>
<tck:test testName="positiveTransformResultTest">
    <c:import url="simple2.xml" var="xmlDoc"/>
    <c:import url="simple2.xsl" var="xslDoc"/>
    <%
        ByteArrayOutputStream eBoa = new ByteArrayOutputStream(128);
        ByteArrayOutputStream rBoa = new ByteArrayOutputStream(128);
        Result eResult = new StreamResult(eBoa);
        Result rResult = new StreamResult(rBoa);
        pageContext.setAttribute("eResult", eResult);
        pageContext.setAttribute("rResult", rResult);
    %>

   <!-- The transform action can accept a Result object which
             acts as a holder for the transformed result. -->
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'
                    result='<%= (Result) pageContext.getAttribute("rResult") %>'/>
    Result:<br>
    <%
        StreamResult postResult = (StreamResult) pageContext.getAttribute("rResult");
        ByteArrayOutputStream os = (ByteArrayOutputStream) postResult.getOutputStream();
        out.println(os.toString("ISO-8859-1"));
    %>
</tck:test>
