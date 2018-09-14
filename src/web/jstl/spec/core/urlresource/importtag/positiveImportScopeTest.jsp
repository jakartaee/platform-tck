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
<tck:test testName="positiveImportScopeTest">

    <!-- Test scopes -->
    <!-- Page should be the default... -->
    <c:import url="import.txt" var="rpageDef"/>
    <!-- Explicit scope definitions... -->
    <c:import url="import.txt" var="rexpPage" scope="page"/>
    <c:import url="import.txt" var="rexpRequest" scope="request"/>
    <c:import url="import.txt" var="rexpSession" scope="session"/>
    <c:import url="import.txt" var="rexpApplication" scope="application"/>
    <tck:checkScope varName="rpageDef"/>
    <tck:checkScope varName="rexpPage"/>
    <tck:checkScope varName="rexpRequest" inScope="request"/>
    <tck:checkScope varName="rexpSession" inScope="session"/>
    <tck:checkScope varName="rexpApplication" inScope="application"/>
    <c:remove var="rexpApplication" scope="application"/>
</tck:test>

