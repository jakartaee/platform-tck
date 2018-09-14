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
<tck:test testName="positiveTransformBodyXmlParamsTest">
    <c:import var="xsl" url="param.xsl"/>

    <!-- Validate the transform action is able to parse
             an XML document provided as body content to the
             action, including the existence of nested x:param
             subtags. -->
    <x:transform xslt='<%= (String) pageContext.getAttribute("xsl") %>'>
        <elemental>THIS SHOULD BE REPLACED</elemental>
        <x:param name="fsize" value="11pt"/>
        <x:param name="message" value="Paramter properly passed!"/>
    </x:transform><br>
</tck:test>
