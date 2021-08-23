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
<tck:test testName="positiveXParamNameTest">
    <tck:localAbsUrl var="aUrl" path="/jstl_xml_xformparam_web/param.xsl"/>
    <c:import var="xsl" url="${aUrl}"/>
    <c:set var="message" value="message"/>
    <c:set var="fsize" value="fsize"/>

    <!-- Validate the name attribute of the x:param action.
             It must be able to accept both static and dynamic values. -->
    <x:transform xslt="${xsl}">
        <x:param name="message" value="Param properly used"/>
        <a>REPLACE</a>
        <x:param name='<%= (String) pageContext.getAttribute("fsize") %>' value="10pt"/>
    </x:transform>
</tck:test>
