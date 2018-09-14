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
<tck:test testName="positiveSetBundleScopeVarTest">

    <!-- If scope is specified, var will be exported to that scope.
             If not specified, var will be exported to the page scope
             by default. -->
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="riPage"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="rePage" scope="page"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="reRequest" scope="request"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="reSession" scope="session"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="reApplication" scope="application"/>
    <tck:checkScope varName="riPage"/>
    <tck:checkScope varName="rePage"/>
    <tck:checkScope varName="reRequest" inScope="request"/>
    <tck:checkScope varName="reSession" inScope="session"/>
    <tck:checkScope varName="reApplication" inScope="application"/>
    <c:remove var="reApplication" scope="application"/>
</tck:test>
