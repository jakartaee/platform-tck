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
<tck:test testName="positiveParseSystemIdTest">
    <tck:localAbsUrl var="id" path="/jstl_xml_parse_web/import.xml"/>
    <c:import url="import.xml" var="importedXml"/>

    <!-- Providing a SystemId will allow the resolution of
             external entities.  In this case, the imported XML
             document will have an external entity refereced
             at 'xmlfiles/resolve.xml'. -->
    Result of select against parsed document should be: 'Entity Resolved':<br>
    <x:parse doc='<%= (String) pageContext.getAttribute("importedXml") %>' var="rdoc"
                systemId='<%= (String) pageContext.getAttribute("id") %>'/>
    <x:out select="$rdoc//resolved"/><br>

    XML Document provided as text to body of parse:<br>
    Result of select against parsed document should be: 'Entity Resolved':<br>
    <x:parse var="rdoc1" systemId='<%= (String) pageContext.getAttribute("id") %>'>
        <?xml version="1.0"?>
        <!DOCTYPE root [
            <!ENTITY external SYSTEM "xfiles/resolve.xml">
        ]>
        <root>
            &external;
        </root>
    </x:parse>
    <x:out select="$rdoc1//resolved"/><br>
</tck:test>
