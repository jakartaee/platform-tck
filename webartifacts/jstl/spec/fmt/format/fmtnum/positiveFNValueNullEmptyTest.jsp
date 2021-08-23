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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveFNValueNullEmptyTest">

    <!-- If value is null or empty, no action is performed -->
    <fmt:formatNumber value='<%= null %>'/>
    <fmt:formatNumber value=""/>

    <!-- if value is null or empty, the scoped variable specified by
         'var' will be removed -->
    <c:set var="pageVar" value="value"/>
    <fmt:formatNumber value='<%= null %>' var="pageVar" />
    <c:out value="${pageVar}" default="Scoped variable removed"/>
</tck:test>
