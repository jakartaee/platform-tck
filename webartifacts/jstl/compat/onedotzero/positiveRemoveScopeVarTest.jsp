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
<tck:test testName="positiveRemoveScopeTest">
    <!-- EL: Validate that remove will removed the scoped variable
             as specified by scope and var. If scope is not specified,
             var will be removed by pageContext.removeAttribute(attr). -->
    <c:set var="rea" value="value1" scope="session"/>
    <c:set var="ePage" value="value2" scope="page"/>
    <c:set var="eRequest" value="value3" scope="request"/>
    <c:set var="eSession" value="value4" scope="session"/>
    <c:set var="eApplication" value="value5" scope="application"/>
    <c:remove var="rea"/>
    <c:remove var="ePage" scope="page"/>
    <c:remove var="eRequest" scope="request"/>
    <c:remove var="eSession" scope="session"/>
    <c:remove var="eApplication" scope="application"/>
    <tck:checkScope varName="rea" inScope="session"/>
    <tck:checkScope varName="ePage"/>
    <tck:checkScope varName="eRequest" inScope="request"/>
    <tck:checkScope varName="eSession" inScope="session"/>
    <tck:checkScope varName="eApplication" inScope="application"/>
    
    
    <!-- EL: Validate that remove will removed the scoped variable
             as specified by scope and var. If scope is not specified,
             var will be removed by pageContext.removeAttribute(attr). -->
    <c:set var="rrea" value="value1" scope="application"/>
    <c:set var="rePage" value="value2" scope="page"/>
    <c:set var="reRequest" value="value3" scope="request"/>
    <c:set var="reSession" value="value4" scope="session"/>
    <c:set var="reApplication" value="value5" scope="application"/>
    <c_rt:remove var="reea"/>
    <c_rt:remove var="rePage" scope="page"/>
    <c_rt:remove var="reRequest" scope="request"/>
    <c_rt:remove var="reSession" scope="session"/>
    <c_rt:remove var="reApplication" scope="application"/>
    <tck:checkScope varName="reaa" inScope="application"/>
    <tck:checkScope varName="rePage"/>
    <tck:checkScope varName="reRequest" inScope="request"/>
    <tck:checkScope varName="reSession" inScope="session"/>
    <tck:checkScope varName="reApplication" inScope="application"/>
</tck:test>
