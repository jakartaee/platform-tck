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
<tck:test testName="positiveUrlScopeTest">
    <!-- EL: Validate the export of var to various scopes (explicit and
             implicit) -->
    <c:url var="iPage" value="/jstltck-core/jstl"/>
    <c:url var="ePage" value="/jstltck-core/jstl" scope="page"/>
    <c:url var="eRequest" value="/jstltck-core/jstl" scope="request"/>
    <c:url var="eSession" value="/jstltck-core/jstl" scope="session"/>
    <c:url var="eApplication" value="/jstltck-core/jstl" scope="application"/>
    <tck:checkScope varName="iPage"/>
    <tck:checkScope varName="ePage"/>
    <tck:checkScope varName="eRequest" inScope="request"/>
    <tck:checkScope varName="eSession" inScope="session"/>
    <tck:checkScope varName="eApplication" inScope="application"/>
    <c:remove var="eApplication" scope="application"/> 
    
    <!-- RT: Validate the export of var to various scopes (explicit and
             implicit) -->
    <c_rt:url var="riPage" value="/jstltck-core/jstl"/>
    <c_rt:url var="rePage" value="/jstltck-core/jstl" scope="page"/>
    <c_rt:url var="reRequest" value="/jstltck-core/jstl" scope="request"/>
    <c_rt:url var="reSession" value="/jstltck-core/jstl" scope="session"/>
    <c_rt:url var="reApplication" value="/jstltck-core/jstl" scope="application"/>
    <tck:checkScope varName="riPage"/>
    <tck:checkScope varName="rePage"/>
    <tck:checkScope varName="reRequest" inScope="request"/>
    <tck:checkScope varName="reSession" inScope="session"/>
    <tck:checkScope varName="reApplication" inScope="application"/>
    <c:remove var="reApplication" scope="application"/>
</tck:test>
