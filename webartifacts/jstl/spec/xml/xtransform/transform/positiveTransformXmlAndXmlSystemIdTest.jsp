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
<tck:test testName="positiveTransformXmlAndXmlSystemIdTest">
    <tck:localAbsUrl var="exUrl" path="/jstl_xml_xform_web/import.xml"/>
    <c:import url="import.xml" var="externXml"/>
    <c:import url="simple.xsl" var="xslDoc"/>

    <%--  Validate that the xmlSystemId and xml attributes are still valid --%>
    <%-- Result should have 'Entity Resolved' wrapped by &lt;h4&gt; elements:   --%>
    <x:transform xml='<%= pageContext.getAttribute("externXml") %>'
                    xslt='<%= pageContext.getAttribute("xslDoc") %>'
                    xmlSystemId='<%= (String) pageContext.getAttribute("exUrl") %>'/><br>

    <%-- Result should have 'Entity Resolved' wrapped by &lt;h4&gt; elements: --%>
    <x:transform xslt='<%= pageContext.getAttribute("xslDoc") %>'
                    xmlSystemId='<%= (String) pageContext.getAttribute("exUrl") %>'>
        <?xml version="1.0"?>

        <!DOCTYPE import [
            <!ENTITY external SYSTEM "xfiles/resolve.xml">
        ]>
        <root>
            &external;
        </root>
    </x:transform><br>
</tck:test>
