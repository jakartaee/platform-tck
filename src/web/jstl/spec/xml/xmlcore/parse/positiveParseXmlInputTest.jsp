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
<%@ page import="java.io.Reader,java.io.StringReader" %>
<tck:test testName="positiveParseXmlInputTest">
    <c:set var="sXml" value="<test attr=\"value\">xmltext</test>"/>
    <%
        Reader r = new StringReader("<test attr=\"value\">xmltext</test>");
        pageContext.setAttribute("eReader" , r);
        Reader rt = new StringReader("<test attr=\"value\">xmltext</test>");
        pageContext.setAttribute("rReader" , rt);
    %>

    <!-- The parse action can accept input from multiple sources.
              - String
              - Reader -->
    Parse String:<br>
    <x:parse var="doc" doc='<%= (String) pageContext.getAttribute("sXml") %>'/>
    <x:out select="$doc//test"/>
    <br>

    Parse Reader:<br>
    <x:parse var="doc" doc='<%= (Reader) pageContext.getAttribute("rReader") %>'/>
    <x:out select="$doc//test"/>
    <br>
</tck:test>
