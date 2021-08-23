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
<%@ page import="java.io.*,javax.xml.transform.*,javax.xml.transform.stream.*" %>
<tck:test testName="positiveTransformXmlInputTest">
    <%-- START TEST INITIALIZATION --%>
    <c:import url="simple.xml" var="xmlDoc"/>
    <c:import url="simple.xsl" var="xslDoc"/>
    <c:import url="simple2.xml" var="xmlDoc2"/>
    <c:import url="simple3.xsl" var="xslDoc2"/>
    <%
        // init reader
        Reader eXmlReader = new StringReader((String) pageContext.getAttribute("xmlDoc"));
        Reader rXmlReader = new StringReader((String) pageContext.getAttribute("xmlDoc"));

        // init javax.xml.transform.Source
        Source eSource = new StreamSource(
                  application.getResourceAsStream("/simple.xml"));
        Source rSource = new StreamSource(
                  application.getResourceAsStream("/simple.xml"));
        pageContext.setAttribute("eReader", eXmlReader);
        pageContext.setAttribute("rReader", rXmlReader);
        pageContext.setAttribute("eSource", eSource);
        pageContext.setAttribute("rSource", rSource);
    %>
    <x:parse var="parsedDoc" doc="${xmlDoc}"/>
    <x:set var="setVar" select="$parsedDoc//c"/>
    <%-- STOP TEST INITIALIZATION --%>

    <%-- The xml attribute must be able to accept the following
             input sources:
                - String
                - Reader
                - javax.xml.transform.Source
                - Object exported by:
                   + x:parse
                   + x:set
                   + x:transform --%>
    In all cases the resulting text should be wrapped by &lt;h4&gt; elements:<br>
    Input from String:<br>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>

    Input from Reader:<br>
    <x:transform doc='<%= pageContext.getAttribute("rReader") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>

    Input from javax.xml.transform.Source:<br>
    <x:transform doc='<%= pageContext.getAttribute("rSource") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>

    Input from result of x:parse action:<br>
    <x:transform doc='<%= pageContext.getAttribute("parsedDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>

    Input from result of x:set action:<br>
    <x:transform doc='<%= pageContext.getAttribute("setVar") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>

    Input from x:transform action:<br>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc2") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc2") %>' var="t1"/>
    <x:transform doc='<%= pageContext.getAttribute("t1") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'/><br>
</tck:test>
