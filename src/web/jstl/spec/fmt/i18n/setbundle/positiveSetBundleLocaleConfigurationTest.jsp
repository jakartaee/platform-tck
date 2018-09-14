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
<tck:test testName="positiveSetBundleLocaleConfigurationTest">
    <fmt:setLocale value="en_US"/>

    <!-- The setBundle action will use the locale as specified
             by the javax.servlet.jsp.jstl.fmt.locale configuration
             variable if present.  This will override any browser based
             locales provided. -->
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"
                   var="rBundle"/>
    <tck:config op="get" var="rConfig" configVar="localectx"/>
    Locale configuration variable type:<br>
    <c:set var="rb1" value="${rBundle.resourceBundle}"/>
    <tck:isInstance varName="rb1" type="com.sun.ts.tests.jstl.common.resources.Resources_en"/>
    <br>
    Exported LocalizationContext ResourceBundle type:<br>
    <c:set var="rb2" value="${rConfig.resourceBundle}"/>
    <tck:isInstance varName="rb2" type="com.sun.ts.tests.jstl.common.resources.Resources_en"/>
    
</tck:test>
