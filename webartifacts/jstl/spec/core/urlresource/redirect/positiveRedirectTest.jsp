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

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page session="false" %>
<tck:test testName="positiveRedirectTest">
    <c:set var="eUrl" value="/urlresource/param/import.jsp"/>
    <c:set var="rUrl" value="import.jsp"/>
    <c:set var="ctx" value="/jstl_core_web"/>

    <!-- Validate that redirects properly occur when the url attribute
             is provided or if url and context are specified. -->
    <c:if test="${param.rt1 != null}">
        <c:redirect url='<%= (String) pageContext.getAttribute("eUrl") %>'/>
    </c:if>
    <c:if test="${param.rt2 != null}">
        <c:redirect url="/urlresource/param/import.jsp"/>
    </c:if>
    <!-- Page-relative paths -->
    <c:if test="${param.rt3 != null}">
        <c:redirect url='<%= (String) pageContext.getAttribute("rUrl") %>'/>
    </c:if>
    <c:if test="${param.rt4 != null}">
        <c:redirect url="import.jsp"/>
    </c:if>
    <!-- Foreign context -->
    <c:if test="${param.rt5 != null}">
        <c:redirect url='<%= (String) pageContext.getAttribute("eUrl") %>'
                    context='<%= (String) pageContext.getAttribute("ctx") %>'/>
    </c:if>
    <c:if test="${param.rt6 != null}">
        <c:redirect url="/urlresource/param/import.jsp" context="/jstl_core_web"/>
    </c:if>
</tck:test>
