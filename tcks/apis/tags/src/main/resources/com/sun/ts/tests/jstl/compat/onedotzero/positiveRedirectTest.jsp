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
<%@ taglib prefix="c_rt" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page session="false" %>
<tck:test testName="positiveRedirectTest">
    <c:set var="eUrl" value="/jstl/core/urlresource/param/import.jsp"/>
    <c:set var="rUrl" value="import.jsp"/>
    <c:set var="ctx" value="/jstl_1_0_compat_web"/>
    <!-- EL: Validate that redirects properly occur when the url attribute
             is provided or if url and context are specified. -->
    <!-- Context-relative paths -->
    <c:if test="${param.el1 != null}">
        <c:redirect url="${eUrl}"/>
    </c:if>
    <c:if test="${param.el2 != null}">
        <c:redirect url="/jstl/core/urlresource/param/import.jsp"/>
    </c:if>
    <!-- Page-relative paths -->
    <c:if test="${param.el3 != null}">
        <c:redirect url="${rUrl}"/>
    </c:if>
    <c:if test="${param.el4 != null}">
        <c:redirect url="import.jsp"/>
    </c:if>
    <!-- Foreign context -->
    <c:if test="${param.el5 != null}">
        <c:redirect url="${eUrl}" context="${ctx}"/>
    </c:if>
    <c:if test="${param.el6 != null}">
        <c:redirect url="/jstl/core/urlresource/param/import.jsp" context="/jstl_1_0_compat_web"/>
    </c:if>

    <!-- RT: Validate that redirects properly occur when the url attribute
             is provided or if url and context are specified. -->
    <c:if test="${param.rt1 != null}">
        <c_rt:redirect url='<%= (String) pageContext.getAttribute("eUrl") %>'/>
    </c:if>
    <c:if test="${param.rt2 != null}">
        <c_rt:redirect url="/jstl/core/urlresource/param/import.jsp"/>
    </c:if>
    <!-- Page-relative paths -->
    <c:if test="${param.rt3 != null}">
        <c_rt:redirect url='<%= (String) pageContext.getAttribute("rUrl") %>'/>
    </c:if>
    <c:if test="${param.rt4 != null}">
        <c_rt:redirect url="import.jsp"/>
    </c:if>
    <!-- Foreign context -->
    <c:if test="${param.rt5 != null}">
        <c_rt:redirect url='<%= (String) pageContext.getAttribute("eUrl") %>' 
                    context='<%= (String) pageContext.getAttribute("ctx") %>'/>
    </c:if>
    <c:if test="${param.rt6 != null}">
        <c_rt:redirect url="/jstl/core/urlresource/param/import.jsp" context="/jstl_1_0_compat_web"/>
    </c:if>
</tck:test>
