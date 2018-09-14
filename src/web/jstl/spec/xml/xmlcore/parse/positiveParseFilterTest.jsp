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
<%@ page import="com.sun.ts.tests.jstl.common.filters.SimpleXmlFilter,org.xml.sax.XMLFilter,java.io.Reader,java.io.StringReader" %>
<tck:test testName="positiveParseFilterTest">
    <c:set var="xmlDoc" value="<test attr='value'>xmltext</test>"/>
    <%
        // Set up a reader
        Reader r = new StringReader("<test attr='value'>xmltext</test>");
        pageContext.setAttribute("eReader" , r);
        Reader rt = new StringReader("<test attr='value'>xmltext</test>");
        pageContext.setAttribute("rReader" , rt);

        // Setup the filter.
        SimpleXmlFilter filter = new SimpleXmlFilter();
        pageContext.setAttribute("fil", filter);
    %>

    <!-- The filter attribute specifies an instance of
             org.xml.sax.XMLFilter to be applied during the 
             parse operation. Validate that the filter is 
             applied to all input types. -->
    In all cases, the result of the filtered select will be 'xmlText'<br>
    <br>
    Filter an XML document provided as a String:<br>
    <x:parse doc="<test attr='value'>xmltext</test>"
                filter='<%= (XMLFilter) pageContext.getAttribute("fil") %>' var="rdoc1"/>
        Filtered Select: <x:out select="$rdoc1/test[@test]"/><br>

    Filter an XML document provided as a Reader:<br>
    <x:parse doc='<%= (Reader) pageContext.getAttribute("rReader") %>'
                filter='<%= (XMLFilter) pageContext.getAttribute("fil") %>' var="rdoc2"/>
        Filtered Select: <x:out select="$rdoc2/test[@test]"/><br>

    Filter an XML document provided as a body content:<br>
    <x:parse filter='<%= (XMLFilter) pageContext.getAttribute("fil") %>' var="rdoc3">
        <c:out value="${xmlDoc}" escapeXml="false"/>
    </x:parse>
        Filtered Select: <x:out select="$rdoc3/test[@test]"/><br>
</tck:test>
