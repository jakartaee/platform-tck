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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positivePNValueTest">
    <c:set var="num" value="1,234"/>
    <fmt:setLocale value="en_US"/>
    <!-- EL: Validate the actions behavior with the value attribute.  The
             action/attribute should be able to support both EL and static
             values. -->
    <fmt:parseNumber value="${num}"/>
    <fmt:parseNumber value="1,234"/>

     <!-- RT: Validate the actions behavior with the value attribute.  The
             action/attribute should be able to support both EL and static
             values. -->
    <fmt_rt:parseNumber value='<%= (String) pageContext.getAttribute("num") %>'/>
    <fmt_rt:parseNumber value="1,234"/>
</tck:test>
