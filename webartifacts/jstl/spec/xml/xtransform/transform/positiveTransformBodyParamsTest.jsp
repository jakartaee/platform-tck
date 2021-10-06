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
<tck:test testName="positiveTransformBodyParamsTest">
    <c:import url="param.xsl" var="xsl"/>

    <!-- The transform action must accept x:param nested actions
             and provide these as parameters to the stylesheet. -->
    <x:transform doc="<a>REPLACE</a>" xslt='<%= (String) pageContext.getAttribute("xsl") %>'>
        <x:param name="message" value="RT Parameter properly passed!"/>
        <x:param name="fsize" value="15pt"/>
    </x:transform><br>
</tck:test>
