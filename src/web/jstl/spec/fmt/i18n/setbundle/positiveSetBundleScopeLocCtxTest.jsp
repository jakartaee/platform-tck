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

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveSetBundleScopeLocCtxTest">

    <!-- If var is not specified, but scope is, scope will dictate
             which scope the confuration variable 
             javax.servlet.jsp.jstl.fmt.LocalizationContext will be exported
             to. If scope is not specifed, then the configuration variable
             is exported to the page scope by default. -->
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.localizationContext"
                    useConfig="true"/>
    <tck:config op="remove" configVar="localectx"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   scope="page"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   scope="request"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   scope="session"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   scope="application"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.localizationContext" 
                    inScope="page" useConfig="true"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.localizationContext" 
                    inScope="request" useConfig="true"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.localizationContext" 
                    inScope="session" useConfig="true"/>
    <tck:checkScope varName="javax.servlet.jsp.jstl.fmt.localizationContext" 
                    inScope="application" useConfig="true"/>
    <tck:config op="remove" configVar="localectx" scope="application"/>
</tck:test>
