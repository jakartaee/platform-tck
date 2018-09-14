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
<tck:test testName="positiveMessageLocaleConfigurationVariableTest">
    <fmt:setLocale value="en_US"/>
    <fmt:setBundle basename="com.sun.ts.tests.jstl.common.resources.Resources"/>

    <!-- If the javax.servlet.jsp.jstl.fmt.locale configuration variable
             then the setBundle action should select the appropriate bundle
             and export the javax.servlet.jsp.jstl.fmt.localizationContext
             config variable.  -->
    <fmt:message key="mkey"/>
</tck:test>
