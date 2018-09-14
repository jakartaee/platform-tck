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
<tck:test testName="positiveTransformScopeTest">
    <c:import url="simple2.xml" var="xmlDoc"/>
    <c:import url="simple2.xsl" var="xslDoc"/>

    <!-- Scope specifies the scope to which var is exported
             in the PageContext.  If scope is not specified, 
             then var is exported to the page scope. -->
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="riPage"/>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="rePage" scope="page"/>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="reRequest" scope="request"/>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="reSession" scope="session"/>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="reApplication" scope="application"/>
    <tck:checkScope varName="riPage"/>
    <tck:checkScope varName="rePage" inScope="page"/>
    <tck:checkScope varName="reRequest" inScope="request"/>
    <tck:checkScope varName="reSession" inScope="session"/>
    <tck:checkScope varName="reApplication" inScope="application"/>
    <c:remove var="reApplication" scope="application"/>
</tck:test>
