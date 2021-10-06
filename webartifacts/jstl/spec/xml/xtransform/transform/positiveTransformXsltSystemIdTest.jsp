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
<tck:test testName="positiveTransformXsltSystemIdTest">
    <tck:localAbsUrl var="exUrl" path="/jstl_xml_xform_web/import.xsl"/>
    <c:import url="simple.xml" var="xmlDoc"/>
    <c:import url="import.xsl" var="externXsl"/>

    <!-- The presence of xmlSystemId should cause the transform
             action to resolve external entities. -->
    Result should display node types of the provided XML document:<br>
    <x:transform doc='<%= pageContext.getAttribute("xmlDoc") %>'
                    xslt='<%= pageContext.getAttribute("externXsl") %>'
                    xsltSystemId='<%= (String) pageContext.getAttribute("exUrl") %>'/><br>
</tck:test>
