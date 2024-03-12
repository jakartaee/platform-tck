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
<tck:test testName="positiveSetScopeTest">
    <!-- EL: Validate that the set action will properly export 
             var to the scope as specified by the scope attribute.
             Also verify that if scope is not specified, var is
             exported to the page scope by default. -->
    <c:set value="defaultPage" var="iPage"/>
    <c:set value="explicitPage" var="ePage" scope="page"/>
    <c:set value="explicitRequest" var="eRequest" scope="request"/>
    <c:set value="explicitSession" var="eSession" scope="session"/>
    <c:set value="explicitApplication" var="eApplication" scope="application"/>
    <tck:checkScope varName="iPage"/>
    <tck:checkScope varName="ePage"/>
    <tck:checkScope varName="eRequest" inScope="request"/>
    <tck:checkScope varName="eSession" inScope="session"/>
    <tck:checkScope varName="eApplication" inScope="application"/>
    <c:remove var="eApplication" scope="application"/>

    <!-- RT: Validate that the set action will properly export 
             var to the scope as specified by the scope attribute.
             Also verify that if scope is not specified, var is
             exported to the page scope by default. -->
    <c_rt:set value="defaultPage" var="iPage"/>
    <c_rt:set value="explicitPage" var="ePage" scope="page"/>
    <c_rt:set value="explicitRequest" var="eRequest" scope="request"/>
    <c_rt:set value="explicitSession" var="eSession" scope="session"/>
    <c_rt:set value="explicitApplication" var="eApplication" scope="application"/>
    <tck:checkScope varName="iPage"/>
    <tck:checkScope varName="ePage"/>
    <tck:checkScope varName="eRequest" inScope="request"/>
    <tck:checkScope varName="eSession" inScope="session"/>
    <tck:checkScope varName="eApplication" inScope="application"/>
    <c:remove var="eApplication" scope="application"/>
</tck:test>

