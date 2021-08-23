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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml" %>
<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveTransformVarTest">
    <c:import url="simple2.xml" var="xmlDoc"/>
    <c:import url="simple2.xsl" var="xslDoc"/>
    <!-- EL: If var is specified, the result of the transformation
             will be export to the PageContext as an instance
             of org.w3c.dom.Document. -->
    <x:transform xml="${xmlDoc}" xslt="${xslDoc}" var="trans1"/>
    <tck:isInstance varName="trans1" type="org.w3c.dom.Document"/>
    
    <!-- RT: If var is specified, the result of the transformation
             will be export to the PageContext as an instance
             of org.w3c.dom.Document. -->
    <x_rt:transform xml='<%= pageContext.getAttribute("xmlDoc") %>' 
                    xslt='<%= pageContext.getAttribute("xslDoc") %>' var="trans2"/>
    <tck:isInstance varName="trans2" type="org.w3c.dom.Document"/>
</tck:test>
