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
<tck:test testName="positiveMessageKeyTest">
    <c:set var="eKey" value="mkey"/>
    <!-- EL: Behavioral test for key attribute -->
    <fmt:bundle basename="com.sun.ts.tests.jstl.common.resources.Resources">
        <!-- EL: Behavioral test for key attribute -->
        <fmt:message key="${eKey}"/>
        <fmt:message key="mkey"/>

        <!-- RT: Behavioral test for key attribute -->
        <fmt_rt:message key='<%= (String) pageContext.getAttribute("eKey") %>'/>
        <fmt_rt:message key="mkey"/>
    </fmt:bundle>
</tck:test>
